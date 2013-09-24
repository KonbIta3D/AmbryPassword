package com.ambry.passw.dbase;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ambry.passw.activity.Item;
import com.ambry.passw.security.Crypto_Code;

public class Operate_DB {
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private final String SAVEPASSWORD_TABLE = "savePassword";
	private final String QUESTIONS_TABLE = "saveQuestion";
	private final String MYTABLE = "mytable";
	private final String ID = "id";
	private final String LOGIN = "login";
	private final String PASSWORD = "passwd";
	private final String COMMENT = "comment";

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

	public ArrayList<Item> findItemsByLogin(String login) {
		return null;

	}

	private void findItems(ArrayList<Item> data, String login1, int i) {
		data.clear();

		String[] columns = null;
		String selection = null;
		String[] selectionArgs = null;
		String orderBy = null;

		if (i == 1) {
			columns = new String[] { "id", "login", "passwd", "comment" };
			selection = "lower(login) LIKE (?)";
			selectionArgs = new String[] { "%" + login1 + "%" };
			orderBy = "login";
		}

		if (i == 2) {
			columns = new String[] { "id", "login", "passwd", "comment" };
			selection = "lower(comment) LIKE (?)";
			selectionArgs = new String[] { "%" + login1 + "%" };
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

	public void insertData(Item item) {

		ContentValues cv = new ContentValues();
		cv.put("login", item.getLogin().toString());
		cv.put("passwd", item.getPassword().toString());
		cv.put("comment", item.getComment().toString());
		db.insert(MYTABLE, null, cv);
		cv.clear();

	}

	public boolean insertPassword(String passwd1) {

		Cursor cursor = db.rawQuery("select count(*) from "
				+ SAVEPASSWORD_TABLE, null);
		cursor.moveToFirst();
		int qtyRows = cursor.getInt(0);
		cursor.close();

		ContentValues cv = new ContentValues();

		cv.put("passwd", passwd1);
		cv.put("activeCheckBox", 1);
		cv.put("comment", getCurrentDateTime());
		if (!isActivePass() & qtyRows == 0) {
			db.insert(SAVEPASSWORD_TABLE, null, cv);
			cv.clear();
			return true;
		}

		return false;
	}

	public void closeDb() {
		db.close();
	}

	public boolean isActivePass() {
		int activeCheckBox = 0;
		String selection = "id=?";
		String[] args = { 1 + "" };

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

	public boolean isPasswordPresent() {
		Cursor cur = db.rawQuery("select * from " + SAVEPASSWORD_TABLE + ";",
				null);
		if (cur.moveToFirst()) {
			cur.close();
			return true;
		} else {
			cur.close();
			return false;
		}
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

		Cursor cursor = db.rawQuery("select count(*) from "
				+ SAVEPASSWORD_TABLE, null);
		cursor.moveToFirst();
		int qtyRows = cursor.getInt(0);
		cursor.close();

		ContentValues values = new ContentValues();
		values.put("passwd", newPassword);
		values.put("activeCheckBox", checkPassword);
		values.put("comment", getCurrentDateTime());
		String whereClause = "id=?";
		if (qtyRows > 0) {
			db.update(SAVEPASSWORD_TABLE, values, whereClause, whereArgs);
			return true;
		}
		return false;
	}

	public void updatePassword(String newPassword) {

		String[] whereArgs = { 1 + "" };

		ContentValues values = new ContentValues();
		values.put("passwd", newPassword);
		values.put("activeCheckBox", "");
		values.put("comment", getCurrentDateTime());
		String whereClause = "id=?";

		db.update(SAVEPASSWORD_TABLE, values, whereClause, whereArgs);

	}

	public boolean updatePassword(boolean isCheckPassword) {
		int checkPassword = 0;
		if (isCheckPassword) {
			checkPassword = 1;
		}

		String[] whereArgs = { 1 + "" };
		ContentValues values = new ContentValues();
		values.put("activeCheckBox", checkPassword);
		values.put("comment", getCurrentDateTime());

		String whereClause = "id=?";
		if (isActivePass()) {
			db.update(SAVEPASSWORD_TABLE, values, whereClause, whereArgs);
			return true;
		}
		return false;
	}

	public long getNextId() {
		Cursor cur = db
				.rawQuery("select max(id)+1 from " + MYTABLE + ";", null);
		long nextId = 1;
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			nextId = cur.getLong(0);
		}
		cur.close();
		return nextId;
	}

	private String getCurrentDateTime() {
		String currentDateTime = "";

		final Calendar c = Calendar.getInstance();
		String yy = c.get(Calendar.YEAR) + "";
		String mth = addZero(c.get(Calendar.MONTH));
		String dd = addZero(c.get(Calendar.DAY_OF_MONTH));
		String hh = addZero(c.get(Calendar.HOUR));
		String min = addZero(c.get(Calendar.MINUTE));
		String sec = addZero(c.get(Calendar.SECOND));

		currentDateTime = yy + "." + mth + "." + dd + " " + hh + ":" + min
				+ ":" + sec;
		return currentDateTime;
	}

	private String addZero(int num) {
		String sNum = num + "";
		if (sNum.length() < 2 & num < 10)
			sNum = "0" + sNum;
		return sNum;
	}

	public void updateMyTable(String newPassword, String oldPassword) {
		Crypto_Code cr = new Crypto_Code();
		Cursor cur = db.rawQuery("select * from " + MYTABLE + ";", null);
		ArrayList<Item> list = new ArrayList<Item>();
		if (cur.getCount() > 0) {

			cur.moveToFirst();
			do {
				Item item = new Item(cur.getString(cur.getColumnIndex(LOGIN)),
						cur.getString(cur.getColumnIndex(PASSWORD)),
						cur.getString(cur.getColumnIndex(COMMENT)),
						cur.getLong(cur.getColumnIndex(ID)));
				list.add(item);
			} while (cur.moveToNext());
		}
		cur.close();

		ContentValues values = new ContentValues();
		for (Item item : list) {

			String deCriptedOldPasInList = cr.decrypt(item.getPassword()
					.toString().getBytes(), oldPassword);

			String newCriptedPassInList = cr.encrypt(deCriptedOldPasInList,
					newPassword);

			item.setPassword(newCriptedPassInList);

			values.put(LOGIN, (String) item.getLogin());
			values.put(PASSWORD, (String) item.getPassword());
			values.put(COMMENT, (String) item.getComment());

			String[] args = { item.getId() + "" };
			String where = ID + "=?";

			db.update(MYTABLE, values, where, args);
			values.clear();
		}
	}
	
	public void updateMyTable(String newPassword) {
		Crypto_Code cr = new Crypto_Code();
		Cursor cur = db.rawQuery("select * from " + MYTABLE + ";", null);
		ArrayList<Item> list = new ArrayList<Item>();
		if (cur.getCount() > 0) {

			cur.moveToFirst();
			do {
				Item item = new Item(cur.getString(cur.getColumnIndex(LOGIN)),
						cur.getString(cur.getColumnIndex(PASSWORD)),
						cur.getString(cur.getColumnIndex(COMMENT)),
						cur.getLong(cur.getColumnIndex(ID)));
				list.add(item);
			} while (cur.moveToNext());
		}
		cur.close();

		ContentValues values = new ContentValues();
		for (Item item : list) {

			String oldPassword = getCheckupPass();
			String deCriptedOldPasInList = cr.decrypt(item.getPassword()
					.toString().getBytes(), oldPassword );

			String newCriptedPassInList = cr.encrypt(deCriptedOldPasInList,
					newPassword);

			item.setPassword(newCriptedPassInList);

			values.put(LOGIN, (String) item.getLogin());
			values.put(PASSWORD, (String) item.getPassword());
			values.put(COMMENT, (String) item.getComment());

			String[] args = { item.getId() + "" };
			String where = ID + "=?";

			db.update(MYTABLE, values, where, args);
			values.clear();
		}
		
	}

	public void delItem(Item myItem) {
		String[] whereArgs = { myItem.getId() + "" };
		db.delete(MYTABLE, ID + "=?", whereArgs);

	}

	public ArrayList<Item> getAlldata() {
		ArrayList<Item> allItems = new ArrayList<Item>();
		Cursor cur = db.query(MYTABLE, null, null, null, null, null, LOGIN);
		if (cur.moveToFirst()) {
			cur.moveToFirst();
			do {
				Item item = new Item(cur.getString(cur.getColumnIndex(LOGIN)),
						cur.getString(cur.getColumnIndex(PASSWORD)),
						cur.getString(cur.getColumnIndex(COMMENT)),
						cur.getLong(cur.getColumnIndex(ID)));
				allItems.add(item);
			} while (cur.moveToLast());
		}
		return allItems;
	}

	public void updateSecurData(String question, String answer) {

		// TODO We need return true if data updated else false
		String[] columns = new String[] { "question", "answer" };
		Cursor c = db.query(QUESTIONS_TABLE, columns, null, null, null, null,
				null);
		ContentValues values = new ContentValues();
		values.put("question", question);
		values.put("answer", answer);

		if (c.getCount() <= 0) {
			db.insert(QUESTIONS_TABLE, null, values);
			c.close();

		} else {
			String[] whereArgs = { 1 + "" };
			String whereClause = "id=?";
			db.update(QUESTIONS_TABLE, values, whereClause, whereArgs);
			c.close();

		}

	}

	public String getAnswerForQuestion(String question) {
		String answer = "";
		String[] columns = new String[] { "question", "answer" };

		Cursor c = db.query(QUESTIONS_TABLE, columns, null, null, null, null,
				null);
		if (c.moveToFirst()) {

			do {
				if (c.getString(c.getColumnIndex("question")).equals(question)) {
					answer = c.getString(c.getColumnIndex("answer"));
				}

			} while (c.moveToNext());
		}

		return answer;
	}

	public String getAnswerForQuestion() {
		String answer = "";
		String[] columns = new String[] { "question", "answer" };

		Cursor c = db.query(QUESTIONS_TABLE, columns, null, null, null, null,
				null);
		if (c.moveToFirst()) {

			do {

				answer = c.getString(c.getColumnIndex("answer"));

			} while (c.moveToNext());
		}

		return answer;
	}

	public String getQuestion() {
		String[] columns = { "question" };
		String question = "";
		Cursor cursor = db.query(QUESTIONS_TABLE, columns, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				question = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		return question;
	}

	

}
