package com.randioo.owlofwar_server_simplify_protobuf;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.randioo.owlofwar_server_simplify_protobuf.module.login.service.LoginService;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.ClientMessage.CSMessage;
import com.randioo.owlofwar_server_simplify_protobuf.protocol.Login.LoginCreateRoleRequest;
import com.randioo.owlofwar_server_simplify_protobuf.utils.JSInvoker;
import com.randioo.owlofwar_server_simplify_protobuf.xmlreader.ReadXml;
import com.randioo.randioo_server_base.module.ServiceManager;
import com.randioo.randioo_server_base.net.ServerConfig;
import com.randioo.randioo_server_base.net.SpringContext;
import com.randioo.randioo_server_base.net.WanServer;
import com.randioo.randioo_server_base.net.protocal.protobuf.ServerMessageCodecFactory;
import com.randioo.randioo_server_base.utils.ConfigLoader;
import com.randioo.randioo_server_base.utils.db.DatabaseInitialization;
import com.randioo.randioo_server_base.utils.sensitive.SensitiveWordDictionary;
import com.randioo.randioo_server_base.utils.system.SystemManager;

/**
 * Hello world!
 *
 */
public class OwlofwarSimplifyProtobufApp {
	public static void main(String[] args) {
		int port = 9998;

		ReadXml.readAll("./config/config.zip");
		ConfigLoader.loadConfig("com.randioo.owlofwar_server_simplify_protobuf.entity.file", "./config2.zip");
		
		SensitiveWordDictionary.readAll("./config/sensitive.txt");

		SpringContext.initSpringCtx("ApplicationContext.xml");

		// 初始化数据库
		DatabaseInitialization databaseInitialization = SpringContext.getBean("databaseInitialization");
		databaseInitialization.setDatabaseName(databaseInitialization.getDatabaseName() + port);
		databaseInitialization.initialize();

		// 服务初始化
		((ServiceManager) SpringContext.getBean("serviceManager")).initServices();

		// 服务器开关
		SystemManager systemManager = SpringContext.getBean("systemManager");
		systemManager.close();

		System.out.println("Hello OwlofwarSimplifyProtobufApp!");

		WanServer.startServer(
				new ProtocolCodecFilter(new ServerMessageCodecFactory(Charset.forName(ServerConfig.getCharSet()))),
				new ServerHandler(), new InetSocketAddress(port));
		System.out.println(port);
		systemManager.open();
		
	}

}
