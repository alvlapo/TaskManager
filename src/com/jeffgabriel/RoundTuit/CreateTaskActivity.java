package com.jeffgabriel.RoundTuit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeffgabriel.RoundTuit.Interfaces.IDbHelper;

public class CreateTaskActivity extends Activity implements
		View.OnClickListener {

	public CreateTaskActivity() {
	}

	NewTaskWidget form;
	final String EDIT_INSTANCE = "TaskInProgress";
	static Task _savedState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		Intent startupIntent = getIntent();
		form = (NewTaskWidget) findViewById(R.id.newTaskForm);

		Button button = (Button) findViewById(R.id.saveTaskButton);
		if (button != null) {
			button.setOnClickListener(this);
		}

		Button saveContinueBtn = (Button) findViewById(R.id.saveContinueTaskButton);
		if (saveContinueBtn != null) {
			saveContinueBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					performTaskAdd();
				}
			});
		}

		if (startupIntent != null
				&& startupIntent.getSerializableExtra("task") != null)
			_savedState = (Task) startupIntent.getSerializableExtra("task");
		if (_savedState == null && savedInstanceState != null)
			_savedState = (Task) savedInstanceState
					.getSerializable(EDIT_INSTANCE);
		if (_savedState != null) {
			form.updateFromExisting(_savedState);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (_savedState != null) {
			form.updateFromExisting(_savedState);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		_savedState = new Task(-1, form.getTaskName(), form.getDateAndTime(),
				false);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(EDIT_INSTANCE, _savedState);
	}

	public void onClick(View v) {
		if (performTaskAdd())
			this.finish();
	}

	private boolean performTaskAdd() {
		IDbHelper helper = new DatabaseHelper(this);
		Task task = _savedState;
		if (task == null || task.get_id() <= 0) {
			task = new Task();
			task.set_id(-1);
		}
		task.set_name(form.getTaskName());
		task.set_dueDate(form.getDateAndTime());
		task.set_isComplete(false);
		try {
			helper.add(task);
		} catch (IllegalArgumentException eTaskFail) {
			Toast toast = Toast.makeText(this, eTaskFail.getLocalizedMessage(),
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			return false;
		}
		Intent createdIntent = new Intent(TaskIntent.CREATE_TASK_ACTION,
				task.get_Uri());
		createdIntent.putExtra(TaskIntent.TASK_DATA_KEY, task);
		setResult(RESULT_OK, createdIntent);
		sendBroadcast(createdIntent);
		AlarmService alarmSender = new AlarmService(this);
		alarmSender.startAlarm(task);
		form.clearForm();
		_savedState = null;
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent optionIntent = null;
		if (item.getItemId() == R.id.settings) {
			optionIntent = new Intent(this, Options.class);
		} 
		if (optionIntent != null) {
			startActivity(optionIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
