/**
 * @filename DTV
 * @author:
 * @date:
 * @version 0.1
 * <p/>
 * history:
 * 2012-7-17:
 * 2012-7-17:
 * 2012-9-13
 */
package com.changhong.tvos.dtv.vo;

/**
 *
 **/
public class DTVConstant {

    /**  */
    public final static int DTV_OK = 0;

    /**  */
    public abstract class ErrorCode {
        /**  **/
        public final static int ERROR_INVALID_PARAM = -1;

        /**  **/
        public final static int ERROR_NOT_SUPPORT = -1;

        /**  **/
        public final static int ERROR_FAILED = -3;

        /**  **/
        public final static int ERROR_BINDER_FAILD = -4;
    }

    /**  **/
    public abstract class ConstVideoLayerType {

        /**  **/
        public final static int VIDEO_LAYER = 0;

        /**
         * OSD
         **/
        public final static int VIDEO_OSD_SURFACE = 1;
    }

    /**
     *
     */
    public abstract class BroadcastConst {
        public static final String MSG_TYPE_NAME = "Msg_Type";

        public static final String MSG_INFO_NAME = "Msg_Info";

        public static final String MSG_INFO_NAME_1 = "Msg_Info1";

        public static final String CLIENT_UUID_NAME = "Msg_Client_uuid";
    }


    public static final String SCAN_STATUS_BROADCAST =
            "com.changhong.tvos.dtv.SCAN_STATUS_BROADCAST";

    public static final String CHANNEL_INFO_CHANGED_BROADCAST =
            "com.changhong.tvos.dtv.CHANNEL_INFO_CHANGED_BROADCAST";


    public abstract class ConstScanMode {
        /**  **/
        public final static int SCAN_MODE_NIT = 1;

        /**  **/
        public final static int SCAN_MODE_LIST = 2;

        /**  **/
        public final static int SCAN_MODE_MANUAL = 3;
    }

    /**
     *
     */
    public abstract class DFAConst {
        public static final String DFA_STATUS_BROADCAST =
                "com.changhong.tvos.dtv.DFA_STATUS_BROADCAST";

        public static final int DFA_MSG_TYPE_TRIGGER = 0;

        public static final int DFA_MSG_TYPE_PROGRESS = 1;


        public static final int DFA_MSG_TYPE_RESULT = 2;
    }


    public static final String DTV_PLAYER_STATUS_CHANGE =
            "com.changhong.tvos.dtv.DTV_PLAYER_STATUS_CHANGE";

    public static final String DTV_EPG_DATA_RECIVE_COMPELETED =
            "com.changhong.tvos.dtv.DTV_EPG_DATA_RECIVE_COMPELETED";

    public static final String DTV_CICAM_PROMPT_NOTIRY =
            "com.changhong.tvos.dtv.DTV_CICAM_PROMPT_NOTIRY";

    public abstract class ConstCICAMOpCode {
        /**  **/
        public final static int DEFAULT_OP_CODE_CONFIRM = 0;

        /**  **/
        public final static int DEFAULT_OP_CODE_BACK = 1;

        /**  **/
        public final static int DEFAULT_OP_CODE_EIXT = 2;

        /**  **/
        public final static int INVALID_OPERATE_CODE = -1;
    }


    /**
     *
     */
    public abstract class startControlConst {

        public static final String DTV_START_SERVICE_INFO =
                "com.changhong.tvos.dtv.DTV_START_SERVICE_INFO";
    }

    /**  **/
    public class ConstSwitchMode {
        /**  **/
        public static final int BLACK = 0;
        /**  **/
        public static final int FRAME_FREEZE = 1;
    }

    /**
     * ״̬
     **/
    public class ConstPlayerEvent {
        /**  **/
        public static final int OK = 0;

        /**  **/
        public static final int NO_CHANNEL = 1;

        /**  **/
        public static final int NO_SIGNAL = 2;

        /**  **/
        public static final int NO_SAMRT_CARD = 3;

        /**  **/
        public static final int SAMRT_CARD_ERROR = 4;

        /**  **/
        public static final int CA_ENTITLE = 5;

        /**  **/
        public static final int FORCE_PAUSE = 6;

        /**  **/
        public static final int FORCE_RESUME = 7;

        /**  **/
        public static final int STATUS_ERROR = 8;

