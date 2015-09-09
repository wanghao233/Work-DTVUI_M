package com.changhong.menudata.menuPageData;

import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemPromptData;
import com.changhong.data.pageData.itemData.ItemRatingBarData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerStatus;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import android.content.Context;
import android.util.Log;

public class ChannelInfoData extends ListPageData{
	private static final String TAG = ChannelInfoData.class.getSimpleName();
	
	Context mContext = null;
	ItemPromptData mChannelNum = null;
	ItemPromptData mChannelName = null;
	ItemPromptData mOrgNetTSServiceID = null;
	ItemPromptData mAudioVedioPID = null;
	ItemPromptData mAudioVedioType = null;
	ItemPromptData mAudioMode = null;
	ItemPromptData mAudioTrack = null;
	ItemPromptData mFrequency = null;
	ItemPromptData mSymbolRate = null;
	ItemPromptData mQam = null;
	ItemPromptData mErrorRate = null;
	
	ItemPromptData mEncodeData =null;
	ItemPromptData  mBandwidth = null;
	
	
	ItemRatingBarData mSignalStrength = null; 
	ItemRatingBarData mSignalQuality = null; 
	DtvChannelManager mChannelManager = null;
	DtvSourceManager mDemodeTypeManager = null;
	
	String[] audioModeStr = null;
	String[] audioTrackAllStr = null;
	String[] audioTrackStr = null;
	String[] mQamStr = null;
	int mChannelNumValue = 0;
	String mChannelNameValue = "";
	int mOrgNetIDValue = 0;
	int mTSIDValue = 0;
	int mServiceIDValue = 0;
	int mAudioPIDValue = 0;
	int mVedioPIDValue = 0;
	int mAudioTypeValue = -1;
	int mVedioTypeValue = -1;
	String mAudioModeValue = "";
	String mAudioTrackValue = "";
	
	String bandWidth = "";
	
	int mFrequencyValue = 0;
	int mSymbolRateValue = 0;
	String mQamValue = "";
	
	
	int mErrorRateValue = 0;
	int mSignalStrengthValue = 0; 
	int mSignalQualityValue = 0; 
	
	int demodType = ConstDemodType.DVB_C;
	public ChannelInfoData(String strTitle, int picTitle,Context context) {
		super(ConstPageDataID.CHANNEL_INFO_PAGE_DATA,strTitle,picTitle);
		mContext = context;
		mType = EnumPageType.BroadListPage;
		mChannelManager = DtvChannelManager.getInstance();
		mDemodeTypeManager = DtvSourceManager.getInstance();
		
		this.isFoucsAble = false;
		this.init();
	}

