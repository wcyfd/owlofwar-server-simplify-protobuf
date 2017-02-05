package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.FightConstant.GameFightType;

/**
 * 战斗监听器
 * 
 * @author wcy 2016年8月11日
 *
 */
public interface FightEventListener {

	String getAward();

	void setAward(String award);

	GameFightType getReturnType(Role role);

	Role getRole();

	int getAI();

	int getPalaceLv();

	void enterGame(Role role);

	void matchFighter(OwlofwarGame game, Role role);

	void readyFight(OwlofwarGame game, Role role1, Role role2);

	void startFight(OwlofwarGame game, Role role1, Role role2);

	void afterEnd(OwlofwarGame game, byte result);
}
