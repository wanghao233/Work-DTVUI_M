package com.changhong.pushoutView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class BusinessHotOnline extends MagicBaseButton {

	private int mProgrameIdx = 0;

	private String TAG = "DTVpushview_BusinessHotOnline";

	public BusinessHotOnline(Context context) {
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
		Log.d(TAG, "set info in " + this.getClass().getName());
		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;
		//reset the programe idx whit the first hot
		getLiveHotTop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.d(TAG, getBusinessName() + "is onclicked");
			changeChannel(serviceId);

			//lyw add 2015年3月13日12:42:16
			DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.HotOnline);
			Log.i("liuyuwang", "dtv热门推荐换台  " + DtvChannelManager.getInstance().getViewSource());

			// 数据上报 YangLiu
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
					+ mContext.getResources().getString(R.string.collect2_dtv) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_livehot_recommend));
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public String getBusinessName() {
		String businessName;
		if (mProgrameIdx == 0)
			businessName = this.getClass().getCanonicalName();
		else
			businessName = this.getClass().getCanonicalName() + "TWO";
		return businessName;
	}

	private synchronized void getLiveHotTop() {
		// if (networkTest(mContext)) {
		Log.d(TAG, "start get hot online of getLiveHotTop()");

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String param = creatHotPostParam();
				String wikidata = getHotWikiData(param);
				JSONObject object = null;
				try {
					String errorCode;
					object = new JSONObject(wikidata);
					errorCode = getResultData(object);
					Log.d(TAG, errorCode);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 创建欢网请求参数GetWikiLiveHot
	 * @return
	 */
	private String creatHotPostParam() {
		String postParame = null;
		try {
			JSONObject object = new JSONObject();

			object.put("action", "GetWikiLiveHot");

			JSONObject developer = new JSONObject();
			developer.put("apikey", "HZP9DZMA");
			developer.put("secretkey", "197d2dc3226786eb2377995f8c58e1df");
			object.put("developer", developer);

			JSONObject device = new JSONObject();
			device.put("dnum", "123456");
			object.put("device", device);

			JSONObject user = new JSONObject();
			user.put("userid", "123456");
			object.put("user", user);

			JSONObject param = new JSONObject();

			param.put("cover_type", "big");
			object.put("param", param);

			postParame = object.toString();
			Log.i("String", object.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postParame;
	}

	/**
	 * 从欢网取得热门节目数据
	 * @param param
	 * @return
	 */
	private String getHotWikiData(String param) {
		Log.d(TAG, "get hot wiki data online");
		HttpPost request = new HttpPost(MagicButtonCommon.HUAN_URL);
		HttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String in = EntityUtils.toString(response.getEntity(), "UTF-8");

				Log.i(TAG, "the hot on line get result is " + in);
				return in;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析获得的热门数据
	 * @param object
	 * @return
	 */
	private String getResultData(JSONObject object) {
		ArrayList<LiveHotWiki> wikiList = new ArrayList<LiveHotWiki>();

		JSONObject error = null;
		String errorCode = null;
		try {
			error = object.optJSONObject("error");
			errorCode = error.optString("code");
			Log.i("errorCode", errorCode);
			String errorMsg = error.optString("info");
			Log.i("errorMsg", errorMsg);

			if ("0".equals(errorCode)) {
				JSONArray wikis = object.optJSONArray("wikis");

				for (int i = 0; i < wikis.length(); i++) {
					LiveHotWiki mWiki = new LiveHotWiki();
					JSONObject wiki = (JSONObject) wikis.opt(i);
					String wiki_cover = wiki.optString("wiki_cover");
					String hot = wiki.optString("hot");

					mWiki.setHot(hot);
					mWiki.setWikiCover(wiki_cover);

					JSONArray programs = (JSONArray) wiki.opt("programs");
					ArrayList<ProgramInfo> mProgramList = new ArrayList<ProgramInfo>();

					for (int j = 0; j < programs.length(); j++) {
						ProgramInfo mProgram = new ProgramInfo();
						JSONObject program = (JSONObject) programs.opt(j);
						String channel_code = program.optString("channel_code");
						mProgram.setChannelCode(channel_code);
						mProgramList.add(mProgram);
					}
					mWiki.setPrograms(mProgramList);
					wikiList.add(mWiki);
				}
			}

			if (wikiList.size() == 0)
				return null;
			if (mProgrameIdx == 0) {
				Log.d(TAG, "get the first hotprogram in hot online list");
				postimgeurl = wikiList.get(0).getWikiCover();
				setpostBgBychild();
				mHorizontalViewContainer.mTitleView.setText(mContext.getString(R.string.pushoutview_hottext) + wikiList.get(0).getHot() + mContext.getString(R.string.pushoutview_percent));
				serviceId = getBaseChannelsByHChannelname(wikiList.get(0).getPrograms());
			} else if (wikiList.size() > 1) {
				Log.d(TAG, "get the second hotprogram in hot online list");
				postimgeurl = wikiList.get(1).getWikiCover();
				setpostBgBychild();
				mHorizontalViewContainer.mTitleView.setText(mContext.getString(R.string.pushoutview_hottext) + wikiList.get(1).getHot() + mContext.getString(R.string.pushoutview_percent));
				serviceId = getBaseChannelsByHChannelname(wikiList.get(1).getPrograms());
			}
			if (serviceId != -1) {
				business_flag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			business_flag = false;
			e.printStackTrace();
		}
		return errorCode;
	}

	public void setProgrameIdx(int programIdx) {
		mProgrameIdx = programIdx;
	}
}