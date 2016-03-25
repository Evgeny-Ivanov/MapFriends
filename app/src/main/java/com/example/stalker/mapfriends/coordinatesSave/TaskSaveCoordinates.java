package com.example.stalker.mapfriends.coordinatesSave;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.db.DBHelper;

/**
 * Created by stalker on 25.03.16.
 */

public class TaskSaveCoordinates implements Runnable {
    private LocationManager locationManager;
    private int minDistance = 1;//если на такое расстояние изменится местоположение то придут новые координаты.
    private int interval = 10 * 1000;//минимальное время (в миллисекундах) между получением данных
    private Context context;
    private SQLiteDatabase dbConnection;

    TaskSaveCoordinates(Context context,SQLiteDatabase dbConnection) {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        this.context = context;
        this.dbConnection = dbConnection;
    }

    @Override
    public void run() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;//проверка на то есть ли у нас права для получения координат
        }
        //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //стартовать экран с настройками для включения сети и GPS
        locationManager.requestLocationUpdates(//подписываемся на получение координат по GPS
                LocationManager.GPS_PROVIDER, interval, minDistance, locationListener);
        locationManager.requestLocationUpdates(//по сети
                LocationManager.NETWORK_PROVIDER,interval,minDistance,locationListener);

        //Есть еще третий тип провайдера - PASSIVE_PROVIDER.
        // Сам по себе этот провайдер никакие данные не вернет.
        // Но повесив на него слушателя, вы сможете получать данные о местоположении,
        // когда кто-то еще в системе пытается определить местоположение через обычные
        // провайдеры. Система будет дублировать результаты и вам.
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {//новые данные о местоположении
            ContentValues contentValuesLocation = new ContentValues();// создаем объект для данных
            contentValuesLocation.put(DBHelper.COLUMN_LATITUDE,location.getLatitude());
            contentValuesLocation.put(DBHelper.COLUMN_LONGITUDE,location.getLongitude());

            dbConnection.insert(DBHelper.TABLE_NAME, null, contentValuesLocation);

            Log.d(MainApplication.log,
                    "LocationListener: " +
                        "latitude = " + location.getLatitude() +
                                " longitude = " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //изменился статус указанного провайдера.
            // В поле status могут быть значения:
            // OUT_OF_SERVICE (данные будут недоступны долгое время),
            // TEMPORARILY_UNAVAILABLE (данные временно недоступны),
            // AVAILABLE (все ок, данные доступны).

            if(provider.equals(LocationManager.GPS_PROVIDER)){

            }
            if(provider.equals(LocationManager.NETWORK_PROVIDER)){

            }
        }

        @Override
        public void onProviderEnabled(String provider) {// указанный провайдер(включили GPS или сеть) был включен юзером
            Log.d(MainApplication.log,"start provider " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {//указанный провайдер был отключен юзером
            //getLastKnownLocation (он может вернуть null) запрашиваем последнее доступное
            // местоположениеот включенного провайдера и отображаем его. Оно может быть вполне актуальным,
            // если вы до этого использовали какое-либо приложение с определением местоположения.
            Log.d(MainApplication.log,"stop provider " + provider);
        }
    };
}