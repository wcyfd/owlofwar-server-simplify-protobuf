package com.randioo.owlofwar_server_simplify_protobuf.module.card.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardLevelConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.CardDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardConfig;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardLevel;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardList;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.CardConstant;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardChangeMainCardResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardChooseUseCardListResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardEditCardListResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardGetCardsInfoResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardLvUpResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.CardData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.CardListData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.randioo_server_base.module.BaseService;

public class CardServiceImpl extends BaseService implements CardService {
	private CardDao cardDao;

	public void setCardDao(CardDao cardDao) {
		this.cardDao = cardDao;
	}

	@Override
	public void initCard(Role role, List<Card> list) {
		for (Card card : list) {
			role.getCardMap().put(card.getCardId(), card);
		}
	}

	
	public void randomCardList(Role role, int i) {
		CardList cardList = new CardList();
		cardList.setIndex(i);

		// 随机主将
		cardList.setMainId(this.randomMainId(role.getCardMap()));
		// 随机卡组 主将卡已经无法使用 英雄只能随机一次，其他卡可以随机多次
		Map<Integer, Integer> count = new HashMap<Integer, Integer>();
		List<Card> randomList = new ArrayList<Card>();
		for (Card x : role.getCardMap().values()) {
			if (x.getLv() >= 1) {
				// 解锁的武将才可用
				randomList.add(x);
			}
		}
		Random r = new Random();
		for (int j = 0; j < 7; j++) {
			int use = r.nextInt(randomList.size());
			int useId = randomList.get(use).getCardId();
			// 判断是否合法
			if (useId == cardList.getMainId()) {
				j--;
				continue;
			}
			// 判断卡类型
			CardConfig config = CardConfigCache.getConfigById(useId);
			if (count.get(useId) == null) {
				cardList.getList().add(useId);
				count.put(useId, 1);
			} else if (count.get(useId) != null && count.get(useId) < config.getUseTime()) {
				cardList.getList().add(useId);
				int time = count.get(useId);
				count.put(useId, time + 1);
			} else {
				j--;
			}
		}
		role.getCardListMap().put(cardList.getIndex(), cardList);
	}

	@Override
	public GeneratedMessage getInfoCards(Role role) {
		CardGetCardsInfoResponse.Builder cardGetCardsInfoResponseBuilder = CardGetCardsInfoResponse.newBuilder();
		for (CardList x : role.getCardListMap().values()) {
			cardGetCardsInfoResponseBuilder.addCardListDatas(CardListData.newBuilder().setMainId(x.getMainId())
					.setCardListIndex(x.getIndex()).addAllCardIds(x.getList()));
		}
		
		return SCMessage.newBuilder().setCardGetCardsInfoResponse(cardGetCardsInfoResponseBuilder.setUseCardId(role.getUseCardsId())).build();
	}

	@Override
	public GeneratedMessage changeCards(Role role, int id, int index, int cardId) {
		// 取出卡组
		CardList cardList = role.getCardListMap().get(id);
		if (cardList == null)
			return null;
		// 判断是否 拥有
		if (!role.getCardMap().containsKey(cardId)) {

			return SCMessage.newBuilder()
					.setCardEditCardListResponse(CardEditCardListResponse.newBuilder().setErrorCode(ErrorCode.NO_CARD))
					.build();
		}
		if (index > 11) {
			return SCMessage
					.newBuilder()
					.setCardEditCardListResponse(
							CardEditCardListResponse.newBuilder().setErrorCode(ErrorCode.OUT_INDEX_OF)).build();
		}

		// 开始过滤合法性
		CardConfig config = CardConfigCache.getConfigById(cardId);
		// 记录替换的卡 是否出现在卡组中
		int oldCard;
		if (index == 0) {
			oldCard = cardList.getMainId();
			// 更换主将
			if (config.getType() != 0) {
				return SCMessage
						.newBuilder()
						.setCardEditCardListResponse(
								CardEditCardListResponse.newBuilder().setErrorCode(ErrorCode.ERR_TYPE_MAIN_ID)).build();
			}
			// 判断替换卡是否出现在卡组中

			if (cardList.getList().contains(cardId)) {
				int oldIndex = cardList.getList().indexOf(cardId);
				// 将旧主将替换该位置
				cardList.getList().set(oldIndex, oldCard);
			}
			// 替换新 主将
			cardList.setMainId(cardId);
		} else {
			// 替换卡组
			index = index - 1;
			// 检查新卡是否超过上限
			if (cardList.getMainId() == cardId) {
				return SCMessage
						.newBuilder()
						.setCardEditCardListResponse(
								CardEditCardListResponse.newBuilder().setErrorCode(ErrorCode.ERR_NUM_CARD_H)).build();
			}
			if (!this.checkCardNum(cardList, cardId, config)) {
				return SCMessage
						.newBuilder()
						.setCardEditCardListResponse(
								CardEditCardListResponse.newBuilder().setErrorCode(ErrorCode.ERR_NUM_CARD_N)).build();
			}
			cardList.getList().set(index, cardId);
		}

		return SCMessage
				.newBuilder()
				.setCardEditCardListResponse(
						CardEditCardListResponse
								.newBuilder()
								.setErrorCode(ErrorCode.SUCCESS)
								.setCardListData(
										CardListData.newBuilder().setMainId(cardList.getMainId())
												.setCardListIndex(cardList.getIndex())
												.addAllCardIds(cardList.getList()))).build();
	}

