package com.randioo.owlofwar_server_simplify_protobuf.xmlreader;

public class ReadXml {
	public static void readAll() {
		CardXmlReader.readXml("./xml/Card.xml");
		AIPlayer001Reader.readXml("./xml/AIPlayer001.xml");
		CardLvXmlReader.readXml("./xml/CardLevel.xml");
		ExtraCardXmlReader.readXml("./xml/ExtraCard.xml");
		MapsXmlReader.readXml("./xml/Maps.xml");
		AutoNpcXmlReader.readXml("./xml/AutoNPC.xml");
		CardInitXmlReader.readXml("./xml/CardInit.xml");
	}
}
