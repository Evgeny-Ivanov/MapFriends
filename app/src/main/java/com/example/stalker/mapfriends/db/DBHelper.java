package com.example.stalker.mapfriends.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stalker on 25.03.16.
 */

public class DBHelper extends SQLiteOpenHelper {//класс отвечающий за структуру бд
    public static final String DB_NAME  = "mapfriends";
    public static final String TABLE_NAME  = "Coordinates";
    public static final String COLUMN_LATITUDE = "latitude";//широта
    public static final String COLUMN_LONGITUDE = "longitude";//долгота
    public static final String COLUMN_TIME = "time";
    private static final int DB_VERSION = 4;

    public DBHelper(Context context){
      super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//метод, который будет вызван, если БД, к которой мы хотим подключиться – не существует
        db.execSQL("CREATE table " + TABLE_NAME + " ("
                + "_id integer primary key autoincrement,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL, "
                + COLUMN_TIME + " DATE DEFAULT (datetime('now','localtime'))" + ");");
        //_id - в android принято первичному ключу давать такое имя
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {// будет вызван в случае, если мы пытаемся подключиться к БД более новой версии, чем существующая
        db.execSQL("DROP TABLE " + TABLE_NAME + " ;");
        onCreate(db);
    }
}
