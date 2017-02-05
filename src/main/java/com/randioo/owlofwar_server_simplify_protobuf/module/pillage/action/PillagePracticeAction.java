package com.randioo.owlofwar_server_simplify_protobuf.module.pillage.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.pillage.service.PillageService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Pillage.PillagePracticeRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(PillagePracticeRequest.class)
public class PillagePracticeAction extends ActionSupport {
	private PillageService pillageService;

	public void setPillageService(PillageService pillageService) {
		this.pillageService = pillageService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		PillagePracticeRequest request = (PillagePracticeRequest) data;
		Role role = (Role)RoleCache.getRoleBySession(session);
		pillageService.practice(role, session);
	}
}
