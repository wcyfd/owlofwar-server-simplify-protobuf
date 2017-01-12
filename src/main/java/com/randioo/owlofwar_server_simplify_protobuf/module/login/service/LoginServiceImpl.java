package com.randioo.owlofwar_server_simplify_protobuf.module.login.service;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardInitConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.WarChapterConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.CardDao;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.RoleDao;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.StoreVideoDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardList;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Market;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.OwlofwarGame;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.module.login.LoginConstant;
import com.randioo.owlofwar_server_simplify_protobuf.module.market.service.MarketService;
import com.randioo.owlofwar_server_simplify_protobuf.module.role.service.RoleService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.CardData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.CardListData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Entity.RoleData;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginCheckAccountRequest;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginCheckAccountResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginCreateRoleRequest;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginCreateRoleResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginGetRoleDataRequest;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginGetRoleDataResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.owlofwar_server_simplify_protobuf.utils.TimeUtils;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.module.login.LoginHandler;
import com.randioo.randioo_server_base.module.login.LoginModelService;
import com.randioo.randioo_server_base.net.SpringContext;
import com.randioo.randioo_server_base.utils.sensitive.SensitiveWordDictionary;
import com.randioo.randioo_server_base.utils.system.SystemManager;
import com.randioo.randioo_server_base.utils.template.Ref;

public class LoginServiceImpl extends BaseService implements LoginService {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private RoleDao roleDao;

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	private CardDao cardDao;

	public void setCardDao(CardDao cardDao) {
		this.cardDao = cardDao;
	}

	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	private StoreVideoDao storeVideoDao;

	public void setStoreVideoDao(StoreVideoDao storeVideoDao) {
		this.storeVideoDao = storeVideoDao;
	}
	
	private MarketService marketService;
	public void setMarketService(MarketService marketService) {
		this.marketService = marketService;
	}
	
	private LoginModelService loginModelService;
	public void setLoginModelService(LoginModelService loginModelService) {
		this.loginModelService = loginModelService;
	}
	

	@Override
	public void init() {
		//初始化所有已经有过的帐号和昵称
		List<List> lists = roleDao.getAllAccounts$Names();
		for(List list:lists){
			RoleCache.getAccountSet().add((String) list.get(0));
			RoleCache.getNameSet().add((String) list.get(1));
		}
		
		// 为游戏id初始化
		int maxGameId = storeVideoDao.getMaxStoreVideoId();
		try {
			Field field = OwlofwarGame.class.getDeclaredField("id");
			field.setAccessible(true);
			field.setInt(null, maxGameId);
			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loginModelService.init();
		loginModelService.setLoginHandler(new LoginHandlerImpl());
	}

	private class LoginHandlerImpl implements LoginHandler {

		@Override
		public boolean checkLoginAccountCanLogin(String account, Ref<Object> canLoginErrorMessage) {
			SystemManager systemManager = SpringContext.getBean("systemManager");
			if (!systemManager.isService()) {
				canLoginErrorMessage.set(SCMessage
						.newBuilder()
						.setLoginCheckAccountResponse(
								LoginCheckAccountResponse.newBuilder().setErrorCode(ErrorCode.REJECT_LOGIN)).build());
				return false;
			}
			return true;
		}

		@Override
		public String getLoginAccount(Object loginMessage) {
			LoginCheckAccountRequest request = (LoginCheckAccountRequest) loginMessage;
			return request.getAccount();
		}

		@Override
		public GeneratedMessage isNewAccount(String account) {
			return SCMessage
					.newBuilder()
					.setLoginCheckAccountResponse(
							LoginCheckAccountResponse
									.newBuilder()
									.setErrorCode(
											RoleCache.getAccountSet().contains(account) ? ErrorCode.SUCCESS : ErrorCode.SHORT_TWO))
					.build();
		}

		@Override
		public String getCreateRoleAccount(Object createRoleMessage) {
			LoginCreateRoleRequest request = (LoginCreateRoleRequest) createRoleMessage;
			return request.getAccount();
		}

		@Override
		public boolean checkCreateRoleAccount(Object createRoleMessage, Ref<Object> checkCreateRoleAccountMessage) {
			LoginCreateRoleRequest request = (LoginCreateRoleRequest) createRoleMessage;

			String name = request.getName();
			
			//姓名不可重复
//			if (RoleCache.getNameSet().contains(name)) {
//				checkCreateRoleAccountMessage.set(SCMessage
//						.newBuilder()
//						.setLoginCreateRoleResponse(
//								LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.NAME_IS_AREADY_HAS))
//						.build());
//				return false;
//			}

			if (RoleCache.getAccountSet().contains(request.getAccount())) { // 判定账号是否存在
				checkCreateRoleAccountMessage.set(SCMessage
						.newBuilder()
						.setLoginCreateRoleResponse(
								LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.ACCOUNT_ILLEGEL)).build());
				return false;
			}
			
			if(SensitiveWordDictionary.containsSensitiveWord(name)){
				checkCreateRoleAccountMessage.set(SCMessage
						.newBuilder()
						.setLoginCreateRoleResponse(
								LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.NAME_SENSITIVE)).build());
				return false;
			}
			return true;
		}

