package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventAwardHero extends EventElement {

	private Map<Byte, Integer> awardHeroMap = new HashMap<>();

	@Override
	protected void formatData(String data) {
		String[] s = data.split(",");
		int i = 0;
		while (i < s.length) {
			byte quality = Byte.parseByte(s[i++]);
			int count = Integer.parseInt(s[i++]);

			awardHeroMap.put(quality, count);
		}
	}

	public Map<Byte, Integer> getAwardHeroMap() {
		return awardHeroMap;
	}
}
