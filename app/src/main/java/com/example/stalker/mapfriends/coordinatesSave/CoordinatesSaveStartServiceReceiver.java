package com.example.stalker.mapfriends.coordinatesSave;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.coordinatesSave.CoordinatesSaveService;

public class CoordinatesSaveStartServiceReceiver extends BroadcastReceiver {//с помощью этого запускаем сервис сбора координат при включении телефона
    public CoordinatesSaveStartServiceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Start CoordinatesService in CoordinatesSaveStartServiceReceiver",Toast.LENGTH_SHORT);
        Log.d(MainApplication.log, "Start CoordinatesService");
        Intent startServiceIntent = new Intent(context,CoordinatesSaveService.class);
        context.startService(startServiceIntent);
    }
}
