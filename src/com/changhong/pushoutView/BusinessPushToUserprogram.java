package com.changhong.pushoutView;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class BusinessPushToUserprogram extends MagicBaseButton {

	private static final String TAG = "DTVpushview_BusinessPushToUserprogram";

	public BusinessPushToUserprogram(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setInfo(BaseProgram baseProgram, List<BaseChannel> baseChannelList) {
		super.setInfo(baseProgram, baseChannelList);
		if (!Showable) {
			Log.d(TAG, "the businessis not allowed show");
			return;
		}
		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;
		getPushtoUserDate(baseProgram.getWikiInfo());
	}

	@Override
	public String getBusinessName() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.d(TAG, getBusinessName() + "is onclicked");
			changeChannel(serviceId);

			//lyw add 2015年3月13日12:42:16
			DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.PushToUser);
			Log.i("liuyuwang", "dtv预约节目换台  " + DtvChannelManager.getInstance().getViewSource());

			// 数据上报 YangLiu
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
					+ mContext.getResources().getString(R.string.collect2_dtv) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_automatic_recommend));
		}
		return super.onKeyDown(keyCode, event);
	}

	private Void getPushtoUserDate(String wikidata) {
		JSONObject object = null;
		try {
			object = new JSONObject(wikidata);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		String title = object.optString("wiki_title");
		String chanel_code = object.optString("channel_code");
		String wiki_cover = object.optString("wiki_cover");

		mHorizontalViewContainer.mTitleView.setText(title);
		postimgeurl = wiki_cover;
		setpostBgBychild();
		serviceId = getBaseChannelsByHChannelname(chanel_code);
		if (serviceId != -1) {
			business_flag = true;
		}
		Log.d(TAG, "push to user chanel_code is " + chanel_code);
		Log.d(TAG, "push to user title is " + title);
		Log.d(TAG, "push to user wiki_cover is " + wiki_cover);
		Log.d(TAG, "push to user serviceIdx is " + serviceId);

		return null;
	}
}