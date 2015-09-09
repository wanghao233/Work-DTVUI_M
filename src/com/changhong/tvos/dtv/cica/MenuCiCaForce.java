package com.changhong.tvos.dtv.cica;

import com.changhong.softkeyboard.CHSoftKeyboardManager;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;
import com.changhong.tvos.dtv.tvap.DtvCicaManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.util.SimpleListView;
import com.changhong.tvos.dtv.util.SimpleListView.OnInnerKeyDownListener;
import com.changhong.tvos.dtv.vo.CICAMMenuBase;
import com.changhong.tvos.dtv.vo.CICAMMenuConfirm;
import com.changhong.tvos.dtv.vo.CICAMMenuInput;
import com.changhong.tvos.dtv.vo.CICAMMenuList;
import com.changhong.tvos.dtv.vo.CICAMMenuNormal;
import com.changhong.tvos.dtv.vo.CICAMMessageBase.ConstCICAMsgType;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardStatus;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMMenuID;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMMenuType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstCICAMOpCode;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuCiCaForce extends Dialog {
	private final static String TAG = "DtvCiCaUserMenu";
	//	private static ChCICAService mChCICAService;
	private LinearLayout layout;
	public static int i_contentListItemSel = 0;
	private int curMenuId;
	private int curMenuType;
	private SimpleListView contentListView = null;
	private SimpleListView titleListView = null;
	TextView tv_Title;
	TextView tv_sub_Title;
	//	TextView tv_help;

	private CicaAdapter titledapter;
	private CicaAdapter itemAdapter;

	Intent mIntent = null;
	Bundle mBundle = null;
	CICAMMenuList menuList = null;
	CICAMMenuConfirm menuConfirm = null;
	CICAMMenuInput menuInput = null;
	CICAMMenuNormal menuNormal = null;
	String wait = null;
	//MenuMain menuMain;
	private Context mContext;
	public Handler handler;
	public Runnable showRun;
	public Runnable sendMsgRun;
	int showTime;

	private boolean isMoving = false;
	private int xPostion = 0;
	private int yPostion = 0;
	private Runnable moveRun;
	private int xoff = 0;
	private int yoff = 0;
	private boolean moveRight = true;
	private int xWidth = 0;
	private int yHeight = 0;
	private int xOffset = 20;
	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;
	OnVirtualKeyDownListener mOnVirtualKeyDownListener = null;

	public interface OnVirtualKeyDownListener {
		public void onVirtualKeyDownListener(int keyCode, KeyEvent event);
	}

	public void setOnVirtualKeyDownListener(OnVirtualKeyDownListener listener) {
		mOnVirtualKeyDownListener = listener;
	}

	public MenuCiCaForce(Context context) {
		super(context, R.style.Theme_ActivityDialog);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setContentView(R.layout.menu_cica_force);

		layout = (LinearLayout) findViewById(R.id.linearLayout0);
		contentListView = (SimpleListView) findViewById(R.id.id_ca_main_listview);
		titleListView = (SimpleListView) findViewById(R.id.id_title_listview);

		//		tv_help = (TextView) findViewById(R.id.id_ca_help);
		tv_sub_Title = (TextView) findViewById(R.id.id_ca_sub_title);
		tv_Title = (TextView) findViewById(R.id.id_ca_main_title);

		wait = context.getString(R.string.dtv_ca_wait);

		//		tv_help.setText(wait);
		tv_sub_Title.setText("");
		contentListView.setDivider(null);
		contentListView.setOnItemClickListener(mListener);
		itemAdapter = new CicaAdapter(context);
		titledapter = new CicaAdapter(context);
		String Info[] = { wait };
		itemAdapter.cicaInfoInit(Info, 1, 1);
		contentListView.setAdapter(itemAdapter);
		titleListView.setAdapter(titledapter);

		titleListView.setFocusable(false);

		handler = new Handler();
		showRun = new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				//				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType,
				//						curMenuId, -1, -1,
				//						ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
				//				
				//				Log.e(TAG, "Runnable EXIT");
				//				MenuCiCaForce.this.dismiss(); 
				isMoving = true;
				handler.post(moveRun);
				Log.i(TAG, "LL showRun>>start to move***");
			}
		};
		showTime = 30000;

		titleListView.setOnInnerKeyDownListener(new OnInnerKeyDownListener() {

			@Override
			public void onInnerKeyDownListener() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL titleListView>>onInnerKeyDownListener()***");
				removeMoveRunCallback();
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
			}
		});
		contentListView.setOnInnerKeyDownListener(new OnInnerKeyDownListener() {

			@Override
			public void onInnerKeyDownListener() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL contentListView>>onInnerKeyDownListener()***");
				removeMoveRunCallback();
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
			}
		});
		xPostion = (int) (mContext.getResources().getDisplayMetrics().scaledDensity * 400);
		yPostion = -(int) (mContext.getResources().getDisplayMetrics().scaledDensity * 100);
		xoff = xPostion;
		yoff = yPostion;
		Log.i(TAG, "LL setPosition()>>xPostion=" + xPostion + ",yPostion=" + yPostion);
		this.setPositon(xPostion, yPostion);
	}

	public void updateMenu(Bundle mBundle) {
		CICAMMenuBase menuBase = (CICAMMenuBase) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);//DTVPlayer 1201	
		//cacicallback_class implements cacicallback中的cica_notify_Callback方法，发送bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, obj);
		//cacicallback_class的实例化对象callback_obj3被底层DTVServiceJNI.get_caci_instance().RegisterCallback(objRouterID, callback_obj3);调用
		//即通过DTVServiceJNI.get_caci_instance().RegisterCallback(objRouterID, callback_obj3);发送obj广播
		Log.i(TAG, "" + menuBase.miMenuType);
		switch (menuBase.miMenuType) {
		case ConstCICAMMenuType.MENU_LIST:
			Log.v(TAG, "DtvCiCaReceiver>>MENU_LIST");
			curMenuType = ConstCICAMMenuType.MENU_LIST;
			InstallDataList(mBundle);
			break;
		case ConstCICAMMenuType.MENU_CONFIRM:
			Log.v(TAG, "DtvCiCaReceiver>>MENU_CONFIRM");
			curMenuType = ConstCICAMMenuType.MENU_CONFIRM;
			InstallDataConfirm(mBundle);
			break;
		case ConstCICAMMenuType.MENU_INPUT:
			Log.v(TAG, "DtvCiCaReceiver>>MENU_INPUT");
			curMenuType = ConstCICAMMenuType.MENU_INPUT;
			InstallDataInput(mBundle);
			break;
		case ConstCICAMMenuType.MENU_NORMAL:
			Log.v(TAG, "DtvCiCaReceiver>>MENU_NORMAL");
			curMenuType = ConstCICAMMenuType.MENU_NORMAL;
			InstallDataNormal(mBundle);
			break;
		default:
			Log.v(TAG, "DtvCiCaReceiver>>onReceive>>msgClass>>default:");
			break;
		}
	}

	private void InstallDataList(Bundle p_Bundle) {
		int i = 0;

		mBundle = p_Bundle;
		if (null == mBundle) {
			Log.v(TAG, "InstallData>>List Menu mBundle null err");
			return;
		}

		menuList = (CICAMMenuList) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);

		if (null == menuList) {
			return;
		}
		Log.i(TAG, "LL InstallDataList>>menuList.miMenuID = " + menuList.miMenuID);
		if (menuList.miMenuID == ConstCICAMMenuID.MENU_ID_ROOT) {
			Log.v(TAG, "CH_DTV_CICA_Constant.DEFALT_OPCODE_EXIT");
			curMenuId = menuList.miMenuID;
			MenuCiCaForce.this.dismiss();
			return;
		}
		showBaseInfo(menuList.mstrTitle, menuList.mstrSubtitle, menuList.mstrHelp);

		itemAdapter.cicaInfoInit(menuList.mastrList, menuList.miItems, 1);
		titledapter.cicaInfoInit(null, 0, 0);
		itemAdapter.notifyDataSetChanged();
		titledapter.notifyDataSetChanged();

		i_contentListItemSel = 0;

		if (menuList.miItems > 0) {
			contentListView.setOnItemClickListener(mListener);
			contentListView.setItemsCanFocus(true);
			contentListView.requestFocus();
			contentListView.setSelection(i_contentListItemSel);
		}
		curMenuId = menuList.miMenuID;
		this.show();
	}

	void InstallDataConfirm(Bundle p_Bundle) {
		if (p_Bundle == null) {
			return;
		}
		menuConfirm = (CICAMMenuConfirm) p_Bundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
		final MenuCiCaInput mydialog = new MenuCiCaInput(mContext, p_Bundle, 1);
		removeMoveRunCallback();
		handler.removeCallbacks(showRun);
		mydialog.show();
		mydialog.getOKbutton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, menuConfirm.miMenuID, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM, 0, null);
				mydialog.dismiss();
			}
		});
		mydialog.getNObutton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, menuConfirm.miMenuID, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);
				mydialog.dismiss();
			}
		});
		mydialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, menuConfirm.miMenuID, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);
			}
		});
		mydialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				removeMoveRunCallback();
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
				Log.i(TAG, "LL InstallDataConfirm>>handler.postDelayed***");
			}
		});

		curMenuId = menuConfirm.miMenuID;
		//		tv_help.setText("");
		super.show();
	}

	void InstallDataInput(Bundle p_Bundle) {
		if (p_Bundle == null) {
			return;
		}
		menuInput = (CICAMMenuInput) p_Bundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
		final MenuCiCaInput mydialog = new MenuCiCaInput(mContext, p_Bundle, 2);
		mydialog.setLayoutPositon(xPostion, yPostion);
		removeMoveRunCallback();
		handler.removeCallbacks(showRun);
		if (null != mAdjustCHSKM) {
			if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
				mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
			}
		}
		mydialog.show();
		Log.i(TAG, "LL InstallDataInput>>curMenuType = " + curMenuType + ",menuInput.miMenuID = " + menuInput.miMenuID + ",menuInput.miItems = " + menuInput.miItems);
		mydialog.getOKbutton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, menuInput.miMenuID, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM, menuInput.miItems, mydialog.getString());
				mydialog.dismiss();
			}
		});
		mydialog.getNObutton().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, menuInput.miMenuID, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);
				mydialog.dismiss();
			}
		});
		mydialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, menuInput.miMenuID, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);
			}
		});
		mydialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				removeMoveRunCallback();
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
				Log.i(TAG, "LL InstallDataInput>>handler.postDelayed***");
			}
		});
		curMenuId = menuInput.miMenuID;
		//		tv_help.setText("");
		super.show();
	}

	void InstallDataNormal(Bundle p_Bundle) {
		if (p_Bundle == null) {
			return;
		}

		menuNormal = (CICAMMenuNormal) p_Bundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
		if (menuNormal == null) {
			return;
		}
		showBaseInfo(menuNormal.mstrTitle, menuNormal.mstrSubtitle, menuNormal.mstrHelp);

		itemAdapter.cicaInfoInit(menuNormal.mastrContentList, menuNormal.miRow, menuNormal.miCol);
		titledapter.cicaInfoInit(menuNormal.mastrContentListTitle, 1, menuNormal.miCol);
		itemAdapter.notifyDataSetChanged();
		titledapter.notifyDataSetChanged();

		if ((menuNormal.mastrOperateList == null || menuNormal.miOperateItems <= 0) && menuNormal.miCol == 1 && menuNormal.miRow == 1) {
			itemAdapter.setTextSingeLine(false);
		} else {
			if (menuNormal.miOperateItems > 0) {
			}
			i_contentListItemSel = 0;

			if (menuNormal.miRow > 0) {
				contentListView.setItemsCanFocus(true);
				contentListView.requestFocus();
				contentListView.setSelection(i_contentListItemSel);
			}
		}
		contentListView.setOnItemClickListener(null);
		curMenuId = menuNormal.miMenuID;
		this.show();
	}

	OnItemClickListener mListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int operand = position;
			int opcode = -1;
			int defOpcode = ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM;
			removeMoveRunCallback();
			handler.removeCallbacks(showRun);
			handler.postDelayed(showRun, showTime);
			Log.v(TAG, "LL InstallData onItemClick>>handler.postDelayed***");
			switch (curMenuType) {
			case ConstCICAMMenuType.MENU_LIST:
			case ConstCICAMMenuType.MENU_CONFIRM:
			case ConstCICAMMenuType.MENU_INPUT:
				break;
			case ConstCICAMMenuType.MENU_NORMAL:
				operand = i_contentListItemSel;
				opcode = position;
				defOpcode = -1;
				break;
			default:
				return;
			}

			sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, operand, opcode, defOpcode, 0, null);
			//			tv_help.setText(wait);
		}
	};

	@Override
	public boolean onKeyUp(int arg0, KeyEvent arg1) {
		Log.i(TAG, "LL onKeyUp()>>arg0 = " + arg0);
		switch (arg0) {
		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD: //软键盘
			if (mAdjustCHSKM == null) {
				mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);
				Log.i(TAG, "LL onKeyUp()>>mAdjustCHSKM为空");
			}
			if (null != mAdjustCHSKM) {
				if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
					Log.i(TAG, "LL onKeyUp()>>mAdjustCHSKM键盘显示");
				} else {

					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_CENTER);
					Log.i(TAG, "LL onKeyUp()>>mAdjustCHSKM键盘不显示");
					return true;
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {

		// TODO Auto-generated method stub
		removeMoveRunCallback();
		handler.removeCallbacks(showRun);
		handler.postDelayed(showRun, showTime);
		Log.v(TAG, "LL MenuCiCaForce>>keyCode == " + arg0 + ", ScanCode" + arg1.getScanCode());
		switch (arg1.getScanCode()) {
		case 231://keyboard Menu
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			Log.i(TAG, "LL onKeyDown>>clickSelectView***");
			int operand = contentListView.getSelectedItemPosition();
			int opcode = -1;
			int defOpcode = ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM;
			handler.removeCallbacks(showRun);
			handler.postDelayed(showRun, showTime);
			Log.v(TAG, "InstallData onItemClick");
			switch (curMenuType) {
			case ConstCICAMMenuType.MENU_LIST:
			case ConstCICAMMenuType.MENU_CONFIRM:
			case ConstCICAMMenuType.MENU_INPUT:
				break;
			case ConstCICAMMenuType.MENU_NORMAL:
				operand = i_contentListItemSel;
				opcode = contentListView.getSelectedItemPosition();
				defOpcode = -1;
				break;
			default:
				return false;
			}

			sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, operand, opcode, defOpcode, 0, null);
			Log.i(TAG, "LL keyboard Source");
			//			tv_help.setText(wait);
			return true;
		case 235://keyboard Volume Down
		case 236://keyboard Volume Up
			return true;
		default:
			break;
		}
		switch (arg0) {
		case KeyEvent.KEYCODE_BACK:
			DtvCardStatus mCardStatus = null;
			Log.v(TAG, "onKeyDown>>KEYCODE_BACK");

			mCardStatus = DtvCicaManager.getCardStatus();
			if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);
			} else {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
				dismiss();
			}

			break;
		case KeyEvent.KEYCODE_SOURCE:
			//		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
		case KeyEvent.KEYCODE_MENU:
			Log.v(TAG, "onKeyDown>>KEYCODE_MENU");

			mCardStatus = DtvCicaManager.getCardStatus();
			if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
			} else {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
				dismiss();
			}
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
		case KeyEvent.KEYCODE_CHANNEL_UP://KEYCODE_STAIR_CHANNEL_UP:
		case KeyEvent.KEYCODE_CHANNEL_DOWN://KEYCODE_STAIR_CHANNEL_DOWN:
		case KeyEvent.KEYCODE_DPAD_DOWN://KEYCODE_STAIR_CHANNEL_UP:
			//case KeyEvent.KEYCODE_CHANNEL_DOWN://KEYCODE_STAIR_CHANNEL_DOWN:
		case KeyEvent.KEYCODE_DPAD_UP:

			Log.i(TAG, "LL 虚拟键按下");

			if (mOnVirtualKeyDownListener != null) {
				mOnVirtualKeyDownListener.onVirtualKeyDownListener(arg0, arg1);
			}
			//		sendKey(arg0);
			sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
			dismiss();
			break;
		//		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD: //KEYCODE_CHANGHONGIR_SOFTKEYBOARD 软键盘
		//			if (mAdjustCHSKM == null) {
		//				mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);		
		//			}
		//			if(null != mAdjustCHSKM){
		//				if(mAdjustCHSKM.isSoftKeyPanelOnShow()){
		//		
		//					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
		//				}else{
		//					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_LEFT);
		//					return true;
		//				}
		//			}
		//			break;
		default:
			break;
		}

		return super.onKeyDown(arg0, arg1);
	}

	//	public void sendKey(final int keyCode)
	//	{
	//		Thread mThread=new Thread(){
	//			public void run()
	//			{
	//
	//				try {
	//					Thread.sleep(50);
	//					Object object = new Object();
	//					Method getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
	//					Object obj = getService.invoke(object, new Object[]{new String("window")});
	//					IWindowManager manager = IWindowManager.Stub.asInterface((IBinder)obj);
	////					IWindowManager manager=IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
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
	void sendMsg(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, String[] arg7) {
		DtvCicaManager.cicaQueryControl(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);

	}

	void showBaseInfo(String mstr_Title, String mstr_Subtitle, String mstr_Help) {
		if (null != mstr_Title) {
			tv_Title.setText(mstr_Title);
		}

		if (null != mstr_Subtitle) {

			tv_sub_Title.setText(mstr_Subtitle);
		} else {
			tv_sub_Title.setText("");

		}
		if (null != mstr_Help) {

			String str1 = mstr_Help.replace("OK", mContext.getResources().getString(R.string.menu_sure));
			String str2 = str1.replace(mContext.getString(R.string.menu_setting_foot_exit), mContext.getResources().getString(R.string.menu_setting_foot_back));
			//			tv_help.setText(str2+","+mContext.getString(R.string.dtv_help_exit));
		} else {
			//			tv_help.setText(mContext.getString(R.string.dtv_help_exit)); 

		}
	}

	public void removeMoveRunCallback() {
		handler.removeCallbacks(moveRun);
		if (isMoving == true) {
			setPositon(xPostion, yPostion);
			xoff = xPostion;
			yoff = yPostion;
			moveRight = true;
			isMoving = false;
		}
	}

	public void show() {
		removeMoveRunCallback();
		handler.removeCallbacks(showRun);
		handler.postDelayed(showRun, showTime);
		Log.i(TAG, "LL this.show()>>handler.postDelayed***");
		super.show();
		DtvDialogManager.AddShowDialog(this);
		if (moveRun == null) {
			Window window = this.getWindow();
			yHeight = window.getWindowManager().getDefaultDisplay().getHeight();
			xWidth = window.getWindowManager().getDefaultDisplay().getWidth() - layout.getLayoutParams().width;
			Log.i(TAG, "LL yHeight = " + yHeight + ", xWidth = " + xWidth + ", layout = " + layout.getLayoutParams().width);
			moveRun = new Runnable() {
				public void run() {
					// TODO Auto-generated method stub

					if (moveRight) {
						if (xoff < xWidth / 2) {
							xoff += xOffset;
						} else {
							moveRight = false;
						}
					} else {
						if (xoff > xWidth / 2 - layout.getLayoutParams().width) {
							xoff -= xOffset;
						} else {
							moveRight = true;
						}

					}
					Log.i(TAG, "LL this.show()>>xoff = " + xoff);
					setPositon(xoff, yPostion);
					handler.postDelayed(moveRun, 1000);
				}
			};
		}
	}

	public void dismiss() {
		removeMoveRunCallback();
		handler.removeCallbacks(showRun);
		Log.v(TAG, "LL dismiss***");
		super.dismiss();
		DtvDialogManager.RemoveDialog(this);
	}

	public void setPositon(int xoff, int yoff) {
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = xoff;
		lp.y = yoff;
		window.setAttributes(lp);
	}

	class CicaAdapter extends BaseAdapter {
		String[] dataInfo = null;
		int mi_Row = 0;
		int mi_Col = 0;
		boolean singleLine = true;
		Context myContext;

		public CicaAdapter(Context myContext) {
			// TODO Auto-generated constructor stub
			this.myContext = myContext;
			dataInfo = null;
			mi_Row = 0;
			mi_Col = 0;

		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mi_Row;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LinearLayout layout = (LinearLayout) LayoutInflater.from(myContext).inflate(R.layout.view_cica_force_list_item, null);
			LinearLayout itemLayout = (LinearLayout) layout.findViewById(R.id.linearLayout2);
			itemLayout.removeAllViews();
			for (int i = 0; i < mi_Col; i++) {
				TextView textInfo = new TextView(myContext);
				textInfo.setTextSize(20);
				if ((mi_Col * position + i) < dataInfo.length) {
					textInfo.setText(dataInfo[mi_Col * position + i]);
				}
				textInfo.setTextColor(Color.BLACK);
				textInfo.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				if (singleLine) {
					textInfo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
					textInfo.setSingleLine(singleLine);
					textInfo.setMarqueeRepeatLimit(-1);
					textInfo.setPadding(0, 0, 10, 0);
					itemLayout.addView(textInfo, getLayoutParams(i));
				} else {
					textInfo.setText("       " + dataInfo[0]);
					return textInfo;
				}
				//textInfo.setFocusable(true);	
			}
			return layout;
		}

		private LayoutParams getLayoutParams(int index) {
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			if (mi_Col > 1) {
				//    				int windowWidth = mContext.getResources().getDisplayMetrics().widthPixels;
				//    				float windwoDensity = (float) windowWidth/1280;
				//    				int viewWidth = (int) (windwoDensity*640);
				//    				param.width = viewWidth / mi_Col;
				//    				Log.i(TAG,"LL windowWidth = " + windowWidth + ",windwoDensity = " +  windwoDensity + ",viewWidth = " + viewWidth + "clipViewWidth = " + param.width);
				param.width = (int) (mContext.getResources().getDisplayMetrics().scaledDensity * 640) / mi_Col;
				//    				Log.i(TAG,"scaledDensity = " + mContext.getResources().getDisplayMetrics().scaledDensity + ",width = " + param.width);
			} else {
				param.width = (int) (mContext.getResources().getDisplayMetrics().scaledDensity * 640);
			}
			return param;
		}

		void cicaInfoInit(String[] info, int Row, int Col) {
			if (info == null || Row == 0 || Col == 0) {
				dataInfo = null;
				mi_Row = 0;
				mi_Col = 0;
			} else {
				dataInfo = info;
				mi_Row = Row;
				mi_Col = Col;
				singleLine = true;
			}
		}

		void setTextSingeLine(Boolean singleLine) {
			this.singleLine = singleLine;
		}
	}

}
