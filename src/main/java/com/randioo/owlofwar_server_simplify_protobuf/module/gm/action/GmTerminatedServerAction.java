package com.randioo.owlofwar_server_simplify_protobuf.module.gm.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.module.gm.service.GmService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmTerminatedServerRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(GmTerminatedServerRequest.class)
public class GmTerminatedServerAction extends ActionSupport {
	private GmService gmService;

	public void setGmService(GmService gmService) {
		this.gmService = gmService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		GmTerminatedServerRequest request = (GmTerminatedServerRequest) data;
		gmService.terminatedServer(request.getCode(),session);
	}
}
