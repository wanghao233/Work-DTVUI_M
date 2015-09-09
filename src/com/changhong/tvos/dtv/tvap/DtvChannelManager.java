package com.changhong.tvos.dtv.tvap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.pushoutView.LiveHotWiki;
import com.changhong.pushoutView.MagicButtonCommon;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.epg.normal.ChannelListFragment;
import com.changhong.tvos.dtv.epg.normal.LiveHotProgram;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseChannelUtil;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvOpFeature;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerStatus;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.system.commondialog.CommonClickDialog;
import com.changhong.tvos.system.commondialog.CommonHintToast;

public class DtvChannelManager {

	public static String TAG = "DtvChannelManager";
	private static DtvChannelManager mDtvChannelManager = null;
	private OnChangeChannelBroadcast mOnChangeChannelBroadcast = null;
	private Intent mIntent = null;
	private Handler mHandler;
	private Handler ReportHander;
	private Thread checkPlayDurationThread;
	public static ArrayList<BaseChannel> mBaseChannelList;
	private static int mChannelChangeDelay = 50;
	private static int mChannelIndexInnerClassUsed = -1;
	private List<DtvProgram> mChannelList;
	private DtvProgram mPreProgram;
	private DtvProgram mCurProgram;
	private int mCurProgramNum;
	private int mCurProgramServiceIndex;
	private int mCurProgramListIndex;
	private int mChannelType;
	private int monitoer_time_channel_num = 0;
	private boolean mProgramSetupFlag = true;
	private boolean mProgramAllList = false;
	private DtvInterface mDtvInterface = null;
	private DtvConfigManager mDtvConfigManager = null;
	private DtvEpgManager mEpgManager = null;
	private static final String CURRENT_CHANNEL_TYPE = "CHANNEL_TYPE";
	private static final String CURRENT_CHANNEL_PROGRAM = "CHANNEL_PROGRAM";
	private static final String CURRENT_CHANNEL_SERVICE_INDEX = "CHANNEL_SERVICE_INDEX";
	private static final String CHANNEL_PROGRAM_ALL = "CHANNELALL_FLAG"; //FY ADD 2014-7-22
	private static final String DTV_REPORT_FLAG = "DTV_CHANNEL_REPORT_FLAG";
	private static final String DTV_SCAN_TYPE = "DTV_CHANNEL_SCAN_TYPE";
	private static final int CHANGE_CHANNEL = 0xff;
	private static final int ChangePorgrama = 0x55;
	private static boolean mFirstChangeFlag = true;
	private static String mStartChangeTime = null;
	private static String mPreStartChangeTime = null;
	private boolean registedSyncProgramReceiver = false;
	/*****************************pang***********************************/
	private CommonHintToast hintToast = null; // Toast提示框
	/***************************微信电视*********************************/
	private final static String DTVChannelAction = "com.changhong.tvos.dtv.for3rd.DtvCurrentChannelStatue";
	/******************************lyw***********************************/
	private static String REPORT = "report";
	private Thread posProInfoThread;
	private String postProInfoData;
	private String DTVVersion;
	private String subClass = null;
	private String mac = null;
	private String channel = null;
	private String program = null;
	private String channelID;
	private String programID;
	private String status = "online";
	private String srcFro = "emb_dtv";
	private static String PortUrl = null;
	private static long PortPeriod = 60;
	private static boolean PortEnable = false;
	private static boolean isGetAddr = false;
	private static boolean isHasGetBaseChannelList = false;
	private static ConnectivityManager cManager;
	private static NetworkInfo nInfo;
	private static String province = "null";
	private static String city = "null";
	private static String district = "null";
	private static String street = "null";

	private int viewSource = 0;// 0表示dtv上下键换台，1表示epg热门推荐

	public static class ReportCurChannelType {
		public static final int LocalProgram = 0; // 本地换台
		public static final int LocalEpg = 1; // 本地EPG
		public static final int PushToUser = 2; // 欢网节目提示
		public static final int UserAdd = 3; // 用户预约节目
		// public static final int LinkOnline = 4; //本地关联点播
		public static final int LocalHot = 4; // 本地播放最多
		public static final int HotOnline = 5; // 网上最热
		public static final int VCR = 6; // 录播节目
	}

	/*************************** YangLiu ********************************/
	public static String YL = "YangLiu";
	private CommonClickDialog commonClickDialog = null;
	private ArrayList<Map<String, String>> mMapList;
	private String targetAction;
	private String targetApkName;
	private String curEventName;
	private String subEventName;

	// 频道改变的广播
	public interface OnChangeChannelBroadcast {
		public void onChangeChannel(Intent intent);
	}

	// 设置频道改变的广播
	public void setOnChangeChannelBroadcast(OnChangeChannelBroadcast arg0) {
		mOnChangeChannelBroadcast = arg0;
	}

	// 管理器的getInstance()方法
	public static DtvChannelManager getInstance() {
		if (mDtvChannelManager == null) {
			mDtvChannelManager = new DtvChannelManager();
		}
		return mDtvChannelManager;
	}

	// DTV频道管理，开启线程监听按键换台
	private DtvChannelManager() {
		Log.v(TAG, "channelManager;");
		mDtvInterface = DtvInterface.getInstance();
		mDtvConfigManager = DtvConfigManager.getInstance();
		mEpgManager = DtvEpgManager.getInstance();

		int bootChannelType = this.getBootChannelType();
		if (bootChannelType == ConstServiceType.SERVICE_TYPE_RADIO) {
			mChannelType = ConstServiceType.SERVICE_TYPE_RADIO;
		} else {
			mChannelType = ConstServiceType.SERVICE_TYPE_TV;
		}
		setCurChannelType(mChannelType);

		channelThread.start();// 换台线程
	}

	// 获取初始频道列表
	public void channelListInit(int type) {
		mChannelList = mDtvInterface.getDtvChannels(type);
	}

	// 设置当前频道类型
	public void setCurChannelType(int type) {
		Log.i(TAG, "LL setCurChannelType>>channelType = " + type);

		channelListInit(type);
		if (mChannelList != null && mChannelList.size() > 0) {
			Log.i(TAG, "LL setCurChannelType>>mChannelList.size() = " + mChannelList.size());
			mChannelType = type;
			int bootChannelNum = getBootChannelNum();
			setCurProgram(getProgramByNum(bootChannelNum));// 起始开机频道，指定“电视商场”
			saveChannelType(mChannelType);
		} else {
			mChannelType = type;
			mPreProgram = null;
			mCurProgram = null;
			mCurProgramNum = 0;
			mCurProgramListIndex = 0;
			mCurProgramServiceIndex = 0;
			DtvChannelManager.mChannelIndexInnerClassUsed = -1;
			Log.i(TAG, "LL setCurChannelType>> linr 192 = mPreProgram ========== null");
		}
	}

	// 更新当前频道列表
	public void updateCurChannelList() {
		channelListInit(mChannelType);
		setCurProgram(getProgramByServiceIndex(mCurProgramServiceIndex));
	}

	// 改变频道类型且延时播放
	public boolean changeChannelType() {
		boolean result = false;
		int type;
		if (mChannelType == ConstServiceType.SERVICE_TYPE_RADIO) {
			type = ConstServiceType.SERVICE_TYPE_TV;
		} else {
			type = ConstServiceType.SERVICE_TYPE_RADIO;
		}
		List<DtvProgram> list = mDtvInterface.getDtvChannels(type);
		if (list != null && list.size() > 0) {
			result = true;
			mChannelType = type;
			mChannelList = list;
			setCurProgram(getProgramByNum(loadCurProgramNum(mChannelType)));
			selectChannel();// 延时监听按键播放
			saveChannelType(mChannelType);
		}
		return result;
	}

	// 改变频道类型但不播放
	public boolean changeChannelTypeButNoPlay() {
		boolean result = false;
		int type;
		if (mChannelType == ConstServiceType.SERVICE_TYPE_RADIO) {
			type = ConstServiceType.SERVICE_TYPE_TV;
		} else {
			type = ConstServiceType.SERVICE_TYPE_RADIO;
		}
		List<DtvProgram> list = mDtvInterface.getDtvChannels(type);
		if (list != null && list.size() > 0) {
			result = true;
			mChannelType = type;
			mChannelList = list;
			setCurProgram(getProgramByNum(loadCurProgramNum(mChannelType)));
			saveChannelType(mChannelType);
		}
		return result;
	}

	// 取得当前的频道类型
	public int getCurChannelType() {
		return mChannelType;
	}

	// 取得当前的节目
	public DtvProgram getCurProgram() {
		if (mCurProgram == null) {
			Log.e(TAG, "LL mCurProgram line 243===================null***");
			return null;
		}
		return mCurProgram;
	}

	// 取得当前的节目位置
	public int getCurProgramListIndex() {
		return mCurProgramListIndex;
	}

	// 取得当前的节目服务号
	public int getCurPorgramServiceIndex() {
		return mCurProgramServiceIndex;
	}

	// 取得当前的节目号
	public int getCurProgramNum() {
		return mCurProgramNum;
	}

	// 通过节目号取得当前的节目
	public DtvProgram getProgramByNum(int programNum) {
		DtvProgram mProgram = null;
		if (mChannelList != null && mChannelList.size() > 0) {
			mProgram = mChannelList.get(0);
			for (int i = 0; i < mChannelList.size(); i++) {
				if (mChannelList.get(i).mProgramNum == programNum) {
					mProgram = mChannelList.get(i);
					break;
				}
			}
		}
		return mProgram;
	}

	// 通过服务号取得当前的节目
	public DtvProgram getProgramByServiceIndex(int serviceIndex) {
		DtvProgram mProgram = null;
		if (mChannelList != null && mChannelList.size() > 0) {
			mProgram = mChannelList.get(0);
			for (int i = 0; i < mChannelList.size(); i++) {
				if (mChannelList.get(i).mServiceIndex == serviceIndex) {
					mProgram = mChannelList.get(i);
					break;
				}
			}
		}
		return mProgram;
	}

