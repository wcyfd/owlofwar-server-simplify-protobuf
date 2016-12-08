package com.randioo.owlofwar_server_simplify_protobuf.module.ping.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Ping.PingResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.randioo_server_base.module.BaseService;

public class PingServiceImpl extends BaseService implements PingService {

	@Override
	public GeneratedMessage ping(IoSession session, long clientTimestamp) {
		return SCMessage
				.newBuilder()
				.setPingResponse(
						PingResponse.newBuilder().setClientTimestamp(clientTimestamp)
								.setServerTimestamp(System.currentTimeMillis())).build();
	}

}
