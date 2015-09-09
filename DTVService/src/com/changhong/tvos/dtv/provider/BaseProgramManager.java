package com.changhong.tvos.dtv.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.changhong.tvos.dtv.service.TimerShedule;
import com.changhong.tvos.dtv.vo.TimerInfo;
import com.changhong.tvos.dtv.vo.TimerInfo.TimerType;

/**
 * 预约事件管理器
 * @author YangLiu
 */
public class BaseProgramManager {

	private static final String TAG = "BaseProgramManager";
	Context mcontext = null;
	private static BaseProgramManager mBaseProgramManager = null;
	private TimerShedule mTimerShedule = null;
	public static final int EPG_TIMER = TimerType.EPG_TIMER;
	
	//接受service Timer 触发
	public static final String EVENT_SCHEDULE_BROADCAST = "com.changhong.tvos.dtv.tvap.DtvScheduleManager.EVENT_SCHEDUL_BROADCAST";
	//主场景预约
	public static final String TIMER_BROADCAST_ACTION ="com.changhong.tvos.dtv.timer";
	
	public class ConstSourceID{
		public static final int DVBC = 1;
		public static final int DTMB = 2;
	}
	
	public BaseProgramManager(Context context) {
		mcontext = context;
		mTimerShedule = TimerShedule.getInstance(context);
	}
	
