package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardInitConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardInitConfig;

public class CardInitXmlReader {
	public static void readXml(String path)
	{
		SAXReader sax = new SAXReader();
		//Step 1 目录
		Document doc;
		try {
			doc = sax.read(path);
			Element root = doc.getRootElement();
			//规定  字段名称
			CardInitConfig temp = new CardInitConfig();
			temp.setId(root.getStringValue());
			CardInitConfigCache.addList(root.getStringValue().trim());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
