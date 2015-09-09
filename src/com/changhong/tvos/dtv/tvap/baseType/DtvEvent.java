package com.changhong.tvos.dtv.tvap.baseType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.util.Log;
import com.changhong.tvos.dtv.vo.EPGEvent;
import com.changhong.tvos.dtv.vo.UTCDate;
import com.changhong.tvos.dtv.vo.UTCTime;

public class DtvEvent {//节目事务信息，每个时间段的节目

	private static final String TAG = " Event";
	private int mEventID;
	private Date mStartTime;
	private Date mEndTime;
	private String mEventName;
	private String mShortText;

	public DtvEvent(int mEventID, Date mStartTime, Date mEndTime,
			String mEventName, String mShortText) {
		this.mEventID = mEventID;
		this.mStartTime = mStartTime;
		this.mEndTime = mEndTime;
		this.mEventName = mEventName;
		this.mShortText = mShortText;
	}

	public DtvEvent(Date mStartTime, String mEventName, String mShortText) {
		this.mStartTime = mStartTime;
		this.mEventName = mEventName;
		this.mShortText = mShortText;
	}

	public DtvEvent() {

	}

	public DtvEvent(EPGEvent event) {
		// TODO Auto-generated constructor stub
		if (event != null) {
			mEventID = event.miEventID;
			mStartTime = new Date(chTimeToLong(event.startDate,
					event.startTime));
			mEndTime = new Date(chTimeToLong(event.startDate,
					event.startTime, event.duringTime));
			mEventName = event.eventName;
			mShortText = event.shortText;
		}
	}


	public String getTitle() {
		return mEventName;
	}

	public void setTitle(String title) {
		mEventName = title;
	}

	public String getDescribe() {

		return mShortText;
	}

	public Date getStartTime() {
		return mStartTime;
	}

	public void setStartTime(Date start) {
		mStartTime = start;
	}

	public void setEndTime(Date end) {
		mEndTime = end;
	}

	public Date getEndTime() {
		return mEndTime;
	}

	public boolean equals(DtvEvent event) {
		if(null == event || null == event.mStartTime || null == mStartTime){
			return false;
		}else {
		if (mStartTime.getTime()/1000L == event.mStartTime.getTime()/1000L) {
			Log.e(TAG,"LL DtvEvent startime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm ").format(mStartTime) + ",titile = " + mEventName);
			return true;
		} else {
			return false;
		}
	}
	}

	private long chTimeToLong(UTCDate date, UTCTime time) {
		long chTime = 0;
		if (date != null && time != null) {
			Calendar cal = Calendar.getInstance();
			cal.set(date.miYear, date.miMonth - 1, date.miDay, time.miHour,
					time.miMinute, time.miSecond);
			chTime = cal.getTimeInMillis();
		}
		return chTime;
	}

	private long chTimeToLong(UTCDate date, UTCTime startTime,
			UTCTime duringTime) {
		long chTime = 0;
		if (date != null && startTime != null && duringTime != null) {
			Calendar cal = Calendar.getInstance();
			cal.set(date.miYear, date.miMonth - 1, date.miDay,
					startTime.miHour + duringTime.miHour, startTime.miMinute
							+ duringTime.miMinute, startTime.miSecond
							+ duringTime.miSecond);
			chTime = cal.getTimeInMillis();
		}
		return chTime;
	}
}
