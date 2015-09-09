package com.changhong.pushoutView;

import android.util.Log;

public final class MagicButtonCommon {
	
	private static final String TAG = "DTVpushview";
//	public static final boolean LOG_ENABLE = false;
	public static final boolean LOG_ENABLE = true;
	private static final boolean DETAIL_ENABLE = true;
	
	//the operate when the magic button was clicked
	public static final int NON_OPERATE = -1;
	public static final int START_ACTIVITY = 1;
	public static final int SEND_BRODECAST = 2;
	public static final int CALL_FUNCTION =3;
	public static final int CALL_FUNCTION_ONE =4;
	
	//the post flag
	public static final int POST__NO_NEED = -1; //no need post we get picture form local
	public static final int POST_IMAGE_READY = 1;
	public static final int POST_IMAGE_NOTREADY = 0;
	
	//business type
	public static final int BUSINESS_LOCAL_HOT_CHANNEL = 1;
	public static final int BUSINESS_SPECAL_PROGRAME = 2;
	public static final int BUSINESS_VCR = 3;
	public static final int BUSINESS_HOT_ONLINE = 4;
	public static final int BUSINESS_PUSH_TO_USER = 5;
	public static final int BUSINESS_USER_ADDED= 6;
	
	//dtv local logo
	public static final String DTV_LOCAL_LOGO_PATH = "/data/dtv/logo/";
	public static final String DTV_CHANNEL_TABLE_LIST_ADRESS = "content://com.changhong.tvos.dtv.basechannelprovider/channel";
	public static final String HUAN_URL = "http://www.epg.huan.tv/json2";
	
	public static final int PUSH_OUT_VIEW_STAY_TIME = 20000;
	public static final int PUSH_OUT_VIEW_USERADDED_TIME = 30000;

	//start type
	public static final int START_TYPE_NONETYPE = -1;
	public static final int START_TYPE_USERADD = 1;
	public static final int START_TYPE_RESTARTSHOW = 2;
	public static final int START_TYPE_PUSHOUTSHOW = 3;
	
	private static String buildMsg(String msg) {
		StringBuilder buffer = new StringBuilder();

		if (DETAIL_ENABLE) {
			final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];

			buffer.append("[T:");
			buffer.append(Thread.currentThread().getName());
			buffer.append(" F:");
			buffer.append(stackTraceElement.getFileName());
			buffer.append(" L:");
			buffer.append(stackTraceElement.getLineNumber());
			buffer.append(" M:");
			buffer.append(stackTraceElement.getMethodName());
			buffer.append("()]--");
		}
		buffer.append(msg);
		return buffer.toString();
	}

	public static void print(boolean saveWhenRelease, String msg) {

		if (saveWhenRelease) {

			Log.v(TAG, buildMsg(msg));
		} else {

			if (LOG_ENABLE) {

				Log.v(TAG, buildMsg(msg));
			}
		}
	}
}