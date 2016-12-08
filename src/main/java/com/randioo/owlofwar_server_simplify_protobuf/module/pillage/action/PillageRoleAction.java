package com.randioo.owlofwar_server_simplify_protobuf.module.pillage.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.pillage.service.PillageService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Pillage.PillageRoleRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(PillageRoleRequest.class)
public class PillageRoleAction extends ActionSupport {
	private PillageService pillageService;

	public void setPillageService(PillageService pillageService) {
		this.pillageService = pillageService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		PillageRoleRequest request = (PillageRoleRequest) data;
		Role role = RoleCache.getRoleBySession(session);
		pillageService.choose(role, request.getNpc(), session);
	}
}
