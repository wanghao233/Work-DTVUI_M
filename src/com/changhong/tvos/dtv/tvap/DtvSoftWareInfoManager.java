package com.changhong.tvos.dtv.tvap;

import android.util.Log;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvVersion;

public class DtvSoftWareInfoManager {
	private static DtvInterface mDtvInterface = DtvInterface.getInstance();

	/**
	 * 主版本号(0)：当功能模块有较大的变动，比如增加多个模块或者整体架构发生变化。此版本号由项目决定是否修改。
	 * 子版本号(0)：当功能有一定的增加或变化，比如增加了对权限控制、增加自定义视图等功能。此版本号由项目决定是否修改。 阶段版本号(0)：一般是
	 * Bug 修复或是一些小的变动，要经常发布修订版，时间间隔不限，修复一个严重的bug即可发布一个修订版。此版本号由项目经理决定是否修改。
	 * 日期版本号(12-10-01-18:30):用于记录修改项目的当前日期，每天对项目的修改都需要更改日期版本号。此版本号由开发人员决定是否修改。
	 * 希腊字母版本号(Beta):此版本号用于标注当前版本的软件处于哪个开发阶段，当软件进入到另一个阶段时需要修改此版本号。此版本号由项目决定是否修改。
	 */

	public static String mMainVersion = "";
	private static String mSubVersion = "";
	public static String mUIVersion = "";
	public static String mHardVersion = "";
	// private static String mStageVersion = "1";
	public static String releaseTime = "2013-07-04 00:00";
	public static String mSoftwareVersion = mMainVersion + mSubVersion;

	public static DtvVersion getDtvVersion() {
		DtvVersion DtvVersion = null;
		DtvVersion = mDtvInterface.getDTVVersionInfo();
		return DtvVersion;

	}

	public static DtvCardStatus getCardStatus() {
		DtvCardStatus mCardStatus = null;
		mCardStatus = mDtvInterface.getCicaCardStatus();
		return mCardStatus;
	}

	public static DtvOperator getCurOperator() {
		DtvOperator curOperator = null;
		curOperator = mDtvInterface.loadCurOperator();
		return curOperator;
	}

	public static int getCheckVersion() {
		String str = mSoftwareVersion;// mMainVersion + mSubVersion; //+// mStageVersion;
		int curVersion = 0;
		try {
			curVersion = Integer.valueOf(str);
		} catch (NumberFormatException e) {
			Log.e("DtvSoftWareInfoManager",
					"the format err because of feng changed wrong");
		}
		return curVersion;

	}

	public static void setmUIVersion(String strchipmodel, String struiversion,
			String strhardversion, String strapkmainversion,
			String strapksubversion, String strReleaseTime) {
		if (null == strchipmodel) {
			Log.i("DtvSoftWareInfoManager", "chipmodel is " + strchipmodel);
			return;
		}

		if (null == struiversion) {
			Log.i("DtvSoftWareInfoManager", "struiversion is " + struiversion);
			return;
		}
		if (null == strhardversion) {
			Log.i("DtvSoftWareInfoManager", "strhardversion is "
					+ strhardversion);
			return;
		}
		if (null == strapkmainversion) {
			Log.i("DtvSoftWareInfoManager", "strapkmainversion is "
					+ strapkmainversion);
			return;
		}
		if (null == strapksubversion) {
			Log.i("DtvSoftWareInfoManager", "strapksubversion is "
					+ strapksubversion);
			return;
		}

		if (null == strReleaseTime) {
			Log.i("DtvSoftWareInfoManager", "strreleaseTime is "
					+ strReleaseTime);
			return;
		}

		try {

			mHardVersion = strhardversion;
			mMainVersion = strapkmainversion;// tem[0];
			mSubVersion = strapksubversion;// tem[1];
			mUIVersion = struiversion;// items[0] + items[1];
			releaseTime = strReleaseTime;
			mSoftwareVersion = mMainVersion + mSubVersion;

		} catch (Exception e) {

			Log.e("DtvSoftWareInfoManager", "err in setmUIVersion " + e);
		}
	}
}
