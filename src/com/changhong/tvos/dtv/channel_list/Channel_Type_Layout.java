package com.changhong.tvos.dtv.channel_list;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;

public class Channel_Type_Layout extends RelativeLayout {

	public interface OnTypeChangeListener{
		public void refreshChannelList();
	}
	private OnTypeChangeListener onTypeChangeListener;
	private Context mContext;
	
	private TextView channel_type_text;
	
	private ImageView rightArrow;
	private ImageView leftArrow;
	
	private ImageView img_DotR;
	private ImageView img_DotL;
	private CommonInfoDialog myDialog;
	private int dialog_height = 70;
	private int dialog_width = 520;
	private int dialog_margin = 480;
	private int dialog_margin_y = 30;
	public static final int TYEP_RADIO = DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO;
	public static final int TYPE_TV = DTVConstant.ConstServiceType.SERVICE_TYPE_TV;

	private static final String TAG = "Channel_Type_Layout";
	
	public DtvChannelManager dtvmanager = null;
	
	public Channel_Type_Layout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
		
	}

	public Channel_Type_Layout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public Channel_Type_Layout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	
	public void init(){
		
		DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();	
		dialog_width = (int) (dialog_width * mDisplayMetrics.scaledDensity);
		dialog_height = (int) (dialog_height * mDisplayMetrics.scaledDensity);
		
		RelativeLayout relativeLayout = (RelativeLayout)LayoutInflater.from(mContext).inflate(com.changhong.tvos.dtv.R.layout.channellist_menu_optionview, null);
		
		this.addView(relativeLayout);
		
		dtvmanager =DtvChannelManager.getInstance();
		channel_type_text = (TextView)relativeLayout.findViewById(R.id.Value);
		img_DotL  =  (ImageView)relativeLayout.findViewById(R.id.DotL);
		img_DotR = (ImageView)relativeLayout.findViewById(R.id.DotR) ;
		
		myDialog = new CommonInfoDialog(mContext);
		myDialog.setGravity(Gravity.BOTTOM|Gravity.LEFT, dialog_margin, dialog_margin_y);
		myDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
		myDialog.info_layout.setLayoutParams(new FrameLayout.LayoutParams(dialog_width,dialog_height));
		myDialog.tv.setTextColor(Color.WHITE);
		myDialog.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
//		freshView();
		
		rightArrow = (ImageView)relativeLayout.findViewById(R.id.RImage);
		leftArrow = (ImageView)relativeLayout.findViewById(R.id.LImage);
		
		rightArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(changType()){
					onTypeChangeListener.refreshChannelList();
				}
			}
		});
		
		leftArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(changType()){
					onTypeChangeListener.refreshChannelList();
				}
			}
		});
	}
	
	public boolean setType(int channelType){
		boolean ischange = dtvmanager.changeChannelType();
		if(ischange){
			freshView();
		}else {
			
			myDialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
			if(dtvmanager.getCurChannelType() == ConstServiceType.SERVICE_TYPE_TV){
				myDialog.setMessage(mContext.getString(R.string.NoRadioChannel));
			}else{
				myDialog.setMessage(mContext.getString(R.string.NoDtvChannel));
			}
			
			myDialog.setDuration(3);
			myDialog.show();
		}
		
		return ischange;
	}
	
	public int getType(){
		return dtvmanager.getCurChannelType();
	}

	public void freshView() {
		// TODO Auto-generated method stub
		if(dtvmanager.getCurChannelType() == TYPE_TV){
			channel_type_text.setText("ต็     สำ ");
			img_DotL.setImageResource(R.drawable.dot_sel);
			img_DotR.setImageResource(R.drawable.dot_unsel);
			Log.v(TAG, "-->Fresh Type View setType TYPE_TV<--");
		}else{
			channel_type_text.setText("นใ     ฒฅ ");
			img_DotR.setImageResource(R.drawable.dot_sel);
			img_DotL.setImageResource(R.drawable.dot_unsel);
			Log.v(TAG, "-->Fresh Type View setType TYPE_RADIO<--");
		}
	}

	public boolean changType() {
		return setType(TYPE_TV);
		
	}

	public void setOnTypeChangeListener(OnTypeChangeListener onTypeChangeListener) {
		this.onTypeChangeListener = onTypeChangeListener;
	}

	public OnTypeChangeListener getOnTypeChangeListener() {
		return onTypeChangeListener;
	}

	
}
