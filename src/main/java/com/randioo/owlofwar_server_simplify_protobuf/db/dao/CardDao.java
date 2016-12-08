package com.randioo.owlofwar_server_simplify_protobuf.db.dao;

import java.sql.Connection;
import java.util.List;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;

public interface CardDao {

	/**
	 * 插入新卡片不关闭链接
	 * 
	 * @param card
	 * @param conn
	 */
	public void insertCard(Card card, Connection conn);

	public void insertCardNormal(Card card);

	public void updateCard(Card card);

	public List<Card> getAllCardByRoleId(int roleId);

	void removeCard(Card card);

	void removeCard(int roleId, int cardId);
}
