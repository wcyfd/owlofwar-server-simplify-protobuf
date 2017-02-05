package com.randioo.owlofwar_server_simplify_protobuf.module.fight;

public class FightConstant {
	/** 一局总时长 */
	public static int TOTAL_TIME = 4 * 60 * 1000;

	/** 战斗胜利 */
	public static final byte WIN = 1;
	/** 战斗失败 */
	public static final byte LOSS = -1;
	/** 战斗打平 */
	public static final byte DOGFALL = 0;
	public enum GameFightType{
		PILLAGE,WAR,TEST
	}
}
