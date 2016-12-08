package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.ExtraCardConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.ExtraCardConfig;

public class ExtraCardXmlReader {
	public static void readXml(String path) {
		SAXReader sax = new SAXReader();
		Document doc;
		try {
			doc = sax.read(path);
			Element root = doc.getRootElement();
			Iterator<Element> itemIt = root.elementIterator("item");
			while (itemIt.hasNext()) {
				Element itemE = itemIt.next();
				int extraCardId = Integer.parseInt(itemE.attributeValue("extraCardId"));
				int cardId = Integer.parseInt(itemE.attributeValue("cardId"));
				String name = itemE.element("c_name").getStringValue();

				ExtraCardConfig config = new ExtraCardConfig();
				config.setExtraCardId(extraCardId);
				config.setName(name);

				ExtraCardConfigCache.putExtraCardConfig(config);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
