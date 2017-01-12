package com.randioo.owlofwar_server_simplify_protobuf.module.war.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.war.service.WarService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarShowWarChapterRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(WarShowWarChapterRequest.class)
public class WarShowWarChapterInfoAction extends ActionSupport{
	private WarService warService;
	public void setWarService(WarService warService) {
		this.warService = warService;
	}
	
	@Override
	public void execute(Object data, IoSession session) {
		WarShowWarChapterRequest request = (WarShowWarChapterRequest) data;
		
		Role role = (Role)RoleCache.getRoleBySession(session);
		GeneratedMessage message = warService.showWarChapterInfo(role);
		if(message!=null){
			session.write(message);
		}
	}
}
