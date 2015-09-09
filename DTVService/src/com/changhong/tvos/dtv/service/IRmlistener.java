package com.changhong.tvos.dtv.service;

public interface IRmlistener {
	public static final int STATUS_INIT = 0;
	public static final int STATUS_FREE = 1;
	public static final int STATUS_USED = 2;
	public static final int STATUS_FORCEUSED = 3;
	public static final int STATUS_CLOSED = 4;

	public void RmlistenCallback(int Staus, int RsID, int RsType, String ClientID);
}
