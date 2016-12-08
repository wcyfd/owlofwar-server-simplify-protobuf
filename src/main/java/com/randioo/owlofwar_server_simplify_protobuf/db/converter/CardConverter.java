package com.randioo.owlofwar_server_simplify_protobuf.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class CardConverter implements ResultConverter<Card> {

	public Card convert(ResultSet rs) throws SQLException {
		Card card = new Card();
		card.setRoleId(rs.getInt("roleId"));
		card.setCardId(rs.getInt("cardId"));
		card.setLv(rs.getByte("lv"));
		card.setNum(rs.getInt("num"));
		return card;
	}
}
