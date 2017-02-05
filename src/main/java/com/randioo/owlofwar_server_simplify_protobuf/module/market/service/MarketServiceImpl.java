package com.randioo.owlofwar_server_simplify_protobuf.module.market.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardConfig;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Market;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.MarketItem;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.module.market.MarketConstant.MarketBuyType;
import com.randioo.owlofwar_server_simplify_protobuf.module.role.service.RoleService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.MarketItemData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.MarketItemDataBuyType;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketArtificialRefreshResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketBuyMarketItemResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketShowResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.owlofwar_server_simplify_protobuf.utils.JSInvoker;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.utils.RandomUtils;
import com.randioo.randioo_server_base.utils.TimeUtils;

public class MarketServiceImpl extends BaseService implements MarketService {

	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	public void marketInit(Role role, int nowTime) {
		this.refreshMarketItem(role, nowTime);
	}
	
	private JSInvoker jsInvoker;
	
	public void setJsInvoker(JSInvoker jsInvoker) {
		this.jsInvoker = jsInvoker;
	}

	@Override
	public void refreshMarketItem(Role role, int nowTime) {
		Market market = role.getMarket();
		// 获得上次刷新时间
		int refreshTime = market.getRefreshTime();
		// 获得零点时间
		int todayZeroTime = getTodayZeroTime(nowTime);

		// 检查是否要刷新时间
		// 如果今天的零点时间小于上次的刷新时间，则不用刷新
		if (todayZeroTime < refreshTime) {
			return;
		}

		// 重置刷新次数
		market.setRefreshCount(0);
		// 刷新时间戳
		market.setRefreshTime(nowTime);

		// 刷新卡牌
		this.basicRefresh(role);
	}

	@Override
	public GeneratedMessage showMarketItem(Role role) {
		int nowTime = TimeUtils.getNowTime();
		// 刷新商城
		this.refreshMarketItem(role, nowTime);
		Market market = role.getMarket();
		MarketShowResponse.Builder response = MarketShowResponse.newBuilder();
	
		// 是否显示动画
		boolean isShowAnimation = market.isShowAnimation();
		response.setIsRefresh(isShowAnimation);
		// 设置剩余免费刷新的次数
		int remainRefreshCount = this.getFreeRefreshCount() - market.getRefreshCount();
		remainRefreshCount = remainRefreshCount < 0 ? 0 : remainRefreshCount;
		response.setRefreshCount(remainRefreshCount);
		// 刷新需要的银币数
		int needMoney = this.getRefreshNeedMoney(market.getRefreshCount());
		response.setNeedMoney(needMoney);
		// 显示过以后就不显示动画了，知道下一次更新
		market.setShowAnimation(false);
		int MARKET_ITEM_COUNT = this.getMarketItemCount();
		// 卡片信息序列化
		for (int i = 0; i < MARKET_ITEM_COUNT; i++) {
			MarketItem item = role.getMarket().getMarketItemMap().get(i);
	
			int nextBuyMoney = this.getCost(item.getId(), item.getDayBuyCount()+1,item.getBuyType());
			MarketItemData marketItemData = MarketItemData.newBuilder()
					.setBuyType(this.getBuyType(item.getBuyType())).setCardId(item.getId())
					.setNextBuyMoney(nextBuyMoney).build();
	
			response.addMarketItemData(marketItemData);
		}
		return SCMessage.newBuilder().setMarketShowResponse(response).build();
	}

