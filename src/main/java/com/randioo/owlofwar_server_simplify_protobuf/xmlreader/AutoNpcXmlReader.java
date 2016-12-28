package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.AutoNpcConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.AutoNpcConfig;
import com.randioo.randioo_server_base.utils.XmlReader;

public class AutoNpcXmlReader implements XmlReader{
	
	@Override
	public void readXml(InputStream inputStream){
		SAXReader sax = new SAXReader();
		// Step 1 目录
		Document doc;
		try {
			doc = sax.read(inputStream);
			Element root = doc.getRootElement();
			List list = root.elements();
			Iterator it = list.iterator();

			while (it.hasNext()) {
				AutoNpcConfig autoNpc = new AutoNpcConfig();
				Element mapsIdElement = (Element) it.next();
				autoNpc.setPoint(Integer.parseInt(mapsIdElement.attributeValue("point")));
				Iterator idIt = mapsIdElement.elements("npc").iterator();
				while (idIt.hasNext()) {
					Element idElement = (Element) idIt.next();
					autoNpc.getNpcIdList().add(Integer.parseInt(idElement.attributeValue("id")));
				}
				AutoNpcConfigCache.putAutpNpcConfig(autoNpc);
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
