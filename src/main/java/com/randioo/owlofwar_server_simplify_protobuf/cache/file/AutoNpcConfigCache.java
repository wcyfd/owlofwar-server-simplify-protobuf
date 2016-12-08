package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.ArrayList;
import java.util.List;

import com.randioo.owlofwar_server_simplify_protobuf.entity.file.AutoNpcConfig;

public class AutoNpcConfigCache {
	private static List<AutoNpcConfig> list = new ArrayList<>();

	public static void putAutpNpcConfig(AutoNpcConfig config) {
		if(config == null){
			return;
		}
		list.add(config);
	}

	public static List<AutoNpcConfig> getAllAutoNpcConfigList() {
		return list;
	}

	public static AutoNpcConfig getMaxAutoNpcConfig() {
		return list.get(list.size() - 1);
	}
}
