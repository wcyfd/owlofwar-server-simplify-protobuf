package com.randioo.owlofwar_server_simplify_protobuf.cache.file;

import java.util.ArrayList;
import java.util.List;

public class CardInitConfigCache {
	private static List<Integer> list = new ArrayList<Integer>();

	public static List<Integer> getList() {
		return list;
	}

	public static void setList(List<Integer> list) {
		CardInitConfigCache.list = list;
	}
	
	public static void addList(String id)
	{
		String strs[] = id.split(",");
		for(String x: strs)
		{
			list.add(Integer.parseInt(x));
		}
	}
}
