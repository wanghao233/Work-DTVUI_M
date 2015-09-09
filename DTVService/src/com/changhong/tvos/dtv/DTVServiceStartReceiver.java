package com.changhong.tvos.dtv;

import com.changhong.tvos.dtv.vo.DTVConstant;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DTVServiceStartReceiver extends BroadcastReceiver {
	public final String TAG = "dtvservice.BroadcastReceiver ";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Intent itent = new Intent(DTVConstant.DTV_SERVICE_NAME);
		context.startService(itent);

		Log.i(TAG, "get the broadcast!!!!!!!!!!");
	}
}
