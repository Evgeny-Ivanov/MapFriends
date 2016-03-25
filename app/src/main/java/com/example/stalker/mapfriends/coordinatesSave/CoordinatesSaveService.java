package com.example.stalker.mapfriends.coordinatesSave;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.stalker.mapfriends.MainActivity;
import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.db.DBHelper;

/**
 * Created by stalker on 24.03.16.
 */
public class CoordinatesSaveService extends Service {
    private DBHelper dbHelper;
    private SQLiteDatabase dbConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
        dbConnection = dbHelper.getWritableDatabase();//получаем подключение к бд

        Handler handler = new Handler();
        handler.post(new TaskSaveCoordinates(this,dbConnection));
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Log.d(MainApplication.log, "start Coordinates Service");
        getDataInDB();
        return START_STICKY;//перезапустить сервис после того как его убъет система
    }

    public void getDataInDB(){
        Cursor cursor = dbConnection.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);//получаем все данные

        // определяем номера столбцов по имени в выборке
        int latitudeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_LATITUDE);
        int longitudeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_LONGITUDE);

        while (cursor.moveToNext()){
            Log.d(MainApplication.log,
                    "DB : " +
                        "latitude = " + cursor.getDouble(latitudeColIndex) +
                                "longitude = " + cursor.getDouble(longitudeColIndex));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbConnection.close();
        dbHelper.close();
    }
}
