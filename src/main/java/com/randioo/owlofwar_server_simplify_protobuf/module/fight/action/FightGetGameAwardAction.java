package com.randioo.owlofwar_server_simplify_protobuf.module.fight.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.service.FightService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightGetGameAwardRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(FightGetGameAwardRequest.class)
public class FightGetGameAwardAction extends ActionSupport {
	private FightService fightService;

	public void setFightService(FightService fightService) {
		this.fightService = fightService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		FightGetGameAwardRequest request = (FightGetGameAwardRequest) data;
		Role role = (Role)RoleCache.getRoleBySession(session);

		
		GeneratedMessage message = fightService.getGameAward(role, request.getGameResultData());
		if(message!=null){
			session.write(message);
		}
	}
}
