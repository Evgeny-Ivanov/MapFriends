package com.example.stalker.mapfriends;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.stalker.mapfriends.coordinatesSave.CoordinatesSaveService;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.vk.sdk.util.VKUtil;

//тут проверяем авторизацию
//если не авторизован - показываем фрагмент авторизации
//иначе - запускаем главный экран
public class SplashActivity extends AppCompatActivity {

    private int timeDelayed = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent startServiceIntent = new Intent(this, CoordinatesSaveService.class);
        this.startService(startServiceIntent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, timeDelayed);

        DrawerImageLoader.init(abstractDrawerImageLoader);//определяем как библиотека будет скачивать изображения
    }

    private AbstractDrawerImageLoader abstractDrawerImageLoader = new AbstractDrawerImageLoader() {
        @Override
        public void set(ImageView imageView, Uri uri, Drawable placeholder) {
            super.set(imageView, uri, placeholder);
            Picasso
                    .with(imageView.getContext())
                    .load(uri).placeholder(placeholder)
                    .error(R.drawable.img_default)
                    .into(imageView);
        }

        @Override
        public void cancel(ImageView imageView) {
            super.cancel(imageView);
            Picasso
                    .with(imageView.getContext())
                    .cancelRequest(imageView);
        }
    };


}
