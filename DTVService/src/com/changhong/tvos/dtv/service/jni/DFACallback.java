package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.CH_DTV_DFA_MsgDownloadStatus;
import com.changhong.tvos.dtv.CH_DTV_NIT_DESC_STRUCT;

public interface DFACallback {
	/**
	 * 
	 * @param mi_node_number
	 * @param obj_NIT_DESC
	 */
	void DFA_Monitor_CallBack(int mi_node_number,CH_DTV_NIT_DESC_STRUCT obj_NIT_DESC[]);
	/**
	 * 
	 * @param obj_DownloadStatus
	 */
	void DFA_DownloadStatus_CallBack(CH_DTV_DFA_MsgDownloadStatus obj_DownloadStatus);
	
	
	//void DFA_Monitor_CallBack(CH_DTV_DFA_MsgTrigger obj_trigger);
	//void DFA_DownloadStatus_CallBack(CH_DTV_DFA_MsgProgress obj_progress,CH_DTV_DFA_MsgResult obj_result);
}
