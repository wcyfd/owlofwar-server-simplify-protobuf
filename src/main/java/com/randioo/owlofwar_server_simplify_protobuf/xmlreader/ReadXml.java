package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

public class ReadXml {
	public static void readAll() {
		System.out.println("load xml");
		CardXmlReader.readXml("./config/Card.xml");
		AIPlayer001Reader.readXml("./config/AIPlayer001.xml");
		CardLvXmlReader.readXml("./config/CardLevel.xml");
		ExtraCardXmlReader.readXml("./config/ExtraCard.xml");
		MapsXmlReader.readXml("./config/Maps.xml");
		AutoNpcXmlReader.readXml("./config/AutoNPC.xml");
		CardInitXmlReader.readXml("./config/CardInit.xml");
		System.out.println("load xml complete");
	}
}
