package com.example.stalker.mapfriends.network;

import com.example.stalker.mapfriends.message.DataAndStatusMsg;
import com.example.stalker.mapfriends.message.DataMsg;
import com.example.stalker.mapfriends.message.RequestDataMsg;
import com.example.stalker.mapfriends.message.StatusMsg;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by stalker on 16.04.16.
 */
public interface APIServerGetCoor {
    @POST("data-from-server")
    Call<DataAndStatusMsg> get(@Body RequestDataMsg msg);
}