	public synchronized static BaseProgramManager getInstance(Context context) {
		if(mBaseProgramManager != null){
			return mBaseProgramManager;
		}
		
		mBaseProgramManager = new BaseProgramManager(context);
		return mBaseProgramManager;
	}
	
	
	/**
	 * add方法，添加开机节目
	 * @param baseProgram
	 */
	public synchronized void addScheduleProgram(BaseProgram baseProgram) {
		// TODO Auto-generated method stub
		TimerInfo timerInfo = null;
		if (baseProgram != null) {
			Log.i(TAG,
					"LL addScheduleProgram()>>" 
							+"	getStartTime = "+ baseProgram.getStartTime() 
							+"	getSourceID= "+baseProgram.getSourceID()
							+"	getOriginal= "+baseProgram.getOriginal()
							+"	getWikiInfo= " +baseProgram.getWikiInfo() 
							+"	getProgramName= "+ baseProgram.getProgramName());

			timerInfo = convertScheduleProgramToTimerInfo(baseProgram);
			/*Log.i("YangLiu", "timerInfo="+"----mlStartNotifyTime="+timerInfo.mlStartNotifyTime+
										  "----mlNotNotifyTime="+timerInfo.mlNotNotifyTime+
										  "----miIndex="+timerInfo.miIndex+
										  "----mOriginal="+timerInfo.mOriginal+
										  "----miType="+timerInfo.miType+
										  "----mByteDataInfo="+timerInfo.mByteDataInfo);*/
			if (timerInfo != null) {
				try {
					if (!isOrderdTimer(timerInfo)) {// 判断是否是预约过的
						mTimerShedule.addTimer(timerInfo);
						Log.i(TAG, "add success");
					}else {
						Log.i(TAG, "add fail");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 将节目信息转化为TimerInfo
	 * @param baseProgram
	 * @return
	 */
	public TimerInfo convertScheduleProgramToTimerInfo(BaseProgram baseProgram) {//private
		TimerInfo timerInfo = null;
		if (null != baseProgram) {
			/*Log.i("YangLiu", "LL convertScheduleEventToTimerInfo>>baseProgram>>" 
			+"	getProgramName = "+ baseProgram.getProgramName() 
			+"	getEventName = "+ baseProgram.getEventName() 
			+"	getWikiInfo = "+ baseProgram.getWikiInfo());*/

			ScheduleProgramSerialize sch = new ScheduleProgramSerialize(
					baseProgram.getSourceID(),// dtvScheduleEvent.getSourceID()
					baseProgram.getServiceIndex(),// dtvScheduleEvent.getProgramServiceIndex()
					baseProgram.getProgramNum(),// dtvScheduleEvent.getProgramNum()
					baseProgram.getProgramName(),//dtvScheduleEvent.getProgramName()
					baseProgram.getEventName(),//dtvScheduleEvent.getTitle()
					baseProgram.getOriginal(),//  2015-1-19
					baseProgram.getWikiInfo());
			byte[] bytes = this.serializeP(sch);
			Log.i(TAG, "LL convertScheduleEventToTimerInfo>>bytes = " + bytes);

			if (null != bytes) {
				timerInfo = new TimerInfo(
						baseProgram.getOriginal(),//  2015-1-19
						BaseProgramManager.EPG_TIMER,//BaseProgramManager.BOOT_RECOMMEND_TYPE,//3  TimerType.EPG_TIMER
						baseProgram.getStartTime().getTime(), //
						baseProgram.getEndTime().getTime(),//
						BaseProgramManager.EVENT_SCHEDULE_BROADCAST,//BaseProgramManager.EVENT_SCHEDULE_BROADCAST 
						bytes);
			}
		}
		return timerInfo;
	}
	
	private byte[] serializeP (ScheduleProgramSerialize scheduleProgramSerialize) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);

			out.writeObject(scheduleProgramSerialize);

			out.close();
			mem_out.close();

			return mem_out.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL serialize>>IOException***");
			return null;
		}
	}

	/**
	 * 将TimerInfo转化为节目信息
	 * @param timerInfos
	 * @return
	 */
	public BaseProgram convertTimerInfoToScheduleProgram(TimerInfo timerInfo) {
		BaseProgram baseProgram = null;
		if (null != timerInfo) {
			ScheduleProgramSerialize sch = this
					.deserializeP(timerInfo.mByteDataInfo);
			if (null != sch) {
				/*Log.i("YangLiu",
				"LL convertTimerInfoToScheduleEvent(TimerInfo)>>mWikiInfo = "
						+ sch.mWikiInfo);*/
				baseProgram = new BaseProgram(
						new Date(timerInfo.mlStartNotifyTime), 
						new Date(timerInfo.mlNotNotifyTime), 
						sch.mSourceID,
						sch.mOriginal,//timerInfo.mOriginal
						sch.mServiceIndex, 
						sch.mProgramNum, 
						sch.mProgramName,
						sch.mEventName, 
						sch.mWikiInfo);

				/*Log.i("YangLiu",
				"LL convertTimerInfoToScheduleProgram>>baseProgram>>"
						+ "	getSourceID = " + baseProgram.getSourceID()
						+ "	getStartTime = "
						+ baseProgram.getStartTime()
						+ "	programName = "
						+ baseProgram.getProgramName()
						+ "	getWikiInfo = " + baseProgram.getWikiInfo());*/
			}
		}
		return baseProgram;
	}
	
	//将TimerInfo列表转化为节目信息列表
	public List<BaseProgram> convertTimerInfosToSchedulePrograms(
			List<TimerInfo> timerInfos) {

		List<BaseProgram> listBasePrograms = null;
		if (timerInfos==null||timerInfos.size()==0) {
			return listBasePrograms;
		}
		
		listBasePrograms = new ArrayList<BaseProgram>();
		if (null != timerInfos) {
			for (TimerInfo tmpTimerInfo : timerInfos) {
				if (tmpTimerInfo.mOriginal==2) {//开机预约
					BaseProgram baseProgram = this.convertTimerInfoToScheduleProgram(tmpTimerInfo);
					if (null != baseProgram) {
						listBasePrograms.add(baseProgram);
					}
				}
			}
		}
		return listBasePrograms;
	}
		
	// 反序列化
	private ScheduleProgramSerialize deserializeP(byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ScheduleProgramInputStream in = new ScheduleProgramInputStream(
					mem_in);

			ScheduleProgramSerialize scheduleProgramSerialize = (ScheduleProgramSerialize) in
					.readObject();

			in.close();
			mem_in.close();

			return scheduleProgramSerialize;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL deserialize>>ClassNotFoundException***");
			return null;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL deserialize>>StreamCorruptedException***");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL deserialize>>IOException***");
			return null;
		}
	}

