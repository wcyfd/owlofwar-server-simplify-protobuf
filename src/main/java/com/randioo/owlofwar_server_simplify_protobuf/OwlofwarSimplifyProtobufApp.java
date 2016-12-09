package com.randioo.owlofwar_server_simplify_protobuf;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.randioo.owlofwar_server_simplify_protobuf.xmlreader.ReadXml;
import com.randioo.randioo_server_base.module.ServiceManager;
import com.randioo.randioo_server_base.net.DataSource;
import com.randioo.randioo_server_base.net.ServerConfig;
import com.randioo.randioo_server_base.net.SpringContext;
import com.randioo.randioo_server_base.net.WanServer;
import com.randioo.randioo_server_base.net.protocal.protobuf.ServerMessageCodecFactory;
import com.randioo.randioo_server_base.utils.system.SystemManager;

/**
 * Hello world!
 *
 */
public class OwlofwarSimplifyProtobufApp 
{
    public static void main( String[] args )
    {        
//    	DataSource.jdbcUrl = "jdbc:mysql://localhost:3306/bycodegameowl6666?useUnicode=true&characterEncoding=utf-8";
    	
    	ReadXml.readAll();
        SpringContext.initSpringCtx("ApplicationContext.xml");
        DataSource dataSource = SpringContext.getBean("dataSource");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/bycodegameowl6667?useUnicode=true&characterEncoding=utf-8");
		((ServiceManager) SpringContext.getBean("serviceManager")).initServices();	
		
		SystemManager systemManager = SpringContext.getBean("systemManager");
		systemManager.open();
		
		
		System.out.println("Hello OwlofwarSimplifyProtobufApp!");
		WanServer.startServer(
				new ProtocolCodecFilter(new ServerMessageCodecFactory(Charset.forName(ServerConfig.getCharSet()))),
				new ServerHandler(), new InetSocketAddress(6667));
		
    	
//		StoreVideoDao storeVideoDao = SpringContext.getBean("storeVideoDao");
//		StoreVideo storeVideo = storeVideoDao.getStoreVideoByGameId(0);
//		System.out.println(storeVideo.getStoreFrames().getFramesCount());
//		System.out.println(storeVideo.getStoreRoleResourceInfos());
//		System.out.println(storeVideo.getGameResultStr());
//		System.out.println(storeVideo.getStartTime());

//		Video video = new Video();
//		video.setGameId(0);
//		video.setStartTime(System.currentTimeMillis());
//		video.getRoleResourceInfoMap().add(
//				SCFightLoadResource.newBuilder().setIsNPCGame(true).setMyPlayerId(20).setNPCMapId(3).build());
//		for (int i = 0; i < 7200; i++) {
//			video.getFrames().add(Frame.newBuilder().build());
//		}
//		VideoManager.addVideo(video);
//
//		ServerTerminatedHandler.init();
//
//		System.exit(0);
		
    }
}
