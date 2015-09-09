package com.changhong.tvos.dtv.tvap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.pushoutView.PushoutService;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.menuManager.MenuDisplayManager;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.provider.BaseProgramManager;
import com.changhong.tvos.dtv.scan.ScanManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstActivityStatus;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.util.ViewActionInfo;
import com.changhong.tvos.dtv.util.ViewActionInfo.OnPlayScheduleListener;
import com.changhong.tvos.dtv.vo.TimerInfo;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.dtv.vo.TimerInfo.TimerType;
import com.changhong.tvos.model.EnumInputSource;
import com.changhong.tvos.model.ChOsType.EnumPipInputSource;
import com.changhong.tvos.system.commondialog.CommonHintToast;

/**
 * 预约事件管理器
 * @author YangLiu
 */
public class DtvScheduleManager {
	enum SpecCodeForSourceID {
		DVBC, DTMB
	}

	private static final String TAG = "DtvScheduleManager";
	private DtvInterface mDtvInterface = null;
	private DtvCommonManager mCommonManager = null;
	private IScheduleReceive mOnScheduleReceive = null;
	private static CommonHintToast hintToast = null;
	private static DtvScheduleManager mDtvScheduleManager = null;
	public static final int EPG_TIMER_TYPE = TimerType.EPG_TIMER;
	public static final String EVENT_SCHEDULE_BROADCAST = "com.changhong.tvos.dtv.tvap.DtvScheduleManager.EVENT_SCHEDUL_BROADCAST";
	public static final String SCHEDULE_FOR_MOBILE_BROADCAST = "com.changhong.tvos.dtv.tvp.DtvScheduleManager.MobileTerminalScheduleEvent";
	public static final String SCHEDULE_MutuallyExclusiveSHOW_BROADCAST = "com.changhong.system.suspend_app.show";
	public static final String SCHEDULE_MutuallyExclusiveSHOW_FromWhichAPP = "com.changhong.tvos.dtv";
	public static final String SCHEDULE_PVR_START_BROADCAST = "com.changhong.pvr.start";
	public static final String SCHEDULE_PVR_PAUSE_BROADCAST = "com.changhong.pvr.pause";
	public static final String SCHEDULE_PVR_STOP_BROADCAST = "com.changhong.pvr.stop";
	public static final String SCHEDULE_PVR_ENCRYPT_BROADCAST = "com.changhong.pvr.encryptprogram";
	public static final String SCHEDULE_PVR_NETDIED_BROADCAST = "netwrok.dcc.cs.creat";

	private DtvScheduleManager() {
		mDtvInterface = DtvInterface.getInstance();
		mCommonManager = DtvCommonManager.getInstance();
		// mDtvChannelManager = DtvChannelManager.getInstance();
	}

	// getInstance()方法
	public synchronized static DtvScheduleManager getInstance() {
		if (mDtvScheduleManager == null) {
			mDtvScheduleManager = new DtvScheduleManager();
		}
		return mDtvScheduleManager;
	}

	// ScheduleReceive接口
	public interface IScheduleReceive {
		public void onScheduleTimeUp(DtvScheduleEvent dtvSchelEvent);
	}

	// 设置ScheduleListener
	public void setOnScheduleListener(IScheduleReceive sch) {
		mOnScheduleReceive = sch;
	}

	// 将预约事件转化为时间信息
	private TimerInfo convertScheduleEventToTimerInfo(DtvScheduleEvent dtvScheduleEvent) {
		TimerInfo timerInfo = null;
		if (null != dtvScheduleEvent) {
			Log.i(TAG, "LL convertScheduleEventToTimerInfo>>programServiceIndex = " + dtvScheduleEvent.getProgramServiceIndex() + ",programNum = " + dtvScheduleEvent.getProgramNum()
					+ ",programName = " + dtvScheduleEvent.getProgramName() + ",eventTitle = " + dtvScheduleEvent.getTitle());
			ScheduleSerialize sch = new ScheduleSerialize(dtvScheduleEvent.getSourceID(), dtvScheduleEvent.getProgramServiceIndex(), dtvScheduleEvent.getProgramNum(),
					dtvScheduleEvent.getProgramName(), dtvScheduleEvent.getTitle());
			byte[] bytes = this.serialize(sch);
			Log.i(TAG, "LL convertScheduleEventToTimerInfo>>bytes = " + bytes);
			if (null != bytes) {
				timerInfo = new TimerInfo(DtvScheduleManager.EPG_TIMER_TYPE, dtvScheduleEvent.getStartTime().getTime(), dtvScheduleEvent.getEndTime().getTime(),
						DtvScheduleManager.EVENT_SCHEDULE_BROADCAST, bytes);
			}
		}
		return timerInfo;
	}

	// 将单个时间信息转化为单个预约事件
	private DtvScheduleEvent convertTimerInfoToScheduleEvent(TimerInfo timerInfo) {
		DtvScheduleEvent dtvScheduleEvent = null;
		if (null != timerInfo) {
			ScheduleSerialize sch = this.deserialize(timerInfo.mByteDataInfo);
			if (null != sch) {
				Log.i(TAG, "LL convertTimerInfoToScheduleEvent(TimerInfo)>>schName = " + sch.mProgramName);
				dtvScheduleEvent = new DtvScheduleEvent(new Date(timerInfo.mlStartNotifyTime), new Date(timerInfo.mlNotNotifyTime), sch.mSourceID, sch.mServiceIndex, sch.mProgramNum,
						sch.mProgramName, sch.mEventName);
				Log.i(TAG, "LL convertTimerInfoToScheduleEvent>>programServiceIndex = " + dtvScheduleEvent.getProgramServiceIndex() + ",programNum = " + dtvScheduleEvent.getProgramNum()
						+ ",programName = " + dtvScheduleEvent.getProgramName() + ",eventTitle = " + dtvScheduleEvent.getTitle());
			}
		}
		return dtvScheduleEvent;
	}

