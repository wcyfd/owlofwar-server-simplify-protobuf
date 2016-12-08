package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

public class EventGetCard extends EventElement {

	private byte initLv;

	@Override
	protected void formatData(String data) {
		byte initLv = Byte.parseByte(data);
		this.initLv = initLv;
	}

	public byte getInitLv() {
		return initLv;
	}
}
