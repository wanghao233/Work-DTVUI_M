/**
 * @filename TV ҪDTV(com.changhong.tvos.dtvapi.jar)
 * @author:
 * @date:
 * @version 0.2
 * @history 2012-12-28 1.5
 */
package com.changhong.tvos;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.changhong.tvos.dtv.for3rd.For3rdConst;
import com.changhong.tvos.dtv.for3rd.IDTV3rdInterface;
import com.changhong.tvos.dtv.for3rd.IDTV3rdService;
import com.changhong.tvos.dtv.for3rd.IPlayerEventListener;
import com.changhong.tvos.dtv.for3rd.InterAudioTrack;
import com.changhong.tvos.dtv.for3rd.InterCardStatus;
import com.changhong.tvos.dtv.for3rd.InterChDetailInfo;
import com.changhong.tvos.dtv.for3rd.InterChannelInfo;
import com.changhong.tvos.dtv.for3rd.InterDTVChannelBaseInfo;
import com.changhong.tvos.dtv.for3rd.InterDTVSource;
import com.changhong.tvos.dtv.for3rd.InterEPGEvent;
import com.changhong.tvos.dtv.for3rd.InterOperator;
import com.changhong.tvos.dtv.for3rd.InterTimerInfo;
import com.changhong.tvos.dtv.for3rd.InterTunerStatus;
import com.changhong.tvos.dtv.for3rd.InterUTCDate;
import com.changhong.tvos.dtv.for3rd.InterUTCTime;
import com.changhong.tvos.dtv.for3rd.InterVersionInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * TV
 */
public class DTVM {
	final static String TAG = "DTV.DTVM";

	private static DTVM instance = null;

	private UUID mUuid;
	Context mContext = null;

	private IDTV3rdInterface mIDtvInterface = null;
	private IDTV3rdService mDTV3rdService = null;
	private IPlayerStatusListener callbackFunction = null;
	private PlayerStatusCallback callbackListener = new PlayerStatusCallback();

	/**  **/
	public static class DTVUTCTime {
		/**  **/
		public int miHour;

		/**  **/
		public int miMinute;

		/**  **/
		public int miSecond;

		/**
		 * @param hour
		 * @param minute
		 * @param second
		 * @throws
		 */
		public DTVUTCTime(int hour, int minute, int second) {
			this.miHour = hour;
			this.miMinute = minute;
			this.miSecond = second;
		}

		public DTVUTCTime() {
			// TODO Auto-generated constructor stub
			miHour = 0;
			miMinute = 0;
			miSecond = 0;
		}
	}

	/**  **/
	public static class DTVUTCDate {
		/**  **/
		public int miYear;

		/**  **/
		public int miMonth;

		/**  **/
		public int miDay;

		/**  **/
		public int miWeekDay;

		/**
		 * @param year
		 * @param month
		 * @param day
		 * @param weekDay
		 * @throws
		 */
		public DTVUTCDate(int year, int month, int day, int weekDay) {
			this.miYear = year;
			this.miMonth = month;
			this.miDay = day;
			this.miWeekDay = weekDay;
		}

		public DTVUTCDate() {
			// TODO Auto-generated constructor stub
			miYear = 0;
			miMonth = 0;
			miDay = 0;
			miWeekDay = 0;
		}
	}

	public static enum EnumAudioEncodeType {
		/**
		 * MPEG1
		 **/
		AUDIO_CODE_MPEG1,

		/**
		 * MPEG2
		 **/
		AUDIO_CODE_MPEG2,

		/**
		 * MP3
		 **/
		AUDIO_CODE_MP3,

		/**
		 * AC3
		 **/
		AUDIO_CODE_AC3,

		/**
		 * AAC
		 **/
		AUDIO_CODE_AAC_ADTS,

		/**
		 * AAC
		 **/
		AUDIO_CODE_AAC_LOAS,

		/**
		 * HEAAC
		 **/
		AUDIO_CODE_HEAAC_ADTS,

		/**
		 * HEAAC
		 **/
		AUDIO_CODE_HEAAC_LOAS,

		/**
		 * WMA
		 **/
		AUDIO_CODE_WMA,

		/**
		 * AC3
		 **/
		AUDIO_CODE_AC3_PLUS,

		/**
		 * LPCMʽ
		 **/
		AUDIO_CODE_LPCM,

		/**
		 * DTS
		 **/
		AUDIO_CODE_DTS,

		/**
		 * ATRAC
		 **/
		AUDIO_CODE_ATRAC,

		/**  **/
		AUDIO_CODE_UNKNOWN
	}

	public static enum EnumVideoEncodeType {
		/**
		 * MPEG2
		 **/
		VIDEO_CODE_MPEG2,

		/**
		 * MPEG2
		 **/
		VIDEO_CODE_MPEG2_HD,

		/**
		 * MPEG4
		 **/
		VIDEO_CODE_MPEG4_ASP,

		/**
		 * MPEG4
		 **/
		VIDEO_CODE_MPEG4_ASP_A,

		/**
		 * MPEG4
		 **/
		VIDEO_CODE_MPEG4_ASP_B,

		/**
		 * MPEG4
		 **/
		VIDEO_CODE_MPEG4_ASP_C,

		/**
		 * DIVX
		 **/
		VIDEO_CODE_DIVX,

		/**
		 * VC1
		 **/
		VIDEO_CODE_VC1,

		/**
		 * H264
		 **/
		VIDEO_CODE_H264,

		/**  **/
		VIDEO_CODE_UNKNOWN
	}

	public static enum EnumServiceType {
		/**  */
		DTV_RPOGRAM_TYPE_ALL,

		/**  **/
		DTV_PROGRAM_TYPE_TV,

		/**  **/
		DTV_PROGRAM_TYPE_RADIO,

		/**  **/
		DTV_PROGRAM_TYPE_UNKNOWN
	}

	/**  **/
	public static enum EnumAudioMode {
		/**  **/
		AUDIO_MODE_MONO,

		/**  **/
		AUDIO_MODE_STERO,

		/**  **/
		AUDIO_MODE_UNKNOWN

	}

	/**
	 * DTVChannelInfo
	 **/
	public static class DTVChannelInfo {

		public int miChannelIndex;

		public int miChannelnumber;

		public String mstrServiceName;

		public EnumServiceType meServiceType;

		public EnumVideoEncodeType meVideoType;

		public EnumAudioEncodeType meAudioType;

		public boolean mbScrambled;

		public EnumAudioMode meAudioMode;

		public String mstrRating;

		public int miBanlenceVolume;

		public boolean mbLock;

		public boolean mbSkip;

		public boolean mbFav;

		String mstrURI;

