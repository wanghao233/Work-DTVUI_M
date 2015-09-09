package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.VersionInfo;

public class DtvVersion{
	
	private int mHardwareVersion;//硬件版本 
	private int mOpVersion; //运营商版本 
	private int mAPIMainVersion; //API主版本
	private int mAPISubVersion; //API次版本
	private int mMainVersion; // 软件主版本 
	private int mSubVersion; // 软件次版本 
	private String mReleaseDateTime = ""; // 发布日期时间
	private int mKOVersion;			//KO版本
	public DtvVersion(VersionInfo dtvVersion) {
		// TODO Auto-generated constructor stub
		if(null != dtvVersion){			
			mHardwareVersion =dtvVersion.miHardwareVersion;
			mOpVersion =dtvVersion.miOpVersion;
			mAPIMainVersion = dtvVersion.miAPIMainVersion;
			mAPISubVersion = dtvVersion.miAPISubVersion;
			mMainVersion =dtvVersion.miMainVersion;
			mSubVersion =dtvVersion.miSubVersion;
			mReleaseDateTime =dtvVersion.mstrReleaseDateTime;
			mKOVersion = dtvVersion.miKOVersion;
		}
	}
	public int getHardwareVersion() {
		return mHardwareVersion;
	}
	public int getOpVersion() {
		return mOpVersion;
	}
	public int getAPIMainVersion() {
		return mAPIMainVersion;
	}
	public int getAPISubVersion() {
		return mAPISubVersion;
	}
	public int getMainVersion() {
		return mMainVersion;
	}
	public int getSubVersion() {
		return mSubVersion;
	}
	public String getReleaseDateTime() {
		return mReleaseDateTime;
	}
	public int getKOVersion() {
		return mKOVersion;
	}
}
