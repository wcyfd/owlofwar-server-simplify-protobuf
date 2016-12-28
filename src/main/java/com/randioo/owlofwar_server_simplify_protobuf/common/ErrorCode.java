package com.randioo.owlofwar_server_simplify_protobuf.common;

/**
 * 错误代码
 * 
 * @author xjd
 *
 */
public class ErrorCode {

	/** 成功 */
	public static final short SUCCESS = 1;
	/** 没有用户 */
	public static final short SHORT_TWO = 2;

	/** 角色名字重复 */
	public static final short NAME_IS_AREADY_HAS = 30101;
	/** 没有该卡 */
	public static final short NO_CARD = 30102;
	/** 没有勋章 */
	public static final short NO_MEDAL = 30103;
	/** 没有银币 */
	public static final short NO_MONEY = 30104;
	/** 没有粮草 */
	public static final short NO_FOOD = 30105;
	/** 没有金币 */
	public static final short NO_GOLD = 30106;
	/** 没有rmb */
	public static final short NO_RMB = 30107;
	/** 玩家名称不合法 */
	public static final short NAME_NOT_ILLEGEL = 30108;
	/** 名字太长 */
	public static final short NAME_TOO_LONG = 30109;
	/** 录像已经过期 */
	public static final short VIDEO_OUT_OF_DATE = 30110;
	/** 拒绝登陆 */
	public static final short REJECT_LOGIN = 30111;
	/** 帐号不合法，只能是英文字母 */
	public static final short ACCOUNT_ILLEGEL = 30112;
	/** 正在登录中 */
	public static final short IN_LOGIN = 30113;
	/** 玩家名有敏感字 */
	public static final short NAME_SENSITIVE = 30114;

	/** 无效的放置 */
	public static final short ERR_PUT_LOC = 30201;
	/** 无效的取消 */
	public static final short ERR_CANCEL_ROOM = 30202;
	/** 最大匹配次数 */
	public static final short MAX_MATCHING_COUNT = 30203;
	

	/**
	 * 战斗
	 */
	/** 已经确定战斗结果 */
	public static final short HAS_RESULT = 30301;
	/** 战斗过于频繁请稍候 */
	public static final short IN_THE_ROOM = 30302;
	/**
	 * 卡组
	 */
	/** 只有英雄可以作为主将 */
	public static final short ERR_TYPE_MAIN_ID = 30401;
	/** 下标越界 */
	public static final short OUT_INDEX_OF = 30402;
	/** 不能重复放置英雄卡 */
	public static final short ERR_NUM_CARD_H = 30403;
	/** 超过该卡片类型 */
	public static final short ERR_NUM_CARD_N = 30404;
	/** 创建战场卡组失败 */
	public static final short CREATE_WAR_CARDLIST_FAILED = 30405;
	/** 最高卡片等级 */
	public static final short MAX_CARD_LV = 30406;
	/** 学识不够 */
	public static final short LACK_LEARN = 30407;
	/** 商才不够 */
	public static final short LACK_TRADE = 30408;
	/** 体魄不够 */
	public static final short LACK_FORCE = 30409;

	/**
	 * 酒馆
	 */
	/** 只有武将可以寻访 */
	public static final short ONLY_HERO = 30501;
	/** 不能重复派遣 */
	public static final short CAN_NOT_VISIT = 30502;
	/** 时间未到 */
	public static final short NO_TIME = 30503;
	/** 地区错误 */
	public static final short ERR_AREA = 30504;
	/** 酒馆等级不足 */
	public static final short NO_PUB_LV = 30505;

