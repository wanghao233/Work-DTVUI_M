package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewDtvProgram extends RelativeLayout {

	protected static final String TAG = "ViewDtvProgram";
	RelativeLayout myRelativeLayout;
	RelativeLayout drawContainer;
	TextView channelNum;
	TextView channelName;
	ImageView channelLog;
	private Context mContext;
    private boolean isAbleImage = true;
	public ViewDtvProgram(Context context) {
		super(context);
		initView(context);

	}

	public ViewDtvProgram(Context context, AttributeSet attr) {
		super(context, attr);
		initView(context);
	}

	public ViewDtvProgram(Context context, DtvProgram program) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
		init(program);
	}

	private void initView(Context context) {
		mContext = context;
		myRelativeLayout = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.view_program_sort_cell, null);
		this.setLayoutParams(new LayoutParams(myRelativeLayout.getWidth(),
				LayoutParams.WRAP_CONTENT));
		this.addView(myRelativeLayout);
		drawContainer = (RelativeLayout) myRelativeLayout
				.findViewById(R.id.relativeLayout2);
		channelNum = (TextView) myRelativeLayout.findViewById(R.id.textView1);
		channelName = (TextView) myRelativeLayout.findViewById(R.id.textView2);
		channelLog = (ImageView) myRelativeLayout.findViewById(R.id.imageView1);

		this.setFocusable(false);
	}

	public void init(DtvProgram program) {
		// TODO Auto-generated method stub
		this.clearAnimation();
		if (null != program) {

			channelName.setText("" + program.mProgramName);
			Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(
					("/data/dtv/logo/" + program.mDtvLogo),
					new ImageCallback() {

						@Override
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							if (null == imageDrawable) {
								Log.i(TAG, "EL--> the image is null");
							} else {
								if (null != channelLog && isAbleImage) {

									channelLog.setImageDrawable(imageDrawable);
								}
							}
						}

					});
			if (null != drawable) {
				channelLog.setImageDrawable(drawable);
			}
		} else {
			channelLog.setImageDrawable(null);
			channelName.setText("");
		}
	}

	public void setSortNum(int num) {
		channelNum.setText("" + num);
	}

	public ViewDtvProgram(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setDisappareAnim(Animation anim) {
		this.setAnimation(anim);
	}

	public void dismiss() {
		this.startLayoutAnimation();

	}

	public void reset() {
		Log.i(TAG, "reset null");
		this.clearAnimation();
		channelLog.setImageDrawable(null);
		channelName.setText("");
		// channelNum.setText("" );
	}

	public void setLocation(int[] location) {
		this.setX(Float.valueOf(location[0]));
		this.setY(Float.valueOf(location[1]));
	}

	public void setBackGround(Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			drawContainer.setBackground(drawable);
		} else {
			drawContainer.setBackgroundDrawable(drawable);

		}
	}
	
	public void setImageAble(boolean enable){
		isAbleImage = enable;
	}
}
