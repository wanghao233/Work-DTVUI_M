package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.DTVSource;

public class ResourceManager {

	private static ResourceManager instance = null;

	protected ResourceManager() {

	}

	public static synchronized ResourceManager getinstance() {
		if (instance == null)
			instance = new ResourceManager();
		return instance;
	}

	public native int requestResource(int type, int id);

	public native int releaseResource(int type, int id);

	public native DTVSource[] getDTVSourceList();
}