		public DTVChannelInfo(InterChannelInfo channelInfo) {
			miChannelIndex = channelInfo.miChannelIndex;

			miChannelnumber = channelInfo.miChannelnumber;

			mstrServiceName = channelInfo.mstrServiceName;

			if (channelInfo.miServiceType == For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV) {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_TV;
			} else if (channelInfo.miServiceType == For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_RADIO) {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_RADIO;
			} else {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_UNKNOWN;
			}

			if ((channelInfo.miVideoType >= 0) && (channelInfo.miVideoType < EnumVideoEncodeType.VIDEO_CODE_UNKNOWN.ordinal())) {
				meVideoType = EnumVideoEncodeType.values()[channelInfo.miVideoType];
			} else {
				meVideoType = EnumVideoEncodeType.VIDEO_CODE_UNKNOWN;
			}

			if ((channelInfo.miAudioType >= 0) && (channelInfo.miAudioType < EnumAudioEncodeType.AUDIO_CODE_UNKNOWN.ordinal())) {
				meAudioType = EnumAudioEncodeType.values()[channelInfo.miAudioType];
			} else {
				meAudioType = EnumAudioEncodeType.AUDIO_CODE_UNKNOWN;
			}

			if ((channelInfo.miAudioMode >= 0) && (channelInfo.miAudioMode < EnumAudioMode.AUDIO_MODE_UNKNOWN.ordinal())) {
				meAudioMode = EnumAudioMode.values()[channelInfo.miAudioMode];
			} else {
				meAudioMode = EnumAudioMode.AUDIO_MODE_UNKNOWN;
			}

			mbScrambled = channelInfo.mbScrambled;

			mstrRating = channelInfo.mstrRating;

			miBanlenceVolume = channelInfo.miBanlenceVolume;

			mbLock = channelInfo.mbLock;

			mbSkip = channelInfo.mbSkip;

			mbFav = channelInfo.mbFav;

			mstrURI = channelInfo.mstrURI;
		}
	}

	/**
	 * DTVChannelBaseInfo
	 **/
	public static class DTVChannelBaseInfo {

		public static final int DTV_CHANNEL_AUDIO_MODE_MONO = 0;

		public static final int DTV_CHANNEL_AUDIO_MODE_STERO = 1;

		public int miChannelIndex;

		public int miChannelnumber;

		public String mstrServiceName;

		public int miServiceType;

		public EnumServiceType meServiceType;

		/**
		 * ServiceID
		 **/
		public int miServiceID;

		/**
		 * TSID
		 **/
		public int miTSID;

		/**
		 * Orignal Network ID
		 **/
		public int miOrgNetID;

		public boolean mbScrambled;

		public String mstrCurAudioTrack;

		public int miSoundMode;

		public int miBanlenceVolume;

		public String msRating;

		public boolean mbLock;

		public boolean mbSkip;

		public boolean mbFav;

		public int miDemodType;

		public String msLogo;

		public DTVChannelBaseInfo(InterDTVChannelBaseInfo channelInfo) {
			miChannelIndex = channelInfo.miChannelIndex;
			miChannelnumber = channelInfo.miChannelnumber;
			mstrServiceName = channelInfo.mstrServiceName;

			if (channelInfo.miServiceType == For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV) {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_TV;
			} else {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_RADIO;
			}

			miServiceID = channelInfo.miServiceID;
			miTSID = channelInfo.miTSID;
			miOrgNetID = channelInfo.miOrgNetID;

			mbScrambled = channelInfo.mbScrambled;
			mstrCurAudioTrack = channelInfo.mstrCurAudioTrack;
			miSoundMode = channelInfo.miSoundMode;
			miBanlenceVolume = channelInfo.miBanlenceVolume;
			msRating = channelInfo.msRating;
			mbLock = channelInfo.mbLock;
			mbSkip = channelInfo.mbSkip;
			mbFav = channelInfo.mbFav;
			miDemodType = channelInfo.miDemodType;

			msLogo = channelInfo.msLogo;
		}

		public DTVChannelBaseInfo() {
			// TODO Auto-generated constructor stub
		}
	}

	/**
	 * DTVChannelDetailInfo
	 **/
	public static class DTVChannelDetailInfo {
		public static final int DTV_CHANNEL_AUDIO_MODE_MONO = 0;

		public static final int DTV_CHANNEL_AUDIO_MODE_STERO = 1;

		public int miChannelIndex;

		public int miChannelnumber;

		public String mstrServiceName;

		public EnumServiceType meServiceType;

		/**
		 * ServiceID
		 **/
		public int miServiceID;

		/**
		 * TSID
		 **/
		public int miTSID;

		/**
		 * Orignal Network ID
		 **/
		public int miOrgNetID;

		public EnumVideoEncodeType meVideoType;

		public EnumAudioEncodeType meAudioType;

		public boolean mbScrambled;

		public EnumAudioMode meAudioMode;

		public String mstrRating;

		public int miBanlenceVolume;

		public int miCarrierIndex;

		public int miSignalLevel;

		public int miSignalQuality;

		public DTVAudioTrack mAudioTrackInfo;

		public int miSoundMode;

		public boolean mbLock;

		public boolean mbSkip;

		public boolean mbFav;

		String mstrURI;

		public DTVChannelDetailInfo(InterChDetailInfo channelInfo) {
			mAudioTrackInfo = new DTVAudioTrack();

			miChannelIndex = channelInfo.miChannelIndex;
			miChannelnumber = channelInfo.miChannelnumber;
			mstrServiceName = channelInfo.mstrServiceName;

			if (channelInfo.miServiceType == For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV) {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_TV;
			} else {
				meServiceType = EnumServiceType.DTV_PROGRAM_TYPE_RADIO;
			}

			miServiceID = channelInfo.miServiceID;
			miTSID = channelInfo.miTSID;
			miOrgNetID = channelInfo.miOrgNetID;

			miCarrierIndex = channelInfo.miCarrierIndex;
			miSignalLevel = channelInfo.miSignalLevel;
			miSignalQuality = channelInfo.miSignalQuality;


			if ((channelInfo.miVideoType >= 0) && (channelInfo.miVideoType < EnumVideoEncodeType.VIDEO_CODE_UNKNOWN.ordinal())) {
				meVideoType = EnumVideoEncodeType.values()[channelInfo.miVideoType];
			} else {
				meVideoType = EnumVideoEncodeType.VIDEO_CODE_UNKNOWN;
			}

			if ((channelInfo.miAudioType >= 0) && (channelInfo.miAudioType < EnumAudioEncodeType.AUDIO_CODE_UNKNOWN.ordinal())) {
				meAudioType = EnumAudioEncodeType.values()[channelInfo.miAudioType];
			} else {
				meAudioType = EnumAudioEncodeType.AUDIO_CODE_UNKNOWN;
			}

			if ((channelInfo.miAudioMode >= 0) && (channelInfo.miAudioMode < EnumAudioMode.AUDIO_MODE_UNKNOWN.ordinal())) {
				meAudioMode = EnumAudioMode.values()[channelInfo.miAudioMode];
			} else {
				meAudioMode = EnumAudioMode.AUDIO_MODE_UNKNOWN;
			}

			mAudioTrackInfo.miCurrSelect = channelInfo.mAudioTrackInfo.miCurrSelect;
			mAudioTrackInfo.miTrackNumb = channelInfo.mAudioTrackInfo.mstrAudioLanguage.length;
			mAudioTrackInfo.mstrAudioLanguage = channelInfo.mAudioTrackInfo.mstrAudioLanguage;

			mbScrambled = channelInfo.mbScrambled;
			mstrRating = channelInfo.msRating;
			miBanlenceVolume = channelInfo.miBanlenceVolume;
			mbLock = channelInfo.mbLock;
			mbSkip = channelInfo.mbSkip;
			mbFav = channelInfo.mbFav;
			mstrURI = null;
		}

