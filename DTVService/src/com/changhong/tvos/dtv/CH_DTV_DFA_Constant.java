package com.changhong.tvos.dtv;

public final class CH_DTV_DFA_Constant {	
	public final static int MAX_NIT_DESC_COUNT = 100;
	public final static int MAX_PROMPT_INFO_LIST = 100;
	public final static int DFA_RESULT_SUCCESS = 1;
	public final static int DFA_RESULT_FAILED = 0;
	public final static String DFA_UPDATE_TRIGGER_TITLE = "Update...";
	public final static String DFA_UPDATE_PROGRESS_TITLE = "Update Progress";
	public final static String DFA_UPDATE_RESULT_TITLE = "Update Result";
	
	/*CHMID_OTA_STATUS*/
	public final static int CHMID_OTA_DOWNLOAD_NIT = 0;
	public final static int CHMID_OTA_DOWNLOAD_DSI = 1;
	public final static int CHMID_OTA_DOWNLOAD_DII = 2;
	public final static int CHMID_OTA_DOWNLOAD_DDB = 3;
	public final static int CHMID_OTA_UNCOMPRESS_IMAGE = 4;
	public final static int CHMID_OTA_VERIFY_IMAGE = 5;
	public final static int CHMID_OTA_SAVE_IMAGE = 6;
	public final static int CHMID_OTA_COMPLETE = 7;

	/*CHMID_OTA_RESULT_e*/
	public final static int CHMID_OTA_OK = 0;
	public final static int CHMID_OTA_FAILED = 1;
}

