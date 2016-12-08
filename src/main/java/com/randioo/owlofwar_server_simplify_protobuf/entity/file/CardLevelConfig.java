package com.randioo.owlofwar_server_simplify_protobuf.entity.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardLevel;

public class CardLevelConfig {
	private byte quality;
	
	private Map<Integer, CardLevel> lvMap = new HashMap<Integer, CardLevel>();

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public Map<Integer, CardLevel> getLvMap() {
		return lvMap;
	}

	public void setLvMap(Map<Integer, CardLevel> lvMap) {
		this.lvMap = lvMap;
	}
	
}
