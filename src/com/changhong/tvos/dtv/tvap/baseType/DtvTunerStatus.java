package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.DTVTunerStatus;

public class DtvTunerStatus{
	private int 		mSignalLevel =0;
	private int 		mSignalQuality =0;
	private boolean	mIsLock =false;
	private int mBitErrorRate = 0;
	public DtvTunerStatus(DTVTunerStatus TunerStatus){	
		if(TunerStatus !=null){
		mSignalLevel = TunerStatus.miSignalLevel; 
		mSignalQuality = TunerStatus.miSignalQuality;  
		mIsLock = TunerStatus.mbLock;
		mBitErrorRate = TunerStatus.miBitErrorRate;
		}
	}
	public int getSignalLevel(){
		return mSignalLevel;
	}
	public int getSignalQuality(){
		return mSignalQuality;
	}
	public boolean isLock(){
		return mIsLock;
	}
	public int mBitErrorRate(){
		return mBitErrorRate;
	}
}