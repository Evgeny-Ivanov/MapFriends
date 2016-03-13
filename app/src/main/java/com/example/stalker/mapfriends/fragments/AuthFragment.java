package com.example.stalker.mapfriends.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stalker.mapfriends.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stalker on 10.03.16.
 */
public class AuthFragment extends Fragment
        implements View.OnClickListener{

    private TextView responseView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_auth, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ImageView authButton = (ImageView)getView().findViewById(R.id.authButton);
        authButton.setOnClickListener(this);

        responseView = (TextView)getView().findViewById(R.id.textAuth);
    }

    @Override//нажатие на кнопку входа
    public void onClick(View v) {
        VKSdk.login(this, "Auth");
    }

    @Override//метод вызовется после окончания авторизации
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {// Пользователь успешно авторизовался
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS));
                request.executeWithListener(example);
            }
            @Override
            public void onError(VKError error) {// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                responseView.setText("Ошибка входа");
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private VKRequest.VKRequestListener example = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {//Do complete stuff
            String last_name = "error";
            String first_name = "error";
            try {
                JSONObject responseJSON = response.json.getJSONArray("response").getJSONObject(0);
                last_name = (String)responseJSON.get("last_name");
                first_name = (String)responseJSON.get("first_name");
            }catch (JSONException e){
                e.printStackTrace();
            }
            responseView.setText("Привет "+ first_name + " "+ last_name);
        }

        @Override
        public void onError(VKError error) {
            responseView.setText(error.toString());
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {//I don't really believe in progress

        }
    };

}
