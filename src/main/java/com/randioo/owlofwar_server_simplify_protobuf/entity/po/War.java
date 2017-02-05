package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

import java.util.HashMap;
import java.util.Map;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarBuild;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarChapter;

public class War {
	/** 玩家id */
	private int roleId;
	/** 章节信息 */
	private Map<Integer, WarChapter> warChapterMap = new HashMap<>();
	/** 建筑信息 */
	private Map<Integer, WarBuild> warBuildMap = new HashMap<>();

	private int marchBuildId;
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Map<Integer, WarBuild> getWarBuildMap() {
		return warBuildMap;
	}

	public Map<Integer, WarChapter> getWarChapterMap() {
		return warChapterMap;
	}

	public int getMarchBuildId() {
		return marchBuildId;
	}

	public void setMarchBuildId(int marchBuildId) {
		this.marchBuildId = marchBuildId;
	}

}
