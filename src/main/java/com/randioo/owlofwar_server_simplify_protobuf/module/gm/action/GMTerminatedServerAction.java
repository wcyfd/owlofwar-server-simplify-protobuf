package com.randioo.owlofwar_server_simplify_protobuf.module.gm.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.module.gm.service.GMService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.GM.GMTerminatedServerRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(GMTerminatedServerRequest.class)
public class GMTerminatedServerAction extends ActionSupport {
	private GMService gmService;

	public void setGmService(GMService gmService) {
		this.gmService = gmService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		GMTerminatedServerRequest request = (GMTerminatedServerRequest) data;
		GeneratedMessage message = gmService.terminatedServer(request.getCode());
		if (message != null) {
			session.write(message);
		}
	}
}
