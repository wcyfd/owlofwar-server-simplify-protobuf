package com.randioo.owlofwar_server_simplify_protobuf.module.market.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

/**
 * 
 * @author AIM
 *
 */
public interface MarketService extends BaseServiceInterface {
	/**
	 * 商城初始化使用
	 * 
	 * @param role
	 */
	public void marketInit(Role role, int nowTime);

	/**
	 * 刷新商城信息
	 * 
	 * @param role
	 * @param nowTime 当前时间
	 * @return 是否刷新成功
	 * @author wcy 2017年1月9日
	 */
	void refreshMarketItem(Role role, int nowTime);

	/**
	 * 人工刷新
	 * @param role
	 * @return
	 * @author wcy 2017年1月9日
	 */
	GeneratedMessage artificalRefreshMarketItem(Role role);
	/**
	 * 显示商品
	 * 
	 * @param role
	 * @return
	 */
	GeneratedMessage showMarketItem(Role role);

	/**
	 * 购买商品
	 * 
	 * @param role
	 * @param id
	 * @return
	 */
	GeneratedMessage buyMarketItem(Role role, IoSession session, int index);

}