		public DTVChannelDetailInfo() {
			// TODO Auto-generated constructor stub
			mAudioTrackInfo = new DTVAudioTrack();
		}
	}

	/**
	 * Operator
	 */
	public static class Operator {
		public String mstrOperatorName;

		public int miOperatorCode;

	}

	/**
	 * DTVEPGEvent
	 **/
	public static class DTVEPGEvent {
		public int miEventID;

		public DTVUTCDate mStartDate;

		public DTVUTCTime mStartTime;

		public DTVUTCTime mDuringTime;

		public String mstrEventName;

		public String mstrShortText;

		public DTVEPGEvent(InterEPGEvent event) {
			miEventID = event.miEventID;
			mStartDate = new DTVUTCDate(event.mStartDate.miYear, event.mStartDate.miMonth,
					event.mStartDate.miDay, event.mStartDate.miWeekDay);

			mStartTime = new DTVUTCTime(event.mStartTime.miHour, event.mStartTime.miMinute, event.mStartTime.miSecond);
			mDuringTime = new DTVUTCTime(event.mDuringTime.miHour, event.mDuringTime.miMinute, event.mDuringTime.miSecond);
			mstrEventName = event.mstrEventName;
			mstrShortText = event.mstrShortText;
		}
	}

	/**
	 * DTVAudioTrack
	 **/
	public static class DTVAudioTrack {
		public String[] mstrAudioLanguage;

		public int miTrackNumb;

		public int miCurrSelect;
	}

	/**
	 * TunerStatus
	 */
	public static class TunerStatus {
		public int miSignalLevel;
		public int miSignalQuality;
		public boolean mbLock;
	}


	/**
	 * DTVVersionInfo
	 */
	public static class DTVVersionInfo {
		public int miHardwareVersion;

		public int miOpVersion;

		public int miAPIMainVersion;

		public int miAPISubVersion;

		public int miMainVersion;

		public int miSubVersion;

		public String mstrReleaseDateTime;
	}

	/**
	 * EnumPlayerNotifyEvent
	 */
	public static enum EnumPlayerNotifyEvent {
		OK,

		SOURCE_IS_3D,

		CHANNEL_INFO_CHANGED,

		STOPED,

		NO_SIGNAL,

		NO_SAMRT_CARD,

		SAMRT_CARD_ERROR,

		CA_NOT_ENTITLE,

		FORCE_PAUSE,

		FORCE_RESUME,

		STATUS_ERROR,

		UN_SURPPORTED
	}

	public static class CardStatus {
		public abstract class CardType {
			public final static int CARD_TYPE_CI = 0;

			public final static int CARD_TYPE_CA = 1;

			public final static int CARD_TYPE_CHECK = 2;

			public final static int CARD_TYPE_NO_CARD = -1;
		}

		public abstract class CardStatusConst {
			public final static int CARD_STATUS_INSERT = 0;

			public final static int CARD_STATUS_CHECKING = 1;

			public final static int CARD_STATUS_OUT = 2;

			public final static int CARD_STATUS_OK = 3;

			public final static int CARD_STATUS_INVALID = 4;
		}

		public int miCardType;

		public int miCardStatus;

		public String mstrCardID;
	}

	/**
	 * ConstSoundMode
	 **/
	public static class ConstSoundMode {
		public static final int CH_DTV_SOUND_STERE0 = 0;

		public static final int CH_DTV_SOUND_LEFT = 1;

		public static final int CH_DTV_SOUND_RIGHT = 2;

		public static final int CH_DTV_SOUND_MONO = 3;

		public static final int CH_DTV_SOUND_AUTO = 4;

		public static final int CH_DTV_SOUND_MAX = 5;
	}

	public static abstract class ConstantAudioEncodeType {
		/**
		 * MPEG1
		 **/
		public static final int AUDIO_CODE_MPEG1 = 0;

		/**
		 * MPEG2
		 **/
		public static final int AUDIO_CODE_MPEG2 = 1;

		/**
		 * MP3
		 **/
		public static final int AUDIO_CODE_MP3 = 2;

		/**
		 * AC3
		 **/
		public static final int AUDIO_CODE_AC3 = 3;

		/**
		 * AAC
		 **/
		public static final int AUDIO_CODE_AAC_ADTS = 4;

		/**
		 * AAC
		 **/
		public static final int AUDIO_CODE_AAC_LOAS = 5;

		/**
		 * HEAAC
		 **/
		public static final int AUDIO_CODE_HEAAC_ADTS = 6;

		/**
		 * HEAAC
		 **/
		public static final int AUDIO_CODE_HEAAC_LOAS = 7;

		/**
		 * WMA
		 **/
		public static final int AUDIO_CODE_WMA = 8;

		/**
		 * AC3
		 **/
		public static final int AUDIO_CODE_AC3_PLUS = 9;

		/**
		 * LPCM
		 **/
		public static final int AUDIO_CODE_LPCM = 10;

		/**
		 * DTS
		 **/
		public static final int AUDIO_CODE_DTS = 11;

		/**
		 * ATRAC
		 **/
		public static final int AUDIO_CODE_ATRAC = 12;

		/** **/
		public static final int AUDIO_CODE_UNKNOWN = -1;
	}

	public abstract class ConstantVideoEncodeType {
		/**
		 * MPEG2
		 **/
		public static final int VIDEO_CODE_MPEG2 = 0;

		/**
		 * MPEG2
		 **/
		public static final int VIDEO_CODE_MPEG2_HD = 1;

		/**
		 * MPEG4
		 **/
		public static final int VIDEO_CODE_MPEG4_ASP = 2;

		/**
		 * MPEG4
		 **/
		public static final int VIDEO_CODE_MPEG4_ASP_A = 3;

		/**
		 * MPEG4
		 **/
		public static final int VIDEO_CODE_MPEG4_ASP_B = 4;

		/**
		 * MPEG4
		 **/
		public static final int VIDEO_CODE_MPEG4_ASP_C = 5;

		/**
		 * DIVX
		 **/
		public static final int VIDEO_CODE_DIVX = 6;

		/**
		 * VC1
		 **/
		public static final int VIDEO_CODE_VC1 = 7;

		/**
		 * H264
		 **/
		public static final int VIDEO_CODE_H264 = 8;

		public static final int VIDEO_CODE_UNKNOWN = -1;
	}

	/**
	 * ChannelDetailInfo
	 */
	public static class ChannelDetailInfo {

		public static final int DTV_CHANNEL_AUDIO_MODE_MONO = 0;

		public static final int DTV_CHANNEL_AUDIO_MODE_STERO = 1;

		public int miChannelIndex;

