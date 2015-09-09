package com.changhong.tvos.dtv.tvap.baseType;

import java.util.Date;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import android.util.Log;

public class DtvScheduleEvent {

	private static final String TAG = "DtvScheduleEvent";
	private Date mStartTime;
	private Date mEndTime;
	private int mServiceIndex;
	private int mProgramNum;
	private String mProgramName;
	private String mEventName;
	private int mSourceID = ConstSourceID.DVBC;
	private DtvSourceManager mSourceManger = DtvSourceManager.getInstance();

	public DtvScheduleEvent(Date startTime, Date endTime, int sourceID,int serviceIndex,
			int programNum, String programName, String eventName) {
		this.mStartTime = startTime;
		this.mEndTime = endTime;
		this.mSourceID = sourceID;
		this.mServiceIndex = serviceIndex;
		this.mProgramNum = programNum;
		this.mProgramName = programName;
		this.mEventName = eventName;
	}
	
	public DtvScheduleEvent(Date startTime, Date endTime, int serviceIndex,
			int programNum, String programName, String eventName) {
		this.mStartTime = startTime;
		this.mEndTime = endTime;
		this.mServiceIndex = serviceIndex;
		this.mProgramNum = programNum;
		this.mProgramName = programName;
		this.mEventName = eventName;
		this.mSourceID = mSourceManger.getCurSourceID();
	}
	
	public DtvScheduleEvent(Date mStartTime, String mEventName,
			DtvProgram program) {
		this.mStartTime = mStartTime;
		this.mEventName = mEventName;
		this.mProgramNum = program.mProgramNum;
		this.mServiceIndex = program.mServiceIndex;
		this.mProgramName = program.mProgramName;
		this.mSourceID = mSourceManger.getCurSourceID();
	}

	public DtvScheduleEvent(Date mStartTime, Date mEndTime, String mEventName,
			DtvProgram program) {
		this.mStartTime = mStartTime;
		this.mEndTime = mEndTime;
		this.mProgramNum = program.mProgramNum;
		this.mServiceIndex = program.mServiceIndex;
		this.mProgramName = program.mProgramName;
		this.mEventName = mEventName;
		this.mSourceID = mSourceManger.getCurSourceID();
	}

	public DtvScheduleEvent(Date mStartTime, String mEventName,
			int mProgramNum, int mProgramIndex, String mProgramName) {
		this.mStartTime = mStartTime;
		this.mEventName = mEventName;
		this.mProgramNum = mProgramNum;
		this.mServiceIndex = mProgramIndex;
		this.mProgramName = mProgramName;
		this.mSourceID = mSourceManger.getCurSourceID();
	}

	public DtvScheduleEvent(int mChannelIndex, String mChannelName,
			String mEventTitle, Date mStartTime, Date mEndTime) {
		// TODO Auto-generated constructor stub
		this.mServiceIndex = mChannelIndex;
		this.mProgramName = mChannelName;
		this.mEventName = mEventTitle;
		this.mStartTime = mStartTime;
		this.mEndTime = mEndTime;
		this.mSourceID = mSourceManger.getCurSourceID();
	}

	public DtvScheduleEvent(DtvEvent event, DtvProgram program) {
		this.mStartTime = event.getStartTime();
		this.mEventName = event.getTitle();
		this.mEndTime = event.getEndTime();
		this.mProgramNum = program.mProgramNum;
		this.mServiceIndex = program.mServiceIndex;
		this.mProgramName = program.mProgramName;
		this.mSourceID = mSourceManger.getCurSourceID();
	}

	public DtvScheduleEvent(DtvEvent event) {
		this.mStartTime = event.getStartTime();
		this.mEventName = event.getTitle();
		this.mEndTime = event.getEndTime();
		this.mSourceID = mSourceManger.getCurSourceID();
	}

	public String getTitle() {
		return mEventName;
	}

	public String getProgramName() {

		return mProgramName;
	}

	public int getProgramNum() {

		return mProgramNum;
	}

	public int getProgramServiceIndex() {

		return mServiceIndex;
	}

	public Date getStartTime() {
		return mStartTime;
	}

	public Date getEndTime() {
		return mEndTime;
	}
	public int getSourceID(){
		return mSourceID;
	}

	public boolean equals(DtvScheduleEvent event) {
		if (this.mStartTime.getTime() / 1000L == event.mStartTime.getTime() / 1000L
				&& this.mServiceIndex == event.mServiceIndex&&this.mSourceID == event.mSourceID) {
			Log.i(TAG,"LL equals()>>this.mServiceIndex="+this.mServiceIndex+",event.mServiceIndex="+event.mServiceIndex);
			return true;
		} else {
			return false;
		}
	}

	public boolean conflicts(DtvScheduleEvent event) {
		if (this.mStartTime.getTime() / 1000L == event.mStartTime.getTime() / 1000L
				&& (this.mServiceIndex != event.mServiceIndex || this.mSourceID != event.mSourceID)) {
			Log.i(TAG,"LL conflicts()>>this.mServiceIndex="+this.mServiceIndex+",event.mServiceIndex="+event.mServiceIndex);
			return true;
		} else {
			return false;
		}
	}
}
