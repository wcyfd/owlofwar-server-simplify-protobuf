package com.randioo.owlofwar_server_simplify_protobuf.module.login.service;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.randioo_server_base.module.login.LoginModelService;

public interface LoginService extends LoginModelService{

	/**
	 * 初始化数据
	 * @param role
	 * @author wcy 2016年12月7日
	 */
	public void loginRoleModuleDataInit(Role role);

}
