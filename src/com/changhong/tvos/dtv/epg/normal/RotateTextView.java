package com.changhong.tvos.dtv.epg.normal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class RotateTextView extends TextView {

	public RotateTextView(Context context) {
		super(context);
	}

	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 倾斜45°,上下左右居中

		canvas.rotate(45, (int) ((float) getMeasuredWidth() / 1.44), (int) ((float) getMeasuredHeight() / 1.44));

		canvas.translate(-(int) ((float) getMeasuredHeight() / 3.7), (int) ((float) getMeasuredHeight() / 3.5));

		super.onDraw(canvas);
	}
}