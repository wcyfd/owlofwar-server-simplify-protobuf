package com.randioo.owlofwar_server_simplify_protobuf.entity.bo;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.match.MatchInfo;
import com.randioo.randioo_server_base.module.match.Matchable;
import com.randioo.randioo_server_base.utils.db.Saveable;

public abstract class GameRole implements RoleInterface, Matchable ,Saveable{

	protected MatchInfo matchInfo;
	protected boolean isNPC;

	@Override
	public void setMatchInfo(MatchInfo matchInfo) {
		// TODO Auto-generated method stub
		this.matchInfo = matchInfo;
	}

	@Override
	public MatchInfo getMatchInfo() {
		// TODO Auto-generated method stub
		return matchInfo;
	}

	@Override
	public void setNpc(boolean isNpc) {
		// TODO Auto-generated method stub
		this.isNPC = isNpc;
	}

	@Override
	public boolean isNPC() {
		// TODO Auto-generated method stub
		return isNPC;
	}

	protected String account;
	protected String name;
	protected int roleId;

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return account;
	}

	@Override
	public void setAccount(String account) {
		// TODO Auto-generated method stub
		this.account = account;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public int getRoleId() {
		// TODO Auto-generated method stub
		return roleId;
	}

	@Override
	public void setRoleId(int roleId) {
		// TODO Auto-generated method stub
		this.roleId = roleId;
	}

	protected boolean change;
	
	@Override
	public void setChange(boolean change) {
		// TODO Auto-generated method stub
		this.change = change;
	}

	@Override
	public boolean isChange() {
		// TODO Auto-generated method stub
		if(!change){
			change = checkChange();
		}		
		return change;
	}

	@Override
	public abstract boolean checkChange() ;

}
