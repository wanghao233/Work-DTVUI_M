package com.changhong.tvos.dtv.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.changhong.tvos.dtv.provider.BaseChannelUtil;
import com.changhong.tvos.dtv.service.jni.ResourceManager;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.InterPanelInfo;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.model.PanelInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DTVService extends Service {

	public static final boolean V_5508Q2 = true;

	private final static String TAG = "DtvService.DTVService";
	private final static String DTVServiceVersion = "2015-09-08";
	private static final String changeDate = "rebuild-2015-05-26";
	private static boolean iSStartActivity = false;
	private static boolean isCallbackLoopReady = false;
	private static long mlThreadid = 0;
	private int m_iChannelIndex = -1;
	private TimerShedule timerShedulobj = null;
	@SuppressWarnings("unused")
	private DTVSettings mDTVSettings = null;
	private static Context context = null;
	private static DTVChannelBaseInfo[] oldChList = null;
	IBinder ib_Service = null;
	PVRRecord pvr;

	static {
		try {
			System.loadLibrary("chdtv");
			//System.loadLibrary("dtv");
			System.loadLibrary("DTVServiceJNI");
			Log.i(TAG, "DTVService --->Load JNI LIB chdtv and DTVServiceJNI");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	void handle_HisPlayMessage() {
		String mstrServiceName = "";
		String mstrLog = "";
		int miChannelnumber = -1;

		m_iChannelIndex = DTVPlayer.currentProgramID;
		if (m_iChannelIndex == -1) {
			Log.e(TAG, "!--->m_iChannelIndex == null");
			return;
		}
		if (DTVPlayer.miTunerStatus == 0) {
			return;
		}

		DTVChannelBaseInfo channelbaseInfo = DTVServiceJNI.get_channelmanager_instance().getChannelBaseInfo(m_iChannelIndex);
		if (channelbaseInfo != null) {
			miChannelnumber = channelbaseInfo.miChannelnumber;
			mstrServiceName = channelbaseInfo.mstrServiceName;
			mstrLog = channelbaseInfo.msLogo;

			Intent myintent = new Intent("com.changhong.tvos.dtv.CHANNEL_CHANGEDEXT");
			Bundle bundle = new Bundle();

			bundle.putInt("channelNume", miChannelnumber);
			bundle.putString("channelName", mstrServiceName);
			bundle.putString("channelLog", mstrLog);

			myintent.putExtras(bundle);
			context.sendBroadcast(myintent);

			//Log.e(TAG, "------>handle_HisPlayMessage--success--" + miChannelnumber);
		} else {
			Log.e(TAG, "!--->handle_HisPlayMessage-----no channel info 1");
		}
	}

	public void setPanelInfo() {
		try {
			InterPanelInfo mInterPanelInfo = new InterPanelInfo();
			PanelInfo mPanelInfo = new PanelInfo();
			if (mInterPanelInfo == null || mPanelInfo == null) {

			}
			if (DTVServiceRm.getinstance() == null) {
				Log.i(TAG, "DTVServiceRm.getinstance() is null");
				return;
			}
			if (DTVServiceRm.getinstance().tvos == null) {
				Log.i(TAG, "DTVServiceRm.getinstance().tvos is null");
				return;
			}
			if (DTVServiceRm.getinstance().tvos.tvos_MiscManager == null) {
				Log.i(TAG, "DTVServiceRm.getinstance().tvos.tvos_MiscManager is null");
				return;
			}
			mPanelInfo = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getPanelInfo();
			if (mPanelInfo == null) {
				Log.e(TAG, "!--->PanelInfo-----null");

				return;
			}

			mInterPanelInfo.miPanelWidth = mPanelInfo.miPanelWidth;
			mInterPanelInfo.miPanelHeight = mPanelInfo.miPanelHeight;

			Log.e(TAG, "!--->setPanelInfo-----miPanelWidth:" + mPanelInfo.miPanelWidth + " miPanelHeight:" + mInterPanelInfo.miPanelHeight);

			DTVServiceJNI.get_system_instance().setPanelInfo(mInterPanelInfo.miPanelWidth, mInterPanelInfo.miPanelHeight);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void createDtvinfoFile() {
		int msg = -1;

		try {
			ApplicationInfo appInfo;
			appInfo = context.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			msg = appInfo.metaData.getInt("region");

			if (msg != 0) {
				//CreateText(Integer.toString(msg));
				File dir = new File("/data/dtv");
				if (!dir.exists()) {
					dir.mkdir();
					try {
						Runtime.getRuntime().exec("chmod 777 " + dir);

						File file = new File("/data/dtv/dtvinfo.txt");
						if (file.exists()) {
							file.delete();
						} else {
							file.createNewFile();
							Runtime.getRuntime().exec("chmod 777 " + dir);

							FileWriter fw = null;
							BufferedWriter bw = null;

							fw = new FileWriter("/data/dtv/dtvinfo.txt", true);
							bw = new BufferedWriter(fw);

							String myreadline = Integer.toString(msg);
							bw.write(myreadline + "/n");
							bw.newLine();
							bw.flush();
							bw.close();
							fw.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createChlistTmpFile() {
		DTVChannelBaseInfo[] chlist = DTVServiceJNI.get_channelmanager_instance().getChannelListByTpye(DTVConstant.ConstServiceType.SERVICE_TYPE_TV);
		/**
		 * 节目列表改变，重新生成别名库	YangLiu	2015-2-26（之前通用下搜台执行3次要0.4s，现在需要0.1s）
		 */
		boolean isSameList = false;
		if (oldChList != null && chlist != null && oldChList.length == chlist.length) {
			for (int i = 0; i < chlist.length; i++) {
				if (chlist[i].miChannelIndex == oldChList[i].miChannelIndex) {
					isSameList = true;
				} else {
					isSameList = false;
					break;
				}
			}
		} else {
			isSameList = false;
		}

		if (!isSameList) {
			Log.i("YangLiu", "节目列表为null，或者节目列表改变，重新生成别名库");
			BaseChannelUtil.getInstance(context).refreshChannelData();
		}
		oldChList = chlist;

		File dtvdir = new File("/tmp/DTV");
		if (!dtvdir.exists()) {
			if (!dtvdir.mkdir()) {
				return;
			}
			try {
				Runtime.getRuntime().exec("chmod 777 " + dtvdir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		dtvdir = new File("/tmp/DTV/SndCtrDtv");
		if (!dtvdir.exists()) {
			if (!dtvdir.mkdir()) {
				return;
			}
			try {
				Runtime.getRuntime().exec("chmod 777 " + dtvdir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File channelfile = new File("/tmp/DTV/SndCtrDtv/DtvSnd.sav");

		if (!channelfile.exists()) {
			try {
				if (!channelfile.createNewFile()) {
					return;
				}
				Runtime.getRuntime().exec("chmod 777 " + channelfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(channelfile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return;
		}

		try {
			String str = new String("[");
			fos.write(str.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		if (chlist != null) {
			int i = 0;
			for (; i < chlist.length - 1; i++) {
				String tmpstr = new String("{\"ChNo\":" + "\"" + chlist[i].miChannelnumber + "\"," + "\"ChName\":" + "\"" + chlist[i].mstrServiceName + "\"" + "},");
				try {
					fos.write(tmpstr.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}

			String tmpstr = new String("{\"ChNo\":" + "\"" + chlist[i].miChannelnumber + "\"," + "\"ChName\":" + "\"" + chlist[i].mstrServiceName + "\"" + "}]");
			try {
				fos.write(tmpstr.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createOpFile() {
		File channelfile = new File("/tmp/DTV/opInfo.sav");
		if (!channelfile.exists()) {
			try {
				if (!channelfile.createNewFile()) {
					return;
				}
				Runtime.getRuntime().exec("chmod 777 " + channelfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		//Operator[] opList = DTVServiceJNI.get_settings_instance().getOPList();
		Operator curOp = DTVServiceJNI.get_settings_instance().getOP();

		if (curOp == null) {
			return;
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(channelfile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return;
		}

		try {
			int opCode = curOp.miOperatorCode;
			if ((opCode & 0x00f) == 0) {
				opCode = 0;
			}
			String str = new String(curOp.mstrOperatorName + "," + opCode);
			fos.write(str.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createCurchTmpFile(int curChIndex) {
		DTVChannelDetailInfo chInfo = DTVServiceJNI.get_channelmanager_instance().getChanneDetailInfo(curChIndex);
		if (chInfo == null) {
			return;
		}

		File dtvdir = new File("/tmp/DTV");
		if (!dtvdir.exists()) {
			if (!dtvdir.mkdir()) {
				return;
			}
			try {
				Runtime.getRuntime().exec("chmod 777 " + dtvdir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		dtvdir = new File("/tmp/DTV/DtvCurrChEpg");
		if (!dtvdir.exists()) {
			if (!dtvdir.mkdir()) {
				return;
			}
			try {
				Runtime.getRuntime().exec("chmod 777 " + dtvdir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File channelfile = new File("/tmp/DTV/DtvCurrChEpg/DtvCurrChEpg.sav");
		if (!channelfile.exists()) {
			try {
				if (!channelfile.createNewFile()) {
					return;
				}
				Runtime.getRuntime().exec("chmod 777 " + channelfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(channelfile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return;
		}

		String tmpstr = new String("[{\"ChNo\":" + "\"" + curChIndex + "\"," + "\"ChName\":" + "\"" + chInfo.mstrServiceName + "\"" + "}]");

		try {
			fos.write(tmpstr.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Override
	public void onCreate() {
		super.onCreate();
		// DTVService version
		Log.i(TAG, "\n\n当前DTVService版本为：" + DTVServiceVersion);

		Log.i(TAG, "\n\nonCreate DTVService start");

		// registerNeedRunBaseChannelUtilReceiver();//2015-2-6 YangLiu

		Log.i(TAG, "onCreate DTVService done");
	}

	// @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand DTVService " + changeDate);
		ib_Service = new DTVServiceOm();

		if (null != intent) {
			int iDtvType = intent.getIntExtra("supportDTVType", 1);
			Log.i(TAG, "Receiver----> supportDTVType!!" + iDtvType);

			DTVServiceJNI.get_system_instance().setProductType(iDtvType);
			DTVPlayer.iProductType = iDtvType;

			setPanelInfo();
		}

		try {
			context = createPackageContext("com.changhong.tvos.dtv", CONTEXT_INCLUDE_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file = context.getDir("tmp", Context.MODE_PRIVATE);
		Log.i(TAG, "libs path" + file.getPath());

		String path = file.getParent();
		Log.i(TAG, "libs path 1" + path);

		DTVServiceJNI.get_system_instance().dtvsystemStart(1, path);

		Thread callbackThread = new Thread() {
			public void run() {
				Log.i(TAG, "test callback thread entry");
				isCallbackLoopReady = true;
				DTVServiceJNI.get_system_instance().callbackLoop();
			}
		};

		callbackThread.setPriority(Thread.MAX_PRIORITY);
		callbackThread.setDaemon(true);
		callbackThread.start();

		new Thread() {
			public void run() {
				int i = 0;
				while (!isCallbackLoopReady) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i >= 100) {
						Log.e(TAG, "DTVService->onCreate i>=100****");
						break;
					}
				}
				// LSY add for test start
				if ((ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME) != null)) {
					Log.i(TAG, "com.changhong.tvos.dtv.service.DTVService is not null");
				} else {
					Log.i(TAG, "com.changhong.tvos.dtv.service.DTVService is null,add");
					ServiceManager.addService(DTVConstant.DTV_SERVICE_NAME, ib_Service);
					Log.i(TAG, "onCreate DTVService add DTVService to ServiceManager");
				}
				// LSY add for test end

				startService(new Intent("com.changhong.tvos.dtv.for3rd.DTV3rdService"));

				mlThreadid = Thread.currentThread().getId();

				Intent myintent = new Intent("com.changhong.dtvservicestatus.action");
				Bundle bundle = new Bundle();
				bundle.getLong("DtvServiceStatus", mlThreadid);

				myintent.putExtras(bundle);
				context.sendBroadcast(myintent, "com.changhong.permesion.receiver");

				Log.i(TAG, "onCreate DTVService--->SendBroadcast Over");
			}
		}.start();

		if (null != intent) {
			Log.i(TAG, "jackie>>onStartCommand>>getBooleanExtra");

			iSStartActivity = intent.getBooleanExtra("bStartDTVAP", false);
		} else {
			Log.e(TAG, "jackie>>onStartCommand>>intent is null");
		}
		Log.i(TAG, "jackie>>onStartCommand>>iSStartActivity>>" + iSStartActivity);

		new Thread() {
			public void run() {
				if (iSStartActivity) {
					Log.i(TAG, "jackie>>run>>startActivity DtvRoot");

					Intent activityIntent = new Intent();
					activityIntent.setClassName("com.changhong.tvos.dtv", "com.changhong.tvos.dtv.DtvRoot");
					activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(activityIntent);
					iSStartActivity = false;
				} else {
					Log.e(TAG, "jackie>>run>>iSStartActivity is false");
				}

				createChlistTmpFile();
				createOpFile();
			}
		}.start();

		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(60000);
						handle_HisPlayMessage();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			;
		}.start();

		timerShedulobj = TimerShedule.getInstance(context);

		return super.onStartCommand(intent, flags, startId);
	}

	//  @Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy DTVService");
		DTVServiceJNI.get_system_instance().dtvsystemStop();
	}

	//  @Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind DTVService");
		return ib_Service;
	}


	public class DTVServiceOm extends IDTVService.Stub {

		private class playerClient {
			private String mUuid;
			private int iTunerID;
			private int eLayerType;
			private int iIndex;
			private int iCLientType;
			private DTVPlayer obj;
		}

		private class channelmanagerClient {
			private String mUuid;
			private ChannelManager obj;
		}

		private DTVSettings DTVSettingsobj = null;
		private PVRRecord pvr = null;
		private List<playerClient> playerClientList = null;
		private List<channelmanagerClient> channelmanagerClientList = null;

		public DTVServiceOm() {
			playerClientList = new ArrayList<playerClient>();
			channelmanagerClientList = new ArrayList<channelmanagerClient>();
		}

		public IBinder CreateDTVPlayer(String mUuid, int iTunerID, int eLayerType, int iIndex, int iCLientType) {
			int i;
			playerClient client;
			Log.i(TAG, "[mUuid]" + mUuid + "[iTunerID]" + iTunerID + "[eLayerType]" + eLayerType + "[iIndex]" + iIndex + "[iCLientType]" + iCLientType);
			//DTVPlayer player = null;
			Log.i(TAG, "CreateDTVPlayer start");
			synchronized (playerClientList) {
				for (i = 0; i < playerClientList.size(); i++) {
					client = playerClientList.get(i);

					if (mUuid == client.mUuid) {
						Log.i(TAG, "CreateDTVPlayer find the same");
						return client.obj;
					}

					//	if(client.obj != null){
					//		player = client.obj;
					// }
				}

				client = new playerClient();
				client.mUuid = mUuid;
				client.iTunerID = iTunerID;
				client.eLayerType = eLayerType;
				client.iIndex = iIndex;
				client.iCLientType = iCLientType;

				//	if(player != null){
				//		client.obj = player;
				//	} else
				{
					client.obj = new DTVPlayer(context, mUuid, iTunerID, eLayerType, iIndex, iCLientType);
				}
				playerClientList.add(client);
			}
			Log.i(TAG, "CreateDTVPlayer end");

			return client.obj;
		}

		public int DestroyDTVPlayer(IBinder obj) {
			Log.i(TAG, "DestroyDTVPlayer:" + obj);
			playerClient client;
			int i;
			synchronized (playerClientList) {
				for (i = 0; i < playerClientList.size(); i++) {
					client = playerClientList.get(i);

					if (obj == client.obj) {
						playerClientList.remove(obj);
						return 0;
					}
				}
			}
			return -1;
		}

		public IBinder CreateChannelManager(String mUuid) {
			channelmanagerClient client;
			int i;
			for (i = 0; i < channelmanagerClientList.size(); i++) {
				client = channelmanagerClientList.get(i);
				if (mUuid == client.mUuid)
					return client.obj;
			}

			client = new channelmanagerClient();
			client.mUuid = mUuid;

			client.obj = new ChannelManager(mUuid);
			synchronized (channelmanagerClientList) {
				channelmanagerClientList.add(client);
			}

			return client.obj;
		}

		public int DestroyChannelManager(IBinder obj) {
			channelmanagerClient client;
			int i;
			synchronized (channelmanagerClientList) {
				for (i = 0; i < channelmanagerClientList.size(); i++) {
					client = channelmanagerClientList.get(i);
					if (obj == client.obj) {
						channelmanagerClientList.remove(obj);
						return 0;
					}
				}
			}
			return -1;
		}

		public IBinder GetDTVSettings() {

			if (DTVSettingsobj == null) {
				DTVSettingsobj = new DTVSettings();
			}
			return DTVSettingsobj;
		}

		public int isCanBreakdown() {
			return (DTVPlayer.isDTV_Busying == 0 || ChannelManager.bootScan == 0) ? 1 : 0;
		}

		public DTVDTTime getUTCTime() {
			playerClient client;
			int i;
			for (i = 0; i < playerClientList.size(); i++) {
				client = playerClientList.get(i);

				if (client.iTunerID == 0) {
					try {
						return client.obj.getTDTTime();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		public IBinder getTimerShudle() throws RemoteException {
			// TODO Auto-generated method stub
			if (timerShedulobj == null) {
				timerShedulobj = TimerShedule.getInstance(context);
			}

			Log.i(TAG, "create timerShudle:" + timerShedulobj);
			return timerShedulobj;
		}

		@Override
		public int SystemReset(int type) throws RemoteException {
			// TODO Auto-generated method stub
			return DTVServiceJNI.get_settings_instance().systemReset(type);
		}

		@Override
		public int requestResource(int type, int id) throws RemoteException {
			// TODO Auto-generated method stub
			return ResourceManager.getinstance().requestResource(type, id);
		}

		@Override
		public int releaseResource(int type, int id) throws RemoteException {
			// TODO Auto-generated method stub
			return ResourceManager.getinstance().releaseResource(type, id);
		}

		@Override
		public DTVSource[] getDTVSourceList() throws RemoteException {
			return ResourceManager.getinstance().getDTVSourceList();
		}

		@Override
		public IBinder GetPVR() throws RemoteException {
			if (pvr == null) {
				pvr = PVRRecord.getInstance(context);
			}
			return pvr;
		}

		@Override
		public int getPVRStatus() throws RemoteException {
			int iret = -1;
			if (pvr != null) {
				iret = pvr.getPVRStatus();
			}
			return iret;
		}
	}
}