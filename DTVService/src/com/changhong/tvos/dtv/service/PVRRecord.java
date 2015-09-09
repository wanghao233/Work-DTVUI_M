package com.changhong.tvos.dtv.service;

import java.io.FileDescriptor;
import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.changhong.tvos.common.ISourceManager.ISourceListener;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.common.exception.SourceNotFoundException;
import com.changhong.tvos.common.exception.TVManagerNotInitException;
import com.changhong.tvos.dtv.service.jni.pvr;
import com.changhong.tvos.dtv.service.jni.pvrcallback;
import com.changhong.tvos.model.EnumInputSource;

public class PVRRecord extends IPVR.Stub {
	private static final String TAG="PVRRecord";
	private TVManager tvm = TVManager.getInstance(null);
	MySourceListener slistener =new MySourceListener();
	MemoryFile mmf;
	ParcelFileDescriptor mpfd;
	private int bufLen =96*1024;
	public int mHID;
	public int mChannelID =-1;
	public String mUrl="";
	public String mChannelName;
	public long msTime=0;
	public long meTime=0;
	private Context mContext = null;
	public static PVRRecord mInstance;
	public pvr pvrInstance;
	byte[] mainBuf =new byte[96*1024*5];
    private static EnumInputSource curSource=EnumInputSource.E_INPUT_SOURCE_DTV;
    private static FileDescriptor fDescriptor=null;
    /**for3rd**/
    private int mStatus =-1;
    private static int STATUS_START =0;
    private static int STATUS_PAUSE =1;
    private static int STATUS_STOP =2;
    public static boolean isRecordMode =false;
    /**for3rd**/
	private static final String PVRSTART_BROADCAST="com.changhong.pvr.start";
	private static final String PVRPAUSE_BROADCAST="com.changhong.pvr.pause";
	private static final String PVRSTOP_BROADCAST="com.changhong.pvr.stop";
	private static final String PVRENCRYPT_BROADCAST="com.changhong.pvr.encryptprogram";
    
	private  static DTVServiceRm rmManager = null;
	private PlayerPvrCallback pvrCallback;
	private pvrcallback jnicallback = new MyJNICallback();
	private DTVPlayer mPlayer;