	@Override
	public GeneratedMessage artificalRefreshMarketItem(Role role) {
	
		Market market = role.getMarket();
		// 先检查是否到了每日刷新时间，如果是，则直接刷新不算次数
		this.refreshMarketItem(role, TimeUtils.getNowTime());
		// 如果没有要求显示动画,则说明没有到重置时间,说明是人工刷新
		if (!market.isShowAnimation()) {
			int needMoney = this.getRefreshNeedMoney(market.getRefreshCount());
			// 检查银币是否足够
			if (role.getMoney() < needMoney) {
				// 不足则返回错误码
				return SCMessage.newBuilder().setMarketArtificialRefreshResponse(
						MarketArtificialRefreshResponse.newBuilder().setErrorCode(ErrorCode.NO_MONEY)).build();
			}
	
			if (needMoney != 0)
				roleService.addMoney(role, -needMoney);
	
			// 今日刷新次数加1
			market.setRefreshCount(market.getRefreshCount() + 1);
			// 刷新卡牌
			this.basicRefresh(role);
		}
	
		//人工刷新也算显示，有客户端必须每次播放动画，此处直接设为false
		market.setShowAnimation(false);
		
		// 获得剩余免费刷新次数
		int remainRefreshCount = this.getFreeRefreshCount() - market.getRefreshCount();
		remainRefreshCount = remainRefreshCount < 0 ? 0 : remainRefreshCount;
	
		// 获得下次刷新需要多少花费
		int nextNeedMoney = this.getRefreshNeedMoney(market.getRefreshCount());
	
		MarketArtificialRefreshResponse.Builder response = MarketArtificialRefreshResponse.newBuilder()
				.setErrorCode(ErrorCode.SUCCESS).setRefreshCount(remainRefreshCount).setNeedMoney(nextNeedMoney);
		// 商品数量
		int MARKET_ITEM_COUNT = this.getMarketItemCount();
		for (int i = 0; i < MARKET_ITEM_COUNT; i++) {
			MarketItem item = role.getMarket().getMarketItemMap().get(i);
	
			int nextBuyMoney = this.getCost(item.getId(), item.getDayBuyCount()+1,item.getBuyType());
			MarketItemData marketItemData = MarketItemData.newBuilder()
					.setBuyType(this.getBuyType(item.getBuyType())).setCardId(item.getId())
					.setNextBuyMoney(nextBuyMoney).build();
	
			
			response.addMarketItemData(marketItemData);
		}
		return SCMessage.newBuilder().setMarketArtificialRefreshResponse(response).build();
	}

