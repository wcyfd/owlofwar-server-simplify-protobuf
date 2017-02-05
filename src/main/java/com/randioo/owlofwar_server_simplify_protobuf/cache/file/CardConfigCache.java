package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardConfig;

public class CardConfigCache {
	private static Map<Integer, CardConfig> map = new HashMap<Integer, CardConfig>();

	private static Map<Integer, CardConfig> map_9 = new HashMap<>();

	public static void putCardConfig(CardConfig cardConfig) {
		map.put(cardConfig.getId(), cardConfig);
		if (cardConfig.getType() != 9) {
			map_9.put(cardConfig.getId(), cardConfig);
		}
	}

	public static CardConfig getConfigById(int id) {
		return map.get(id);
	}

	public static Map<Integer, CardConfig> getMap() {
		return map;
	}

	public static void setMap(Map<Integer, CardConfig> map) {
		CardConfigCache.map = map;
	}
	public static Map<Integer,CardConfig> getResMap(){
		return map_9;
	}

}
