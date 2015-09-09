package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.CICAMMessageBase;

public interface cacicallback {
	 //public int Prompt_notify_Callback(int routerID,CICAMMessageBase obj);
	 public int cica_notify_Callback(int routerID,CICAMMessageBase obj);
}
