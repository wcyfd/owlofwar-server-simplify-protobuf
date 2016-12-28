package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.randioo.randioo_server_base.utils.ReflectUtils;
import com.randioo.randioo_server_base.utils.XmlReader;

public class ReadXml {
	public static void readAll(String zipPathName) {
		System.out.println("load xml");
		Map<String, Class<? extends XmlReader>> classFileMap = getMap();
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPathName))) {
			for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {

				String fileName = entry.getName();
				Class<? extends XmlReader> clazz = classFileMap.get(fileName);
				if (clazz == null) {
					continue;
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = zis.read(buffer)) >= 0) {
					baos.write(buffer, 0, len);
				}

				InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
				baos.close();
				XmlReader xmlReader = ReflectUtils.newInstance(clazz);
				xmlReader.readXml(inputStream);
				inputStream.close();

			}
		} catch (FileNotFoundException e1) {
			System.out.println("no found config file : " + zipPathName);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("load config complete");
	}

	public static Map<String, Class<? extends XmlReader>> getMap() {
		Map<String, Class<? extends XmlReader>> map = new HashMap<>();

		map.put("AIPlayer001.xml", AIPlayer001Reader.class);
		map.put("Card.xml", CardXmlReader.class);
		map.put("CardLevel.xml", CardLvXmlReader.class);
		map.put("ExtraCard.xml", ExtraCardXmlReader.class);
		map.put("Maps.xml", MapsXmlReader.class);
		map.put("AutoNPC.xml", AutoNpcXmlReader.class);
		map.put("CardInit.xml", CardInitXmlReader.class);

		return map;
	}
}
