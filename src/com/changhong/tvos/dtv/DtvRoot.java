package com.changhong.tvos.dtv;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.dmt.system.miscservice.CHMiscServiceBinder;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.pushoutView.PushoutService;
import com.changhong.softkeyboard.CHSoftKeyboardManager;
import com.changhong.tvos.common.MiscManager;
import com.changhong.tvos.common.SystemManager;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.common.exception.TVManagerNotInitException;
import com.changhong.tvos.dtv.DtvMessageThread.TimerCallBack;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.cica.MenuCiCaForce;
import com.changhong.tvos.dtv.cica.MenuCiCaForce.OnVirtualKeyDownListener;
import com.changhong.tvos.dtv.cica.MenuCiCaMail;
import com.changhong.tvos.dtv.cica.MenuCiCaMail.OnFloatMailListener;
import com.changhong.tvos.dtv.cica.MenuCiCaSubtitle;
import com.changhong.tvos.dtv.cica.MenuCiCaUser;
import com.changhong.tvos.dtv.epgView.MenuEpg_NEW_UI;
import com.changhong.tvos.dtv.menuManager.MenuDisplayManager;
import com.changhong.tvos.dtv.menuManager.MenuMoveManager;
import com.changhong.tvos.dtv.scan.MenuScan;
import com.changhong.tvos.dtv.scan.ScanManager.scantype;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustAudioManager;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvCicaManager;
import com.changhong.tvos.dtv.tvap.DtvCommonManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvMsgManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.DtvSoftWareInfoManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstActivityStatus;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstLongTimeDelay;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstMessageType;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstOperatorCode;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstOperatorState;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstProductType;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.tvap.baseType.DtvVersion;
import com.changhong.tvos.dtv.userMenu.MenuColorKey;
import com.changhong.tvos.dtv.userMenu.MenuOperation;
import com.changhong.tvos.dtv.userMenu.MenuQuickChannel;
import com.changhong.tvos.dtv.userMenu.MenuSearchGuide;
import com.changhong.tvos.dtv.util.ViewBootImageInfo;
import com.changhong.tvos.dtv.util.ViewBootTextInfo;
import com.changhong.tvos.dtv.util.ViewChannelInfo;
import com.changhong.tvos.dtv.util.ViewPromptInfo;
import com.changhong.tvos.dtv.util.ViewRadioBackground;
import com.changhong.tvos.dtv.vo.CICAMAnnounce;
import com.changhong.tvos.dtv.vo.CICAMFinger;
import com.changhong.tvos.dtv.vo.CICAMForceChannel;
import com.changhong.tvos.dtv.vo.CICAMMail;
import com.changhong.tvos.dtv.vo.CICAMMessageBase.ConstCICAMsgType;
import com.changhong.tvos.dtv.vo.CICAMPrompt;
import com.changhong.tvos.dtv.vo.CICAMSubtitle;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardType;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMMenuID;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMMenuType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMOpCode;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstPlayerEvent;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.dtv.vo.PlayStatusInfo;
import com.changhong.tvos.dtv.vo.StartControlInfo;
import com.changhong.tvos.dtv.vo.TimerInfo;
import com.changhong.tvos.model.ChOsType.EnumPanelType;
import com.changhong.tvos.model.PanelInfo;
import com.changhong.tvos.system.commondialog.CommonAcionDialog;
import com.changhong.tvos.system.commondialog.CommonHintToast;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;
import com.changhong.tvos.system.commondialog.CommonProgressInfoDialog;
import com.changhong.tvos.system.commondialog.VchCommonToastDialog;
import com.vwidget.vinterface.ProgressbarItemsSetForTheme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DtvRoot extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	private static final String TAG = "DtvRoot";
	public static boolean is5508Q2 = true;
	public static Context mContext = null;
	private Activity mEPGActivity;// this

	public static int mDtvRootCurStatus = ConstActivityStatus.ACTIVITY_INVALID_STATUS;
	public static int mDtvRootPreStatus = ConstActivityStatus.ACTIVITY_INVALID_STATUS;
	private static int scrambledNum = 0;
	private static int freeNum = 0;
	private static int allNum = 0;
	private int mInputNum = 0;
	private int mInputIndex = -1;
	private static String mOnCreateTime = null;
	private static String mOnStartTime = null;
	private static String mOnResumeTime = null;
	private static String mOnPauseTime = null;
	private static String mOnStopTime = null;
	private static String mOnDestroyTime = null;
	private MainMenuRootData mMainMenuRootData = null;
	private DtvChannelManager mChannelManager = null; // 节目管理
	private DtvCommonManager mDtvCommonManager = null; // 资源管理
	private DtvScheduleManager mScheduleManager = null; // 节目预约
	private DtvOperatorManager mOperatorManager = null; // 运营商管理
	private DtvSourceManager mSourceManager = null; // 节目源
	private RelativeLayout mBgLayout = null;
	private ViewChannelInfo mChannelInfoView = null; // 节目信息
	private ViewPromptInfo mPromptInfoView = null; // 提示信息
	private ViewRadioBackground mRadioBackView = null; // 广播的的背景
	private String mStrCiCaPrompt = null;
	private String mStrPlayerPrompt = null;
	private MenuMoveManager mMenuMoveManager = null; // 提示移动管理
	private DtvMsgManager mDtvMsgManager = null; // 消息管理
	private TextView mInputNumView = null; // 输入节目号
	private MenuCiCaMail mDtvCiCaMail = null; // ＣＩ　CA菜单
	private MenuCiCaUser mDtvCiCaUserMenu = null;
	private MenuCiCaForce mDtvCiCaForceMenu = null;
	private MenuCiCaSubtitle mDtvCiCaSubtitle = null;
	private CommonAcionDialog mCommonAcionDialog = null; // 提示对话框
	private CommonAcionDialog mScanWarningAcionDialog = null;// 搜索提示对话框
	private CommonHintToast hintToast = null; // Toast提示框
	private MenuScan mScanDialog = null; // 搜索菜单

	private Handler mHandler = null;
	private Runnable mRunnable = null;
	private Runnable mRunProgNum = null;
	private Runnable mRunForceProgNum = null;
	private Runnable mRunForceProgIndex = null;
	private CiCaQueryReceiver mCiCaQueryReceiver = null;
	private AutoSearchReceiver mAutoSearchReceiver = null;
	private LongPressMenuKeyReceiver mLongPressMenuKeyReceiver = null;
	private LongPressHomeKeyReceiver mLongPressHomeKeyReceiver = null;
	private ShortPressSystemKeyReceiver mShortPressSystemKeyReceiver = null;
	private EasySettingExitReceiver mEasySettingExitReceiver = null;
	public static boolean isStartControlEnd = false;
	public static boolean isFirstEnterMainMenu = true;
	private static boolean isHasSignal = true;
	private static boolean isHasProgram = true;
	private static boolean isReturnLastSource = false;
	private static boolean isNotDTVChanged = true;
	private static boolean return_source_back = true;
	private static boolean isNeedUpdateChannelList = false;
	private boolean mEasySettingFlag = false;
	private boolean isNeedResourcePrepare = false;
	private boolean isReqStartAutoScan = false;
	private boolean isStartWasuApp = false;
	private MenuEpg_NEW_UI mMenuEpg = null;
	private RelativeLayout mMailLayout;
	private TextView mMailText;
	private SystemManager systemmanage = null;// 退出屏保
	private MenuQuickChannel mQuickChannel = null; // 快速换台节目菜单
	private static DtvInterface mInterface = null; // 所有和调用API的接口的集合
	private static AdjustAudioManager mAdjustAudioManager = null; // 适配ＭＴＫ和ＭＳＴＡＲ的声音处理器
	private static final int mDelayTime = 2000;
	private static final int CHANNEL_CHANGE_DELAY = 2000;
	private static final int START_ACTIVITY = 13;

	/**
	 * 数字键盘
	 **/
	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;

	/**
	 * 等待服务启动对话框
	 **/
	private boolean isTimeArrive = true;
	private CommonProgressInfoDialog waitDialog = null;

	/**
	 * TVOS线程里的handler
	 **/
	public Handler serviceHandler;
	/**
	 * TVOS线程
	 **/
	private Thread waitSerivceThread = null;
	/**
	 * 等待服务启动么
	 **/
	static boolean isWaitingService = true;
	/**
	 * bootService 是否开始启动
	 **/
	private boolean isBeganBootService;
	/**
	 * bootService 是否被打断了
	 **/
	private boolean isBootException;
	/**
	 * 是否是第一次启动
	 **/
	private boolean isFirstStart = true;
	/**
	 * service被杀之后，重启的一个广播
	 **/
	private DTVServiceReboot reBootUIReceiver = null;

	/**
	 * 节目过滤的对话框
	 **/
	private FilterChannels mFilterChannel;
	/**
	 * 是否弹出自动搜索的对话框
	 **/
	private boolean isShowAutoScan = true;
	/**
	 * 是否弹出搜索向导的对话框
	 **/
	private static boolean isShowGuideScan = false;
	/**
	 * 是否进入运营商设置
	 **/
	public static boolean isEnterOperaterDirect;
	/**
	 * 是否开机搜索开始
	 **/
	private static boolean isDTV_BOOT_SVC_SCAN_START = false;
	/**
	 * 解决首次开机向导中，突然进入屏保的bug
	 **/
	private static MenuSearchGuide guideMenu;
	private int mChannelListIndex = -1;

	/**
	 * 显示等待服务对话框消息 										0x11
	 */
	private final int SHOW_DIALOG = 0x11;
	/**
	 * 返回DTV时，prepare 资源准备好的消息 				0x12
	 */
	private final int DTV_RESUME_OK = 0x12;
	/**
	 * 取消等待服务的对话框消息 									0x13
	 */
	private final int DISSMISS_DIALOG = 0x13;
	/**
	 * DtvResume 开始处理的消息									0x14
	 */
	private final int DTV_RESUME_BEGIN = 0x14;
	/**
	 * service被杀后重启的消息										0x15
	 */
	private final int DTV_SERVICE_RESTAR = 0x15;
	/**
	 * 从其他源过来，但是boot尚未执行，这时候启动boot的消息 0x16
	 */
	private final int DTV_START_BOOT = 0x16;
	/**
	 * 强制去抢占资源的消息，主要是为了在启动时候有服务更新的状态，从ATV源下切回来，停止播放ATV的节目 0x17
	 */
	private final int DTV_FORCE_PREPARE = 0x17;
	/**
	 * 节目改变/换台的消息											0x18
	 */
	private final int CHANNEL_CHANGED = 0x18;
	/**
	 * 退出极简设置的消息												0x20
	 */
	private final int DISMISS_VSETTING_MENU = 0x20;// 2015-2-10 YangLiu

	/**
	 * 语音换台传递的参数
	 **/
	private Intent activityIntent;

	/**
	 * 显示提示信息Dialog
	 **/
	private VchCommonToastDialog mToastDialog = null;

	/**
	 * 保存当前节目信息，解决AC开关机当前频道值变化为1
	 */
	private static SharedPreferences DtvChannelData;
	private static Editor DtvChanneleditor;

	/**
	 * 连续换台5次，提出“按OK键查看更多” 消息 2015-6-17
	 */
	private int dtvroot_monitoer_time_channel_num = 0;
	private Thread channel_Monitoring_Thread = null;

	/**
	 * 我的模式—>图像显示出来后按主页可以退出显示 2015-6-17
	 */
	private ProgressbarItemsSetForTheme progressbar_pq_theme;

	/**
	 * 判断是否是OLED，添加15秒退出菜单功能 2015-2-10
	 */
	private PanelInfo mPanelInfo = null;
	private MiscManager mMiscManager = null;
	private TVManager mTVManager = null;
	private String p_mOnStartTime = null;

	/**
	 * 多窗口菜单
	 */
	private WindowManager mWindowManager = null;
	private WindowManager.LayoutParams mWinLayoutParams = null;
	private ImageView mImageView = null;
	private int pos1 = 0;
	private int pos2 = 0;
	private int pos3 = 200;
	private int pos4 = 200;

	/**
	 * 保存当前的频道信息 		解决AC开关机当前频道值变化为1 	fengy 2014-3-14
	 *
	 * @param TmpNum
	 */
	public static void StoreCurrenChNum(int TmpNum) {
		Log.i(TAG, "fyyyy111111 StoreCurrenChNum   TmpNum= " + TmpNum + "\nDtvChanneleditor=" + DtvChanneleditor);
		if (DtvChanneleditor != null) {
			DtvChanneleditor.putInt("CurrentChNum", TmpNum);
			DtvChanneleditor.commit();
		}
	}

	public static int GetCurrentChNum() {
		int CurrentChNum = 0;
		Log.i(TAG, "fyyyy2222222 DtvChannelData=" + DtvChannelData);
		if (DtvChannelData != null) {
			CurrentChNum = DtvChannelData.getInt("CurrentChNum", 0);
		}
		return CurrentChNum;
	}

	/**
	 * 保存DTV版本等信息		转存/data/dtv/DtvVersionInfo/ 	FY 2014-2-10
	 */
	private void saveDtvVersionInfo() {
		final String FILE_PATH_DES = "/data/dtv/DtvVersionInfo/";
		final String XML_FILE = "DtvUIVersionHistory.xml";
		File outFile_xml = new File(FILE_PATH_DES, XML_FILE);
		BufferedReader bReader = null;
		FileWriter fw = null;
		InputStream input = null;
		try {
			File folder = new File(FILE_PATH_DES); // 创建文件夹
			File data = new File(FILE_PATH_DES, XML_FILE); // 创建xml文件
			if (!folder.exists()) { // 若不存在，创建目录，可以在应用启动的时候创建
				folder.mkdirs();
			}
			if (!data.exists()) {
				data.createNewFile();
			}
			AssetManager assetManager = getAssets(); // 处理/assets目录
			input = assetManager.open(XML_FILE);
			InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
			bReader = new BufferedReader(inputReader);
			fw = new FileWriter(data);
			String line = "";
			while ((line = bReader.readLine()) != null) {
				fw.write(line);
			}
			inputReader.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得DTV版本信息
	 *
	 * @return
	 */
	private String getDtvInfo() {
		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo("com.changhong.tvos.dtv", PackageManager.GET_CONFIGURATIONS);
			String versionName = pinfo.versionName;
			String versionCode = String.valueOf(pinfo.versionCode);
			String versionInfo = "\n\n当前DTV版本为：" + versionName + "——" + versionCode;
			Log.i(TAG, versionInfo);
			return versionInfo;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "没有此应用信息");
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取版本信息，判断是否已经升级，并给与自动搜索提示.当为单T的情况时，则不给予升级提示，只给与收索节目提示
	 *
	 * @author enlong
	 */
	private void getDtvinfo() {
		try {
			ApplicationInfo pkg = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			// String version = pkg.metaData.getString("uiversion");
			// Log.i(TAG, "configOperator()--> getversion " + version);

			Bundle bundle = pkg.metaData;
			String strchipmodel = bundle.getString("ChipModel");
			Log.i("configOperator", "--> strchipmodel " + strchipmodel);

			String struiversion = bundle.getString("UIVersion");

			String strhardversion = bundle.getString("HardVersion");
			Log.i("configOperator", " strhardversion " + strhardversion);

			String strapkmainversion = bundle.getString("apkMainVersion");// apkmainversion
			Log.i("configOperator", " strapkmainversion " + strapkmainversion);

			String strapksubversion = bundle.getString("apkSubVersion");
			Log.i("configOperator", " strapksubversion " + strapksubversion);

			String strReleaseTime = bundle.getString("apkReleaseTime");
			Log.i("configOperator", " strReleaseTime " + strReleaseTime);

			DtvSoftWareInfoManager.setmUIVersion(strchipmodel, struiversion, strhardversion, strapkmainversion, strapksubversion, strReleaseTime);// 保存读到的DTV信息
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "configOperator()-->" + e);
		}
	}

	/******************************开启DTV更新和第三方应用**********************************
	 ***************startDtvUpdateService，startWasuApplication，startThirdApp***********
	 ******************************************************************************************/
	/**
	 * 启动DTV数字电视升级服务
	 */
	private void startDtvUpdateService() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		Log.i(TAG, "LL OperatorCode = " + mOperatorManager.getCurOperator().getOperatorCode());
		intent.putExtra("CurOperator", mOperatorManager.getCurOperator().getOperatorCode());
		ComponentName cn = new ComponentName("com.changhong.tvos.dtv.update", "com.changhong.tvos.dtv.update.dtvUpdateService");// 用来打开其他应用程序中的Activity或服务
		intent.setComponent(cn);
		startService(intent);
		Log.i(TAG, "LL startDtvUpdateService()****");
	}

	/**
	 * 保存服务的包名
	 *
	 * @param packageName
	 */
	private void writePresistServFile(String packageName) {
		boolean isExists = false;
		File file = new File("/data/preapp/presist_serv");
		if (!file.exists()) {
			try {
				File dir = new File("/data/preapp");
				if (!dir.exists()) {
					dir.mkdirs();
					Runtime.getRuntime().exec("chmod 777 " + dir.getAbsolutePath());
					Log.i(TAG, "LL create dir\"/data/preapp\"*** dirName = " + dir.getAbsolutePath());
				}
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
				Log.i(TAG, "LL create file\"/data/preapp/presist_serv\"*** fileName = " + file.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "LL create file \"/data/preapp/presist_serv\" failed>>IOException***");
			}
		}
		BufferedReader bReader = null;
		BufferedWriter bWriter = null;
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String data = null;
			while ((data = bReader.readLine()) != null) {
				if (data.contains(packageName)) {
					isExists = true;
					Log.i(TAG, "LL the file \"/data/preapp/presist_serv\" has contained \"" + packageName + "\"***");
					break;
				}
			}
			if (!isExists) {
				bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				bWriter.newLine();
				Log.i(TAG, "LL write \"" + packageName + "\" to the file \"/data/preapp/presist_serv\"***");
				bWriter.write(packageName);
				bWriter.newLine();
				bWriter.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL read or write file failed>>IOException***");
		} finally {
			try {
				if (bWriter != null) {
					bWriter.close();
					Log.i(TAG, "LL close bReader bWriter success***");
				}
				if (bReader != null) {
					bReader.close();
					Log.i(TAG, "LL close bReader buffer success***");
				}
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(TAG, "LL close buffer failed>>IOException***");
			}
		}
	}

	/**
	 * 是否成功启动华数的apk
	 *
	 * @return
	 */
	private boolean startWasuApplication() {
		boolean success = false;
		PackageInfo packageInfo = null;

		try {
			packageInfo = DtvRoot.this.getPackageManager().getPackageInfo("cn.com.wasu.main", 0);

		} catch (NameNotFoundException e) {
			packageInfo = null;
			Log.i(TAG, "LL startWasuApplication()>>NameNotFoundException***");
		}
		if (packageInfo != null && isStartWasuApp == true) {
			isStartWasuApp = false;
			Intent intent = new Intent();
			intent.setClassName("cn.com.wasu.main", "cn.com.wasu.main.WelcomeActivity");
			// intent.setClassName("com.changhong.test","com.changhong.test.TestActivity");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Log.i(TAG, "LL startWasuApplication()***");
			DtvRoot.this.startActivity(intent);
			success = true;
			this.writePresistServFile("cn.com.wasu.main");// 保存服务的包名
		}
		Log.i(TAG, "LL startWasuApplication() " + success + "***");// /////
		return success;
	}

	/**
	 * 判断是否启动第三方的apk
	 *
	 * @return boolean
	 */
	private boolean startThirdApp() {
		boolean success = false;
		int opCode = -1;
		DtvOperator operator = mOperatorManager.getCurOperator();
		if (operator != null) {
			opCode = operator.getOperatorCode();
		}
		switch (opCode) {
			case ConstOperatorCode.OPERATOR_CODE_HANGZHOU_WASU:
				success = this.startWasuApplication();
				break;

			default:
				break;
		}
		Log.i(TAG, "LL startThirdApp " + success + "***");// /////
		return success;
	}

	/**
	 * 自适应分辨率
	 */
	private void autoAdaptResolution() {
		DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics(); // 将当前窗口的一些信息放在DisplayMetrics类中
		mDisplayMetrics.densityDpi = 160 * mDisplayMetrics.widthPixels / 1280;// 在每英寸160点的显示器上，1dp = 1px
		mDisplayMetrics.density = (float) mDisplayMetrics.densityDpi / 160; // density值表示每英寸有多少个显示点
		mDisplayMetrics.scaledDensity = mDisplayMetrics.density;// 缩放比例
		mDisplayMetrics.xdpi = mDisplayMetrics.densityDpi;
		mDisplayMetrics.ydpi = mDisplayMetrics.densityDpi;
		Log.i(TAG, "LL widthPixels = " + mDisplayMetrics.widthPixels + ",densityDpi = " + mDisplayMetrics.densityDpi + ",density = " + mDisplayMetrics.density + ",scaledDensity = "
				+ mDisplayMetrics.scaledDensity + ",xdpi = " + mDisplayMetrics.xdpi + ",ydpi = " + mDisplayMetrics.ydpi);
		// getResources().updateConfiguration(null, mDisplayMetrics, null);
		getResources().updateConfiguration(null, mDisplayMetrics);
	}

	/**********************************检测运营商和更新频道**********************************
	 *********configOperator，checkOperateChanged，updateChannelListIfRequired*******
	 ******************************************************************************************/
	/**
	 * 检测运营商信息
	 */
	private void configOperator() {
		Log.i(TAG, "LL start configOperator()*****");
		if (mChannelManager.GetDtvReportFlag() == false) {
			mChannelManager.setDtvReportFlag(true);
			Log.i(TAG, "call reportDtvChannelInfo");
			reportDtvChannelInfo();// 获取DTV频道信息
		}
		if (mDtvCommonManager.checkVersion(DtvSoftWareInfoManager.getCheckVersion())) {

			if (mSourceManager.getProductType() == ConstProductType.PRODUCT_T) {
				// 单T 清流 无运营商选择，只有无节目搜台提示
				startBootService();// 启动DTV的boot服务，包括CA初始化，播放，向系统获取资源等
				// 唐超 add start
				DtvRoot.this.startDtvUpdateService();// 启动ＤＴＶ数字电视升级服务
				// 唐超 add end
				return;
			}// 搜台提示
			mCommonAcionDialog = new CommonAcionDialog(DtvRoot.this, R.string.dtv_menu_after_upgrade_setup_title, 0, R.string.dtv_menu_after_upgrade_setup_content, 30);
			mCommonAcionDialog.setCancelable(false);
			mCommonAcionDialog.setShowTV(false);
			mCommonAcionDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_CANCEL);
			mCommonAcionDialog.setDuration(30);
			mCommonAcionDialog.setOkButtonText(R.string.sure);
			mCommonAcionDialog.setCancelButtonText(R.string.ignore);
			mCommonAcionDialog.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface arg0) {
					// TODO Auto-generated method stub
					DtvRoot.removeScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(true);
					mPromptInfoView.hide();
				}
			});
			mCommonAcionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					if (!isEnterOperaterDirect) {
						// mOperatorManager.setCurOperator(mOperatorManager.getCurOpIndex());
						startBootService();
						// 唐超 add start
						DtvRoot.this.startDtvUpdateService();
						// 唐超 add end
					}
				}
			});
			mCommonAcionDialog.setOKButtonListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isEnterOperaterDirect = true;

					/*DtvSourceManager sourceManager = DtvSourceManager.getInstance();
					List<DTVSource> sourceList = sourceManager.getSourceList();

					mOperatorManager.setCurOperator(mOperatorManager.getCurOpIndex());
					if (sourceList != null && sourceList.size() <= 1) {
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_ENTER, ConstPageDataID.OPERATOR_LIST_PAGE_DATA);
					} else {
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_ENTER, ConstPageDataID.SOURCE_SETUP_PAGE_DATA);
					}*/

					MenuOperation mMenuOperation = new MenuOperation(DtvRoot.this);
					mMenuOperation.show();
					mMenuOperation.setShowTv(true);
					mMenuOperation.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface arg0) {
							// TODO Auto-generated method stub
							startBootService();
							// 唐超 add start
							DtvRoot.this.startDtvUpdateService();
							isEnterOperaterDirect = false;
						}
					});

					mMenuOperation.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							// TODO Auto-generated method stub
							startBootService();
							// 唐超 add start
							DtvRoot.this.startDtvUpdateService();
							isEnterOperaterDirect = false;
						}
					});
					mCommonAcionDialog.setShowTV(true);
					mCommonAcionDialog.dismiss();
				}
			});
			mCommonAcionDialog.setCancelButtonListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isEnterOperaterDirect = false;
					mCommonAcionDialog.setShowTV(false);
					mCommonAcionDialog.dismiss();
				}
			});
			mCommonAcionDialog.show();

		} else {
			// enlong add to if oprator changed and the channel list is null warning to search
			startBootService();
			// 唐超 add start
			DtvRoot.this.startDtvUpdateService();
			// 唐超 add end
		}
		Log.i(TAG, "LL end configOperator()*****");
	}

	/**
	 * 检查运营商是否改变（用于显示判断是否需要提示收索和显示收索）
	 *
	 * @return
	 */
	private boolean checkOperateChanged() {
		String tmpOpChange = DtvConfigManager.getInstance().getValue(ConstOperatorState.OPCHANGED);

		String tmpGuide = DtvConfigManager.getInstance().getValue(ConstOperatorState.OP_GUIDE);
		boolean isOpratorChaged = Boolean.valueOf(tmpOpChange);

		boolean isGuideMode = Boolean.valueOf(tmpGuide);

		List<DtvProgram> channelList = mChannelManager.getChannelList();
		this.isShowGuideScan = false;

		DtvConfigManager.getInstance().setValue(ConstOperatorState.OPCHANGED, "false");

		if ((channelList == null || channelList.size() <= 0)) {

			if (isGuideMode) {
				// DtvConfigManager.getInstance().setValue(
				// ConstValueClass.ConstOperatorState.OP_GUIDE, "false");
				this.isShowGuideScan = true;
				showGuideScan(true);
				return false;
			}
			/*if (isOpratorChaged)
			{
				Log.i(TAG, "DTVRoot->configOperator()-> oprator changed and the channel list is null");
				if (isShowAutoScan) {
					mPromptInfoView.hide();
					showScanWarning();
					return true;
				}
			}*/
			if (isShowAutoScan) {
				DtvRoot.removeScreenSaverMessages();
				mDtvCommonManager.updateKeyboardConvertFlag(true);
				mPromptInfoView.hide();
				Log.i("YangLiu", "checkOperateChanged-->channelList.size=" + channelList.size());
				showScanWarning();
				return true;
			}
		}
		return false;
	}

	/**
	 * 按需要更新频道列表信息
	 */
	public static void updateChannelListIfRequired() {
		if (true == DtvRoot.isNeedUpdateChannelList) {
			DtvChannelManager.getInstance().updateCurChannelList();
			DtvRoot.isNeedUpdateChannelList = false;
		}
	}

	/*****************************************换台*****************************************
	 ***************isChangeChanenlRightNow，changeChannelForVoice***************
	 ***************************************************************************************/
	/**
	 * 智能判断是否需要立刻换台： 判断机制， 输入的数字*10 大于最大节目号，即可立刻换台
	 *
	 * @param curNum
	 * @return boolean
	 * @author enlong
	 */
	private boolean isChangeChanenlRightNow(int curNum) {
		// TODO Auto-generated method stub
		int maxChannelNum = mChannelManager.getMaxChannelNum();
		boolean isChangeRight = false;
		if (curNum < maxChannelNum) {
			if (curNum * 10 > maxChannelNum) {
				isChangeRight = true;
			} else {
				isChangeRight = false;
			}
		} else if (curNum == maxChannelNum) {
			isChangeRight = true;
		} else {
			isChangeRight = true;
		}
		return isChangeRight;
	}

	/**
	 * 转换语音换台命令
	 *
	 * @param voiceChannel   语音节目号参数
	 * @param isFirstControl 是DTV第一次启动么？
	 */
	private void changeChannelForVoice(int voiceChannel, boolean isFirstControl) {
		if (-1 != voiceChannel) {
			if (null != mFilterChannel) {
				if (mFilterChannel.isFilter()) {
					Log.i(TAG, "cancelSmartSkip beacause voiceChannel want to change");
					mFilterChannel.stopFilterInOtherThread();
				}
			}
			int tempVoiceChannel = voiceChannel;
			if (0 != (voiceChannel & 0x40000000)) {
				int sourceID = DtvScheduleManager.convertSpecCode2SourceID((voiceChannel & 0x30000000) >> 28);
				if (mSourceManager.getCurSourceID() != sourceID) {
					mSourceManager.setCurSource(sourceID);
					mChannelManager.setCurChannelType(mChannelManager.getBootChannelType());
				}
				tempVoiceChannel &= 0x8fffffff;
			}
			if (0 != (tempVoiceChannel & 0x80000000)) {
				mInputIndex = tempVoiceChannel & 0x7fffffff;
				Log.i(TAG, "LL changeChannelForVoice()>>schedule>>timeout>>mInputIndex = " + mInputIndex);
				int channelType = mChannelManager.getChannelTypeByChannelIndex(mInputIndex);
				if (mChannelManager.getCurChannelType() != channelType) {
					Log.i(TAG, "LL changeChannelForVoice()>>needChangeChannelType>>channelType = " + channelType + ",curChannelType = " + mChannelManager.getCurChannelType());
					if (false == mChannelManager.changeChannelTypeButNoPlay()) {
						Log.e(TAG, "LL changeChannelForVoice()>>needChangeChannelType>>failed to changeChanelType***");
					}
				}
				if (null == mChannelManager.getProgramByServiceIndexIfExists(mInputIndex)) {
					Log.e(TAG, "LL changeChannelForVoice()>>schedule>>not find>>mInputIndex = " + mInputIndex);

					if (isFirstControl) {
						mInputNum = mChannelManager.getBootChannelNum();
						Log.i(TAG, "changeChannelForVoice first start change to bootNum: " + mInputNum);
						mHandler.removeCallbacks(mRunForceProgNum);
						mHandler.post(mRunForceProgNum);
						return;
					} else {

						mInputIndex = mChannelManager.getCurPorgramServiceIndex();
					}
				}
				mHandler.removeCallbacks(mRunForceProgIndex);
				mHandler.post(mRunForceProgIndex);
			} else {
				mInputNum = tempVoiceChannel;
				Log.i(TAG, "LL changeChannelForVoice()>>voiceChannel>>receive>>mInputNum = " + mInputNum);
				if (null == mChannelManager.getProgramByNumIfExists(mInputNum)) {
					mInputNum = mChannelManager.getCurProgramNum();
					Log.e(TAG, "LL changeChannelForVoice()>>voiceChannel>>not found>>mInputNum = " + mInputNum);
					if (isFirstControl) {
						mInputNum = mChannelManager.getBootChannelNum();
					}
				}
				mHandler.removeCallbacks(mRunForceProgNum);
				mHandler.post(mRunForceProgNum);
			}
		}
	}

	/*****************************************监听按键*****************************************
	 *********************************onKeyUp和onKeyDown*********************************
	 *******************************************************************************************/
	/**
	 * onKeyUp事件
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL DtvRoot>>onKeyUp() keyCode = " + keyCode + "	isBeganBootService = " + isBeganBootService + "	isWaitingService= " + isWaitingService);
		if (false == isStartControlEnd || true == isWaitingService) {
			return super.onKeyUp(keyCode, event);
		}

		Log.i(TAG, "LL onKeyUp()>>keyCode = " + keyCode);
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_CHANNEL_UP:
			case KeyEvent.KEYCODE_CHANGHONGKB_CHANNEL_UP:
			/*isReturnLastSource = false;
			if (mChannelListIndex != -1) {
				mChannelManager.channelForceChangeReplay();
			}
			mChannelInfoView.show();
			mPromptInfoView.hideDelay(mDelayTime);*/

				/** added by YangLiu 2015-5-4 */
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 1>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);
				// mChannelListIndex = mChannelManager.channelChangeUp(false);
				// 如果参数为true节目就会直接播放
				if (mChannelListIndex != -1) {
					mChannelManager.channelForceChangeReplay();
					return_source_back = false;
				}
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 2>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);

				mChannelInfoView.show();
				mPromptInfoView.hideDelay(mDelayTime);
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 3>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);

				DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.LocalProgram);
				Log.i("YangLiu", "dtv上键换台  " + DtvChannelManager.getInstance().getViewSource());
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 4>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);
				break;

			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
			case KeyEvent.KEYCODE_CHANGHONGKB_CHANNEL_DOWN:
			/*isReturnLastSource = false;
			if (mChannelListIndex != -1) {
				mChannelManager.channelForceChangeReplay();
			}
			mChannelInfoView.show();
			mPromptInfoView.hideDelay(mDelayTime);*/

				/** added by YangLiu 2015-5-4 */
				// mChannelListIndex = mChannelManager.channelChangeDown(false);
				if (mChannelListIndex != -1) {
					mChannelManager.channelForceChangeReplay();
					return_source_back = false;
				}
				mChannelInfoView.show();
				mPromptInfoView.hideDelay(mDelayTime);

				DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.LocalProgram);
				Log.i("YangLiu", "dtv下键换台  " + DtvChannelManager.getInstance().getViewSource());
				break;

			case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD: // KEYCODE_CHANGHONGIR_SOFTKEYBOARD
				if (mAdjustCHSKM == null) {
					mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(DtvRoot.this);
				}
				if (null != mAdjustCHSKM) {
					if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {

						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
					} else {
						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_LEFT);
						return true;
					}
				}
				break;

			default:
				break;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * onKeyDown事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "LL DtvRoot>>onKeyDown keyCode=" + keyCode + "	isStartControlEnd=" + DtvRoot.isStartControlEnd + "	isWaitingService=" + isWaitingService + "	isBeganBootService: "
				+ isBeganBootService);
		if (false == isStartControlEnd || true == isWaitingService) {
			return true;
		}
		DtvRoot.handleScreenSaverMessages();// 添加屏保延时

		Log.i(TAG, "LL onKeyDown>>ScanCode = " + keyEvent.getScanCode());
		switch (keyEvent.getScanCode()) {
			case 231:// keyboard Menu
				Log.i(TAG, "LL onKeyDown>>keyboard Menu mEasySettingFlag: " + mEasySettingFlag);
				if (mEasySettingFlag == false) {
					if (mMainMenuRootData != null) {
						mMainMenuRootData.VSettingMenuVisibilityControl(true);
					}
					// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_ENTER);//deleted by cuixy 20140607

					if (isOLED()) {// 2015-2-10 YangLiu isOLED()
						mHandler.removeMessages(DISMISS_VSETTING_MENU);
						mHandler.sendEmptyMessageDelayed(DISMISS_VSETTING_MENU, (15 - getIntervalTime()));
					}
				}
				return true;
			case 233:// keyboard Channel Up
			case 234:// keyboard Channel Down
				break;
			case 232:// keyboard Source
			case 235:// keyboard Volume Down
			case 236:// keyboard Volume Up
				return false;
			default:
				break;
		}

		Log.i(TAG, "LL onKeyDown>>keyCode = " + keyCode);
		switch (keyCode) {
			case KeyEvent.KEYCODE_YELLOW:
			/*MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(menuSelfSortDialog);
			MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
			menuSelfSortDialog.setShowTV(false);*/
				MainMenuRootData.dismissSelfSortDialog(false);
				break;

			case KeyEvent.KEYCODE_GREEN:
				MenuColorKey dtvColorKeyMenu = new MenuColorKey(DtvRoot.this, keyCode);
				dtvColorKeyMenu.show();
				break;

		/*case KeyEvent.KEYCODE_RED:
			if (mChannelManager.changeChannelType() == true) {
				isReturnLastSource = false;
				mPromptInfoView.hideDelay(mDelayTime);
			} else {
				CommonInfoDialog myDialog = new CommonInfoDialog(DtvRoot.this);
				myDialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
				if (mChannelManager.getCurChannelType() == ConstServiceType.SERVICE_TYPE_TV) {
					myDialog.setMessage(DtvRoot.this.getString(R.string.NoRadioChannel));
				} else {
					myDialog.setMessage(DtvRoot.this.getString(R.string.NoDtvChannel));
				}
				myDialog.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
				myDialog.show();
			}
			mChannelInfoView.show();
			break;*/

			case KeyEvent.KEYCODE_CHANGHONGKB_MENU:
			case KeyEvent.KEYCODE_MENU:
				if (this.isResumed()) {
					if (mEasySettingFlag == false) {
						if (mMainMenuRootData != null) {
							mMainMenuRootData.VSettingMenuVisibilityControl(true);
							// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_ENTER);//deleted by cuixy 20140607

							if (isOLED()) {// 2015-2-10 YangLiu
								mHandler.removeMessages(DISMISS_VSETTING_MENU);
								mHandler.sendEmptyMessageDelayed(DISMISS_VSETTING_MENU, (15 - getIntervalTime()));
							}
						}
					}
				}
				if (null != mAdjustCHSKM) {
					if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
					}
				}
				break;

			// 极简设置遥控器左右键不要去调节音量，预留着以防功能扩充 多媒体刘珊珊提出的 2014.06.13
			// 应杨恩泽邮件反馈的试收试看要求把左右键切换音量功能添加回来，和ATV暂时保持一致 2014.08.22
			case KeyEvent.KEYCODE_DPAD_LEFT:
				// 将当前音量-1，且显示系统UI
				int keyVolumeDownCount = keyEvent.getRepeatCount();
				if (keyVolumeDownCount >= 15) {
					int tmpVolumeValue = mAdjustAudioManager.getStreamVolume();
					tmpVolumeValue -= 3;
					if (tmpVolumeValue < 0) {
						tmpVolumeValue = 0;
					}
					mAdjustAudioManager.setStreamVolume(tmpVolumeValue);
				} else {
					mAdjustAudioManager.adjustStreamVolume(AudioManager.ADJUST_LOWER);
				}
				break;

			case KeyEvent.KEYCODE_DPAD_RIGHT:
				// 将当前音量+1，且显示系统UI
				mAdjustAudioManager.adjustStreamVolume(AudioManager.ADJUST_RAISE);
				break;

			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_CHANNEL_UP:
			case KeyEvent.KEYCODE_CHANGHONGKB_CHANNEL_UP:
				// this.isReturnLastSource = false;
				// SetReturnLastSourceFlag(false);

				/** changed by YangLiu 2015-05-04 */
				mChannelListIndex = mChannelManager.channelChangeUp(false);// 如果参数为true节目就会直接播放

				mChannelInfoView.show();
				mPromptInfoView.hideDelay(mDelayTime);
			/*if (0 == keyEvent.getRepeatCount()) {//单次按键 p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 1>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);
				mChannelListIndex = mChannelManager.channelChangeUp(false);
				
				if (mChannelListIndex != -1) {// 如果参数为true节目就会直接播放 
					mChannelManager.channelForceChangeReplay();
					return_source_back = false;
				}
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 2>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);

				mChannelInfoView.show();
				mPromptInfoView.hideDelay(mDelayTime);
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 3>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);

				// lyw add 2015年2月10日17:01:16
				DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.LocalProgram);
				Log.i("liuyuwang", "dtv上键换台  " + DtvChannelManager.getInstance().getViewSource());
				p_mOnStartTime = getCurrentTime_print().toString();
				Log.i("pang", "LL 4>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + p_mOnStartTime);
			}*/
				break;

			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
			case KeyEvent.KEYCODE_CHANGHONGKB_CHANNEL_DOWN:
				// SetReturnLastSourceFlag(false);

				/** changed by YangLiu 2015-05-04 */
				mChannelListIndex = mChannelManager.channelChangeDown(false);// 如果参数为true节目就会直接播放

				mChannelInfoView.show();
				mPromptInfoView.hideDelay(mDelayTime);

			/*mChannelListIndex = mChannelManager.channelChangeDown(false);
			if (mChannelListIndex != -1) {
				mChannelManager.channelForceChangeReplay();
				return_source_back = false;
			}
			mChannelInfoView.show();
			mPromptInfoView.hideDelay(mDelayTime);

			// lyw add 2015年2月10日17:01:16
			DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.LocalProgram);
			Log.i("liuyuwang", "dtv下键换台  " + DtvChannelManager.getInstance().getViewSource());*/
				break;

			case KeyEvent.KEYCODE_BACK:
				Log.i("KEYCODE_BACK", "isReturnLastSource = " + isReturnLastSource);
				Log.i(TAG, "isNotDTVChanged = " + isNotDTVChanged);
				/** 酒店电视版*/
			/*if (isReturnLastSource & isNotDTVChanged) {
				//mDtvCommonManager.returnLastInputSource();
				Intent mIntent = new Intent("com.changhong.system.systemkeyfor3rd");
				mIntent.putExtra(Intent.EXTRA_KEY_EVENT, 4);
				mContext.sendBroadcast(mIntent);
			} else {
				if (return_source_back == true) {
					return_source_back = false;
					//mDtvCommonManager.returnLastInputSource();
					Intent mIntent = new Intent("com.changhong.system.systemkeyfor3rd");
					mIntent.putExtra(Intent.EXTRA_KEY_EVENT, 4);
					mContext.sendBroadcast(mIntent);
				} else {
					mChannelManager.channelChangeReturn();
					mChannelInfoView.show();
					mChannelInfoView.updateEpgShow();
				}
			}*/

				/** 正常发布版*/
				if (isReturnLastSource & isNotDTVChanged) {
					mDtvCommonManager.returnLastInputSource();
				} else {
					if (return_source_back == true) {
						return_source_back = false;
						mDtvCommonManager.returnLastInputSource();
					} else {
						mChannelManager.channelChangeReturn();
						mChannelInfoView.show();
						mChannelInfoView.updateEpgShow();
					}
				}
				// isReturnLastSource = false;
				// SetReturnLastSourceFlag(false);
				mPromptInfoView.hideDelay(mDelayTime);
				return true;

			case KeyEvent.KEYCODE_0:// 数字键控制频道选择
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
				mHandler.removeCallbacks(mRunnable);
				// mChannelInfoView.hide();
				if (mInputNum < 1000) { // Support ChannelNum to 9999
					mInputNum = mInputNum * 10 + (keyCode - KeyEvent.KEYCODE_0);
				} else {
					mInputNum = (keyCode - KeyEvent.KEYCODE_0);
				}
				// mInputNumView.setText(this.getString(R.string.dtv_info_tv) + " " + mInputNum);
				// mInputNumView.setVisibility(View.VISIBLE);
				mChannelInfoView.inputChannelNum("" + mInputNum);
				if (isChangeChanenlRightNow(mInputNum)) {
					mHandler.post(mRunnable);
				} else {
					if (null != mAdjustCHSKM && mAdjustCHSKM.isSoftKeyPanelOnShow()) {
						Log.i(TAG, "SKM mInputNum=" + mInputNum);
						mHandler.postDelayed(mRunnable, 6000);
					} else {
						mHandler.postDelayed(mRunnable, 2000);
					}
				}
				break;

			case KeyEvent.KEYCODE_INFO:// KEYCODE_STAIR_INFOR:
				DtvRoot.updateChannelListIfRequired();
				if (mChannelInfoView.isShown()) {
					mChannelInfoView.hide();
				} else {
					mChannelInfoView.show();
					mChannelInfoView.updateEpgShow();
				}
				//	测试语音换台使用
			/*Intent intent = new Intent();
			intent.setClass(DtvRoot.this, DtvRoot.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("ChannelNum", 24);
			startActivity(intent);*/
				break;

			//	MStar628平台上按【缩小】键会出现数字电视停止运行提醒，暂时关闭掉。
		/*case KeyEvent.KEYCODE_CHANGHONGIR_PINCH:
			Intent intentLaucher = new Intent();
			intentLaucher.putExtra("keycode", KeyEvent.KEYCODE_CHANGHONGIR_HOME);
			intentLaucher.setClassName("com.changhong.system.launcher", "com.changhong.system.launcher.Launcher");
			intentLaucher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			DtvRoot.this.startActivity(intentLaucher);
			break;*/
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
			/*List<DtvProgram> channelListTemp = DtvChannelManager.getInstance().getChannelList();
			if (channelListTemp == null || channelListTemp.size() <= 0) {
				Log.e(TAG, "LL there is no any channel***");
				break;
			}
			if (null == mQuickChannel) {
				mQuickChannel = new MenuQuickChannel(DtvRoot.this);
				mQuickChannel.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						MenuDisplayManager.getInstance(mContext).registerDialogShowing(mQuickChannel);
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});
				mQuickChannel.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(mQuickChannel);
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(false);
						mPromptInfoView.show();
					}
				});
			}
			mQuickChannel.show();
			break;
			case KeyEvent.KEYCODE_BLUE:*/
			case KeyEvent.KEYCODE_CHANGHONGIR_EPG:
				// case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL: fy modify

			/*DtvRoot.updateChannelListIfRequired();
			List<DtvProgram> channelList = mChannelManager.getChannelList();
			if (channelList == null || channelList.size() <= 0) {
				CommonInfoDialog mDialog = new CommonInfoDialog(mContext);
				mDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
				mDialog.show();
				break;
			}
			if (mMenuEpg == null) {
				mMenuEpg = new MenuEpg_NEW_UI(DtvRoot.this);
				
				mMenuEpg.setOnShowListener(new DialogInterface.OnShowListener() {
					@Override
					public void onShow(DialogInterface arg0) {
						// TODO Auto-generated method stub 
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});

				mMenuEpg.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub 
						// if(MenuEpg_NEW_UI.mMenuScheduleEdit == null ||  !MenuEpg_NEW_UI.mMenuScheduleEdit.isShowing()){
						DtvRoot.handleScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(false);
						mPromptInfoView.show();
						if (mMenuEpg.isShowTvChannelInfo()) {
							mChannelInfoView.show();
							mChannelInfoView.updateEpgShow();
							mMenuEpg.setShowTvChannelInfo(false);
							mPromptInfoView.hide();
							mMenuEpg.show();
						}
					}
				});
			}*/

				/**打开EPG信息时判断节目列表，为空提示搜索**/
				if (hintToast != null) {
					hintToast.cancel();
				}
				DtvRoot.updateChannelListIfRequired();
				List<DtvProgram> channelList = mChannelManager.getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					showScanWarning();
					break;
				}

				if (is5508Q2) {
					// 2015-4-8 YangLiu 当在广播SERVICE_TYPE_RADIO下不能进入EPG，进入会由于EPG的静态节目列表导致没有及时更新改变后的频道列表而挂掉
					int curChannelType = DtvChannelManager.getInstance().getCurChannelType();
					if (curChannelType == ConstServiceType.SERVICE_TYPE_TV) {
						Intent intent = new Intent(mEPGActivity, com.changhong.tvos.dtv.epg.normal.EpgActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						mEPGActivity.startActivity(intent);
					}
				} else {
					Intent intent = new Intent(mEPGActivity, com.changhong.tvos.dtv.epg.minify.EpgActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					mEPGActivity.startActivity(intent);
				}
				break;

			case KeyEvent.KEYCODE_RED:
			/*DtvRoot.updateChannelListIfRequired();
			List<DtvProgram> channelList1 = mChannelManager.getChannelList();
			if (channelList1 == null || channelList1.size() <= 0) {
				showToastDialog(getResources().getString(R.string.dtv_menu_no_channel_prompt));
			}
			break;*/

			case KeyEvent.KEYCODE_CHANGHONGIR_QUICKCHANGE:
				// case 4129://快速换台
				final List<DtvProgram> channelListTemp = DtvChannelManager.getInstance().getChannelList();
				if (channelListTemp == null || channelListTemp.size() <= 0) {
					Log.e(TAG, "LL there is no any channel***");
					break;
				}
				if (null == mQuickChannel) {
					mQuickChannel = new MenuQuickChannel(DtvRoot.this);
					mQuickChannel.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).registerDialogShowing(mQuickChannel);
							DtvRoot.removeScreenSaverMessages();
							mDtvCommonManager.updateKeyboardConvertFlag(true);
							mPromptInfoView.hide();
							mRadioBackView.hide();
						}
					});
					mQuickChannel.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							// TODO Auto-generated method stub
							MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(mQuickChannel);
							DtvRoot.removeScreenSaverMessages();
							mDtvCommonManager.updateKeyboardConvertFlag(false);
							mChannelInfoView.show();
							mChannelInfoView.updateEpgShow();
							mPromptInfoView.show();
							mRadioBackView.show();
						}
					});
				}
				mQuickChannel.show();
				break;
			// case KeyEvent.KEYCODE_CHANGHONGIR_QUICKRECOMMEND:
			// case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			// case 4128: //智能推荐
			case KeyEvent.KEYCODE_CHANGHONGIR_SMARTRECOMMEND: // fy modify 2012
				Log.i(TAG, "LL sendBroadcast()>>smart recommendation***");
				Intent mIntent = new Intent("com.changhong.broadcast.ForStartSmartRecommend");
				mIntent.putExtra("scene", "DTV");
				sendBroadcast(mIntent);
				break;
		/*case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD:
			//KEYCODE_CHANGHONGIR_SOFTKEYBOARD 软键盘
			if (mAdjustCHSKM == null) {
				mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(DtvRoot.this);
			}
			if (null != mAdjustCHSKM) {
				if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {

					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
				} else {
					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_LEFT);
					return true;
				}
			}
			break;*/
			default:
				break;
		}
		return super.onKeyDown(keyCode, keyEvent);
	}

	/*******************************************等待服务启动**********************************
	 *******************************************************************************************
	 *******************************************************************************************/
	/**
	 * 等待服务启动， 如果时间未超过1s,提示对话框不显示。否则提示框显示，直到在获取服务后1s才消失
	 *
	 * @author enlong
	 */
	private void waitServiceStart() {
		//等待服务启动... 的Dialog
		if (null == waitDialog) {
			waitDialog = new CommonProgressInfoDialog(DtvRoot.this, R.string.dtv_wait_service, true, 10000);
			waitDialog.setCancelable(true);
			waitDialog.setButtonText(getString(R.string.menu_cancel));
			waitDialog.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent("com.changhong.system.systemkeyfor3rd");
					mIntent.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent.KEYCODE_CHANGHONGIR_HOME);
					mContext.sendBroadcast(mIntent);
				}
			});
			waitDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent("com.changhong.system.systemkeyfor3rd");
					mIntent.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent.KEYCODE_CHANGHONGIR_HOME);
					mContext.sendBroadcast(mIntent);
				}
			});
		}
		Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitDialog show");

		//DtvRoot的handler，控制等待服务重启的dialog显示
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Log.i(TAG, "DTVRoot handle msg: " + msg.what + " " + Thread.currentThread());
				switch (msg.what) {
					case START_ACTIVITY:// 启动DTV的初始化
						if (isWaitingService == false && isTimeArrive) {
							waitDialog.dismiss();
							getDtvinfo();// 获取版本信息，判断是否已经升级，并给与自动搜索提示.当为单T的情况时，则不给予升级提示，只给与搜索节目提示
							init();// 初始化所需要的各个manager注册接收广播等
							configOperator();// 单T 清流 无运营商选择，只有无节目搜台提示
							isFirstStart = false;
							DtvMessageThread.startLogOut();// 如果是调试模式，则记录Log
						}
						break;

					case SHOW_DIALOG:
						waitDialog.show();
						break;

					case DISSMISS_DIALOG:
						waitDialog.dismiss();
						break;

					case DTV_RESUME_OK:// DTV成功恢复
						if (isTimeArrive) {
							waitDialog.dismiss();
							isWaitingService = false;
						}
						// mPromptInfoView.show();
						// mDtvCommonManager.updateKeyboardConvertFlag(false);
						// DtvRoot.handleScreenSaverMessages();
						if (null != mRadioBackView) {
							mRadioBackView.show();
						}
						if (isReqStartAutoScan) {
							showAutoScanConfirm();
						}
						break;

					case CHANNEL_CHANGED:// 换台更新节目PF信息
						Log.i(TAG, "received msg:CHANNEL_CHANGED so channleInfo refresh pfevent");
						if (mChannelInfoView.isNeedShow) {
							mChannelInfoView.show();
							mChannelInfoView.updateEpgShow();
						}
						break;

					case DISMISS_VSETTING_MENU:// 15秒菜单退出菜单YangLiu
						if (isOLED() && MainMenuRootData.isVSettingShowing) {
							if (getIntervalTime() >= 15) {
								if (mMainMenuRootData != null) {
									if (MainMenuRootData.isScanning) {// 搜台 亮度减为50%
									/*MainMenuRootData.mMainMenu.getView().getBackground().setAlpha(10);
									mMainMenuRootData.ShowScanMenuDetail();*/
									} else {
										mMainMenuRootData.VSettingMenuVisibilityControl(false);// 关闭菜单
									}
								}
							} else {
								mHandler.removeMessages(DISMISS_VSETTING_MENU);
								mHandler.sendEmptyMessageDelayed(DISMISS_VSETTING_MENU, (15 - getIntervalTime()));
							}
						}
						break;

					default:
						break;
				}
				Log.i(TAG, "DTVRoot handle msg end " + Thread.currentThread());
			}
		};

		// 开始waitSerivceThread，控制DTV的状态，启动，重启，准备等
		Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread");
		waitSerivceThread = new Thread("StarTVOSServiceThread") {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread run");

				startTVOSService();

				Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread run>>prepare begin");
				Looper.prepare();
				Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread run>>prepare end");

				serviceHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread>>handleMessage:" + msg.what + " " + Thread.currentThread());

						switch (msg.what) {
							case DTV_RESUME_BEGIN:// DTV处于准备状态
								Intent intent = (Intent) msg.obj;
								if (null != intent) {
									boolean isFirst = intent.getBooleanExtra("firtStart", false);
									if (isFirst) {
										startTVOSService();
									} else {
										dtv_Resume(intent);
									}
								}
								break;

							case DTV_SERVICE_RESTAR:// 重新启动服务
								isWaitingService = true;
								isStartControlEnd = false;
								DtvInterface mInterface = DtvInterface.getInstance();
								try {
									while (ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME) == null) {
										Log.i(TAG, "waiting restart service");
										Thread.sleep(30);
									}
									mInterface.init();
									isFirstStart = false;
									isWaitingService = false;
									isStartControlEnd = true;
									mChannelManager.updateCurChannelList();
									startBootService();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								mHandler.sendEmptyMessage(DISSMISS_DIALOG);
								break;

							case DTV_START_BOOT:// 开始DTV启动（从其他源过来，但是boot尚未成功执行，这时候设置boot的消息）
								startBootService();
								startDtvUpdateService();
								break;

							case DTV_FORCE_PREPARE: {// 强制DTV资源准备
								if (null != mDtvCommonManager) {
									int result = mDtvCommonManager.dtvResourcePrepare();
									Log.i(TAG, "because of bootException so prepare result: " + result);
								}
							}

							default:
								break;
						}
						Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread>>handleMessage  end" + Thread.currentThread());
					}
				};
				Looper.loop();
				Log.i(TAG, "LL waitSerivceThread Run()>>servieHandler:");
			}
		};
		channel_Monitoring_Thread = new Thread("channel_Monitoring_Thread") {

			@Override
			public void run() {
				// TODO Auto-generated method stub		
				while (true) {
					try {
						Thread.sleep(2000);
						if (mChannelManager != null) {
							dtvroot_monitoer_time_channel_num = mChannelManager.getCurProgram_change_program_Num();
						}
						if (dtvroot_monitoer_time_channel_num >= 5) {// 连续换台20次提示按OK键
							Log.e(TAG, "switch channel 20 times continuously, press 'OK' see more information");

							Handler handler = new Handler(mContext.getMainLooper());
							handler.post(new Runnable() {

								public void run() {

									if (hintToast == null) {
										Log.i("YangLiu", "hintToast is null, creat new one and show");
										hintToast = new CommonHintToast(mContext);
										hintToast.setMessage(R.string.dtv_press_ok_get_more_program_infor);
										hintToast.setGravity(Gravity.RIGHT | Gravity.BOTTOM, 30, 20);
										hintToast.setBackground(R.drawable.more_program_infor_blog);
										hintToast.setDuration(6);
										hintToast.show();
									} else {
										Log.i("YangLiu", "hintToast is not null, directly show");
										hintToast.show();
									}
								}
							});
							mChannelManager.setCurProgram_change_program_Num(0);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		Log.i(TAG, "DtvRoot>>waitServiceStart()>> waitSerivceThread&DtvMessageThread&channel_Monitoring_Thread start");
		waitSerivceThread.start();
		DtvMessageThread.start();
		channel_Monitoring_Thread.start();
	}

	// 按键换台监控线程
	/*private Thread channel_Monitoring_Thread = new Thread("channel_Monitoring_Thread") {

		@Override
		public void run() {
			// TODO Auto-generated method stub		
			while (true) {
				try {
					Thread.sleep(2000);
					if (mChannelManager != null) {
						dtvroot_monitoer_time_channel_num = mChannelManager.getCurProgram_change_program_Num();
					}
					if (dtvroot_monitoer_time_channel_num >= 5) {// 连续换台20次提示按OK键
						Log.e(TAG, "switch channel 20 times continuously, press 'OK' see more information");

						Handler handler = new Handler(mContext.getMainLooper());
						handler.post(new Runnable() {

							public void run() {

								if (hintToast == null) {
									Log.i("YangLiu", "hintToast is null, creat new one and show");
									hintToast = new CommonHintToast(mContext);
									hintToast.setMessage(R.string.dtv_press_ok_get_more_program_infor);
									hintToast.setGravity(Gravity.RIGHT | Gravity.BOTTOM, 30, 20);
									hintToast.setBackground(R.drawable.more_program_infor_blog);
									hintToast.setDuration(6);
									hintToast.show();
								} else {
									Log.i("YangLiu", "hintToast is not null, directly show");
									hintToast.show();
								}
							}
						});
						mChannelManager.setCurProgram_change_program_Num(0);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};*/

	/**
	 * 开始启动CI 播放等动作初始化的处理
	 *
	 * @author lulei
	 */
	private synchronized void startBootService() {
		Log.i(TAG, "LL startBootService>>begin");
		boolean success = mDtvCommonManager.startBootService();// 向系统获取资源，CICA的启动，以及开始启动播放等ＤＴＶ初始化启动
		Log.i(TAG, "LL startBootService>>end success " + success);

		/*if (success) {
			int playStatus = mDtvCommonManager.getPlayStatus();
			if (2 != playStatus) {
				mInputNum = mChannelManager.getBootChannelNum();
				Log.i(TAG, "LL startBootService()>>playProgNum = " + mInputNum);
				mHandler.removeCallbacks(mRunForceProgNum);
				mHandler.post(mRunForceProgNum);
			}
		}*/
		isBeganBootService = true;
	}

	/**
	 * 开始TVOS的service
	 */
	private void startTVOSService() {
		DtvMessageThread.setTimerCallBack(new TimerCallBack() {

			@Override
			public void onTimeCallBack(int curtime) {
				// TODO Auto-generated method stub
				Log.i(TAG, "DtvMessageThread.TimerCallBack curtime:" + curtime);
				if (curtime == 4) { // 如果定时器时间刚好2s，判断是否服务已经启动，如果没有，显示等待框.取消定时器
					if (isWaitingService) {
						isTimeArrive = false;
						mHandler.sendEmptyMessage(SHOW_DIALOG);
					} else {
						// mHandler.sendEmptyMessage(DTV_RESUME_OK);
						DtvMessageThread.removeTimer();
					}

				} else if (6 == curtime) { // 如果定时器时间到了4s，且DTVService启动成功，则启动DTVUI
					isTimeArrive = true;
					boolean sendResult = false;
					if (!isWaitingService) {
						if (isFirstStart) {
							sendResult = mHandler.sendEmptyMessage(START_ACTIVITY);
						} else {
							sendResult = mHandler.sendEmptyMessage(DTV_RESUME_OK);
						}
					}
					Log.i(TAG, "DtvMessageThread.TimerCallBack send message: " + sendResult + "isWaitingService" + isWaitingService);
					DtvMessageThread.removeTimer();
				}
			}
		});
		isTimeArrive = true;
		DtvMessageThread.setTimer(6);//设置定时器时间为4s

		// 判断DTVService是否真的启动成功
		while (true) {
			if ((ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME) != null)) {
				/*DtvInterface dtvInterface = DtvInterface.getInstance();
				while (0 != dtvInterface.resourcePrepare()) {
					try {
						Log.i(TAG, "startTVOSService()>> waiting prepare source");
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
				initManagers();
				if (mAdjustCHSKM == null) {
					mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(DtvRoot.this);
				}
				isWaitingService = false;
				Log.i(TAG, "sendEmptyMessage START_ACTIVITY begin in" + Thread.currentThread());
				boolean result = mHandler.sendEmptyMessage(START_ACTIVITY);//如果DTVService真的启动成功，则启动DTVUI
				Log.i(TAG, "sendEmptyMessage START_ACTIVITY result " + result);
				break;
			} else {
				try {
					Log.i(TAG, "startTVOSService()>> waiting service start");
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/***************************************显示条件判断**************************************
	 *************isDisplayMainMenu，isNeedSaverScreen，isNeedPromptInfo*************
	 *******************isNeedShowRadioView，isNeedDisplayForceMenu*******************
	 *******************************************************************************************/
	/**
	 * 判断显示菜单的前提，是否存在其他菜单。防止各菜单对话框重合
	 *
	 * @return
	 */
	public boolean isDisplayMainMenu() {
		if ((mMenuEpg != null && mMenuEpg.isShowing()) || (mCommonAcionDialog != null && mCommonAcionDialog.isShowing()) || (mScanWarningAcionDialog != null && mScanWarningAcionDialog.isShowing())
				|| (mQuickChannel != null && mQuickChannel.isShowing()) || (mScanDialog != null && mScanDialog.isShowing())) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否需要保存当前屏幕信息
	 *
	 * @return boolean
	 */
	private boolean isNeedSaverScreen() {
		if ((MainMenuReceiver.isRemoved == false) || (mMenuEpg != null && mMenuEpg.isShowing()) || (mCommonAcionDialog != null && mCommonAcionDialog.isShowing())
				|| (mScanWarningAcionDialog != null && mScanWarningAcionDialog.isShowing()) || (mQuickChannel != null && mQuickChannel.isShowing())
				|| (mDtvCiCaForceMenu != null && mDtvCiCaForceMenu.isShowing()) || (mDtvCiCaUserMenu != null && mDtvCiCaUserMenu.isShowing()) || (mScanDialog != null && mScanDialog.isShowing())
				|| (DtvRoot.mDtvRootCurStatus != ConstActivityStatus.ACTIVITY_ONRESUME_STATUS) || (null != guideMenu && guideMenu.isShowing())// 解决首次开机向导中，突然进入屏保的bug
				|| (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing())// VSettingMenu显示时，不保存屏幕信息
				|| (isStartControlEnd == false)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否需要弹出提示框
	 *
	 * @return boolean
	 */
	private boolean isNeedPromptInfo() {
		if ((MainMenuReceiver.isRemoved == false) || (mMenuEpg != null && mMenuEpg.isShowing()) || (mCommonAcionDialog != null && mCommonAcionDialog.isShowing())
				|| (mScanWarningAcionDialog != null && mScanWarningAcionDialog.isShowing()) || (mQuickChannel != null && mQuickChannel.isShowing())
				|| (mDtvCiCaForceMenu != null && mDtvCiCaForceMenu.isShowing()) || (mDtvCiCaUserMenu != null && mDtvCiCaUserMenu.isShowing()) || (mScanDialog != null && mScanDialog.isShowing())
				|| DtvRoot.mDtvRootCurStatus != ConstActivityStatus.ACTIVITY_ONRESUME_STATUS || (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing())// VSettingMenu显示时，不显示提示信息
				|| DtvRoot.isStartControlEnd == false || isWaitingService == true || (mChannelManager.getCurChannelType() == ConstServiceType.SERVICE_TYPE_RADIO)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否需要弹出广播背景
	 *
	 * @return boolean
	 */
	private boolean isNeedShowRadioView() {
		if ((MainMenuReceiver.isRemoved == false) || (mMenuEpg != null && mMenuEpg.isShowing()) || (mCommonAcionDialog != null && mCommonAcionDialog.isShowing())
				|| (mScanWarningAcionDialog != null && mScanWarningAcionDialog.isShowing()) || (mQuickChannel != null && mQuickChannel.isShowing())
				|| (mDtvCiCaForceMenu != null && mDtvCiCaForceMenu.isShowing()) || (mDtvCiCaUserMenu != null && mDtvCiCaUserMenu.isShowing()) || (mScanDialog != null && mScanDialog.isShowing())
				|| DtvRoot.mDtvRootCurStatus != ConstActivityStatus.ACTIVITY_ONRESUME_STATUS || (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing())// VSettingMenu显示时，不显示广播信息
				|| DtvRoot.isStartControlEnd == false || isWaitingService == true) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否需要显示CA菜单
	 *
	 * @return
	 */
	private boolean isNeedDisplayForceMenu() {
		if ((MainMenuReceiver.isRemoved == false) || (mMenuEpg != null && mMenuEpg.isShowing()) || (mCommonAcionDialog != null && mCommonAcionDialog.isShowing())
				|| (mScanWarningAcionDialog != null && mScanWarningAcionDialog.isShowing()) || (mQuickChannel != null && mQuickChannel.isShowing()) || (mScanDialog != null && mScanDialog.isShowing())
				// || (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing())//VSettingMenu显示时，不显示CA菜单信息
				|| DtvRoot.mDtvRootCurStatus != ConstActivityStatus.ACTIVITY_ONRESUME_STATUS || DtvRoot.isStartControlEnd == false) {
			return false;
		}
		return true;
	}

	/*****************************************初始化*******************************************
	 ******************************initManagers和init（视图）*********************************
	 *******************************************************************************************/
	/**
	 * 初始化各种管理器Manager
	 */
	public void initManagers() {
		// Log.i(TAG, "initManagers inThread begin:" + Thread.currentThread().getId());
		mAdjustAudioManager = AdjustAudioManager.getAdjustAudioManagerInstance(DtvRoot.this);
		mDtvCommonManager = DtvCommonManager.getInstance();
		mDtvCommonManager.updateKeyboardConvertFlag(false);
		mScheduleManager = DtvScheduleManager.getInstance();
		mOperatorManager = DtvOperatorManager.getInstance();
		mSourceManager = DtvSourceManager.getInstance();
		mDtvMsgManager = DtvMsgManager.getInstance(DtvRoot.this);
		mChannelManager = DtvChannelManager.getInstance();
		mInterface = DtvInterface.getInstance();
		Log.i(TAG, "initManagers inThread end: " + Thread.currentThread().getId());
	}

	/**
	 * @author lulei 初始化所需要的各个manager\注册接收广播等
	 */
	private void init() {
		Log.i(TAG, "LL [enter] init()***" + "startTime=" + System.currentTimeMillis() + "	" + new Date(System.currentTimeMillis()));

		mMailLayout = (RelativeLayout) findViewById(R.id.mail_layout);
		mMailText = (TextView) findViewById(R.id.mail_text);
		mInputNumView = (TextView) findViewById(R.id.channelNum);
		mInputNumView.setVisibility(View.GONE);

		mChannelInfoView = new ViewChannelInfo(DtvRoot.this);
		mPromptInfoView = new ViewPromptInfo(DtvRoot.this);
		mRadioBackView = new ViewRadioBackground(DtvRoot.this);

		mBgLayout.addView(mRadioBackView);
		mBgLayout.addView(mChannelInfoView);
		mBgLayout.addView(mPromptInfoView);
		mMenuMoveManager = new MenuMoveManager();
		Log.i(TAG, "LL width = " + getWindowManager().getDefaultDisplay().getWidth());
		mMenuMoveManager.init(mPromptInfoView, 0, getWindowManager().getDefaultDisplay().getWidth());
		new MenuMoveManager().init(mRadioBackView, 0, getWindowManager().getDefaultDisplay().getWidth());

		// mAdjustAudioManager = AdjustAudioManager.getAdjustAudioManagerInstance(DtvRoot.this);
		mChannelManager = DtvChannelManager.getInstance();
		if (mMainMenuRootData == null) {
			mMainMenuRootData = new MainMenuRootData(mContext.getString(R.string.dtv_menu_main_title), R.drawable.main_menu_title, mContext);
			mMainMenuRootData.initMainMenu();
		}
		// mDtvCommonManager = DtvCommonManager.getInstance();
		// mDtvCommonManager.updateKeyboardConvertFlag(false);
		// mScheduleManager = DtvScheduleManager.getInstance();
		// mOperatorManager = DtvOperatorManager.getInstance();
		// mSourceManager = DtvSourceManager.getInstance();
		// mDtvMsgManager = DtvMsgManager.getInstance(DtvRoot.this);
		mScheduleManager.setOnScheduleListener(new DtvScheduleManager.IScheduleReceive() {

			public void onScheduleTimeUp(DtvScheduleEvent dtvSchelEvent) {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL Jump to the channel: sourceID = " + dtvSchelEvent.getSourceID() + ",programName = " + dtvSchelEvent.getProgramName());
				if (null == mFilterChannel) {
					mFilterChannel = FilterChannels.getInstance(mContext);
				}
				// if(mFilterChannel.isShowing())
				{
					// Log.i(TAG,"onScheduleTimeUp()--> FilterChannels is showing");
					if (mFilterChannel.isFilter()) {
						Log.i(TAG, "onScheduleTimeUp()--> FilterChannels is isFiltering");
						mFilterChannel.cancelSmartSkip();
					}
				}

				if (mSourceManager.getCurSourceID() != dtvSchelEvent.getSourceID()) {
					mSourceManager.setCurSource(dtvSchelEvent.getSourceID());
					mChannelManager.setCurChannelType(mChannelManager.getBootChannelType());
				}
				int channelIndex = dtvSchelEvent.getProgramServiceIndex();
				int channelType = mChannelManager.getChannelTypeByChannelIndex(channelIndex);
				if (mChannelManager.getCurChannelType() != channelType) {
					Log.i(TAG, "LL onScheduleTimeUp>>needChangeChannelType>>channelType = " + channelType + ",curChannelType = " + mChannelManager.getCurChannelType());
					if (false == mChannelManager.changeChannelTypeButNoPlay()) {
						Log.e(TAG, "LL onScheduleTimeUp>>needChangeChannelType>>failed to changeChanelType***");
					}
				}
				if (null == mChannelManager.getProgramByServiceIndexIfExists(channelIndex)) {
					channelIndex = mChannelManager.getCurPorgramServiceIndex();
					Log.e(TAG, "LL onScheduleTimeUp>>schedule>>not find>>channelIndex = " + channelIndex);
				}
				// SetReturnLastSourceFlag(false);
				// SetisNotDTVChangedFlag(true);
				isNotDTVChanged = true;
				isReturnLastSource = false;
				isReqStartAutoScan = false;
				mChannelManager.channelForceChangeByProgramServiceIndex(channelIndex, false);
				mChannelInfoView.show();
				mChannelInfoView.updateEpgShow();
			}
		});

		mChannelManager.setOnChangeChannelBroadcast(new DtvChannelManager.OnChangeChannelBroadcast() {

			@Override
			public void onChangeChannel(Intent intent) {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL tsid = " + intent.getIntExtra("Tsid", -1) + ",orgnetid = " + intent.getIntExtra("NetworkID", -1) + ",serviceid = " + intent.getIntExtra("ServiceID", -1)
						+ ", providerid = " + intent.getIntExtra("ProviderID", -1) + ", servicename = " + intent.getStringExtra("ServiceName"));
				mHandler.sendEmptyMessage(CHANNEL_CHANGED);
				DtvRoot.this.sendBroadcast(intent);
			}
		});

		if (mCiCaQueryReceiver == null) {
			mCiCaQueryReceiver = new CiCaQueryReceiver();
			IntentFilter cicaIntentFilter = new IntentFilter(MainMenuReceiver.INTENT_CICAQUERY);
			DtvRoot.this.registerReceiver(mCiCaQueryReceiver, cicaIntentFilter);
		}

		if (mAutoSearchReceiver == null) {
			mAutoSearchReceiver = new AutoSearchReceiver();
			IntentFilter autoSearchIntentFilter = new IntentFilter(MainMenuReceiver.INTENT_AUTOSEARCH);
			DtvRoot.this.registerReceiver(mAutoSearchReceiver, autoSearchIntentFilter);
			Log.i(TAG, "*** registerReceiver AutoSearchReceiver ***");
		}

		if (mLongPressMenuKeyReceiver == null) {
			mLongPressMenuKeyReceiver = new LongPressMenuKeyReceiver();
			IntentFilter longPressMenuKeyIntentFilter = new IntentFilter("TV_LongPressMenuKey");
			DtvRoot.this.registerReceiver(mLongPressMenuKeyReceiver, longPressMenuKeyIntentFilter);
			Log.i(TAG, "*** registerReceiver LongPressMenuKeyReceiver ***");
		}

		// /////////////////////////////cuixiaoyan 2014-10-20 ////////////////
		if (mLongPressHomeKeyReceiver == null) {
			mLongPressHomeKeyReceiver = new LongPressHomeKeyReceiver();
			IntentFilter longPressHomeKeyIntentFilter = new IntentFilter("TV_LongPressHomeKey");
			DtvRoot.this.registerReceiver(mLongPressHomeKeyReceiver, longPressHomeKeyIntentFilter);
			Log.i(TAG, "*** registerReceiver LongPressHomeKeyReceiver ***");
		}

		if (mShortPressSystemKeyReceiver == null) {
			mShortPressSystemKeyReceiver = new ShortPressSystemKeyReceiver();
			IntentFilter shortPressSystemKeyIntentFilter = new IntentFilter("com.changhong.system.systemkeyfor3rd");
			DtvRoot.this.registerReceiver(mShortPressSystemKeyReceiver, shortPressSystemKeyIntentFilter);
			Log.i(TAG, "*** registerReceiver ShortPressSystemKeyReceiver ***");
		}
		// //////////////////////////////////////////////////////////////////

		if (mEasySettingExitReceiver == null) {
			mEasySettingExitReceiver = new EasySettingExitReceiver();
			IntentFilter easySettingExitIntentFilter = new IntentFilter("easysetting_exit");
			DtvRoot.this.registerReceiver(mEasySettingExitReceiver, easySettingExitIntentFilter);
			Log.i(TAG, "*** registerReceiver EasySettingExitReceiver ***");
		}

		mChannelManager.installInnerReceiver(DtvRoot.this);
		mDtvMsgManager.install(new DtvMsgManager.IMsgReceive() {
			ViewBootTextInfo mViewBootTextInfo = null;
			ViewBootImageInfo mViewBootImageInfo = null;
			CommonProgressInfoDialog mProgressDiglog = null;

			public void onMsgUserMenu(Bundle mBundle) {
				if (false == DtvRoot.this.isNeedDisplayForceMenu()) {
					return;
				}
				DtvCardStatus cardStatus = DtvCicaManager.getCardStatus();
				if (null == cardStatus) {
					return;
				}

				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "onMsgMail>> failed because dtvroot is stop");
					return;
				}
				switch (cardStatus.getCardType()) {
					case CardType.CARD_TYPE_CI:
						if (null == mDtvCiCaForceMenu) {
							mDtvCiCaForceMenu = new MenuCiCaForce(DtvRoot.this);
							mDtvCiCaForceMenu.setOnVirtualKeyDownListener(new OnVirtualKeyDownListener() {

								@Override
								public void onVirtualKeyDownListener(int keyCode, KeyEvent event) {
									// TODO Auto-generated method stub
									Log.i(TAG, "LL onVirtualKeyDownListener()>>keyCode = " + keyCode);
									onKeyDown(keyCode, event);
									switch (keyCode) {
										case KeyEvent.KEYCODE_CHANNEL_UP:// KEYCODE_STAIR_CHANNEL_UP:
										case KeyEvent.KEYCODE_CHANNEL_DOWN:// KEYCODE_STAIR_CHANNEL_DOWN:

											onKeyUp(keyCode, event);
											break;
										default:
											break;
									}
								}
							});

							mDtvCiCaForceMenu.setOnShowListener(new DialogInterface.OnShowListener() {

								@Override
								public void onShow(DialogInterface arg0) {
									// TODO Auto-generated method stub
									Log.i(TAG, "LL mDtvCiCaForceMenu>>onShow()***");
									DtvRoot.removeScreenSaverMessages();
									mDtvCommonManager.updateKeyboardConvertFlag(true);
									mPromptInfoView.hide();
								}
							});

							mDtvCiCaForceMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface arg0) {
									// TODO Auto-generated method stub
									Log.i(TAG, "LL mDtvCiCaForceMenu>>onDismiss()***");
									DtvRoot.handleScreenSaverMessages();
									mDtvCommonManager.updateKeyboardConvertFlag(false);
									mPromptInfoView.show();
								}
							});
						}

						mDtvCiCaForceMenu.updateMenu(mBundle);
						break;

					default:

						if (null == mDtvCiCaUserMenu) {

							mDtvCiCaUserMenu = new MenuCiCaUser(DtvRoot.this);
							mDtvCiCaUserMenu.setOnShowListener(new DialogInterface.OnShowListener() {

								@Override
								public void onShow(DialogInterface arg0) {
									// TODO Auto-generated method stub
									DtvRoot.removeScreenSaverMessages();
									mDtvCommonManager.updateKeyboardConvertFlag(true);
									mPromptInfoView.hide();
								}
							});

							mDtvCiCaUserMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface arg0) {
									// TODO Auto-generated method stub
									DtvRoot.handleScreenSaverMessages();
									mDtvCommonManager.updateKeyboardConvertFlag(false);
									mPromptInfoView.show();
								}
							});
						}

						mDtvCiCaUserMenu.updateMenu(mBundle);

						break;
				}
			}

			public void onMsgSubtitle(CICAMSubtitle subtitle) {
				// TODO Auto-generated method stub
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "onMsgMail>> failed because dtvroot is stop");
					return;
				}

				if (mDtvCiCaSubtitle == null) {
					mDtvCiCaSubtitle = new MenuCiCaSubtitle(DtvRoot.this);
					mBgLayout.addView(mDtvCiCaSubtitle);

				}
				mDtvCiCaSubtitle.setMenuState(subtitle.miMsgType, subtitle.miMsgID, -1);
				mDtvCiCaSubtitle.show(subtitle.mstr_Subtitle, subtitle.mi_Showtime);
			}

			public void onMsgPrompt(CICAMPrompt prompt) {
				// TODO Auto-generated method stub
				String showInfo = prompt.mstrPrompt;
				Log.i(TAG, "LL onMsgPrompt()>>showInfo = " + showInfo);
				if (showInfo == null || showInfo.trim().length() <= 0 || showInfo.trim().equals("null")) {
					showInfo = null;
				}
				mStrCiCaPrompt = showInfo;

				if (null != mStrPlayerPrompt) {
					showInfo = mStrPlayerPrompt;
				}

				if (DtvRoot.this.isNeedPromptInfo()) {
					/**
					 * deleted by YangLiu 2014-12-25
					 */
					// mPromptInfoView.setPromptInfo(showInfo);
				} else {
					mPromptInfoView.updatePromptInfo(showInfo);
				}
				Log.i(TAG, "LL onMsgPrompt showInfo = " + showInfo + " and isNeedShow()>> " + isNeedPromptInfo());
			}

			public void onMsgMail(final CICAMMail mail) {
				// TODO Auto-generated method stub
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "onMsgMail>> failed because dtvroot is stop");
					return;
				}

				if (null == mDtvCiCaMail) {
					mDtvCiCaMail = new MenuCiCaMail(DtvRoot.this);
				}

				mDtvCiCaMail.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});

				mDtvCiCaMail.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.handleScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(false);
						mPromptInfoView.show();
					}
				});

				mDtvCiCaMail.setOnFloatMailListener(new OnFloatMailListener() {

					@Override
					public void onFloatMail() {
						mMailLayout.setVisibility(View.VISIBLE);
						mMailText.setText("(" + mail.miNewCnt + ")");
					}
				});
				mDtvCiCaMail.setMenuState(mail.miMsgType, mail.miMsgID, -1);
				mDtvCiCaMail.setMailInfo(mail.miNewCnt, mail.miReadCnt, mail.miParam);
			}

			public void onMsgForceScan() {
				// TODO Auto-generated method stub
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "onMsgForceScan>> failed because dtvroot is stop");
					return;
				}

				List<DtvProgram> channelList = mChannelManager.getChannelList();
				Log.i(TAG, "onMsgForceScan--->channelList.size" + channelList.size());
				if (channelList == null || channelList.size() <= 0) {
					mCommonAcionDialog = new CommonAcionDialog(DtvRoot.this, 0, R.string.dtv_menu_no_channel_prompt, 0, 10);
				} else {
					mCommonAcionDialog = new CommonAcionDialog(DtvRoot.this, 0, R.string.dtv_cica_force_scan, 0, 10);
				}
				mCommonAcionDialog.setCancelable(true);
				mCommonAcionDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_CANCEL);
				mCommonAcionDialog.setDuration(30);
				mCommonAcionDialog.setOkButtonText(R.string.yes);
				mCommonAcionDialog.setCancelButtonText(R.string.no);
				mCommonAcionDialog.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});
				mCommonAcionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (mScanDialog == null || !mScanDialog.isShowing()) {

							DtvRoot.handleScreenSaverMessages();
							mDtvCommonManager.updateKeyboardConvertFlag(false);
							mPromptInfoView.show();
						}
					}
				});
				mCommonAcionDialog.setOKButtonListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						/*mScanDialog = new MenuScan(DtvRoot.this, scantype.DTV_ScanAuto);
						mScanDialog.setOnShowListener(new DialogInterface.OnShowListener() {

							@Override
							public void onShow(DialogInterface arg0) {
								// TODO Auto-generated method stub
								DtvRoot.removeScreenSaverMessages();
								mDtvCommonManager.updateKeyboardConvertFlag(true);
								mPromptInfoView.hide();
							}
						});
						mScanDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface arg0) {
								// TODO Auto-generated method stub
								DtvRoot.handleScreenSaverMessages();
								mDtvCommonManager.updateKeyboardConvertFlag(false);
								mPromptInfoView.show();
								showDialogFilter(DtvRoot.this);
							}
						});
						mScanDialog.show();*/

						mCommonAcionDialog.dismiss();
						/*Intent mIntent = new Intent("com.start.search.dtv");
						Log.i(TAG, "sendBroadcast com.start.search.dtv 2222");
						//发送广播
						mContext.sendBroadcast(mIntent);*/
						Intent intent = new Intent(MainMenuReceiver.INTENT_AUTOSEARCH);
						intent.putExtra(MainMenuReceiver.DATA_SEARCHTYPE, 0);// scantype.DTV_ScanAuto
						mContext.sendBroadcast(intent);
						Log.i(TAG, "sendBroadcast com.start.search.dtv 2222");
					}
				});
				mCommonAcionDialog.show();
			}

			public void onMsgForceMenu(Bundle mBundle) {
				// TODO Auto-generated method stub
				/*CICAMMenuBase menuBase = (CICAMMenuBase) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				if (mDtvCiCaForceMenu == null) {
					mDtvCiCaForceMenu = new MenuCiCaForce(DtvRoot.this);
				}
				mDtvCiCaForceMenu.updateMenu(mBundle);*/
			}

			public void onMsgForceChannel(CICAMForceChannel forceChannel) {
				// TODO Auto-generated method stub
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "onMsgForceChannel>> failed because dtvroot is stop");
					return;
				}

				Log.i(TAG, "LL [enter]onMsgForceChannel>>channelIndex = " + forceChannel.miChannelIndex + ", opType = " + forceChannel.miOpType);
				switch (forceChannel.miOpType) {
					case 0: // 0: 应急广播 (需要锁按键)
						Rect rect = new Rect(0, 0, (int) (1000 * getResources().getDisplayMetrics().scaledDensity), (int) (600 * getResources().getDisplayMetrics().scaledDensity));
					/*if (null != mViewBootTextInfo) {
						mBgLayout.removeView(mViewBootTextInfo);
						mViewBootTextInfo = null;
					}
					mViewBootTextInfo = new ViewBootTextInfo(DtvRoot.this, rect);
					mBgLayout.addView(mViewBootTextInfo);
					mViewBootTextInfo.setPromptInfo(DtvRoot.this.getString(R.string.menu_dtv_emergency_broadcast));*/
						mChannelManager.channelChangeByProgramServiceIndex(forceChannel.miChannelIndex, false);
						mChannelInfoView.show();
						mChannelInfoView.updateEpgShow();
						break;

					case 1: // 1：应急广播结束、返回（解锁按键、返回观看之前的节目）
						if (null != mViewBootTextInfo) {
							mBgLayout.removeView(mViewBootTextInfo);
							mViewBootTextInfo = null;
						}
						mChannelManager.channelChangeReturn();
						mChannelInfoView.show();
						mChannelInfoView.updateEpgShow();
						break;

					case 2: // 2：应急广播结束（解锁按键）
						if (null != mViewBootTextInfo) {
							mBgLayout.removeView(mViewBootTextInfo);
							mViewBootTextInfo = null;
						}
						mChannelInfoView.show();
						mChannelInfoView.updateEpgShow();
						break;

					case 3: // 3：强制换台（不锁按键）
						mChannelManager.channelForceChangeByProgramServiceIndex(forceChannel.miChannelIndex, false);
						mChannelInfoView.show();
						mChannelInfoView.updateEpgShow();
						break;

					case 4: // 4: 智能跳台(miChannelIndex：-1——结束；正数——跳台的channel index)
						if (null == mFilterChannel) {
							mFilterChannel = FilterChannels.getInstance(mContext);
						}
						// if(mFilterChannel.isShowing())
						if (mFilterChannel.isFilter()) {
							mFilterChannel.update(forceChannel.miChannelIndex);
						}

					default:
						break;
				}
				Log.i(TAG, "onMsgForceChannel>>receiced message end");
			}

			public void onMsgFinger(CICAMFinger finger) {
				// TODO Auto-generated method stub
			}

			public void onMsgDFA(int msgClass, Bundle mBundle) {
				// TODO Auto-generated method stub
			}

			public void onMsgAnnounce(CICAMAnnounce announce) {
				// TODO Auto-generated method stub
			}

			public void onPlayStatusUpdate(PlayStatusInfo playStatusInfo) {
				// TODO Auto-generated method stub
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "LL onPlayStatusUpdate>> failed because of dtv stop()");
					return;
				}

				String showInfo = null;
				boolean isNeedSaverScreen = false;
				Log.d(TAG, "LL onPlayStatusUpdate>> playStatusInfo.miPlayEvent=" + playStatusInfo.miPlayEvent);
				switch (playStatusInfo.miPlayEvent) {
					case ConstPlayerEvent.OK:
						Log.i(TAG, "LL onPlayStatusUpdate>>ConstPlayerEvent.OK str = " + playStatusInfo.mstrPrompt);
						showInfo = playStatusInfo.mstrPrompt;
						if (showInfo == null || showInfo.trim().length() <= 0 || showInfo.trim().equals("null")) {
							showInfo = null;
						}
						mStrPlayerPrompt = showInfo;

						DtvRoot.removeScreenSaverMessages();
						DtvRoot.isHasProgram = true;
						DtvRoot.isHasSignal = true;

						if (null != mStrCiCaPrompt) {
							showInfo = mStrCiCaPrompt;
						}

						Log.i(TAG, "LL onPlayStatusUpdate>>ConstPlayerEvent.OK showInfo = " + showInfo + " and isNeedShow()>> " + isNeedPromptInfo());
						if (DtvRoot.this.isNeedPromptInfo()) {
							mPromptInfoView.setPromptInfo(showInfo);
						} else {
							mPromptInfoView.updatePromptInfo(showInfo);
						}

						DtvProgram program = mChannelManager.getCurProgram();
						if (null != program && program.isRadio()) {
							if (DtvRoot.this.isNeedShowRadioView()) {
								mPromptInfoView.hide();
								mRadioBackView.show();
							}
						} else {
							mRadioBackView.hide();
						}
						break;

					case ConstPlayerEvent.FORCE_PAUSE:
						if (!isNeedResourcePrepare) {
							isNeedResourcePrepare = true;
							Log.i(TAG, "LL onPlayStatusUpdate>>isNeedResourcePrepare = " + isNeedResourcePrepare);
						}
						break;

					case ConstPlayerEvent.NO_SIGNAL:
						Log.i(TAG, "LL onPlayStatusUpdate>>ConstPlayerEvent.NO_SIGNAL str = " + playStatusInfo.mstrPrompt);

						showInfo = playStatusInfo.mstrPrompt;
						if (showInfo == null || showInfo.trim().length() <= 0 || showInfo.trim().equals("null")) {
							showInfo = null;
						}
						mStrPlayerPrompt = showInfo;
						DtvRoot.isHasSignal = false;

						isNeedSaverScreen = DtvRoot.this.isNeedSaverScreen();
						if (true == isNeedSaverScreen) {
							DtvRoot.handleScreenSaverMessages();
						}
						if (DtvRoot.this.isNeedPromptInfo()) {
							mPromptInfoView.setPromptInfo(showInfo);
						} else {
							mPromptInfoView.updatePromptInfo(showInfo);
						}
						mRadioBackView.hide();
						break;

					case ConstPlayerEvent.NO_CHANNEL:
						Log.i(TAG, "LL onPlayStatusUpdate>>ConstPlayerEvent.NO_CHANNEL str = " + playStatusInfo.mstrPrompt);
						showInfo = playStatusInfo.mstrPrompt;
						if (showInfo == null || showInfo.trim().length() <= 0 || showInfo.trim().equals("null")) {
							showInfo = null;

						}
						mStrPlayerPrompt = showInfo;
						DtvRoot.isHasProgram = false;

						isNeedSaverScreen = DtvRoot.this.isNeedSaverScreen();
						if (true == isNeedSaverScreen) {
							DtvRoot.handleScreenSaverMessages();
						}
						if (DtvRoot.this.isNeedPromptInfo()) {
							mPromptInfoView.setPromptInfo(showInfo);
						} else {
							mPromptInfoView.updatePromptInfo(showInfo);
						}
						mRadioBackView.hide();
						break;

					case ConstPlayerEvent.NO_SAMRT_CARD:
						Log.i(TAG, "LL onPlayStatusUpdate>>ConstPlayerEvent.NO_SAMRT_CARD str = " + playStatusInfo.mstrPrompt);
						showInfo = playStatusInfo.mstrPrompt;
						if (showInfo == null || showInfo.trim().length() <= 0 || showInfo.trim().equals("null")) {
							showInfo = null;
						}
						mStrPlayerPrompt = showInfo;
						if (DtvRoot.this.isNeedPromptInfo()) {

							mPromptInfoView.setPromptInfo(showInfo);
						} else {
							mPromptInfoView.updatePromptInfo(showInfo);
						}
						break;

					case ConstPlayerEvent.CAM_VARY:
						if (null != mDtvCiCaUserMenu) {
							mDtvCiCaUserMenu.dismiss();
						}
						if (null != mDtvCiCaForceMenu) {
							mDtvCiCaForceMenu.dismiss();
						}

						if (!MainMenuReceiver.isRemoved) {
							MainMenuReceiver.update();
						}

						/**
						 * add By YangLiu 2014-12-22
						 */
						if ((mMainMenuRootData != null) && (mMainMenuRootData.IsVSettingMenuShowing())) {
							return;
						}
						/*** 重新设置运营商后重启无法收台 **/
					/*if ((mMainMenuRootData != null) && (mMainMenuRootData.IsVSettingMenuShowing())) {
						mMainMenuRootData.initMainMenu();
					}*/

						showInfo = playStatusInfo.mstrPrompt;
						if (showInfo != null && showInfo.trim().length() > 0 && !showInfo.trim().equals("null")) {
						/*CommonInfoDialog myDialog = new CommonInfoDialog(DtvRoot.this);
						// myDialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
						myDialog.setMessage(showInfo);
						myDialog.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
						myDialog.show();*/
							showToastDialog(showInfo);
						}
						break;

					default:
						break;
				}
				Log.i(TAG, "onPlayStatusUpdate>> receive message end");
			}

			@Override
			public void onBootService(Bundle mBundle) {
				// TODO Auto-generated method stub
				StartControlInfo startControlInfo = (StartControlInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				Log.v(TAG, "*****************************BootServiceReceiver>>msgType=" + startControlInfo.miMessageType);
				if (!DtvRoot.this.isResumed()
				/*ConstActivityStatus.ACTIVITY_ONSTOP_STATUS == DtvRoot.mDtvRootCurStatus
				|| DtvRoot.mDtvRootCurStatus == ConstActivityStatus.ACTIVITY_ONDESTROY_STATUS*/
						) { // 主要是为了防止在启动的时候 被切源了 Log.i(TAG,
					// "onBootService>>DtvRoot.this.isResumed() false");
					if (startControlInfo.miMessageType == StartControlInfo.DTV_START_SERVICE_END) {
						isBeganBootService = false;
						isBootException = false;
					}
					return;
				}

				switch (startControlInfo.miMessageType) {
					case StartControlInfo.DTV_START_SERVICE_ADV_PIC:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_ADV_PIC");
						if (null == mViewBootImageInfo) {

							mViewBootImageInfo = new ViewBootImageInfo(DtvRoot.this, startControlInfo.mPosition);
							mBgLayout.addView(mViewBootImageInfo, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						}
						mViewBootImageInfo.setImageInfo(startControlInfo.mstrPicFileName);
						break;

					case StartControlInfo.DTV_START_SERVICE_ADV_PIC_END:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_ADV_PIC_END");
						if (null != mViewBootImageInfo) {
							mViewBootImageInfo.setVisibility(View.GONE);
						}
						break;

					case StartControlInfo.DTV_START_SERVICE_ADV_CHANNEL:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_ADV_CHANNEL");
						if (!isBootException) {
							mChannelManager.channelChangeByProgramServiceIndex(startControlInfo.miStartChannelIndex, false);
						}
						break;

					case StartControlInfo.DTV_START_SERVICE_ADV_CHANNEL_END:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_ADV_CHANNEL_END");
						break;

					case StartControlInfo.DTV_START_SERVICE_DISP_PROMPT:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_DISP_PROMPT");
					/*if (null == mViewBootTextInfo) {
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.CENTER_IN_PARENT);
						mViewBootTextInfo = new ViewBootTextInfo(DtvRoot.this, startControlInfo.mPosition);
						mBgLayout.addView(mViewBootTextInfo);
					}
					mViewBootTextInfo.setPromptInfo(startControlInfo.mstrPicFileName);*/
						break;

					case StartControlInfo.DTV_START_SERVICE_DISP_PROMPT_END:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_DISP_PROMPT_END");
						if (null != mViewBootTextInfo) {
							mViewBootTextInfo.hide();
						}
						break;

					case StartControlInfo.DTV_START_SERVICE_SCAN_PROGRAM:
						isDTV_BOOT_SVC_SCAN_START = true;
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_SCAN_PROGRAM");

						// channelManager.setBootScanBusy(1);//2015-4-14
						if (null == mProgressDiglog) {
							mProgressDiglog = new CommonProgressInfoDialog(DtvRoot.this);
						}
						mProgressDiglog.setDuration(1000000);
						mProgressDiglog.setButtonVisible(false);
						mProgressDiglog.setCancelable(false);
						mProgressDiglog.setMessage(DtvRoot.this.getString(R.string.dtv_start_service_scan_program));
						mProgressDiglog.show();
						/** 由于progressDialog不想被消掉，于是从对话框管理器中移除 **/
						DtvDialogManager.RemoveDialog(mProgressDiglog);
						break;

					case StartControlInfo.DTV_START_SERVICE_SCAN_PROGRAM_END:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_SCAN_PROGRAM_END");
						if (!isBootException) {
							if (null != mProgressDiglog) {
								mProgressDiglog.dismiss();
							}
						}
						// channelManager.setBootScanFree(0);//2015-4-14

						/**开机自动搜索结束（绵阳运营商等）	YangLiu	2015-2-26*/
						break;

					case StartControlInfo.DTV_BOOT_SVC_AUTO_SCAN_CONFIRM:
						Log.i(TAG, "LL [enter]DTV_BOOT_SVC_AUTO_SCAN_CONFIRM");
						if (!isBootException) {
							showAutoScanConfirm();
						}
						break;

					case StartControlInfo.DTV_START_SERVICE_END:
						Log.i(TAG, "LL [enter]DTV_START_SERVICE_END");
						DtvRoot.isStartControlEnd = true;
						if (!isBootException) {
							if (null != mProgressDiglog) {
								mProgressDiglog.dismiss();
							}

							mChannelManager.setCurChannelType(mChannelManager.getBootChannelType());

							int playStatus = mDtvCommonManager.getPlayStatus();
							Log.i(TAG, "LL DTV_START_SERVICE_END>>playStatus = " + playStatus);

							if (0 != playStatus) { // 资源被抢占销毁了，重新启动基础bootService
								Log.i(TAG, "LL DTV_START_SERVICE_END>>playStatus !=0 so has to do bootservice again");
								DtvRoot.isStartControlEnd = false;
								isBootException = false;
								isBeganBootService = false;
								Message msg = serviceHandler.obtainMessage();
								msg.what = DTV_START_BOOT;
								serviceHandler.sendMessage(msg);
							} else {
								/** 语音换台 **/
								boolean ishasVoice = false;
								mInterface.SetDtvBusyState(0);
								if (null != activityIntent) {
									int voiceNum = activityIntent.getIntExtra("ChannelNum", -1);
									activityIntent.removeExtra("ChannelNum");
									if (-1 != voiceNum) {
										ishasVoice = true;
										changeChannelForVoice(voiceNum, true);
									}
								}

								/** DTV换台 **/
								if (!ishasVoice) {//开机启动 查询开机节目num
									mInputNum = mChannelManager.getBootChannelNum();
									Log.i(TAG, "LL DTV_START_SERVICE_END()>>playProgNum = " + mInputNum);
									mHandler.removeCallbacks(mRunForceProgNum);
									mHandler.post(mRunForceProgNum);
								}

								/** 更新当前状态 **/
								checkOperateChanged();
								setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONRESUME_STATUS);
								if (isDisplayMainMenu() && (MainMenuReceiver.isRemoved) || (mMainMenuRootData != null && !mMainMenuRootData.IsVSettingMenuShowing())) {
									DtvRoot.handleScreenSaverMessages();
									mDtvCommonManager.updateKeyboardConvertFlag(false);
									mPromptInfoView.show();
								}

								/** 智能导视弹出框 **/
								if (is5508Q2) {
									//切换运营商，搜台后重新刷新EPG 2015-7-6 YangLiu
									DtvChannelManager.refreshEPG();

									//开机第一次弹出欢网预约海报 2015-7-6 YangLiu
									Log.i("YangLiu", "开机第一次弹出本地推荐*******");
									if (mChannelManager.getChannelList() != null && mChannelManager.getChannelList().size() > 0) {
										Log.i("YangLiu", "有节目信息，可以弹窗");
										Intent pushoutservice = new Intent(mContext, PushoutService.class);
										TimerInfo timerInfo = null;
										Bundle bundle = new Bundle();
										bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, timerInfo);// add by YangLiu
										pushoutservice.putExtras(bundle);
										pushoutservice.putExtra("control", "show");
										mContext.startService(pushoutservice);
									} else {
										Log.i("YangLiu", "本地没有节目信息，所以不弹窗");
									}
								}

								/** 启动数据上报 2015-5-18 YangLiu **/
								DtvChannelManager.getInstance().startPosProInfoThread();
							}
						} else {
							Log.i(TAG, "isBootException is true so has to do bootservice again");
							DtvRoot.isStartControlEnd = false;
							isBootException = false;
							isBeganBootService = false;
							Message msg = serviceHandler.obtainMessage();
							msg.what = DTV_START_BOOT;
							serviceHandler.sendMessage(msg);
						}
						break;

					default:
						break;
				}
				Log.i(TAG, "onBootService>> message receive end");
			}

			@Override
			public void onChannelChanged() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL onChannelChanged()***");
				DtvRoot.isNeedUpdateChannelList = true;
			}
		});

		/*mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == START_ACTIVITY) {

					DtvRoot.this.startBootService();
					// this.startBootServiceOrSetupOperator();

					// 唐超 add start
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
					Log.i(TAG, "LL OperatorCode = " + mOperatorManager.getCurOperator().getOperatorCode());
					intent.putExtra("CurOperator", mOperatorManager.getCurOperator().getOperatorCode());
					ComponentName cn = new ComponentName("com.changhong.tvos.dtv.update", "com.changhong.tvos.dtv.update.dtvUpdateService");
					intent.setComponent(cn);
					startService(intent);
					// 唐超 add end
					dtv_start();
					dtv_Resume();
				}
			}
		};*/

		mRunnable = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				if (mChannelManager.channelChangeByProgramNum(mInputNum, false)) {
					// isReturnLastSource = false;
					// SetReturnLastSourceFlag(false);
					mChannelInfoView.show();
					mChannelInfoView.updateEpgShow();
					mRadioBackView.show();
					mPromptInfoView.hideDelay(mDelayTime);
				} else {
					if (mInputNum != mChannelManager.getCurProgramNum()) {

						CommonInfoDialog myDialog = new CommonInfoDialog(DtvRoot.this);
						myDialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
						myDialog.setMessage(DtvRoot.this.getResources().getString(R.string.InvalidDtvChannel, mInputNum));
						myDialog.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
						myDialog.show();
					}
					mChannelInfoView.show();
					mChannelInfoView.updateEpgShow();
					mRadioBackView.show();
				}
				mInputNum = 0;
				mInputNumView.setVisibility(View.GONE);
			}
		};
		mRunForceProgNum = new Runnable() {//开机换台线程

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (true == DtvRoot.isStartControlEnd) {
					Log.i(TAG, "LL mRunForceProgNum>>mInputNum = " + mInputNum);
					if (startThirdApp()) {
						mDtvCommonManager.setPlayingProgramIndex(mChannelManager.getCurPorgramServiceIndex());
						mInputNum = 0;
						mHandler.removeCallbacks(mRunForceProgNum);
						return;
					}
					mChannelManager.channelForceChangeByProgramNum(mInputNum, true);
					mChannelInfoView.show();
					mChannelInfoView.updateEpgShow();
					mRadioBackView.show();
					mPromptInfoView.hideDelay(mDelayTime);
					mInputNum = 0;
					mHandler.removeCallbacks(mRunForceProgNum);
					return;
				}
				mHandler.postDelayed(mRunForceProgNum, 50);
			}
		};
		mRunForceProgIndex = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (true == DtvRoot.isStartControlEnd) {
					Log.i(TAG, "LL mRunForceProgIndex>>mInputIndex = " + mInputIndex);
					if (startThirdApp()) {
						mDtvCommonManager.setPlayingProgramIndex(mChannelManager.getCurPorgramServiceIndex());
						mInputIndex = -1;
						mHandler.removeCallbacks(mRunForceProgIndex);
						return;
					}
					mChannelManager.channelForceChangeByProgramServiceIndex(mInputIndex, true);
					mChannelInfoView.show();
					mChannelInfoView.updateEpgShow();
					mPromptInfoView.hideDelay(mDelayTime);
					mInputIndex = -1;
					mHandler.removeCallbacks(mRunForceProgIndex);
					return;
				}
				mHandler.postDelayed(mRunForceProgIndex, 50);
			}
		};
		mRunProgNum = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (true == DtvRoot.isStartControlEnd) {
					Log.i(TAG, "LL mRunProgNum>>mInputNum = " + mInputNum);
					if (mChannelManager.channelChangeByProgramNum(mInputNum, true)) {
						mPromptInfoView.hideDelay(mDelayTime);
						mChannelInfoView.show();
					}
					mInputNum = 0;
					mHandler.removeCallbacks(mRunProgNum);
					return;
				}
				mHandler.postDelayed(mRunProgNum, 50);
			}
		};

		mMainMenuRootData.setOnMenuRemovedListener(new MainMenuRootData.OnMenuRemovedListener() {

			@Override
			public void onRemoved(boolean removed) {
				// TODO Auto-generated method stub
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "mMainMenuRootData on remove err because DtvRoot not resumed");
					return;
				}

				if (true == removed) {
					Log.i(TAG, "mMainMenuRootData onRemoved removed(true)111: " + removed);
					DtvRoot.handleScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(false);
					Log.i(TAG, "mMainMenuRootData onRemoved mChannelManager.getCurChannelType(): " + mChannelManager.getCurChannelType());
					if (mChannelManager.getCurChannelType() != ConstServiceType.SERVICE_TYPE_RADIO) {
						Log.i(TAG, "mMainMenuRootData onRemoved mPromptInfoView.show");
						mPromptInfoView.show();
					}
					DtvProgram program = mChannelManager.getCurProgram();
					if (null != program && program.isRadio() && isHasSignal) {
						mRadioBackView.show();
					} else {
						mRadioBackView.hide();
					}

					if (isEnterOperaterDirect) {
						isEnterOperaterDirect = false;
						startBootService();
						// 唐超 add start
						DtvRoot.this.startDtvUpdateService();
						// 唐超 add end
					}
				} else {
					Log.i(TAG, "mMainMenuRootData onRemoved removed(false):222 " + removed);
					DtvRoot.removeScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(true);
					mPromptInfoView.hide();
					mRadioBackView.hide();
				}
			}
		});

		MainMenuReceiver.setWindows(this.getWindowManager());
		MainMenuReceiver.setOnMenuRemovedListener(new MainMenuReceiver.OnMenuRemovedListener() {

			@Override
			public void onRemoved(boolean removed) {
				if (!DtvRoot.this.isResumed()) {
					Log.e(TAG, "MainMenuReceiver onremove err because DtvRoot not resumed");
					return;
				}
				// TODO Auto-generated method stub
				if (true == removed) {
					DtvRoot.handleScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(false);
					if (mChannelManager.getCurChannelType() != ConstServiceType.SERVICE_TYPE_RADIO) {
						mPromptInfoView.show();
					}
					DtvProgram program = mChannelManager.getCurProgram();
					if (null != program && program.isRadio() && isHasSignal) {
						mRadioBackView.show();
					} else {
						mRadioBackView.hide();
					}

					if (isEnterOperaterDirect) {
						isEnterOperaterDirect = false;
						startBootService();
						// 唐超 add start
						DtvRoot.this.startDtvUpdateService();
						// 唐超 add end
					}
				} else {
					DtvRoot.removeScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(true);
					mPromptInfoView.hide();
					mRadioBackView.hide();
				}
			}
		});

		if (null == reBootUIReceiver) {
			reBootUIReceiver = new DTVServiceReboot();
		}
		IntentFilter serviceFilter = new IntentFilter("com.changhong.dtvservicestatus.action");
		this.registerReceiver(reBootUIReceiver, serviceFilter);

		Log.i(TAG, "LL [end] init()***" + "endTime=" + System.currentTimeMillis() + "	" + new Date(System.currentTimeMillis()));
	}

	/*******************************DTV Activity的生命周期重载*******************************
	 ************onCreate，onStart，onResume，onPause，OnStop，onDestroy***********
	 *******************************************************************************************/
	/**
	 * DTV下的多窗口菜单
	 */
	public void addTransparentTVWindow() {

		pos3 = getWindow().getWindowManager().getDefaultDisplay().getHeight();
		pos4 = getWindow().getWindowManager().getDefaultDisplay().getWidth();

		mImageView = new ImageView(this);
		mImageView.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWinLayoutParams = new WindowManager.LayoutParams();
		mWinLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mWinLayoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mWinLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWinLayoutParams.format = 0x200;
		mWinLayoutParams.x = pos1;
		mWinLayoutParams.y = pos2;
		mWinLayoutParams.height = pos3;
		mWinLayoutParams.width = pos4;
		mWindowManager.addView(mImageView, mWinLayoutParams);
		mImageView.setVisibility(View.VISIBLE);
	}

	/**
	 * 重写onCreate函数
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// DTV版本信息
		getDtvInfo();

		// DTV onCreate
		Log.i(TAG, "\n\nLL [enter] onCreate***" + Thread.currentThread());

		DtvChannelData = this.getSharedPreferences("dtvChannelData", MODE_WORLD_READABLE);// sharedPreferences可以让其他应用程序读取
		if (DtvChannelData == null) {
			Log.i(TAG, "LL DtvRoot.onCreate() DtvChannelData   == NULL ***");
		} else {
			DtvChanneleditor = DtvChannelData.edit();
			Log.i(TAG, "LL DtvRoot.onCreate() DtvChannelData   == ok ***");
		}

		this.saveDtvVersionInfo(); // fy 转存DTVUIVERSIONINFO
		this.autoAdaptResolution(); // 自动调整分辨率
		this.setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONCREATE_STATUS);
		isStartWasuApp = true; // 华数

		mContext = DtvRoot.this;
		mEPGActivity = this;
		mBgLayout = (RelativeLayout) LayoutInflater.from(DtvRoot.this).inflate(R.layout.main, null);
		setContentView(mBgLayout);

		this.waitServiceStart();

		// 打印DTV启动时间
		mOnCreateTime = getCurrentTime().toString();
		Log.i(TAG, "LL end>>DtvRoot.onCreate()***" + Thread.currentThread() + ", mOnCreateTime: " + mOnCreateTime);
	}

	/**
	 * 重写onStart方法
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL [enter]onStart()***");
		super.onStart();
		setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONSTART_STATUS);

		// 显示等待服务中...
		if (isWaitingService && null != waitDialog && !isTimeArrive) {
			waitDialog.show();
		}

		// isReturnLastSource = intent.getBooleanExtra("bSourceChanged", false);
		SetisNotDTVChangedFlag(true, true);

		/** 给微信电视发送onDestroy广播*/
		DtvChannelManager.getInstance().sendWeChatTVBroadcast(mContext, "onStart");

		// 打印DTV启动时间
		mOnStartTime = getCurrentTime().toString();
		Log.i(TAG, "LL end>>DtvRoot.onStart()***" + Thread.currentThread() + ", mOnStartTime: " + mOnStartTime);
	}

	/**
	 * Dtv Resume(添加语音换台)
	 */
	private void dtv_Resume(Intent intent) {
		Log.i(TAG, "LL enter>>dtv_Resume***" + Thread.currentThread());
		if (null == intent) {
			mHandler.sendEmptyMessage(DTV_RESUME_OK);
			Log.e(TAG, "dtv_Resume>>(Intent intent) is null");
			return;
		}

		// isReturnLastSource = intent.getBooleanExtra("bSourceChanged", false);
		Log.i(TAG, "dtv_Resume + isReturnLastSource =" + isReturnLastSource);

		/////////////////////////多屏协同 自动搜索 /////////////////////////////////
		isReqStartAutoScan = intent.getBooleanExtra("bStartAutoScan", false);

		// ///////////////////////////语音换台/////////////////////////////////////
		final int voiceChannel = intent.getIntExtra("ChannelNum", -1);
		intent.removeExtra("ChannelNum");
		Log.i(TAG, "LL dtv_Resume()>>voiceChannel = " + voiceChannel);

		// ///////////////////////////正常换台/////////////////////////////////////
		int playStatus = mDtvCommonManager.getPlayStatus();
		Log.i(TAG, "LL dtv_Resume>>playStatus = " + playStatus);

		if (playStatus != 0) {
			// mInterface.SetDtvBusyState(1); ///fyyyyy
			int playPrepare = 0;
			/*DtvMessageThread.setTimerCallBack(new TimerCallBack() {
				@Override
				public void onTimeCallBack(int curtime) {
					// TODO Auto-generated method stub

					if (isWaitingService) {
						mHandler.sendEmptyMessage(SHOW_DIALOG);
					} else {
						mHandler.sendEmptyMessage(DTV_RESUME_OK);
					}
				}
			});*/
			// 检查DTVService是否启动
			DtvMessageThread.setTimer(6);

			// 判断资源是否准备好，准备好了播放节目
			while (0 != (playPrepare = mDtvCommonManager.dtvResourcePrepare())) {
				try {
					// mHandler.sendEmptyMessage(SHOW_DIALOG);
					Log.i(TAG, "LL dtv_Resume>>wait dtvResourcePrepare ***  playPrepare= " + playPrepare);
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (playPrepare == 0) {//	换台，播放节目
				Log.i(TAG, "LL dtv_Resume>>dtvResourcePrepare success " + playPrepare);
				isWaitingService = false;
				mInterface.SetDtvBusyState(0); // /fyyyyy
				if (-1 == voiceChannel && playStatus != 2) {
					mInputNum = mChannelManager.getCurProgramNum();
					Log.i(TAG, "LL DtvRoot.onResume()>>playProgNum = " + mInputNum);
					mHandler.removeCallbacks(mRunForceProgNum);
					mHandler.post(mRunForceProgNum);
				}
			} else {
				Log.i(TAG, "LL dtv_Resume>>dtvResourcePrepare failed " + playPrepare);
			}
		}
		changeChannelForVoice(voiceChannel, false);
		// DTV_RESUME_OK恢复成功
		mHandler.sendEmptyMessage(DTV_RESUME_OK);
		Log.i(TAG, "LL end>>dtv_Resume***" + Thread.currentThread() + ", mOnResumeTime: " + mOnResumeTime);
	}

	/**
	 * 重写onResume方法：
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL enter>>DtvRoot.onResume()***" + Thread.currentThread());
		super.onResume();

		Intent intent = getIntent();
		activityIntent = intent;

		// isReturnLastSource = intent.getBooleanExtra("bSourceChanged", false);
		Log.i(TAG, "onResume + isReturnLastSource = " + isReturnLastSource);

		////////////////////////多屏协同 自动搜索///////////////////////////////////
		// isReqStartAutoScan = intent.getBooleanExtra("bStartAutoScan", false);

		setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONRESUME_STATUS);

		if (null != serviceHandler && !isFirstStart) {
			if (!isWaitingService && isStartControlEnd && !isBootException) {
				// 1. DTV_RESUME_BEGIN：DTV从onPause状态恢复
				isWaitingService = true;
				isTimeArrive = true;
				intent.putExtra("firtStart", false);
				Message msg = serviceHandler.obtainMessage();
				msg.obj = intent;
				msg.what = DTV_RESUME_BEGIN;
				serviceHandler.sendMessage(msg);

			} else {
				// 主要用于在刚开机就被切源出DTV
				Log.i(TAG, "LL DtvRoot.onResume() is firstStart= " + isFirstStart + " isWaitingService= " + isWaitingService + " isStartControllEnd= " + isStartControlEnd
						+ " hasStarted bootService= " + isBeganBootService + " isBootException= " + isBootException);
				if (!isBeganBootService) {
					// 2. DTV_START_BOOT：开机过程中，bootService未开始启动,就退出DTV
					Log.i(TAG, "bootservice has not start");
					isBootException = false;
					Message msg = serviceHandler.obtainMessage();
					msg.what = DTV_START_BOOT;
					serviceHandler.sendMessage(msg);

				} else if (!isStartControlEnd) {
					// 3. DTV_FORCE_PREPARE：开机过程中，bootService正在启动中，被退出DTV（先将资源抢占回来，避免出现资源被ATV占用的情况，其余事项就在boot广播中处理）
					int playStatus = mDtvCommonManager.getPlayStatus();
					Log.i(TAG, "LL dtv_Resume>>playStatus = " + playStatus);

					if (playStatus != 0) {
						Log.i(TAG, "onResume playStatus != 0 prepare first");
						Message msg = serviceHandler.obtainMessage();
						msg.what = DTV_FORCE_PREPARE;
						serviceHandler.sendMessage(msg);
					}
				} else {
					// 这时候不做处理，等待在开机广播中去处理
				}
			}
			Log.i(TAG, "LL sendBroadcast()>>com.changhong.qls.live.changechalle_EPG_resume***");

			// 发送给第三方的resume广播
			Intent mIntent_EPG_resume = new Intent("com.changhong.qls.live.changechalle_EPG_resume");
			// mIntent.putExtra("scene", "DTV");
			sendBroadcast(mIntent_EPG_resume);

		} else {
			Log.e(TAG, "onResume()>> servieHandler is null");
		}

		/** 给微信电视发送onDestroy广播*/
		DtvChannelManager.getInstance().sendWeChatTVBroadcast(mContext, "onResume");

		/**
		 * 添加多窗口View
		 */
		addTransparentTVWindow();

		// 打印DTV处于onResume的时间
		mOnResumeTime = getCurrentTime().toString();
		Log.i(TAG, "LL end>>DtvRoot.onResume()***" + Thread.currentThread() + ", mOnResumeTime: " + mOnResumeTime);
	}

	/**
	 * 重写onPause方法：
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL enter>>DtvRoot.onPause()***");
		this.setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONPUASE_STATUS);

		//退出菜单
		if ((mMainMenuRootData != null) && (mMainMenuRootData.IsVSettingMenuShowing())) {//cuixiaoyan 2014-10-20
			mMainMenuRootData.VSettingMenuVisibilityControl(false);
		}

		//退出屏保锁定
		Log.i(TAG, "Dtv onPause, remove ScreenSaver and promptInfo");
		DtvRoot.removeScreenSaverMessages();

		//关闭显示
		if (null != mChannelInfoView) {
			mChannelInfoView.hide();
		}
		if (null != mPromptInfoView) {
			mPromptInfoView.hide();
		}
		if (null != mDtvCommonManager) {
			mDtvCommonManager.updateKeyboardConvertFlag(true);
		}
		if (null != mRadioBackView) {
			mRadioBackView.hide();
		}

		// 发送给第三方的pause广播
		Log.i(TAG, "LL sendBroadcast()>>com.changhong.qls.live.changechalle_EPG_pause***");
		Intent mIntent_EPG_pause = new Intent("com.changhong.qls.live.changechalle_EPG_pause");
		// mIntent.putExtra("scene", "DTV");
		sendBroadcast(mIntent_EPG_pause);

		super.onPause();

		//数据上报
		Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect11) + "|subClass:" + mContext.getResources().getString(R.string.collect12)
				+ "|reportInfo:duration=" + mOnStartTime + "|" + mOnPauseTime);

		try {
			systemmanage = TVManager.getInstance(mContext).getSystemManager();
			systemmanage.unlockScreenSaver();
			Log.d(TAG, "exit——unlockScreenSaver success");
		} catch (TVManagerNotInitException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "exit——unlockScreenSaver error " + e.getMessage());
		}

		//打印DTV处于onPause的时间
		mOnPauseTime = getCurrentTime().toString();
		Log.i(TAG, "LL end>>DtvRoot.onPause()***" + Thread.currentThread() + ", mOnPauseTime: " + mOnPauseTime);
	}

	/**
	 * 重写onStop方法：
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL enter>>DtvRoot.onStop()****>>isNeedResourcePrepare = " + isNeedResourcePrepare);
		this.setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONSTOP_STATUS);
		// mInterface.SetDtvBusyState(0); ///fyyyyy
		super.onStop();

		//清除所有dialog
		DtvDialogManager.ClearDialogs();

		//清除频道播放
		if (null != mChannelManager) {
			mChannelManager.channelStop();
			Log.i(TAG, "LL DtvRoot.onStop()>>channelStop()***");
		}

		//退出菜单
		if (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing()) {
			mMainMenuRootData.VSettingMenuVisibilityControl(false);
		}
		// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);//deleted by cuixy 20140607

		//改变isStartControlEnd和isBootException
		if (!isStartControlEnd) {
			isBootException = true;
		} else {
			isBootException = false;
		}
		Log.i(TAG, "isStartControlEnd=" + isStartControlEnd + " and isBootException=" + isBootException);

		//退出资源
		if (null != mDtvCommonManager) {
			mDtvCommonManager.dtvResourceRelease();
			Log.i(TAG, "LL DtvRoot.onStop()>>dtvResourceRelease()***");
		}

		/** 给微信电视发送onDestroy广播*/
		DtvChannelManager.getInstance().sendWeChatTVBroadcast(mContext, "onStop");

		try {
			TVManager.getInstance(mContext).getSystemManager().unlockScreenSaver();
		} catch (TVManagerNotInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * 退出多窗口View
		 */
		if (mImageView != null && mWindowManager != null) {
			mImageView.setVisibility(View.INVISIBLE);
			if (mImageView.getParent() != null) {
				mWindowManager.removeView(mImageView);
			}
		}

		//打印DTV处于onStop的时间
		mOnStopTime = getCurrentTime().toString();
		Log.i(TAG, "LL end>>DtvRoot.onStop()***" + Thread.currentThread() + ", mOnStopTime: " + mOnStopTime);
	}

	/**
	 * 重写onDestroy方法：
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL enter>>onDestroy()***");
		this.setDtvRootCurStatus(ConstActivityStatus.ACTIVITY_ONDESTROY_STATUS);

		if (true == DtvRoot.isStartControlEnd) {
			//移除Dtv监听器
			if (null != mDtvMsgManager) {
				mDtvMsgManager.unInstall();
			}
			if (null != mChannelManager) {
				mChannelManager.uninstallInnerReceiver(DtvRoot.this);
			}
			if (null != mCiCaQueryReceiver) {
				DtvRoot.this.unregisterReceiver(mCiCaQueryReceiver);
				mCiCaQueryReceiver = null;
			}
			if (null != mAutoSearchReceiver) {
				DtvRoot.this.unregisterReceiver(mAutoSearchReceiver);
				mAutoSearchReceiver = null;
				Log.i(TAG, "*** unregisterReceiver AutoSearchReceiver ***");
			}
			if (null != mLongPressMenuKeyReceiver) {
				DtvRoot.this.unregisterReceiver(mLongPressMenuKeyReceiver);
				mLongPressMenuKeyReceiver = null;
				Log.i(TAG, "*** unregisterReceiver LongPressMenuKeyReceiver ***");
			}

			//移除按键监听器
			if (null != mLongPressHomeKeyReceiver) {//cuixiaoyan 2014-10-20
				DtvRoot.this.unregisterReceiver(mLongPressHomeKeyReceiver);
				mLongPressHomeKeyReceiver = null;
				Log.i(TAG, "*** unregisterReceiver LongPressHomeKeyReceiver ***");
			}
			if (null != mShortPressSystemKeyReceiver) {
				DtvRoot.this.unregisterReceiver(mShortPressSystemKeyReceiver);
				mShortPressSystemKeyReceiver = null;
				Log.i(TAG, "*** unregisterReceiver ShortPressSystemKeyReceiver ***");
			}
			if (null != mEasySettingExitReceiver) {
				DtvRoot.this.unregisterReceiver(mEasySettingExitReceiver);
				mEasySettingExitReceiver = null;
				Log.i(TAG, "*** unregisterReceiver EasySettingExitReceiver ***");
			}

			// 发送给第三方的exit广播
			Log.i(TAG, "LL sendBroadcast()>>com.changhong.qls.live.changechalle_EPG_exit***");
			Intent mIntentEPG_exit = new Intent("com.changhong.qls.live.changechalle_EPG_exit");
			// mIntent.putExtra("scene", "DTV");
			sendBroadcast(mIntentEPG_exit);

			//移除UI重启监听器
			try {
				this.unregisterReceiver(reBootUIReceiver);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		super.onDestroy();

		//退出屏保锁定
		Log.i(TAG, "Dtv onDestroy, remove ScreenSaver and promptInfo");
		try {
			systemmanage = TVManager.getInstance(mContext).getSystemManager();
			systemmanage.unlockScreenSaver();
			Log.d(TAG, "exit——unlockScreenSaver success");
		} catch (TVManagerNotInitException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "exit——unlockScreenSaver error " + e.getMessage());
		}

		//打印DTV处于onDestroy的时间
		mOnDestroyTime = getCurrentTime().toString();
		Log.i(TAG, "LL end>>DtvRoot.onDestroy()***" + Thread.currentThread() + ", mOnDestroyTime: " + mOnDestroyTime);
	}

	/**
	 * 重写onNewIntent
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		this.setIntent(intent);
	}

	/*************************************************屏保*************************************
	 *****************************************显示屏保，取消屏保******************************
	 *******************************************************************************************/
	/**
	 * 添加屏保时延
	 */
	public static void handleScreenSaverMessages() {
		Log.i(TAG, "LL DtvRoot>>isHasSignal = " + DtvRoot.isHasSignal + ",isHasProgram = " + DtvRoot.isHasProgram);
		if (false == DtvRoot.isHasSignal || false == DtvRoot.isHasProgram) {
			removeScreenSaverMessages();
			Log.i(TAG, "LL DtvRoot>>handleScreenSaverMessages()***");
			DtvMessageThread.sendEmptyMessageRemovedDelayed(ConstMessageType.DTV_SCREEN_SAVER_MESSAGE, ConstLongTimeDelay.DELAY_MILLIS_15);
		}
	}

	/**
	 * 取消屏保
	 */
	public static void removeScreenSaverMessages() {
		Log.i(TAG, "LL DtvRoot>>removeScreenSaverMessages()***");
		DtvMessageThread.removeMessages(ConstMessageType.DTV_SCREEN_SAVER_MESSAGE);
	}

	/*************************************************显示对话框*************************************
	 *******************************************过滤对话框，搜索对话框******************************
	 *************************************************************************************************/
	/**
	 * 显示节目过滤对话框
	 *
	 * @param context
	 */
	private void showDialogFilter(Context context) {
		if (null == mChannelManager || null == mChannelManager.getChannelList() || mChannelManager.getChannelList().size() == 0) {
			Log.d(TAG, "NO CHANNEL");
			DtvRoot.handleScreenSaverMessages();
			mDtvCommonManager.updateKeyboardConvertFlag(false);
			mPromptInfoView.show();
			return;
		}

		final FilterChannels filterChannels = FilterChannels.getInstance(context);
		filterChannels.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				DtvRoot.handleScreenSaverMessages();
				mDtvCommonManager.updateKeyboardConvertFlag(false);
				mPromptInfoView.show();
			}
		});

		final CommonAcionDialog chooseOrnotDialog = new CommonAcionDialog(context, 0, R.string.dtv_filter_title, 0, 10);
		chooseOrnotDialog.setCancelable(true);
		chooseOrnotDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_CANCEL);
		chooseOrnotDialog.setDuration(30);
		chooseOrnotDialog.setOkButtonText(R.string.yes);
		chooseOrnotDialog.setCancelButtonText(R.string.no);
		chooseOrnotDialog.setOKButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				filterChannels.show();
				filterChannels.startSmartSkip();
				chooseOrnotDialog.dismiss();
			}
		});

		chooseOrnotDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (filterChannels == null || !filterChannels.isShowing()) {

					DtvRoot.handleScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(false);
					mPromptInfoView.show();
				}
			}
		});
		chooseOrnotDialog.show();
	}

	/**
	 * 显示节目自动搜索对话框（在切换运营商之后，无节目的情况下 显示自动节目收索对话框）
	 */
	private void showScanWarning() {
		Log.i("YangLiu", "showScanWarning--->channelList.size=0");
		mScanWarningAcionDialog = new CommonAcionDialog(DtvRoot.this, 0, R.string.dtv_menu_no_channel_prompt, 0, 10);
		mScanWarningAcionDialog.setCancelable(true);
		mScanWarningAcionDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_CANCEL);
		mScanWarningAcionDialog.setDuration(50);
		mScanWarningAcionDialog.setOkButtonText(R.string.yes);
		mScanWarningAcionDialog.setCancelButtonText(R.string.no);
		mScanWarningAcionDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {
				// TODO Auto-generated method stub
				DtvRoot.removeScreenSaverMessages();
				mDtvCommonManager.updateKeyboardConvertFlag(true);
				mPromptInfoView.hide();
			}
		});
		mScanWarningAcionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (mScanDialog == null || !mScanDialog.isShowing()) {

					DtvRoot.handleScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(false);
					mPromptInfoView.show();
				}
			}
		});
		mScanWarningAcionDialog.setOKButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				mScanWarningAcionDialog.dismiss();
				Intent intent = new Intent(MainMenuReceiver.INTENT_AUTOSEARCH);
				intent.putExtra(MainMenuReceiver.DATA_SEARCHTYPE, 0);// scantype.DTV_ScanAuto
				mContext.sendBroadcast(intent);
				Log.i(TAG, "sendBroadcast com.start.search.dtv 3333");
				// Intent mIntent = new Intent("com.start.search.dtv");
				// mContext.sendBroadcast(mIntent);
				// Log.i(TAG, "sendBroadcast com.start.search.dtv 3333");
			}
		});
		mScanWarningAcionDialog.show();
	}

	/**
	 * 显示自动搜索的确认信息
	 */
	protected void showAutoScanConfirm() {
		// TODO Auto-generated method stub
		if (!isDisplayMainMenu() || isEnterOperaterDirect) {
			return;
		}
		List<DtvProgram> channelList = mChannelManager.getChannelList();
		Log.i("YangLiu", "showAutoScanConfirm--->channelList.size" + channelList.size());
		if (null == channelList || channelList.size() == 0) {
			isShowAutoScan = false;
			mScanWarningAcionDialog = new CommonAcionDialog(DtvRoot.this, 0, R.string.dtv_menu_no_channel_prompt, 0, 10);
			mScanWarningAcionDialog.setCancelable(true);
			mScanWarningAcionDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_OK);
			mScanWarningAcionDialog.setDuration(50);
			mScanWarningAcionDialog.setOkButtonText(R.string.yes);
			mScanWarningAcionDialog.setCancelButtonText(R.string.no);
			mScanWarningAcionDialog.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface arg0) {
					// TODO Auto-generated method stub
					DtvRoot.removeScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(true);
					mPromptInfoView.hide();
				}
			});
			mScanWarningAcionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					if (mScanDialog == null || !mScanDialog.isShowing()) {
						DtvRoot.handleScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(false);
						mPromptInfoView.show();
					}
				}
			});
			mScanWarningAcionDialog.setOKButtonListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mScanDialog = new MenuScan(DtvRoot.this, scantype.DTV_ScanAuto);
					mScanDialog.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface arg0) {
							// TODO Auto-generated method stub
							DtvRoot.removeScreenSaverMessages();
							mDtvCommonManager.updateKeyboardConvertFlag(true);
							mPromptInfoView.hide();
						}
					});
					mScanDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							showDialogFilter(DtvRoot.this);
						}
					});
					mScanDialog.show();
					mScanWarningAcionDialog.dismiss();
				}
			});
			mScanWarningAcionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					isShowAutoScan = false;
				}
			});
			mScanWarningAcionDialog.setCancelButtonListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					isShowAutoScan = false;
					mScanWarningAcionDialog.dismiss();
				}
			});
			mScanWarningAcionDialog.show();
		}
	}

	/**
	 * 设置/获取节目搜索向导是否显示标志
	 */
	public static void SetisshowGuideScan(boolean tmpisshowGuideScan) {
		isShowGuideScan = tmpisshowGuideScan;// = false;
	}

	public static boolean GetisshowGuideScan() {
		return isShowGuideScan;// = false;
	}

	/**
	 * 显示节目搜索向导对话框
	 *
	 * @param isScan
	 */
	public void showGuideScan(boolean isScan) {
		// final MenuSearchGuide guideMenu = new MenuSearchGuide(mContext);
		Log.i("showGuideScan", "Scan00000=" + isScan);
		mMainMenuRootData.showSearchGuideSelectMenu();
		/*guideMenu = new MenuSearchGuide(mContext);//����״ο������У�ͻȻ����������bug
		guideMenu.setShowScan(isScan);
		DtvRoot.removeScreenSaverMessages();
		guideMenu.show();
		guideMenu.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub

				if (mScanDialog == null || !mScanDialog.isShowing()) {

					DtvRoot.handleScreenSaverMessages();
					mDtvCommonManager.updateKeyboardConvertFlag(false);
					mPromptInfoView.show();
				}
			}
		});

		guideMenu.setOnSureListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				mScanDialog = new MenuScan(DtvRoot.this, scantype.DTV_ScanAuto);
				DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OP_GUIDE, "false");
				isShowGuideScan = false;
				mScanDialog.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						// TODO Auto-generated method
						// stub
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});
				mScanDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method
						// stub
						showDialogFilter(DtvRoot.this);
					}
				});
				mScanDialog.show();
				guideMenu.dismiss();
			}
		});*/
	}

	/**
	 * 隐藏搜索向导对话框
	 */
	public static void dismissSearchGuide() {
		if ((guideMenu != null) && (guideMenu.isShowing())) {
			guideMenu.dismiss();
		}
	}

	/*****************************************数据上报*****************************************
	 *******************************************************************************************
	 *******************************************************************************************/
	/**
	 * DTV信息数据上报
	 */
	public void reportDtvChannelInfo() {
		// jni中获取到的信息
		String strProgramList = getProgramListInfo();
		DTVSettings settings = DTVSettings.getInstance(mContext);
		Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect11) + "|subClass:" + mContext.getResources().getString(R.string.collect12)
				+ "|reportInfo:" + getSwApp() + getSwMw() + getSwDriver() + getKeyFrenquency()
				// + getProductName()
				+ getCurDemodeType() + "SearchType=" + getScanTypeName() + ";softwareversion_db=" + settings.getDtvInfo(0) + ";"// 0x13062501
				+ getCurOperatorName() + "card_type=" + settings.getDtvInfo(3)// No Card
				+ ";CA_info=" + settings.getDtvInfo(1)// NOVEL
				+ ";CA_zone=" + settings.getDtvInfo(2)// 0
				+ getProgramSize() + strProgramList + "");
	}

	/***************************************get和set方法***************************************
	 *******************************************************************************************
	 *******************************************************************************************/
	/**
	 * 获取节目列表信息
	 *
	 * @return
	 */
	public String getProgramListInfo() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("programlist=");
		List<DtvProgram> channelList = mChannelManager.getChannelList();
		allNum = 0;
		scrambledNum = 0;
		freeNum = 0;
		if (channelList != null && channelList.size() > 0) {
			allNum = channelList.size();
			for (DtvProgram program : channelList) {
				if (program.isScrambled()) {
					scrambledNum++;
					Log.i(TAG, "program: " + program.mProgramName + " num:" + program.mProgramNum + " serviceIndex: " + program.mServiceIndex + " is Scrambled!");
				} else {
					freeNum++;
					Log.i(TAG, "program: " + program.mProgramName + " num:" + program.mProgramNum + " serviceIndex: " + program.mServiceIndex + " is free!");
				}
				mWriteMsg.append(program.mProgramNum);
				mWriteMsg.append(".");
				mWriteMsg.append(program.mProgramName);
				mWriteMsg.append("|");
			}
		} else {
			mWriteMsg.append("null;");
		}
		String wString = mWriteMsg.toString();
		return wString;
	}

	/**
	 * 获取频道PF信息
	 *
	 * @return
	 */
	public ViewChannelInfo getViewChannelInfo() {
		return mChannelInfoView;
	}

	/**
	 * 获取搜索方式的名称
	 *
	 * @return
	 */
	public String getScanTypeName() {
		int scanType = mChannelManager.GetDtvScanType();
		switch (scanType) {
			case 0:// DTV_ScanAuto
				return mContext.getResources().getString(R.string.dtv_autoscan);
			case 1:// DTV_ScanList
				return mContext.getResources().getString(R.string.dtv_listscan);
			case 2:// DTV_ScanMaunal
				return mContext.getResources().getString(R.string.dtv_manualscan);
			default:
				return mContext.getResources().getString(R.string.dtv_autoscan);
		}
	}

	/**
	 * 获取DTV节目类型
	 *
	 * @return
	 */
	public String getProductName() {
		int productType = DtvSourceManager.getInstance().getProductType();
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("DTVtype=");
		switch (productType) {
			case ConstProductType.PRODUCT_C:
				mWriteMsg.append("C;");
				break;
			case ConstProductType.PRODUCT_T:
				mWriteMsg.append("T;");
				break;
			case ConstProductType.PRODUCT_C_T:
				mWriteMsg.append("T+C;");
				break;
			default:
				mWriteMsg.append("C;");
				break;
		}
		String strProductType = mWriteMsg.toString();

		return strProductType;
	}

	/**
	 * 获取搜索频点信息
	 *
	 * @return
	 */
	public String getKeyFrenquency() {
		String[] mQamStr = null;
		int low_fre = 0;
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("key_frequency=");
		DtvOperatorManager mOperatorManager = null;
		mOperatorManager = DtvOperatorManager.getInstance();
		int demode = DtvSourceManager.getInstance().getCurDemodeType();
		if (ConstDemodType.DVB_C == demode) {
			mQamStr = mContext.getResources().getStringArray(R.array.menu_scan_modulation);

			int freValue = mOperatorManager.getOPMainTunerInfo().getFrequency();
			String symValue = String.valueOf(mOperatorManager.getOPMainTunerInfo().getSymbolRate());
			int qamValue = mOperatorManager.getOPMainTunerInfo().getQamMode() - 1;
			mWriteMsg.append(String.valueOf(freValue / 1000));
			mWriteMsg.append("MHz-");
			mWriteMsg.append(symValue);
			mWriteMsg.append("-");
			mWriteMsg.append(mQamStr[qamValue]);
			mWriteMsg.append("QAM");
		} else if (ConstDemodType.DMB_TH == demode) {
			DtvTunerInfo tunerInfo = DtvInterface.getInstance().getDBMTTunerInfo();
			if (null == tunerInfo) {
				Log.e(TAG, "LL tunerInfo == null");
				low_fre = 0;
			} else {
				low_fre = tunerInfo.getMi_FreqKHz();
			}
			mWriteMsg.append(String.valueOf(low_fre / 100));
			mWriteMsg.append("MHz");
		}
		mWriteMsg.append(";");
		String strKeyFrequency = mWriteMsg.toString();

		return strKeyFrequency;
	}

	/**
	 * 获取节目个数
	 *
	 * @return
	 */
	public String getProgramSize() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append(";porgram_total=");
		mWriteMsg.append(allNum);
		mWriteMsg.append(";porgramType_noCA=");
		mWriteMsg.append(freeNum);
		mWriteMsg.append(";porgramType_CA=");
		mWriteMsg.append(scrambledNum);
		mWriteMsg.append(";");
		String wString = mWriteMsg.toString();
		return wString;
	}

	/**
	 * 获取当前运营商信息
	 *
	 * @return
	 */
	public String getCurOperatorName() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("Network_Operator=");
		DtvOperatorManager mDtvOperatorManager = null;
		mDtvOperatorManager = DtvOperatorManager.getInstance();

		int demode = DtvSourceManager.getInstance().getCurDemodeType();
		if (ConstDemodType.DVB_C == demode) {
			DtvOperator curOperator = mDtvOperatorManager.getCurOperator();
			mWriteMsg.append(curOperator.getOperatorName());
			mWriteMsg.append(";");
		} else {
			mWriteMsg.append("null;");
		}
		String strCurOperator = mWriteMsg.toString();
		return strCurOperator;
	}

	/**
	 * 获取当前DTV_Demode信息
	 *
	 * @return
	 */
	public String getCurDemodeType() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("DTVtype=");

		int demode = DtvSourceManager.getInstance().getCurDemodeType();
		if (ConstDemodType.DVB_C == demode) {
			mWriteMsg.append("C;");
		} else if (ConstDemodType.DMB_TH == demode) {
			mWriteMsg.append("T;");
		} else {
			mWriteMsg.append("null;");
		}
		String strCurDemodeType = mWriteMsg.toString();
		return strCurDemodeType;
	}

	/**
	 * 获取app版本号
	 *
	 * @return
	 */
	public static String getSwApp() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("softwareversion_app=");
		mWriteMsg.append(DtvSoftWareInfoManager.mSoftwareVersion);
		mWriteMsg.append(";");
		String appVersion = mWriteMsg.toString();
		return appVersion;
	}

	/**
	 * 获取mw版本号
	 *
	 * @return
	 */
	public String getSwMw() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("softwareversion_mw=null;");
		String mwVersion = mWriteMsg.toString();
		return mwVersion;
	}

	/**
	 * 获取driver版本号
	 *
	 * @return
	 */
	public String getSwDriver() {
		StringBuffer mWriteMsg = new StringBuffer();
		mWriteMsg.append("softwareversion_driver=");
		DtvVersion dtvVersion = DtvSoftWareInfoManager.getDtvVersion();
		if (dtvVersion != null) {
			mWriteMsg.append(dtvVersion.getHardwareVersion());
			mWriteMsg.append(".");
			mWriteMsg.append(dtvVersion.getOpVersion());
			mWriteMsg.append(".");
			mWriteMsg.append(dtvVersion.getAPIMainVersion());
			mWriteMsg.append(".");
			mWriteMsg.append(dtvVersion.getAPISubVersion());
			mWriteMsg.append(".");
			mWriteMsg.append(dtvVersion.getMainVersion());
			mWriteMsg.append(".");
			mWriteMsg.append(dtvVersion.getSubVersion());
			mWriteMsg.append(".");
			mWriteMsg.append(dtvVersion.getKOVersion());
			mWriteMsg.append(".r;");
		} else {
			mWriteMsg.append("null;");
		}
		String swVersion = mWriteMsg.toString();
		return swVersion;
	}

	/**
	 * 获取当前任务进程的包名
	 *
	 * @return
	 */
	public static String getTopTaskPackageName() {
		String packageName = null;
		String activityName = null;
		List<RunningTaskInfo> tasks = null;
		List<RunningAppProcessInfo> processes = null;

		ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		tasks = manager.getRunningTasks(3);
		processes = manager.getRunningAppProcesses();
		for (RunningTaskInfo info : tasks) {
			// info = manager.getRunningTasks(1).get(0);
			packageName = info.topActivity.getPackageName(); // 包名
			activityName = info.topActivity.getClassName();
			if (packageName != null) {
				for (RunningAppProcessInfo proce : processes) {
					if (proce.processName == packageName || proce.processName.contains(packageName)) {
						Log.d(TAG, "current running packageName: " + packageName + ", activityName: " + activityName + ", pid: " + proce.pid);
						return packageName;
					}
				}
			}
		}
		Log.d(TAG, "current running packageName---" + packageName);
		return packageName;
	}

	/**
	 * 获取按键到现在的时间（秒）
	 *
	 * @return
	 */
	private int getIntervalTime() {
		return CHMiscServiceBinder.getInstance(mContext).getPanelMisc().getNoUserActionSec();
	}

	/**
	 * 格式化数字显示，保持2位显示
	 */
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	/**
	 * 取得当前时间
	 *
	 * @return
	 */
	public static StringBuffer getCurrentTime() {
		// get the current date
		int mYear, mMonth, mDay, mHour, mMinute, mSecond, mMILLIsecond;
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);

		StringBuffer mCurTime = new StringBuffer(pad(mYear)).append('-').append(pad(mMonth)).append('-').append(pad(mDay)).append(' ').append(pad(mHour)).append(':').append(pad(mMinute)).append(':')
				.append(pad(mSecond));
		return mCurTime;
	}

	/**
	 * 取得当前时间（总毫秒数）
	 *
	 * @return
	 */
	public static StringBuffer getCurrentTime_print() {
		// get the current date
		int mYear, mMonth, mDay, mHour, mMinute, mSecond, mMILLIsecond;
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);
		mMILLIsecond = c.get(Calendar.MILLISECOND);

		StringBuffer pmCurTime = new StringBuffer(pad(mYear)).append('-').append(pad(mMonth)).append('-').append(pad(mDay)).append(' ').append(pad(mHour)).append(':').append(pad(mMinute)).append(':')
				.append(pad(mSecond)).append(':').append(pad(mMILLIsecond));
		return pmCurTime;
	}

	/**
	 * 设置音量信息
	 *
	 * @param streamType
	 * @param state
	 * @param needUI
	 */
	public static void setStreamMute(int streamType, boolean state, boolean needUI) {
		boolean tmpState = false;
		tmpState = mAdjustAudioManager.isStreamMute(streamType);
		if (state == true) {
			if (tmpState == false) {
				mAdjustAudioManager.setStreamMute(streamType, true, needUI);
			}
		} else {
			if (tmpState == true) {
				mAdjustAudioManager.setStreamMute(streamType, false, needUI);
			}
		}
	}

	/**
	 * 设置当前DTV的状态
	 *
	 * @param status
	 */
	private void setDtvRootCurStatus(int status) {
		DtvRoot.mDtvRootPreStatus = DtvRoot.mDtvRootCurStatus;
		DtvRoot.mDtvRootCurStatus = status;
	}

	/**
	 * 添加isNotDTVChanged标志 fengy 2014-10-21
	 *
	 * @param isnotdtvchanged
	 */
	public static void SetisNotDTVChangedFlag(boolean isnotdtvchanged) {
		isNotDTVChanged = isnotdtvchanged;
		Log.i(TAG, "SetisNotDTVChangedFlag + isReturnLastSource = " + isReturnLastSource);
		Log.i(TAG, "SetisNotDTVChangedFlag + isNotDTVChanged = " + isNotDTVChanged);
	}

	public static void SetisNotDTVChangedFlag(boolean reqReturn, boolean isnotdtvchanged) {
		isNotDTVChanged = isnotdtvchanged;
		isReturnLastSource = reqReturn;
		Log.i(TAG, "SetisNotDTVChangedFlag + isReturnLastSource 1111 = " + isReturnLastSource);
		Log.i(TAG, "SetisNotDTVChangedFlag + isNotDTVChanged 1111= " + isNotDTVChanged);
	}

	/**
	 * 判断开机SVC搜索是否开始
	 *
	 * @return
	 */
	public static boolean HasDTVBootSVCScanStart() {
		return isDTV_BOOT_SVC_SCAN_START;
	}

	/**
	 * 判断是否是OLED屏（15秒关闭菜单）
	 *
	 * @return
	 */
	private boolean isOLED() {
		try {
			mTVManager = TVManager.getInstance(mContext);
			mMiscManager = mTVManager.getMiscManager();
			mPanelInfo = mMiscManager.getPanelInfo();
		} catch (TVManagerNotInitException e) {
			Log.d(TAG, "TVManagerNotInitException =" + e.getMessage().toString());
		}

		if ((mPanelInfo != null) && (mPanelInfo.mePanelType == EnumPanelType.PANEL_OLED)) {
			return true;
		}
		return false;
	}

	/**
	 * 显示提示信息框
	 *
	 * @param mesId
	 */
	public void showToastDialog(String message) {
		if (mToastDialog == null) {
			mToastDialog = new VchCommonToastDialog(DtvRoot.this);
			mToastDialog.setMessage(message);
			mToastDialog.tv.setTextColor(Color.BLACK);
			mToastDialog.info_layout.setBackgroundResource(R.drawable.common_info_bg);
			mToastDialog.getWindow().setType(2003);
			mToastDialog.show();
		} else {
			Log.i("YangLiu", "mToastDialog is not null, directly show");
			mToastDialog.show();
		}
	}

	/********************************************DTV的BroadcastReceiver类***********************************
	 ****************包括CICA，AutoSearch，EasySettingExit以及DTVServiceReboot*************************
	 *******************LongPressMenu，LongPressHome， ShortPressSystem等****************************
	 ***********************************************************************************************************/
	/**
	 * 添加CACI查询接收器
	 *
	 * @author YangLiu
	 */
	class CiCaQueryReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int cardType = (Integer) intent.getExtra(MainMenuReceiver.DATA_CARDTYPE);
			DtvRoot.this.sendCiCaQuery(cardType);
		}
	}

	/**
	 * 显示CICA信息
	 *
	 * @param cardType
	 */
	private void sendCiCaQuery(int cardType) {
		DtvCicaManager.cicaQueryControl(ConstCICAMsgType.MSG_USER_MENU, ConstCICAMMenuType.MENU_LIST, ConstCICAMMenuID.MENU_ID_ROOT, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM, 0, null);
		switch (cardType) {
			case CardType.CARD_TYPE_CI:
				mDtvCiCaForceMenu = new MenuCiCaForce(DtvRoot.this);
				mDtvCiCaForceMenu.setOnVirtualKeyDownListener(new OnVirtualKeyDownListener() {

					@Override
					public void onVirtualKeyDownListener(int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i(TAG, "LL onVirtualKeyDownListener()>>keyCode = " + keyCode);
						onKeyDown(keyCode, event);
						switch (keyCode) {
							case KeyEvent.KEYCODE_CHANNEL_UP:// KEYCODE_STAIR_CHANNEL_UP:
							case KeyEvent.KEYCODE_CHANNEL_DOWN:// KEYCODE_STAIR_CHANNEL_DOWN:
								onKeyUp(keyCode, event);
								break;
							default:
								break;
						}
					}
				});
				mDtvCiCaForceMenu.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});
				mDtvCiCaForceMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.handleScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(false);
						mPromptInfoView.show();
					}
				});
				mDtvCiCaForceMenu.show();
				break;

			default:
				mDtvCiCaUserMenu = new MenuCiCaUser(DtvRoot.this);
				mDtvCiCaUserMenu.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.removeScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(true);
						mPromptInfoView.hide();
					}
				});

				mDtvCiCaUserMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						DtvRoot.handleScreenSaverMessages();
						mDtvCommonManager.updateKeyboardConvertFlag(false);
						mPromptInfoView.show();
					}
				});
				mDtvCiCaUserMenu.show();
				break;
		}
	}

	/**
	 * 添加自动搜索接收器
	 *
	 * @author YangLiu
	 */
	class AutoSearchReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int searchType = (Integer) intent.getExtra(MainMenuReceiver.DATA_SEARCHTYPE);
			Log.i(TAG, "*** AutoSearchReceiver ***");
			DtvRoot.this.sendAutoSearch(searchType);
		}
	}

	/**
	 * 显示自动搜索信息
	 *
	 * @param searchType
	 */
	private void sendAutoSearch(int searchType) {
		/*Intent mIntent = new Intent("com.start.search.dtv");
		mContext.sendBroadcast(mIntent);
		Log.i(TAG, "sendBroadcast com.start.search.dtv 1111");
		return;*/
		Log.i(TAG, "*** sendAutoSearch ***");
		DtvRoot.removeScreenSaverMessages();
		if (null != mDtvCommonManager) {
			mDtvCommonManager.updateKeyboardConvertFlag(true);
		}
		if (null != mPromptInfoView) {
			mPromptInfoView.hide();
		}
		mMainMenuRootData.DirectEnterAutoSearch();
	}

	/**
	 * 添加长按菜单键的接收器（全部设置，极简菜单）
	 *
	 * @author YangLiu
	 */
	class LongPressMenuKeyReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing()) {
				Log.i(TAG, "mEasySettingFlag: " + mEasySettingFlag);
				mMainMenuRootData.VSettingMenuVisibilityControl(false);
			}
			mEasySettingFlag = true;

			Log.i(TAG, "LongPressMenuKeyReceiver, remove ScreenSaver and promptInfo");
			DtvRoot.removeScreenSaverMessages();

			//////////////////////fengy 2014-10-27/////////////////////////
			MenuDisplayManager.getInstance(mContext).dismissAllRegistered();
			MainMenuRootData.dismissSelfSortDialog(false);

			if (null != mChannelInfoView) {
				mChannelInfoView.hide();
			}
			if (null != mPromptInfoView) {
				mPromptInfoView.hide();
			}
			if (null != mDtvCommonManager) {
				mDtvCommonManager.updateKeyboardConvertFlag(true);
			}
			if (null != mRadioBackView) {
				mRadioBackView.hide();
			}
		}
	}

	/**
	 * 添加长按home键的接收器（调出任务源）
	 *
	 * @author YangLiu
	 */
	class LongPressHomeKeyReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing()) {
				mMainMenuRootData.VSettingMenuVisibilityControl(false);
			}

			Log.i(TAG, "LongPressHomeKeyReceiver, remove ScreenSaver and promptInfo");
			DtvRoot.removeScreenSaverMessages();

			if (null != mChannelInfoView) {
				mChannelInfoView.hide();
			}
			if (null != mPromptInfoView) {
				mPromptInfoView.hide();
			}
			if (null != mDtvCommonManager) {
				mDtvCommonManager.updateKeyboardConvertFlag(true);
			}
			if (null != mRadioBackView) {
				mRadioBackView.hide();
			}

			if (is5508Q2) {
				/** 菜单消失时，让我的模式->图像视图也消失，处理悬浮菜单互斥 add by YangLiu 2015-6-17*/
				progressbar_pq_theme = ProgressbarItemsSetForTheme.getProgressbarItemsSetInstance(mContext);
				if (progressbar_pq_theme.ProgressbarIsShow) {
					progressbar_pq_theme.destory();
					Intent intent3 = new Intent("com.changhong.system.suspend_app.hide");
					mContext.sendBroadcast(intent3);
				}
			}
		}
	}

	/**
	 * 添加短按home的接收器（调出主场景）
	 *
	 * @author YangLiu
	 */
	class ShortPressSystemKeyReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getIntExtra(Intent.EXTRA_KEY_EVENT, -1) == 4124) {
				if (mMainMenuRootData != null && mMainMenuRootData.IsVSettingMenuShowing()) {
					mMainMenuRootData.VSettingMenuVisibilityControl(false);
				}
				Log.i(TAG, "ShortPressSystemKeyReceiver, Home key, remove ScreenSaver and promptInfo");
				DtvRoot.removeScreenSaverMessages();
				if (null != mChannelInfoView) {
					mChannelInfoView.hide();
				}
				if (null != mPromptInfoView) {
					mPromptInfoView.hide();
				}
				if (null != mDtvCommonManager) {
					mDtvCommonManager.updateKeyboardConvertFlag(true);
				}
				if (null != mRadioBackView) {
					mRadioBackView.hide();
				}

				if (is5508Q2) {
					/** 菜单消失时，让我的模式->图像视图也消失，处理悬浮菜单互斥 add by YangLiu 2015-6-17*/
					progressbar_pq_theme = ProgressbarItemsSetForTheme.getProgressbarItemsSetInstance(mContext);
					if (progressbar_pq_theme.ProgressbarIsShow) {
						progressbar_pq_theme.destory();
						Intent intent3 = new Intent("com.changhong.system.suspend_app.hide");
						mContext.sendBroadcast(intent3);
					}
				}
			}
		}
	}

	/**
	 * 添加简易菜单退出的接收器（退出全部设置，极简菜单）
	 *
	 * @author YangLiu
	 */
	class EasySettingExitReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "EasySettingExitReceiver, start ScreenSaver and show promptInfo");
			DtvRoot.handleScreenSaverMessages();

			Log.i(TAG, "EasySettingExitReceiver mChannelManager.getCurChannelType(): " + mChannelManager.getCurChannelType());
			if (null != mPromptInfoView) {
				if (mChannelManager.getCurChannelType() != ConstServiceType.SERVICE_TYPE_RADIO) {
					mPromptInfoView.show();
				}
			}
			if (null != mDtvCommonManager) {
				mDtvCommonManager.updateKeyboardConvertFlag(false);
			}
			if (null != mRadioBackView) {
				DtvProgram program = mChannelManager.getCurProgram();
				if (null != program && program.isRadio() && isHasSignal) {
					mRadioBackView.show();
				} else {
					mRadioBackView.hide();
				}
			}

			Log.i(TAG, "mEasySettingFlag: " + mEasySettingFlag);
			mEasySettingFlag = false;
		}
	}

	/**
	 * service重启广播接收器
	 *
	 * @author YangLiu
	 */
	class DTVServiceReboot extends BroadcastReceiver {
		int rebootTimes = 0;

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			rebootTimes++;
			Log.i(TAG, "LL DTVServiceReboot>>onReceive()>>rebootTimes = " + rebootTimes);

			String showInfo = DtvRoot.this.getString(R.string.dtv_service_refresh);//服务更新
			CommonInfoDialog myDialog = new CommonInfoDialog(DtvRoot.this);
			myDialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
			myDialog.setMessage(showInfo);
			myDialog.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
			Log.e(TAG, "DtvRoot--> DTVServiceReboot time " + rebootTimes + showInfo);
			myDialog.show();

			if (null != serviceHandler) {
				serviceHandler.sendEmptyMessage(DTV_SERVICE_RESTAR);
			}
		}
	}
}