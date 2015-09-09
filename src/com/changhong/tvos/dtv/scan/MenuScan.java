package com.changhong.tvos.dtv.scan;

import java.util.regex.Pattern;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.softkeyboard.CHSoftKeyboardManager;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.scan.ScanManager.scantype;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstScanParams;
import com.changhong.tvos.dtv.userMenu.MenuSearchGuide;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;
import com.changhong.tvos.system.commondialog.CommonProgressInfoDialog;

public class MenuScan extends Dialog implements android.view.View.OnKeyListener {

	private static final String TAG = "MenuScan";
	private Context mContext;
	public static final int SCAN_UPDATE_MESSAGE = 1;
	public static final int SCAN_STOP_MESSAGE = 2;
	public static final int DIALOG_EXIT_MESSAGE = 3;
	public static final int SAVE_DATA_MESSAGE = 4;//luobin - 2012-2-1
	public static final int SCAN_filterChannels = 5;
	private static final int DURATION = 30000;
	//	private FilterChannels mFilterChannel;
	private boolean isShowFilter = false;

	//	ProgressBar scanProgressBar;
	//	int titleResid;
	//	RelativeLayout layout;
	//	LinearLayout autoScanLayout;
	//	LinearLayout manualScanLayout;
	//	TextView scanTitle;
	//	TextView frequency;
	//	TextView symbolrate;
	//	TextView modulmode;
	//	TextView resultDTV;
	//	TextView resultRadio;
	//	Button startButton;
	//	TextView sourceIndicate;
	//	ViewFreqInputText frequencyEdit;
	//	ViewInputOptionText symbolrateEdit;
	//	ViewInputOptionText modulmodeEdit;
	//	ViewFreqInputText endFreEdit; 

	//	TextView text_KHz;
	//	TextView text_K;
	//	TextView text_QAM;
	//	TextView textEndMhz;

	int curFrequencyK = ConstScanParams.FREQUANCE_MIN_K_C;
	int curSymbolrate = 6875;
	int curModulmode = 5;
	int curEndFrequencyK = ConstScanParams.FREQUANCE_MAX_K;

	int preCurFrequencyK = ConstScanParams.FREQUANCE_MIN_K_C;
	int preCurSymbolrate = 6875;
	int preCurModulmode = 5;
	int progress;

	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;
	//	ProgressBar signalQualityBar;
	//	ProgressBar signalStrengthBar;
	ScanManager scanManager;
	public Handler handler;
	//	Runnable moveRun;
	scantype scanType;
	Thread mStopScanThread;

	private boolean isSavingData = false;
	private boolean isMatualSearched = false;
	boolean hasDismiss = false;
	CommonProgressInfoDialog progressDiglog;
	CommonInfoDialog infoDialog;
	//	private int xoff =0;
	//	private int yoff =0;
	//	private boolean moveRight = true;
	//	private int xWidth = 0;
	//	private int yHeight = 0;
	//	private int xOffset = 20;
	public boolean isShowTv;

	///////////////////////////////////////////////
	private String strScansource = "";
	private String strFrequency = "";
	private String strSymbolrate = "";
	private String strModulmode = "";
	private String strResultDTV = "";
	private String strResultBroadcast = "";
	private String strFilterChname = "";
	private String strFilterStep = "";
	private int intProgress = 0;
	private boolean terminated = false;

	DtvChannelManager mChannelManager = DtvChannelManager.getInstance();
	DtvInterface mDtvInterface = DtvInterface.getInstance();
	DtvSourceManager mDemodeTypeManager = DtvSourceManager.getInstance();

	private Runnable tunerState = new Runnable() {
		public void run() {
			Log.v("tv", "luobin tunerState update");
			menuManualTunerUpdate();
			menuManualSetTuner();
		}
	};

	private Runnable startScan = new Runnable() {

		public void run() {
			Log.i(TAG, "startScan -- isSearching	|	" + ScanManager.isSearching());
			if (ScanManager.isSearching()) {//2015-4-14 YangLiu
				Log.i(TAG, "startScan syncScanStop");
				syncScanStop();
			}
			Log.i(TAG, "handleMessag scanStart");
			actualScanStart();

			/*if(!scanManager.isSearching()){        		
				Log.i(TAG, "actualScanStart");
				actualScanStart();
			}*/
		}
	};

	private Runnable stopScan = new Runnable() {

		public void run() {
			Log.v(TAG, "stopScan -- isSearching	|	" + ScanManager.isSearching());
			if (ScanManager.isSearching()) {
				Log.v(TAG, "handleMessag scanStop");
				actualScanStop();
			}
		}
	};

	private Runnable uninstallscanRun = new Runnable() {

		public void run() {
			Log.i(TAG, "call uninstallscanRun() 777777777");
			uninstallscan();
		}
	};

	private Runnable dismissRun = new Runnable() {

		public void run() {
			Log.i(TAG, "call dismiss() 11100000000000");
			dismiss();
		}
	};

	public MenuScan(Context context, scantype type) {
		super(context, R.style.Theme_ActivityDialog);
		// TODO Auto-generated constructor stub
		//		setContentView(R.layout.menu_scan);
		mContext = context;
		Log.d(TAG, "=========MenuScan========");
		strScansource = (mContext.getResources().getString(R.string.dtv_scan_source) + " " + DtvSourceManager.getInstance().getCurSourceName());
		scaninit(type);
		isSavingData = false;
	}

