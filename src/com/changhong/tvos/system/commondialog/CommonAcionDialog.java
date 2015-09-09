package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 闁哄拋鍣ｉ敓浠嬫偨閵婏箑鐓曢梺顐㈩樇鐎氥劎绮ｅΔ锟界稉
 * 
 * @author hs_yataozhang OK,jinwei.yang
 */
public class CommonAcionDialog extends AbsCommonDialog {
	/**
	 * 閻庤鐭粻鐔割湡濡鍚囬柣銊ュ閸斿秹鎮欑憴鍕樆闂佹枻鎷�	 */
	public static final int FOCUS_BUTTON_OK = 0;
	public static final int FOCUS_BUTTON_CANCEL = 1;
	private static final String TAG = CommonAcionDialog.class.getSimpleName();
	private boolean isShowTV = false;
	private Button mButtonOK;
	private Button mButtonCancel;
	
	private String mButtonOkText;
	private String mButtonCancelText;
	/**
	 * 濮掓稒顭堥濠氭儍閸曨厼濡介柣鎰�?鐎垫粓鏌﹂鎯у亶鐎殿噯鎷�? */
	private int mDefaultFocusButton = FOCUS_BUTTON_OK;

	/**
	 * 闁告搩鍨遍悥锝嗭紣閿燂拷	 */
	private TextView mSubTitleView;
	private String mSubTitleText;
	/**
	 * 濮掓稒顭堥濠氭儍閸曨剙鐦婚梺娆惧枛婵晜鎷呴敓锟� */
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

	/**
	 * 闁哄�?伴敓浠嬪礄閼恒儲娈�
	 * @param context
	 */
	public CommonAcionDialog(Context context) {
		this(context, R.string.stringTitle, R.string.stringTitle2, R.string.stringMessage, 5);
	}

	/**
	 * 闁哄�?伴敓浠嬪礄閼恒儲娈�
	 * @param context
	 * @param titleId
	 * @param mesId
	 * @param duration
	 */
	public CommonAcionDialog(Context context, int titleId, int subTitleId, int mesId, int duration) {
		super(context, R.style.MTK_Dialog_bg, titleId, mesId, false, duration);
		if(titleId > 0)
			mTitleText = mContext.getString(titleId);
		if(subTitleId > 0)
			mSubTitleText = mContext.getString(subTitleId);
		setContentView(R.layout.common_action_dialog_bg);
		initView();
		initListener();
		setProperty();
	}
	/**
	 * 闁告帗绻傞�?濠囧礌閺嶏妇鐨戝ù鐘虫构閻盯宕ラ锟界彜
	 */
	@Override
	protected void initListener() {
		super.initListener();

		mButtonOK.setOnClickListener(mListenerOK);
		mButtonCancel.setOnClickListener(mListenerCancel);
	}

	/**
	 * 闁告帗绻傞�?濠囧礌閺嶎剦鐎伴柛銉嫹
	 */
	private void initView() {
		mTitleView = (TextView)findViewById(R.id.commonaction_dialog_title);
		mSubTitleView = (TextView)findViewById(R.id.commonaction_dialog_title_2);
		mMessageView = (TextView)findViewById(R.id.commonaction_dialog_hintmessage);

		mButtonOK = (Button)findViewById(R.id.commonaction_dialog_btn_ok);
		mButtonCancel = (Button)findViewById(R.id.commonaction_dialog_btn_cancel);
		
	}

	/**
	 * 閻犱礁澧介悿鍡欎沪閻愯揪鎷�
	 */
	private void setProperty() {
		this.setCancelable(mCancelable2);
		this.setTitle(mTitleText);
		this.setSubTitleText(mSubTitleText);
		this.setMessage(mMessageText);
		this.setDuration(mDuration);
	}

	/**
	 * 闁哄嫬澧介妵姘嚕閸︻厾宕�
	 */
	@Override
	public void show() {
		super.show();	
		
		setFocusButton();
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
	
	/**
	 * 閻犱礁澧介悿鍞巏闁圭顧�?��鎶芥儍閸曨亞鐨戝ù鐘虫构閻盯宕ラ锟界彜
	 * @param mListenerOK
	 *            the mListenerOK to set
	 */
	public void setOKButtonListener(View.OnClickListener mListenerOK) {
		this.mListenerOK = mListenerOK;
	}

	/**
	 * 閻犱礁澧介悿鍝籥ncel闁圭顧�?��顓㈡儍閸曨亞鐨戝ù鐘虫构閻盯宕ラ锟界彜
	 * @param mListenerCancel
	 *            the mListenerCancel to set
	 */
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
	public void setTitle(String title)
	{
		mTitleText = title;
		mTitleView.setText(mTitleText);
	}
	public void setTitle(int titleId)
	{
		if(titleId > 0)
			setTitle(mContext.getString(titleId));
		else
			setTitle("");
	}
	/**
	 * 閻犱礁澧介悿鍡涘礈椤栨稓鍨煎Λ鐗埳戦弸鍐拷閿燂拷	 * @param title2
	 */
	public void setSubTitleText(String title2)
	{
		mSubTitleText = title2;
		mSubTitleView.setText(mSubTitleText);
	}
	
	/**
	 * add By YangLiu
	 * @param size
	 */
	public void setSubTitleTextSize(int size)
	{
		mSubTitleView.setTextSize(size);
	}
	
	public void setSubTitleText(int subTitleId)
	{
		if(subTitleId > 0)
			setSubTitleText(mContext.getString(subTitleId));
		else
			setSubTitleText("");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG,"LL enter ScanWarningDialog onKeyDown>>keyCode = " + keyCode);
		switch (event.getScanCode()) {
		case 231://keyboard Menu
			this.dismiss();
			return true;
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			if(mButtonOK.isFocused()){
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
			if(mButtonOK.isFocused()){
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
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void setShowTV(boolean isShowTV) {
		this.isShowTV = isShowTV;
	}
	public boolean isShowTV() {
		return isShowTV;
	}
	

	
	public void setIcons(Drawable drawableL,Drawable drawableT,Drawable drawableR,Drawable drawabled){
		mTitleView.setCompoundDrawablePadding(5);
		mTitleView.setCompoundDrawables(drawableL, drawableT, drawableR, drawabled);
		mTitleView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
	}
}
