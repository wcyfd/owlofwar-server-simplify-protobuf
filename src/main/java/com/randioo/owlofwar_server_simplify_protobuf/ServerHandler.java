package com.randioo.owlofwar_server_simplify_protobuf;

import java.io.InputStream;
import java.util.Map;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.RoleCache;
import com.randioo.owlofwar_server_simplify_protobuf.cache.local.SessionCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.bo.Role;
import com.randioo.owlofwar_server_simplify_protobuf.module.fight.service.FightService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Game;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ClientMessage.CSMessage;
import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.net.IActionSupport;
import com.randioo.randioo_server_base.net.IoHandlerAdapter;
import com.randioo.randioo_server_base.net.SpringContext;

public class ServerHandler extends IoHandlerAdapter {

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("roleId:" + session.getAttribute("roleId") + " sessionCreated");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("roleId:" + session.getAttribute("roleId") + " sessionOpened");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("roleId:" + session.getAttribute("roleId") + " sessionClosed");
		Role role = RoleCache.getRoleBySession(session);
		if (role != null) {
			try {
				SessionCloseHandler.manipulate(role);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable e) throws Exception {

	}

	@Override
	public void messageReceived(IoSession session, Object messageObj) throws Exception {
		
		

		InputStream input = (InputStream) messageObj;
		try {
			CSMessage message = CSMessage.parseDelimitedFrom(input);

			if (null == message) {
				System.out.println("ERR_MESSAGE_REC");
				return;
			}

			System.out.println(getMessage(message, session));

			Map<FieldDescriptor, Object> allFields = message.getAllFields();
			for (Map.Entry<FieldDescriptor, Object> entrySet : allFields.entrySet()) {

				String name = entrySet.getKey().getName();
				IActionSupport action = Navigation.getAction(name);
				try {
					action.execute(entrySet.getValue(), session);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("伪造的协议ID：" + name);					
					session.close(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
		System.out.println(getMessage(message, session));
	}
	
	private String getMessage(Object message,IoSession session){
		Integer roleId = (Integer) session.getAttribute("roleId");
		String roleAccount = null;
		String roleName = null;
		if (roleId != null) {
			Role role = RoleCache.getRoleById(roleId);
			if (role != null) {
				roleAccount = role.getAccount();
				roleName = role.getName();
			}
		}
		
		return "[account:" + roleAccount + ",name:" + roleName + "] " + message;
	}

}
