package com.randioo.owlofwar_server_simplify_protobuf.module.war.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResultAwardData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResultData;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface WarService extends BaseServiceInterface {
	/**
	 * 显示战场信息
	 * 
	 * @param role
	 * @return
	 * @author wcy 2017年1月10日
	 */
	GeneratedMessage showWarChapterInfo(Role role);

	/**
	 * 显示战场章节信息
	 * 
	 * @param role
	 * @param chapterId
	 * @return
	 * @author wcy 2017年1月10日
	 */
	GeneratedMessage showWarBuildInfo(Role role, int chapterId);

	/**
	 * 行军
	 * 
	 * @param role
	 * @param chapterId
	 * @param buildId
	 * @param session
	 * @author wcy 2017年1月10日
	 */
	void march(Role role, int buildId, IoSession session);

	/**
	 * 刷新建筑
	 * 
	 * @param role
	 * @param buildId
	 * @param starCount
	 * @param session
	 * @return
	 * @author wcy 2017年1月11日
	 */
	void refreshWarBuild(Role role, GameResultData gameResultData,
			GameResultAwardData.Builder gameResultAwardDataBuilder);

	/**
	 * 获得章节奖励
	 * 
	 * @param role
	 * @param chapterId
	 * @return
	 * @author wcy 2017年1月11日
	 */
	GeneratedMessage getChapterAward(Role role, int chapterId);

}