        /**
         * CICA
         **/
        public static final int CAM_VARY = 9;
    }


    public class ConstPlayerTvosEvent {

        public static final int MUTE = 0;

        public static final int UNMUTE = 1;

        public static final int HIDE_VIDEO = 2;

        public static final int SHOW_VIDEO = 3;

        public static final int FREEZE_VIDEO = 4;

        public static final int UNFREEZE_VIDEO = 5;

        public static final int CLEAR_VIDDEO_BUFFER = 6;
    }

    public abstract class ConstServiceType {

        public static final int SERVICE_TYPE_ALL = -1;

        public static final int SERVICE_TYPE_TV = 1;

        public static final int SERVICE_TYPE_RADIO = 2;

        public static final int SERVICE_TYPE_DATA_BROADCAST = 3;

        public static final int SERVICE_TYPE_TELETEXT_TV = 4;

        public static final int SERVICE_TYPE_NVOD_REFERENCE = 5;

        public static final int SERVICE_TYPE_NVOD_SHIFT = 6;

        public static final int SERVICE_TYPE_MASAIC = 7;

        public static final int SERVICE_TYPE_SERVICE_PAL = 8;

        public static final int SERVICE_TYPE_SERVICE_SECAM = 9;

        public static final int SERVICE_TYPE_D2_MAC = 10;

        public static final int SERVICE_TYPE_SERVICE_NTSC = 11;
    }


    public class ConstSoundMode {

        public static final int CH_DTV_SOUND_STERE0 = 0;

        public static final int CH_DTV_SOUND_LEFT = 1;

        public static final int CH_DTV_SOUND_RIGHT = 2;

        public static final int CH_DTV_SOUND_MONO = 3;

        public static final int CH_DTV_SOUND_AUTO = 4;

        public static final int CH_DTV_SOUND_MAX = 5;
    }


    public class ConstDemodType {

        public static final int DVB_C = 0;

        public static final int DVB_S = 1;

        public static final int DVB_S2 = 2;

        public static final int DVB_T = 3;

        public static final int DMB_TH = 4;

        public static final int ISDB_TB = 5;

        public static final int ISDB_S = 6;

        public static final int ISDB_T = 7;

        public static final int ATSC = 8;
    }

    /**
     * QAM
     */
    public class ConstQAMMode {

        public static final int QAM_4 = 0;

        public static final int QAM_8 = 1;

        public static final int QAM_16 = 2;

        public static final int QAM_32 = 3;

        public static final int QAM_64 = 4;

        public static final int QAM_128 = 5;

        public static final int QAM_256 = 6;

        public static final int QAM_512 = 7;

        public static final int QAM_1024 = 8;
    }


    /**
     * ConstBandWidth
     */
    public abstract class ConstBandWidth {

        public static final int BANDWIDTH_6M = 1;

        public static final int BANDWIDTH_7M = 1;

        public static final int BANDWIDTH_8M = 1;
    }

    /**
     * ConstQPSKMode
     */
    public abstract class ConstQPSKMode {

        public static final int AUTO = 0;

        public static final int BPSK = 1;

        public static final int QPSK = 2;

        public static final int MODE_8PSK = 3;
    }

    /**
     * ConstSpectrumMode
     */
    public abstract class ConstSpectrumMode {

        public static final int AUTO = 0;

        public static final int NORMAL = 1;

        public static final int INVERTED = 2;
    }

    /**
     * ConstLNBPowerModeType
     */
    public abstract class ConstLNBPowerModeType {

        public static final int LNB_POWER_AUTO = 0;

        public static final int LNB_POWER_13V = 1;

        public static final int LNB_POWER_18V = 2;
    }

    /**
     * ConstPolarizationMode
     */
    public abstract class ConstPolarizationMode {

        public static final int POLARIZATION_V = 0;

        public static final int POLARIZATION_H = 1;
    }


    /**
     * ConstTone22KhzState
     */
    public abstract class ConstTone22KhzState {

        public static final int TONE_22K_AUTO = 0;

        public static final int TONE_22K_ON = 1;

        public static final int TONE_22K_OFF = 2;
    }

    /**
     * ConstDiseqc10Port
     */
    public abstract class ConstDiseqc10Port {

        public static final int DISEQC10_PORT_A = 0;

        public static final int DISEQC10_PORT_B = 1;

        public static final int DISEQC10_PORT_C = 2;

