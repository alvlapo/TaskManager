package com.jeffgabriel.TaskManager;

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ResourceCursorAdapter;

public class TaskManagerActivity extends ListActivity {

	final ListActivity currentActivity = this;

	public static final String MENU_OPTION_KEY = "MenuItem";
	public static final String MENU_ITEM_OPEN = "OpenItem";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupList();
	}

	private void setupList() {
		TaskProvider provider = new TaskProvider(new DatabaseHelper(this),this);
		TaskCursorAdapter adapter = new TaskCursorAdapter(this, provider.getTaskCursor(),true);
		adapter.registerDataSetObserver(new DataSetObserver(){
			@Override
			public void onChanged() {
				super.onChanged();
				//getListView().refreshDrawableState();
				setupList();
			}
		});
		setListAdapter(adapter);

		/*ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.settings) {
			startActivity(new Intent(this, Options.class));
			return true;
		} else if (item.getItemId() == R.id.addNewTask) {
			startActivityForResult(new Intent(this, CreateTaskActivity.class),
					0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		((ResourceCursorAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();	
		((ResourceCursorAdapter)getListAdapter()).notifyDataSetChanged();
	}
}