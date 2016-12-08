package com.randioo.owlofwar_server_simplify_protobuf.module.fight.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.SessionCache;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardList;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightEventListener;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.MailCard;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.MailCardList;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGame;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGameInfo;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Video;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.VideoManager;
import com.randioo.owlofwar_server_simplify_protobuf.formatter.MailCardListFormatter;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.FightConstant;
import com.randioo.owlofwar_server_simplify_protobuf.module.match.service.MatchService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.FightCard;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.Frame;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResult;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.RoleResourceInfo;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightClientReadyResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightGameActionResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightGameOverResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightGetGameAwardResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightLoadResourceCompleteResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.FightReadFrameResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.SCFightGameOver;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.SCFightKeyFrame;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.SCFightLoadResource;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Fight.SCFightStartGame;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Game.GameAction;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.randioo_server_base.entity.GameEvent;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.net.SpringContext;
import com.randioo.randioo_server_base.utils.game.game_type.GameBase;
import com.randioo.randioo_server_base.utils.game.game_type.GameHandler;
import com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game.RTSGame;
import com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game.RTSGameStarter;

public class FightServiceImpl extends BaseService implements FightService {

	private RTSGameStarter gameStarter;

	public void setGameStarter(RTSGameStarter gameStarter) {
		this.gameStarter = gameStarter;
	}

