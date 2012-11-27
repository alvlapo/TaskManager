package com.jeffgabriel.TaskManager;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class DefaultTimeWidget extends Preference implements
		TimePicker.OnTimeChangedListener {

	public DefaultTimeWidget(Context context) {
		super(context);
	}

	public DefaultTimeWidget(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		super.onCreateView(parent);
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		TextView view = new TextView(getContext());
		view.setText(R.string.defaultTaskTimeLabel);
		view.setLayoutParams(params1);

		layout.addView(view);

		Time time = PreferenceService.getDefaultTaskTime(this.getContext());

		TimePicker picker = new TimePicker(getContext());
		picker.setLayoutParams(params2);
		picker.setOnTimeChangedListener(this);
		picker.setCurrentHour(time.hour);
		picker.setCurrentMinute(time.minute);
		
		layout.addView(picker);

		return layout;
	}

	@Override
	public void onTimeChanged(android.widget.TimePicker view, int hour,
			int minute) {
		Time time = new Time();
		time.set(0, minute, hour, 1, 1, 1970);
		PreferenceService
				.setDefaultTaskTime(getContext(), time.toMillis(false));
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return PreferenceService.getDefaultTaskTime(getContext());
	}
}