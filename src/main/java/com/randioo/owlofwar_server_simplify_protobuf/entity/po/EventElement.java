package com.randioo.owlofwar_server_simplify_protobuf.entity.po;

public abstract class EventElement {
	private byte type;
	private String data;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public void setData(String data) {
		this.data = data;
		this.formatData(data);
	}

	public String getData() {
		return data;
	}

	protected abstract void formatData(String data);
}
