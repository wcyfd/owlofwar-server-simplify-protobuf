package com.randioo.owlofwar_server_simplify_protobuf.module.match.service;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightEventListener;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGame;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface MatchService extends BaseServiceInterface{
	public void matchRole(IoSession session,Role role, boolean isAI, FightEventListener listener);

	public void matchRole(Role role1, FightEventListener listener1, Role role2, FightEventListener listener2);

	void cancelMatch(Role role);
	
	void matchComplete(OwlofwarGame game);

	void init();

}
