package com.randioo.owlofwar_server_simplify_protobuf.entity.file;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.po.EventElement;

public class CardConfig {
	/** ID */
	private int id;
	private String name;
	/** 品质 */
	private byte quality;
	/** 类型 */
	private byte type;
	/** 建造速度 */
	private int makeSpeed;
	/** 可上场数量 */
	private int useTime;
	/** 放置延迟 */
	private double useDealy;
	/** 解锁需要时间 */
	private int unlockNeedTime;
	/** 解锁需要武将数量 */
	private int unlockNeedHeroCount;
	/** 解锁需要商才 */
	private int unlockTrade;
	/** 解锁需要学识 */
	private int unlockLearn;
	/** 解锁需要体魄 */
	private int unlockForce;
	/** 解锁需要粮草 */
	private int unlockNeedFood;
	/** 解锁需要银币 */
	private int unlockNeedMoney;
	/** 解锁需要材料 */
	private String unlockNeedRes;
	/** 事件表 */
	private Map<Byte, EventElement> eventMap = new HashMap<>();

	private Map<Byte, Map<String, Double>> levelAttributeMap = new HashMap<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public int getMakeSpeed() {
		return makeSpeed;
	}

	public void setMakeSpeed(int makeSpeed) {
		this.makeSpeed = makeSpeed;
	}

	public int getUseTime() {
		return useTime;
	}

	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}

	public double getUseDealy() {
		return useDealy;
	}

	public void setUseDealy(double useDealy) {
		this.useDealy = useDealy;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getUnlockNeedTime() {
		return unlockNeedTime;
	}

	public void setUnlockNeedTime(int unlockNeedTime) {
		this.unlockNeedTime = unlockNeedTime;
	}

	public int getUnlockNeedHeroCount() {
		return unlockNeedHeroCount;
	}

	public void setUnlockNeedHeroCount(int unlockNeedHeroCount) {
		this.unlockNeedHeroCount = unlockNeedHeroCount;
	}

	public int getUnlockTrade() {
		return unlockTrade;
	}

	public void setUnlockTrade(int unlockTrade) {
		this.unlockTrade = unlockTrade;
	}

	public int getUnlockLearn() {
		return unlockLearn;
	}

	public void setUnlockLearn(int unlockLearn) {
		this.unlockLearn = unlockLearn;
	}

	public int getUnlockForce() {
		return unlockForce;
	}

	public void setUnlockForce(int unlockForce) {
		this.unlockForce = unlockForce;
	}

	public int getUnlockNeedFood() {
		return unlockNeedFood;
	}

	public void setUnlockNeedFood(int unlockNeedFood) {
		this.unlockNeedFood = unlockNeedFood;
	}

	public int getUnlockNeedMoney() {
		return unlockNeedMoney;
	}

	public void setUnlockNeedMoney(int unlockNeedMoney) {
		this.unlockNeedMoney = unlockNeedMoney;
	}

	public String getUnlockNeedRes() {
		return unlockNeedRes;
	}

	public void setUnlockNeedRes(String unlockNeedRes) {
		this.unlockNeedRes = unlockNeedRes;
	}

	public Map<Byte, EventElement> getEventMap() {
		return eventMap;
	}

	public Map<Byte, Map<String, Double>> getLevelAttributeMap() {
		return levelAttributeMap;
	}

}
