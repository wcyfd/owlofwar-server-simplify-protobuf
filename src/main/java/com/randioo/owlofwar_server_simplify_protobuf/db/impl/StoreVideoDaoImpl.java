package com.randioo.owlofwar_server_simplify_protobuf.db.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.randioo.owlofwar_server_simplify_protobuf.db.converter.StoreVideoConverter;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.StoreVideoDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.StoreVideo;
import com.randioo.randioo_server_base.db.access.DataAccess;
import com.randioo.randioo_server_base.db.converter.IntegerConverter;

public class StoreVideoDaoImpl extends DataAccess implements StoreVideoDao {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void insertStoreVideo(StoreVideo storeVideo, Connection conn) {
		final String sql = "insert into storevideo(gameId,storeRoleResourceInfosBytes,storeFramesBytes,startTime,gameResultStr)"
				+ "values(?,?,?,?,?)";
		try {
			super.insertNotCloseConn(sql, new IntegerConverter(), conn, storeVideo.getGameId(),
					storeVideo.getStoreRoleResourceInfosBytes(), storeVideo.getStoreFramesBytes(),
					storeVideo.getStartTime(), storeVideo.getGameResultStr());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public StoreVideo getStoreVideoByGameId(int id) {
		String sql = "select * from storevideo where gameId=?";
		try {
			Connection conn = dataSource.getConnection();
			StoreVideo storeVideo = super.queryForObject(sql, new StoreVideoConverter(), conn, id);
			return storeVideo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getMaxStoreVideoId() {
		String sql = "select gameId from storevideo";
		int maxId = 0;
		try {
			Connection conn = dataSource.getConnection();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				int tempId = rs.getInt("gameId");
				if (tempId > maxId) {
					maxId = tempId;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxId;
	}
}
