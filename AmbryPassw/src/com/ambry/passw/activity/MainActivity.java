package com.ambry.passw.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


//import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class MainActivity extends SherlockFragmentActivity implements
		View.OnClickListener {
	private static final String INCOM_PASS = "myPass";
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
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intent = getIntent();
		Toast myPass = Toast.makeText(this, intent.getStringExtra(INCOM_PASS), Toast.LENGTH_LONG);
		
		myPass.show();
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

						operate_db.insertData(editTextLogin.getText()
								.toString(), editTextPass.getText().toString(),
								editTextComment.getText().toString());
						editTextLogin.setText("");
						editTextPass.setText("");
						editTextComment.setText("");
						hideKeyBoard();
						operate_db.findItemsByLogin(data, "");
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
					editTextPass.getText().toString(), editTextComment
							.getText().toString());
			editTextLogin.setText("");
			editTextPass.setText("");
			editTextComment.setText("");

			operate_db.findItemsByLogin(data, "");
			updateList();
			break;
		case R.id.buttonFind:
			operate_db.findItemsByLogin(data, editTextLogin.getText()
					.toString());
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

	private void hideKeyBoard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(
				editTextComment.getWindowToken(), 0);
	}

}