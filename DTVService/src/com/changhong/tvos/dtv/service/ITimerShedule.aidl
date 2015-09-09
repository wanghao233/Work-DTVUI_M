package com.changhong.tvos.dtv.service;

import java.util.List;
import com.changhong.tvos.dtv.vo.TimerInfo;

interface ITimerShedule {
	int addTimer(in TimerInfo timer);
	int deleteTimer(in TimerInfo timer);
	List<TimerInfo> getTimerList(int type);
	int deleteAllTimer();
}