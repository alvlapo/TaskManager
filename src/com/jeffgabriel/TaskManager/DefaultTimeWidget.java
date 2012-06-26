package com.jeffgabriel.TaskManager;

import android.app.Activity;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DefaultTimeWidget extends android.widget.LinearLayout {

	static Context _context;

	public DefaultTimeWidget(Context context) {
		super(context);
		_context = context;
	}

	public DefaultTimeWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.time_option, this);
		TimePicker defaultTime = (TimePicker) findViewById(R.id.defaultTaskTimePicker);
		Time time = PreferenceService.getDefaultTaskTime(_context);
		defaultTime.setCurrentHour(time.hour);
		defaultTime.setCurrentMinute(time.minute);
		defaultTime.setOnTimeChangedListener(defaltTimeListener);
	}

	private TimePicker.OnTimeChangedListener defaltTimeListener = new OnTimeChangedListener() {

		public void onTimeChanged(TimePicker view, int hour, int minute) {
			Time time = new Time();
			time.set(0, minute, hour, 1, 1, 1);
			PreferenceService.setDefaultTaskTime(_context, time.toMillis(false));
		}
	};
}
