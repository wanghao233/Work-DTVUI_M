package com.changhong.tvos.dtv.menuManager;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import com.changhong.tvos.dtv.R;

public class AnimationHelper {
	protected static final int LAYOUT_MOVE_LEFT = 0;
	public interface FreshListener{
		public void freshView();
	}
	private FreshListener freshListener;
	private Context mContext;
	private RelativeLayout layout;
	public AnimationHelper(final Context mContext, final RelativeLayout layout) {
		this.mContext = mContext;
		this.layout = layout;
	}
	
	

	public Animation  animationLeft() {
		Animation outLeft = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_out);
		final Animation inRight = AnimationUtils.loadAnimation(mContext, R.anim.anim_right_in);
		outLeft.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
//				handler.sendEmptyMessageAtTime(LAYOUT_MOVE_LEFT, 450);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				freshListener.freshView();
				layout.startAnimation(inRight);
			}
		});
		
		return outLeft;
	}

	public Animation animationRight() {
		Animation outRight = AnimationUtils.loadAnimation(mContext, R.anim.anim_right_out);
		final Animation inLeft = AnimationUtils.loadAnimation(mContext, R.anim.anim_left_in);
		outRight.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				freshListener.freshView();
				layout.startAnimation(inLeft);
			}
		});
		
		return outRight;
	}

	public Animation animationUp() {
		Animation outUp = AnimationUtils.loadAnimation(mContext, R.anim.anim_up_out);
		final Animation inDown = AnimationUtils.loadAnimation(mContext, R.anim.anim_down_in);
		outUp.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				freshListener.freshView();
				layout.startAnimation(inDown);
			}
		});
		
		return outUp;

	}

	public Animation animationDown() {
		Animation outDown = AnimationUtils.loadAnimation(mContext, R.anim.anim_down_out);
		final Animation inUp = AnimationUtils.loadAnimation(mContext, R.anim.anim_up_in);
		outDown.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				freshListener.freshView();
				layout.startAnimation(inUp);
			}
		});
		
		return outDown;
	}



	public void setFreshListener(FreshListener freshListener) {
		this.freshListener = freshListener;
	}



	public FreshListener getFreshListener() {
		return freshListener;
	}
}
