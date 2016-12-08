package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.file.MapsConfig;

public class MapsConfigCache {
	private static Map<Integer, MapsConfig> maps = new HashMap<>();

	public static void putMapsConfig(MapsConfig config) {
		int mapsId = config.getMapsId();
		maps.put(mapsId, config);
	}

	public static MapsConfig getMapsConfigByMapsId(int mapsId) {
		return maps.get(mapsId);
	}
}
