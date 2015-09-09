package com.changhong.tvos.dtv.tvap;

import com.changhong.tvos.dtv.DtvMessageThread;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstStringKey;

public class DtvDebugManager {
    public static DtvDebugManager mDebugManager;
    private boolean isDebug;
    private DtvConfigManager mDtvConfigManager;
    
    private DtvDebugManager(){
    	mDtvConfigManager = DtvConfigManager.getInstance();
    	isDebug = Boolean.valueOf(mDtvConfigManager.getCommenValue(ConstStringKey.USER_SET_IS_DEBUG));
    }
    
    public synchronized static DtvDebugManager getInstance(){
    	if(null == mDebugManager){
    		mDebugManager = new DtvDebugManager();
    	}
    	
    	return mDebugManager;
    }
    
    public void setDebug(boolean debug){
    	isDebug =  debug;
    	mDtvConfigManager.setCommenValue(ConstStringKey.USER_SET_IS_DEBUG, String.valueOf(debug));
    	if(debug){
    		DtvMessageThread.startLogOut();
    	}else{
    		DtvMessageThread.stopLogOut();
    	}
    }
    
    public void checkOutSystemErr(){
    	DtvMessageThread.checkOutSystemErrInfo();
    }
    
    public boolean isDebug(){
    	return isDebug;
    }
} 
