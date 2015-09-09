package com.changhong.tvos.dtv.epg.minify;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class EpgListView extends ListView {

	private int mItemHight = 90;
	private int lastSelectBefore = 0;
	private int top = 0;

	public EpgListView(Context context) {
		super(context);
	}

	public EpgListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EpgListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

		if (!gainFocus) {
			// int lastSelectItem = getSelectedItemPosition();
			int lastSelectItem = getSelectedItemPosition();
			Log.i("lastSelectItem", "lastSelectItem = " + lastSelectItem);

			int lastFirstVisibleItem = getFirstVisiblePosition();
			Log.i("lastFirstVisibleItem", lastFirstVisibleItem + "");

			if ((lastSelectBefore == 0 && lastSelectItem == 0) || (lastSelectBefore != lastSelectItem)) {
				View v0 = getChildAt(0);
				int top0 = (v0 == null) ? 0 : v0.getTop();
				View v1 = getChildAt(lastFirstVisibleItem);
				int top1 = (v1 == null) ? 0 : v1.getTop();

				View v2 = getChildAt(lastSelectItem);
				int top2 = (v2 == null) ? 0 : v2.getTop();

				if (top2 == 0 && top1 != 0) {
					if (lastFirstVisibleItem != 0) {
						top2 = top1 / lastFirstVisibleItem * lastSelectItem;
						mItemHight = top1 / lastFirstVisibleItem;
					}
				}

				if (top2 != 0 && top1 == 0) {
					if (lastSelectItem != 0) {
						mItemHight = top2 / lastSelectItem;
					}
				}

				if (top2 == 0 && top1 == 0) {
					if (lastSelectItem != 0) {
						top2 = mItemHight * lastSelectItem;
					}
					if (lastFirstVisibleItem != 0) {
						top1 = mItemHight * lastFirstVisibleItem;
					}
				}

				top = top2 - top1 + top0;

				// Log.i("top", top + "");
				// Log.i("top0", top0 + "");
				// Log.i("top1", top1 + "");
				// Log.i("top2", top2 + "");
			}
			lastSelectBefore = lastSelectItem;
		}
		if (lastSelectBefore == 0) {
			lastSelectBefore = getSelectedItemPosition();
		}

		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

		if (gainFocus) {
			// setSelection(lastSelectItem);
			setSelectionFromTop(lastSelectBefore, top);
		}
	}
}
