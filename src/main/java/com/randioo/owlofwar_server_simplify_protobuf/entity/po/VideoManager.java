package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VideoManager {
	private static Map<Integer, Video> videoMap = new ConcurrentHashMap<>();

	public static void addVideo(Video video) {
		videoMap.put(video.getGameId(), video);
	}
	
	public static Video getVideoById(int gameId){
		return videoMap.get(gameId);
	}
}
