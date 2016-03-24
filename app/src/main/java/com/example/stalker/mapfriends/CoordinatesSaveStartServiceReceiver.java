package com.example.stalker.mapfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.stalker.mapfriends.services.СoordinatesSaveService;

public class CoordinatesSaveStartServiceReceiver extends BroadcastReceiver {//с помощью этого запускаем сервис сбора координат при включении телефона
    public CoordinatesSaveStartServiceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context,СoordinatesSaveService.class);
        context.startService(startServiceIntent);
    }
}
