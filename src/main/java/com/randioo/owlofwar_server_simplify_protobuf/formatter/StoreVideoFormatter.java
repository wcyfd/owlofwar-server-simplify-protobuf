package com.randioo.owlofwar_server_simplify_protobuf.formatter;

import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.StoreVideo;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Video;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResult;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.StoreFrames;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.StoreRoleResourceInfos;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.SCFightLoadResource;

public class StoreVideoFormatter {
	public static StoreVideo format(Video video) {
		StoreVideo storeVideo = new StoreVideo();
		StoreFrames storeFrames = StoreFrames.newBuilder().addAllFrames(video.getFrames()).build();
		storeVideo.setStoreFrames(storeFrames);

		for (SCFightLoadResource resource : video.getRoleResourceInfoMap()) {
			storeVideo.setStoreRoleResourceInfos(StoreRoleResourceInfos.newBuilder()
					.setMyPlayerId(resource.getMyPlayerId()).setIsNPCGame(resource.getIsNPCGame())
					.setNPCMapId(resource.getNPCMapId()).addAllRoleResourceInfo(resource.getRoleResourceInfoList())
					.build());
			break;
		}
		storeVideo.setGameId(video.getGameId());
		storeVideo.setStartTime(video.getStartTime());
		for (Map.Entry<Integer, GameResult> entrySet : video.getGameResultMap().entrySet()) {
			storeVideo.getGameResultMap().put(entrySet.getKey(), entrySet.getValue());
		}

		return storeVideo;
	}
}
