package com.changhong.tvos.dtv.channel_list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_list.ChannelList.KeyActionInterface;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.menuManager.MenuManager.listState;
import com.changhong.tvos.dtv.util.ViewChannelInfo;

public class MyChannelListView extends RelativeLayout{

	public interface KeyDownActionListener{
		public void updateShowTime();
	}
	
	private KeyDownActionListener keyDownActionListener;
	private static final String TAG = "MyChannelListView";
	
	private ViewChannelInfo viewChannelInfo;
	private RelativeLayout bgLayout;
	private ImageView btn_next;
	public ChannelList listview;
	public Channel_Type_Layout channel_Type_Layout;
	private Context context;
	private MenuManager manager=null;	
	private TextView helpInfo;
	public MyChannelListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.setGravity(Gravity.LEFT|Gravity.TOP);
	}

	public MyChannelListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setGravity(Gravity.LEFT|Gravity.TOP);
	}

	public MyChannelListView(Context context) {
		super(context);
		this.context = context;
		this.setGravity(Gravity.LEFT|Gravity.TOP);
		menuInit();
	}


	private void menuInit() {
		
		this.bgLayout = (RelativeLayout)LayoutInflater.from(context).inflate(com.changhong.tvos.dtv.R.layout.my_channel_list_view, null);
		this.addView(bgLayout);
		this.setFocusable(true);
		helpInfo = (TextView) bgLayout.findViewById(R.id.textView2);
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Onclick Father Onclick");
				keyDownActionListener.updateShowTime();
			}
		});
		channel_Type_Layout = (Channel_Type_Layout)findViewById(R.id.channel_Type_Layout);
		channel_Type_Layout.setFocusable(false);
		channel_Type_Layout.setSelected(true);
		channel_Type_Layout.setOnTypeChangeListener(new Channel_Type_Layout.OnTypeChangeListener() {
			
			@Override
			public void refreshChannelList() {
				
				updateCurViews();
				keyDownActionListener.updateShowTime();
			}
		});
		
		viewChannelInfo = new ViewChannelInfo(context);
        addView(viewChannelInfo);
        
        btn_next = (ImageView)findViewById(R.id.chanel_nextPage);
        
        
		Log.i("my view", "begin init list");
		listview = (ChannelList)findViewById(R.id.channel_list);
		listview.requestFocus();
		
		
		listview.setKeyActionInterface(new KeyActionInterface() {
			
			@Override
			public void onkeyPressedAction(int keyCode, KeyEvent event) {
				
				onkeyDown( keyCode,  event);
			}

			
			@Override
			public void changeChannelType(boolean isChange) {
				
				if(isChange){
					channel_Type_Layout.setBackgroundResource(R.drawable.setting_picture_sel);
					listview.setSelector(R.drawable.translucent_background);
				}else {
					channel_Type_Layout.setBackgroundResource(R.drawable.translucent_background);
					listview.setSelector(R.drawable.setting_picture_sel);
				}
				
			}

			@Override
			public void showChannelInfo() {
				if(viewChannelInfo.isShown()){
					viewChannelInfo.hide();
				}
				viewChannelInfo.show();
				viewChannelInfo.updateEpgShow();
				Log.v(TAG, "mChannelInfoView is show");
			}


			@Override
			public void refreshShowTime() {
				keyDownActionListener.updateShowTime();
			}
		});
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Log.i(TAG, " EL -- >next page");
				listview.pageNext();
			}
		});
		
	}
	
	public void initMenu(listState state){
		
		manager = MenuManager.getInstance();
		manager.init(state);
		channel_Type_Layout.freshView();
		listview.initChannelList(manager);
		
	}
	
	
	public void updateCurViews(){
		Log.i(TAG,"LL updateCurViews()>>start init ChannelList******");
		this.initMenu(MenuManager.listState.channel_List);
		Log.i(TAG,"LL updateCurViews()>>end init ChannelList******");
		
		
	}
	
	
	public void onkeyDown(int keyCode, KeyEvent event) {
		Log.v(TAG,"LL ViewChannelList>>onKeyDown KeyCode"+keyCode+"event"+event);
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:	
			
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
	
			if(channel_Type_Layout.changType()){
				initMenu(MenuManager.listState.channel_List);
				viewChannelInfo.show();
				viewChannelInfo.updateEpgShow();
			}
			
			break;	
		default:
			break;
		}
		
		
	}

	public void setKeyDownActionListener(KeyDownActionListener keyDownActionListener) {
		this.keyDownActionListener = keyDownActionListener;
	}

	public KeyDownActionListener getKeyDownActionListener() {
		return keyDownActionListener;
	}


	
	
}
