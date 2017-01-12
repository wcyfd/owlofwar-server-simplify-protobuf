package com.randioo.owlofwar_server_simplify_protobuf.db.dao;

import java.sql.Connection;
import java.util.List;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarChapter;

public interface WarChapterDao {

	/**
	 * 插入新建筑不关闭链接
	 * 
	 * @param card
	 * @param conn
	 */
	public void insertWarChapter(WarChapter warChapter, Connection conn);

	public void insertWarChapter(WarChapter warChapter);

	public void updateWarChapter(WarChapter warChapter);

	public List<WarChapter> getAllWarChapterByRoleId(int roleId);

}
