package com.randioo.owlofwar_server_simplify_protobuf.entity.bo;

public class Card {
	/** role ID */
	private int roleId;
	/** card ID */
	private int cardId;
	/** 等级 */
	private byte lv;
	/** 数量 */
	private int num;

	private boolean change;

	public Card clone() {
		Card dest = new Card();
		dest.setCardId(cardId);
		dest.setLv(lv);
		dest.setNum(num);
		dest.setRoleId(roleId);
		return dest;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
		setChange(true);
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
		setChange(true);
	}

	public byte getLv() {
		return lv;
	}

	public void setLv(byte lv) {
		this.lv = lv;
		setChange(true);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		setChange(true);
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

}