	// 以InnerClassUsed将时间信息转化为预约事件  private
	public static BaseProgram convertTimerInfoToScheduleProgramInnerClassUsed(
			TimerInfo timerInfo) {
		BaseProgram baseProgram = null;
		if (null != timerInfo) {
			ScheduleProgramSerialize sch = BaseProgramManager
					.deserializePInnerClassUsed(timerInfo.mByteDataInfo);
			if (null != sch) {
				/*Log.i(TAG,
				"LL convertTimerInfoToScheduleEvent(TimerInfo)>>schName = "
						+ sch.mProgramName);*/
				baseProgram = new BaseProgram(
						new Date(timerInfo.mlStartNotifyTime), 
						new Date(timerInfo.mlNotNotifyTime), 
						sch.mSourceID,
						sch.mOriginal,//  timerInfo.mOriginal
						sch.mServiceIndex, 
						sch.mProgramNum, 
						sch.mProgramName,
						sch.mEventName, 
						sch.mWikiInfo);
			}
		}
		return baseProgram;
	}
	
	// 以InnerClassUsed反序列化 利用内部类批准它拜会表面类中的所有成员和措施
	private static ScheduleProgramSerialize deserializePInnerClassUsed(
			byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ScheduleProgramInputStream in = new ScheduleProgramInputStream(
					mem_in);

			ScheduleProgramSerialize scheduleProgramSerialize = (ScheduleProgramSerialize) in
					.readObject();

			in.close();
			mem_in.close();

			return scheduleProgramSerialize;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL deserialize>>ClassNotFoundException***");
			return null;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL deserialize>>StreamCorruptedException***");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LL deserialize>>IOException***");
			return null;
		}
	}
	
	
	/**
	 * del方法，删除预约节目,有节目才删除
	 */
	public synchronized void delScheduleProgram(BaseProgram baseProgram) {
		// TODO Auto-generated method stub
		List<TimerInfo> timerInfos = null;
		TimerInfo timerInfo = null;
		try {
			timerInfos = mTimerShedule.getTimerList(TimerType.EPG_TIMER);//所有EPG预约
			if (timerInfos==null || timerInfos.size()==0) {
				return;
			}
			
			timerInfo = convertScheduleProgramToTimerInfo(baseProgram);
			
			// 开需不需要也把本地节目删除
			if (timerInfos != null && timerInfos.size()>0 && timerInfo!=null) {
				for (TimerInfo mTimerInfo : timerInfos) {
					if (mTimerInfo.mOriginal==2 && mTimerInfo.IsSame(timerInfo)) {//表示为开机预约
						mTimerShedule.deleteTimer(mTimerInfo);
					}
				}
			}
		} catch (RemoteException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 删除所有Programs
	public synchronized void delAllSchedulePrograms() {

		Log.i(TAG, "LL delAllScheduleEvents()>>clear");
		try {
			mTimerShedule.deleteAllTimer();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除所有过期的Programs
	public synchronized void delOverdueSchedulePrograms() {
		List<TimerInfo> timerInfos = null;
		try {
			timerInfos = mTimerShedule.getTimerList(TimerType.EPG_TIMER);//所有EPG预约
			if (timerInfos==null || timerInfos.size()==0) {
				return;
			}
			
			// 开需不需要也把本地节目删除
			if (timerInfos != null && timerInfos.size()>0) {
				for (TimerInfo mTimerInfo : timerInfos) {
					if (mTimerInfo.mOriginal==2) {//表示为开机预约
						Log.i(TAG, "isEquals = " + isOverdueTimer(mTimerInfo));
						if (isOverdueTimer(mTimerInfo)) {
							mTimerShedule.deleteTimer(mTimerInfo);
						}
					}
				}
			}
		} catch (RemoteException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//取得所有的预约节目
	public List<BaseProgram> getSchedulePrograms() {
		List<BaseProgram> basePrograms = null;
		try {
			List<TimerInfo> timerInfos = mTimerShedule.getTimerList(TimerType.EPG_TIMER);//所有EPG预约
			
			if (timerInfos==null || timerInfos.size()==0) {
				return basePrograms;
			}
			
//			Log.i("YangLiu", "----------timerInfos.size()-----------"+timerInfos.size());
			if (timerInfos!=null && timerInfos.size()>0) {	
				basePrograms = convertTimerInfosToSchedulePrograms(timerInfos);
			}
			return basePrograms;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return basePrograms;
	}
	
	/**
	 * 判断条件
	 */
	private static long getCurrentTime() {
		long systemTime = System.currentTimeMillis();
		return systemTime;
	}
	
	// 判断是否是已经存在的timerInfo
	public synchronized boolean isOrderdTimer(TimerInfo timerInfo) {
		List<TimerInfo> timerInfos = null;
		boolean isLocalScheduleEvent = false;
		
		try {
			timerInfos = mTimerShedule.getTimerList(TimerType.EPG_TIMER);// 取得所有EPG预约

			Log.i(TAG, "[ListLen]"+timerInfos.size());
			if (timerInfos==null || timerInfos.size()==0) {
				return false;
			}
			
			if (timerInfos != null && timerInfos.size()>0) {
				for (TimerInfo mTimerInfo : timerInfos) {
			
					/*Log.i("YangLiu", "当前时间" + timerInfo.mlStartNotifyTime
							+ "		  列表中时间" + mTimerInfo.mlStartNotifyTime);*/
					
					isLocalScheduleEvent = ((timerInfo.mlStartNotifyTime/(60 * 1000L) == mTimerInfo.mlStartNotifyTime/(60 * 1000L))) ? true: false;

					if (isLocalScheduleEvent) {// 是否时间冲突timerInfo.IsSameTime(mTimerInfo)
						return true;
					}
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// 判断是否已经存在预约节目
	public synchronized boolean isOrderdBaseProgram(BaseProgram baseProgram) {
		List<BaseProgram> basePrograms = null;
//		List<BaseProgram> basePrograms = this.convertTimerInfosToSchedulePrograms(mDtvInterface.getScheduleEvents(3));

		try {
			basePrograms = this.convertTimerInfosToSchedulePrograms(mTimerShedule.getTimerList(TimerType.EPG_TIMER));

			if (basePrograms==null || basePrograms.size()==0) {
				return false;
			}
			
			if (basePrograms!=null && basePrograms.size()>0) {
//			Log.i("YangLiu", "basePrograms.size="+basePrograms.size());
				for (BaseProgram mBaseProgram : basePrograms) {
					/*Log.i("YangLiu", "当前时间" + baseProgram.getStartTime().getTime()
							+ "		  列表中时间" + mBaseProgram.getStartTime().getTime());*/
//					Log.i("YangLiu", "isEquals = " + mBaseProgram.conflicts(baseProgram));
					if (mBaseProgram.conflicts(baseProgram)) {
						return true;
					}
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// 判断是否是过期的timerInfo
	public static synchronized boolean isOverdueTimer(TimerInfo timerInfo) {

		/** 丢掉已过期和被过滤掉的预约节目 */
		if (timerInfo.mlStartNotifyTime < getCurrentTime()
				&& (timerInfo.mlStartNotifyTime / (60 * 1000L) != getCurrentTime()/ (60 * 1000L))) {
			return true;
		}
		return false;
	}
	
	//判断是否过期的program
	public static synchronized boolean isOverdueProgram(BaseProgram baseProgram) {

		/** 丢掉已过期和被过滤掉的预约节目 */
		if (baseProgram.getStartTime().getTime()<getCurrentTime()
				&& (baseProgram.getStartTime().getTime() / (60 * 1000L) != getCurrentTime() / (60 * 1000L))) {
			return true;
		}
		return false;
	}
}

class ScheduleProgramSerialize implements Serializable {
	private static final long serialVersionUID = 1L;
	public int mSourceID;
	public int mServiceIndex;
	public int mProgramNum;
	public String mProgramName;
	public String mEventName;
	public int mOriginal;
	public String mWikiInfo;//取得的自动推荐数据
	
	public ScheduleProgramSerialize() {
	
	}

	public ScheduleProgramSerialize(int sourceID,int servicIndex, int programNum,
			String programName, String eventName ,int original ,String wikiInfo) {
		this.mSourceID = sourceID;
		this.mServiceIndex = servicIndex;
		this.mProgramNum = programNum;
		this.mProgramName = programName;
		this.mEventName = eventName;
		this.mOriginal = original;
		this.mWikiInfo = wikiInfo;
	}
}

class ScheduleProgramInputStream extends ObjectInputStream{

	public ScheduleProgramInputStream(InputStream input)
			throws StreamCorruptedException, IOException {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass osClass)
			throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return ScheduleProgramSerialize.class;
	}

	@Override
	protected Object resolveObject(Object object) throws IOException {
		// TODO Auto-generated method stub
		return ScheduleProgramSerialize.class;
	}
}