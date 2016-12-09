package com.randioo.owlofwar_server_simplify_protobuf.module.gm.service;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface GMService extends BaseServiceInterface{
	public GeneratedMessage rejectLogin(String code);
	public GeneratedMessage terminatedServer(String code);
	public GeneratedMessage openLogin(String code);
}
