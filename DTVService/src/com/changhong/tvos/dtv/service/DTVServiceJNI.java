package com.changhong.tvos.dtv.service;

import com.changhong.tvos.dtv.service.jni.avplayer;
import com.changhong.tvos.dtv.service.jni.caci;
import com.changhong.tvos.dtv.service.jni.channelmanager;
import com.changhong.tvos.dtv.service.jni.DFA;
import com.changhong.tvos.dtv.service.jni.epg;
import com.changhong.tvos.dtv.service.jni.nvod;
import com.changhong.tvos.dtv.service.jni.scan;
import com.changhong.tvos.dtv.service.jni.settings;
import com.changhong.tvos.dtv.service.jni.system;

public class DTVServiceJNI {

	/**
	 * avplayer
	 *
	 * @return
	 */
	public static avplayer get_avplayer_instance() {
		return avplayer.getinstance();
	}

	/**
	 * epg
	 *
	 * @return
	 */
	public static epg get_epg_instance() {
		return epg.getinstance();
	}

	/**
	 * scan
	 *
	 * @return
	 */
	public static scan get_scan_instance() {
		return scan.getinstance();
	}

	/**
	 * DTVSetting
	 *
	 * @return
	 */
	public static settings get_settings_instance() {
		return settings.getinstance();
	}

	/**
	 * system
	 *
	 * @return
	 */
	public static system get_system_instance() {
		return system.getinstance();
	}

	/**
	 * NVOD
	 *
	 * @return
	 */
	public static nvod get_nvod_instance() {
		return nvod.getinstance();
	}

	/**
	 * CICA
	 *
	 * @return
	 */
	public static caci get_caci_instance() {
		return caci.getinstance();
	}

	/**
	 * channelmanager
	 *
	 * @return
	 */
	public static channelmanager get_channelmanager_instance() {
		return channelmanager.getinstance();
	}

	/**
	 * DFA
	 *
	 * @return
	 */
	public static DFA get_dfa_instance() {
		return DFA.getinstance();
	}
}
