package com.randioo.owlofwar_server_simplify_protobuf.entity.file;

import java.util.ArrayList;
import java.util.List;

public class AutoNpcConfig {
	private List<Integer> npcIdList = new ArrayList<>();
	private int point;

	public List<Integer> getNpcIdList() {
		return npcIdList;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getPoint() {
		return point;
	}
}
