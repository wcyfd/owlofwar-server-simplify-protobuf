package com.randioo.owlofwar_server_simplify_protobuf.module.fight.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGame;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResult;
import com.randioo.randioo_server_base.module.BaseServiceInterface;
import com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game.RTSGame;

public interface FightService extends BaseServiceInterface {
	/**
	 * 客户端加载完毕
	 * 
	 * @param role
	 * @return
	 */
	public void clientReady(Role role, IoSession session);

	void loadResource(OwlofwarGame game);

	void loadResourceComplete(Role role, IoSession session);

	public void receiveGameAction(Role role, GeneratedMessage message);

	void receiveGameEnd(Role role,IoSession session);

	/**
	 * 对战时掉线处理
	 * 
	 * @param role
	 * @author wcy 2016年8月23日
	 */
	void offline(Role role);

	void sendKeyFrameInfo(RTSGame game);

	/**
	 * 获取所有帧
	 * @param role
	 * @return
	 * @author wcy 2016年12月5日
	 */
	GeneratedMessage readFrames(Role role);

	/**
	 * 获得比赛奖励
	 * @param gameId
	 * @param role
	 * @param result
	 * @param score
	 * @author wcy 2016年12月6日
	 */
	GeneratedMessage getGameAward(Role role, GameResult result, int score);

}
