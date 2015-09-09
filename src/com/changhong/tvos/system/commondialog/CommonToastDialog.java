package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;

public class CommonToastDialog extends CommonInfoDialog{
	private int dialog_height = 70;
	private int dialog_width = 520;
	private int dialog_margin = 480;
	private int dialog_margin_y = 30;
	
	public CommonToastDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();	
		dialog_width = (int) (dialog_width * mDisplayMetrics.scaledDensity);
		dialog_height = (int) (dialog_height * mDisplayMetrics.scaledDensity);
		dialog_margin = (int) (dialog_margin * mDisplayMetrics.scaledDensity);
		this.setGravity(Gravity.BOTTOM|Gravity.LEFT, dialog_margin, dialog_margin_y);
		this.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
		this.info_layout.setLayoutParams(new FrameLayout.LayoutParams(dialog_width,dialog_height));
		this.tv.setTextColor(Color.WHITE);
		this.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
	}
}