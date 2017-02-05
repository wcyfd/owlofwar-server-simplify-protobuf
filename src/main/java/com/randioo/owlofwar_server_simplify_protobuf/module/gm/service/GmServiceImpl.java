package com.randioo.owlofwar_server_simplify_protobuf.module.gm.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.owlofwar_server_simplify_protobuf.SessionCloseHandler;
import com.randioo.owlofwar_server_simplify_protobuf.db.dao.StoreVideoDao;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.StoreVideo;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.Video;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.VideoManager;
import com.randioo.owlofwar_server_simplify_protobuf.formatter.StoreVideoFormatter;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmOpenLoginResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmRejectLoginResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.GM.GmTerminatedServerResponse;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.gm.ServerGMMessage.SCGMMessage;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.utils.system.Platform;
import com.randioo.randioo_server_base.utils.system.ServerManagerHandler;
import com.randioo.randioo_server_base.utils.system.SignalTrigger;
import com.randioo.randioo_server_base.utils.system.SystemManager;
import com.randioo.randioo_server_base.utils.system.Platform.OS;
import com.randioo.randioo_server_base.utils.template.Function;

public class GmServiceImpl extends BaseService implements GmService {

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
		
		Function function = new Function(){

			@Override
			public Object apply(Object... params) {
				systemManager.close();
				
				System.out.println("port close");	
				System.out.println("start save");			
				
				everybodyOffline();
				saveVideo();
				
				System.out.println("save complete");
				
				System.exit(0);
				return null;
			}
			
		};
		
		//命令关闭信号
		try {
			System.out.println(Platform.getOS());
			if (Platform.getOS() == OS.WIN)
				SignalTrigger.setSignCallback("INT", function);
			else if (Platform.getOS() == OS.LINUX)
				SignalTrigger.setSignCallback("ABRT", function);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public GeneratedMessage rejectLogin(String code) {
		if (!systemManager.checkPassword(code)) {
			return SCGMMessage.newBuilder()
					.setGmRejectLoginResponse(GmRejectLoginResponse.newBuilder().setErrorCode(false)).build();
		}
		systemManager.close();

		return SCGMMessage.newBuilder().setGmRejectLoginResponse(GmRejectLoginResponse.newBuilder().setErrorCode(true))
				.build();

	}

	@Override
	public GeneratedMessage openLogin(String code) {
		if (!systemManager.checkPassword(code)) {
			return SCGMMessage.newBuilder()
					.setGmOpenLoginResponse(GmOpenLoginResponse.newBuilder().setErrorCode(false)).build();
		}
		systemManager.open();

		return SCGMMessage.newBuilder().setGmOpenLoginResponse(GmOpenLoginResponse.newBuilder().setErrorCode(true))
				.build();

	}

	@Override
	public void terminatedServer(String code, IoSession session) {
		if (!systemManager.checkPassword(code)) {
			session.write(SCGMMessage.newBuilder()
					.setGmTerminatedServerResponse(GmTerminatedServerResponse.newBuilder().setErrorCode(false)).build());
			return;
		}

		systemManager.close();

		System.out.println("port close");

		System.out.println("start save");
		systemManager.terminated();
		System.out.println("save complete");

		session.write(SCGMMessage.newBuilder()
				.setGmTerminatedServerResponse(GmTerminatedServerResponse.newBuilder().setErrorCode(true)).build());
		// 关闭
		System.exit(0);

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

		for (RoleInterface roleInterface : RoleCache.getRoleMap().values()) {
			try {
				SessionCloseHandler.manipulate((Role) roleInterface);
			} catch (Exception e) {
				System.out.println("Role: " + roleInterface.getRoleId() + " saveError!");
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
