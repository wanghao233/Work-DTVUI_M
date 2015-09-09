package com.changhong.tvos.dtv.cica;

import com.changhong.softkeyboard.CHSoftKeyboardManager;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;
import com.changhong.tvos.dtv.util.SimpleEditText;
import com.changhong.tvos.dtv.util.SimpleEditText.OnInnerKeyDownListener;
import com.changhong.tvos.dtv.vo.CICAMEditBox;
import com.changhong.tvos.dtv.vo.CICAMMenuConfirm;
import com.changhong.tvos.dtv.vo.CICAMMenuInput;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuCiCaInput extends Dialog {

	private static final String TAG = "MenuCiCaInput";
	private Context context;
	private int type;
	private LinearLayout layout;
	private Button dtv_okbutton;
	private Button dtv_nobutton;
	private TextView dtv_main_tittle;
	private TextView dtv_edit_info;
	private TextView dtv_edit_help_info;
	private TextView dtv_main_help_info;
	private TextView dtv_main_sub_tittle;
	private Bundle mybBundle;
	private int item=1;
	private CICAMEditBox[] edit;
	private int limit_length=30;
	private boolean send;
	private boolean hide=false;
	private int x=0;
	private int y=0;
	
	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;
	
	CICAMMenuConfirm  menuConfirm=null;
	CICAMMenuInput menuInput=null;
	int showTime = 30000;
	public Handler handler = new Handler();
	public Runnable showRun = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dismiss();
		}
	};
	public boolean onKeyDown(int keyCode,KeyEvent event){
		Log.i(TAG,"LL onKeyDown()***");
		handler.removeCallbacks(showRun);
		handler.postDelayed(showRun, showTime);
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
	
	public boolean onKeyUp(int keyCode,KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD:  //软键盘
				if (mAdjustCHSKM == null) {
					mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(getContext());		
				}
				if(null != mAdjustCHSKM){
					if(mAdjustCHSKM.isSoftKeyPanelOnShow()){
						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
					}else{
						
						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_CENTER);
						return true;
					}
				}
				break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	public MenuCiCaInput(Context context,Bundle bundle,int type) {
		super(context,R.style.Theme_MenuDialog);
		this.context=context;
		this.type=type;
		this.mybBundle=bundle;
		getLayout();
	}

	public void show(){
	if(type ==1){
		if(mybBundle==null)
		{
			return;
		}
		menuConfirm=(CICAMMenuConfirm)mybBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
		x=menuConfirm.miAbsX;
		y=menuConfirm.miAbsY;
		setLayoutPositon(x, y);
		if(menuConfirm==null)
		{
			return;
		}
		if(menuConfirm.mstrTitle!=null)
		{
			dtv_main_tittle.setText(menuConfirm.mstrTitle);
		}
		else {
			dtv_main_tittle.setText("");
		}
		if(menuConfirm.mstrPrompt!=null)
		{
			dtv_main_sub_tittle.setText(menuConfirm.mstrPrompt);
		}
		else {
			dtv_main_sub_tittle.setText("");
		}
		
	}
	else
	{
		if(mybBundle==null)
		{
			return;
		}
		menuInput=mybBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
		x=menuInput.miAbsX;
		y=menuInput.miAbsY;
		setLayoutPositon(x, y);
		if(menuInput.mstrTitle!=null)
		{
			dtv_main_tittle.setText(menuInput.mstrTitle);
			
		}
		else{
			dtv_main_tittle.setText("");
		}
		if(menuInput.mstrHelp!=null)
		{
			dtv_main_help_info.setText(menuInput.mstrHelp);
		}
		else {
			dtv_main_help_info.setText("");
		}
		item=menuInput.miItems;
		edit=menuInput.maEditboxList;
		dtv_main_sub_tittle.setVisibility(View.GONE);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(160,40);		
		for(int i=0;i<item;i++)
		{
			SimpleEditText editText=new SimpleEditText(context);
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			limit_length=edit[i].miLength;
			hide=edit[i].mbHide;
			send=edit[i].mbAuto;
			if(hide==true)
			{
				editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
		    editText.setId(i);
		    editText.setBackgroundResource(R.drawable.dtv_cica_set_time_selector);
		    editText.setPadding(10, 0, 10, 0);
		    editText.setOnInnerKeyDownListener(new OnInnerKeyDownListener() {
				@Override
				public void onInnerKeyDownListener() {
					// TODO Auto-generated method stub
					Log.i(TAG,"LL editText>>onInnerKeyDownListener()***");
					handler.removeCallbacks(showRun);
					handler.postDelayed(showRun, showTime);
				}
		    });
		    
		    editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				public void onFocusChange(View v, boolean hasFocus) {
					Log.i(TAG,"LL onFocusChange()***");
					if(hasFocus)
					{
						if(edit[v.getId()].mstrHelp!=null)
						{
						dtv_edit_info.setText(edit[v.getId()].mstrHelp);
					    }else {
						dtv_edit_info.setText("");
					   }
						if(edit[v.getId()].mstrPrompt!=null)
						{
							dtv_edit_help_info.setText(edit[v.getId()].mstrPrompt);
						}
						else {
							dtv_edit_help_info.setText("");
						}
					}
					
				}
			});
		    editText.addTextChangedListener(new TextWatcher() {
				
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					Log.i(TAG,"LL onTextChanged()***");
					View focus = getCurrentFocus();
					if(focus instanceof SimpleEditText){
						SimpleEditText eText=(SimpleEditText) focus;
						if(eText.getText().length()>(limit_length-1))
						{
							new Handler().post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									
									MenuCiCaInput.this.dtv_okbutton.requestFocus();
//									sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
								}
							});
							lengthFilter(context,eText,limit_length);
						}
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					Log.i(TAG,"LL beforeTextChanged()***");
				}
				
				public void afterTextChanged(Editable s) {
					Log.i(TAG,"LL afterTextChanged()***");
				}
			});		    
		    layout.addView(editText,params);
		}
	}
	super.show();
	handler.removeCallbacks(showRun);
	handler.postDelayed(showRun, showTime);
	}
	public void getLayout()
	{
		setContentView(R.layout.menu_cica_input_dialog);
		layout=(LinearLayout)findViewById(R.id.editboxinfo);
		dtv_okbutton=(Button)findViewById(R.id.dtv_ok_button);
		dtv_nobutton=(Button)findViewById(R.id.dtv_no_button);
		dtv_main_tittle=(TextView)findViewById(R.id.dtv_main_tittle);
		dtv_main_help_info=(TextView)findViewById(R.id.dtv_main_help_info);
		dtv_edit_help_info=(TextView)findViewById(R.id.dtv_edit_help_info);
		dtv_edit_info=(TextView)findViewById(R.id.dtv_edit_info);	
		dtv_main_sub_tittle=(TextView)findViewById(R.id.dtv_main_sub_tittle);
		dtv_main_tittle.setText("");
		dtv_main_help_info.setText("");
		dtv_edit_help_info.setText("");
		dtv_edit_info.setText("");
		dtv_main_sub_tittle.setText("");
}
	public Button getOKbutton()
	{
		return dtv_okbutton;
	}
	public Button getNObutton()
	{	
		return dtv_nobutton;
	}

	
	public String[] getString()
	{
		String []str=new String[item];
		for(int i=0;i<item;i++)
		{
			SimpleEditText edit=(SimpleEditText)findViewById(i);
		String string=edit.getText().toString();
			
			if(null != string){
				String regEx="\\d*";
				if(string.matches(regEx)){
	                        	str[i]=string;
				}else{
					str[i]="0000";
					Log.i(TAG, "input format err, not all number");
				}
			}
		Log.i(TAG,"LL getString()>>str["+ i + "] = " + str[i]);
		}
		return str;
	}
//	public void sendKey(final int keyCode)
//	{
//		Thread mThread=new Thread(){
//			public void run()
//			{
//				
//				try {
//					Thread.sleep(50);
//					IWindowManager manager=IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
//					KeyEvent keyup=new KeyEvent(KeyEvent.ACTION_DOWN,keyCode);
//					KeyEvent keydown=new KeyEvent(KeyEvent.ACTION_UP,keyCode);
//					manager.injectKeyEvent(keyup, false);
//					manager.injectKeyEvent(keydown, false);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}	
//			}	
//		};
//		mThread.start();
//	}
	public void setLayoutPositon(int x,int y)
	{
		WindowManager.LayoutParams params=this.getWindow().getAttributes();
		params.x+=x;
		params.y+=y;
		this.getWindow().setAttributes(params);
	}
	public static void lengthFilter(final Context context, final SimpleEditText editText, final int max_length) {
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter.LengthFilter(max_length) {
		public CharSequence filter(CharSequence source, int start, int end,
		Spanned dest, int dstart, int dend) {
		int destLen =dest.toString().length();
		int sourceLen =source.toString().length();
		if(destLen+sourceLen>max_length)
		{
			return "";
		}
		return source;
		}
		};
		editText.setFilters(filters);
		}

}