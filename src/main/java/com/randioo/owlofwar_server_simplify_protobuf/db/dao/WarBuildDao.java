package com.randioo.owlofwar_server_simplify_protobuf.db.dao;

import java.sql.Connection;
import java.util.List;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarBuild;

public interface WarBuildDao {

	/**
	 * 插入新建筑不关闭链接
	 * 
	 * @param card
	 * @param conn
	 */
	public void insertWarBuild(WarBuild warBuild, Connection conn);

	public void insertWarBuild(WarBuild warBuild);

	public void updateWarBuild(WarBuild warBuild);

	public List<WarBuild> getAllWarBuildByRoleId(int roleId);

}
