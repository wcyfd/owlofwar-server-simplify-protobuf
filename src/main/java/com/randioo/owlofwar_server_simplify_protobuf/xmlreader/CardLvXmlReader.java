package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardLevelConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardLevelConfig;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.CardLevel;
import com.randioo.randioo_server_base.utils.XmlReader;

public class CardLvXmlReader implements XmlReader{
	

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
			Iterator<Element> els = root.elementIterator("quality");
			while (els.hasNext()) {
				Element element = (Element) els.next();
				CardLevelConfig temp = new CardLevelConfig();
				temp.setQuality(Byte.parseByte(element.attribute("id").getValue()));
				Iterator<Element> itemIt = element.elementIterator("item");
				while (itemIt.hasNext()) {
					Element element2 = (Element) itemIt.next();
					
					CardLevel cardLevel = new CardLevel();					
					String level = element2.attribute("level").getValue();
					String costCard = element2.attribute("costCard").getValue();
					String cost = element2.attribute("cost").getValue();
					
					cardLevel.setLevel(Integer.parseInt(level));
					cardLevel.setCostCard(Integer.parseInt(costCard));
					cardLevel.setCostMoney(Integer.parseInt(cost));
					
					temp.getLvMap().put(cardLevel.getLevel(), cardLevel);
				}
				CardLevelConfigCache.putConfig(temp);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
