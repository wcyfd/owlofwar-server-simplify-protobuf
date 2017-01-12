package com.randioo.owlofwar_server_simplify_protobuf.entity.bo;

public class WarBuild {
	/** 玩家id */
	private int roleId;
	/** 建筑id */
	private int buildId;
	/** 星数 */
	private int starCount;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getBuildId() {
		return buildId;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public int getStarCount() {
		return starCount;
	}

	public void setStarCount(int starCount) {
		this.starCount = starCount;
	}

}
