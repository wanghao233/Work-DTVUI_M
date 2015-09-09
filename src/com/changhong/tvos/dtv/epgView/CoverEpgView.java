package com.changhong.tvos.dtv.epgView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;

public class CoverEpgView extends RelativeLayout {

	private static final String TAG = "CoverEpgView";
	private Context mContext;
	private DtvEvent dtv_Event;
	private TextView tv_starttime_text;
	private TextView tv_endtime_text;
	private TextView tv_curTitle_text;
	private TextView tv_cur_time_text;
	private ProgressBar progressBar;
	private SimpleDateFormat dateFormat;
	//	private Drawable drawable ;
	private Date startDate;
	private Date endDate;
	private Calendar mCalendar = Calendar.getInstance();

	private int duration = 0;
	private boolean isStop;

	public CoverEpgView(Context context) {
		super(context);
		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.epg_old_cur_play_layout, null);
		tv_starttime_text = (TextView) view.findViewById(R.id.tv_start_time);
		tv_endtime_text = (TextView) view.findViewById(R.id.tv_end_time);
		tv_curTitle_text = (TextView) view.findViewById(R.id.tv_curTitle);
		tv_cur_time_text = (TextView) view.findViewById(R.id.tv_cur_time);
		progressBar = (ProgressBar) view.findViewById(R.id.epg_progressBar);
		progressBar.setFocusable(false);
		progressBar.setIndeterminate(false);
		this.addView(view);
	}

	public CoverEpgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.epg_old_cur_play_layout, null);
		tv_starttime_text = (TextView) view.findViewById(R.id.tv_start_time);
		tv_endtime_text = (TextView) view.findViewById(R.id.tv_end_time);
		tv_curTitle_text = (TextView) view.findViewById(R.id.tv_curTitle);
		tv_cur_time_text = (TextView) view.findViewById(R.id.tv_cur_time);
		progressBar = (ProgressBar) view.findViewById(R.id.epg_progressBar);
		progressBar.setFocusable(false);
		progressBar.setIndeterminate(false);
		this.addView(view);
	}

	public CoverEpgView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.epg_old_cur_play_layout, null);
		tv_starttime_text = (TextView) view.findViewById(R.id.tv_start_time);
		tv_endtime_text = (TextView) view.findViewById(R.id.tv_end_time);
		tv_curTitle_text = (TextView) view.findViewById(R.id.tv_curTitle);
		tv_cur_time_text = (TextView) view.findViewById(R.id.tv_cur_time);
		progressBar = (ProgressBar) view.findViewById(R.id.epg_progressBar);
		progressBar.setFocusable(false);
		progressBar.setIndeterminate(false);
		this.addView(view);
	}

	public CoverEpgView(Context context, DtvEvent dtv_event) {
		super(context);
		this.requestFocus();
		mContext = context;
		this.dtv_Event = dtv_event;
		View view = LayoutInflater.from(mContext).inflate(R.layout.epg_old_cur_play_layout, null);
		tv_starttime_text = (TextView) view.findViewById(R.id.tv_start_time);
		tv_endtime_text = (TextView) view.findViewById(R.id.tv_end_time);
		tv_curTitle_text = (TextView) view.findViewById(R.id.tv_curTitle);
		tv_cur_time_text = (TextView) view.findViewById(R.id.tv_cur_time);
		progressBar = (ProgressBar) view.findViewById(R.id.epg_progressBar);
		progressBar.setFocusable(false);
		progressBar.setIndeterminate(false);
		this.addView(view);
		//		init(dtv_event);

	}

	public void init(DtvEvent dtv_event) {
		this.dtv_Event = dtv_event;

		String preString = mContext.getResources().getString(R.string.menu_epg_pre_title);
		tv_curTitle_text.setText(preString);

		dateFormat = new SimpleDateFormat("HH:mm");
		tv_starttime_text.setText(dateFormat.format(dtv_event.getStartTime()));
		tv_endtime_text.setText(dateFormat.format(dtv_event.getEndTime()));
		tv_cur_time_text.setText(dateFormat.format(mCalendar.getTime()));
		tv_cur_time_text.setVisibility(View.GONE);

		startDate = dtv_event.getStartTime();
		endDate = dtv_event.getEndTime();

		isStop = false;
		//		drawable = mContext.getResources().getDrawable(R.drawable.epg_progress_cur);
		//		drawable = mContext.getResources().getDrawable(Color.RED);
		//		startProgress();
	}

	public int getDuration(Date start, Date end) {
		long start_time;
		long end_time;
		start_time = getIntTime(start);
		end_time = getIntTime(end);

		return (int) ((end_time - start_time) / (1000 * 60));
	}

	public long getIntTime(Date date) {

		return date.getTime();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 12 && !isStop) {
				handler.postDelayed(getCurTimeRunnable, 1000 * 60);
			}
		}

	};

	private Runnable getCurTimeRunnable = new Runnable() {

		@Override
		public void run() {

			Date curDate = mCalendar.getTime();
			int curOff = getDuration(startDate, curDate);

			int pro = (int) (curOff * 100 / duration);
			tv_cur_time_text.setText(dateFormat.format(curDate));
			progressBar.setProgress(pro);
			Log.v(TAG, "****fresh progress and cur is " + curOff);
			handler.sendEmptyMessage(12);

		}

	};

	public void startProgress() {

		Date curDate = mCalendar.getTime();
		int cur_end_offset = getDuration(curDate, endDate);
		int cur_start_offset = getDuration(startDate, curDate);
		duration = getDuration(startDate, endDate);

		Log.v(TAG, "@@@ the cur_end_offset is " + cur_end_offset + " @@@and cur_start_offset is " + cur_start_offset + " @@@and duration  is " + duration);

		handler.removeCallbacks(getCurTimeRunnable);

		if (cur_end_offset >= 0 && cur_start_offset >= 0 && duration > 0) {
			isStop = false;
			progressBar.setMax(100);
			int pro = (int) (cur_start_offset * 100 / duration);
			progressBar.setProgress(pro);
			handler.sendEmptyMessage(12);

		} else {
			isStop = true;
			handler.removeCallbacks(getCurTimeRunnable);
			progressBar.setProgress(0);
		}
	}

	public void onDestroy() {
		isStop = true;
		handler.removeCallbacks(getCurTimeRunnable);
		Log.v(TAG, "**try to stop the time task second**");
	}
}
