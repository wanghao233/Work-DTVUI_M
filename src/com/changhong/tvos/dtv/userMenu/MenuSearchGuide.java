package com.changhong.tvos.dtv.userMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.changhong.data.pageData.itemData.ItemRadioButtonData;
import com.changhong.menuView.itemView.ItemRadioButtonView;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.scan.MenuScan;
import com.changhong.tvos.dtv.scan.ScanManager;
import com.changhong.tvos.dtv.scan.ScanManager.scantype;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstOperatorState;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstProductType;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.vwidget.wheel.DTVProviderList;
import com.vwidget.wheel.DTVProviderSelector;
import com.vwidget.wheel.DTVProvider;
import com.vwidget.wheel.OnWheelOKListener;
import com.vwidget.wheel.WheelView;

public class MenuSearchGuide extends Dialog implements android.view.View.OnClickListener {

	private LinearLayout providorReboot;
	@SuppressWarnings("unused")
	private LinearLayout confirmContainer;
	private LinearLayout ProvidorContainer;

	private LinearLayout ScanfilterContainer;
	private static LinearLayout ScanContainer;
	private static LinearLayout filterContainer;

	private LinearLayout scanconfirmcontainer;

	private Button btnNext;
	private Button btnSure;
	private Button btnCancel;
	private Button btnStop;

	private ItemRadioButtonView btnCable;
	private ItemRadioButtonView btnWireLess;

	private static boolean isShowScan = false; //判断是否处于最后阶段

	private static final String TAG = "MenuSearchGuide";
	private View mContentView;

	private Context mContext;
	private boolean isShowTv;
	private Runnable dissMissRunnable;
	private Handler mHandler;
	private int duration = 300000;

	public ViewFlipper filpper;
	public int showID;
	@SuppressWarnings("unused")
	private TextView warningText;
	private DtvSourceManager mSourceManager;
	private int curSelectSourceId;
	private ImageView indication;
	private ItemRadioButtonData cableData;
	private ItemRadioButtonData wireLessData;

	// /////////////////////////////////////////////////////////////////
	private List<DtvOperator> operatorListtmp;
	List<String> mOperatorNameList = null;
	DTVProviderSelector dtvprovider_selector;
	DTVProviderList dtvprovider_list;
	private DtvOperatorManager mDtvOperatorManager;
	//	private List<DtvOperator> operatorList;
	private Map<String, List<DtvOperator>> data;

	int searchguide_step = 0;
	int currentProvidorCode = 0;
	private boolean isGuideMode;
	private boolean ProvidorChanged = false;

	/////////////////////////////////
	//	static MenuScan menuAutoScan;
	ScanManager scanManager;
	//	static scantype mscanType;
	MenuScan menuAutoScan;
	public static Handler handler;
	private static boolean isbtnsure = false;
	private static ProgressBar guide_progressBar;
	private static TextView guide_resultDTV;
	private static TextView guide_resultbroadcast;
	private static TextView guide_Modulmode;
	private static TextView guide_Symbolrate;
	private static TextView guide_Frequency;
	private static TextView guide_Scansource;
	private static TextView guide_FilterChannelName;
	private static TextView guide_FilterStep;

	static int guide_progressBarvalue = 0;
	static String guide_resultDTVstr = "";
	static String guide_resultbroadcaststr = "";
	static String guide_Modulmodestr = "";
	static String guide_Symbolratestr = "";

	// /////////////////////////////////////////////////////////////////////////////////
	public void setGuideMode(boolean isGuideMode) {
		this.isGuideMode = isGuideMode;
	}

	public boolean isGuideMode() {
		return isGuideMode;
	}

