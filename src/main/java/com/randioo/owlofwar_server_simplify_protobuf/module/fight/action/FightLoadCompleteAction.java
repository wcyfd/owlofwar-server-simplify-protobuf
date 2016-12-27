package com.randioo.owlofwar_server_simplify_protobuf.module.fight.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.service.FightService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightLoadResourceCompleteRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(FightLoadResourceCompleteRequest.class)
public class FightLoadCompleteAction extends ActionSupport {
	private FightService fightService;

	public void setFightService(FightService fightService) {
		this.fightService = fightService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		FightLoadResourceCompleteRequest request = (FightLoadResourceCompleteRequest) data;
		Role role = (Role)RoleCache.getRoleBySession(session);
		fightService.loadResourceComplete(role, session);
	}
}
