package com.randioo.owlofwar_server_simplify_protobuf.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.randioo.owlofwar_server_simplify_protobuf.db.converter.CardConverter;
import com.randioo.owlofwar_server_simplify_protobuf.db.converter.WarBuildConverter;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.CardDao;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.WarBuildDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarBuild;
import com.randioo.randioo_server_base.db.access.DataAccess;
import com.randioo.randioo_server_base.db.converter.IntegerConverter;

public class WarBuildDaoImpl extends DataAccess implements WarBuildDao {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void insertWarBuild(WarBuild warBuild, Connection conn) {
		// TODO Auto-generated method stub
		final String sql = "insert into warbuild(roleId,buildId,starCount)" + "values(?,?,?)";
		try {
			super.insertNotCloseConn(sql, new IntegerConverter(), conn, warBuild.getRoleId(), warBuild.getBuildId(),
					warBuild.getStarCount());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertWarBuild(WarBuild warBuild) {
		// TODO Auto-generated method stub
		final String sql = "insert into warbuild(roleId,buildId,starCount)" + "values(?,?,?)";
		try {
			Connection conn = dataSource.getConnection();
			super.insert(sql, new IntegerConverter(), conn, warBuild.getRoleId(), warBuild.getBuildId(),
					warBuild.getStarCount());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateWarBuild(WarBuild warBuild) {
		// TODO Auto-generated method stub
		final String sql = "update warbuild set buildId=?,starCount=?" + " where roleId=? and buildId=? limit 1";
		try {
			Connection conn = dataSource.getConnection();
			super.update(sql, conn, warBuild.getBuildId(), warBuild.getStarCount(), warBuild.getRoleId(), warBuild.getBuildId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<WarBuild> getAllWarBuildByRoleId(int roleId) {
		// TODO Auto-generated method stub
		String sql = "select * from warbuild where roleId=?";
		try {
			Connection conn = dataSource.getConnection();
			List<WarBuild> result = super.queryForList(sql, new WarBuildConverter(), conn, roleId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
