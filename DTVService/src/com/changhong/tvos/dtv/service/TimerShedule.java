package com.changhong.tvos.dtv.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.changhong.tvos.dtv.provider.BaseProgramManager;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.TimerInfo;

public class TimerShedule extends ITimerShedule.Stub {
	final static String TAG = "dtvservice.TimerShedule";

	boolean TimerChanged = false;
	private static TimerShedule mInstance = null;
	private TimerStorge timerdb = null;
	Context mcontext = null;
	List<TimerInfo> timerList = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
	BaseProgramManager baseProgramManager = null;

	private void checkTimer() {
		long curTime = System.currentTimeMillis();
		boolean isFlush = false;

		Iterator<TimerInfo> iterator = timerList.iterator();
		while (iterator.hasNext()) {
			TimerInfo tempTimer = iterator.next();
			/*Log.i(TAG,"[curTime]"+curTime + 
					"[start]"+tempTimer.mlStartNotifyTime+
					";[notify]"+tempTimer.mlNotNotifyTime+
					";[original]"+tempTimer.mOriginal);*/
			if (curTime > tempTimer.mlNotNotifyTime) {
				String str = String.format("curTime = %s,mlStartNotifyTime = %s,mlNotNotifyTime = %s,", dateFormat.format(new Date(curTime)), dateFormat.format(new Date(tempTimer.mlStartNotifyTime)),
						dateFormat.format(tempTimer.mlNotNotifyTime));
				Log.i(TAG, "[str]" + str);
				iterator.remove();
				isFlush = true;
				continue;
			}
			/**��Ŀʱ�䵽�㣬��UI���㲥**/
			if (curTime >= tempTimer.mlStartNotifyTime) {
				Intent intent = new Intent(tempTimer.mstrTriggerBroadCast);
				Bundle bundle = new Bundle();
				bundle.putParcelable(DTVConstant.BroadcastConst.MSG_INFO_NAME, tempTimer);
				intent.putExtras(bundle);
				mcontext.sendBroadcast(intent);
				Log.i(TAG, "service sendBroadcast>>mstrTriggerBroadCast = " + tempTimer.mstrTriggerBroadCast + "[start]" + tempTimer.mlStartNotifyTime + "[end]" + tempTimer.mlNotNotifyTime);
				iterator.remove();
				isFlush = true;
			}
			if (isFlush) {
				Log.i(TAG, "isFlush,TimerChanged");
				TimerChanged = true;
			}
		}
	}

	/**ѭ��**/
	private int init() {
		final Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (timerList != null) {
						synchronized (timerList) {
							checkTimer();
						}
					}
					if (TimerChanged) {
						/**�����б�,��timerList-->timertmpList**/
						List<TimerInfo> timertmpList = new ArrayList<TimerInfo>();
						synchronized (timerList) {
							Iterator<TimerInfo> iterator = timerList.iterator();
							while (iterator.hasNext()) {
								TimerInfo tempTimer = iterator.next();
								timertmpList.add(tempTimer);
							}
						}
						/**��timertmpListд��**/
						Log.i(TAG, "save timer start!");
						timerdb.setTimersRecordsToDB(timertmpList);
						timerdb.flushMedia();
						Log.i(TAG, "save timer end!");
						TimerChanged = false;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setDaemon(true);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				thread.start();
			}
		}, 30000);
		return 0;
	}

	private TimerShedule(Context context) {
		mcontext = context;
		timerdb = TimerStorge.getInstance(context);
		timerList = timerdb.getTimerRecordsFromDB();

		if (null == timerList) {
			timerList = new ArrayList<TimerInfo>();
		}
		this.init();
	}

	public static TimerShedule getInstance(Context context) {
		if (mInstance != null) {
			return mInstance;
		}
		mInstance = new TimerShedule(context);
		return mInstance;
	}

	@Override
	public int addTimer(TimerInfo timer) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "add timer type:" + timer.miType + " start:" + timer.mlStartNotifyTime + "broadcast:" + timer.mstrTriggerBroadCast);
		if (timerList != null) {
			synchronized (timerList) {
				Log.i(TAG, "[timerListfirstSize]" + timerList.size());
				Iterator<TimerInfo> iterator = timerList.iterator();
				while (iterator.hasNext()) {
					TimerInfo tempTimer = iterator.next();
					if (tempTimer.IsSame(timer)) {
						Log.i(TAG, "add timer :find the same timer");
						return 1;
					}
				}
			}
		}

		synchronized (timerList) {
			timerList.add(timer);
			Log.i(TAG, "[timerListafterSize]" + timerList.size());
			/** ����**/
			Collections.sort(timerList, new Comparator<TimerInfo>() {
				public int compare(TimerInfo arg0, TimerInfo arg1) {
					return (int) (arg0.mlStartNotifyTime - arg1.mlStartNotifyTime);
				}
			});
		}
		TimerChanged = true;
		return 0;
	}

	/**
	 * return 
	 * 0-success;1-
	 */
	@Override
	public int deleteTimer(TimerInfo timer) throws RemoteException {
		Log.i(TAG, "src timer: id:" + timer.miIndex + " type:" + timer.miType + " start:" + timer.mlStartNotifyTime + " stop:" + timer.mlNotNotifyTime + "broadcast:" + timer.mstrTriggerBroadCast
				+ " data:" + timer.mByteDataInfo + "hashcode:" + timer.hashCode());
		if (timerList != null) {
			synchronized (timerList) {
				Iterator<TimerInfo> iterator = timerList.iterator();
				while (iterator.hasNext()) {
					TimerInfo tempTimer = iterator.next();
					if (tempTimer.IsSame(timer)) {
						Log.i(TAG, "find!");
						iterator.remove();
						TimerChanged = true;
						return 0;
					}
				}
			}
		}
		return 1;
	}

	@Override
	public List<TimerInfo> getTimerList(int type) throws RemoteException {
		// TODO Auto-generated method stub
		List<TimerInfo> tmpTimerList = null;
		if (timerList == null) {
			Log.i(TAG, "getTimerList : not finded timer");
			return null;
		}

		tmpTimerList = new ArrayList<TimerInfo>();
		Log.i(TAG, "getTimerList : " + timerList.size());
		synchronized (timerList) {
			Iterator<TimerInfo> iterator = timerList.iterator();
			while (iterator.hasNext()) {
				TimerInfo tempTimer = iterator.next();
				if (tempTimer.miType == type) {
					tmpTimerList.add(tempTimer);
				}
			}
		}
		return tmpTimerList;
	}

	@Override
	public int deleteAllTimer() throws RemoteException {
		if (timerList == null) {
			return 0;
		}
		synchronized (timerList) {
			timerList.clear();
			TimerChanged = true;
		}
		return 0;
	}
}
