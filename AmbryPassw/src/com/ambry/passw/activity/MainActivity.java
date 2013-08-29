package com.ambry.passw.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ambry.passw.R;
import com.ambry.passw.activity.fragment.AddNewItemFragment;
import com.ambry.passw.activity.fragment.ConfigFragment;
import com.ambry.passw.activity.fragment.SearchFragment;
import com.ambry.passw.dbase.DBHelper;
import com.ambry.passw.dbase.Operate_DB;

public class MainActivity extends SherlockFragmentActivity {
	private static final String INCOM_PASS = "myPass";
	final String ID = "id";
	final String LOGIN = "login";
	final String PASSWD = "passwd";
	final String COMMENT = "comment";
	DBHelper dbHelper;
	Button bfind, bsave;
	EditText editTextLogin, editTextPass, editTextComment;
	ArrayList<Item> data = new ArrayList<Item>();
	public static MyAdapter sAdapter;
	ConfigFragment conFrgmnt;
	SearchFragment searchFragment;
	AddNewItemFragment addItemFragment;

	Operate_DB operate_db;
	Intent intent;

	private static String secretWord = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		intent = getIntent();
		secretWord = intent.getStringExtra(INCOM_PASS);

		addItemFragment = new AddNewItemFragment();
		conFrgmnt = new ConfigFragment();
		searchFragment = new SearchFragment();

		operate_db = new Operate_DB(getApplicationContext());


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
			updateList();
			break;
		case R.id.menu_actnBtnAdd:
			addItemFragment.show(getSupportFragmentManager(), "add");
			updateList();
			break;
		case R.id.menu_conf:
			conFrgmnt.show(getSupportFragmentManager(), "conf");
			break;
		case R.id.menu_exit:
			finish();
			break;
		default:
			return true;
		}
		return false;

	}

	public static String getSecretWord() {
		return secretWord;
	}

	public void updateList() {

		sAdapter = new MyAdapter(getApplicationContext(), data, secretWord);
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(sAdapter);
		registerForContextMenu(list);
		sAdapter.notifyDataSetChanged();
	}



}