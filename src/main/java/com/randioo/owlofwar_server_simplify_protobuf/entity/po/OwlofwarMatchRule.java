package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import com.randioo.randioo_server_base.utils.game.matcher.MatchRule;

public class OwlofwarMatchRule extends MatchRule {
	private FightEventListener fightEventListener;

	public FightEventListener getFightEventListener() {
		return fightEventListener;
	}

	public void setFightEventListener(FightEventListener fightEventListener) {
		this.fightEventListener = fightEventListener;
	}

}
