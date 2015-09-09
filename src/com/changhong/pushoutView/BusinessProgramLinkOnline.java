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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

/*
 * for the second button link the program
 */
public class BusinessProgramLinkOnline extends MagicBaseButton {

	private static final String TAG = "DTVpushview_BusinessProgramLinkOnline";
	private String targetAction;
	private String targetApkName;
	private ArrayList<SpecialContentParm> mSpecialContentParmList;
	private ArrayList<SpecialTencentParm> mSpecialTencentParmList;

	public BusinessProgramLinkOnline(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getBusinessName() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public boolean getShowable() {
		return super.getShowable();
	}

	@Override
	public void setInfo(BaseProgram baseProgram, List<BaseChannel> baseChannelList) {
		super.setInfo(baseProgram, baseChannelList);

		if (!Showable) {
			Log.d(TAG, "the businessis not allowed show");
			return;
		}
		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;
		Log.d(TAG, "set the infomation of business BusinessProgramLinkOnline");

		mHorizontalViewContainer.mTitleView.setText(R.string.pushoutview_specialprogram);// 2015-3-17

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(TAG, "start get some data from huan and changhong cloud");

				// step 1 get now program
				Log.d(TAG, "step 1 get now program hid");
				String param = creatSpecialPrograme();// 1.根据当前节目向欢网请求参数

				// step 2 get program wiki form huan
				Log.d(TAG, "step 2 get program wiki form huan");
				if (param == null)
					return;
				String wikidata = getSpecialProgrameWikiData(param);// 2.根据请求参数取得的返回数据

				JSONObject object = null;
				try {
					String errorCode;
					object = new JSONObject(wikidata);

					// step 3 get the program info
					Log.d(TAG, "tep 3 get the program info");
					errorCode = getSpecalResultData(object);// 3.解析返回数据，并刷新海报的显示
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.d(TAG, getBusinessName() + "is onclicked");

			try {
				if (!isTencentAPK()) {
					/******************************TVMall播放接口*******************************/
					Log.d(TAG, "mSpecialContentParmList(0)'s name is " + mSpecialContentParmList.get(0).name);
					Log.d(TAG, "mSpecialContentParmList(0)'s value is " + mSpecialContentParmList.get(0).value);

					Intent intent = new Intent();// 9.从云部取得数据后，点击播放服务器存在的关联节目
					ComponentName componeName = new ComponentName(targetApkName, targetAction);
					intent.putExtra(mSpecialContentParmList.get(0).name, mSpecialContentParmList.get(0).value);
					intent.putExtra(mSpecialContentParmList.get(1).name, mSpecialContentParmList.get(1).value);
					intent.setComponent(componeName);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					mContext.startActivity(intent);

				} else {
					/******************************Tencent播放接口*******************************/
					Log.d(TAG, "mSpecialTencentParmList(0)'s name is " + mSpecialTencentParmList.get(0).name);
					Log.d(TAG, "mSpecialTencentParmList(0)'s value is " + mSpecialTencentParmList.get(0).value);

					Intent intent = new Intent();
					String uri = mSpecialTencentParmList.get(0).value;
					intent.setData(Uri.parse(uri + "&pull_from=100303"));
					intent.setAction("com.tencent.qqlivetv.open");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PackageManager packageManager = mContext.getPackageManager();
					List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
					boolean isIntentSafe = activities.size() > 0;
					Log.d(TAG, "Find if exist tencent apk, the reslut is isIntentSafe=" + isIntentSafe);
					if (isIntentSafe) {
						mContext.startActivity(intent);
						return true;
					} else {
						Log.d(TAG, "There is no tencent apk, please install one");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 数据上报 YangLiu
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
					+ mContext.getResources().getString(R.string.collect2_dtv) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_currentprogarm_connection));
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 判断是否使用腾讯接口
	 * @return
	 */
	private boolean isTencentAPK() {
		return true;
	}

	/**
	 * 根据当前节目创建欢网请求参数
	 * @return
	 */
	private String creatSpecialPrograme() {

		String postParame = null;
		JSONObject object = new JSONObject();

		try {
			object.put("action", "GetProgramByChannels");
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
			// param.put("showprogram", "yes");
			JSONArray channelcodes = new JSONArray();

			DtvChannelManager mDtvChannelManager = DtvChannelManager.getInstance();
			DtvProgram dtvCurProgram = mDtvChannelManager.getCurProgram();
			if (null == dtvCurProgram) {
				return null;
			}
			Log.d(TAG, " the curprograme is " + dtvCurProgram.getProgramServiceIndex());

			String currnchannel = null;
			for (BaseChannel p : mBaseChannelList) {
				if (dtvCurProgram.getProgramServiceIndex() == p.getIndex()) {
					currnchannel = p.getCode();
				}
			}

			if (currnchannel == null)
				return null;

			channelcodes.put(currnchannel);
			Log.d(TAG, "the channel code we are watching is " + channelcodes);

			param.put("channel_codes", channelcodes);
			object.put("param", param);

			postParame = object.toString();
			Log.d(TAG, "the creatSpecialPrograme result is " + object.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return postParame;
	}

	/**
	 * 根据当前节目从欢网取得的与该节目有关的数据
	 * @param param
	 * @return
	 */
	private String getSpecialProgrameWikiData(String param) {
		Log.d(TAG, "getSpecialProgrameWikiData from huan");

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
				Log.i(TAG, "the special program info is  " + in);
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
	 * 解析从欢网根据当前节目取得的数据，数据格式为programs的json字符数组
	 * @param object
	 * @return
	 */
	private String getSpecalResultData(JSONObject object) {

		final LiveHotWiki mWiki = new LiveHotWiki();

		JSONObject error = null;
		String errorCode = null;
		try {
			error = object.optJSONObject("error");
			errorCode = error.optString("code");
			Log.i("errorCode", errorCode);
			String errorMsg = error.optString("info");
			Log.i("errorMsg", errorMsg);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if ("0".equals(errorCode)) {
			JSONArray wikis = object.optJSONArray("programs");

			for (int i = 0; i < wikis.length(); i++) {
				JSONObject wiki = (JSONObject) wikis.opt(i);
				String wiki_id = wiki.optString("wiki_id");
				String model = wiki.optString("model");
				String wiki_title = wiki.optString("wiki_title");
				String wiki_cover = wiki.optString("wiki_cover");
				String wiki_content = wiki.optString("wiki_content");
				String hot = wiki.optString("hot");

				JSONArray wiki_director = wiki.optJSONArray("wiki_director");
				JSONArray wiki_starring = wiki.optJSONArray("wiki_starring");
				JSONArray wiki_host = wiki.optJSONArray("wiki_host");
				JSONArray wiki_guest = wiki.optJSONArray("wiki_guest");
				JSONArray tags = wiki.optJSONArray("tags");

				mWiki.setWikiId(wiki_id);
				mWiki.setModel(model);
				mWiki.setWikiTitle(wiki_title);
				mWiki.setWikiCover(wiki_cover);
				mWiki.setWikiContent(wiki_content);
				mWiki.setHot(hot);
				mWiki.setWikiDirector(wiki_director);
				mWiki.setWikiStarring(wiki_starring);
				mWiki.setWikiHost(wiki_host);
				mWiki.setWikiGuest(wiki_guest);
				mWiki.setWikiTags(tags);

				try {
					Log.i("wiki_id", wiki_id);
					Log.i("model", model);
					Log.i("wiki_title", wiki_title);
					Log.i("wiki_cover", wiki_cover);
					Log.i("wiki_content", wiki_content);
					Log.i("hot", hot);
					if (wiki_director != null)
						Log.i("wiki_director", wiki_director.toString());
					if (wiki_starring != null)
						Log.i("wiki_starring", wiki_starring.toString());
					if (wiki_host != null)
						Log.i("wiki_host", wiki_host.toString());
					if (wiki_guest != null)
						Log.i("wiki_guest", wiki_guest.toString());
					if (tags != null)
						Log.i("tags", tags.toString());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				postimgeurl = mWiki.getWikiCover();
				setpostBgBychild();// 4.根据欢网返回数据刷新海报
			}

			Log.d(TAG, "step four get some date from changhong cloud or tencent");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!isTencentAPK()) {
						/******************************TVMall播放接口*******************************/
						String contentPrama = creatSpecialProgrameforContent(mWiki);// 5.创建向云部TVMall请求的参数
						String contentResult = getContentData(contentPrama);// 6.得到云部TVMall返回的请求结果
						Log.d(TAG, "get content linkOnline program is=" + contentResult);

					} else {
						/******************************Tencent播放接口*******************************/
						String tencentPrama = creatSpecialProgrameforTencent(mWiki);// 5.创建向腾讯Tencent请求的参数
						String tencentResult = getTencentData(tencentPrama);// 6.得到腾讯Tencent返回的请求结果
						Log.d(TAG, "get tencent linkOnline program is=" + tencentResult);
					}
				}
			}).start();
		}
		return errorCode;
	}

	/*************************************TVMall**************************************/
	/**
	 * 根据欢网返回的节目数据 创建向云部TVMall请求参数
	 * @param mlivewiki
	 * @return
	 */
	private String creatSpecialProgrameforContent(LiveHotWiki mlivewiki) {

		String postParame = null;
		try {
			JSONObject object = new JSONObject();
			//client
			JSONObject client = new JSONObject();
			client.put("agent_name", "aabbcc");
			client.put("agent_ver", "1.0.327");
			client.put("device", "TV");
			object.put("client", client);

			//searchWord
			JSONObject dibingList = new JSONObject();
			//searchWord——>dibingList
			JSONArray programName = new JSONArray();
			//searchWord——>dibingList——>name
			JSONObject name = new JSONObject();
			name.put("name", mlivewiki.getWikiTitle());
			if (mlivewiki.getModel().endsWith("film") || mlivewiki.getModel().endsWith("teleplay")) {
				Log.d(TAG, "the type of now program is " + mlivewiki.getModel());
				if (!"".equals(mlivewiki.getModel()))
					name.put("type", mlivewiki.getModel());
				if (!"".equals(mlivewiki.getWikiDirector()))
					name.put("director", mlivewiki.getWikiDirector());
				if (!"".equals(mlivewiki.getWikiStarring())) {
					// put star of the tv to changhong cloud but now it is not added
					name.put("cast", mlivewiki.getWikiStarring());
				}
			} else {
				Log.d(TAG, "the type of now program is " + mlivewiki.getModel() + "send host");
				name.put("type", mlivewiki.getModel());
				if (mlivewiki.getWikiHost() != null) {
					name.put("host", mlivewiki.getWikiHost());
				}
			}
			programName.put(name);
			dibingList.put("dibingList", programName);
			object.put("searchWord", dibingList.toString());

			postParame = object.toString();
			Log.d(TAG, "the content of now program send to changhong cloud is" + postParame);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return postParame;
	}

	/**
	 * 得到云部TVMall返回的结果
	 * @param param
	 * @return
	 */
	private String getContentData(String param) {
		// String url ="http://182.140.244.133:8080/cloud/services?appKey=p8q4tr&method=ch.tvmall.resource.dibbing&v=3&format=json";
		String url = "http://cloud.smart-tv.cn/cloud/services?appKey=p8q4tr&method=ch.tvmall.resource.dibbing&v=3&format=json";
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String errorCode = null;
		try {
			StringEntity entity = new StringEntity(param.toString(), "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			request.setEntity(entity);
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String in = EntityUtils.toString(response.getEntity(), "GB2312");
				// Log.d(TAG, in);

				JSONObject object = new JSONObject(in);
				errorCode = getSpecialContentResultData(object);// 7.解析云部TVMall返回结果信息，得到关联的节目
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	/**
	 * 解析云部TVMall返回结果信息，得到关联的节目
	 * @param object
	 * @return
	 */
	private String getSpecialContentResultData(JSONObject object) {

		String playerResult = "";
		try {
			if ("TVMALL_DIBBING".equals(object.optString("serviceName"))) {
				//content
				JSONArray content = object.optJSONArray("content");
				if (content != null && !content.equals("") && !(content.toString()).equals("[]")) {
					//content——>player
					JSONObject player = content.optJSONObject(0).optJSONObject("player");
					//content——>player——>activity
					targetAction = player.optString("activity");
					//content——>player——>apk
					targetApkName = player.optString("apk");
					//content——>player——>parameters
					mSpecialContentParmList = new ArrayList<SpecialContentParm>();
					JSONArray parameters = player.optJSONArray("parameters");
					for (int i = 0; i < parameters.length(); i++) {
						JSONObject parameter = (JSONObject) parameters.get(i);
						SpecialContentParm mSpecialContentParm = new SpecialContentParm();
						mSpecialContentParm.name = parameter.optString("name");
						mSpecialContentParm.value = parameter.optString("value");
						mSpecialContentParmList.add(mSpecialContentParm);
					}

					business_flag = true;
					playerResult = player.toString();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			business_flag = false;
		}

		/*Log.d(TAG, "the targetAction is " + targetAction);
		Log.d(TAG, "the targetApkName is " + targetApkName);
		Log.d(TAG, "the name is " + mSpecialContentParmList.get(0).name);
		Log.d(TAG, "the value is " + mSpecialContentParmList.get(0).value);
		Log.d(TAG, "the name is " + mSpecialContentParmList.get(1).name);
		Log.d(TAG, "the value is " + mSpecialContentParmList.get(1).value);*/
		return playerResult;
	}

	class SpecialContentParm {
		String name;
		String value;
	}

	/*************************************Tencent**************************************/
	/**
	 * 根据欢网返回的节目数据 创建向腾讯请求参数
	 * @param mlivewiki
	 * @return
	 */
	private String creatSpecialProgrameforTencent(LiveHotWiki mlivewiki) {

		String postParame = "";
		JSONObject object = new JSONObject();

		JSONArray query_listObject = new JSONArray();
		JSONObject queryObject = new JSONObject();
		if (mlivewiki != null) {
			try {
				if (mlivewiki.getModel().endsWith("film")) {
					queryObject.put("category", "1"); //类型1：电影
				} else if (mlivewiki.getModel().endsWith("teleplay")) {
					queryObject.put("category", "2");//类型2：电视剧
				} else if (mlivewiki.getModel().endsWith("television")) {
					JSONArray typeArray = new JSONArray();
					typeArray.put("1");
					typeArray.put("2");
					queryObject.put("xcategory", typeArray);//类型非：其他（非1/2，即非电影/电视剧）
				}
				queryObject.put("name", mlivewiki.getWikiTitle());
				queryObject.put("director", mlivewiki.getWikiDirector());
				queryObject.put("cast", mlivewiki.getWikiStarring());
				queryObject.put("host", mlivewiki.getWikiHost());

				query_listObject.put(queryObject);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			object.put("format", "json");
			object.put("query_list", query_listObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		postParame = object.toString();
		Log.d(TAG, "the content of now program send to tencent is" + postParame);
		return postParame;
	}

	/**
	 * 得到腾讯返回的结果
	 * @param param
	 * @return
	 */
	private String getTencentData(String param) {
		//		String tencentURL = "http://1.tv.t002.ottcn.com/i-tvbin/qtv_video/search/get_search_live_video?Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DCH%26CHID%3D10009%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0&guid=95c60a8d505a0d308b59facbe05d7bfe";//测试
		String tencentURL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/search/get_search_live_video?Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DCH%26CHID%3D10009%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0&guid=95c60a8d505a0d308b59facbe05d7bfe"; //正式
		HttpPost request = new HttpPost(tencentURL);
		HttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String tencentData = null;
		try {
			StringEntity entity = new StringEntity(param.toString(), "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			request.setEntity(entity);
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String in = EntityUtils.toString(response.getEntity(), "GB2312");
				//				Log.d(TAG, in);

				JSONObject object = new JSONObject(in);
				tencentData = getSpecialTencentResultData(object);// 7.解析腾讯返回结果的信息，得到关联的节目
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tencentData;
	}

	/**
	 * 解析腾讯返回结果信息，得到关联的节目
	 * @param object
	 * @return
	 */
	private String getSpecialTencentResultData(JSONObject jsonObject) {
		String queryResult = "";
		try {
			JSONObject dataObject = jsonObject.getJSONObject("data");
			if (dataObject == null) {
				return "";
			}

			mSpecialTencentParmList = new ArrayList<SpecialTencentParm>();
			JSONArray query_listArray = dataObject.getJSONArray("list");
			if (query_listArray != null && query_listArray.length() > 0) {
				for (int i = 0; i < query_listArray.length(); i++) {
					JSONObject queryObject = (JSONObject) query_listArray.getJSONObject(i);
					SpecialTencentParm mSpecialTencentParm = new SpecialTencentParm();
					mSpecialTencentParm.name = queryObject.optString("title");
					mSpecialTencentParm.value = queryObject.optString("target");
					mSpecialTencentParmList.add(mSpecialTencentParm);
				}
			}

			business_flag = true;
			queryResult = query_listArray.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			business_flag = false;
			e.printStackTrace();
		}

		/*Log.d(TAG, "mSpecialTencentParmList(0)'s name is " + mSpecialTencentParmList.get(0).name);
		Log.d(TAG, "mSpecialTencentParmList(0)'s value is " + mSpecialTencentParmList.get(0).value);
		Log.d(TAG, "mSpecialTencentParmList(1)'s name is " + mSpecialTencentParmList.get(1).name);
		Log.d(TAG, "mSpecialTencentParmList(1)'s value is " + mSpecialTencentParmList.get(1).value);*/
		return queryResult;
	}

	public class SpecialTencentParm {
		String name;
		String value;
	}
}