	/**
	 * 战场
	 */
	/** 已经在战役中 */
	public static final short BATTLE_IN_BATTLE = 30601;
	/** 没有足够的武将进入战场 */
	public static final short NO_HERO_ENTER_WAR = 30602;
	/** 超出战场次数 */
	public static final short OUT_COUNT_OF_WAR = 30603;
	/** 没有该卡组 */
	public static final short NO_WAR_CARDLIST = 30604;
	/** 没有军饷 */
	public static final short NO_WAR_MONEY = 30605;
	/** 没有军粮 */
	public static final short NO_WAR_FOOD = 30606;
	/** 行军失败 */
	public static final short NO_MARCH_TARGET = 30607;
	/** 已经有部队驻扎 */
	public static final short HAS_ARMY = 30608;
	/** 不在手牌 */
	public static final short NOT_IN_HAND = 30609;
	/** 卡牌已经在手中 */
	public static final short WAR_CARDLIST_ALREADY_IN_HAND = 30610;
	/** 部队已经死亡 */
	public static final short WAR_CARDLIST_DEAD = 30611;
	/** 超出最大卡组 */
	public static final short MAX_WAR_CARDLIST = 30612;
	/**
	 * 公会
	 */
	/** 已经有公会 */
	public static final short HAS_LEGION = 30801;
	/** 最大公会人数 */
	public static final short LEGION_MAX_MEMBER_COUNT = 30802;
	/** 没有公会 */
	public static final short NO_LEGION = 30803;
	/** 没有解散公会的权利 */
	public static final short NO_DISMISS_PRIVILEGE = 30804;
	/** 不能退出 */
	public static final short EXIT_FAILED = 30805;
	/** 不能加入公会 */
	public static final short JOIN_FAILED = 30806;
	/** 没有审批权限 */
	public static final short NO_AGREE_PRIVILEGE = 30807;
	/** 没有踢人的权限 */
	public static final short NO_KICK_PRIVILEGE = 30808;
	/** 不能踢自己 */
	public static final short KICK_MYSELF = 30809;
	/** 没有提升或降低的权限 */
	public static final short NO_PRIVILEGE_PRIVILEGE = 30810;
	/** 不是公会成员 */
	public static final short NOT_LEGION_MEMBER = 30811;
	/** 不能改变会长的权限 */
	public static final short LEGION_CAPTAIN = 30812;
	/** 最大副会长数量 */
	public static final short MAX_DEPUTY_CAPTAIN = 30813;
	/** 最大长老数量 */
	public static final short MAX_ELDER = 30814;
	/** 提升权限失败 */
	public static final short UP_PRIVILEGE_FAILED = 30815;
	/** 公会名不合法 */
	public static final short LEGION_NAME_ILLEGAL = 30816;
	/** 不是公会会长 */
	public static final short NOT_LEGION_CAPTAIN = 30817;
	/** 重复申请公会 */
	public static final short REPEAT_APPLY_LEGION = 30818;
	/** 公会拒绝加入 */
	public static final short REJECT_JOIN = 30819;
	/** 玩家已经取消申请 */
	public static final short CANCEL_APPLY = 30820;
	/** 积分不足 */
	public static final short POINT_NOT_ENOUGH = 30821;
	/** 没有聊天权限 */
	public static final short NO_CHAT_PRIVILEGE = 30822;
	/** 没有切磋权限 */
	public static final short NO_FIGHT_PRIVILEGE = 30823;
	/** 已经切磋 */
	public static final short HAS_FIGHT = 30824;
	/** 不能与自己开战 */
	public static final short FIGHT_WITH_YOUR_SELF = 30825;
	/** 公会名称重复 */
	public static final short LEGION_NAME_REPEAT = 30826;
	/** 超出最大描述信息 */
	public static final short OUT_OF_LENGTH = 30827;
	/** 切磋时间已过期 */
	public static final short CHAT_INFO_OUT_OF_DATE = 30828;

	/**
	 * 道具
	 */
	/** 道具不足 */
	public static final short NO_PROP = 30901;

	/**
	 * 商城
	 */
	/** 商城项目超过每日购买次数 */
	public static final short MARKET_ITEM_OUT_OF_DAY_BUY_COUNT = 31001;
	/** 超出最大上限 */
	public static final short MAX_CAPACITY = 31002;
	/**
	 * 经营
	 */
	/** 最大建筑等级 */
	public static final short MAX_BUILD_LV = 31101;
	/** 英雄缺少 */
	public static final short HERO_COUNT_ERROR = 31102;
	/** 事件已经完成 */
	public static final short GAME_EVENT_COMPLETE = 31103;
	/** 已经有事件 */
	public static final short HAS_GAME_EVENT = 31104;
	/** 武将在事件中 */
	public static final short HERO_IN_GAME_EVENT = 31105;
	/** 建筑等级不能大于皇宫 */
	public static final short LV_OUT_OF_PALACE = 31106;
	/** 正在进行研发 */
	public static final short IN_TECH_RESEARCH = 31107;
	/** 正在进行寻访 */
	public static final short IN_PUB_VISIT = 31108;
	/** 正在进行酒馆升级 */
	public static final short IN_PUB_LV = 31109;
	/** 正在进行研究所升级 */
	public static final short IN_TECH_LV = 31110;
	/** 重复领取奖励 */
	public static final short REPEAT_GET_AWARD = 31111;

	/**
	 * 挂机
	 */
	/** 该英雄正在挂机 */
	public static final short HERO_TRAINING = 31301;

	/**
	 * 邮箱
	 */
	/** 没有邮件 */
	public static final short NO_MAIL = 31401;

	/** 正在执行重要操作 */
	public static final short RUNNING_IMPORTANT_OPERATION = 32001;
	/** 服务器关闭 */
	public static final short SERVICE_CLOSED = 32002;
	/** 密码错误 */
	public static final short GM_CODE_ERROR = 32003;

}