	// 通过频率取得当前的节目
	public DtvProgram getProgramByFre(int Frequency) {
		if (mChannelList != null && mChannelList.size() > 0) {
			for (int i = 0; i < mChannelList.size(); i++) {
				if (mChannelList.get(i).mFrequency == Frequency) {
					Log.i(TAG, "LL Dtvchannelmanager>>getProgramByFre>>Frequency = " + Frequency + ", mFrequency = " + mChannelList.get(i).mFrequency);
					return mChannelList.get(i);
				}
			}
			return (mCurProgram == null) ? mChannelList.get(0) : mCurProgram;
		}
		return null;
	}

	// 通过频点获取EPG节目
	public DtvProgram getEpgProgram() {
		DtvOperatorManager operatorManager = DtvOperatorManager.getInstance();
		DtvOpFeature opFeature = operatorManager.getOpFeature();
		if (null == opFeature) {
			return null;
		}
		Log.i(TAG, "LL getEpgProgram()>>isEpgOnlyOneFreq = " + opFeature.isEpgOnlyOneFreq());
		if (opFeature.isEpgOnlyOneFreq() == true) {
			int frequency = opFeature.getEpgFrequency();
			if (mCurProgram != null) {
				if (mCurProgram.mFrequency == frequency) {
					return null;
				}
				Log.e(TAG, "LL getEpgProgram()>>mCurProgram.mFrequency = " + mCurProgram.mFrequency + "frequency = " + frequency);
			}

			if (mChannelList != null) {
				for (int i = 0; i < mChannelList.size(); i++) {
					if (mChannelList.get(i).mFrequency == frequency) {
						return mChannelList.get(i);
					}
				}
			}
		}
		return null;
	}

	// 得到换台的次数
	public int getCurProgram_change_program_Num() {
		return monitoer_time_channel_num;
	}

	// 设置换台的次数
	public void setCurProgram_change_program_Num(int chang_progream_num) {
		monitoer_time_channel_num = chang_progream_num;
	}

	// 设置当前节目
	public synchronized void setCurProgram(DtvProgram program) {
		mPreProgram = mCurProgram;
		mCurProgram = program;

		if (mPreProgram != mCurProgram) {
			DtvRoot.SetisNotDTVChangedFlag(false);
		}

		if (null != mCurProgram) {
			Log.i(TAG, "set setCurProgram");
			mCurProgramListIndex = mChannelList.indexOf(mCurProgram);
			mCurProgramNum = mCurProgram.getProgramNum();
			mCurProgramServiceIndex = mCurProgram.getProgramServiceIndex();
			DtvChannelManager.mChannelIndexInnerClassUsed = mCurProgramServiceIndex;
		} else {
			mCurProgramListIndex = 0;
			mCurProgramNum = 0;
			mCurProgramServiceIndex = 0;
			DtvChannelManager.mChannelIndexInnerClassUsed = -1;
			Log.i(TAG, "mCurProgram is line 349 ================null");
		}
	}

	// 设置当前节目并确定是否更新之前节目
	public synchronized void setCurProgram(DtvProgram program, boolean updatePreProgram) {
		if (updatePreProgram == true) {
			mPreProgram = mCurProgram;
		}
		mCurProgram = program;
		if (null != mCurProgram) {
			mCurProgramListIndex = mChannelList.indexOf(mCurProgram);
			mCurProgramNum = mCurProgram.getProgramNum();
			mCurProgramServiceIndex = mCurProgram.getProgramServiceIndex();
			DtvChannelManager.mChannelIndexInnerClassUsed = mCurProgramServiceIndex;
		} else {
			mCurProgramListIndex = 0;
			mCurProgramNum = 0;
			mCurProgramServiceIndex = 0;
			DtvChannelManager.mChannelIndexInnerClassUsed = -1;
			Log.i(TAG, "mCurProgram is line 369======================null");
		}
		Log.i(TAG, "setCurProgram(DtvProgram ,boolean ) end");
	}

	// 设置当前节目是否过滤
	public void setCurProgramSkipState(boolean skip) {
		if (null != mCurProgram) {
			mCurProgram.setSkipState(skip);
		}
	}

	// 交换两个节目
	public void programSwap(int index1, int index2) {
		Log.v("TAG", "programSwap index1 =" + index1);
		Log.v("TAG", "programSwap index2 =" + index2);
		if (index1 > mChannelList.size() - 1 || index1 < 0 || index2 > mChannelList.size() - 1 || index2 < 0) {
			Log.e(TAG, "programSwap()>> index out of index");
			return;
		}
		int programnum = mChannelList.get(index1).getProgramNum();
		DtvProgram program1 = mChannelList.get(index1);
		DtvProgram program2 = mChannelList.get(index2);
		program1.setProgramNum(program2.getProgramNum());
		program2.setProgramNum(programnum);
		mChannelList.remove(index1);
		mChannelList.add(index1, program2);
		mChannelList.remove(index2);
		mChannelList.add(index2, program1);
		Log.i(TAG, "LL programSwap()>>program1.mServiceIndex = " + program1.mServiceIndex + ",program2.mServiceIndex = " + program2.mServiceIndex);
		mDtvInterface.channelSwap(program1.mServiceIndex, program2.mServiceIndex);
		setCurProgram(program1);
		if (program1.mServiceIndex != program2.mServiceIndex) {
			selectChannel();
		}
	}

	// 通过节目号获取节目
	public DtvProgram getProgramByNumIfExists(int programNum) {
		DtvProgram mProgram = null;
		if (mChannelList != null && mChannelList.size() > 0) {
			for (int i = 0; i < mChannelList.size(); i++) {
				if (mChannelList.get(i).mProgramNum == programNum) {
					mProgram = mChannelList.get(i);
					break;
				}
			}
		}
		return mProgram;
	}

	// 通过服务号获取节目
	public DtvProgram getProgramByServiceIndexIfExists(int serviceIndex) {
		DtvProgram mProgram = null;
		if (mChannelList != null && mChannelList.size() > 0) {
			for (int i = 0; i < mChannelList.size(); i++) {
				if (mChannelList.get(i).mServiceIndex == serviceIndex) {
					mProgram = mChannelList.get(i);
					break;
				}
			}
		}
		return mProgram;
	}

	// 强制监听按键换台（按键换台）
	public void channelForceChangeReplay() {

		if (mCurProgram != null) {
			Log.i(TAG, "LL channelForceChangeReplay()>>programNum = " + mCurProgram.mProgramNum);
			mProgramSetupFlag = true;
			selectChannel();
		}
	}

	// 设置”全部节目“开关
	public void setAllchFlag(boolean AllchFlag) {
		mDtvConfigManager.setValue(CHANNEL_PROGRAM_ALL, String.valueOf(AllchFlag));
	}

	// 获取”全部节目“开关
	public boolean GetAllchFlag() {
		boolean isGuideMode = false;
		String tmpflag = mDtvConfigManager.getValue(CHANNEL_PROGRAM_ALL);
		if (null != tmpflag) {
			isGuideMode = Boolean.valueOf(tmpflag);
		}
		return (isGuideMode);
	}

	// 强制通过频道号换台（开机换台）
	public void channelForceChangeByProgramNum(int num, boolean isPlayDefault) {
		DtvProgram program = null;
		if (isPlayDefault == false) {
			program = getProgramByNumIfExists(num);
		} else {
			program = getProgramByNum(num);
		}
		if (program != null) {
			Log.i(TAG, "LL channelForceChangeByProgramNum()>>programNum = " + program.mProgramNum);
			setCurProgram(program);
			selectChannel();
		}
	}

	// 通过节目号换台
	public boolean channelChangeByProgramNum(int num, boolean isPlayDefault) {
		boolean change = false;
		if (mCurProgramNum != num) {
			DtvProgram program = null;
			if (isPlayDefault == false) {
				program = getProgramByNumIfExists(num);
			} else {
				program = getProgramByNum(num);
			}
			if (program != null) {
				setCurProgram(program);
				selectChannel();
				change = true;
			}
		}
		return change;
	}

	// 强制通过服务号换台
	public void channelForceChangeByProgramServiceIndex(int index, boolean isPlayDefault) {
		DtvProgram program = null;
		if (isPlayDefault == false) {
			program = getProgramByServiceIndexIfExists(index);
		} else {
			program = getProgramByServiceIndex(index);
		}
		if (program != null) {
			setCurProgram(program);
			selectChannel();
		}
	}

	// 通过服务号换台
	public boolean channelChangeByProgramServiceIndex(int serviceIndex, boolean isPlayDefault) {
		boolean change = false;
		if (mCurProgramServiceIndex != serviceIndex) {
			DtvProgram program = null;
			if (isPlayDefault == false) {
				program = getProgramByServiceIndexIfExists(serviceIndex);
			} else {
				program = getProgramByServiceIndex(serviceIndex);
			}
			if (program != null) {
				setCurProgram(program);
				selectChannel();
				change = true;
			}
		}
		Log.i(TAG, "LL channelChangeByProgramServiceIndex>>mCurProgramServiceIndex = " + mCurProgramServiceIndex + ",serviceIndex = " + serviceIndex + ",change = " + change);
		return change;
	}

	// 通过服务号换台
	public void channelChangeUpOffset(int offset) {
		boolean findChannel = false;
		int index = mCurProgramListIndex + offset;
		Log.v(TAG, "channelChangeUp curProgramIndex =" + mCurProgramListIndex);
		do {
			if (index < mChannelList.size() && !mChannelList.get(index).mIsSkip) {
				findChannel = true;
				break;
			}
			index++;
		} while (index < mChannelList.size());

		if (!findChannel) {
			for (index = 0; index < mCurProgramListIndex; index++) {
				if (!mChannelList.get(index).mIsSkip) {
					findChannel = true;
					break;
				}
			}
		}
		Log.v(TAG, "channelChangeUp index =" + mCurProgramListIndex);
		if (findChannel) {
			setCurProgram(mChannelList.get(index));
			selectChannel();
		}
	}

