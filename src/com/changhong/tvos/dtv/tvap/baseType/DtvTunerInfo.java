package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.CarrierInfo;

public class DtvTunerInfo {
	private int mFrequency;
	private int mSymbolRate;
	private int mQamMode;

	private int mi_CarrierMode; /* 载波模式 */
	private int mi_FreqKHz; /* 下行频率，单位KHz */
	private int mi_NCOFreqKHz; /* NCO频率，单位KHz */
	private int mi_LDPCRate; /* LDCP（内码纠错）码率定义 */
	private int mi_FrameHeader; /* 帧头格式定义 */
	private int mi_InterleaverMod; /* 交织模式 */

	public DtvTunerInfo(CarrierInfo carrierInfo) {
		
		this.mFrequency = carrierInfo.miFrequencyK;
		this.mSymbolRate = 6875;
		this.mQamMode = 5;
		this.mi_FreqKHz = mFrequency;
	}

	public DtvTunerInfo(int mFrequency, int mSymbolRate, int mQamMode) {
		this.mFrequency = mFrequency;
		this.mSymbolRate = mSymbolRate;
		this.mQamMode = mQamMode;
		this.mi_FreqKHz = mFrequency;
	}

	public DtvTunerInfo( int mi_CarrierMode, int mi_FreqKHz, int mi_NCOFreqKHz,int mQamMode, int mi_LDPCRate,
			int mi_FrameHeader, int mi_InterleaverMod) {

		this.mi_CarrierMode = mi_CarrierMode;
		this.mi_FreqKHz = mi_FreqKHz;
		this.mi_NCOFreqKHz = mi_NCOFreqKHz;
		this.mQamMode = mQamMode;
		this.mi_LDPCRate = mi_LDPCRate;
		this.mi_FrameHeader = mi_FrameHeader;
		this.mi_InterleaverMod = mi_InterleaverMod;
		
		this.mFrequency = mi_FreqKHz;
	}

	public int getFrequency() {
		return mFrequency;
	}

	public int getSymbolRate() {
		return mSymbolRate;
	}

	public int getQamMode() {
		return mQamMode;
	}

	public void setMi_CarrierMode(int mi_CarrierMode) {
		this.mi_CarrierMode = mi_CarrierMode;
	}

	public int getMi_CarrierMode() {
		return mi_CarrierMode;
	}

	public void setMi_FreqKHz(int mi_FreqKHz) {
		this.mi_FreqKHz = mi_FreqKHz;
	}

	public int getMi_FreqKHz() {
		return mi_FreqKHz;
	}

	public void setMi_NCOFreqKHz(int mi_NCOFreqKHz) {
		this.mi_NCOFreqKHz = mi_NCOFreqKHz;
	}

	public int getMi_NCOFreqKHz() {
		return mi_NCOFreqKHz;
	}

	public void setMi_LDPCRate(int mi_LDPCRate) {
		this.mi_LDPCRate = mi_LDPCRate;
	}

	public int getMi_LDPCRate() {
		return mi_LDPCRate;
	}

	public void setMi_FrameHeader(int mi_FrameHeader) {
		this.mi_FrameHeader = mi_FrameHeader;
	}

	public int getMi_FrameHeader() {
		return mi_FrameHeader;
	}

	public void setMi_InterleaverMod(int mi_InterleaverMod) {
		this.mi_InterleaverMod = mi_InterleaverMod;
	}

	public int getMi_InterleaverMod() {
		return mi_InterleaverMod;
	}
}