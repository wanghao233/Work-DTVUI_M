package com.changhong.tvos.dtv.epg.normal;

public class RecommendSource {

	private String activity;
	private String apk;
	private String id;
	private String type;
	private String pic;
	private String name;
	private String playUri;

	public RecommendSource() {

	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getApk() {
		return apk;
	}

	public void setApk(String apk) {
		this.apk = apk;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayUri() {
		return playUri;
	}

	public void setPlayUri(String playUrl) {
		this.playUri = playUrl;
	}
}