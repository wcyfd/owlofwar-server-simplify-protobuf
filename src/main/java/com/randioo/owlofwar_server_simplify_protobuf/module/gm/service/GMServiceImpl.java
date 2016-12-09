package com.randioo.owlofwar_server_simplify_protobuf.module.gm.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.SessionCloseHandler;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.SessionCache;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.StoreVideoDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.StoreVideo;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Video;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.VideoManager;
import com.randioo.owlofwar_server_simplify_protobuf.formatter.StoreVideoFormatter;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.GM.GMOpenLoginResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.GM.GMRejectLoginResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.GM.GMTerminatedServerResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ServerMessage.SCMessage;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.net.DataSource;
import com.randioo.randioo_server_base.utils.system.ServerManagerHandler;
import com.randioo.randioo_server_base.utils.system.SystemManager;

public class GMServiceImpl extends BaseService implements GMService {

	private SystemManager systemManager;

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private StoreVideoDao storeVideoDao;

	public void setStoreVideoDao(StoreVideoDao storeVideoDao) {
		this.storeVideoDao = storeVideoDao;
	}

	@Override
	public void init() {
		systemManager.setServerTerminatedHandler(new ServerManagerHandler() {

			@Override
			public void terminated() {
				everybodyOffline();
				saveVideo();
			}

			@Override
			public void close() {
				everybodyOffline();
			}

			@Override
			public void open() {

			}
		});
	}

	@Override
	public GeneratedMessage rejectLogin(String code) {
		if (!systemManager.checkPassword(code)) {
			return SCMessage.newBuilder()
					.setGmRejectLoginResponse(GMRejectLoginResponse.newBuilder().setErrorCode(false)).build();
		}
		systemManager.close();

		return SCMessage.newBuilder().setGmRejectLoginResponse(GMRejectLoginResponse.newBuilder().setErrorCode(true))
				.build();

	}

	@Override
	public GeneratedMessage openLogin(String code) {
		if (!systemManager.checkPassword(code)) {
			return SCMessage.newBuilder().setGmOpenLoginResponse(GMOpenLoginResponse.newBuilder().setErrorCode(false))
					.build();
		}
		systemManager.open();

		return SCMessage.newBuilder().setGmOpenLoginResponse(GMOpenLoginResponse.newBuilder().setErrorCode(true))
				.build();

	}

	@Override
	public GeneratedMessage terminatedServer(String code) {
		if (!systemManager.checkPassword(code)) {
			return SCMessage.newBuilder()
					.setGmTerminatedServerResponse(GMTerminatedServerResponse.newBuilder().setErrorCode(false)).build();
		}

		systemManager.terminated();

		return SCMessage.newBuilder()
				.setGmTerminatedServerResponse(GMTerminatedServerResponse.newBuilder().setErrorCode(true)).build();
	}

	/**
	 * 所有人下线
	 * 
	 * @author wcy 2016年12月9日
	 */
	private void everybodyOffline() {
		// 所有人下线
		Collection<IoSession> allSession = SessionCache.getAllSession();
		Iterator<IoSession> it = allSession.iterator();
		while (it.hasNext()) {
			it.next().close(true);
		}

		for (Role role : RoleCache.getRoleMap().values()) {
			try {
				SessionCloseHandler.manipulate(role);
			} catch (Exception e) {
				System.out.println("Role: " + role.getRoleId() + " saveError!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存记录
	 * 
	 * @author wcy 2016年12月9日
	 */
	private void saveVideo() {
		try (Connection conn = dataSource.getConnection()) {
			for (Video video : VideoManager.getAllVideo().values()) {
				try {
					StoreVideo storeVideo = StoreVideoFormatter.format(video);
					storeVideoDao.insertStoreVideo(storeVideo, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
