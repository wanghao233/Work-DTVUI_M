package com.changhong.tvos.dtv.channel_manager;

import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.util.ViewChannelInfo;
import com.changhong.tvos.dtv.util.ViewPromptInfo;

/**
 * @author enlong
 */
public class FilterChannels extends Dialog {
	private static MenuManager mMenuManager = null;
	private static final String TAG = "FilterChannels";
	private DtvInterface dtvinterface = null;
	private boolean isFiltering = false;
	private Button actionButton;
	private DtvChannelManager mChannelManager = null;
	private List<DtvProgram> channList;
	private int curFilteNum;
	private TextView curFilteTextView;
	private TextView curProgressTextView;
	private ProgressBar progressBar;
	private static FilterChannels filterChannels;
	private boolean isShowTv;
	private int preFilteNum;
	private ViewChannelInfo mChannelInfoView = null;
	private ViewPromptInfo mPromptInfoView = null;
	private Handler mHandler;

	private DtvProgram preProgram = null;

	private boolean isMoving = false;
	private int xPostion = 0;
	private int yPostion = 0;
	private int xoff = 0;
	private int yoff = 0;
	private boolean moveRight = true;
	private int xWidth = 0;
	private int yHeight = 0;
	private int xOffset = 20;

	private LinearLayout layout;
	private Runnable moveRun;
	Context myContext;

	private static final int FILTER_UPDATE = 0x01;
	private static final int FILTER_BEGIN = 0x02;
	private static final int FILTER_STOP = 0x03;
	private static final int FILTER_OVER = 0x04;

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

