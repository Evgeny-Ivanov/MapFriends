package com.example.stalker.mapfriends;

import android.app.Application;
import android.content.Intent;


import com.example.stalker.mapfriends.coordinatesSave.CoordinatesSaveService;
import com.vk.sdk.VKSdk;

/**
 * Created by stalker on 12.03.16.
 */
//самый главный класс приложения
public class MainApplication extends Application {
    public static String log = "myLog";

    @Override
    public void onCreate(){
        super.onCreate();
        VKSdk.initialize(this);//VKsdk требует инициализацию тут, иначе exception
    }
}
