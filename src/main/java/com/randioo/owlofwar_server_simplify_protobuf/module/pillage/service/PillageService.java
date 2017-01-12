package com.randioo.owlofwar_server_simplify_protobuf.module.pillage.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.randioo_server_base.module.BaseServiceInterface;
import com.randioo.randioo_server_base.net.protocal.randioo.Message;

public interface PillageService extends BaseServiceInterface{
	public void choose(Role role, boolean isAI,IoSession session);

	public void cancelChoose(Role role,IoSession session);


	GeneratedMessage showMatchingInfo(Role role);

	void competitionNotice(Role role, int competitionId, IoSession session);

	

}
