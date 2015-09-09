package com.changhong.tvos.dtv.tvap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import com.changhong.tvos.dtv.vo.CICAMAnnounce;
import com.changhong.tvos.dtv.vo.CICAMFinger;
import com.changhong.tvos.dtv.vo.CICAMForceChannel;
import com.changhong.tvos.dtv.vo.CICAMMail;
import com.changhong.tvos.dtv.vo.CICAMMessageBase.ConstCICAMsgType;
import com.changhong.tvos.dtv.vo.CICAMPrompt;
import com.changhong.tvos.dtv.vo.CICAMSubtitle;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.dtv.vo.DTVConstant.DFAConst;
import com.changhong.tvos.dtv.vo.DTVConstant.startControlConst;
import com.changhong.tvos.dtv.vo.DfaProgressInfo;
import com.changhong.tvos.dtv.vo.DfaResultInfo;
import com.changhong.tvos.dtv.vo.DfaTriggerInfo;
import com.changhong.tvos.dtv.vo.PlayStatusInfo;

public class DtvMsgManager {
	private static final String TAG = "DtvMsgManager";
	private static DtvMsgManager curManager = null;
	private IMsgReceive mOnMsgReceive = null;
	private CiCaReceiver mCiCaReceiver = null;
	private IntentFilter mCicaIntentFilter = null;
	private DfaReceiver mDfaReceiver = null;
	private IntentFilter mDfaIntentFilter = null;
	private PlayStatusReceiver mPlayStatusReceiver = null;
	private IntentFilter mPlayStatusIntentFilter = null;
	private BootServiceReceiver mBootServiceReceiver = null;
	private IntentFilter mBootServiceIntentFilter = null;
	private ChannelChangedReceiver mChannelChangedReceiver = null;
	private IntentFilter mChannelChangedIntentFilter = null;
	private Context mContext;

	public interface IMsgReceive {
		public void onMsgMail(CICAMMail mail);

		public void onMsgFinger(CICAMFinger finger);

		public void onMsgAnnounce(CICAMAnnounce announce);

		public void onMsgPrompt(CICAMPrompt prompt);

		public void onMsgSubtitle(CICAMSubtitle subtitle);

		public void onMsgForceScan();

		public void onMsgForceChannel(CICAMForceChannel forceChannel);

		public void onMsgUserMenu(Bundle mBundle);

		public void onMsgForceMenu(Bundle mBundle);

		public void onMsgDFA(int msgClass, Bundle mBundle);

		public void onPlayStatusUpdate(PlayStatusInfo playStatusInfo);

		public void onBootService(Bundle mBundle);

		public void onChannelChanged();

	}

	private DtvMsgManager(Context context) {
		super();
	}

	public static DtvMsgManager getInstance(Context context) {

		if (curManager == null) {
			curManager = new DtvMsgManager(context);
			curManager.mContext = context;
		}
		return curManager;
	}

	public void install(IMsgReceive onMsg) {
		if (mCiCaReceiver == null) {
			mCiCaReceiver = new CiCaReceiver();
			mCicaIntentFilter = new IntentFilter(DTVConstant.DTV_CICAM_PROMPT_NOTIRY);
			mContext.registerReceiver(mCiCaReceiver, mCicaIntentFilter);
		}
		if (mDfaReceiver == null) {
			mDfaReceiver = new DfaReceiver();
			mDfaIntentFilter = new IntentFilter(DFAConst.DFA_STATUS_BROADCAST);
			mContext.registerReceiver(mDfaReceiver, mDfaIntentFilter);
		}
		if (mPlayStatusReceiver == null) {
			mPlayStatusReceiver = new PlayStatusReceiver();
			mPlayStatusIntentFilter = new IntentFilter(DTVConstant.DTV_PLAYER_STATUS_CHANGE);
			mContext.registerReceiver(mPlayStatusReceiver, mPlayStatusIntentFilter);
		}
		if (mBootServiceReceiver == null) {
			mBootServiceReceiver = new BootServiceReceiver();
			mBootServiceIntentFilter = new IntentFilter(startControlConst.DTV_START_SERVICE_INFO);
			mContext.registerReceiver(mBootServiceReceiver, mBootServiceIntentFilter);
		}
		if (mChannelChangedReceiver == null) {
			mChannelChangedReceiver = new ChannelChangedReceiver();
			//mChannelChangedIntentFilter = new IntentFilter(DTVConstant.CHANNEL_INFO_CHANGED_BROADCAST);
			mChannelChangedIntentFilter = new IntentFilter("com.changhong.tvos.update_DTV_channel_list");
			mContext.registerReceiver(mChannelChangedReceiver, mChannelChangedIntentFilter);
		}

		mOnMsgReceive = onMsg;
	}