	private void menuAutoScanInit() {
		//		sourceIndicate = (TextView) autoScanLayout.findViewById(id.dtv_scan_source);
		//		symbolrate = (TextView) autoScanLayout.findViewById(id.textView2);
		//		frequency = (TextView) autoScanLayout.findViewById(id.textView1);
		//
		//		symbolrate.setText(strScansource);
		//		
		//		scanTitle = (TextView) autoScanLayout.findViewById(id.textView7);
		//		
		//		modulmode = (TextView) autoScanLayout.findViewById(id.textView3);
		//		resultDTV = (TextView) autoScanLayout.findViewById(id.textView4);
		//		resultRadio = (TextView) autoScanLayout.findViewById(id.textView5);
		//		scanProgressBar = (ProgressBar) autoScanLayout
		//				.findViewById(R.id.progressBar1);
		//		startButton = (Button) autoScanLayout.findViewById(id.button1);
		//		
		//		setScanTitle(titleResid);
		//		startButton.requestFocus();
		//		startButton.setOnKeyListener(this);
		//		startButton.setOnClickListener(startButtonListener);
	}

	private void menuManualScanInit() {
		//mNavIntegration.iStopChannel();
		//		endFreEdit = (ViewFreqInputText)manualScanLayout.findViewById(id.endFreq);
		//		textEndMhz = (TextView)manualScanLayout.findViewById(id.text_endfre);
		//		frequency = (TextView) manualScanLayout.findViewById(id.manual_textView1);
		//		sourceIndicate = (TextView) manualScanLayout.findViewById(id.manual_dtv_scan_source);
		//		symbolrate = (TextView) manualScanLayout.findViewById(id.manual_textView2);
		//		modulmode = (TextView)manualScanLayout.findViewById(id.manual_textView3);
		//		text_KHz = (TextView) manualScanLayout.findViewById(id.text_KHz);
		//		text_K = (TextView) manualScanLayout.findViewById(id.text_K);
		//		text_QAM = (TextView) manualScanLayout.findViewById(id.text_QAM);
		//		text_KHz.setText("MHz");
		//		frequencyEdit = (ViewFreqInputText) manualScanLayout.findViewById(id.editText1);
		//		frequencyEdit.setOnKeyListener(new View.OnKeyListener() {
		//			
		//			@Override
		//			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
		//				// TODO Auto-generated method stub
		//				if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP ){
		//					endFreEdit.setFreqText(convertString2Integer(frequencyEdit.getFreqText().toString()));
		//				}
		//				return false;
		//			}
		//		});
		//		
		//		List<String> list = new ArrayList<String>();
		//		
		//		if(ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()){
		//			frequency.setText(mContext.getResources().getString(R.string.dtv_scan_start_fre));
		//			text_K.setText("Kbps");
		//			text_QAM.setVisibility(View.VISIBLE);
		//			text_K.setVisibility(View.VISIBLE);
		//			symbolrate.setVisibility(View.VISIBLE);
		//			
		//			symbolrate.setText(mContext.getResources().getString(
		//					R.string.dtv_scan_Symbolrate));
		//			
		//			sourceIndicate.setText(mContext.getResources().getString(
		//					R.string.dtv_scan_source) + " " + DtvSourceManager.getInstance().getCurSourceName());
		//			
		//			
		//		}else if(ConstDemodType.DMB_TH == mDemodeTypeManager.getCurDemodeType()){
		//			frequency.setText(mContext.getResources().getString(R.string.dtv_scan_frequency));
		//			sourceIndicate.setText(mContext.getResources().getString(
		//					R.string.dtv_scan_source) + " " + DtvSourceManager.getInstance().getCurSourceName());
		//			
		//			text_K.setText("MHz");
		//			
		//			text_QAM.setVisibility(View.INVISIBLE);
		//			text_K.setVisibility(View.INVISIBLE);
		//			symbolrate.setVisibility(View.INVISIBLE);
		//		}
		//
		//		int[] Table = scanManager.getFreTable();
		//		for (int i = 0; i < Table.length; i++) {
		//			list.add(String.valueOf(Table[i]));
		//		}
		//		if(scanType ==scantype.DTV_ScanMaunal && mChannelManager.getCurProgram()!=null){
		//			if(ConstDemodType.DMB_TH == mDemodeTypeManager.getCurDemodeType()){
		//				frequencyEdit.setList(scanManager.currentFrequency , list ,mChannelManager.getCurProgram().mFrequency, ConstScanParams.FREQUANCE_MAX_K_T / 100);
		//				endFreEdit.setList(scanManager.currentFrequency , list ,mChannelManager.getCurProgram().mFrequency, ConstScanParams.FREQUANCE_MAX_K_T / 100);
		//			}else {
		//				frequencyEdit.setList(scanManager.currentFrequency , list ,mChannelManager.getCurProgram().mFrequency, ConstScanParams.FREQUANCE_MAX_K / 1000);
		//				endFreEdit.setList(scanManager.currentFrequency , list ,mChannelManager.getCurProgram().mFrequency, ConstScanParams.FREQUANCE_MAX_K / 1000);
		//			}
		//		}else{
		//			if(ConstDemodType.DMB_TH == mDemodeTypeManager.getCurDemodeType()){
		//				frequencyEdit.setList(scanManager.currentFrequency  , list ,ConstScanParams.FREQUANCE_MIN_K_T,ConstScanParams.FREQUANCE_MAX_K_T / 100);
		//				endFreEdit.setList(scanManager.currentFrequency  , list ,ConstScanParams.FREQUANCE_MIN_K_T,ConstScanParams.FREQUANCE_MAX_K_T / 100);
		//			}else if(ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()){
		//				frequencyEdit.setList(scanManager.currentFrequency  , list ,ConstScanParams.FREQUANCE_MIN_K_C,ConstScanParams.FREQUANCE_MAX_K / 1000);
		//				endFreEdit.setList(scanManager.currentFrequency  , list ,ConstScanParams.FREQUANCE_MIN_K_C,ConstScanParams.FREQUANCE_MAX_K / 1000);
		//			}
		//		}
		//
		//		list = new ArrayList<String>();
		//		Table = scanManager.getSymbolRateTable();
		//		int curSymbolIndex = 0;
		//		for (int i = 0; i < Table.length; i++) {
		//			list.add(String.valueOf(Table[i]));
		//			if(scanManager.symbolRate == Table[i]){
		//				curSymbolIndex = i;
		//			}
		//		}
		//		
		//		symbolrateEdit = (ViewInputOptionText) manualScanLayout.findViewById(id.editText2);
		//		
		//		if(ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()){
		//			symbolrateEdit.setVisibility(View.VISIBLE);
		//			symbolrateEdit.setFocusable(true);
		//			symbolrateEdit.setEnabled(true);
		//			symbolrateEdit.setInputEnable(true);
		//			symbolrateEdit.setList(list, curSymbolIndex ,0,10000);
		//			Log.i(TAG, "EL--> demode type DTVC");
		//			}else{
		//			symbolrateEdit.setVisibility(View.INVISIBLE);
		//			symbolrateEdit.setList(list, 0, 0, ConstScanParams.FREQUANCE_BANDWIDTH);
		//			symbolrateEdit.setText(ConstScanParams.FREQUANCE_BANDWIDTH + " K");
		//			Log.i(TAG, "EL--> demode type DBMT");
		//			}
		//		
		//		list = new ArrayList<String>();
		//		String[] qamTable = scanManager.getModulModeTable();
		//		for (int i = 0; i < qamTable.length; i++) {
		//			list.add(qamTable[i]);
		//		}
		//		
		//		modulmodeEdit = (ViewInputOptionText) manualScanLayout.findViewById(id.editText3);
		//		
		//		if(ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()){
		//			modulmodeEdit.setVisibility(View.VISIBLE);
		//			modulmodeEdit.setList(list, scanManager.modulMode ,4,1024);
		//		}else {
		//			modulmodeEdit.setVisibility(View.INVISIBLE);
		//		}
		//		
		//		modulmodeEdit.setInputEnable(false);
		//		resultDTV = (TextView) manualScanLayout.findViewById(id.manual_textView4);
		//		resultRadio = (TextView) manualScanLayout.findViewById(id.manual_textView5);
		//		scanProgressBar = (ProgressBar) manualScanLayout.findViewById(R.id.manual_progressBar1);
		//		signalQualityBar = (ProgressBar) manualScanLayout.findViewById(R.id.progressBar2);
		//		signalStrengthBar = (ProgressBar) manualScanLayout.findViewById(R.id.progressBar3);
		//		startButton = (Button) manualScanLayout.findViewById(id.manual_button1);
		//		
		//		startButton.setText(R.string.dtv_scan_start);
		//		startButton.setOnKeyListener(this);
		//		startButton.setOnClickListener(startButtonListener);
		//
		//		frequencyEdit.setNextFocusUpId(id.button1);
		//		startButton.setNextFocusDownId(frequencyEdit.getId());
		//		
		//		if(ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()){
		//			startButton.setNextFocusUpId(id.editText3);
		//			modulmodeEdit.setNextFocusUpId(id.editText2);
		//			symbolrateEdit.setNextFocusDownId(id.editText3);
		//			
		////			if(DtvOperatorManager.getInstance().getCurOperator().getOperatorCode() == ConstOperatorCode.DTMB_OP_CODE_JX){
		//				endFreEdit.setVisibility(View.VISIBLE);
		//				textEndMhz.setVisibility(View.VISIBLE);
		//				frequencyEdit.setNextFocusDownId(id.endFreq);
		//				endFreEdit.setNextFocusDownId(id.editText2);
		//				endFreEdit.setNextFocusUpId(id.editText1);
		//				symbolrateEdit.setNextFocusUpId(id.endFreq);
		//				sourceIndicate.setText(mContext.getResources().getString(R.string.dtv_scan_end_fre));
		////			}else{
		////				endFreEdit.setVisibility(View.GONE);
		////				textEndMhz.setVisibility(View.GONE);
		////			}
		//			
		//		}
		//		handler.postDelayed(tunerState, 1000*3);
	}

