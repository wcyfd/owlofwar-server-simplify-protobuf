package com.randioo.owlofwar_server_simplify_protobuf.module.login.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardInitConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.SessionCache;
import com.randioo.owlofwar_server_simplify_protobuf.common.ErrorCode;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.CardDao;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.RoleDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Card;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardList;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Video;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.VideoManager;
import com.randioo.owlofwar_server_simplify_protobuf.module.card.service.CardService;
import com.randioo.owlofwar_server_simplify_protobuf.module.login.LoginConstant;
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
import com.randioo.randioo_server_base.entity.Ref;
import com.randioo.randioo_server_base.module.login.LoginHandler;
import com.randioo.randioo_server_base.module.login.LoginModelServiceImpl;

public class LoginServiceImpl extends LoginModelServiceImpl implements LoginService {
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

	@Override
	public void init() {
		setLoginHandler(new LoginHandlerImpl());
	}

	private class LoginHandlerImpl implements LoginHandler<Role> {

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
		public Object checkCreateRoleAccount(Object createRoleMessage) {
			LoginCreateRoleRequest request = (LoginCreateRoleRequest) createRoleMessage;

			if (RoleCache.getNameSet().contains(request.getName())) {
				return SCMessage
						.newBuilder()
						.setLoginCreateRoleResponse(
								LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.NAME_IS_AREADY_HAS))
						.build();
			}

			if (RoleCache.getAccountSet().contains(request.getAccount())) { // 判定账号是否存在
				return SCMessage
						.newBuilder()
						.setLoginCreateRoleResponse(
								LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.ACCOUNT_ILLEGEL)).build();
			}
			return null;
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
			cardInit(role, conn);

			// 加入role缓存
			RoleCache.putNewRole(role);

			return SCMessage.newBuilder()
					.setLoginCreateRoleResponse(LoginCreateRoleResponse.newBuilder().setErrorCode(ErrorCode.SUCCESS))
					.build();
		}

		@Override
		public GeneratedMessage getRoleData(Ref<Role> ref) {
			Role role = ref.get();
			
			RoleData.Builder roleDataBuilder = RoleData.newBuilder().setRoleId(role.getRoleId())
					.setName(role.getName());
			for (Card card : role.getCardMap().values()) {
				roleDataBuilder.addCardDatas(CardData.newBuilder().setCardId(card.getCardId()).setLv(card.getLv())
						.setNum(card.getNum()));
			}
			Map<Integer,CardList> cardListMap = role.getCardListMap();
			for (CardList cardList : cardListMap.values()) {
				roleDataBuilder.addCardListDatas(CardListData.newBuilder().setMainId(cardList.getMainId())
						.setCardListIndex(cardList.getIndex()).addAllCardIds(cardList.getList()));
			}
			
			boolean isInFight = false;
			Video video = VideoManager.getVideoById(role.getOwlofwarGameId());
			if (video != null && video.getGameResultMap().get(role.getRoleId()) == null) {
				isInFight = true;
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
		public Object getRoleObjectFromCollectionsByGeneratedMessage(Ref<Role> ref, Object createRoleMessage) {
			LoginGetRoleDataRequest request = (LoginGetRoleDataRequest) createRoleMessage;
			String account = request.getAccount();
			Role role = RoleCache.getRoleByAccount(account);
			if (role == null) {
				 role = roleDao.getRoleByAccount(account);
				loginRoleModuleDataInit(role);
				if (role == null) {
					return SCMessage.newBuilder()
							.setLoginGetRoleDataResponse(LoginGetRoleDataResponse.newBuilder().setErrorCode(30103))
							.build();
				}
			}
			ref.set(role);

			return null;
		}

		@Override
		public String getIoSessionTag() {
			return "roleId";
		}

		@Override
		public Object getIoSessionAttributeValue(Ref<Role> ref) {
			return ref.get().getRoleId();
		}

		@Override
		public IoSession getSessionByRef(Ref<Role> ref) {
			IoSession session = SessionCache.getSessionById(ref.get().getRoleId());
			return session;
		}

		@Override
		public Object connectingError() {
			return SCMessage
					.newBuilder()
					.setLoginGetRoleDataResponse(LoginGetRoleDataResponse.newBuilder().setErrorCode(ErrorCode.IN_LOGIN))
					.build();
		}

		@Override
		public void recordSession(Ref<Role> ref, IoSession session) {
			SessionCache.addSession(ref.get().getRoleId(), session);
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
//		// 此时随机 完成卡组生成
//		for (int i = 1; i <= 3; i++) {
//			cardService.randomCardList(role, i);
//		}
		
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
	
	@Override
	public void loginRoleModuleDataInit(Role role){
		// 将数据库中的数据放入缓存中
		RoleCache.putRoleCache(role);

		// 初始化卡组信息
		List<Card> list = cardDao.getAllCardByRoleId(role.getRoleId());
		cardService.initCard(role, list);			
	}
}
