package com.randioo.owlofwar_server_simplify_protobuf.module.war.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.war.service.WarService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarMarchRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(WarMarchRequest.class)
public class WarMarchAction extends ActionSupport{
	private WarService warService;
	public void setWarService(WarService warService) {
		this.warService = warService;
	}
	
	@Override
	public void execute(Object data, IoSession session) {
		WarMarchRequest request = (WarMarchRequest)data;
		Role role = (Role)RoleCache.getRoleBySession(session);
		warService.march(role, request.getBuildId(), session);
		
	}
}
