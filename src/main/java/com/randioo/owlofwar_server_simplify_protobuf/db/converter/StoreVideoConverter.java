package com.randioo.owlofwar_server_simplify_protobuf.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.StoreVideo;
import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class StoreVideoConverter implements ResultConverter<StoreVideo> {

	public StoreVideo convert(ResultSet rs) throws SQLException {
		StoreVideo storeVideo = new StoreVideo();
		storeVideo.setGameId(rs.getInt("gameId"));
		storeVideo.setGameResultStr(rs.getString("gameResultStr"));
		storeVideo.setStartTime(rs.getLong("startTime"));
		storeVideo.setStoreFramesBytes(rs.getBytes("storeFramesBytes"));
		storeVideo.setStoreRoleResourceInfosBytes(rs.getBytes("storeRoleResourceInfosBytes"));
		return storeVideo;
	}
}
