package com.randioo.owlofwar_server_simplify_protobuf.db.dao;

import java.sql.Connection;
import java.util.List;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.StoreVideo;

public interface StoreVideoDao {

	/**
	 * 插入新卡片不关闭链接
	 * 
	 * @param card
	 * @param conn
	 */
	public void insertStoreVideo(StoreVideo storeVideo, Connection conn);

	public StoreVideo getStoreVideoByGameId(int id);
	
	public int getMaxStoreVideoId();
}
