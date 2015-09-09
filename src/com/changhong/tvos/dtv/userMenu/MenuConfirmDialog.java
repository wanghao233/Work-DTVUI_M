package com.changhong.tvos.dtv.userMenu;

import com.changhong.tvos.dtv.R;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.R.integer;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MenuConfirmDialog extends Dialog{
	private static final String TAG = "MenuConfirmDialog";
	private Context context;
	private Button confirm_ok_Button;
	private Button confirm_no_Button;
	private Button confirm_other_Button;
	private TextView confirm_main_tittle;
	private TextView confirm_sub_tittle;
	private TextView confirm_message;
	private int theme;
	private LinearLayout layouttextview;
	private LinearLayout layoutbutton1;
	private LinearLayout layoutbutton2;
	private LinearLayout layoutbutton3;
	private LinearLayout layoutbutton;
	private int time=15000;
	private Dialog dialog=this;
	private Handler handler=new Handler();
	private boolean iscancle=false;
	public MenuConfirmDialog(Context context) {
		super(context,R.style.Theme_DialogFactory);
		this.context=context;
		getLayout();
	}
	public MenuConfirmDialog(Context context,int theme)
	{
		super(context,theme);
		this.context=context;
		this.theme=theme;
		getLayout();

	}
	public void show()
	{
		if(iscancle==true)
		{
			setTime(time);
		}
		//MenuDataController.Is_ShowLauncher =false;
		super.show();
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		//MenuDataController.Is_ShowLauncher =true;
		handler.removeCallbacks(runnable);
		super.dismiss();
	}
	public void setCancel(boolean flag)
	{
		this.iscancle=flag;
	}
	
	android.view.View.OnClickListener myClickListener =new View.OnClickListener() {
		

		public void onClick(View v) {
			// TODO Auto-generated method stub
			cancel();
		}
	};
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	Log.i("tv","LL MenuConfirmDialog>>onKeyDown>>keyCode = " + keyCode);
	switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_CHANGHONGIR_MENU:	
		cancel();
		break;
	default:
		refreshdialog();
		break;
	}
	return super.onKeyDown(keyCode, event);
}
	public void getLayout()
	{
		setContentView(R.layout.menu_confirm_dialog);
		confirm_ok_Button=(Button)findViewById(R.id.confirm_ok_button_dialog);
		confirm_no_Button=(Button)findViewById(R.id.confirm_no_button_dialog);
		confirm_other_Button=(Button)findViewById(R.id.confirm_other_button_dialog);
		confirm_main_tittle=(TextView)findViewById(R.id.confirm_dialog_tittle);
		confirm_message=(TextView)findViewById(R.id.confirm_dialog_info);
		layouttextview=(LinearLayout)findViewById(R.id.dtv_linearlayout_dialog);
		layoutbutton1=(LinearLayout)findViewById(R.id.layoutbutton1_dialog);
		layoutbutton2=(LinearLayout)findViewById(R.id.layoutbutton2_dialog);
		layoutbutton=(LinearLayout)findViewById(R.id.layoutbutton_dialog);
		confirm_ok_Button.setOnClickListener(myClickListener);
		confirm_no_Button.setOnClickListener(myClickListener);;
		confirm_other_Button.setOnClickListener(myClickListener);
		confirm_ok_Button.requestFocus();	layoutbutton3=(LinearLayout)findViewById(R.id.layoutbutton3_dialog);
		setButtonText(context.getResources().getString(R.string.menu_sure), null, context.getResources().getString(R.string.menu_cancel));
	}
	public Button getOKbutton()
	{
		return confirm_ok_Button;
	}
	public Button getNObutton()
	{
		return confirm_no_Button;
	}
	public Button getOtherButton()
	{
		return confirm_other_Button;
	}
	public void setMessage(String message){
		setMessage(null,message);
	}
	public void setMessage(String tittle,String message)
	{
		int flag=0;
		if(tittle==null||tittle=="")
		{
			confirm_main_tittle.setVisibility(View.GONE);
			flag++;
		}
		if(message==null||message=="")
		{
			confirm_message.setVisibility(View.GONE);
			flag++;
		}
		Log.v("flag", ""+flag);
		if(flag==2)
		{
			layouttextview.setWeightSum(1);
		}
		else {
			layouttextview.setWeightSum(2-flag);
		}
		if((flag==1)&&message!=null&&message!="")
		{
			confirm_message.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		}
		confirm_main_tittle.setText(tittle);
		confirm_message.setText(message);
	}
	public void setButtonText(String okButton,String otherButton,String noButton)
	{
		int temp=0;
		if(okButton==""||okButton==null)
		{
			layoutbutton1.setVisibility(View.GONE);
			temp++;
		}
		if(otherButton==""||otherButton==null)
		{
			layoutbutton2.setVisibility(View.GONE);
			temp++;
		}
		if(noButton==""||noButton==null)
		{
			layoutbutton3.setVisibility(View.GONE);
			temp++;
		}
		Log.v("temp", ""+temp);
		
		if(temp==3)
		{
			layoutbutton.setWeightSum(1);
		}
		else {
			layoutbutton.setWeightSum(3-temp);
		}
		confirm_ok_Button.setText(okButton);
		confirm_no_Button.setText(noButton);
		confirm_other_Button.setText(otherButton);
	}
    private Runnable runnable=new Runnable() {
		

		public void run() {
			// TODO Auto-generated method stub
			if(MenuConfirmDialog.this.context!=null&&(MenuConfirmDialog.this.context instanceof Activity)&&(!((Activity)MenuConfirmDialog.this.context).isFinishing())){
				Log.i(TAG,"LL mContext = " + MenuConfirmDialog.this.context + "isFinish = " + ((Activity)MenuConfirmDialog.this.context).isFinishing());
				dialog.dismiss();
			}
		}
	};
	public void setTime(int time)
	{
		this.time=time;
		handler.postDelayed(runnable, time);
	}
	private void refreshdialog()
	{
		handler.removeCallbacks(runnable);
		setTime(time);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(runnable);
		super.onStop();
	}
	public void setTittleSize(Float size)
	{
		confirm_main_tittle.setTextSize(size);
	}
	public void setSubTittleSize(Float size)
	{
		confirm_sub_tittle.setTextSize(size);
	}
	public void setMessageSize(Float size)
	{
		confirm_message.setTextSize(size);
	}
	public void setTittleColor(int color)
	{
		confirm_main_tittle.setBackgroundColor(color);
	}
	public void setSubTittleColor(int color)
	{
		confirm_sub_tittle.setBackgroundColor(color);
	}
	public void setMessageColor(int color)
	{
		confirm_message.setBackgroundColor(color);
	}
	public void setOKbuttondraw(int draw)
	{
		confirm_ok_Button.setBackgroundResource(draw);
	}
	public void setNObuttondraw(int draw)
	{
		confirm_no_Button.setBackgroundResource(draw);
	}
	public void setOTHERbutton(int draw)
	{
		confirm_other_Button.setBackgroundResource(draw);
	}
	
}