	public void updatePage(){
		Log.i(TAG,"LL updatePage***");
		this.updatePageData();
		super.updatePage();
	}
	private void init() {
		demodType = mDemodeTypeManager.getCurDemodeType();
		audioModeStr = getContext().getResources().getStringArray(
				R.array.menu_audio_mode);
		audioTrackAllStr = getContext().getResources().getStringArray(
				R.array.menu_audio_track);
		audioTrackStr = mChannelManager.getAudioTrack(audioTrackAllStr);
		
		mChannelNum = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_num),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
			
		};
		mChannelName = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_name),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mOrgNetTSServiceID = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_ts_service_id),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mAudioVedioType = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_audio_vedio_encode),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mAudioVedioPID = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_audio_vedio_pid),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mAudioMode = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_audio),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mAudioTrack = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_audio_track),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mFrequency = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_frequency),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
			mSymbolRate = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_sybol_rate),0,0) {
				
				@Override
				public int isEnable() {
					// TODO Auto-generated method stub
					return 1;
				}
			};
			
		
		mQam = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_qam),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mErrorRate = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_erro_rate),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mSignalStrength = new ItemRatingBarData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_signal_strength),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mSignalStrength.setTotalCount(10);
		mSignalQuality = new ItemRatingBarData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_signal_quality),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};		
		mSignalQuality.setTotalCount(10);
		
		String titleString = getContext().getResources().getString(R.string.dtv_info_cryption) + "/" +
		 	getContext().getResources().getString(R.string.dtv_info_encryption);
		
		mEncodeData = new ItemPromptData(0,titleString,0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		
		mBandwidth = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_channel_info_channel_bandwidth),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		
		this.updatePageData();
		this.addPageItem();
	}
	private void updatePageData(){
		demodType = mDemodeTypeManager.getCurDemodeType();
		DtvProgram dtvCurProgram = mChannelManager.getCurProgram();
		audioTrackStr = mChannelManager.getAudioTrack(audioTrackAllStr);
		
		if(ConstDemodType.DVB_C == demodType){
			
			mQamStr = getContext().getResources().getStringArray(
					R.array.menu_scan_modulation);
		}else{
			
			mQamStr = getContext().getResources().getStringArray(
					R.array.menu_scan_modulation_dmbt);
		}
		if(dtvCurProgram!=null){
			mChannelNumValue = dtvCurProgram.mProgramNum;
			mChannelNameValue = dtvCurProgram.mProgramName;
			mOrgNetIDValue = dtvCurProgram.mOrgNetID;
			mTSIDValue = dtvCurProgram.mTsID;
			mServiceIDValue = dtvCurProgram.mServiceID;
			
			if(mChannelManager.getAudioModeSel() < 0 || mChannelManager.getAudioModeSel() >= audioModeStr.length){
				mAudioModeValue = audioModeStr[0];
				Log.e(TAG, "ChannelInfoData updatePageData() err array outIndex mChannelManager.getAudioModeSel() " +
						mChannelManager.getAudioModeSel());
			}else {
			mAudioModeValue = audioModeStr[mChannelManager.getAudioModeSel()];
			}
			
			if(mChannelManager.getAudioTrackSelIndex() < 0 || mChannelManager.getAudioTrackSelIndex() >= audioTrackStr.length){
				mAudioTrackValue = audioTrackStr[0];
				Log.e(TAG, "ChannelInfoData updatePageData() err array outIndex getAudioTrackSelIndex() is " + mChannelManager.getAudioTrackSelIndex());
			}else {
			mAudioTrackValue = audioTrackStr[mChannelManager.getAudioTrackSelIndex()];
			}
			
			if (ConstDemodType.DVB_C == demodType) {
				DVBCCarrier carrier = mChannelManager.getDVBCCurTunerInfo();
				if(carrier != null){
					Log.i(TAG,"LL mQamValue = " + mQamValue + ",carrier.miQamMode = " + carrier.miQamMode);
					mFrequencyValue = carrier.miFrequencyK;
					mSymbolRateValue = carrier.miSymbolRateK;
					if(0 > carrier.miQamMode-1 || mQamStr.length <= carrier.miQamMode-1){
						mQamValue = mQamStr[0];
					}else {
						mQamValue = mQamStr[carrier.miQamMode-1];
					}
					
				}else{
					Log.e(TAG,"LL carrier == null");
					mFrequencyValue = 0;
					mSymbolRateValue = 0;
					mQamValue = "";
				}
			} else {
				DMBTHCarrier carrier = mDemodeTypeManager.getDMBTCarrierInfo();
				if(null != carrier){
					
					mFrequencyValue = carrier.miFrequencyK ;
					if(0 > carrier.miDTMBTHQamMode-1 || mQamStr.length <= carrier.miDTMBTHQamMode-1){
						Log.e(TAG, "ChannelInfoData updatePageData() err array outIndex " + carrier.miDTMBTHQamMode);
						mQamValue = mQamStr[0];
					}else {
						mQamValue = mQamStr[carrier.miDTMBTHQamMode-1];
					}
					
 					bandWidth = "8 MHz";
 					
				}else {
					 mFrequencyValue = 0;
					 mQamValue = "";
					 bandWidth = "";
				}
			}
			
			DTVChannelDetailInfo channelDetailInfo = mChannelManager.getDtvChannelDetailInfo(mChannelManager.getCurPorgramServiceIndex());
			if(channelDetailInfo!=null){
				mAudioPIDValue = channelDetailInfo.miAudioPID;
				mVedioPIDValue = channelDetailInfo.miVedioPID;
				mAudioTypeValue = channelDetailInfo.miAudioType;
				mVedioTypeValue = channelDetailInfo.miVideoType;
				
			}else{
				Log.e(TAG,"LL channelDetailInfo == null");
				mAudioPIDValue = 0;
				mVedioPIDValue = 0;
				mAudioTypeValue = -1;
				mVedioTypeValue = -1;
				
			}
			DtvTunerStatus tunerStatus = mChannelManager.getDtvTunerStatus();
			if(tunerStatus!=null){
				mErrorRateValue = tunerStatus.mBitErrorRate();
				mSignalStrengthValue = tunerStatus.getSignalLevel(); 
				mSignalQualityValue = tunerStatus.getSignalQuality(); 
			}else{
				Log.e(TAG,"LL tunerStatus == null");
				mErrorRateValue = 0;
				mSignalStrengthValue = 0; 
				mSignalQualityValue = 0; 
			}
			
			mChannelNum.setValue(mChannelNumValue+ "");
			mOrgNetTSServiceID.setValue(mOrgNetIDValue+"/"+mTSIDValue+"/"+mServiceIDValue);
			mAudioVedioPID.setValue(mAudioPIDValue+"/"+mVedioPIDValue);
			
			String audioType = dtvStreamTypeConvert(mAudioTypeValue);
			String vedioType = dtvStreamTypeConvert(mVedioTypeValue);
			 if(null == audioType){
				mAudioVedioType.setValue(vedioType);
			}else if (null == vedioType) {
				mAudioVedioType.setValue(audioType);
			}else {
				mAudioVedioType.setValue(audioType + "/" + vedioType);
			}
			
			mQam.setValue(mQamValue + "QAM");
			
			if (ConstDemodType.DVB_C == demodType) {
				mSymbolRate.setValue(mSymbolRateValue+" Kbps");
				mFrequency.setValue(mFrequencyValue / 1000 +" MHz");
				
			}else {
				mFrequency.setValue(mFrequencyValue / 1000.0 +" MHz");
				mBandwidth.setValue(bandWidth);
			
			}
		
			if(null != dtvCurProgram){
				if(dtvCurProgram.isScrambled()){
					mChannelNameValue = mChannelNameValue.concat("    " + getContext().getString(R.string.dtv_info_cryption));
//					mEncodeData.setValue(":" +getContext().getString(R.string.dtv_info_cryption));
				}else {
					mChannelNameValue = mChannelNameValue.concat("    " + getContext().getString(R.string.dtv_info_encryption));
//					mEncodeData.setValue(":"+ getContext().getString(R.string.dtv_info_encryption));
				}
			}
			mErrorRate.setValue(mErrorRateValue+"");
			
		}else{
			Log.e(TAG,"LL dtvCurProgram == null");
			mChannelNumValue = 0;
			mChannelNameValue = "";
			mOrgNetIDValue = 0;
			mTSIDValue = 0;
			mServiceIDValue = 0;
			mAudioModeValue = "";
			mAudioTrackValue = "";
			mFrequencyValue = 0;
			mSymbolRateValue = 0;
			mQamValue = "";
			mAudioPIDValue = 0;
			mVedioPIDValue = 0;
			mAudioTypeValue = 0;
			mVedioTypeValue = 0;
			mErrorRateValue = 0;
			mSignalStrengthValue = 0; 
			mSignalQualityValue = 0; 
			bandWidth = "";
		
			mChannelNum.setValue("");
			mOrgNetTSServiceID.setValue("");
			mAudioVedioPID.setValue("");
			mAudioVedioType.setValue("");
		
			mFrequency.setValue("");
			mQam.setValue("");
			mErrorRate.setValue("");
		if (ConstDemodType.DVB_C == demodType) {
			mSymbolRate.setValue("");
		    mEncodeData.setValue(":" );
					
		}else {
			mBandwidth.setValue(bandWidth);
			
			}
		}
	
		mAudioMode.setValue(mAudioModeValue +"      "+mAudioTrackValue);
		mAudioTrack.setValue(mAudioTrackValue);
		mChannelName.setValue(mChannelNameValue);
		mSignalStrength.setBlueCount(mSignalStrengthValue/10);
		mSignalQuality.setBlueCount(mSignalQualityValue/10);
	}
	
	public void addPageItem(){
		demodType = mDemodeTypeManager.getCurDemodeType();
		this.clear();
		this.add(mChannelNum);
		this.add(mChannelName);
		
		if(ConstDemodType.DVB_C == demodType){
//			this.add(mEncodeData);
			this.add(mFrequency);
			this.add(mSignalStrength);
			this.add(mSignalQuality);
			this.add(mAudioMode);
			this.add(mAudioVedioType);
//			this.add(mAudioTrack);
			this.add(mSymbolRate);
			this.add(mQam);
			this.add(mErrorRate);
//			this.add(mAudioVedioPID);
			
//			this.add(mOrgNetTSServiceID);
			
		}else {
			this.add(mFrequency);
 			this.add(mBandwidth);
			this.add(mSignalStrength);
			this.add(mSignalQuality);
			this.add(mAudioMode);
//			this.add(mAudioTrack);
//			this.add(mAudioVedioPID);
			this.add(mAudioVedioType);
		}
	}
	public Context getContext() {
		return mContext;
	}

	public String dtvStreamTypeConvert(int  ri_StreamType)
	 {
	     String  Ret = null;
	     int baseType = ri_StreamType & 0x00ff;
	     Log.i(TAG, "dtvStreamTypeConvert() ri_StreamType:<" + Integer.toHexString(ri_StreamType) + ">; " + ri_StreamType );
	     switch(baseType)
	     {
	     	case -1:
	     		Ret = null;
	     		break;
	         /**  Value: 0x00;       Description:ITU-T | ISO/IEC Reserved **/
	         case 0x00:
	        	 Ret= "MPEG1";
	        	 break;
	         /**  Value: 0x01;       Description:ISO/IEC 11172 Video     **/
	         case 0x1:/**  编码格式<MPEG-I视频>;    类型<视频>*/
	         {
	             Ret= "MPEG2";
	         }break;

	         /**  Value: 0x02;       Description:ITU-T Rec. H.262 | ISO/IEC 13818-2 Video or ISO/IEC 11172-2 constrained parameter video stream     **/
	         case 0x2:/*编码格式<MPEG-II视频>;    类型<视频>*/
	         {
	             Ret="MPEG2";
	         }break;

	         /**  Value: 0x03;       Description:ISO/IEC 11172 Audio   **/
	         case 0x3:/*编码格式<MPEG-I音频>;    类型<音频>*/
	         {
	             Ret= "MPEG1";
	         }break;

	         /**  Value: 0x04;       Description:ISO/IEC 13818-3 Audio   **/
	         case 0x4:/*编码格式<MPEG-II音频>;    类型<音频>*/
	         {
	             Ret="MPEG2";
	         }break;

	         /**  Value: 0x06;       Description:ITU-T Rec. H.222.0 | ISO/IEC 13818-1 PES packets containing private data   **/
	         case 0x6:/*编码格式<AC-3音频*>;    类型<音频>*/
	         {
	             Ret="Dolby D";
	         }break;

	         case 0xF:/*编码格式<ISO/IEC 13818-7 Audio with ADTS transport syntax(AACMpeg2/HEAACMpeg2)>;    类型<音频>*/
	         {
	             Ret= "AAC"; //CH_AUDIO_CODE_AAC_ADTS;
	         }break;

	         case 0x10:/*编码格式<MPEG-4 视频>;    类型<视频>*/
	         {
	             Ret="MPEG4";//CH_VIDEO_CODE_MPEG4_ASP;
	         }break;

	         case 0x11:/*编码格式<ISO/IEC 14496-3 Audio with the LATM transport syntax as defined in ISO/IEC 14496-3/Amd.1 (AACMpeg4/HEAACMpeg4)>;    类型<音频>*/
	         {
	             Ret= "AAC";//CH_AUDIO_CODE_AAC_ADTS;
	         }break;

	         case 0x1B:/*编码格式<H.264视频>;    类型<视频>*/
	         {
	             Ret="H264"; //CH_VIDEO_CODE_H264;
	         }break;

	         case 0x1C:/*编码格式<ISO/IEC 14496-3 Audio, without using any additional transport syntax, such as DST, ALS and SLS>;    类型<音频>*/
	         {
	             Ret="DTS"; //CH_AUDIO_CODE_DTS;
	         }break;

	         case 0x42:/*编码格式<国标AVS视频>;    类型<视频>*/
	         {
	             Ret="AVS"; //CH_VIDEO_CODE_AVS;
	         }break;

	         case 0x65:/*编码格式<DRA音频>;    类型<音频>*/
	         {
	             Ret="DRA"; //CH_AUDIO_CODE_DRA;
	        	 int result = (ri_StreamType >> 24)& 0xf;
	        	 if(result != 0){
	        		 Ret = Ret.concat(result + ".0");
	        		 Log.i(TAG, "ChanenlInfo DRA version : " +Ret);
	        	 }
	         }break;

	         case 0x81:/*编码格式<AC-3音频>;    类型<音频>*/
	         {
	             Ret="Dolby D";  //CH_AUDIO_CODE_AC3;
	         }break;

	         case 0x87:/*编码格式<EAC-3音频>;    类型<音频>*/
	         {
	             Ret="Dolby D+";//CH_AUDIO_CODE_AC3_PLUS;
	         }break;

	         case 0xEA:/*编码格式<VC1视频>;    类型<视频>*/
	         {
	             Ret="VC1";//CH_VIDEO_CODE_VC1;
	         }break;
	         
	         default:
	         {
	        	 Ret = "MPEG2";
	         }break;
	     }

	     return Ret;
	 }
}
