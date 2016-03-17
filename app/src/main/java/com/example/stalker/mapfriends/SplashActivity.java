package com.example.stalker.mapfriends;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.stalker.mapfriends.fragments.AuthFragment;
import com.example.stalker.mapfriends.fragments.SplashFragment;
import com.vk.sdk.VKSdk;
//тут проверяем авторизацию
//если не авторизован - показываем фрагмент авторизации
//иначе - запускаем главный экран
public class SplashActivity extends AppCompatActivity
        implements AuthFragment.OnSuccessAuth {

    private int timeDelayed = 2000;
    private SplashFragment splashFragment = new SplashFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.fragmentContainerSplash, splashFragment);
        t.commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VKSdk.isLoggedIn()) {
                    onSuccessAuth();
                } else {
                    AuthFragment authFragment = new AuthFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerSplash, authFragment);
                    transaction.commit();
                }
            }
        },timeDelayed);
    }

    @Override
    public void onSuccessAuth() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
