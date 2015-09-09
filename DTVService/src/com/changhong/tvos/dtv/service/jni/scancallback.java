package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.ScanStatusInfo;


public interface scancallback {
  public int ScanStatusInfo_Callback(int routerID,ScanStatusInfo obj,CarrierInfo[] lCarrier, DTVChannelBaseInfo[] lService, DTVTunerStatus tunerstatus);
}
