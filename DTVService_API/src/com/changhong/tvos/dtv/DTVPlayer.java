/**
 * @filename 
 * 	DTV ��ع��ܽӿڷ�װ
 * @author:
 * @date: 
 * @version 0.1
 * history:
 * 	2012-7-17 ����getSmartCardStatus�ӿ� 
 */
package com.changhong.tvos.dtv;

import java.util.UUID;
import com.changhong.tvos.dtv.service.IDTVPlayer;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.vo.AudioTrack;
import com.changhong.tvos.dtv.vo.CICAMInformation;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstVideoLayerType;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.DTVVideoInfo;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DTVConstant.ErrorCode;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.EPGEvent;
import com.changhong.tvos.dtv.vo.NvodRefEvent;
import com.changhong.tvos.dtv.vo.NvodRefService;
import com.changhong.tvos.dtv.vo.NvodShiftEvent;
import com.changhong.tvos.dtv.vo.DTVCardStatus;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

/**
 *	DTV ���ܽӿڷ�װ.<br>
 *	��������Ҫ�ǶԽӿڵķ�װ
 */
public class DTVPlayer {

	final static String TAG = "DTVAPI.DTVPlayer";

	private UUID mUuid;

	private IDTVService mDTVServer;
	private IDTVPlayer mIDTVPlayer;
	private IBinder mDtvPlayerBinder;
	private int miTunerID;

	public static int DtvUIActivityState = 0;

	/** �μ�{@link DTVConstant#ConstVideoLayerType ConstVideoLayerType}*/
	private int miLayerType;
	private int miLayerIndex;

	public Play play = new Play();

	/** NVOD���ƽӿ� **/
	public Nvod nvod = new Nvod();
	/** epg���ƽӿ� **/
	public Epg epg = new Epg();
	/** �������ƽӿ� **/
	public Scan scan = new Scan();
	/** �����������ƽӿ� **/
	public DFA dfa = new DFA();
	/** CI/CAģ����ƽӿ� **/
	public Cicam cicam = new Cicam();
	/** ����������ƽӿ�**/
	public StartControl startControl = new StartControl();

	private int hasPrepare = 0;

	private boolean checkServiceOK() {
		Log.i(TAG, "[checkServiceOK]");
		//service�Ѿ���������Ҫ��������
		if ((mDtvPlayerBinder != null) && (!mDtvPlayerBinder.isBinderAlive())) {
			mDTVServer = null;
			mDtvPlayerBinder = null;
			mIDTVPlayer = null;
			Log.e(TAG, "service bind relinked");
		}

		if (mDTVServer == null) {
			IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
			if (bind != null) {
				mDTVServer = IDTVService.Stub.asInterface(bind);
				if (hasPrepare == 1) {
					prepare();
					hasPrepare = 0;
				}
			} else {
				Log.e(TAG, "service bind failed");
				return false;
			}
		}

		if (mIDTVPlayer == null) {
			Log.i(TAG, "mIDTVPlayer not preapare,mIDTVPlayer is null");
			//this.DtvUIActivityState  = 5;
			return false;
		} else {
			this.DtvUIActivityState = 0;
			Log.i(TAG, "mIDTVPlayer is not null");
		}

		return true;
	}

	/** 
	 * ���캯�� .<br>
	*/
	public DTVPlayer(Context context) {
		mUuid = UUID.randomUUID();
		miTunerID = 0;
		miLayerType = ConstVideoLayerType.VIDEO_LAYER;
		miLayerIndex = 0;

		IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
		if (bind != null) {
			mDTVServer = IDTVService.Stub.asInterface(bind);
		} else {
			Log.e(TAG, "service bind filed");
		}
	}

	/**
	 * ���캯��<br>
	 * @param iTunerID ��playerҪʹ�õ�tuner���
	 * @param eLayerType ��player���Ҫʹ�õ�����Ƶ�㻹��surface,�μ�{@link DTVConstant#ConstVideoLayerType ConstVideoLayerType}
	 * @param iIndex ��player������ʹ�õ�����Ƶ�㣬��ò�����ʾʹ�õ��ǵڼ��㣬���ֻ֧��һ�㣬��Ϊ0
	 * @param context Ӧ�õ�������
	 * @exception û���쳣�׳�
	 */
	public DTVPlayer(int iTunerID, int iLayerType, int iIndex, Context context) {
		mUuid = UUID.randomUUID();

		miTunerID = iTunerID;
		miLayerType = iLayerType;
		miLayerIndex = iIndex;

		IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
		if (bind != null) {
			mDTVServer = IDTVService.Stub.asInterface(bind);
		} else {
			Log.e(TAG, "service bind filed");
		}
	}

