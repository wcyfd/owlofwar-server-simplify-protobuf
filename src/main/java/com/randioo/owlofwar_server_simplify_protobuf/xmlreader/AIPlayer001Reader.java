package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.AIPlayerConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.AIPlayer001;
import com.randioo.randioo_server_base.utils.XmlReader;

public class AIPlayer001Reader implements XmlReader{


	@Override
	public void readXml(InputStream inputStream) {
		// TODO Auto-generated method stub
		SAXReader sax = new SAXReader();
		//Step 1 目录
		Document doc;
		try {
			doc = sax.read(inputStream);
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
