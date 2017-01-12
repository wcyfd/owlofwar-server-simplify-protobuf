package com.randioo.owlofwar_server_simplify_protobuf.module.gm.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.module.gm.service.GmService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmOpenLoginRequest;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmRejectLoginRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(GmRejectLoginRequest.class)
public class GmRejectLoginAction extends ActionSupport {
	private GmService gmService;

	public void setGmService(GmService gmService) {
		this.gmService = gmService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		GmOpenLoginRequest request = (GmOpenLoginRequest) data;
		GeneratedMessage message = gmService.rejectLogin(request.getCode());
		if (message != null) {
			session.write(message);
		}
	}
}
