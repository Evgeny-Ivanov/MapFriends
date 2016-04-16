package com.example.stalker.mapfriends.network;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.stalker.mapfriends.db.DBApi;
import com.example.stalker.mapfriends.db.DBHelper;
import com.example.stalker.mapfriends.message.DataMsg;
import com.example.stalker.mapfriends.message.StatusMsg;
import com.example.stalker.mapfriends.model.Coor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stalker on 16.04.16.
 */
public class CoorSendServerTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private int idUser;

    public CoorSendServerTask(Context context, int idUser){
        this.context = context;
        this.idUser = idUser;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DBApi dbApi = new DBApi(context);
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
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://195.19.44.134:8081/")
                .build();

        DataMsg dataMsg = new DataMsg(idUser, coors);
        APIServerSendCoor apiServerSendCoor = retrofit.create(APIServerSendCoor.class);
        Call<StatusMsg> callSend =  apiServerSendCoor.send(dataMsg);
        try {
            Response<StatusMsg> responseSend = callSend.execute();
            if(responseSend.isSuccessful()){
                dbApi.deleteAll();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        dbApi.close();
        return null;
    }
}
