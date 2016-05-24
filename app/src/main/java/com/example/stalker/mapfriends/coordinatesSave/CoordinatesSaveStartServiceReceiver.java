package com.example.stalker.mapfriends.coordinatesSave;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.db.DBApi;

public class CoordinatesSaveStartServiceReceiver extends BroadcastReceiver {//с помощью этого запускаем сервис сбора координат при включении телефона

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainApplication.log, "Start CoordinatesService");
        DBApi dbApi = new DBApi(context);
        dbApi.open();

        new TaskSaveCoordinates(context, dbApi);
    }
}
