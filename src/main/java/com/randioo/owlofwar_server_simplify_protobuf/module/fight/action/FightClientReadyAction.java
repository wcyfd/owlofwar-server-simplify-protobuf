package com.randioo.owlofwar_server_simplify_protobuf.module.fight.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.service.FightService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightClientReadyRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(FightClientReadyRequest.class)
public class FightClientReadyAction extends ActionSupport {
	private FightService fightService;

	public void setFightService(FightService fightService) {
		this.fightService = fightService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		FightClientReadyRequest request = (FightClientReadyRequest) data;
		Role role = (Role)RoleCache.getRoleBySession(session);
		fightService.clientReady(role, session);
	}
}
