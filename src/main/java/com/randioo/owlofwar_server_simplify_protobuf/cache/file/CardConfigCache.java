package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardConfig;

public class CardConfigCache {
	private static Map<Integer, CardConfig> map = new HashMap<Integer, CardConfig>();

	public static Map<Integer, CardConfig> getMap() {
		return map;
	}

	public static void setMap(Map<Integer, CardConfig> map) {
		CardConfigCache.map = map;
	}
	
	
	public static void putCardConfig(CardConfig cardConfig)
	{
		map.put(cardConfig.getId(), cardConfig);
	}
	
	public static CardConfig getConfigById(int id)
	{
		return map.get(id);
	}
}
