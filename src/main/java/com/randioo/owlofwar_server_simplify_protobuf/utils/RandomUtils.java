package com.randioo.owlofwar_server_simplify_protobuf.utils;

import java.util.Random;

public class RandomUtils {
	private final static Random random = new Random();

	public static final int getRandomNum(int startIndex, int base) {
		if (base <= 0) {
			return 0;
		}
		return startIndex + random.nextInt(base - startIndex + 1);
	}

	public static final int getRandomNum(int limit) {
		if (limit <= 0) {
			return 0;
		}
		return random.nextInt(limit);
	}
}