	// ///////////////////////////////////////////////////////////
	public void RebootProvidorChanged(final int tmpcode) {
		DtvChannelManager.getInstance().reset();
		DtvConfigManager.getInstance().clearAll();

		DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OP_GUIDE, "true");
		MenuManager.getInstance().delAllScheduleEvents();
		filpper.showNext();
		btnNext.setVisibility(View.GONE);
		searchguide_step = 2;
		indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide3));

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDtvOperatorManager.setCurOperatorByCode(tmpcode);
				PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
				powerManager.reboot("");
			}
		}, 5000);
	}

	private void initDtvProviderData() {
		// TODO Auto-generated method stub
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

	//////////////////////////////////////////////////////////////////////
	public View getproviderView() {
		// TODO Auto-generated method stub
		initDtvProviderData();
		dtvprovider_selector = new DTVProviderSelector(mContext, dtvprovider_list);
		dtvprovider_selector.setTextSize(36);
		initCurDtvProvider();
		this.ProvidorChanged = false;
		dtvprovider_selector.SetOnclickListener(new OnWheelOKListener() {

			@Override
			public void onOK(WheelView view, int code) {
				// TODO Auto-generated method stub
				if (code != currentProvidorCode) {
					ProvidorChanged = true;
					RebootProvidorChanged(code);
				}
				Log.d("wheel", "code=====" + code);
			}
		});
		dtvprovider_selector.setProvinceCyclic(true);
		return dtvprovider_selector.getView();
	}

	private void initCurDtvProvider() {
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
					Log.i(TAG, "Province: " + i + ", Provider: " + j);
				}
			}
		}
	}

	public MenuSearchGuide(Context context) {
		// TODO Auto-generated constructor stub
		super(context, R.style.Theme_ActivityTransparent);
		mContext = context;
		// TODO Auto-generated constructor stub
		mContentView = LayoutInflater.from(context).inflate(R.layout.menu_search_guide, null);
		setContentView(mContentView);

		MainMenuRootData.setGuideScan(true);
		isbtnsure = false;
		mDtvOperatorManager = DtvOperatorManager.getInstance();
		currentProvidorCode = mDtvOperatorManager.getCurOperatorCode();
		indication = (ImageView) mContentView.findViewById(R.id.guide_lable);

		/////////////////////////////////////
		//private LinearLayout providorReboot;
		String tmpGuide = DtvConfigManager.getInstance().getValue(ConstOperatorState.OP_GUIDE);
		@SuppressWarnings("unused")
		boolean isGuideMode = Boolean.valueOf(tmpGuide);
		if (!DtvRoot.GetisshowGuideScan()) {
			providorReboot = (LinearLayout) mContentView.findViewById(R.id.providor_reboot);
			providorReboot.addView(getproviderView());
		}
		ProvidorContainer = (LinearLayout) mContentView.findViewById(R.id.flipperdetail);
		ScanfilterContainer = (LinearLayout) mContentView.findViewById(R.id.guidescanfilterdetail);
		ScanContainer = (LinearLayout) mContentView.findViewById(R.id.guidesearch_detail);
		filterContainer = (LinearLayout) mContentView.findViewById(R.id.guidefilter_detail);
		scanconfirmcontainer = (LinearLayout) mContentView.findViewById(R.id.dtvscan_confirm);

		btnCable = (ItemRadioButtonView) mContentView.findViewById(R.id.btnCable);
		btnWireLess = (ItemRadioButtonView) mContentView.findViewById(R.id.btnWireLess);

		btnNext = (Button) mContentView.findViewById(R.id.btnNext);
		btnSure = (Button) mContentView.findViewById(R.id.btnSure);
		btnCancel = (Button) mContentView.findViewById(R.id.btnCancel);
		btnStop = (Button) mContentView.findViewById(R.id.btnStop);

		btnNext.setOnClickListener(this);
		btnWireLess.setOnClickListener(this);
		btnCable.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnStop.setOnClickListener(this);

		btnCable.setSelectStyle(ItemRadioButtonView.SELECT_STYLE_CHECK);
		btnWireLess.setSelectStyle(ItemRadioButtonView.SELECT_STYLE_CHECK);

		filpper = (ViewFlipper) mContentView.findViewById(R.id.flipper);
		warningText = (TextView) mContentView.findViewById(R.id.msg_warning);

		////////////////////////////////////////////////
		guide_Frequency = (TextView) mContentView.findViewById(R.id.dtvscan_Frequency);
		guide_Scansource = (TextView) mContentView.findViewById(R.id.dtvscan_Scansource);
		guide_Symbolrate = (TextView) mContentView.findViewById(R.id.dtvscan_Symbolrate);
		guide_Modulmode = (TextView) mContentView.findViewById(R.id.dtvscan_Modulmode);
		guide_resultDTV = (TextView) mContentView.findViewById(R.id.dtvscan_resultDTV);
		guide_resultbroadcast = (TextView) mContentView.findViewById(R.id.dtvscan_resultbroadcast);
		guide_progressBar = (ProgressBar) mContentView.findViewById(R.id.dtvscan_progressBar);
		guide_FilterChannelName = (TextView) mContentView.findViewById(R.id.dtvscan_FilterChannelName);
		guide_FilterStep = (TextView) mContentView.findViewById(R.id.dtvscan_FilterStep);
		/////////////////////////////////////////////////////////////////////////

		dissMissRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dismiss();
			}
		};
		mHandler = new Handler();

		mSourceManager = DtvSourceManager.getInstance();

		btnNext.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				//upDateMenu();
			}
		});

		cableData = new ItemRadioButtonData(0, (String) mSourceManager.getSourceNameById(ConstSourceID.DVBC), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};

		wireLessData = new ItemRadioButtonData(0, (String) mSourceManager.getSourceNameById(ConstSourceID.DTMB), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};

		searchguide_step = 0;
		btnCable.init(cableData);
		btnWireLess.init(wireLessData);
		btnCable.setTextGravity(Gravity.CENTER);
		btnWireLess.setTextGravity(Gravity.CENTER);
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (isShowTv) {
			MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
		}
		mHandler.removeCallbacks(dissMissRunnable);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		isShowTv = false;
		searchguide_step = 0;
		DtvDialogManager.AddShowDialog(this);
		mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.wave_scale));
		curSelectSourceId = mSourceManager.getCurSourceID();
		if (!isShowScan) {
			if (mSourceManager.getProductType() == ConstProductType.PRODUCT_T) { // DTMB
				btnCable.setVisibility(View.GONE);
				wireLessData.setSelected(true);
			} else if (mSourceManager.getProductType() == ConstProductType.PRODUCT_C) {//DVBC
				btnWireLess.setVisibility(View.GONE);
				cableData.setSelected(true);
			} else {//T+C
				btnWireLess.setVisibility(View.VISIBLE);
				btnCable.setVisibility(View.VISIBLE);
			}
			upDateMenu();
			btnNext.setVisibility(View.VISIBLE);
			btnNext.requestFocus();
			indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide1));
		} else {
			isShowScan = false;
			filpper.showNext();
			btnNext.setVisibility(View.GONE);
			ProvidorContainer.setVisibility(View.GONE);
			ScanfilterContainer.setVisibility(View.VISIBLE);
			ScanContainer.setVisibility(View.VISIBLE);
			scanconfirmcontainer.setVisibility(View.VISIBLE);
			btnSure.requestFocus();
			indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide4));
		}
	}

	public void setShowTv(boolean b) {
		// TODO Auto-generated method stub
		isShowTv = b;
	}

	public boolean isShowTv() {
		return isShowTv;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onKeyDown>> keyCode = " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_SOURCE:
			// case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126:// KEYCODE_CHANGHONGIR_TOOLBAR
		case 170:// KEYCODE_CHANGHONGIR_TV
		case 4135:
			isShowTv = true;
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
			if (!isbtnsure) {
				dismiss();
			}
			return true;

		default:
			mHandler.removeCallbacks(dissMissRunnable);
			//	mHandler.postDelayed(dissMissRunnable, duration);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public static void RefreshGuideScanParameter(String strFrequency, String strScansource, String strSymbolrate, String strModulmode, String strResultDTV, String strResultBroadcast,
			String strFilterChname, String strFilterStep, int intProgress, boolean isFilter) {
		guide_Frequency.setText(strFrequency);
		guide_Scansource.setText(strScansource);
		guide_Symbolrate.setText(strSymbolrate);
		guide_Modulmode.setText(strModulmode);
		guide_resultDTV.setText(strResultDTV);
		guide_resultbroadcast.setText(strResultBroadcast);

		if (isFilter) {
			ScanContainer.setVisibility(View.GONE);
			filterContainer.setVisibility(View.VISIBLE);
			guide_FilterChannelName.setText(strFilterChname);
			guide_FilterStep.setText(strFilterStep);
		}
		guide_progressBar.setProgress(intProgress);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btnCable:
			if (curSelectSourceId != ConstSourceID.DVBC) {
				DtvProgram curDtvProgram = DtvChannelManager.getInstance().getCurProgram();
				if (null != curDtvProgram) {
					// 切换源之前将当前节目的节目号保存
					DtvConfigManager.getInstance().setValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())), String.valueOf(curDtvProgram.mProgramNum));
				}
				Log.i(TAG, "cuixy curSelectSourceId: " + curSelectSourceId);
				curSelectSourceId = ConstSourceID.DVBC;
				mSourceManager.setCurSource(curSelectSourceId);
				upDateMenu();
				// mSourceManager.setCurSource(mSourceManager.getSourceIDByIndex(curSelectSourceId));
				DtvChannelManager.getInstance().setCurChannelType(DtvChannelManager.getInstance().getBootChannelType());
				Log.i(TAG, "cuixy demodeType: " + mSourceManager.getCurDemodeType());
				// 读取保存的节目， 如果没有，则用startBoot的那个节目号
				String tmp = DtvConfigManager.getInstance().getValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())));
				if (tmp == null) {
					DtvChannelManager.getInstance().channelForceChangeByProgramNum(DtvChannelManager.getInstance().getBootChannelNum(), true);
				} else {
					DtvChannelManager.getInstance().channelForceChangeByProgramNum(Integer.valueOf(tmp), true);
				}
			}
			btnNext.requestFocus();
			break;

		case R.id.btnWireLess:
			if (curSelectSourceId != ConstSourceID.DTMB) {
				DtvProgram curDtvProgram = DtvChannelManager.getInstance().getCurProgram();
				if (null != curDtvProgram) {
					// 切换源之前将当前节目的节目号保存
					DtvConfigManager.getInstance().setValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())), String.valueOf(curDtvProgram.mProgramNum));
				}
				Log.i(TAG, "cuixy curSelectSourceId: " + curSelectSourceId);
				curSelectSourceId = ConstSourceID.DTMB;
				mSourceManager.setCurSource(curSelectSourceId);
				upDateMenu();
				//	mSourceManager.setCurSource(mSourceManager.getSourceIDByIndex(curSelectSourceId));
				DtvChannelManager.getInstance().setCurChannelType(DtvChannelManager.getInstance().getBootChannelType());
				Log.i(TAG, "cuixy demodeType: " + mSourceManager.getCurDemodeType());
				// 读取保存的节目， 如果没有，则用startBoot的那个节目号
				String tmp = DtvConfigManager.getInstance().getValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())));
				if (tmp == null) {
					DtvChannelManager.getInstance().channelForceChangeByProgramNum(DtvChannelManager.getInstance().getBootChannelNum(), true);
				} else {
					DtvChannelManager.getInstance().channelForceChangeByProgramNum(Integer.valueOf(tmp), true);
				}
			}
			btnNext.requestFocus();
			break;

		case R.id.btnNext:
			searchguide_step++;
			Log.i("btnNext", "btnNext-----00000====" + searchguide_step);
			switch (searchguide_step) {
			case 0:
				indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide1));
				break;
			case 1:
				if (curSelectSourceId == ConstSourceID.DTMB) {
					indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide4));
					searchguide_step = 3;
				} else {
					indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide2));
				}
				break;
			case 2:
				indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide3));
				break;
			case 3:
				indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide4));
				break;
			}

			if (searchguide_step < 2) {
				filpper.showNext();
			} else {
				if (ProvidorChanged) {
					filpper.showNext();
				} else {
					indication.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtv_search_guide4));
					btnNext.setVisibility(View.GONE);
					ProvidorContainer.setVisibility(View.GONE);
					ScanfilterContainer.setVisibility(View.VISIBLE);
					ScanContainer.setVisibility(View.VISIBLE);
					scanconfirmcontainer.setVisibility(View.VISIBLE);
					btnSure.requestFocus();
				}
			}
			break;

		case R.id.btnSure:
			//	btnCancel.requestFocus();
			scanconfirmcontainer.setVisibility(View.GONE);
			btnStop.setVisibility(View.VISIBLE);
			btnStop.requestFocus();
			isbtnsure = true;
			menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
			menuAutoScan.Scanready();
			break;

		case R.id.btnCancel:
			isbtnsure = false;
			MainMenuRootData.setGuideScan(false);
			cancel();
			break;

		case R.id.btnStop:
			if (isbtnsure) {
				menuAutoScan.Scantermined();
				isbtnsure = false;
				MainMenuRootData.setGuideScan(false);
				cancel();
			}
			break;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mHandler.removeCallbacks(dissMissRunnable);
			//	mHandler.postDelayed(dissMissRunnable, duration);
		} else {
			mHandler.removeCallbacks(dissMissRunnable);
		}
	}

	public void setOnSureListener(View.OnClickListener listener) {
		if (null != listener) {
			//	btnSure.setOnClickListener(listener);
		}
	}

	public void setOnCancelListener(View.OnClickListener listener) {
		if (null != listener) {
			btnCable.setOnClickListener(listener);
		}
	}

	public void setShowScan(boolean showScan) {
		isShowScan = showScan;
	}

	private void upDateMenu() {
		if (curSelectSourceId == ConstSourceID.DVBC) {
			cableData.setSelected(true);
			wireLessData.setSelected(false);
		} else {
			cableData.setSelected(false);
			wireLessData.setSelected(true);
		}
		btnCable.update();
		btnWireLess.update();
	}
}