	public void manualScanInit(int freqStart, int freqEnd, String symbol, int qam) {
		if (ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType() && (freqStart > freqEnd)) {
			curFrequencyK = freqEnd;
			curEndFrequencyK = freqStart;
		} else {
			curFrequencyK = freqStart;
			curEndFrequencyK = freqEnd;
		}
		curSymbolrate = convertString2Integer(symbol);
		curModulmode = qam;
	}

	private void scaninit(scantype type) {
		menuUpdateTaskInit();
		//  MainMenuRootData.AutoScan_updatetaskinit();
		// MainMenuRootData.AutoScanUpdate();
		Log.i(TAG, "LL scaninit>>new ScanManager>>start time = " + System.currentTimeMillis());
		scanManager = new ScanManager(mContext, type);
		Log.i(TAG, "LL scaninit>>new ScanManager>>end time = " + System.currentTimeMillis());
		preCurModulmode = curModulmode = scanManager.modulMode;
		preCurFrequencyK = curFrequencyK = scanManager.currentFrequency;
		preCurSymbolrate = curSymbolrate = scanManager.symbolRate;
		Log.i(TAG, "LL scaninit()>>curFrequency = " + curFrequencyK + ",curSymbolrate = " + curSymbolrate + ",curModulmode = " + curModulmode);
		scanType = type;
		switch (type) {
		case DTV_ScanMaunal:
		case DTV_ScanMaunal_Dmbt:
			mChannelManager.setDtvScanType(2);
			//			titleResid = R.string.dtv_manualscan;
			//			menuManualScanInit();
			menuManualScanUpdate();
			//	manualScanLayout.setVisibility(View.VISIBLE);
			isSavingData = false;
			syncScanStart();
			break;
		case DTV_ScanList:
			mChannelManager.setDtvScanType(1);
			//			titleResid = R.string.dtv_listscan;
			// menuAutoScanInit();
			menuListScanUpdate();//****
			//	autoScanLayout.setVisibility(View.VISIBLE);
			isSavingData = false;
			syncScanStart();
			break;
		case DTV_ScanAuto:
		case DTV_ScanAutoExtra:
		default:
			mChannelManager.setDtvScanType(0);
			// titleResid = R.string.dtv_autoscan;
			//	menuAutoScanInit();
			menuAutoScanUpdate();
			//	autoScanLayout.setVisibility(View.VISIBLE);
			isSavingData = false;
			syncScanStart();
			break;
		}
	}

