package com.ambry.passw.dbase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ambry.passw.activity.Item;

public class Operate_DB {
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private final String SAVEPASSWORD_TABLE = "savePassword";
	private final String MYTABLE = "mytable";

	public Operate_DB(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();

	}

	public void findItemsByLogin(ArrayList<Item> data, String login1) {
		findItems(data, login1, 1);
	}

	public void findItemsByComment(ArrayList<Item> data, String login1) {
		findItems(data, login1, 2);
	}

	private void findItems(ArrayList<Item> data, String login1, int i) {
		data.clear();

		String[] columns = null;
		String selection = null;
		String[] selectionArgs = null;
		String orderBy = null;

		if (i == 1) {
			columns = new String[] { "id", "login", "passwd", "comment" };
			selection = "login LIKE (?)";
			selectionArgs = new String[] { login1 + "%" };
			orderBy = "login";
		}

		if (i == 2) {
			columns = new String[] { "id", "login", "passwd", "comment" };
			selection = "comment LIKE (?)";
			selectionArgs = new String[] { login1 + "%" };
			orderBy = "comment";
		}
		Cursor c = db.query(MYTABLE, columns, selection, selectionArgs, null,
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
	}

	public void insertData(String login1, String passwd1, String comment1) {

		ContentValues cv = new ContentValues();
		cv.put("login", login1);
		cv.put("passwd", passwd1);
		cv.put("comment", comment1);
		db.insert(MYTABLE, null, cv);
		cv.clear();

	}

	// public boolean insertPassword(String passwd1) {
	//
	// ContentValues cv = new ContentValues();
	//
	// if (passwd1.length() != 0) {
	// cv.put("passwd", passwd1);
	// cv.put("activeCheckBox", 1);
	// cv.put("comment", "");
	// db.insert(SAVEPASSWORD_TABLE, null, cv);
	// cv.clear();
	// }
	//
	// String checkupSave = getCheckupPass();
	// if (checkupSave.equals(passwd1)) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	public boolean insertPassword(String passwd1) {

		ContentValues cv = new ContentValues();

		// if (passwd1.length() != 0) {
		cv.put("passwd", passwd1);
		cv.put("activeCheckBox", 1);
		cv.put("comment", "");
		if (!isActivePass()) {
			db.insert(SAVEPASSWORD_TABLE, null, cv);
			cv.clear();
			return true;
		}
		// }
		return false;
	}

	public void closeDb() {
		db.close();
	}

	public boolean isActivePass() {
		int activeCheckBox = 0;
		String selection = "id=?";
		String[] args = { 1 + "" };
		// String[] columns = new String[] { "passwd", "activeCheckBox",
		// "comment" };
		// Cursor c = db.query(SAVEPASSWORD_TABLE, columns, null, null, null,
		// null, null);
		Cursor c = db.query(SAVEPASSWORD_TABLE, null, selection, args, null,
				null, null);
		if (c.moveToFirst()) {
			activeCheckBox = c.getInt(c.getColumnIndex("activeCheckBox"));

		}
		c.close();
		if (activeCheckBox == 1) {
			return true;
		} else
			return false;
	}

	public String getCheckupPass() {

		String passwd = "";
		String[] columns = new String[] { "passwd", "activeCheckBox", "comment" };

		Cursor c = db.query(SAVEPASSWORD_TABLE, columns, null, null, null,
				null, null);
		if (c.moveToFirst()) {

			do {
				passwd = c.getString(c.getColumnIndex("passwd"));

			} while (c.moveToNext());
		}

		return passwd;
	}

	public boolean updatePassword(boolean isCheckPassword, String newPassword) {
		int checkPassword = 0;
		if (isCheckPassword) {
			checkPassword = 1;
		}

		String[] whereArgs = { 1 + "" };

		ContentValues values = new ContentValues();
		values.put("passwd", newPassword);
		values.put("activeCheckBox", checkPassword);
		String whereClause = "id=?";
		if (isActivePass()) {
			db.update(SAVEPASSWORD_TABLE, values, whereClause, whereArgs);
			return true;
		}
		return false;
	}

	public boolean updatePassword(boolean isCheckPassword) {
		int checkPassword = 0;
		if (isCheckPassword) {
			checkPassword = 1;
		}

		String[] whereArgs = { 1 + "" };
		ContentValues values = new ContentValues();
		values.put("activeCheckBox", checkPassword);

		String whereClause = "id=?";
		if (isActivePass()) {
			db.update(SAVEPASSWORD_TABLE, values, whereClause, whereArgs);
			return true;
		}
		return false;
	}

}
