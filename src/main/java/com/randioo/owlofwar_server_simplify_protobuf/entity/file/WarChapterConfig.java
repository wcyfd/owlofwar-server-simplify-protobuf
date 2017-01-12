package com.randioo.owlofwar_server_simplify_protobuf.entity.file;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.WarChapterConfigCache;

public class WarChapterConfig{
	public static final String urlKey="warchapter.tbl";
	/** 章节id */
	public int chapterId;
	/** 章节奖励 */
	public int chapterAward;
		
	public static void parse(ByteBuffer buffer){
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		while(buffer.hasRemaining()){
			WarChapterConfig config = new WarChapterConfig();
			config.chapterId=buffer.getInt();
			config.chapterAward=buffer.getInt();
			
			WarChapterConfigCache.putConfig(config);
		}
	}
}
