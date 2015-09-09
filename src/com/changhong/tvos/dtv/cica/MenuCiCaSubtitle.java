package com.changhong.tvos.dtv.cica;

import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvCicaManager;
import com.changhong.tvos.dtv.util.ViewScrollText;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMOpCode;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public  class MenuCiCaSubtitle extends RelativeLayout{
	private static final String TAG = "MenuCiCaSubtitle";
	RelativeLayout mBgView = null;
	ViewScrollText subtitle1;
	ViewScrollText subtitle2;
	int showtime;
	private Context myContext;
	
	int mi_MsgType =-1;
	int mi_MsgID =-1;
	int mi_MenuID =-1;
	Handler myHandler;
	
	Runnable showRun;
	Runnable showTimeRun;
	Runnable scrollRun1, scrollRun2;
	public MenuCiCaSubtitle(Context context) {
		super(context);
		myContext =context;
		mBgView = (RelativeLayout) LayoutInflater.from(myContext).inflate(R.layout.menu_cica_subtitle, null);
		subtitle1 =(ViewScrollText) mBgView.findViewById(R.id.dtv_cica_subtitle1);
		subtitle2 =(ViewScrollText) mBgView.findViewById(R.id.dtv_cica_subtitle2);
		this.addView(mBgView);
		subtitle1.setText("");
		subtitle2.setText("");
		myHandler =new Handler();
		scrollRun1 =new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(subtitle1.isStopScroll())
				{
					Log.v("tv","StopScroll 1");
                    sendMsg(mi_MsgType,mi_MsgID,mi_MenuID,-1, -1,ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM, 0, null);	

				}else{
					myHandler.postDelayed(scrollRun1, 500);
				}
			}
		};
		scrollRun2 =new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(subtitle2.isStopScroll())
				{
					Log.v("tv","StopScroll 2");
					sendMsg(mi_MsgType,mi_MsgID,mi_MenuID,-1, -1,ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM, 0, null);	

				}else{
					myHandler.postDelayed(scrollRun2, 500);
				}
			}
		};
		showTimeRun =new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(mContext!=null&&(mContext instanceof Activity)&&(!((Activity)mContext).isFinishing())){
					Log.i(TAG,"LL mContext = " + mContext + "isFinish = " + ((Activity)mContext).isFinishing());
					cancel();
					Log.v("tv","showTime over");
				}
			}
		};
		showRun = new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(mContext!=null&&(mContext instanceof Activity)&&(!((Activity)mContext).isFinishing())){
					Log.i(TAG,"LL mContext = " + mContext + "isFinish = " + ((Activity)mContext).isFinishing());
					show();
					Log.v("tv","showTime start");
				}
			}
		};
	}

	public void setMenuState(int mi_MsgType, int mi_MsgID ,int mi_MenuID ){
		this.mi_MsgType =mi_MsgType;
		this.mi_MsgID =mi_MsgID;
		this.mi_MenuID =mi_MenuID;
	}
	
	public void show(String mstr_Subtitle ,int mi_Showtime) {
		// TODO Auto-generated method stub
		myHandler.removeCallbacks(showRun);
		myHandler.removeCallbacks(showTimeRun);
		showtime =mi_Showtime;
		
		switch(mi_MsgID){
		case 1:
			if(mstr_Subtitle ==null||mstr_Subtitle.equals("null")){
				subtitle1.setText("");
				subtitle1.init();
				subtitle1.stopScroll();
			}else{
				subtitle1.setText(mstr_Subtitle);
				if(showtime ==0){
					subtitle1.setScrollTimes(1);
					myHandler.removeCallbacks(scrollRun1);
					myHandler.postDelayed(scrollRun1, 1000);
				}else{
					subtitle1.setScrollTimes(-1);
				}
				subtitle1.startScroll();
			}
			break;
		case 2:
		default:	
			if(mstr_Subtitle ==null||mstr_Subtitle.equals("null")){
				subtitle2.setText("");
				subtitle2.init();
				subtitle2.stopScroll();
			}else{
				subtitle2.setText(mstr_Subtitle);
				if(showtime ==0){
					subtitle2.setScrollTimes(1);
					myHandler.removeCallbacks(scrollRun2);
					myHandler.postDelayed(scrollRun2,1000);
				}else{
					subtitle2.setScrollTimes(-1);
				}
				subtitle2.startScroll();
			}
			break;
		}
		if(subtitle1.isStopScroll() && subtitle2.isStopScroll()){
			cancel();
		}

		if(showtime <0){
			myHandler.postDelayed(showTimeRun, 10*60*1000);
		}else if(showtime >0){
			myHandler.postDelayed(showTimeRun, showtime*1000);		
		}
		show();
	}


	public void cancel() {
		// TODO Auto-generated method stub
		myHandler.removeCallbacks(scrollRun1);
		myHandler.removeCallbacks(scrollRun2);
		myHandler.removeCallbacks(showRun);
		myHandler.removeCallbacks(showTimeRun);
		setVisibility(View.GONE);
	}

	public void show() {
		// TODO Auto-generated method stub
		Log.v(TAG,"MenuCiCaSubtitle>>show()>>subtitle1 = "+subtitle1.getText().toString());
		myHandler.postDelayed(showRun,1000 );
		setVisibility(View.VISIBLE);
	}
	void sendMsg(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
			int arg6, String[] arg7) {
	DtvCicaManager.cicaQueryControl(arg0, arg1, arg2, arg3,
			arg4, arg5, arg6, arg7);

	}
	
}
