package com.changhong.tvos.dtv.provider;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import com.changhong.tvos.dtv.provider.BaseProgramManager.ConstSourceID;

public class BaseProgramUtil {
	private static final String TAG="BaseProgramUtil";
	private static BaseProgramUtil instance = null;
	private BaseProgramManager mBaseProgramManager = null;
	private  Context mContext;
	private boolean isAddDone = false;
	private List<BaseProgram> mBasePrograms = null;
	
	public BaseProgramUtil(Context context) {
		this.mContext = context;

		mBaseProgramManager = BaseProgramManager.getInstance(context);
	}

	/**
	 * null
	 * mContext = context; 
	 * @param context
	 * @return
	 */
	public synchronized static BaseProgramUtil getInstance(Context context) {
		if (instance!=null) {
			return instance;
		}
		
		instance = new BaseProgramUtil(context);
		return instance;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public boolean networkTest(Context context) {//private  2015-2-3
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();

		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取并添加数据
	 */
	public synchronized void refreshChannelData(final BroadcastReceiver mNetworkReceiver) {//synchronized	
		//final BroadcastReceiver networkReceiver = mNetworkReceiver;
		new Thread(new Runnable() {
			@Override
			public void run() {	
				isAddDone = false;//2015-2-2
				while (true) {					
//					Log.i(TAG, "CHECK NetWork Is Ok="+networkTest(mContext));
					if (networkTest(mContext)) {
						Log.i(TAG, "CHECK IS ADDED DONE isAddDone="+isAddDone);
						if(isAddDone){
							Log.d(TAG, "the baseProgram is add done, unregisteNetworkReceiver");
							BaseChannelUtil.getInstance(mContext).unregisteNetworkReceiver(mNetworkReceiver);
							break;
						}
						
						Log.i(TAG, "NET IS OK,START POST DATA");
						mBasePrograms = getProgramData();							
						if (mBasePrograms!=null && mBasePrograms.size()>0) {
							addTimer();
							isAddDone = true;
						}
						
						Log.i(TAG, "isAddDone="+isAddDone);
						if (!isAddDone) {//有网且没有添加成功，则延时继续添加
							try {
								Log.i(TAG, "START SLEEP");
								Thread.sleep(10000);
								Log.i(TAG, "END SLEEP");
							} catch (Exception e) {
								// TODO: handle exception
								Log.i(TAG, e.getMessage().toString());
							}
						}
					}
				}
			}
		}).start();
	}

	/*public void refreshChannelData2(){//synchronized	
		Log.i(TAG, "refreshChannelData2");
		if (networkTest(mContext)) {
			mBasePrograms = getProgramData();	
			if (mBasePrograms!=null && mBasePrograms.size()>0) {
				addTimer();			
				Log.i(TAG, "sendMessage");
			}
		}
	}*/
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unused")
	@SuppressLint("SimpleDateFormat")
	private List<BaseProgram> getResultData(JSONObject object) {
		List<BaseProgram> basePrograms = new ArrayList<BaseProgram>();

		JSONObject error = object.optJSONObject("error");
		String errorCode = error.optString("code");
		Log.i("errorCode", errorCode);
		String errorMsg = error.optString("info");
		Log.i("errorMsg", errorMsg);

		if ("0".equals(errorCode)) {
			JSONArray programs = object.optJSONArray("programs");
			Log.i(TAG, "----------JSON.length()-----------"+programs.length());
			
			int j = 0;
			
			for (int i = 0; i < programs.length(); i++) {
				BaseProgram baseProgram = new BaseProgram();
				
				JSONObject program = (JSONObject) programs.opt(i);
				String name = program.optString("name");
				String date = program.optString("date");
				String start_time = program.optString("start_time");
				String end_time = program.optString("end_time");
				String channel_name = program.optString("channel_name");
				String wiki_title = program.optString("wiki_title");
				String channel_code = program.optString("channel_code");
				String channel_logo = program.optString("channel_logo");
				String wiki_id = program.optString("wiki_id");
				String wiki_cover = program.optString("wiki_cover");
				String wiki_screenshots = program.optString("wiki_screenshots");
				String tags = program.optString("tags");

				/*Log.i("YangLiu", "\nname="+name+"\ndate="+date+"\nstart_time="+start_time+"\nend_time="+end_time+
						"\nchannel_name="+channel_name+"\nchannel_code="+channel_code+"\nchannel_logo="+channel_logo
						+"\nwiki_id="+wiki_id+"\nwiki_title="+wiki_title+"\nwiki_cover="+wiki_cover
						+"\nwiki_screenshots="+wiki_screenshots+"\ntags="+tags);*/
							
				BaseChannel baseChannel = getBaseChannelByCode(channel_code);
//				Log.i("YangLiu", "baseChannel="+baseChannel);
	
				//2015-3-12
				boolean isHasChannelList = (BaseChannelUtil.getInstance(mContext).getDTVChannelList(mContext)!=null && BaseChannelUtil.getInstance(mContext).getDTVChannelList(mContext).size()>0) ? true : false;
				boolean isHasBaseChannel  = (baseChannel!=null  && baseChannel.getIndex()>=0) ? true : false;
				boolean isNotOutDate = (getDatefromString(start_time).getTime()>=System.currentTimeMillis()) ? true : false;
				
				if (isHasBaseChannel && isHasChannelList && isNotOutDate) {				
//					baseProgram.setStartTime(new Date(System.currentTimeMillis()+60000*(++j)));
					baseProgram.setStartTime(getDatefromString(start_time));
					baseProgram.setEndTime(getDatefromString(end_time));
					baseProgram.setSourceID(ConstSourceID.DVBC);//DTMB2  //DVBC1
					baseProgram.setServiceIndex(baseChannel.getIndex());
					baseProgram.setProgramNum(0);		
					baseProgram.setProgramName(baseChannel.getName());
					baseProgram.setEventName(wiki_title);
					baseProgram.setOriginal(2);
					baseProgram.setWikiInfo(program.toString());
										
					Log.i(TAG, "baseProgram info="
							+"||||||||getStartTime"+baseProgram.getStartTime()
							+"||||||||getEndTime"+baseProgram.getEndTime()
							+"||||||||getSourceID"+baseProgram.getSourceID()
							+"||||||||getServiceIndex"+baseProgram.getServiceIndex()
							+"||||||||getProgramName"+baseProgram.getProgramName()
							+"||||||||getEventName"+baseProgram.getEventName()
							+"||||||||getOriginal"+baseProgram.getOriginal()
							+"||||||||getWikiInfo"+baseProgram.getWikiInfo()+"\n\n\n\n");
					
					basePrograms.add(baseProgram);		
					baseProgram = null;//2015-1-13
				}	
			}
		}
		return basePrograms;
	}
	
	/**
	 * 
	 * @return
	 */
	private String creatPostParam() {
		String postParame = null;
		try {
			JSONObject object = new JSONObject();

			object.put("action", "GetProgramsByChannel0");//GetWikiLiveHot  GetWikisByPackage

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
//			param.put("name", "livehot_top");//
//			param.put("showprogram", "yes");//
			param.put("cover_type", "big");//2015-1-22
			param.put("code", "changhong");
			object.put("param", param);

			postParame = object.toString();
			Log.i(TAG, "[postParame]"+postParame);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postParame;
	}

	/**
	 * POST
	 * @return
	 */
	private List<BaseProgram> getProgramData() {
		Log.i(TAG,"[getProgramData]");
		String url = "http://www.epg.huan.tv/json2";
		List<BaseProgram> basePrograms = null;
		
		String name = creatPostParam();
		
		if (name == null) {
			return null;
		}
		
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", name));
		HttpResponse response;

		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(request);
			Log.i(TAG, "HTTP请求");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.i(TAG, "请求返回码为："+response.getStatusLine().getStatusCode());
				String in = EntityUtils.toString(response.getEntity(), "UTF-8");
				JSONObject object = new JSONObject(in);
				basePrograms = getResultData(object);
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
		return basePrograms;
	}

	/**
	 * 
	 * @param timeString
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private Date getDatefromString(String timeString) {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(); 
		try {
			date = dtFormat.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * @param channel_code
	 * @return
	 */
	private BaseChannel getBaseChannelByCode(String channel_code) {
		ContentResolver contentResolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://com.changhong.tvos.dtv.basechannelprovider/channel");
		Cursor cursor = contentResolver.query(uri, new String[] { "name","[index]", "type", "code" }, null, null, null);

		if (cursor==null || cursor.getCount()==0) {
			isAddDone = true;//2015-2-2 没有别名库就不需要添加数据
			return null;
		}
		
		BaseChannel mBaseChannel = null;	
		while (cursor.moveToNext()) {
			mBaseChannel = new BaseChannel();
			if (channel_code.equals(cursor.getString(3))) {				
				Log.i(TAG, "name  " + cursor.getString(0));
				Log.i(TAG, "index  " + cursor.getInt(1));
				Log.i(TAG, "type  " + cursor.getString(2));
				Log.i(TAG, "code  " + cursor.getString(3));
				Log.i(TAG, "------------");
				mBaseChannel.setName(cursor.getString(0));
				mBaseChannel.setIndex(cursor.getInt(1));
				mBaseChannel.setType(cursor.getString(2));
				mBaseChannel.setCode(cursor.getString(3));
				return mBaseChannel;
			}
		}
		mBaseChannel = null;
		cursor.close();
		return mBaseChannel;
	}

	private void addTimer(){
		for (BaseProgram baseProgram : mBasePrograms) {
			Log.i(TAG, "start addScheduleProgram baseProgram="+baseProgram);
			if (baseProgram != null) {
				mBaseProgramManager.addScheduleProgram(baseProgram);
				Log.i(TAG, "addScheduleProgram done");
			}
		}
	}
}