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
import com.randioo.owlofwar_server_simplify_protobuf.module.market.MarketConstant;
import com.randioo.owlofwar_server_simplify_protobuf.module.market.MarketConstant.MarketBuyType;
import com.randioo.owlofwar_server_simplify_protobuf.module.role.service.RoleService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.MarketItemData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketArtificialRefreshResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketBuyMarketItemResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketShowMarketItemResponse;
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

	@Override
	public void refreshMarketItem(Role role, int nowTime) {
		// TODO Auto-generated method stub
		Market market = role.getMarket();
		//获得上次刷新时间
		int refreshTime = market.getRefreshTime();
		//获得零点时间
		int todayZeroTime = getTodayZeroTime(nowTime);

		// 检查是否要刷新时间
		//如果今天的零点时间小于上次的刷新时间，则不用刷新
		if (todayZeroTime < refreshTime) {
			return;
		}
		
		// 重置刷新次数
		market.setRefreshCount(0);
		// 刷新时间戳
		market.setRefreshTime(nowTime);
//		// 设置需要显示动画
//		market.setShowAnimation(true);
//		// 克隆所有卡片id
//		List<Integer> cardIdList = new ArrayList<>(CardConfigCache.getMap().keySet());
//		for (int i = 0; i < MarketConstant.MARKET_ITEM_COUNT; i++) {
//			// 随机一个索引
//			int randIndex = RandomUtils.getRandomNum(cardIdList.size());
//			int cardId = cardIdList.get(randIndex);
//			// 取出这个索引便得到一个值
//			cardIdList.remove(randIndex);
//			// 重置对应卡片的信息
//			MarketItem marketItem = market.getMarketItemMap().get(i);
//			// 如果没有对象则新建
//			if (marketItem == null) {
//				marketItem = new MarketItem();
//				market.getMarketItemMap().put(i, marketItem);
//			}
//			// 设置商品id
//			marketItem.setId(cardId);
//			// 设置今日该商品购买的次数
//			marketItem.setDayBuyCount(0);
//			// 设置货币类型
//			// 如果是第一个就得花钱
//			MarketBuyType buyType = i == 0 ? MarketBuyType.RMB : MarketBuyType.MONEY;
//			marketItem.setBuyType(buyType);
//		}
		
		//刷新卡牌
		this.basicRefresh(role);
	}
	
	private void basicRefresh(Role role) {
		Market market = role.getMarket();

		// 设置需要显示动画
		market.setShowAnimation(true);
		// 克隆所有卡片id
		List<Integer> cardIdList = new ArrayList<>(CardConfigCache.getMap().keySet());
		for (int i = 0; i < MarketConstant.MARKET_ITEM_COUNT; i++) {
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
			// 设置今日该商品购买的次数
			marketItem.setDayBuyCount(0);
			// 设置货币类型
			// 如果是第一个就得花钱
			MarketBuyType buyType = i == 0 ? MarketBuyType.RMB : MarketBuyType.MONEY;
			marketItem.setBuyType(buyType);
		}
	}
	
	@Override
	public GeneratedMessage artificalRefreshMarketItem(Role role) {
		Market market = role.getMarket();
		int needGood = 9;
		if (market.getRefreshCount() >= MarketConstant.MAX_FREE_REFRESH_COUNT) {
			// 检查金币是否足够
			if (role.getGold() < needGood) {
				// 不足则返回错误码
				return SCMessage
						.newBuilder()
						.setMarketArtificialRefreshResponse(
								MarketArtificialRefreshResponse.newBuilder().setErrorCode(ErrorCode.NO_GOLD)).build();
			}

			roleService.addGold(role, -needGood);

		}
		// 今日刷新次数加1
		market.setRefreshCount(market.getRefreshCount() + 1);
		// 刷新卡牌
		this.basicRefresh(role);

		MarketArtificialRefreshResponse.Builder response = MarketArtificialRefreshResponse.newBuilder()
				.setErrorCode(ErrorCode.SUCCESS).setRefreshCount(market.getRefreshCount());
		
		for (int i = 0; i < MarketConstant.MARKET_ITEM_COUNT; i++) {
			MarketItem item = role.getMarket().getMarketItemMap().get(i);

			MarketItemData marketItemData = MarketItemData.newBuilder()
					.setBuyType(MarketConstant.buyTypeMap.get(item.getBuyType())).setCardId(item.getId())
					.setDayBuyCount(item.getDayBuyCount()).build();

			response.addMarketItemData(marketItemData);
		}
		return SCMessage.newBuilder().setMarketArtificialRefreshResponse(response).build();
	}

	/**
	 * 获得今日零点时间
	 * @param nowTime
	 * @return
	 * @author wcy 2017年1月9日
	 */
	private int getTodayZeroTime(int nowTime){
		int zeroTime = TimeUtils.getTodayTimePointByRegex(nowTime, "00:00:00", "HH:mm:ss");
		
		return zeroTime;
	}

	@Override
	public GeneratedMessage showMarketItem(Role role) {
		int nowTime = TimeUtils.getNowTime();
		this.refreshMarketItem(role, nowTime);
		Market market = role.getMarket();
		MarketShowMarketItemResponse.Builder response = MarketShowMarketItemResponse.newBuilder();
		
		//是否显示动画
		boolean isShowAnimation = market.isShowAnimation();
		response.setIsRefresh(isShowAnimation);
		//设置今天刷新的次数
		response.setRefreshCount(market.getRefreshCount());
		//显示过以后就不显示动画了，知道下一次更新
		market.setShowAnimation(false);
		//卡片信息序列化
		for(int i = 0;i<MarketConstant.MARKET_ITEM_COUNT;i++){
			MarketItem item = role.getMarket().getMarketItemMap().get(i);

			MarketItemData marketItemData = MarketItemData.newBuilder()
					.setBuyType(MarketConstant.buyTypeMap.get(item.getBuyType())).setCardId(item.getId())
					.setDayBuyCount(item.getDayBuyCount()).build();
			
			response.addMarketItemData(marketItemData);
		}
		return SCMessage.newBuilder().setMarketShowMarketItemResponse(response).build();
	}

	@Override
	public GeneratedMessage buyMarketItem(Role role, IoSession session, int index) {
		Market market = role.getMarket();
		Map<Integer, MarketItem> marketItemMap = market.getMarketItemMap();
		MarketItem marketItem = marketItemMap.get(index);
		// 获得卡片id
		int cardId = marketItem.getId();
		Card card = role.getCardMap().get(cardId);
		if (card == null || (card.getNum() == 0 && card.getLv() == 0)) {
			// TODO 如果没有卡片则需要进行购买流程
			//必须是客户端已经支付完成才会调用该方法,所以我直接默认它已经支付完毕,给一张卡片
			card = cardService.createCard(role, cardId,(byte)1);
			return SCMessage
					.newBuilder()
					.setMarketBuyMarketItemResponse(
							MarketBuyMarketItemResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)
									.setDayBuyCount(0).setBuyCount(0)).build();
		}
		int dayBuyCount = marketItem.getDayBuyCount();
		int buyCardCount = 1;
		int costMoney = getCost(cardId, dayBuyCount);
		// 如果钱不够则返回错误信息
		if (role.getMoney() < costMoney) {
			return SCMessage
					.newBuilder()
					.setMarketBuyMarketItemResponse(
							MarketBuyMarketItemResponse.newBuilder().setErrorCode(ErrorCode.NO_MONEY)
									.setDayBuyCount(dayBuyCount)).build();
		}
		
		//该商品购买次数加1
		dayBuyCount++;
		marketItem.setDayBuyCount(dayBuyCount);
		//卡片数量增加
		card.setNum(card.getNum() + buyCardCount);
		
		//扣除银币
		roleService.addMoney(role, -costMoney);

		return SCMessage
				.newBuilder()
				.setMarketBuyMarketItemResponse(
						MarketBuyMarketItemResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)
								.setDayBuyCount(dayBuyCount).setBuyCount(buyCardCount)).build();
	}
	
	/**
	 * 获得花费
	 * @param cardId
	 * @param dayBuyCount
	 * @return
	 * @author wcy 2017年1月9日
	 */
	private int getCost(int cardId, int dayBuyCount) {
		CardConfig config = CardConfigCache.getConfigById(cardId);
		return (int) JSInvoker.getInstance().invoke("getCostMoney", config.getQuality(), dayBuyCount);
	}

