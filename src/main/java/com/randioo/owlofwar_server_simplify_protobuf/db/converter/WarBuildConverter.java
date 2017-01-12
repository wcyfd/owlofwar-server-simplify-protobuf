package com.randioo.owlofwar_server_simplify_protobuf.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarBuild;
import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class WarBuildConverter implements ResultConverter<WarBuild> {

	public WarBuild convert(ResultSet rs) throws SQLException {
		WarBuild warBuild = new WarBuild();
		
		warBuild.setRoleId(rs.getInt("roleId"));
		warBuild.setBuildId(rs.getInt("buildId"));
		warBuild.setStarCount(rs.getByte("starCount"));
		
		return warBuild;
	}
}
