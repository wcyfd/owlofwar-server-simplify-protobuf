package com.randioo.owlofwar_server_simplify_protobuf.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class RoleConverter implements ResultConverter<Role> {

	public Role convert(ResultSet rs) throws SQLException {
		Role role = new Role();

		role.setRoleId(rs.getInt("id"));
		role.setAccount(rs.getString("account"));
		role.setName(rs.getString("name"));
		role.setMoney(rs.getInt("money"));
		role.setGold(rs.getInt("gold"));
		role.setFood(rs.getInt("food"));
		role.setRawListStr(rs.getString("listStr"));
		role.setUseCardsId(rs.getInt("useCardsId"));
		role.setPoint(rs.getInt("point"));

		return role;
	}
}
