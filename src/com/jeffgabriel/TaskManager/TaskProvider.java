package com.jeffgabriel.TaskManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.jeffgabriel.TaskManager.Interfaces.ITaskProvider;

public class TaskProvider implements ITaskProvider {

	public static final String ID = "_id";
	public static final String NAME = "Name";
	public static final String DUE_DATE = "DueDate";
	public static final String CATEGORY_ID = "CategoryId";
	public static final String CREATE_DATE = "CreateDate";
	public static final String IS_COMPLETE = "IsComplete";
	
	com.jeffgabriel.TaskManager.Interfaces.IDbHelper _dbHelper;
	private final Context _context;
	private TaskQuery _standardQuery;
	public TaskProvider(
			com.jeffgabriel.TaskManager.Interfaces.IDbHelper helper,
			Context context) {
		_context = context;
		_dbHelper = helper;
		_standardQuery = new TaskQuery(PreferenceService.getShowCompletedPreference(_context));
		try {
			_dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete(Task task) {
		_dbHelper.delete(task.get_id());
	}

	public List<Task> getAll() {
		return getSome(PreferenceService.getDefaultListPageSize(_context), 0);
	}

	String categoryWhere = " CategoryId = ? ";

	public List<Task> getSome(int pageSize, int pageIndex) {
		List<Task> fullResults = _dbHelper.getTasks(_standardQuery);
		if (pageSize > 0 && fullResults.isEmpty() == false) {
			int resultEndIndex = pageSize * (pageIndex + 1);
			resultEndIndex = resultEndIndex > fullResults.size() ? fullResults
					.size() : resultEndIndex;
			int startIndex = resultEndIndex > pageSize ? resultEndIndex
					- pageSize : 0;
			return fullResults.subList(startIndex, resultEndIndex);
		}
		return fullResults;
	}
	
	public Cursor getTaskCursor(){
		return _dbHelper.getTaskCursor(_standardQuery);
	}
	

	public void update(Task task) {
		if (task.get_dueDate() == null)
			throw new IllegalStateException(_context.getResources().getString(
					R.string.noDueDateError));
		_dbHelper.update(task);
	}

	String whereClause = "_id = ?";

	public Task get(int taskId) {
		TaskQuery byId = new TaskQuery(false);
		byId.set_WhereParameters(new String[] { Integer.toString(taskId) });
		byId.set_whereStatement(whereClause);
		ArrayList<Task> tasks = _dbHelper.getTasks(byId);
		if (tasks.isEmpty() == false)
			return tasks.get(0);
		return null;
	}
}
