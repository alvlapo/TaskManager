package com.jeffgabriel.RoundTuit;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
		fixTimePickerAMPM(picker);
		layout.addView(picker);

		return layout;
	}
	
	private void fixTimePickerAMPM(final TimePicker picker){
	    View amPmView  = ((ViewGroup)picker.getChildAt(0)).getChildAt(2);
	    if(amPmView instanceof Button)
	    {
	        amPmView.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                Log.d("OnClickListener", "OnClickListener called");
	                if(v instanceof Button)
	                {
	                    if(((Button) v).getText().equals("AM"))
	                    {
	                        ((Button) v).setText("PM");
	                         if (picker.getCurrentHour() < 12) {
	                             picker.setCurrentHour(picker.getCurrentHour() + 12);
	                            }  

	                    }
	                    else{
	                        ((Button) v).setText("AM");
	                         if (picker.getCurrentHour() >= 12) {
	                             picker.setCurrentHour(picker.getCurrentHour() - 12);
	                            }
	                    }
	                }

	            }
	        });
	    }
	    //else if(amPmView instanceof	NumberPicker){
	    	
	    //}
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