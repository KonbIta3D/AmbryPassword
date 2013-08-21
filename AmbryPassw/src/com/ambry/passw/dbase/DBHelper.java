package com.ambry.passw.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//final String LOG_TAG="myLogs"

/**
 * Created by pcuser on 18.07.13.
 */
public class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
          //  Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "login text,"
                    + "passwd text,"
                    + "comment text"+");");
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table mytable;");

    }

   // @Override
   // public void onUpgrate(SQLiteDatabase db, int oldVersion, int newVersion) {    }
}
