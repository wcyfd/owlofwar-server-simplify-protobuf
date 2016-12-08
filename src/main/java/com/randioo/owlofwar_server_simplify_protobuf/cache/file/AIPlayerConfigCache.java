package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.file.AIPlayer001;

public class AIPlayerConfigCache {
	private static Map<String, AIPlayer001> map = new HashMap<String, AIPlayer001>();

	public static Map<String, AIPlayer001> getMap() {
		return map;
	}

	public static void setMap(Map<String, AIPlayer001> map) {
		AIPlayerConfigCache.map = map;
	}
	
	public static AIPlayer001 getBean(String key)
	{
		return map.get(key);
	}
}
