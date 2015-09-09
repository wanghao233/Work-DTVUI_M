package com.changhong.pushoutView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class ColorAnimView extends View {

	private final Paint mPaint = new Paint();
	private Rect mViewRect = new Rect();
	private AnimData mAnimData = null;

	private int entryDeltaRadiusUp = 0;
	private int entryDeltaRadiusDown = 0;
	private int exitDeltaRadius = 0;

	private static int delta_top_first = 50;
	private final int delta_alpha = 20;

	private static int entry_draw_up_min_frame = 3;
	private static int entry_draw_up_max_frame = 5;

	private static int entry_draw_down_min_frame = 3;
	private static int entry_draw_down_max_frame = 5;

	private static int exit_draw_min_frame = 2;//5;
	private static int exit_draw_max_frame = 4;//8;

	private static int delta_topColor_height = 40;
	private final static int[] mDelayTime = { 0, 40, 80, 120, 160, 200, 240, 280, 320, 360 };
	private static int[] mDelays = { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 };
	private static int mDelaysIndex = 0;

	public final static int ENTRY_ANIMA = 1;
	public final static int EXIT_ANIMA = 2;
	public final static int NONE_ANIMA = 0;

	private int mIndex = 0;

	private int mAnimType = 0;
	private boolean mStartExitAnimaFlag = false;

	private OnColorEntryAnimDoneListener colorAnimaDoneListener = null;
	private OnColorEntryAnimDoneHalfListener colorAnimaDoneHalfListener = null;
	private OnColorExitAnimDoneListener colorExitAnimaDoneListener = null;
	private OnColorExitAnimStartListener colorExitAnimaStartListener = null;
	private OnColorEntryAnimaStartListener colorEntryAnimaStartListener = null;

	private Handler mHander = null;

	public interface OnColorEntryAnimaStartListener {

		public void OnColorEntryAnimaStart();
	}

	public interface OnColorEntryAnimDoneListener {

		public void OnColorEntryAnimDone();
	}

	public interface OnColorEntryAnimDoneHalfListener {

		public void OnColorEntryAnimDoneHalf();
	}

	public interface OnColorExitAnimDoneListener {

		public void OnColorExitAnimDone();
	}

	public interface OnColorExitAnimStartListener {

		public void OnColorExitAnimStart();
	}

	public ColorAnimView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mHander = new Handler();

		setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);

		//		mPaint.setAntiAlias(true);
	}

	public ColorAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mHander = new Handler();

		setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);

		//		mPaint.setAntiAlias(true);
	}

	public void setColorEntryAnimStartListener(OnColorEntryAnimaStartListener colorEntryAnimaStartListener) {

		this.colorEntryAnimaStartListener = colorEntryAnimaStartListener;
	}

	public void setColorEntryAnimDoneListener(OnColorEntryAnimDoneListener colorAnimaDoneListener) {

		this.colorAnimaDoneListener = colorAnimaDoneListener;
	}

	public void setColorExitAnimDoneListener(OnColorExitAnimDoneListener colorExitAnimaDoneListener) {

		this.colorExitAnimaDoneListener = colorExitAnimaDoneListener;
	}

	public void setColorExitAnimStartListener(OnColorExitAnimStartListener colorExitAnimaStartListener) {

		this.colorExitAnimaStartListener = colorExitAnimaStartListener;
	}

	public void setColorEntryAnimDoneHalfListener(OnColorEntryAnimDoneHalfListener colorAnimaDoneHalfListener) {

		this.colorAnimaDoneHalfListener = colorAnimaDoneHalfListener;
	}

	public void setColorViewIndex(int index) {

		this.mIndex = index;
	}

	private int getRandomDelayTime() {

		if (mDelaysIndex < 0)
			mDelaysIndex = 0;

		return mDelayTime[mDelays[mDelaysIndex++ % mDelays.length] % mDelayTime.length];
	}

	public void playExitAnim() {

		int location[] = new int[2];
		getLocationOnScreen(location);
		int right_onScreen = location[0];

		MagicButtonCommon.print(false, "playExitColorAnima---1--right_onScreen===" + right_onScreen);
		boolean isOndrawColor = false;
		//1920 change to wmParam.width
		if (right_onScreen <= 1920 && right_onScreen >= 0) {

			isOndrawColor = true;
		} else if (right_onScreen < 0 && right_onScreen + getWidth() > 0) {

			isOndrawColor = true;
		}

		if (isOndrawColor) {

			MagicButtonCommon.print(false, "playExitColorAnima---index---" + mIndex);
			mStartExitAnimaFlag = true;
			//			final int randomDelayTime = (int)(Math.random()*anima_startTime);
			final int randomDelayTime = getRandomDelayTime();

			mHander.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					MagicButtonCommon.print(false, "onLayout---color---randomDelayTime==" + randomDelayTime);
					setAnimType(ColorAnimView.EXIT_ANIMA);
					postInvalidate();
				}
			}, randomDelayTime);
		} else {

			if (colorExitAnimaDoneListener != null) {

				MagicButtonCommon.print(false, "--------onDraw---playExitAnima---2");
				setAnimType(NONE_ANIMA);
				colorExitAnimaDoneListener.OnColorExitAnimDone();
			}
		}

	}

	public void playEntryAnima() {

		resetAnimData();

		int location[] = new int[2];
		getLocationOnScreen(location);
		int right_onScreen = location[0];

		boolean isOndrawColor = false;

		if (right_onScreen <= 1920 && right_onScreen >= 0) {

			isOndrawColor = true;
		} else if (right_onScreen < 0 && right_onScreen + getWidth() > 0) {

			isOndrawColor = true;
		}

		if (isOndrawColor) {

			MagicButtonCommon.print(false, "onLayout-------right==" + right_onScreen);
			MagicButtonCommon.print(false, "onLayout-------index==" + mIndex);

			//			final int randomDelayTime = (int)(Math.random()*anima_startTime);
			final int randomDelayTime = getRandomDelayTime();

			mHander.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					MagicButtonCommon.print(false, "onLayout---color---postDelayed time==" + randomDelayTime);
					setAnimType(ENTRY_ANIMA);
					postInvalidate();

				}
			}, randomDelayTime);
		} else {

			if (colorAnimaDoneListener != null) {

				MagicButtonCommon.print(false, "--------onDraw---animan done---1");
				setAnimType(NONE_ANIMA);
				colorAnimaDoneListener.OnColorEntryAnimDone();

			}
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub

		MagicButtonCommon.print(false, "onLayout---color---left" + left);
		MagicButtonCommon.print(false, "onLayout---color---right" + right);
		mViewRect.left = left;
		mViewRect.right = right;
		mViewRect.top = top;
		mViewRect.bottom = bottom;

		super.onLayout(changed, left, top, right, bottom);
	}

	public void resetAnimData() {

		mAnimData = new AnimData(mViewRect);
	}

	public void setAnimType(int type) {

		mAnimType = type;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		switch (mAnimType) {
		case ENTRY_ANIMA:

			playEntryAnima(canvas);
			break;
		case EXIT_ANIMA:

			playExitAnima(canvas);
			break;
		default:
			break;
		}

	}

	private void playExitAnima(Canvas canvas) {

		if (colorExitAnimaStartListener != null && mStartExitAnimaFlag) {

			colorExitAnimaStartListener.OnColorExitAnimStart();
			mStartExitAnimaFlag = false;
			MagicButtonCommon.print(false, "--------onDraw---playExitAnima---0");
		}

		if (!mAnimData.startExitAnim()) {

			mPaint.setShader(mAnimData.getColorShader());
			canvas.drawCircle(mAnimData.startPoint.x, mAnimData.startPoint.y, mAnimData.radius, mPaint);

			mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			mPaint.setShader(mAnimData.getTopColorShader());

			canvas.drawCircle(mAnimData.startPoint.x, mAnimData.startPoint.y, mAnimData.radius - delta_topColor_height, mPaint);
			mPaint.setXfermode(null);

			invalidate();
			MagicButtonCommon.print(false, "--------onDraw---playExitAnima---1");
		} else {

			if (colorExitAnimaDoneListener != null) {

				MagicButtonCommon.print(false, "--------onDraw---playExitAnima---2");
				setAnimType(NONE_ANIMA);
				colorExitAnimaDoneListener.OnColorExitAnimDone();
			}

			MagicButtonCommon.print(false, "--------onDraw---playExitAnima");
		}
	}

	private void playEntryAnima(Canvas canvas) {

		if (!mAnimData.startEntryAnim()) {

			if (!mAnimData.getDirectionDraw() && colorAnimaDoneHalfListener != null) {

				colorAnimaDoneHalfListener.OnColorEntryAnimDoneHalf();
			}
			mPaint.setShader(mAnimData.getColorShader());
			canvas.drawCircle(mAnimData.startPoint.x, mAnimData.startPoint.y, mAnimData.radius, mPaint);

			mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			mPaint.setShader(mAnimData.getTopColorShader());
			canvas.drawCircle(mAnimData.startPoint.x, mAnimData.startPoint.y, mAnimData.radius - delta_topColor_height, mPaint);
			mPaint.setXfermode(null);

			invalidate();
		} else {

			if (colorAnimaDoneListener != null) {

				MagicButtonCommon.print(false, "--------onDraw---animan done---1");
				setAnimType(NONE_ANIMA);
				colorAnimaDoneListener.OnColorEntryAnimDone();

			}

			MagicButtonCommon.print(false, "--------onDraw---animan done");
		}
	}

	class AnimData {

		private int dstRadius = 0;
		public int radius = 20;
		private int alpha = 255;
		private int delta_radius = 0;

		private final int RED = 203;
		private final int GREEN = 36;
		private final int BLUE = 88;

		private final int RED_TOP = 255;
		private final int GREEN_TOP = 60;
		private final int BLUE_TOP = 146;

		private Shader colorShader = null;
		private Shader colorShaderTop = null;

		public Point startPoint = new Point();

		private Point recordStartPoint = new Point();

		private int[] color = new int[] { Color.RED, Color.RED };
		private int[] colorTop = new int[] { Color.RED, Color.RED };

		public Rect viewRect;

		private int delta_top = 0;
		private boolean direction_draw = true;
		private boolean first_radius_entry = true;
		private boolean first_radius_exit = true;
		private int record_init_radius = 0;

		private int alphaExit = 0;
		private final int alphaExitMin = 200;

		public AnimData(Rect viewRect) {

			MagicButtonCommon.print(false, "AnimData-------viewRect.bottom====" + viewRect.bottom);
			MagicButtonCommon.print(false, "AnimData-------viewRect.top====" + viewRect.top);
			MagicButtonCommon.print(false, "AnimData-------viewRect.right====" + viewRect.right);
			MagicButtonCommon.print(false, "AnimData-------viewRect.left====" + viewRect.left);

			int across_radius = (int) Math.sqrt((viewRect.bottom - viewRect.top) * (viewRect.bottom - viewRect.top) + (viewRect.right - viewRect.left) * (viewRect.right - viewRect.left) / 4);

			MagicButtonCommon.print(false, "AnimData-------across_radius===" + across_radius);
			int across_radius_delta = across_radius - (viewRect.bottom - viewRect.top);

			MagicButtonCommon.print(false, "AnimData-------across_radius_delta====" + across_radius_delta);

			if (across_radius_delta < delta_top_first) {

				delta_top = across_radius_delta;
			} else {

				delta_top = delta_top_first;
			}

			int radius_change = ((viewRect.right - viewRect.left) * (viewRect.right - viewRect.left) / (4 * delta_top) - (2 * (viewRect.bottom - viewRect.top) + delta_top)) / 2;

			MagicButtonCommon.print(false, "AnimData-------radius_change=====" + radius_change);
			this.startPoint.x = viewRect.left + viewRect.right / 2;
			this.startPoint.y = viewRect.bottom + radius_change;

			recordStartPoint.x = this.startPoint.x;
			recordStartPoint.y = this.startPoint.y;

			this.dstRadius = radius_change + (viewRect.bottom - viewRect.top);
			this.delta_radius = delta_top;

			MagicButtonCommon.print(false, "AnimData-------this.delta_radius=====" + this.delta_radius);
			MagicButtonCommon.print(false, "AnimData-------this.dstRadius=====" + this.dstRadius);
			this.viewRect = viewRect;

			radius = (int) Math.sqrt((viewRect.right - viewRect.left) * (viewRect.right - viewRect.left) / 4 + radius_change * radius_change);
			record_init_radius = radius;

			alphaExit = 255 - alphaExitMin;
			entryDeltaRadiusUp = (this.dstRadius - record_init_radius) / (entry_draw_up_min_frame + (int) ((entry_draw_up_max_frame - entry_draw_up_min_frame) * Math.random()));
			entryDeltaRadiusDown = delta_top / (entry_draw_down_min_frame + (int) ((entry_draw_down_max_frame - entry_draw_down_min_frame) * Math.random()));
			if (entryDeltaRadiusDown == 0) {

				entryDeltaRadiusDown = 1;
			}
			exitDeltaRadius = (this.dstRadius - record_init_radius) / (exit_draw_min_frame + (int) ((exit_draw_max_frame - exit_draw_min_frame) * Math.random()));
		}

		public boolean getDirectionDraw() {

			return direction_draw;
		}

		public boolean startExitAnim() {

			boolean animaIsDone = false;

			alpha = alphaExitMin + (int) (alphaExit * Math.random());
			alphaExit = alpha - alphaExitMin;

			if (first_radius_exit) {

				radius = dstRadius;
				first_radius_exit = false;
			} else {

				radius -= exitDeltaRadius;
			}

			MagicButtonCommon.print(false, "startExitAnim------dstRadius==" + dstRadius);
			MagicButtonCommon.print(false, "startExitAnim------radius==" + radius);
			MagicButtonCommon.print(false, "startExitAnim------record_init_radius==" + record_init_radius);

			if (radius > record_init_radius) {

				animaIsDone = false;
			} else {

				animaIsDone = true;
			}

			startPoint.x = recordStartPoint.x;
			startPoint.y = recordStartPoint.y;

			color[1] = color[0] = Color.argb(alpha, RED, GREEN, BLUE);
			colorShader = new SweepGradient(startPoint.x, startPoint.y, color, null);

			colorTop[1] = colorTop[0] = Color.argb(alpha, RED_TOP, GREEN_TOP, BLUE_TOP);
			colorShaderTop = new SweepGradient(startPoint.x, startPoint.y, colorTop, null);

			return animaIsDone;
		}

		public boolean startEntryAnim() {

			boolean stopDraw = true;

			if (radius > (dstRadius - delta_top) * 4 / 5) {

				if (alpha > 0)
					alpha -= delta_alpha;
				else
					alpha = 0;
			}

			if (radius < dstRadius && direction_draw) {

				stopDraw = false;
				if (first_radius_entry) {

					first_radius_entry = false;
				} else {

					radius += entryDeltaRadiusUp;
				}

				if (radius >= dstRadius) {

					radius = dstRadius;
				}

			} else {

				stopDraw = false;
				direction_draw = false;

				this.delta_radius -= entryDeltaRadiusDown;

				if (this.delta_radius <= 2) {

					alpha = 0;
					stopDraw = true;
				} else {

					int radius_change = ((this.viewRect.right - this.viewRect.left) * (this.viewRect.right - this.viewRect.left) / (4 * this.delta_radius) - (this.delta_radius + 2 * (viewRect.bottom
							- viewRect.top - delta_top))) / 2;

					MagicButtonCommon.print(false, "startAnim-----------radius_change=" + radius_change);
					this.startPoint.y = radius_change + this.viewRect.bottom;

					radius = (viewRect.bottom - viewRect.top - delta_top) + radius_change + this.delta_radius;
				}
			}

			color[1] = color[0] = Color.argb(alpha, RED, GREEN, BLUE);

			colorShader = new SweepGradient(startPoint.x, startPoint.y, color, null);

			colorTop[1] = colorTop[0] = Color.argb(alpha, RED_TOP, GREEN_TOP, BLUE_TOP);

			colorShaderTop = new SweepGradient(startPoint.x, startPoint.y, colorTop, null);

			return stopDraw;
		}

		public Shader getColorShader() {

			return colorShader;
		}

		public Shader getTopColorShader() {

			return colorShaderTop;
		}
	}
}
