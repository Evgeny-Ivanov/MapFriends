package com.example.stalker.mapfriends.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stalker.mapfriends.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;


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
        getView().findViewById(R.id.authButton).setOnClickListener(this);
        getView().findViewById(R.id.authButtonOk).setOnClickListener(this);
        getView().findViewById(R.id.authButtonFacebook).setOnClickListener(this);
        responseView = (TextView)getView().findViewById(R.id.textAuth);
    }

    @Override//нажатие на кнопку входа
    public void onClick(View v) {

        String message = "Когда нибудь сделаем";
        switch (v.getId()){
            case R.id.authButton:
                VKSdk.login(this, "Auth");
                break;
            case R.id.authButtonOk:
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            case R.id.authButtonFacebook:
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

    }

    @Override//метод вызовется после окончания авторизации
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {// Пользователь успешно авторизовался
                OnSuccessAuth activity = ((OnSuccessAuth) getActivity());
                activity.onSuccessAuth();//рекомендация от google - запилить интерфейст в фрагменте и реализовывать его в активити
            }
            @Override
            public void onError(VKError error) {// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                responseView.setText("Ошибка входа");
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnSuccessAuth{
        void onSuccessAuth();
    }

}
