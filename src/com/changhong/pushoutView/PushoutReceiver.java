package com.changhong.pushoutView;

import com.changhong.inface.net.NetworkDCC.NetworkDCCUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushoutReceiver extends BroadcastReceiver {
	private static String TAG = "DTVpushview_PushoutReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(TAG, "broadcast has been received"+intent.getAction());
		
		if(NetworkDCCUtils.Network_DCC_DEVICE_SYNC_VIDEO_POSTER_BROADCAST.equals(intent.getAction())) {
			
			int devicePort = intent.getIntExtra(NetworkDCCUtils.Network_DCC_Device_Port, 0x40);
			Intent mIntent = new Intent(context, PushoutService.class);
			mIntent.putExtra("control", "deviceready");
			mIntent.putExtra("deviceport", devicePort);
			context.startService(mIntent);
		} 
	}
}
