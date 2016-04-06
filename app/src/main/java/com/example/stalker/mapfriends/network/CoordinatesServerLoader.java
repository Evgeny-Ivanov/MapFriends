package com.example.stalker.mapfriends.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.db.DBApi;
import com.example.stalker.mapfriends.db.DBHelper;
import com.example.stalker.mapfriends.fragments.MapCustomFragment;
import com.google.android.gms.maps.model.LatLng;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by stalker on 05.04.16.
 */

//Loader - используется для ассинхронного выполнения задачи + умеет кэшировать данные при повороте экрана
public class CoordinatesServerLoader extends AsyncTaskLoader<List<LatLng>> {//указываем какой тип будет возращать Loader после работы
    private int idUser;

    public CoordinatesServerLoader(Context context,Bundle data){
        super(context);
        Log.d(MainApplication.log, "CoordinatesServerLoader");
        if(data != null){
            idUser = data.getInt(MapCustomFragment.BUNDLE_ID_USER);
        }
    }

    @Override//вызывается при старте активности к которой привязан Loader
    public void onStartLoading(){
        super.onStartLoading();
        Log.d(MainApplication.log, "onStartLoading");
    }

    @Override
    public List<LatLng> loadInBackground(){
        Log.d(MainApplication.log, "loadInBackground");
        DBApi dbApi = new DBApi(getContext());
        dbApi.open();
        Cursor cursor = dbApi.getAllCoordinates();

        // определяем номера столбцов по имени в выборке
        int latitudeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_LATITUDE);
        int longitudeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_LONGITUDE);

        List<LatLng> positions = new LinkedList<>();
        while (cursor.moveToNext()){
            LatLng position = new LatLng(cursor.getDouble(latitudeColIndex),cursor.getDouble(longitudeColIndex));
            positions.add(position);
        }
        dbApi.close();

        return positions;
    }

    @Override//вызывается при остановке активности к которой привязан Loader
    public void onStopLoading(){
        super.onStopLoading();
        Log.d(MainApplication.log, "onStopLoading");
    }

    @Override//лоадер стал неактивным (создан новый а текущий устарел)
    public void onAbandon(){
        super.onAbandon();
        Log.d(MainApplication.log, "onAbandon");
    }

    @Override//вызывается при уничтожении активности и данного Loader
    public void onReset(){
        super.onReset();
        Log.d(MainApplication.log, "onReset");
    }


}
