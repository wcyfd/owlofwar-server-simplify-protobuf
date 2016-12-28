package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.randioo.owlofwar_server_simplify_protobuf.cache.file.CardConfigCache;
import com.randioo.owlofwar_server_simplify_protobuf.entity.file.CardConfig;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.EventAwardHero;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.EventElement;
import com.randioo.owlofwar_server_simplify_protobuf.entity.po.EventGetCard;
import com.randioo.randioo_server_base.utils.XmlReader;

public class CardXmlReader implements XmlReader{


	@Override
	public void readXml(InputStream inputStream) {
		// TODO Auto-generated method stub
		SAXReader sax = new SAXReader();
		// Step 1 目录
		Document doc;
		try {
			doc = sax.read(inputStream);
			Element root = doc.getRootElement();
			Map<Byte, Class<? extends EventElement>> classMap = new HashMap<>();
			classMap.put((byte) 5, EventAwardHero.class);
			classMap.put((byte) 10, EventGetCard.class);

			// 规定 字段名称
			Iterator<Element> els = root.elementIterator("item");
			while (els.hasNext()) {
				Element element = (Element) els.next();
				CardConfig temp = new CardConfig();
				int id = Integer.parseInt(element.attribute("id").getValue());
				String name = element.attribute("name").getValue();
				byte quality = Byte.parseByte(element.attribute("quality").getValue());
				byte type = Byte.parseByte(element.attribute("type").getValue());
				int useTimes = Integer.parseInt(element.elementText("useTimes"));
				int makeSpeed = Integer.parseInt(element.elementText("cost"));
				double useDelay = Double.parseDouble(element.elementText("useDealy"));

				Element unlockElement = element.element("unlock");
				int needTime = Integer.parseInt(unlockElement.attribute("needTime").getValue());
				int heroCount = Integer.parseInt(unlockElement.attribute("needHero").getValue());
				int trade = Integer.parseInt(unlockElement.attribute("trade").getValue());
				int learn = Integer.parseInt(unlockElement.attribute("learn").getValue());
				int force = Integer.parseInt(unlockElement.attribute("force").getValue());
				int food = Integer.parseInt(unlockElement.attribute("food").getValue());
				int money = Integer.parseInt(unlockElement.attribute("money").getValue());
				String needRes = unlockElement.attribute("needRes").getValue();

				Map<Byte, EventElement> eventMap = temp.getEventMap();
				Iterator<Element> eventIt = unlockElement.elementIterator("event");
				while (eventIt.hasNext()) {
					Element eventElement = eventIt.next();
					byte eventType = Byte.parseByte(eventElement.attribute("type").getValue());
					String eventData = eventElement.attribute("data").getValue();
					try {
						EventElement obj = classMap.get(eventType).newInstance();
						obj.setType(eventType);
						obj.setData(eventData);
						eventMap.put(eventType, obj);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}

				}

				Element levelInfoElement = element.element("levelinfo");

				Iterator<Element> levelInfoElementIt = levelInfoElement.elementIterator("item");
				List<Element> attributeInfoList = levelInfoElement.elements("attribute");
				while (levelInfoElementIt.hasNext()) {
					Element levelElement = levelInfoElementIt.next();
					byte level = Byte.parseByte(levelElement.attributeValue("level"));
					double cK = Double.parseDouble(levelElement.attributeValue("cK"));
					for (Element attribute : attributeInfoList) {
						String attrType = attribute.attributeValue("type");
						if (attrType.equals("trade") || attrType.equals("learn") || attrType.equals("force")) {
							double value = Double.parseDouble(attribute.attributeValue("value"));
							double cP = Double.parseDouble(attribute.attributeValue("cP"));
							double cA = Double.parseDouble(attribute.attributeValue("cA"));
							double cB = Double.parseDouble(attribute.attributeValue("cB"));

							double y = (value * Math.pow(1 + cP, level - 1) + cA * Math.pow(level - 1, 2) + cB
									* (level - 1))
									* cK;

							Map<Byte, Map<String, Double>> levelAttributeMap = temp.getLevelAttributeMap();
							Map<String, Double> map2 = levelAttributeMap.get(level);
							if (map2 == null) {
								map2 = new HashMap<>();
								levelAttributeMap.put(level, map2);
							}
							map2.put(attrType, y);
						}
					}
				}

				// while (levelInfoElementIt.hasNext()) {
				// Element itemElement = levelInfoElementIt.next();
				// // byte level =
				// // Byte.parseByte(itemElement.attributeValue("level"));
				// Iterator<Element> item2ElementIt =
				// itemElement.elementIterator("item");
				// while (item2ElementIt.hasNext()) {
				// Element levelElement = item2ElementIt.next();
				// byte level = Byte.parseByte(element.attributeValue("level"));
				// element.attributeValue("cK");
				// }
				//
				// Iterator<Element> itemElementIt =
				// itemElement.elementIterator("attribute");
				// while (itemElementIt.hasNext()) {
				// Element item = itemElementIt.next();
				// String attrType = item.attributeValue("type");
				// int attrValue = 0;
				// try {
				// attrValue = Integer.parseInt(item.attributeValue("value"));
				// } catch (Exception e) {
				//
				// }
				// Map<Byte, Map<String, Integer>> levelAttributeMap =
				// temp.getLevelAttributeMap();
				// Map<String, Integer> map2 = levelAttributeMap.get(level);
				// if (map2 == null) {
				// map2 = new HashMap<>();
				// levelAttributeMap.put(level, map2);
				// }
				// map2.put(attrType, attrValue);
				// }
				//
				// }

				temp.setId(id);
				temp.setName(name);
				temp.setQuality(quality);
				temp.setType(type);
				temp.setUseTime(useTimes);
				temp.setMakeSpeed(makeSpeed);
				temp.setUseDealy(useDelay);
				temp.setUnlockForce(force);
				temp.setUnlockLearn(learn);
				temp.setUnlockTrade(trade);
				temp.setUnlockNeedFood(food);
				temp.setUnlockNeedHeroCount(heroCount);
				temp.setUnlockNeedMoney(money);
				temp.setUnlockNeedRes(needRes);
				temp.setUnlockNeedTime(needTime);

				CardConfigCache.putCardConfig(temp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
