package com.example.stalker.mapfriends;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//тут проверяем авторизацию
//если не авторизован - показываем фрагмент авторизации
//иначе - запускаем главный экран
public class SplashActivity extends AppCompatActivity {

    private int timeDelayed = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, timeDelayed);
    }
}
