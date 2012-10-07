package com.jeffgabriel.TaskManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Options extends PreferenceActivity {
	final Activity currentActivity = this;
	Preference defaultTimePref;
	boolean currentDisplayPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		defaultTimePref = findPreference("defaultNewTimePref");
		defaultTimePref.setLayoutResource(R.layout.custom_option);		
		currentDisplayPref = PreferenceService.getShowCompletedPreference(this);
	}
}