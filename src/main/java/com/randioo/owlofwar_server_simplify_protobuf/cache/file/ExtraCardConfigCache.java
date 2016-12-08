package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.po.ExtraCardConfig;

public class ExtraCardConfigCache {
	private static Map<Integer, ExtraCardConfig> extraCardMap = new HashMap<>();

	public static void putExtraCardConfig(ExtraCardConfig config) {
		int extraCardId = config.getExtraCardId();
		extraCardMap.put(extraCardId, config);
	}

	public static ExtraCardConfig getExtraCardConfigByExtraCardId(int extraCardId) {
		return extraCardMap.get(extraCardId);
	}
}
