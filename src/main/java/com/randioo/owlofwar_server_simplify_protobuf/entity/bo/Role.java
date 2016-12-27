package com.randioo.owlofwar_server_simplify_protobuf.entity.bo;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardList;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGame;
import com.randioo.owlofwar_server_simplify_protobuf.utils.StringUtils;

public class Role extends GameRole {

	/** 银币 */
	private int money;
	/** 金币 */
	private int gold;
	/** 粮食 */
	private int food;
	/** 积分 */
	private int point;
	/** 现有卡集合 */
	private Map<Integer, Card> cardMap = new HashMap<Integer, Card>();
	/** 卡组 */
	private Map<Integer, CardList> cardListMap = new HashMap<Integer, CardList>();
	/** 卡组STR */
	private String listStr;
	/** 当前使用中的卡组ID */
	private int useCardsId;

	private boolean change;
	/** 战斗世界 */
	private OwlofwarGame owlofwarGame;
	/** 游戏id */
	private int owlofwarGameId;

	public void setOwlofwarGame(OwlofwarGame owlofwarGame) {
		this.owlofwarGame = owlofwarGame;
	}

	public OwlofwarGame getOwlofwarGame() {
		return owlofwarGame;
	}

	public String getCardStr() {
		StringBuilder sb = new StringBuilder();
		for (Card card : this.cardMap.values()) {
			sb.append(card.getCardId()).append(",").append(card.getLv()).append(",").append(card.getNum()).append(",")
					.append(";");
		}
		return sb.toString();
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		setChange(true);
		this.money = money;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		setChange(true);
		this.gold = gold;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		setChange(true);
		this.food = food;
	}

	public Map<Integer, Card> getCardMap() {
		return cardMap;
	}

	public Map<Integer, CardList> getCardListMap() {
		return cardListMap;
	}

	public String getListStr() {
		StringBuilder sb = new StringBuilder();
		for (CardList x : cardListMap.values()) {
			sb.append(x.getIndex()).append(",").append(x.getMainId()).append(",");
			int count = 0;
			for (Integer z : x.getList()) {
				count++;
				if (count >= x.getList().size()) {
					sb.append(z).append(";");
				} else {
					sb.append(z).append(",");
				}
			}
		}
		this.listStr = sb.toString();
		return listStr;
	}

	public boolean setListStr(String listStr) {

		if (listStr == null || listStr.equals(""))
			return false;
		String str[] = listStr.split(";");
		for (String x : str) {
			CardList temp = new CardList(x);
			this.cardListMap.put(temp.getIndex(), temp);
		}
		
		return true;
	}
	
	public void setListRawStr(String listStr){
		if(setListStr(listStr)){
			this.listStr = listStr;
		}
	}

	public int getUseCardsId() {
		return useCardsId;
	}

	public void setUseCardsId(int useCardsId) {
		setChange(true);
		this.useCardsId = useCardsId;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		setChange(true);
		this.point = point;
	}

	public boolean isChange() {
		if (!change)
			change = StringUtils.checkChange(listStr, getListStr());
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public int getOwlofwarGameId() {
		return owlofwarGameId;
	}

	public void setOwlofwarGameId(int owlofwarGameId) {
		this.owlofwarGameId = owlofwarGameId;
	}

}