        public static final int DISEQC10_PORT_D = 3;
    }

    /**
     * ConstDiseqcVersion
     */
    public abstract class ConstDiseqcVersion {

        public static final int DISEQC_10 = 0;

        public static final int DISEQC_11 = 1;

        public static final int DISEQC_12 = 2;

        public static final int DISEQC_USALS = 3;

        public static final int DISEQC_20 = 4;

        public static final int DISEQC_23 = 5;
    }

    /**
     * ConstDiseqc12Direction
     */
    public abstract class ConstDiseqc12Direction {

        public static final int DIRECTION_EAST = 0;

        public static final int DIRECTION_WEST = 1;
    }


    /**
     * ConstDMBTHCarrierMode
     */
    public abstract class ConstDMBTHCarrierMode {

        public static final int SINGLE_CARRIER = 0;

        public static final int MULTI_CARRIER = 1;
    }

    /**
     * ConstDTMBTHQAMMode
     */
    public abstract class ConstDTMBTHQAMMode {

        public static final int DMBTH_QAM4_NR = 0;

        public static final int DMBTH_QAM4 = 1;

        public static final int DMBTH_QAM16 = 2;

        public static final int DMBTH_QAM32 = 3;

        public static final int DMBTH_QAM64 = 4;

    }

    /**
     * ConstDMBTHLDPCRate
     */
    public abstract class ConstDMBTHLDPCRate {
        /**
         * 0.4
         **/
        public static final int DMBTH_LDPC_04 = 0;

        /**
         * 0.6
         **/
        public static final int DMBTH_LDPC_06 = 1;

        /**
         * 0.8
         **/
        public static final int DMBTH_LDPC_08 = 2;
    }

    /**
     * ConstDMBTHFrameHeader
     */
    public abstract class ConstDMBTHFrameHeader {
        /**
         * PN945
         **/
        public static final int DMBTH_PN945 = 0;

        /**
         * PN595
         **/
        public static final int DMBTH_PN595 = 1;

        /**
         * PN420
         **/
        public static final int DMBTH_PN420 = 2;
    }

    /**
     * ConstDMBTHInterleaverMode
     */
    public abstract class ConstDMBTHInterleaverMode {

        public static final int DMBTH_INTERLEAVER_M720 = 0;

        public static final int DMBTH_INTERLEAVER_M240 = 1;
    }

    /**
     * ConstAudioEncodeType
     */
    public abstract class ConstAudioEncodeType {

        public static final int AUDIO_CODE_MPEG1 = 0;

        public static final int AUDIO_CODE_MPEG2 = 1;

        public static final int AUDIO_CODE_MP3 = 2;

        public static final int AUDIO_CODE_AC3 = 3;

        public static final int AUDIO_CODE_AAC_ADTS = 4;

        public static final int AUDIO_CODE_AAC_LOAS = 5;

        public static final int AUDIO_CODE_HEAAC_ADTS = 6;

        public static final int AUDIO_CODE_HEAAC_LOAS = 7;

        public static final int AUDIO_CODE_WMA = 8;

        public static final int AUDIO_CODE_AC3_PLUS = 9;

        public static final int AUDIO_CODE_LPCM = 10;

        public static final int AUDIO_CODE_DTS = 11;

        public static final int AUDIO_CODE_ATRAC = 12;
    }


    /**
     * VideoEncodeType
     */
    public abstract class VideoEncodeType {

        public static final int VIDEO_CODE_MPEG2 = 0;

        public static final int VIDEO_CODE_MPEG2_HD = 1;

        public static final int VIDEO_CODE_MPEG4_ASP = 3;

        public static final int VIDEO_CODE_MPEG4_ASP_A = 4;

        public static final int VIDEO_CODE_MPEG4_ASP_B = 5;

        public static final int VIDEO_CODE_MPEG4_ASP_C = 6;

        public static final int VIDEO_CODE_DIVX = 7;

        public static final int VIDEO_CODE_VC1 = 8;

        public static final int VIDEO_CODE_H264 = 9;
    }

    /**
     * ConstVideoAspectRatio
     */
    public abstract class ConstVideoAspectRatio {
        /**
         * 4:3
         */
        public static final int VIDEO_ASPECT_4X3 = 0;

        /**
         * 16:9
         */
        public static final int VIDEO_ASPECT_16X9 = 1;
    }

