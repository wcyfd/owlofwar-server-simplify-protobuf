package com.randioo.owlofwar_server_simplify_protobuf.module.war.service;

import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.WarBuildConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.WarChapterConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.WarBuildDao;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.WarChapterDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarBuild;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.WarChapter;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightEventListener;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.FightEventListenerAdapter;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.War;
import com.randioo.owlofwar_server_simplify_protobuf.module.match.service.MatchService;
import com.randioo.owlofwar_server_simplify_protobuf.module.role.service.RoleService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResultAwardData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.GameResultData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.WarBuildData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.WarChapterData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarGetChapterAwardResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarMatchResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarShowWarBuildResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.War.WarShowWarChapterResponse;
import com.randioo.randioo_server_base.module.BaseService;

public class WarServiceImpl extends BaseService implements WarService {

	private WarBuildDao warBuildDao;

	public void setWarBuildDao(WarBuildDao warBuildDao) {
		this.warBuildDao = warBuildDao;
	}

	private WarChapterDao warChapterDao;

	public void setWarChapterDao(WarChapterDao warChapterDao) {
		this.warChapterDao = warChapterDao;
	}

	private MatchService matchService;

	public void setMatchService(MatchService matchService) {
		this.matchService = matchService;
	}

	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public GeneratedMessage showWarChapterInfo(Role role) {
		War war = role.getWar();
		// 章节列表
		Map<Integer, WarChapter> chapterMap = war.getWarChapterMap();
		// 建筑列表
		Map<Integer, WarBuild> warBuildMap = war.getWarBuildMap();
		WarShowWarChapterResponse.Builder warShowWarChapterResponseBuilder = WarShowWarChapterResponse.newBuilder();
		for (WarChapter warChapter : chapterMap.values()) {
			int chapterId = warChapter.getChapterId();
			// 根据章节获得建筑列表
			List<Integer> warBuildIdList = this.getWarBuildByChapterId(chapterId);
			int totalStar = 0;
			for (Integer warBuildId : warBuildIdList) {
				WarBuild warBuild = warBuildMap.get(warBuildId);
				// 如果没有打过该建筑，或该建筑的星数为零则跳过
				if (warBuild == null || warBuild.getStarCount() == 0)
					continue;
				int starCount = warBuild.getStarCount();
				totalStar += starCount;
			}
			WarChapterData warChapterData = WarChapterData.newBuilder().setChapterId(warChapter.getChapterId())
					.setStarCount(totalStar).build();
			warShowWarChapterResponseBuilder.addWarChapterData(warChapterData);
		}
		return SCMessage.newBuilder().setWarShowWarChapterResponse(warShowWarChapterResponseBuilder).build();
	}

	@Override
	public GeneratedMessage showWarBuildInfo(Role role, int chapterId) {
		War war = role.getWar();
		Map<Integer, WarBuild> warBuildMap = war.getWarBuildMap();
		List<Integer> warBuildIdList = this.getWarBuildByChapterId(chapterId);
		WarShowWarBuildResponse.Builder warShowWarBuildResponseBuilder = WarShowWarBuildResponse.newBuilder();
		for (Integer buildId : warBuildIdList) {
			WarBuild warBuild = warBuildMap.get(buildId);
			// 如果没有打过该建筑，或该建筑的星数为零则跳过
			if (warBuild == null || warBuild.getStarCount() == 0)
				continue;
			WarBuildData warBuildData = WarBuildData.newBuilder().setStarCount(warBuild.getStarCount())
					.setBuildId(buildId).build();
			warShowWarBuildResponseBuilder.addWarBuildData(warBuildData);
		}

		return SCMessage.newBuilder().setWarShowWarBuildResponse(warShowWarBuildResponseBuilder).build();
	}

	@Override
	public void march(Role role, int buildId, IoSession session) {
		int chapterId = this.getChapterIdByBuildId(buildId);
		if (!this.checkBuild(role, chapterId, buildId)) {
			session.write(SCMessage.newBuilder()
					.setWarMatchResponse(WarMatchResponse.newBuilder().setErrorCode(ErrorCode.NO_MARCH_TARGET)).build());
			return;
		}

		Map<Integer, WarChapter> warChapterMap = role.getWar().getWarChapterMap();

		// 如果没有这个章节，则新建
		WarChapter warChapter = warChapterMap.get(chapterId);
		if (warChapter == null) {
			warChapter = new WarChapter();
			warChapter.setAward(0);
			warChapter.setChapterId(chapterId);
			warChapter.setRoleId(role.getRoleId());
			// 插入数据库
			warChapterDao.insertWarChapter(warChapter);

			warChapterMap.put(chapterId, warChapter);
		}

		Map<Integer, WarBuild> warBuildMap = role.getWar().getWarBuildMap();
		// 如果没有这个建筑则加入
		WarBuild warBuild = warBuildMap.get(buildId);
		if (warBuild == null) {
			warBuild = new WarBuild();
			warBuild.setBuildId(buildId);
			warBuild.setRoleId(role.getRoleId());
			warBuild.setStarCount(0);
			// 插入数据库
			warBuildDao.insertWarBuild(warBuild);

			warBuildMap.put(buildId, warBuild);
		}

		final int npcTeam = this.getNPCTeam(buildId);
		FightEventListener listener = new FightEventListenerAdapter(role) {
			@Override
			public int getAI() {
				return npcTeam;
			}
		};
		session.write(SCMessage.newBuilder()
				.setWarMatchResponse(WarMatchResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)).build());

