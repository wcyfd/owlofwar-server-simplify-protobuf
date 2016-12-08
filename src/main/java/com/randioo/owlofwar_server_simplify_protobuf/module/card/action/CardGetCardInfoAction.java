package com.randioo.owlofwar_server_simplify_protobuf.module.card.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardGetCardsInfoRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(CardGetCardsInfoRequest.class)
public class CardGetCardInfoAction extends ActionSupport {
	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		CardGetCardsInfoRequest request = (CardGetCardsInfoRequest) data;
		Role role = RoleCache.getRoleBySession(session);
		GeneratedMessage message = cardService.getInfoCards(role);
		if(message!=null){
			session.write(message);
		}
	}
}
