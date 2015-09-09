package com.changhong.tvos.system.commondialog;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.changhong.softkeyboard.CHSoftKeyboardManager;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;
import com.changhong.tvos.dtv.util.ViewInputCodeText;

public class CommenInputDialog  extends AbsCommonDialog{
	public static final int FOCUS_BUTTON_OK = 0;
	public static final int FOCUS_BUTTON_CANCEL = 1;
	private static final String TAG = "CommenInputDialog";
	private boolean isShowTV = false;
	private Button mButtonOK;
	private Button mButtonCancel;
	
	private String mButtonOkText;
	private String mButtonCancelText;
	private ViewInputCodeText editText;
	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;
	
	private int mDefaultFocusButton = FOCUS_BUTTON_OK;
	
	private View.OnClickListener mListenerOK = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
		}
	};
	private View.OnClickListener mListenerCancel = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
		}
	};
	
	public CommenInputDialog(Context context){
		super(context, R.style.MTK_Dialog_bg);
		setContentView(R.layout.input_password_dialog);
		initView();
		initListener();
		setProperty();
	}

	private void setProperty() {
		// TODO Auto-generated method stub
		this.setCancelable(mCancelable2);
		this.setMessage(mMessageText);
		this.setDuration(mDuration);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTitleView = (TextView)findViewById(R.id.input_dialog_title);
		mMessageView = (TextView)findViewById(R.id.input_dialog_info);
		editText = (ViewInputCodeText)findViewById(R.id.pass_text);

		mButtonOK = (Button)findViewById(R.id.input_dialog_btn_ok);
		mButtonCancel = (Button)findViewById(R.id.input_dialog_btn_cancel);
		
		editText.setNextFocusDownId(R.id.input_dialog_btn_ok);
		
		if (mAdjustCHSKM == null) {
			mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(getContext());		
		}
	}
	
	@Override
	protected void initListener() {
		super.initListener();

		mButtonOK.setOnClickListener(mListenerOK);
		mButtonCancel.setOnClickListener(mListenerCancel);
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if(null != mAdjustCHSKM){
			if(mAdjustCHSKM.isSoftKeyPanelOnShow()){
				mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
			}
		}
	}

	@Override
	public void show() {
		super.show();	
		editText.reset();
		editText.requestFocus();
	}
	private void setFocusButton()
	{
		Button btn = mButtonOK;
		if(mDefaultFocusButton == FOCUS_BUTTON_CANCEL)
		{
			btn = mButtonCancel;
		}
		btn.setFocusable(true);
		btn.setFocusableInTouchMode(true);
		btn.requestFocus();
	}
	
	public void setOKButtonListener(View.OnClickListener mListenerOK) {
		this.mListenerOK = mListenerOK;
	}
	
	public void setCancelButtonListener(View.OnClickListener mListenerCancel) {
		this.mListenerCancel = mListenerCancel;
	}
	
	/**
	 * @return the mDefaultFocusButton
	 */
	public int getDefaultFocusButton() {
		return mDefaultFocusButton;
	}

	/**
	 * @param mDefaultFocusButton the mDefaultFocusButton to set
	 */
	public void setDefaultFocusButton(int mDefaultFocusButton) {
		this.mDefaultFocusButton = mDefaultFocusButton;
	}

	public void setOkButtonText(String strText)
	{
		mButtonOkText = strText;
		if(mButtonOK != null)
		{
			mButtonOK.setText(mButtonOkText);
		}
	}
	public void setOkButtonText(int resId)
	{
		if(resId > 0)
			setOkButtonText(mContext.getString(resId));
		else
			setOkButtonText("");
	}
	
	public void setCancelButtonText(String strText)
	{
		mButtonCancelText = strText;
		if(mButtonCancel != null)
		{
			mButtonCancel.setText(mButtonCancelText);
		}
	}
	public void setCancelButtonText(int resId)
	{
		if(resId > 0)
			setCancelButtonText(mContext.getString(resId));
		else
			setCancelButtonText("");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG,"LL enter ScanWarningDialog onKeyDown>>keyCode = " + keyCode);
		setDuration(30);
		switch (event.getScanCode()) {
		case 231://keyboard Menu
			this.dismiss();
			return true;
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			if(editText.isFocused()){
				mButtonOK.callOnClick();
			}else if(mButtonOK.isFocused()){
				mButtonOK.callOnClick();
			}else{
				
				mButtonCancel.callOnClick();
			}
			return true;
			
		case 235://keyboard Volume Down
			if(mButtonOK.isFocused()){
				mButtonCancel.requestFocus();
			}else{
				
				mButtonOK.requestFocus();
			}
			return true;
		case 236://keyboard Volume Up
			if(editText.isFocused()){
				mButtonOK.requestFocus();
			}else
				if(mButtonCancel.isFocused()){
				mButtonOK.requestFocus();
			}else{
				
				mButtonCancel.requestFocus();
			}
			return true;
		default:
			break;
		}
		switch (keyCode) {
		
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if(editText.isFocused()){
				mButtonOK.callOnClick();
			}else if(mButtonOK.isFocused()){
				mButtonOK.callOnClick();
			}else{
				mButtonCancel.callOnClick();
			}
			return true;
		case KeyEvent.KEYCODE_SOURCE:
//			case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
				setShowTV(true);
				dismiss();
				break;
				
//		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD:  //软键盘
//				if (mAdjustCHSKM == null) {
//					mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);		
//				}
//				if(null != mAdjustCHSKM){
//					if(mAdjustCHSKM.isSoftKeyPanelOnShow()){
//						
//						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
//					}else{
//						
//						mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_CENTER);
//						return true;
//					}
//				}
//				break;
		case  KeyEvent.KEYCODE_BACK:
			dismiss();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD:  //软键盘
				if (mAdjustCHSKM == null) {
					mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);		
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
	public void setShowTV(boolean isShowTV) {
		this.isShowTV = isShowTV;
	}
	public boolean isShowTV() {
		return isShowTV;
	}
	
	public String getInputWorld(){
		return editText.getCodeString();
	}

	/**
	 * 是否隐藏输入的数字，以*表示
	 * @param isEncode
	 */
	public void setEncode(boolean isEncode) {
		editText.setInputEncode(isEncode);
	}

	public boolean isEncode() {
		return editText.isInputEncode();
	}

	public void setNumHint(String value) {
		// TODO Auto-generated method stub
		editText.setHint(value);
	}
}