	@Override
	public GeneratedMessage buyMarketItem(Role role, IoSession session, int index) {
		Market market = role.getMarket();
		Map<Integer, MarketItem> marketItemMap = market.getMarketItemMap();
		MarketItem marketItem = marketItemMap.get(index);
		// 获得卡片id
		int cardId = marketItem.getId();
		//购买数量，现在只能一次买一张
		int buyCount = 1;
		if (marketItem.getBuyType() == MarketBuyType.RMB) {
			// TODO 如果没有卡片则需要进行人民币购买流程
			// 必须是客户端已经支付完成才会调用该方法,所以我直接默认它已经支付完毕,给一张卡片
			Card card = cardService.createCard(role, cardId);
			card.setNum(1);
			role.getCardMap().put(card.getCardId(), card);
			
			//购买完后用银币买
			marketItem.setBuyType(MarketBuyType.MONEY);
			//购买次数置0
			marketItem.setDayBuyCount(0);

			int nextBuyMoney = this.getCost(cardId, marketItem.getDayBuyCount() + 1, marketItem.getBuyType());
			return SCMessage.newBuilder()
					.setMarketBuyMarketItemResponse(MarketBuyMarketItemResponse.newBuilder()
							.setErrorCode(ErrorCode.SUCCESS).setNextBuyMoney(nextBuyMoney).setBuyCount(buyCount)
							.setBuyType(this.getBuyType(marketItem.getBuyType())))
					.build();

		}
		int dayBuyCount = marketItem.getDayBuyCount();
		int costMoney = getCost(cardId, dayBuyCount + 1, marketItem.getBuyType());

		// 如果钱不够则返回错误信息
		if (role.getMoney() < costMoney) {
			return SCMessage.newBuilder().setMarketBuyMarketItemResponse(
					MarketBuyMarketItemResponse.newBuilder().setErrorCode(ErrorCode.NO_MONEY)).build();
		}

		Card card = role.getCardMap().get(cardId);
		// 该商品购买次数加1
		dayBuyCount += 1;
		marketItem.setDayBuyCount(dayBuyCount);
		// 卡片数量增加
		card.setNum(card.getNum() + buyCount);

		// 扣除银币
		roleService.addMoney(role, -costMoney);

		int nextBuyNeedMoney = this.getCost(cardId, dayBuyCount + 1, marketItem.getBuyType());

		return SCMessage.newBuilder()
				.setMarketBuyMarketItemResponse(MarketBuyMarketItemResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)
						.setNextBuyMoney(nextBuyNeedMoney).setBuyCount(buyCount)
						.setBuyType(this.getBuyType(marketItem.getBuyType())))
				.build();
	}

	/**
	 * 卡牌刷新
	 * 
	 * @param role
	 */
	
	private void basicRefresh(Role role){
		basicRefresh2(role);
	}
	private void basicRefresh1(Role role) {
		Market market = role.getMarket();

		// 设置需要显示动画
		market.setShowAnimation(true);
		// 克隆所有卡片id
		List<Integer> cardIdList = new ArrayList<>(CardConfigCache.getResMap().keySet());

		// 获得刷新卡牌的数量
		int MARKET_ITEM_COUNT = this.getMarketItemCount();
		for (int i = 0; i < MARKET_ITEM_COUNT; i++) {
			// 随机一个索引
			int randIndex = RandomUtils.getRandomNum(cardIdList.size());
			int cardId = cardIdList.get(randIndex);
			// 取出这个索引便得到一个值
			cardIdList.remove(randIndex);
			// 重置对应卡片的信息
			MarketItem marketItem = market.getMarketItemMap().get(i);
			// 如果没有对象则新建
			if (marketItem == null) {
				marketItem = new MarketItem();
				market.getMarketItemMap().put(i, marketItem);
			}
			// 设置商品id
			marketItem.setId(cardId);
			//重置这张牌的购买次数
			marketItem.setDayBuyCount(0);
			// 设置货币类型
			// 如果是第一个就得花钱
			MarketBuyType buyType = i == 0 ? MarketBuyType.RMB : MarketBuyType.MONEY;
			marketItem.setBuyType(buyType);
		}
	}
	
	private void basicRefresh2(Role role){
		Market market = role.getMarket();

		// 设置需要显示动画
		market.setShowAnimation(true);
		// 克隆所有卡片id
		List<Integer> cardIdList = new ArrayList<>(CardConfigCache.getResMap().keySet());

		// 获得刷新卡牌的数量
		int MARKET_ITEM_COUNT = this.getMarketItemCount();
		for (int i = 0; i < MARKET_ITEM_COUNT; i++) {
			// 随机一个索引
			int randIndex = RandomUtils.getRandomNum(cardIdList.size());
			int cardId = cardIdList.get(randIndex);
			// 取出这个索引便得到一个值
			cardIdList.remove(randIndex);
			// 重置对应卡片的信息
			MarketItem marketItem = market.getMarketItemMap().get(i);
			// 如果没有对象则新建
			if (marketItem == null) {
				marketItem = new MarketItem();
				market.getMarketItemMap().put(i, marketItem);
			}
			// 设置商品id
			marketItem.setId(cardId);
			//重置这张牌的购买次数
			marketItem.setDayBuyCount(0);
			// 设置货币类型
			// 如果没有这张卡牌就得用rmb				
			MarketBuyType buyType = role.getCardMap().containsKey(cardId) ? MarketBuyType.MONEY : MarketBuyType.RMB;
			marketItem.setBuyType(buyType);
		}
	}

	private MarketItemDataBuyType getBuyType(MarketBuyType buyType){
		MarketItemDataBuyType type = null;
		if(buyType == MarketBuyType.GOLD){
			type = MarketItemDataBuyType.GOLD;
		}else if(buyType ==MarketBuyType.MONEY){
			type = MarketItemDataBuyType.MONEY;
		}else if(buyType == MarketBuyType.RMB){
			type = MarketItemDataBuyType.RMB;
		}
		
		return type;
	}

	/**
	 * 获得今日零点时间
	 * 
	 * @param nowTime
	 * @return
	 * @author wcy 2017年1月9日
	 */
	private int getTodayZeroTime(int nowTime) {
		int zeroTime = TimeUtils.getTodayTimePointByRegex(nowTime, "00:00:00", "HH:mm:ss");

		return zeroTime;
	}

	/**
	 * 获得花费
	 * 
	 * @param cardId
	 * @param dayBuyCount
	 * @return
	 * @author wcy 2017年1月9日
	 */
	private int getCost(int cardId, int dayBuyCount,MarketBuyType marketBuyType) {
		CardConfig config = CardConfigCache.getConfigById(cardId);
		int type = 1;
		if (marketBuyType == MarketBuyType.MONEY) {
			type = 1;
		} else if (marketBuyType == MarketBuyType.RMB) {
			type = 2;
		}
		return (int) (double) jsInvoker.invoke("getCostMoney", config.getQuality(), dayBuyCount,type);
	}

	/**
	 * 获得刷新需要的钱
	 * 
	 * @param refreshCount
	 * @return
	 */
	private int getRefreshNeedMoney(int refreshCount) {
		return (int) ((double) jsInvoker.invoke("refreshMarketNeedMoney", refreshCount));
	}

	private int getFreeRefreshCount() {
		return (int) (double) jsInvoker.invoke("getMarketFreeRefreshMaxCount");
	}

	private int getMarketItemCount() {
		return (int) (double) jsInvoker.invoke("getMarketItemCount");
	}

}
