package com.changhong.pushoutView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Interface
 * 1. Constructor
 * 2. start: void start(View toView)/void start(FocusBoxParam toParam)
 * 3. show: void show(boolean show)
 * 4. set listener: entry animation and exit animation listener
 * 5. set listener: focus change
 */
public class FocusBoxTranslateAnimView extends FocusBoxView {

	private final static int FOCUS_BOX_TRANSLATE_ANIM_DURATION = 200;
	private final static int FOCUS_BOX_ENTRY_ANIM_DURATION = 250;
	private final static int FOCUS_BOX_EXIT_ANIM_DURATION = 250;
	private final static int FOCUS_BOX_REBOUND_ANIM_DURATION = 300;
	private final static int KICKBACK_DISTANCE = 40;

	private ValueAnimator mValueAnimator;

	public FocusBoxTranslateAnimView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public FocusBoxTranslateAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	protected void playEntryAnim() {
		// TODO Auto-generated method stub
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

		AnimationSet entrySet = new AnimationSet(true);
		entrySet.addAnimation(alphaAnimation);
		entrySet.addAnimation(scaleAnimation);

		entrySet.setInterpolator(new DecelerateInterpolator());
		entrySet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				MagicButtonCommon.print(false, "entrySet---onAnimationEnd");
				if (mOnEntryAnimDoneListener != null)
					mOnEntryAnimDoneListener.onEntryAnimDone();
			}
		});

		entrySet.setDuration(FOCUS_BOX_ENTRY_ANIM_DURATION);

		getFocusBoxView().startAnimation(entrySet);
	}

	@Override
	public void playExitAnim() {
		// TODO Auto-generated method stub
		Animation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

		AnimationSet exitSet = new AnimationSet(true);
		exitSet.addAnimation(alphaAnimation);
		exitSet.addAnimation(scaleAnimation);

		exitSet.setInterpolator(new AccelerateInterpolator());
		exitSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				MagicButtonCommon.print(true, "exitSet---onAnimationEnd");
				if (mOnExitAnimDoneListener != null)
					mOnExitAnimDoneListener.onExitAnimDone();
			}
		});

		exitSet.setDuration(FOCUS_BOX_EXIT_ANIM_DURATION);

		getFocusBoxView().startAnimation(exitSet);
	}

	@Override
	protected void playFocusChangeAnim() {
		// TODO Auto-generated method stub
		mValueAnimator.start();
	}

	@Override
	public void playReboundAnim(boolean isForward) {
		// TODO Auto-generated method stub
		int deltafromX = 0;
		if (isForward) {

			deltafromX = KICKBACK_DISTANCE;
		} else {

			deltafromX = -KICKBACK_DISTANCE;
		}

		getFocusBoxView().clearAnimation();
		Animation translateAnimation = new TranslateAnimation(0, deltafromX, 0, 0);
		translateAnimation.setDuration(FOCUS_BOX_REBOUND_ANIM_DURATION);
		getFocusBoxView().startAnimation(translateAnimation);
	}

	private void init() {

		mValueAnimator = new ValueAnimator();
		mValueAnimator.setFloatValues(0.04f, 1);
		mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

				float value = (Float) animation.getAnimatedValue();

				int leftMargin = (int) (getFocusBoxFromParam().mX - mPaddingX + (getFocusBoxToParam().mX - getFocusBoxFromParam().mX) * value);
				int topMargin = (int) (getFocusBoxFromParam().mY - mPaddingY + (getFocusBoxToParam().mY - getFocusBoxFromParam().mY) * value);
				int width = (int) (getFocusBoxFromParam().mW + 2 * mPaddingX + (getFocusBoxToParam().mW - getFocusBoxFromParam().mW) * value);
				int height = (int) (getFocusBoxFromParam().mH + 2 * mPaddingY + (getFocusBoxToParam().mH - getFocusBoxFromParam().mH) * value);

				setFocusBoxLayoutParam(leftMargin, topMargin, width, height);

				getFocusBoxView().bringToFront();
			}
		});

		mValueAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				MagicButtonCommon.print(false, "onAnimationEnd");
				if (mOnFocusChangeAnimDoneListener != null)
					mOnFocusChangeAnimDoneListener.onFocusChangeAnimDone();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
			}
		});

		mValueAnimator.setDuration(FOCUS_BOX_TRANSLATE_ANIM_DURATION);
	}
}