	@Override
	public void init() {
		gameStarter.setGameHandler(new GameHandler() {

			@Override
			public void gameLogic(GameBase gameBase) {
				OwlofwarGame game = (OwlofwarGame) gameBase;
				if (game.isEnd()) {
					return;
				}
				Lock lock = game.getLock();
				try {
					lock.lock();
					if (game.isEnd() || game.getFrameNumber() > game.getTotalTime() * game.getFrameCountInOneSecond()) {
						game.setEnd(true);
						game.getScheduledFuture().cancel(true);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
				sendKeyFrameInfo(game);
			}
		});
	}

	@Override
	public void clientReady(Role role, IoSession session) {
		OwlofwarGame game = role.getOwlofwarGame();
		if (game == null || game.isEnd()) {
			session.write(SCMessage
					.newBuilder()
					.setFightClientReadyResponse(
							FightClientReadyResponse.newBuilder().setErrorCode(ErrorCode.ERR_CANCEL_ROOM)).build());
			return;
		}

		session.write(SCMessage.newBuilder()
				.setFightClientReadyResponse(FightClientReadyResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS))
				.build());

		Lock lock = game.getLock();
		lock.lock();
		try {
			if (this.checkSomeoneOffline(game)) {
				return;
			}
			game.getReadyRoleIdSet().add(role.getRoleId());
			try {
				// 如果玩家都准备好了，则开始加载资源
				if (game.getReadyRoleIdSet().size() + game.getAiCount() >= game.getRoleMap().size()) {
					this.loadResource(game);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void loadResource(OwlofwarGame game) {
		List<Integer> roleIdList = game.getRolePositionList();
		Map<Integer, String> cardListStrMap = new HashMap<>();
		Map<Integer, MailCardList> mailCardListMap = new HashMap<>();
		for (Role role : game.getRoleMap().values()) {
			MailCardList mailCardList = this.getUseCardList(role);
			cardListStrMap.put(role.getRoleId(), MailCardListFormatter.formatRandomMaiLCardList(mailCardList));
			mailCardListMap.put(role.getRoleId(), mailCardList);
		}
		for (Integer roleId : roleIdList) {
			SCFightLoadResource.Builder builder = SCFightLoadResource.newBuilder()
					.setIsNPCGame(game.isAIGame()).setNPCMapId(game.getAiMapsId());
			for (int i = 0, size = roleIdList.size(); i < size; i++) {
				if (roleId == roleIdList.get(i)) {
					builder.setMyPlayerId(i + 1);
				}

				Role role = game.getRoleMap().get(roleIdList.get(i));
				RoleResourceInfo.Builder roleResourceInfoBuilder = RoleResourceInfo.newBuilder()
						.setName(role.getName()).setFightPoint(0).setCorpName("").setMainLv(1).setPlayerId(i + 1)
						.setCampId(i + 1);
				MailCard mainCard = mailCardListMap.get(roleIdList.get(i)).getMainCard();
				roleResourceInfoBuilder.setGeneralCard(FightCard.newBuilder().setCardId(mainCard.getCardId())
						.setLv(mainCard.getLv()));
				List<MailCard> list = mailCardListMap.get(roleIdList.get(i)).getList();
				for (int index = 1; index < list.size(); index++) {
					MailCard mailCard = list.get(index);
					roleResourceInfoBuilder.addHandCards(FightCard.newBuilder().setCardId(mailCard.getCardId())
							.setLv(mailCard.getLv()));
				}

				builder.addRoleResourceInfo(roleResourceInfoBuilder);
			}

			SCFightLoadResource scFightLoadResource = builder.build();
			
			/** 添加录像 */
			Video video = new Video();
			video.setGameId(game.getGameId());
			game.setVideo(video);
			VideoManager.addVideo(game.getVideo());
			
			// 放入资源表
			game.getVideo().getRoleResourceInfoMap().put(roleId, scFightLoadResource);

			IoSession session = SessionCache.getSessionById(roleId);
			if (session != null) {
				session.write(SCMessage.newBuilder().setScFightLoadResource(scFightLoadResource)
						.build());
			}
		}
	}

	/**
	 * 获得使用中的卡组
	 * 
	 * @param role
	 * @return
	 */
	private MailCardList getUseCardList(Role role) {

		CardList cardList = null;
		Map<Integer, Card> cardMap = null;
		MailCardList mailCardList = null;

		if (role.getRoleId() != 0) {
			// 如果不是npc
			int index = role.getUseCardsId();
			cardList = role.getCardListMap().get(index);
			cardMap = role.getCardMap();
			mailCardList = new MailCardList(cardList, cardMap);
		} else {
			// 如果是npc
			String str = "16106,1,16101,1,16102,1,16209,1,16105,1,16108,1,16301,1,16206,1,16107,1,16301,1,16103,1,16302,1";
			mailCardList = new MailCardList(str);
		}

		return mailCardList;
	}

	@Override
	public void loadResourceComplete(Role role, IoSession session) {
		session.write(SCMessage.newBuilder().setFightLoadResourceCompleteResponse(FightLoadResourceCompleteResponse.newBuilder())
				.build());

		OwlofwarGame owlofwarGame = role.getOwlofwarGame();
		if (owlofwarGame == null || role.getOwlofwarGame().isEnd()) {
			return;
		}
		Lock lock = owlofwarGame.getLock();
		try {
			lock.lock();
			owlofwarGame = role.getOwlofwarGame();
			if (owlofwarGame == null || role.getOwlofwarGame().isEnd())
				return;

			this.startGame(owlofwarGame);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	private void startGame(OwlofwarGame game) {
		System.out.println("function startGame()");
		Lock lock = game.getLock();
		if (game.isStart())
			return;
		if (lock.tryLock()) {
			try {
				if (game.isStart())
					return;
				game.setStart(true);

			} finally {
				lock.unlock();
			}
		}

		System.out.println("startGame");
		// int roleId1 = game.getRolePositionList().get(0);
		// int roleId2 = game.getRolePositionList().get(1);
		// Role role1 = game.getRoleMap().get(roleId1);
		// Role role2 = game.getRoleMap().get(roleId2);
		// FightEventListener listener1 =
		// game.getRoleGameInfoMap().get(roleId1).getListener();
		// FightEventListener listener2 =
		// game.getRoleGameInfoMap().get(roleId2).getListener();
		//
		// // 准备战斗邮件信息
		// listener1.readyFight(game, role1, role2);
		// if (listener2 != null)
		// listener2.readyFight(game, role2, role1);
		//
		// listener1.startFight(game, role1, role2);
		// if (listener2 != null)
		// listener2.startFight(game, role2, role1);

		for (Role role : game.getRoleMap().values()) {
			int roleId = role.getRoleId();
			Role otherRole = game.getAnotherRole(role);
			FightEventListener listener = game.getRoleGameInfoMap().get(roleId).getListener();
			if (listener != null) {
				listener.startFight(game, role, otherRole);
			}
		}

		for (Role x : game.getRoleMap().values()) {
			IoSession ioSession = SessionCache.getSessionById(x.getRoleId());
			if (ioSession != null) {
				ioSession.write(SCMessage.newBuilder()
						.setScFightStartGame(SCFightStartGame.newBuilder().setTotalTime(game.getTotalTime())).build());
			}
		}

		game.setAddDeltaFrame(2);
		game.setFrameCountInOneSecond(20);
		int addDeltaFrame = game.getAddDeltaFrame();
		int keyFrameDeltaTime = 1000/((OwlofwarGame)game).getFrameCountInOneSecond()*addDeltaFrame;
		
		gameStarter.startRTSGame(game,keyFrameDeltaTime);

	}

	@Override
	public void receiveGameAction(Role role, GeneratedMessage gameActionMessage) {
		OwlofwarGame game = (OwlofwarGame) role.getOwlofwarGame();
		if (game == null || game.isEnd()) {
			return;
		}
		try {
			SCMessage sc = SCMessage.newBuilder()
					.setFightGameActionResponse(FightGameActionResponse.newBuilder()).build();
			IoSession session = SessionCache.getSessionById(role.getRoleId());
			session.write(sc);

			int currentFrame = this.getCurrentFrameIndex(game);

			// 将事件和当前的帧数加入到队列中，延迟执行帧数目前设为0
			game.getActionQueue().add(gameActionMessage, currentFrame, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getCurrentFrameIndex(OwlofwarGame game) {
		long startTime = game.getStartTime();
		long nowTime = System.currentTimeMillis();
		// 获得游戏开始到现在的时间毫秒间隔
		long deltaTime = nowTime - startTime;
		// 获得每帧的需要的毫秒数量
		long oneFrameMilliSecond = 1000 / game.getFrameCountInOneSecond();
		// 毫秒间隔除以每帧毫秒数量去整获得第几帧
		int currentFrame = (int) (deltaTime / oneFrameMilliSecond);

		return currentFrame;
	}

	@Override
	public void sendKeyFrameInfo(RTSGame game) {
		List<GameEvent> gameEvents = game.getActionQueue().pollAll(game.getNextFrameNumber());

		// 整合消息
		SCFightKeyFrame.Builder scFightKeyFrameBuilder = SCFightKeyFrame.newBuilder();

		for (int frame = game.getFrameNumber(), nextFrameNumber = game.getNextFrameNumber(); frame < nextFrameNumber; frame++) {
			Frame.Builder frameBuilder = Frame.newBuilder();
			frameBuilder.setFrameIndex(frame);
			for (int j = 0, gameEventSize = gameEvents.size(); j < gameEventSize; j++) {
				GameEvent gameEvent = gameEvents.get(j);
				int frameIndex = gameEvent.getExecuteFrameIndex();
				if (frameIndex == frame) {
					GameAction gameAction = (GameAction) gameEvent.getAction();
					frameBuilder.addGameActions(gameAction);
				}
			}
			Frame f = frameBuilder.build();
			scFightKeyFrameBuilder.addFrames(f);

			// 记录帧
			((OwlofwarGame) game).getVideo().getFrames().add(f);
			// for(OwlofwarGameInfo gameInfo
			// :((OwlofwarGame)game).getRoleGameInfoMap().values()){
			// gameInfo.getRunQueue().add(f);
			// }
		}

		game.setFrameNumber(game.getNextFrameNumber());
		game.setNextFrameNumber(game.getNextFrameNumber() + ((OwlofwarGame) game).getAddDeltaFrame());

		SCMessage sc = SCMessage.newBuilder().setScFightKeyFrame(scFightKeyFrameBuilder).build();
		// 向各用户发送消息
		for (Role role : ((OwlofwarGame) game).getRoleMap().values()) {
			IoSession session = SessionCache.getSessionById(role.getRoleId());
			if (session != null)
				session.write(sc);
		}
	}

	@Override
	public GeneratedMessage readFrames(Role role) {
		OwlofwarGame game = role.getOwlofwarGame();
		List<Frame> allFrames = game.getVideo().getFrames();

		FightReadFrameResponse.Builder builder = FightReadFrameResponse.newBuilder();
		for (Frame frame : allFrames) {
			builder.addFrames(frame);
		}
		return SCMessage.newBuilder().setFightReadFrameResponse(builder).build();
	}

	@Override
	public void receiveGameEnd(Role role, IoSession session) {
		session.write(SCMessage.newBuilder().setFightGameOverResponse(FightGameOverResponse.newBuilder()).build());
		OwlofwarGame game = role.getOwlofwarGame();
		if (game == null || game.isEnd()) {
			return;
		}
		Lock lock = game.getLock();
		lock.lock();
		try {
			if (game.isEnd())
				return;
			game.setEnd(true);
			game.getScheduledFuture().cancel(true);
		} finally {
			lock.unlock();
		}

		// this.sendEnd(game, role, result, score1, score2);
	}

	@Override
	public GeneratedMessage getGameAward(Role role, GameResult result, int score) {
		// Video video = VideoManager.getVideoById(gameId);
		// if (video != null) {
		// SCFightLoadResourceResponse info =
		// video.getRoleResourceInfoMap().get(role.getRoleId());
		// int index = info.getIndex();
		// //获得另一个玩家的信息
		// RoleResourceInfo roleResourceInfo = null;
		// for(int i = 0;i<info.getRoleResourceInfoList().size();i++){
		// if(index!=i){
		// roleResourceInfo = info.getRoleResourceInfo(i);
		// break;
		// }
		// }
		//
		// int palaceLv = roleResourceInfo.getPalaceLv();
		// }

		int point = 0;
		switch (result) {
		case WIN:
			point = 20;
			break;
		case LOSS:
			point = -20;
			break;
		case DOGFALL:
			point = 0;
			break;
		default:
			break;
		}

		return SCMessage.newBuilder()
				.setFightGetGameAwardResponse(FightGetGameAwardResponse.newBuilder().setPoint(point)).build();
	}

	private void sendEnd(OwlofwarGame game, Role role, byte result, int roleScore1, int roleScore2) {
		System.out.println("function sendEnd()");
		if (this.decideResultByOnlyOneRole(game, role, result, roleScore1, roleScore2)) {
			System.out.println("receiveEnd result is:" + game.getResultMap() + " score is" + game.getScoreMap());
			Map<Integer, Byte> resultMap = game.getResultMap();
			Map<Role, Byte> roleResultMap = new HashMap<>();
			for (Role tempRole : game.getRoleMap().values()) {

				roleResultMap.put(tempRole, resultMap.get(tempRole.getRoleId()));
				IoSession ioSession = SessionCache.getSessionById(tempRole.getRoleId());
				FightEventListener listener = game.getRoleGameInfoMap().get(tempRole.getRoleId()).getListener();
				if (ioSession != null) {
					String award = "";
					Byte tempResult = resultMap.get(tempRole.getRoleId());
					if (tempResult == FightConstant.WIN) {
						if (listener != null) {
							award = listener.getAward();
						}
					}
					ioSession.write(SCMessage.newBuilder()
							.setScFightGameOver(SCFightGameOver.newBuilder().setAward(award).setResult(tempResult))
							.build());
				}
			}

			// if (!game.isAIGame()) {
			// int roleId1 = game.getRolePositionList().get(0);
			// int roleId2 = game.getRolePositionList().get(1);
			// Role role1 = game.getRoleMap().get(roleId1);
			// Role role2 = game.getRoleMap().get(roleId2);
			// OwlofwarGameInfo info1 = game.getRoleGameInfoMap().get(roleId1);
			// OwlofwarGameInfo info2 = game.getRoleGameInfoMap().get(roleId2);
			//
			// }
		}

	}

	private boolean decideResultByOnlyOneRole(OwlofwarGame game, Role role, byte result, int roleScore1, int roleScore2) {
		System.out.println("decideResultByOnlyOneRole");
		System.out.println("roleScore1:" + roleScore1 + " roleScore2:" + roleScore2);
		if (game == null || game.isEnd())
			return false;

		Lock lock = game.getLock();
		lock.lock();
		try {
			if (game == null || game.isEnd())
				return false;
			// 当游戏结束时就应该获得结果,前提是两者完全同步的情况下,所以只要进入过一次，剩下的所有进入都算返回false
			game.getScheduledFuture().cancel(true);
			game.setEnd(true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		Role role2 = game.getAnotherRole(role);
		try {
			if (role != null) {
				// 如果玩家发送结果
				// 获得双方的通信接口
				IoSession session = SessionCache.getSessionById(role.getRoleId());

				// 赋予结果，score是比分，result是胜负

				if (session == null || !session.isConnected()) {
					// 自己掉线了，则判定对面赢
					this.checkCorrectResult(game, role2);
				} else {
					game.getScoreMap().put(role.getRoleId(), roleScore1);
					game.getScoreMap().put(role2.getRoleId(), roleScore2);
					game.getResultMap().put(role.getRoleId(), result);
					game.getResultMap().put(role2.getRoleId(), (byte) -result);
				}

			} else {
				// 如果是游戏时间到了
				int roleId1 = game.getRolePositionList().get(0);
				int roleId2 = game.getRolePositionList().get(1);
				// 获取收集到的分数信息
				OwlofwarGameInfo info1 = game.getRoleGameInfoMap().get(roleId1);
				OwlofwarGameInfo info2 = game.getRoleGameInfoMap().get(roleId2);
				Map<Integer, Integer> role1ScoreInfo = info1.getScoreMap();
				Map<Integer, Integer> role2ScoreInfo = info2.getScoreMap();

				if (game.isAIGame()) {// 如果是npc战斗
					int score1 = this.nullThen0(role1ScoreInfo, roleId1);
					int score2 = this.nullThen0(role1ScoreInfo, roleId2);
					if (score1 > score2) {
						result = FightConstant.WIN;
					} else if (score1 == score2) {
						result = FightConstant.DOGFALL;
					} else {
						result = FightConstant.LOSS;
					}
					game.getScoreMap().put(roleId1, score1);
					game.getScoreMap().put(roleId2, score2);
					game.getResultMap().put(roleId1, result);
					game.getResultMap().put(roleId2, (byte) -result);
				} else {
					// 如果不是npc战斗
					int role1Score1 = this.nullThen0(role1ScoreInfo, roleId1);
					int role1Score2 = this.nullThen0(role1ScoreInfo, roleId2);
					int role2Score1 = this.nullThen0(role2ScoreInfo, roleId1);
					int role2Score2 = this.nullThen0(role2ScoreInfo, roleId2);
					if (role1Score1 != role2Score1 || role1Score2 != role2Score2) {
						this.checkCorrectResult(game, role);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	private int nullThen0(Map<Integer, Integer> map, int key) {
		Integer value = map.get(key);
		if (value == null) {
			value = 0;
			map.put(key, value);
		}
		return value;
	}

	// private void directReceiveResult(OwlofwarGame game, Role role) {
	// Map<Integer, Byte> receiveResultMap = game.getReceiveResultMap();
	// Map<Integer, Byte> resultMap = game.getResultMap();
	// for (Entry<Integer, Byte> entrySet : receiveResultMap.entrySet()) {
	// resultMap.put(entrySet.getKey(), entrySet.getValue());
	// }
	// // win loss or dogfall
	// Map<Integer, Integer> scoreMap =
	// game.getRoleGameInfoMap().get(role.getRoleId()).getScoreMap();
	// for (Map.Entry<Integer, Integer> entrySet : scoreMap.entrySet()) {
	// game.getScoreMap().put(entrySet.getKey(), entrySet.getValue());
	// }
	// }

	private void checkCorrectResult(OwlofwarGame game, Role role) {
		// this.directReceiveResult(game, role);
		System.out.println("hasCalculate");
	}

	@Override
	public void offlineCancelRoom(Role role) {
		MatchService matchService = SpringContext.getBean("matchService");
		// 先取消匹配
		matchService.cancelMatch(role);

		OwlofwarGame game = role.getOwlofwarGame();
		if (game == null || game.isEnd()) {
			return;
		}
		Lock lock = game.getLock();
		try {
			lock.lock();
			if (role.getOwlofwarGame() == null || game.isEnd()) {
				return;
			}

			if (game.isStart()) {
				// 如果游戏开始了则发送结果
				this.sendEnd(game, role, (byte) -1, 0, 0);
			} else {
				// 游戏还没有开始，就下线，则直接赋值成空
				game.setEnd(true);
				if (game.getScheduledFuture() != null) {
					game.getScheduledFuture().cancel(true);
				}
				for (Role r : game.getRoleMap().values()) {
					r.setOwlofwarGame(null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 检查是否有人下线
	 * 
	 * @param game
	 * @return
	 * @author wcy 2016年12月7日
	 */
	private boolean checkSomeoneOffline(OwlofwarGame game) {
		boolean hasOffline = false;
		Lock lock = game.getLock();
		try {
			if (game == null || game.isEnd()) {
				return true;
			}
			lock.lock();
			if (game == null || game.isEnd()) {
				return true;
			}
			// 如果有人已经离线了，则直接返回
			Map<Integer, Role> roleMap = game.getRoleMap();
			for (Role r : roleMap.values()) {
				if (r.getOwlofwarGame() == null) {
					hasOffline = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return hasOffline;
	}

}