	public void menuAutoScanUpdate() {
		String strTitle;
		if (ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()) {
			strSymbolrate = mContext.getResources().getString(R.string.dtv_scan_Symbolrate) + " " + scanManager.symbolRate + " Kbps";
			strFrequency = mContext.getResources().getString(R.string.dtv_scan_frequency) + " " + scanManager.currentFrequency / 1000 + " MHz";
			strTitle = mContext.getResources().getString(R.string.dtv_scan_programing) + " " + scanManager.currentFrequency / 1000 + "MHz";
		} else {
			strSymbolrate = "";
			strFrequency = mContext.getResources().getString(R.string.dtv_scan_frequency) + " " + scanManager.currentFrequency / 1000.0 + " MHz";
			if (scanManager.currentFrequency < 70000) {
				strTitle = mContext.getResources().getString(R.string.dtv_scan_programing) + " " + scanManager.currentFrequency / 1000.0 + "MHz";
			} else {
				strTitle = mContext.getResources().getString(R.string.dtv_scan_programing) + " " + scanManager.currentFrequency / 1000 + "MHz";
			}
		}

		if (scanManager.modulMode < 0 || scanManager.modulMode >= scanManager.getModulModeTable().length) {
			scanManager.modulMode = scanManager.getModulModeTable().length - 1;
		}
		strModulmode = (mContext.getResources().getString(R.string.dtv_scan_Modulmode) + " " + scanManager.getModulModeTable()[scanManager.modulMode] + " QAM");

		/////////////////fengy 2014-5-16   autoscan refresh /////////////////////////////////
		//	String strTitle = mContext.getResources().getString(R.string.dtv_scan_programing);
		String strSubTitle = DtvSourceManager.getInstance().getCurSourceName();
		strResultDTV = mContext.getResources().getString(R.string.dtv_scan_resultDTV) + "\u3000" + scanManager.resultOfDTV;
		strResultBroadcast = mContext.getResources().getString(R.string.dtv_scan_resultbroadcast) + "\u3000" + scanManager.resultOfRadio;

		intProgress = scanManager.scanProgress;
		if (!terminated) {
			if (MainMenuRootData.getGuideScan()) {
				MenuSearchGuide.RefreshGuideScanParameter(strFrequency, strScansource, strSymbolrate, strModulmode, strResultDTV, strResultBroadcast, strFilterChname, strFilterStep, intProgress,
						false);
			} else {
				MainMenuRootData.RefreshScanMenu(strTitle, strSubTitle, strResultDTV, strResultBroadcast, intProgress);
			}
		}
	}

	public void menuListScanUpdate() {
		menuAutoScanUpdate();
	}

	public void menuManualScanUpdate() {
		String strTitle;
		if (ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()) {
			strTitle = mContext.getResources().getString(R.string.dtv_scan_programing) + " " + scanManager.currentFrequency / 1000 + "MHz";
		} else {
			if (scanManager.currentFrequency < 70000) {
				strTitle = mContext.getResources().getString(R.string.dtv_scan_programing) + " " + scanManager.currentFrequency / 1000.0 + "MHz";
			} else {
				strTitle = mContext.getResources().getString(R.string.dtv_scan_programing) + " " + scanManager.currentFrequency / 1000 + "MHz";
			}
		}

		String strSubTitle = DtvSourceManager.getInstance().getCurSourceName();
		strResultDTV = mContext.getResources().getString(R.string.dtv_scan_resultDTV) + "\u3000" + scanManager.resultOfDTV;
		strResultBroadcast = mContext.getResources().getString(R.string.dtv_scan_resultbroadcast) + "\u3000" + scanManager.resultOfRadio;

		intProgress = scanManager.scanProgress;
		if (!terminated) {
			MainMenuRootData.RefreshScanMenu(strTitle, strSubTitle, strResultDTV, strResultBroadcast, intProgress);
		}
	}

	public void menuManualTunerUpdate() {
		//		signalQualityBar.setProgress(scanManager.getSignalQuality());
		//		signalStrengthBar.setProgress(scanManager.getSignalLevel());
	}

	public void menuManualSetTuner() {
		//		curFrequencyK = this.convertString2Integer(frequencyEdit.getFreqText().toString());
		//		curSymbolrate = this.convertString2Integer(symbolrateEdit.getText().toString());
		//		curModulmode = modulmodeEdit.getCurrentIndex()+1;
		//		if((curFrequencyK != preCurFrequencyK) || (curSymbolrate !=preCurSymbolrate)||(curModulmode != preCurModulmode))
		//		{
		//			preCurFrequencyK = curFrequencyK;
		//			preCurSymbolrate = curSymbolrate;
		//			preCurModulmode = curModulmode;
		//			handler.postDelayed(tunerState, 200);
		//		}
		//		else
		//		{
		//			handler.postDelayed(tunerState, 500);
		//		}
	}

