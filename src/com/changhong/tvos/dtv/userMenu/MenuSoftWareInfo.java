package com.changhong.tvos.dtv.userMenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvSoftWareInfoManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvVersion;
import com.changhong.tvos.dtv.util.ViewTipInfo;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardStatus;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;

public class MenuSoftWareInfo extends Dialog implements OnKeyListener {
	private static final String TAG = "MenuSoftWareInfo";
	private Context mContext = null;
	private ViewTipInfo mViewTipInfo = null;
	private String mStrOperator, mStrCardInfo, mStrUIVersion, mStrAPIVersion;
	private Button mButtonSure = null;
	private long mShowTime = 30000;
	private Handler mHandler = new Handler();
	private Runnable mRunShow = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mContext != null && (mContext instanceof Activity) && (!((Activity) mContext).isFinishing())) {
				Log.i(TAG, "LL mContext = " + mContext + "isFinish = " + ((Activity) mContext).isFinishing());
				dismiss();
			}
		}
	};

	public MenuSoftWareInfo(Context context) {
		super(context, R.style.Theme_ActivityTransparent);
		// TODO Auto-generated constructor stub
		mContext = context;
		setContentView(R.layout.menu_soft_ware_info);
		Log.i(TAG, "LL MenuSoftWareInfo>>setContentView()***");
		this.setDisplayText();
		mViewTipInfo = (ViewTipInfo) findViewById(R.id.dtv_soft_ware_info);
		mViewTipInfo.SetSoftWareInfoStr(mContext.getString(R.string.menu_dtv_version_information), mStrOperator, mStrCardInfo, mStrUIVersion, mStrAPIVersion, null);
		mViewTipInfo.SetTipType(ViewTipInfo.SOFTWARE_INFORMATION);
		mButtonSure = (Button) findViewById(R.id.dtv_soft_ware_info_yes);
		mButtonSure.setOnKeyListener(this);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL MenuSoftWareInfo>>show()***");
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		mButtonSure.requestFocus();
		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		super.dismiss();
	}

	private void setDisplayText() {
		Log.i(TAG, "LL MenuSoftWareInfo>>setDisplayText()***");
		String strNoInfo = mContext.getString(R.string.menu_soft_ware_no_info);
		String strCardType = mContext.getString(R.string.menu_soft_ware_card_type_unknown);
		String strCardStatus = mContext.getString(R.string.menu_soft_ware_card_status_exception);
		DtvOperator dtvOperator = DtvSoftWareInfoManager.getCurOperator();
		if (dtvOperator != null) {
			mStrOperator = mContext.getString(R.string.menu_soft_ware_operator_info) + dtvOperator.getOperatorName();
		} else {
			mStrOperator = mContext.getString(R.string.menu_soft_ware_operator_info) + strNoInfo;

		}
		DtvCardStatus dtvCardStatus = DtvSoftWareInfoManager.getCardStatus();
		if (dtvCardStatus != null) {
			switch (dtvCardStatus.getCardType()) {
			case CardType.CARD_TYPE_CI:
				strCardType = mContext.getString(R.string.menu_soft_ware_card_type_ci);
				break;
			case CardType.CARD_TYPE_CA:
				strCardType = mContext.getString(R.string.menu_soft_ware_card_type_ca);
				break;
			case CardType.CARD_TYPE_CHECK:
			case CardType.CARD_TYPE_NO_CARD:
			default:
				break;
			}
			switch (dtvCardStatus.getCardStatus()) {
			case CardStatus.CARD_STATUS_OK:
				strCardStatus = mContext.getString(R.string.menu_soft_ware_card_status_ok);
				break;
			case CardStatus.CARD_STATUS_OUT:
				strCardStatus = mContext.getString(R.string.menu_soft_ware_card_status_out);
				break;
			case CardStatus.CARD_STATUS_INSERT:
			case CardStatus.CARD_STATUS_CHECKING:
			case CardStatus.CARD_STATUS_INVALID:
			default:
				break;
			}
			mStrCardInfo = mContext.getString(R.string.menu_soft_ware_card_type_info) + strCardType + " " + mContext.getString(R.string.menu_soft_ware_card_status_info) + strCardStatus + " "
					+ mContext.getString(R.string.menu_soft_ware_card_cas_info);
		} else {
			mStrCardInfo = mContext.getString(R.string.menu_soft_ware_card_type_info) + strCardType + mContext.getString(R.string.menu_soft_ware_card_status_info) + strCardType
					+ mContext.getString(R.string.menu_soft_ware_card_cas_info) + strCardType;
		}
		mStrUIVersion = mContext.getString(R.string.menu_soft_ware_version_info) + DtvSoftWareInfoManager.mUIVersion;
		DtvVersion dtvVersion = DtvSoftWareInfoManager.getDtvVersion();
		if (null != dtvVersion) {
			mStrAPIVersion = dtvVersion.getHardwareVersion() + "." + dtvVersion.getOpVersion() + "." + dtvVersion.getAPIMainVersion() + "." + dtvVersion.getAPISubVersion() + "."
					+ dtvVersion.getMainVersion() + "." + dtvVersion.getSubVersion() + "." + dtvVersion.getKOVersion() + "." + dtvVersion.getReleaseDateTime();
		}

	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		if (keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
			return false;
		}
		Log.i(TAG, "LL MenuSoftWareInfo>>onKey()>>keyCode = " + keyCode);
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			dismiss();
			Log.i(TAG, "LL MenuSoftWareInfo>>onKey()>>dismiss()***");
			break;
		default:
			break;
		}
		return false;
	}
}
