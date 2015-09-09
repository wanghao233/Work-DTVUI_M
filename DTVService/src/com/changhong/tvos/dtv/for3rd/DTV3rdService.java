package com.changhong.tvos.dtv.for3rd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import com.changhong.tvos.dtv.for3rd.For3rdConst.ConstantPlayerNotifyEvent;
import com.changhong.tvos.dtv.service.IChannelManager;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.service.IDTVSettings;
import com.changhong.tvos.dtv.service.ITimerShedule;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.PlayStatusInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class DTV3rdService extends Service{
	final static String TAG = "DTV3rdService";
	
	private UUID  	muuid;
	IBinder ib_Service = null;
	
	public List<DTV3rdManager> clientList = null;
	
	private IDTVService 	mIDtvServer = null;
	private IDTVSettings 	mIDTVSettingsServer = null;
	private IChannelManager mIChannelManagerServer = null;
	private ITimerShedule   mITimerServer = null;
	
	private PlayerNotifyEventListener listener = new PlayerNotifyEventListener();
   
 //   @Override
    public void onCreate() {
    	super.onCreate();
    	
    	muuid = UUID.randomUUID();
    	
    	ib_Service = new Serverbinder();

    	new Thread(){  
    		public void run(){  
    			ServiceManager.addService("com.changhong.tvos.dtv.for3rd.DTV3rdService", ib_Service);
    			Log.i(TAG, "onCreate DTVService add DTV3rdService to ServiceManager");
    		}  
    	}.start();  

    	Log.i(TAG, "onCreate DTV3rdService");
    }

 //   @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
    	super.onStartCommand(intent, flags, startId);
    	Log.i(TAG,"[onStartCommand]");
		IBinder binder;
		
		binder = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
		if(binder != null)
		{
			mIDtvServer = IDTVService.Stub.asInterface(binder);
			
			try{
				binder = mIDtvServer.GetDTVSettings();
				if(binder != null)
				{
					mIDTVSettingsServer = IDTVSettings.Stub.asInterface(binder);
				}
			}
			catch (RemoteException exception){
				//nothing to do!
			}
			
			try{
				binder = mIDtvServer.CreateChannelManager(muuid.toString());
				if(binder != null){
					mIChannelManagerServer = IChannelManager.Stub.asInterface(binder);
					
					//2013-02-22
					//mIChannelManagerServer.setChannelSource(DTVConstant.ConstDemodType.DVB_C, 0);
				}
			}
			catch (RemoteException exception){
				//nothing to do!
			}
			
			try{
				binder = mIDtvServer.getTimerShudle();
				if(binder != null){
					mITimerServer = ITimerShedule.Stub.asInterface(binder);
				}
			}
			catch (RemoteException exception){
				Log.i(TAG, "DTV3rdManager getTimerShudle binder failed!");;
			}
		}

	    IntentFilter intentFilter = new IntentFilter(DTVConstant.DTV_PLAYER_STATUS_CHANGE); 
		registerReceiver(listener, intentFilter);

		new Thread(){  
            public void run(){  

            	while(true){
            		try {
            			Thread.sleep(1000);
            		} 
            		catch (InterruptedException e) {
            			// TODO Auto-generated catch block
            			continue;
            		}
            		
	            	synchronized(clientList){
	            		DTV3rdManager tempclient = null;
	            		for(Iterator<DTV3rdManager> i = clientList.iterator(); i.hasNext();){
	            			tempclient = i.next();
	            			if((tempclient.mPlayerEventLister != null) 
	            					&& (tempclient.mheartbeatCheck.isBinderAlive() == false))
	            			{
	            				if(tempclient.mPlayer != null){
	        	            		try {
	        	            			tempclient.Release();
	        	            		}
	        	            		catch(RemoteException e){
	         							// Nothing to do
	         						}
	            				}
	            				i.remove();
	        //    				clientList.remove(tempclient);
	            			}
	            		}
	            	}
            	}
            }  
        }.start();  
		
        return 0; 
    }
    
  //  @Override
    public void onDestroy() {
    	Log.i(TAG, "onDestroy DTVService");
    	super.onDestroy();
    }

  //  @Override
    public IBinder onBind(Intent intent) {
    	Log.i(TAG, "onBind DTVService");
    	return ib_Service;
    }
    
    public class Serverbinder extends IDTV3rdService.Stub {
    	
    	public Serverbinder(){
    		clientList = new ArrayList<DTV3rdManager>();
    	}

		@Override
		public IBinder createDTVManager(String uuid, IBinder playerListerner) throws RemoteException {
			// TODO Auto-generated method stub
			DTV3rdManager client;
			
			Log.i(TAG,"th:"+Thread.currentThread().getId());
			Log.i(TAG, "uuid:"+uuid+", getCallingPid:"+ getCallingPid()+", getCallingUid:"+getCallingUid());
			client = new DTV3rdManager(uuid, getCallingPid(), getCallingUid(), playerListerner, mIDtvServer, 
					mIDTVSettingsServer, mIChannelManagerServer,mITimerServer);
			Log.i(TAG,"1");
			if(mIDtvServer == null){
				Log.i(TAG,"2");
				mIDtvServer = client.mIDtvServer;
			}
			Log.i(TAG,"3");
			if(mIDTVSettingsServer == null){
				Log.i(TAG,"4");
				mIDTVSettingsServer = client.mIDTVSettingsServer;
				Log.i(TAG,"5");
			}
			Log.i(TAG,"6");
			if(mIChannelManagerServer == null){
				Log.i(TAG,"7");
				mIChannelManagerServer = client.mIChannelManagerServer;
				Log.i(TAG,"8");
			}
			Log.i(TAG,"9");
			if(mITimerServer == null){
				Log.i(TAG,"10");
				mITimerServer = client.mITimerServer;
				Log.i(TAG,"11");
			}
			Log.i(TAG,"12");
        	synchronized(clientList){
        		Log.i(TAG,"13");
        		clientList.add(client);
        		Log.i(TAG,"14");
        	}
        	Log.i(TAG,"15");
			return client;
		}
    }
    
	public class PlayerNotifyEventListener extends BroadcastReceiver{
			@Override
			public void onReceive(Context context, Intent intent){
				Log.i(TAG,"get Player status broadcast:com.changhong.tvos.dtv.DTV_PLAYER_STATUS_CHANGE");
				if((clientList == null) || (clientList.size() == 0)){
					return;
				}
				
				Bundle tmpBoundle = intent.getExtras();
				PlayStatusInfo status = null;
				status = tmpBoundle.getParcelable(DTVConstant.BroadcastConst.MSG_INFO_NAME);
				Log.i(TAG,"status="+status);
				if(status == null){
					return;
				}
				Log.i(TAG,"status.mbIsUseUUID="+status.mbIsUseUUID+
						" status.mPlayerUuid="+status.mPlayerUuid+
						" status.miPlayEvent="+status.miPlayEvent);
				DTV3rdManager tempclient = null;
				synchronized(clientList){
 				for(Iterator<DTV3rdManager> i = clientList.iterator(); i.hasNext();){
 					tempclient = i.next();
 					if(tempclient.mPlayer == null){
 						continue;
 					}
 					Log.i(TAG,"tempclient.mUuid="+tempclient.mUuid+
 							" tempclient.mPlayerEventLister"+tempclient.mPlayerEventLister);
 					if(((status.mbIsUseUUID == false) || 
 							((status.mPlayerUuid != null )&& 
 									(status.mPlayerUuid.toString() == tempclient.mUuid)))){
 						if(tempclient.mPlayerEventLister == null){
 							Log.i(TAG,"tempclient status listener is null,return");
 							return;
 						}
 						
 						int event = 0;
 						switch(status.miPlayEvent){
 							case DTVConstant.ConstPlayerEvent.NO_SIGNAL:
 								event = ConstantPlayerNotifyEvent.NO_SIGNAL;
 								break;
 							case DTVConstant.ConstPlayerEvent.CA_ENTITLE:
 								event = ConstantPlayerNotifyEvent.CA_NOT_ENTITLE;
 								break;
 							case DTVConstant.ConstPlayerEvent.FORCE_PAUSE:
 								event = ConstantPlayerNotifyEvent.FORCE_PAUSE;
 								break;
 							case DTVConstant.ConstPlayerEvent.FORCE_RESUME:
 								event = ConstantPlayerNotifyEvent.FORCE_RESUME;
 								break;
 							case DTVConstant.ConstPlayerEvent.NO_CHANNEL:
 								event = ConstantPlayerNotifyEvent.NO_SIGNAL;
 								break;
 							case DTVConstant.ConstPlayerEvent.NO_SAMRT_CARD:
 								event = ConstantPlayerNotifyEvent.NO_SAMRT_CARD;
 								break;
 							case DTVConstant.ConstPlayerEvent.OK:
 								event = ConstantPlayerNotifyEvent.OK;
 								break;
 							case DTVConstant.ConstPlayerEvent.SAMRT_CARD_ERROR:
 								event = ConstantPlayerNotifyEvent.SAMRT_CARD_ERROR;
 								break;
 							case DTVConstant.ConstPlayerEvent.STATUS_ERROR:
 								event = ConstantPlayerNotifyEvent.STATUS_ERROR;
 								break;
 							default:
 								return;
 						}
 						
 						Log.i(TAG,"PlayerNotifyEventListenerToTVOS:" + event);
 						try{
 							tempclient.mPlayerEventLister.onPlayerNotifyEvent(event);
 						}
 						catch(RemoteException e){
 							// Nothing to do
 							Log.i(TAG,"totvos error "+e.getMessage());
 						}
 					}
 				}
 			}
		}
}
}
