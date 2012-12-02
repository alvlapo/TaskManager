package com.jeffgabriel.RoundTuit;

import java.io.Serializable;

import android.content.Intent;
import android.net.Uri;

public class TaskIntent {

	static final String CREATE_TASK_ACTION = "com.jeffgabriel.RoundTuit.CreateTask";
	static final String DELETE_TASK_ACTION = "com.jeffgabriel.RoundTuit.DeleteTask";
	static final String UPDATE_TASK_ACTION = "com.jeffgabriel.RoundTuit.UpdateTask";
	static final String DISPLAY_TASK_ACTION = "com.jeffgabriel.RoundTuit.ShowTask";
	public static final Uri CONTENT_URI = Uri.parse("content://" + TaskProvider.AUTHORITY + "/Tasks");
	static final String TASK_DATA_KEY = "TaskData";

	Task _task;
	TaskProvider _provider;
	Intent _intent;

	public TaskIntent(Intent intent) {
		_intent = intent;
		Serializable payload = intent.getSerializableExtra(TASK_DATA_KEY);
		if (payload != null)
			setTask((Task) payload);
	}

	Task getTask() {
		if (_task == null && _intent.getData() != null) {
			int taskId = Task.getIdFromUri(_intent.getData());
			if (_provider != null)
				setTask(_provider.get(taskId));
		}
		return _task;
	}

	void setTask(Task task) {
		_task = task;
		_intent.setData(task.get_Uri());
		_intent.putExtra(TASK_DATA_KEY, task);
	}

}
