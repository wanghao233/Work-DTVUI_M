package com.changhong.tvos.dtv.tvap;

import java.util.Date;
import android.view.KeyEvent;

public class DtvCommonManager {
	private static DtvInterface mDtvInterface =null; 
	private static DtvCommonManager mDtvCommonManager = null;
	private DtvCommonManager(){}
	public static DtvCommonManager getInstance(){
		if(null == mDtvCommonManager){
			mDtvInterface =DtvInterface.getInstance();
			mDtvCommonManager = new DtvCommonManager();
		}
		return mDtvCommonManager;
	}

	//dtv资源准备
	public int dtvResourcePrepare(){
		return mDtvInterface.resourcePrepare();
	}
	
	//dtv资源释放
	public void dtvResourceRelease(){
		mDtvInterface.resourceRelease();
	}
	
	//开启服务广播
	public boolean startBootService(){
		return mDtvInterface.startBootService();
	}
	
	//检查版本号
	public boolean checkVersion(int curVersion){
		return mDtvInterface.checkVersion(curVersion);
	}
	
	//获取当前时间
	public Date getCurrentDate() {
		return mDtvInterface.getCurrentDate();
	}

	//进去屏保
	public boolean enterScreenSaver(){
		return mDtvInterface.enterScreenSaver();
	}
	
	//进入常规设置
	public void enterCommonSetting(){
		mDtvInterface.enterCommonSetting();
	}
	
	//是否是键盘按键
	public boolean isKeyboardKey(int keyCode,KeyEvent event){
		boolean isKeyboardKey = false;
		isKeyboardKey = mDtvInterface.isKeyboardKey(keyCode, event);
		return isKeyboardKey;
	}
	
	//更新键盘转化标志
	public void updateKeyboardConvertFlag(boolean flag){
		mDtvInterface.updateKeyboardConvertFlag(flag);
	}
	
	//返回上次的信号源
	public boolean returnLastInputSource(){
		return mDtvInterface.returnLastInputSource();
	}

	//返回系统3D
    public boolean returnSystem3D(){
		return mDtvInterface.getSystem3D();
	}

    //获取SW版本号
	public String getSWVersion(){
		return mDtvInterface.getSWVersion();
	}
	
	//开始信号源
	public boolean startInputSource(int channelNum){
		return mDtvInterface.startInputSource(channelNum);
	}
	
	//获取播放状态
	public int getPlayStatus(){
		return mDtvInterface.getPlayerStatus();
	}
	
	//设置正在播放的节目位置
	public void setPlayingProgramIndex(int programIndex){
		mDtvInterface.setPlayingProgramIndex(programIndex);
	}
}