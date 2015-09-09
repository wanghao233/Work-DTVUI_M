/**
 * @filename ��DTVServiceRm.java
 * DTV������ؽӿ�
 * @author:
 * @date:
 * @version 0.1
 * @history: 2012-12-25 ���ͷ���Դ��ʱ��λ��ǰ���Ž�Ŀ����Ϣ
 */
package com.changhong.tvos.dtv.service;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.Xml;

import com.changhong.tvos.common.IResourceManager.IResourceListener;
import com.changhong.tvos.common.ITVPlayer;
import com.changhong.tvos.common.SoundManager;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.common.exception.TVManagerNotInitException;
import com.changhong.tvos.model.EnumInputSource;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DTVServiceRm implements IResourceListener {

	/**
	 * DTVRouterInfo��JNI��������,һ��DTVRouterInfo��һ��RouterID,����Ψһ����
	 */
	public class DTVRouterInfo {
		public int Tuner_RsID;
		public int Vdecoder_RsID;
		public int Vwindown_RsID;
		boolean tvos_used;
		public List<DTVPlayer> playerlist;
		public int RouterID;
	}

	final static String TAG = "DtvService.DTVServiceRm";

	public static final int request_auto = 0;
	/**
	 * �Զ���������Դ�Ż�����ɹ�
	 **/
	public static final int request_force = 1;
	/**
	 * ǿ������������û����Դ��Ҫ������ɹ�
	 **/
	public static final int request_share = 2;
	public static final int request_prioity = 3;
	/**
	 * ����Ӧ�����ȼ�������
	 **/

	public static final String tvos_clientid = "tvos_clientid";
	TVOSService tvos = null;
	private static DTVServiceRm Rmobj = null;

	private boolean isUseTVOS = false;
	static private boolean isTVOSGetDTVRs = false;
	/**
	 * �Ƿ���TVOS�����DTV��Դ��TVOS�ͷ�DTV��Դʱ��Ҫ�ָ���false
	 **/

	private IRmlistener Rmlistener = null;
	/**
	 * ��Դ�ص��ӿڶ���
	 **/

	private List<Resource> RsList = null;
	/**
	 * ��Դ�б�
	 **/
	public List<DTVRouterInfo> RouterList = null;
	private static boolean isRecordMode = false;
	private static EnumInputSource curSource;

	protected DTVServiceRm() {
		Log.i(TAG, "in DTVServiceRMManager");
		isUseTVOS = false;

		//��ʼ��DTVServiceRMManager�������ϵͳ �����ļ��л�ȡ��Դ��Ϣ 
		try {
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("com/changhong/tvos/dtv/service/config/RmConfig.xml");
			try {
				RsList = GetRmlist(inStream);
				//test
				if (RsList != null) {
					for (int i = 0; i < RsList.size(); i++) {
						Log.i(TAG, "RsList[" + i + "]" + RsList.get(i).RsID + ";" + RsList.get(i).RsType + ";" + RsList.get(i).RsStaus);
					}
				}
				//test
			} catch (Exception e) {
				e.printStackTrace();
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//���¼�¼״̬��Ϣ
		{
			int i;
			Resource resource = null;
			for (i = 0; i < RsList.size(); i++) {
				resource = RsList.get(i);
				resource.RsStaus = Resource.STATUS_FREEED;
				resource.Clientlist = new ArrayList<RsClient>();
			}
		}
		RouterList = new ArrayList<DTVRouterInfo>();
	}

	public static synchronized DTVServiceRm getinstance() {
		if (Rmobj == null) {
			Rmobj = new DTVServiceRm();
		}
		return Rmobj;
	}

	//�������ļ���ȡ��Դ��Ϣ
	private List<Resource> GetRmlist(InputStream responseInStream) throws Exception {
		Resource resource = null;
		List<Resource> resources = null;
		XmlPullParser pullParser = Xml.newPullParser();

		if (responseInStream == null) {
			Log.i(TAG, "in GetRmlist responseInStream error");
			resources = new ArrayList<Resource>();
			resource = new Resource();
			resource.RsID = 0;
			resource.RsType = 0;
			resource.TVOSUsed = 1;
			resources.add(resource);
			return resources;
		}

		pullParser.setInput(responseInStream, "utf-8");
		int evenType = pullParser.getEventType();//������һ���¼�

		while (evenType != XmlPullParser.END_DOCUMENT) {
			switch (evenType) {
				case XmlPullParser.START_DOCUMENT:
					resources = new ArrayList<Resource>();
					Log.i(TAG, "in START_DOCUMENT");
					break;
				case XmlPullParser.START_TAG: {
					String tag_name = pullParser.getName();
					Log.i(TAG, "in START_TAG tag_name = " + tag_name);
					if ("Resource".equals(tag_name)) {
						resource = new Resource();
						Log.i(TAG, "in START_TAG Resource");
					}

					if (resource != null) {
						if ("ResouceID".equals(tag_name)) {
							resource.RsID = Integer.parseInt(pullParser.nextText());

							Log.i(TAG, "in START_TAG ResouceID=" + resource.RsID);
						}
					}
					if (resource != null) {
						if ("ResouceType".equals(tag_name)) {
							resource.RsType = Integer.parseInt(pullParser.nextText());
							Log.i(TAG, "in START_TAG ResouceType=" + resource.RsType);
						}
					}
					if (resource != null) {
						if ("ResouceTVOS".equals(tag_name)) {
							resource.TVOSUsed = Integer.parseInt(pullParser.nextText());
							Log.i(TAG, "in START_TAG ResouceTVOS=" + resource.TVOSUsed);
						}
					}
				}
				break;
				case XmlPullParser.END_TAG:
					String tag_name = pullParser.getName();
					Log.i(TAG, "in END_TAG tag_name = " + tag_name);
					if ("Resource".equals(tag_name)) {
						resources.add(resource);
						resource = null;
						Log.i(TAG, "in END_TAG Resource");
					}
					break;

				default:
					break;
			}
			evenType = pullParser.next();
		}
		return resources;
	}

	//����һ����Դ
	public int RmRequest(int RsID, int RsType, int requesttype, String ClientID, int clienttype) {
		synchronized (RouterList) {
			Log.i(TAG, "[isUseTVOS]" + isUseTVOS);
			/** �ж��������Դ�Ƿ��ATV�г�ͻ **/
			if (isUseTVOS == true) {
				if (IsTVOSRsType(RsID, RsType) == true) //true
				{
					Log.i(TAG, "[istvostype]" + isTVOSGetDTVRs);
					//��TVOS����DTV��Դ
					if (isTVOSGetDTVRs == false) {
						isTVOSGetDTVRs = tvos.requestResource(TVOSService.RSS_DTV);
						if (isTVOSGetDTVRs) {
							Log.i(TAG, "start registerResourceListener");
							/*��TVOSע����Դ��ռ�ص�*/
							tvos.registerResourceListener(this);

							// �����������
							TVManager tvM = TVManager.getInstance(null);
							ITVPlayer tvPlayer = null;
							try {
								tvPlayer = tvM.getTVPlayer();
							} catch (TVManagerNotInitException e) {
								// TO DO nothing
							}
							if (tvPlayer != null) {
								tvPlayer.unmuteVideo();
							}
						}
					}
					// else
					//{/**ϵͳ��Ҫ֪ͨ�л���AP**/
					/* if(RsType == Resource.RESOURCE_TUNER){
						 Log.i(TAG,"RmRequest***");
						 tvos.requestResource(TVOSService.RSS_DTV);   //С����  !?
					 }
					 */
					// }

					Log.i(TAG, "RmRequest----isTVOSGetDTVRs-->" + isTVOSGetDTVRs + " GetRsStatus(RsID,RsType):" + GetRsStatus(RsID, RsType));
					if (isTVOSGetDTVRs == false) {
						return -1;/*��Դ����ʧ��*/
					}
					/*��TVOSע����Դ��ռ�ص�*/
					// tvos.registerResourceListener(this);
					// Log.i(TAG,"start registerResourceListener");
				}
			}
			/*(2)�жϵ�ǰ��Դ�Ƿ����*/
			// if(GetRsStatus(RsID,RsType) == Resource.STATUS_FREEED){
			/*������Դ��Ϣ*/
			AddClient(RsID, RsType, ClientID, clienttype);
			SetRsStaus(RsID, RsType, Resource.STATUS_USED);
			//֪ͨ�ͻ����Ѿ���??
			if (Rmlistener != null) {
				Rmlistener.RmlistenCallback(Resource.STATUS_USED, RsID, RsType, ClientID);
			}
			// }
			// else{
			// //�ж�ʹ��ǿ����ռ��Դ
			// if(requesttype == request_force){
			// AddClient(RsID,RsType,ClientID,clienttype);
			// SetRsStaus(RsID,RsType,Resource.STATUS_FORCEUSED);
			// //֪ͨ�ͻ���ǿ����ռ��
			// if(Rmlistener != null){
			//					 Rmlistener.RmlistenCallback(Resource.STATUS_FORCEUSED, RsID,RsType,ClientID);
			// }
			//				 
			// }
			// else{
			// return -1;/*��Դ����ʧ��*/
			//			 }	 
			// }

			/*(3)*/

			return 0;
		}
	}

	//��ȡ������Դ���͵���ԴID�б�
	public List<Resource> RmGetFreeList(int RsType) {
		Log.i(TAG, "[RmGetFreeList]" + RsType);
		List<Resource> FreeRsList = null;
		int i;
		Resource resource = null;
		FreeRsList = new ArrayList<Resource>();
		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.RsType == RsType) {
				FreeRsList.add(resource);
			}
		}
		return FreeRsList;
	}

	//�ͷ�ָ���ͻ�����Դ��client idΪnullʱΪ�ͷ����пͻ���Դ
	//��ʧȥ��Դ�Ŀͻ���(UUID)���б���ɾ��--�˿ͻ�����Դ����ռ
	public int RmRelease(int RsID, int RsType, String ClientID) {
		int i;
		int j;
		Resource resource = null;
		RsClient client = null;

		Log.i(TAG, "RmRelease-->RsID:" + RsID + " RsType:" + RsType + " ClientID:" + ClientID);

		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.RsID == RsID && resource.RsType == RsType) {

				for (j = 0; j < resource.Clientlist.size(); j++) {
					client = resource.Clientlist.get(j);
					Log.i(TAG, "RmRelease-->client:" + client);
					if (ClientID == null) {
						resource.Clientlist.remove(client);
					} else {
						if (client.ClientID == ClientID) {
							resource.Clientlist.remove(client);
						}
					}
				}

				Log.i(TAG, "RmRelease-->resource.Clientlist.size:" + resource.Clientlist.size());

				if (resource.Clientlist.size() == 0) {/*����Դ�Ѿ�û�пͻ���ʹ�� */
					Log.i(TAG, "[the 3 resource be freed]" + RsID + ";" + RsType);
					SetRsStaus(RsID, RsType, Resource.STATUS_FREEED);
					//֪ͨ�ͻ��˸���Դ����
					if (Rmlistener != null) {
						Rmlistener.RmlistenCallback(Resource.STATUS_FREEED, RsID, RsType, ClientID);
					}
				}
				return 0;
			}
		}
		return -1;
	}

	//�����Ƿ�ʹ��TVOS��Դ���� 
	public void RmSetTVOS(boolean istvos, Context context) {
		Log.i(TAG, "[RmSetTVOS]" + istvos + "[tvos]" + tvos);
		isUseTVOS = istvos;
		//isUseTVOS = false;
		if (isUseTVOS == true) {
			if (tvos == null) {
				tvos = TVOSService.getinstance(context);
				Log.i(TAG, "RmSetTVOS->allocate TVOSService");
			}
		}
	}

	//ע����Դ״̬���»ػص�
	public void RmRgListener(IRmlistener rRgListener) {
		Log.i(TAG, "[RmRgListener]" + rRgListener);
		Rmlistener = rRgListener;
	}

	//ɾ����Դ״̬���»ص�
	public void RmRemoveRgListener(IRmlistener rRgListener) {
		Log.i(TAG, "[RmRemoveRgListener]" + rRgListener);
		Rmlistener = null;
	}

	//��ȡ��Դ��ʹ��״��
	private int GetRsStatus(int RsID, int RsType) {
		int i;
		Resource resource = null;
		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.RsID == RsID && resource.RsType == RsType) {
				return resource.RsStaus;
			}
		}
		return -1;
	}

	//������Դ��ʹ��״��
	private int SetRsStaus(int RsID, int RsType, int RsStatus) {
		int i;

		Log.i(TAG, "SetRsStaus RsID:" + RsID + "   RsType:" + RsType);
		Resource resource = null;
		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.RsID == RsID && resource.RsType == RsType) {
				resource.RsStaus = RsStatus;
				return 0;
			}
		}
		return -1;
	}

	//����ʹ����Դ�ͻ�
	private int AddClient(int RsID, int RsType, String ClientID, int clienttype) {
		int i;
		Resource resource = null;
		RsClient client = null;

		Log.i(TAG, "AddClient RsID:" + RsID + "   RsType:" + RsType);
		synchronized (RsList) {
			for (i = 0; i < RsList.size(); i++) {
				resource = RsList.get(i);
				if (resource.RsID == RsID && resource.RsType == RsType) {
					client = new RsClient();
					client.ClientID = ClientID;
					client.CLientType = clienttype;

					resource.Clientlist.add(client);
					return 0;
				}
			}
		}

		return -1;
	}

	//ɾ��ʹ����Դ�ͻ�
	public int RemoveClient(int RsID, int RsType, String ClientID, int clienttype) {
		int i;
		Resource resource = null;
		RsClient client = null;

		Log.i(TAG, "RemoveClient RsID:" + RsID + "   RsType:" + RsType);
		synchronized (RsList) {
			for (i = 0; i < RsList.size(); i++) {
				resource = RsList.get(i);
				if (resource.RsID == RsID && resource.RsType == RsType) {
					client = new RsClient();
					client.ClientID = ClientID;
					client.CLientType = clienttype;
					resource.Clientlist.remove(client);
					return 0;
				}
			}
		}
		return -1;
	}

	//�жϸ���Դ�Ƿ����
	public boolean IsExitRs(int RsID, int RsType) {
		int i;

		Log.i(TAG, "IsExitRs RsID:" + RsID + "   RsType:" + RsType);
		Resource resource = null;
		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.RsID == RsID && resource.RsType == RsType) {
				return true;
			}
		}
		return false;
	}

	//�жϸ���Դ�Ƿ���TVOSʹ�õ���Դ(0,0)
	private boolean IsTVOSRsType(int RsID, int RsType) {
		int i;

		Log.i(TAG, "IsTVOSRsType RsID:" + RsID + "   RsType:" + RsType);
		Resource resource = null;
		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.RsID == RsID && resource.RsType == RsType) {
				if (resource.TVOSUsed >= 1)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public void onResourceLose() {
		Log.i(TAG, "onResourceLose");
		synchronized (RouterList) {
			synchronized (RsList) {
				//֪ͨ�ͻ���ǿ����ռ�¼�
				Log.i(TAG, "[isUseTVOS]" + isUseTVOS + "[Rmlistener]" + Rmlistener);
				if (Rmlistener != null && isUseTVOS == true) {
					int i;
					Resource resource = null;
					Log.i(TAG, "[rslist]" + RsList.size());
					for (i = 0; i < RsList.size(); i++) {
						resource = RsList.get(i);
						Log.i(TAG, "[resourceParam]" + resource.TVOSUsed);
						if (resource.TVOSUsed >= 1) {
							//�����TVOS�ͻ�
							Log.i(TAG, "[destroy all router]");
							DestroyTVOSRouter();
							//DTVPlayer will deal with the RmlistenCallback
							Log.i(TAG, "[DTVPlayer deal with]");
							Rmlistener.RmlistenCallback(Resource.STATUS_FORCEUSED, resource.RsID, resource.RsType, tvos_clientid);
						}
					}
					//�л�ǰ�����;���
					TVManager tvM = TVManager.getInstance(null);
					ITVPlayer tvPlayer = null;
					SoundManager soundM = null;
					Log.i(TAG, "[tvM]" + tvM);
					try {
						tvPlayer = tvM.getTVPlayer();
						soundM = tvM.getSoundManager();
					} catch (TVManagerNotInitException e) {
						// TO DO nothing
					}
					if (tvPlayer != null) {
						tvPlayer.muteVideo();
					}

					if (soundM != null) {
						soundM.enableUserMute(true);
					}

					tvos.unRegisterResourceListener(this);
					isTVOSGetDTVRs = false;
				}
			}
			Log.i(TAG, "[RsList--]");
		}
		Log.i(TAG, "[RouterList--]");
	}

	public void testTvosCallback() {
		Log.i(TAG, "testTvosCallback");
		if (Rmlistener != null && isUseTVOS == true) {
			Log.i(TAG, "testTvosCallback");
			tvos.test_ResourceCallback();
		}
	}

	public int RequestRouteID(int Tuner_RsID, int Vdecoder_RsID, int Vwindown_RsID, DTVPlayer playobj) {

		int i;
		int j;
		int routerID;
		int request_type = DTVServiceRm.request_auto;
		Resource resource = null;
		boolean tuner_tvos = false;
		boolean vdecoder_tvos = false;
		boolean vwindow_tvos = false;

		Log.i(TAG, "RequestRouteID Tuner_RsID:" + Tuner_RsID + "   Vdecoder_RsID:" + Vdecoder_RsID + "DTVPlayer=" + playobj);

		if (RouterList == null || playobj == null)
			return -1;
		if (playobj.objiCLientType == RsClient.CLIENT_FORCE) {
			request_type = DTVServiceRm.request_force;
		} else if (playobj.objiCLientType == RsClient.CLIENT_AUTO) {
			request_type = DTVServiceRm.request_auto;
		} else if (playobj.objiCLientType == RsClient.CLIENT_SHARE) {
			request_type = DTVServiceRm.request_share;
		} else if (playobj.objiCLientType == RsClient.CLIENT_PRIORITY) {
			request_type = DTVServiceRm.request_prioity;
		}
		for (i = 0; i < RsList.size(); i++) {
			resource = RsList.get(i);
			if (resource.TVOSUsed >= 1 && resource.RsType == Resource.RESOURCE_TUNER && resource.RsID == Tuner_RsID) {
				tuner_tvos = true;
			}
			if (resource.TVOSUsed >= 1 && resource.RsType == Resource.RESOURCE_VIDEODECODER && resource.RsID == Vdecoder_RsID) {
				vdecoder_tvos = true;
			}
			if (resource.TVOSUsed >= 1 && resource.RsType == Resource.RESOURCE_VIDEOWINDDOW && resource.RsID == Vwindown_RsID) {
				vwindow_tvos = true;
			}
		}

		DTVRouterInfo routerinfo = null;

		synchronized (RouterList) {
			Log.i(TAG, "[RouterListSize]" + RouterList.size());
			for (i = 0; i < RouterList.size(); i++) {
				routerinfo = RouterList.get(i);

				if (routerinfo.Tuner_RsID == Tuner_RsID && routerinfo.Vdecoder_RsID == Vdecoder_RsID && routerinfo.Vwindown_RsID == Vwindown_RsID) {
					Log.i(TAG, "[routerinfoPlayerlistSize]" + routerinfo.playerlist.size());
					for (j = 0; j < routerinfo.playerlist.size(); j++) {
						DTVPlayer player = routerinfo.playerlist.get(j);
						Log.i(TAG, "R[" + i + "_" + j + "]" + "[player]" + player + "[playobj]" + playobj);
						if (player == playobj) {
							//�ŵ����
							Log.e(TAG, " move client, current client entry size:" + routerinfo.playerlist.size() + "player:" + player);
							routerinfo.playerlist.remove(player);
							routerinfo.playerlist.add(player);
							Log.e(TAG, " move client, current client end size:" + routerinfo.playerlist.size() + "player:" + player);

							break;
						}
					}
					if (j >= routerinfo.playerlist.size()) {
						routerinfo.playerlist.add(playobj);
						Log.i(TAG, " add client, current client:" + routerinfo.playerlist.size());
					}
					//if(request_type == DTVServiceRm.request_force)//force set other player object router invalid
					{
						for (j = 0; j < routerinfo.playerlist.size(); j++) {
							DTVPlayer player = routerinfo.playerlist.get(j);
							Log.i(TAG, "RR[" + i + "_" + j + "]" + "[player]" + player + "[playobj]" + playobj);
							if (player != playobj) {
								Log.i(TAG, "RequestRouteID-->ROUTER_USED_BY_OTHER  tvos_clientid:" + tvos_clientid);
								//��Դ����ռ(ֱ�ӽ���Դ���ظ���ǰ������)	,��֪֮ͨǰռ����RmlistenCallback(STATUS_FORCEUSED)
								//player.objRouterStatus = DTVPlayer.ROUTER_DESTROIED;
								player.objRouterStatus = DTVPlayer.ROUTER_USED_BY_OTHER;
								player.RmlistenCallback(Resource.STATUS_FORCEUSED, resource.RsID, resource.RsType, tvos_clientid);
							}
						}
					}
					//��Դ����ռ(ֱ�ӽ���Դ���ظ���ǰ������)
					Log.i(TAG, "return last RouterID=" + routerinfo.RouterID + "client size:" + routerinfo.playerlist.size() + "obj:" + playobj);
					return routerinfo.RouterID;
				}
			}
			Log.i(TAG, "[lsy createRouter start]");
			routerID = DTVServiceJNI.get_system_instance().createRouter(Tuner_RsID, Vdecoder_RsID, Vwindown_RsID);
			Log.i(TAG, "[lsy player begin set 1 start]" + DTVPlayer.rmLock.get(0));
			DTVPlayer.rmLock.set(0, 1);
			Log.i(TAG, "[lsy player begin set 1 end]" + DTVPlayer.rmLock.get(0));
			Log.i(TAG, "[lsy createRouter end]");
			if (routerID != -1) {
				routerinfo = new DTVRouterInfo();
				routerinfo.playerlist = new ArrayList<DTVPlayer>();
				routerinfo.Tuner_RsID = Tuner_RsID;
				routerinfo.Vdecoder_RsID = Vdecoder_RsID;
				routerinfo.Vwindown_RsID = Vwindown_RsID;
				routerinfo.RouterID = routerID;

				if (tuner_tvos == true || vdecoder_tvos == true || vwindow_tvos == true) {
					routerinfo.tvos_used = true;
				} else {
					routerinfo.tvos_used = false;
				}

				routerinfo.playerlist.add(playobj);
				Log.i(TAG, "success New RouterID=" + routerID + "[playobj]" + playobj);
				RouterList.add(routerinfo);

				if (!isTVOSGetDTVRs) {
					//�ٴ�ע��ص�����ֹ����ǰע��ص����ͷŲ�ͬ������ʧ������
					tvos.registerResourceListener(this);

					//2013-01-22
					isTVOSGetDTVRs = tvos.requestResource(TVOSService.RSS_DTV);
				}
				return routerID;
			} else {
				Log.i(TAG, "failed New RouterID=" + routerID);
				return -1;
			}
		}
	}

	private int DestroyTVOSRouter() {
		int i;
		int j;
		DTVRouterInfo routerinfo = null;
		Log.i(TAG, "DestroyTVOSRouter :" + RouterList.size());

		synchronized (RouterList) {
			for (i = 0; i < RouterList.size(); i++) {
				routerinfo = RouterList.get(i);
				{
					if (routerinfo.tvos_used == true) {
						for (j = 0; j < routerinfo.playerlist.size(); j++) {
							DTVPlayer player = routerinfo.playerlist.get(j);
							synchronized (player) {
								DestroyRouteID(routerinfo.Tuner_RsID, routerinfo.Vdecoder_RsID, routerinfo.Vwindown_RsID, player, true);

								// DestroyRouteIDnolock(routerinfo.Tuner_RsID,routerinfo.Vdecoder_RsID,
								// routerinfo.Vwindown_RsID,player, true);

							}
							// {/**֪ͨϵͳ����Դ�ͷ�**/
							// Log.i(TAG,"RmRelease***");
							// tvos.releaseResource();
							// tvos.unRegisterResourceListener(this);
							// isTVOSGetDTVRs = false;
							// }
							//
						}
					}
				}
			}
		}
		return 0;
	}

	public int DestroyRouteID(int Tuner_RsID, int Vdecoder_RsID, int Vwindown_RsID, DTVPlayer playobj, boolean isForce) {
		int i;
		int j;

		Log.i(TAG, "DestroyRouteID :" + RouterList.size() + "[Tuner_RsID]" + Tuner_RsID + "[Vdecoder_RsID]" + Vdecoder_RsID + "[Vwindown_RsID]" + Vwindown_RsID + "[playobj]" + playobj + "[isForce]"
				+ isForce);

		DTVRouterInfo routerinfo = null;
		if (RouterList == null || playobj == null)
			return -1;

		synchronized (RouterList) {
			for (i = 0; i < RouterList.size(); i++) {
				routerinfo = RouterList.get(i);
				if (routerinfo.Tuner_RsID == Tuner_RsID && routerinfo.Vdecoder_RsID == Vdecoder_RsID && routerinfo.Vwindown_RsID == Vwindown_RsID) {
					Log.i(TAG, "DestroyRouteID player numb:" + routerinfo.playerlist.size());
					for (j = 0; j < routerinfo.playerlist.size(); j++) {
						DTVPlayer player = routerinfo.playerlist.get(j);
						Log.i(TAG, "D[" + i + "_" + j + "]" + "[player]" + player + "[playobj]" + playobj);
						if (player == playobj) {
							Log.i(TAG, "[playerlist remove]");
							routerinfo.playerlist.remove(playobj);
							break;
						}
					}
					Log.i(TAG, "[playerlistsize]" + routerinfo.playerlist.size());
					// if((routerinfo.playerlist.size() == 0) && (isForce))
					if (routerinfo.playerlist.size() == 0) {
						Log.i(TAG, "[lsy get_system_instance().destroyRouter]" + routerinfo.RouterID);
						synchronized (DTVPlayer.rmLock) {
							Log.i(TAG, "[lsy dtvRm rmLock]" + DTVPlayer.rmLock.get(0));
							if (DTVPlayer.rmLock.get(0) == 1) {
								int iret = DTVServiceJNI.get_system_instance().destroyRouter(routerinfo.RouterID);
								Log.i(TAG, "[lsy Rm begin set 0 start]" + DTVPlayer.rmLock.get(0) + " iret=" + iret);
								DTVPlayer.rmLock.set(0, 0);
								Log.i(TAG, "[lsy Rm begin set 0 end]" + DTVPlayer.rmLock.get(0));
							}
						}

						Log.i(TAG, "[lsy destroyRouter1]" + routerinfo.RouterID);
						RouterList.remove(i);
						if (RouterList != null) {
							Log.i(TAG, "current RouterListSize=" + RouterList.size());
						}
						if (isTVOSGetDTVRs) {
							Log.i(TAG, "DestroyRouteID release***");
							tvos.releaseResource();
							tvos.unRegisterResourceListener(this);
							isTVOSGetDTVRs = false;
							DTVPlayer.isPlaying = 0;
							DTVPlayer.isDTV_Busying = 0;

							if (DTVPlayer.currentProgramID != -1) {
								DTVPlayer.lastProgramID = DTVPlayer.currentProgramID;
								DTVPlayer.currentProgramID = -1;
							}
						}
						return 0;
					}
				}
			}
		}

		return 0;
	}

	////////////////////////////////////////////////////////////////////////
	public int DestroyRouteIDnolock(int Tuner_RsID, int Vdecoder_RsID, int Vwindown_RsID, DTVPlayer playobj, boolean isForce) {
		int i;
		int j;

		Log.i(TAG, "DestroyRouteID :" + RouterList.size());

		DTVRouterInfo routerinfo = null;
		if (RouterList == null || playobj == null)
			return -1;

		// synchronized(RouterList)
		{
			for (i = 0; i < RouterList.size(); i++) {
				routerinfo = RouterList.get(i);
				if (routerinfo.Tuner_RsID == Tuner_RsID && routerinfo.Vdecoder_RsID == Vdecoder_RsID && routerinfo.Vwindown_RsID == Vwindown_RsID) {
					Log.i(TAG, "DestroyRouteID player numb:" + routerinfo.playerlist.size());
					for (j = 0; j < routerinfo.playerlist.size(); j++) {
						DTVPlayer player = routerinfo.playerlist.get(j);
						if (player == playobj) {
							routerinfo.playerlist.remove(playobj);
							break;
						}
					}
					// if((routerinfo.playerlist.size() == 0) && (isForce))
					if (routerinfo.playerlist.size() == 0) {
						Log.i(TAG, "get_system_instance().destroyRouter :" + routerinfo.RouterID);
						int iret = DTVServiceJNI.get_system_instance().destroyRouter(routerinfo.RouterID);
						Log.i(TAG, "[destroyRouter2]" + routerinfo.RouterID + ";[return]" + iret);
						RouterList.remove(i);
						if (isTVOSGetDTVRs) {
							Log.i(TAG, "DestroyRouteID release***");
							tvos.releaseResource();
							tvos.unRegisterResourceListener(this);
							isTVOSGetDTVRs = false;

							DTVPlayer.isPlaying = 0;
							DTVPlayer.isDTV_Busying = 0;

							if (DTVPlayer.currentProgramID != -1) {
								DTVPlayer.lastProgramID = DTVPlayer.currentProgramID;
								DTVPlayer.currentProgramID = -1;
							}
						}
						return 0;
					}
				}
			}
		}

		return 0;
	}

	/**
	 * �ͷŸ���һ��Ӧ��
	 **/
	public int DestroyRouteIDForBack(int Tuner_RsID, int Vdecoder_RsID, int Vwindown_RsID, DTVPlayer playobj) {
		int i;
		int j;

		Log.i(TAG, "DestroyRouteIDForBack :" + RouterList.size());

		DTVRouterInfo routerinfo = null;
		if (RouterList == null || playobj == null)
			return -1;

		synchronized (RouterList) {
			for (i = 0; i < RouterList.size(); i++) {
				routerinfo = RouterList.get(i);

				if (routerinfo.Tuner_RsID == Tuner_RsID && routerinfo.Vdecoder_RsID == Vdecoder_RsID && routerinfo.Vwindown_RsID == Vwindown_RsID) {
					for (j = 0; j < routerinfo.playerlist.size(); j++) {
						DTVPlayer player = routerinfo.playerlist.get(j);
						if (player == playobj) {
							routerinfo.playerlist.remove(playobj);
							break;
						}
					}

					for (j = routerinfo.playerlist.size(); j > 0; j--) {
						DTVPlayer player = routerinfo.playerlist.get(j - 1);
						//						   if((player != null) && (player.objRouterStatus == DTVPlayer.ROUTER_DESTROIED)){
						if ((player != null) && (player.objRouterStatus == DTVPlayer.ROUTER_USED_BY_OTHER)) {
							//���������ʹ����Դ�����ظ���һ��Ӧ��
							try {
								player.prepare();
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							break;
						}

					}

					if (routerinfo.playerlist.size() == 0) {
						Log.i(TAG, "get_system_instance().destroyRouter :" + routerinfo.RouterID);
						int iret = DTVServiceJNI.get_system_instance().destroyRouter(routerinfo.RouterID);
						Log.i(TAG, "[destroyRouter3]" + routerinfo.RouterID + ";[return]" + iret);
						RouterList.remove(i);

						if (isTVOSGetDTVRs) {
							Log.i(TAG, "DestroyRouteIDForBack release***");
							tvos.releaseResource();
							tvos.unRegisterResourceListener(this);
							isTVOSGetDTVRs = false;
						}
						return 0;
					}
				}
			}
		}

		return 0;
	}

	public void SetRsStaus(boolean isDTVRs) {
		isTVOSGetDTVRs = isDTVRs;
		if (isDTVRs) {
			tvos.registerResourceListener(this);
		}
	}
}
