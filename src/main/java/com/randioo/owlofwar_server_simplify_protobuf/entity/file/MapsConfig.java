package com.randioo.owlofwar_server_simplify_protobuf.entity.file;


public class MapsConfig {
	public enum CardType {
		EXTRA_CARD, CARD;
	}

	/** 地图id */
	private int mapsId;
	/** 主帅id */
	private int mainId;
	/** 卡牌类型 */
	private CardType cardType;

	public int getMainId() {
		return mainId;
	}

	public void setMainId(int mainId) {
		this.mainId = mainId;
	}

	public int getMapsId() {
		return mapsId;
	}

	public void setMapsId(int mapsId) {
		this.mapsId = mapsId;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

}
