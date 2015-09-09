/**
 * @filename 
 * @describe
 * 	  ϵͳ������ؽӿڷ�װ���������������ص�ֻ����STB����һ�������ϵͳ�ӹܣ�	
 * @author:
 * @date: 
 * @version 0.1
 * history:
 * 	 2012-7-17  ���ӻ�ȡ��Ӫ����Ƶ���б�ӿ�getOPMainFreqList
 */
package com.changhong.tvos.dtv;

import java.util.List;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.service.ITimerShedule;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.TimerInfo;
import android.util.Log;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

/**
 * DTV 的预约节目管理
 */
public class TimerManager {

	public final static String TAG = "DTVAPI.TimerManager";

	static TimerManager msInstance = null;
	private ITimerShedule ITimerServer = null;
	private IDTVService mDTVServer = null;
	IBinder serviceBinder = null;


	/**
	 * 构造方法
	 */
	private TimerManager() {
	}
	public static TimerManager getInstance(Context context) {
		if (msInstance != null) {
			return msInstance;
		}
		msInstance = new TimerManager();
		bindService();
		return msInstance;
	}
	
	private static void bindService(){
		IDTVService dtvServer = null;
		ITimerShedule timerServer = null;
		IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
		if (bind != null) {
			dtvServer = IDTVService.Stub.asInterface(bind);

			try {
				IBinder serviceBinder = dtvServer.getTimerShudle();
				if (serviceBinder != null) {
					timerServer = ITimerShedule.Stub.asInterface(serviceBinder);
					Log.e(TAG, "ITimerShedule bind : " + timerServer);
				}
			} catch (RemoteException exception) {
				Log.e(TAG, "ITimerShedule bind filed");
			}
		} else {
			Log.e(TAG, "service bind filed");
		}
		msInstance.mDTVServer = dtvServer;
		msInstance.ITimerServer = timerServer;
	}
	
	/**
	 * 检查Service是否OK，解决EPG不能预约问题	2015-6-9		YangLiu
	 * @return
	 */
	private boolean checkServiceOK() {
		if ((serviceBinder != null) && (!serviceBinder.isBinderAlive())) {
			ITimerServer = null;
			serviceBinder = null;
			ITimerServer = null;
		}

		if (ITimerServer == null) {
			Log.i(TAG, "ITimerServer=" + ITimerServer);
			if (mDTVServer != null) {
				Log.i(TAG, "mDTVServer=" + mDTVServer);
				try {
					serviceBinder = mDTVServer.getTimerShudle();
					Log.i(TAG, "serviceBinder=" + serviceBinder);
					if (serviceBinder != null) {
						ITimerServer = ITimerShedule.Stub.asInterface(serviceBinder);
						Log.i(TAG, "stub ITimerServer=" + ITimerServer);
						return true;
					}
					return false;
				} catch (RemoteException exception) {
					return false;
				}
			} else {
				//重新刷新一遍BindService
				IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
				if (bind != null) {
					mDTVServer = IDTVService.Stub.asInterface(bind);
				}
				if (mDTVServer != null) {
					try {
						serviceBinder = mDTVServer.getTimerShudle();
						if (serviceBinder != null) {
							ITimerServer = ITimerShedule.Stub.asInterface(serviceBinder);
							return true;
						}
						return false;
					} catch (RemoteException exception) {
						return false;
					}
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * EPG不能预约问题 checkServiceOK is not OK，添加Timer
	 * @param timer
	 * @return
	 * @exception
	 */
	public int addTimer(TimerInfo timer) {
		if (!checkServiceOK()) {
			Log.i(TAG, "addTimer---->checkService is not OK, return -4");
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			Log.i(TAG, "addTimer---->add succeed, return 1");
			return ITimerServer.addTimer(timer);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			Log.i(TAG, "addTimer---->add failed, return -4");
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * 删除某个Timer
	 * @param timer
	 * @return
	 */
	public int deleteTimer(TimerInfo timer) {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			return ITimerServer.deleteTimer(timer);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * 获取Timer列表
	 * @param type
	 * @return
	 */
	public List<TimerInfo> getTimerList(int type) {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			return ITimerServer.getTimerList(type);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除所有Timer列表
	 * @return
	 */
	public int deleteAllTimer() {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			return ITimerServer.deleteAllTimer();
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
	}
}