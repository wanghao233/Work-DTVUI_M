package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.OPFeatureInfo;

public class DtvOpFeature{
	private boolean mIsEpgOnlyOneFreq;
	private boolean mIsHideManualScan;
	private boolean mIsHideListScan;
	private int mEpgFreq;
	private int mStartChannelIndex;
	public DtvOpFeature(OPFeatureInfo opFeature){
		mIsEpgOnlyOneFreq =opFeature.mbEpgOnlyOneFreq;
		mIsHideManualScan=opFeature.mbHideManualScan;
		mIsHideListScan =opFeature.mbHideListScan;
		mEpgFreq = opFeature.mEpgFreq;
		mStartChannelIndex = opFeature.miStartProgramIndex;
	}
	public int getEpgFrequency(){
		return mEpgFreq;
	}	
	
	public boolean isEpgOnlyOneFreq(){
		return mIsEpgOnlyOneFreq;
	}	
	public boolean isHideManualScan(){
		return mIsHideManualScan;
	}	
	public boolean isHideListScan(){
		return mIsHideListScan;
	}
	public int getStartChannelIndex(){
		return mStartChannelIndex;
	}
}