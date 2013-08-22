package com.ambry.passw.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

//import android.content.ContentValues;
import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ambry.passw.activity.fragment.AddNewItemFragment;
import com.ambry.passw.activity.fragment.ConfigFragment;
import com.ambry.passw.activity.fragment.SearchFragment;
import com.ambry.passw.dbase.DBHelper;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.R;

public class MainActivity extends SherlockFragmentActivity implements
		View.OnClickListener {

	final String ID = "id";
	final String LOGIN = "login";
	final String PASSWD = "passwd";
	final String COMMENT = "comment";
	DBHelper dbHelper;
	Button bfind, bsave;
	EditText editTextLogin, editTextPass, editTextComment;
	ArrayList<Item> data = new ArrayList<Item>();
	MyAdapter sAdapter;
	ConfigFragment conFrgmnt;
	SearchFragment searchFragment;
	AddNewItemFragment addItemFragment;
	
	Operate_DB operate_db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		operate_db = new Operate_DB(getApplicationContext());

		conFrgmnt = new ConfigFragment();
		searchFragment = new SearchFragment();
		addItemFragment = new AddNewItemFragment();

		bfind = (Button) findViewById(R.id.buttonFind);
		bfind.setOnClickListener(this);

		bsave = (Button) findViewById(R.id.buttonSave);
		bsave.setOnClickListener(this);

		editTextLogin = (EditText) findViewById(R.id.editText);

		editTextPass = (EditText) findViewById(R.id.editText2);

		editTextComment = (EditText) findViewById(R.id.editText3);
		editTextComment.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER
							|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
						
						operate_db.insertData(editTextLogin.getText().toString(),
												editTextPass.getText().toString(),
												editTextComment.getText().toString()
												);
						editTextLogin.setText("");
						editTextPass.setText("");
						editTextComment.setText("");
						hideKeyBoard();
						operate_db.findItemsByLogin(data,"");
						updateList();

						return true;
					}
				}
				return false;
			}
		});

		

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonSave:
			operate_db.insertData(editTextLogin.getText().toString(),
									editTextPass.getText().toString(),
									editTextComment.getText().toString()
									);
			editTextLogin.setText("");
			editTextPass.setText("");
			editTextComment.setText("");
			
			operate_db.findItemsByLogin(data,"");
			updateList();
			break;
		case R.id.buttonFind:
			operate_db.findItemsByLogin(data,editTextLogin.getText().toString());
			updateList();
			hideKeyBoard();
		default:
			break;
		}

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

	public String md5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuffer sb = new StringBuffer(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[1] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateList() {

		sAdapter = new MyAdapter(getApplicationContext(), data);
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(sAdapter);
		registerForContextMenu(list);
		sAdapter.notifyDataSetChanged();
	}
/*
	private void findItems(String login1) {
		data.clear();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] columns = new String[] { "id", "login", "passwd", "comment" };
		String selection = "login LIKE (?)";
		String[] selectionArgs = new String[] { login1 + "%" };
		String orderBy = "login";

		Cursor c = db.query("mytable", columns, selection, selectionArgs, null,
				null, orderBy);
		if (c.moveToFirst()) {

			do {

				String login = c.getString(c.getColumnIndex("login"));
				String password = c.getString(c.getColumnIndex("passwd"));
				String comment = c.getString(c.getColumnIndex("comment"));
				long id = c.getLong(c.getColumnIndex("id"));

				data.add(new Item(login, password, comment, id));
			} while (c.moveToNext());

		}
		db.close();
	}

	private void insertData() {

		ContentValues cv = new ContentValues();
		String login1 = editTextLogin.getText().toString();
		String passwd1 = editTextPass.getText().toString();
		String comment1 = editTextComment.getText().toString();

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (login1.length() != 0 && passwd1.length() != 0) {
			cv.put("login", login1);
			cv.put("passwd", passwd1);
			cv.put("comment", comment1);
			db.insert("mytable", null, cv);
			cv.clear();
		}
		editTextLogin.setText("");
		editTextPass.setText("");
		editTextComment.setText("");

		hideKeyBoard();

		db.close();
	} */

	private void hideKeyBoard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(
				editTextComment.getWindowToken(), 0);
	}

}