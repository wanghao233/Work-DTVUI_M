package com.changhong.menudata.menuPageData;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.changhong.data.pageData.ListPageData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.common.ThreeDimensionManager;
import com.changhong.tvos.common.exception.IllegalValueException;
import com.changhong.tvos.common.exception.TVCommonException;
import com.changhong.tvos.common.exception.TVManagerNotInitException;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.menuManager.MenuDisplayManager;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.scan.MenuFilter;
import com.changhong.tvos.dtv.scan.MenuScan;
import com.changhong.tvos.dtv.scan.ScanManager;
import com.changhong.tvos.dtv.scan.ScanManager.scantype;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvCicaManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSoftWareInfoManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstProductType;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstScanParams;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstStringKey;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvVersion;
import com.changhong.tvos.dtv.userMenu.MenuSearchGuide;
import com.changhong.tvos.dtv.userMenu.MenuSortChannel;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardStatus;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardType;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.model.ChOsType.ENUM3DMode;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;
import com.changhong.tvos.system.commondialog.CommonProgressInfoDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;
import com.changhong.tvos.system.commondialog.VchCommonToastDialog;
import com.vwidget.ui.VListView;
import com.vwidget.ui.VListViewItem;
import com.vwidget.ui.VListViewItemBase.OnItemViewClickListener;
import com.vwidget.ui.VListViewItemBase.OnItemViewOnKeyListener;
import com.vwidget.ui.VListViewItemBase.OnPulldownListener;
import com.vwidget.ui.VSetting;
import com.vwidget.ui.VSettingBase.OnDestroyListener;
import com.vwidget.ui.VSettingBase.OnSetfocuseListener;
import com.vwidget.ui.VSettingItem;
import com.vwidget.vinterface.ThemeModeSet;
import com.vwidget.vinterface.VRadio;
import com.vwidget.vinterface.VRadioGroup;
import com.vwidget.vinterface.VRadioGroup.OnVRadioGroupListener;
import com.vwidget.vinterface.VSeekbar;
import com.vwidget.wheel.DTVProvider;
import com.vwidget.wheel.DTVProviderList;
import com.vwidget.wheel.DTVProviderSelector;
import com.vwidget.wheel.OnWheelOKListener;
import com.vwidget.wheel.WheelView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainMenuRootData extends ListPageData {

	protected static final String TAG = MainMenuRootData.class.getSimpleName();
	static Context mContext;

	// added by cuixy
	private boolean isDirectSearch = false;
	private Button btnAutoSearchYes;
	private Button btnAutoSearchNo;
	private Button btnAutoSearchCancle;
	private Button btnListSearchYes, btnListSearchNo, btnListSearchCancle;
	private Button btnUserSearchYes, btnUserSearchNo, btnUserSearchCancle;
	private Button btnUserDmbtSearchYes, btnUserDmbtSearchNo, btnUserDmbtSearchCancle;
	private Button btnManualSearchYes, btnManualSearchNo, btnManualSearchCancle;
	private Button btnManualDmbtSearchYes, btnManualDmbtSearchNo, btnManualDmbtSearchCancle;
	String[] mFrequencyStr;
	int[] mFrequencyTable;
	String[] mSymbolRateStr;
	String[] mQamStr;
	String[] mDmbtFrequencyStr;
	int[] mFrequencyDtmbTable;
	String[] mBandWidthStr;
	int freManualStartValue = 0;
	int freManualEndValue = 0;
	int symManualValue = 0;
	int qamManualValue = 0;
	int low_fre_manual = 0;
	String strSymbolManual = null;
	private static View mAutoSearchView = null;
	private static LinearLayout auto_search_step1;
	private static LinearLayout auto_search_step2;
	private static LinearLayout list_search_step1;
	private static LinearLayout list_search_step2;
	private static LinearLayout user_search_step1;
	private static LinearLayout user_search_step2;
	private static LinearLayout user_dmbt_search_step1;
	private static LinearLayout user_dmbt_search_step2;
	private static LinearLayout guide_search_step1;
	private static LinearLayout guide_search_step2;
	private static LinearLayout manual_search_step1;
	private static LinearLayout manual_search_step2;
	private static LinearLayout manual_dmbt_search_step1;
	private static LinearLayout manual_dmbt_search_step2;
	private List<DTVSource> mSourceList = null;
	private VRadioGroup mgroup = null;
	private VRadioGroup mSignalInputgroup = null;
	private int mLastSourceIndex;
	private int mCurSourceIndex;
	private static int curSourceIndex;
	private LayoutInflater appInfView = null;
	TVManager tvManager = null;
	ThreeDimensionManager threedmanager = null;
	int demodType = ConstDemodType.DVB_C;
	FilterChannels mFilterDialog = null;
	private DtvChannelManager mDtvChannelManager = null;
	private DtvOperatorManager mOperatorManager = null;
	private MenuManager mMenuManager = null;
	DtvSourceManager mSourceManager = null;
	private int demoType;
	int mSourceID = ConstSourceID.DVBC;
	private DtvInterface dtvInterface = null;
	public static boolean isVSettingShowing = false;// by YangLiu
	public static VSetting mMainMenu = null;
	public static boolean isCAFirstShowing = false;// by YangLiu
	ThemeModeSet ScenceMode;

	VSettingItem mProgramManage; // 节目管理
	VListView mProgramManageView = null;
	VSettingItem mScanManage;// 搜索管理
	VListView mScanManageView = null;
	VSettingItem mMoreSetting;// 更多设置
	VListView mMoreSettingView = null;

	// ////////////////mProgramManage///////////////
	VListViewItem itemManualSort;
	VListViewItem itemChannelInfo;
	static VListViewItem itemProgFilter;
	VListViewItem itemAllProgSwitch;
	VListViewItem itemDtvRadio;
	private static View mProgFilterView = null;
	private static LinearLayout prog_filter_step1;
	private static LinearLayout prog_filter_step2;
	private static Button btnProgFilterYes;
	private Button btnProgFilterNo;
	private static Button btnProgFilterCancle;
	static ProgressBar mProgFilterProgressBar;
	static TextView mProgFilterParameter1;
	static TextView mProgFilterParameter2;
	private static String strProgramManager_ProgramFilter;
	private static boolean isFiltering = false;
	public static boolean isFilterMenuShow = false;

	// ////////////mScanManage////////////////
	static VListViewItem itemSearchGuide;
	static VListViewItem itemAutoSearch;
	VListViewItem itemSearchAdvance;

	VListViewItem itemMainFreqDvbc;
	VListViewItem itemMainFreqDmbt;
	VListViewItem itemSymbolRate;
	VListViewItem itemQam;
	static VListViewItem mOperatorItem;

	VSettingItem mListSearch;
	static View mListSearchView = null;
	VListView listSearchDvbc = null;
	VListView listSearchDmbt = null;
	static VListViewItem mListSearchItem;

	static VListViewItem mUserSearchItem;
	static View mUserSearchView = null;
	static VListViewItem itemUserSearch;
	static View mUserDmbtSearchView = null;
	static VListViewItem itemUserDmbtSearch;

	VListView manualSearchDvbc = null;
	VListView manualSearchDmbt = null;
	static VListViewItem mManualSearchItem;
	static View mManualSearchView = null;
	static VListViewItem itemManualSearch;
	static View mManualDmbtSearchView = null;
	static VListViewItem itemManualDmbtSearch;
	VListViewItem itemStartFreqDvbc;
	VListViewItem itemEndFreqDvbc;
	VListViewItem itemStartFreqDmbt;
	VListViewItem itemEndFreqDmbt;
	VListViewItem itemManualSearchSymbolRate;
	VListViewItem itemManualSearchQam;
	VSeekbar seekbar11;
	VSeekbar seekbar12;

	VSettingItem mCustomSearch;
	VListView mCustomSearchView = null;

	VSettingItem mDtvProvider;

	VSettingItem mSignalInput;
	VListViewItem mSignalInputItem;
	private View mSignalInputView = null;
	LinearLayout signalInputLayout1;
	LinearLayout signalInputLayout2;
	TextView textView1;
	Button btnSigalInputYes;
	Button btnSigalInputNo;

	boolean bSearchGuideFirst = false;
	VSettingItem mSearchGuide;
	VListView mSearchGuideListView = null;
	int mSearchGuideStepBackFlag = 0;
	VListViewItem mSearchGuideStep1Item;
	VListViewItem mSearchGuideStep2Item;
	VListViewItem mSearchGuideStep3Item;
	static VListViewItem mSearchGuideStep4Item;
	private View mSearchGuideViewStep1 = null;
	private View mSearchGuideViewStep2 = null;
	private View mSearchGuideViewStep3 = null;
	private static View mSearchGuideViewStep4 = null;
	String strScanManager_Guide;
	String strSearchGuideStep1;
	String strSearchGuideStep2;
	String strSearchGuideStep3;
	static String strSearchGuideStep4;
	String strAdvanced_Customer_Symbol;

	private View mSearchGuideView = null;
	LinearLayout searchGuideContent1;
	LinearLayout searchGuideContent2;
	LinearLayout searchGuideContent3;
	Button btnSearchGuideYes;
	Button btnSearchGuideNo;
	Button btnSearchGuideCancel;
	Button btnSearchGuideStep1Next;
	Button btnSearchGuideStep2Next;
	private VRadioGroup mSearchGuideSignalgroup = null;
	TextView curSignalInput;
	TextView curOperatorName;
	static TextView searchGuideTitle;

	VSettingItem mAudioSetting;
	VSettingItem mModuleUpgrade;
	VListViewItem itemAudioSetting;

	VSettingItem mModuleInfo = null;

	VListViewItem itemDTVInfo;
	VListViewItem mCustomSearchItem;

	LinearLayout moduleInfoView = null;

	VListView mChannelManageView = null;

	VListView mAudioSettingView = null;

	VListView listSenior = null;

	// VListViewItem itemSearchGuide;

	VListViewItem item3DMode, itemDTVSetting, itemAllSetting;
	VListViewItem itemAudioMode, itemAudioTrack;
	VListViewItem itemEncryptionInfo;

	static MenuSearchGuide guideMenu;
	static MenuScan menuAutoScan = null;
	ScanManager scanManager;
	static scantype mscanType;
	public static Handler handler;
	static ProgressBar AutoscanProgressBar;
	static TextView resultDTV;
	static TextView resultRadio;

	static String strScanManager_AutoScan;
	static String strAdvanced_Customer_ListScan;
	static String strAdvanced_Customer_UserScan;
	static String strAdvanced_Customer_Scan;
	static String strAdvanced_Customer_ManualScan;
	String strAdvanced_AntennaInput;
	String strmModuleUpgrade;
	String strAudioSetting;
	String strDtvinfo;
	String strScanManager_Advanced_Customer;
	String strAllSetting;
	String strAdvanced_OperatorSetting;

	static ProgressBar mAutoSearchProgressBar;
	static TextView mAutoSearchParameter1;
	static TextView mAutoSearchParameter2;
	static ProgressBar mListSearchProgressBar;
	static TextView mListSearchParameter1;
	static TextView mListSearchParameter2;
	static ProgressBar mUserSearchProgressBar;
	static TextView mUserSearchParameter1;
	static TextView mUserSearchParameter2;
	static ProgressBar mUserDmbtSearchProgressBar;
	static TextView mUserDmbtSearchParameter1;
	static TextView mUserDmbtSearchParameter2;
	static ProgressBar mGuideSearchProgressBar;
	static TextView mGuideSearchParameter1;
	static TextView mGuideSearchParameter2;
	static ProgressBar mManualSearchProgressBar;
	static TextView mManualSearchParameter1;
	static TextView mManualSearchParameter2;
	static ProgressBar mManualDmbtSearchProgressBar;
	static TextView mManualDmbtSearchParameter1;
	static TextView mManualDmbtSearchParameter2;

	static MenuSortChannel menuSelfSortDialog = null;// new MenuSortChannel(mContext);

	private List<DtvOperator> operatorListtmp;

	List<String> mOperatorNameList = null;

	DTVProviderSelector dtvprovider_selector;
	DTVProviderList dtvprovider_list = null;
	static int tmpOperateCode = 0;
	static boolean codeHasChanged = false;
	DTVProviderSelector dtvprovider_selector_search_guide;

	private DtvOperatorManager mDtvOperatorManager;
	private List<DtvOperator> operatorList;

	private Map<String, List<DtvOperator>> data;

	private View mDtvOperatorSelectView = null;
	LinearLayout operatorSelectLayout1;
	LinearLayout operatorSelectLayout2;
	TextView curOperator;
	TextView operatorSelectWarning;
	Button btnOperatorSelectYes;
	Button btnOperatorSelectNo;

	public static boolean isScanning = false;// 2015-2-10 YangLiu change to public
	static boolean guideScan = false;
	boolean isKeyboardProcessed = false;

	// fengy 2014-10-24
	static int KeyboardState = 0;
	final int Keyboard_null = 0;
	final int Keyboard_enter = 1;
	final int Keyboard_left = 2;
	final int Keyboard_back = 3;
	final int Keyboard_nofunc = 4;

	// added by cuixy
	CommonInfoDialog mDialog = null;
	VchCommonToastDialog mToastDialog = null;
	CommonProgressInfoDialog progressDiglog = null;
	private static boolean isHasInitData = false;

	public static int getLineNumber(Exception e) {
		StackTraceElement[] trace = e.getStackTrace();
		if (trace == null || trace.length == 0)
			return -1;
		return trace[0].getLineNumber();
	}

	/*******************************************************
	 * **** 设置菜单事件监听****
	 ******************************************************/
	private static List<MainMenuRootData.OnMenuRemovedListener> mOnMenuRemovedListener = null;

	public interface OnMenuRemovedListener {
		public void onRemoved(boolean removed);
	}

	// 设置菜单事件监听器的销毁方法（一个一个）
	private void onRemoved(boolean removed) {
		if (mOnMenuRemovedListener != null) {
			for (OnMenuRemovedListener listener : mOnMenuRemovedListener) {
				Log.i(TAG, "onRemoved --- removed: " + removed);
				listener.onRemoved(removed);
			}
		}
	}

	// 添加菜单事件监听器（添加一个list）
	public void setOnMenuRemovedListener(OnMenuRemovedListener listener) {
		if (mOnMenuRemovedListener == null) {
			mOnMenuRemovedListener = new ArrayList<MainMenuRootData.OnMenuRemovedListener>();
		}
		mOnMenuRemovedListener.add(listener);
	}

	/**
	 * MainMenuRootData的构造方法以及get方法
	 */
	public MainMenuRootData(String strTitle, int picTitle, Context context) {
		// TODO Auto-generated constructor stub
		super(strTitle, picTitle);
		mContext = context;
		mType = EnumPageType.NarrowListPage;
		// this.initMainMenu();
	}

	///////////////////////////////////fengy 2014-10-24/////////////////////////////
	public int GetKeyboardState(int arg1, KeyEvent arg2, int viewStyle) {
		KeyboardState = Keyboard_null;

		int scankeyCode = arg2.getScanCode();
		int keyAction = arg2.getAction();

		if (((keyAction == KeyEvent.ACTION_DOWN) & (((scankeyCode == 236) && (viewStyle == VListViewItem.Jump_To)) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_RIGHT)))
				|| (scankeyCode == 232)) {
			KeyboardState = Keyboard_enter;
			// isKeyboardProcessed = false;
		} else if ((keyAction == KeyEvent.ACTION_DOWN) & ((scankeyCode == 235) || (arg1 == KeyEvent.KEYCODE_DPAD_LEFT))) {
			KeyboardState = Keyboard_left;
			// isKeyboardProcessed = false;
		} else if ((keyAction == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_BACK)) {
			KeyboardState = Keyboard_back;
			// isKeyboardProcessed = false;
		} else if ((keyAction == KeyEvent.ACTION_DOWN) & ((scankeyCode == 236) && (viewStyle == VListViewItem.pull_down))) {
			KeyboardState = Keyboard_nofunc;
		}
		isKeyboardProcessed = false;
		return KeyboardState;
	}

	public Context getContext() {
		return mContext;
	}

	/*******************************************************
	 * ******************* 初始化菜单************************
	 ******************************************************/
	public void initMainMenu() {
		// TODO Auto-generated method stub
		if (isHasInitData == false) {
			guideScan = false;
			isHasInitData = true;
		}
		if (mMainMenu != null) {
			mMainMenu = null;
		}

		if (mMainMenu == null) {
			Log.i(TAG, "mMainMenu is null and init here 111");
			Log.i(TAG, "mMainMenu is null and init manager");
			mDialog = new CommonInfoDialog(mContext);
			mToastDialog = new VchCommonToastDialog(mContext);
			mDtvChannelManager = DtvChannelManager.getInstance();
			mOperatorManager = DtvOperatorManager.getInstance();
			mSourceManager = DtvSourceManager.getInstance();
			mSourceID = mSourceManager.getCurSourceID();
			dtvInterface = DtvInterface.getInstance();
			mMenuManager = MenuManager.getInstance();
			mDtvOperatorManager = DtvOperatorManager.getInstance();
			operatorList = null;
			operatorList = mDtvOperatorManager.getOperatorList();
			if (appInfView == null) {
				appInfView = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}

			Log.i(TAG, "mMainMenu is null and init String");
			strScanManager_AutoScan = mContext.getResources().getString(R.string.dtv_ScanManager_AutoScan);
			strAdvanced_Customer_ListScan = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_ListScan);
			strAdvanced_Customer_UserScan = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan);
			strAdvanced_Customer_Scan = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_Scan);
			strAdvanced_Customer_ManualScan = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_ManualScan);
			strAdvanced_AntennaInput = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_AntennaInput);
			strmModuleUpgrade = mContext.getResources().getString(R.string.dtv_DTVSetting_DTVUpGrade);
			strAudioSetting = mContext.getResources().getString(R.string.dtv_DTVSetting_AudioSetting);
			strDtvinfo = mContext.getResources().getString(R.string.dtv_DTVSetting_DTV_Info);
			strScanManager_Advanced_Customer = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer);
			strAllSetting = mContext.getResources().getString(R.string.dtv_MoreSetting_AllSetting);
			strAdvanced_OperatorSetting = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_OperatorSetting);
			strScanManager_Guide = mContext.getResources().getString(R.string.dtv_ScanManager_Guide);
			strSearchGuideStep1 = mContext.getResources().getString(R.string.str_vch_search_guide_title_1);
			strSearchGuideStep2 = mContext.getResources().getString(R.string.str_vch_search_guide_title_2);
			strSearchGuideStep3 = mContext.getResources().getString(R.string.str_vch_search_guide_title_3);
			strSearchGuideStep4 = mContext.getResources().getString(R.string.str_vch_search_guide_title_4);
			strAdvanced_Customer_Symbol = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_Symbol);

			Log.i(TAG, "mMainMenu is null and init new space");
			mMainMenu = new VSetting(mContext);
			String strMainmenu_Dtv = mContext.getResources().getString(R.string.dtv_Mainmenu_Dtv);
			mMainMenu.setTitle(strMainmenu_Dtv);// 设置主菜单标题“设置”

			ScenceMode = new ThemeModeSet(mContext, mMainMenu, ThemeModeSet.TV);// 情景模式

			mProgramManage = new VSettingItem(mContext);
			String strMainmenu_ProgramManager = mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager);
			mProgramManage.setTitle(strMainmenu_ProgramManager);

			mScanManage = new VSettingItem(mContext);// NEW
			String strMainmenu_ScanManager = mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager);
			mScanManage.setTitle(strMainmenu_ScanManager);// 搜索管理

			mMoreSetting = new VSettingItem(mContext);
			String strMainmenu_MoreSetting = mContext.getResources().getString(R.string.dtv_Mainmenu_MoreSetting);
			mMoreSetting.setTitle(strMainmenu_MoreSetting);

			// //////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////

			mAudioSetting = new VSettingItem(mContext);
			mAudioSetting.setTitle(strAudioSetting);
			mModuleUpgrade = new VSettingItem(mContext);
			mModuleUpgrade.setTitle(strmModuleUpgrade);
			mSignalInput = new VSettingItem(mContext);
			mSignalInput.setTitle(strAdvanced_AntennaInput);
			mModuleInfo = new VSettingItem(mContext);
			mModuleInfo.setTitle(strDtvinfo);
			mCustomSearch = new VSettingItem(mContext);
			mCustomSearch.setTitle(strScanManager_Advanced_Customer);
			mDtvProvider = new VSettingItem(mContext);
			mDtvProvider.setTitle(strAdvanced_OperatorSetting);
			mSearchGuide = new VSettingItem(mContext);
			mSearchGuide.setTitle(strScanManager_Guide);

			Log.i(TAG, "mMainMenu is null and initProgramManagerView");
			initProgramManagerView();
			mProgramManage.setVListView(mProgramManageView);

			Log.i(TAG, "mMainMenu is null and initDtvProviderData");
			initDtvProviderData();
			dtvprovider_selector = new DTVProviderSelector(mContext, dtvprovider_list);
			dtvprovider_selector_search_guide = new DTVProviderSelector(mContext, dtvprovider_list);

			Log.i(TAG, "mMainMenu is null and initCurDtvProvider");
			initCurDtvProvider();

			Log.i(TAG, "mMainMenu is null and initScanManagerView");
			initScanManagerView();
			mScanManage.setVListView(mChannelManageView);

			Log.i(TAG, "mMainMenu is null and initMoreSettingView");
			initMoreSettingView();
			mMoreSetting.setVListView(mMoreSettingView);

			Log.i(TAG, "mMainMenu is null and initAudioSettingView");
			initAudioSettingView();
			mAudioSetting.setVListView(mAudioSettingView);
			mSignalInput.setView(initSignalInputView());

			Log.i(TAG, "mMainMenu is null and initModuleInfoMenu");
			initModuleInfoMenu();
			mModuleInfo.setView(moduleInfoView);

			Log.i(TAG, "mMainMenu is null and initCustomSearchView");
			initCustomSearchView();
			mCustomSearch.setVListView(mCustomSearchView);
			if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
				mDtvProvider.setView(initmDtvProviderView());
				initSearchGuideView();
				mSearchGuide.setVListView(mSearchGuideListView);
			}

			mMainMenu.addItem(mProgramManage);
			mMainMenu.addItem(mScanManage);// 把节目管理加到控件里�?
			mMainMenu.addItem(mMoreSetting);
			mMainMenu.setBackToLeftWork(true);
			mMainMenu.setOnDestroyListener(new OnDestroyListener() {

				@Override
				public int Destroy() {
					// TODO Auto-generated method stub
					Log.i(TAG, "mMainMenu.setOnDestroyListener 111");
					isVSettingShowing = false;
					onRemoved(true);
					return 0;
				}
			});
		} else {
			Log.i(TAG, "mMainMenuelse 1 is null and init here 33333");
			Log.e(TAG, "pang    mDtvChannelManager is no any channel***");
			/* mDtvChannelManager = DtvChannelManager.getInstance();*pang+* */
			Log.i(TAG, "mMainMenuelse 2 is null and init here 33333");
			initProgramManagerView();
			mProgramManage.setVListView(mProgramManageView);
			itemAutoSearch.refreshTitle(strScanManager_AutoScan);
			itemAutoSearch.refreshSubhead("");
			auto_search_step1.setVisibility(View.GONE);
			auto_search_step2.setVisibility(View.GONE);
			itemAutoSearch.PullDown(false);
			mListSearchItem.refreshTitle(strAdvanced_Customer_ListScan);
			mListSearchItem.refreshSubhead("");
			list_search_step1.setVisibility(View.GONE);
			list_search_step2.setVisibility(View.GONE);
			mListSearchItem.PullDown(false);
			if (ConstProductType.PRODUCT_C != mSourceManager.getProductType()) {
				itemUserDmbtSearch.refreshTitle(strAdvanced_Customer_Scan);
				itemUserDmbtSearch.refreshSubhead("");
				user_dmbt_search_step1.setVisibility(View.GONE);
				user_dmbt_search_step2.setVisibility(View.GONE);
				itemUserDmbtSearch.PullDown(false);
			}
			if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
				itemUserSearch.refreshTitle(strAdvanced_Customer_Scan);
				itemUserSearch.refreshSubhead("");
				user_search_step1.setVisibility(View.GONE);
				user_search_step2.setVisibility(View.GONE);
				itemUserSearch.PullDown(false);
				initScanManagerView();
				mScanManage.setVListView(mChannelManageView);
				initMoreSettingView();
				mMoreSetting.setVListView(mMoreSettingView);
				initAudioSettingView();
				mAudioSetting.setVListView(mAudioSettingView);
				initModuleInfoMenu();
				mModuleInfo.setView(moduleInfoView);
				DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
				if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
					itemEncryptionInfo.setLosefocuse(false);
				} else {
					itemEncryptionInfo.setLosefocuse(true);
				}
			}
			Log.i(TAG, "mMainMenu is already init, just refresh some value here!");
		}
	}

	/*******************************************************
	 * **** 一、情景模式****
	 ******************************************************/
	/*
	 * public void VSettingMenuVisibilityControl(boolean flag){ if(flag ==
	 * true){ this.initMainMenu();//初始化菜单，首先加载情景模式视图
	 * mMainMenu.SetfocuseContent(false); ScenceMode.refresh();//ScenceMode情景模式
	 * mMainMenu.show(); isVSettingShowing = true; onRemoved(false); Log.i(TAG,
	 * "mMainMenu.show 111 ..."); } else{//当没有节目源时提示自动搜索
	 * mMainMenu.setMenukeyUseless(false); if(menuAutoScan != null){
	 * menuAutoScan.Scantermined(); } mMainMenu.destroy(); Log.i(TAG,
	 * "mMainMenu.destroy 222 ..."); } }
	 */
	public void VSettingMenuVisibilityControl(boolean flag) {
		if (flag == true) {
			this.initMainMenu();// 初始化菜单，首先加载情景模式视图
			mMainMenu.SetfocuseContent(false);
			ScenceMode.refresh();// ScenceMode情景模式
			mMainMenu.show();
			isVSettingShowing = true;
			onRemoved(false);
			Log.i(TAG, "mMainMenu.show 111 ...");
		} else {// 当没有节目源时提示自动搜索
			mMainMenu.setMenukeyUseless(false);
			if (menuAutoScan != null) {
				menuAutoScan.Scantermined();
			}
			mMainMenu.destroy();
			Log.i(TAG, "mMainMenu.destroy 222 ...");
		}
	}

	public boolean IsVSettingMenuShowing() {
		return isVSettingShowing;
	}

	public static boolean getGuideScan() {
		return guideScan;
	}

	public static void setGuideScan(boolean bguidescan) {
		guideScan = bguidescan;
	}

	/* 搜索管理——>自动搜索（无节目时直接进入自动搜索）***** */
	public void DirectEnterAutoSearch() {
		if (mMainMenu == null) {
			Log.i(TAG, "mMainMenu is null ...");
			return;
		}
		if (isVSettingShowing) {
			Log.i(TAG, "mMainMenu is showing, destory here ...");
			mMainMenu.destroy();
		}
		// 如何跳转到指定页
		mMainMenu.SetfocuseContent(true);
		mMainMenu.setOnfocuseListener(new OnSetfocuseListener() {

			@Override
			public int setfocuse() {
				// TODO Auto-generated method stub
				// 去掉自动搜索的第一步：是否覆盖原有节目
				auto_search_step1.setVisibility(View.GONE);
				// 显示自动搜索的第二步：正在搜索
				auto_search_step2.setVisibility(View.VISIBLE);
				auto_search_step2.requestFocus();
				itemAutoSearch.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
				btnAutoSearchCancle.setFocusable(true);
				btnAutoSearchCancle.requestFocus();
				return 2;
			}

			@Override
			public boolean getpulldown() {
				// TODO Auto-generated method stub
				// mMainMenu.setMenukeyUseless(true);
				isDirectSearch = true;
				// 设置下拉事件，显示是否覆盖原频道
				itemAutoSearch.PullDown(true);
				mMainMenu.SetfocuseContent(false);
				setGuideScan(false);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mscanType = scantype.DTV_ScanAuto;
						// 显示搜索具体细节
						ShowScanMenuDetail();
						// 开始搜索
						menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
						menuAutoScan.Scanready();
					}
				}, 100);
				return false;
			}
		});
		ScenceMode.refresh();
		mMainMenu.show();
		isVSettingShowing = true;
		onRemoved(false);
	}

	/* 搜索管理——>搜索向导（无节目时自动弹出搜索向导）***** */
	public void showSearchGuideSelectMenu() {
		if (mMainMenu == null) {
			Log.i(TAG, "mMainMenu is null ...");
			return;
		}
		if (isVSettingShowing) {
			Log.i(TAG, "mMainMenu is showing, destory here ...");
			mMainMenu.destroy();
		}
		// 如何跳转到指定页
		mMainMenu.SetfocuseContent(true);
		mMainMenu.setOnfocuseListener(new OnSetfocuseListener() {

			@Override
			public int setfocuse() {
				// TODO Auto-generated method stub
				// 当前信号接入
				curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
				// 当前运营商选择
				curOperatorName.setText(mContext.getResources().getString(R.string.dtv_scan_setup_cur_operator, mDtvOperatorManager.getCurOperator().getOperatorName()));
				// 信号
				mCurSourceIndex = mSourceManager.getCurSourceIndex();
				mSearchGuideSignalgroup.check(mCurSourceIndex);
				mSearchGuideSignalgroup.refreshCheck(mCurSourceIndex);
				return 2;
			}

			@Override
			public boolean getpulldown() {
				// TODO Auto-generated method stub
				// mMainMenu.setMenukeyUseless(true);
				// itemSearchGuide.PullDown(true);
				mMainMenu.backToNextItem(mScanManage, mSearchGuide);
				// 第一步：
				mSearchGuideStep1Item.setLosefocuse(false);
				// 第二步：第一步如果选择无线DMB_TH，则第二步不可用；如果第一步选择有线，设置第二步
				if ((mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH)) {
					mSearchGuideStep2Item.setLosefocuse(true);
				} else {
					mSearchGuideStep2Item.setLosefocuse(false);
				}
				// 第三步：不可�?
				mSearchGuideStep3Item.setLosefocuse(true);
				// 第四步：调出下拉菜单，设置“是”（btnSearchGuideYes）的焦点
				mSearchGuideStep4Item.setLosefocuse(false);
				mSearchGuideStep1Item.PullDown(false);
				mSearchGuideStep2Item.PullDown(false);
				mSearchGuideStep3Item.PullDown(false);
				mSearchGuideStep4Item.PullDown(true);
				btnSearchGuideYes.requestFocus();
				mMainMenu.SetfocuseContent(false);
				return false;
			}
		});
		ScenceMode.refresh();
		mMainMenu.show();
		isVSettingShowing = true;
		onRemoved(false);
	}

	@SuppressWarnings("unused")
	public static void ShowmenuSelfSortDialog(boolean flag) {
		if (false) // dismiss
		{
			if (menuSelfSortDialog.isShowTV()) {
				MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(menuSelfSortDialog);
			}
		} else {
			MenuDisplayManager.getInstance(mContext).registerDialogShowing(menuSelfSortDialog);
		}
		// menuSelfSortDialog.setShowTV(false);
		// menuSelfSortDialog.show();
	}

	public static void dismissSelfSortDialog(boolean flag) {
		if (null != menuSelfSortDialog) {
			ShowmenuSelfSortDialog(flag);
			menuSelfSortDialog.setShowTV(false);
		}
	}

	// 搜索管理——刷新过滤菜单
	public static void RefreshFilterMenu(String strTitle, String strSubTitle, String strParameter1, String strParameter2, int tmpProgress) {
		Log.i(TAG, "RefreshFilterMenu");
		itemProgFilter.refreshTitle(strTitle);
		itemProgFilter.refreshSubhead(strSubTitle);
		mProgFilterProgressBar.setProgress(tmpProgress);
		mProgFilterParameter1.setText(strParameter1);
		mProgFilterParameter2.setText(strParameter2);
		if (tmpProgress == 100) {
			// HideFilterMenuDetail();
			HideFilterMenu();// 2015-4-20
		}
	}

	// 过滤节目细节——隐藏节目过滤细节
	public static void HideFilterMenuDetail() {
		Log.i(TAG, "HideFilterMenuDetail");
		if (isFiltering == true) {
			isFiltering = false;
			CommonProgressInfoDialog savingDiglog = new CommonProgressInfoDialog(mContext);
			savingDiglog.getWindow().setType(2003);
			savingDiglog.setDuration(1);
			savingDiglog.setButtonVisible(false);
			savingDiglog.setCancelable(false);
			savingDiglog.setMessage(mContext.getString(R.string.dtv_scan_store_program));
			savingDiglog.show();
		}

		mMainMenu.setMenukeyUseless(false);
		itemProgFilter.PullDown(false);
		itemProgFilter.refreshTitle(strProgramManager_ProgramFilter);
		itemProgFilter.refreshSubhead("");
		itemProgFilter.requestfouse();
		prog_filter_step1.setVisibility(View.VISIBLE);
		prog_filter_step2.setVisibility(View.GONE);
	}

	// 过滤节目细节——显示节目过滤细节
	public static void ShowFilterMenuDetail() {
		Log.i(TAG, "ShowFilterMenuDetail");
		isFiltering = true;
		prog_filter_step1.setVisibility(View.GONE);
		prog_filter_step2.setVisibility(View.VISIBLE);
		itemProgFilter.refreshTitle(mContext.getResources().getString(R.string.dtv_smartskip_programing));
		btnProgFilterCancle.setFocusable(true);
		btnProgFilterCancle.requestFocus();
	}

	// 过滤菜单——搜索结束后显示节目过滤菜单
	public static void ShowFilterMenu() {
		isFilterMenuShow = true;
		isScanning = false;
		if (mscanType == scantype.DTV_ScanAutoExtra_SearchGuide) {// 搜索向导
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanAutoExtra_SearchGuide");
			// setGuideScan(true);
			mSearchGuideStep4Item.refreshTitle(strSearchGuideStep4);
			mSearchGuideStep4Item.removePulldownView();
			mSearchGuideStep4Item.addPullDownItem(mProgFilterView);
			mSearchGuideStep4Item.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		} else if (mscanType == scantype.DTV_ScanAuto) {// 自动搜索
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanAuto");
			itemAutoSearch.refreshTitle(strScanManager_AutoScan);
			itemAutoSearch.removePulldownView();
			itemAutoSearch.addPullDownItem(mProgFilterView);
			itemAutoSearch.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		} else if (mscanType == scantype.DTV_ScanList) {// 高级——>全频段搜索
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanList");
			mListSearchItem.refreshTitle(strAdvanced_Customer_ListScan);
			mListSearchItem.removePulldownView();
			mListSearchItem.addPullDownItem(mProgFilterView);
			mListSearchItem.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		} else if (mscanType == scantype.DTV_ScanMaunal) {// 高级——>手动搜索
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanMaunal");
			itemManualSearch.refreshTitle(strAdvanced_Customer_Scan);
			itemManualSearch.removePulldownView();
			itemManualSearch.addPullDownItem(mProgFilterView);
			itemManualSearch.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		} else if (mscanType == scantype.DTV_ScanAutoExtra) {// 高级——>指定主频点搜索
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanAutoExtra");
			itemUserSearch.refreshTitle(strAdvanced_Customer_Scan);
			itemUserSearch.removePulldownView();
			itemUserSearch.addPullDownItem(mProgFilterView);
			itemUserSearch.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		} else if (mscanType == scantype.DTV_ScanMaunal_Dmbt) {// T下手动搜索
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanMaunal_Dmbt");
			itemManualDmbtSearch.refreshTitle(strAdvanced_Customer_Scan);
			itemManualDmbtSearch.removePulldownView();
			itemManualDmbtSearch.addPullDownItem(mProgFilterView);
			itemManualDmbtSearch.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		} else if (mscanType == scantype.DTV_ScanAutoExtra_Dmbt) {// T下指定主频点搜索
			Log.i(TAG, "ShowFilterMenu, mscanType = scantype.DTV_ScanAutoExtra_Dmbt");
			itemUserDmbtSearch.refreshTitle(strAdvanced_Customer_Scan);
			itemUserDmbtSearch.removePulldownView();
			itemUserDmbtSearch.addPullDownItem(mProgFilterView);
			itemUserDmbtSearch.PullDown(true);
			prog_filter_step1.setVisibility(View.VISIBLE);
			prog_filter_step2.setVisibility(View.GONE);
			btnProgFilterYes.setFocusable(true);
			btnProgFilterYes.requestFocus();
		}
	}

	// 搜索菜单——过滤结束后显示节目搜索菜单
	public static void HideFilterMenu() {
		isFilterMenuShow = false;
		isFiltering = false;
		if (mscanType == scantype.DTV_ScanAutoExtra_SearchGuide) {// 搜索向导
			Log.i(TAG, "searchGuide search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			mSearchGuideStep4Item.removePulldownView();
			mSearchGuideStep4Item.addPullDownItem(mSearchGuideViewStep4);
			mSearchGuideStep4Item.PullDown(false);
			mSearchGuideStep4Item.refreshSubhead("");
			mSearchGuideStep4Item.requestfouse();

			guide_search_step1.setVisibility(View.VISIBLE);
			guide_search_step2.setVisibility(View.GONE);
		} else if (mscanType == scantype.DTV_ScanAuto) {// 自动搜索
			Log.i(TAG, "auto search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			itemAutoSearch.removePulldownView();
			itemAutoSearch.addPullDownItem(mAutoSearchView);
			itemAutoSearch.PullDown(false);
			itemAutoSearch.refreshSubhead("");
			itemAutoSearch.requestfouse();

			auto_search_step1.setVisibility(View.VISIBLE);
			auto_search_step2.setVisibility(View.GONE);
		} else if (mscanType == scantype.DTV_ScanList) {// 高级——>全频段搜索
			Log.i(TAG, "list search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			mListSearchItem.removePulldownView();
			mListSearchItem.addPullDownItem(mListSearchView);
			mListSearchItem.PullDown(false);
			mListSearchItem.refreshSubhead("");
			mListSearchItem.requestfouse();

			list_search_step1.setVisibility(View.VISIBLE);
			list_search_step2.setVisibility(View.GONE);
		} else if (mscanType == scantype.DTV_ScanMaunal) {// 高级——>手动搜索
			Log.i(TAG, "manual search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			itemManualSearch.removePulldownView();
			itemManualSearch.addPullDownItem(mManualSearchView);
			itemManualSearch.PullDown(false);
			itemManualSearch.refreshSubhead("");
			itemManualSearch.requestfouse();

			manual_search_step1.setVisibility(View.VISIBLE);
			manual_search_step2.setVisibility(View.GONE);
		} else if (mscanType == scantype.DTV_ScanAutoExtra) {// 高级——>指定主频点搜索
			Log.i(TAG, "user search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			itemUserSearch.removePulldownView();
			itemUserSearch.addPullDownItem(mUserSearchView);
			itemUserSearch.PullDown(false);
			itemUserSearch.refreshSubhead("");
			itemUserSearch.requestfouse();

			user_search_step1.setVisibility(View.VISIBLE);
			user_search_step2.setVisibility(View.GONE);
		} else if (mscanType == scantype.DTV_ScanMaunal_Dmbt) {// T下手动搜索
			Log.i(TAG, "manual Dmbt search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			itemManualDmbtSearch.removePulldownView();
			itemManualDmbtSearch.addPullDownItem(mManualDmbtSearchView);
			itemManualDmbtSearch.PullDown(false);
			itemManualDmbtSearch.refreshSubhead("");
			itemManualDmbtSearch.requestfouse();

			manual_dmbt_search_step1.setVisibility(View.VISIBLE);
			manual_dmbt_search_step2.setVisibility(View.GONE);
		} else if (mscanType == scantype.DTV_ScanAutoExtra_Dmbt) {// T下指定主频点搜索
			Log.i(TAG, "user Dmbt search HideScanMenu");
			mMainMenu.setMenukeyUseless(false);

			itemUserDmbtSearch.removePulldownView();
			itemUserDmbtSearch.addPullDownItem(mUserDmbtSearchView);
			itemUserDmbtSearch.PullDown(false);
			itemUserDmbtSearch.refreshSubhead("");
			itemUserDmbtSearch.requestfouse();

			user_dmbt_search_step1.setVisibility(View.VISIBLE);
			user_dmbt_search_step2.setVisibility(View.GONE);
		}
	}

	/*******************************************************
	 * **** 二、节目管理****
	 ******************************************************/

	private void initProgramManagerView() {
		// TODO Auto-generated method stub
		mProgramManageView = new VListView(mContext);

		String strProgramManager_ManualSort = mContext.getResources().getString(R.string.dtv_ProgramManager_ManualSort);
		String strProgramManager_ProgramInfo = mContext.getResources().getString(R.string.dtv_ProgramManager_ProgramInfo);
		strProgramManager_ProgramFilter = mContext.getResources().getString(R.string.dtv_ProgramManager_ProgramFilter);
		String strProgramManager_ProgarmAll = mContext.getResources().getString(R.string.dtv_ProgramManager_ProgarmAll);
		String strProgramManager_DtvRadio = mContext.getResources().getString(R.string.dtv_ProgramManager_DtvRadio);

		/** 2.0 节目管理—手动排序 ***********/
		itemManualSort = new VListViewItem(mContext, strProgramManager_ManualSort, null, VListViewItem.Jump_To);
		itemManualSort.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				// /////////////////////////////////fengy
				// 2014-10-24/////////////////////////////

				if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {
					List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
					if (channelList == null || channelList.size() <= 0) {
						Log.e(TAG, "LL there is no any channel###");
						mToastDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
						mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
						mToastDialog.getWindow().setType(2003);
						mToastDialog.show();
						return true;
					}

					// final MenuSortChannel menuSelfSortDialog = new
					// MenuSortChannel(mContext);
					menuSelfSortDialog = new MenuSortChannel(mContext);
					menuSelfSortDialog.getWindow().setType(2003);
					menuSelfSortDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
							if (menuSelfSortDialog.isShowTV()) {
								MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(menuSelfSortDialog);
								// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
								menuSelfSortDialog.setShowTV(false);
							} else {
								// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
							}
						}
					});
					menuSelfSortDialog.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface dialog) {
							// TODO Auto-generated method stub
							MenuDisplayManager.getInstance(mContext).registerDialogShowing(menuSelfSortDialog);
							// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
							menuSelfSortDialog.setShowTV(false);
						}
					});
					menuSelfSortDialog.show();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) {
					mProgramManageView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				} else {
					return false;
				}
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel###");
					mToastDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
					mToastDialog.getWindow().setType(2003);
					mToastDialog.show();
					return true;
				}

				final MenuSortChannel menuSelfSortDialog = new MenuSortChannel(mContext);
				menuSelfSortDialog.getWindow().setType(2003);
				menuSelfSortDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						ShowmenuSelfSortDialog(false);
						// if (menuSelfSortDialog.isShowTV()) {
						// MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(menuSelfSortDialog);
						// }
					}
				});
				menuSelfSortDialog.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface dialog) {
						// TODO Auto-generated method stub
						ShowmenuSelfSortDialog(true);
						// MenuDisplayManager.getInstance(mContext).registerDialogShowing(menuSelfSortDialog);
					}
				});
				menuSelfSortDialog.setShowTV(false);
				menuSelfSortDialog.show();
				return false;
			}
		});
		itemManualSort.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "setOnClickListener .....");
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		/** 2.1 节目管理—节目信息 ***********/
		itemChannelInfo = new VListViewItem(mContext, strProgramManager_ProgramInfo, null, VListViewItem.pull_down);
		itemChannelInfo.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
				Log.d("CH_ER_COLLECT",
						"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
								+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager)
								+ ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_ProgramInfo));
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				View mChannelInfoView = null;
				mChannelInfoView = initChannelInfoMenu(mChannelInfoView);
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel###");
					mToastDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
					mToastDialog.getWindow().setType(2003);
					mToastDialog.show();
					// return null;
				}
				// else{
				return mChannelInfoView;
				// }
			}
		});
		itemChannelInfo.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				// //////////////////////////////FENGY 2014-10 //////
				if (GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_enter) {
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_left) {
					// mProgramManageView.colsepulldown();//这个函数是实现回收动作但下面的函数已经包含回收动作了
					mMainMenu.leftItemfouce();
					/*** 里面已经包含回收动作 **/
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_back) {
					mProgramManageView.colsepulldown();
				}

				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		/** 2.2 节目管理—节目过滤 **************/
		itemProgFilter = new VListViewItem(mContext, strProgramManager_ProgramFilter, null, VListViewItem.Text_pure_pull);
		mProgFilterView = appInfView.inflate(R.layout.prog_skip_warning_dialog, null, false);
		prog_filter_step1 = (LinearLayout) mProgFilterView.findViewById(R.id.warning_yes_no_dialog);
		prog_filter_step2 = (LinearLayout) mProgFilterView.findViewById(R.id.progressbar_cancel_dialog);
		btnProgFilterYes = (Button) prog_filter_step1.findViewById(R.id.yes);
		btnProgFilterNo = (Button) prog_filter_step1.findViewById(R.id.no);
		btnProgFilterCancle = (Button) prog_filter_step2.findViewById(R.id.cancel);
		mProgFilterProgressBar = (ProgressBar) prog_filter_step2.findViewById(R.id.progressBar);
		mProgFilterParameter1 = (TextView) prog_filter_step2.findViewById(R.id.Parameter1);
		mProgFilterParameter2 = (TextView) prog_filter_step2.findViewById(R.id.Parameter2);
		// itemProgFilter.addPullDownItem(mProgFilterView); 2015-4-17

		btnProgFilterYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("CH_ER_COLLECT",
						"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
								+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager)
								+ ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_ProgramFilter));
				mMainMenu.setMenukeyUseless(true);
				ShowFilterMenuDetail();
				MenuFilter.getInstance(mContext).startFilter();
			}
		});
		btnProgFilterYes.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							Log.d("CH_ER_COLLECT",
									"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
											+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
											+ mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager) + ";item2="
											+ mContext.getResources().getString(R.string.dtv_ProgramManager_ProgramFilter));
							mMainMenu.setMenukeyUseless(true);
							ShowFilterMenuDetail();
							MenuFilter.getInstance(mContext).startFilter();
							return true;
						case 235:// keyboard Volume Down
							// itemProgFilter.PullDown(false);
							HideFilterMenu(); // 2015-4-17
							mProgramManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}

					if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						// itemProgFilter.PullDown(false);
						HideFilterMenu(); // 2015-4-17
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						// itemProgFilter.requestfouse();
						itemAutoSearch.requestfouse(); // 2015-4-17
						return true;
					}
				}
				return false;
			}
		});

		btnProgFilterNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "itemProgFilter-->btnProgFilterNo-->setOnClickListener-->HideFilterMenu");
				// HideFilterMenuDetail();
				HideFilterMenu(); // 2015-4-17
			}
		});
		btnProgFilterNo.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							Log.i(TAG, "itemProgFilter-->btnProgFilterNo-->setOnKeyListener-->HideFilterMenu");
							// HideFilterMenuDetail();
							HideFilterMenu(); // 2015-4-17
							return true;
						case 235:// keyboard Volume Down
							// itemProgFilter.PullDown(false);
							HideFilterMenu(); // 2015-4-17
							mProgramManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}

					if ((arg1 == KeyEvent.KEYCODE_DPAD_LEFT) || (arg1 == KeyEvent.KEYCODE_DPAD_DOWN)) {
						// itemProgFilter.PullDown(false);
						HideFilterMenu(); // 2015-4-17
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						// itemProgFilter.requestfouse();
						itemAutoSearch.requestfouse(); // 2015-4-17
						return true;
					}
				}
				return false;
			}
		});

		btnProgFilterCancle.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							Log.i(TAG, "itemProgFilter-->btnProgFilterCancle-->setOnKeyListener-->HideFilterMenu");
							mMainMenu.setMenukeyUseless(false);
							MenuFilter.getInstance(mContext).cancelFilter();
							// HideFilterMenuDetail();
							HideFilterMenu(); // 2015-4-17
							return true;
						case 235:// keyboard Volume Down
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}

					if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
						Log.i(TAG, "itemProgFilter-->btnProgFilterCancle-->setOnKeyListener(enter)-->HideFilterMenu");
						mMainMenu.setMenukeyUseless(false);
						MenuFilter.getInstance(mContext).cancelFilter();
						// HideFilterMenuDetail();
						HideFilterMenu(); // 2015-4-17
						return false;
					}
				}
				return true;
			}
		});

		itemProgFilter.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							itemProgFilter.PullDown(false);// 关闭过滤下拉信息
							mProgramManageView.colsepulldown();// 关闭节目管理下拉信息
							mMainMenu.leftItemfouce();// 主菜单根条目获取焦点
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}

					if ((arg1 == KeyEvent.KEYCODE_DPAD_LEFT) || (arg1 == KeyEvent.KEYCODE_DPAD_UP) || (arg1 == KeyEvent.KEYCODE_BACK)) {
						itemProgFilter.PullDown(false);// 关闭过滤下拉信息
						Log.i(TAG, "Yang    mProgramManageView itemProgSkip= " + itemProgFilter);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		itemProgFilter.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					prog_filter_step1.setVisibility(View.VISIBLE);
					prog_filter_step2.setVisibility(View.GONE);
					prog_filter_step1.requestFocus();
					btnProgFilterYes.requestFocus();
				}
			}
		});

		/** 2.3 节目管理—全部节目 ***********/
		itemAllProgSwitch = new VListViewItem(mContext, strProgramManager_ProgarmAll, null, VListViewItem.on_off);
		itemAllProgSwitch.setSwitchOn(DtvChannelManager.getInstance().GetAllchFlag());

		itemAllProgSwitch.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "cuixy arg0: " + arg0);
				if (arg0 == 0) {
					DtvChannelManager.getInstance().setAllchFlag(false);
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager)
									+ ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_ProgarmAll) + ",value=" + mContext.getResources().getString(R.string.off));
				} else {
					DtvChannelManager.getInstance().setAllchFlag(true);
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager)
									+ ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_ProgarmAll) + ",value=" + mContext.getResources().getString(R.string.on));
				}

				/**
				 * 全部节目开关改变，重新刷新EPG	2015-2-25	YangLiu
				 */
				if (DtvRoot.is5508Q2) {
					Log.i("YangLiu", "itemAllProgSwitch---->the channelList has changed,	refreshEPG");
					DtvChannelManager.refreshEPG();
				}
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		// /////////////////////////////////////////////cuixiaoyan 2014-10-20
		// ////////////////////////////
		itemAllProgSwitch.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							mProgramManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
				}
				return false;
			}
		});
		// //////////////////////////////////////

		/** 2.4 节目管理—数字广播 ***********/
		itemDtvRadio = new VListViewItem(mContext, strProgramManager_DtvRadio, null, VListViewItem.on_off);
		List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
		if (channelList == null || channelList.size() <= 0) {
			itemDtvRadio.setSwitchOn(false);
		} else {
			Log.e(TAG, "pang    111there is no any channel***");
			int channelType = mDtvChannelManager.getCurChannelType();
			Log.e(TAG, "pang    222there is no any channelType==*** " + channelType);
			if (channelType == ConstServiceType.SERVICE_TYPE_TV) {
				itemDtvRadio.setSwitchOn(false);
			} else {
				itemDtvRadio.setSwitchOn(true);
			}
			Log.e(TAG, "pang    333there is no any channel***");
		}
		itemDtvRadio.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel***");
					itemDtvRadio.setSwitchOn(false);
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager)
									+ ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_DtvRadio) + ",value=" + mContext.getResources().getString(R.string.off));
					mToastDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
					mToastDialog.getWindow().setType(2003);
					mToastDialog.show();
					return;
				}
				boolean ischange = mDtvChannelManager.changeChannelType();
				int mchannelType = mDtvChannelManager.getCurChannelType();
				if (ischange) {
					// 切换类型
					if (arg0 == 0) {
						Log.d("CH_ER_COLLECT",
								"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
										+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
										+ mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager) + ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_DtvRadio)
										+ ",value=" + mContext.getResources().getString(R.string.off));
					} else {
						Log.d("CH_ER_COLLECT",
								"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
										+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
										+ mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager) + ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_DtvRadio)
										+ ",value=" + mContext.getResources().getString(R.string.on));
					}
				} else {
					// 无另外类型时，出提示对应提示信息�?
					if (mDtvChannelManager.getCurChannelType() == ConstServiceType.SERVICE_TYPE_TV) {
						itemDtvRadio.setSwitchOn(false);
						Log.d("CH_ER_COLLECT",
								"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
										+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
										+ mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager) + ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_DtvRadio)
										+ ",value=" + mContext.getResources().getString(R.string.off));
						mToastDialog.setMessage(R.string.NoRadioChannel);
					} else {
						itemDtvRadio.setSwitchOn(true);
						Log.d("CH_ER_COLLECT",
								"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
										+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
										+ mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager) + ";item2=" + mContext.getResources().getString(R.string.dtv_ProgramManager_DtvRadio)
										+ ",value=" + mContext.getResources().getString(R.string.on));
						mToastDialog.setMessage(R.string.NoDtvChannel);
					}
					mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
					mToastDialog.getWindow().setType(2003);
					mToastDialog.show();
				}
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		// ////////////////////////////cuixiaoyan 2014-10-20
		// ////////////////////////
		itemDtvRadio.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							mProgramManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
				}
				return false;
			}
		});

		/* if (!DtvRoot.HasDTVBootSVCScanStart()) {
			mProgramManageView.addListViewItem(itemManualSort);//2015-3-23 加入EPG智能分类后不需要手动排序 YangLiu
		} */

		/*
		 * if(ConstSourceID.DTMB != mSourceManager.getCurSourceID()){
		 * mProgramManageView.addListViewItem(itemProgFilter);//2015-4-16
		 * 根据规格取消手动过滤独立功能 YangLiu }
		 */
		mProgramManageView.addListViewItem(itemChannelInfo);
		mProgramManageView.addListViewItem(itemAllProgSwitch);
		mProgramManageView.addListViewItem(itemDtvRadio);
	}

	// //////////////////fengy 2014-5-19 ///////////////////////////////////
	// 搜索管理——刷新搜索菜单
	public static void RefreshScanMenu(String strTitle, String strSubTitle, String strParameter1, String strParameter2, int tmpProgress) {
		if (mscanType == scantype.DTV_ScanAuto) {
			itemAutoSearch.refreshTitle(strTitle);
			itemAutoSearch.refreshSubhead(strSubTitle);
			mAutoSearchProgressBar.setProgress(tmpProgress);
			mAutoSearchParameter1.setText(strParameter1);
			mAutoSearchParameter2.setText(strParameter2);
		} else if (mscanType == scantype.DTV_ScanList) {
			mListSearchItem.refreshTitle(strTitle);
			mListSearchItem.refreshSubhead(strSubTitle);
			mListSearchProgressBar.setProgress(tmpProgress);
			mListSearchParameter1.setText(strParameter1);
			mListSearchParameter2.setText(strParameter2);
		} else if (mscanType == scantype.DTV_ScanAutoExtra) {
			itemUserSearch.refreshTitle(strTitle);
			itemUserSearch.refreshSubhead(strSubTitle);
			mUserSearchProgressBar.setProgress(tmpProgress);
			mUserSearchParameter1.setText(strParameter1);
			mUserSearchParameter2.setText(strParameter2);
		} else if (mscanType == scantype.DTV_ScanAutoExtra_Dmbt) {
			itemUserDmbtSearch.refreshTitle(strTitle);
			itemUserDmbtSearch.refreshSubhead(strSubTitle);
			mUserDmbtSearchProgressBar.setProgress(tmpProgress);
			mUserDmbtSearchParameter1.setText(strParameter1);
			mUserDmbtSearchParameter2.setText(strParameter2);
		} else if (mscanType == scantype.DTV_ScanAutoExtra_SearchGuide) {
			searchGuideTitle.setText(strTitle);
			mGuideSearchProgressBar.setProgress(tmpProgress);
			mGuideSearchParameter1.setText(strParameter1);
			mGuideSearchParameter2.setText(strParameter2);
		} else if (mscanType == scantype.DTV_ScanMaunal) {
			itemManualSearch.refreshTitle(strTitle);
			itemManualSearch.refreshSubhead(strSubTitle);
			mManualSearchProgressBar.setProgress(tmpProgress);
			mManualSearchParameter1.setText(strParameter1);
			mManualSearchParameter2.setText(strParameter2);
		} else if (mscanType == scantype.DTV_ScanMaunal_Dmbt) {
			itemManualDmbtSearch.refreshTitle(strTitle);
			itemManualDmbtSearch.refreshSubhead(strSubTitle);
			mManualDmbtSearchProgressBar.setProgress(tmpProgress);
			mManualDmbtSearchParameter1.setText(strParameter1);
			mManualDmbtSearchParameter2.setText(strParameter2);
		}
	}

	// //////////////////fengy 2014-5-19 ///////////////////////////////////
	// 搜索信号——隐藏信号扫描信号
	public static void HideScanMenuDetail() {
		if (isScanning == true) {
			isScanning = false;
			CommonProgressInfoDialog savingDiglog = new CommonProgressInfoDialog(mContext);
			savingDiglog.getWindow().setType(2003);
			savingDiglog.setDuration(1);
			savingDiglog.setButtonVisible(false);
			savingDiglog.setCancelable(false);
			savingDiglog.setMessage(mContext.getString(R.string.dtv_scan_store_program));
			savingDiglog.show();
		}
		mMainMenu.setMenukeyUseless(false);
		if (MainMenuRootData.getGuideScan()) {
			if ((guideMenu != null) && (guideMenu.isShowing())) {
				guideMenu.dismiss();
			}
			DtvRoot.dismissSearchGuide();
			DtvRoot.handleScreenSaverMessages();
		} else {
			if (mscanType == scantype.DTV_ScanAuto) {
				Log.i(TAG, "auto search HideScanMenuDetail");
				itemAutoSearch.PullDown(false);
				itemAutoSearch.refreshTitle(strScanManager_AutoScan);
				itemAutoSearch.refreshSubhead("");
				itemAutoSearch.requestfouse();
				auto_search_step1.setVisibility(View.VISIBLE);
				auto_search_step2.setVisibility(View.GONE);
			} else if (mscanType == scantype.DTV_ScanList) {
				Log.i(TAG, "list search HideScanMenuDetail");
				mListSearchItem.PullDown(false);
				mListSearchItem.refreshTitle(strAdvanced_Customer_ListScan);
				mListSearchItem.refreshSubhead("");
				mListSearchItem.requestfouse();
				list_search_step1.setVisibility(View.VISIBLE);
				list_search_step2.setVisibility(View.GONE);
			} else if (mscanType == scantype.DTV_ScanAutoExtra) {
				Log.i(TAG, "user search HideScanMenuDetail");
				itemUserSearch.PullDown(false);
				itemUserSearch.refreshTitle(strAdvanced_Customer_Scan);
				itemUserSearch.refreshSubhead("");
				itemUserSearch.requestfouse();
				user_search_step1.setVisibility(View.VISIBLE);
				user_search_step2.setVisibility(View.GONE);
			} else if (mscanType == scantype.DTV_ScanAutoExtra_Dmbt) {
				Log.i(TAG, "user Dmbt search HideScanMenuDetail");
				itemUserDmbtSearch.PullDown(false);
				itemUserDmbtSearch.refreshTitle(strAdvanced_Customer_Scan);
				itemUserDmbtSearch.refreshSubhead("");
				itemUserDmbtSearch.requestfouse();
				user_dmbt_search_step1.setVisibility(View.VISIBLE);
				user_dmbt_search_step2.setVisibility(View.GONE);
			} else if (mscanType == scantype.DTV_ScanMaunal) {
				Log.i(TAG, "manual search HideScanMenuDetail");
				itemManualSearch.PullDown(false);
				itemManualSearch.refreshTitle(strAdvanced_Customer_Scan);
				itemManualSearch.refreshSubhead("");
				itemManualSearch.requestfouse();
				manual_search_step1.setVisibility(View.VISIBLE);
				manual_search_step2.setVisibility(View.GONE);
			} else if (mscanType == scantype.DTV_ScanMaunal_Dmbt) {
				Log.i(TAG, "manual Dmbt search HideScanMenuDetail");
				itemManualDmbtSearch.PullDown(false);
				itemManualDmbtSearch.refreshTitle(strAdvanced_Customer_Scan);
				itemManualDmbtSearch.refreshSubhead("");
				itemManualDmbtSearch.requestfouse();
				manual_dmbt_search_step1.setVisibility(View.VISIBLE);
				manual_dmbt_search_step2.setVisibility(View.GONE);
			} else if (mscanType == scantype.DTV_ScanAutoExtra_SearchGuide) {
				Log.i(TAG, "searchGuide search HideScanMenuDetail");
				mSearchGuideStep4Item.PullDown(false);
				mSearchGuideStep4Item.requestfouse();
				guide_search_step1.setVisibility(View.VISIBLE);
				guide_search_step2.setVisibility(View.GONE);
			}
		}
	}

	// //////////////////fengy 2014-5-19 ///////////////////////////////////
	// 搜索信息——显示信号扫描信信号
	public void ShowScanMenuDetail() {
		isScanning = true;
		setGuideScan(false);
		if (mscanType == scantype.DTV_ScanAuto) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanAuto");
			auto_search_step1.setVisibility(View.GONE);
			auto_search_step2.setVisibility(View.VISIBLE);
			itemAutoSearch.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
			btnAutoSearchCancle.setFocusable(true);
			btnAutoSearchCancle.requestFocus();
		} else if (mscanType == scantype.DTV_ScanList) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanList");
			list_search_step1.setVisibility(View.GONE);
			list_search_step2.setVisibility(View.VISIBLE);
			mListSearchItem.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
			btnListSearchCancle.setFocusable(true);
			btnListSearchCancle.requestFocus();
		} else if (mscanType == scantype.DTV_ScanAutoExtra) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanAutoExtra");
			user_search_step1.setVisibility(View.GONE);
			user_search_step2.setVisibility(View.VISIBLE);
			itemUserSearch.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
			btnUserSearchCancle.setFocusable(true);
			btnUserSearchCancle.requestFocus();
		} else if (mscanType == scantype.DTV_ScanAutoExtra_Dmbt) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanAutoExtra_Dmbt");
			user_dmbt_search_step1.setVisibility(View.GONE);
			user_dmbt_search_step2.setVisibility(View.VISIBLE);
			itemUserDmbtSearch.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
			btnUserDmbtSearchCancle.setFocusable(true);
			btnUserDmbtSearchCancle.requestFocus();
		} else if (mscanType == scantype.DTV_ScanMaunal) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanMaunal");
			manual_search_step1.setVisibility(View.GONE);
			manual_search_step2.setVisibility(View.VISIBLE);
			itemManualSearch.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
			btnManualSearchCancle.setFocusable(true);
			btnManualSearchCancle.requestFocus();
		} else if (mscanType == scantype.DTV_ScanMaunal_Dmbt) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanMaunal_Dmbt");
			manual_dmbt_search_step1.setVisibility(View.GONE);
			manual_dmbt_search_step2.setVisibility(View.VISIBLE);
			itemManualDmbtSearch.refreshTitle(mContext.getResources().getString(R.string.dtv_scan_programing));
			btnManualDmbtSearchCancle.setFocusable(true);
			btnManualDmbtSearchCancle.requestFocus();
		} else if (mscanType == scantype.DTV_ScanAutoExtra_SearchGuide) {
			Log.i(TAG, "ShowScanMenuDetail, mscanType = scantype.DTV_ScanAutoExtra_SearchGuide");
			// 搜索向导的yes/no小时，第二步取消出现
			// setGuideScan(true);
			searchGuideTitle.setText(mContext.getResources().getString(R.string.dtv_scan_programing));
			guide_search_step1.setVisibility(View.GONE);
			guide_search_step2.setVisibility(View.VISIBLE);
			btnSearchGuideCancel.setFocusable(true);
			btnSearchGuideCancel.requestFocus();
		}
	}

	// ///////////////////////////////////
	/* 运营商选择——DTV的运营商信息 */
	private void initDtvProviderData() {
		// TODO Auto-generated method stub
		dtvprovider_list = null;
		dtvprovider_list = new DTVProviderList();
		int citySize = 0;
		data = mDtvOperatorManager.getProvincesBySourceId(1, mContext);
		if (null != data && data.size() > 0) {
			String tmp = mContext.getString(R.string.dtv_scansettings_operator_general);
			Set<String> provinces = data.keySet();
			List<String> proList = new ArrayList<String>(provinces);
			Collections.sort(proList);
			if (proList.contains(tmp)) {
				proList.remove(tmp);
				proList.add(0, tmp);
			}

			for (final String province : proList) {
				operatorListtmp = data.get(province);
				Collections.sort(operatorListtmp);
				if (null != operatorListtmp) {
					citySize = operatorListtmp.size();
					for (int i = 0; i < citySize; i++) {
						String strOperatorName = operatorListtmp.get(i).getOperatorName();
						int intOperatorCode = operatorListtmp.get(i).getOperatorCode();
						DTVProvider provider = new DTVProvider(province, strOperatorName, intOperatorCode);
						dtvprovider_list.add(provider);
					}
				}
			}
		}
	}

	/* 运营商选择——运营商信息初始 */
	private void initCurDtvProvider() {
		/**
		 * 动态更新运营商界面标题，解决从T切C换运营商不成功问题(切信号源后需要重新刷新运营商列表) 2015-4-21 YangLiu
		 */
		operatorList = DtvOperatorManager.getInstance().getOperatorList();
		String string = mContext.getResources().getString(R.string.dtv_scan_setup_cur_operator, mDtvOperatorManager.getCurOperator().getOperatorName());
		// Log.i("YangLiu", "-------------------string="+string);
		if (curOperator != null) {
			curOperator.setText(string);
		}

		/**
		 * 读取所有运营商列表，并设置当前运营商列表
		 */
		DtvOperator curOperator = mDtvOperatorManager.getCurOperator();
		int curCode = curOperator.getOperatorCode();
		Log.i(TAG, "curCode: " + curCode);

		List<String> provinceList = dtvprovider_list.getProvinceList();
		for (int i = 0; i < provinceList.size(); i++) {
			Log.i(TAG, "provinceList.get(" + i + "): " + provinceList.get(i));
			List<Integer> providerCode = dtvprovider_list.getCodeList(i);
			for (int j = 0; j < providerCode.size(); j++) {
				Log.i(TAG, "providerCode.get(" + j + "): " + providerCode.get(j));
				if (curCode == providerCode.get(j)) {
					dtvprovider_selector.setCurrentProvince(i);
					dtvprovider_selector.setCurrentProvider(j);
					dtvprovider_selector_search_guide.setCurrentProvince(i);
					dtvprovider_selector_search_guide.setCurrentProvider(j);
					Log.i(TAG, "Province: " + i + ", Provider: " + j);
				}
			}
		}
	}

	/* 运营商选择——通过运营商代码改变运营商信息 */
	public void changeOperationByCode(int intOperatorCode) {
		// TODO Auto-generated method stub
		for (DtvOperator operator : operatorList) {
			if (operator.getOperatorCode() == intOperatorCode) {
				mDtvOperatorManager.setCurOperator(operator);
				break;
			}
		}
	}

	/*******************************************************
	 * **** 三、搜索管理****
	 ******************************************************/
	private void initScanManagerView() {
		// TODO Auto-generated method stub
		mChannelManageView = new VListView(mContext);
		Log.i(TAG, "pang    initScanManagerView 1 moduleInfoView== " + mChannelManageView);
		String strScanManager_Advanced = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced);
		if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
			Log.i(TAG, "pang    initScanManagerView 2 moduleInfoView== " + mChannelManageView);
			/* 3.1 搜索管理—搜索向导********* */
			itemSearchGuide = new VListViewItem(mContext, strScanManager_Guide, null, VListViewItem.Jump_To);
			itemSearchGuide.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_RIGHT)) {
							mSearchGuideStepBackFlag = 0;
							mMainMenu.backToNextItem(mScanManage, mSearchGuide);
							curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
							mCurSourceIndex = mSourceManager.getCurSourceIndex();
							mSearchGuideSignalgroup.check(mCurSourceIndex);
							mSearchGuideSignalgroup.refreshCheck(mCurSourceIndex);
							mSearchGuideStep1Item.setLosefocuse(false);
							mSearchGuideStep2Item.setLosefocuse(true);
							mSearchGuideStep3Item.setLosefocuse(true);
							mSearchGuideStep4Item.setLosefocuse(true);
							mSearchGuideStep1Item.PullDown(true);
							mSearchGuideStep2Item.PullDown(false);
							mSearchGuideStep3Item.PullDown(false);
							mSearchGuideStep4Item.PullDown(false);
							btnSearchGuideStep1Next.requestFocus();
							bSearchGuideFirst = true;
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					mSearchGuideStepBackFlag = 0;
					mMainMenu.backToNextItem(mScanManage, mSearchGuide);
					curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
					mCurSourceIndex = mSourceManager.getCurSourceIndex();
					mSearchGuideSignalgroup.check(mCurSourceIndex);
					mSearchGuideSignalgroup.refreshCheck(mCurSourceIndex);
					mSearchGuideStep1Item.setLosefocuse(false);
					mSearchGuideStep2Item.setLosefocuse(true);
					mSearchGuideStep3Item.setLosefocuse(true);
					mSearchGuideStep4Item.setLosefocuse(true);
					mSearchGuideStep1Item.PullDown(true);
					mSearchGuideStep2Item.PullDown(false);
					mSearchGuideStep3Item.PullDown(false);
					mSearchGuideStep4Item.PullDown(false);
					btnSearchGuideStep1Next.requestFocus();
					bSearchGuideFirst = true;
					return false;
				}
			});
		}

		/* 3.2 搜索管理——>自动搜索************* */
		itemAutoSearch = new VListViewItem(mContext, strScanManager_AutoScan, null, VListViewItem.Text_pure_pull);
		mAutoSearchView = appInfView.inflate(R.layout.warning_dialog, null, false);
		auto_search_step1 = (LinearLayout) mAutoSearchView.findViewById(R.id.warning_yes_no_dialog);
		auto_search_step2 = (LinearLayout) mAutoSearchView.findViewById(R.id.progressbar_cancel_dialog);
		btnAutoSearchYes = (Button) auto_search_step1.findViewById(R.id.yes);
		btnAutoSearchCancle = (Button) auto_search_step2.findViewById(R.id.cancel);
		btnAutoSearchNo = (Button) auto_search_step1.findViewById(R.id.no);
		mAutoSearchProgressBar = (ProgressBar) auto_search_step2.findViewById(R.id.progressBar);
		mAutoSearchParameter1 = (TextView) auto_search_step2.findViewById(R.id.Parameter1);
		mAutoSearchParameter2 = (TextView) auto_search_step2.findViewById(R.id.Parameter2);
		itemAutoSearch.addPullDownItem(mAutoSearchView);

		btnAutoSearchYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("CH_ER_COLLECT",
						"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
								+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
								+ ";item2=" + mContext.getResources().getString(R.string.dtv_ScanManager_AutoScan));
				mMainMenu.setMenukeyUseless(true);
				mscanType = scantype.DTV_ScanAuto;
				ShowScanMenuDetail();
				menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
				menuAutoScan.Scanready();
			}
		});
		btnAutoSearchYes.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// //////////////////////////////////////cuixiaoyan 2014-10-20//////////////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							Log.d("CH_ER_COLLECT",
									"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
											+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
											+ ";item2=" + mContext.getResources().getString(R.string.dtv_ScanManager_AutoScan));
							mMainMenu.setMenukeyUseless(true);
							mscanType = scantype.DTV_ScanAuto;
							ShowScanMenuDetail();
							menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
							menuAutoScan.Scanready();
							return true;
						case 235:// keyboard Volume Down
							itemAutoSearch.PullDown(false);
							mChannelManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}

					// ///////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						itemAutoSearch.PullDown(false);
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						itemAutoSearch.requestfouse();
						return true;
					}
				}
				return false;
			}
		});

		btnAutoSearchNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setGuideScan(false);
				mscanType = scantype.DTV_ScanAuto;
				HideScanMenuDetail();
			}
		});
		btnAutoSearchNo.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// ///////////////////////////////////////cuixiaoyan 2014
					// -10 -24 /////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							setGuideScan(false);
							mscanType = scantype.DTV_ScanAuto;
							HideScanMenuDetail();
							return true;
						case 235:// keyboard Volume Down
							itemAutoSearch.PullDown(false);
							mChannelManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// /////////////////////////////////////////////////////////////////////////////////

					if ((arg1 == KeyEvent.KEYCODE_DPAD_LEFT) || (arg1 == KeyEvent.KEYCODE_DPAD_DOWN)) {
						itemAutoSearch.PullDown(false);
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						itemAutoSearch.requestfouse();
						return true;
					}
				}
				return false;
			}
		});

		btnAutoSearchCancle.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// //////////////////////////////cuixiaoyan 2014- 10- 20
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							mMainMenu.setMenukeyUseless(false);
							mscanType = scantype.DTV_ScanAuto;
							menuAutoScan.Scantermined();
							HideScanMenuDetail();
							return true;
						case 235:// keyboard Volume Down
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// /////////////////////////////////////////////////////
					if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
						mMainMenu.setMenukeyUseless(false);
						mscanType = scantype.DTV_ScanAuto;
						menuAutoScan.Scantermined();
						HideScanMenuDetail();
						return false;
					}
				}
				return true;
			}
		});

		itemAutoSearch.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// /////////////////////////////////////////////////cuixiaoyan
					// 2014 -10-20 //////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							itemAutoSearch.PullDown(false);
							mChannelManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// //////////////////////////////////////////////////////////////////////////
					if ((arg1 == KeyEvent.KEYCODE_DPAD_LEFT) || (arg1 == KeyEvent.KEYCODE_DPAD_UP) || (arg1 == KeyEvent.KEYCODE_BACK)) {
						itemAutoSearch.PullDown(false);
						Log.i(TAG, "pang    initScanManagerView 7 itemAutoSearch== " + itemAutoSearch);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		itemAutoSearch.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					if (isDirectSearch == true) {
						isDirectSearch = false;
					} else {
						auto_search_step1.setVisibility(View.VISIBLE);
						auto_search_step2.setVisibility(View.GONE);
						auto_search_step1.requestFocus();
						btnAutoSearchYes.requestFocus();
					}
				}
			}
		});

		/**** 3.3 搜索管理——>高级 ************/
		itemSearchAdvance = new VListViewItem(mContext, strScanManager_Advanced, null, VListViewItem.pull_down);
		itemSearchAdvance.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				listSenior = new VListView(mContext);
				// 搜索管理——>高级——>自定义搜索
				mCustomSearchItem = new VListViewItem(mContext, strScanManager_Advanced_Customer, null, VListViewItem.Jump_To);
				mCustomSearchItem.setOnKeyListener(new OnItemViewOnKeyListener() {

					@Override
					public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
						// TODO Auto-generated method stub
						// ////////////////////////////////////////////////////////////////////////
						// /////////////////////////////////fengy
						// 2014-10-24/////////////////////////////

						if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {// 跳转到自定义搜索界面

							mListSearchItem.PullDown(false);
							mMainMenu.jumpToNextItem(mScanManage, mCustomSearch);
							return true;
						} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) {
							mChannelManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_back) {
							itemSearchAdvance.requestfouse();
							return true;
						}
						return false;
					}

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						// 跳转到自定义搜索界面
						mMainMenu.jumpToNextItem(mScanManage, mCustomSearch);
						return false;
					}
				});

				// 搜索管理——>高级——>信号接入
				if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
					String strCurSource = mSourceManager.getSourceList().get(mSourceManager.getCurSourceIndex()).miSourceName;
					mSignalInputItem = new VListViewItem(mContext, strAdvanced_AntennaInput, strCurSource, VListViewItem.Jump_To);
					mSignalInputItem.setOnKeyListener(new OnItemViewOnKeyListener() {

						@Override
						public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
							// TODO Auto-generated method stub
							if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
								if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
									mChannelManageView.colsepulldown();
								} else if (arg1 == KeyEvent.KEYCODE_BACK) {
									itemSearchAdvance.requestfouse();
									return true;
								} else if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_RIGHT)) {
									// 跳转到信号接入
									mCurSourceIndex = mSourceManager.getCurSourceIndex();
									mSignalInputgroup.check(mCurSourceIndex);
									mSignalInputgroup.refreshCheck(mCurSourceIndex);
									mSignalInputItem.refreshSubhead(mSourceList.get(mCurSourceIndex).miSourceName);
									mMainMenu.jumpToNextItem(mScanManage, mSignalInput);
									return true;
								}
							}
							return false;
						}

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							// TODO Auto-generated method stub
							// 跳转到信号接�?
							mCurSourceIndex = mSourceManager.getCurSourceIndex();
							mSignalInputgroup.check(mCurSourceIndex);
							mSignalInputgroup.refreshCheck(mCurSourceIndex);
							mSignalInputItem.refreshSubhead(mSourceList.get(mCurSourceIndex).miSourceName);
							mMainMenu.jumpToNextItem(mScanManage, mSignalInput);
							return false;
						}
					});
				}

				// 搜索管理——>高级——>运营商设置
				if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
					mOperatorItem = new VListViewItem(mContext, strAdvanced_OperatorSetting, null, VListViewItem.Jump_To);
					mOperatorItem.setOnKeyListener(new OnItemViewOnKeyListener() {

						@Override
						public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
							// TODO Auto-generated method stub
							if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
								if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
									mChannelManageView.colsepulldown();
								} else if (arg1 == KeyEvent.KEYCODE_BACK) {
									itemSearchAdvance.requestfouse();
									return true;
								} else if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_RIGHT)) {
									initCurDtvProvider();
									mMainMenu.jumpToNextItem(mScanManage, mDtvProvider);
									return true;
								}
							}
							return false;
						}

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							// TODO Auto-generated method stub
							initCurDtvProvider();
							mMainMenu.jumpToNextItem(mScanManage, mDtvProvider);
							return false;
						}
					});
				}

				// 搜索管理——>高级（添加3个条目）
				listSenior.addListViewItem(mCustomSearchItem);
				if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
					if (null != mSourceManager.getSourceList() && (mSourceManager.getSourceList().size() == 1)) {
						if (mSourceManager.getProductType() == ConstProductType.PRODUCT_C) {
							listSenior.addListViewItem(mOperatorItem);
						}
					} else {
						listSenior.addListViewItem(mSignalInputItem);
						listSenior.addListViewItem(mOperatorItem);
					}
				}
				return listSenior.getView();
			}
		});

		itemSearchAdvance.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// ////////////////////////////////cuixiaoyan 2014-10-20
					// //////////////////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							mChannelManageView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// ////////////////////////////////////////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mChannelManageView.colsepulldown();
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		itemSearchAdvance.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
						if (mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH) {
							mOperatorItem.setLosefocuse(true);
						} else {
							mOperatorItem.setLosefocuse(false);
						}
					}
				}
			}
		});

		if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
			mChannelManageView.addListViewItem(itemSearchGuide);
		}
		mChannelManageView.addListViewItem(itemAutoSearch);
		mChannelManageView.addListViewItem(itemSearchAdvance);
	}

	/**
	 * 更多设置—�?3D模式有关的方法，为下面更多设置中3D模式做准�?
	 */
	// 3D模式种类
	private String dtv3DModeConvertToString(ENUM3DMode en3DMode) {
		String ret = null;

		switch (en3DMode) {
			case CH_3D_MODE_OFF:// 3
				ret = mContext.getResources().getString(R.string.dtv_3DMode_Off);
				break;
			case CH_3D_MODE_SBS:// 左右
				ret = mContext.getResources().getString(R.string.dtv_3DMode_LeftRight);
				break;
			case CH_3D_MODE_TP:// 上下
				ret = mContext.getResources().getString(R.string.dtv_3DMode_UpDown);
				break;
			case CH_3D_MODE_2DTO3D:// 2D-3D
				ret = mContext.getResources().getString(R.string.dtv_2DMode23D);
				break;
			default:
				// ret = "不支�?
				break;
		}
		return ret;
	}

	// 设置3D模式
	private void setDtv3DModeByIndex(int index) {
		ENUM3DMode en3DMode = ENUM3DMode.CH_3D_MODE_OFF;
		switch (index) {
			case 0:// 3D�?
				en3DMode = ENUM3DMode.CH_3D_MODE_OFF;
				break;
			case 1:// 左右
				en3DMode = ENUM3DMode.CH_3D_MODE_SBS;
				break;
			case 2:// 上下
				en3DMode = ENUM3DMode.CH_3D_MODE_TP;
				break;
			case 3:// 2D�?D
				en3DMode = ENUM3DMode.CH_3D_MODE_2DTO3D;
				break;
			default:
				en3DMode = ENUM3DMode.CH_3D_MODE_OFF;
				break;
		}
		try {
			threedmanager.select3DMode(en3DMode);
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 取得3D模式
	private ENUM3DMode getCur3DMode() {
		ENUM3DMode en3DMode = ENUM3DMode.CH_3D_MODE_OFF;
		try {
			en3DMode = threedmanager.get3DMode();
		} catch (TVCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!isDtv3DMode(en3DMode)) {
			en3DMode = ENUM3DMode.CH_3D_MODE_OFF;
		}
		return en3DMode;
	}

	private int getIndexBy3DMode(ENUM3DMode en3DMode) {
		int index = 0;
		switch (en3DMode) {
			case CH_3D_MODE_OFF:
				index = 0;
				break;

			case CH_3D_MODE_SBS:
				index = 1;
				break;

			case CH_3D_MODE_TP:
				index = 2;
				break;

			case CH_3D_MODE_2DTO3D:
				index = 3;
				break;

			default:
				index = 0;
				break;
		}
		return index;
	}

	// 判断是不�?D模式
	private boolean isDtv3DMode(ENUM3DMode en3DMode) {
		if ((en3DMode != ENUM3DMode.CH_3D_MODE_OFF) && (en3DMode != ENUM3DMode.CH_3D_MODE_SBS) && (en3DMode != ENUM3DMode.CH_3D_MODE_TP) && (en3DMode != ENUM3DMode.CH_3D_MODE_2DTO3D)) {
			return false;
		} else {
			return true;
		}
	}

	/*******************************************************
	 * **** 四、更多设�?****
	 ******************************************************/
	private void initMoreSettingView() {
		// TODO Auto-generated method stub
		tvManager = TVManager.getInstance(mContext);
		try {
			threedmanager = tvManager.getThreeDimensionManager();
		} catch (TVManagerNotInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMoreSettingView = new VListView(mContext);
		/* 4.1 更多设置—�?3D设置****************************************** */
		if (dtvInterface.getSystem3D() == true) {
			String str3DModeValue = dtv3DModeConvertToString(getCur3DMode());
			String str3DMode = mContext.getResources().getString(R.string.dtv_MoreSetting_3DMode);

			item3DMode = new VListViewItem(mContext, str3DMode, str3DModeValue, VListViewItem.pull_down);
			final String[] dtv3DModeArray = mContext.getResources().getStringArray(R.array.str_dtv_3d_modes);
			mgroup = new VRadioGroup(mContext);
			for (int i = 0; i < dtv3DModeArray.length; i++) {
				VRadio vRadio = new VRadio(dtv3DModeArray[i]);
				mgroup.addVRadio(vRadio);
			}
			mgroup.check(getIndexBy3DMode(getCur3DMode()));

			mgroup.setOnVRadioGroupListener(new OnVRadioGroupListener() {

				@Override
				public void onClick(int arg0) {
					// TODO Auto-generated method stub
					setDtv3DModeByIndex(arg0);
					item3DMode.refreshSubhead(dtv3DModeArray[arg0]);
				}

				@Override
				public void onCheckedChange(int arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean onKey(int arg0, KeyEvent arg1, int arg2) {
					// TODO Auto-generated method stub
					if (arg1.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg0 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mMoreSettingView.colsepulldown();
						} else if (arg0 == KeyEvent.KEYCODE_BACK) {
							item3DMode.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1, int arg2) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			item3DMode.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_BACK) {
							mMoreSettingView.colsepulldown();
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			item3DMode.addPullDownItem(mgroup);
		}

		// /////////////////////////////
		/* 4.2 更多设置—>DTV设置****************************************** */
		String strDTVSetting = mContext.getResources().getString(R.string.dtv_MoreSetting_DTVSetting);
		itemDTVSetting = new VListViewItem(mContext, strDTVSetting, null, VListViewItem.pull_down);
		VListView listDTVSetting = new VListView(mContext);
		if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
			// 4.2.1 更多设置—>DTV设置—>视密信息
			String strCICA_Info = mContext.getResources().getString(R.string.dtv_DTVSetting_CICA_Info);
			itemEncryptionInfo = new VListViewItem(mContext, strCICA_Info, null, VListViewItem.Jump_To);
			DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
			if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
				itemEncryptionInfo.setLosefocuse(false);
			} else {
				itemEncryptionInfo.setLosefocuse(true);
			}
			itemEncryptionInfo.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mMoreSettingView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemDTVSetting.requestfouse();
							return true;
						} else if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_RIGHT)) {
							DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
							if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
								// 保留下面这行代码，否则视密信息无法读�?

								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
								mMainMenu.destroy();// by cuixy

								// isCAFirstShowing = true;By YangLiu
								// 判断CA是否第一次显示

								Intent intent = new Intent(MainMenuReceiver.INTENT_CICAQUERY);
								intent.putExtra(MainMenuReceiver.DATA_CARDTYPE, mCardStatus.getCardType());
								mContext.sendBroadcast(intent);

								Log.i(TAG, "主菜单退出\n" + "card的类型是" + mCardStatus.getCardType());// by yangliu
							} else {
								Log.e(TAG, "LL no card or card exception***");
								mDialog.setMessage(R.string.dtv_menu_no_cica_prompt);
								mDialog.show();
							}
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
					if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
						// 保留下面这行代码，否则视密信息无法出�?
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
						mMainMenu.destroy();// by cuixy
						Intent intent = new Intent(MainMenuReceiver.INTENT_CICAQUERY);
						intent.putExtra(MainMenuReceiver.DATA_CARDTYPE, mCardStatus.getCardType());
						mContext.sendBroadcast(intent);
					} else {
						Log.e(TAG, "LL no card or card exception***");
						mDialog.setMessage(R.string.dtv_menu_no_cica_prompt);
						mDialog.show();
					}
					return false;
				}
			});
		}

		// 4.2.2 更多设置—�?DTV设置—�?声音设置
		itemAudioSetting = new VListViewItem(mContext, strAudioSetting, null, VListViewItem.Jump_To);
		itemAudioSetting.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				// ////////////////////////fengy
				// 2014-10-27/////////////////////////////
				if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {
					mAudioSettingView.colsepulldown();
					mMainMenu.jumpToNextItem(mMoreSetting, mAudioSetting);

					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) {
					mMoreSettingView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_back) {
					itemDTVSetting.requestfouse();
					return true;
				}
				// ///////////////////////////////////////////////////////////////

				/*
				 * if(arg2.getAction() == KeyEvent.ACTION_DOWN){
				 * /////////////////////////cuixiaoyan 2014-10-20
				 * ///////////////////////////////////////////
				 * isKeyboardProcessed = false; switch (arg2.getScanCode()) {
				 * case 232://keyboard Source case 236://keyboard Volume Up
				 * mMainMenu.jumpToNextItem(mMoreSetting, mAudioSetting); return
				 * true; case 235://keyboard Volume Down
				 * mMoreSettingView.colsepulldown(); mMainMenu.leftItemfouce();
				 * return true; default: break; }
				 * ///////////////////////////////
				 * //////////////////////////////////////////////////////////
				 * if(arg1 == KeyEvent.KEYCODE_DPAD_LEFT){
				 * mMoreSettingView.colsepulldown(); } else if(arg1 ==
				 * KeyEvent.KEYCODE_BACK){ itemDTVSetting.requestfouse(); return
				 * true; } else if((arg1 == KeyEvent.KEYCODE_DPAD_CENTER)
				 * ||(arg1==KeyEvent.KEYCODE_ENTER) ||(arg1 ==
				 * KeyEvent.KEYCODE_DPAD_RIGHT)){
				 * mAudioSettingView.colsepulldown();
				 * mMainMenu.jumpToNextItem(mMoreSetting, mAudioSetting); return
				 * true; } }
				 */
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mAudioSettingView.colsepulldown();
				mMainMenu.jumpToNextItem(mMoreSetting, mAudioSetting);
				return false;
			}
		});

		// 4.2.3 更多设置—�?DTV设置—�?DTV更新
		VListViewItem itemModuleUpgrade = new VListViewItem(mContext, strmModuleUpgrade, null, VListViewItem.Jump_To);
		//
		itemModuleUpgrade.setLosefocuse(true);
		itemModuleUpgrade.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {
					Intent mintent = new Intent();
					mintent.setAction("com.changhong.dtvloader.startMainMenu");
					getContext().sendBroadcast(mintent);
					mMainMenu.destroy();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) {
					mMoreSettingView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_back) {
					itemDTVSetting.requestfouse();
					return true;
				}

				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			public View getView() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		// 4.2.4 更多设置—�?DTV设置—�?DTV信息
		itemDTVInfo = new VListViewItem(mContext, strDtvinfo, null, VListViewItem.Jump_To);
		itemDTVInfo.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				// /////////////////////////////////////fengy
				// 2014-10-27////////////////////////////
				if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {
					mMainMenu.jumpToNextItem(mMoreSetting, mModuleInfo);
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) {
					mMoreSettingView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_back) {
					itemDTVSetting.requestfouse();
					return true;
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mMainMenu.jumpToNextItem(mMoreSetting, mModuleInfo);
				return false;
			}
		});

		// 4.2.5 更多设置—�?DTV设置—�?DTV出厂设置
		String strDTVFactoryOut = mContext.getResources().getString(R.string.dtv_DTVSetting_DTVFactoryOut);
		VListViewItem itemDTVFactoryOut = new VListViewItem(mContext, strDTVFactoryOut, null, VListViewItem.Text_pure);
		itemDTVFactoryOut.setOnClickListener(new OnItemViewClickListener() {
			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
				final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
				dialog.setDisplayString(mContext.getResources().getString(R.string.no_string),// dtv_scan_setup_waring
						mContext.getResources().getString(R.string.dtv_reset_warn_info));
				dialog.setOnShowListener(new DialogInterface.OnShowListener() {
					@Override
					public void onShow(DialogInterface arg0) {
						dialog.setShowTV(false);
					}
				});
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface arg0) {
						if (dialog.isShowTV()) {
							// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
							mMainMenu.destroy();// by cuixy
							dialog.setShowTV(false);
						}
					}
				});
				dialog.show();
				dialog.getmYesButton().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Log.d("CH_ER_COLLECT",
								"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
										+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_MoreSetting)
										+ ";item2=" + mContext.getResources().getString(R.string.dtv_MoreSetting_DTVSetting) + ";item3="
										+ mContext.getResources().getString(R.string.dtv_DTVSetting_DTVFactoryOut));
						DtvChannelManager.getInstance().reset();
						DtvConfigManager.getInstance().clearAll();
						MenuManager.getInstance().delAllScheduleEvents();
						DtvChannelManager.getInstance().setAllchFlag(false);
						itemAllProgSwitch.setSwitchOn(DtvChannelManager.getInstance().GetAllchFlag());
						dialog.dismiss();
						mToastDialog.setMessage(R.string.dtv_factory_over);
						mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
						mToastDialog.getWindow().setType(2003);
						mToastDialog.show();
					}
				});
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		itemDTVFactoryOut.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// //////////////////////////cuixiaoyan 2014-10-20
					// ///////////////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							mMoreSettingView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// //////////////////////////////////////////////////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mMoreSettingView.colsepulldown();
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						itemDTVSetting.requestfouse();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		if (mSourceManager.getProductType() != ConstProductType.PRODUCT_T) {
			listDTVSetting.addListViewItem(itemEncryptionInfo);
		}
		listDTVSetting.addListViewItem(itemAudioSetting);
		listDTVSetting.addListViewItem(itemModuleUpgrade);
		listDTVSetting.addListViewItem(itemDTVInfo);
		listDTVSetting.addListViewItem(itemDTVFactoryOut);

		itemDTVSetting.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				// ///////////////////////fengy 2014-10-30
				// /////////////////////////////
				if ((GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_enter) || (GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_nofunc)) {
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_left) {
					mMainMenu.leftItemfouce();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.pull_down) == Keyboard_back) {
					mMoreSettingView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		itemDTVSetting.addPullDownItem(listDTVSetting);

		// 4.3 更多设置—�?全部设置
		itemAllSetting = new VListViewItem(mContext, strAllSetting, null, VListViewItem.Jump_To);
		itemAllSetting.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub
				// 跳转到多媒体提供的全部设置apk
				Log.i(TAG, "remove ScreenSaver before enter EasySetting ");
				if (IsVSettingMenuShowing()) {
					VSettingMenuVisibilityControl(false);
				}
				DtvRoot.removeScreenSaverMessages();
				Intent in = new Intent("Changhong.EasySetting");
				mContext.startService(in);
			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		// ///////////////////////////////cuixiaoyan 2014-10-20
		// ////////////////////////
		itemAllSetting.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				// ///////////////////////////////fengy
				// 2014-10-24/////////////////////////////

				if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {
					// mListSearchItem.PullDown(false);
					// mMainMenu.jumpToNextItem(mScanManage, mCustomSearch);

					if (IsVSettingMenuShowing()) {
						VSettingMenuVisibilityControl(false);
					}
					DtvRoot.removeScreenSaverMessages();
					Intent in = new Intent("Changhong.EasySetting");
					mContext.startService(in);

					return true;
				} else if ((GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) || (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_back)) {
					// mMoreSettingView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				}
				/*
				 * else if(GetKeyboardState(arg1,arg2,VListViewItem.Jump_To)==
				 * Keyboard_back) { mMainMenu.leftItemfouce(); return true; }
				 */

				/*
				 * if(arg2.getAction() == KeyEvent.ACTION_DOWN){
				 * isKeyboardProcessed = false; switch (arg2.getScanCode()) {
				 * case 235://keyboard Volume Down
				 * mMoreSettingView.colsepulldown(); mMainMenu.leftItemfouce();
				 * return true; // case 236://keyboard Volume Up // return true;
				 * default: break; } }
				 */
				return false;
			}
		});
		// ////////////////////////////////////////////////////////////////////

		if (dtvInterface.getSystem3D() == true) {
			mMoreSettingView.addListViewItem(item3DMode);
		}
		mMoreSettingView.addListViewItem(itemDTVSetting);
		mMoreSettingView.addListViewItem(itemAllSetting);
	}

	/**
	 * 更多设置—�?全部设置—�?声音设置视图
	 */
	private void initAudioSettingView() {
		// TODO Auto-generated method stub
		mAudioSettingView = new VListView(mContext);
		final int curAudioMode;
		String strCurAudioMode;
		final String[] audioModeArray = mContext.getResources().getStringArray(R.array.menu_audio_mode);
		curAudioMode = mDtvChannelManager.getAudioModeSel();
		if ((curAudioMode < 0) || (curAudioMode > audioModeArray.length)) {
			strCurAudioMode = audioModeArray[0];
		} else {
			strCurAudioMode = audioModeArray[curAudioMode];
		}

		String strAudioMode = mContext.getResources().getString(R.string.dtv_AudioSetting_mode);
		itemAudioMode = new VListViewItem(mContext, strAudioMode, strCurAudioMode, VListViewItem.pull_down);
		mgroup = new VRadioGroup(mContext);
		for (int i = 0; i < audioModeArray.length; i++) {
			VRadio vRadio = new VRadio(audioModeArray[i]);
			mgroup.addVRadio(vRadio);
		}

		mgroup.check(curAudioMode);
		mgroup.setOnVRadioGroupListener(new OnVRadioGroupListener() {

			@Override
			public void onClick(int arg0) {
				// TODO Auto-generated method stub
				mDtvChannelManager.setAudioMode(arg0);
				itemAudioMode.refreshSubhead(audioModeArray[arg0]);
			}

			@Override
			public void onCheckedChange(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onKey(int arg0, KeyEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg0 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mAudioSettingView.colsepulldown();
						return false;
					} else if (arg0 == KeyEvent.KEYCODE_BACK) {
						itemAudioMode.requestfouse();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		itemAudioMode.addPullDownItem(mgroup);
		itemAudioMode.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mMainMenu.backToNextItem(mMoreSetting, mMoreSetting);
						itemAudioSetting.requestfouse();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		final int curAudioTrack;
		String[] audioTrackArray = mContext.getResources().getStringArray(R.array.menu_audio_track);
		String strAudioTrack = mContext.getResources().getString(R.string.dtv_AudioSetting_track);
		final String[] strAudioTrackvalue = mDtvChannelManager.getAudioTrack(audioTrackArray);
		curAudioTrack = mDtvChannelManager.getAudioTrackSelIndex();
		itemAudioTrack = new VListViewItem(mContext, strAudioTrack, strAudioTrackvalue[curAudioTrack], VListViewItem.pull_down);
		mgroup = new VRadioGroup(mContext);
		for (int i = 0; i < strAudioTrackvalue.length; i++) {
			VRadio vRadio = new VRadio(strAudioTrackvalue[i]);
			mgroup.addVRadio(vRadio);
		}

		mgroup.check(curAudioTrack);
		mgroup.setOnVRadioGroupListener(new OnVRadioGroupListener() {

			@Override
			public boolean onKey(int arg0, KeyEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg0 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mAudioSettingView.colsepulldown();
						return false;
					} else if (arg0 == KeyEvent.KEYCODE_BACK) {
						itemAudioTrack.requestfouse();
						return true;
					}
				}
				return false;
			}

			@Override
			public void onClick(int arg0) {
				// TODO Auto-generated method stub
				mDtvChannelManager.setAudioTrack(arg0);
				itemAudioTrack.refreshSubhead(strAudioTrackvalue[arg0]);
			}

			@Override
			public void onCheckedChange(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		itemAudioTrack.addPullDownItem(mgroup);

		itemAudioTrack.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mMainMenu.backToNextItem(mMoreSetting, mMoreSetting);
						itemAudioSetting.requestfouse();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mAudioSettingView.addListViewItem(itemAudioMode);
		mAudioSettingView.addListViewItem(itemAudioTrack);
	}

	/**
	 * 搜索管理—�?搜索向导视图
	 */
	private void initSearchGuideView() {
		// TODO Auto-generated method stub
		mSearchGuideListView = new VListView(mContext);
		mSearchGuideStep1Item = new VListViewItem(mContext, strSearchGuideStep1, null, VListViewItem.Text_pure_pull);
		mSearchGuideStep2Item = new VListViewItem(mContext, strSearchGuideStep2, null, VListViewItem.Text_pure_pull);
		mSearchGuideStep3Item = new VListViewItem(mContext, strSearchGuideStep3, null, VListViewItem.Text_pure_pull);
		mSearchGuideStep4Item = new VListViewItem(mContext, strSearchGuideStep4, null, VListViewItem.Text_pure_pull);

		mSearchGuideViewStep1 = appInfView.inflate(R.layout.vch_menu_search_guide_step1, null, false);
		mSearchGuideViewStep2 = appInfView.inflate(R.layout.vch_menu_search_guide_step2, null, false);
		mSearchGuideViewStep3 = appInfView.inflate(R.layout.vch_menu_search_guide_step3, null, false);
		mSearchGuideViewStep4 = appInfView.inflate(R.layout.vch_menu_search_guide_step4, null, false);
		searchGuideContent1 = (LinearLayout) mSearchGuideViewStep1.findViewById(R.id.vch_search_guide_content_1);
		searchGuideContent2 = (LinearLayout) mSearchGuideViewStep2.findViewById(R.id.vch_search_guide_content_2);
		searchGuideContent3 = (LinearLayout) mSearchGuideViewStep3.findViewById(R.id.vch_search_guide_content_3);
		curSignalInput = (TextView) mSearchGuideViewStep1.findViewById(R.id.vch_cur_signal_input);
		curOperatorName = (TextView) mSearchGuideViewStep2.findViewById(R.id.vch_cur_operater);
		btnSearchGuideStep1Next = (Button) mSearchGuideViewStep1.findViewById(R.id.btnStep1Next);
		btnSearchGuideStep2Next = (Button) mSearchGuideViewStep2.findViewById(R.id.btnStep2Next);

		guide_search_step1 = (LinearLayout) mSearchGuideViewStep4.findViewById(R.id.warning_yes_no_dialog);
		guide_search_step2 = (LinearLayout) mSearchGuideViewStep4.findViewById(R.id.progressbar_cancel_dialog);
		btnSearchGuideYes = (Button) guide_search_step1.findViewById(R.id.btnYes);
		btnSearchGuideNo = (Button) guide_search_step1.findViewById(R.id.btnNo);
		btnSearchGuideCancel = (Button) guide_search_step2.findViewById(R.id.btnCancel);
		searchGuideTitle = (TextView) guide_search_step2.findViewById(R.id.title);
		mGuideSearchProgressBar = (ProgressBar) guide_search_step2.findViewById(R.id.progressBar);
		mGuideSearchParameter1 = (TextView) guide_search_step2.findViewById(R.id.Parameter1);
		mGuideSearchParameter2 = (TextView) guide_search_step2.findViewById(R.id.Parameter2);

		mSourceList = mSourceManager.getSourceList();
		mCurSourceIndex = mSourceManager.getCurSourceIndex();
		mLastSourceIndex = mCurSourceIndex;
		mSearchGuideSignalgroup = new VRadioGroup(mContext);
		if (null != mSourceList && mSourceList.size() > 0) {
			int size = mSourceList.size();
			for (int i = 0; i < size; i++) {
				VRadio vRadio = new VRadio(mSourceList.get(i).miSourceName);
				mSearchGuideSignalgroup.addVRadio(vRadio);
				if (i == mCurSourceIndex) {
					mSearchGuideSignalgroup.check(i);
				}
			}
		} else {
			Log.i(TAG, "EL--> the demondType list size =0");
		}

		mSearchGuideSignalgroup.setOnVRadioGroupListener(new OnVRadioGroupListener() {

			@Override
			public void onClick(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCheckedChange(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onKey(int arg0, KeyEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg0 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep1Item.requestfouse();
						return true;
					} else if ((arg0 == KeyEvent.KEYCODE_ENTER) || (arg0 == KeyEvent.KEYCODE_DPAD_CENTER)) {
						curSourceIndex = arg2;
						if (mCurSourceIndex == curSourceIndex) {
							// 当前信号源上按确认键，焦点移至Next上�?
							btnSearchGuideStep1Next.requestFocus();
						} else {
							mLastSourceIndex = mCurSourceIndex;
							mCurSourceIndex = curSourceIndex;
							mSearchGuideSignalgroup.check(mCurSourceIndex);
							mSearchGuideSignalgroup.refreshCheck(mCurSourceIndex);
							DtvProgram curDtvProgram = mDtvChannelManager.getCurProgram();
							if (null != curDtvProgram) {
								// 切换源之前将当前节目的节目号保存
								DtvConfigManager.getInstance().setValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())), String.valueOf(curDtvProgram.mProgramNum));
							}
							mSourceManager.setCurSource(mSourceManager.getSourceIDByIndex(curSourceIndex));
							mDtvChannelManager.setCurChannelType(mDtvChannelManager.getBootChannelType());
							// 读取保存的节目， 如果没有，则用startBoot的那个节目号
							String tmp = DtvConfigManager.getInstance().getValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())));
							if (tmp == null) {
								mDtvChannelManager.channelForceChangeByProgramNum(mDtvChannelManager.getBootChannelNum(), true);
							} else {
								mDtvChannelManager.channelForceChangeByProgramNum(Integer.valueOf(tmp), true);
							}
							curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
							if (!bSearchGuideFirst) {
								if ((mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH)) {
									mSearchGuideStep2Item.setLosefocuse(true);
								} else {
									mSearchGuideStep2Item.setLosefocuse(false);
								}
							}
							btnSearchGuideStep1Next.requestFocus();
						}
						/**信号源改变	YangLiu	2015-2-26*/
						return true;
					} else if (arg0 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep1Item.PullDown(false);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		searchGuideContent1.addView(mSearchGuideSignalgroup.getVRadioGroup());
		btnSearchGuideStep1Next.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						bSearchGuideFirst = false;
						mSearchGuideStep1Item.PullDown(false);
						if (mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH) {
							mSearchGuideStep4Item.setLosefocuse(false);
							mSearchGuideStep4Item.PullDown(true);
							btnSearchGuideYes.requestFocus();
						} else {
							curOperatorName.setText(mContext.getResources().getString(R.string.dtv_scan_setup_cur_operator, mDtvOperatorManager.getCurOperator().getOperatorName()));
							codeHasChanged = false;
							initCurDtvProvider();
							mSearchGuideStep2Item.setLosefocuse(false);
							mSearchGuideStep2Item.PullDown(true);
							btnSearchGuideStep2Next.requestFocus();
						}
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_DOWN) {
						if ((mSearchGuideStep2Item.isfouse()) || mSearchGuideStep4Item.isfouse()) {
							mSearchGuideStep1Item.PullDown(false);
						}
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep1Item.requestfouse();
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep1Item.PullDown(false);
					}
				}
				return false;
			}
		});

		btnSearchGuideStep2Next.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						mSearchGuideStep2Item.PullDown(false);
						if (codeHasChanged) {
							mSearchGuideStep3Item.setLosefocuse(false);
							mSearchGuideStep3Item.PullDown(true);
							mSearchGuideStep3Item.requestfouse();
							changeOperationByCode(tmpOperateCode);
							progressDiglog = new CommonProgressInfoDialog(mContext);
							progressDiglog.getWindow().setType(2003);
							progressDiglog.setDuration(1000000);
							progressDiglog.setButtonVisible(false);
							progressDiglog.setCancelable(false);
							progressDiglog.setMessage(mContext.getString(R.string.dtv_system_reboot));
							progressDiglog.show();
							DtvChannelManager.getInstance().reset();
							DtvConfigManager.getInstance().clearAll();
							DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OP_GUIDE, "true");
							MenuManager.getInstance().delAllScheduleEvents();
							Log.i(TAG, "codeHasChanged: " + codeHasChanged);
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Log.i(TAG, "powerManager.reboot 1111");
									PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
									powerManager.reboot("");
									Log.i(TAG, "powerManager.reboot 1112");
								}
							}, 5000);
							Log.i(TAG, "powerManager.reboot 1113");
							codeHasChanged = false;
						} else {
							mSearchGuideStep4Item.setLosefocuse(false);
							mSearchGuideStep4Item.PullDown(true);
							btnSearchGuideYes.requestFocus();
						}
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_DOWN) {
						if (mSearchGuideStep3Item.isfouse() || mSearchGuideStep4Item.isfouse()) {
							mSearchGuideStep2Item.PullDown(false);
						}
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep2Item.PullDown(false);
						curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
						mSearchGuideStepBackFlag = 0;
						mSearchGuideStep1Item.PullDown(true);
						btnSearchGuideStep1Next.requestFocus();
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep2Item.PullDown(false);
					}
					if (codeHasChanged) {
						codeHasChanged = false;
					}
				}
				return false;
			}
		});

		dtvprovider_selector_search_guide.setTextSize(28);// 28px(<==>21sp)
		dtvprovider_selector_search_guide.setFakeBoldText(false);
		dtvprovider_selector_search_guide.SetOnclickListener(new OnWheelOKListener() {

			@Override
			public void onOK(WheelView view, int code) {
				// TODO Auto-generated method stub
				Log.d("wheel", "code=====" + code);
				tmpOperateCode = code;
				if (code != mDtvOperatorManager.getCurOperatorCode()) {
					// changeOperationByCode(tmpOperateCode);
					codeHasChanged = true;
					btnSearchGuideStep2Next.requestFocus();
					Log.i(TAG, "changeOperationByCode tmpOperateCode:" + tmpOperateCode + ", codeHasChanged: " + codeHasChanged);
				}
			}
		});
		dtvprovider_selector_search_guide.setProvinceCyclic(true);
		dtvprovider_selector_search_guide.setProvinceOnkeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep2Item.PullDown(false);
						curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
						mSearchGuideStepBackFlag = 0;
						mSearchGuideStep1Item.PullDown(true);
						btnSearchGuideStep1Next.requestFocus();
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep2Item.PullDown(false);
					}
				}
				return false;
			}
		});
		dtvprovider_selector_search_guide.setProviderOnkeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep2Item.PullDown(false);
						curSignalInput.setText(mContext.getResources().getString(R.string.str_vch_cur_signal_input, mSourceManager.getCurSourceName()));
						mSearchGuideStepBackFlag = 0;
						mSearchGuideStep1Item.PullDown(true);
						btnSearchGuideStep1Next.requestFocus();
					}
				}
				return false;
			}
		});
		searchGuideContent2.addView(dtvprovider_selector_search_guide.getView());

		btnSearchGuideYes.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						// mSearchGuideStep4Item.PullDown(false);
						// new Handler().postDelayed(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// DirectEnterAutoSearch();
						// }
						// }, 500);
						mMainMenu.setMenukeyUseless(true);
						mscanType = scantype.DTV_ScanAutoExtra_SearchGuide;
						ShowScanMenuDetail();
						menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
						menuAutoScan.Scanready();
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep4Item.PullDown(false);
						if (mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH) {
							mSearchGuideStepBackFlag = 0;
							mSearchGuideStep1Item.PullDown(true);
							btnSearchGuideStep1Next.requestFocus();
						} else {
							mSearchGuideStep2Item.PullDown(true);
							btnSearchGuideStep2Next.requestFocus();
						}
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep4Item.PullDown(false);
					}
				}
				return false;
			}
		});

		btnSearchGuideNo.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						// mSearchGuideStep4Item.PullDown(false);
						// mSearchGuideStep4Item.requestfouse();
						setGuideScan(false);
						mscanType = scantype.DTV_ScanAutoExtra_SearchGuide;
						HideScanMenuDetail();
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep4Item.PullDown(false);
						if (mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH) {
							mSearchGuideStepBackFlag = 0;
							mSearchGuideStep1Item.PullDown(true);
							btnSearchGuideStep1Next.requestFocus();
						} else {
							mSearchGuideStep2Item.PullDown(true);
							btnSearchGuideStep2Next.requestFocus();
						}
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep4Item.PullDown(false);
					}
				}
				return false;
			}
		});

		btnSearchGuideCancel.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						mMainMenu.setMenukeyUseless(false);
						mscanType = scantype.DTV_ScanAutoExtra_SearchGuide;
						menuAutoScan.Scantermined();
						HideScanMenuDetail();
						return false;
					}
				}
				return true;
			}
		});

		mSearchGuideStep1Item.addPullDownItem(mSearchGuideViewStep1);
		mSearchGuideStep2Item.addPullDownItem(mSearchGuideViewStep2);
		mSearchGuideStep3Item.addPullDownItem(mSearchGuideViewStep3);
		mSearchGuideStep4Item.addPullDownItem(mSearchGuideViewStep4);
		mSearchGuideStep1Item.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					mSearchGuideStepBackFlag = 0;
					btnSearchGuideStep1Next.requestFocus();
				}
			}
		});

		mSearchGuideStep1Item.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						if (mSearchGuideStepBackFlag == 0) {
							mMainMenu.backToNextItem(mScanManage, mScanManage);
							itemSearchGuide.requestfouse();
						}
						mSearchGuideStepBackFlag++;
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mSearchGuideStep1Item.PullDown(false);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mSearchGuideStep2Item.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					btnSearchGuideStep2Next.requestFocus();
				}
			}
		});

		mSearchGuideStep2Item.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep2Item.PullDown(false);
						// new Handler().postDelayed(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						itemSearchGuide.requestfouse();
						// }
						// }, 500);
						return true;
					} else if ((arg1 == KeyEvent.KEYCODE_DPAD_UP) || (arg1 == KeyEvent.KEYCODE_DPAD_LEFT)) {
						mSearchGuideStep2Item.PullDown(false);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mSearchGuideStep3Item.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					return true;
				}
				return false;
			}
		});

		mSearchGuideStep4Item.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					btnSearchGuideYes.requestFocus();
				}
			}
		});

		mSearchGuideStep4Item.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mSearchGuideStep4Item.PullDown(false);
						guide_search_step1.setVisibility(View.VISIBLE);
						guide_search_step2.setVisibility(View.GONE);
						// new Handler().postDelayed(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						itemSearchGuide.requestfouse();
						// }
						// }, 500);
						return true;
					} else if ((arg1 == KeyEvent.KEYCODE_DPAD_UP) || (arg1 == KeyEvent.KEYCODE_DPAD_LEFT)) {
						mSearchGuideStep4Item.PullDown(false);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mSearchGuideListView.addListViewItem(mSearchGuideStep1Item);
		mSearchGuideListView.addListViewItem(mSearchGuideStep2Item);
		mSearchGuideListView.addListViewItem(mSearchGuideStep3Item);
		mSearchGuideListView.addListViewItem(mSearchGuideStep4Item);
	}

	/**
	 * 搜索管理——>高级——>信号接入视图
	 */
	private View initSignalInputView() {
		// TODO Auto-generated method stub
		mSignalInputView = appInfView.inflate(R.layout.vch_signal_input_layout, null, false);
		signalInputLayout1 = (LinearLayout) mSignalInputView.findViewById(R.id.layout1);
		signalInputLayout2 = (LinearLayout) mSignalInputView.findViewById(R.id.layout2);
		textView1 = (TextView) signalInputLayout2.findViewById(R.id.signal_input_title);
		btnSigalInputYes = (Button) signalInputLayout2.findViewById(R.id.yes);
		btnSigalInputNo = (Button) signalInputLayout2.findViewById(R.id.no);
		mSourceList = mSourceManager.getSourceList();
		mCurSourceIndex = mSourceManager.getCurSourceIndex();
		mLastSourceIndex = mCurSourceIndex;
		mSignalInputgroup = new VRadioGroup(mContext);
		if (null != mSourceList && mSourceList.size() > 0) {
			int size = mSourceList.size();
			for (int i = 0; i < size; i++) {
				VRadio vRadio = new VRadio(mSourceList.get(i).miSourceName);
				mSignalInputgroup.addVRadio(vRadio);
				if (i == mCurSourceIndex) {
					mSignalInputgroup.check(i);
				}
			}
		} else {
			Log.i(TAG, "EL--> the demondType list size =0");
		}

		mSignalInputgroup.setOnVRadioGroupListener(new OnVRadioGroupListener() {

			@Override
			public void onClick(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCheckedChange(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onKey(int arg0, KeyEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg0 == KeyEvent.KEYCODE_BACK) {
						signalInputLayout2.setVisibility(View.GONE);
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						mSignalInputItem.requestfouse();
						return true;
					} else if ((arg0 == KeyEvent.KEYCODE_ENTER) || (arg0 == KeyEvent.KEYCODE_DPAD_CENTER)) {
						curSourceIndex = arg2;
						if (mCurSourceIndex == curSourceIndex) {
							// 当前信号源上按确认键，则返回上一级菜单，现在返回键未做好，暂时保留该需求
							mMainMenu.backToNextItem(mScanManage, mScanManage);
							mSignalInputItem.requestfouse();
						} else {
							String string = mContext.getResources().getString(R.string.dtv_scansetting_change_source_normal_confirm, mSourceList.get(curSourceIndex).miSourceName);
							textView1.setText(string);
							signalInputLayout2.setVisibility(View.VISIBLE);
							signalInputLayout2.requestFocus();
							btnSigalInputYes.requestFocus();
						}
						return true;
					} else if (arg0 == KeyEvent.KEYCODE_DPAD_LEFT) {
						signalInputLayout2.setVisibility(View.GONE);
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1, int arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		signalInputLayout1.addView(mSignalInputgroup.getVRadioGroup());

		btnSigalInputYes.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						signalInputLayout2.setVisibility(View.GONE);
						signalInputLayout1.requestFocus();
						mLastSourceIndex = mCurSourceIndex;
						mCurSourceIndex = curSourceIndex;
						mSignalInputgroup.check(mCurSourceIndex);
						mSignalInputgroup.refreshCheck(mCurSourceIndex);
						mSignalInputItem.refreshSubhead(mSourceList.get(mCurSourceIndex).miSourceName);
						DtvProgram curDtvProgram = mDtvChannelManager.getCurProgram();
						if (null != curDtvProgram) {
							// 切换源之前将当前节目的节目号保存
							DtvConfigManager.getInstance().setValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())), String.valueOf(curDtvProgram.mProgramNum));
						}
						mSourceManager.setCurSource(mSourceManager.getSourceIDByIndex(curSourceIndex));
						mDtvChannelManager.setCurChannelType(mDtvChannelManager.getBootChannelType());
						// 读取保存的节目， 如果没有，则用startBoot的那个节目号
						String tmp = DtvConfigManager.getInstance().getValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())));
						if (tmp == null) {
							mDtvChannelManager.channelForceChangeByProgramNum(mDtvChannelManager.getBootChannelNum(), true);
						} else {
							mDtvChannelManager.channelForceChangeByProgramNum(Integer.valueOf(tmp), true);
						}
						if ((mSourceManager.getCurDemodeType() == ConstDemodType.DMB_TH)) {
							mOperatorItem.setLosefocuse(true);
						} else {
							mOperatorItem.setLosefocuse(false);
						}
						/**信号源改变	YangLiu	2015-2-26*/
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_UP) {
						signalInputLayout2.setVisibility(View.GONE);
						signalInputLayout1.requestFocus();
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						signalInputLayout2.setVisibility(View.GONE);
						signalInputLayout1.requestFocus();
					}
				}
				return false;
			}
		});

		btnSigalInputNo.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						signalInputLayout2.setVisibility(View.GONE);
						signalInputLayout1.requestFocus();
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						signalInputLayout2.setVisibility(View.GONE);
						signalInputLayout1.requestFocus();
					}
				}
				return false;
			}
		});

		return mSignalInputView;
	}

	/**
	 * 系统信息
	 */
	private void initModuleInfoMenu() {
		// TODO Auto-generated method stub
		String strNoInfo = mContext.getString(R.string.menu_soft_ware_no_info);
		String strCardType = mContext.getString(R.string.menu_soft_ware_card_type_unknown);
		String strCardStatus = mContext.getString(R.string.menu_soft_ware_card_status_exception);
		if (moduleInfoView != null) {
			moduleInfoView = null;
		}
		if (moduleInfoView == null) {
			moduleInfoView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.vch_module_info, null);
		}

		TextView mOperator = (TextView) moduleInfoView.findViewById(R.id.vch_operator);
		TextView mCardType = (TextView) moduleInfoView.findViewById(R.id.vch_card_type);
		TextView mCardStatus = (TextView) moduleInfoView.findViewById(R.id.vch_card_status);
		if (mSourceManager.getProductType() == ConstProductType.PRODUCT_T) {
			mOperator.setVisibility(View.GONE);
			mCardType.setVisibility(View.GONE);
			mCardStatus.setVisibility(View.GONE);
		} else {
			DtvOperator dtvOperator = DtvSoftWareInfoManager.getCurOperator();
			if (dtvOperator != null) {
				mOperator.setText(String.format(mContext.getString(R.string.vch_str_operator_info), dtvOperator.getOperatorName()));
			} else {
				mOperator.setText(String.format(mContext.getString(R.string.vch_str_operator_info), strNoInfo));
			}

			DtvCardStatus dtvCardStatus = DtvSoftWareInfoManager.getCardStatus();
			if (dtvCardStatus != null) {
				switch (dtvCardStatus.getCardType()) {
					case CardType.CARD_TYPE_CI:
						strCardType = mContext.getString(R.string.menu_soft_ware_card_type_ci);
						break;
					case CardType.CARD_TYPE_CA:
						strCardType = mContext.getString(R.string.menu_soft_ware_card_type_ca);
						break;
					case CardType.CARD_TYPE_CHECK:
					case CardType.CARD_TYPE_NO_CARD:
					default:
						break;
				}

				switch (dtvCardStatus.getCardStatus()) {
					case CardStatus.CARD_STATUS_OK:
						strCardStatus = mContext.getString(R.string.menu_soft_ware_card_status_ok);
						break;
					case CardStatus.CARD_STATUS_OUT:
						strCardStatus = mContext.getString(R.string.menu_soft_ware_card_status_out);
						break;
					case CardStatus.CARD_STATUS_INSERT:
					case CardStatus.CARD_STATUS_CHECKING:
					case CardStatus.CARD_STATUS_INVALID:
					default:
						break;
				}
			}
			mCardType.setText(String.format(mContext.getResources().getString(R.string.vch_str_card_type_info), strCardType));
			mCardStatus.setText(String.format(mContext.getResources().getString(R.string.vch_str_card_status_info), strCardStatus));
		}

		TextView mHardVer = (TextView) moduleInfoView.findViewById(R.id.vch_hardver);
		mHardVer.setText(String.format(mContext.getResources().getString(R.string.vch_str_hw_version), DtvSoftWareInfoManager.mHardVersion));

		TextView mAppVer = (TextView) moduleInfoView.findViewById(R.id.vch_appver);
		mAppVer.setText(String.format(mContext.getResources().getString(R.string.vch_str_app_version), DtvSoftWareInfoManager.mSoftwareVersion));

		TextView mReleaseDate = (TextView) moduleInfoView.findViewById(R.id.vch_release_date);
		mReleaseDate.setText(String.format(mContext.getResources().getString(R.string.vch_str_release_time), DtvSoftWareInfoManager.releaseTime));
		TextView mDriverVer = (TextView) moduleInfoView.findViewById(R.id.vch_dtv_version);
		DtvVersion dtvVersion = DtvSoftWareInfoManager.getDtvVersion();
		if (null != dtvVersion) {
			mDriverVer.setText(String.format(mContext.getResources().getString(R.string.vch_str_dtv_version),
					dtvVersion.getHardwareVersion() + "." + dtvVersion.getOpVersion() + "." + dtvVersion.getAPIMainVersion() + "." + dtvVersion.getAPISubVersion() + "." + dtvVersion.getMainVersion()
							+ "." + dtvVersion.getSubVersion() + "." + dtvVersion.getKOVersion() + ".r"));
			String buildTime = dtvVersion.getReleaseDateTime();
			if (null == buildTime || (-1 == buildTime.indexOf("<")) || (-1 == buildTime.lastIndexOf(":"))) {
				Log.e(TAG, "TvosService has died");
				buildTime = "<00:00:00:";
			}
			TextView mDate = (TextView) moduleInfoView.findViewById(R.id.vch_build_time);
			mDate.setText(String.format(mContext.getResources().getString(R.string.vch_str_build_time), buildTime.substring(buildTime.indexOf("<") + 1, buildTime.lastIndexOf(":"))));
		}
		Button btnBack = (Button) moduleInfoView.findViewById(R.id.button1);
		btnBack.setBackgroundResource(R.drawable.vch_item_default_bg);
		btnBack.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				// /////////////////////////////////////fengy
				// 2014-10-27////////////////////////////
				if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_enter) {
					// mMainMenu.jumpToNextItem(mMoreSetting, mModuleInfo);
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_left) {
					// mMoreSettingView.colsepulldown();
					mMainMenu.leftItemfouce();
					return true;
				} else if (GetKeyboardState(arg1, arg2, VListViewItem.Jump_To) == Keyboard_back) {
					// itemDTVSetting.requestfouse();
					mMainMenu.backToNextItem(mMoreSetting, mMoreSetting);
					itemDTVInfo.requestfouse();
					return true;
				}

				// /////////////////////////////////////////////////////////

				/*
				 * if(arg2.getAction() == KeyEvent.ACTION_DOWN){ Log.e(TAG,
				 * " module info 3333"); if(arg1 == KeyEvent.KEYCODE_BACK){
				 * mMainMenu.backToNextItem(mMoreSetting, mMoreSetting);
				 * itemDTVInfo.requestfouse(); return true; } }
				 */
				return false;
			}
		});
	}

	/**
	 * 搜索管理——>搜索向导——>搜索信号——>频道搜索信息
	 */
	private View initChannelInfoMenu(View channelInfoView) {
		// TODO Auto-generated method stub
		int channelNum = 0;
		String strChannelName = null;
		String strCurFreq = null;
		String mQamValue = null;
		String bandWidth = null;
		int mFrequencyValue = 0;
		int mSymbolRateValue = 0;
		int mErrorRateValue = 0;
		int mSignalStrengthValue = 0;
		int mSignalQualityValue = 0;
		int mAudioPIDValue = 0;
		int mVedioPIDValue = 0;
		int mAudioTypeValue = 0;
		int mVedioTypeValue = 0;
		String strAudioMode = null;
		String strAudioTrack = null;
		String strAencode = null;
		String strVencode = null;
		String strSignalStrength = null;
		String strSignalQuality = null;
		String strErrorRate = null;
		String[] audioModeStr = null;
		String[] audioTrackAllStr = null;
		String[] audioTrackStr = null;
		String[] mQamStr = null;
		String audioType = null;
		String vedioType = null;

		channelInfoView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.vch_channel_info, null);

		audioModeStr = getContext().getResources().getStringArray(R.array.menu_audio_mode);
		audioTrackAllStr = getContext().getResources().getStringArray(R.array.menu_audio_track);
		audioTrackStr = mDtvChannelManager.getAudioTrack(audioTrackAllStr);
		demodType = mSourceManager.getCurDemodeType();
		DtvProgram dtvCurProgram = mDtvChannelManager.getCurProgram();
		if (ConstDemodType.DVB_C == demodType) {
			mQamStr = getContext().getResources().getStringArray(R.array.menu_scan_modulation);
		} else {
			mQamStr = getContext().getResources().getStringArray(R.array.menu_scan_modulation_dmbt);
		}

		if (dtvCurProgram != null) {
			channelNum = dtvCurProgram.mProgramNum;
			strChannelName = dtvCurProgram.mProgramName;
			Log.i(TAG, "LL channelNum = " + channelNum);
			Log.i(TAG, "LL strChannelName = " + strChannelName);

			if (mDtvChannelManager.getAudioModeSel() < 0 || mDtvChannelManager.getAudioModeSel() >= audioModeStr.length) {
				strAudioMode = audioModeStr[0];
				Log.e(TAG, "ChannelInfoData updatePageData() err array outIndex mDtvChannelManager.getAudioModeSel() " + mDtvChannelManager.getAudioModeSel());
			} else {
				strAudioMode = audioModeStr[mDtvChannelManager.getAudioModeSel()];
			}

			if (mDtvChannelManager.getAudioTrackSelIndex() < 0 || mDtvChannelManager.getAudioTrackSelIndex() >= audioTrackStr.length) {
				strAudioTrack = audioTrackStr[0];
				Log.e(TAG, "ChannelInfoData updatePageData() err array outIndex getAudioTrackSelIndex() is " + mDtvChannelManager.getAudioTrackSelIndex());
			} else {
				strAudioTrack = audioTrackStr[mDtvChannelManager.getAudioTrackSelIndex()];
			}

			if (ConstDemodType.DVB_C == demodType) {
				DVBCCarrier carrier = mDtvChannelManager.getDVBCCurTunerInfo();
				if (carrier != null) {
					Log.i(TAG, "LL mQamValue = " + mQamValue + ",carrier.miQamMode = " + carrier.miQamMode);
					mFrequencyValue = carrier.miFrequencyK;
					mSymbolRateValue = carrier.miSymbolRateK;
					if (0 > carrier.miQamMode - 1 || mQamStr.length <= carrier.miQamMode - 1) {
						mQamValue = mQamStr[0];
					} else {
						mQamValue = mQamStr[carrier.miQamMode - 1];
					}
				} else {
					Log.e(TAG, "LL carrier == null");
					mFrequencyValue = 0;
					mSymbolRateValue = 0;
					mQamValue = "";
				}
			} else {
				DMBTHCarrier carrier = mSourceManager.getDMBTCarrierInfo();
				if (null != carrier) {
					mFrequencyValue = carrier.miFrequencyK;
					if (0 > carrier.miDTMBTHQamMode - 1 || mQamStr.length <= carrier.miDTMBTHQamMode - 1) {
						Log.e(TAG, "ChannelInfoData updatePageData() err array outIndex " + carrier.miDTMBTHQamMode);
						mQamValue = mQamStr[0];
					} else {
						mQamValue = mQamStr[carrier.miDTMBTHQamMode - 1];
					}
					bandWidth = "8 MHz";
				} else {
					mFrequencyValue = 0;
					mQamValue = "";
					bandWidth = "";
				}
			}

			DTVChannelDetailInfo channelDetailInfo = mDtvChannelManager.getDtvChannelDetailInfo(mDtvChannelManager.getCurPorgramServiceIndex());
			if (channelDetailInfo != null) {
				mAudioPIDValue = channelDetailInfo.miAudioPID;
				mVedioPIDValue = channelDetailInfo.miVedioPID;
				mAudioTypeValue = channelDetailInfo.miAudioType;
				mVedioTypeValue = channelDetailInfo.miVideoType;
			} else {
				Log.e(TAG, "LL channelDetailInfo == null");
				mAudioPIDValue = 0;
				mVedioPIDValue = 0;
				mAudioTypeValue = -1;
				mVedioTypeValue = -1;
			}
			DtvTunerStatus tunerStatus = mDtvChannelManager.getDtvTunerStatus();
			if (tunerStatus != null) {
				mErrorRateValue = tunerStatus.mBitErrorRate();
				mSignalStrengthValue = tunerStatus.getSignalLevel();
				mSignalQualityValue = tunerStatus.getSignalQuality();
			} else {
				Log.e(TAG, "LL tunerStatus == null");
				mErrorRateValue = 0;
				mSignalStrengthValue = 0;
				mSignalQualityValue = 0;
			}

			Log.i(TAG, "mErrorRateValue: " + mErrorRateValue);
			Log.i(TAG, "mSignalStrengthValue: " + mSignalStrengthValue);
			Log.i(TAG, "mSignalQualityValue: " + mSignalQualityValue);

			audioType = dtvStreamTypeConvert(mAudioTypeValue);
			vedioType = dtvStreamTypeConvert(mVedioTypeValue);

			if (ConstDemodType.DVB_C == demodType) {
				mFrequencyValue /= 1000;
			} else {
				mFrequencyValue /= 1000.0;
			}

			if (null != dtvCurProgram) {
				if (dtvCurProgram.isScrambled()) {
					strChannelName = strChannelName.concat("    " + getContext().getString(R.string.dtv_info_cryption));
				} else {
					strChannelName = strChannelName.concat("    " + getContext().getString(R.string.dtv_info_encryption));
				}
			}
		} else {
			Log.e(TAG, "LL dtvCurProgram == null");
			channelNum = 0;
			strChannelName = "";
			strAudioMode = "";
			strAudioTrack = "";
			mFrequencyValue = 0;
			mSymbolRateValue = 0;
			mAudioPIDValue = 0;
			mVedioPIDValue = 0;
			mAudioTypeValue = 0;
			mVedioTypeValue = 0;
			mErrorRateValue = 0;
			mSignalStrengthValue = 0;
			mSignalQualityValue = 0;
			bandWidth = "";
			Log.i(TAG, "LL channelNum = " + channelNum);
			Log.i(TAG, "LL strChannelName = " + strChannelName);
		}

		TextView mChannelNum = (TextView) channelInfoView.findViewById(R.id.vch_program_num);
		mChannelNum.setText(String.format(mContext.getResources().getString(R.string.vch_str_channel_num), channelNum));
		TextView mChannelName = (TextView) channelInfoView.findViewById(R.id.vch_program_name);
		mChannelName.setText(String.format(mContext.getResources().getString(R.string.vch_str_channel_name), strChannelName));
		TextView mFreq = (TextView) channelInfoView.findViewById(R.id.vch_cur_freq);
		mFreq.setText(String.format(mContext.getResources().getString(R.string.vch_str_channel_frequency), mFrequencyValue));
		TextView mAudio = (TextView) channelInfoView.findViewById(R.id.vch_audio_track);
		mAudio.setText(String.format(mContext.getResources().getString(R.string.vch_str_channel_audio), strAudioMode, strAudioTrack));
		TextView mAVencode = (TextView) channelInfoView.findViewById(R.id.vch_av_encode);
		mAVencode.setText(String.format(mContext.getResources().getString(R.string.vch_str_channel_audio_vedio_encode), audioType, vedioType));
		TextView mStrength = (TextView) channelInfoView.findViewById(R.id.vch_signal_strength_str);
		mStrength.setText(mContext.getResources().getString(R.string.vch_str_channel_signal_strength));
		ProgressBar mCurStrength = (ProgressBar) channelInfoView.findViewById(R.id.vch_signal_strength_value);
		mCurStrength.setProgress(mSignalStrengthValue);
		TextView mQuality = (TextView) channelInfoView.findViewById(R.id.vch_signal_quality_str);
		mQuality.setText(mContext.getResources().getString(R.string.vch_str_channel_signal_quality));
		ProgressBar mCurQuality = (ProgressBar) channelInfoView.findViewById(R.id.vch_signal_quality_value);
		mCurQuality.setProgress(mSignalQualityValue);
		TextView mErrorRate = (TextView) channelInfoView.findViewById(R.id.vch_error_rate);
		if ((mSourceManager.getProductType() == ConstProductType.PRODUCT_T) || (ConstDemodType.DMB_TH == demodType)) {
			mErrorRate.setVisibility(View.GONE);
		} else {
			mErrorRate.setText(String.format(mContext.getResources().getString(R.string.vch_str_channel_erro_rate), mErrorRateValue));
		}
		return channelInfoView;
	}

	/**
	 * 节目管理——>节目信息——>视频编码类型
	 */
	public String dtvStreamTypeConvert(int ri_StreamType) {
		String Ret = null;
		int baseType = ri_StreamType & 0x00ff;
		Log.i(TAG, "dtvStreamTypeConvert() ri_StreamType:<" + Integer.toHexString(ri_StreamType) + ">; " + ri_StreamType);
		switch (baseType) {
			case -1:
				Ret = null;
				break;
			/** Value: 0x00; Description:ITU-T | ISO/IEC Reserved **/
			case 0x00:
				Ret = "MPEG1";
				break;
			/** Value: 0x01; Description:ISO/IEC 11172 Video **/
			case 0x1:/** 编码格式<MPEG-I视频>; 类型<视频> */
			{
				Ret = "MPEG2";
			}
			break;

			/**
			 * Value: 0x02; Description:ITU-T Rec. H.262 | ISO/IEC 13818-2 Video or
			 * ISO/IEC 11172-2 constrained parameter video stream
			 **/
			case 0x2:/* 编码格式<MPEG-II视频>; 类型<视频> */ {
				Ret = "MPEG2";
			}
			break;

			/** Value: 0x03; Description:ISO/IEC 11172 Audio **/
			case 0x3:/* 编码格式<MPEG-I音频>; 类型<音频> */ {
				Ret = "MPEG1";
			}
			break;

			/** Value: 0x04; Description:ISO/IEC 13818-3 Audio **/
			case 0x4:/* 编码格式<MPEG-II音频>; 类型<音频> */ {
				Ret = "MPEG2";
			}
			break;

			/**
			 * Value: 0x06; Description:ITU-T Rec. H.222.0 | ISO/IEC 13818-1 PES
			 * packets containing private data
			 **/
			case 0x6:/* 编码格式<AC-3音频*>; 类型<音频> */ {
				Ret = "Dolby D";
			}
			break;

			case 0xF:/*
					* 编码格式<ISO/IEC 13818-7 Audio with ADTS transport
					* syntax(AACMpeg2/HEAACMpeg2)>; 类型<音频>
					*/ {
				Ret = "AAC"; // CH_AUDIO_CODE_AAC_ADTS;
			}
			break;

			case 0x10:/* 编码格式<MPEG-4 视频>; 类型<视频> */ {
				Ret = "MPEG4";// CH_VIDEO_CODE_MPEG4_ASP;
			}
			break;

			case 0x11:/*
					* 编码格式<ISO/IEC 14496-3 Audio with the LATM transport syntax as
					* defined in ISO/IEC 14496-3/Amd.1 (AACMpeg4/HEAACMpeg4)>;
					* 类型<音频>
					*/ {
				Ret = "AAC";// CH_AUDIO_CODE_AAC_ADTS;
			}
			break;

			case 0x1B:/* 编码格式<H.264视频>; 类型<视频> */ {
				Ret = "H264"; // CH_VIDEO_CODE_H264;
			}
			break;

			case 0x1C:/*
					* 编码格式<ISO/IEC 14496-3 Audio, without using any additional
					* transport syntax, such as DST, ALS and SLS>; 类型<音频>
					*/ {
				Ret = "DTS"; // CH_AUDIO_CODE_DTS;
			}
			break;

			case 0x42:/* 编码格式<国标AVS视频>; 类型<视频> */ {
				Ret = "AVS"; // CH_VIDEO_CODE_AVS;
			}
			break;

			case 0x65:/* 编码格式<DRA音频>; 类型<音频> */ {
				Ret = "DRA"; // CH_AUDIO_CODE_DRA;
				int result = (ri_StreamType >> 24) & 0xf;
				if (result != 0) {
					Ret = Ret.concat(result + ".0");
					Log.i(TAG, "ChanenlInfo DRA version : " + Ret);
				}
			}
			break;

			case 0x81:/* 编码格式<AC-3音频>; 类型<音频> */ {
				Ret = "Dolby D"; // CH_AUDIO_CODE_AC3;
			}
			break;

			case 0x87:/* 编码格式<EAC-3音频>; 类型<音频> */ {
				Ret = "Dolby D+";// CH_AUDIO_CODE_AC3_PLUS;
			}
			break;

			case 0xEA:/* 编码格式<VC1视频>; 类型<视频> */ {
				Ret = "VC1";// CH_VIDEO_CODE_VC1;
			}
			break;

			default: {
				Ret = "MPEG2";
			}
			break;
		}
		return Ret;
	}

	// private String[] getAllFreTable(int demoType) {
	// String[] freTable = null;
	// int index = 0;
	// int fre;
	// if(ConstDemodType.DVB_C == demoType){
	// freTable = new String[96];
	// fre = ConstScanParams.FREQUANCE_MIN_K_C;
	// for (; fre < 474000&& index < freTable.length; fre = fre +
	// ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
	// freTable[index] = String.valueOf(fre);
	// }
	// for (fre = 474000; fre <= ConstScanParams.FREQUANCE_MAX_K && index <
	// freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH,
	// index++) {
	// freTable[index] = String.valueOf(fre);
	// }
	// }else {
	// freTable = new String[57];
	// fre = ConstScanParams.FREQUANCE_MIN_K_T;
	// for (; fre < 70000&& index < freTable.length; fre = fre +
	// ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
	// freTable[index] = String.valueOf(fre);
	// }
	// freTable[index++] = String.valueOf(80000);
	// freTable[index++] = String.valueOf(88000);
	// for (fre = 171000 ; fre <= 219000&& index < freTable.length; fre = fre +
	// ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
	// freTable[index] = String.valueOf(fre);
	// }
	// for (fre = 474000; fre <= 562000 && index < freTable.length; fre = fre +
	// ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
	// freTable[index] = String.valueOf(fre);
	// }
	// for (fre = 610000; fre <= ConstScanParams.FREQUANCE_MAX_K && index <
	// freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH,
	// index++) {
	// freTable[index] = String.valueOf(fre);
	// }
	// }
	// return freTable;
	// }

	private int[] getAllFreTable(int demoType) {
		int[] freTable = null;
		int index = 0;
		int fre;
		if (ConstDemodType.DVB_C == demoType) {
			freTable = new int[96];
			fre = ConstScanParams.FREQUANCE_MIN_K_C;
			for (; fre < 474000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			for (fre = 474000; fre <= ConstScanParams.FREQUANCE_MAX_K && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
		} else {
			freTable = new int[57];
			fre = ConstScanParams.FREQUANCE_MIN_K_T;
			for (; fre < 70000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			freTable[index++] = 80000;
			freTable[index++] = 88000;
			for (fre = 171000; fre <= 219000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			for (fre = 474000; fre <= 562000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			for (fre = 610000; fre <= ConstScanParams.FREQUANCE_MAX_K && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
		}
		return freTable;
	}

	/**
	 * 搜索管理—>高级—>自定义搜索
	 */
	private void initCustomSearchView() {
		mCustomSearchView = new VListView(mContext);
		listSearchDvbc = new VListView(mContext);
		listSearchDmbt = new VListView(mContext);
		manualSearchDvbc = new VListView(mContext);
		manualSearchDmbt = new VListView(mContext);
		mListSearchItem = new VListViewItem(mContext, strAdvanced_Customer_ListScan, null, VListViewItem.Text_pure_pull);
		mListSearchView = appInfView.inflate(R.layout.list_search_warning_dialog, null, false);
		mListSearchItem.addPullDownItem(mListSearchView);
		list_search_step1 = (LinearLayout) mListSearchView.findViewById(R.id.warning_yes_no_dialog);
		list_search_step2 = (LinearLayout) mListSearchView.findViewById(R.id.progressbar_cancel_dialog);
		btnListSearchYes = (Button) list_search_step1.findViewById(R.id.yes);
		btnListSearchCancle = (Button) list_search_step2.findViewById(R.id.cancel);
		btnListSearchNo = (Button) list_search_step1.findViewById(R.id.no);
		mListSearchProgressBar = (ProgressBar) list_search_step2.findViewById(R.id.progressBar);
		mListSearchParameter1 = (TextView) list_search_step2.findViewById(R.id.Parameter1);
		mListSearchParameter2 = (TextView) list_search_step2.findViewById(R.id.Parameter2);

		btnListSearchYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("CH_ER_COLLECT",
						"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
								+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
								+ ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior) + ";item3="
								+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
								+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_ListScan));
				mMainMenu.setMenukeyUseless(true);
				mscanType = scantype.DTV_ScanList;
				ShowScanMenuDetail();
				menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanList);
				menuAutoScan.Scanready();
			}
		});
		btnListSearchYes.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// //////////////////////////////////////////cuixiaoyan
					// 2014-10-20 //////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							Log.d("CH_ER_COLLECT",
									"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
											+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
											+ ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior) + ";item3="
											+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
											+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_ListScan));
							mMainMenu.setMenukeyUseless(true);
							mscanType = scantype.DTV_ScanList;
							ShowScanMenuDetail();
							menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanList);
							menuAutoScan.Scanready();
							return true;
						case 235:// keyboard Volume Down
							mListSearchItem.PullDown(false);
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// ///////////////////////////////////////////////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mListSearchItem.PullDown(false);
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mListSearchItem.requestfouse();
						return true;
					}
				}
				return false;
			}
		});

		btnListSearchNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setGuideScan(false);
				mscanType = scantype.DTV_ScanList;
				HideScanMenuDetail();
			}
		});
		btnListSearchNo.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// //////////////////////////////cuixiaoyan 2014-10-20
					// //////////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							setGuideScan(false);
							mscanType = scantype.DTV_ScanList;
							HideScanMenuDetail();
							return true;
						case 235:// keyboard Volume Down
							mListSearchItem.PullDown(false);
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// /////////////////////////////////////////////////////////////////////////////////////
					if ((arg1 == KeyEvent.KEYCODE_DPAD_LEFT) || (arg1 == KeyEvent.KEYCODE_DPAD_DOWN)) {
						mListSearchItem.PullDown(false);
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mListSearchItem.requestfouse();
						return true;
					}
				}
				return false;
			}
		});

		btnListSearchCancle.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// ////////////////////////////////////////cuixiaoyan
					// 2014-10-20 /////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
							break;
						case 232:// keyboard Source
							mMainMenu.setMenukeyUseless(false);
							mscanType = scantype.DTV_ScanList;
							menuAutoScan.Scantermined();
							HideScanMenuDetail();
							return true;
						case 235:// keyboard Volume Down
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// //////////////////////////////////////////////////////////////////////////////////////
					if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
						mMainMenu.setMenukeyUseless(false);
						mscanType = scantype.DTV_ScanList;
						menuAutoScan.Scantermined();
						HideScanMenuDetail();
						return false;
					}
				}
				return true;
			}
		});
		mListSearchItem.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// ///////////////////////cuixiaoyan 2014-10-20
					// //////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 231:// keyboard Menu
						case 233:// keyboard Channel Up
						case 234:// keyboard Channel Down
						case 232:// keyboard Source
							break;
						case 235:// keyboard Volume Down
							mListSearchItem.PullDown(false);
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// ////////////////////////////////////////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						mListSearchItem.PullDown(false);
					} else if (arg1 == KeyEvent.KEYCODE_BACK) {
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						mCustomSearchItem.requestfouse();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		mListSearchItem.setOnPulldownListener(new OnPulldownListener() {

			@Override
			public void OnPulldown(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 1) {
					list_search_step1.setVisibility(View.VISIBLE);
					list_search_step2.setVisibility(View.GONE);
					list_search_step1.requestFocus();
					btnListSearchYes.requestFocus();
				}
			}
		});

		mUserSearchItem = new VListViewItem(mContext, strAdvanced_Customer_UserScan, " ", VListViewItem.pull_down);
		String strAdvanced_Customer_Freq = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_Freq);
		String tmp = null;
		int freValue = 0, symValue = 0, qamValue = 0;
		String strFreq = null;
		int curProgress = 0;
		if (ConstProductType.PRODUCT_T != mSourceManager.getProductType()) {
			itemMainFreqDvbc = new VListViewItem(mContext, strAdvanced_Customer_Freq, null, VListViewItem.SeekBar);
			mFrequencyTable = getAllFreTable(ConstDemodType.DVB_C);
			mFrequencyStr = new String[mFrequencyTable.length];
			for (int i = 0; i < mFrequencyTable.length; i++) {
				int tmpfreq = (mFrequencyTable[i]) / 1000;
				mFrequencyStr[i] = String.valueOf(tmpfreq);
			}
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
			freValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
			strFreq = String.valueOf(freValue);
			Log.i(TAG, "1111 curProgress: " + curProgress);
			for (int i = 0; i < mFrequencyTable.length; i++) {
				Log.i(TAG, "strFreq: " + strFreq + "mFrequencyTable[" + i + "]: " + mFrequencyTable[i]);
				if (freValue == mFrequencyTable[i]) {
					curProgress = i;
					Log.i(TAG, "1122 curProgress: " + curProgress);
					break;
				}
			}
			VSeekbar seekbar1 = new VSeekbar(strAdvanced_Customer_Freq, curProgress, (mFrequencyStr.length - 1), mFrequencyStr);
			itemMainFreqDvbc.addSeekBarItem(seekbar1);
			itemMainFreqDvbc.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					int temp = mFrequencyTable[arg0];
					int curValue = (temp > 10000) ? temp : temp * 1000;
					DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_FREQ, String.valueOf(curValue));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemMainFreqDvbc.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mUserSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			itemSymbolRate = new VListViewItem(mContext, strAdvanced_Customer_Symbol, null, VListViewItem.SeekBar);
			mSymbolRateStr = getContext().getResources().getStringArray(R.array.menu_scan_symbol_rate);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_SYM);
			symValue = tmp == null ? MainMenuReceiver.getIndexByItem(mSymbolRateStr, String.valueOf(mOperatorManager.getOPMainTunerInfo().getSymbolRate())) : MainMenuReceiver.getIndexByItem(
					mSymbolRateStr, tmp);

			VSeekbar vSeekbar3 = new VSeekbar(strAdvanced_Customer_Symbol, symValue, (mSymbolRateStr.length - 1), mSymbolRateStr);
			itemSymbolRate.addSeekBarItem(vSeekbar3);
			itemSymbolRate.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_SYM, mSymbolRateStr[arg0]);
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemSymbolRate.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mUserSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			String strAdvanced_Customer_Qam = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_Qam);
			itemQam = new VListViewItem(mContext, strAdvanced_Customer_Qam, null, VListViewItem.SeekBar);
			mQamStr = getContext().getResources().getStringArray(R.array.menu_scan_modulation);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
			qamValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getQamMode() - 1 : Integer.valueOf(tmp);
			VSeekbar vSeekbar4 = new VSeekbar(strAdvanced_Customer_Qam, qamValue, (mQamStr.length - 1), mQamStr);
			itemQam.addSeekBarItem(vSeekbar4);
			itemQam.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_QAM, String.valueOf(arg0));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemQam.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mUserSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			itemUserSearch = new VListViewItem(mContext, strAdvanced_Customer_Scan, null, VListViewItem.Text_pure_pull);
			mUserSearchView = appInfView.inflate(R.layout.user_search_warning_dialog, null, false);
			itemUserSearch.addPullDownItem(mUserSearchView);
			user_search_step1 = (LinearLayout) mUserSearchView.findViewById(R.id.warning_yes_no_dialog);
			user_search_step2 = (LinearLayout) mUserSearchView.findViewById(R.id.progressbar_cancel_dialog);
			btnUserSearchYes = (Button) user_search_step1.findViewById(R.id.yes);
			btnUserSearchCancle = (Button) user_search_step2.findViewById(R.id.cancel);
			btnUserSearchNo = (Button) user_search_step1.findViewById(R.id.no);
			mUserSearchProgressBar = (ProgressBar) user_search_step2.findViewById(R.id.progressBar);
			mUserSearchParameter1 = (TextView) user_search_step2.findViewById(R.id.Parameter1);
			mUserSearchParameter2 = (TextView) user_search_step2.findViewById(R.id.Parameter2);

			btnUserSearchYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
									+ ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior) + ";item3="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
					mMainMenu.setMenukeyUseless(true);
					mscanType = scantype.DTV_ScanAutoExtra;
					ShowScanMenuDetail();
					menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);// DTV_ScanAutoExtra
					menuAutoScan.Scanready();
				}
			});
			btnUserSearchYes.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////cuixiaoyan 2014-10-20
						// ////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								Log.d("CH_ER_COLLECT",
										"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:"
												+ mContext.getResources().getString(R.string.collect2) + "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
												+ mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager) + ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior)
												+ ";item3=" + mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
												+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
								mMainMenu.setMenukeyUseless(true);
								mscanType = scantype.DTV_ScanAutoExtra;
								ShowScanMenuDetail();
								menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);// DTV_ScanAutoExtra
								menuAutoScan.Scanready();
								return true;
							case 235:// keyboard Volume Down
								itemUserSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								break;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemUserSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemUserSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnUserSearchNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setGuideScan(false);
					mscanType = scantype.DTV_ScanAutoExtra;
					HideScanMenuDetail();
				}
			});
			btnUserSearchNo.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// /////////////////////////////////////cuixiaoyan
						// 2014-10-20 ///////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								setGuideScan(false);
								mscanType = scantype.DTV_ScanAutoExtra;
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								itemUserSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								break;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemUserSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemUserSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnUserSearchCancle.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ///////////////////////////////////cuixiaoyan
						// 2014-10-20
						// ///////////////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								mMainMenu.setMenukeyUseless(false);
								mscanType = scantype.DTV_ScanAutoExtra;
								menuAutoScan.Scantermined();
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////////////////////
						if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
							mMainMenu.setMenukeyUseless(false);
							mscanType = scantype.DTV_ScanAutoExtra;
							menuAutoScan.Scantermined();
							HideScanMenuDetail();
							return false;
						}
					}
					return true;
				}
			});

			itemUserSearch.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemUserSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_DPAD_UP) {
							itemUserSearch.PullDown(false);
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemUserSearch.PullDown(false);
							mUserSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			itemUserSearch.setOnPulldownListener(new OnPulldownListener() {

				@Override
				public void OnPulldown(int arg0) {
					// TODO Auto-generated method stub
					if (arg0 == 1) {
						user_search_step1.setVisibility(View.VISIBLE);
						user_search_step2.setVisibility(View.GONE);
						user_search_step1.requestFocus();
						btnUserSearchYes.requestFocus();
					}
				}
			});

			listSearchDvbc.addListViewItem(itemMainFreqDvbc);
			listSearchDvbc.addListViewItem(itemSymbolRate);
			listSearchDvbc.addListViewItem(itemQam);
			listSearchDvbc.addListViewItem(itemUserSearch);
		}

		if (ConstProductType.PRODUCT_C != mSourceManager.getProductType()) {
			itemMainFreqDmbt = new VListViewItem(mContext, strAdvanced_Customer_Freq, null, VListViewItem.SeekBar);
			mFrequencyDtmbTable = getAllFreTable(ConstDemodType.DMB_TH);
			mDmbtFrequencyStr = new String[mFrequencyDtmbTable.length];
			for (int i = 0; i < mFrequencyDtmbTable.length; i++) {
				if (mFrequencyDtmbTable[i] < 70000) {
					mDmbtFrequencyStr[i] = String.valueOf(mFrequencyDtmbTable[i] / 1000.0);
				} else {
					mDmbtFrequencyStr[i] = String.valueOf(mFrequencyDtmbTable[i] / 1000);
				}
			}
			DtvTunerInfo tunerInfo = dtvInterface.getDBMTTunerInfo();
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);
			int low_fre = tmp == null ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp);
			strFreq = String.valueOf(low_fre);
			Log.i(TAG, "1111 curProgress: " + curProgress);
			for (int i = 0; i < mFrequencyDtmbTable.length; i++) {
				Log.i(TAG, "strFreq: " + strFreq + "mFrequencyDtmbTable[" + i + "]: " + mFrequencyDtmbTable[i]);
				if (low_fre == mFrequencyDtmbTable[i]) {
					curProgress = i;
					Log.i(TAG, "1122 curProgress: " + curProgress);
					break;
				}
			}
			VSeekbar seekbar2 = new VSeekbar(strAdvanced_Customer_Freq, curProgress, (mDmbtFrequencyStr.length - 1), mDmbtFrequencyStr);
			itemMainFreqDmbt.addSeekBarItem(seekbar2);
			itemMainFreqDmbt.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					// float curValue =
					// Float.valueOf(mDmbtFrequencyStr[arg0])*1000;
					int temp = Integer.valueOf(mFrequencyDtmbTable[arg0]);
					int curValue = (temp > 10000) ? temp : temp * 100;
					DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ, String.valueOf(curValue));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemMainFreqDmbt.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////cuixiaoyan 2014-10-20
						// ////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 235:// keyboard Volume Down
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mUserSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			// dmbt
			itemUserDmbtSearch = new VListViewItem(mContext, strAdvanced_Customer_Scan, null, VListViewItem.Text_pure_pull);
			mUserDmbtSearchView = appInfView.inflate(R.layout.user_search_warning_dialog, null, false);
			itemUserDmbtSearch.addPullDownItem(mUserDmbtSearchView);
			user_dmbt_search_step1 = (LinearLayout) mUserDmbtSearchView.findViewById(R.id.warning_yes_no_dialog);
			user_dmbt_search_step2 = (LinearLayout) mUserDmbtSearchView.findViewById(R.id.progressbar_cancel_dialog);
			btnUserDmbtSearchYes = (Button) user_dmbt_search_step1.findViewById(R.id.yes);
			btnUserDmbtSearchCancle = (Button) user_dmbt_search_step2.findViewById(R.id.cancel);
			btnUserDmbtSearchNo = (Button) user_dmbt_search_step1.findViewById(R.id.no);
			mUserDmbtSearchProgressBar = (ProgressBar) user_dmbt_search_step2.findViewById(R.id.progressBar);
			mUserDmbtSearchParameter1 = (TextView) user_dmbt_search_step2.findViewById(R.id.Parameter1);
			mUserDmbtSearchParameter2 = (TextView) user_dmbt_search_step2.findViewById(R.id.Parameter2);

			btnUserDmbtSearchYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
									+ ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior) + ";item3="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
					mMainMenu.setMenukeyUseless(true);
					mscanType = scantype.DTV_ScanAutoExtra_Dmbt;
					ShowScanMenuDetail();
					menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);// DTV_ScanAutoExtra
					menuAutoScan.Scanready();
				}
			});

			btnUserDmbtSearchYes.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////cuixiaoyan 2014-10-20
						// ///////////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								Log.d("CH_ER_COLLECT",
										"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:"
												+ mContext.getResources().getString(R.string.collect2) + "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
												+ mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager) + ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior)
												+ ";item3=" + mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
												+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
								mMainMenu.setMenukeyUseless(true);
								mscanType = scantype.DTV_ScanAutoExtra_Dmbt;
								ShowScanMenuDetail();
								menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);// DTV_ScanAutoExtra
								menuAutoScan.Scanready();
								return true;
							case 235:// keyboard Volume Down
								itemUserDmbtSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ///////////////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemUserDmbtSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemUserDmbtSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnUserDmbtSearchNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setGuideScan(false);
					mscanType = scantype.DTV_ScanAutoExtra_Dmbt;
					HideScanMenuDetail();
				}
			});
			btnUserDmbtSearchNo.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////////cuixiaoyan
						// 2014-10-20 ////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								setGuideScan(false);
								mscanType = scantype.DTV_ScanAutoExtra_Dmbt;
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								itemUserDmbtSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// //////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemUserDmbtSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemUserDmbtSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnUserDmbtSearchCancle.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ///////////////////////////////////////cuixiaoyan
						// 2014-10-20 /////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								mMainMenu.setMenukeyUseless(false);
								mscanType = scantype.DTV_ScanAutoExtra_Dmbt;
								menuAutoScan.Scantermined();
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// /////////////////////////////////////////////////////////////////////////////////////////////
						if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
							mMainMenu.setMenukeyUseless(false);
							mscanType = scantype.DTV_ScanAutoExtra_Dmbt;
							menuAutoScan.Scantermined();
							HideScanMenuDetail();
							return false;
						}
					}
					return true;
				}
			});

			itemUserDmbtSearch.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////////cuixiaoyan
						// 2014-10-20 //////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 235:// keyboard Volume Down
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemUserDmbtSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_DPAD_UP) {
							itemUserDmbtSearch.PullDown(false);
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemUserDmbtSearch.PullDown(false);
							mUserSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			itemUserDmbtSearch.setOnPulldownListener(new OnPulldownListener() {

				@Override
				public void OnPulldown(int arg0) {
					// TODO Auto-generated method stub
					if (arg0 == 1) {
						user_dmbt_search_step1.setVisibility(View.VISIBLE);
						user_dmbt_search_step2.setVisibility(View.GONE);
						user_dmbt_search_step1.requestFocus();
						btnUserDmbtSearchYes.requestFocus();
					}
				}
			});

			listSearchDmbt.addListViewItem(itemMainFreqDmbt);
			listSearchDmbt.addListViewItem(itemUserDmbtSearch);
		}

		mUserSearchItem.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				int curProductType = mSourceManager.getProductType();
				if (ConstProductType.PRODUCT_C == curProductType) {
					Log.i(TAG, "cuixy search setting productType: PRODUCT_C ");
					return listSearchDvbc.getView();
				} else if (ConstProductType.PRODUCT_T == curProductType) {
					Log.i(TAG, "cuixy search setting productType: PRODUCT_T ");
					return listSearchDmbt.getView();
				} else {
					Log.i(TAG, "cuixy search setting productType: PRODUCT_C_T ");
					demoType = mSourceManager.getCurDemodeType();
					if (ConstDemodType.DVB_C == demoType) {
						Log.i(TAG, "cuixy search setting demoType: DVB_C ");
						return listSearchDvbc.getView();
					} else if (ConstDemodType.DMB_TH == demoType) {
						Log.i(TAG, "cuixy search setting demoType: DMB_TH ");
						return listSearchDmbt.getView();
					}
					return null;
				}
			}
		});

		mUserSearchItem.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// ///////////////////////////////////////cuixiaoyan
					// 2014-10-20 /////////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							mCustomSearchView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// /////////////////////////////////////////////////////////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						mCustomSearchItem.requestfouse();
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_L) {
						mUserSearchItem.PullDown(false);
					}
					/**
					 * add by YangLiu 修改DTV出厂设置后显示频点与实际搜索频点不一致问题 2014-12-08
					 */
					else if (arg1 == KeyEvent.KEYCODE_DPAD_CENTER) {
						// C下.............
						if (ConstProductType.PRODUCT_T != mSourceManager.getProductType()) {

							/* 再次读取频点 */
							String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
							int freValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
							for (int i = 0; i < mFrequencyTable.length; i++) {
								if (freValue == mFrequencyTable[i]) {
									itemMainFreqDvbc.refreshSeekbar(i);
									break;
								}
							}

							/* 再次读取符号率 */
							tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_SYM);
							int symValue = tmp == null ? MainMenuReceiver.getIndexByItem(mSymbolRateStr, String.valueOf(mOperatorManager.getOPMainTunerInfo().getSymbolRate())) : MainMenuReceiver
									.getIndexByItem(mSymbolRateStr, tmp);
							itemSymbolRate.refreshSeekbar(symValue);

							/* 再次读取QAM */
							tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
							int qamValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getQamMode() - 1 : Integer.valueOf(tmp);
							itemQam.refreshSeekbar(qamValue);
						}

						// T下...........
						if (ConstProductType.PRODUCT_C != mSourceManager.getProductType()) {

							/* 再次读取频点 */
							DtvTunerInfo tunerInfo = dtvInterface.getDBMTTunerInfo();
							String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);
							int low_fre = tmp == null ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp);
							for (int i = 0; i < mFrequencyDtmbTable.length; i++) {
								if (low_fre == mFrequencyDtmbTable[i]) {
									itemMainFreqDmbt.refreshSeekbar(i);
									break;
								}
							}
						}
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mManualSearchItem = new VListViewItem(mContext, strAdvanced_Customer_ManualScan, " ", VListViewItem.pull_down);
		String strAdvanced_Manual_StartFreq = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Manual_StartFreq);
		String strAdvanced_Manual_EndFreq = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Manual_EndFreq);
		// int freManualStartValue = 0, freManualEndValue = 0;
		String strStartFreq = null;
		String strEndFreq = null;
		int curStartProgress = 0, curEndFreqProgress = 0;
		Log.d(TAG, "cuixy, getProductType: " + mSourceManager.getProductType());
		if (ConstProductType.PRODUCT_T != mSourceManager.getProductType()) {
			itemStartFreqDvbc = new VListViewItem(mContext, strAdvanced_Manual_StartFreq, null, VListViewItem.SeekBar);
			mFrequencyTable = getAllFreTable(ConstDemodType.DVB_C);
			mFrequencyStr = new String[mFrequencyTable.length];
			for (int i = 0; i < mFrequencyTable.length; i++) {
				int tmpfreq = (mFrequencyTable[i]) / 1000;
				mFrequencyStr[i] = String.valueOf(tmpfreq);
			}
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
			freManualStartValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
			strStartFreq = String.valueOf(freManualStartValue);
			Log.i(TAG, "1111 curStartProgress: " + curStartProgress);
			for (int i = 0; i < mFrequencyTable.length; i++) {
				Log.i(TAG, "strStartFreq: " + strStartFreq + "mFrequencyTable[" + i + "]: " + mFrequencyTable[i]);
				if (freManualStartValue == mFrequencyTable[i]) {
					curStartProgress = i;
					Log.i(TAG, "1122 curStartProgress: " + curStartProgress);
					break;
				}
			}

			seekbar11 = new VSeekbar(strAdvanced_Manual_StartFreq, curStartProgress, (mFrequencyStr.length - 1), mFrequencyStr);
			itemStartFreqDvbc.addSeekBarItem(seekbar11);
			itemStartFreqDvbc.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					int temp = mFrequencyTable[arg0];
					int curValue = (temp > 10000) ? temp : temp * 1000;
					freManualStartValue = curValue;
					if (freManualStartValue > freManualEndValue) {
						freManualEndValue = freManualStartValue;
						itemEndFreqDvbc.refreshSeekbar(arg0);
					}

					// DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_FREQ,
					// String.valueOf(curValue));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemStartFreqDvbc.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			itemEndFreqDvbc = new VListViewItem(mContext, strAdvanced_Manual_EndFreq, null, VListViewItem.SeekBar);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
			freManualEndValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
			strEndFreq = String.valueOf(freManualEndValue);
			Log.i(TAG, "1111 curEndFreqProgress: " + curEndFreqProgress);
			for (int i = 0; i < mFrequencyTable.length; i++) {
				Log.i(TAG, "strEndFreq: " + strEndFreq + "mFrequencyTable[" + i + "]: " + mFrequencyTable[i]);
				if (freManualEndValue == mFrequencyTable[i]) {
					curEndFreqProgress = i;
					Log.i(TAG, "1122 curEndFreqProgress: " + curEndFreqProgress);
					break;
				}
			}
			seekbar12 = new VSeekbar(strAdvanced_Manual_EndFreq, curEndFreqProgress, (mFrequencyStr.length - 1), mFrequencyStr);
			itemEndFreqDvbc.addSeekBarItem(seekbar12);
			itemEndFreqDvbc.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					int temp = mFrequencyTable[arg0];
					int curValue = (temp > 10000) ? temp : temp * 1000;
					freManualEndValue = curValue;
					if (freManualStartValue > freManualEndValue) {
						freManualStartValue = freManualEndValue;
						itemStartFreqDvbc.refreshSeekbar(arg0);
					}
					// DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_FREQ,
					// String.valueOf(curValue));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemEndFreqDvbc.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			itemManualSearchSymbolRate = new VListViewItem(mContext, strAdvanced_Customer_Symbol, null, VListViewItem.SeekBar);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_SYM);
			symManualValue = tmp == null ? MainMenuReceiver.getIndexByItem(mSymbolRateStr, String.valueOf(mOperatorManager.getOPMainTunerInfo().getSymbolRate())) : MainMenuReceiver.getIndexByItem(
					mSymbolRateStr, tmp);
			strSymbolManual = mSymbolRateStr[symManualValue];

			VSeekbar vSeekbar13 = new VSeekbar(strAdvanced_Customer_Symbol, symManualValue, (mSymbolRateStr.length - 1), mSymbolRateStr);
			itemManualSearchSymbolRate.addSeekBarItem(vSeekbar13);
			itemManualSearchSymbolRate.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					strSymbolManual = mSymbolRateStr[arg0];
					// DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_SYM,
					// mSymbolRateStr[arg0]);
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemManualSearchSymbolRate.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			String strAdvanced_Customer_Qam = mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_Qam);
			itemManualSearchQam = new VListViewItem(mContext, strAdvanced_Customer_Qam, null, VListViewItem.SeekBar);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
			qamManualValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getQamMode() - 1 : Integer.valueOf(tmp);
			VSeekbar vSeekbar14 = new VSeekbar(strAdvanced_Customer_Qam, qamManualValue, (mQamStr.length - 1), mQamStr);
			itemManualSearchQam.addSeekBarItem(vSeekbar14);
			itemManualSearchQam.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					qamManualValue = arg0;
					// DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_QAM,
					// String.valueOf(arg0));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemManualSearchQam.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			itemManualSearch = new VListViewItem(mContext, strAdvanced_Customer_Scan, null, VListViewItem.Text_pure_pull);
			mManualSearchView = appInfView.inflate(R.layout.manual_search_warning_dialog, null, false);
			itemManualSearch.addPullDownItem(mManualSearchView);
			manual_search_step1 = (LinearLayout) mManualSearchView.findViewById(R.id.warning_yes_no_dialog);
			manual_search_step2 = (LinearLayout) mManualSearchView.findViewById(R.id.progressbar_cancel_dialog);
			btnManualSearchYes = (Button) manual_search_step1.findViewById(R.id.yes);
			btnManualSearchCancle = (Button) manual_search_step2.findViewById(R.id.cancel);
			btnManualSearchNo = (Button) manual_search_step1.findViewById(R.id.no);
			mManualSearchProgressBar = (ProgressBar) manual_search_step2.findViewById(R.id.progressBar);
			mManualSearchParameter1 = (TextView) manual_search_step2.findViewById(R.id.Parameter1);
			mManualSearchParameter2 = (TextView) manual_search_step2.findViewById(R.id.Parameter2);

			btnManualSearchYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
									+ ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior) + ";item3="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_ManualScan));
					mMainMenu.setMenukeyUseless(true);
					mscanType = scantype.DTV_ScanMaunal;
					ShowScanMenuDetail();
					menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanMaunal);
					menuAutoScan.manualScanInit(freManualStartValue, freManualEndValue, strSymbolManual, qamManualValue);
					menuAutoScan.Scanready();
				}
			});
			btnManualSearchYes.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////cuixiaoyan 2014-10-20
						// ////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								Log.d("CH_ER_COLLECT",
										"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:"
												+ mContext.getResources().getString(R.string.collect2) + "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
												+ mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager) + ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior)
												+ ";item3=" + mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
												+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
								mMainMenu.setMenukeyUseless(true);
								mscanType = scantype.DTV_ScanMaunal;
								ShowScanMenuDetail();
								menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanMaunal);
								menuAutoScan.manualScanInit(freManualStartValue, freManualEndValue, strSymbolManual, qamManualValue);
								menuAutoScan.Scanready();
								return true;
							case 235:// keyboard Volume Down
								itemManualSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								break;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemManualSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemManualSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnManualSearchNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setGuideScan(false);
					mscanType = scantype.DTV_ScanMaunal;
					HideScanMenuDetail();
				}
			});
			btnManualSearchNo.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// /////////////////////////////////////cuixiaoyan
						// 2014-10-20 ///////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								setGuideScan(false);
								mscanType = scantype.DTV_ScanMaunal;
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								itemManualSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								break;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemManualSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_DPAD_DOWN) {
							itemManualSearch.PullDown(false);
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemManualSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnManualSearchCancle.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ///////////////////////////////////cuixiaoyan
						// 2014-10-20
						// ///////////////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								mMainMenu.setMenukeyUseless(false);
								mscanType = scantype.DTV_ScanMaunal;
								menuAutoScan.Scantermined();
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////////////////////
						if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
							mMainMenu.setMenukeyUseless(false);
							mscanType = scantype.DTV_ScanMaunal;
							menuAutoScan.Scantermined();
							HideScanMenuDetail();
							return false;
						}
					}
					return true;
				}
			});

			itemManualSearch.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemManualSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_DPAD_UP) {
							itemManualSearch.PullDown(false);
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemManualSearch.PullDown(false);
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			itemManualSearch.setOnPulldownListener(new OnPulldownListener() {

				@Override
				public void OnPulldown(int arg0) {
					// TODO Auto-generated method stub
					if (arg0 == 1) {
						manual_search_step1.setVisibility(View.VISIBLE);
						manual_search_step2.setVisibility(View.GONE);
						manual_search_step1.requestFocus();
						btnManualSearchYes.requestFocus();
					}
				}
			});

			manualSearchDvbc.addListViewItem(itemStartFreqDvbc);
			manualSearchDvbc.addListViewItem(itemEndFreqDvbc);
			manualSearchDvbc.addListViewItem(itemManualSearchSymbolRate);
			manualSearchDvbc.addListViewItem(itemManualSearchQam);
			manualSearchDvbc.addListViewItem(itemManualSearch);
		}

		if (ConstProductType.PRODUCT_C != mSourceManager.getProductType()) {
			itemStartFreqDmbt = new VListViewItem(mContext, strAdvanced_Customer_Freq, null, VListViewItem.SeekBar);
			mFrequencyDtmbTable = getAllFreTable(ConstDemodType.DMB_TH);
			mDmbtFrequencyStr = new String[mFrequencyDtmbTable.length];
			for (int i = 0; i < mFrequencyDtmbTable.length; i++) {
				if (mFrequencyDtmbTable[i] < 70000) {
					mDmbtFrequencyStr[i] = String.valueOf(mFrequencyDtmbTable[i] / 1000.0);
				} else {
					mDmbtFrequencyStr[i] = String.valueOf(mFrequencyDtmbTable[i] / 1000);
				}
			}
			DtvTunerInfo tunerInfo = dtvInterface.getDBMTTunerInfo();
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);
			low_fre_manual = tmp == null ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp);
			strFreq = String.valueOf(low_fre_manual);
			Log.i(TAG, "1111 curProgress: " + curProgress);
			for (int i = 0; i < mFrequencyDtmbTable.length; i++) {
				Log.i(TAG, "strFreq: " + strFreq + "mFrequencyDtmbTable[" + i + "]: " + mFrequencyDtmbTable[i]);
				if (low_fre_manual == mFrequencyDtmbTable[i]) {
					curProgress = i;
					Log.i(TAG, "1122 curProgress: " + curProgress);
					break;
				}
			}
			VSeekbar seekbar21 = new VSeekbar(strAdvanced_Customer_Freq, curProgress, (mDmbtFrequencyStr.length - 1), mDmbtFrequencyStr);
			itemStartFreqDmbt.addSeekBarItem(seekbar21);
			itemStartFreqDmbt.setOnClickListener(new OnItemViewClickListener() {

				@Override
				public void onClickItemView(int arg0) {
					// TODO Auto-generated method stub
					// float curValue =
					// Float.valueOf(mDmbtFrequencyStr[arg0])*1000;
					int temp = Integer.valueOf(mFrequencyDtmbTable[arg0]);
					int curValue = (temp > 10000) ? temp : temp * 100;
					low_fre_manual = curValue;
					// DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ,
					// String.valueOf(curValue));
				}

				@Override
				public View getView() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			itemStartFreqDmbt.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////cuixiaoyan 2014-10-20
						// ////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 235:// keyboard Volume Down
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			// dmbt
			itemManualDmbtSearch = new VListViewItem(mContext, strAdvanced_Customer_Scan, null, VListViewItem.Text_pure_pull);
			mManualDmbtSearchView = appInfView.inflate(R.layout.manual_search_warning_dialog, null, false);
			itemManualDmbtSearch.addPullDownItem(mManualDmbtSearchView);
			manual_dmbt_search_step1 = (LinearLayout) mManualDmbtSearchView.findViewById(R.id.warning_yes_no_dialog);
			manual_dmbt_search_step2 = (LinearLayout) mManualDmbtSearchView.findViewById(R.id.progressbar_cancel_dialog);
			btnManualDmbtSearchYes = (Button) manual_dmbt_search_step1.findViewById(R.id.yes);
			btnManualDmbtSearchCancle = (Button) manual_dmbt_search_step2.findViewById(R.id.cancel);
			btnManualDmbtSearchNo = (Button) manual_dmbt_search_step1.findViewById(R.id.no);
			mManualDmbtSearchProgressBar = (ProgressBar) manual_dmbt_search_step2.findViewById(R.id.progressBar);
			mManualDmbtSearchParameter1 = (TextView) manual_dmbt_search_step2.findViewById(R.id.Parameter1);
			mManualDmbtSearchParameter2 = (TextView) manual_dmbt_search_step2.findViewById(R.id.Parameter2);

			btnManualDmbtSearchYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("CH_ER_COLLECT",
							"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:" + mContext.getResources().getString(R.string.collect2)
									+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1=" + mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager)
									+ ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior) + ";item3="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
									+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
					mMainMenu.setMenukeyUseless(true);
					mscanType = scantype.DTV_ScanMaunal_Dmbt;
					ShowScanMenuDetail();
					menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanMaunal);
					menuAutoScan.manualScanInit(low_fre_manual, 0, strSymbolManual, 0);
					menuAutoScan.Scanready();
				}
			});
			btnManualDmbtSearchYes.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////cuixiaoyan 2014-10-20
						// ///////////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								Log.d("CH_ER_COLLECT",
										"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1) + "|subClass:"
												+ mContext.getResources().getString(R.string.collect2) + "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3) + ";item1="
												+ mContext.getResources().getString(R.string.dtv_Mainmenu_ScanManager) + ";item2=" + mContext.getResources().getString(R.string.channel_manage_senior)
												+ ";item3=" + mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer) + ";item4="
												+ mContext.getResources().getString(R.string.dtv_ScanManager_Advanced_Customer_UserScan));
								mMainMenu.setMenukeyUseless(true);
								mscanType = scantype.DTV_ScanMaunal_Dmbt;
								ShowScanMenuDetail();
								menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanMaunal);
								menuAutoScan.manualScanInit(low_fre_manual, 0, strSymbolManual, 0);
								menuAutoScan.Scanready();
								return true;
							case 235:// keyboard Volume Down
								itemManualDmbtSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ///////////////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemManualDmbtSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemManualDmbtSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnManualDmbtSearchNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setGuideScan(false);
					mscanType = scantype.DTV_ScanMaunal_Dmbt;
					HideScanMenuDetail();
				}
			});
			btnManualDmbtSearchNo.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////////cuixiaoyan
						// 2014-10-20 ////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								setGuideScan(false);
								mscanType = scantype.DTV_ScanMaunal_Dmbt;
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								itemManualDmbtSearch.PullDown(false);
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// //////////////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemManualDmbtSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemManualDmbtSearch.requestfouse();
							return true;
						}
					}
					return false;
				}
			});

			btnManualDmbtSearchCancle.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ///////////////////////////////////////cuixiaoyan
						// 2014-10-20 /////////////////////////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 231:// keyboard Menu
							case 233:// keyboard Channel Up
							case 234:// keyboard Channel Down
								break;
							case 232:// keyboard Source
								mMainMenu.setMenukeyUseless(false);
								mscanType = scantype.DTV_ScanMaunal_Dmbt;
								menuAutoScan.Scantermined();
								HideScanMenuDetail();
								return true;
							case 235:// keyboard Volume Down
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// /////////////////////////////////////////////////////////////////////////////////////////////
						if ((arg1 == KeyEvent.KEYCODE_ENTER) || (arg1 == KeyEvent.KEYCODE_DPAD_CENTER)) {
							mMainMenu.setMenukeyUseless(false);
							mscanType = scantype.DTV_ScanMaunal_Dmbt;
							menuAutoScan.Scantermined();
							HideScanMenuDetail();
							return false;
						}
					}
					return true;
				}
			});

			itemManualDmbtSearch.setOnKeyListener(new OnItemViewOnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						// ////////////////////////////////////cuixiaoyan
						// 2014-10-20 //////////////
						isKeyboardProcessed = false;
						switch (arg2.getScanCode()) {
							case 235:// keyboard Volume Down
								mCustomSearchView.colsepulldown();
								mMainMenu.leftItemfouce();
								return true;
							case 236:// keyboard Volume Up
								return true;
							default:
								break;
						}
						// ////////////////////////////////////////////////////////////////////////
						if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
							itemManualDmbtSearch.PullDown(false);
							mCustomSearchView.colsepulldown();
						} else if (arg1 == KeyEvent.KEYCODE_DPAD_UP) {
							itemManualDmbtSearch.PullDown(false);
						} else if (arg1 == KeyEvent.KEYCODE_BACK) {
							itemManualDmbtSearch.PullDown(false);
							mManualSearchItem.requestfouse();
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			itemManualDmbtSearch.setOnPulldownListener(new OnPulldownListener() {

				@Override
				public void OnPulldown(int arg0) {
					// TODO Auto-generated method stub
					if (arg0 == 1) {
						manual_dmbt_search_step1.setVisibility(View.VISIBLE);
						manual_dmbt_search_step2.setVisibility(View.GONE);
						manual_dmbt_search_step1.requestFocus();
						btnManualDmbtSearchYes.requestFocus();
					}
				}
			});

			manualSearchDmbt.addListViewItem(itemStartFreqDmbt);
			manualSearchDmbt.addListViewItem(itemManualDmbtSearch);
		}

		mManualSearchItem.setOnClickListener(new OnItemViewClickListener() {

			@Override
			public void onClickItemView(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public View getView() {
				// TODO Auto-generated method stub
				int curProductType = mSourceManager.getProductType();
				if (ConstProductType.PRODUCT_C == curProductType) {
					Log.i(TAG, "cuixy manual search setting productType: PRODUCT_C ");
					return manualSearchDvbc.getView();
				} else if (ConstProductType.PRODUCT_T == curProductType) {
					Log.i(TAG, "cuixy manual search setting productType: PRODUCT_T ");
					return manualSearchDmbt.getView();
				} else {
					Log.i(TAG, "cuixy manual search setting productType: PRODUCT_C_T ");
					demoType = mSourceManager.getCurDemodeType();
					if (ConstDemodType.DVB_C == demoType) {
						Log.i(TAG, "cuixy manual search setting demoType: DVB_C ");
						return manualSearchDvbc.getView();
					} else if (ConstDemodType.DMB_TH == demoType) {
						Log.i(TAG, "cuixy manual search setting demoType: DMB_TH ");
						return manualSearchDmbt.getView();
					}
					return null;
				}
			}
		});

		mManualSearchItem.setOnKeyListener(new OnItemViewOnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					// ///////////////////////////////////////cuixiaoyan
					// 2014-10-20 /////////////////////////////////
					isKeyboardProcessed = false;
					switch (arg2.getScanCode()) {
						case 235:// keyboard Volume Down
							mCustomSearchView.colsepulldown();
							mMainMenu.leftItemfouce();
							return true;
						case 236:// keyboard Volume Up
							return true;
						default:
							break;
					}
					// /////////////////////////////////////////////////////////////////////////////////////////////
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						mCustomSearchItem.requestfouse();
						return true;
					} else if (arg1 == KeyEvent.KEYCODE_L) {
						mManualSearchItem.PullDown(false);
					}
					/**
					 * add by YangLiu 修改DTV出厂设置后显示频点与实际搜索频点不一致问题 2014-12-08
					 * 保持与指定主频点同步
					 */
					else if (arg1 == KeyEvent.KEYCODE_DPAD_CENTER) {
						// C下...........
						if (ConstProductType.PRODUCT_T != mSourceManager.getProductType()) {
							/* 再次读取起始频点 */
							String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
							int freManualStartValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
							for (int i = 0; i < mFrequencyTable.length; i++) {
								if (freManualStartValue == mFrequencyTable[i]) {
									itemStartFreqDvbc.refreshSeekbar(i);
									break;
								}
							}

							/* 再次读取截止频点 */
							tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
							int freManualEndValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
							for (int i = 0; i < mFrequencyTable.length; i++) {
								if (freManualEndValue == mFrequencyTable[i]) {
									itemEndFreqDvbc.refreshSeekbar(i);
									break;
								}
							}

							/* 再次读取符号率 */
							tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_SYM);
							int symManualValue = tmp == null ? MainMenuReceiver.getIndexByItem(mSymbolRateStr, String.valueOf(mOperatorManager.getOPMainTunerInfo().getSymbolRate()))
									: MainMenuReceiver.getIndexByItem(mSymbolRateStr, tmp);
							itemManualSearchSymbolRate.refreshSeekbar(symManualValue);

							/* 再次读取QAM */
							tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
							int qamManualValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getQamMode() - 1 : Integer.valueOf(tmp);
							itemManualSearchQam.refreshSeekbar(qamManualValue);
						}

						// T下............
						if (ConstProductType.PRODUCT_C != mSourceManager.getProductType()) {

							/* 再次读取起始频点 */
							DtvTunerInfo tunerInfo = dtvInterface.getDBMTTunerInfo();
							String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);
							int low_fre_manual = tmp == null ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp);
							for (int i = 0; i < mFrequencyDtmbTable.length; i++) {
								if (low_fre_manual == mFrequencyDtmbTable[i]) {
									itemStartFreqDmbt.refreshSeekbar(i);
									break;
								}
							}
						}
					}
				}
				return false;
			}

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mCustomSearchView.addListViewItem(mListSearchItem);
		mCustomSearchView.addListViewItem(mManualSearchItem);
		mCustomSearchView.addListViewItem(mUserSearchItem);
	}

	/**
	 * 搜索管理——>高级——>运营商设置视图
	 */
	private View initmDtvProviderView() {
		mDtvOperatorSelectView = appInfView.inflate(R.layout.vch_dtv_operator_select_layout, null, false);
		// 上半部分布局1：显示滚筒选择运营商
		operatorSelectLayout1 = (LinearLayout) mDtvOperatorSelectView.findViewById(R.id.layout1);
		// 下半部分布局2：选择运营商后显示是否切换
		operatorSelectLayout2 = (LinearLayout) mDtvOperatorSelectView.findViewById(R.id.layout2);
		// 修改当前运营商字符串
		curOperator = (TextView) mDtvOperatorSelectView.findViewById(R.id.dtv_current_operator);
		String string = mContext.getResources().getString(R.string.dtv_scan_setup_cur_operator, mDtvOperatorManager.getCurOperator().getOperatorName());
		curOperator.setText(string);
		// 显示是否切换提示与确定按钮
		operatorSelectWarning = (TextView) operatorSelectLayout2.findViewById(R.id.dtv_operator_select_title);
		btnOperatorSelectYes = (Button) operatorSelectLayout2.findViewById(R.id.yes);
		btnOperatorSelectNo = (Button) operatorSelectLayout2.findViewById(R.id.no);

		// dtvprovider_selector = new
		// DTVProviderSelector(mContext,dtvprovider_list);
		dtvprovider_selector.setTextSize(28);// 28px(<==>21sp)
		dtvprovider_selector.setFakeBoldText(false);
		dtvprovider_selector.SetOnclickListener(new OnWheelOKListener() {

			@Override
			// 确定运营商后显示布局2
			public void onOK(WheelView view, int code) {
				// TODO Auto-generated method stub
				Log.d("wheel", "code=====" + code);
				tmpOperateCode = code;
				if (code != mDtvOperatorManager.getCurOperatorCode()) {
					String string = mContext.getResources().getString(R.string.dtv_scan_setup_change_operator_normal_confirm, mDtvOperatorManager.getCurOperator().getOperatorName());
					operatorSelectWarning.setText(string);
					operatorSelectLayout2.setVisibility(View.VISIBLE);
					operatorSelectLayout2.requestFocus();
					btnOperatorSelectYes.requestFocus();
				}
			}

		});
		/* 运营商省份选择******************************* */
		dtvprovider_selector.setProvinceCyclic(true);
		dtvprovider_selector.setProvinceOnkeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						mOperatorItem.requestfouse();
					}
				}
				return false;
			}
		});
		/* 运营商省份选择******************************* */
		// dtvprovider_selector.setProviderCyclic(true);
		dtvprovider_selector.setProviderOnkeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						operatorSelectLayout2.setVisibility(View.GONE);
						mMainMenu.backToNextItem(mScanManage, mScanManage);
						mOperatorItem.requestfouse();
					}
				}
				return false;
			}
		});
		/* 点击确定后再次显示布局1，并完成重启功能******************************** */
		operatorSelectLayout1.addView(dtvprovider_selector.getView());
		btnOperatorSelectYes.setOnKeyListener(new OnKeyListener() {

			@Override
			// 点击ok后退回到布局1
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						operatorSelectLayout2.setVisibility(View.GONE);
						operatorSelectLayout1.requestFocus();
						progressDiglog = new CommonProgressInfoDialog(mContext);
						progressDiglog.getWindow().setType(2003);
						progressDiglog.setDuration(1000000);
						progressDiglog.setButtonVisible(false);
						progressDiglog.setCancelable(false);
						progressDiglog.setMessage(mContext.getString(R.string.dtv_system_reboot));
						progressDiglog.show();
						DtvChannelManager.getInstance().reset();
						DtvConfigManager.getInstance().clearAll();
						DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OP_GUIDE, "true");
						MenuManager.getInstance().delAllScheduleEvents();
						changeOperationByCode(tmpOperateCode);

						// 重启
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Log.i(TAG, "powerManager.reboot 2221");
								PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
								powerManager.reboot("");
								Log.i(TAG, "powerManager.reboot 2222");
							}
						}, 5000);
						Log.i(TAG, "powerManager.reboot 2223");
						return true;
					}
					// 点击向上按钮退回到布局1
					else if (arg1 == KeyEvent.KEYCODE_DPAD_UP) {
						operatorSelectLayout2.setVisibility(View.GONE);
						operatorSelectLayout1.requestFocus();
						return true;
					}
					// 点击向左按钮退回到布局1
					else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						operatorSelectLayout2.setVisibility(View.GONE);
						operatorSelectLayout1.requestFocus();
					}
				}
				return false;
			}
		});
		/* 点击否退回到布局1*********************************************** */
		btnOperatorSelectNo.setOnKeyListener(new OnKeyListener() {

			@Override
			// 点击否退回到布局1
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if ((arg1 == KeyEvent.KEYCODE_DPAD_CENTER) || (arg1 == KeyEvent.KEYCODE_ENTER)) {
						operatorSelectLayout2.setVisibility(View.GONE);
						operatorSelectLayout1.requestFocus();
						return true;
					}// 点击向左退回到布局1
					else if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT) {
						operatorSelectLayout2.setVisibility(View.GONE);
						operatorSelectLayout1.requestFocus();
					}
				}
				return false;
			}
		});
		return mDtvOperatorSelectView;
	}
}