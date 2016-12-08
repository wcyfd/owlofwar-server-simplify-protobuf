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
		System.out.println("Hello OwlofwarSimplifyProtobufApp!");
		WanServer.startServer(
				new ProtocolCodecFilter(new ServerMessageCodecFactory(Charset.forName(ServerConfig.getCharSet()))),
				new ServerHandler(), new InetSocketAddress(6667));
    }
}
