package com.randioo.owlofwar_server_simplify_protobuf;

import com.randioo.owlofwar_server_simplify_protobuf.db.dao.CardDao;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.RoleDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.service.FightService;
import com.randioo.owlofwar_server_simplify_protobuf.module.match.service.MatchService;
import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.net.SpringContext;

/**
 * session关闭角色数据处理
 * 
 */
public class SessionCloseHandler {

	/**
	 * 移除session缓存
	 * 
	 * @param id
	 */
	public static void manipulate(Role role) {
		System.out.println("[account:" + role.getAccount() + ",name:" + role.getName() + "] manipulate");

		SessionCache.removeSessionById(role.getRoleId());

		FightService fightService = SpringContext.getBean("fightService");
		MatchService matchService = SpringContext.getBean("matchService");

		matchService.offline(role);
		fightService.offline(role);

		updateRoleData(role);
	}

	public static void updateRoleData(Role role) {
		try {
			RoleDao roleDao = (RoleDao) SpringContext.getBean("roleDao");
			CardDao cardDao = (CardDao) SpringContext.getBean("cardDao");

			for (Card x : role.getCardMap().values()) {
				if (x.isChange()) {
					cardDao.updateCard(x);
					x.setChange(false);
				}
			}

			if (role.isChange()) {
				roleDao.updateRole(role);
				role.setChange(false);
			}

			System.out.println("[account:" + role.getAccount() + " name:" + role.getName() + "] manipulate Success");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("role:" + role.getAccount() + " save error");
		}

	}
}
