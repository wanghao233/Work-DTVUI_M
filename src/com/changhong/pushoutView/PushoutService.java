package com.changhong.pushoutView;

import java.util.ArrayList;
import java.util.List;
import com.changhong.tvos.common.SystemManager;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.common.exception.SourceNotFoundException;
import com.changhong.tvos.common.exception.TVManagerNotInitException;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.provider.BaseProgramManager;
import com.changhong.tvos.dtv.scan.ScanManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.vo.TimerInfo;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.model.ENUMPlayerScenes;
import com.changhong.tvos.model.EnumInputSource;
import com.changhong.tvos.model.ChOsType.EnumPipInputSource;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.pushoutView.MagicBaseButton;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class PushoutService extends Service {

	private static final String TAG = "DTVpushview";

	private static WindowManager windview;
	private static Context mContext;
	private static WindowManager.LayoutParams params = null;
	private static RelativeLayout parent = null;

	// private static FocusViewManager focus;

	private BusinessLocalHot mBusinessLocalHot;
	private BusinessProgramLinkOnline mBusinessProgramLinkOnline;
	private BusinessVCR mBusinessVCR;
	private BusinessHotOnline mBusinessHotOnline;
	private BusinessHotOnline mBusinessHotOnlineSecond;
	private BusinessPushToUserprogram mBusinessPushToUserprogram;
	private BusinessUserAdded mBusinessUserAdded;
	private SparseArray<MagicBaseButton> viewlist;
	// private SparseArray<MagicBaseButton> viewvisblelist;
	private HorizontalListView mHoriListView;
	private FocusBoxView mFocusBoxView;
	// private boolean isViewShowed = false;

	private int nowfoucus = -1;
	private int visiblebutton = 0;

	private int mStartType;

	SystemManager systemmanage;

	List<BaseChannel> mBaseChannelList;

	private boolean startBusiness = false;

	protected Object MagicBaseButton;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "the service was oncreat1");
		mContext = getApplicationContext();
		windview = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		parent = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.push_out_view, null);

		mBusinessLocalHot = new BusinessLocalHot(mContext);
		mBusinessProgramLinkOnline = new BusinessProgramLinkOnline(mContext);
		mBusinessVCR = new BusinessVCR(mContext);
		mBusinessHotOnline = new BusinessHotOnline(mContext);
		mBusinessHotOnlineSecond = new BusinessHotOnline(mContext);
		mBusinessHotOnlineSecond.setProgrameIdx(1);
		mBusinessPushToUserprogram = new BusinessPushToUserprogram(mContext);
		mBusinessUserAdded = new BusinessUserAdded(mContext);

		viewlist = new SparseArray<MagicBaseButton>();
		viewlist.append(0, mBusinessLocalHot);
		viewlist.append(1, mBusinessProgramLinkOnline);
		viewlist.append(2, mBusinessVCR);
		viewlist.append(3, mBusinessHotOnline);
		viewlist.append(4, mBusinessHotOnlineSecond);
		viewlist.append(5, mBusinessPushToUserprogram);

		mHoriListView = new HorizontalListView(mContext);

		mFocusBoxView = new FocusBoxTranslateAnimView(mContext);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		parent.addView(mFocusBoxView, lp);

		mHoriListView.setFocusBoxView(mFocusBoxView);

		mBusinessUserAdded.setOnExitDone(new HorizontalListView.OnExitDone() {

			@Override
			public void onExitDone() {
				// TODO Auto-generated method stub
				Log.d(TAG, "businessuseradded is ready to exit");
				mHandler.removeCallbacks(poseThread);
				mHandler.post(poseThread);
			}

		});

		mHoriListView.setOnExitDone(new HorizontalListView.OnExitDone() {

			@Override
			public void onExitDone() {
				// TODO Auto-generated method stub
				try {
					parent.removeView(mHoriListView);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				try {
					windview.removeView(parent);
					startBusiness = false;
					mStartType = MagicButtonCommon.START_TYPE_NONETYPE;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					startBusiness = false;
				}

				try {
					systemmanage = TVManager.getInstance(mContext).getSystemManager();
					systemmanage.unlockScreenSaver();
					Log.i("ScreenSaver", "unlockScreenSaver---onExitDone---unlockScreenSaver");
				} catch (TVManagerNotInitException e1) {
					// TODO Auto-generated catch block
					Log.d(TAG, "unlockScreenSaver error");
					e1.printStackTrace();
				}
				MagicButtonCommon.print(false, "mHoriListView--------onExitDone");
			}
		});

		mHoriListView.setOnKeyDownListener(new HorizontalListView.OnKeyDownListener() {

			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event, View focusedView) {
				// TODO Auto-generated method stub
				// up key to demo remove a view with anim
				Log.d(TAG, "the keyevent is " + event);
				Log.d(TAG, "the parent width is " + parent.getWidth() + "the parent hight is " + parent.getHeight() + "~~~~~~~~~~" + parent.getX() + "~~~~~~~~" + parent.getY());
				Log.d(TAG, "the mHoriListView longth is " + mHoriListView.getWidth() + "mHoriListView height is " + mHoriListView.getHeight());
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					mHandler.removeCallbacks(poseThread);
					mHandler.post(poseThread);
					return true;
				} else {
					mHandler.removeCallbacks(poseThread);
					mHandler.postDelayed(poseThread, MagicButtonCommon.PUSH_OUT_VIEW_STAY_TIME);
				}
				return false;
			}
		});

		params = new WindowManager.LayoutParams();
		params.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		params.format = PixelFormat.TRANSPARENT;
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		params.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				Log.d(TAG, "pushout view is start to show");
				// 本地节目为空不显示 2015-4-16
				if (DtvChannelManager.getInstance().getChannelList() == null || DtvChannelManager.getInstance().getChannelList().size() == 0) {
					Log.i(TAG, "start show but returned, because local channelList is " + DtvChannelManager.getInstance().getChannelList());
					if (DtvChannelManager.getInstance().getChannelList() != null) {
						Log.i(TAG, "start show but returned, because local channelList size is " + DtvChannelManager.getInstance().getChannelList().size());
					}
					return;
				}

				// 别名库为空不显示
				/*
				 * if(mBaseChannelList.size() == 0) { Log.d(TAG,
				 * "start show but returned,	 because of the chanellist size is 0 call dinglei"
				 * ); return; }
				 */

				// 正在搜索不显示
				if (ScanManager.isSearching()) {
					Log.i(TAG, "start show but returned,	because of the channls isSearching");
					return;
				}

				if (MainMenuRootData.isFilterMenuShow) {// 2015-4-28 YangLiu
					Log.i(TAG, "start show but returned,	because of the channls filter is Showing");
					return;
				}

				// 正在过滤不显示 2015-4-16
				if (FilterChannels.getInstance(mContext).isFilter()) {
					Log.i(TAG, "start show but returned,	because of the channls isFiltering");
					return;
				}

				// 多窗口不显示
				Log.d(TAG,"SystemProperties.get(sys.multiwindow.state)="+SystemProperties.get("sys.multiwindow.state", "0"));
				if(!"0".equals(SystemProperties.get("sys.multiwindow.state", "0"))){
					Log.i(TAG, "start show but returned,	because of the now is multWindow mode");
					return;
				}

				try {
					systemmanage = TVManager.getInstance(mContext).getSystemManager();
					// systemmanage.leaveScreenSaver();
					leaveScreenSaverIntent();
					Log.i("ScreenSaver", "pushout view is start---leaveScreenSaver");
					systemmanage.lockScreenSaver();
					Log.i("ScreenSaver", "pushout view is start---lockScreenSaver");
				} catch (TVManagerNotInitException e1) {
					// TODO Auto-generated catch block
					Log.d(TAG, "lockScreenSaver error");
					e1.printStackTrace();
				}
				mHandler.removeCallbacksAndMessages(null);
				mHandler.sendEmptyMessageDelayed(1, 5000);// 2015-3-16
				break;
				
			case 1:
				EnumInputSource mEnumInputSource = null;
				ENUMPlayerScenes mENUMPlayerScenes = null;
				try {
					mEnumInputSource = TVManager.getInstance(mContext).getSourceManager().getCurInputSource(EnumPipInputSource.E_MAIN_INPUT_SOURCE);
					mENUMPlayerScenes = TVManager.getInstance(mContext).getTVPlayer().getCurrentPlayerScenes();
				} catch (SourceNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TVManagerNotInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.d(TAG, "now source is " + mEnumInputSource);
				Log.d(TAG, "now mStartType is " + mStartType);
				if (mEnumInputSource == null) {
					Log.d(TAG, "get now source failed ,tvos return a null mEnumInputSource");
					startBusiness = false;
					return;
				}

				if (mStartType == MagicButtonCommon.START_TYPE_NONETYPE) {
					Log.d(TAG, "return of the start type is none");
					startBusiness = false;
					return;
				} else {
					if (!mEnumInputSource.equals(EnumInputSource.E_INPUT_SOURCE_DTV)) {
						Log.d(TAG, "push out  by dtv but now at source of " + mEnumInputSource);
						startBusiness = false;
						return;
					} else if (!ENUMPlayerScenes.EN_PLAYER_SCENES_DTV.equals(mENUMPlayerScenes)) {
						Log.d(TAG, "push out  by dtv but now at playerScenes of " + mENUMPlayerScenes);
						startBusiness = false;
						return;
					}
				}

				mHoriListView.clearAllView();
				visiblebutton = 0;
				for (int i = 0; i < viewlist.size(); i++) {
					if (viewlist.get(i).getShowable()) {
						Log.d(TAG, "the" + viewlist.get(i).getBusinessName() + "is ready");
						mHoriListView.addChildView(viewlist.get(i));
						visiblebutton++;
					} else {
						Log.d(TAG, "the" + viewlist.get(i).getBusinessName() + "is not ready");
					}
				}

				Log.d(TAG, "the visiblebutton = " + visiblebutton);
				if (visiblebutton == 0) {// mHoriListView.getChildCount()	2015-3-12
					Log.d(TAG, "the view list has not child");
					mHandler.removeCallbacks(poseThread);
					mHandler.post(poseThread);
					startBusiness = false;
					return;
				}

				// add the HoriListView to the parent layout
				RelativeLayout.LayoutParams mHoriListViewlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				DisplayMetrics metric = new DisplayMetrics();
				windview.getDefaultDisplay().getMetrics(metric);
				Log.d(TAG, "the divice with is " + metric.widthPixels + "the device height is " + metric.heightPixels);
				parent.addView(mHoriListView, mHoriListViewlp);
				mHoriListView.setX(1920 - 230 * visiblebutton - 10 * (visiblebutton) - 20);// 离右边要有些距离
				// mHoriListView.setX(1920-230*visiblebutton -
				// 10*(visiblebutton-1));
				mHoriListView.setY(570);

				try {
					windview.addView(parent, params);
					startBusiness = true;

					if (ScanManager.isSearching()) {// 2015-3-20
						Log.i(TAG, "isSearching and return");
						hidePushView(1);// hide by self
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.d(TAG, "return of the view added wrong");
					e.printStackTrace();
					startBusiness = false;
					return;
				}
				doAnimationAlpha();
				parent.bringChildToFront(mFocusBoxView);
				mHoriListView.requestFocusExt();
				mHandler.removeCallbacks(poseThread);
				Log.d(TAG, "the parent width is " + parent.getWidth() + "the parent hight is " + parent.getHeight() + "~~~~~~~~~~" + parent.getX() + "~~~~~~~~" + parent.getY());
				mHandler.postDelayed(poseThread, MagicButtonCommon.PUSH_OUT_VIEW_STAY_TIME);
				break;
				
			case 2:
				try {
					systemmanage = TVManager.getInstance(mContext).getSystemManager();
					// systemmanage.leaveScreenSaver();
					leaveScreenSaverIntent();
					Log.i("ScreenSaver", "mBusinessUserAdded view is show---leaveScreenSaver");
					systemmanage.lockScreenSaver();
					Log.i("ScreenSaver", "mBusinessUserAdded view is show---lockScreenSaver");
				} catch (TVManagerNotInitException e1) {
					// TODO Auto-generated catch block
					Log.d(TAG, "lockScreenSaver error");
					e1.printStackTrace();
				}

				RelativeLayout.LayoutParams mBusinessUserAddedlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				DisplayMetrics metric1 = new DisplayMetrics();
				windview.getDefaultDisplay().getMetrics(metric1);
				Log.d(TAG, "the divice with is " + metric1.widthPixels + "the device height is " + metric1.heightPixels);
				parent.addView(mBusinessUserAdded, mBusinessUserAddedlp);
				mBusinessUserAdded.requestFocus();

				try {
					windview.addView(parent, params);
					startBusiness = true;
					if (ScanManager.isSearching()) {// 2015-3-20
						Log.i(TAG, "isSearching and return");
						hidePushView(1);// hide by self
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.d(TAG, "return of the view added wrong");
					e.printStackTrace();
					startBusiness = false;
					return;
				}
				mHandler.postDelayed(poseThread, MagicButtonCommon.PUSH_OUT_VIEW_STAY_TIME);
				break;
			default:
				break;
			}
		}
	};

	private void doAnimationAlpha() {

		Animation mAlphaAnimation;
		mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		mAlphaAnimation.setDuration(100);
		mAlphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				mHoriListView.hideChildViewOnScreen();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub、
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				// mHandler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				mHoriListView.playEntryAnima();
				// }
				// },500);
			}
		});
		mHoriListView.startAnimation(mAlphaAnimation);
	}

	private Runnable poseThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!startBusiness) {
				Log.d(TAG, "recived exit post but i'am not on show");
				return;
			}
			if (mStartType == MagicButtonCommon.START_TYPE_USERADD) {
				try {
					parent.removeView(mBusinessUserAdded);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				try {
					windview.removeView(parent);
					startBusiness = false;
					mStartType = MagicButtonCommon.START_TYPE_NONETYPE;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {
					systemmanage = TVManager.getInstance(mContext).getSystemManager();
					systemmanage.unlockScreenSaver();
					Log.i("ScreenSaver", "recived mBusinessUserAdded exit---leaveScreenSaver");
				} catch (TVManagerNotInitException e1) {
					// TODO Auto-generated catch block
					Log.d(TAG, "unlockScreenSaver error");
					e1.printStackTrace();
				}
			} else {
				mFocusBoxView.playExitAnim();
			}
		}
	};

	private void creatBaseChannels() {
		ContentResolver contentResolver = mContext.getContentResolver();
		Uri uri = Uri.parse(MagicButtonCommon.DTV_CHANNEL_TABLE_LIST_ADRESS);
		Cursor cursor = contentResolver.query(uri, new String[] { "name", "[index]", "type", "code" }, null, null, null);

		if (cursor == null || cursor.getCount() == 0) {
			return;
		}

		BaseChannel mBaseChannel = null;
		while (cursor.moveToNext()) {
			mBaseChannel = new BaseChannel();
			mBaseChannel.setName(cursor.getString(0));
			mBaseChannel.setIndex(cursor.getInt(1));
			mBaseChannel.setType(cursor.getString(2));
			mBaseChannel.setCode(cursor.getString(3));

			mBaseChannelList.add(mBaseChannel);
			mBaseChannel = null;
		}
		cursor.close();
	}

	// 0 hide by other 1 hide by self
	private void hidePushView(int hidesource) {
		Log.i(TAG, "hidePushView	hidesource=" + hidesource);
		mHandler.removeCallbacks(poseThread);
		mHandler.post(poseThread);
	}

	@Override
	public void onStart(Intent intent, int startId) {

		// the parent
		Log.d(TAG, "some one start me");

		String control = null;
		TimerInfo timerInfo = null;
		BaseProgram baseProgram = null;
		Bundle mBundle = null;

		try {
			mBundle = intent.getExtras();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			timerInfo = (TimerInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
			// Log.i(TAG, "timerInfo="+timerInfo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.i(TAG, "get timerInfo's erro="+e.getMessage().toString());
		}

		try {
			baseProgram = BaseProgramManager.convertTimerInfoToScheduleProgramInnerClassUsed(timerInfo);
			// Log.i(TAG, "baseProgram="+baseProgram);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.i(TAG, "get baseProgram's erro="+e.getMessage().toString());
		}

		try {
			control = intent.getStringExtra("control");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.i(TAG, "get control's erro="+e.getMessage().toString());
		}
		if ("stopall".equals(control)) {
			mHandler.removeCallbacksAndMessages(null);
			hidePushView(0);
			return;
		}

		if ("hide".equals(control)) {
			hidePushView(0);
			return;
		}
		if ("deviceready".equals(control)) {
			mBusinessVCR.setReciveport(intent.getIntExtra(("deviceport"), -1));
			;
			return;
		}

		if (startBusiness) {
			Log.d(TAG, "business was started before so we return");
			return;
		}

		// 加入本地节目为空的处理 2015-4-16
		if (DtvChannelManager.getInstance().getChannelList() == null || DtvChannelManager.getInstance().getChannelList().size() == 0) {
			Log.i(TAG, "returned because of the local channelList is " + DtvChannelManager.getInstance().getChannelList());
			if (DtvChannelManager.getInstance().getChannelList() != null) {
				Log.i(TAG, "returned because of the local channelList size is " + DtvChannelManager.getInstance().getChannelList().size());
			}
			return;
		}

		if (mBaseChannelList == null || mBaseChannelList.size() == 0) {
			mBaseChannelList = new ArrayList<BaseChannel>();
			creatBaseChannels();
		}

		if (mBaseChannelList.size() == 0) {
			Log.d(TAG, "returned because of the chanellist size is 0 call dinglei");
			return;
		}

		EnumInputSource mEnumInputSource = null;
		try {
			mEnumInputSource = TVManager.getInstance(mContext).getSourceManager().getCurInputSource(EnumPipInputSource.E_MAIN_INPUT_SOURCE);
		} catch (SourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TVManagerNotInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(TAG, "now source is " + mEnumInputSource);
		Log.d(TAG, "now timerInfo is " + timerInfo);
		if (mEnumInputSource == null) {
			Log.d(TAG, "get now source failed ,tvos return a null mEnumInputSource");
			return;
		}

		mStartType = MagicButtonCommon.START_TYPE_NONETYPE;

		if (timerInfo == null && mEnumInputSource.equals(EnumInputSource.E_INPUT_SOURCE_DTV)) {
			Log.d(TAG, "start first show ,need show two hot online");
			mBusinessLocalHot.setShowable(true);
			mBusinessProgramLinkOnline.setShowable(true);
			mBusinessVCR.setShowable(true);
			mBusinessHotOnline.setShowable(true);
			mBusinessHotOnlineSecond.setShowable(true);
			mBusinessPushToUserprogram.setShowable(false);
			mStartType = MagicButtonCommon.START_TYPE_RESTARTSHOW;
			for (int i = 0; i < viewlist.size(); i++) {
				viewlist.get(i).setInfo(baseProgram, mBaseChannelList);
			}
			startBusiness = true;
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessageDelayed(0, 20000);

		} else if (timerInfo != null && timerInfo.mOriginal == 2 && mEnumInputSource.equals(EnumInputSource.E_INPUT_SOURCE_DTV)) {
			Log.d(TAG, "start push to user show ");
			Log.d(TAG, "start push to user show mOriginal=2 \nnow source is " + mEnumInputSource);

			mBusinessLocalHot.setShowable(true);
			mBusinessProgramLinkOnline.setShowable(true);
			mBusinessVCR.setShowable(true);
			mBusinessHotOnline.setShowable(true);
			mBusinessHotOnlineSecond.setShowable(false);
			mBusinessPushToUserprogram.setShowable(true);
			mStartType = MagicButtonCommon.START_TYPE_PUSHOUTSHOW;
			for (int i = 0; i < viewlist.size(); i++) {
				viewlist.get(i).setInfo(baseProgram, mBaseChannelList);
			}
			startBusiness = true;
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessageDelayed(0, 10000);

		} else if (timerInfo != null && timerInfo.mOriginal == 1) {

			mBusinessUserAdded.setInfo(baseProgram, mBaseChannelList);
			mStartType = MagicButtonCommon.START_TYPE_USERADD;
			startBusiness = true;
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessageDelayed(2, 10000);

		} else {
			Log.d(TAG, "timerInfo is  " + timerInfo);
			Log.d(TAG, "now source is " + mEnumInputSource);
			Log.d(TAG, "not at the right source so return");
			return;
		}
	}

	private void leaveScreenSaverIntent() {
		Intent intent = new Intent("com.changhong.CKYquitscsr");
		mContext.sendBroadcast(intent);
	}
}