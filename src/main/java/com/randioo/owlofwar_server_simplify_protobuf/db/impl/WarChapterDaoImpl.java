package com.randioo.owlofwar_server_simplify_protobuf.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.randioo.owlofwar_server_simplify_protobuf.db.converter.WarChapterConverter;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.WarChapterDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarChapter;
import com.randioo.randioo_server_base.db.access.DataAccess;
import com.randioo.randioo_server_base.db.converter.IntegerConverter;

public class WarChapterDaoImpl extends DataAccess implements WarChapterDao {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void insertWarChapter(WarChapter warChapter, Connection conn) {
		// TODO Auto-generated method stub
		final String sql = "insert into warchapter(roleId,chapterId,award)" + "values(?,?,?)";
		try {
			super.insertNotCloseConn(sql, new IntegerConverter(), conn, warChapter.getRoleId(), warChapter.getChapterId(),
					warChapter.getAward());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertWarChapter(WarChapter warChapter) {
		// TODO Auto-generated method stub
		final String sql = "insert into warchapter(roleId,chapterId,award)" + "values(?,?,?)";
		try {
			Connection conn = dataSource.getConnection();
			super.insert(sql, new IntegerConverter(), conn, warChapter.getRoleId(), warChapter.getChapterId(),
					warChapter.getAward());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateWarChapter(WarChapter warChapter) {
		// TODO Auto-generated method stub
		final String sql = "update warchapter set chapterId=?,award=?" + " where roleId=? and chapterId=? limit 1";
		try {
			Connection conn = dataSource.getConnection();
			super.update(sql, conn, warChapter.getChapterId(), warChapter.getAward(), warChapter.getRoleId(), warChapter.getChapterId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<WarChapter> getAllWarChapterByRoleId(int roleId) {
		// TODO Auto-generated method stub
		String sql = "select * from warchapter where roleId=?";
		try {
			Connection conn = dataSource.getConnection();
			List<WarChapter> result = super.queryForList(sql, new WarChapterConverter(), conn, roleId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