	private void menuUpdateTaskInit() {

		handler = new Handler() {
			public void handleMessage(Message msg) {
				Log.i(TAG, "menuUpdateTaskInit    hasDismiss: " + hasDismiss);
				if (hasDismiss) {
					return;
				}
				switch (msg.what) {
				case MenuScan.SCAN_UPDATE_MESSAGE:
					Log.i(TAG, "FYYY0000 SCAN_UPDATE_MESSAGE");
					if (scanType == scantype.DTV_ScanMaunal) {
						menuManualScanUpdate();
					} else if (scanType == scantype.DTV_ScanList) {
						menuListScanUpdate();
					} else {
						menuAutoScanUpdate();
					}
					break;

				case MenuScan.SAVE_DATA_MESSAGE:
					Log.i(TAG, "FYYY0000 SAVE_DATA_MESSAGE");
					if (scanType == scantype.DTV_ScanMaunal) {
						menuManualScanUpdate();
					} else if (scanType == scantype.DTV_ScanList) {
						menuListScanUpdate();
					} else {
						menuAutoScanUpdate();
					}
					/*
					 * if(progressDiglog ==null && scanType
					 * !=scantype.DTV_ScanMaunal){ progressDiglog = new
					 * CommonProgressInfoDialog(mContext);
					 * progressDiglog.setDuration(DURATION);
					 * progressDiglog.setButtonVisible(false);
					 * progressDiglog.setCancelable(false);
					 * progressDiglog.setMessage
					 * (mContext.getString(R.string.dtv_scan_store_program));
					 * isSavingData = true; progressDiglog.show(); }
					 */
					isSavingData = true;
					break;
				case MenuScan.DIALOG_EXIT_MESSAGE:
					Log.i(TAG, "FYYY0000 DIALOG_EXIT_MESSAGE	| terminated=" + terminated);
					// 停止搜索
					syncScanStop();
					if (scanManager.scanProgress != 100) {
						scanManager.scanProgress = 100;
						Log.i(TAG, "LL setProgressBar to full ***");
						// scanProgressBar.setProgress(scanManager.scanProgress);
					}
					scanStopFinish();
					Log.i(TAG, "MenuScan.DIALOG_EXIT_MESSAGE-->scanStopFinish");

					/**
					 * 添加搜索结束后提醒用户是否过滤的功能：
					 */
					Log.i(TAG, "MenuScan isTerminated-->" + terminated);
					if (!terminated) {//!terminated
						terminated = true;
						Log.i(TAG, "isCurDemodeType=" + DtvSourceManager.getInstance().getCurDemodeType() + "	0表示C，1表示T");
						Log.i(TAG, "isCurOperator=" + DtvOperatorManager.getInstance().getCurOperator().getOperatorName() + "------operatorCode="
								+ DtvOperatorManager.getInstance().getCurOperator().getOperatorCode());
						Log.i(TAG, "isCurScanType=" + scanType);
						Log.i(TAG, "isCurScanResult=" + scanManager.resultOfData + "|" + scanManager.resultOfDTV);

						boolean isCurDemodeType_C = (ConstDemodType.DVB_C == DtvSourceManager.getInstance().getCurDemodeType()) ? true : false;
						boolean isCurOperator_TongYong = ((-16777215 == DtvOperatorManager.getInstance().getCurOperator().getOperatorCode())
								|| (1 == DtvOperatorManager.getInstance().getCurOperator().getOperatorCode()) || (2 == DtvOperatorManager.getInstance().getCurOperator().getOperatorCode()) || (3 == DtvOperatorManager
								.getInstance().getCurOperator().getOperatorCode())) ? true : false;
						boolean isCurScanType_Manual = (scanType == scantype.DTV_ScanMaunal) ? true : false;
						boolean isCurScanHasResults = (scanManager.resultOfData > 0 || scanManager.resultOfDTV > 0) ? true : false;

						if (!isCurDemodeType_C && !isCurScanType_Manual && isCurScanHasResults) {
							Log.i(TAG, "MenuScan-->T-->ShowFilterMenu");
							MainMenuRootData.ShowFilterMenu();
						} else if (isCurDemodeType_C && isCurOperator_TongYong && !isCurScanType_Manual && isCurScanHasResults) {
							Log.i(TAG, "MenuScan-->C.TongYong-->ShowFilterMenu");
							MainMenuRootData.ShowFilterMenu();
						} else {
							MainMenuRootData.HideScanMenuDetail();// add by cxy

							/**
							 * 修改手动搜素频点无节目时播放上一个节目 2014-12-05
							 */
							Log.i(TAG, "isCurScanType_Manual=" + isCurScanType_Manual + "	\nisCurScanHasResults=" + isCurScanHasResults);
							if (isCurScanType_Manual && !isCurScanHasResults) {
								if (mChannelManager != null) {
									DtvProgram ch = mChannelManager.getCurProgram();
									if (ch != null) {
										mChannelManager.channelForceChangeByProgramServiceIndex(ch.mServiceIndex, false);
										if (mContext instanceof DtvRoot) {
											((DtvRoot) mContext).getViewChannelInfo().show();
										}
									} else {
										Log.i("YangLiu", "ch==null");
									}
								}
							}
						}
					}

					/*if (!terminated) {
						terminated = true;
						Log.i(TAG, "scanType=" + scanType + "|"+ scanManager.resultOfData + "|" + scanManager.resultOfDTV);
						if ((scanType != scantype.DTV_ScanMaunal) &&(scanManager.resultOfData > 0 || scanManager.resultOfDTV > 0)) {
							setShowFilter(true);
							final FilterChannels filterChannels = FilterChannels.getInstance(getContext());
							filterChannels.show();
							Log.i(TAG, "startSmartSkip");
							filterChannels.startSmartSkip();
						} else {
							setShowFilter(false);
							MainMenuRootData.HideScanMenuDetail();// add by cxy

							*//**
						* 修改手动搜素频点无节目时播放上一个节目 2014-12-05
						*/
					/*
					if ((scanManager.resultOfRadio + scanManager.resultOfDTV) == 0) {
					if (mChannelManager != null) {
						DtvProgram ch = mChannelManager.getCurProgram();
						if (ch != null) {
							mChannelManager.channelForceChangeByProgramServiceIndex(ch.mServiceIndex, false);
							if (mContext instanceof DtvRoot) {
								((DtvRoot) mContext).getViewChannelInfo().show();
							}
						} else {
							Log.i("YangLiu", "ch==null");
						}
					}
					}
					}
					}*/
					break;
				default:
					break;
				}
			}
		};
	}

