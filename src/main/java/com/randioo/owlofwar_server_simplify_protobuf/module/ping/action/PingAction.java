package com.randioo.owlofwar_server_simplify_protobuf.module.ping.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.module.ping.service.PingService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Ping.PingRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(PingRequest.class)
public class PingAction extends ActionSupport {

	private PingService pingService;

	public void setPingService(PingService pingService) {
		this.pingService = pingService;
	}

	public void execute(Object data, IoSession session) {
		GeneratedMessage message = pingService.ping(session, ((PingRequest) data).getClientTimestamp());
		if (message != null) {
			session.write(message);
		}
	}
}
