package com.randioo.owlofwar_server_simplify_protobuf.cache.local;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;


public class RoleCache {
	/** 所有玩家缓存信息		*/
	private static ConcurrentMap<Integer, Role> roleMap = new ConcurrentHashMap<Integer, Role>();
	private static ConcurrentMap<String, Role> roleAccountMap = new ConcurrentHashMap<String, Role>();
	/** 姓名集合 				*/
	private static Set<String> nameSet = new HashSet<>();
	/** 登录账号集合 			*/
	private static Set<String> accountSet = new HashSet<>();
	
	/**
	 * 根据id获取角色缓存
	 * @param id
	 * @return
	 */
	public static Role getRoleById(int id){
		return getRoleMap().get(id);
	}
	
	public static Role getRoleBySession(IoSession ioSession){
		try {
			Integer roleId = (Integer) ioSession.getAttribute("roleId");
			if (roleId == null)
				return null;
			return getRoleMap().get(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 更具账号获取角色缓存
	 * @param name
	 * @return
	 */
	public static Role getRoleByAccount(String account){
		return roleAccountMap.get(account);
	}
	
	public static Set<String> getNameSet() {
		return nameSet;
	}
	public static void setNameSet(Set<String> nameSet) {
		RoleCache.nameSet = nameSet;
	}
	public static Set<String> getAccountSet() {
		return accountSet;
	}
	public static void setAccountSet(Set<String> accountSet) {
		RoleCache.accountSet = accountSet;
	}
	public static ConcurrentMap<Integer, Role> getRoleMap() {
		return roleMap;
	}
	public static void setRoleMap(ConcurrentMap<Integer, Role> roleMap) {
		RoleCache.roleMap = roleMap;
	}
	
	public static synchronized void putNewRole(Role role)
	{
		accountSet.add(role.getAccount());
		nameSet.add(role.getName());
		roleMap.put(role.getRoleId(), role);
		roleAccountMap.put(role.getAccount(), role);
	}
	
	public static void putRoleCache(Role role)
	{
		roleMap.put(role.getRoleId(), role);
		roleAccountMap.put(role.getAccount(), role);
	}
	
	public static void serverInit(Role role)
	{
		accountSet.add(role.getAccount());
		nameSet.add(role.getName());
	}
	public static ConcurrentMap<String, Role> getRoleAccountMap() {
		return roleAccountMap;
	}
	public static void setRoleAccountMap(ConcurrentMap<String, Role> roleAccountMap) {
		RoleCache.roleAccountMap = roleAccountMap;
	}
}	