	public class Nvod {
		/**
		 * ��ʼ���ݽ���
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int start() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.nvodstart();
			} catch (RemoteException ex) {
				ex.printStackTrace();
				Log.e(TAG, "nvodstart --->exception");

				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * �������ݽ���
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int stop() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.nvodstop();
			} catch (RemoteException ex) {
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ͣ���ݽ���.<br>
		 * �ͷŹ�������������ɾ������
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int suspend() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.nvodsuspend();
			} catch (RemoteException ex) {
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * �������ݽ���.<br>
		 * ��suspend���ʹ��
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int resume() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.nvodresume();
			} catch (RemoteException ex) {
				Log.e(TAG, "nvodresume --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ȡ�ο�ҵ���б�
		 *		 
		 * @return null:ʧ�� ����:�ο�ҵ���б�
		 * @exception û���쳣�׳�
		 */
		public NvodRefService[] getRefServices() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getRefServices();
			} catch (RemoteException ex) {
				Log.e(TAG, "nvod getRefServices --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ��ȡ�ο��¼��б�
		 * @param serviceId
		 * 		ҵ��ID	 
		 * @return null:ʧ�� ����:�ο��¼��б�
		 * @exception û���쳣�׳�
		 */
		public NvodRefEvent[] getRefEvents(int serviceId) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getRefEvents(serviceId);
			} catch (RemoteException ex) {
				Log.e(TAG, "nvod getRefEvents --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ��ȡʱ���¼��б�
		 * @param serviceId
		 * 		ҵ��ID	 
		 * @param refEventId
		 * 		�ο��¼�ID	 
		 * @return null:ʧ�� ����:ʱ���¼��б�
		 * @exception û���쳣�׳�
		 */
		public NvodShiftEvent[] getShiftEvents(int serviceId, int refEventId) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getShiftEvents(serviceId, refEventId);
			} catch (RemoteException ex) {
				Log.e(TAG, "nvod getShiftEvents --->exception");
				ex.printStackTrace();
				return null;
			}
		}

	}

