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
import com.example.stalker.mapfriends.db.DBApi;
import com.example.stalker.mapfriends.db.DBHelper;

/**
 * Created by stalker on 24.03.16.
 */
public class CoordinatesSaveService extends Service {
    private DBApi dbApi;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbApi = new DBApi(this);
        dbApi.open();

        Handler handler = new Handler();
        handler.post(new TaskSaveCoordinates(this,dbApi));
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Log.d(MainApplication.log, "start Coordinates Service");
        return START_STICKY;//перезапустить сервис после того как его убъет система
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbApi.close();
    }
}
