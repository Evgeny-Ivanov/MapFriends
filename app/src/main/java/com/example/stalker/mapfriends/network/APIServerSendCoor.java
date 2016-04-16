package com.example.stalker.mapfriends.network;

import com.example.stalker.mapfriends.message.DataMsg;
import com.example.stalker.mapfriends.message.StatusMsg;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by stalker on 16.04.16.
 */
public interface APIServerSendCoor {
    @POST("data-to-server")
    Call<StatusMsg> send(@Body DataMsg msg);
}
