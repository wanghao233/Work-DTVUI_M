package com.changhong.tvos.dtv.service;

import android.content.Context;
import android.util.Log;

import com.changhong.tvos.common.IResourceManager;
import com.changhong.tvos.common.IResourceManager.IResourceListener;
import com.changhong.tvos.common.ISourceManager;
import com.changhong.tvos.common.ITVPlayer;
import com.changhong.tvos.common.MiscManager;
import com.changhong.tvos.common.PictureManager;
import com.changhong.tvos.common.SoundManager;
import com.changhong.tvos.common.SystemManager;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.model.EnumResource;

public class TVOSService {
	final static String TAG = "DtvService.TVOSService";
	public static final int RSS_DTV = 0;
	public static final int RSS_ATV = 1;
	public static final int RSS_MMP = 2;
	public static final int RSS_NET = 3;

	public SystemManager tvos_SystemManager;
	public SoundManager tvos_SoundManager;
	public ISourceManager tvos_ISourceManager;
	public MiscManager tvos_MiscManager;
	public ITVPlayer tvos_ITVPlayer;

//////////////////////////////////////////////////////////
//	public HotelManager      tvos_HotelManager;
	//   public boolean getHotelMode = false;
	//	TVManager.

	private TVManager tvos_TVManager;
	private IResourceListener tvosListener = null;
	private IResourceManager tvos_ResourceManager = null;
	private PictureManager tvos_PictureManager;
	private static TVOSService instance = null;

	protected TVOSService(Context context) {
		Log.i(TAG, "tvos_TVManager start\n");
		tvos_TVManager = TVManager.getInstance(context);
		Log.i(TAG, "tvos_TVManager end" + tvos_TVManager.toString());

		try {
			Log.i(TAG, "tvos_ResourceManager start\n");
			tvos_ResourceManager = tvos_TVManager.getResourceManager();
			tvos_SystemManager = tvos_TVManager.getSystemManager();
			tvos_SoundManager = tvos_TVManager.getSoundManager();
			tvos_ISourceManager = tvos_TVManager.getSourceManager();
			tvos_PictureManager = tvos_TVManager.getPictureManager();
			tvos_MiscManager = tvos_TVManager.getMiscManager();
			tvos_ITVPlayer = tvos_TVManager.getTVPlayer();

			Log.i(TAG, "tvos_MiscManager end" + tvos_MiscManager.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

/*
Log.i(TAG, "tvos_HotelManager start\n");
		tvos_HotelManager =	HotelManager.getInstance(context);
		Log.i(TAG, "tvos_HotelManager end"+tvos_HotelManager.toString());
	  
		try{
			Log.i(TAG, "tvos_ResourceManager start\n");	
			tvos_ResourceManager = tvos_TVManager.getResourceManager();
			tvos_SystemManager   = tvos_TVManager.getSystemManager();
	 		tvos_SoundManager    = tvos_TVManager.getSoundManager();
	 		tvos_ISourceManager  = tvos_TVManager.getSourceManager();
	 		tvos_PictureManager  = tvos_TVManager.getPictureManager();
	 		tvos_MiscManager     = tvos_TVManager.getMiscManager();	
	 		tvos_ITVPlayer		 = tvos_TVManager.getTVPlayer();
	 		
			Log.i(TAG, "tvos_MiscManager end"+tvos_MiscManager.toString());
		}
		catch(Exception e){
			 e.printStackTrace();
		}
*/

	}

	public static synchronized TVOSService getinstance(Context context) {
		if (instance == null)
			instance = new TVOSService(context);
		return instance;
	}

	public void test_ResourceCallback() {
		if (tvosListener != null) {
			tvosListener.onResourceLose();
		}
	}

	public boolean requestResource(int eResType) {
		//   return true;
		boolean result = true;
		Log.i(TAG, "requestResource start");
		result = tvos_ResourceManager.requestResource(EnumResource.CH_RESOURCE_DTV, "resource type" + eResType);
		Log.i(TAG, "requestResource end =" + result);
		return result;
	}

	public boolean releaseResource() {
		//   return true;
		boolean result = true;
		Log.i(TAG, "releaseResource start");
		result = tvos_ResourceManager.releaseResource();
		Log.i(TAG, "releaseResource end =" + result);
		return result;
	}

	public void registerResourceListener(IResourceListener rssListener) {
		Log.i(TAG, "registerResourceListener start");

		tvosListener = rssListener;
		tvos_ResourceManager.registerResourceListener(rssListener);

		Log.i(TAG, "registerResourceListener end");
	}

	public void unRegisterResourceListener(IResourceListener rssListener) {
		tvosListener = null;
		tvos_ResourceManager.unRegisterResourceListener(rssListener);
	}

}
