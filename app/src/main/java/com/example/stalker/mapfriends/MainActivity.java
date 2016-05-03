package com.example.stalker.mapfriends;


import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stalker.mapfriends.fragments.AuthFragment;
import com.example.stalker.mapfriends.fragments.ContactsFragment;
import com.example.stalker.mapfriends.fragments.FriendsFragment;
import com.example.stalker.mapfriends.fragments.MapCustomFragment;
import com.example.stalker.mapfriends.network.CoorSendBroadcast;
import com.google.android.gms.maps.MapFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;


public class MainActivity extends AppCompatActivity
        implements Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener,
        AuthFragment.OnSuccessAuth, FriendsFragment.MapFriend {

    private Drawer drawer;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private VKApiUserFull currentUser;
    private Integer drawerSaveCurrentSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(MainApplication.log, "ACCESS_FINE_LOCATION isnt yet");
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d(MainApplication.log, "ACCESS_FINE_LOCATION is already");
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(MainApplication.log, "WRITE_EXTERNAL_STORAGE isnt yet");
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Log.d(MainApplication.log, "WRITE_EXTERNAL_STORAGE is already");
        }

        if(savedInstanceState != null){
            currentUser = savedInstanceState.getParcelable("user");
            drawerSaveCurrentSelection = savedInstanceState.getInt("currentSelection");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//устанавливаем toolbar в качестве ActionBar

        if (!VKSdk.isLoggedIn()) {
            showFragment(Fragments.AUTH);
        } else {
            onSuccessAuth();
        }
    }

    @Override
    public void onSuccessAuth() {
        if(currentUser == null) {
            VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, VKApiUserFull.FIELD_PHOTO_200));
            request.executeSyncWithListener(requestGetUserProfileListener);
            createDrawer(currentUser);
            registerCoorSendBroadcastReceiver(currentUser.id);

        } else {
            Log.d(MainApplication.log, "create Drawer current User");
            createDrawer(currentUser);
        }

        if (drawerSaveCurrentSelection != null)
            showFragment(drawerSaveCurrentSelection);
        else
            showFragment(Fragments.MAPS);
    }

    private void createDrawer(VKApiUserFull user){
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {//определяем как библиотека будет скачивать изображения
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
        });

        String fullName = user.last_name + " " + user.first_name;
        IProfile profile = new ProfileDrawerItem()
                .withName(fullName)
                .withIcon(user.photo_200);//теперь библиотека сама скачает изображение

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(profile)
                .withSelectionListEnabled(false)//выключить список профилей
                .withOnAccountHeaderListener(this)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)//включаем стрелку
                .withActionBarDrawerToggleAnimated(true)//анимация стрелки
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new DrawerItem()
                                .withName(R.string.drawer_item_my_map)
                                .withIcon(FontAwesome.Icon.faw_map)
                                .withIdentifier(Fragments.MAPS),
                        new DrawerItem()
                                .withName(R.string.drawer_item_friends)
                                .withIcon(FontAwesome.Icon.faw_vk)
                                .withIdentifier(Fragments.FRIENDS),

                        new SectionDrawerItem()
                                .withName(R.string.drawer_item_extra),

                        new DrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_anchor)
                                .withIdentifier(Fragments.SETTINGS),
                        new DrawerItem()
                                .withName(R.string.drawer_item_contact)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withIdentifier(Fragments.CONTACTS)
                )
                .build();
        if(drawerSaveCurrentSelection != null){
            drawer.setSelection(drawerSaveCurrentSelection);
        }
    }

    private void showFragment(int position, int idUser){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();//вся работа с фрагментами происходит в транзакции

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null)//если не удалять при повторном создании карты будет конфликт
            transaction.remove(mapFragment);

        transaction.setCustomAnimations(R.animator.fragment_show, R.animator.fragment_hide);
        switch(position){
            case Fragments.MAPS:
                getSupportActionBar().setTitle(R.string.titleMap);
                MapCustomFragment mapCustomFragment = new MapCustomFragment();

                Bundle arguments = new Bundle();
                arguments.putInt(MapCustomFragment.BUNDLE_ID_USER, idUser);
                mapCustomFragment.setArguments(arguments);

                transaction.replace(R.id.fragmentContainer, mapCustomFragment);
                transaction.commit();
                break;
            case Fragments.FRIENDS:
                getSupportActionBar().setTitle(R.string.titleFriends);
                FriendsFragment friendsFragment = new FriendsFragment();
                transaction.replace(R.id.fragmentContainer, friendsFragment);
                transaction.commit();
                break;
            case Fragments.SETTINGS:
                getSupportActionBar().setTitle(R.string.titleSettings);
                VKSdk.logout();
                Intent intent = new Intent(this,SplashActivity.class);
                startActivity(intent);
                finish();
                break;
            case Fragments.CONTACTS:
                getSupportActionBar().setTitle(R.string.titleContact);
                ContactsFragment contactsFragment = new ContactsFragment();
                transaction.replace(R.id.fragmentContainer, contactsFragment);
                transaction.commit();
                break;
            case Fragments.AUTH:
                getSupportActionBar().setTitle(R.string.titleAuth);
                AuthFragment authFragment = new AuthFragment();
                transaction.replace(R.id.fragmentContainer, authFragment);
                transaction.commit();
                break;
        }
    }

    private void showFragment(int position){
        if(currentUser != null){
            showFragment(position, currentUser.id);
        }else {
            showFragment(position, -1);
        }
    }



    @Override//нажатие на пункт меню
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        showFragment((int) drawerItem.getIdentifier());
        return false;
    }

    @Override//нажатие на изображение профиля
    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
        return true;
    }

    @Override//при нажатии на кнопку назад закрываем менюшку
    public void onBackPressed(){
        if(drawer != null && drawer.isDrawerOpen()){
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {//сохраняем данные при повороте экрана
        super.onSaveInstanceState(outState);
        outState.putParcelable("user", currentUser);
        if(drawer != null) {
            Long currentSelection =  drawer.getCurrentSelection();
            outState.putInt("currentSelection", currentSelection.intValue());
        }
        Log.d(MainApplication.log, "save");
    }

    @Override
    public void showMapFriends(VKApiUserFull friend) {
        showFragment(Fragments.MAPS, friend.id);
        String title = "Карта " + friend.last_name + " " + friend.first_name;
        getSupportActionBar().setTitle(title);
    }

    private class DrawerItem extends PrimaryDrawerItem {//база для пунктов меню
        public DrawerItem(){
            super();
            withOnDrawerItemClickListener(MainActivity.this);
        }
    }

    private void registerCoorSendBroadcastReceiver(int idUser){//регистрируем сервис отправки координат на сервер
        CoorSendBroadcast coorSendBroadcast = new CoorSendBroadcast(idUser);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.TIME_TICK");
        registerReceiver(coorSendBroadcast, intentFilter);
    }

    private VKRequest.VKRequestListener requestGetUserProfileListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {//успех
            VKList<VKApiUserFull> usersArray = (VKList<VKApiUserFull>)response.parsedModel;
            VKApiUserFull userFull = usersArray.get(0);
            currentUser = userFull;
        }

        @Override
        public void onError(VKError error) {
            String message = "Проблемы с сетью";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        }
    };

    private class Fragments  {
        public static final int MAPS = 0;
        public static final int FRIENDS = 1;
        public static final int SETTINGS = 2;
        public static final int CONTACTS = 3;
        public static final int AUTH = 4;
    }

}



