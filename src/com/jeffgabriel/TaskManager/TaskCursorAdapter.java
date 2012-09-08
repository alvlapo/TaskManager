package com.jeffgabriel.TaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.jeffgabriel.TaskManager.Interfaces.ITaskProvider;

public class TaskCursorAdapter extends ResourceCursorAdapter {
	private CheckBox dontShowAgain;
	private Context _context;
	private static ITaskProvider _taskProvider = null;

	private static synchronized ITaskProvider getProvider(Context context) {
		if (_taskProvider == null)
			_taskProvider = new TaskProvider(new DatabaseHelper(context),
					context);
		return _taskProvider;
	}

	public TaskCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, R.layout.task_view, c, autoRequery);
		_context = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Task currentTask = new Task(cursor.getInt(DatabaseHelper.idColumn),
				cursor.getString(DatabaseHelper.NameColumn), new Date(
						cursor.getLong(DatabaseHelper.DueDateColumn)),
				cursor.getInt(DatabaseHelper.CompleteColumn) == 0 ? false
						: true);
		final TaskState state = new TaskState();
		state.Task = currentTask;
		state.Context = context;
		final CheckBox taskCheck = (CheckBox) view
				.findViewById(R.id.taskIsComplete);
		taskCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				state.Task.set_isComplete(isChecked);
				getProvider(state.Context).update(state.Task);
				if (state.Task.get_isComplete())
					taskCheck.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				else
					taskCheck.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
			}
		});
		final TextView dateView = (TextView) view
				.findViewById(R.id.taskDueDate);
		setTaskData(currentTask, taskCheck, dateView);
		Button button = (Button) view.findViewById(R.id.btnDeleteTask);
		if (button != null)
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean hideMessage = PreferenceService
							.getHideDeleteWarningPreference(state.Context);
					if (hideMessage)
						deleteTask(state.Context, state.Task);
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								state.Context);
						builder.setMessage(R.string.deleteWarning)
								.setPositiveButton("Yes", dialogClickListener)
								.setNegativeButton("No", dialogClickListener);
						LayoutInflater adbInflater = LayoutInflater
								.from(state.Context);
						View deleteLayout = adbInflater.inflate(
								R.layout.checkbox, null);
						dontShowAgain = (CheckBox) deleteLayout
								.findViewById(R.id.skip);
						builder.setView(deleteLayout);
						builder.show();
					}
				}
			});

	}

	public void setTaskData(Task task, CheckBox taskCheck, TextView dateView) {
		if (task != null && taskCheck != null && dateView != null) {
			taskCheck.setChecked(task.get_isComplete());
			taskCheck.setText(task.get_name());
			dateView.setText(getFormattedDate(task.get_dueDate()));
		}
	}

	private final String simpleDateFormat = "MM/dd/yyyy";

	private String getFormattedDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
		return format.format(date);
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			PreferenceService.setHideDeleteWarningPreference(_context,
					dontShowAgain.isChecked());

			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				deleteTask(_context, null);
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}

		}
	};

	private void deleteTask(Context context, Task task) {
		getProvider(context).delete(task);
		this.notifyDataSetChanged();
	}

	class TaskState {
		Task Task;
		Context Context;
	}
}
