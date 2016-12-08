package com.randioo.owlofwar_server_simplify_protobuf.module.card.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardChangeMainCardRequest;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(CardChangeMainCardRequest.class)
public class CardChangeHeroAction extends ActionSupport {
	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		CardChangeMainCardRequest request = (CardChangeMainCardRequest) data;
		Role role = RoleCache.getRoleBySession(session);
		GeneratedMessage message = cardService.changeHero(role, request.getCardListIndex(), request.getCardId());
		if (message != null) {
			session.write(message);
		}
	}
}
