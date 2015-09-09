package com.changhong.tvos.dtv.service.jni;

public class pvr {

	private static pvr instance = null;
	protected  pvr(){
		
	}
	public static synchronized pvr getinstance(){
		if(instance == null)
			instance = new pvr();
		return instance;
	}
	
	public native int prepare();
	public native int release();
    public native int recStart(int channelIndex,java.io.FileDescriptor fd,int length);
    public native int recStop(int channelIndex);
    public native int registerCallback(int channelIndex,pvrcallback callback);
    public native int unregisterCallback(int channelIndex,pvrcallback callback);
    public native int getRecLength(int channelIndex);
    public native int refreshRecBuffer(int channelIndex);
	
}
