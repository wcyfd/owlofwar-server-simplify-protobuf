package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.MapsConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.MapsConfig;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.MapsConfig.CardType;

public class MapsXmlReader {
	public static void readXml(String path) {
		SAXReader sax = new SAXReader();
		// Step 1 目录
		Document doc;
		try {
			doc = sax.read(path);
			Element root = doc.getRootElement();
			Iterator<Element> itemIt = root.elementIterator("item");
			while (itemIt.hasNext()) {
				Element itemE = itemIt.next();
				int itemId = Integer.parseInt(itemE.attributeValue("id"));
				Element NpcActions = itemE.element("NpcActions");
				//TODO
				if(NpcActions!=null){
					List cardElements = NpcActions.elements();
					int len = cardElements.size();
					if (len != 0) {
						Element mainElement = (Element) cardElements.get(len - 1);
						int mainId = Integer.parseInt(mainElement.attributeValue("id"));
						CardType cardType = mainElement.getName().equals("Card") ? CardType.CARD : CardType.EXTRA_CARD;
						
						MapsConfig mapsConfig = new MapsConfig();
						mapsConfig.setMapsId(itemId);
						mapsConfig.setMainId(mainId);
						mapsConfig.setCardType(cardType);
						
						MapsConfigCache.putMapsConfig(mapsConfig);
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