	// 按照偏移量向上换台
	public int channelChangeUp(boolean isPlay) {
		boolean findChannel = false;
		int index = mCurProgramListIndex + 1;
		boolean tmpskipstate = false;

		mProgramAllList = GetAllchFlag();
		do {
			if (index < mChannelList.size()) {
				tmpskipstate = (!mProgramAllList && mChannelList.get(index).mIsSkip);
				/*Log.i(TAG, "fyyyyyy UP111111 mProgramAllList = " + mProgramAllList);
				Log.i(TAG, "fyyyyyy UP111111 mIsSkip = " + mChannelList.get(index).mIsSkip);
				Log.i(TAG, "fyyyyyy UP111111 tmpskipstate = " + tmpskipstate);*/
			}

			// if (index < mChannelList.size() && !mChannelList.get(index).mIsSkip) {
			if (index < mChannelList.size() && !tmpskipstate) {
				findChannel = true;
				break;
			}
			index++;
		} while (index < mChannelList.size());

		if (!findChannel) {
			for (index = 0; index < mCurProgramListIndex; index++) {
				tmpskipstate = (!mProgramAllList && mChannelList.get(index).mIsSkip);

				if (index < mChannelList.size() && !tmpskipstate) {
					// if (index < mChannelList.size()&&!mChannelList.get(index).mIsSkip) {
					findChannel = true;
					break;
				}
			}
		}
		Log.v(TAG, "channelChangeUp index =" + index + " size is: " + mChannelList.size());
		if (findChannel) {
			if (true == isPlay) {
				setCurProgram(mChannelList.get(index));
				selectChannel();
			} else {
				Log.i(TAG, "LL channelChangeUp()>>mProgramSetupFlag = " + mProgramSetupFlag);
				if (mProgramSetupFlag == true) {
					mProgramSetupFlag = false;
					setCurProgram(mChannelList.get(index));
				} else {
					setCurProgram(mChannelList.get(index), false);
				}
			}
		} else {
			index = -1;
		}
		return index;
	}

	// 向上换台
	public void channelChangeNextOffset(int offset) {
		boolean findChannel = false;
		int index = mCurProgramListIndex - offset;
		Log.v(TAG, "channelChangeDown curProgramIndex =" + mCurProgramListIndex);
		do {
			if (index >= 0) {
				findChannel = true;
				break;
			}
			index--;
		} while (index >= 0);

		if (!findChannel) {
			for (index = mChannelList.size() - 1; index > mCurProgramListIndex; index--) {
				findChannel = true;
				break;
			}
		}
		Log.v(TAG, "channelChangeNext index =" + index);
		if (findChannel) {
			setCurProgram(mChannelList.get(index));
			selectChannel();
		}
	}

	// 向后换台
	public void channelChangeNext() {
		boolean findChannel = false;
		int index = mCurProgramListIndex - 1;
		Log.v(TAG, "channelChangeDown curProgramIndex =" + mCurProgramListIndex);
		do {
			if (index >= 0) {
				findChannel = true;
				break;
			}
			index--;
		} while (index >= 0);

		if (!findChannel) {
			for (index = mChannelList.size() - 1; index > mCurProgramListIndex; index--) {
				findChannel = true;
				break;
			}
		}
		Log.v(TAG, "channelChangeNext index =" + index);
		if (findChannel) {
			setCurProgram(mChannelList.get(index));
			selectChannel();
		}
	}

	// 安照偏移量向前换台
	public void channelChangePreOffset(int offset) {
		boolean findChannel = false;
		int index = mCurProgramListIndex + offset;
		Log.v(TAG, "channelChangeUp curProgramIndex =" + mCurProgramListIndex);
		do {
			if (index < mChannelList.size()) {
				findChannel = true;
				break;
			}
			index++;
		} while (index < mChannelList.size());

		if (!findChannel) {
			for (index = 0; index < mCurProgramListIndex; index++) {
				findChannel = true;
				break;
			}
		}
		Log.v(TAG, "channelChangePre index =" + mCurProgramListIndex);
		if (findChannel) {
			setCurProgram(mChannelList.get(index));
			selectChannel();
		}
	}

	// 向前换台
	public void channelChangePre() {
		boolean findChannel = false;
		int index = mCurProgramListIndex + 1;
		Log.v(TAG, "channelChangeUp curProgramIndex =" + mCurProgramListIndex);
		do {
			if (index < mChannelList.size()) {
				findChannel = true;
				break;
			}
			index++;
		} while (index < mChannelList.size());

		if (!findChannel) {
			for (index = 0; index < mCurProgramListIndex; index++) {
				findChannel = true;
				break;
			}
		}
		Log.v(TAG, "channelChangePre index =" + mCurProgramListIndex);
		if (findChannel) {
			setCurProgram(mChannelList.get(index));
			selectChannel();
		}
	}

	// 按照偏移量向下换台
	public void channelChangeDownOffset(int offset) {
		boolean findChannel = false;
		int index = mCurProgramListIndex - offset;
		Log.v(TAG, "channelChangeDown curProgramIndex =" + mCurProgramListIndex);
		do {
			if (index >= 0 && !mChannelList.get(index).mIsSkip) {
				findChannel = true;
				break;
			}
			index--;
		} while (index >= 0);

		if (!findChannel) {
			for (index = mChannelList.size() - 1; index > mCurProgramListIndex; index--) {
				if (!mChannelList.get(index).mIsSkip) {
					findChannel = true;
					break;
				}
			}
		}
		Log.v(TAG, "channelChangeDown index =" + index);
		if (findChannel) {
			setCurProgram(mChannelList.get(index));
			selectChannel();
		}
	}

	// 向下换台，并确定是否播放
	public int channelChangeDown(boolean isPlay) {
		boolean findChannel = false;
		int index = mCurProgramListIndex - 1;
		// findChannel = true;
		boolean tmpskipstate = false;
		mProgramAllList = GetAllchFlag();
		do {
			// if (index >= 0 && index < mChannelList.size()&&!mChannelList.get(index).mIsSkip) {
			if (index >= 0) {
				tmpskipstate = (!mProgramAllList && mChannelList.get(index).mIsSkip);
			}

			if (index >= 0 && index < mChannelList.size() && !tmpskipstate) {
				findChannel = true;
				break;
			}
			index--;
		} while (index >= 0);

		if (!findChannel) {
			for (index = mChannelList.size() - 1; index > mCurProgramListIndex; index--) {
				// tmpskipstate = false;//mChannelList.get(index).mIsSkip;
				tmpskipstate = (!mProgramAllList && mChannelList.get(index).mIsSkip);
				/*Log.i(TAG, "fyyyyyy DOWN000111 mProgramAllList = " + mProgramAllList);
				Log.i(TAG, "fyyyyyy DOWN000111 mIsSkip = " + mChannelList.get(index).mIsSkip);
				Log.i(TAG, "fyyyyyy DOWN000111 tmpskipstate = " + tmpskipstate);*/

				if (index < mChannelList.size() && !tmpskipstate) {
					// if (index < mChannelList.size()&&!mChannelList.get(index).mIsSkip) {
					findChannel = true;
					break;
				}
			}
		}
		Log.v(TAG, "channelChangeDown index =" + index);
		if (findChannel) {
			if (true == isPlay) {
				setCurProgram(mChannelList.get(index));
				selectChannel();
			} else {
				Log.i(TAG, "LL channelChangeDown()>>mProgramSetupFlag = " + mProgramSetupFlag);
				if (mProgramSetupFlag == true) {
					mProgramSetupFlag = false;
					setCurProgram(mChannelList.get(index));
				} else {

					setCurProgram(mChannelList.get(index), false);
				}
			}
		} else {
			index = -1;
		}
		return index;
	}

	// 是否改变为之前的频道
	public boolean channelChangeReturn() {
		boolean change = false;
		DtvProgram program = mPreProgram;
		if (mCurProgram != null && mPreProgram != null) {
			if (mPreProgram.mIsRadio == mCurProgram.mIsRadio) {
				if (!program.equals(mCurProgram)) {
					setCurProgram(program);
					selectChannel();
					change = true;
				}
			} else {
				change = changeChannelType();
			}
		}
		return change;
	}

	// 停止播放
	public void channelStop() {
		if (null != mHandler) {
			mHandler.removeMessages(CHANGE_CHANNEL);
		}
		mDtvInterface.channelStop();
	}

	// 获取当前DVB通道信息
	public DVBCCarrier getDVBCCurTunerInfo() {
		return mDtvInterface.getDVBCCurTunerInfo();
	}

	// 获取音频轨道
	public String[] getAudioTrack(String[] track) {
		if (mCurProgram != null) {
			return mCurProgram.getAudioTrack(track);
		} else {
			String[] audioTrack = new String[1];
			audioTrack[0] = track[1];
			return audioTrack;
		}
	}

	// 获取音频轨道号
	public int getAudioTrackSelIndex() {
		if (mCurProgram != null) {
			return mCurProgram.getAudioTrackSelIndex();
		}
		return 0;
	}

	// 获取音频Sel
	public String getAudioTrackSel() {
		if (mCurProgram != null) {
			return mCurProgram.mAudioTrackSel;
		}
		return "chi";
	}

	// 获取音频模式Sel
	public int getAudioModeSel() {
		if (mCurProgram != null) {
			return mCurProgram.mAudioModeSel;
		}
		return 0;
	}

	// 获取音频模式
	public void setAudioMode(int index) {
		if (mCurProgram != null) {
			mCurProgram.setAudioMode(index);
		}
	}

	// 获取音频轨道
	public void setAudioTrack(int index) {
		if (mCurProgram != null) {
			mCurProgram.setAudioTrack(index);
		}
	}

	// 获取频道列表
	public List<DtvProgram> getChannelList() {
		return mChannelList;
	}

