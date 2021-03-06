package com.changhong.tvos.dtv.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.changhong.tvos.dtv.service.DTVService;
import com.changhong.tvos.dtv.service.IChannelManager;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BaseChannelUtil {
	private static final String TAG = "BaseChannelUtil";
	
	public static String AliasName_CreatedDone_BROADCAST = "com.changhong.tvos.dtv.BaseChannelUtil.CreatAliasNameListDone";
	public IDTVService mIDtvServer = null;
	public IChannelManager mIChannelManagerServer = null;
	private static BaseChannelUtil instance;
	private Context mContext;
	private boolean isFirstRistered = false;
	private ConnectivityManager cManager;
	private NetworkInfo nInfo;
	
	
	private BaseChannelUtil(Context context) {
		this.mContext = context;
	}

	public synchronized static BaseChannelUtil getInstance(Context context) {
		if (instance == null) {
			instance = new BaseChannelUtil(context);
		}
		return instance;
	}
	
	
	/**
	 * 创建欢网预约推荐数据库
	 */
	private void creatTimerHuanDB(){
		if (DTVService.V_5508Q2) {
			Log.i(TAG, "start post BaseProgram");
			if (BaseProgramUtil.getInstance(mContext).networkTest(mContext)) {// 开机启动时，没网则不会创建欢网预约库
				Log.i(TAG, "[mNetworkReceiver]" + mNetworkReceiver);
				BaseProgramUtil.getInstance(mContext).refreshChannelData(mNetworkReceiver);
				Log.i(TAG, "post BaseProgram done");
			}
		}
	}
	
	/**
	 * 生成别名库
	 */
	private void refreshBaseChannelDB() {
		Log.i(TAG, "开始更新别名库");
		List<DTVChannelDetailInfo> dtvChannelList = getDTVChannelList(mContext);
		if (dtvChannelList != null && dtvChannelList.size() > 0) {
			for (DTVChannelDetailInfo dtvChannelDetailInfo : dtvChannelList) {
				Log.i(TAG, "dtvChannelList.channelName=" + dtvChannelDetailInfo.mstrServiceName);
			}
		}
		
		dtvChannelList = checkChanneldb(dtvChannelList);
		if (dtvChannelList != null && dtvChannelList.size() > 0) {
			int num = dtvChannelList.size();
			String[] names = new String[num];
			for (int i = 0; i < num; i++) {
				names[i] = dtvChannelList.get(i).mstrServiceName;
				Log.i(TAG, "[name]" + names[i]);
			}

			if (!BaseProgramUtil.getInstance(mContext).networkTest(mContext)) {// 没网，取本地文件
				getLocalBaseChannel(dtvChannelList);

				Log.d(TAG, "no net，registerNetworkReceiver");
				if (!isFirstRistered) {
					registerNetworkReceiver();
					isFirstRistered = true;
				}
			} else {// 有网，判断是否取成功
				Log.d(TAG, "we has net");
				while (true) {
					if (getChannelCodeData(names, dtvChannelList) != null) {// 取成功，退出
						/*
						 * Log.d(TAG, "有网，成功取得数据，取消网络广播");
						 * unregisteNetworkReceiver(mNetworkReceiver);
						 */
						break;
					} else {// 取失败，5m后继续尝试
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (getChannelCodeData(names, dtvChannelList) != null) {// 第二次取成功，退出
							/*
							 * Log.d(TAG, "有网，第二次成功取得数据，取消网络广播");
							 * unregisteNetworkReceiver
							 * (mNetworkReceiver);
							 */
							break;
						} else {// 第二次取失败，取本地
							getLocalBaseChannel(dtvChannelList);
							Log.d(TAG, "has net，but no data，registerNetworkReceiver");
							if (!isFirstRistered) {
								registerNetworkReceiver();
								isFirstRistered = true;
							}
							break;
						}
					}
				}

				/**
				 * 生成欢网预约DB
				 */
				creatTimerHuanDB();
			}

			/**
			 * created aliasName done,send broadcast
			 */
			Intent mIntent = new Intent(BaseChannelUtil.AliasName_CreatedDone_BROADCAST);
			mContext.sendBroadcast(mIntent);
			Log.i(TAG, "send aliasName has created Broadcast");
		}
	}
	
	/**
	 * refreshChannelData创建别名库
	 */
	public void refreshChannelData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (0 == CheckServiceBinder()) {
						Log.i(TAG, "CheckServiceBinder is Ok");
						refreshBaseChannelDB();
						Log.i(TAG, "Creat channelAliasName done");
						break;
					} else {
						Log.i(TAG, "CheckServiceBinder is not Ok,delay 3m and keep on check");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							Log.i(TAG, "InterruptedException = " + e.getMessage().toString());
						}
					}
				}
			}
		}).start();
	}

	
	
	/**
	 * CheckServiceBinder
	 * @return
	 */
	private int CheckServiceBinder() {
		Log.i(TAG, "[checkServiceBinder]");
		int iret = -1;
		if (mIDtvServer == null) {
			IBinder binder;
			binder = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
			Log.i(TAG, "[binder]" + binder);
			if (binder != null) {
				mIDtvServer = IDTVService.Stub.asInterface(binder);
				Log.i(TAG, "[mIDtvServer]" + mIDtvServer);
				if (mIDtvServer == null) {
					Log.i(TAG, "[mIDtvServer is null]");
					return iret;
				}
			} else {
				Log.i(TAG, "[service binder failed!]");
				return iret;
			}
		}
		if (mIChannelManagerServer == null) {
			try {
				IBinder binder = mIDtvServer.CreateChannelManager(null);
				Log.i(TAG, "[binder2]" + binder);
				if (binder != null) {
					mIChannelManagerServer = IChannelManager.Stub.asInterface(binder);
					Log.i(TAG, "[mIChannelManagerServer]" + mIChannelManagerServer);
					if (mIChannelManagerServer == null) {
						Log.i(TAG, "[mIChannelManagerServer is null]");
						return iret;
					}
					mIChannelManagerServer.setChannelSource(DTVConstant.ConstDemodType.DVB_C, 0);
				}
				return iret;
			} catch (RemoteException exception) {
				Log.i(TAG, "CreateChannelManager binder failed!");
			}
		}
		iret = 0;
		Log.i(TAG, "[iret]" + iret);
		return iret;
	}
	
	/**
	 * checkChanneldb
	 * @param dtvChannelList
	 * @return
	 */
	private List<DTVChannelDetailInfo> checkChanneldb(List<DTVChannelDetailInfo> dtvChannelList) {
		ArrayList<BaseChannel> dbChannels = BaseChannelDBUtil.getInstance(mContext).getAllData();
		if (dbChannels != null && dbChannels.size() > 0) {
			for (BaseChannel dbchannel : dbChannels) {
				if (dtvChannelList != null && dtvChannelList.size() > 0) {
					boolean flag = false;
					for (int i = 0; i < dtvChannelList.size(); i++) {
						if (dbchannel.getName().equals(dtvChannelList.get(i).mstrServiceName)) {
							Log.i(TAG, "channeldb" + dbchannel.getName());
							if (dbchannel.getIndex() != dtvChannelList.get(i).miChannelIndex) {
								dbchannel.setIndex(dtvChannelList.get(i).miChannelIndex);

								BaseChannelDBUtil.getInstance(mContext).updateIndex(dbchannel);
							}
							dtvChannelList.remove(i);
							flag = true;
							break;
						}
					}
					
					if (!flag) {
						Log.i(TAG, "channeldb" + dbchannel.getName());
						BaseChannelDBUtil.getInstance(mContext).deleteByNames(dbchannel.getName());
					}
				} else {
					Log.i(TAG, "channeldtv kong");// 保存channeDB

					// 2015-3-12 没有本地节目则删除别名库 2015-4-13 查询时别名库表为空
					BaseChannelDBUtil.getInstance(mContext).deleteByNames(dbchannel.getName());
					// BaseChannelDBUtil.getInstance(mContext).upGradeDB();

					Log.i(TAG, "delAllSchedulePrograms");
					// 2015-3-12 没有本地节目则删除Timer数据库
					if (BaseProgramManager.getInstance(mContext).getSchedulePrograms() != null && BaseProgramManager.getInstance(mContext).getSchedulePrograms().size() > 0) {
						BaseProgramManager.getInstance(mContext).delAllSchedulePrograms();
						Log.i(TAG, "hasn't local channel but timer.db is not null, so delete timer");
					} else {
						Log.i(TAG, "hasn't local channel and timer.db is null too");
					}
				}
			}
		} else {
			Log.i(TAG, "channeldb kong");
		}
		return dtvChannelList;
	}

	/**
	 * deleteDBChannelList	删除别名库
	 * @return
	 */
	public boolean deleteDBChannelList() {
		ArrayList<BaseChannel> dbChannels = BaseChannelDBUtil.getInstance(mContext).getAllData();
		if (dbChannels != null && dbChannels.size() > 0) {
			for (BaseChannel dbchannel : dbChannels) {
				BaseChannelDBUtil.getInstance(mContext).deleteByNames(dbchannel.getName());
			}
		}
		
		if (dbChannels == null || (dbChannels != null && dbChannels.size() == 0)) {
			Log.i("YangLiu", "delete db channelList done");
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * getLocalBaseChannel
	 * @param dtvChannelList
	 */
	private void getLocalBaseChannel(List<DTVChannelDetailInfo> dtvChannelList) {
		String in = getFromAssets(mContext, "ChannelLibrary.txt");
		if (in != null && !in.equals("")) {
			ArrayList<BaseChannel> basenames = resolveData(mContext, in);
			ArrayList<BaseChannel> namelibrary = getNameList(basenames);
			Log.i(TAG, "size2 " + namelibrary.size() + "");
			compareName(dtvChannelList, namelibrary, mContext);
		}
	}
	
	public List<DTVChannelDetailInfo> getDTVChannelList(Context context) {
		List<DTVChannelDetailInfo> dtvChannelList = getChannelList();
		return dtvChannelList;
	}

	private List<DTVChannelDetailInfo> getChannelList() {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "CheckServiceBinder service binder failed");
			return null;
		}
		try {
			DTVChannelDetailInfo[] tmpList = mIChannelManagerServer.getChanneDetailInfoListByTpye(DTVConstant.ConstServiceType.SERVICE_TYPE_ALL);
			if ((tmpList == null) || (tmpList.length <= 0)) {
				Log.e(TAG, "[getChanneDetailInfoListByTpye NULL]");
				return null;
			}
			List<DTVChannelDetailInfo> channelList = new ArrayList<DTVChannelDetailInfo>();
			for (int i = 0; i < tmpList.length; i++) {
				channelList.add(tmpList[i]);
			}
			return channelList;
		} catch (RemoteException exception) {
			Log.e(TAG, "[getChannelList exception]");
		}
		return null;
	}


	
	/**
	 * @param basenames
	 * @return
	 */
	private String creatPostParam(String[] basenames) {
		String postParame = null;
		try {
			JSONObject object = new JSONObject();
			object.put("action", "GetChannelsByNames");
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
			JSONArray names = new JSONArray();
			for (String basename : basenames) {
				names.put(basename);
				Log.i("basename", basename);
			}
			param.put("names", names);
			object.put("param", param);
			postParame = object.toString();
			Log.i("String", object.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return postParame;
	}

	/**
	 * @param names
	 * @param dtvChannelList
	 * @return
	 */
	private String getChannelCodeData(String[] names, List<DTVChannelDetailInfo> dtvChannelList) {
		String url = "http://www.epg.huan.tv/json2";
		String errorCode = null;
		String name = creatPostParam(names); // ServiceNames
		if (name == null) {
			return null;
		}
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		// 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", name));
		HttpResponse response;
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(request);
			Log.i(TAG, "response code=" + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.i(TAG, "start post data from huan wang");
				HttpEntity entity = response.getEntity();
				Log.i(TAG, "post data done");
				String in = EntityUtils.toString(entity, "UTF-8");
				Log.i(TAG, "in1=" + in);
				JSONObject object = new JSONObject(in);
				errorCode = getResultData(object, dtvChannelList);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "YangLiu errorCode=" + errorCode);
		return errorCode;
	}

	private String getResultData(JSONObject object, List<DTVChannelDetailInfo> dtvChannelList) {
		JSONObject error = object.optJSONObject("error");
		String errorCode = error.optString("code");
		Log.i("errorCode", errorCode);
		String errorMsg = error.optString("info");
		Log.i("errorMsg", errorMsg);
		if ("0".equals(errorCode)) {
			JSONArray channels = object.optJSONArray("channels");
			BaseChannel basechannel = null;
			for (int i = 0; i < channels.length(); i++) {
				basechannel = new BaseChannel();
				JSONObject channel = (JSONObject) channels.opt(i);
				String name = channel.optString("name");
				String code = channel.optString("code");
				String type = channel.optString("type");
				basechannel.setName(name);
				basechannel.setType(type);
				basechannel.setCode(code);
				Log.i("name111", name);
				Log.i("code111", code);
				Log.i("type111", type);
				if (code != null && !code.equals("")) {
					for (DTVChannelDetailInfo dtvchannel : dtvChannelList) {
						if (dtvchannel.mstrServiceName.equals(name)) {
							basechannel.setIndex(dtvchannel.miChannelIndex);
							BaseChannelDBUtil.getInstance(mContext).save(basechannel);
							Log.i("index111", dtvchannel.miChannelIndex + "");
							basechannel = null;
							break;
						}
					}
				}
				basechannel = null;
			}
		}
		return errorCode;
	}

	public String getFromAssets(Context context, String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			int lenght = in.available();
			byte[] buffer = new byte[lenght];
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "GB2312");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private ArrayList<BaseChannel> resolveData(Context context, String in) {
		ArrayList<BaseChannel> channelNames = new ArrayList<BaseChannel>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(in).getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			BaseChannel data = gson.fromJson(el, BaseChannel.class);
			channelNames.add(data);
			// Log.i(TAG, "[name2]"+data.getName());
			// Log.i(TAG, "[code]"+data.getCode());
			if (data.getMemo() != null) {
				// Log.i(TAG, "[momo]"+data.getMemo());
			}
			// Log.i(TAG,"[type]" +data.getType());
		}
		return channelNames;
	}

	private ArrayList<BaseChannel> getNameList(ArrayList<BaseChannel> channelNames) {
		ArrayList<BaseChannel> names = new ArrayList<BaseChannel>();
		String memo = null;
		String[] splits = null;
		BaseChannel name = null;
		for (int i = 0; i < channelNames.size(); i++) {
			BaseChannel baseName = channelNames.get(i);
			String code = baseName.getCode();
			String type = baseName.getType();
			name = new BaseChannel();
			name.setCode(code);
			name.setName(baseName.getName());
			name.setType(type);
			names.add(name);
			name = null;
			memo = baseName.getMemo();
			if (memo != null && memo.length() > 0) {
				splits = memo.split(",");
				if (splits == null || splits.length == 0) {
					continue;
				}
				for (int j = 0; j < splits.length; j++) {
					name = new BaseChannel();
					name.setCode(code);
					name.setType(type);
					name.setName(fullWidthToHalfWidth(splits[j]).toLowerCase());
					names.add(name);
					name = null;
				}
			}
		}
		return names;
	}

	private void compareName(List<DTVChannelDetailInfo> dtvChannels, ArrayList<BaseChannel> names, Context context) {
		ArrayList<BaseChannel> channels = new ArrayList<BaseChannel>();
		ArrayList<BaseChannel> errorChannels = new ArrayList<BaseChannel>();
		BaseChannel mChannel = null;
		String unknownName = null;
		JSONObject object = new JSONObject();
		JSONArray channel = new JSONArray();
		for (int i = 0; i < dtvChannels.size(); i++) {
			String name = fullWidthToHalfWidth(dtvChannels.get(i).mstrServiceName).toLowerCase();
			int index = dtvChannels.get(i).miChannelIndex;
			boolean flag = false;
			String code = null, type = null;
			for (int j = 0; j < names.size(); j++) {
				if (name.equals(names.get(j).getName())) {
					code = names.get(j).getCode();
					type = names.get(j).getType();
					flag = true;
					break;
				}
			}
			if (flag) {
				mChannel = new BaseChannel();
				mChannel.setCode(code);
				mChannel.setName(dtvChannels.get(i).mstrServiceName);
				mChannel.setIndex(index);
				mChannel.setType(type);
				channels.add(mChannel);
				Log.i(TAG, "[namey]" + name);
				if (code != null && !code.equals("")) {
					BaseChannelDBUtil.getInstance(mContext).save(mChannel);
					Log.i(TAG, "[indexy] " + mChannel.getIndex() + "");
				}
				mChannel = null;
			} else {
				mChannel = new BaseChannel();
				mChannel.setName(name);
				mChannel.setIndex(index);
				errorChannels.add(mChannel);
				Log.i(TAG, "[namex]" + name);
				channel.put(name);
				mChannel = null;
			}
		}

		try {
			object.put("channels", channel);
			unknownName = object.toString();
			Log.i(TAG, "[unknownName]" + unknownName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String fullWidthToHalfWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}
		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
			} else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char) (source[i] - 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	private boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	
	
	
	/**
	 * NetworkReceiver： when has net
	 * 
	 * execute baseChannelUtil, create alias channel name;
	 */
	private void registerNetworkReceiver() {
		Log.i(TAG, "[registerNetworkReceiver]" + mNetworkReceiver);
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(mNetworkReceiver, mFilter);
	}

	public synchronized void unregisteNetworkReceiver(BroadcastReceiver mNetworkReceiver) {
		if (isFirstRistered) {
			mContext.unregisterReceiver(mNetworkReceiver);
			isFirstRistered = false;
			Log.d(TAG, "unregisterReceiver succeed");
		}
	}

	private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				nInfo = cManager.getActiveNetworkInfo();
				if (nInfo != null && nInfo.isAvailable()) {
					Log.d(TAG, "netWorkInfo is ok，start creat alais name db");
					refreshChannelData();
				} else {
					Log.d(TAG, "[netWorkInfo is null]");
				}
			}
		}
	};
}