    /**
     * ConstVideoAspectMode
     */
    public abstract class ConstVideoAspectMode {
        /**
         * Pan/scan
         */
        public static final int VIDEO_PANSCANs = 0;

        /**
         * Letterbox
         */
        public static final int VIDEO_LETTERBOX = 1;

        /**
         * Combine
         */
        public static final int CHMID_AVM_VIDEO_COMBINE = 2;

        /**
         * FULL
         */
        public static final int CHMID_AVM_VIDEO_FULL = 3;
    }

    /**
     * ConstRFOutFormat
     */
    public abstract class ConstRFOutFormat {
        public static final int NTSC_433 = 0;
        public static final int NTSC_J = 1;
        public static final int NTSC_M = 2;
        public static final int PAL_G = 3;
        public static final int PAL_I = 4;
        public static final int PAL_M = 5;
        public static final int PAL_DK = 6;
        public static final int PAL_N = 7;
    }

    /**
     * ConstOutPort
     */
    public abstract class ConstOutPort {

        public static final int VIDEO_PORT_CVBS = 0x000001;

        public static final int VIDEO_PORT_YC = 0x000002;

        public static final int VIDEO_PORT_YCrCb = 0x000004;

        public static final int VIDEO_PORT_RGB = 0x000008;

        public static final int VIDEO_PORT_VGA = 0x000100;

        public static final int VIDEO_PORT_YPbPr = 0x000200;

        public static final int VIDEO_PORT_DVI = 0x000400;

        public static final int VIDEO_PORT_HDMI = 0x000800;
    }

    /**
     * ConstOutPortFormat
     */
    public abstract class ConstOutPortFormat {

        public static final int FMT_480I60 = 0;

        public static final int FMT_576I50 = 1;

        public static final int FMT_576I60 = 2;

        public static final int FMT_576I50_SECAM = 3;

        public static final int FMT_HD_480P60 = 4;

        public static final int FMT_HD_576P50 = 5;

        public static final int FMT_HD_720P50 = 6;

        public static final int FMT_HD_720P60 = 7;

        public static final int FMT_HD_1080I50 = 8;

        public static final int FMT_HD_1080I60 = 9;

        public static final int FMT_HD_1080P50 = 10;
    }

    /**
     * ConstLayerType
     */
    public abstract class ConstLayerType {

        public static final int LAYER_BG = 0;

        public static final int LAYER_STILL = 1;

        public static final int LAYER_CURSOR = 2;

        public static final int LAYER_OSD1 = 0x10;

        public static final int LAYER_OSD2 = 0x11;

        public static final int LAYER_VIDEO1 = 0x40;

        public static final int LAYER_VIDEO2 = 0x41;
    }

    /**
     * ConstScartOutMode
     */
    public abstract class ConstScartOutMode {

        public static final int SCART_MODE_RF = 0;

        public static final int SCART_MODE_CVBS = 1;

        public static final int SCART_MODE_RGB = 12;

        public static final int CHMID_AVM_SCART_MODE_YC = 13;
    }

    /**
     * ConstChannelSortType
     */
    public abstract class ConstChannelSortType {

        public static final int SORT_BY_DEFUALT = 0;

        public static final int SORT_BY_SERVICE_ID = 1;

        public static final int SORT_BY_CHANNEL_NAME = 2;
    }

    /**
     * ConstCICAMMenuType
     **/
    public abstract class ConstCICAMMenuType {

        public static final int MENU_LIST = 0;

        public static final int MENU_INPUT = 1;

        public static final int MENU_CONFIRM = 2;

        public static final int MENU_NORMAL = 3;
    }

    /**
     * ConstCICAMMenuID
     **/
    public abstract class ConstCICAMMenuID {

        public final static int MENU_ID_ROOT = 0;

        public final static int MENU_ID_MAIN = 1;

        public final static int MENU_ID_NOMENU = 0xefffffff;

        public final static int MENU_ID_INVALID = 0xffffffff;
    }

    public abstract class ConstSourceType {

        public static final int SOURCE_DVBC = 0;
        public static final int SOURCE_DVBT = 1;
        public static final int SOURCE_DVBS = 2;
        public static final int SOURCE_DMBTH = 3;
    }

    /**
     * dtv service name
     ***/
    public static final String DTV_SERVICE_NAME = "DTVService";
}
