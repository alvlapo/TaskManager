package com.jeffgabriel.RoundTuit;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class RoundTuitActivity extends ListActivity {

	final ListActivity currentActivity = this;

	public static final String MENU_OPTION_KEY = "MenuItem";
	public static final String MENU_ITEM_OPEN = "OpenItem";

	TaskCursorAdapter Adapter() {
		TaskCursorAdapter adapter = new TaskCursorAdapter(this, _runningCursor,
				false);
		return adapter;
	}

	Cursor _runningCursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		_runningCursor = managedQuery(TaskProvider.ALL_TASKS, null, null, null,
				null);
		setListAdapter(Adapter());
		this.getListAdapter().registerDataSetObserver(dsObserver);
		this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				
				TaskProvider provider = new TaskProvider(null,view.getContext());
				Task currentTask = provider.get((int)id);
				Intent editIntent = new Intent(Intent.ACTION_EDIT,currentTask.get_Uri());
				editIntent.setType("vnd.android.cursor.item/Task");
				editIntent.putExtra("task", currentTask);
				startActivity(editIntent);
			}
		});
	}

	private final DataSetObserver dsObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			super.onChanged();
		}
	};

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
		} else if (item.getItemId() == R.id.addNewTask) {
			optionIntent = new Intent(this, CreateTaskActivity.class);
		}
		if (optionIntent != null) {
			startActivityForResult(optionIntent, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_runningCursor.close();
		_runningCursor = null;
		_runningCursor = managedQuery(TaskProvider.ALL_TASKS, null, null, null,
				null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (_runningCursor != null) {
			_runningCursor.requery();
			setListAdapter(Adapter());
		} else
			_runningCursor = managedQuery(TaskProvider.ALL_TASKS, null, null,
					null, null);
	}
}