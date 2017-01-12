package com.randioo.owlofwar_server_simplify_protobuf.module.market.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.market.service.MarketService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Market.MarketBuyMarketItemRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(MarketBuyMarketItemRequest.class)
public class MarketBuyMarketItemAction extends ActionSupport {
	
	private MarketService marketService;
	public void setMarketService(MarketService marketService) {
		this.marketService = marketService;
	}
	@Override
	public void execute(Object data, IoSession session) {
		MarketBuyMarketItemRequest request = (MarketBuyMarketItemRequest)data;
		Role role = (Role) RoleCache.getRoleBySession(session);
		GeneratedMessage message = marketService.buyMarketItem(role, session, request.getIndex());
		if(message !=null){
			session.write(message);
		}
	}
}
