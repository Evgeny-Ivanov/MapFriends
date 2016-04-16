package com.example.stalker.mapfriends.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.fragments.MapCustomFragment;
import com.example.stalker.mapfriends.message.DataAndStatusMsg;
import com.example.stalker.mapfriends.message.RequestDataMsg;



import java.io.IOException;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by stalker on 05.04.16.
 */

//Loader - используется для ассинхронного выполнения задачи + умеет кэшировать данные при повороте экрана
public class CoordinatesServerLoader extends AsyncTaskLoader<DataAndStatusMsg> {//указываем какой тип будет возращать Loader после работы
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
    public DataAndStatusMsg loadInBackground(){
        Log.d(MainApplication.log, "loadInBackground");

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://195.19.44.134:8081/")
                .build();

        RequestDataMsg requestDataMsg = new RequestDataMsg();
        requestDataMsg.setId(idUser);
        APIServerGetCoor apiServerGetCoor = retrofit.create(APIServerGetCoor.class);
        Call<DataAndStatusMsg> callGet = apiServerGetCoor.get(requestDataMsg);
        try {
            Response<DataAndStatusMsg> responseGet = callGet.execute();
            return responseGet.body();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
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
