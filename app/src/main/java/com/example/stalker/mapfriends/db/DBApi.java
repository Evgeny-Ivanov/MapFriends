package com.example.stalker.mapfriends.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by stalker on 05.04.16.
 */
public class DBApi {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase dbConnectionWritable;
    private SQLiteDatabase dbConnectionReadable;
    public DBApi(Context context){
        this.context = context;
    }

    public void open(){
        dbHelper = new DBHelper(context);
        dbConnectionWritable = dbHelper.getWritableDatabase();
        dbConnectionReadable = dbHelper.getReadableDatabase();
    }

    public Cursor getAllCoordinates(){
        return dbConnectionWritable.query(DBHelper.TABLE_NAME,null,null,null,null,null,null);
    }

    public void saveLatLng(double latitude,double longitude){
        ContentValues contentValuesLocation = new ContentValues();// создаем объект для данных
        contentValuesLocation.put(DBHelper.COLUMN_LATITUDE, latitude);
        contentValuesLocation.put(DBHelper.COLUMN_LONGITUDE, longitude);

        dbConnectionWritable.insert(DBHelper.TABLE_NAME, null, contentValuesLocation);
    }

    public void deleteAll(){
        dbConnectionWritable.delete(DBHelper.TABLE_NAME,null,null);
    }

    public void close(){
        dbConnectionWritable.close();
    }
}
