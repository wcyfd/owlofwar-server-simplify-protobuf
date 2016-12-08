package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.Frame;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResult;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.SCFightLoadResource;

public class Video {
	private int gameId;
	/** 游戏帧 */
	private List<Frame> frames = new ArrayList<>();
	/** 资源信息表 */
	private Map<Integer, SCFightLoadResource> roleResourceInfoMap = new HashMap<>();
	/** 游戏结局 */
	private Map<Integer, GameResult> gameResultMap = new HashMap<>();

	public List<Frame> getFrames() {
		return frames;
	}

	public Map<Integer, SCFightLoadResource> getRoleResourceInfoMap() {
		return roleResourceInfoMap;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public Map<Integer, GameResult> getGameResultMap() {
		return gameResultMap;
	}

}
