package com.jeffgabriel.RoundTuit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.jeffgabriel.RoundTuit.Interfaces.ITaskProvider;

public class TaskProvider extends ContentProvider implements ITaskProvider {

	public static final String ID = "_id";
	public static final String NAME = "Name";
	public static final String DUE_DATE = "DueDate";
	public static final String CATEGORY_ID = "CategoryId";
	public static final String CREATE_DATE = "CreateDate";
	public static final String IS_COMPLETE = "IsComplete";
	public static final String AUTHORITY = "com.jeffgabriel.RoundTuit";
	public static final Uri ALL_TASKS = Uri.parse("content://" + AUTHORITY
			+ "/task");
	private static final int TASKS = 1;
	private static final int TASK_ID = 2;

	com.jeffgabriel.RoundTuit.Interfaces.IDbHelper _dbHelper;
	Context _context;

	public TaskProvider() {
		super();
	}

	public TaskProvider(com.jeffgabriel.RoundTuit.Interfaces.IDbHelper helper,
			Context context) {
		setFromLocalContext(context);
		_dbHelper = helper;
		initProvider();
	}

	private void initProvider() {
		if (_dbHelper == null)
			_dbHelper = new DatabaseHelper(_context);
		try {
			_dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TaskQuery getTaskQueryWithPreference() {
		return new TaskQuery(
				PreferenceService.getShowCompletedPreference(_context));
	}

	private void setFromLocalContext(Context localContext) {
		_context = getContext();
		if (_context == null && localContext != null)
			_context = localContext;
	}

	public void delete(Task task) {
		_dbHelper.delete(task.get_id());
	}

	public List<Task> getAll() {
		return getSome(PreferenceService.getDefaultListPageSize(getContext()),
				0);
	}

	String categoryWhere = " CategoryId = ? ";

	public List<Task> getSome(int pageSize, int pageIndex) {
		List<Task> fullResults = _dbHelper
				.getTasks(getTaskQueryWithPreference());
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

	public Cursor getTaskCursor() {
		return _dbHelper.getTaskCursor(getTaskQueryWithPreference());
	}

	public void update(Task task) {
		if (task.get_dueDate() == null)
			throw new IllegalStateException(getContext().getResources()
					.getString(R.string.noDueDateError));
		_dbHelper.update(task);
	}

	String whereClause = "_id = ?";

	public Task get(int taskId) {
		TaskQuery byId = new TaskQuery(true);
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(Integer.toString(taskId));
		byId.set_whereParameters(parms);
		byId.set_whereStatement(whereClause);
		ArrayList<Task> tasks = _dbHelper.getTasks(byId);
		if (tasks.isEmpty() == false)
			return tasks.get(0);
		return null;
	}

	public void closeDatabase() {
		_dbHelper.close();
	}

	@Override
	public int delete(Uri taskUri, String where, String[] whereArgs) {
		String idString = taskUri.getLastPathSegment();
		int taskId = Integer.parseInt(idString);
		Task deleteTask = get(taskId);
		if (deleteTask != null) {
			delete(deleteTask);
			return 1;
		}
		return 0;
	}

	@Override
	public String getType(Uri taskUri) {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(TaskProvider.AUTHORITY, "task", TASKS);
		matcher.addURI(TaskProvider.AUTHORITY, "task/#", TASK_ID);
		switch (matcher.match(taskUri)) {
		case TASKS:
			return "vnd.android.cursor.dir/Tasks";
		case TASK_ID:
			return "vnd.android.cursor.item/Task";
		default:
			throw new IllegalArgumentException("Unknown URI " + taskUri);
		}
	}

	@Override
	public Uri insert(Uri tableUri, ContentValues taskContent) {
		Task newTask = new Task(taskContent);
		if (newTask.get_name().length() > 0) {
			newTask.set_id(-1);
			_dbHelper.add(newTask);
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		setFromLocalContext(null);
		initProvider();
		return true;
	}

	@Override
	public Cursor query(Uri tableUri, String[] projection, String selection,
			String[] arguments, String sortOrder) {
		return getTaskCursor();
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		Task taskToUpdate = new Task(values);
		if (taskToUpdate.get_id() > 0) {
			Task existing = get(taskToUpdate.get_id());
			if (existing != null) {
				_dbHelper.update(taskToUpdate);
				return 1;
			}
		}
		return 0;
	}
}
