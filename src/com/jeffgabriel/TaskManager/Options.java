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
		currentDisplayPref = PreferenceService.getShowCompletedPreference(this);
	}
}