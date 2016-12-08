package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;

public class MailCard {
	/**卡片id*/
	private int cardId;
	/**卡片等级*/
	private byte lv;
	
	public MailCard(){
		
	}
	
	public MailCard(Card card){
		this.cardId = card.getCardId();
		this.lv = card.getLv();
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public byte getLv() {
		return lv;
	}
	public void setLv(byte lv) {
		this.lv = lv;
	}
	
	
}
