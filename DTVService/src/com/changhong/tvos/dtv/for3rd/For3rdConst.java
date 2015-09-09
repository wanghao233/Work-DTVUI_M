/**
 * @filename
 * 	TV DTV(com.changhong.tvos.dtvapi.jar)API
 * @author:
 * @date: 
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

/** for 3rd const **/
public abstract class For3rdConst{
	
	public abstract class ConstantAudioEncodeType {
		/** MPEG1 **/
		public static final int AUDIO_CODE_MPEG1 = 0;

		/** MPEG2 **/
		public static final int AUDIO_CODE_MPEG2 = 1;

		/** MP3 **/
		public static final int AUDIO_CODE_MP3 = 2;

		/** AC3 **/
		public static final int AUDIO_CODE_AC3 = 3;

		/** AAC **/
		public static final int AUDIO_CODE_AAC_ADTS = 4;

		/** AAC **/
		public static final int AUDIO_CODE_AAC_LOAS = 5;

		/** HEAAC **/
		public static final int AUDIO_CODE_HEAAC_ADTS = 6;

		/** HEAAC **/
		public static final int AUDIO_CODE_HEAAC_LOAS = 7;

		/** WMA **/
		public static final int AUDIO_CODE_WMA = 8;

		/** AC3 **/
		public static final int AUDIO_CODE_AC3_PLUS = 9;

		/** LPCM **/
		public static final int AUDIO_CODE_LPCM = 10;

		/** DTS **/
		public static final int AUDIO_CODE_DTS = 11;

		/** ATRAC **/
		public static final int AUDIO_CODE_ATRAC = 12;
		
		/** UNKNOWN **/
		public static final int AUDIO_CODE_UNKNOWN = -1;
	}

	/** VideoEncodeType **/
	public abstract class ConstantVideoEncodeType {
		/** MPEG2 **/
		public static final int VIDEO_CODE_MPEG2 = 0;

		/** MPEG2 HD **/
		public static final int  VIDEO_CODE_MPEG2_HD = 1;

		/** MPEG4 ASP **/
		public static final int  VIDEO_CODE_MPEG4_ASP = 2;

		/** MPEG4 ASP_A **/
		public static final int  VIDEO_CODE_MPEG4_ASP_A = 3;

		/** MPEG4_ASP_B **/
		public static final int  VIDEO_CODE_MPEG4_ASP_B = 4;

		/** MPEG4_ASP_C **/
		public static final int  VIDEO_CODE_MPEG4_ASP_C  = 5;

		/** DIVX **/
		public static final int   VIDEO_CODE_DIVX = 6;

		/** VC1 **/
		public static final int   VIDEO_CODE_VC1 = 7;
	
		/** H264 **/
		public static final int   VIDEO_CODE_H264 = 8;
		
		/** UNKNOWN **/
		public static final int   VIDEO_CODE_UNKNOWN = -1;
	}

	/** ServiceType **/
    public abstract class ConstantServiceType {
		/** ALL */
    	public static final int   DTV_RPOGRAM_TYPE_ALL  = 0;
		
		/** TY **/
    	public static final int   DTV_PROGRAM_TYPE_TV = 1;

		/** RADIO **/
    	public static final int   DTV_PROGRAM_TYPE_RADIO = 2;

		/** UNKNOWN **/
    	public static final int   DTV_PROGRAM_TYPE_UNKNOWN = -1;
	}
    
    /** AudioMode **/
	public abstract class ConstantAudioMode {
		/** MONO **/
		public static final int   AUDIO_MODE_MONO = 0;

		/** STERO **/
		public static final int   AUDIO_MODE_STERO = 1;
		
		/** UNKNOWN **/
		public static final int   AUDIO_MODE_UNKNOWN = -1;
	}

	/** PlayerNotifyEvent **/
	public abstract class ConstantPlayerNotifyEvent{
		/** ok **/
		public static final int  OK = 0;

		/** is 3d **/
		public static final int SOURCE_IS_3D = 1;
		
		/** channel info changed **/
		public static final int CHANNEL_INFO_CHANGED = 2;
		
		/** stop **/
		public static final int STOPED = 3;

		/** no signal **/
		public static final int NO_SIGNAL = 4;

		/** no smart card **/
		public static final int NO_SAMRT_CARD = 5;

		/** smart card erro **/
		public static final int SAMRT_CARD_ERROR = 6;

		/** ca not entitle  **/
		public static final int CA_NOT_ENTITLE = 7;

		/** force pause **/
		public static final int FORCE_PAUSE = 8;
		
		/** force resume **/
		public static final int FORCE_RESUME = 9;
		
		/** error **/
		public static final int STATUS_ERROR = 10;
		
		/** no surpported **/
		public static final int NOT_SURPPORTED = -1;
	}

	/** AudioEncodeType **/
	public abstract class ConstAudioEncodeType {
		/** MPEG1 **/
		public static final int	AUDIO_CODE_MPEG1 = 0;

		/** MPEG2 **/
		public static final int	AUDIO_CODE_MPEG2 = 1;

		/** MP3 **/
		public static final int	AUDIO_CODE_MP3 = 2;

		/** AC3 **/
		public static final int	AUDIO_CODE_AC3 = 3;

		/** AAC_ADTS **/
		public static final int	AUDIO_CODE_AAC_ADTS = 4;

		/** AAC_LOAS **/
		public static final int	AUDIO_CODE_AAC_LOAS = 5;

		/** HEAAC_ADTS **/
		public static final int	AUDIO_CODE_HEAAC_ADTS = 6;

		/** HEAAC_LOAS **/
		public static final int	AUDIO_CODE_HEAAC_LOAS = 7;

		/** WMA **/
		public static final int	AUDIO_CODE_WMA = 8;

		/** AC3_PLUS **/
		public static final int	AUDIO_CODE_AC3_PLUS = 9;

		/** LPCM **/
		public static final int	AUDIO_CODE_LPCM  =10;

		/** DTS **/
		public static final int	AUDIO_CODE_DTS = 11;

		/** ATRAC **/
		public static final int	AUDIO_CODE_ATRAC = 12;
	}
	

	/** VideoEncodeType **/
	public abstract class VideoEncodeType {
		/** MPEG2 **/
		public static final int	VIDEO_CODE_MPEG2 = 0;

		/** MPEG2_HD **/
		public static final int	VIDEO_CODE_MPEG2_HD = 1;

		/** MPEG4_ASP **/
		public static final int	VIDEO_CODE_MPEG4_ASP = 3;

		/** MPEG4_ASP_A **/
		public static final int	VIDEO_CODE_MPEG4_ASP_A = 4;

		/** MPEG4_ASP_B **/
		public static final int	VIDEO_CODE_MPEG4_ASP_B = 5;

		/** MPEG4_ASP_C **/
		public static final int	VIDEO_CODE_MPEG4_ASP_C = 6;

		/** DIVX **/
		public static final int	VIDEO_CODE_DIVX = 7;

		/** VC1 **/
		public static final int	VIDEO_CODE_VC1 = 8;
	
		/** H264 **/
		public static final int	VIDEO_CODE_H264 = 9;
	}	
	
	/** DTVSERVICE **/
    public static final String DTVSERVICE="DTVService";
}