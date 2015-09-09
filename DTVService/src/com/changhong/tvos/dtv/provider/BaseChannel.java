package com.changhong.tvos.dtv.provider;

public class BaseChannel {

	private String name;
	private String code;
	private int index;
	private String type;
	private String memo;

	public BaseChannel() {
	}

	public BaseChannel(String name, int index, String code, String type) {

		this.name = name;
		this.code = code;
		this.index = index;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