//	private RoleService roleService;
//
//	public void setRoleService(RoleService roleService) {
//		this.roleService = roleService;
//	}
//
//	private PropService propService;
//
//	public void setPropService(PropService propService) {
//		this.propService = propService;
//	}
//
//	private IncomeService incomeService;
//
//	public void setIncomeService(IncomeService incomeService) {
//		this.incomeService = incomeService;
//	}
//
//	@Override
//	public void marketInit(Role role, int nowTime) {
//		this.refreshMarketItem(role, nowTime);
//	}
//
//	@Override
//	public void refreshMarketItem(Role role, int nowTime) {
//		Market market = role.getMarket();
//		int refreshTime = market.getRefreshTime();
//		market.setRefreshTime(nowTime);
//
//		Map<Integer, MarketConfig> notRefreshConfigMap = MarketConfigCache.getNotRefreshMap();
//		Map<Integer, MarketConfig> refreshConfigMap = MarketConfigCache.getRefreshMap();
//		Map<Integer, MarketItem> notRefreshMarketItemMap = market.getNotRefreshMarketItemMap();
//		Map<Integer, MarketItem> refreshMarketItemMap = market.getRefreshMarketItemMap();
//
//		for (MarketConfig marketConfig : notRefreshConfigMap.values()) {
//			int startTime = marketConfig.getStartTime();
//			// 刷新时间小于物品出现时间，则说明是新加的上的商品，需要添加
//			if (refreshTime < startTime) {
//				int id = marketConfig.getId();
//				int dayBuyCount = marketConfig.getDayBuyCount();
//
//				MarketItem item = new MarketItem();
//				item.setDayBuyCount(dayBuyCount);
//				item.setId(id);
//
//				notRefreshMarketItemMap.put(id, item);
//			}
//		}
//
//		for (MarketConfig marketConfig : refreshConfigMap.values()) {
//			int id = marketConfig.getId();
//			int dayBuyCount = marketConfig.getDayBuyCount();
//
//			MarketItem item = new MarketItem();
//			item.setId(id);
//			item.setDayBuyCount(dayBuyCount);
//
//			refreshMarketItemMap.put(id, item);
//
//		}
//
//		market.setChange(true);
//	}
//
//	/**
//	 * 重置刷新次数
//	 * 
//	 * @param role
//	 * @param nowTime
//	 */
//	private void resetMarketItem(Role role, int nowTime) {
//		Market market = role.getMarket();
//		int time = this.getMarketTodayRefreshTime(nowTime);
//		int refreshTime = market.getRefreshTime();
//
//		// 刷新时间如果小于需要刷新的时间点而现在的时间大于需要刷新的时间点时，说明还需要重置次数
//		if (refreshTime < time && nowTime >= time) {
//			Map<Integer, MarketConfig> marketConfigMap = MarketConfigCache.getRefreshMap();
//			Map<Integer, MarketItem> refreshMarketItemMap = market.getRefreshMarketItemMap();
//			for (MarketItem item : refreshMarketItemMap.values()) {
//				int id = item.getId();
//				MarketConfig marketConfig = marketConfigMap.get(id);
//				int dayBuyCount = marketConfig.getDayBuyCount();
//				item.setDayBuyCount(dayBuyCount);
//			}
//		}
//	}
//
//	@Override
//	public Message buyMarketItem(Role role, IoSession session, int id) {
//		Message message = new Message();
//		message.setType(MarketConstant.BUY_MARKET_ITEM);
//		Market market = role.getMarket();
//
//		Map<Integer, MarketItem> refreshMarketItemMap = market.getRefreshMarketItemMap();
//		Map<Integer, MarketItem> notRefreshMarketItemMap = market.getNotRefreshMarketItemMap();
//		Map<Integer, MarketItem> tempMarketItemMap = null;
//		boolean isNotRefreshMarketItem = false;
//		if (refreshMarketItemMap.containsKey(id)) {
//			tempMarketItemMap = refreshMarketItemMap;
//		} else if (notRefreshMarketItemMap.containsKey(id)) {
//			isNotRefreshMarketItem = true;
//			tempMarketItemMap = notRefreshMarketItemMap;
//		}
//
//		if (tempMarketItemMap == null) {
//			return null;
//		}
//
//		MarketItem marketItem = tempMarketItemMap.get(id);
//		int dayBuyCount = marketItem.getDayBuyCount();
//		// 检查限购
//		if (dayBuyCount != -1) {
//			// 如果是永久可以购买
//			if (dayBuyCount == 0) {
//				message.putShort(ErrorCode.MARKET_ITEM_OUT_OF_DAY_BUY_COUNT);
//				return message;
//			}
//		}
//
//		MarketConfig config = MarketConfigCache.getMarketConfigById(id);
//		byte itemType = config.getItemType();
//		int singleBuyNum = config.getSingleBuyNum();
//		byte payMethod = config.getPayMethod();
//		int cost = config.getCost();
//		int itemId = config.getItemId();
//
//		// 检查是否超过物品上线或库存上限
//		if (!incomeService.checkItemLimit(role, itemType, itemId, payMethod)) {
//			message.putShort(ErrorCode.MAX_CAPACITY);
//			return message;
//		}
//
//		// 检查货币是否足够
//		if (payMethod == MarketConstant.PAY_METHOD_MONEY) {
//			int money = role.getMoney();
//			if (money < cost) {
//				message.putShort(ErrorCode.NO_MONEY);
//				return message;
//			}
//		} else if (payMethod == MarketConstant.PAY_METHOD_GOLD) {
//			int gold = role.getGold();
//			if (gold < cost) {
//				message.putShort(ErrorCode.NO_GOLD);
//				return message;
//			}
//		}
//		// else if(payMethod == MarketConstant.PAY_METHOD_RMB){
//		// message.putShort(ErrorCode.NO_RMB);
//		// return message;
//		// }
//
//		// 扣除
//		if (payMethod == MarketConstant.PAY_METHOD_MONEY) {
//			roleService.addMoney(role, -cost);
//		} else if (payMethod == MarketConstant.PAY_METHOD_GOLD) {
//			roleService.addGold(role, -cost);
//		}
//
//		// 扣除限购
//		if (dayBuyCount != -1) {
//			dayBuyCount--;
//			marketItem.setDayBuyCount(dayBuyCount);
//		}
//
//		// 给玩家商品
//		byte method = (byte) (payMethod == MarketConstant.PAY_METHOD_GOLD ? 1 : 0);
//		incomeService.award(role, itemType, itemId, singleBuyNum, method);
//
//		// 如果不是常驻商品则检查去除
//		if (isNotRefreshMarketItem) {
//			if (marketItem.getDayBuyCount() == 0) {
//				notRefreshMarketItemMap.remove(id);
//			}
//		}
//
//		market.setChange(true);
//		// 替换商品的信息
//		int bindId = MarketConfigCache.getMarketConfigById(id).getBindId();
//		int bindDayBuyCount = 0;
//		Map<Integer, MarketItem> tempBindMarketItemMap = null;
//		if (bindId != 0) {
//			if (refreshMarketItemMap.containsKey(bindId)) {
//				tempBindMarketItemMap = refreshMarketItemMap;
//			} else if (notRefreshMarketItemMap.containsKey(bindId)) {
//				tempBindMarketItemMap = notRefreshMarketItemMap;
//			}
//			bindDayBuyCount = tempBindMarketItemMap.get(bindId).getDayBuyCount();
//		}
//
//		message.putShort(ErrorCode.SUCCESS);
//		message.putInt(marketItem.getDayBuyCount());
//		message.putInt(bindId);
//		message.putInt(bindDayBuyCount);
//
//		return message;
//	}
//
//	@Override
//	public Message showMarketItem(Role role) {
//		Message message = new Message();
//		message.setType(MarketConstant.SHOW_MARKET_ITEM);
//		Market market = role.getMarket();
//
//		int nowTime = Utils.getNowTime();
//		this.resetMarketItem(role, nowTime);
//		Map<Integer, MarketItem> notRefreshMarketItemMap = market.getNotRefreshMarketItemMap();
//		Map<Integer, MarketItem> refreshMarketItemMap = market.getRefreshMarketItemMap();
//
//		Map<Integer, MarketItem> tempRefreshMarketItemMap = new HashMap<>();
//		tempRefreshMarketItemMap.putAll(refreshMarketItemMap);
//		// 当首冲存在时只显示首冲的
//		for (MarketItem item : notRefreshMarketItemMap.values()) {
//			int id = item.getId();
//			MarketConfig config = MarketConfigCache.getMarketConfigById(id);
//			int bindId = config.getBindId();
//			if (bindId != 0) {
//				tempRefreshMarketItemMap.remove(bindId);
//			}
//		}
//
//		Map<Integer, MarketItem> allItem = new HashMap<>();
//		allItem.putAll(notRefreshMarketItemMap);
//		allItem.putAll(tempRefreshMarketItemMap);
//		List<Integer> itemList = new ArrayList<>(allItem.keySet());
//		Collections.sort(itemList);
//		message.putInt(itemList.size());
//		for (Integer id : itemList) {
//			MarketItem item = allItem.get(id);
//			int count = item.getDayBuyCount();
//			message.putInt(id);
//			message.putInt(count);
//		}
//
//		return message;
//	}
//
//	/**
//	 * 获得商城今天的刷新时间
//	 * 
//	 * @param nowTime
//	 * @return
//	 */
//	private int getMarketTodayRefreshTime(int nowTime) {
//		int time = Utils.getTodayTimePoint(nowTime, 0, 0);
//		return time;
//	}

}
