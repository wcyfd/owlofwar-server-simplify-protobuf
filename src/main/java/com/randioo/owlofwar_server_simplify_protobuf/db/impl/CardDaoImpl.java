package com.randioo.owlofwar_server_simplify_protobuf.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.randioo.owlofwar_server_simplify_protobuf.db.converter.CardConverter;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.CardDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.randioo_server_base.db.access.DataAccess;
import com.randioo.randioo_server_base.db.converter.IntegerConverter;

public class CardDaoImpl extends DataAccess implements CardDao {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void insertCard(Card card, Connection conn) {
		final String sql = "insert into card(roleId,cardId,lv,num)" + "values(?,?,?,?)";
		try {
			super.insertNotCloseConn(sql, new IntegerConverter(), conn, card.getRoleId(), card.getCardId(),
					card.getLv(), card.getNum());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertCardNormal(Card card) {
		final String sql = "insert into card(roleId,cardId,lv,num)" + "values(?,?,?,?)";
		try {
			Connection conn = dataSource.getConnection();
			super.insert(sql, new IntegerConverter(), conn, card.getRoleId(), card.getCardId(), card.getLv(),
					card.getNum());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeCard(Card card) {
		this.removeCard(card.getRoleId(), card.getCardId());
	}

	@Override
	public void removeCard(int roleId, int cardId) {
		final String sql = "delete from card where roleId=? and cardId=?";

		try {
			Connection conn = dataSource.getConnection();
			super.delete(sql, conn, roleId, cardId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateCard(Card card) {
		final String sql = "update card set lv=?,num=?" + " where roleId=? and cardId=? limit 1";
		try {
			Connection conn = dataSource.getConnection();
			super.update(sql, conn, card.getLv(), card.getNum(), card.getRoleId(), card.getCardId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Card> getAllCardByRoleId(int roleId) {
		String sql = "select * from card where roleId=?";
		try {
			Connection conn = dataSource.getConnection();
			List<Card> result = super.queryForList(sql, new CardConverter(), conn, roleId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
