package com.randioo.owlofwar_server_simplify_protobuf.module.pillage.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.randioo_server_base.module.BaseServiceInterface;
import com.randioo.randioo_server_base.net.protocal.randioo.Message;

public interface PillageService extends BaseServiceInterface{
//	public Message heart(String value, IoSession session);

//	public Message loginTest(IoSession ioSession);

	public void choose(Role role, boolean isAI,IoSession session);

	public void cancelChoose(Role role,IoSession session);

//	public void checkNum(Room room);

	GeneratedMessage showMatchingInfo(Role role);

	// public Message putMonster(int x,int y,int id, String path,Role role);
	//
	// public void sendEnd(int roomId);
	//
	// public void checkEnd(Role role);

}
