package com.randioo.owlofwar_server_simplify_protobuf.module.war.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.war.service.WarService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarGetChapterAwardRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;
@PTAnnotation(WarGetChapterAwardRequest.class)
public class WarGetChapterAwardAction extends ActionSupport{
	private WarService warService;
	public void setWarService(WarService warService) {
		this.warService = warService;
	}
	
	@Override
	public void execute(Object data, IoSession session) {
		WarGetChapterAwardRequest request = (WarGetChapterAwardRequest)data;
		Role role = (Role)RoleCache.getRoleBySession(session);
		GeneratedMessage message = warService.getChapterAward(role, request.getChapterId());
		if(message!=null){
			session.write(message);
		}
	}
}
