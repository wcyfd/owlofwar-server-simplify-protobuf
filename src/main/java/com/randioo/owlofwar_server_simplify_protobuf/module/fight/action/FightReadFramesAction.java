package com.randioo.owlofwar_server_simplify_protobuf.module.fight.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.service.FightService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightReadFrameRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(FightReadFrameRequest.class)
public class FightReadFramesAction extends ActionSupport {
	private FightService fightService;

	public void setFightService(FightService fightService) {
		this.fightService = fightService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		FightReadFrameRequest request = (FightReadFrameRequest) data;
		Role role = RoleCache.getRoleBySession(session);

		GeneratedMessage message = fightService.readFrames(role);
		if(message!=null){
			session.write(message);
		}
	}
}
