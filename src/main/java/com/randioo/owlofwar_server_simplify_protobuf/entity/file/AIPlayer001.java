package com.randioo.owlofwar_server_simplify_protobuf.entity.file;

import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardList;

public class AIPlayer001 {
	private String account;
	private String nickname;
	private String cards;
	
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCards() {
		return cards;
	}
	public void setCards(String cards) {
		this.cards = cards;
	}
	
	public CardList getCardList()
	{
		CardList cardList = new CardList();
		cardList.setIndex(0);
		String[] tempStr = this.cards.split(",");
		cardList.setMainId(Integer.parseInt(tempStr[0]));
		for(int i= 1 ; i < tempStr.length;i++)
		{
			if(!tempStr[i].equals(""))
			{
				cardList.getList().add(Integer.parseInt(tempStr[i]));
			}
		}
		
		return cardList;
	}
}
