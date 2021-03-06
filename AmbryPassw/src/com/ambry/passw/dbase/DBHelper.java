package com.ambry.passw.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by YAKOVLEV on 18.07.13.
 */
public class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context) {
            
            super(context, "myDB.db", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
          
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "login text,"
                    + "passwd text,"
                    + "comment text"+");");
            db.execSQL("create table savePassword ("
                    + "id integer primary key autoincrement,"
                    + "passwd text,"
                    + "activeCheckBox integer,"
                    + "comment text"+");");
            db.execSQL("create table saveQuestion ("
                    + "id integer primary key autoincrement,"
                    + "question text,"
                    + "answer text"+");");
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table mytable;");

    }

   // @Override
   // public void onUpgrate(SQLiteDatabase db, int oldVersion, int newVersion) {    }
}
