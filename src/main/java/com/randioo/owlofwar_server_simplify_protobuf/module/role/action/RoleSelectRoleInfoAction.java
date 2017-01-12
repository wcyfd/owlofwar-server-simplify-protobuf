package com.randioo.owlofwar_server_simplify_protobuf.module.role.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.role.service.RoleService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Role.RoleInfoSelectRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(RoleInfoSelectRequest.class)
public class RoleSelectRoleInfoAction extends ActionSupport{
	private RoleService roleService;
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@Override
	public void execute(Object data, IoSession session) {
		RoleInfoSelectRequest request = (RoleInfoSelectRequest)data;
		
		Role role = (Role) RoleCache.getRoleBySession(session);		
		roleService.selectRoleInfo(role, request.getRoleInfoTypeList());
	}

}