		matchService.matchRole(session, role, true, listener);
	}

	private boolean checkBuild(Role role, int chapterId, int buildId) {
		// 检查前一章节
		if (!this.checkChapter(role, chapterId)) {
			return false;
		}

		// 检查前一建筑
		List<Integer> buildIdList = this.getWarBuildByChapterId(chapterId);
		// 获得该建筑的索引
		int index = buildIdList.indexOf(buildId);
		// 如果是第一个值，则给予通过，否则就要检查前面一个建筑是否获得至少一星
		if (index > 0) {
			int formerIndex = index - 1;
			int formerBuildId = buildIdList.get(formerIndex);
			WarBuild formerWarBuild = role.getWar().getWarBuildMap().get(formerBuildId);
			if (formerWarBuild == null || formerWarBuild.getStarCount() == 0) {
				return false;
			}
		}

		return true;
	}

	private boolean checkChapter(Role role, int chapterId) {
		List<Integer> chapterIdList = this.getChapterId();
		int index = chapterIdList.indexOf(chapterId);
		// 如果是第一章则通过
		if (index == 0) {
			return true;
		}
		int formerId = chapterIdList.get(index - 1);
		WarChapter warChapter = role.getWar().getWarChapterMap().get(formerId);
		// 如果没有前一章数据或前一章没有打完则返回false
		// 如果没有前一章数据
		if (warChapter == null) {
			return false;
		}

		// 检查前一章最后一个建筑是否获得至少一星
		List<Integer> warBuildIdList = getWarBuildByChapterId(formerId);
		int warBuildId = warBuildIdList.get(warBuildIdList.size() - 1);
		WarBuild warBuild = role.getWar().getWarBuildMap().get(warBuildId);
		if (warBuild == null || warBuild.getStarCount() == 0) {
			return false;
		}

		return true;
	}

	@Override
	public void refreshWarBuild(Role role, GameResultData gameResultData,
			GameResultAwardData.Builder gameResultAwardDataBuilder) {
		War war = role.getWar();
		int buildId = gameResultData.getBuildId();
		int starCount = gameResultData.getStarCount();
		WarBuild warBuild = war.getWarBuildMap().get(buildId);
		if (warBuild.getStarCount() == 0 && starCount != 0) {
			if (warBuild.getStarCount() < starCount) {
				warBuild.setStarCount(starCount);
			}
			int money = this.getWarBuildWinMoney(buildId);
			gameResultAwardDataBuilder.setMoney(money);
			
			roleService.addMoney(role, money);
		}
	}

	@Override
	public GeneratedMessage getChapterAward(Role role, int chapterId) {
		War war = role.getWar();
		WarChapter warChapter = war.getWarChapterMap().get(chapterId);

		if (warChapter.getAward() != 0) {
			return SCMessage
					.newBuilder()
					.setWarGetChapterAwardResponse(
							WarGetChapterAwardResponse.newBuilder().setErrorCode(ErrorCode.REPEAT_GET_AWARD)).build();
		}
		boolean allow = true;

		List<Integer> warBuildIdList = this.getWarBuildByChapterId(chapterId);
		for (Integer buildId : warBuildIdList) {
			WarBuild warBuild = role.getWar().getWarBuildMap().get(buildId);
			// 有建筑是空或者没有满星,则不能领取
			if (warBuild == null || warBuild.getStarCount() != this.getWarBuildFullStar(buildId)) {
				allow = false;
			}
		}
		if (!allow) {
			return SCMessage
					.newBuilder()
					.setWarGetChapterAwardResponse(
							WarGetChapterAwardResponse.newBuilder()
									.setErrorCode(ErrorCode.CHAPTER_AWARD_NEED_STAR_FULL)).build();
		}
		warChapter.setAward(1);
		int money = this.getChapterWinMoney(chapterId);
		roleService.addMoney(role, money);
		
		return SCMessage.newBuilder().setWarGetChapterAwardResponse(WarGetChapterAwardResponse.newBuilder()).build();
	}

	/**
	 * 根据章节获得建筑id列表
	 * 
	 * @param chapterId
	 * @return
	 * @author wcy 2017年1月10日
	 */
	private List<Integer> getWarBuildByChapterId(int chapterId) {		
		return WarBuildConfigCache.getBuildIdListByChapterId(chapterId);
	}

	private List<Integer> getChapterId() {
		return WarChapterConfigCache.getChapterIdList();
	}

	private int getChapterIdByBuildId(int buildId) {
		return WarBuildConfigCache.getWarBuildConfigByChapterId(buildId).chapterId;
	}

	private int getNPCTeam(int buildId) {
		return WarBuildConfigCache.getWarBuildConfigByChapterId(buildId).npcTeam;
	}

	private int getWarBuildWinMoney(int buildId) {
		return WarBuildConfigCache.getWarBuildConfigByBuildId(buildId).award;
	}

	private int getChapterWinMoney(int chapterId) {
		return WarChapterConfigCache.getChapterById(chapterId).chapterAward;
	}

	private int getWarBuildFullStar(int buildId) {
		return 3;
	}
}
