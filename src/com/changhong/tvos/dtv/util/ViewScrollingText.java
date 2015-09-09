package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class ViewScrollingText extends TextView {

	public ViewScrollingText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ViewScrollingText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewScrollingText(Context context) {
		super(context);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused){
			
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean focused) {
		if (focused){
			
			super.onWindowFocusChanged(focused);
		}
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}