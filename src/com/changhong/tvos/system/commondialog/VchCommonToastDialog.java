package com.changhong.tvos.system.commondialog;

import java.util.Timer;
import java.util.TimerTask;
import com.changhong.tvos.dtv.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 闂侇偅姘ㄩ弫銈嗙┍閳╁啩绱栭柟缁樺姉閵囨氨绮ｉ敓锟介敓鏂ゆ嫹
 * @author hs_yataozhang,jinwei.yang
 */
public class VchCommonToastDialog extends Dialog {
	/**
	 * 闁圭娲ら悾鎯ь嚕閸︻厾宕剁紒鎰殰瑜版盯鎯冮崟顓☆枂闁搞劌顑戠槐浼村箣閹邦剙顬氶柕鍡曠窔閺佸﹦鎷犻姗堟嫹閿燂拷閿熸枻鎷烽幉锟犲Υ娴ｉ顏遍柤鍓插厸娣囧﹪骞侀敓锟? */
	public final static int TYPE_OK = 0;
	public final static int TYPE_ERROR = 1;
	public final static int TYPE_WARN = 2;
	public final static int TYPE_MESSAGE = 3;
	public final static int TYPE_NONE = -1;
	
	public TextView tv;
	public LinearLayout info_layout;
	private Dialog mDialog=this;
	/**
	 * 闁哄嫬澧介妵姘辩玻閿燂拷閿熸枻鎷烽柣銊ュ鐎垫梻绱掗鐔割槼闂傚偊锟?	 */
	private int mDuration=1;
	private Context mContext;
	/**
	 * 闁活枌鍔嶉崺娑㈡儍閸曨剙绲归敓锟介敓鏂ゆ嫹娣囧﹪骞侀敓锟? */
	private String mMessageText="";
	/**
	 * 闁哄嫬澧介妵姘跺炊閻愵剛锟?
	 */
	private ImageView hintIcon;
	/**
	 * 鐟滅増鎸告晶鐘绘儍閸曨偉鍓ㄧ紒鎰殶鐞氼偊宕归敓锟? */
	private int mInfoType = TYPE_NONE;
	
	/**
	 * 濮掓稒顭堥濠氬几閸曨垽鎷烽柛鎴ｅГ閿燂拷
	 */
	public VchCommonToastDialog(Context context) {
		super(context, R.style.Theme_ToastDialog);
		// TODO Auto-generated constructor stub
		mContext=context;
		initView();
	}
	/**
	 * 闁哄瀚伴敓浠嬪礄閼恒儲锟?
	 * @param context
	 * @param infoType闁挎稒鑹鹃懘濠勭玻濡ゅ啳顤嗛柛顭掓嫹TYPE_OK缂佹冻锟?	 * @param message闁挎稒纰嶅Ο澶岀矆閾忚鐣辨繛鎴濈墛閿燂拷
	 * @param duration闁挎稒纰嶇�鏃傜磼椤撶姵鐣遍柡鍐ㄧ埣閿燂�	 */
	public VchCommonToastDialog(Context context, int infoType, int mesId, int duration)
	{
		super(context, R.style.Theme_ToastDialog);
		mContext=context;
		mDuration = duration;
		this.mInfoType = infoType;
		mMessageText = context.getString(mesId);		
		initView();
	}
	/**
	 * 闂傚牊鐟﹂敓浠嬪礄閼恒儲娈堕柨娑樼灱閺併倖绂嶆惔鈩冨渐闂侇偆鍠曢鏇犵磾椤旇壈鍓ㄩ柛鎴ｆ閻涖儵宕ｉ敓锟? * @param context
	 * @param infoType闁挎稒鑹鹃懘濠勭玻濡ゅ啳顤嗛柛顭掓嫹
	 * @param message闁挎稒纰嶅Ο澶岀矆閾忚鐣辨繛鎴濈墛閿燂拷
	 * @param duration闁挎稒纰嶇�鏃傜磼椤撶姵鐣遍柡鍐ㄧ埣閿燂�	 * @return CommonInfoDialog闁搞劌顑呴顔炬寬閿燂拷	 */
	public static VchCommonToastDialog makeDialog(Context context, int infoType, int mesId, int duration)
	{
		VchCommonToastDialog result = new VchCommonToastDialog(context, infoType, mesId, duration);
		
        return result;
	}
	/**
	 * 闁告帗绻傞‖濠囧礌閺堫潻ew
	 */
	private void initView() {
		// TODO Auto-generated method stub
		LayoutInflater layout = (LayoutInflater) mContext
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = layout.inflate(R.layout.vch_common_toast_dialog,null);
		
		tv = (TextView) (view.findViewById(R.id.vch_common_toast_hintmessage));
		tv.setText(getMessage());

		info_layout = (LinearLayout)view.findViewById(R.id.vch_common_toast_layout);
		
		hintIcon=(ImageView)(view.findViewById(R.id.vch_common_toast_hinticon));
		switch(mInfoType)
		{
		case TYPE_OK:
			setHintIcon(0);
			break;
		case TYPE_ERROR:
			setHintIcon(0);
			break;
		case TYPE_WARN:
			setHintIcon(0);
			break;
		case TYPE_MESSAGE:
			setHintIcon(0);
		default:
			break;
		}
		
		this.setContentView(view);
	}