		public int miChannelnumber;

		public String mstrServiceName;

		public int miServiceType;

		public boolean mbScrambled;

		public int miCarrierIndex;

		public int miSignalLevel;

		public int miSignalQuality;

		/**
		 * ServiceID
		 **/
		public int miServiceID;

		/**
		 * TSID
		 **/
		public int miTSID;

		/**
		 * Orignal Network ID
		 **/
		public int miOrgNetID;

		public int miVideoType;

		public int miAudioType;

		public DTVAudioTrack mAudioTrackInfo;

		public int miSoundMode;

		public int miBanlenceVolume;

		public String msRating;

		public boolean mbLock;

		public boolean mbSkip;

		public boolean mbFav;
	}

	/**
	 * DTVSpecSource
	 */
	public static class DTVSpecSource {
		public int miSourceType;
		public int miSourceID;
		public String miSourceName;
	}

	private class PlayerStatusCallback extends IPlayerEventListener.Stub {
		public void onPlayerNotifyEvent(int arg0) throws RemoteException {
			// TODO Auto-generated method stub
			if (callbackFunction == null) {
				return;
			}

			EnumPlayerNotifyEvent notifyEvent;

			switch (arg0) {
				case For3rdConst.ConstantPlayerNotifyEvent.OK:
					notifyEvent = EnumPlayerNotifyEvent.OK;

					break;

				case For3rdConst.ConstantPlayerNotifyEvent.NO_SIGNAL:
					notifyEvent = EnumPlayerNotifyEvent.NO_SIGNAL;
					break;

				case For3rdConst.ConstantPlayerNotifyEvent.FORCE_PAUSE:
					notifyEvent = EnumPlayerNotifyEvent.FORCE_PAUSE;
					break;

				case For3rdConst.ConstantPlayerNotifyEvent.FORCE_RESUME:
					notifyEvent = EnumPlayerNotifyEvent.FORCE_RESUME;
					break;

				case For3rdConst.ConstantPlayerNotifyEvent.NO_SAMRT_CARD:
					notifyEvent = EnumPlayerNotifyEvent.NO_SAMRT_CARD;
					break;

				case For3rdConst.ConstantPlayerNotifyEvent.CA_NOT_ENTITLE:
					notifyEvent = EnumPlayerNotifyEvent.CA_NOT_ENTITLE;
					break;

				case For3rdConst.ConstantPlayerNotifyEvent.SAMRT_CARD_ERROR:
					notifyEvent = EnumPlayerNotifyEvent.SAMRT_CARD_ERROR;
					break;

				case For3rdConst.ConstantPlayerNotifyEvent.STATUS_ERROR:
					notifyEvent = EnumPlayerNotifyEvent.STATUS_ERROR;
					break;

				default:
					return;
			}

			if (callbackFunction != null) {
				callbackFunction.onPlayerNotifyEvent(notifyEvent);
			}
		}
	}

	/**
	 * IPlayerStatusListener
	 */
	public interface IPlayerStatusListener {
		void onPlayerNotifyEvent(EnumPlayerNotifyEvent event);
	}

	/**
	 * checkServiceOK
	 *
	 * @return
	 */
	private boolean checkServiceOK() {
		Log.i(TAG, "[checkServiceOK]" + mIDtvInterface);
		if ((mIDtvInterface == null) || (!mIDtvInterface.asBinder().isBinderAlive())) {
			if (mIDtvInterface != null) {
				Log.i(TAG, "alive=" + mIDtvInterface.asBinder().isBinderAlive());
			}
			IBinder binder;
			binder = ServiceManager.getService("com.changhong.tvos.dtv.for3rd.DTV3rdService");
			if (binder != null) {
				//��DTV3rdServiceת��ΪIDTV3rdService,�ܳɹ���?
				mDTV3rdService = IDTV3rdService.Stub.asInterface(binder);
				try {
					binder = mDTV3rdService.createDTVManager(mUuid.toString(), callbackListener);
					if (binder != null) {
						Log.i(TAG, "function checkServiceOK binder is not null");
						mIDtvInterface = IDTV3rdInterface.Stub.asInterface(binder);
						return true;
					} else {
						Log.i(TAG, "function checkServiceOK binder is null");
						return false;
					}
				} catch (RemoteException exception) {
					Log.e(TAG, "checkServiceOK --> exception");
					exception.printStackTrace();
					return false;
				}
			}
			Log.i(TAG, "Enter -->checkServiceOK() binder== null");
			return false;
		}

		return true;
	}


	private DTVM(Context context) {
		Log.i(TAG, "new DTVM started callpid=" + Binder.getCallingPid());
		mContext = context;
		mUuid = UUID.randomUUID();
		checkServiceOK();
		instance = this;
	}

