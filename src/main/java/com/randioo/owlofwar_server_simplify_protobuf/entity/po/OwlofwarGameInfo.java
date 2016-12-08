package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.Frame;
import com.randioo.owlofwar_server_simplify_protobuf.utils.TagReaderQueue;
import com.randioo.randioo_server_base.entity.GameEvent;

public class OwlofwarGameInfo {
	private int roleId;
	private FightEventListener listener;
	private Map<Integer, Integer> scoreMap = new HashMap<>();
	private TagReaderQueue<Frame> runQueue = new TagReaderQueue<>();

	public void setListener(FightEventListener listener) {
		this.listener = listener;
	}

	public FightEventListener getListener() {
		return listener;
	}

	public Map<Integer, Integer> getScoreMap() {
		return scoreMap;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	public TagReaderQueue<Frame> getRunQueue() {
		return runQueue;
	}
}
