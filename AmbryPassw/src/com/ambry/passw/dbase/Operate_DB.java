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
	
	public Operate_DB(Context context){
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public void findItemsByLogin(ArrayList<Item> data, String login1){
		findItems(data,login1, 1);
	}
	
	public void findItemsByComment(ArrayList<Item> data, String login1){
		findItems(data,login1, 2);
	}
	
	private void findItems(ArrayList<Item> data, String login1, int i) {
		data.clear();
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] columns = null;
		String selection = null;
		String[] selectionArgs = null;
		String orderBy = null;
		
		if(i==1){			
			columns = new String[] { "id", "login", "passwd", "comment" };
			selection = "login LIKE (?)";
			selectionArgs = new String[] { login1 + "%" };
			orderBy = "login";
			}
		
		if(i==2){			
			columns = new String[] { "id", "login", "passwd", "comment" };
			selection = "comment LIKE (?)";
			selectionArgs = new String[] { login1 + "%" };
			orderBy = "comment";
			}
		Cursor c = db.query("mytable", columns, selection, selectionArgs, null,null, orderBy);
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

	public void insertData(String login1,String passwd1,String comment1 ) {

		ContentValues cv = new ContentValues();	
			cv.put("login", login1);
			cv.put("passwd", passwd1);
			cv.put("comment", comment1);
			db.insert("mytable", null, cv);
			cv.clear();
	
	}
	
	public boolean insertPassword(String passwd1) {

		ContentValues cv = new ContentValues();
//		String login1 = editTextLogin.getText().toString();
//		String passwd1 = editTextPass.getText().toString();
//		String comment1 = editTextComment.getText().toString();

//		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (passwd1.length() != 0) {
			cv.put("passwd", passwd1);
			cv.put("activeCheckBox", 1);
			cv.put("comment", "");
			db.insert("savePassword", null, cv);
			cv.clear();
		}
		
		String checkupSave = getCheckupPass();		
		if(checkupSave.equals(passwd1))return true;
		return false;
	}
	
	
	public void closeDb(){
		db.close();
	}
	
	public boolean checkupActivePass() {
			
		int activeCheckBox = 0;
		
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
					
			String[] columns = new String[] { "id","passwd","activeCheckBox", "comment" };
				
		
		Cursor c = db.query("savePassword", columns, null, null, null,null, null);
		if (c.moveToFirst()) {

			do {				
				activeCheckBox = c.getInt(c.getColumnIndex("activeCheckBox"));
//				long id = c.getLong(c.getColumnIndex("id"));
				
			} while (c.moveToNext());
		}
		db.close();
		if(activeCheckBox==1)return true;
		return false;
	}
	
public String getCheckupPass() {
	
		String passwd = null;
		
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
					
			String[] columns = new String[] { "id","passwd","activeCheckBox", "comment" };
				
		
		Cursor c = db.query("savePassword", columns, null, null, null,null, null);
		if (c.moveToFirst()) {

			do {				
				passwd = c.getString(c.getColumnIndex("passwd"));
//				long id = c.getLong(c.getColumnIndex("id"));
				
			} while (c.moveToNext());
		}
		
		return passwd;
	}


}
