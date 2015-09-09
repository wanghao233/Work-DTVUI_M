package com.changhong.pushoutView;

import java.util.List;
import com.changhong.inface.net.NetworkDCC.NetworkDCCUtils;
import com.changhong.pushoutView.AsyncImageLoader.ImageCallback;
import com.changhong.pushoutView.HorizontalListViewBase.OnExitDone;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.dtv.PVR;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.model.ENUMPlayerScenes;
import com.changhong.tvos.model.EnumInputSource;
import com.changhong.tvos.model.ChOsType.EnumPipInputSource;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BusinessUserAdded extends RelativeLayout {

	private static final String TAG = "DTVpushview_BusinessUserAdded";
	private LinearLayout userAddedLayout;
	private TextView userAddedTextName;
	private TextView userAddedTextInfo;
	private TextView userAddedTextExplain;
	private ImageView userAddedBg;
	private int serviceId;
	@SuppressWarnings("unused")
	private int post_image_flag = 0;
	private String postimgeurl;
	private BaseProgram mBaseProgram;
	private OnExitDone mOnExitDone;

	public BusinessUserAdded(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		addView(LayoutInflater.from(context).inflate(R.layout.push_out_useraddview, null));
		userAddedLayout = (LinearLayout) findViewById(R.id.push_out_user_want_text);
		userAddedLayout.setBackgroundColor(Color.argb(153, 0, 0, 0));
		userAddedTextName = (TextView) findViewById(R.id.push_out_user_want_text_name);
		userAddedTextInfo = (TextView) findViewById(R.id.push_out_user_want_text_info);
		userAddedTextExplain = (TextView) findViewById(R.id.push_out_user_want_text_explain);
		userAddedBg = (ImageView) findViewById(R.id.push_out_user_want_bg);
		this.setFocusable(true);
	}

	public void setInfo(BaseProgram baseProgram, List<BaseChannel> baseChannelList) {

		if (baseProgram == null) {
			Log.d(TAG, "returned by the baseProgram" + baseProgram);
			return;
		}
		mBaseProgram = baseProgram;
		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;
		postimgeurl = baseProgram.getWikiInfo();
		setPostBg(postimgeurl);

		userAddedTextName.setText("《" + baseProgram.getProgramName() + "》");
		userAddedTextName.setGravity(Gravity.CENTER_HORIZONTAL);
		Log.d(TAG, "program start time " + baseProgram.getStartTime());
		Log.d(TAG, "program start time " + baseProgram.getEndTime());

		long time1 = baseProgram.getStartTime().getTime();
		long time2 = baseProgram.getEndTime().getTime();
		long programtime = time2 - time1;

		long day = programtime / (24 * 60 * 60 * 1000);
		long hour = (programtime / (60 * 60 * 1000) - day * 24);
		long min = ((programtime / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (programtime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		Log.d(TAG, "the time of program time is" + programtime);
		String programtimestring = "";
		if (day > 0)
			programtimestring = "" + day + ":";
		if (hour > 0)
			programtimestring = programtimestring + hour + ":";
		if (min > 0)
			programtimestring = programtimestring + min + ":";
		if (s > 0)
			programtimestring = programtimestring + s;

		Log.d(TAG, "the time of programtimestring  is" + programtimestring);
		userAddedTextInfo.setText(mContext.getString(R.string.pushoutview_progrm_time) + programtimestring);
		userAddedTextExplain.setText(mContext.getString(R.string.pushoutview_programehelp));//2015-4-28 YangLiu 录制提示中英混显
		serviceId = baseProgram.getServiceIndex();
		Log.d(TAG, "the service IDX is " + serviceId);
	}

	protected void setPostBg(String postimgeurl) {
		new Thread().start();
		final ImageView imageView = userAddedBg;
		imageView.setTag(postimgeurl);
		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;
		Log.d(TAG, "the url is " + postimgeurl);
		Drawable cachedImage = AsyncImageLoader.getInstance().loadDrawable(postimgeurl, new ImageCallback() {
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {

				if (imageView != null) {
					imageView.setImageDrawable(imageDrawable);
				}
				post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
			}
		});
		if (cachedImage == null) {
			imageView.setImageResource(R.drawable.pushview_default_s);
			Log.d(TAG, "the bg img posted and not ready use loaclpicture");
			post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
		} else {
			imageView.setImageDrawable(cachedImage);
			Log.d(TAG, "the bg img posted ready ");
			post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
		}
	}

	protected void changeChannel(int serviceId) {

		Log.d(TAG, "send boradcast to change program" + serviceId);
		Intent it = new Intent("com.changhong.tvos.Start_DTV");
		serviceId = serviceId | 0x40000000;
		it.putExtra("ChannelNumber", serviceId);
		mContext.sendBroadcast(it);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, this.getClass().getName() + "recived key down");
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.d(TAG, this.getClass().getName() + "is onclicked");
			changeChannel(serviceId);

			//lyw add 2015年3月13日12:42:16
			DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.UserAdd);
			Log.i("liuyuwang", "dtv主场景预约换台  " + DtvChannelManager.getInstance().getViewSource());

			// 数据上报 YangLiu
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
					+ mContext.getResources().getString(R.string.collect2_launcher_v2) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_reservation_localprogram_play));
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

			//Scenes is dtv or atv, can't record
			EnumInputSource mEnumInputSource = null;
			ENUMPlayerScenes mENUMPlayerScenes = null;

			try {
				mEnumInputSource = TVManager.getInstance(mContext).getSourceManager().getCurInputSource(EnumPipInputSource.E_MAIN_INPUT_SOURCE);
				mENUMPlayerScenes = TVManager.getInstance(mContext).getTVPlayer().getCurrentPlayerScenes();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.i(TAG, "The cur Scenes is " + mENUMPlayerScenes + "\nThe cur source is " + mEnumInputSource);
			if ((ENUMPlayerScenes.EN_PLAYER_SCENES_ATV.equals(mENUMPlayerScenes) && EnumInputSource.E_INPUT_SOURCE_ATV.equals(mEnumInputSource))
					|| (ENUMPlayerScenes.EN_PLAYER_SCENES_DTV.equals(mENUMPlayerScenes))) {
				Log.i(TAG, "cur is play DTV or ATV and return");
				String filedNotRightSCENESPvrHit = mContext.getResources().getString(R.string.filedNotRightSCENESPvrHit);
				DtvScheduleManager.showHintToast(mContext, filedNotRightSCENESPvrHit);
				return true;
			}

			//3288 is connenct, statrt record
			if (NetworkDCCUtils.getInstance(mContext).DCCgetSocketConnectStatus(NetworkDCCUtils.HDMI_PORT4)
					&& NetworkDCCUtils.getInstance(mContext).getCECDeviceConnect(NetworkDCCUtils.HDMI_PORT4) == 0xfb) {
				Log.i(TAG, "the 3288 has connected!!");

				try {
					Log.i(TAG, "PVRStatus=" + PVR.getInstace().getPVRStatus());
					if (PVR.getInstace().getPVRStatus() == 0) {
						PVR.getInstace().PVR_REC_START(-1, mBaseProgram.getServiceIndex(), postimgeurl, mBaseProgram.getProgramName(), mBaseProgram.getStartTime().getTime(),
								mBaseProgram.getEndTime().getTime());
					} else if (PVR.getInstace().getPVRStatus() == 1) {
						String filedAlreadyPvrHit = mContext.getResources().getString(R.string.pvr_Filed_AlreadyRecordedPro);
						DtvScheduleManager.showHintToast(mContext, filedAlreadyPvrHit);
					}

					//				PVR.getInstace().PVR_REC_START(-1,mBaseProgram.getServiceIndex(),postimgeurl,mBaseProgram.getProgramName(),mBaseProgram.getStartTime().getTime(),mBaseProgram.getEndTime().getTime());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				//3288 is disconnenct, can't record
			} else {
				Log.i(TAG, "the 3288 has not connected!!");
				String filedDevNotConnectedPvrHit = mContext.getResources().getString(R.string.pvr_Filed_3288DevNotConnectedSuccess);
				DtvScheduleManager.showHintToast(mContext, filedDevNotConnectedPvrHit);
			}/*else if(!(NetworkDCCUtils.getInstance(mContext).getCECDeviceConnect(NetworkDCCUtils.HDMI_PORT4) == 0xfb)){
				Log.i(TAG, "the 3288 has not connected!!");
				String filedDevNotConnectedPvrHit = mContext.getResources().getString(R.string.pvr_Filed_3288DevNotConnected);
				DtvScheduleManager.showPvrToast(mContext, filedDevNotConnectedPvrHit);
				}else if(!NetworkDCCUtils.getInstance(mContext).DCCgetSocketConnectStatus(NetworkDCCUtils.HDMI_PORT4)){
				Log.i(TAG, "net disConnected and the 3288 disConnected!!");
				String filedNetWorkNotConnectedPvrHit = mContext.getResources().getString(R.string.pvr_Filed_3288NetdisConnected);
				DtvScheduleManager.showPvrToast(mContext, filedNetWorkNotConnectedPvrHit);
				}*/

			// 数据上报 YangLiu
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
					+ mContext.getResources().getString(R.string.collect2_launcher_v2) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_reservation_localprogram_record));
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mOnExitDone != null)
				mOnExitDone.onExitDone();
			else
				Log.d(TAG, "key back but the mOnExitDone is null");
			return true;
		}
		return false;
	}

	public void setOnExitDone(OnExitDone onExitDone) {
		// TODO Auto-generated method stub
		mOnExitDone = onExitDone;
	}
}