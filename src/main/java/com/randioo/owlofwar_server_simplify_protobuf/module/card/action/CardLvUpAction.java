package com.randioo.owlofwar_server_simplify_protobuf.module.card.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardLvUpRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(CardLvUpRequest.class)
public class CardLvUpAction extends ActionSupport {
	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		CardLvUpRequest request = (CardLvUpRequest) data;
		Role role = RoleCache.getRoleBySession(session);
		GeneratedMessage message = cardService.cardLvUp(role, request.getCardId());
		if(message!=null){
			session.write(message);
		}
	}
}
