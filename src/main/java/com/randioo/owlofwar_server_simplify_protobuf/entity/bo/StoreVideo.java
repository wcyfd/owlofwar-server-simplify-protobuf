package com.randioo.owlofwar_server_simplify_protobuf.entity.bo;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResult;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.StoreFrames;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.StoreRoleResourceInfos;

public class StoreVideo {
	private int gameId;
	private long startTime;
	private byte[] storeRoleResourceInfosBytes;
	private StoreRoleResourceInfos storeRoleResourceInfos;
	private byte[] storeFramesBytes;
	private StoreFrames storeFrames;
	private String gameResultStr;
	private Map<Integer, GameResult> gameResultMap = new HashMap<>();

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public StoreRoleResourceInfos getStoreRoleResourceInfos() {
		return storeRoleResourceInfos;
	}

	public void setStoreRoleResourceInfos(StoreRoleResourceInfos storeRoleResourceInfos) {
		this.storeRoleResourceInfos = storeRoleResourceInfos;
	}

	public StoreFrames getStoreFrames() {
		return storeFrames;
	}

	public void setStoreFrames(StoreFrames storeFrames) {
		this.storeFrames = storeFrames;
	}

	public Map<Integer, GameResult> getGameResultMap() {
		return gameResultMap;
	}

	public String getGameResultStr() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, GameResult> entrySet : gameResultMap.entrySet()) {
			sb.append(entrySet.getKey()).append(",").append(entrySet.getValue().ordinal()).append(",");
		}
		gameResultStr = sb.toString();
		return gameResultStr;
	}

	public void setGameResultStr(String gameResultStr) {
		if (gameResultStr == null || gameResultStr.equals("")) {
			return;
		}
		gameResultMap.clear();
		String[] data = gameResultStr.split(",");
		int i = 0;
		while (i < data.length) {
			Integer roleId = Integer.parseInt(data[i++]);
			GameResult gameResult = GameResult.valueOf(Integer.parseInt(data[i++]));
			gameResultMap.put(roleId, gameResult);
		}
		this.gameResultStr = gameResultStr;
	}

	public byte[] getStoreFramesBytes() {
		byte[] bytes = new byte[0];
		if (storeFrames != null) {
			bytes = storeFrames.toByteArray();
		}
		storeFramesBytes = bytes;
		System.out.println(bytes);
		return storeFramesBytes;
	}

	public void setStoreFramesBytes(byte[] storeFramesBytes) {
		if (storeFramesBytes == null) {
			return;
		}
		this.storeFramesBytes = storeFramesBytes;

		try {
			this.storeFrames = StoreFrames.parseFrom(storeFramesBytes);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] getStoreRoleResourceInfosBytes() {
		byte[] bytes = new byte[0];
		if (storeRoleResourceInfos != null) {
			bytes = storeRoleResourceInfos.toByteArray();
		}
		storeRoleResourceInfosBytes = bytes;
		System.out.println(bytes);
		return storeRoleResourceInfosBytes;
	}

	public void setStoreRoleResourceInfosBytes(byte[] storeRoleResourceInfosBytes) {
		if (storeRoleResourceInfosBytes == null) {
			return;
		}
		this.storeRoleResourceInfosBytes = storeRoleResourceInfosBytes;

		try {
			this.storeRoleResourceInfos = StoreRoleResourceInfos.parseFrom(storeRoleResourceInfosBytes);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
