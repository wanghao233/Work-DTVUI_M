package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author jinwei.yang,
 */
public class CommonProgressInfoDialog extends AbsCommonDialog{
	LinearLayout mLinearLayout = null;
    private ProgressBar mProgress;
    
    private Drawable mProgressDrawable;

	private Button mButtonCancel;
	private String mButtonCancelText;
	private boolean mButtonCancelVisible = true;
	
	/**
	 * 闂佸憡鐟﹂悧妤冪矓閻戣棄绠板鑸靛姈鐏忥箓鎮楅悽鍨殌缂併劍鐓￠幆鍐礋椤忓棛娈ゆ繛瀵稿У濠�褰掓儓濞戙垹瑙︽い鎺炴嫹閿燂拷
	 */
	private View.OnClickListener mListener = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
		}
	};

	private View.OnClickListener mListenerCancel = mListener;


	/**
	 * 闂佸搫顑呯�氫即鏁撴禒瀣闁兼亽鍎查敓锟?	 * @param context
	 */
	public CommonProgressInfoDialog(Context context) {
		this(context, R.string.stringMessage, true, 10);

	}
	public CommonProgressInfoDialog(Context context, int mesId, boolean cancelAble, int duration) {
		super(context, R.style.MTK_Dialog_bg, 0, mesId, cancelAble, duration);
		mButtonCancelVisible = true;
		setContentView(R.layout.common_progressinfo_dialog_2);
		initView();
		setProperty();
	}
	
	/**
	 * 闂佸憡甯楃换鍌炩�栨繝鍥х闁哄稄濡囬惃鎴澝归悩铏瀯闁活偅鐩畷銉╊敆閿熺晫褰?
	 */
	@Override
	protected void initListener() {
		super.initListener();
		mButtonCancel.setOnClickListener(getListenerCancel());

	}

	/**
	 * 闂佸憡甯楃换鍌炩�栨繝鍥х闁哄稁鍓﹂悗浼存煕閵夘垱瀚?	 */
	private void initView() {
		mLinearLayout = (LinearLayout)findViewById(R.id.llprogress);
		mProgress = (ProgressBar)findViewById(R.id.progress);
		
		mMessageView = (TextView)findViewById(R.id.message_progress);

		mButtonCancel = (Button)findViewById(R.id.commonaction_dialog_btn_cancel);

		mButtonCancel.setOnClickListener(mListener);
		
	}

	/**
	 * 闁荤姳绀佹晶浠嬫偪閸℃瑤娌柣鎰彧閿燂拷
	 */
	private void setProperty() {
		setCancelable(mCancelable2);
		setDuration(mDuration);
//		setProgressDrawable(mProgressDrawable);
		setMessage(mMessageText);
		setButtonVisible(mButtonCancelVisible);
	}

	public void setButtonVisible(boolean visible)
	{
		mButtonCancelVisible = visible;
		if(mButtonCancel != null)
		{
			mButtonCancel.setVisibility(mButtonCancelVisible ? View.VISIBLE : View.GONE);
		}
		setFocusButton();
	}
	
	/**
	 * 闂佸搫瀚晶浠嬪Φ濮橆剦鍤曢柛锔诲幘閿燂拷
	 */
	@Override
	public void show() {
		super.show();
		setFocusButton();
	}
	private void setFocusButton()
	{
		if(mCancelable2 && null != mButtonCancel)
		{
			mButtonCancel.setFocusable(true);
			mButtonCancel.setFocusableInTouchMode(true);
			mButtonCancel.requestFocus();
		}
	}

	/**
	 * 闂佸吋鍎抽崲鑼躲亹閸㈢禈ncel闂佸湱顭堥¨渚�寮銏″剭闁告洦浜為惃鎴澝归悩铏瀯闁活偅鐩畷銉╊敆閿熺晫褰?
	 * @return the mListenerCancel
	 */
	public View.OnClickListener getListenerCancel() {
		return mListenerCancel;
	}

	/**
	 * 闁荤姳绀佹晶浠嬫偪閸濈饱ncel闂佸湱顭堥¨渚�寮銏″剭闁告洦浜為惃鎴澝归悩铏瀯闁活偅鐩畷銉╊敆閿熺晫褰?
	 * @param mListenerCancel
	 *            the mListenerCancel to set
	 */
	public void setCancelListener(View.OnClickListener mListenerCancel) {
		this.mListenerCancel = mListenerCancel;
	}

	public void setProgressDrawable(Drawable d) {
		mProgressDrawable = d;
        if (mProgress != null) {
            mProgress.setIndeterminateDrawable(mProgressDrawable);
        }
    }
	
	
	public void setButtonText(int resId)
	{
		if(resId > 0)
			setButtonText(mContext.getString(resId));
		else
			setButtonText("");
	}
	public void setButtonText(String strText)
	{
		mButtonCancelText = strText;
		if(mButtonCancel != null)
		{
			mButtonCancel.setText(mButtonCancelText);
		}
	}
}
