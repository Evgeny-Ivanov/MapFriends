package com.example.stalker.mapfriends;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

//тут проверяем авторизацию
//если не авторизован - показываем фрагмент авторизации
//иначе - запускаем главный экран
public class SplashActivity extends AppCompatActivity {
    private static final String BROADCAST_ACTION_START_COOR_COLLECTION = "start.coordinate.collection";

    private int timeDelayed = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, timeDelayed);

        Intent intent = new Intent(BROADCAST_ACTION_START_COOR_COLLECTION);
        sendBroadcast(intent);
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
