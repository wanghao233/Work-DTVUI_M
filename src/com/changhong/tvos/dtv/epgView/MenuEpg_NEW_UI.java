package com.changhong.tvos.dtv.epgView;

import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.util.ViewChannelInfo;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.system.commondialog.CommonProgressInfoDialog;

public class MenuEpg_NEW_UI extends Dialog {
	public static String TAG = "MenuEpg";
	private static final int EPG_LEFT_MARGIN = 5;
	private static final int EPG_TOP_MARGIN = 5;
	private Context mContext = null;
	private RelativeLayout mBg = null;
	public static boolean[] hasReceiveEpg = null;

	private boolean showTvChannelInfo = false;
	private DtvProgram preProgram = null;
	private ViewChannelInfo viewChannelInfo;
	private DtvProgram mEpgProgram = null;
	private CommonProgressInfoDialog mEpgRecevieDialog = null;
	private DtvChannelManager mChannelManager = DtvChannelManager.getInstance();
	private SmartViewGroup mEpgView = null;
	private LinearLayout.LayoutParams mEpgViewParams = null;
	private int stopTimes = 3;
	View mView;
	private long mShowTime = 30000;
	private Runnable stopChannelRun = null;
	Handler mHandler = new Handler();
	Runnable mRunShow = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mContext != null && (mContext instanceof Activity) && (!((Activity) mContext).isFinishing())) {
				Log.i(TAG, "LL mContext = " + mContext + "isFinish = " + ((Activity) mContext).isFinishing());
				dismiss();
			}
		}
	};

	public MenuEpg_NEW_UI(Context context, int theme) {
		super(context, theme);
		mContext = context;

	}

	public MenuEpg_NEW_UI(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MenuEpg_NEW_UI(Context context) {
		super(context, R.style.Theme_ActivityTransparent);

		mContext = context;
		// add grouView
		mBg = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_null, null);
		mView = (View) mBg;
		viewChannelInfo = new ViewChannelInfo(context);
		mBg.addView(viewChannelInfo);

		mEpgView = new SmartViewGroup(mContext);
		mEpgView.setChangeChannelListener(new SmartViewGroup.ChangeChannelListener() {

			@Override
			public void showChannelInfo() {
				//				viewChannelInfo.show();
				setShowTvChannelInfo(true);
			}

			@Override
			public void dissMissDialog() {
				dismiss();
			}
		});
		mEpgView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Onclick father dialog click");
				refeshShowTime();

			}
		});
		mEpgViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mEpgViewParams.leftMargin = EPG_LEFT_MARGIN;
		mEpgViewParams.topMargin = EPG_TOP_MARGIN;
		mBg.addView(mEpgView, mEpgViewParams);
		mBg.setGravity(Gravity.LEFT);

		setContentView(mBg);

		mEpgView.getEpgView(MenuEpg_NEW_UI.this);
		mEpgView.requestFocus();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		mEpgProgram = mChannelManager.getEpgProgram();
		if (mEpgProgram == null) {
			hasCurSourceReceiveEpg(true, true);
		}
		Log.i(TAG, "LL mEpgProgram = " + mEpgProgram + ", hasReceiveEpg = " + hasReceiveEpg);

		mEpgView.resetWeekDay();

		super.show();

		if (mEpgProgram != null && hasCurSourceReceiveEpg(false, false) == false) {
			// 等待获取epg信息，先跳台到主频点播放，获取到epg之后，再返回到之前播放的节目
			this.showReceiveDialog();

			preProgram = mChannelManager.getPreChannel();

			mChannelManager.channelStop();
			mChannelManager.channelChangeByProgramNum(mEpgProgram.getProgramNum(), false);
			if (null == stopChannelRun) {
				stopChannelRun = new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						mChannelManager.channelStop();
						if (stopTimes-- > 0) {
							mHandler.postDelayed(stopChannelRun, 1000);
						} else {
							mHandler.removeCallbacks(stopChannelRun);
						}
					}
				};
				mHandler.postDelayed(stopChannelRun, 1000);
			}

		} else {
			mEpgView.init();
		}
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(stopChannelRun);
		mHandler.removeCallbacks(mRunShow);
		mEpgView.onDestroy();
		super.dismiss();
	}

	public void update() {
		mEpgView.curProgramChange();
	}

	private void showReceiveDialog() {
		Log.i(TAG, "LL [enter]showReceiveDialog()***");
		mHandler.removeCallbacks(mRunShow);
		mEpgRecevieDialog = new CommonProgressInfoDialog(mContext);
		mEpgRecevieDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
				hasCurSourceReceiveEpg(true, true);
				mHandler.removeCallbacks(stopChannelRun);
				mChannelManager.channelChangeReturn();
				if (null != preProgram) {
					mChannelManager.setPreChannel(preProgram);
				}
				mEpgView.getEpgInfoTimes = mEpgView.GET_EPG_TIMES;
				mEpgView.init();

			}
		});

		mEpgRecevieDialog.setMessage(mContext.getString(R.string.menu_epg_get_epg));
		mEpgRecevieDialog.setButtonVisible(false);
		mEpgRecevieDialog.setDuration(30);
		mEpgRecevieDialog.show();

		Log.i(TAG, "LL [end]showReceiveDialog()***");
	}

	public void refeshShowTime() {
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "LL MenuEpg_NEW_UI>>onKeyDown>>keyCode = " + keyCode);
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);

		boolean isToSuper = mEpgView.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_BACK:
			break;
		case KeyEvent.KEYCODE_SOURCE:
			//		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
		case KeyEvent.KEYCODE_MENU:
			dismiss();
			break;
		case KeyEvent.KEYCODE_RED:

			break;
		case KeyEvent.KEYCODE_GREEN:

			break;
		default:
			break;
		}
		if (isToSuper) {
			return super.onKeyDown(keyCode, event);
		} else {
			Log.v(TAG, "MenuEpg return false");
			return true;
		}

	}

	public void setShowTvChannelInfo(boolean showTvChannelInfo) {
		this.showTvChannelInfo = showTvChannelInfo;
	}

	public boolean isShowTvChannelInfo() {
		return showTvChannelInfo;
	}

	public static boolean hasCurSourceReceiveEpg(boolean isUpdate, boolean updateValue) {
		boolean received = false;
		DtvSourceManager sourceManger = DtvSourceManager.getInstance();
		if (hasReceiveEpg == null) {
			List<DTVSource> sourceList = sourceManger.getSourceList();
			if (sourceList != null && sourceList.size() > 0) {
				hasReceiveEpg = new boolean[sourceList.size()];
			} else {
				hasReceiveEpg = new boolean[1];

			}
		}
		if (hasReceiveEpg.length > 1) {

			switch (sourceManger.getCurSourceID()) {
			case ConstSourceID.DVBC:
				if (isUpdate) {
					hasReceiveEpg[0] = updateValue;
					received = hasReceiveEpg[0];
				} else {

					received = hasReceiveEpg[0];
				}
				break;
			case ConstSourceID.DTMB:
				if (isUpdate) {
					hasReceiveEpg[1] = updateValue;
					received = hasReceiveEpg[1];
				} else {

					received = hasReceiveEpg[1];
				}
				break;
			default:
				break;
			}
		} else {
			if (isUpdate) {
				hasReceiveEpg[0] = updateValue;
				received = hasReceiveEpg[0];
			} else {

				received = hasReceiveEpg[0];
			}
		}
		return received;
	}

}
