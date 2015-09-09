package com.changhong.tvos.dtv.tvap;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.util.Log;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.UTCDate;
import com.changhong.tvos.dtv.vo.UTCTime;

/**
 * DtvEpg�Ĺ�����
 * @author YangLiu
 *
 */
public class DtvEpgManager{
	
	public static final String TAG = "DtvEpgManager";
	private static DtvEpgManager mDtvEpgManager =null;
	private List<DtvEvent> mEventList;
	private DtvEvent [] mPFinfo;
	private DtvInterface mDtvInterface =DtvInterface.getInstance();
	private DtvEpgManager(){
		
	}
	
	//getInstance()����
	public static DtvEpgManager getInstance(){
		if(mDtvEpgManager ==null){
			mDtvEpgManager =new DtvEpgManager();
		}
		return mDtvEpgManager;
	}
	
	//ȡ�ý�Ŀ��PF��Ϣ
	public DtvEvent[] getPFInfo(int proramIndex){
		mPFinfo =mDtvInterface.getPFInfo(proramIndex);
		return mPFinfo;
	}
	
	//ȡ����ֹʱ�䵽����ʱ���ڵĽ�Ŀ��Ϣ
	public List<DtvEvent> getEventsByTime(int proramIndex ,int dayIndex,int startTime, int endTime){
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
		Calendar cal = Calendar.getInstance();
		
		long sTime = mDtvInterface.getCurrentDate().getTime() + (dayIndex * 24 + startTime) * 60 * 60 * 1000L;

//		if(0 != dayIndex){			
		cal.setTimeInMillis(sTime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
//		}
		UTCDate sUTCDate = new UTCDate();
		sUTCDate.miYear = cal.get(Calendar.YEAR);        
		sUTCDate.miMonth = cal.get(Calendar.MONTH)+1;
        sUTCDate.miDay = cal.get(Calendar.DAY_OF_MONTH);   
        UTCTime sUTCTime = new UTCTime();
        sUTCTime.miHour = cal.get(Calendar.HOUR_OF_DAY);
        sUTCTime.miMinute = cal.get(Calendar.MINUTE);
        sUTCTime.miSecond = cal.get(Calendar.SECOND);
        DTVDTTime sDTTime = new DTVDTTime(sUTCDate, sUTCTime);
        Log.i(TAG,"LL getEventsByTime>>sUTCDate>>year = " + sDTTime.mstruDate.miYear + ", month = " + sDTTime.mstruDate.miMonth + ", day = " + sDTTime.mstruDate.miDay
				+ ", hour = " + sDTTime.mstruTime.miHour + ", minute = " + sDTTime.mstruTime.miMinute);
//      long eTime = sTime+endTime * 60 * 60 * 1000L-1;
//      cal.setTimeInMillis(eTime);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        UTCDate eUTCDate = new UTCDate();
        eUTCDate.miDay = cal.get(Calendar.DAY_OF_MONTH);
        eUTCDate.miMonth = cal.get(Calendar.MONTH)+1;
        eUTCDate.miYear = cal.get(Calendar.YEAR);
        UTCTime eUTCTime = new UTCTime();
        eUTCTime.miHour = cal.get(Calendar.HOUR_OF_DAY);
        eUTCTime.miMinute = cal.get(Calendar.MINUTE);
        eUTCTime.miSecond = cal.get(Calendar.SECOND);
        DTVDTTime eDTTime = new DTVDTTime(eUTCDate, eUTCTime);
        Log.i(TAG,"LL getEventsByTime>>eUTCDate>>year = " + eDTTime.mstruDate.miYear + ", month = " + eDTTime.mstruDate.miMonth + ", day = " + eDTTime.mstruDate.miDay
				+ ", hour = " + eDTTime.mstruTime.miHour + ", minute = " + eDTTime.mstruTime.miMinute);
//		Log.i(TAG,"LL " + f.format(mDtvInterface.getCurrentDate().getTime()));
		mEventList =mDtvInterface.getEventsBytime(proramIndex,sDTTime,eDTTime);
		return mEventList;
	}
	
	/**
	 * get events after curDate among programList
	 * �ӽ�Ŀ�б�programList�л�ȡcurDate֮��Ľ�Ŀ�¼�   2014-12-30
	 * @param curDate ��ǰʱ��
	 * @param programList �ܵĽ�Ŀ�б�
	 * @return
	 */
	public List<DtvEvent> getEventsFromNowOn(Date curDate, List<DtvEvent> programList){
		int ProgramListPosition = 0;
		for (int i = 0; i < programList.size(); i++) {
			if((programList.get(i).getStartTime().before(curDate)) && 
					(programList.get(i).getEndTime().after(curDate))){																						
				ProgramListPosition = i; 
			}		
		}
		programList = programList.subList(ProgramListPosition, programList.size());
		return programList;	
	}
	
	//��ȡ��ǰ���ڲ��Ž�Ŀ��λ��
	public int getPlayingTVProgramPositon(List<DtvEvent> events) {
		if (events == null) {
			return 0;
		}
		final Date now = mDtvInterface.getCurrentDate();
		final Long time = now.getTime();
		Log.v("EPGChannelInfo", "***********now"+time);
		int i = 0;
		int innerIndex = 0;
		int outerIndex = 0;
		int index = 0;
		for (i = 0; i < events.size(); i++) {
			Log.v("EPGChannelInfo", "***********"+events.get(i).getStartTime().getTime());
			
			if (time >= events.get(i).getStartTime().getTime()
					&& time <= events.get(i).getEndTime().getTime()) {
				innerIndex = i;
				Log.v("EPGChannelInfo", "getPlayingTVProgramPositon()>>innerIndex = " + innerIndex);
			}else if(time < events.get(i).getStartTime().getTime()){
				outerIndex =i-1;
				Log.v("EPGChannelInfo", "getPlayingTVProgramPositon()>>outerIndex = " + outerIndex);
				break;
			}
		}
		if(i>=events.size()){
			index = events.size() - 1;
//			index = -1;
		}else if(outerIndex == -1){
			index = outerIndex;
		}else{			
			index = (innerIndex>outerIndex)?innerIndex:outerIndex;
		}
		Log.v("EPGChannelInfo", "getPlayingTVProgramPositon()>>index = " + index);
		return index;
	}
}