package com.randioo.owlofwar_server_simplify_protobuf.module.gm.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.module.gm.service.GmService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmOpenLoginRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(GmOpenLoginRequest.class)
public class GmOpenLoginAction extends ActionSupport {
	private GmService gmService;

	public void setGmService(GmService gmService) {
		this.gmService = gmService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		GmOpenLoginRequest request = (GmOpenLoginRequest) data;
		GeneratedMessage message = gmService.openLogin(request.getCode());
		if (message != null) {
			session.write(message);
		}
	}
}
