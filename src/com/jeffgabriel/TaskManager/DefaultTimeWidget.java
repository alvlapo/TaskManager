package com.jeffgabriel.TaskManager;

import android.content.Context;
import android.preference.Preference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class DefaultTimeWidget extends Preference {

	public DefaultTimeWidget(Context context) {
		super(context);
	}

	public DefaultTimeWidget(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	// public DefaultTimeWidget(Context context, int theme,
	// OnTimeSetListener callBack, int hourOfDay, int minute,
	// boolean is24HourView) {
	// super(context, theme, callBack, hourOfDay, minute, false);
	// Time time = PreferenceService.getDefaultTaskTime(context);
	// this.updateTime(time.hour, time.minute);
	// }

	// @Override
	// protected View onCreateView(ViewGroup parent) {
	// // TODO Auto-generated method stub
	// return super.onCreateView(parent);
	// this.setLayoutResource(R.layout.custom_option);
	// TimePicker defaultTime = (TimePicker) (R.id.defaultTaskTimePicker);
	// Time time = PreferenceService.getDefaultTaskTime(getContext());
	// defaultTime.setCurrentHour(time.hour);
	// defaultTime.setCurrentMinute(time.minute);
	// defaultTime.setOnTimeChangedListener(defaultTimeListener);
	// }

	TimePicker.OnTimeChangedListener defaultTimeListener = new TimePicker.OnTimeChangedListener() {

		@Override
		public void onTimeChanged(android.widget.TimePicker view, int hour,
				int minute) {
			Time time = new Time();
			time.set(0, minute, hour, 1, 1, 1);
			PreferenceService.setDefaultTaskTime(getContext(),
					time.toMillis(false));
		}
	};
}
