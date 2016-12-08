package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.AIPlayerConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.AIPlayer001;

public class AIPlayer001Reader {
	public static void readXml(String path)
	{
		SAXReader sax = new SAXReader();
		//Step 1 目录
		Document doc;
		try {
			doc = sax.read(path);
			Element root = doc.getRootElement();
			//规定  字段名称
			AIPlayer001 temp = new AIPlayer001();
			temp.setAccount(root.attribute("account").getValue());
			temp.setNickname(root.attribute("nickname").getValue());
			temp.setCards(root.elementText("cards"));
			AIPlayerConfigCache.getMap().put(temp.getAccount(), temp);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
