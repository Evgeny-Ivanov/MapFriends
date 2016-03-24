package com.example.stalker.mapfriends.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by stalker on 24.03.16.
 */
public class СoordinatesSaveService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Handler handler = new Handler();
        handler.post(new TaskSaveСoordinates());

        return START_STICKY;//перезапустить сервис после того как его убъет система
    }

    private class TaskSaveСoordinates implements Runnable {
        private LocationManager locationManager;
        private int minDistance = 10;//если на такое расстояние изменится местоположение то придут новые координаты.
        private int interval = 10 * 1000;//минимальное время (в миллисекундах) между получением данных

        TaskSaveСoordinates() {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (ActivityCompat.checkSelfPermission(СoordinatesSaveService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        }

        private LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {//новые данные о местоположении
                //location.getLatitude();//широта
                //location.getLongitude();//долгота

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

            }

            @Override
            public void onProviderDisabled(String provider) {//указанный провайдер был отключен юзером
                //getLastKnownLocation (он может вернуть null) запрашиваем последнее доступное
                // местоположениеот включенного провайдера и отображаем его. Оно может быть вполне актуальным,
                // если вы до этого использовали какое-либо приложение с определением местоположения.

            }
        };
    }



}
