package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.PlayStatusInfo;
import com.changhong.tvos.dtv.vo.StartControlInfo;

public interface avplayercallback {
	  public int player_TvosCallback(int routerID, int event);
	  public int playerStatusCallbck(int routerID, PlayStatusInfo info);
	  public int startServiceCallback(int routerID, StartControlInfo info);
}
