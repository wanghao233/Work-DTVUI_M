package com.changhong.tvos.dtv.service;

import java.util.List;

public class Resource {
	public static final int RESOURCE_TUNER = 0;
	public static final int RESOURCE_VIDEODECODER = 1;
	public static final int RESOURCE_VIDEOWINDDOW = 2;
	public static final int RESOURCE_AUDIODECODER = 3;

	public static final int STATUS_FREEED = 0;
	public static final int STATUS_USED = 1;
	public static final int STATUS_FORCEUSED = 2;

	public int RsID;
	public int RsType;
	public int TVOSUsed;
	public int RsStaus;
	public List<RsClient> Clientlist;
}