	/**
	 * getChannelList
	 */
	public List<DTVChannelInfo> getChannelList() {
		if (!checkServiceOK()) {
			return null;
		}

		List<DTVChannelInfo> channelList = new ArrayList<DTVChannelInfo>();

		if (mIDtvInterface != null) {
			try {
				List<InterChannelInfo> tmplist;

				tmplist = mIDtvInterface.getChannelList();
				if ((tmplist == null) || (tmplist.size() == 0)) {
					Log.i(TAG, "Enter -->getChannelList() tmplist.size null!!!");

					return null;
				}

				InterChannelInfo tmpInfo;
				for (Iterator<InterChannelInfo> i = tmplist.iterator(); i.hasNext(); ) {
					tmpInfo = i.next();
					if (tmpInfo == null) {
						Log.i(TAG, "Enter -->getChannelList() tmpInfo null ");

						continue;
					}
					channelList.add(new DTVChannelInfo(tmpInfo));
				}

				Log.e(TAG, "List<DTVChannelInfo> getChannelList() SIZE:" + channelList.size());

				return channelList;
			} catch (RemoteException exception) {
				Log.e(TAG, "getChannelList --> exception");
				exception.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * getChannelCount
	 */
	public int getChannelCount() {
		int nChannelCount = -1;
		if (!checkServiceOK()) {
			return 0;
		}

		try {
			nChannelCount = mIDtvInterface.getChannelCount();

			Log.i(TAG, "Enter -->getChannelCount() === " + nChannelCount);

			//	 int callingPid = Binder.getCallingPid();
			//	Log.i(TAG,"getCallingPid = "+callingPid);

			return nChannelCount;
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return 0;
		}
	}

	/**
	 * getChannelListByType
	 */
	public List<DTVChannelInfo> getChannelListByType(EnumServiceType type) {
		if (!checkServiceOK()) {
			return null;
		}

		int iServiceType;

		if (type == EnumServiceType.DTV_PROGRAM_TYPE_TV) {
			iServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV;
		} else if (type == EnumServiceType.DTV_PROGRAM_TYPE_RADIO) {
			iServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_RADIO;
		} else if (type == EnumServiceType.DTV_RPOGRAM_TYPE_ALL) {
			iServiceType = For3rdConst.ConstantServiceType.DTV_RPOGRAM_TYPE_ALL;
		} else {
			return null;
		}

		Log.i(TAG, "Enter -->getChannelListByType() " + type);

		List<DTVChannelInfo> channelList = new ArrayList<DTVChannelInfo>();

		if (mIDtvInterface != null) {
			try {
				List<InterChannelInfo> tmpList;

				tmpList = mIDtvInterface.getChannelListByType(iServiceType);
				if ((tmpList == null) || (tmpList.size() == 0)) {
					Log.i(TAG, "Enter -->getChannelListByType() tmpList.size() " + tmpList.size());

					return null;
				}

				InterChannelInfo tmpInfo;
				for (Iterator<InterChannelInfo> i = tmpList.iterator(); i.hasNext(); ) {
					tmpInfo = i.next();
					if (tmpInfo == null) {
						continue;
					}
					channelList.add(new DTVChannelInfo(tmpInfo));
				}

				return channelList;
			} catch (RemoteException exception) {
				Log.e(TAG, "getChannelListByType --> exception");
				exception.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * prepare
	 */
	public int prepare() {
		if (!checkServiceOK()) {
			return 1;
		}

		int nValue = -1;

		try {
			Log.i(TAG, "dtvm.prepare");
			nValue = mIDtvInterface.prepare();
			Log.i(TAG, "Enter -->prepare() " + nValue);
			return nValue;
		} catch (RemoteException exception) {
			Log.e(TAG, "prepare--------> exception");
			exception.printStackTrace();
		}
		return 1;
	}

	/**
	 * Release
	 */
	public int Release() {

		if (!checkServiceOK()) {
			return 1;
		}

		int nValue = -1;

		try {
			Log.i(TAG, "dtvm.release()");
			nValue = mIDtvInterface.Release();
			Log.i(TAG, "Enter -->Release() " + nValue);
			return nValue;
		} catch (RemoteException exception) {
			Log.e(TAG, "Release--------> exception");
			exception.printStackTrace();
			return 1;
		}
	}

	/**
	 * ReleaseForBack
	 */
	public int ReleaseForBack() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDtvInterface.ReleaseForBack();
			return 0;
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return 1;
		}
	}

	/**
	 * play
	 */
	public int play(int channelIndex) {
		if (!checkServiceOK()) {
			return 1;
		}
		try {
			Log.i(TAG, "dtvm.play() " + channelIndex);
			return mIDtvInterface.play(channelIndex);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return 1;
		}
	}


	/**
	 * stop
	 */
	public int stop() {
		if (!checkServiceOK()) {
			return 1;
		}
		try {
			return mIDtvInterface.stop();
		} catch (RemoteException exception) {
			Log.e(TAG, "stop--------> exception");
			exception.printStackTrace();
			return 1;
		}
	}

	/**
	 * isPlaying
	 */
	public int isPlaying() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDtvInterface.isPlaying();
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return -1;
		}
	}

	/**
	 * getTunerStatus
	 */
	public TunerStatus getTunerStatus() {

		if (!checkServiceOK()) {
			return null;
		}

		InterTunerStatus tunerStatus;

		try {
			tunerStatus = mIDtvInterface.getTunerStatus();
			if (tunerStatus != null) {
				TunerStatus status = new TunerStatus();
				status.mbLock = tunerStatus.mbLock;
				status.miSignalLevel = tunerStatus.miSignalLevel;
				status.miSignalQuality = tunerStatus.miSignalQuality;
				return status;
			}
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getAudioTrack
	 */
	public DTVAudioTrack getAudioTrack() {
		if (!checkServiceOK()) {
			return null;
		}

		InterAudioTrack track;

		try {
			track = mIDtvInterface.getAudioTrack();
			if (track != null) {
				DTVAudioTrack audioTrack = new DTVAudioTrack();
				audioTrack.miCurrSelect = track.miCurrSelect;
				audioTrack.miTrackNumb = track.miTrackNumb;
				audioTrack.mstrAudioLanguage = track.mstrAudioLanguage;

				return audioTrack;
			}

			Log.i(TAG, "Enter -->getAudioTrack() track== null");
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * setAudioTrack
	 */
	public int setAudioTrack(int audioTrack) {
		if (!checkServiceOK()) {
			return 1;
		}

		Log.i(TAG, "Enter -->setAudioTrack() audioTrack== " + audioTrack);

		try {
			return mIDtvInterface.setAudioTrack(audioTrack);
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return 1;
	}

	/**
	 * getEPGPFEvent
	 */
	public List<DTVEPGEvent> getEPGPFEvent(int channelIndex) {
		if (!checkServiceOK()) {
			return null;
		}
		Log.i(TAG, "Enter -->getEPGPFEvent() channelIndex== " + channelIndex);

		try {
			List<InterEPGEvent> tmpList = mIDtvInterface.getEPGPFEvent(channelIndex);
			if (tmpList != null) {
				List<DTVEPGEvent> epgEventList = new ArrayList<DTVEPGEvent>();
				for (Iterator<InterEPGEvent> i = tmpList.iterator(); i.hasNext(); ) {
					InterEPGEvent interInfo = i.next();
					if (interInfo == null) {
						continue;
					}
					DTVEPGEvent eventTmp = new DTVEPGEvent(interInfo);
					epgEventList.add(eventTmp);
				}

				return epgEventList;
			}

			Log.i(TAG, "Enter -->getEPGPFEvent() tmpList== null");
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getEPGSchelueEvent
	 */
	public List<DTVEPGEvent> getEPGSchelueEvent(int channelIndex) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getEPGSchelueEvent() channelIndex== " + channelIndex);

		try {
			List<InterEPGEvent> tmpList = mIDtvInterface.getEPGSchelueEvent(channelIndex);
			if (tmpList != null) {
				List<DTVEPGEvent> epgEventList = new ArrayList<DTVEPGEvent>();
				for (Iterator<InterEPGEvent> i = tmpList.iterator(); i.hasNext(); ) {
					InterEPGEvent interInfo = i.next();
					if (interInfo == null) {
						continue;
					}
					DTVEPGEvent eventTmp = new DTVEPGEvent(interInfo);
					epgEventList.add(eventTmp);
				}

				return epgEventList;
			}

			Log.i(TAG, "Enter -->getEPGSchelueEvent() tmpList== null");
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getSchelueEventWithStamp
	 */
	public List<DTVEPGEvent> getSchelueEventWithStamp(int channelIndex, DTVUTCDate date, DTVUTCTime time) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getSchelueEventWithStamp() channelIndex== " + channelIndex);

		try {
			InterVersionInfo version = mIDtvInterface.getDTVSwVersion();
			if ((version == null) ||
					((version.mi3rdAPIMainVersion <= 1) && (version.mi3rdAPISubVersion < 5))) {//�ӿڰ汾С��1.5����service��֧�ָýӿ�
				return null;
			}


			InterUTCDate datetmp = new InterUTCDate();
			InterUTCTime timetmp = new InterUTCTime();

			List<InterEPGEvent> tmpList = mIDtvInterface.getSchelueEventWithStamp(channelIndex, datetmp, timetmp);
			if (tmpList != null) {
				List<DTVEPGEvent> epgEventList = new ArrayList<DTVEPGEvent>();
				for (Iterator<InterEPGEvent> i = tmpList.iterator(); i.hasNext(); ) {
					InterEPGEvent interInfo = i.next();
					if (interInfo == null) {
						continue;
					}
					DTVEPGEvent eventTmp = new DTVEPGEvent(interInfo);
					epgEventList.add(eventTmp);
				}
				date.miYear = datetmp.miYear;
				date.miMonth = datetmp.miMonth;
				date.miDay = datetmp.miDay;
				date.miWeekDay = datetmp.miWeekDay;
				time.miHour = timetmp.miHour;
				time.miMinute = timetmp.miMinute;
				time.miSecond = timetmp.miSecond;

				return epgEventList;
			}

			Log.i(TAG, "Enter -->getEPGSchelueEvent() tmpList== null");
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}


	/**
	 * getEPGPFEventExtendInfo
	 */
	public String getEPGPFEventExtendInfo(int channelIndex, int eventID) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getSchelueEventWithStamp() channelIndex== " + channelIndex + " eventID:" + eventID);

		try {
			return mIDtvInterface.getEPGPFEventExtendInfo(channelIndex, eventID);
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getEPGSchelueEventExtendInfo
	 */
	public String getEPGSchelueEventExtendInfo(int channelIndex, int eventID) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getEPGSchelueEventExtendInfo() channelIndex== " + channelIndex + " eventID:" + eventID);

		try {
			return mIDtvInterface.getEPGSchelueEventExtendInfo(channelIndex, eventID);
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}


	/**
	 * registerStatusListener
	 */
	public int registerStatusListener(IPlayerStatusListener Ilistener) {

		callbackFunction = Ilistener;

		return 0;
	}

	/**
	 * unRegisterStatusListener
	 */
	public int unRegisterStatusListener(IPlayerStatusListener Ilistener) {
		Log.i(TAG, "[unRegisterStatusListener]" + Ilistener);
		if (callbackFunction == Ilistener) {
			callbackFunction = null;
			return 0;
		}
		return 1;
	}

	/**
	 * getInstance
	 */
	public static synchronized DTVM getInstance(Context context) {
		Log.i(TAG, "[get DTVM Instance]" + instance);
		if (instance != null) {
			return instance;
		}

		instance = new DTVM(context);
		return instance;
	}

	/**
	 * isDTVBusy
	 */
	public boolean isDTVBusy() {
		if (!checkServiceOK()) {
			return false;
		}

		try {
			return mIDtvInterface.isDTVBusy();
		} catch (RemoteException exception) {
			exception.printStackTrace();
			Log.e(TAG, "isDTVBusy--------> exception");
		}

		return false;
	}

	/**
	 * getDTVUtcTime
	 */
	public int getDTVUtcTime() {
		if (!checkServiceOK()) {
			Log.i(TAG, "getDTVUtcTime return 0");
			return 0;
		}

		try {
			Log.i(TAG, "[mIDtvInterface.getDTVTime()]" +
					mIDtvInterface.getDTVTime());
			return mIDtvInterface.getDTVTime();
		} catch (RemoteException exception) {
			Log.i(TAG, "[getDTVUtcTime exception]" + exception.getMessage());
			exception.printStackTrace();
		}

		return 0;
	}

	/**
	 * getCardStatus
	 */
	public CardStatus getCardStatus() {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			InterCardStatus interStatus = mIDtvInterface.getCardStatus();
			if (interStatus != null) {
				CardStatus status = new CardStatus();
				status.miCardStatus = interStatus.miCardStatus;
				status.miCardType = interStatus.miCardType;
				status.mstrCardID = interStatus.mstrCardID;
				return status;
			}

			Log.i(TAG, "Enter -->getCardStatus() interStatus== null");
		} catch (RemoteException exception) {
			Log.e(TAG, "getCardStatus--------> exception");
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getOperator
	 */
	public Operator getOperator() {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			InterOperator interOprator = mIDtvInterface.getOperator();
			if (interOprator != null) {
				Operator oprator = new Operator();
				oprator.miOperatorCode = interOprator.miOperatorCode;
				oprator.mstrOperatorName = interOprator.mstrOperatorName;
				return oprator;
			}

			Log.i(TAG, "Enter -->getOperator() interOprator== null");
		} catch (RemoteException exception) {
			Log.e(TAG, "getOperator--------> exception");
			exception.printStackTrace();
		}

		return null;
	}


	/**
	 * getDTVSwVersion
	 */
	public DTVVersionInfo getDTVSwVersion() {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			InterVersionInfo version;
			version = mIDtvInterface.getDTVSwVersion();

			DTVVersionInfo dtvVersion = new DTVVersionInfo();
			dtvVersion.miHardwareVersion = version.miHardwareVersion;
			dtvVersion.miOpVersion = version.miOpVersion;
			dtvVersion.miMainVersion = version.miMainVersion;
			dtvVersion.miSubVersion = version.miAPISubVersion;
			dtvVersion.miAPIMainVersion = version.mi3rdAPIMainVersion;
			dtvVersion.miAPISubVersion = version.mi3rdAPISubVersion;
			dtvVersion.mstrReleaseDateTime = version.mstrReleaseDateTime;

			return dtvVersion;
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getDTVLastChannel
	 */
	public int getDTVLastChannel() {
		if (!checkServiceOK()) {
			return -1;
		}

		try {
			return mIDtvInterface.getDTVLastChannel();
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return -1;
	}

	/**
	 * getDTVCurrentChannel
	 */
	public int getDTVCurrentChannel() {
		if (!checkServiceOK()) {
			return -1;
		}

		try {
			return mIDtvInterface.getDTVCurrentChannel();
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return -1;
	}

	/**
	 * getChannelInfo
	 */
	public DTVChannelInfo getChannelInfo(int channelIndex) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getChannelInfo() channelIndex== " + channelIndex);

		try {
			InterVersionInfo version = mIDtvInterface.getDTVSwVersion();
			if ((version == null) ||
					((version.mi3rdAPIMainVersion <= 1) && (version.mi3rdAPISubVersion < 1))) {//�ӿڰ汾С��1.1����service��֧�ָýӿ�
				Log.i(TAG, "Enter -->getChannelInfo() version== null");

				return null;
			}

			InterChannelInfo interInfo = mIDtvInterface.getChannelInfo(channelIndex);
			if (interInfo == null) {
				Log.i(TAG, "Enter -->getChannelInfo() interInfo== null");

				return null;
			}
			DTVChannelInfo channelInfo = new DTVChannelInfo(interInfo);
			return channelInfo;
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getChannelDetailInfo
	 */
	public DTVChannelDetailInfo getChannelDetailInfo(int channelIndex) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getChannelDetailInfo() channelIndex== " + channelIndex);

		try {
			InterVersionInfo version = mIDtvInterface.getDTVSwVersion();
			if ((version == null) ||
					((version.mi3rdAPIMainVersion <= 1) && (version.mi3rdAPISubVersion < 5))) {//�ӿڰ汾С��1.5����service��֧�ָýӿ�
				Log.i(TAG, "Enter -->getChannelDetailInfo() version== null");

				return null;
			}

			InterChDetailInfo interInfo = mIDtvInterface.getChannelDetailInfo(channelIndex);
			if (interInfo == null) {
				Log.i(TAG, "Enter -->getChannelDetailInfo() interInfo== null");

				return null;
			}
			DTVChannelDetailInfo channelInfo = new DTVChannelDetailInfo(interInfo);
			return channelInfo;
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getChannelBaseInfo
	 */
	public DTVChannelBaseInfo getChannelBaseInfo(int channelIndex) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getChannelBaseInfo() channelIndex== " + channelIndex);

		try {
			InterVersionInfo version = mIDtvInterface.getDTVSwVersion();
			if ((version == null) ||
					((version.mi3rdAPIMainVersion <= 1) && (version.mi3rdAPISubVersion < 5))) {//�ӿڰ汾С��1.5����service��֧�ָýӿ�
				Log.i(TAG, "Enter -->getChannelBaseInfo() version== null");

				return null;
			}

			InterDTVChannelBaseInfo interInfo = mIDtvInterface.getChannelBaseInfo(channelIndex);
			if (interInfo == null) {
				Log.i(TAG, "Enter -->getChannelBaseInfo() interInfo== null");

				return null;
			}
			DTVChannelBaseInfo channelInfo = new DTVChannelBaseInfo(interInfo);
			return channelInfo;
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * getChannelListByTpye
	 */
	public List<DTVChannelBaseInfo> getChannelListByTpye(int iServiceType) {
		if (!checkServiceOK()) {
			return null;
		}

		Log.i(TAG, "Enter -->getChannelListByTpye() iServiceType== " + iServiceType);

		try {
			InterVersionInfo version = mIDtvInterface.getDTVSwVersion();
			if ((version == null) ||
					((version.mi3rdAPIMainVersion <= 1) && (version.mi3rdAPISubVersion < 5))) {//�ӿڰ汾С��1.5����service��֧�ָýӿ�
				return null;
			}

			List<DTVChannelBaseInfo> channelList = new ArrayList<DTVChannelBaseInfo>();
			List<InterDTVChannelBaseInfo> tmpList;

			tmpList = mIDtvInterface.getChannelListByTpye(iServiceType);
			if ((tmpList == null) || (tmpList.size() == 0)) {
				Log.i(TAG, "Enter -->getChannelListByTpye() tmpList== null");

				return null;
			}

			InterDTVChannelBaseInfo tmpInfo;
			for (Iterator<InterDTVChannelBaseInfo> i = tmpList.iterator(); i.hasNext(); ) {
				tmpInfo = i.next();
				if (tmpInfo == null) {
					continue;
				}
				channelList.add(new DTVChannelBaseInfo(tmpInfo));
			}

			return channelList;
		} catch (RemoteException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * SystemReset
	 */
	public int SystemReset(int type) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDtvInterface.SystemReset(type);
		} catch (RemoteException exception) {
			Log.e(TAG, "SystemReset--------> exception");
			exception.printStackTrace();
			return 1;
		}
	}

	/**
	 * getSpecSourceList
	 */
	public List<DTVSpecSource> getSpecSourceList() {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			List<InterDTVSource> sourceList = mIDtvInterface.getDTVSourceList();
			if ((sourceList == null) || (sourceList.size() <= 0)) {
				return null;
			}

			InterDTVSource tmpsource = null;
			DTVSpecSource specSource = null;

			List<DTVSpecSource> tmpSourceList = new ArrayList<DTVSpecSource>();
			for (Iterator<InterDTVSource> i = sourceList.iterator(); i.hasNext(); ) {
				tmpsource = i.next();
				if (tmpsource == null) {
					continue;
				}
				specSource = new DTVSpecSource();
				specSource.miSourceType = tmpsource.miSourceType;
				specSource.miSourceID = tmpsource.miSourceID;
				specSource.miSourceName = tmpsource.miSourceName;
				tmpSourceList.add(specSource);
			}
			return tmpSourceList;
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * requestSpecResource
	 */
	public int requestSpecResource(int type, int id) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDtvInterface.requestResource(type, id);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return 1;
		}
	}

	/**
	 * releaseSpecResource
	 */
	public int releaseSpecResource(int type, int id) {
		if (!checkServiceOK()) {
			return 1;
		}
		try {
			return mIDtvInterface.releaseResource(type, id);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return 1;
		}
	}

	/**
	 * TVOS+getTimerInfo
	 *
	 * @param start
	 * @param end
	 * @param original
	 * @param sourceID
	 * @param serviceIndex
	 * @param url
	 * @param programNum
	 * @param programName
	 * @param eventName
	 * @return
	 */
	public InterTimerInfo getTimerInfo(Date start, Date end,
									   int original,
									   int sourceID, int serviceIndex, String url,
									   int programNum, String programName, String eventName) {
		if (!checkServiceOK()) {
			return null;
		}
		try {
			long lstart = start.getTime();
			long lend = end.getTime();
			Log.i(TAG, "[dstart]" + lstart + "[dend]" + lend + "[original]" + original);
			return mIDtvInterface.getTimerInfo(lstart, lend, original, sourceID,
					serviceIndex, url, programNum, programName, eventName);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * TVOS+getTimerInfo
	 *
	 * @param start
	 * @param end
	 * @param original
	 * @param sourceID
	 * @param serviceIndex
	 * @param url
	 * @param programNum
	 * @param programName
	 * @param eventName
	 * @return
	 */
	public InterTimerInfo getTimerInfo(long start, long end,
									   int original,
									   int sourceID, int serviceIndex, String url,
									   int programNum, String programName, String eventName) {
		if (!checkServiceOK()) {
			return null;
		}
		try {
			Log.i(TAG, "[lstart]" + start + "[lend]" + end + "[original]" + original);
			return mIDtvInterface.getTimerInfo(start, end, original, sourceID,
					serviceIndex, url, programNum, programName, eventName);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return null;
		}
	}


	/**
	 * addTimer
	 */
	public int addTimer(InterTimerInfo timer) {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
		try {
			return mIDtvInterface.addTimer(timer);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * deleteTimer
	 */
	public int deleteTimer(InterTimerInfo timer) {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			return mIDtvInterface.deleteTimer(timer);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * getTimerList
	 */
	public List<InterTimerInfo> getTimerList(int type) {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			return mIDtvInterface.getTimerList(type);
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * deleteAllTimer
	 */
	public int deleteAllTimer() {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			return mIDtvInterface.deleteAllTimer();
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * getCurSourceID
	 */
	public int getCurSourceID() {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			return mIDtvInterface.getCurSourceID();
		} catch (RemoteException exception) {
			exception.printStackTrace();
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}

	}
//////////////////////////////////////////////////////////////
//public int getDTVProductType()
//{
//if(!checkServiceOK()){
//					return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
//				}
//				
//				try{
//					return mIDtvInterface.getDTVProductType();
//				}
//				catch (RemoteException exception){
//					exception.printStackTrace();
//					return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
//				}
//}

	public int setSourceByID(int iSourceID) throws RemoteException {
		if (!checkServiceOK()) {
			return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
		}
		return setSourceByID(iSourceID);
	}

/////////////////////////////////////////////////////////////////
/*public List<Operator> getOPListBySourceID(int sourceID) 
				{
  if(!checkServiceOK()){
		return null;
				}
////////////////////////////////////////////////////////////////

	try{
		// InterOperator interOprator = mIDtvInterface.getOperator();
		
		List<InterOperator> OpList = mIDtvInterface.getOPListBySourceID(sourceID);
		
		if(OpList.size() == 0)
				{
			
		}
		
		 List<InterOperator> operatorlist = mIDtvInterface.getOPListBySourceID(sourceID);		
			
			if((operatorlist == null) || (operatorlist.length <= 0))	{
				return null;
			}			
			InterOperator tmpOperator = null;			
			List<InterOperator> OpList = new ArrayList<InterOperator>();
			for(int i = 0; i < operatorlist.length; i ++){		//				InterOperator interOperator = new InterOperator();
//				interOperator.miOperatorCode = operatorlist[i].miOperatorCode;
//				interOperator.mstrOperatorName = operatorlist[i].mstrOperatorName;
				tmpOperator = new InterOperator(operatorlist[i].mstrOperatorName,
						operatorlist[i].miOperatorCode);				
				OpList.add(tmpOperator);					
			}
			return OpList;	
		}
	 	catch (RemoteException exception){
	 		return null;
	 	}
///////////////////////////////////////////////////////////////////
*/

/*


  
  try{
        		 InterOperator interOprator = mIDtvInterface.getOperator();
        		 if(interOprator != null){
        			 Operator oprator = new Operator();
        			 oprator.miOperatorCode = interOprator.miOperatorCode;
        			 oprator.mstrOperatorName = interOprator.mstrOperatorName;
        			 return oprator;
        		 }
        		 
        		 Log.i(TAG, "Enter -->getOperator() interOprator== null");
  			}
  		 	catch (RemoteException exception){
  		 		Log.e(TAG, "getOperator--------> exception");
					exception.printStackTrace();
  		 	}
  			
  			return null;
  			
    
}
*/
/*

 public Operator getOperator(){
  		    if(!checkServiceOK()){
 		    	return null;
 			}
  		    
        	 try{
        		 InterOperator interOprator = mIDtvInterface.getOperator();
        		 if(interOprator != null){
        			 Operator oprator = new Operator();
        			 oprator.miOperatorCode = interOprator.miOperatorCode;
        			 oprator.mstrOperatorName = interOprator.mstrOperatorName;
        			 return oprator;
        		 }
        		 
        		 Log.i(TAG, "Enter -->getOperator() interOprator== null");
  			}
  		 	catch (RemoteException exception){
  		 		Log.e(TAG, "getOperator--------> exception");
  		 		exception.printStackTrace();
  		 	}
  			
  			return null;
         }

*/

//////////////////////////////////////////////////////////////////


//public List<InterOperator> getOPListBySourceID(int sourceID) {
//	if(!checkServiceOK()){
//		return null;
//	}
//	
//	try{
//		return mIDtvInterface.getOPListBySourceID(sourceID);
//		//return mIDtvInterface.getTimerList(type);
//	}
//	catch (RemoteException exception){
//		exception.printStackTrace();
//		return null;
//	}
//}


//public int setOperator(int iOperatorCode)
//    {
//    if(!checkServiceOK()){
// 		    	
//					return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
//				}
//				
//        	 try{        		
//        		 return mIDtvInterface.setOperator(iOperatorCode);
//        		 
//        		// Log.i(TAG, "Enter -->getOperatorlist() interOprator!= null");
//  			}
//  		 	catch (RemoteException exception){
//  		 		Log.e(TAG, "getOperator--------> exception");
//  		 		exception.printStackTrace();
//  		 	}
//  			
//  		//	return null;
//  		return DTVConstant.ErrorCode.ERROR_BINDER_FAILD;
//			}


////////////////////////////////////////////////////////////////


	/**
	 * TV Service
	 **/
	public static final String DTVSERVICE = "com.changhong.tvos.dtv.service.DTVService";


	//lsy add for cloud service(1/6)
	public int LoadOrSavedDb(String pathname, int isSave) {
		try {
			return mIDtvInterface.LoadOrSavedDb(pathname, isSave);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;

	}

	//lsy add for cloud service(2/6)
	public int getDBVersion() {
		try {
			Log.i(TAG, "getDBVersion() start");
			return mIDtvInterface.getDBVersion();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	//lsy add for cloud service(3/6)
	public int getChannelListVersion() {
		try {
			return mIDtvInterface.getChannelListVersion();
		} catch (RemoteException e) {

		}
		return -1;
	}

	//lsy add for cloud service(4/6)
	public List<Operator> getOPListByCity(String province, String city) {
		Log.i(TAG, "DTVM getOPListByCity start_prov:" + province + ";city:" + city);
		if (!checkServiceOK()) {
			Log.i(TAG, "DTVM getOPListByCity null");
			return null;
		}
		try {
			Log.i(TAG, "DTVM getOPListByCity mIDtvInterface");
			List<InterOperator> interOprator = mIDtvInterface.getOPListByCity(province, city);
			List<Operator> oplist = new ArrayList<Operator>();
			for (int i = 0; i < interOprator.size(); i++) {
				Operator op = new Operator();
				op.mstrOperatorName = interOprator.get(i).mstrOperatorName;
				op.miOperatorCode = interOprator.get(i).miOperatorCode;
				oplist.add(op);
			}
			return oplist;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	//lsy add for cloud service(5/6)
	public int setOP(int iOperatorCode) {
		try {
			return mIDtvInterface.setOP(iOperatorCode);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}


	//lsy add for cloud service(6/6)
	//action="com.changhong.tvos.cloud.download"
	public void sendDownBroadCast(String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra("dbdown", "begindown");
		mContext.sendBroadcast(intent);
	}


	public int getPVRStatus() {
		try {
			Log.i(TAG, "[getPVRStatus]");
			return mIDtvInterface.getPVRStatus();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int PVR_REC_START(int hID, int ChannelID, String url, String ChannelName,
							 long sTime, long eTime) {
		try {
			return mIDtvInterface.PVR_REC_START(hID, ChannelID, url, ChannelName, sTime, eTime);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int PVR_REC_STOP(int ChannelID) {
		try {
			return mIDtvInterface.PVR_REC_STOP(ChannelID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int PVR_REC_STOP() {
		try {
			return mIDtvInterface.PVRSTOP();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int getBaseLength() {
		try {
			return mIDtvInterface.getLength();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public ParcelFileDescriptor getFileDescriptor() {
		try {
			return mIDtvInterface.getFileDescriptor();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void refreshRecBuffer(int ChannelID) {
		try {
			mIDtvInterface.refreshRecBuffer(ChannelID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}