package com.changhong.tvos.dtv.scan;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.util.ViewList;
import com.changhong.tvos.system.commondialog.CommonAcionDialog;

class ViewCityList extends ViewList implements android.view.View.OnKeyListener {

	public ViewCityList(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		onePageNum = 11;
	}

	public View getView(int index) {
		if (viewList.size() <= index) {
			for (int i = viewList.size(); i < index + 1; i++) {
				TextView view = new TextView(context);
				view.setText((String) list.get(i));
				view.setLayoutParams(new LayoutParams(148, 40));
				view.setTextSize(20);
				view.setTextColor(Color.BLACK);
				view.setGravity(Gravity.CENTER);
				view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
				view.setSingleLine(true);
				view.setMarqueeRepeatLimit(-1);
				view.setFocusable(true);
				view.setOnKeyListener(this);
				view.setPadding(11, 0, 11, 0);
				//view.setp
				viewList.add(view);
			}
		}
		return viewList.get(index);
	}

	public void updateView(int position) {
		if (position == curIndex) {
			viewList.get(position).setBackgroundResource(R.drawable.scan_city_setup_selected);
			viewList.get(position).requestFocus();
		} else {
			viewList.get(position).setBackgroundResource(0);
		}
	}
}

public class MenuScanSetup extends Dialog {
	private static final String TAG = "ViewCityList";
	public static final String OpIdName = "OpIdNameKey";
	Context mContext;
	List<String> cityNameList;
	int citySelect = -1;
	RelativeLayout cityListLayout;
	ViewCityList listView;
	String tmp;
	boolean cityHasChanged = false;
	DtvOperatorManager operatorManager;
	DtvTunerInfo mDtvTunerInfo = null;

	public MenuScanSetup(Context context) {
		//super(context, R.style.Theme_ScanDialog);
		super(context, R.style.Theme_DialogFactory);
		// TODO Auto-generated constructor stub
		mContext = context;
		setContentView(R.layout.menu_scansetup);
		Log.v(TAG, "LL DtvScanSetup init");
		operatorManager = DtvOperatorManager.getInstance();

		cityListLayout = (RelativeLayout) findViewById(R.id.relativeLayout3);
		listView = (ViewCityList) findViewById(R.id.cityListView);

		cityNameList = getcityNameList(operatorManager.getOperatorList());
		citySelect = operatorManager.getCurOpIndex();

		listView.init(cityNameList, citySelect);

	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	public MenuScanSetup(Context context, List<String> list) {
		super(context, R.style.Theme_DialogFactory);
		mContext = context;
		this.cityNameList = list;
		setContentView(R.layout.menu_scansetup);

		cityListLayout = (RelativeLayout) findViewById(R.id.relativeLayout3);
		listView = (ViewCityList) findViewById(R.id.cityListView);
		Log.v(TAG, "LL &&&&&&&&&" + cityNameList.size() + cityNameList.get(0));
		listView.init(cityNameList, 0);
		citySelect = -1;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL MenuScanSetup>>onKeyDown>>keyCode = " + keyCode);
		//listView.onKey(listView, keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			final CommonAcionDialog mydialog = new CommonAcionDialog(mContext, R.string.no_string, 0, R.string.dtv_scan_setup_waring_info, 5);//dtv_scan_setup_waring
			mydialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_CANCEL);
			mydialog.setOKButtonListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					cityListLayout.setVisibility(View.INVISIBLE);
					Log.i(TAG, "LL citySelect = " + citySelect + ", curIndex = " + listView.getCurIndex());
					if (citySelect != listView.getCurIndex()) {
						cityHasChanged = true;
						citySelect = listView.getCurIndex();
						operatorManager.setCurOperator(citySelect);//������Ӫ��
						DtvChannelManager.getInstance().reset();
						DtvConfigManager.getInstance().clearAll();
						MenuManager.getInstance().delAllScheduleEvents();
						//add get tuner info
						mDtvTunerInfo = operatorManager.getOPMainTunerInfo();
					}
					mydialog.dismiss();
					MenuScanSetup.this.dismiss();
				}
			});
			mydialog.setOnDismissListener(new OnDismissListener() {

				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if (cityHasChanged) {

					}
				}
			});
			mydialog.show();
			break;
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			dismiss();
			//menuMain.showMenu();
			break;
		default:
			break;
		}

		super.onKeyDown(keyCode, event);
		return true;
	}

	private List<String> getcityNameList(List<DtvOperator> broadcastOperatorList) {
		List<String> list = new ArrayList<String>();
		Log.i(TAG, "LL [enter] function getcityNameList()");
		if (null != broadcastOperatorList) {

			for (int i = 0; i < broadcastOperatorList.size(); i++) {
				Log.i(TAG, "LL broadcastOperatorName = " + broadcastOperatorList.get(i).getOperatorName());
				list.add(broadcastOperatorList.get(i).getOperatorName());
			}
		}
		Log.i(TAG, "LL [end] function getcityNameList()");
		return list;
	}

	public void setPositon(int xoff, int yoff) {
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = xoff;
		lp.y = yoff;
		window.setAttributes(lp);
	}

	public String getSelCityValue() {
		if (citySelect < 0) {
			return null;
		}
		return cityNameList.get(citySelect);

	}

	public boolean cityHasChanged() {
		return cityHasChanged;
	}

	public int getSetupFre() {
		if (mDtvTunerInfo != null) {
			return mDtvTunerInfo.getFrequency() / 1000;
		} else {
			return 107;
		}
	}

	public int getSymbolRate() {
		if (mDtvTunerInfo != null) {
			return mDtvTunerInfo.getSymbolRate();
		} else {
			return 6875;
		}
	}

	public int getQamMode() {
		if (mDtvTunerInfo != null) {
			return mDtvTunerInfo.getQamMode();
		} else {
			return 5;
		}
	}
}