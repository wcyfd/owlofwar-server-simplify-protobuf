package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.HashMap;
import java.util.Map;

import com.randioo.randioo_server_base.utils.game.matcher.MatchInfo;

public class OwlofwarMatchInfo extends MatchInfo {
	private Map<Integer, FightEventListener> fightEventListeners = new HashMap<>();
	private boolean isAIGame;
	private int aiMapsId;

	public Map<Integer, FightEventListener> getFightEventListeners() {
		return fightEventListeners;
	}

	public boolean isAIGame() {
		return isAIGame;
	}

	public void setAIGame(boolean isAIGame) {
		this.isAIGame = isAIGame;
	}

	public int getAiMapsId() {
		return aiMapsId;
	}

	public void setAiMapsId(int aiMapsId) {
		this.aiMapsId = aiMapsId;
	}
}