	/**
	 * ����EPG���ݽ��տ��ƽӿ�
	 */
	public class Epg {
		/**
		 * ��ʼ���ݽ���
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int start() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.epgStart();
			} catch (RemoteException ex) {
				Log.e(TAG, "epg Start --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * �������ݽ���
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int stop() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.epgStop();
			} catch (RemoteException ex) {
				Log.e(TAG, "epg stop --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ͣ���ݽ���.<br>
		 * �ͷŹ�������������ɾ��EPG����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int suspend() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.epgSuspend();
			} catch (RemoteException ex) {
				Log.e(TAG, "epg suspend --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * �������ݽ���.<br>
		 * ��suspend���ʹ��
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int resume() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.epgResume();
			} catch (RemoteException ex) {
				Log.e(TAG, "epg resume --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ȡ��Ŀ�ĵ�ǰ����¼�
		 * 
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @return null:ʧ�� ����:ָ����Ŀ��PF�¼�
		 * @exception û���쳣�׳�
		 */
		public EPGEvent[] getPFEvent(int iChannelIndex) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getPFEvent(iChannelIndex);
			} catch (RemoteException ex) {
				Log.e(TAG, "epg getPFEvent --->exception");
				ex.printStackTrace();

				return null;
			}
		}

		/**
		 * ��ȡ��Ŀ���ܱ���Ϣ
		 * 
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @return null:ʧ�� ����:ָ����Ŀ���ܱ��¼�
		 * @exception û���쳣�׳�
		 */
		public EPGEvent[] getSchelueEvent(int iChannelIndex) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getSchelueEvent(iChannelIndex);
			} catch (RemoteException ex) {
				Log.e(TAG, "epg getSchelueEvent --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ��ȡ��Ŀ���ܱ�ָ��ʱ��ε��ܱ���Ϣ<br>
		 * ˵�����Դ˺��������Ϊ�õ�event�Ŀ�ʼʱ����UI������ʼ�ͽ���ʱ��֮���events����
		 * 
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @param startTime
		 *            ��ʼʱ��
		 * @param endTime
		 *            ����ʱ��
		 * @return null:ʧ�� ����:ָ����Ŀ��ָ��ʱ��ε��ܱ��¼�
		 * @exception û���쳣�׳�
		 */
		public EPGEvent[] getSchelueEventByTime(int channelIndex, DTVDTTime startTime, DTVDTTime endTime) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getSchelueEventByTime(channelIndex, startTime, endTime);
			} catch (RemoteException ex) {
				Log.e(TAG, "epg getSchelueEventByTime --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ��ȡ��Ŀ��PF�¼�����չ��Ϣ
		 * 
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @param iEventID
		 *            �¼����, �μ�{@link vo.EPGEvent#miEventID EPGEvent.miEventID}
		 * @return NULL:���¼�����չ��Ϣ<br>
		 * ����ֵ�� ���¼�����չ����
		 * @exception û���쳣�׳�
		 */
		public String getPFEventExtendInfo(int iChannelIndex, int iEventID) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getPFEventExtendInfo(iChannelIndex, iEventID);
			} catch (RemoteException ex) {
				Log.e(TAG, "epg getPFEventExtendInfo --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ��ȡ��Ŀ��Schelue�¼�����չ��Ϣ
		 * 
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @param iEventID
		 *            �¼����, �μ�{@link vo.EPGEvent#miEventID EPGEvent.miEventID}
		 * @return NULL:���¼�����չ��Ϣ<br>
		 * ����ֵ�� ���¼�����չ����
		 * @exception û���쳣�׳�
		 */
		public String getSchelueEventExtendInfo(int iChannelIndex, int iEventID) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getSchelueEventExtendInfo(iChannelIndex, iEventID);
			} catch (RemoteException ex) {
				Log.e(TAG, "epg getSchelueEventExtendInfo --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ���ʱ���
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @param ʱ��
		 * @return
		 */
		public EPGEvent[] getSchelueEventWithStamp(int iChannelIndex, DTVDTTime time) {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getSchelueEventWithStamp(iChannelIndex, time);
			} catch (RemoteException ex) {
				Log.e(TAG, "epg getSchelueEventWithStamp --->exception");
				ex.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * ����DVB-C��DVB-T������Ŀʹ�õĽӿ���
	 */
	public class Scan {

		/**
		 * ��������.<br>
		 * ���Զ�����{@link vo.DTVConstant.ScanMode#SCAN_MODE_NIT SCAN_MODE_NIT}<br>
		 *  (1) ��freqListΪnull,<br>
		 * 		���OPIDΪ�ƶ���Ӫ�̣���DTV M/Wʹ��Ĭ�ϵ���Ӫ�̶�Ӧ��Ƶ�����NIT����<br>
		 * 		���OPIDΪͨ����Ӫ�̣���DTV M/Wʹ��ȫƵ��ɨƵ�ķ�ʽ��������<br>
		 *  (2) ��Ƶ����Ϣ����Ϊ1,��DTV MWʹ��UI�����ao_CtunerInfo����������Ƶ�ͽ���NIT����<br>
		 * ���ֶ�����{@link vo.DTVConstant.ScanMode#SCAN_MODE_MANUAL SCAN_MODE_MANUAL}ʱ��<br>
		 * 			freqListֻʹ�õ�1��Ƶ��<br>
		 * ���б�����{@link vo.DTVConstant.ScanMode#SCAN_MODE_LIST SCAN_MODE_LIST}ʱ��<br>
		 *  ��������̲������û��ֶ���������lFreqList�ɴ�null,�ɵײ������Ӫ��Ҫ���������<br>
		 * 
		 * @param iScanMode
		 *            ����ģʽ���μ�{@link vo.DTVConstant.ScanMode DTVConstant.ScanMode}
		 * @param lFreqList
		 *        Ƶ���б�,Ƶ����Ϣ��miIndex����Ҫ����<br>
		 *        �ھ�������ʱ�������������ʣ�ʵ����carrier����
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */

		public int setParam(int iScanMode, CarrierInfo[] lFreqList) {

			Log.i(TAG, "setParam(" + iScanMode + ")");

			if (!checkServiceOK()) {
				Log.e(TAG, "!checkServiceOK() err");
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			int isourceType;

			try {
				isourceType = mIDTVPlayer.getSourceType();
				if ((lFreqList != null) && (lFreqList[0].miDemodType != isourceType)) {
					Log.e(TAG, "ERROR_INVALID_PARAM err, <" + lFreqList[0].miDemodType + "," + isourceType + ">");
					return ErrorCode.ERROR_INVALID_PARAM;
				}
			} catch (RemoteException ex) {
				Log.e(TAG, "exception err");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				if (isourceType == ConstDemodType.DVB_C) {
					return mIDTVPlayer.SetDVBCParam(iScanMode, (DVBCCarrier[]) lFreqList);
				}
				if (isourceType == ConstDemodType.DVB_T) {
					return mIDTVPlayer.SetDVBTParam(iScanMode, (DVBTCarrier[]) lFreqList);
				}
				if (isourceType == ConstDemodType.DVB_S) {
					return mIDTVPlayer.SetDVBSParam(iScanMode, (DVBSTransponder[]) lFreqList);
				}
				if (isourceType == ConstDemodType.DMB_TH) {
					return mIDTVPlayer.SetDMBTHParam(iScanMode, (DMBTHCarrier[]) lFreqList);
				} else {
					Log.e(TAG, "Current not suport this source type");
					return ErrorCode.ERROR_NOT_SUPPORT;
				}

			} catch (RemoteException ex) {
				Log.e(TAG, "SCAN setParam --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ����Ҫ����������(DVB-Sʹ��).<br>
		 *   �������õ����Ǳ��������ȱ༭�����ǲ�������ͨ��{@link ChannelManager.SatelliteManager SatelliteManager}
		 *   ���浽���ݿ��е�����<br>
		 *   �ڵ�����������ǰ����Ҫ�ȵ��ñ��ӿ�ѡ�������
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int setSatellite(int iSatelliteID) {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.setSatellite(iSatelliteID);
			} catch (RemoteException ex) {
				Log.e(TAG, "SCAN setSatellite --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��������.<br>
		 *   ����������ǰ����Ҫ����setParam�������������ú�
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int start() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.scanStart();
			} catch (RemoteException ex) {
				Log.e(TAG, "SCAN start --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ֹͣ����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int stop() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.scanStop();
			} catch (RemoteException ex) {
				Log.e(TAG, "SCAN stop --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ȡ��(��ֹ)
		 * 
		 * @return 0:�ɹ�<br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		/**		public int cancel() {
					if(!checkServiceOK())
					{
						return ErrorCode.ERROR_BINDER_FAILD;
					}
					
					try
					{
						return mIDTVPlayer.scanCancel();
					}
					catch (RemoteException exception)
					{
						return ErrorCode.ERROR_BINDER_FAILD;
					}
				}
		**/
	}

	/**
	 * DTV������
	 */
	public class Play {

		/**
		 * ���û�̨ʱ��Ƶģʽ����֡/������
		 * 
		 * @param eSwitchMode �μ�{@link DTVConstant.ConstSwitchMode ConstSwitchMode}
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int setSwitchMode(int iSwitchMode) {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.setSwitchMode(iSwitchMode);
			} catch (RemoteException ex) {
				Log.e(TAG, "Play setSwitchMode --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ���õ�ǰ��Ŀ������
		 * 
		 * @param volume
		 * 		 ��������Χ0~64
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int setVolume(int volume) {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.setVolume(volume);
			} catch (RemoteException exception) {
				Log.e(TAG, "Play setVolume --->exception");

				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ȡ��ǰ��Ŀ����������<br>
		 * ������������Χ-10~-10;
		 * @return ��ǰ��Ŀ��ǰʹ�õ�������
		 * 		   <-10�� �������
		 * @exception û���쳣�׳�
		 */
		public int getVolume() {
			if (!checkServiceOK()) {
				return -100;
			}

			try {
				return mIDTVPlayer.getVolume();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play getVolume --->exception");
				ex.printStackTrace();
				return -100;
			}
		}

		/**
		 * ���Ž�Ŀ
		 * 
		 * @param iChannelIndex
		 *            ��Ŀ����, �μ�{@link vo.DTVChannelBaseInfo#miChannelIndex
		 *            DTVChannelBaseInfo.miChannelIndex}
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int play(int iChannelIndex) {
			if (!checkServiceOK()) {
				Log.i(TAG, "!checkServiceOK()");
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.play(iChannelIndex);
			} catch (RemoteException ex) {
				Log.e(TAG, "Play ");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ͨ������������Ž�Ŀ.<br>
		 *  �ýӿ���Ҫ����VOD������Ӧ�ó��ϲ��Ž�Ŀ������һ����ǰ�˷���<br>
		 *  ��ʹ�õĲ�����ֵ-1������ģʽ�����ʹ������㸳ֵ
		 * @param iFrequencyKd
		 *    	Ƶ��
		 * @param iSymbolRateK
		 *    	������
		 * @param eQamMode
		 *    	����ģʽ,�μ�{@link DTVConstant.ConstQAMMode ConstQAMMode}
		 * @param iServiceID
		 *    	����ID
		 * @param PMTPID
		 *    	PMT���PID
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int play(int iFrequencyK, int iSymbolRateK, int iQamMode, int iServiceID, int PMTPID) {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.playvod(iFrequencyK, iSymbolRateK, iQamMode, iServiceID, PMTPID);
			} catch (RemoteException ex) {
				Log.e(TAG, "Play 2");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ֹͣ����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int stop() {
			Log.i(TAG, "[stop]");
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.playStop();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play stop --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ͣ��Ƶ����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int Pausevideo() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.Pausevideo();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play Pausevideo --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ������Ƶ����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 **/
		public int Resumevideo() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.Resumevideo();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play Resumevideo --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ͣ��Ƶ����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int PauseAudio() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.PauseAudio();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play PauseAudio --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ������Ƶ����
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 **/
		public int ResumeAudio() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.ResumeAudio();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play ResumeAudio --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ȡ��Ŀ�������Ϣ����ǰ��Ŀ�İ�����Ϣ��
		 * 
		 * @return NULL:�ý�Ŀ�޶������??AudioTrack:�������??
		 * @exception û���쳣�׳�
		 */
		public AudioTrack getChannelAudioTrack() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getChannelAudioTrack();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play getChannelAudioTrack --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * �л����� (��ǰ��Ŀ)
		 * 
		 * @param iAudioTrack
		 *            ��Ҫ�л��İ�����Ϣ,�μ�{@link vo.AudioTrack#audioLanguagelist
		 *            AudioTrack.audioLanguagelist}
		 * @return 0:�ɹ�<br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int setChannelAudioTrack(int iAudioTrack) {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.setChannelAudioTrack(iAudioTrack);
			} catch (RemoteException ex) {
				Log.e(TAG, "Play setChannelAudioTrack --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ȡ��ǰ��Ŀ����Ƶ��Ϣ
		 * 
		 * @return DTVVideoInfo, ��ǰ��Ŀ����Ƶ��Ϣ
		 * @exception û���쳣�׳�
		 */
		public DTVVideoInfo getVideoInfo() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getVideoInfo();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play getVideoInfo --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ���video��ʾ����
		 * @return 0:�ɹ�<br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int clearVideoBuffer() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.clearVideoBuffer();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play clearVideoBuffer --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ȡ��ĿƵ����Ϣ����ǰ��Ŀ��Ƶ����Ϣ��
		 * 
		 * @return NULL:��ȡ�ý�ĿƵ����Ϣ�쳣??DVBCCarrier:��ȡ�ý�ĿƵ����Ϣ����??
		 * @exception û���쳣�׳�
		 */
		public DVBCCarrier getDVBCCurTunerInfo() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getDVBCCurTunerInfo();
			} catch (RemoteException ex) {
				Log.e(TAG, "Play getDVBCCurTunerInfo --->exception");
				ex.printStackTrace();
				return null;
			}
			//			return null;
		}
	}

	/**
	 * ����CI/CAM���ƽӿ�
	 */
	public class Cicam {

		/**
		 * ��ȡCAM��ģ����??
		 * 
		 * @return CAM��Ϣ
		 * @exception û���쳣�׳�
		 */
		public CICAMInformation getInfo() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getInfo();
			} catch (RemoteException ex) {
				ex.printStackTrace();
				Log.e(TAG, "CICAMInformation getInfo --->exception");

				return null;
			}
		}

		/**
		 * ��ȡ���ܿ����ţ�˵��������ֵ����Ϊ�޿������ܿ��Ż�������ڹ����˵�
		 * 
		 * @return null����ȡʧ��
		 * 		   ����ֵ������
		 * @exception û���쳣�׳�
		 */
		public String getSmartCardID() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				return mIDTVPlayer.getSmartCardID();
			} catch (RemoteException ex) {
				Log.e(TAG, "getSmartCardID --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ��ȡ��״̬.<br>
		 * @return ��״̬�����������ͺ�״̬��
		 * @exception û���쳣�׳�
		 */
		public DTVCardStatus getSmartCardStatus() {
			if (!checkServiceOK()) {
				return null;
			}

			try {
				Log.e(TAG, "getSmartCardStatus>>getCardStatus>>" + mIDTVPlayer.getCardStatus().miCardType + ", " + mIDTVPlayer.getCardStatus());
				return mIDTVPlayer.getCardStatus();
			} catch (RemoteException ex) {
				Log.e(TAG, "getSmartCardStatus --->exception");
				ex.printStackTrace();
				return null;
			}
		}

		/**
		 * ����û����ݣ������ʼ�����Ϣ��
		 * 
		 * @return 0:�ɹ� <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int clearUserData() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.clearUserData();
			} catch (RemoteException ex) {
				Log.e(TAG, "clearUserData --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		/**
		 * ��ѯ���ƽӿ�. <br>
		 * <p>
		 * ˵��<br>
		 * 1. ����CA,CI���˵�ʱ�����ã�<br>
		 * 	CH_DTV_CICA_QueryControl(CH_DTV_CICA_MsgType.MSG_USER_MENU, -1,<br>
		 * 	CH_DTV_CICA_Constant.MENU_ID_MAIN, <br>
		 * 	CH_DTV_CICA_Constant.DEFAULT_OP_CODE_CONFIRM,<br>
		 * 	-1, NULL); <br>
		 * 	2. ��ĳһ��CA,CI�˵������ϼ��˵���ʱ�����ã�<br>
		 * 	CH_DTV_CICA_QueryControl(CH_DTV_CICA_MsgType.MSG_USER_MENU, -1<br>
		 * 	��ǰ�˵�ID,-1,-1, CH_DTV_CICA_Constant.DEFAULT_OP_CODE_BACK,<br>
		 * 	-1, NULL);<br>
		 * 	3. ��ĳһ��CA,CI�˵�ֱ���˳��༶�˵��ص�CA,CI���˵�����һ���˵�ʱ�����ã�<br>
		 * 	CH_DTV_CICA_QueryControl(CH_DTV_CICA_MsgType.MSG_USER_MENU, -1<br>
		 * 	��ǰ�˵�ID,-1,-1,<br>
		 * 	CH_DTV_CICA_Constant.DEFAULT_OP_CODE_EXIT,<br>
		 * 	-1, NULL);<br>
		 * 	4. ��ѯ�ʼ��Ĺ��ܣ�ͨ�������ʼ�֪ͨ��Ϣ�����ID��CA/CIģ��ͨ�����Ͳ˵���Ϣ�ķ�ʽʵ��<br>
		 * <br>
		 * 
		 * @param iMsgType
		 *            ��Ϣ����,(��CAM���ͳ�����Ϣ�д�������)���μ�{@linkCICAMMessageBase#ConstMsgType
		 *            ConstCICAMsgType}
		 * @param iMsgID
		 *            ��Ϣ��ʶ(��CAM���ͳ�����Ϣ�д�������)���μ�{@linkCICAMMessageBase#miMsgID
		 *            miMsgID}
		 * @param iMenuID
		 *            �˵�ID,(��CAM���ͳ�����Ϣ�д�������)���μ�{@linkCICAMMenuBase#miMenuID
		 *            miMenuID}
		 * @param operand
		 *            �����������ͨ�˵��е�mastr_ContentList������
		 * @param opcode
		 *            �����룬�μ�{@link vo.DTVConstant.ConstCICAMOpCode ConstCICAMOpCode})
		 *            INVALID_OPERATE_CODE��ʾ��ʹ�ô˲���
		 * @param defOpcode
		 *            Ĭ�ϲ����룬�μ�{@link vo.DTVConstant.CICAMOpCode CICAMOpCode})<br>
		 *            �����ڲ˵��У���ȷ�ϡ����¼��˵��������ص��ϼ��˵������˳����в˵���<br>
		 *            INVALID_OPERATE_CODE��ʾ��ʹ�ô˲���
		 * @param inputItems
		 *            �������
		 * @param inputList
		 *            ���������б�
		 * @exception û���쳣�׳�
		 */
		public void queryControl(int iMsgType, int iMsgID, int iMenuID, int operand, int opcode, int defOpcode, int inputItems, String[] inputList) throws RemoteException {
			if (!checkServiceOK()) {
				return;
			}

			try {
				mIDTVPlayer.queryControl(iMsgType, iMsgID, iMenuID, operand, opcode, defOpcode, inputItems, inputList);
			} catch (RemoteException ex) {
				Log.e(TAG, "queryControl --->exception");
				ex.printStackTrace();
				return;
			}
		}
	}

	/**
	 * ����DFA�������ƽӿ�
	 */
	public class DFA {

		/**
		 * ��������.<br>
		 * ˵����ͨ��type��msgID��ȷ�����ƶ����Ƕ��Ǹ���Ϣ�ķ���
		 * 
		 * @param type
		 *        ��Ϣ����, (�μ�{@link vo.DfaMessageBase#ConstCICAMsgType DfaMessageBase.ConstCICAMsgType})
		 * @param iMsgID
		 *        ��Ϣ���(�μ�{@link vo.DfaMessageBase#miMsgID DfaMessageBase.miMsgID})
		 * @param iOperand
		 *            ��������������Ϣ������ѡ���б����??
		 * @param iOpcode
		 *            �����룬0��ȡ����1-Ϊȷ��
		 * @param strInputList
		 *            �������б�����·����Ϣ
		 * @return 0:�ɹ�; <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int DFAControl(int iType, int iMsgID, int iOperand, int iOpcode, String[] strInputList) {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.DFAControl(iType, iMsgID, iOperand, iOpcode, strInputList);
			} catch (RemoteException ex) {
				Log.e(TAG, "DFAControl --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}
	}

	/**
	 * ���ǿ���������ƽӿ�
	 */
	public class StartControl {

		/**
		 * ��ʼ�����������<br>
		 * @return 0:�ɹ�; <br>
		 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
		 * @exception û���쳣�׳�
		 */
		public int start() {
			if (!checkServiceOK()) {
				return ErrorCode.ERROR_BINDER_FAILD;
			}

			try {
				return mIDTVPlayer.start();
			} catch (RemoteException ex) {
				Log.e(TAG, "StartControl start --->exception");
				ex.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}
	}

	/**
	 * ���汾�Ƿ�仯<br>
	 * @return 0:�ɹ�; <br>
	 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
	 * @exception û���쳣�׳�
	 */
	public int checkVersion(int curVersion) {
		if (!checkServiceOK()) {
			return ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			return mIDTVPlayer.checkVersion(curVersion);
		} catch (RemoteException ex) {
			ex.printStackTrace();
			Log.e(TAG, "checkVersion --->exception");
			return ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * ��ȡ��⵽��TDTʱ��
	 * @return CHDTVDTTime, ��ǰ��Ŀ����Ƶ��
	 * @exception û���쳣�׳�
	 */
	public DTVDTTime getTDTTime() {
		//		if(!checkServiceOK())
		//		{
		//			return null;
		//		}

		if ((null == mIDTVPlayer) || (!mIDTVPlayer.asBinder().isBinderAlive())) {
			Log.e(TAG, "service bind failed");
			return null;
		}

		try {
			return mIDTVPlayer.getTDTTime();
		} catch (RemoteException exception) {
			Log.e(TAG, "getTDTTime --->exception");
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * ���õ�ǰ���Ž�Ŀ������ID
	 * @param nProgramID
	 * @return  �����ķ���ֵ�������õĽ�Ŀ����ֵ
	 * @throws RemoteException
	 */
	public int setPlayingProgramID(int nProgramID) throws RemoteException {
		if (!checkServiceOK()) {
			return ErrorCode.ERROR_BINDER_FAILD;
		}
		try {
			return mIDTVPlayer.setPlayingProgramID(nProgramID);
		} catch (RemoteException ex) {
			Log.e(TAG, "setPlayingProgramID --->exception");
			ex.printStackTrace();
			return -100;
		}
	}

	/**
	 * ׼����Դ.<br>
	 * ���ñ��ӿں��ײ����ʽ���������Դ������ʹ��ʱ���뼰ʱ����Release�����ͷ�
	 * �ڵ���player����ҪӲ����Դ�Ľӿ�ǰ�������ȵ��ñ��ӿڽ�����Դ����
	 * 
	 * @return 0:�ɹ� <br>
	 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
	 * @exception û���쳣�׳�
	 */
	public int prepare() {
		Log.i(TAG, "API prepare return 0000  DtvUIActivityState:" + DtvUIActivityState);
		if (DtvUIActivityState == 5) {
			Log.i(TAG, "API prepare return 1111  DtvUIActivityState:" + DtvUIActivityState);
			//	this.DtvUIActivityState = 0;
			return 0;
		}

		if (mDTVServer == null) {
			hasPrepare = 1;
			return ErrorCode.ERROR_BINDER_FAILD;
		}
		Log.i(TAG, "[mIDTVPlayer]" + mIDTVPlayer);
		if (mIDTVPlayer == null) {
			try {
				String uuid = mUuid.toString();
				Log.i(TAG, "dtv_uuid=" + uuid);
				mDtvPlayerBinder = mDTVServer.CreateDTVPlayer(uuid, miTunerID, miLayerType, miLayerIndex, 0);
				if (mDtvPlayerBinder != null) {
					mIDTVPlayer = IDTVPlayer.Stub.asInterface(mDtvPlayerBinder);
					Log.i(TAG, "[new DTVPlayer]" + mIDTVPlayer);
				} else {
					Log.e(TAG, "prepare --->mDtvPlayerBinder null");
					return ErrorCode.ERROR_FAILED;

				}
			} catch (RemoteException ex) {
				ex.printStackTrace();
				Log.e(TAG, "prepare --->exception");
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}
		if (mIDTVPlayer != null) {
			try {
				Log.i(TAG, "dtv prepare");
				return mIDTVPlayer.prepare();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "prepare --->exception2");
				e.printStackTrace();
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		}

		return ErrorCode.ERROR_FAILED;
	}

	/**
	 * �ͷ���Դ.<br>
	 * ���ӿ���prepare���ʹ��??
	 * ���ñ��ӿں������Դ�����ͷţ���Ҫ���µ���prepare����ܵ���player�ȿ��ƽӿ�
	 * 
	 * @return 0:�ɹ� <br>
	 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
	 * @exception û���쳣�׳�
	 */
	public int Release() {
		if (!checkServiceOK()) {
			return ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			//			Log.e("DTVPlayer", "Release");
			//			
			//			java.util.Map<Thread, StackTraceElement[]> ts = Thread.getAllStackTraces();  
			//			  StackTraceElement[] ste = ts.get(Thread.currentThread());  
			//			  for (StackTraceElement s : ste) {  
			//			  Log.e("SS     ", s.toString()); //�����android�Դ��ģ����û�У��������Ĵ�ӡ����һ��   
			//			}  
			Log.i(TAG, "dtv release resource");
			mIDTVPlayer.Release();
			Log.i(TAG, "dtv release a");
			mDTVServer.DestroyDTVPlayer(mDtvPlayerBinder);
			Log.i(TAG, "dtv release b");
			mDtvPlayerBinder = null;
			mIDTVPlayer = null;
			return DTVConstant.DTV_OK;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * ǿ�Ƶ�г��ĳ��Ƶ��
	 * 
	 * @param carrierInfo
	 *     Ƶ����Ϣ.<br>
	 *     �ýӿ�ֻʹ����CarrierInfo�е�Ƶ�������ص���Ϣ��Ƶ���ţ�TSID���ɹ��ܲ��ṩ�ĵ���Ϣ���Բ���
	 * @return 0:�ɹ�; <br>
	 * 		       С��0�� �����룺�μ�{@link DTVConstant.ErrorCode ErrorCode}
	 * @exception û���쳣�׳�
	 */
	public int SetTuner(CarrierInfo carrierInfo) {
		if (carrierInfo == null) {
			return ErrorCode.ERROR_BINDER_FAILD;
		}

		if (!checkServiceOK()) {
			return ErrorCode.ERROR_BINDER_FAILD;
		}

		try {
			if (carrierInfo.miDemodType == DTVConstant.ConstDemodType.DVB_C) {
				return mIDTVPlayer.SetDVBCTuner((DVBCCarrier) carrierInfo);
			} else if (carrierInfo.miDemodType == DTVConstant.ConstDemodType.DVB_S) {
				return mIDTVPlayer.SetDVBSTuner((DVBSTransponder) carrierInfo);
			} else if (carrierInfo.miDemodType == DTVConstant.ConstDemodType.DVB_T) {
				return mIDTVPlayer.SetDVBTTuner((DVBTCarrier) carrierInfo);
			} else if (carrierInfo.miDemodType == DTVConstant.ConstDemodType.DMB_TH) {
				return mIDTVPlayer.SetDMBTHTuner((DMBTHCarrier) carrierInfo);
			} else {
				return ErrorCode.ERROR_BINDER_FAILD;
			}
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return ErrorCode.ERROR_BINDER_FAILD;
		}
	}

	/**
	 * ��ȡTUNER״̬
	 * @return DTVTunerStatus, ��ǰtuner״̬
	 * @exception û���쳣�׳�
	 */
	public DTVTunerStatus getTunerStatus() {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			return mIDTVPlayer.getTunerStatus();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡ��ǰƵ����Ϣ
	 * @return CarrierInfo, ��ǰƵ����Ϣ
	 * @exception û���쳣�׳�
	 */
	public CarrierInfo getCurTunerInfo() {
		if (!checkServiceOK()) {
			return null;
		}
		Log.i(TAG, "checkServiceOK is not null");
		int isourceType;

		try {
			isourceType = mIDTVPlayer.getSourceType();
			Log.i(TAG, "isourceType->" + isourceType);
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return null;
		}

		try {
			if (isourceType == ConstDemodType.DVB_C) {
				return mIDTVPlayer.getDVBCCurTunerInfo();
			}
			if (isourceType == ConstDemodType.DVB_T) {
				return mIDTVPlayer.getDVBTCurTunerInfo();
			}
			if (isourceType == ConstDemodType.DVB_S) {
				return mIDTVPlayer.getDVBSCurTunerInfo();
			}
			if (isourceType == ConstDemodType.DMB_TH) {
				return mIDTVPlayer.getDMBTHCurTunerInfo();
			} else {
				Log.e(TAG, "Current not suport this source type");
				return null;
			}
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * ��ȡplayer��UUID.<br>
	 * UUIDΪ�����Ψһ��ʶ����������ֵ��Ҫ���ڷֱ�㲥�Ƿ��͸���player
	 * @return UUID
	 * @exception û���쳣�׳�
	 */
	public UUID getUuid() {
		return mUuid;
	}

	/**
	 * ��ȡplayer��״̬.<br>
	 * @return <br> 
	 * 	0�����ž���
	 *  1��û��������Դ��δ��prepare��
	 *  2: ������DTVӦ��ռ����Դ
	 *  3��������Դ��ռ��Դ
	 *  -1: ͨѶʧ��
	 * @exception û���쳣�׳�
	 */
	public int getPlayerStatus() {
		if (!checkServiceOK()) {
			return -1;
		}

		try {
			return mIDTVPlayer.getPlayerStatus();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}

	/**
	 * ���ò���Դ����ĿԴ��ID��
	 * 
	 * @return 0:??
	 * @exception û���쳣�׳�
	 */
	public int setSource(int iSourceID) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDTVPlayer.setSource(iSourceID);
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 1;
		}
	}

	/**
	 * ���ص�ǰ����Դ
	 * 
	 * @return 0:??
	 * @exception û���쳣�׳�
	 */
	public DTVSource getSource() {
		if (!checkServiceOK()) {
			return null;
		}

		try {
			return mIDTVPlayer.getSource();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * ���ص�ǰ��Ʒ����
	 * 
	 * @return ����0:??
	 * @exception û���쳣�׳�
	 */
	public int getProductType() {

		if (!checkServiceOK()) {
			return -1;
		}

		try {
			return mIDTVPlayer.getProductType();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * ��ʼ������̨
	 * 
	 * @return 0:??
	 * @exception û���쳣�׳�
	 */
	public int SmartSkip(int iType) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDTVPlayer.SmartSkip(iType);
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * ����DTV״̬ BUSY
	 * 
	 * @return 0:??
	 * @exception û���쳣�׳�
	 */
	public int SetDtvBusyState(int iType) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDTVPlayer.SetDtvBusyState(iType);
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	///////////////////////////////////////////////////////////////
	//  public int SetDtvUIActivityState(int iState) {
	public int SetDtvUIActivityState(int iState) throws RemoteException {
		//if(!checkServiceOK())
		{
			//	return 1;
		}

		//mIDTVPlayer.SetDtvUIActivityState(iState);
		Log.i(TAG, "SetDtvUIActivityState =" + iState);
		this.DtvUIActivityState = iState;
		return 0;
	}

	///////////////////////////////////////////////////////////////

	/**
	 * ֹͣ������̨
	 * 
	 * @return 0:??
	 * @exception û���쳣�׳�
	 */
	public int SmartSkipStop() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return mIDTVPlayer.SmartSkipStop();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	//////////////////////HOTEL MODE ///////////fy 2014-2-26//////
	public int setHotelMode(boolean bHotelMode) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelMode(bHotelMode);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public boolean getHotelMode() {
		if (!checkServiceOK()) {
			return false;
		}

		try {
			return mIDTVPlayer.getHotelMode();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			Log.e(TAG, "enterScreenSaver --->>exception");
			return false;
		}

	}

	public int setHotelMaxVolume(int iMaxVolume) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelMaxVolume(iMaxVolume);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int getHotelMaxVolume() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return (mIDTVPlayer.getHotelMaxVolume());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int getHotelPowerOnMode() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return (mIDTVPlayer.getHotelPowerOnMode());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int setHotelPowerOnMode(int iPowerOnMode) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelPowerOnMode(iPowerOnMode);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int getHotelPowerOnSource() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return (mIDTVPlayer.getHotelPowerOnSource());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int setHotelPowerOnSource(int iPowerOnSource) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelPowerOnSource(iPowerOnSource);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int getHotelPowerOnVolume() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return (mIDTVPlayer.getHotelPowerOnVolume());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int setHotelPowerOnVolume(int iPowerOnVolume) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelPowerOnVolume(iPowerOnVolume);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int getHotelPowerOnChannel() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return (mIDTVPlayer.getHotelPowerOnChannel());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int setHotelPowerOnChannel(int iPowerOnChannel) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelPowerOnChannel(iPowerOnChannel);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int getHotelMusicMode() {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			return (mIDTVPlayer.getHotelMusicMode());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int setHotelMusicMode(int iMusicMode) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelMusicMode(iMusicMode);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public boolean getHotelLocalKeyLockFlag() {
		if (!checkServiceOK()) {
			return false;
		}

		try {
			return mIDTVPlayer.getHotelLocalKeyLockFlag();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			Log.e(TAG, "enterScreenSaver --->>exception");
			return false;
		}

	}

	public int setHotelLocalKeyLockFlag(boolean bLocalKeyLockFlag) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelLocalKeyLockFlag(bLocalKeyLockFlag);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public boolean getHotelTuneLockFlag() {
		if (!checkServiceOK()) {
			return false;
		}

		try {
			return mIDTVPlayer.getHotelTuneLockFlag();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			Log.e(TAG, "enterScreenSaver --->>exception");
			return false;
		}

	}

	public int setHotelTuneLockFlag(boolean bTuneLockFlag) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelTuneLockFlag(bTuneLockFlag);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public boolean getHotelAutoPresetFlag() {
		if (!checkServiceOK()) {
			return false;
		}

		try {
			return mIDTVPlayer.getHotelAutoPresetFlag();
		} catch (RemoteException ex) {
			ex.printStackTrace();
			Log.e(TAG, "enterScreenSaver --->>exception");
			return false;
		}

	}

	public int setHotelAutoPresetFlag(boolean bAutoPresetFlag) {
		if (!checkServiceOK()) {
			return 1;
		}

		try {
			mIDTVPlayer.setHotelAutoPresetFlag(bAutoPresetFlag);
			return 1;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
}