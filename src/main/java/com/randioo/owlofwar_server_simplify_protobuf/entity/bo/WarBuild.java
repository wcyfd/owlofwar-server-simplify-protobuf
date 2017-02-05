package com.randioo.owlofwar_server_simplify_protobuf.entity.bo;

import com.randioo.randioo_server_base.utils.db.Saveable;

public class WarBuild implements Saveable{
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
		setChange(true);
		this.starCount = starCount;
	}

	private boolean change;
	@Override
	public void setChange(boolean change) {
		// TODO Auto-generated method stub
		this.change = change;
	}

	@Override
	public boolean isChange() {
		// TODO Auto-generated method stub
		return change;
	}

	@Override
	public boolean checkChange() {
		// TODO Auto-generated method stub
		return false;
	}

}