	// 获取观看过的频道列表
	public List<DtvProgram> getWatchedChannelList() {
		return mDtvInterface.getWatchedChannelList(mChannelType);
	}

	// 获取频道详细信息
	public DTVChannelDetailInfo getDtvChannelDetailInfo(int channelIndex) {
		return mDtvInterface.getDtvChannelDetailInfo(channelIndex);
	}

	// 获取DtvTuner状态
	public DtvTunerStatus getDtvTunerStatus() {
		return mDtvInterface.getTunerStatus();
	}

	// 获取开机启动频道号
	public int getBootChannelNum() {
		int bootChannelIndex = -1;
		int bootChannelNum = 0;
		bootChannelIndex = this.getBootChannelIndex();
		if (-1 != bootChannelIndex) {
			bootChannelNum = mDtvInterface.getBootChannelNum(bootChannelIndex);
		} else {
			bootChannelNum = this.loadCurProgramNum(this.loadCurChannelType());
		}
		Log.i(TAG, "get bootChannelNum is " + bootChannelNum);
		return bootChannelNum;
	}

	// 获取开机频道类型
	public int getBootChannelType() {
		int bootChannelIndex = -1;
		int bootChannelType = ConstServiceType.SERVICE_TYPE_TV;
		bootChannelIndex = this.getBootChannelIndex();

		if (-1 != bootChannelIndex) {
			bootChannelType = mDtvInterface.getBootChannelType(bootChannelIndex);
		} else {
			bootChannelType = this.loadCurChannelType();
		}
		return bootChannelType;
	}

	// 通过频道位置获取频道类型
	public int getChannelTypeByChannelIndex(int channelIndex) {
		int channelType = ConstServiceType.SERVICE_TYPE_TV;
		channelType = mDtvInterface.getBootChannelType(channelIndex);
		return channelType;
	}

	// 获取开机启动频道位置
	private int getBootChannelIndex() {
		DtvOpFeature dtvOpFeature = DtvOperatorManager.getInstance().getOpFeature();
		int bootChannelIndex = -1;
		if (null != dtvOpFeature) {
			bootChannelIndex = dtvOpFeature.getStartChannelIndex();
			Log.i(TAG, "LL getBootChannelIndex()>>bootChannelIndex = " + bootChannelIndex);
		}
		Log.i(TAG, "LL bootChannelIndex = " + bootChannelIndex);
		return bootChannelIndex;
	}

	// 读取当前节目类型
	private int loadCurChannelType() {
		int type = ConstServiceType.SERVICE_TYPE_TV;
		String str = mDtvConfigManager.getValue(CURRENT_CHANNEL_TYPE);
		if (null != str) {
			type = Integer.valueOf(str);
		}
		return type;
	}

	// 保存当前节目类型
	private void saveChannelType(int value) {
		mDtvConfigManager.setValue(CURRENT_CHANNEL_TYPE, String.valueOf(value));
	}

	// 读取当前节目号
	private int loadCurProgramNum(int type) {
		int curNum = 0;
		String str = mDtvConfigManager.getValue(CURRENT_CHANNEL_PROGRAM.concat(String.valueOf(type)));
		if (null != str) {
			curNum = Integer.valueOf(str);
		}
		// Log.i(TAG,"fyyy0 loadCurProgramNum = " + curNum);
		if (curNum == 0) {
			curNum = DtvRoot.GetCurrentChNum();
			Log.i(TAG, "fyyyy11111 loadtmpcurNum = " + curNum);
		}
		return curNum;
	}

	// 保存当前节目号
	private void saveCurProgramNum(int value, int type, int serviceindex) {
		DtvRoot.StoreCurrenChNum(value);
		mDtvConfigManager.setValue(CURRENT_CHANNEL_PROGRAM.concat(String.valueOf(type)), String.valueOf(value));
		mDtvConfigManager.setValue(CURRENT_CHANNEL_SERVICE_INDEX.concat(String.valueOf(type)), String.valueOf(serviceindex));
	}

	// 重置保存参数
	private void resetSavedProg() {
		mDtvConfigManager.setValue(CURRENT_CHANNEL_TYPE, String.valueOf(ConstServiceType.SERVICE_TYPE_TV));
		mDtvConfigManager.setValue(CURRENT_CHANNEL_PROGRAM.concat(String.valueOf(ConstServiceType.SERVICE_TYPE_TV)), String.valueOf(0));
		mDtvConfigManager.setValue(CURRENT_CHANNEL_SERVICE_INDEX.concat(String.valueOf(ConstServiceType.SERVICE_TYPE_TV)), String.valueOf(0));
		DtvRoot.StoreCurrenChNum(1);
	}

	// 获取当前InnerClassUsed服务号
	public static int getCurServiceIndexInnerClassUsed() {
		return DtvChannelManager.mChannelIndexInnerClassUsed;
	}

	private SyncProgramReceiver mSyncProgramReceiver = null;
	private IntentFilter mSyncProgramIntentFilter = null;

	// 意图过滤的接收器
	public void installInnerReceiver(Context context) {
		if (mSyncProgramReceiver == null && registedSyncProgramReceiver == false) {
			mSyncProgramReceiver = new SyncProgramReceiver();
			mSyncProgramIntentFilter = new IntentFilter("com.changhong.tvos.smartrecommend.switch");
			context.registerReceiver(mSyncProgramReceiver, mSyncProgramIntentFilter);
			registedSyncProgramReceiver = true;
		}
	}

	// 取消意图过滤的接收
	public void uninstallInnerReceiver(Context context) {
		if (mSyncProgramReceiver != null && registedSyncProgramReceiver == true) {
			context.unregisterReceiver(mSyncProgramReceiver);
			mSyncProgramReceiver = null;
			registedSyncProgramReceiver =false;
		}
	}