	@Override
	public GeneratedMessage cardLvUp(Role role, int cardId) {
		Card card = role.getCardMap().get(cardId);
		if (card == null)
			return null;
		CardConfig config = CardConfigCache.getConfigById(cardId);
		CardLevel cardLevel = CardLevelConfigCache.getConfig(config.getQuality(), card.getLv());
		if (cardLevel == null) {
			return null;
		}

		if (cardLevel.getCostMoney() == 0) {
			return SCMessage.newBuilder()
					.setCardLvUpResponse(CardLvUpResponse.newBuilder().setErrorCode(ErrorCode.MAX_CARD_LV)).build();
		}

		if (role.getMoney() < cardLevel.getCostMoney()) {
			return SCMessage.newBuilder()
					.setCardLvUpResponse(CardLvUpResponse.newBuilder().setErrorCode(ErrorCode.NO_MONEY)).build();
		}

		if (card.getNum() < cardLevel.getCostCard()) {
			return SCMessage.newBuilder()
					.setCardLvUpResponse(CardLvUpResponse.newBuilder().setErrorCode(ErrorCode.NO_CARD)).build();
		}
		card.setLv((byte) (card.getLv() + 1));
		card.setNum(card.getNum() - cardLevel.getCostCard());
		// roleService.addMoney(role, -cardLevel.getCostMoney());

		return SCMessage.newBuilder()
				.setCardLvUpResponse(CardLvUpResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS).setCardId(cardId))
				.build();

	}

	@Override
	public GeneratedMessage chooseUseCardList(Role role, int index) {

		role.setUseCardsId(index);
		return SCMessage.newBuilder().setCardChooseUseCardListResponse(CardChooseUseCardListResponse.newBuilder())
				.build();
	}

	public int randomMainId(Map<Integer, Card> map) {
		List<Integer> mainList = new ArrayList<Integer>();
		for (Card x : map.values()) {
			CardConfig config = CardConfigCache.getConfigById(x.getCardId());
			if (config.getType() == 0) {
				mainList.add(x.getCardId());
			}
		}
		if (mainList.size() == 0) {
			return -1;
		}
		Random r = new Random();
		int rr = r.nextInt(mainList.size());
		return mainList.get(rr);

	}

	@Override
	public boolean checkCardNum(CardList cardList, int cardId, CardConfig config) {
		boolean flag = false;
		int count = 0;

		for (Integer x : cardList.getList()) {
			if (x == cardId)
				count++;
		}

		if (count < config.getUseTime()) {
			flag = true;
		}
		return flag;
	}

	@Override
	public Card createCard(Role role, int cardId, byte lv) {
		Card card = new Card();
		card.setCardId(cardId);
		card.setLv(lv);
		card.setNum(0);
		card.setRoleId(role.getRoleId());

		cardDao.insertCardNormal(card);
		return card;
	}
	
	@Override
	public Card createCard(Role role,int cardId){
		Card card = this.createCard(role, cardId, (byte)1);
		return card;
	}
	
	@Override
	public GeneratedMessage changeHero(Role role,int useCardListIndex,int cardId){
		CardList cardList = role.getCardListMap().get(useCardListIndex);
		CardConfig config = CardConfigCache.getConfigById(cardId);
		if (config.getType() != CardConstant.CARD_HERO) {
			return SCMessage
					.newBuilder()
					.setCardChangeMainCardResponse(
							CardChangeMainCardResponse.newBuilder().setErrorCode(ErrorCode.ERR_TYPE_MAIN_ID)).build();

		}
		int mainId = cardList.getMainId();
		if (cardId == mainId) {
			return SCMessage
					.newBuilder()
					.setCardChangeMainCardResponse(
							CardChangeMainCardResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)).build();

		}

		List<Integer> list = cardList.getList();
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == cardId) {
				index = i;
				break;
			}
		}

		list.remove(index);
		list.add(index, mainId);
		cardList.setMainId(cardId);

		return SCMessage.newBuilder()
				.setCardChangeMainCardResponse(CardChangeMainCardResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS))
				.build();

	}

}
