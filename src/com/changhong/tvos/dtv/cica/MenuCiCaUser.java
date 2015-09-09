package com.changhong.tvos.dtv.cica;

import com.changhong.tvos.dtv.R;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuCiCaUser extends Dialog {
	private final static String TAG = "DtvCiCaUserMenu";
	//	private static ChCICAService mChCICAService;

	public static int i_contentListItemSel = 0;
	private int curMenuId;
	private int curMenuType;
	private SimpleListView contentListView = null;
	private SimpleListView titleListView = null;
	private SimpleListView operateListView = null;
	TextView tv_Title;
	TextView tv_sub_Title;
	TextView tv_help;

	private CicaAdapter operateAdapter;
	private CicaAdapter titledapter;
	private CicaAdapter itemAdapter;

	Intent mIntent = null;
	Bundle mBundle = null;
	CICAMMenuList menuList = null;
	CICAMMenuConfirm menuConfirm = null;
	CICAMMenuInput menuInput = null;
	CICAMMenuNormal menuNormal = null;
	String wait = null;
	private Context mContext;
	public Handler handler;
	public Runnable showRun;
	public Runnable sendMsgRun;
	int showTime;

	public MenuCiCaUser(Context context) {
		super(context, R.style.Theme_ActivityDialog);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setContentView(R.layout.menu_cica_user);

		contentListView = (SimpleListView) findViewById(R.id.id_ca_main_listview);
		titleListView = (SimpleListView) findViewById(R.id.id_title_listview);
		operateListView = (SimpleListView) findViewById(R.id.id_operater_listview);

		//		tv_help = (TextView) findViewById(R.id.id_ca_help);
		tv_sub_Title = (TextView) findViewById(R.id.id_ca_sub_title);
		tv_Title = (TextView) findViewById(R.id.id_ca_main_title);

		//		tv_help.setText("");
		tv_sub_Title.setText("");
		contentListView.setDivider(null);
		//		operateListView.setDivider(null);

		contentListView.setOnItemClickListener(mListener);
		operateListView.setOnItemClickListener(mListener);

		wait = context.getString(R.string.dtv_ca_wait);

		itemAdapter = new CicaAdapter(context);
		titledapter = new CicaAdapter(context);
		operateAdapter = new CicaAdapter(context);
		String Info[] = { wait };
		itemAdapter.cicaInfoInit(Info, 1, 1);
		contentListView.setAdapter(itemAdapter);
		titleListView.setAdapter(titledapter);
		operateListView.setAdapter(operateAdapter);

		titleListView.setFocusable(false);
		handler = new Handler();
		showRun = new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);

				Log.e(TAG, "Runnable EXIT");
				MenuCiCaUser.this.dismiss();
			}
		};
		showTime = 30000;
		contentListView.setOnInnerKeyDownListener(new OnInnerKeyDownListener() {

			@Override
			public void onInnerKeyDownListener() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL contentListView>>onInnerKeyDownListener()***");
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
			}
		});
		titleListView.setOnInnerKeyDownListener(new OnInnerKeyDownListener() {

			@Override
			public void onInnerKeyDownListener() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL titleListView>>onInnerKeyDownListener()***");
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
			}
		});
		operateListView.setOnInnerKeyDownListener(new OnInnerKeyDownListener() {

			@Override
			public void onInnerKeyDownListener() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL operateListView>>onInnerKeyDownListener()***");
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
			}
		});
	}

	//���ȷ���ͷ��ؼ����²˵� 3.
	/**
	 * ͨ��DtvRoot��onMsgUserMenu(Bundle mBundle)������mDtvCiCaUserMenu.updateMenu(mBundle)����;	DtvRoot 1846
	 * onMsgUserMenu(Bundle mBundle)ͨ����CiCaReceiver�е�mOnMsgReceive.onMsgUserMenu(mBundle)����(�����ʼ��mBundle);	DtvMsgManager 227 158
	 * ��CiCaReceiver��install(IMsgReceive onMsg)������ʵ����������ע��mContext.registerReceiver(mCiCaReceiver, mCicaIntentFilter); DtvMsgManager 89
	 * install������DtvRoot��mDtvMsgManager.install(new DtvMsgManager.IMsgReceive()�б����ã� DtvRoot 1734
	 * ��cica_notify_Callback�з��͹㲥
	 * 		Intent myintent = new Intent(DTVConstant.DTV_CICAM_PROMPT_NOTIRY);
		    Bundle bundle = new Bundle();//DtvPlayer 1201 s
	 * @param mBundle
	 */
	public void updateMenu(Bundle mBundle) {
		CICAMMenuBase menuBase = (CICAMMenuBase) mBundle.getParcelable(BroadcastConst.MSG_INFO_NAME);
		Log.i(TAG, "" + menuBase.miMenuType);
		switch (menuBase.miMenuType) {
		case ConstCICAMMenuType.MENU_LIST:
			Log.v(TAG, "LL DtvCiCaReceiver>>MENU_LIST");
			curMenuType = ConstCICAMMenuType.MENU_LIST;
			InstallDataList(mBundle);
			break;
		case ConstCICAMMenuType.MENU_CONFIRM:
			Log.v(TAG, "LL DtvCiCaReceiver>>MENU_CONFIRM");
			curMenuType = ConstCICAMMenuType.MENU_CONFIRM;
			InstallDataConfirm(mBundle);
			break;
		case ConstCICAMMenuType.MENU_INPUT:
			Log.v(TAG, "LL DtvCiCaReceiver>>MENU_INPUT");
			curMenuType = ConstCICAMMenuType.MENU_INPUT;
			InstallDataInput(mBundle);
			break;
		case ConstCICAMMenuType.MENU_NORMAL:
			Log.v(TAG, "LL DtvCiCaReceiver>>MENU_NORMAL");
			curMenuType = ConstCICAMMenuType.MENU_NORMAL;
			InstallDataNormal(mBundle);
			break;
		default:
			Log.v(TAG, "LL DtvCiCaReceiver>>onReceive>>msgClass>>default:");
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
		if (menuList.miMenuID == ConstCICAMMenuID.MENU_ID_ROOT) {
			Log.v(TAG, "CH_DTV_CICA_Constant.DEFALT_OPCODE_EXIT");
			curMenuId = menuList.miMenuID;
			MenuCiCaUser.this.dismiss();
			return;
		}
		showBaseInfo(menuList.mstrTitle, menuList.mstrSubtitle, menuList.mstrHelp);

		itemAdapter.cicaInfoInit(menuList.mastrList, menuList.miItems, 1);
		titledapter.cicaInfoInit(null, 0, 0);
		operateAdapter.cicaInfoInit(null, 0, 0);
		itemAdapter.notifyDataSetChanged();
		titledapter.notifyDataSetChanged();
		operateAdapter.notifyDataSetChanged();

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
		handler.removeCallbacks(showRun);
		mydialog.show();
		Log.i(TAG, "LL InstallDataConfirm>>curMenuType = " + curMenuType + ",menuConfirm.miMenuID = " + menuConfirm.miMenuID);
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
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
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
		handler.removeCallbacks(showRun);
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
				handler.removeCallbacks(showRun);
				handler.postDelayed(showRun, showTime);
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
		//        titledapter.cicaInfoInit(menuNormal.mastrContentListTitle, 1, menuNormal.miCol);//by yangliu
		operateAdapter.cicaInfoInit(menuNormal.mastrOperateList, menuNormal.miOperateItems, 1);
		itemAdapter.notifyDataSetChanged();//by yangliu
		//        titledapter.notifyDataSetChanged();
		operateAdapter.notifyDataSetChanged();
		if (menuNormal.mastrContentListTitle != null && menuNormal.mastrContentListTitle[0].equals(" ") && menuNormal.mastrContentListTitle[1].equals(" ")) {
			titledapter.cicaInfoInit(null, 0, 0);//��Ϊ������Ϣʱ
		} else {
			titledapter.cicaInfoInit(menuNormal.mastrContentListTitle, 1, menuNormal.miCol);//��Ϊ������Ϣʱ
		}
		titledapter.notifyDataSetChanged();

		if ((menuNormal.mastrOperateList == null || menuNormal.miOperateItems <= 0) && menuNormal.miCol == 1 && menuNormal.miRow == 1) {
			itemAdapter.setTextSingeLine(false);
			operateListView.setFocusable(false);
		} else {
			if (menuNormal.miOperateItems > 0) {
				operateListView.setItemsCanFocus(true);
				operateListView.setFocusable(true);
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

	//���ȷ���� 2.
	OnItemClickListener mListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int operand = position;
			int opcode = -1;
			int defOpcode = ConstCICAMOpCode.DEFAULT_OP_CODE_CONFIRM;
			handler.removeCallbacks(showRun);
			handler.postDelayed(showRun, showTime);
			Log.v(TAG, "InstallData onItemClick");

			Log.v(TAG, "��ǰ�˵����͡�������>" + curMenuType);

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
			//				tv_help.setText(wait);

			Log.i(TAG, "OnItemClickListener����>ConstCICAMsgType.MSG_USER_MENU�ǣ�" + ConstCICAMsgType.MSG_USER_MENU + "\ncurMenuType�ǣ�" + curMenuType + "\ncurMenuId�ǣ�" + curMenuId + "\noperand��"
					+ operand + "\nopcode�ǣ�" + opcode + "\ndefOpcode�ǣ�" + defOpcode);
			//			5   0	0	0	-1	0	0	null  			by yangliu
		}
	};

	//���е���� 1.
	public boolean onKeyUp(int arg0, KeyEvent arg1) {
		Log.i(TAG, "LL onKeyUp()>>arg0 = " + arg0);
		return true;
	}

	//������ؼ� 2.
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		// TODO Auto-generated method stub
		handler.removeCallbacks(showRun);
		handler.postDelayed(showRun, showTime);
		Log.v(TAG, "LL MenuCiCaUser>>keyCode == " + arg0 + ", ScanCode" + arg1.getScanCode());
		boolean isKeyboardProcessed = false;
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
			//			tv_help.setText(wait);
			return true;
		case 235://keyboard Volume Down
			if (!operateListView.isFocused()) {
				operateListView.requestFocus();
			}
			return true;
		case 236://keyboard Volume Up
			if (!contentListView.isFocused()) {
				contentListView.requestFocus();
			}

			return true;
		default:
			break;
		}
		switch (arg0) {
		case KeyEvent.KEYCODE_BACK:
			/*sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType,
						curMenuId, -1, -1,
						ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);*///by yangliu

			DtvCardStatus mCardStatus = null;

			mCardStatus = DtvCicaManager.getCardStatus();
			if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_BACK, 0, null);
				Log.i(TAG, "����˷��ؼ�,��״̬��Ϊ�գ�����ConstCICAMOpCode.DEFAULT_OP_CODE_BACK\n");
			} else {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
				dismiss();
				Log.i(TAG, "����˷��ؼ�,��״̬Ϊ�գ�����ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT\n");
			}
			Log.i(TAG, "onKeyDown����>ConstCICAMsgType.MSG_USER_MENU�ǣ�" + ConstCICAMsgType.MSG_USER_MENU + "\ncurMenuType�ǣ�" + curMenuType + "\ncurMenuId�ǣ�" + curMenuId + "\noperand�ǣ�-1"
					+ "\nopcode�ǣ�-1" + "\ndefOpcode�ǣ�" + ConstCICAMOpCode.DEFAULT_OP_CODE_BACK);
			//			5	0	0	-1	-1	1	0	null		by yangliu
			break;
		case KeyEvent.KEYCODE_SOURCE:
			//		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
		case KeyEvent.KEYCODE_MENU:
			Log.v(TAG, "onKeyDown>>KEYCODE_MENU");

			/*sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType,
					curMenuId, -1, -1,
					ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);*///by yangliu

			mCardStatus = DtvCicaManager.getCardStatus();
			if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
			} else {
				sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType, curMenuId, -1, -1, ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
				dismiss();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (curMenuType == ConstCICAMMenuType.MENU_NORMAL) {
				contentListView.requestFocus();
				//contentListView.setSelection(i_contentListItemSel);	
				itemAdapter.notifyDataSetChanged();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:

			//�����û��ѡ��ʱ�����������ý���
			//			if(curMenuType ==ConstCICAMMenuType.MENU_NORMAL){ 	
			if (curMenuType == ConstCICAMMenuType.MENU_NORMAL && menuNormal.miOperateItems > 0) {//by yangliu
				i_contentListItemSel = contentListView.getSelectedItemPosition();

				if (contentListView.getSelectedView() != null) {
					//				contentListView.getSelectedView().setBackgroundColor(Color.GRAY);
					contentListView.getSelectedView().setBackgroundColor(Color.LTGRAY);//by YangLiu
				}
				Log.v(TAG, "i_contentListItemSel =" + i_contentListItemSel);
				operateListView.requestFocus();
				operateListView.setSelection(0);
			}
			break;
		}
		Log.i(TAG, "LL onKeyDown>>isKeyboardProcessed = " + isKeyboardProcessed);
		if (isKeyboardProcessed == true) {
			return true;
		}
		return super.onKeyDown(arg0, arg1);
	}

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
			//			tv_help.setText(str2+","+context.getString(R.string.dtv_help_exit));
		} else {
			//			tv_help.setText(context.getString(R.string.dtv_help_exit)); 

		}
	}

	public void show() {

		handler.removeCallbacks(showRun);
		handler.postDelayed(showRun, showTime);
		/*		
				DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
				if (MainMenuRootData.isCAFirstShowing && mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {	
					Intent intent = new Intent(MainMenuReceiver.INTENT_CICAQUERY);
					intent.putExtra(MainMenuReceiver.DATA_CARDTYPE,mCardStatus.getCardType());
					mContext.sendBroadcast(intent);
					MainMenuRootData.isCAFirstShowing = false;
				}//By YangLiu
		*/
		Log.i(TAG, "LL this.show()>>handler.postDelayed***");

		super.show();
		DtvDialogManager.AddShowDialog(this);
	}

	public void dismiss() {
		handler.removeCallbacks(showRun);
		//		sendMsg(ConstCICAMsgType.MSG_USER_MENU, curMenuType,
		//				curMenuId, -1, -1,
		//				ConstCICAMOpCode.DEFAULT_OP_CODE_EIXT, 0, null);
		//		java.util.Map<Thread, StackTraceElement[]> ts =
		//		Thread.getAllStackTraces();
		//		StackTraceElement[] ste = ts.get(Thread.currentThread());  
		//		if(null != ste){
		//		for (StackTraceElement s : ste) {  
		//			Log.i(TAG, s.toString()); //�����android�Դ��ģ����û�У��������Ĵ�ӡ����һ��   
		//		}
		//		}
		Log.v(TAG, "dismiss");
		super.dismiss();
		DtvDialogManager.RemoveDialog(this);
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
			LinearLayout layout = (LinearLayout) LayoutInflater.from(myContext).inflate(R.layout.view_cica_list_item, null);
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
			}
			return param;
		}

		void cicaInfoInit(String Info[], int Row, int Col) {
			if (Info == null || Row == 0 || Col == 0) {
				dataInfo = null;
				mi_Row = 0;
				mi_Col = 0;
			} else {
				dataInfo = Info;
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
