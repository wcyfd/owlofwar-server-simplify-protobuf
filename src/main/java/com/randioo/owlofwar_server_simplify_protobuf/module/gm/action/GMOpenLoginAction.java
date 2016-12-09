package com.randioo.owlofwar_server_simplify_protobuf.module.gm.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.module.gm.service.GMService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.GM.GMOpenLoginRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(GMOpenLoginRequest.class)
public class GMOpenLoginAction extends ActionSupport {
	private GMService gmService;

	public void setGmService(GMService gmService) {
		this.gmService = gmService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		GMOpenLoginRequest request = (GMOpenLoginRequest) data;
		GeneratedMessage message = gmService.openLogin(request.getCode());
		if (message != null) {
			session.write(message);
		}
	}
}
