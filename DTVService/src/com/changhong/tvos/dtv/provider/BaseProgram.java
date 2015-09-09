package com.changhong.tvos.dtv.provider;

import java.util.Date;
import com.changhong.tvos.dtv.provider.BaseProgramManager.ConstSourceID;
import android.util.Log;

public class BaseProgram {
	private Date mStartTime;
	private Date mEndTime;
	private int mSourceID = ConstSourceID.DVBC;
	private int mOriginal;
//	private int mSourceID;
	private int mServiceIndex;
	private int mProgramNum;
	private String mProgramName;
	private String mEventName;
	private String mWikiInfo;
	private static final String TAG="BaseProgram";
	public BaseProgram() {
		super();
	}

	public BaseProgram(Date mStartTime, Date mEndTime, int mSourceID,
			int mOriginal , int mServiceIndex, int mProgramNum, 
			String mProgramName, String mEventName, String mWikiInfo) {
		super();
		this.mStartTime = mStartTime;
		this.mEndTime = mEndTime;
		this.mSourceID = mSourceID;
		this.mOriginal = mOriginal;
		this.mServiceIndex = mServiceIndex;
		this.mProgramNum = mProgramNum;
		this.mProgramName = mProgramName;
		this.mEventName = mEventName;
		this.mWikiInfo = mWikiInfo;
	}

	public Date getStartTime() {
		return mStartTime;
	}

	public void setStartTime(Date mStartTime) {
		this.mStartTime = mStartTime;
	}

	public Date getEndTime() {
		return mEndTime;
	}

	public void setEndTime(Date mEndTime) {
		this.mEndTime = mEndTime;
	}

	public int getSourceID() {
		return mSourceID;
	}

	public void setSourceID(int mSourceID) {
		this.mSourceID = mSourceID;
	}

	public int getOriginal() {
		return mOriginal;
	}

	public void setOriginal(int mOriginal) {
		this.mOriginal = mOriginal;
	}

	public int getServiceIndex() {
		return mServiceIndex;
	}

	public void setServiceIndex(int mServiceIndex) {
		this.mServiceIndex = mServiceIndex;
	}

	public int getProgramNum() {
		return mProgramNum;
	}

	public void setProgramNum(int mProgramNum) {
		this.mProgramNum = mProgramNum;
	}

	public String getProgramName() {
		return mProgramName;
	}

	public void setProgramName(String mProgramName) {
		this.mProgramName = mProgramName;
	}

	public String getEventName() {
		return mEventName;
	}

	public void setEventName(String mEventName) {
		this.mEventName = mEventName;
	}

	public String getWikiInfo() {
		return mWikiInfo;
	}

	public void setWikiInfo(String mWikiInfo) {
		this.mWikiInfo = mWikiInfo;
	}

	@Override
	public String toString() {
		return "BaseProgram [mStartTime=" + mStartTime + ", mEndTime="
				+ mEndTime + ", mSourceID=" + mSourceID + ", mOriginal="
				+ mOriginal + ", mServiceIndex=" + mServiceIndex
				+ ", mProgramNum=" + mProgramNum + ", mProgramName="
				+ mProgramName + ", mEventName=" + mEventName + ", mWikiInfo="
				+ mWikiInfo + "]";
	}

	public boolean equals(BaseProgram baseProgram) {
		if (this.mStartTime.getTime() / 1000L == baseProgram.mStartTime.getTime() / 1000L
				&& this.mServiceIndex == baseProgram.mServiceIndex&&this.mSourceID == baseProgram.mSourceID
				&& this.mOriginal == baseProgram.mOriginal) {
			Log.i(TAG,"LL equals()>>this.mServiceIndex="+this.mServiceIndex+",event.mServiceIndex="+baseProgram.mServiceIndex);
			return true;
		} else {
			return false;
		}
	}

	public boolean conflicts(BaseProgram baseProgram) {
		if (this.mStartTime.getTime() / 1000L == baseProgram.mStartTime.getTime() / 1000L) {
			Log.i(TAG,"LL conflicts()>>this.mStartTime="+this.mStartTime.getTime() / 1000L+",baseProgram.mStartTime="+baseProgram.mStartTime.getTime() / 1000L);
			return true;
		} else {
			return false;
		}
	}
}