package com.ambry.passw.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ambry.passw.R;
import com.ambry.passw.activity.fragment.AddNewItemFragment;
import com.ambry.passw.activity.fragment.ChangePasswordFragment;
import com.ambry.passw.activity.fragment.ChangeSecurityQuestionFragment;
import com.ambry.passw.activity.fragment.SearchFragment;
import com.ambry.passw.dbase.DBHelper;
import com.ambry.passw.dbase.Operate_DB;

public class MainActivity extends SherlockFragmentActivity {

	final String ID = "id";
	final String LOGIN = "login";
	final String PASSWD = "passwd";
	final String COMMENT = "comment";

	private static final int CM_DELETE_ID = 0;
	DBHelper dbHelper;
	Button bfind, bsave;
	EditText editTextLogin, editTextPass, editTextComment;

	public static ArrayList<Item> data;
	private MyAdapter sAdapter;
	ChangePasswordFragment chngPswdFragment;
	SearchFragment searchFragment;
	AddNewItemFragment addItemFragment;
	ChangeSecurityQuestionFragment chngQstnFragment;

	Operate_DB operate_db;
	Intent intent;

	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		data = new ArrayList<Item>();
		intent = getIntent();

		addItemFragment = new AddNewItemFragment();
		chngPswdFragment = new ChangePasswordFragment();
		searchFragment = new SearchFragment();
		chngQstnFragment = new ChangeSecurityQuestionFragment();

		operate_db = new Operate_DB(getApplicationContext());

		list = (ListView) findViewById(R.id.list);
		updateList();

		registerForContextMenu(list);
		sAdapter.notifyDataSetChanged();
		setTitle("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		MenuInflater infltr = getSupportMenuInflater();
		infltr.inflate(R.menu.menu_main_activity, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_actnBtnFind:
			searchFragment.show(getSupportFragmentManager(), "search");
			break;
		case R.id.menu_actnBtnAdd:
			addItemFragment.show(getSupportFragmentManager(), "add");
			updateList();
			break;
		case R.id.menu_change_password:
			chngPswdFragment.show(getSupportFragmentManager(), "chngPswd");
			break;
		case R.id.menu_change_secur_question:
			chngQstnFragment.show(getSupportFragmentManager(), "chngSecur");
			break;
		case R.id.menu_exit:
			finish();
			break;

		default:
			return true;
		}
		return false;

	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		if (item.getItemId() == CM_DELETE_ID) {
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
					.getMenuInfo();

			Item toRemoveItem = data.get(acmi.position);
			data.remove(acmi.position);
			operate_db.delItem(toRemoveItem);
			updateList();
		}
		return super.onContextItemSelected(item);
	}

	private void updateList() {
		sAdapter = new MyAdapter(getApplicationContext(), data,
				operate_db.getCheckupPass());
		list.setAdapter(sAdapter);
		sAdapter.notifyDataSetChanged();

	}

	@Override
	protected void onStop() {
		operate_db.closeDb();
		super.onStop();
	}

}