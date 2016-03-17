package com.example.stalker.mapfriends;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by stalker on 12.03.16.
 */
//самый главный класс приложения
public class MainApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        VKSdk.initialize(this);//VKsdk требует инициализацию тут, иначе exception
    }
}
