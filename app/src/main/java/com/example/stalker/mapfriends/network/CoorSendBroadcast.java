package com.example.stalker.mapfriends.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;


/**
 * Created by stalker on 16.04.16.
 */
public class CoorSendBroadcast extends BroadcastReceiver {
    private int idUser;

    public CoorSendBroadcast(int idUser){
        this.idUser = idUser;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainApplication.log, "send coor");

        Intent intentStartCoorSendService = new Intent(context, CoorSendIntentService.class);
        intentStartCoorSendService.putExtra("idUser",idUser);
        context.startService(intentStartCoorSendService);
    }
}

