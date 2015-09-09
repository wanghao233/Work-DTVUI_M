package com.changhong.tvos.dtv.util;

import java.text.SimpleDateFormat;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvCommonManager;
import com.changhong.tvos.dtv.tvap.DtvEpgManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewChannelInfo extends RelativeLayout {
	private static final String TAG = "ViewChannelInfo";
	private Context mContext;
	private LinearLayout mBgLayout;
	private TextView dtvChannelNum, dtvChannelName, dtvCurTime, dtvEncryption, dtvAudioMode, dtvAudioTrack;
	private TextView curProTime, nextProTime, curProName, nextProName;
	//	SimpleDateFormat mFormatDtvCurTime = new SimpleDateFormat ("yyyy.MM.dd HH:mm");
	SimpleDateFormat mFormatDtvCurTime = new SimpleDateFormat("yyyy-MM-dd	HH:mm");
	SimpleDateFormat mFormatEventStartTime = new SimpleDateFormat("HH:mm");
	private DtvChannelManager mChannelManager = DtvChannelManager.getInstance();
	private DtvCommonManager mCommonManager = DtvCommonManager.getInstance();
	private DtvEpgManager mEpgManager = DtvEpgManager.getInstance();
	private DtvSourceManager mSourceManager = DtvSourceManager.getInstance();
	private DtvProgram mCurProgram = null;
	private Handler mhandler = null;
	private Runnable mShowRun = null;
	private int mShowTime = 5000;
	public static boolean isNeedShow = true;

	public ViewChannelInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public ViewChannelInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	private void init() {
		mBgLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_channel_info, null);
		dtvChannelNum = (TextView) mBgLayout.findViewById(R.id.DtvChannelNum);
		dtvChannelName = (TextView) mBgLayout.findViewById(R.id.DtvChannelName);
		dtvCurTime = (TextView) mBgLayout.findViewById(R.id.DtvCurTime);
		dtvEncryption = (TextView) mBgLayout.findViewById(R.id.DtvEncryption);
		dtvAudioMode = (TextView) mBgLayout.findViewById(R.id.DtvAudioTrack);
		dtvAudioTrack = (TextView) mBgLayout.findViewById(R.id.DtvAudioLanguage);
		curProTime = (TextView) mBgLayout.findViewById(R.id.curProTime);
		curProName = (TextView) mBgLayout.findViewById(R.id.curProName);
		nextProTime = (TextView) mBgLayout.findViewById(R.id.nextProTime);
		nextProName = (TextView) mBgLayout.findViewById(R.id.nextProName);
		this.addView(mBgLayout);
		mhandler = new Handler();
		mShowRun = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				hide();
			}
		};
		this.setVisibility(View.GONE);
	}

	public void show() {
		mCurProgram = mChannelManager.getCurProgram();
		//		mEpgManager.getPFInfo(mCurProgram.getProgramServiceIndex());
		if (mCurProgram != null) {
			String track[] = mContext.getResources().getStringArray(R.array.menu_audio_track);
			String[] trackInfo = mChannelManager.getAudioTrack(track);
			String[] modeArray = mContext.getResources().getStringArray(R.array.menu_audio_mode);
			int currentlangIndex = mChannelManager.getAudioTrackSelIndex();
			int currentAudioMode = mChannelManager.getAudioModeSel();
			Log.i(TAG, "LL currentlangIndex = " + currentlangIndex);
			Log.i(TAG, "LL currentAudioMode = " + currentAudioMode);
			if (mCurProgram.isScrambled() == true) {
				dtvEncryption.setText(mContext.getString(R.string.dtv_info_cryption));
			} else {
				dtvEncryption.setText(mContext.getString(R.string.dtv_info_encryption));
			}

			dtvChannelNum.setText("" + mChannelManager.getCurProgramNum());
			dtvChannelName.setText(mCurProgram.getProgramName());
			/////////////////////////////////////////////////////////////////////////////////
			String sourceName = null;
			if (mSourceManager.getCurSourceID() == ConstSourceID.DVBC) {
				sourceName = mContext.getString(R.string.channel_info_cable); //有线数字
			} else {
				sourceName = mContext.getString(R.string.channel_info_wireless); //无线数字
			}

			String TV_RadioName = null;
			if (mChannelManager.getCurChannelType() == ConstServiceType.SERVICE_TYPE_TV) {
				TV_RadioName = mContext.getString(R.string.channel_type_tv);//电视
			} else {
				TV_RadioName = mContext.getString(R.string.channel_type_radio);//广播
			}

			sourceName = sourceName + TV_RadioName + ":    ";
			/////////////////////////////////////////////////////////////////////////////////
			dtvCurTime.setText(sourceName + mFormatDtvCurTime.format(mCommonManager.getCurrentDate()));

			//			dtvCurTime.setText(mFormatDtvCurTime.format(mCommonManager.getCurrentDate()));				

			String preAudio = mContext.getString(R.string.menu_audio);
			dtvAudioMode.setText(preAudio + "：" + modeArray[currentAudioMode] + "\u3000\u3000\u3000" + trackInfo[currentlangIndex]);

			//			dtvAudioMode.setText(mContext.getString(R.string.menu_audio_mode)+":"+modeArray[currentAudioMode]);
			//			dtvAudioTrack.setText(mContext.getString(R.string.menu_audio_track)+":"+trackInfo[currentlangIndex]);

			curProTime.setText("");
			curProName.setText("");
			nextProTime.setText("");
			nextProName.setText("");

		} else {
			dtvChannelNum.setText("0");
			dtvChannelName.setText(mContext.getString(R.string.dtv_info_null));
			dtvCurTime.setText(mFormatDtvCurTime.format(mCommonManager.getCurrentDate()));
			dtvEncryption.setText("");
			dtvAudioMode.setText("");
			dtvAudioTrack.setText("");
			curProTime.setText("");
			curProName.setText("");
			nextProTime.setText("");
			nextProName.setText("");
			Log.e("ViewChannelInfo", "111mCurProgram======NULL :");
		}
		setVisibility(View.VISIBLE);
		mhandler.removeCallbacks(mShowRun);
		mhandler.postDelayed(mShowRun, mShowTime);
	}

	public void hide() {
		mhandler.removeCallbacks(mShowRun);
		setVisibility(View.GONE);
	}

	public void inputChannelNum(String channelNum) {
		dtvChannelNum.setText(channelNum);
		dtvChannelName.setText("");
		dtvCurTime.setText(mFormatDtvCurTime.format(mCommonManager.getCurrentDate()));
		dtvEncryption.setText("");
		dtvAudioMode.setText("");
		dtvAudioTrack.setText("");
		curProTime.setText("");
		curProName.setText("");
		nextProTime.setText("");
		nextProName.setText("");
		setVisibility(View.VISIBLE);
		mhandler.removeCallbacks(mShowRun);
		mhandler.postDelayed(mShowRun, mShowTime);
	}

	public void updateEpgShow() {
		mCurProgram = mChannelManager.getCurProgram();
		if (mCurProgram != null) {
			DtvEvent[] PFinfo = mEpgManager.getPFInfo(mCurProgram.mServiceIndex);
			if (PFinfo[0] != null) {
				curProTime.setText(mFormatEventStartTime.format(PFinfo[0].getStartTime()) + "-" + mFormatEventStartTime.format(PFinfo[0].getEndTime()));
				if (null != PFinfo[0].getTitle()) {
					curProName.setText(PFinfo[0].getTitle().trim());
				}
			} else {
				curProTime.setText("");
				curProName.setText("");
			}
			if (PFinfo[1] != null) {
				nextProTime.setText(mFormatEventStartTime.format(PFinfo[1].getStartTime()) + "-" + mFormatEventStartTime.format(PFinfo[1].getEndTime()));
				if (null != PFinfo[1].getTitle()) {
					nextProName.setText(PFinfo[1].getTitle().trim());
				}
			} else {
				nextProTime.setText("");
				nextProName.setText("");
			}

		} else {
			curProTime.setText("");
			curProName.setText("");
			nextProTime.setText("");
			nextProName.setText("");
			Log.e("ViewChannelInfo", "2222mCurProgram======NULL :");
		}
		setVisibility(View.VISIBLE);
		mhandler.removeCallbacks(mShowRun);
		mhandler.postDelayed(mShowRun, mShowTime);
	}
}