	/**
	 * 闁告凹鍨版慨鈺嬫嫹?濮橆厽顦抽柛锝庣厜缁辨繈鎮介妸銈囪壘闁告帡鏀遍弻濠囧及閸撗佷粵
	 */
	private void startTimeTask() {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				// what to do()
				if(mDialog!=null&&mDuration>0)
				{
					mDialog.show();
					mDuration--;
					Log.v("CHLauncher", "mDuration:"+mDuration);
				}
				else
				{
					if(mDialog != null)
					{
						mDialog.cancel();
					}
					timer.cancel();
					Log.v("CHLauncher", "mDialog.cancel();");
				}
			}
		};
		timer.schedule(task, 1 * 1000, 1 * 1000);
	}

	/**
	 * 
	 * 閻犱礁澧介悿鍡楊嚕閸︻厾宕堕柣銊ュ鐎垫梻绱掗鐔割槼闂傚偊锟?	 * @param duration:闁归晲鑳堕悽濠氭儍閸曨剚顦抽梻鍌︽嫹
	 */
	public void setDuration(int duration) {
		// TODO Auto-generated method stub
		this.mDuration=duration;
	}

	/**
	 * 闁哄嫬澧介妵姘嚕閻熸澘姣夌紒鎰殰閿燂拷
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();  //修改在弹出开始录制提示框时Can't create handler inside thread that has not called Looper.prepare()导致的异常 2015-3-2
		startTimeTask();
	}

	/**
	 * 闁兼儳鍢茶ぐ鍥偨閵婏箑鐓曢柣銊ュ瑜颁胶绮堟潪棰佺箚闁诡叏锟?	 * @return the mHintMessage
	 */
	public String getMessage() {
		return mMessageText;
	}

	/**
	 * 閻犱礁澧介悿鍡涙偨閵婏箑鐓曢柣銊ュ瑜颁胶绮堟潪棰佺箚闁诡叏锟?	 * @param mHintMessage 闁挎稒姘ㄩ弫銈夊箣閻ゎ垼鍟庣紓鍐惧枤濞堟垿骞撻幇顔轰粵濞ｅ洠鍓濇导鍜冩嫹?濡ゅ喚鍎婂☉鎿勬嫹
	 */
	public void setMessage(String message) {
		this.mMessageText = message;
		tv.setText(message);
	}
	/**
	 * 閻犱礁澧介悿鍡涙偨閵婏箑鐓曢柣銊ュ瑜颁胶绮堟潪棰佺箚闁诡叏锟?	 * @param mesId闁挎稒姘ㄩ弫銈夊箣閻ゎ垼鍟庣紓鍐惧枤濞堟垿骞撻幇顔轰粵濞ｅ洠鍓濇导鍛導閸曨剛鐖眎d
	 */
	public void setMessage(int mesId) {
		if(mesId > 0)
			setMessage(mContext.getString(mesId));
		else
			setMessage("");
	}
	/**
	 * 閻犱礁澧介悿鍡涘炊閻愵剛鍨奸悹褍瀚花鐢�
	 * @param resID闁挎稒姘ㄩ弫銈夊箣閻ゎ垼鍟庣紓鍐惧枤濞堟垿骞撻幇顔轰粵闁搞儳鍋撻悥锝囨導閸曨剛鐖眎d
	 */
	public void setHintIcon(int resID){
		if(resID > 0)
			hintIcon.setImageResource(resID);
	}
	/**
	 * 閻犱礁澧介悿鍡涘炊閻愵剛鍨奸悹褍瀚敓锟?	 * @param drawable闁挎稒姘ㄩ弫銈夊箣閻ゎ垼鍟庣紓鍐惧枤濞堟垿骞撻幇顔轰粵闁搞儳鍋撻悥锝囨導閸曨剛鐖遍悗鐢殿攰閿燂拷
	 */
	public void setHintIcon(Drawable drawable){
		hintIcon.setImageDrawable(drawable);
	}
	
	/**
	 * 閻犱礁澧介悿鍡欑玻閿燂拷閿熸枻鎷烽柣銊ュ鐞氼偊宕归敓锟? * @param iType闁挎稒鑹鹃懘濠勭玻濡ゅ啳顤嗛柛銊ョ崲d闁挎稑顒璝PE_OK,TYPE_ERROR......
	 */
	public void setInfoType(int iType)
	{
		mInfoType = iType;
	}
	/**
	 * 闁兼儳鍢茶ぐ鍥╃玻閿燂拷閿熸枻鎷烽柣銊ュ鐞氼偊宕归敓锟? * @return int闁挎稒鑹鹃懘濠勭玻濡ゅ啳顤嗛柛銊ョ崲d闁挎稑顒璝PE_OK,TYPE_ERROR......
	 */
	public int getInfoType()
	{
		return mInfoType;
	}
}