package com.jeffgabriel.TaskManager.Interfaces;

import java.io.IOException;
import java.util.ArrayList;

import com.jeffgabriel.TaskManager.Task;
import com.jeffgabriel.TaskManager.TaskQuery;

import android.database.Cursor;
import android.database.SQLException;


public interface IDbHelper {
	 void createDataBase() throws IOException;
	 void openDataBase() throws SQLException;
	 void close();
	 ArrayList<Task> getTasks(TaskQuery query);
	 Cursor getTaskCursor(TaskQuery query);
	 boolean delete(int taskId);
	 Task add(Task task) throws SQLException;
	 Task update(Task task) throws SQLException;
}
