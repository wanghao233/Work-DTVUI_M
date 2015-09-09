package com.changhong.pushoutView;

import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;

/*
 * for the first button the local hot programe
 */
public class BusinessLocalHot extends MagicBaseButton {

	private static final String TAG = "DTVpushview_BusinessLocalHot";

	public BusinessLocalHot(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getBusinessName() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public void setInfo(BaseProgram baseProgram, List<BaseChannel> baseChannelList) {
		super.setInfo(baseProgram, baseChannelList);
		if (!Showable) {
			Log.d(TAG, "the businessis not allowed show");
			return;
		}
		Log.d(TAG, "set the infomation of business localhot");

		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;

		mHorizontalViewContainer.mPosterView.setBackgroundColor(Color.argb(153, 0, 0, 0));
		/*
		 * get local hot programe
		 */
		try {
			DtvChannelManager mDtvChannelManager = DtvChannelManager.getInstance();
			DtvInterface mDtvInterface = DtvInterface.getInstance();
			int curChannelType = mDtvChannelManager.getCurChannelType();

			//	List<DtvProgram>  mChannelList = mDtvInterface.getDtvChannels(0x10000);
			List<DtvProgram> mChannelList = mDtvInterface.getWatchedChannelList(curChannelType);

			String logoadress = mChannelList.get(0).mDtvLogo;

			logoadress = MagicButtonCommon.DTV_LOCAL_LOGO_PATH + logoadress;
			Log.d(TAG, "the mdtv logo is + " + logoadress);
			mHorizontalViewContainer.mHalfView.setBackground(Drawable.createFromPath(logoadress));
			serviceId = mChannelList.get(0).mServiceIndex;
			Log.d(TAG, "the local hot channel is + " + serviceId);

			String localhotchannel = mChannelList.get(0).mProgramName;
			Log.d(TAG, "the local hot channel name is + " + localhotchannel);
			mHorizontalViewContainer.mTitleView.setText(localhotchannel);
			business_flag = true;
			post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
		} catch (Exception e) {
			// TODO: handle exception
			business_flag = false;
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.d(TAG, getBusinessName() + "	is onclicked");
			changeChannel(serviceId);

			//lyw add 2015年3月13日12:42:16
			DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.LocalHot);
			Log.i("liuyuwang", "dtv本地热门换台  " + DtvChannelManager.getInstance().getViewSource());

			// 数据上报 YangLiu
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
					+ mContext.getResources().getString(R.string.collect2_dtv) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_localchannel_recommend));
		}
		return super.onKeyDown(keyCode, event);
	}
}