package com.randioo.owlofwar_server_simplify_protobuf.module.card.action;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Card.CardChooseUseCardListRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(CardChooseUseCardListRequest.class)
public class CardChooseCardListAction extends ActionSupport {
	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		CardChooseUseCardListRequest request = (CardChooseUseCardListRequest) data;
		Role role = (Role)RoleCache.getRoleBySession(session);
		GeneratedMessage message = cardService.chooseUseCardList(role, request.getIndex());
		if (message != null) {
			session.write(message);
		}
	}
}
