package com.randioo.owlofwar_server_simplify_protobuf.module.pillage.service;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightEventListener;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightEventListenerAdapter;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightVideo;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGame;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.module.match.service.MatchService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Pillage.PillageCancelResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Pillage.PillageRoleResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Pillage.PillageShowResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.randioo_server_base.module.BaseService;

public class PillageServiceImpl extends BaseService implements PillageService {

	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	private MatchService matchService;

	public void setMatchService(MatchService matchService) {
		this.matchService = matchService;
	}

	/**
	 * 点击 匹配按钮
	 */
	@Override
	public void choose(Role role, boolean isAI, IoSession session) {		
		if(role.getOwlofwarGame() !=null){
			session.write(SCMessage.newBuilder()
					.setPillageRoleResponse(PillageRoleResponse.newBuilder().setErrorCode(ErrorCode.IN_THE_ROOM)).build());
			return;
		}
		session.write(SCMessage.newBuilder()
				.setPillageRoleResponse(PillageRoleResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)).build());

		FightEventListener listener = new FightEventListenerAdapter(role) {

			@Override
			public void matchFighter(OwlofwarGame game, Role role) {
				// role.setPlayTime(role.getPlayTime() - 1);

				// int moneyCount = getRoleMoney(getRole());
				// Resource res = new Resource();
				// res.setItemId(0);
				// res.setNum(moneyCount);
				// res.setResType(IncomeConstant.ITEM_TYPE_MONEY);
				// String award = GameUtils.formatResources(res);
				// setAward(award);
			}

			@Override
			public void afterEnd(OwlofwarGame game, byte result) {

				if (!game.isAIGame()) {
					// int nowTime = TimeUtils.getNowTime();
					//
					// Map<Integer, Integer> roleScoreMap = game.getScoreMap();
					// FightMailData data =
					// game.getRoleGameInfoMap().get(getRole().getRoleId()).getFightMailData();
					// int roleScore = roleScoreMap.get(data.getRoleId());
					// int enemyScore = roleScoreMap.get(data.getEnemyId());
					//
					// data.setEnemyScore(enemyScore);
					// data.setRoleScore(roleScore);
					// data.setFightId(video.getVideoId());

				}

				// Set<Resource> resources =
				// GameUtils.formatResouces(this.getAward());
				// for (Resource resource : resources) {
				// int num = resource.getNum();
				// if (result == FightConstant.WIN) {
				// resource.setNum(Math.abs(num));
				// incomeService.award(getRole(),
				// GameUtils.formatResources(resources));
				// } else if (result == FightConstant.LOSS) {
				// resource.setNum(-Math.abs(num));
				// incomeService.award(getRole(),
				// GameUtils.formatResources(resources));
				// }
				//
				// }
			}

			@Override
			public int getAI() {
				return this.getAIMapIdByPoint();
			}

		};

		// RoomCache.addRoom(role, isAI, listener);
		matchService.matchRole(session, role, isAI, listener);

	}

	@Override
	public void cancelChoose(Role role, IoSession session) {
		session.write(SCMessage.newBuilder()
				.setPillageCancelResponse(PillageCancelResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)).build());
		matchService.cancelMatch(role);
	}

	@Override
	public GeneratedMessage showMatchingInfo(Role role) {
		return SCMessage.newBuilder()
				.setPillageShowResponse(PillageShowResponse.newBuilder().setPoint(role.getPoint())).build();
	}

}