	// 将时间信息列表转化为预约事件列表
	private List<DtvScheduleEvent> convertTimerInfoToScheduleEvent(List<TimerInfo> timerInfo) {
		List<DtvScheduleEvent> listDtvScheduleEvents = new ArrayList<DtvScheduleEvent>();
		if (null != timerInfo) {
			for (TimerInfo tmpTimerInfo : timerInfo) {
				DtvScheduleEvent sch = this.convertTimerInfoToScheduleEvent(tmpTimerInfo);
				if (null != sch) {
					Log.i(TAG, "LL convertTimerInfoToScheduleEvent(List<TimerInfo>)>>schName = " + sch.getTitle());
					listDtvScheduleEvents.add(sch);
				}
			}
		}
		return listDtvScheduleEvents;
	}

	// 获取当前时间
	public Date getCurrentDate() {
		return mCommonManager.getCurrentDate();
	}

	/**
	 * 判断是否为预约事件
	 * @param event
	 * @param program
	 * @return
	 */
	public synchronized boolean isOrderEvent(DtvEvent event, DtvProgram program) {
		List<DtvScheduleEvent> scheduleEventList = this.convertTimerInfoToScheduleEvent(mDtvInterface.getScheduleEvents(DtvScheduleManager.EPG_TIMER_TYPE));

		DtvScheduleEvent ScheduleEvent = new DtvScheduleEvent(event, program);

		for (DtvScheduleEvent mEvent : scheduleEventList) {
			Log.i(TAG, "isEquals = " + mEvent.equals(ScheduleEvent));
			if (mEvent.equals(ScheduleEvent)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为本地EPG预约事件  2015-3-23 YangLiu
	 * @param event
	 * @param program
	 * @return
	 */
	public synchronized boolean isOrderLocalEvent(DtvEvent event, DtvProgram program) {//0:本地EPG预约  1:主场景预约		2:欢网预约
		List<DtvScheduleEvent> localScheduleEventList = this.convertTimerInfoToScheduleEvent(mDtvInterface.getEPGScheduleEventsByOriginal(0));

		DtvScheduleEvent ScheduleEvent = new DtvScheduleEvent(event, program);

		for (DtvScheduleEvent mEvent : localScheduleEventList) {
			//	Log.i(TAG, "isEquals = " + mEvent.equals(ScheduleEvent));
			if (mEvent.equals(ScheduleEvent)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * judge the scheduleEvent is Overdue or not
	 * 判断事件是否为过期预约节目 2014-12-30 YangLiu
	 * @param scheduleEvent
	 * @return
	 */
	public static synchronized boolean isOverdueEvent(DtvScheduleEvent scheduleEvent) {

		/**丢掉已过期和被过滤掉的预约节目*/
		Date curDate = MenuManager.getInstance().getCurrentDate();
		if (scheduleEvent.getStartTime().before(curDate) && (scheduleEvent.getStartTime().getTime() / (60 * 1000L) != curDate.getTime() / (60 * 1000L))) {
			return true;
		}
		return false;
	}

	/**
	 * 判断到点的预约节目是否是过滤节目，被过滤掉的节目不再提醒
	 * @param scheduleEvent 到点的预约节目
	 * @return
	 */
	public static boolean isFilteredScheduleEvent(DtvScheduleEvent scheduleEvent) {

		/*change By YangLiu 2014-12-15*/
		List<DtvProgram> mChannelList = MenuManager.getInstance().getWatchedChannelList();
		for (int i = 0; i < mChannelList.size(); i++) {
			if (scheduleEvent.getProgramServiceIndex() == mChannelList.get(i).getProgramServiceIndex()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断到点的预约节目是否是过滤节目，被过滤掉的节目不再提醒
	 * @param scheduleEvent 到点的预约节目
	 * @return
	 */
	public static boolean isFilteredScheduleEvent(BaseProgram baseProgram) {

		/*change By YangLiu 2014-12-15*/
		List<DtvProgram> mChannelList = MenuManager.getInstance().getWatchedChannelList();
		for (int i = 0; i < mChannelList.size(); i++) {
			if (baseProgram.getServiceIndex() == mChannelList.get(i).getProgramServiceIndex()) {
				return false;
			}
		}
		return true;
	}

	// 取得预约冲突事件
	public synchronized DtvScheduleEvent getConflictEvent(DtvEvent event, DtvProgram program) {
		List<DtvScheduleEvent> scheduleEventList = this.convertTimerInfoToScheduleEvent(mDtvInterface.getScheduleEvents(DtvScheduleManager.EPG_TIMER_TYPE));
		DtvScheduleEvent scheduleEvent = new DtvScheduleEvent(event, program);
		for (DtvScheduleEvent mEvent : scheduleEventList) {
			Log.i(TAG, "isConflict = " + mEvent.conflicts(scheduleEvent));
			if (mEvent.conflicts(scheduleEvent)) {
				return mEvent;
			}
		}
		return null;
	}

	/**
	 * use newScheduleEvent to replace oldScheduleEvent
	 * 将当前准备预约的节目替换掉之前预约的冲突节目   2014-12-30-YangLiu
	 * @param oldScheduleEvent 冲突的预约节目
	 * @param newScheduleEvent 当前准备预约的节目
	 */
	public synchronized void replaceConflictEvent(DtvScheduleEvent oldScheduleEvent, DtvScheduleEvent newScheduleEvent) {
		if (oldScheduleEvent != null && newScheduleEvent != null) {
			delScheduleEvent(oldScheduleEvent);
			addScheduleEvent(newScheduleEvent);
		}
	}

	// 替换掉相同的开机推荐预约2015-1-21
	public synchronized boolean replaceOrderdBootTimer(DtvEvent event, DtvProgram program) {

		DtvScheduleEvent scheduleEvent = new DtvScheduleEvent(event, program);
		TimerInfo timerInfo = this.convertScheduleEventToTimerInfo(scheduleEvent);

		List<TimerInfo> timerInfos = null;
		boolean isBootScheduleProgram = false;

		timerInfos = mDtvInterface.getScheduleEvents(TimerType.EPG_TIMER);//取得所有的EPG预约

		if (timerInfos == null || timerInfos.size() == 0) {
			return false;
		}

		if (timerInfos != null && timerInfos.size() > 0) {
			for (TimerInfo mTimerInfo : timerInfos) {
				if (mTimerInfo.mOriginal == 2) {//开机预约
					isBootScheduleProgram = ((timerInfo.mlStartNotifyTime / (60 * 1000L) == mTimerInfo.mlStartNotifyTime / (60 * 1000L))) ? true : false;

					// 用本地的预约替换掉开机推荐的预约
					if (isBootScheduleProgram) {// 是否时间冲突timerInfo.IsSameTime(mTimerInfo)
						Log.i("YangLiu", "替换掉同时间的开机预约");
						mDtvInterface.delScheduleEvent(mTimerInfo);
						mDtvInterface.addScheduleEvent(timerInfo);
						return true;
					}
				}
			}
		}
		return false;
	}

	// 取得所有的预约事件列表
	public List<DtvScheduleEvent> getScheduleEvents() {
		List<DtvScheduleEvent> scheduleEventList = this.convertTimerInfoToScheduleEvent(mDtvInterface.getScheduleEvents(DtvScheduleManager.EPG_TIMER_TYPE));
		return scheduleEventList;
	}

	/**
	 * get filtered scheduleEvents those are ordered when the filterFlag is on
	 * 取得在"全部节目"打开时预约的，而当"全部节目"关闭后被过滤掉的预约列表         2014-12-30-YangLiu
	 * @param curChannelList 当前的频道列表
	 * @return scheduleEventList 筛选后的预约列表
	 */
	public List<DtvScheduleEvent> getChangedScheduleEvents(List<DtvProgram> curChannelList) {
		List<DtvScheduleEvent> scheduleEventList = this.convertTimerInfoToScheduleEvent(mDtvInterface.getScheduleEvents(DtvScheduleManager.EPG_TIMER_TYPE));

		// 删除过滤预约节目
		if (!DtvChannelManager.getInstance().GetAllchFlag()) {
			if (scheduleEventList != null && scheduleEventList.size() > 0) {
				for (int i = 0; i < scheduleEventList.size(); i++) {
					int j;
					for (j = 0; j < curChannelList.size(); j++) {
						if (scheduleEventList.get(i).getProgramServiceIndex() == curChannelList.get(j).getProgramServiceIndex()) {
							break;
						}
					}
					if (j == curChannelList.size()) {
						scheduleEventList.remove(i);
						i = -1;
					}
				}
			}
		}
		// 删除过期预约节目
		for (int i = 0; i < scheduleEventList.size(); i++) {
			if (isOverdueEvent(scheduleEventList.get(i))) {
				scheduleEventList.remove(i);
				mDtvScheduleManager.delScheduleEvent(scheduleEventList.get(i));//在预约列表中删除
				i = i - 1;
			}
		}
		return scheduleEventList;
	}

	// 增加指定节目的预约事件
	public synchronized void addScheduleEvent(DtvEvent event, DtvProgram program) {
		DtvScheduleEvent scheduleEvent = new DtvScheduleEvent(event, program);

		Log.i(TAG, "LL addScheduleEvent()>>schName = " + scheduleEvent.getTitle() + "sch_serIndex= " + scheduleEvent.getProgramServiceIndex() + "sch_proNum= " + scheduleEvent.getProgramNum());
		mDtvInterface.addScheduleEvent(this.convertScheduleEventToTimerInfo(scheduleEvent));
	}

	// 增加指定预约节目的预约事件  add by YangLiu 2014-12-30
	public synchronized void addScheduleEvent(DtvScheduleEvent event) {
		if (event != null) {
			Log.i(TAG, "LL addScheduleEvent()>>schName = " + event.getTitle() + "sch_serIndex= " + event.getProgramServiceIndex() + "sch_proNum= " + event.getProgramNum());
			mDtvInterface.addScheduleEvent(this.convertScheduleEventToTimerInfo(event));
		}
	}

	// 删除指定节目的预约事件
	@SuppressLint("SimpleDateFormat")
	public synchronized void delScheduleEvent(DtvEvent event, DtvProgram program) {
		DtvScheduleEvent scheduleEvent = new DtvScheduleEvent(event, program);

		SimpleDateFormat dateFormat;
		dateFormat = new SimpleDateFormat("HH:mm");
		Log.i(TAG,
				"LL addScheduleEvent()>>schName = " + scheduleEvent.getTitle() + "sch_serIndex= " + scheduleEvent.getProgramServiceIndex() + "sch_start= "
						+ dateFormat.format(scheduleEvent.getStartTime()));
		mDtvInterface.delScheduleEvent(this.convertScheduleEventToTimerInfo(scheduleEvent));
	}

	// 删除指定预约事件的预约事件
	@SuppressLint("SimpleDateFormat")
	public synchronized void delScheduleEvent(DtvScheduleEvent event) {
		if (null != event) {

			SimpleDateFormat dateFormat;
			dateFormat = new SimpleDateFormat("HH:mm");
			Log.i(TAG, "LL addScheduleEvent()>>schName = " + event.getTitle() + "sch_serIndex = " + event.getProgramServiceIndex() + "sch_start = " + dateFormat.format(event.getStartTime()));
			mDtvInterface.delScheduleEvent(this.convertScheduleEventToTimerInfo(event));
		}
	}

	// 删除所有预约事件
	public synchronized void delAllScheduleEvents() {

		Log.i(TAG, "LL delAllScheduleEvents()>>clear");
		mDtvInterface.delAllScheduleEvents();
	}

	// 对预约序列化
	private byte[] serialize(ScheduleSerialize shcScheduleSerialize) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);

			out.writeObject(shcScheduleSerialize);

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

	// 反序列化
	private ScheduleSerialize deserialize(byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ScheduleInputStream in = new ScheduleInputStream(mem_in);

			ScheduleSerialize scheduleSerialize = (ScheduleSerialize) in.readObject();

			in.close();
			mem_in.close();

			return scheduleSerialize;
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

	// 以InnerClassUsed反序列化
	private static ScheduleSerialize deserializeInnerClassUsed(byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ScheduleInputStream in = new ScheduleInputStream(mem_in);

			ScheduleSerialize scheduleSerialize = (ScheduleSerialize) in.readObject();

			in.close();
			mem_in.close();

			return scheduleSerialize;
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

	// 以InnerClassUsed将时间信息转化为预约事件
	private static DtvScheduleEvent convertTimerInfoToScheduleEventInnerClassUsed(TimerInfo timerInfo) {
		DtvScheduleEvent dtvScheduleEvent = null;
		if (null != timerInfo) {
			ScheduleSerialize sch = DtvScheduleManager.deserializeInnerClassUsed(timerInfo.mByteDataInfo);
			if (null != sch) {
				Log.i(TAG, "LL convertTimerInfoToScheduleEventInnerClassUsed(TimerInfo)>>schName = " + sch.mProgramName);
				dtvScheduleEvent = new DtvScheduleEvent(new Date(timerInfo.mlStartNotifyTime), new Date(timerInfo.mlNotNotifyTime), sch.mSourceID, sch.mServiceIndex, sch.mProgramNum,
						sch.mProgramName, sch.mEventName);
			}
		}
		return dtvScheduleEvent;
	}

	// 将信号源转化为specCode
	public static int convertSourceID2SpecCode(int sourceID) {
		int specCode = 0;
		switch (sourceID) {
		case ConstSourceID.DVBC:
			specCode = SpecCodeForSourceID.DVBC.ordinal();
			break;
		case ConstSourceID.DTMB:
			specCode = SpecCodeForSourceID.DTMB.ordinal();
			break;
		default:
			break;
		}
		Log.i(TAG, "LL convertSourceID2SpecCode()>>specCode = " + specCode);
		return specCode;
	}

	// 将specCode转化为信号源
	public static int convertSpecCode2SourceID(int specCode) {
		int sourceID = ConstSourceID.DVBC;
		SpecCodeForSourceID[] arrayCodes = SpecCodeForSourceID.values();
		SpecCodeForSourceID code = SpecCodeForSourceID.DVBC;
		if (specCode >= 0 && specCode < arrayCodes.length) {
			code = arrayCodes[specCode];
		}
		switch (code) {
		case DVBC:
			sourceID = ConstSourceID.DVBC;
			break;
		case DTMB:
			sourceID = ConstSourceID.DTMB;
			break;
		default:
			break;
		}
		Log.i(TAG, "LL convertSpecCode2SourceID()>>sourceID = " + sourceID);
		return sourceID;

	}

	// 预约事件到点提醒接收器
	public static class ScheduleEventReceiver extends BroadcastReceiver {
		private static final String TAG = "ScheduleEventReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "LL [enter]ScheduleEventReceiver>>onReceive***");

			/*****************************Q2********************************/
			if (DtvRoot.is5508Q2) {
				Log.i(TAG, "YL_Q2	ScheduleEventReceiver");
				final Context tempContext = context;
				String action = intent.getAction();

				/**
				 * 一、实现主场景互斥显示，有其他应用显示时关闭弹出框
				 */
				if (DtvScheduleManager.SCHEDULE_MutuallyExclusiveSHOW_BROADCAST.equals(action)) {
					Log.i(TAG, "互斥来自哪里=" + intent.getStringExtra("from_which_app"));
					if (!DtvScheduleManager.SCHEDULE_MutuallyExclusiveSHOW_FromWhichAPP.equals(intent.getStringExtra("from_which_app"))) {
						Intent pushoutservice = new Intent(context, PushoutService.class);
						TimerInfo timerInfo = null;
						Bundle bundle = new Bundle();
						bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, timerInfo);//add by YangLiu
						pushoutservice.putExtras(bundle);
						pushoutservice.putExtra("control", "hide");
						context.startService(pushoutservice);
					}
				}

				/**
				 * 二、录播弹窗提示信息
				 */
				// 开始录播提示框
				if (DtvScheduleManager.SCHEDULE_PVR_START_BROADCAST.equals(action)) {
					Log.i(TAG, "----SCHEDULE_PVR_START_BROADCAST----");
					int channelID = intent.getIntExtra("CHANNELID", -1);
					String url = intent.getStringExtra("URL");
					String channelName = intent.getStringExtra("CHANNELNAME");
					Long startTime = intent.getLongExtra("START", 0L);
					Long endTime = intent.getLongExtra("END", 0L);

					if (channelName != null) {
						String startPvrHit = context.getResources().getString(R.string.pvr_Started_Pro) + intent.getStringExtra("CHANNELNAME");
						showHintToast(tempContext, startPvrHit);
					} else {
						String startPvrHit = context.getResources().getString(R.string.pvr_Started_CurSchPro);
						showHintToast(tempContext, startPvrHit);
					}
				}

				// 录播结束提示框 
				if (DtvScheduleManager.SCHEDULE_PVR_STOP_BROADCAST.equals(action)) {
					Log.i(TAG, "----SCHEDULE_PVR_STOP_BROADCAST----");
					int channelID = intent.getIntExtra("CHANNELID", -1);
					String url = intent.getStringExtra("URL");
					String channelName = intent.getStringExtra("CHANNELNAME");
					Long startTime = intent.getLongExtra("START", 0L);
					Long endTime = intent.getLongExtra("END", 0L);

					if (channelName != null) {
						String endPvrHit = intent.getStringExtra("CHANNELNAME") + context.getResources().getString(R.string.pvr_Done_Pro);
						showHintToast(tempContext, endPvrHit);
					} else {
						String endPvrHit = context.getResources().getString(R.string.pvr_Done_curSchPro);
						showHintToast(tempContext, endPvrHit);
					}
				}

				// 暂停录播提示框    没添加清单文件 2015-2-13
				if (DtvScheduleManager.SCHEDULE_PVR_PAUSE_BROADCAST.equals(action)) {
				}

				// 加密节目，无法录播提示框
				if (DtvScheduleManager.SCHEDULE_PVR_ENCRYPT_BROADCAST.equals(action)) {
					Log.i(TAG, "----SCHEDULE_PVR_ENCRYPT_BROADCAST----");
					int channelID = intent.getIntExtra("CHANNELID", -1);
					String url = intent.getStringExtra("URL");
					String channelName = intent.getStringExtra("CHANNELNAME");
					Long startTime = intent.getLongExtra("START", 0L);
					Long endTime = intent.getLongExtra("END", 0L);

					if (channelName != null) {
						String filedEncryptPvrHit = intent.getStringExtra("CHANNELNAME") + context.getResources().getString(R.string.pvr_Filed_EncryptPro);
						showHintToast(tempContext, filedEncryptPvrHit);
					} else {
						String filedEncryptPvrHit = context.getResources().getString(R.string.pvr_Filed_CurEncryptPro);
						showHintToast(tempContext, filedEncryptPvrHit);
					}
				}

				/**
				 * 三、预约节目到点弹窗
				 */
				if (DtvScheduleManager.EVENT_SCHEDULE_BROADCAST.equals(action)) {
					Log.i("YangLiu", "timer up");
					Bundle mBundle = intent.getExtras();
					TimerInfo timerInfo = (TimerInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);

					// 如果本地节目为空，则不显示  2015-4-16
					if (DtvChannelManager.getInstance().getChannelList() == null || DtvChannelManager.getInstance().getChannelList().size() == 0) {
						Log.i(TAG, "return schedule event's show, because the local channelList is " + DtvChannelManager.getInstance().getChannelList());
						if (DtvChannelManager.getInstance().getChannelList() != null) {
							Log.i(TAG, "return schedule event's show, because the local channelList size is " + DtvChannelManager.getInstance().getChannelList().size());
						}
						return;
					}

					// 正在搜索不显示
					if (ScanManager.isSearching()) {//2015-4-28 YangLiu
						Log.i(TAG, "start show but returned,	because of the channls isSearching");
						return;
					}

					if (MainMenuRootData.isFilterMenuShow) {//2015-4-28 YangLiu
						Log.i(TAG, "start show but returned,	because of the channls filter is Showing");
						return;
					}

					// 正在过滤不显示    2015-4-16
					if (FilterChannels.getInstance(context).isFilter()) {//2015-4-28 YangLiu
						Log.i(TAG, "start show but returned,	because of the channls isFiltering");
						return;
					}

					// EPG本地预约
					if (timerInfo.mOriginal == 0) {
						final DtvScheduleEvent schedule = DtvScheduleManager.convertTimerInfoToScheduleEventInnerClassUsed(timerInfo);
						if (null == schedule) {
							return;
						}

						Log.i("YangLiu",
								"\n\n\n接收到的本地EPG预约信息为：" + "getStartTime=" + schedule.getStartTime() + "getEndTime=" + schedule.getEndTime() + "getProgramServiceIndex="
										+ schedule.getProgramServiceIndex() + "getProgramNum=" + schedule.getProgramNum() + "getProgramName=" + schedule.getProgramName() + "getTitle="
										+ schedule.getTitle());

						// 修改被过滤和过期的预约节目仍然提醒的问题  add by YangLiu 2014-11-28
						if (isFilteredScheduleEvent(schedule) || isOverdueEvent(schedule)) {
							Log.i("YangLiu", "isFilteredScheduleEvent(schedule) or isOverdueEvent(schedule) is true");
							return;
						}

						if (((ConstActivityStatus.ACTIVITY_ONPUASE_STATUS == DtvRoot.mDtvRootCurStatus || ConstActivityStatus.ACTIVITY_ONRESUME_STATUS == DtvRoot.mDtvRootCurStatus)
								&& schedule.getProgramServiceIndex() == DtvChannelManager.getInstance().getCurPorgramServiceIndex() && schedule.getSourceID() == DtvSourceManager.getInstance()
								.getCurSourceID()) || (false == DtvRoot.isStartControlEnd)) {
							Log.e(TAG, "LL ScheduleEventReceiver>>DtvRoot.status = " + DtvRoot.mDtvRootCurStatus + ",isStartControlEnd = " + DtvRoot.isStartControlEnd);
							return;
						}

						final WindowManager wManager = (WindowManager) tempContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
						WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
						wmParams.x = 0;
						wmParams.y = 0;
						wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
						wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
						wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
						wmParams.windowAnimations = 0;
						wmParams.format = PixelFormat.TRANSPARENT | WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW;
						wmParams.gravity = Gravity.CENTER;
						wmParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

						final ViewActionInfo viewActionInfo = new ViewActionInfo(tempContext);
						viewActionInfo.setDisplayInfo(schedule.getProgramName(), schedule.getTitle(), tempContext.getString(R.string.stringRequest), tempContext.getString(R.string.yes),
								tempContext.getString(R.string.no));

						viewActionInfo.setOnPlayScheduleListener(new OnPlayScheduleListener() {

							@Override
							public void onPlaySchedule() {
								// TODO Auto-generated method stub
								if ((ConstActivityStatus.ACTIVITY_ONPUASE_STATUS == DtvRoot.mDtvRootCurStatus || ConstActivityStatus.ACTIVITY_ONRESUME_STATUS == DtvRoot.mDtvRootCurStatus)
										&& (true == DtvRoot.isStartControlEnd)) {
									IScheduleReceive tmpSch = DtvScheduleManager.getInstance().mOnScheduleReceive;
									if (null != tmpSch) {
										tmpSch.onScheduleTimeUp(schedule);
										MenuDisplayManager.getInstance(tempContext).dismissAllRegistered();
									}
								} else {
									Intent intent = new Intent("com.changhong.dmt.itv.tvpartner.closeimeui");
									tempContext.sendBroadcast(intent);
									// the highest position for distinguishing channelNum and channelIndex,  the second highest for distinguishing carrying sourceID or not
									int indexCarryInfo = schedule.getProgramServiceIndex() | 0xc0000000 | (convertSourceID2SpecCode(schedule.getSourceID()) << 28 & 0x30000000);
									Log.i(TAG, String.format("LL onPlaySchedule()>>indexCarryInfo1 = 0x%x", indexCarryInfo));
									indexCarryInfo = indexCarryInfo | 0x40000000;
									Log.i(TAG, String.format("LL onPlaySchedule()>>indexCarryInfo2 = 0x%x", indexCarryInfo));
									DtvCommonManager.getInstance().startInputSource(indexCarryInfo);
								}
								viewActionInfo.setVisibility(View.GONE);
							}
						});
						wManager.addView(viewActionInfo, wmParams);

						// 如果在DTV源下的DTV场景下就发给移动端
						EnumInputSource mEnumInputSource = null;
						try {
							mEnumInputSource = TVManager.getInstance(tempContext).getSourceManager().getCurInputSource(EnumPipInputSource.E_MAIN_INPUT_SOURCE);
							if (EnumInputSource.E_INPUT_SOURCE_DTV.equals(mEnumInputSource)) {

								Log.i("YangLiu", "给移动端发送本地预约广播");
								Intent mIntent = new Intent(DtvScheduleManager.SCHEDULE_FOR_MOBILE_BROADCAST);
								mIntent.putExtra("StartTime", schedule.getStartTime().getTime());//节目开始时间 L
								mIntent.putExtra("EndTime", schedule.getEndTime().getTime());//节目结束时间 L
								mIntent.putExtra("SourceID", schedule.getSourceID());//dvbc与dtmb
								mIntent.putExtra("Original", 0);//预约来源
								mIntent.putExtra("ServiceIndex", schedule.getProgramServiceIndex());//服务号，本地根据这个换台
								mIntent.putExtra("ProgramNum", schedule.getProgramNum());//频道号
								mIntent.putExtra("ProgramName", schedule.getProgramName());//频道名称
								mIntent.putExtra("EventName", schedule.getTitle());//节目名称
								mIntent.putExtra("WikiInfo", "");//节目海报
								tempContext.sendBroadcast(mIntent);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.d(TAG, "SourceNotFoundException=" + e.getMessage().toString());
						}

						// 主场景和开机预约
					} else if (timerInfo.mOriginal == 1 || timerInfo.mOriginal == 2) {

						// 解决从C切到T下弹框问题，或者节目列表更新后弹框不符问题  2015-3-30
						BaseProgram baseProgram = BaseProgramManager.convertTimerInfoToScheduleProgramInnerClassUsed(timerInfo);
						if (isFilteredScheduleEvent(baseProgram)) {
							Log.i("YangLiu", "return, because schedule event isFilteredScheduleEvent");
							return;
						}

						if (timerInfo.mOriginal == 1) {//主场景预约     
							Log.i("YangLiu", "\n\n\n主场景预约到点提示信息为：" + "	mlStartNotifyTime=" + new Date(timerInfo.mlStartNotifyTime) + "	mlNotNotifyTime=" + new Date(timerInfo.mlNotNotifyTime)
									+ "	miIndex=" + timerInfo.miIndex + "	miType=" + timerInfo.miType + "	mOriginal=" + timerInfo.mOriginal + "	mByteDataInfo=" + timerInfo.mByteDataInfo);
						}
						if (timerInfo.mOriginal == 2) {//开机预约
							Log.i("YangLiu", "\n\n\n开机预约到点提示信息为：" + "	mlStartNotifyTime=" + new Date(timerInfo.mlStartNotifyTime) + "	mlNotNotifyTime=" + new Date(timerInfo.mlNotNotifyTime)
									+ "	miIndex=" + timerInfo.miIndex + "	miType=" + timerInfo.miType + "	mOriginal=" + timerInfo.mOriginal + "	mByteDataInfo=" + timerInfo.mByteDataInfo);
						}

						// 接收到主场景预约，显示谭军信息
						// 节目正在搜索或者过滤或者别名库为空，都不显示  2015-4-16
						Log.i("YangLiu", "ScanManager.isSearching = " + ScanManager.isSearching());
						Log.i("YangLiu", "FilterChannels.isFilter = " + FilterChannels.getInstance(context).isFilter());
						if (ScanManager.isSearching() || FilterChannels.getInstance(context).isFilter()) {
							Intent mIntent = new Intent(context, PushoutService.class);
							mIntent.putExtra("control", "stopall");
							context.startService(mIntent);
							Log.i("YangLiu", "isSearching or isFiltering and send stopall intent, return");
							return;
						}

						Intent pushoutservice = new Intent(context, PushoutService.class);
						Bundle bundle = new Bundle();
						bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, timerInfo);//add by YangLiu
						pushoutservice.putExtras(bundle);
						pushoutservice.putExtra("control", "show");
						context.startService(pushoutservice);

						/*
						 * 如果在DTV源下就发给移动端：
						 */
						/*EnumInputSource mEnumInputSource = null;
						try {
							mEnumInputSource = TVManager.getInstance(tempContext).getSourceManager().getCurInputSource(EnumPipInputSource.E_MAIN_INPUT_SOURCE);
							if (EnumInputSource.E_INPUT_SOURCE_DTV.equals(mEnumInputSource)) {*/

						Log.i("YangLiu", "给移动端发送网络预约广播");
						Intent mIntent = new Intent(DtvScheduleManager.SCHEDULE_FOR_MOBILE_BROADCAST);
						/*Log.i("YangLiu", "baseProgram="
								+"	getStartTime="+baseProgram.getStartTime()
								+"	getEndTime="+baseProgram.getEndTime()
								+"	getServiceIndex="+baseProgram.getServiceIndex()
								+"	getSourceID="+baseProgram.getSourceID()
								+"	getOriginal="+baseProgram.getOriginal()
								+"	getProgramName="+baseProgram.getProgramName()
								+"	getProgramNum="+baseProgram.getProgramNum()
								+"	getEventName="+baseProgram.getEventName()
								+"	getWikiInfo="+baseProgram.getWikiInfo());*/
						mIntent.putExtra("StartTime", baseProgram.getStartTime().getTime());//节目开始时间 L
						mIntent.putExtra("EndTime", baseProgram.getEndTime().getTime());//节目结束时间 L
						mIntent.putExtra("SourceID", baseProgram.getSourceID());//dvbc与dtmb
						mIntent.putExtra("Original", baseProgram.getOriginal());//预约来源
						mIntent.putExtra("ServiceIndex", baseProgram.getServiceIndex());//服务号，本地根据这个换台
						mIntent.putExtra("ProgramNum", baseProgram.getProgramNum());//频道号
						mIntent.putExtra("ProgramName", baseProgram.getProgramName());//频道名称
						mIntent.putExtra("EventName", baseProgram.getEventName());//节目名称
						mIntent.putExtra("WikiInfo", baseProgram.getWikiInfo());//节目海报		
						tempContext.sendBroadcast(mIntent);
					}
				}
				/*****************************Q1********************************/
			} else {
				Log.i(TAG, "YL_Q1	ScheduleEventReceiver");
				final Context tempContext = context;
				Bundle mBundle = intent.getExtras();
				TimerInfo timerInfo = (TimerInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				final DtvScheduleEvent schedule = DtvScheduleManager.convertTimerInfoToScheduleEventInnerClassUsed(timerInfo);
				if (null == schedule) {
					return;
				}

				// 如果本地节目为空，则不显示  2015-4-16
				if (DtvChannelManager.getInstance().getChannelList() == null || DtvChannelManager.getInstance().getChannelList().size() == 0) {
					Log.i(TAG, "return schedule event's show, because the local channelList is " + DtvChannelManager.getInstance().getChannelList());
					if (DtvChannelManager.getInstance().getChannelList() != null) {
						Log.i(TAG, "return schedule event's show, because the local channelList size is " + DtvChannelManager.getInstance().getChannelList().size());
					}
					return;
				}

				// 修改被过滤和过期的预约节目仍然提醒的问题  add by YangLiu 2014-11-28
				if (isFilteredScheduleEvent(schedule) || isOverdueEvent(schedule)) {
					Log.i("YangLiu", "isFilteredScheduleEvent(schedule) or isOverdueEvent(schedule) is true");
					return;
				}

				if (((ConstActivityStatus.ACTIVITY_ONPUASE_STATUS == DtvRoot.mDtvRootCurStatus || ConstActivityStatus.ACTIVITY_ONRESUME_STATUS == DtvRoot.mDtvRootCurStatus)
						&& schedule.getProgramServiceIndex() == DtvChannelManager.getInstance().getCurPorgramServiceIndex() && schedule.getSourceID() == DtvSourceManager.getInstance()
						.getCurSourceID()) || (false == DtvRoot.isStartControlEnd)) {
					Log.e(TAG, "LL ScheduleEventReceiver>>DtvRoot.status = " + DtvRoot.mDtvRootCurStatus + ",isStartControlEnd = " + DtvRoot.isStartControlEnd);
					return;
				}

				final WindowManager wManager = (WindowManager) tempContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
				WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
				wmParams.x = 0;
				wmParams.y = 0;
				wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
				wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
				wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
				wmParams.windowAnimations = 0;
				wmParams.format = PixelFormat.TRANSPARENT | WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW;
				wmParams.gravity = Gravity.CENTER;
				wmParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

				final ViewActionInfo viewActionInfo = new ViewActionInfo(tempContext);
				viewActionInfo.setDisplayInfo(schedule.getProgramName(), schedule.getTitle(), tempContext.getString(R.string.stringRequest), tempContext.getString(R.string.yes),
						tempContext.getString(R.string.no));

				viewActionInfo.setOnPlayScheduleListener(new OnPlayScheduleListener() {

					@Override
					public void onPlaySchedule() {
						// TODO Auto-generated method stub
						if ((ConstActivityStatus.ACTIVITY_ONPUASE_STATUS == DtvRoot.mDtvRootCurStatus || ConstActivityStatus.ACTIVITY_ONRESUME_STATUS == DtvRoot.mDtvRootCurStatus)
								&& (true == DtvRoot.isStartControlEnd)) {
							IScheduleReceive tmpSch = DtvScheduleManager.getInstance().mOnScheduleReceive;
							if (null != tmpSch) {
								tmpSch.onScheduleTimeUp(schedule);
								MenuDisplayManager.getInstance(tempContext).dismissAllRegistered();
							}
						} else {
							Intent intent = new Intent("com.changhong.dmt.itv.tvpartner.closeimeui");
							tempContext.sendBroadcast(intent);
							// the highest position for distinguishing channelNum and channelIndex,  the second highest for distinguishing carrying sourceID or not
							int indexCarryInfo = schedule.getProgramServiceIndex() | 0xc0000000 | (convertSourceID2SpecCode(schedule.getSourceID()) << 28 & 0x30000000);
							Log.i(TAG, String.format("LL onPlaySchedule()>>indexCarryInfo1 = 0x%x", indexCarryInfo));
							indexCarryInfo = indexCarryInfo | 0x40000000;
							Log.i(TAG, String.format("LL onPlaySchedule()>>indexCarryInfo2 = 0x%x", indexCarryInfo));
							DtvCommonManager.getInstance().startInputSource(indexCarryInfo);
						}
						viewActionInfo.setVisibility(View.GONE);
					}
				});
				wManager.addView(viewActionInfo, wmParams);
			}
		}
	}

	/**
	 * 显示信息提示框
	 * @param context							显示上下文
	 * @param text								显示内容
	 * @param duration						自定义显示时长，设置为<=0显示默认的5s，设置为>0显示指定的时间
	 */
	public static void showHintToast(Context context, String text, int duration) {
		if (hintToast == null) {
			//	Log.i("YangLiu", "3hintToast is null, creat new one and show");
			hintToast = new CommonHintToast(context);
			hintToast.setMessage(text);
			if (duration > 0)
				hintToast.setDuration(duration);
			hintToast.show();
		} else {
			//	Log.i("YangLiu", "3hintToast is not null, directly show");
			hintToast.show();
		}
	}

	public static void showHintToast(Context context, String text) {
		if (hintToast == null) {
			//	Log.i("YangLiu", "2hintToast is null, creat new one and show");
			hintToast = new CommonHintToast(context);
			hintToast.setMessage(text);
			hintToast.show();
		} else {
			//	Log.i("YangLiu", "2hintToast is not null, directly show");
			hintToast.show();
		}
	}
}

/**
 * 预约事件序列化类
 * @author YangLiu
 */
class ScheduleSerialize implements Serializable {
	private static final long serialVersionUID = 1L;
	public int mServiceIndex;
	public int mProgramNum;
	public String mProgramName;
	public String mEventName;
	public int mSourceID;

	public ScheduleSerialize() {
	}

	public ScheduleSerialize(int sourceID, int servicIndex, int programNum, String programName, String eventName) {
		this.mSourceID = sourceID;
		this.mServiceIndex = servicIndex;
		this.mProgramNum = programNum;
		this.mProgramName = programName;
		this.mEventName = eventName;
	}
}

/**
 * 预约事件输入流类
 * @author YangLiu
 */
class ScheduleInputStream extends ObjectInputStream {

	public ScheduleInputStream(InputStream input) throws StreamCorruptedException, IOException {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass osClass) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return ScheduleSerialize.class;
	}

	@Override
	protected Object resolveObject(Object object) throws IOException {
		// TODO Auto-generated method stub
		return ScheduleSerialize.class;
	}
}