	public void syncScanStart() {
		handler.removeCallbacks(startScan);
		handler.postDelayed(startScan, 100);
	}

	public void syncScanStop() {
		Log.i(TAG, "syncScanStop--stopScan");
		handler.removeCallbacks(stopScan);
		handler.postDelayed(stopScan, 100);
	}

	private void actualScanStart() {
		if (scanType == scantype.DTV_ScanMaunal) {
			//			int fre = convertString2Integer(frequencyEdit.getFreqText().toString());
			//				if(fre < 1000){
			//					fre *= 1000;
			//				}
			//			if(ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()){
			//				if(fre < ConstScanParams.FREQUANCE_MIN_K_C){
			//					fre = ConstScanParams.FREQUANCE_MIN_K_C;
			//					endFreEdit.setFreqText(fre);
			//				}
			//			}else {
			//				if(fre < ConstScanParams.FREQUANCE_MIN_K_T){
			//					fre = ConstScanParams.FREQUANCE_MIN_K_T;
			//					endFreEdit.setFreqText(fre);
			//				}
			//			}
			//			frequencyEdit.setFreqText(fre);
			//
			//			curFrequencyK = fre;
			//			curSymbolrate = this.convertString2Integer(symbolrateEdit.getText().toString());
			//			curModulmode = scanManager.getQamModeIndex(modulmodeEdit.getText().toString());
			if (ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()) {
				//				scanManager.setListFreMax(convertString2Integer(endFreEdit.getFreqText().toString()));
				scanManager.setListFreMax(curEndFrequencyK);
				scanManager.scanStart(handler, curFrequencyK, curSymbolrate, curModulmode);
			} else if (ConstDemodType.DMB_TH == mDemodeTypeManager.getCurDemodeType()) {
				scanManager.scanStartDMBT(handler, curFrequencyK, curSymbolrate, curModulmode);
			}
			isMatualSearched = true;
		} else {
			if (ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()) {
				scanManager.scanStart(handler, curFrequencyK, curSymbolrate, curModulmode);
			} else if (ConstDemodType.DMB_TH == mDemodeTypeManager.getCurDemodeType()) {
				//need to change
				scanManager.scanStartDMBT(handler, curFrequencyK, curSymbolrate, curSymbolrate);
			}
		}
		//startButton.setText(R.string.dtv_scan_stop);
	}

	private void actualScanStop() {
		Log.i(TAG, "LL start scanstop ***");
		scanManager.scanStop();
		Log.i(TAG, "LL end scanstop ***");
	}

	private void scanStopFinish() {
		handler.postDelayed(uninstallscanRun, 2000);

		int scanResult = scanManager.resultOfRadio + scanManager.resultOfDTV;
		if (scanResult != 0 || scanType != scantype.DTV_ScanMaunal) {
			onBackPressed();
			//cancel();
		} else {
			//			startButton.setText(R.string.dtv_scan_start);
			isSavingData = false;
		}

		if (mContext instanceof DtvRoot) {
			Log.i(TAG, "call reportDtvChannelInfo");
			((DtvRoot) mContext).reportDtvChannelInfo();
		}
		/**搜索结束/取消	YangLiu	2015-2-26*/
	}

