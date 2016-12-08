package com.randioo.owlofwar_server_simplify_protobuf.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.db.converter.RoleConverter;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.RoleDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.randioo_server_base.db.access.DataAccess;
import com.randioo.randioo_server_base.db.converter.IntegerConverter;

public class RoleDaoImpl extends DataAccess implements RoleDao{
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	public int insertRole(Role role, Connection conn) {
		int id = 0;
		final String sql = "insert into role(id,account,name,money,gold,food,useCardsId,listStr)"
				+ "values(?,?,?,?,?,?,?,?)";
		try {
			id = super.insertNotCloseConn(sql, new IntegerConverter() , conn,null,role.getAccount(),role.getName(),
					role.getMoney(),role.getGold(),role.getFood(),role.getUseCardsId(),role.getListStr());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	@Override
	public void updateRole(Role role) {
		final String sql = "update role set name=?,money=?,gold=?,food=?,listStr=?,useCardsId=?,point=?"
				+ " where id=? limit 1";
		try {
			Connection conn = dataSource.getConnection();
			super.update(sql, conn, role.getName(),role.getMoney(),role.getGold(),role.getFood()
					,role.getListStr(),role.getUseCardsId(),role.getPoint()
					,role.getRoleId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void serverStartInit() {
		try {
			Connection conn = dataSource.getConnection();
			List<Role> resultTmep = super.queryForList(
							"select * from role",new RoleConverter(), conn);
			if(resultTmep.size() > 0){
				for(Role startRole : resultTmep){
					RoleCache.serverInit(startRole);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Role> getAllRole() {
//		int id = 0;
//		final String sql = "insert into role(id,name,account,lv,exp,money,gold,prestige,food,exploit,"
//				+ "vipLv,alreadyGetVipAward,armyToken,useFormationID,maxBagNum,"
//				+ "roleScience,legionContribution,lastGetArmyToken,faceId,raidTimes,population,populationLimit,buildQueue,roleScienceQueue,"
//				+ "registerIp,registerTime,armsResearchStr,rank,iconUnlock,fightValue,leadPoint,leadStr,formationStr,worldFormation,rankRecordTime,"
//				+ "villageDefStr,villageMineFarmWorldArmyId,villageMineFarmStartTime,villageNation,worldDoublePStr)"
//				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		try {
//			id = super.insertNotCloseConn(sql, new IntegerConverter() , conn,null,role.getName(),role.getAccount();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}
	
	public Role getRoleByAccount(String account){
		final String sql = "select * from role where account=? ";
		try {
			Connection conn = dataSource.getConnection();
			Role result = super.queryForObject(sql, new RoleConverter() , conn, account);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Role getRoleById(int id){
		final String sql = "select * from role where id=?";
		try {
			Connection conn = dataSource.getConnection();
			Role result = super.queryForObject(sql, new RoleConverter() , conn, id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
