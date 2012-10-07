package com.jeffgabriel.TaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.concurrent.ConcurrentHashMap;

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
	private ConcurrentHashMap<CheckBox, Task> taskViews = new ConcurrentHashMap<CheckBox, Task>();
	private Cursor _cursor;
	LayoutInflater _layoutInflator;

	private static synchronized ITaskProvider getProvider(Context context) {
		if (_taskProvider == null)
			_taskProvider = new TaskProvider(new DatabaseHelper(context),
					context);
		return _taskProvider;
	}

	public TaskCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, R.layout.task_view, c, autoRequery);
		_context = context;
		_cursor = c;
		_layoutInflator = LayoutInflater.from(context);
		getProvider(context);
	}

	@Override
	public View newView(Context context, Cursor cursor,
			android.view.ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.task_view, null);
	};

	@Override
	public View getView(int position, View convertView,
			android.view.ViewGroup parent) {
		_cursor.moveToPosition(position);
		final Task currentTask = new Task(
				_cursor.getInt(DatabaseHelper.idColumn),
				_cursor.getString(DatabaseHelper.NameColumn), new Date(
						_cursor.getLong(DatabaseHelper.DueDateColumn)),
				_cursor.getInt(DatabaseHelper.CompleteColumn) == 0 ? false
						: true);
		if (convertView == null) {
			convertView = _layoutInflator.inflate(R.layout.task_view, null);
		}

		final CheckBox taskCheck = (CheckBox) convertView
				.findViewById(R.id.taskIsComplete);

		if (taskViews.containsKey(taskCheck))
			taskViews.replace(taskCheck, currentTask);
		else {
			taskViews.put(taskCheck, currentTask);
			taskCheck.setOnCheckedChangeListener(checkListener);
		}

		final TextView dateView = (TextView) convertView
				.findViewById(R.id.taskDueDate);

		setTaskData(currentTask, taskCheck, dateView);

		Button button = (Button) convertView.findViewById(R.id.btnDeleteTask);
		if (button != null)
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean hideMessage = PreferenceService
							.getHideDeleteWarningPreference(v.getContext());
					if (hideMessage) {
						deleteTask(v.getContext(), currentTask);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(v
								.getContext());
						builder.setMessage(R.string.deleteWarning)
								.setPositiveButton("Yes", dialogClickListener)
								.setNegativeButton("No", dialogClickListener);
						LayoutInflater adbInflater = LayoutInflater.from(v
								.getContext());
						View deleteLayout = adbInflater.inflate(
								R.layout.checkbox, null);
						dontShowAgain = (CheckBox) deleteLayout
								.findViewById(R.id.skip);
						builder.setView(deleteLayout);
						builder.show();
					}
				}
			});

		return convertView;
	};

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

	}

	OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Task currentTask = taskViews.get(buttonView);
			currentTask.set_isComplete(isChecked);
			updateTask(buttonView.getContext(), currentTask);
			if (currentTask.get_isComplete())
				buttonView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			else
				buttonView.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
		}
	};

	public void setTaskData(Task task, CheckBox taskCheck, TextView dateView) {
		if (task != null && taskCheck != null && dateView != null) {
			taskCheck.setChecked(task.get_isComplete());
			taskCheck.setText(task.get_name());
			dateView.setText(getFormattedDate(task.get_dueDate()));
			if (task.get_isComplete())
				taskCheck.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			else
				taskCheck.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
		}
	}

	private final String simpleDateFormat = "MM/dd/yyyy";

	private String getFormattedDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
		return format.format(date);
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
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
		_cursor.requery();
		notifyDataSetChanged();
	}

	private void updateTask(Context context, Task task) {
		getProvider(context).update(task);
	}

	class TaskState {
		Task Task;
		Context Context;
	}
}
