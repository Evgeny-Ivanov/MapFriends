package com.example.stalker.mapfriends.network;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.db.DBApi;
import com.example.stalker.mapfriends.db.DBHelper;
import com.example.stalker.mapfriends.fragments.MapCustomFragment;
import com.example.stalker.mapfriends.message.DataMsg;
import com.example.stalker.mapfriends.message.StatusMsg;
import com.example.stalker.mapfriends.model.Coor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CoorSendIntentService extends IntentService {
    public CoorSendIntentService(){
        super("CoorSendIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int idUser = intent.getIntExtra(MapCustomFragment.BUNDLE_ID_USER,-1);
        DBApi dbApi = new DBApi(this);
        dbApi.open();
        Cursor cursor = dbApi.getAllCoordinates();

        // определяем номера столбцов по имени в выборке
        int latitudeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_LATITUDE);
        int longitudeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_LONGITUDE);
        int timeColIndex = cursor.getColumnIndex(DBHelper.COLUMN_TIME);

        List<Coor> coors = new LinkedList<>();
        while (cursor.moveToNext()){
            Coor coor = new Coor();
            coor.setId(idUser);
            coor.setLatitude(cursor.getDouble(latitudeColIndex));
            coor.setLongitude(cursor.getDouble(longitudeColIndex));
            coor.setDataBaseDate(cursor.getLong(timeColIndex));
            coors.add(coor);
            Log.d(MainApplication.log, coor.toString());
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServerCoor.SERVER_URL)
                .build();

        DataMsg dataMsg = new DataMsg(idUser, coors);
        APIServerCoor apiServerCoor = retrofit.create(APIServerCoor.class);
        Call<StatusMsg> callSend =  apiServerCoor.send(dataMsg);
        try {
            Response<StatusMsg> responseSend = callSend.execute();
            Log.d(MainApplication.log, new Integer(responseSend.body().getStatus()).toString());
            if(responseSend.isSuccessful()){
                Log.d(MainApplication.log, "success send");
                dbApi.deleteAll();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        dbApi.close();
    }
}
