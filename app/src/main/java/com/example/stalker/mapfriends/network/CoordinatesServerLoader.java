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

    @Override
    public DataAndStatusMsg loadInBackground(){
        Log.d(MainApplication.log, "loadInBackground");
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(APIServerCoor.SERVER_URL)
                .build();

        RequestDataMsg requestDataMsg = new RequestDataMsg();
        requestDataMsg.setId(idUser);
        APIServerCoor apiServerCoor = retrofit.create(APIServerCoor.class);
        Call<DataAndStatusMsg> callGet = apiServerCoor.get(requestDataMsg);
        try {
            Response<DataAndStatusMsg> responseGet = callGet.execute();
            return responseGet.body();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

}