	private Runnable mRunShow = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dismiss();
		}
	};
	private long mShowTime = 30000;

	public FilterChannels(Context context) {
		super(context, R.style.Theme_ActivityTransparent);
		myContext = context;
		init(context);
		this.setCancelable(false);
	}

	public void init(Context context) {

		setContentView(R.layout.channel_filter_dialog);

		layout = (LinearLayout) findViewById(R.id.layout_filter);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		actionButton = (Button) findViewById(R.id.button_filter);
		curFilteTextView = (TextView) findViewById(R.id.curfilter);
		curProgressTextView = (TextView) findViewById(R.id.curprogress);
		mChannelInfoView = (ViewChannelInfo) findViewById(R.id.channelInfo);
		mPromptInfoView = (ViewPromptInfo) findViewById(R.id.promptInfo);
		actionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFiltering) {
					cancelSmartSkip();
				} else {
					startSmartSkip();
				}

			}
		});
		mChannelManager = DtvChannelManager.getInstance();
		channList = mChannelManager.getChannelList();
		dtvinterface = DtvInterface.getInstance();
		mMenuManager = MenuManager.getInstance();

		Window window = this.getWindow();
		yHeight = window.getWindowManager().getDefaultDisplay().getHeight();
		xWidth = window.getWindowManager().getDefaultDisplay().getWidth() - layout.getLayoutParams().width;
		Log.i(TAG, "LL yHeight = " + yHeight + ", xWidth = " + xWidth + ", layout = " + layout.getLayoutParams().width);
		xoff = xWidth / 2;
		setPositon(xoff, yoff);

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {

				case FILTER_UPDATE:
					break;

				case FILTER_BEGIN:
					dtvinterface.SetDtvBusyState(1);
					Log.i("YangLiu", "开始节目过滤");
					startSmartSkip();
					break;

				case FILTER_STOP:
					dtvinterface.SetDtvBusyState(0);
					Log.i("YangLiu", "节目过滤取消");
					cancelSmartSkip();
					dismiss();
					break;

				case FILTER_OVER:
					dtvinterface.SetDtvBusyState(0);
					Log.i("YangLiu", "节目过滤结束");
					endSmartSkip();
					break;
				default:
					break;
				}
			}
		};
	}

	public static FilterChannels getInstance(Context context) {
		if (null == filterChannels) {
			filterChannels = new FilterChannels(context);
		}
		return filterChannels;
	}

	@Override
	public void show() {
		//DtvDialogManager.AddShowDialog(this);
		//	mHandler.removeCallbacks(mRunShow);
		//	mHandler.postDelayed(mRunShow, mShowTime );
		//	super.show();

		reset();
		/*
			 if(moveRun==null){
		        	moveRun =new Runnable() {
		        		public void run() {
		        			// TODO Auto-generated method stub
		        			
		        			if (moveRight) {
		        				if (xoff < xWidth) {
		        					xoff += xOffset;
		        				}else {
		        					moveRight = false;
		        				}
		        			}else {
		        				if (xoff > 0) {
		        					xoff -= xOffset;
		        				}else {
		        					moveRight = true;
		        				}
		        				
		        			}			
		        			Log.i(TAG,"LL this.show()>>xoff = " + xoff);
		        			setPositon(xoff, yPostion);
		        			mHandler.postDelayed(moveRun, 1000);
		        		}
		        	};
		        }
			 */
	}

	private void reset() {
		curFilteNum = 0;
		isFiltering = false;
		/*		
				xoff = xWidth /2;
				moveRight = true;
				
				curFilteTextView.setVisibility(View.GONE);
				mChannelInfoView.hide();
				mPromptInfoView.hide();
				
				curProgressTextView.setText(""+ 0 + "/" +mChannelManager.getChannelList().size());
		*/
		channList = mChannelManager.getChannelList();
		//	actionButton.setText(R.string.dtv_scan_start);
		//	progressBar.setProgress(0);
	}

	public void update(int index) {
		Log.i(TAG, "FilterChannels update-->index is  " + index);
		if (-1 == index) {
			endSmartSkip();
		} else {
			DtvProgram tem = mChannelManager.getProgramByServiceIndex(index);
			if (null != tem) {
				mChannelManager.setCurProgram(tem);
				curFilteNum++;
				int size = channList.size();
				if (size < curFilteNum) {
					curFilteNum = size;
				}
				//dtv_scan_source
				String strScansource = (myContext.getResources().getString(R.string.dtv_scan_source) + " " + DtvSourceManager.getInstance().getCurSourceName());
				//strTitle
				String strTitle = myContext.getResources().getString(R.string.dtv_smartskip_programing);
				String strSubTitle = DtvSourceManager.getInstance().getCurSourceName();
				//strFilterChname
				strFilterChname = myContext.getResources().getString(R.string.dtv_filter_info_curfilter) + mChannelManager.getCurProgram().getProgramName();
				strFilterStep = myContext.getResources().getString(R.string.dtv_filter_info_curprogress) + curFilteNum + "/" + size;
				try {
					if (size > 0) {
						intProgress = (curFilteNum * 100 / size);
					} else {
						intProgress = 0;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.i(TAG, "----------------当前过滤的节目为：" + curFilteNum + "-------------------总的过滤进度为：" + intProgress);
				MainMenuRootData.RefreshFilterMenu(strTitle, strSubTitle, strFilterChname, strFilterStep, intProgress);

				/*if (MainMenuRootData.getGuideScan()) {
					MenuSearchGuide.RefreshGuideScanParameter(strFrequency,strScansource, strSymbolrate, strModulmode,strResultDTV, strResultBroadcast, strFilterChname,strFilterStep, intProgress, true);
				} else {
					MainMenuRootData.RefreshScanMenu(strTitle, strSubTitle,strFilterChname, strFilterStep, intProgress);
				}*/
			} else {
				Log.d(TAG, "FilterChannels update-->err program not found by service index " + index);
			}
		}
	}

	public void startSmartSkip() {
		preProgram = mChannelManager.getCurProgram();
		Log.i(TAG, "preProgram=" + preProgram);
		curFilteNum = 0;
		isFiltering = true;
		progressBar.setProgress(0);
		actionButton.setText(R.string.dtv_scan_stop);
		curFilteTextView.setVisibility(View.GONE);
		mHandler.removeCallbacks(mRunShow);

		preFilteNum = 0;
		Log.i(TAG, "channList.size()=" + channList.size());
		for (DtvProgram program : channList) {
			if (program.isSkip()) {
				preFilteNum++;
			}
		}

		DtvRoot.setStreamMute(AudioManager.STREAM_TV, true, false);

		dtvinterface.startSmartSkip();
		Log.i(TAG, "FilterChannels startSmartSkip--> ");

		mHandler.postDelayed(moveRun, 1000);
		isMoving = true;
	}

	public void cancelSmartSkip() {
		Log.i(TAG, "FilterChannels cancelSmartSkip--> ");
		curFilteNum = 0;
		isFiltering = false;
		actionButton.setText(R.string.dtv_scan_start);
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		//action to cancel

		dtvinterface.cancellSmartSkip();
		//interface to c level
		//
		//updateCurChannelList
		mChannelManager.updateCurChannelList();

		channList = mChannelManager.getChannelList();
		for (DtvProgram program : channList) {
			if (program.isSkip()) {
				curFilteNum++;
				Log.i(TAG, "program: " + program.mProgramName + " num:" + program.mProgramNum + " serviceIndex: " + program.mServiceIndex + " skipped");
			}
		}

		curFilteTextView.setVisibility(View.VISIBLE);
		curFilteTextView.setText(getContext().getString(R.string.dtv_filter_info_curfilter) + (curFilteNum - preFilteNum));
		progressBar.setProgress(100);

		DtvRoot.setStreamMute(AudioManager.STREAM_TV, false, false);

		if (null != channList && channList.size() > 0) {

			for (int i = 0; i < channList.size(); i++) {

				if (null != preProgram && preProgram.mServiceIndex == channList.get(i).mServiceIndex) {
					preProgram = channList.get(i);
				}

				if (!channList.get(i).isSkip()) {
					mChannelManager.channelForceChangeByProgramServiceIndex(channList.get(i).getProgramServiceIndex(), false);
					mChannelInfoView.show();
					mPromptInfoView.show();

					mMenuManager.init(MenuManager.listState.channel_List);

					Log.d(TAG, "FilterChannels cancelSmartSkip--> and change channel to " + mMenuManager.getCurList().get(0).getProgramName() + " is skip " + mMenuManager.getCurList().get(0).isSkip());
					break;
				}
			}

			if (null != preProgram && preProgram.isSkip()) {
				mChannelManager.setPreChannel(mChannelManager.getCurProgram());
			} else {
				mChannelManager.setPreChannel(preProgram);
			}

		}
		if (isMoving) {
			mHandler.removeCallbacks(moveRun);
			xoff = xWidth / 2;
			moveRight = true;
			isMoving = false;
			setPositon(xoff, yoff);
		}

		try {
			Runtime.getRuntime().gc();

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**节目过滤结束	YangLiu	2015-2-26*/
	}

	public void endSmartSkip() {
		Log.i(TAG, "FilterChannels endSmartSkip--> ");

		curFilteNum = 0;
		isFiltering = false;
		actionButton.setText(R.string.dtv_scan_start);
		mChannelManager.updateCurChannelList();
		channList = mChannelManager.getChannelList();
		for (DtvProgram program : channList) {
			if (program.isSkip()) {
				curFilteNum++;
				Log.i(TAG, "program: " + program.mProgramName + " num:" + program.mProgramNum + " serviceIndex: " + program.mServiceIndex + " skipped");
			}
		}

		curFilteTextView.setVisibility(View.VISIBLE);
		curFilteTextView.setText(getContext().getString(R.string.dtv_filter_info_curfilter) + (curFilteNum - preFilteNum));
		progressBar.setProgress(100);

		DtvRoot.setStreamMute(AudioManager.STREAM_TV, false, false);

		if (null != channList && channList.size() > 0) {

			for (int i = 0; i < channList.size(); i++) {

				if (null != preProgram && preProgram.mServiceIndex == channList.get(i).mServiceIndex) {
					preProgram = channList.get(i);
				}

				if (!channList.get(i).isSkip()) {
					mChannelManager.channelForceChangeByProgramServiceIndex(channList.get(i).getProgramServiceIndex(), false);
					mChannelInfoView.show();
					mPromptInfoView.show();

					//
					mMenuManager.init(MenuManager.listState.channel_List);

					Log.d(TAG, "FilterChannels endSmartSkip--> and change channel to " + mMenuManager.getCurList().get(0).getProgramName() + " is skip " + mMenuManager.getCurList().get(0).isSkip());
					break;
				}
			}

			if (null != preProgram && preProgram.isSkip()) {
				mChannelManager.setPreChannel(mChannelManager.getCurProgram());
			} else {
				mChannelManager.setPreChannel(preProgram);
			}
		}

		/*
		if(isMoving){
			mHandler.removeCallbacks(moveRun);
			xoff = xWidth / 2;
			moveRight = true;
			isMoving= false;
			setPositon(xoff, yoff);
		}
		
		try{
			Runtime.getRuntime().gc();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		*/

		dismiss();
		//		MainMenuRootData.HideScanMenuDetail();//add by cuixy 5.23
		MainMenuRootData.HideFilterMenu(); //2015-4-17
	}

	public void setCurFilteNum(int curFilteNum) {
		this.curFilteNum = curFilteNum;
	}

	public int getCurFilteNum() {
		return curFilteNum;
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		Log.i(TAG, "FilterChannels dismiss--> ");
		if (isFiltering) {
			cancelSmartSkip();
		}
		mHandler.removeCallbacks(moveRun);
		mHandler.removeCallbacks(mRunShow);
		mChannelInfoView.hide();
		mPromptInfoView.hide();
		super.dismiss();
		DtvDialogManager.RemoveDialog(this);

		//		MainMenuRootData.HideScanMenuDetail();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onKeyDown>>keyCode:" + keyCode + ", event:" + event);

		switch (keyCode) {

		case KeyEvent.KEYCODE_SOURCE:
			//			case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case KeyEvent.KEYCODE_CHANGHONGIR_VOICE:
		case 4135:
			setShowTv(true);
			if (isFiltering) {
				cancelSmartSkip();
			}
			dismiss();
			break;

		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:

			setShowTv(false);
			if (isFiltering) {
				cancelSmartSkip();
			}
			dismiss();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setShowTv(boolean isShowTv) {
		this.isShowTv = isShowTv;
	}

	public boolean isShowTv() {
		return isShowTv;
	}

	public void setPositon(int xoff, int yoff) {
		layout.setX(xoff);

	}

	public boolean isFilter() {
		return isFiltering;
	}

	public void stopFilterInOtherThread() {
		Log.i(TAG, "stop filter in thread:" + Thread.currentThread());
		if (null != mHandler) {
			this.setShowTv(true);
			mHandler.sendEmptyMessage(FILTER_STOP);
		}
	}
}