		@Override
		public Connection getConnection() throws SQLException {

			return dataSource.getConnection();

		}

		@Override
		public Object createRole(Connection conn, Object createRoleMessage) {
			LoginCreateRoleRequest request = (LoginCreateRoleRequest) createRoleMessage;

			// 用户数据
			Role role = roleInit(request.getAccount(), conn, request.getName());
			//卡片初始化
			cardInit(role, conn);
			//商城初始化
			marketInit(role, conn);

			// 加入role缓存
			RoleCache.putNewRole(role);

			return SCMessage.newBuilder()
					.setLoginCreateRoleResponse(LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS))
					.build();
		}

		@Override
		public GeneratedMessage getRoleData(Ref<RoleInterface> ref) {
			Role role = (Role) ref.get();
			
			//知道最新进度的章节id
			List<Integer> chapterIdList= WarChapterConfigCache.getChapterIdList();
			int maxChapterId = WarChapterConfigCache.getMinChapterId();
			for(Integer chapterId:chapterIdList){
				if(role.getWar().getWarChapterMap().containsKey(chapterId)){
					maxChapterId = chapterId;
				}else{
					break;
				}
			}

			RoleData.Builder roleDataBuilder = RoleData.newBuilder().setRoleId(role.getRoleId())
					.setName(role.getName()).setCurrentChapterId(maxChapterId);
			for (Card card : role.getCardMap().values()) {
				roleDataBuilder.addCardDatas(CardData.newBuilder().setCardId(card.getCardId()).setLv(card.getLv())
						.setNum(card.getNum()));
			}
			Map<Integer, CardList> cardListMap = role.getCardListMap();
			for (CardList cardList : cardListMap.values()) {
				roleDataBuilder.addCardListDatas(CardListData.newBuilder().setMainId(cardList.getMainId())
						.setCardListIndex(cardList.getIndex()).addAllCardIds(cardList.getList()));
			}

			boolean isInFight = false;
			//是否正在比赛
			OwlofwarGame game = role.getOwlofwarGame();
			if (game != null) {
				if (!game.isEnd()) {
					game.getLock();
					try {
						game.getLock().lock();
						if (!game.isEnd()) {
							isInFight = true;
						}
					} catch (Exception e) {
						e.printStackTrace(); 
					} finally {
						game.getLock().unlock();
					}
				}
			}
			roleDataBuilder.setInFight(isInFight);

			return SCMessage
					.newBuilder()
					.setLoginGetRoleDataResponse(
							LoginGetRoleDataResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS)
									.setServerTime(TimeUtils.getNowTime()).setRoleData(roleDataBuilder)).build();
		}

		@Override
		public String getRoleDataAccount(Object createRoleMessage) {
			LoginGetRoleDataRequest request = (LoginGetRoleDataRequest) createRoleMessage;
			return request.getAccount();
		}

		@Override
		public boolean getRoleObject(Ref<RoleInterface> ref, Object createRoleMessage,
				Ref<Object> errorMessage) {
			LoginGetRoleDataRequest request = (LoginGetRoleDataRequest) createRoleMessage;
			Role role = getRoleByAccount(request.getAccount());
			if (role == null) {
				errorMessage
						.set(SCMessage.newBuilder()
								.setLoginGetRoleDataResponse(LoginGetRoleDataResponse.newBuilder().setErrorCode(30103))
								.build());
				return false;
			}

			ref.set(role);

			return true;
		}

		@Override
		public boolean connectingError(Ref<Object> errorConnectingMessage) {
			errorConnectingMessage
					.set(SCMessage
							.newBuilder()
							.setLoginGetRoleDataResponse(
									LoginGetRoleDataResponse.newBuilder().setErrorCode(ErrorCode.IN_LOGIN)).build());
			return false;
		}

	}

	/**
	 * 初始化用户数据
	 * 
	 * @param account
	 * @param conn
	 * @return
	 */
	private Role roleInit(String account, Connection conn, String name) {
		// 创建用户
		Role role = new Role();
		role.setAccount(account);

		// String name = this.getRandowName();
		role.setName(name);

		int id = roleDao.insertRole(role, conn);
		role.setRoleId(id);
		return role;
	}

	private void cardInit(Role role, Connection conn) {
		for (Integer x : CardInitConfigCache.getList()) {
			Card temp = new Card();
			temp.setLv((byte) 1);
			temp.setCardId(x);
			temp.setNum(1);
			temp.setRoleId(role.getRoleId());
			cardDao.insertCard(temp, conn);
			role.getCardMap().put(temp.getCardId(), temp);
		}
		// // 此时随机 完成卡组生成
		// for (int i = 1; i <= 3; i++) {
		// cardService.randomCardList(role, i);
		// }

		for (int i = 1; i <= 3; i++) {
			List<Integer> cardIntegerList = CardInitConfigCache.getList();
			CardList cardList = new CardList();
			cardList.setIndex(i);
			for (int j = 0; j < LoginConstant.CARDLIST_SIZE; j++) {
				if (j == 0) {
					cardList.setMainId(cardIntegerList.get(0));
				} else {
					cardList.getList().add(cardIntegerList.get(j));
				}
			}
			role.getCardListMap().put(i, cardList);
		}

		role.setUseCardsId(1);
	}
	
	private void marketInit(Role role, Connection conn) {
		int roleId = role.getRoleId();

		Market market = new Market();
		market.setRoleId(roleId);
		role.setMarket(market);
		int nowTime = TimeUtils.getNowTime();

		marketService.marketInit(role, nowTime);
//		marketDao.insertMarket(market, conn);
	}

	@Override
	public void loginRoleModuleDataInit(Role role) {
		// 将数据库中的数据放入缓存中
		RoleCache.putRoleCache(role);

		// 初始化卡组信息
		List<Card> list = cardDao.getAllCardByRoleId(role.getRoleId());
		cardService.initCard(role, list);
	}

	@Override
	public Object getRoleData(Object requestMessage, IoSession ioSession) {
		return loginModelService.getRoleData(requestMessage, ioSession);
	}

	@Override
	public Object creatRole(Object msg) {
		return loginModelService.creatRole(msg);
	}

	@Override
	public Object login(Object msg) {
		return loginModelService.login(msg);
	}

	@Override
	public Role getRoleById(int roleId) {
		Role role = (Role) RoleCache.getRoleById(roleId);
		if (role == null) {
			role = roleDao.getRoleById(roleId);
			if (role == null) 
				return role;
			this.loginRoleModuleDataInit(role);
		}
		return role;
	}

	@Override
	public Role getRoleByAccount(String account) {
		Role role = (Role) RoleCache.getRoleByAccount(account);
		if (role == null) {
			role = roleDao.getRoleByAccount(account);
			if (role == null)
				return role;
			this.loginRoleModuleDataInit(role);
		}

		return role;
	}
}
