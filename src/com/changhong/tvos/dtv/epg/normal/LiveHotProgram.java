package com.changhong.tvos.dtv.epg.normal;

public class LiveHotProgram {

	private String name;
	private String channel_name;
	private String channel_code;
	private String start_time;
	private String end_time;

	public LiveHotProgram() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannelName() {
		return channel_name;
	}

	public void setChannelName(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getChannelCode() {
		return channel_code;
	}

	public void setChannelCode(String channel_code) {
		this.channel_code = channel_code;
	}

	public String getStartTime() {
		return start_time;
	}

	public void setStartTime(String start_time) {
		this.start_time = start_time;
	}

	public String getEndTime() {
		return end_time;
	}

	public void setEndTime(String end_time) {
		this.end_time = end_time;
	}

}
