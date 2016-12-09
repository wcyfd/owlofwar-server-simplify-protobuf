package com.randioo.owlofwar_server_simplify_protobuf.utils;

import com.randioo.randioo_server_base.utils.system.SystemManager;

public class DefaultSystemManager extends SystemManager {

	@Override
	public String createCode() {
		return RandomUtils.randowStr(16);
	}

	@Override
	public boolean checkCode(String origin, String code) {
		return true;
	}

}