	static{
		try{
			System.loadLibrary("chdtv");
			System.loadLibrary("DTVServiceJNI");			
			Log.i(TAG, "DTVService --->Load JNI LIB chdtv and DTVServiceJNI");
		}catch(java.lang.Exception e){
			e.printStackTrace();
		}
	}
    
    
	public static PVRRecord getInstance(Context context){
		if(mInstance==null){
			mInstance= new PVRRecord(context);
		}
		return mInstance;
	}
	private PVRRecord(Context context){	
		try {
			mContext =context;
			if(rmManager == null){
				rmManager =  DTVServiceRm.getinstance();
				if(rmManager != null){
					
				}else{
					Log.i(TAG,"[rmManager is null]");
				}
			}			
			tvm.getSourceManager().registerSourceListener(slistener);
			pvrInstance =pvr.getinstance();
			mmf=new MemoryFile("",bufLen);
			checkTimer();
		} catch (TVManagerNotInitException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

    /**
     * ISourceListener:get EnumInputSource from TVOS
     */
    class MySourceListener implements ISourceListener{
		@Override
		public void onSourceChange(EnumInputSource source) {
			Log.i(TAG,"[Entry onSourceChange]"+"[curSource]"+curSource
					+"[source]"+source+"[rmLock]"+DTVPlayer.rmLock.get(0));
			if(!isRecordMode){
				Log.i(TAG,"[pvr has stopped]");
				return;
			}
			if(source==EnumInputSource.E_INPUT_SOURCE_ATV){
				try {
					PVR_REC_PAUSE(mChannelID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			if(curSource==EnumInputSource.E_INPUT_SOURCE_ATV 
					&&	source !=EnumInputSource.E_INPUT_SOURCE_ATV 
					&& source !=EnumInputSource.E_INPUT_SOURCE_DTV){
				if(DTVPlayer.rmLock.get(0)==0){
					try {
						Log.i(TAG,"[lsy pvr rmLock is 0 start]");
						PVR_REC_START(mHID,mChannelID,mUrl,mChannelName,msTime,meTime);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			curSource =source;
		}
    }

    class MyJNICallback implements pvrcallback{
		@Override
		public int pvrStatusCallbck(int status) {
			Log.i(TAG,"[pvrcallback status]"+status);
			int iret =-1;
			switch(status){
			case 0:
				iret =0;
				break;
			case 1:
				iret =1;
				break;
			case 2:   //����,����¼��,������
				
				break;
			case 3:  //¼�Ƶļ�������,��¼��
				Log.i(TAG,"[crypt program stop record]");
				PVR_REC_STOP(mChannelID,0);
				break;
				default:
					break;
			}
			return iret;
		}
    }
    
    private void checkTimer(){
    	new Thread(new Runnable(){
			@Override
			public void run() {
				long curTime =-1L; 
		    	while(true){
		    		curTime=System.currentTimeMillis();
		    		if(meTime!=0 && meTime<=curTime){
		    			Log.i(TAG,"[pvr completed]"+curTime+";"+meTime);
		    			try {
		    				if(mChannelID!=-1){
		    					if(isRecordMode){
			    					PVR_REC_STOP(mChannelID);			    					
		    					}
		    					break;
		    				}
						} catch (RemoteException e){
							e.printStackTrace();
						}
		    		}
		    		
		    		try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
			}    		
    	}).start();
    }

	@Override
	public ParcelFileDescriptor getFileDescriptor() throws RemoteException {
		Log.i(TAG, "Get File Descriptor"+mmf); 
		ParcelFileDescriptor pfd=null;
		try {
			if(mmf!=null){	
				//pfd =ParcelFileDescriptor.dup(fDescriptor);
				pfd = mmf.getParcelFileDescriptor();
				Log.i(TAG,"[get start]"+(pfd==null?"null":pfd)+"[get end]"
						+"[valid]"+pfd.getFileDescriptor().valid()); 
			}
			return pfd;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
   
    
    @Override
    public int getLength(){
    	return pvrInstance.getRecLength(mChannelID);
    }

	@Override
	public int getStatus() throws RemoteException {
		return mStatus;
	}

	private void setStatus(int stat){
		mStatus =stat;
	}
	
	@Override
	public int PVR_REC_START(int hID,int ChannelID,String url,String ChannelName, long sTime, long eTime)
			throws RemoteException {
		mHID =hID;
		mChannelID =ChannelID;
		mUrl =url;
		mChannelName =ChannelName;
		msTime =sTime;
		meTime =eTime;
		Log.i(TAG,"[hID]"+hID+"[ChannelID]"+ChannelID+"[ChannelName]"+ChannelName
				+"[sTime]"+sTime+"[cTime]"+System.currentTimeMillis()
				+"[eTime]"+eTime+"[source]"+curSource+"<pvr_2015-03-02 11:02>");
		if(System.currentTimeMillis()>=eTime){
			Log.i(TAG,"[eTime is over]");
			return -1;
		}	
		try{
			EnumInputSource curS = tvm.getSourceManager().getCurInputSourceAndScence();
			Log.i(TAG,"[curS]"+curS);
			if(curS==EnumInputSource.E_INPUT_SOURCE_ATV
					|| curS==EnumInputSource.E_INPUT_SOURCE_DTV){
				Log.i(TAG,"[curS is ATV or DTV,do not record]");
				if(isRecordMode){
					Log.i(TAG,"[curS is ATV or DTV,do not record,is record mode,pause]");
					PVR_REC_PAUSE(ChannelID);
					return -1;
				}else{
					Log.i(TAG, "[do nothing]");
					return -1;
				}
			}else{
				Log.i(TAG,"can record");
			}
		}catch(SourceNotFoundException e){
			e.printStackTrace();
		} catch (TVManagerNotInitException e) {
			e.printStackTrace();
		}

		Log.i(TAG,"[lsy pvrprepare start]");
		synchronized(DTVPlayer.rmLock){	
			Log.i(TAG,"[lsy pvr rmLock]"+DTVPlayer.rmLock.get(0)
					+";"+DTVPlayer.rmLock.get(1));
			if(DTVPlayer.rmLock.get(0)==2){
				Log.i(TAG,"[lsy pvr start rmlock is 2]");
				return -1;
			}
			if(DTVPlayer.rmLock.get(0)==1){
				mPlayer.setCallback(new PlayerPvrCallback(){
					@Override
					public void ReleaseResource(int iret) {
						Log.i(TAG,"[[lsy pvr call player release success]]");
					}
					
				});
			}
			if(DTVPlayer.rmLock.get(0)!=2){
				int pret =pvrInstance.prepare();
				Log.i(TAG,"[lsy pvr start begin set 2 start]"+DTVPlayer.rmLock.get(0));
				DTVPlayer.rmLock.set(0, 2);
				pvrInstance.registerCallback(mChannelID,jnicallback);
				Log.i(TAG,"[lsy pvr start begin set 2 end]"+DTVPlayer.rmLock.get(0));
				if(pret!=0){
					Log.i(TAG,"[prepare fail]");
					return -2;
				}
			}
			
		}
		Log.i(TAG,"[lsy pvrprepare end]");

		try{
			Log.i(TAG,"[mmf]"+mmf+"[getFileDescriptor1]"+fDescriptor);
			if(fDescriptor==null){
				fDescriptor =mmf.getFileDescriptor();			
			}
			Log.i(TAG,"[fd]"+fDescriptor);
			pvrInstance.recStart(ChannelID,fDescriptor,96*1024);
			Log.i(TAG,"[recStart]");
			setStatus(STATUS_START);
			isRecordMode=true;
			Log.i(TAG,"[send pvr_start broadcast]");
			Intent intent =new Intent(PVRSTART_BROADCAST);
			intent.putExtra("CHANNELID", ChannelID);
			intent.putExtra("URL", url);
			intent.putExtra("CHANNELNAME", ChannelName);
			intent.putExtra("START", sTime);
			intent.putExtra("END", eTime);
			mContext.sendBroadcast(intent);
		}catch(IOException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int PVR_REC_PAUSE(int ChannelID) throws RemoteException {
		int iret =-1;
		synchronized(DTVPlayer.rmLock){
			iret =pvrInstance.recStop(ChannelID);
			pvrInstance.unregisterCallback(ChannelID,jnicallback);
			Log.i(TAG,"[lsy pvr release start]");		
			Log.i(TAG,"[lsy pvr pause rmlock]"+DTVPlayer.rmLock.get(0));
			if(DTVPlayer.rmLock.get(0)==2){
				pvrInstance.release();
				Log.i(TAG,"[lsy pvr pause begin set 0 start]"+DTVPlayer.rmLock.get(0));
				DTVPlayer.rmLock.set(0, 0);
				Log.i(TAG,"[lsy pvr pause begin set 0 start]"+DTVPlayer.rmLock.get(0));
			}
		}
		Log.i(TAG,"[lsy pvr release end]");
		setStatus(STATUS_PAUSE);
		Log.i(TAG,"[send pvr_pause broadcast]");
		Intent intent =new Intent(PVRPAUSE_BROADCAST);
		intent.putExtra("CHANNELID", ChannelID);
		mContext.sendBroadcast(intent);
		return iret;
	}	

	private int PVR_REC_STOP(int ChannelID,int arg){
		synchronized(DTVPlayer.rmLock){
			pvrInstance.recStop(ChannelID);
			pvrInstance.unregisterCallback(ChannelID,jnicallback);
			Log.i(TAG,"[-lsy pvr release start]");		
			if(DTVPlayer.rmLock.get(0)==2){
				pvrInstance.release();
				Log.i(TAG,"[-lsy pvr stop begin set 0 start]"+DTVPlayer.rmLock.get(0));
				DTVPlayer.rmLock.set(0, 0);
				Log.i(TAG,"[-lsy pvr stop begin set 0 end]"+DTVPlayer.rmLock.get(0));
			}
		}
		Log.i(TAG,"[-lsy pvr release end]");
		setStatus(STATUS_STOP);
		isRecordMode=false;
		Log.i(TAG,"[-send pvr_stop broadcast]"+arg);
		if(arg==0){  //�������ݲ�¼�ƹ㲥
			Intent inte = new Intent(PVRENCRYPT_BROADCAST);
			inte.putExtra("CHANNELID", ChannelID);
			inte.putExtra("URL", mUrl);
			inte.putExtra("CHANNELNAME", mChannelName);
			inte.putExtra("START", msTime);
			inte.putExtra("END", meTime);			
			mContext.sendBroadcast(inte);
		}else{
			Intent intent = new Intent(PVRSTOP_BROADCAST);
			intent.putExtra("CHANNELID", ChannelID);
			intent.putExtra("URL", mUrl);
			intent.putExtra("CHANNELNAME", mChannelName);
			intent.putExtra("START", msTime);
			intent.putExtra("END", meTime);	
			mContext.sendBroadcast(intent);
		}
		return 0;
	}
	
	
	@Override
	public int PVR_REC_STOP(int ChannelID) throws RemoteException {	
		synchronized(DTVPlayer.rmLock){
			pvrInstance.recStop(ChannelID);
			pvrInstance.unregisterCallback(ChannelID,jnicallback);
			Log.i(TAG,"[lsy pvr release start]");		
			Log.i(TAG,"[lsy pvr stop rmlock]"+DTVPlayer.rmLock.get(0));
			if(DTVPlayer.rmLock.get(0)==2){
				pvrInstance.release();
				Log.i(TAG,"[lsy pvr stop begin set 0 start]"+DTVPlayer.rmLock.get(0));
				DTVPlayer.rmLock.set(0, 0);
				Log.i(TAG,"[lsy pvr stop begin set 0 end]"+DTVPlayer.rmLock.get(0));
			}
		}
		Log.i(TAG,"[lsy pvr release end]");
		setStatus(STATUS_STOP);
		isRecordMode=false;
		Log.i(TAG,"[send pvr_stop broadcast]");
		Intent intent =new Intent(PVRSTOP_BROADCAST);
		intent.putExtra("CHANNELID", ChannelID);
		intent.putExtra("CHANNELID", ChannelID);
		intent.putExtra("URL", mUrl);
		intent.putExtra("CHANNELNAME", mChannelName);
		intent.putExtra("START", msTime);
		intent.putExtra("END", meTime);	
		mContext.sendBroadcast(intent);
		return 0;
	}

	@Override
	public int PVRSTOP() throws RemoteException {
		Log.i(TAG,"[PVRSTOP]"+mChannelID);
		if(mChannelID!=-1){
			PVR_REC_STOP(mChannelID);
		}
		return 0;
	}
	
	
	/**
	 * buffer.length must be bufLen
	 * you may call this method by:
	 * 
	 * this method not used now  
	 *   
	 **/
	@Override
	public int write(byte[] buffer) throws RemoteException {
		int iret =-1;
		int destPos =0;
		if(buffer!=null && buffer.length!=bufLen)
			return iret;
		try {
			mmf.writeBytes(buffer, 0, destPos, bufLen);
			destPos =destPos+bufLen;
			iret =0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return iret;
	}
	
	@Override
	public void refreshRecBuffer(int ChannelID) throws RemoteException {
		pvrInstance.refreshRecBuffer(ChannelID);
	}
	
	public void setCallback(PlayerPvrCallback callback){
		pvrCallback =callback;
		//release resource
		Log.i(TAG,"[lsy player call pvr release start]");
		int iret =-1;
		try {
			iret =PVR_REC_PAUSE(mChannelID);	
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Log.i(TAG,"[lsy player call pvr release end]");
		pvrCallback.ReleaseResource(iret);
	}

	public void setPlayer(DTVPlayer player){
		this.mPlayer =player;
	}
	@Override
	public int getPVRStatus() throws RemoteException {
		Log.i(TAG,"[getPVRStatus]"+isRecordMode);
		return (isRecordMode==true?1:0);
	}
}
