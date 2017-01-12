package com.randioo.owlofwar_server_simplify_protobuf.module.pillage.action;

import org.apache.mina.core.session.IoSession;

import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.pillage.service.PillageService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Pillage.PillageCompetitionNoticeRequest;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.net.ActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;

@PTAnnotation(PillageCompetitionNoticeRequest.class)
public class PillageCompetitionNoticeAction extends ActionSupport {
	private PillageService pillageService;

	public void setPillageService(PillageService pillageService) {
		this.pillageService = pillageService;
	}

	@Override
	public void execute(Object data, IoSession session) {
		PillageCompetitionNoticeRequest request = (PillageCompetitionNoticeRequest) data;
		Role role = (Role) RoleCache.getRoleBySession(session);
		pillageService.competitionNotice(role, request.getCompetitionId(), session);
	}
}