	public void setScanTitle(int title) {
		//		scanTitle.setText(title);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD:
			if (mAdjustCHSKM == null) {
				mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);
			}
			if (null != mAdjustCHSKM) {
				if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
				} else {

					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_CENTER);
					return true;
				}
			}
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "LL MenuScan>>onKeyDown>>keyCode = " + keyCode);
		switch (event.getScanCode()) {
		case 231://keyboard Menu
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
			//			this.onKey(startButton, keyCode, event);
			return true;

		case 235://keyboard Volume Down
			return true;
		case 236://keyboard Volume Up
			return true;
		default:
			break;
		}
		if ((keyCode == KeyEvent.KEYCODE_CHANGHONGIR_TV || keyCode == 170) && isSavingData == false) {
			if (ScanManager.isSearching()) {
				isSavingData = true;
				syncScanStop();
			} else {
				isShowTv = true;
				Log.i(TAG, "call dismiss() 222");
				dismiss();
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU && isSavingData == false) {
			if (ScanManager.isSearching()) {
				isSavingData = true;
				syncScanStop();
			} else {
				Log.i(TAG, "call dismiss() 333");
				dismiss();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	android.view.View.OnClickListener startButtonListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Log.i(TAG, "LL onClick()*** startButtonListener.....view.");
			if (ScanManager.isSearching()) {
				isSavingData = true;
				syncScanStop();
				Log.i(TAG, "LL intent to stop scanning ***");

				if (progressDiglog == null) {
					progressDiglog = new CommonProgressInfoDialog(mContext);
					progressDiglog.setDuration(DURATION);
					progressDiglog.setButtonVisible(false);
					progressDiglog.setCancelable(false);
					progressDiglog.setMessage(mContext.getString(R.string.dtv_scan_store_program));
				}
				progressDiglog.show();
			} else {
				isSavingData = false;
				syncScanStart();
			}
		}

	};

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "LL onKey ***");
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isSavingData == false) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					if (ScanManager.isSearching()) {
						isSavingData = true;
						syncScanStop();
						Log.i(TAG, "LL intent to stop scanning ***");
						// handler.post(stopScan);
						// handler.postDelayed(stopScan, 300);
						// handler.sendEmptyMessage(DtvScanDialog.SCAN_STOP_MESSAGE);
						// handler.sendEmptyMessageDelayed(DtvScanDialog.SCAN_STOP_MESSAGE,
						// 300);
						if (progressDiglog == null) {
							progressDiglog = new CommonProgressInfoDialog(mContext);
							progressDiglog.setDuration(DURATION);
							progressDiglog.setButtonVisible(false);
							progressDiglog.setCancelable(false);
							progressDiglog.setMessage(mContext.getString(R.string.dtv_scan_store_program));
						}
						progressDiglog.show();
					} else {
						isSavingData = false;
						syncScanStart();
					}
					return true;
				case KeyEvent.KEYCODE_MENU:
				case KeyEvent.KEYCODE_BACK:
					Log.v("DtvAutoScan", "onKey KEYCODE_BACK");
					if (ScanManager.isSearching()) {
						isSavingData = true;
						syncScanStop();
						if (progressDiglog == null) {
							progressDiglog = new CommonProgressInfoDialog(mContext);
							progressDiglog.setDuration(DURATION);
							progressDiglog.setButtonVisible(false);
							progressDiglog.setCancelable(false);
							progressDiglog.setMessage(mContext.getString(R.string.dtv_scan_store_program));
						}
						progressDiglog.show();
					}
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					return true;
				case KeyEvent.KEYCODE_DPAD_UP:
				case KeyEvent.KEYCODE_DPAD_DOWN:
					if (ScanManager.isSearching()) {
						return true;
					}
					break;
				default:
					break;
				}
			} else {
				Log.v(TAG, "LL onKey savaDate ==true");
				Log.i(TAG, "postDelayed dismissRun 111");
				handler.postDelayed(dismissRun, 60000);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (ScanManager.isSearching()) {
			Log.e(TAG, "LL onStop.scanManager.isSearching()..scanStop();");
			syncScanStop();
		}
		super.onStop();
	}

	public void show() {
		super.show();
		DtvDialogManager.AddShowDialog(this);
		isShowTv = false;
		isShowFilter = false;
		hasDismiss = false;
		Log.i(TAG, "666 hasDismiss: " + hasDismiss);
		//		Window window = this.getWindow();
		//		yHeight = window.getWindowManager().getDefaultDisplay().getHeight();
		//		xWidth = window.getWindowManager().getDefaultDisplay().getWidth()-layout.getLayoutParams().width;
		//		Log.i(TAG,"LL yHeight = " + yHeight + ", xWidth = " + xWidth + ", layout = " + layout.getLayoutParams().width);
		//		scanManager.install();
		//		moveRun =new Runnable() {
		//			public void run() {
		//				// TODO Auto-generated method stub
		//
		//				if (moveRight) {
		//					if (xoff<xWidth/2) {
		//						xoff = xoff+xOffset;
		//					}else {
		//						moveRight = false;
		//					}
		//				}else {
		//					if (xoff>-xWidth/2) {
		//						xoff = xoff-xOffset;
		//					}else {
		//						moveRight = true;
		//					}
		//					
		//				}			
		//				setPositon(xoff, 0);
		//				if(false == isSavingData){
		//					handler.postDelayed(moveRun, 1000);
		//				}
		//			}
		//		};
		//		if(scanType !=scantype.DTV_ScanMaunal){
		//			handler.postDelayed(moveRun, 1000);
		//		}else{
		//			isMatualSearched = false;
		//		}
	}

	public void Scanready() {
		hasDismiss = false;
		Log.i(TAG, "333 hasDismiss: " + hasDismiss);
		scanManager.install();
	}

	//点击取消搜索、过滤键
	public void Scantermined() {
		this.terminated = true;
		Log.i(TAG, "Scantermined  0000000 hasDismiss: " + hasDismiss);

		//取消过滤    delete by YangLiu 2015-4-14
		/*if (null == mFilterChannel) {
			mFilterChannel = FilterChannels.getInstance(mContext);
		}
		if (mFilterChannel.isFilter()) {
			Log.i(TAG, "onScheduleTimeUp()--> FilterChannels is isFiltering");
			mFilterChannel.cancelSmartSkip();
			Log.d("CH_ER_COLLECT",
					"reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1)
					+ "|subClass:" + mContext.getResources().getString(R.string.collect2)
					+ "|reportInfo:menu=" + mContext.getResources().getString(R.string.collect3)
					+ ";item1=" + mContext.getResources().getString(R.string.dtv_ScanManager_CancelFilter));
			// scanManager.unInstall();
			// mFilterChannel.endSmartSkip();
		
		//取消搜台
		} else {
			// if (scanManager.isSearching())
			isSavingData = true;
			syncScanStop();
			handler.postDelayed(uninstallscanRun, 2000);
		}*/

		isSavingData = true;
		syncScanStop();
		//		handler.postDelayed(uninstallscanRun, 2000);

		Log.i(TAG, "Scantermined 1111111 hasDismiss: " + hasDismiss);
	}

	public void uninstallscan() {
		if (hasDismiss == true) {
			return;
		}
		scanManager.unInstall();
		//		handler.removeCallbacks(moveRun);
		handler.removeCallbacks(tunerState);
		handler.removeCallbacks(dismissRun);
		handler.removeCallbacks(startScan);
		handler.removeCallbacks(stopScan);

		if (ScanManager.isSearching()) {
			syncScanStop();
		}

		DtvProgram ch = null;
		if (scanType != scantype.DTV_ScanMaunal && (scanManager.resultOfDTV + scanManager.resultOfRadio) > 0) {
			if (scanManager.resultOfDTV > 0) {
				mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_TV);
			} else {
				mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_RADIO);
			}
			ch = mChannelManager.getProgramByNum(mChannelManager.getBootChannelNum());
		}

		if (scanType == scantype.DTV_ScanMaunal && isMatualSearched == true) {
			if ((scanManager.resultOfRadio + scanManager.resultOfDTV) > 0) {
				if (scanManager.resultOfDTV > 0) {
					mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_TV);
				} else {
					mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_RADIO);
				}
				if ((scanManager.resultOfRadio + scanManager.resultOfDTV) > 0) {
					ch = mChannelManager.getProgramByFre(curFrequencyK);
				} else {
					ch = mChannelManager.getCurProgram();
				}
			} else {
				ch = mChannelManager.getCurProgram();
			}
		}

		if (ch != null) {
			mChannelManager.channelForceChangeByProgramServiceIndex(ch.mServiceIndex, false);
			if (mContext instanceof DtvRoot) {
				((DtvRoot) mContext).getViewChannelInfo().show();
			}
		} else {
			Log.e(TAG, "LL dismiss>> ch==null");
		}
		hasDismiss = true;
	}

	public void dismiss() {
		super.dismiss();
		DtvDialogManager.RemoveDialog(this);
		Log.v(TAG, "LL scan dialog dismiss hasDismiss=" + hasDismiss);
		Log.i(TAG, "444 hasDismiss: " + hasDismiss);
		if (hasDismiss == true) {
			return;
		}
		if (null != mAdjustCHSKM) {
			if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
				mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
			}
		}

		scanManager.unInstall();
		//		handler.removeCallbacks(moveRun);
		handler.removeCallbacks(tunerState);
		handler.removeCallbacks(dismissRun);
		handler.removeCallbacks(startScan);
		handler.removeCallbacks(stopScan);
		if (progressDiglog != null) {
			progressDiglog.dismiss();
			progressDiglog = null;
		}
		if (ScanManager.isSearching()) {
			syncScanStop();
		}

		DtvProgram ch = null;
		if (scanType != scantype.DTV_ScanMaunal && (scanManager.resultOfDTV + scanManager.resultOfRadio) > 0) {

			if (scanManager.resultOfDTV > 0) {
				mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_TV);
			} else {
				mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_RADIO);
			}

			ch = mChannelManager.getProgramByNum(mChannelManager.getBootChannelNum());
		}

		if (scanType == scantype.DTV_ScanMaunal && isMatualSearched == true) {
			if ((scanManager.resultOfRadio + scanManager.resultOfDTV) > 0) {
				if (scanManager.resultOfDTV > 0) {
					mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_TV);
				} else {
					mChannelManager.setCurChannelType(ConstServiceType.SERVICE_TYPE_RADIO);
				}
				Log.i(TAG, "LL curFrequency = " + curFrequencyK);
				if ((scanManager.resultOfRadio + scanManager.resultOfDTV) > 0) {
					ch = mChannelManager.getProgramByFre(curFrequencyK);
				} else {
					ch = mChannelManager.getCurProgram();
				}
			} else {
				ch = mChannelManager.getCurProgram();
			}
		}

		if (ch != null) {
			mChannelManager.channelForceChangeByProgramServiceIndex(ch.mServiceIndex, false);
			if (mContext instanceof DtvRoot) {
				((DtvRoot) mContext).getViewChannelInfo().show();
			}
		} else {
			Log.e(TAG, "LL dismiss>> ch==null");
		}

		hasDismiss = true;
		Log.i(TAG, "5555 hasDismiss: " + hasDismiss);
	}

	private int convertString2Integer(String str) {
		int target = 0;
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);

		if (str != null && str != "") {
			try {
				target = Integer.parseInt(p.matcher(str).replaceAll("").trim());
			} catch (NumberFormatException e) {
				// TODO: handle exception
				Log.e(TAG, "LL convertString2Integer()>>NumberFormatException***" + str);
				target = 0;
			}
		} else {
			Log.e(TAG, "LL convertString2Integer()>>input is null");
		}
		return target;
	}

	class StopScanTask implements Runnable {
		public void run() {
			syncScanStop();
			handler.sendEmptyMessage(MenuScan.DIALOG_EXIT_MESSAGE);
		}
	}

	public void setPositon(int xoff, int yoff) {
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = xoff;
		lp.y = yoff;
		//		this.xoff = xoff;
		//		this.yoff = yoff;
		window.setAttributes(lp);
	}

	public void setShowFilter(boolean isShowFilter) {
		this.isShowFilter = isShowFilter;
	}

	public boolean isShowFilter() {
		return isShowFilter;
	}
}