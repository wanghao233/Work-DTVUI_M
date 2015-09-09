package com.changhong.tvos.dtv.util;

import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewRadioBackground extends RelativeLayout {

	private RelativeLayout mView;
	private TextView curPlay;
	private TextView pre1;
	private TextView pre2;
	private TextView next1;
	private TextView next2;
	private TextView next3;
	private Context mContext;
	
	private DtvChannelManager mChannelManager;
	
	private static final String TAG = "ViewRadioBackground";
	public ViewRadioBackground(Context context) {
		super(context);
		mContext = context;
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		 mView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.view_radio_show, null);
		 this.addView(mView);
		 curPlay = (TextView) mView.findViewById(R.id.textView1);
		 pre1 = (TextView) mView.findViewById(R.id.textView2);
		 pre2 = (TextView) mView.findViewById(R.id.textView3);
		 next1 = (TextView) mView.findViewById(R.id.textView4);
		 next2 = (TextView) mView.findViewById(R.id.textView5);
		 next3 = (TextView) mView.findViewById(R.id.textView6);
		 
		 mView.setVisibility(View.GONE);
	}

	public ViewRadioBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ViewRadioBackground(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void update(List<DtvProgram> programs, DtvProgram curProgram){
		if(null == curProgram || programs == null || programs.size() == 0 || !curProgram.isRadio()){
			hide();
			return;
		}
		mView.setVisibility(View.VISIBLE);
		int curIndex = programs.indexOf(curProgram);
		int size = programs.size();
		if(-1 != curIndex){
			curPlay.setText(curProgram.getProgramName());
			try {
				if(curIndex == (size -1)){ //最后一个节目
					next1.setText("");
					next2.setText("");
					next3.setText("");
				}else if(curIndex == (size -2)){//倒数第二个
					next3.setText("");
					next2.setText("");
					next1.setText(programs.get(curIndex +1).getProgramName());
				}else if(curIndex == (size - 3)){//倒数第三个
					next3.setText("");
					next2.setText(programs.get(curIndex +2).getProgramName());
					next1.setText(programs.get(curIndex +1).getProgramName());
				}else if(curIndex < (size - 3)){
					next3.setText(programs.get(curIndex +3).getProgramName());
					next2.setText(programs.get(curIndex +2).getProgramName());
					next1.setText(programs.get(curIndex +1).getProgramName());
				}
				
				if(curIndex == 0){  //第一个节目
					pre1.setText("");
					pre2.setText("");
				}else if(curIndex == 1){ //channel 2
					pre2.setText("");
					pre1.setText(programs.get(curIndex -1).getProgramName());
				}else if(curIndex > 1){
					pre2.setText(programs.get(curIndex -2).getProgramName());
					pre1.setText(programs.get(curIndex -1).getProgramName());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Exception err: " + e);
			}
		}else{
			hide();
			Log.e(TAG, "find curProgram err");
		}
	}


	public void hide(){
		mView.setVisibility(View.GONE);
	}

	public void show(){
		if(null == mChannelManager){
			mChannelManager = DtvChannelManager.getInstance();
		}
		
		this.update(mChannelManager.getChannelList(), mChannelManager.getCurProgram());
	}

}
