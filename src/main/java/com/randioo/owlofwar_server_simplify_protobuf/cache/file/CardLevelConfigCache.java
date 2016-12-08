package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardLevelConfig;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardLevel;

public class CardLevelConfigCache {
	private static Map<Byte, CardLevelConfig> map = new HashMap<>();

	public static Map<Byte, CardLevelConfig> getMap() {
		return map;
	}	
	
	public static void putConfig(CardLevelConfig config)
	{
		map.put(config.getQuality(), config);
	}
	
	public static CardLevel getConfig(byte quality , int lv)
	{
		CardLevelConfig config = map.get(quality);
		Map<Integer,CardLevel> lvMap = config.getLvMap();
		return lvMap.get(lv);
	}
	
}