	// 通过服务号设置当前频道接收器
	public class SyncProgramReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "LL [enter]>>SyncProgramReceiver>>onReceiver()***");
			int serviceIndex = intent.getIntExtra("serviceIndex", -1);
			Log.i(TAG, "LL [enter]>>SyncProgramReceiver>>serviceIndex = " + serviceIndex);
			if (serviceIndex != -1) {
				DtvProgram dtvProgram = getProgramByServiceIndex(serviceIndex);
				if (dtvProgram != null) {
					setCurProgram(dtvProgram);
				}
			}
		}
	}

	// 得到最大的节目号
	public int getMaxChannelNum() {

		if (null != mChannelList && mChannelList.size() > 0) {
			int max = mChannelList.get(0).mProgramNum;
			for (DtvProgram program : mChannelList) {
				if (program.mProgramNum > max) {
					max = program.mProgramNum;
				}
			}
			return max;
		}
		return 0;
	}

	// 获得之前频道
	public DtvProgram getPreChannel() {
		return mPreProgram;
	}

	// 设置之前频道
	public void setPreChannel(DtvProgram preProgram) {
		mPreProgram = preProgram;
	}

	// 设置排序改变后的节目列表
	public void setChangedSortList(List<DtvProgram> myChangeList) {
		// TODO Auto-generated method stub
		mChannelList.clear();
		mChannelList.addAll(myChangeList);

		if (null != mCurProgram) {
			mCurProgramListIndex = myChangeList.indexOf(mCurProgram);
			mCurProgram.setProgramNum(myChangeList.get(mCurProgramListIndex).mProgramNum);
			mCurProgramNum = mCurProgram.getProgramNum();
		}
		mDtvInterface.setSortChannelList(myChangeList);
	}

	// 监听按键换台
	public void selectChannel() {
		if (null != mHandler) {
			mHandler.removeMessages(CHANGE_CHANNEL);
			mHandler.sendEmptyMessageDelayed(CHANGE_CHANNEL, mChannelChangeDelay);
			// mHandler.sendEmptyMessage(CHANGE_CHANNEL);
		} else {
			Log.e(TAG, "mHandler still not ready");
		}

		Log.i(REPORT, "lyw-hauntai---------");
		if (null != ReportHander) {
			ReportHander.removeMessages(ChangePorgrama);
			ReportHander.sendEmptyMessageDelayed(ChangePorgrama, mChannelChangeDelay);
			// mHandler.sendEmptyMessage(CHANGE_CHANNEL);
		} else {
			Log.e(REPORT, "ReportHander still not ready");
		}

		/** 给微信电视发送onDestroy广播*/
		sendWeChatTVBroadcast(DtvRoot.mContext, "DtvChChanged");
	}

	// 设置Report标志
	public void setDtvReportFlag(boolean reportFlag) {// 2014-8-27 CuiXiaoYan
		mDtvConfigManager.setValue(DTV_REPORT_FLAG, String.valueOf(reportFlag));
	}

	// 获取Report标志
	public boolean GetDtvReportFlag() {
		String tmpflag = mDtvConfigManager.getValue(DTV_REPORT_FLAG);
		if (tmpflag != null) {
			return (Boolean.valueOf(tmpflag));
		} else {
			return false;
		}
	}

	// 设置DTV扫描方式
	public void setDtvScanType(int scanType) {
		mDtvConfigManager.setValue(DTV_SCAN_TYPE, String.valueOf(scanType));
	}

	// 获取DTV扫描方式
	public int GetDtvScanType() {
		String tmptype = mDtvConfigManager.getValue(DTV_SCAN_TYPE);
		if (tmptype != null) {
			return (Integer.valueOf(tmptype));
		} else {
			return 0;
		}
	}

	// 得到子版本信息
	public String getSubClass() {
		String subClass = null;
		subClass = DtvRoot.mContext.getResources().getString(R.string.collect13);
		return subClass;
	}

	// 得到节目换台时间
	public String getProgramaChangeTime() {
		// TODO Auto-generated method stub
		return mStartChangeTime;
	}

	// 取得当前正在播放的节目
	public DtvProgram mCurProgram() {
		// TODO Auto-generated method stub
		return mCurProgram;
	}

	// 获取本机的MAC地址
	public String ReadMac() {
		String path = "/sys/class/net/eth0/address";
		// oCuqZuGRd1tg_DWIrlrovuxVcv60
		String mac = "";
		// 读取path路径
		File file = new File(path);
		if (file.isDirectory()) {
			Log.d("TestFile", "The File doesn't not exist.");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;

					while ((line = buffreader.readLine()) != null) {
						mac = line;
						Log.i("line", line);
					}
					instream.close();
				}
			} catch (java.io.FileNotFoundException e) {
				Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				Log.d("TestFile", e.getMessage());
			}
		}
		return mac;
	}

	// 得到viewSource
	public int getViewSource() {
		return viewSource;
	}

	// 设置viewSource
	public void setViewSource(int viewSource) {
		this.viewSource = viewSource;
	}

	/**
	 * 按键换台线程
	 */
	private Thread channelThread = new Thread("channelThread") {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			mHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					synchronized (mCurProgram) {
						Log.d(TAG, "channelThread handle msg " + msg.what + Thread.currentThread());
						Log.d(TAG, "channelThread Recive mDtvInterface add= :" + mDtvInterface);

						// 等待一个CHANG_CHANNEL消息这个消息是从DTVROOT监控上下键的地方发来
						if (msg.what == CHANGE_CHANNEL && null != mCurProgram && null != mDtvInterface) {
							Log.i(TAG, "channelThread Recive CHANGE_CHANNEL :" + mCurProgram.mProgramName);
							Log.i(TAG, "channelThread Recive mDtvInterface add= :" + mDtvInterface);

							/**** 完成节目的换台 *****/
							int result = mDtvInterface.channelSelect(mCurProgram.mServiceIndex);// 调用API再调用SERVICE再到JNI来播放节目
							Log.d(TAG, "inThread: " + Thread.currentThread() + "channel has change to:name= " + mCurProgram.mProgramName + "	num=" +mCurProgram.mProgramNum);
							if (result == 0) {
								/**** 上报节目信息 *****/
								mStartChangeTime = DtvRoot.getCurrentTime().toString();
								if (mFirstChangeFlag == true) {
									mPreStartChangeTime = mStartChangeTime;
									mFirstChangeFlag = false;
								} else {
									if (IsReportDtvInfo()) {
										reportPreDtvProgram();// 上报先前节目信息
									}
									mPreStartChangeTime = mStartChangeTime;
								}
								reportCurDtvProgram();// 上报节目信息

								/**** 检测节目时间和当前时间 *****/
								//	checkEventTime();		YangLiu	2015-5-20

								/**** 保存当前节目信息 *****/
								DtvChannelManager.this.saveCurProgramNum(mCurProgram.mProgramNum, mChannelType, mCurProgram.mServiceIndex);
								DtvChannelManager.this.setAudioMode(mCurProgram.mAudioModeSel);

								/**** 给第三方发布一个消息只要在android系统注册了这个消息就会受到这个广播 *****/
								if (mIntent == null) {
									mIntent = new Intent("com.changhong.tvos.dtv.for3rd.SmartEPG");
									mIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
								}
								mIntent.putExtra("ServiceID", mCurProgram.mServiceID);
								mIntent.putExtra("ServiceIndex", mCurProgram.mServiceIndex);
								mIntent.putExtra("ServiceName", mCurProgram.mProgramName);
								mIntent.putExtra("Tsid", mCurProgram.mTsID);
								mIntent.putExtra("NetworkID", mCurProgram.mOrgNetID);
								mIntent.putExtra("ProviderID", mCurProgram.mProviderID);
								mOnChangeChannelBroadcast.onChangeChannel(mIntent);
							} else {
								Log.e(TAG, "DtvChannelManager change channel failed :" + mCurProgram.mProgramNum);
							}
						}
					}
				}
			};
			Looper.loop();
		}
	};

	// 上报先前节目信息，完成本地上报和查看更多节目信息提示
	private void reportPreDtvProgram() {
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date begin = dfs.parse(mPreStartChangeTime);
			java.util.Date end = dfs.parse(mStartChangeTime);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成s
			Log.d(TAG, "mPreStartChangeTime: " + mPreStartChangeTime + ", mStartChangeTime: " + mStartChangeTime + ", between: " + between);

			if (between <= 40 && between > 1) {// 节目播放时间在40s以内，则计数一次（40s模拟连续换台）
				if (mPreProgram != null) {
					monitoer_time_channel_num = monitoer_time_channel_num + 1;
					Log.d(TAG, "monitoer_time_channel_num: " + monitoer_time_channel_num);
					if (monitoer_time_channel_num >= 20) {
						// monitoer_time_channel_num = 0;
						Log.e(TAG, "switch channel 20 times continuously, press 'OK' see more program information");
					}
				} else {
					Log.e(TAG, "reportPreDtvProgram, but mPreProgram is null!");
				}
			} else {// 播放时间超过40s，表示没有连续换台，计数次数归0
				monitoer_time_channel_num = 0;
			}

			if (between >= 30) {// 节目播放超过30s，开始数据本地上报
				if (mPreProgram != null) {
					DtvEvent[] PFinfo = mEpgManager.getPFInfo(mPreProgram.mServiceIndex);
					if ((PFinfo[0] != null) && (PFinfo[0].getTitle() != null)) {
						Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + DtvRoot.mContext.getResources().getString(R.string.collect11) + "|subClass:"
								+ DtvRoot.mContext.getResources().getString(R.string.collect13) + "|reportInfo:channel=" + mPreProgram.mProgramName + ";program=" + PFinfo[0].getTitle().trim()
								+ ";duration=" + mPreStartChangeTime + "|" + mStartChangeTime);
					} else {
						Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + DtvRoot.mContext.getResources().getString(R.string.collect11) + "|subClass:"
								+ DtvRoot.mContext.getResources().getString(R.string.collect13) + "|reportInfo:channel=" + mPreProgram.mProgramName + ";program=null;duration=" + mPreStartChangeTime
								+ "|" + mStartChangeTime);
					}
				} else {
					Log.e(TAG, "reportPreDtvProgram, but mPreProgram is null!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 上报当前节目信息
	private void reportCurDtvProgram() {//每隔10分钟进行数据本地上报
		if (mHandler != null) {
			mHandler.removeCallbacks(reportCurDtvRun);
			mHandler.postDelayed(reportCurDtvRun, 600000);
		} else {
			Log.e(TAG, "reportCurDtvProgram, but mHandler is null!");
		}
	}

	// 是否需要显示节目信息
	public Boolean IsReportDtvInfo() {// 2014-9-24 CuiXiaoYan
		if ("0".equals(SystemProperties.get("sys.pm.sleep.flag"))) {
			if ((DtvRoot.getTopTaskPackageName()).equals("com.changhong.tvos.dtv")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// 显示当前节目信息的runnable
	private Runnable reportCurDtvRun = new Runnable() {
		public void run() {
			if (IsReportDtvInfo()) {
				if (mCurProgram != null) {
					DtvEvent[] PFinfo = mEpgManager.getPFInfo(mCurProgram.mServiceIndex);
					if ((PFinfo[0] != null) && (PFinfo[0].getTitle() != null)) {
						Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + DtvRoot.mContext.getResources().getString(R.string.collect11) + "|subClass:"
								+ DtvRoot.mContext.getResources().getString(R.string.collect13) + "|reportInfo:channel=" + mCurProgram.mProgramName + ";program=" + PFinfo[0].getTitle().trim()
								+ ";status=online");
					} else {
						Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + DtvRoot.mContext.getResources().getString(R.string.collect11) + "|subClass:"
								+ DtvRoot.mContext.getResources().getString(R.string.collect13) + "|reportInfo:channel=" + mCurProgram.mProgramName + ";program=null;status=online");
					}
					reportCurDtvProgram();
				} else {
					Log.e(TAG, "reportCurDtvRun, but mCurProgram is null!");
				}
			}
		}
	};

	// 重新初始化，复位
	public void reset() {
		if (null != mHandler) {
			mHandler.removeMessages(CHANGE_CHANNEL);
		}
		if (null != ReportHander) {
			ReportHander.removeMessages(ChangePorgrama);
		}

		mChannelList.clear();
		mPreProgram = null;
		mCurProgram = null;
		mCurProgramNum = 0;
		mCurProgramListIndex = 0;
		mCurProgramServiceIndex = 0;
		DtvChannelManager.mChannelIndexInnerClassUsed = -1;
		resetSavedProg();
		mDtvInterface.dtvReset();
		Log.i(TAG, "LL DTV RESET >> linr 159 =mCurProgram mPreProgram ========== null");
	}

	// 换台的runnable
	@SuppressWarnings("unused")
	private Runnable mRun = new Runnable() {
		public void run() {
			int result = mDtvInterface.channelSelect(mCurProgram.mServiceIndex);// 为0表示成功，小于0表示失败
			if (result == 0) {
				DtvChannelManager.this.saveCurProgramNum(mCurProgram.mProgramNum, mChannelType, mCurProgram.mServiceIndex);
				DtvChannelManager.this.setAudioMode(mCurProgram.mAudioModeSel);
				if (mIntent == null) {
					mIntent = new Intent("com.changhong.tvos.dtv.for3rd.SmartEPG");
					mIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
				}
				mIntent.putExtra("ServiceID", mCurProgram.mServiceID);
				mIntent.putExtra("ServiceName", mCurProgram.mProgramName);
				mIntent.putExtra("Tsid", mCurProgram.mTsID);
				mIntent.putExtra("NetworkID", mCurProgram.mOrgNetID);
				mIntent.putExtra("ProviderID", mCurProgram.mProviderID);
				mOnChangeChannelBroadcast.onChangeChannel(mIntent);// 给第三方发消息包含节目信息信息
			} else {
				Log.e(TAG, "DtvChannelManager change channel failed :" + mCurProgram.mProgramNum);
			}
		}
	};

	/********************************************** YangLiu *********************************************/
	/**
	 * 检测当前时间与节目播放结束时间 当节目播放结束时，推荐网络关联 added by YangLiu 2015-05-05
	 */
	private void checkEventTime() {
		if (mHandler != null) {
			mHandler.removeCallbacks(checkCurEventTimeRunnable);
			mHandler.postDelayed(checkCurEventTimeRunnable, 10 * 1000);
			Log.i(YL, "\n\n\ndelay 10s and start checkCurEventTimeRunnable");
		} else {
			Log.e(YL, "checkEventTime, but mHandler is null!");
		}
	}

	private Runnable checkCurEventTimeRunnable = new Runnable() {
		@Override
		public void run() {
			// ...IsReportDtvInfo判断当前是否在DtvApk之下...//
			if (IsReportDtvInfo()) {
				if (mCurProgram != null) {
					Log.i(YL, "mCurProgram num=" + mCurProgram.getProgramNum() + "\nmCurProgram name=" + mCurProgram.getProgramName());

					long curTimeL = System.currentTimeMillis();
					long curEventTimeL = 0L;
					DtvEvent[] PFinfos = DtvEpgManager.getInstance().getPFInfo(mCurProgram.mServiceIndex);
					if (PFinfos[0] != null) {
						curEventName = PFinfos[0].getTitle();
						Log.i(YL, "当前节目名称：" + PFinfos[0].getTitle());
						// Log.i(YL, "节目开始时间为：" +
						// PFinfos[0].getStartTime()+"		|	"+PFinfos[0].getStartTime().getTime());
						Log.i(YL, "节目结束时间为：" + PFinfos[0].getEndTime() + "		|	" + PFinfos[0].getEndTime().getTime());
						Log.i(YL, "节目当前时间为：" + new Date(curTimeL) + "		|	" + curTimeL);
						curEventTimeL = PFinfos[0].getEndTime().getTime();
					} else {
						Log.i(YL, "curProgram's PF info is null, start checkEventTime again");
					}
					if (curTimeL / (60 * 1000) >= (curEventTimeL / (60 * 1000) - 1) && curEventTimeL > 0L) {// 在节目结束前1分钟取海报数据
						Log.i(YL, "当前节目已经播放完成，点击弹出框播放网络关联");
						// 1.节目播放结束前，查询有没有网络关联
						getLinkOnlineProgram();

						if (curTimeL / (60 * 1000) >= curEventTimeL / (60 * 1000) && curEventTimeL > 0L) {// 在节目结束时显示提示框
							// 2.节目播放结束，显示节目结束提示信息
							if (!MainMenuRootData.isVSettingShowing && targetApkName != null && targetAction != null) {
								showProgramPlayedEndHintDialog();
							}
						}
					}
				} else {
					Log.e(YL, "checkCurEventTimeRunnable, but mCurProgram is null!");
				}
				// 这一次比较完成后准备下一次的时间比较
				checkEventTime();
			} else {
				Log.e(YL, "cur source is Not at DtvApk");
			}
		}
	};

	/**
	 * 显示提示节目播放结束弹出框
	 */
	private void showProgramPlayedEndHintDialog() {
		Log.i(YL, "\nstart showEventDoneHiteDialog, commonClickDialog is " + commonClickDialog);
		if (commonClickDialog == null) {
			Log.i(YL, "commonClickDialog is null, creat new one to show");
			commonClickDialog = new CommonClickDialog(DtvRoot.mContext);
			commonClickDialog.setCancelable(false);
			commonClickDialog.setShowTV(false);
			commonClickDialog.setMessageText("当前节目已经播放完，点击查看相关网络视频");
			commonClickDialog.setDuration(10);
			commonClickDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent keyEvent) {
					// TODO Auto-generated method stub
					Log.i(YL, "\n\nStep 7, start play onlineLink resource");

					Log.i(YL, "    commonClickDialog onKey——>keyCode=" + keyCode);
					if (KeyEvent.ACTION_DOWN == keyEvent.getAction() && (KeyEvent.KEYCODE_ENTER == keyCode || KeyEvent.KEYCODE_DPAD_CENTER == keyCode)) {

						// 点击播放网络点播
						try {
							Log.i(YL, "    ready play's targetApkName=" + targetApkName + "\n    ready play's targetAction=" + targetAction);
							Log.i(YL, "    mMapList.get(0).name=" + mMapList.get(0).get("name") + "		mMapList.get(0).value" + mMapList.get(0).get("value"));
							Log.i(YL, "    mMapList.get(1).name=" + mMapList.get(1).get("name") + "		mMapList.get(1).value" + mMapList.get(1).get("value"));

							if (targetApkName != null && targetAction != null) {
								Intent intent = new Intent();// 9.从云部取得数据后，点击播放服务器存在的关联节目
								ComponentName componeName = new ComponentName(targetApkName, targetAction);

								/*intent.putExtra(mSpecialContentParmList.get(0).name, mSpecialContentParmList.get(0).value);
								intent.putExtra(mSpecialContentParmList.get(1).name, mSpecialContentParmList.get(1).value);*/

								intent.putExtra(mMapList.get(0).get("name"), mMapList.get(0).get("value"));
								intent.putExtra(mMapList.get(1).get("name"), mMapList.get(1).get("value"));
								intent.setComponent(componeName);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								DtvRoot.mContext.startActivity(intent);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					return false;
				}
			});
		} else {
			Log.i(YL, "commonClickDialog is not null, ready to show");
		}
		commonClickDialog.show();
	}

	/**
	 * 查询网络是否存在本地有关的视频
	 */
	private void getLinkOnlineProgram() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(YL, "before get onlineLink Data, reset constants");
				// 解决从TVMall取不到数据或者没有执行TVMall时，这些值会保留上次状态，导致跳转到上次关联 2015-5-8 YangLiu
				mMapList = new ArrayList<Map<String, String>>();
				targetAction = null;
				targetApkName = null;

				Log.d(YL, "\nstart get curProgram's onlineLink data from huan and changhong cloud");
				// step 1 get now program
				Log.d(YL, "Step 1, creat curProgram's  post parameter");
				String param = creatSpecialPrograme();// 1.根据当前节目向欢网请求参数

				// step 2 get program wiki form huan
				Log.d(YL, "Step 2, get curProgram's wikiDatas form huan");
				if (param == null)
					return;
				String wikidata = getSpecialProgrameWikiData(param);// 2.根据请求参数取得的返回数据

				JSONObject object = null;
				try {
					String errorCode;
					object = new JSONObject(wikidata);

					// step 3 get the program info
					Log.d(YL, "Step 3, get curProgram's wikiInfo from wikiDatas");
					errorCode = getSpecalResultData(object);// 3.解析返回数据，并刷新海报的显示
					Log.d("errorCode", "getLinkOnlineProgram--->errorCode=" + errorCode);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 根据当前节目创建欢网请求参数
	 * @return
	 */
	private String creatSpecialPrograme() {
		String postParame = null;
		JSONObject object = new JSONObject();
		try {
			object.put("action", "GetProgramByChannels");
			JSONObject developer = new JSONObject();
			developer.put("apikey", "HZP9DZMA");
			developer.put("secretkey", "197d2dc3226786eb2377995f8c58e1df");
			object.put("developer", developer);

			JSONObject device = new JSONObject();
			device.put("dnum", "123456");
			object.put("device", device);

			JSONObject user = new JSONObject();
			user.put("userid", "123456");
			object.put("user", user);

			JSONObject param = new JSONObject();
			param.put("cover_type", "big");
			// param.put("showprogram", "yes");

			JSONArray channelcodes = new JSONArray();

			DtvChannelManager mDtvChannelManager = DtvChannelManager.getInstance();
			DtvProgram dtvCurProgram = mDtvChannelManager.getCurProgram();
			if (null == dtvCurProgram) {
				return null;
			}
			Log.d(YL, "    the curPrograme serviceIndex is " + dtvCurProgram.getProgramServiceIndex());

			String currnchannel = null;
			if (mBaseChannelList != null && mBaseChannelList.size() > 0) {
				for (BaseChannel baseChannel : mBaseChannelList) {
					if (dtvCurProgram.getProgramServiceIndex() == baseChannel.getIndex()) {
						currnchannel = baseChannel.getCode();
					}
				}
			}
			if (currnchannel == null) {
				return null;
			}
			channelcodes.put(currnchannel);
			Log.d(YL, "    the channel code we are watching is " + channelcodes);
			param.put("channel_codes", channelcodes);
			object.put("param", param);

			postParame = object.toString();
			Log.d(YL, "    the created curPrograme's post parameter is " + object.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return postParame;
	}

	/**
	 * 根据当前节目从欢网取得的与该节目有关的数据
	 * @param param
	 * @return
	 */
	private String getSpecialProgrameWikiData(String param) {
		Log.d(YL, "    getSpecialProgrameWikiDatas from huan");
		HttpPost request = new HttpPost(MagicButtonCommon.HUAN_URL);
		HttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String in = EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.i(YL, "    the return curProgram's wikiInfos is  " + in);
				return in;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析从欢网根据当前节目取得的数据，数据格式为programs的json字符数组
	 * @param object
	 * @return
	 */
	private String getSpecalResultData(JSONObject object) {
		final LiveHotWiki mWiki = new LiveHotWiki();
		JSONObject error = null;
		String errorCode = null;
		try {
			error = object.optJSONObject("error");
			errorCode = error.optString("code");
			Log.i("errorCode", errorCode);
			String errorMsg = error.optString("info");
			Log.i("errorMsg", errorMsg);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if ("0".equals(errorCode)) {
			LiveHotProgram mProgram = null;
			JSONArray wikis = object.optJSONArray("programs");
			for (int i = 0; i < wikis.length(); i++) {
				JSONObject wiki = (JSONObject) wikis.opt(i);
				/** 添加取得数据的匹配，若取得节目和当前节目不一致，则不向TVmall取数据 2015-5-14 YangLiu */
				String name = wiki.optString("name");
				name = name.trim().toLowerCase();
				curEventName = curEventName.trim().toLowerCase();
				if (curEventName.length() >= 2) {
					subEventName = curEventName.substring(0, 2);
				} else {
					subEventName = curEventName;
				}
				Log.i(YL, "subEventName=" + subEventName + "	isContanins=" + isContanins(name, subEventName));
				if (!isContanins(name, subEventName)) {
					return errorCode;
				}

				String wiki_id = wiki.optString("wiki_id");
				String model = wiki.optString("model");
				String wiki_title = wiki.optString("wiki_title");
				String wiki_cover = wiki.optString("wiki_cover");
				String wiki_content = wiki.optString("wiki_content");
				String hot = wiki.optString("hot");

				JSONArray wiki_director = wiki.optJSONArray("wiki_director");
				JSONArray wiki_starring = wiki.optJSONArray("wiki_starring");
				JSONArray wiki_host = wiki.optJSONArray("wiki_host");
				JSONArray wiki_guest = wiki.optJSONArray("wiki_guest");
				JSONArray tags = wiki.optJSONArray("tags");

				mWiki.setWikiId(wiki_id);
				mWiki.setModel(model);
				mWiki.setWikiTitle(wiki_title);
				mWiki.setWikiCover(wiki_cover);
				mWiki.setWikiContent(wiki_content);
				mWiki.setHot(hot);
				mWiki.setWikiDirector(wiki_director);
				mWiki.setWikiStarring(wiki_starring);
				mWiki.setWikiHost(wiki_host);
				mWiki.setWikiGuest(wiki_guest);
				mWiki.setWikiTags(tags);
				try {
					Log.i("wiki_id", wiki_id);
					Log.i("model", model);
					Log.i("wiki_title", wiki_title);
					Log.i("wiki_cover", wiki_cover);
					Log.i("wiki_content", wiki_content);
					Log.i("hot", hot);
					if (wiki_director != null)
						Log.i("wiki_director", wiki_director.toString());
					if (wiki_starring != null)
						Log.i("wiki_starring", wiki_starring.toString());
					if (wiki_host != null)
						Log.i("wiki_host", wiki_host.toString());
					if (wiki_guest != null)
						Log.i("wiki_guest", wiki_guest.toString());
					if (tags != null)
						Log.i("tags", tags.toString());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d(YL, "Step 4, creat curProgram_huan's post parameter to changhong cloud");
					String contentprama = creatSpecialProgrameforcontent(mWiki);// 5.创建向云部请求的参数
					Log.d(YL, "Step 5, get curProgram_huan's wikiDatas from changhong cloud");
					getContentData(contentprama);// 6.得到云部返回的请求结果
				}
			}).start();
		}
		return errorCode;
	}

	/**
	 * 根据欢网返回的节目数据 创建向云部请求参数
	 * @param mlivewiki
	 * @return
	 */
	private String creatSpecialProgrameforcontent(LiveHotWiki mlivewiki) {
		String postParame = null;
		JSONObject object = new JSONObject();
		try {
			JSONObject client = new JSONObject();
			client.put("agent_name", "aabbcc");
			client.put("agent_ver", "1.0.327");
			client.put("device", "TV");
			object.put("client", client);

			JSONArray programname = new JSONArray();
			JSONObject name = new JSONObject();
			name.put("name", mlivewiki.getWikiTitle());
			if (mlivewiki.getModel().endsWith("film") || mlivewiki.getModel().endsWith("teleplay")) {
				Log.d(YL, "    the type of now program is " + mlivewiki.getModel());
				if (!"".equals(mlivewiki.getModel()))
					name.put("type", mlivewiki.getModel());
				if (!"".equals(mlivewiki.getWikiDirector()))
					name.put("director", mlivewiki.getWikiDirector());
				if (!"".equals(mlivewiki.getWikiStarring())) {
					// put star of the tv to changhong cloud but now it is not
					// added
					name.put("cast", mlivewiki.getWikiStarring());
				}
			} else {// television 综艺 teleplay 电视剧
				Log.d(YL, "    the type of curProgram_huan is " + mlivewiki.getModel() + " send host");
				name.put("type", mlivewiki.getModel());
				if (mlivewiki.getWikiHost() != null) {
					name.put("host", mlivewiki.getWikiHost());
				}
			}
			programname.put(name);
			JSONObject dibingList = new JSONObject();
			dibingList.put("dibingList", programname);
			object.put("searchWord", dibingList.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		postParame = object.toString();
		Log.d(YL, "    the content of curProgram_huan send to changhong cloud is" + postParame);
		return postParame;
	}

	/**
	 * 得到云部返回的结果
	 * @param param
	 * @return
	 */
	private String getContentData(String param) {
		// String url ="http://182.140.244.133:8080/cloud/services?appKey=p8q4tr&method=ch.tvmall.resource.dibbing&v=3&format=json";
		String url = "http://cloud.smart-tv.cn/cloud/services?appKey=p8q4tr&method=ch.tvmall.resource.dibbing&v=3&format=json";
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String errorCode = null;
		try {
			StringEntity entity = new StringEntity(param.toString(), "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			request.setEntity(entity);
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String in = EntityUtils.toString(response.getEntity(), "GB2312");
				Log.d(YL, "    get curProgram_huan's changhong cloud result in=" + in);
				JSONObject object = new JSONObject(in);
				Log.d(YL, "Step 6, get curProgram's onlineLink data from cloud wikiDatas results");
				errorCode = getSpecialContentResultData(object);// 7.解析云部返回结果信息，得到关联的节目
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	/**
	 * 解析云部返回结果信息，得到关联的节目
	 * @param object
	 * @return
	 */
	private String getSpecialContentResultData(JSONObject object) {
		String errorCode = null;
		try {
			if ("TVMALL_DIBBING".equals(object.optString("serviceName"))) {
				JSONArray content = object.optJSONArray("content");
				JSONObject player = content.optJSONObject(0).optJSONObject("player");

				targetAction = player.optString("activity");
				targetApkName = player.optString("apk");

				JSONArray parameters = player.optJSONArray("parameters");
				for (int i = 0; i < parameters.length(); i++) {
					JSONObject p = (JSONObject) parameters.get(i);
					Log.d(YL, "    tvmall's name = " + p.optString("name"));
					Log.d(YL, "    tvmall's value = " + p.optString("value"));

					Map<String, String> map = new HashMap<String, String>();
					map.put("name", p.optString("name"));
					map.put("value", p.optString("value"));
					mMapList.add(map);
					map = null;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			Log.d(YL, "    the targetAction is " + targetAction);
			Log.d(YL, "    the targetApkName is " + targetApkName);
			Log.d(YL, "    the name is " + mMapList.get(0).get("name"));
			Log.d(YL, "    the value is " + mMapList.get(0).get("value"));
			Log.d(YL, "    the name is " + mMapList.get(1).get("name"));
			Log.d(YL, "    the value is " + mMapList.get(1).get("value") + "\n\n\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	/**
	 * 查询string里是否包含subString的每个字符
	 * @param string  所查询的字符串
	 * @param subString 查询的字符串
	 * @return 返回包含结果
	 */
	private boolean isContanins(String string, String subString) {
		boolean isContains = false;
		for (int i = 0; i < subString.length(); i++) {
			if (string.contains(subString.charAt(i) + "")) {
				isContains = true;
			} else {
				isContains = false;
				break;
			}
		}
		return isContains;
	}

	/*******************************************数据上报************************************/
	/**
	 * 数据上报线程，换台进行数据上报
	 */
	public void startPosProInfoThread() {
		Log.i(REPORT, "start posProInfoThread");
		channelID = DtvRoot.mContext.getResources().getString(R.string.huanWang);
		programID = "null";

		if (posProInfoThread == null) {
			posProInfoThread = new Thread("posProInfoThread") {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					ReportHander = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							synchronized (mCurProgram) {
								if (msg.what == ChangePorgrama) {
									reportDtvInfo();//每60s进行数据上报
								}
							}
						}
					};
					Looper.loop();
				}
			};
			posProInfoThread.start();
		} else {
			Log.i(REPORT, "posProInfoThread isn't null, it's already started");
		}
	}

	/** 控制每隔60s重新进行数据上报*/
	private void reportDtvInfo() {
		if (ReportHander != null) {
			ReportHander.removeCallbacks(reportCurDtvInfo);
			ReportHander.postDelayed(reportCurDtvInfo, PortPeriod * 1000);

			if (netWorkTest(DtvRoot.mContext)) {
				if (!PortEnable) {//获取请求结果
					startRequestThread();
				}
				if (!isHasGetBaseChannelList) {//获取别名库
					getBaseChannels(DtvRoot.mContext);
				}
				if (!isGetAddr) {//获取地理位置信息
					startGetAddr(DtvRoot.mContext);
				}
			} else {
				Log.e(TAG, "reportCurDtvInfo, but netWorkTest is not ok!");
			}
		} else {
			Log.e(TAG, "reportCurDtvInfo, but ReportHander is null!");
		}
	}

	/** 数据上报Runnable*/
	private Runnable reportCurDtvInfo = new Runnable() {
		public void run() {
			// ...IsReportDtvInfo判断当前是否在dtvapk之下...//
			if (IsReportDtvInfo()) {
				// ...PortEnable 判断是否允许数据上报...//
				if (PortEnable) {
					if (mCurProgram != null) {
						DtvEvent[] PFinfo = mEpgManager.getPFInfo(mCurProgram.mServiceIndex);
						if ((PFinfo[0] != null) && (PFinfo[0].getTitle() != null)) {
							channel = mCurProgram.mProgramName;
							program = PFinfo[0].getTitle().trim();
						} else {
							channel = mCurProgram.mProgramName;
							program = "null";
						}
						DTVVersion = DtvSoftWareInfoManager.mSoftwareVersion;
						subClass = DtvChannelManager.getInstance().getSubClass();
						mac = ReadMac();
						channelID = getChannelCodeByName(mCurProgram.mProgramName);

						Log.i(REPORT, "srcFro=" + srcFro);
						Log.i(REPORT, "DTVVersion=" + DTVVersion);
						Log.i(REPORT, "subClass=" + subClass);
						Log.i(REPORT, "mac=" + mac);
						Log.i(REPORT, "viewSource=" + viewSource);

						Log.i(REPORT, "channel=" + channel);
						Log.i(REPORT, "program=" + program);
						Log.i(REPORT, "channelID=" + channelID);
						Log.i(REPORT, "programID=" + programID);
						Log.i(REPORT, "status=" + status);

						// Port DtvPrograma Info
						JSONObject objectreport = new JSONObject();
						try {
							objectreport.put("srcFro", srcFro);
							objectreport.put("reportType", "DTV_RATING");
							objectreport.put("DTVVersion", DTVVersion);
							objectreport.put("subClass", subClass);
							objectreport.put("mac", mac);
							objectreport.put("viewSource", viewSource);

							JSONObject reportinfo = new JSONObject();
							reportinfo.put("channel", channel);
							reportinfo.put("program", program);
							reportinfo.put("channelID", channelID);
							reportinfo.put("programID", programID);
							reportinfo.put("status", status);
							objectreport.put("reportinfo", reportinfo);

							JSONObject wifiLocate = new JSONObject();
							wifiLocate.put("province", province);
							wifiLocate.put("city", city);
							wifiLocate.put("district", district);
							wifiLocate.put("street", street);
							objectreport.put("wifiLocate", wifiLocate);

							JSONArray jArray = new JSONArray();
							jArray.put(objectreport);
							postProInfoData = jArray.toString();
							Log.i(REPORT, "jarray=" + jArray.toString());

							// ...进行httpPost上报...//
							startPostThread();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Log.i(REPORT, "report curProgramInfo filed, because mCurProgram is null");
					}
				}
				Log.i(REPORT, "report again in 60s later");
				reportDtvInfo();
			} else {
				Log.i(REPORT, "report curProgramInfo filed, because current is not at DTV");
			}
		}
	};

	/** 数据上报线程*/
	private void startPostThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String returnValue = DtvPortInfoGetANDEncryptionPost.EncryptedPOST(postProInfoData, PortUrl);
				String succreturn = "[0, \"OK\"]";
				boolean cmp = succreturn.equals(returnValue);
				// cmp返回值
				if (cmp) {
					Log.i(REPORT, "judgement: Port succ!!!!!");
				} else {
					Log.i(REPORT, "judgement: Port not succ!!!!!");
				}
			}
		}).start();
	}

	/** 上报请求线程*/
	private synchronized static void startRequestThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String urlStr = "http://ss.chiq-cloud.com:13000/api/v1/dtvConfig"; // 正式
				//	urlStr = "http://10.3.30.18:13000/api/v1/dtvConfig"; // 测试
				String getstr = null;
				DtvPortInfoGetANDEncryptionPost getpost = new DtvPortInfoGetANDEncryptionPost();
				getstr = getpost.getURLContent(urlStr);
				if (null != getstr) {
					JSONObject js;
					try {
						js = new JSONObject(getstr);
						PortEnable = js.getBoolean("enable");
						PortUrl = js.getString("postUrl");
						PortPeriod = js.getLong("period");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Log.i(REPORT, "lyw-true-get getstr err!");
				}
			}
		}).start();
	}

	/** 获取地理位置信息*/
	private static String startGetAddr(Context context) {
		// String Addr = "四川-绵阳-高新区-绵兴东路8号";
		String Addr = Settings.Secure.getString(context.getContentResolver(), "com.changhong.cityphone");
		//	Log.i(REPORT, "getAddr " + Addr);
		if (Addr != null) {
			String temp_String = Addr;
			province = temp_String.substring(0, temp_String.indexOf("-"));
			//	Log.i(REPORT, " province111 " + province);
			temp_String = temp_String.substring(temp_String.indexOf("-") + 1, temp_String.length());
			city = temp_String.substring(0, temp_String.indexOf("-"));
			//	Log.i(REPORT, " city2222 " + city);
			temp_String = temp_String.substring(temp_String.indexOf("-") + 1, temp_String.length());
			district = temp_String.substring(0, temp_String.indexOf("-"));
			//	Log.i(REPORT, " district3333 " + district);
			temp_String = temp_String.substring(temp_String.indexOf("-") + 1, temp_String.length());
			street = temp_String.substring(0, temp_String.length());
			//	Log.i(REPORT, " street4444 " + street);
			isGetAddr = true;
		}
		return Addr;
	}

	/** 根据name取得code值*/
	private String getChannelCodeByName(String name) {
		String channelCode = "null";
		Log.i(REPORT, "getChannelCodeByName——>name=" + name);
		if (mBaseChannelList != null && mBaseChannelList.size() > 0) {
			for (int i = 0; i < mBaseChannelList.size(); i++) {
				if (name.equals(mBaseChannelList.get(i).getName())) {
					channelCode = mBaseChannelList.get(i).getCode();
					Log.i(REPORT, "getChannelCodeByName——>code=" + channelCode);
					break;
				}
			}
		} else {
			channelCode = "erro";
		}
		return channelCode;
	}

	/** 监听网络广播，当有网时进行数据请求*/
	public static class httpBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.i(REPORT, "网络状态已经改变=" + netWorkTest(context));
				if (netWorkTest(context)) {
					if (!PortEnable) {//获取请求结果
						startRequestThread();
					}
					if (!isHasGetBaseChannelList) {//获取别名库
						getBaseChannels(context);
					}
					if (!isGetAddr) {//获取地理位置信息
						startGetAddr(context);
					}
				}
			}
		};
	}

	public static boolean netWorkTest(Context context) {
		cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		nInfo = cManager.getActiveNetworkInfo();
		if (nInfo != null && nInfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	/**************************************更新别名库********************************/
	/**
	 * Service别名库生成成功广播
	 * @author YangLiu
	 */
	public static class aliasNameDoneReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Log.i(TAG, "Receive AliasName Created Done Broadcat");
			if (BaseChannelUtil.AliasName_CreatedDone_BROADCAST.equals(action)) {
				Log.i(TAG, "别名库生成成功，重新读取别名库");
				getBaseChannels(context); //1：重新刷新别名库

				if (DtvRoot.is5508Q2) {
					Log.i(TAG, "Q2版本需要刷新EPG");
					refreshEPG(); //2：重新刷新EPG
				}
			}
		}
	}

	/**
	 * 1：重新刷新读取别名库
	 * @param context
	 */
	public static void getBaseChannels(Context context) {
		Log.i(TAG, "start getBaseChannels");
		mBaseChannelList = new ArrayList<BaseChannel>();
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.changhong.tvos.dtv.basechannelprovider/channel");
		Cursor cursor = contentResolver.query(uri, new String[] { "name", "[index]", "type", "code" }, null, null, null);
		BaseChannel mBaseChannel = null;
		while (cursor.moveToNext()) {
			mBaseChannel = new BaseChannel();
			mBaseChannel.setName(cursor.getString(0));
			mBaseChannel.setIndex(cursor.getInt(1));
			mBaseChannel.setType(cursor.getString(2));
			mBaseChannel.setCode(cursor.getString(3));
			mBaseChannelList.add(mBaseChannel);
			//	Log.i(TAG, "name  " + cursor.getString(0));
			//	Log.i(TAG, "index  " + cursor.getInt(1));
			//	Log.i(TAG, "type  " + cursor.getString(2));
			//	Log.i(TAG, "code  " + cursor.getString(3));
			// Log.i(TAG, "type  " + cursor.getString(4));
			// Log.i(TAG, "loaclinfo  " + cursor.getString(5));
			mBaseChannel = null;
		}
		cursor.close();

		if (mBaseChannelList != null && mBaseChannelList.size() > 0) {
			isHasGetBaseChannelList = true;
		}
	}

	/**
	 * 2：重新刷新EPG显示数据
	 */
	public static void refreshEPG() {
		// 更新全部节目列表
		ChannelListFragment.getChannelList();
		Log.i(YL, "频道列表改变后的长度为：" + ChannelListFragment.mChannelList.size());

		// 更新常用节目列表
		ChannelListFragment.getCommonList();
		Log.i(YL, "常用列表改变后的长度为：" + ChannelListFragment.mCommonList.size());

		// 更新频道类别列表
		ChannelListFragment.getChannelTypeList();
		Log.i(YL, "频道类别改变后的长度为：" + ChannelListFragment.mTypes.length);

		// 更新所有类别的频道列表
		ChannelListFragment.getAllTypeChannelList();
	}

	/**************************************微信电视广播**********************************/
	/**
	 * 给微信服务器发送广播	2015-6-3	YangLiu
	 * @param context 上下文
	 * @param state	发送的状态，有：onStart，onResume，onStop，DtvChChanged
	 */
	public void sendWeChatTVBroadcast(Context context, String state) {
		int curServiceIndex = getCurServiceIndexInnerClassUsed();
		Log.i(TAG, "state=" + state + "\ncurServiceIndex=" + curServiceIndex + "\nmCurProgramServiceIndex=" + mCurProgramServiceIndex);

		String channel_broadcastStr = null;
		if (context != null) {
			channel_broadcastStr = Settings.System.getString(context.getContentResolver(), "channel_broadcast");
		}
		if (channel_broadcastStr != null && ("1".equals(channel_broadcastStr))) {
			Intent mIntent_WeChatTV_resume = new Intent(DTVChannelAction);
			mIntent_WeChatTV_resume.putExtra("state", state);
			mIntent_WeChatTV_resume.putExtra("curServiceIndex", curServiceIndex);
			context.sendBroadcast(mIntent_WeChatTV_resume);
		}
	};
}