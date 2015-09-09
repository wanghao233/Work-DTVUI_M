package com.changhong.tvos.dtv.tvap.baseType;

public class ConstValueClass {
	public class ConstStringKey{
		public static final String USER_SET_SCAN_FREQ = "user_set_scan_freq";
		public static final String USER_SET_SCAN_SYM = "user_set_scan_sym";
		public static final String USER_SET_SCAN_QAM = "user_set_scan_qam";
		public static final String USER_SET_FAV_SORT = "user_set_fav_sort";
		public static final String USER_SET_SCAN_CARRIER_MODE = "user_set_scan_carrier";
		public static final String USER_SET_SCAN_LOW_FREQ = "user_set_scan_lowfreq";
		public static final String USER_SET_SCAN_NCO_FREQ = "user_set_scan_ncofre";
		public static final String USER_SET_SCAN_LDPC_RATE = "user_set_scan_ldpc";
		public static final String USER_SET_SCAN_FRAM_HEADER = "user_set_scan_frameheader";
		public static final String USER_SET_SCAN_INTER_LEAVE = "user_set_scan_interleave";
		public static final String USER_SET_SCAN_DEMOD_TYPE = "user_set_demod_type";
		public static final String USER_SET_BANDWIDTH = "user_set_bandwidth";
		public static final String USER_SET_IS_DEBUG = "user_set_is_debug";
	}
	public class ConstActivityStatus{
		public static final int ACTIVITY_INVALID_STATUS = -1;
		public static final int ACTIVITY_ONCREATE_STATUS = 0;
		public static final int ACTIVITY_ONSTART_STATUS = 1;
		public static final int ACTIVITY_ONRESUME_STATUS = 2;
		public static final int ACTIVITY_ONPUASE_STATUS = 3;
		public static final int ACTIVITY_ONSTOP_STATUS = 4;
		public static final int ACTIVITY_ONDESTROY_STATUS = 5;
	}
	
	public class ConstPageDataID{
		public static final String CHANNEL_SCAN_SETUP_DATA = "channelScanSetupData";
		public static final String OPERATOR_LIST_PAGE_DATA = "operatorListPageData";
		public static final String SOURCE_SETUP_PAGE_DATA = "sourceSetupPageData";
		public static final String SOURCE_LIST_PAGE_DATA = "sourceListPageData";
		public static final String CHANNEL_INFO_PAGE_DATA = "channelInfoPageData";
		public static final String CHANNEL_SCAN_DATA = "channelScanData";
		public static final String SYSTEM_SET_UP = "systemSetup";
		
	}
	public class ConstMessageType{
		public static final int DTV_SCREEN_SAVER_MESSAGE = 0x100;
		public static final int DTV_LOG_OUT_MESSAGE = 0x101;
	}
	
	public class ConstLongTimeDelay{
		public static final long DELAY_MILLIS_5 = 5000;
		public static final long DELAY_MILLIS_10 = 10000;
		public static final long DELAY_MILLIS_15 = 15000;
	}
	public class ConstDefautUserValue{
		public static final int FAV_SORT_DEFAULT_VALUE = 1;
		
	}
	public class ConstOperatorCode{
		public static final int OPERATOR_CODE_HANGZHOU_WASU = 0x00571102;
		public static final int DTMB_OP_CODE_COMMON_CHCA = 0x00000100;
		public static final int DTMB_OP_CODE_JX = 0x00791001;
	}
	public class ConstSourceID{
		public static final int DVBC = 1;
		public static final int DTMB = 2;
		
	}
	
	public class ConstOperatorState{
		public static final String OPCHANGED = "opchage";
		public static final String OP_GUIDE = "opguide";
	}
	
	public class ConstScanParams{
		public static final int FREQUANCE_MIN_K_C = 107000;
		public static final int FREQUANCE_MIN_K_T = 52500;
		public static final int FREQUANCE_MAX_K   = 999000;
		public static final int FREQUANCE_BANDWIDTH = 8000;
		public static final int FREQUANCE_MAX_K_T   = 999000;
	}
	public class ConstProductType{
		public static final int	PRODUCT_C = 1;
		public static final int	PRODUCT_T = 2;
		public static final int	PRODUCT_C_T = 3;
	}
}
