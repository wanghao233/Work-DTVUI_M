package com.changhong.pushoutView;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.inface.net.NetworkDCC.NetworkDCCUtils;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class BusinessVCR extends MagicBaseButton {

	private static final String TAG = "DTVpushview_BusinessVCR";
	private String ipString = null;

	private int deviceport;
	private String mPackageName;
	private String VideoID;
	private int mReciveport = -1;

	private String ImageUrl;
	private String address = null;

	public BusinessVCR(Context context) {
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
		deviceport = -1;
		mPackageName = null;
		VideoID = null;
		business_flag = false;
		mHorizontalViewContainer.mTitleView.setText(R.string.pushoutview_Pvr);
		address = initDeviceData();
		if (address == null) {
			Log.d(TAG, "the adress is" + address);
			return;
		}
		Log.d(TAG, "the address is" + address);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getData(address);
			}
		}).start();

	}

	private void getData(String url) {
		HttpPost request = new HttpPost(url);
		HttpResponse httpResponse;
		String retSrc = null;
		try {
			httpResponse = new DefaultHttpClient().execute(request);
			retSrc = EntityUtils.toString(httpResponse.getEntity());
			Log.d(TAG, "the retSrc is " + retSrc);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject object = null;
		try {
			object = new JSONObject(retSrc);
			Log.d(TAG, object.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			deviceport = object.getInt("device");
			JSONArray packagesjsonArray = object.optJSONArray("packages");
			mPackageName = packagesjsonArray.getJSONObject(0).optString("packageName");

			JSONObject recommendationsobject = object.optJSONObject("recommendations");
			JSONArray recommendationsjsonArray = recommendationsobject.optJSONArray(mPackageName);
			for (int i = 0; i < recommendationsjsonArray.length(); i++) {
				JSONObject tmpJSONObject = new JSONObject();
				tmpJSONObject = recommendationsjsonArray.getJSONObject(i);
				if (tmpJSONObject.optInt("record_status") == 0) {
					VideoID = tmpJSONObject.optString("intent_param");
					if (VideoID != "") {
						ImageUrl = tmpJSONObject.optString("imageUrl");
						postimgeurl = ImageUrl;
						setpostBgBychild();
						business_flag = true;
						return;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String initDeviceData() {
		Boolean socket_status = NetworkDCCUtils.getInstance(mContext).DCCgetSocketConnectStatus(mReciveport);
		if (socket_status) {
			ipString = NetworkDCCUtils.getInstance(mContext).getCECDCCDeviceIP(mReciveport);
		} else {
			return null;
		}
		return "http://" + ipString + ":8080/dccdevice/push/com.changhong.video.recorder";
	}

	public void setReciveport(int reciveport) {
		mReciveport = reciveport;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.d(TAG, "start VCR");
			Log.d(TAG, "deviceport " + deviceport + mPackageName + VideoID);
			NetworkDCCUtils.getInstance(mContext).DCCStartRecommendation(deviceport, mPackageName, VideoID);
		}
		return super.onKeyDown(keyCode, event);
	}
}