	public void unInstall() {
		if (mCiCaReceiver != null) {
			mContext.unregisterReceiver(mCiCaReceiver);
			mCiCaReceiver = null;
		}
		if (mDfaReceiver != null) {
			mContext.unregisterReceiver(mDfaReceiver);
			mDfaReceiver = null;
		}
		if (mPlayStatusReceiver != null) {
			mContext.unregisterReceiver(mPlayStatusReceiver);
			mPlayStatusReceiver = null;
		}
		if (mBootServiceReceiver != null) {
			mContext.unregisterReceiver(mBootServiceReceiver);
			mBootServiceReceiver = null;
		}
		if (mChannelChangedReceiver != null) {
			mContext.unregisterReceiver(mChannelChangedReceiver);
			mChannelChangedReceiver = null;
		}
	}

	class PlayStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle mBundle = intent.getExtras();
			PlayStatusInfo playStatusInfo = null;
			playStatusInfo = (PlayStatusInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
			mOnMsgReceive.onPlayStatusUpdate(playStatusInfo);
		}
	}

	class CiCaReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {

			Bundle mBundle = intent.getExtras();
			int msgType = mBundle.getInt(BroadcastConst.MSG_TYPE_NAME);

			Log.v(TAG, "*****************************CiCaReceiver>>msgType=" + msgType);

			switch (msgType) {
			case ConstCICAMsgType.MSG_MAIL: /*0： CA/CI的邮件信息*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_MAIL");
				CICAMMail mail;
				mail = (CICAMMail) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				mOnMsgReceive.onMsgMail(mail);
			}
				break;
			case ConstCICAMsgType.MSG_FINGER: /*1：指纹信息*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_FINGER");
				CICAMFinger finger;
				finger = (CICAMFinger) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				mOnMsgReceive.onMsgFinger(finger);

			}
				break;
			case ConstCICAMsgType.MSG_ANNOUNCE: /*4：announce*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_ANNOUNCE");
				CICAMAnnounce announce;
				announce = (CICAMAnnounce) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				mOnMsgReceive.onMsgAnnounce(announce);
			}
				break;
			case ConstCICAMsgType.MSG_PROMPT: /*2：提示信息*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_PROMPT");
				CICAMPrompt prompt;
				prompt = (CICAMPrompt) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				mOnMsgReceive.onMsgPrompt(prompt);
			}
				break;
			case ConstCICAMsgType.MSG_SUBTITLE: /*3：子标题*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_SUBTITLE");
				CICAMSubtitle subtitle;
				subtitle = (CICAMSubtitle) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				mOnMsgReceive.onMsgSubtitle(subtitle);
			}
				break;
			case ConstCICAMsgType.MSG_FORCE_RESCAN: /*8：强制重新搜台*/
				Log.v(TAG, "ConstCICAMsgType.MSG_FORCE_RESCAN");
				mOnMsgReceive.onMsgForceScan();
				break;
			case ConstCICAMsgType.MSG_FORCE_CHANNEL: /*9：强制换台*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_FORCE_CHANNEL");
				CICAMForceChannel forceChannel = null;
				forceChannel = (CICAMForceChannel) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				mOnMsgReceive.onMsgForceChannel(forceChannel);
			}
				break;
			case ConstCICAMsgType.MSG_USER_MENU: /*5：用户显示*/
				Log.v(TAG, "ConstCICAMsgType.MSG_USER_MENU");
				// sendBroadcast(context ,intent);
				mOnMsgReceive.onMsgUserMenu(mBundle);
				break;
			case ConstCICAMsgType.MSG_FORCE_MENU: /*6：强制显示*/
			{
				Log.v(TAG, "ConstCICAMsgType.MSG_FORCE_MENU");
				mOnMsgReceive.onMsgForceMenu(mBundle);
				break;
			}
			case ConstCICAMsgType.MSG_CHANNEL_UPDATE:/*11：节目更新*/
				Log.v(TAG, "ConstCICAMsgType.MSG_CHANNEL_UPDATE");
				mOnMsgReceive.onChannelChanged();
				break;
			default:
				break;
			}
		}
	}

	class DfaReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {

			Bundle mBundle = intent.getExtras();
			int msgType = mBundle.getInt(BroadcastConst.MSG_TYPE_NAME);

			Log.v(TAG, "*****************************DfaReceiver>>msgType=" + msgType);
			Log.v(TAG, "CH_DTV_CICA_MsgType.MSG_DFA_DTV");
			mOnMsgReceive.onMsgDFA(msgType, mBundle);
			switch (msgType) {
			case DFAConst.DFA_MSG_TYPE_TRIGGER: {
				DfaTriggerInfo dfaTrigger;
				dfaTrigger = (DfaTriggerInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);

				break;
			}
			case DFAConst.DFA_MSG_TYPE_PROGRESS: {
				DfaProgressInfo dfaProcess;
				dfaProcess = (DfaProgressInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);

				break;
			}
			case DFAConst.DFA_MSG_TYPE_RESULT: {
				DfaResultInfo dfaResult;
				dfaResult = (DfaResultInfo) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
				break;
			}
			default:
				break;
			}
		}
	}

	class BootServiceReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {

			Bundle mBundle = intent.getExtras();
			mOnMsgReceive.onBootService(mBundle);
		}
	}

	class ChannelChangedReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {
			mOnMsgReceive.onChannelChanged();
		}
	}
}