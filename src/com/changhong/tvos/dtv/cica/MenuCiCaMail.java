package com.changhong.tvos.dtv.cica;

import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvCicaManager;
import com.changhong.tvos.dtv.vo.CICAMMessageBase.ConstCICAMsgType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMMenuType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMOpCode;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuCiCaMail extends Dialog {
	private static final String TAG = "MenuCiCaMail";
	private Context mContext;
	private LinearLayout float_mail;
	private Button okButton;
	private Button cancleButton;
	private TextView mailInfo;
	private int showtime = 10000;
	private int mi_MsgType = -1;
	private int mi_MsgID = -1;
	private int mi_MenuID = -1;
	private Handler mHandler;
	private Runnable showTimeRun;
	private OnFloatMailListener mOnFloatMailListener = null;
	
	public MenuCiCaMail(Context context) {
		super(context, R.style.Theme_ActivityDialog);
		mContext = context;
		setContentView(R.layout.menu_cica_mail);
		okButton = (Button) findViewById(R.id.dtv_cica_mail_ok);
		cancleButton = (Button) findViewById(R.id.dtv_cica_mail_cancle);
		mailInfo = (TextView) findViewById(R.id.dtv_cica_mail_info);
		okButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMsg(ConstCICAMsgType.MSG_USER_MENU,
						ConstCICAMMenuType.MENU_LIST, 0xdfffffff, -1, -1,
						ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM, 0, null);
				cancel();
			}
		});
		cancleButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMsg(mi_MsgType, mi_MsgID, mi_MenuID, -1, -1,
						ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
				cancel();
				
				mOnFloatMailListener.onFloatMail();
			}
		});
		mHandler = new Handler();
		showTimeRun = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				if (mContext != null && (mContext instanceof Activity)
						&& (!((Activity) mContext).isFinishing())) {
					Log.i(TAG, "LL mContext = " + mContext + "isFinish = "
							+ ((Activity) mContext).isFinishing());
					sendMsg(mi_MsgType, mi_MsgID, mi_MenuID, -1, -1,
							ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
					cancel();
				}
			}
		};
	}

	@Override
	public void cancel() {
		mHandler.removeCallbacks(showTimeRun);
		super.cancel();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v("tv", "LL MenuCiCaMail>>keyCode == " + keyCode);
		mHandler.removeCallbacks(showTimeRun);
		mHandler.postDelayed(showTimeRun, showtime);
		switch (event.getScanCode()) {
		case 231://keyboard Menu
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
			break;
		case 235://keyboard Volume Down
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			break;
		case 236://keyboard Volume Up
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setMenuState(int mi_MsgType, int mi_MsgID, int mi_MenuID) {
		this.mi_MsgType = mi_MsgType;
		this.mi_MsgID = mi_MsgID;
		this.mi_MenuID = mi_MenuID;
	}

	public void setMailInfo(int mi_NewCnt, int mi_ReadCnt, int mi_Param) {
		// TODO Auto-generated method stub
		if (mi_Param == 1) {
			mailInfo.setText(mContext.getResources().getString(
					R.string.dtv_cica_mail));
		} else if (mi_Param == 2) {
			mailInfo.setText(mContext.getResources().getString(
					R.string.dtv_cica_mail_full));
		} else {
			Log.v("MSG_MAIL", "cancel mi_Param = " + mi_Param);
			cancel();
			return;
		}
		okButton.requestFocus();
		mHandler.removeCallbacks(showTimeRun);
		mHandler.postDelayed(showTimeRun, showtime);
		setPositon(0, 250);
		Log.v("MSG_MAIL", "show");
		show();
	}

	public void setPositon(int xoff, int yoff) {
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = xoff;
		lp.y = yoff;
		window.setAttributes(lp);

	}

	void sendMsg(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
			int arg6, String[] arg7) {
		DtvCicaManager.cicaQueryControl(arg0, arg1, arg2, arg3, arg4, arg5,
				arg6, arg7);

	}
		
	public void setOnFloatMailListener(OnFloatMailListener arg0){
		mOnFloatMailListener = arg0;
	}
	public interface OnFloatMailListener{
		public void onFloatMail();
	} 

}