package com.jeffgabriel.TaskManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeffgabriel.TaskManager.Interfaces.IDbHelper;

public class CreateTaskActivity extends Activity implements
		View.OnClickListener {

	public CreateTaskActivity() {
	}

	NewTaskWidget form;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);

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
	}

	public void onClick(View v) {
		if (performTaskAdd())
			this.finish();
	}

	private boolean performTaskAdd() {
		IDbHelper helper = new DatabaseHelper(this);
		Task task = new Task(-1, form.getTaskName(), form.getDateAndTime(),
				false);
		try {
			helper.add(task);
		} catch (IllegalArgumentException eTaskFail) {
			Toast toast = Toast.makeText(this, eTaskFail.getLocalizedMessage(),
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
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
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}
}
