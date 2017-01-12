package com.randioo.owlofwar_server_simplify_protobuf.module.gm.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface GmService extends BaseServiceInterface{
	public GeneratedMessage rejectLogin(String code);
	public void terminatedServer(String code,IoSession session);
	public GeneratedMessage openLogin(String code);
}
