package com.example.stalker.mapfriends;


import android.Manifest;
import android.app.FragmentTransaction;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.stalker.mapfriends.fragments.AuthFragment;
import com.example.stalker.mapfriends.fragments.ContactsFragment;
import com.example.stalker.mapfriends.fragments.FriendsFragment;
import com.example.stalker.mapfriends.fragments.MapCustomFragment;
import com.example.stalker.mapfriends.fragments.PrefFragment;
import com.example.stalker.mapfriends.interfaces.ProgressbarVisibility;
import com.example.stalker.mapfriends.network.CoorSendBroadcast;
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
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.Stack;


public class MainActivity extends AppCompatActivity
        implements Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener,
        AuthFragment.OnSuccessAuth, FriendsFragment.MapFriend,
        ProgressbarVisibility{

    private Drawer drawer;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private VKApiUserFull currentUser;
    private Integer drawerSaveCurrentSelection;
    private Stack<Integer> fragmentStack = new Stack<>();
    private ProgressBar progressBar;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//устанавливаем toolbar в качестве ActionBar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        if(savedInstanceState != null){
            currentUser = savedInstanceState.getParcelable("user");
            drawerSaveCurrentSelection = savedInstanceState.getInt("currentSelection");
            fragmentStack = (Stack<Integer>)savedInstanceState.getSerializable("fragmentStack");
            CharSequence title = savedInstanceState.getCharSequence("title");
            getSupportActionBar().setTitle(title);
        }

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
            request.executeWithListener(requestGetUserProfileListener);
        } else {
            Log.d(MainApplication.log, "create Drawer current User");
            createDrawer(currentUser);
            if (drawerSaveCurrentSelection == null)
                showFragment(Fragments.MAPS);
        }
    }

    private void createDrawer(VKApiUserFull user){
        String fullName = user.last_name + " " + user.first_name;
        IProfile profile = new ProfileDrawerItem()
                .withName(fullName)
                .withIcon(user.photo_200);//теперь библиотека сама скачает изображение

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(profile)
                .withSelectionListEnabled(false)//выключить список профилей
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderListener(this)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)//включаем стрелку
                .withActionBarDrawerToggleAnimated(true)//анимация стрелки
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_my_map)
                                .withIcon(FontAwesome.Icon.faw_map)
                                .withIdentifier(Fragments.MAPS)
                                .withOnDrawerItemClickListener(MainActivity.this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_friends)
                                .withIcon(FontAwesome.Icon.faw_vk)
                                .withIdentifier(Fragments.FRIENDS)
                                .withOnDrawerItemClickListener(MainActivity.this),

                        new SectionDrawerItem()
                                .withName(R.string.drawer_item_extra),

                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_anchor)
                                .withIdentifier(Fragments.SETTINGS)
                                .withOnDrawerItemClickListener(MainActivity.this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_contact)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withIdentifier(Fragments.CONTACTS)
                                .withOnDrawerItemClickListener(MainActivity.this)
                )
                .build();
        if(drawerSaveCurrentSelection != null){
            drawer.setSelection(drawerSaveCurrentSelection);
        }
    }

    private void showFragment(int position, int idUser){
        progressBar.setVisibility(View.VISIBLE);
        fragmentStack.add(position);
        setTitleToolbar(position);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();//вся работа с фрагментами происходит в транзакции
        //transaction.setCustomAnimations(R.animator.fragment_show, R.animator.fragment_hide);
        switch(position){
            case Fragments.MAPS:
                MapCustomFragment mapCustomFragment = new MapCustomFragment();
                Bundle arguments = new Bundle();
                arguments.putInt(MapCustomFragment.BUNDLE_ID_USER, idUser);
                mapCustomFragment.setArguments(arguments);

                transaction.replace(R.id.fragmentContainer, mapCustomFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Fragments.FRIENDS:
                FriendsFragment friendsFragment = new FriendsFragment();
                transaction.replace(R.id.fragmentContainer, friendsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Fragments.SETTINGS:
                PrefFragment prefFragment = new PrefFragment();
                transaction.replace(R.id.fragmentContainer, prefFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Fragments.CONTACTS:
                ContactsFragment contactsFragment = new ContactsFragment();
                transaction.replace(R.id.fragmentContainer, contactsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Fragments.AUTH:
                AuthFragment authFragment = new AuthFragment();
                transaction.replace(R.id.fragmentContainer, authFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setTitleToolbar(Integer position){
        int resId = R.string.titleMap;
        switch(position){
            case Fragments.MAPS:
                resId = R.string.titleMap;
                break;
            case Fragments.FRIENDS:
                resId = R.string.titleFriends;
                break;
            case Fragments.SETTINGS:
                resId = R.string.titleSettings;
                break;
            case Fragments.CONTACTS:
                resId = R.string.titleContact;
                break;
            case Fragments.AUTH:
                resId = R.string.titleAuth;
                break;
        }
        getSupportActionBar().setTitle(resId);
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
            return;
        }
        if (getFragmentManager().getBackStackEntryCount() > 1 ){
            getFragmentManager().popBackStack();
            fragmentStack.pop();
            Integer currentFragment = fragmentStack.peek();
            setTitleToolbar(currentFragment);
            drawer.setSelection(currentFragment);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {//сохраняем данные при повороте экрана
        super.onSaveInstanceState(outState);
        outState.putParcelable("user", currentUser);
        if (drawer != null) {
            Long currentSelection =  drawer.getCurrentSelection();
            outState.putInt("currentSelection", currentSelection.intValue());
            outState.putSerializable("fragmentStack", fragmentStack);
            CharSequence title = getSupportActionBar().getTitle();
            outState.putCharSequence("title",title);
        }
        Log.d(MainApplication.log, "save");
    }

    @Override
    public void showMapFriends(VKApiUserFull friend) {
        showFragment(Fragments.MAPS, friend.id);
        String title = "Карта " + friend.last_name + " " + friend.first_name;
        getSupportActionBar().setTitle(title);
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
            currentUser = usersArray.get(0);
            createDrawer(currentUser);
            registerCoorSendBroadcastReceiver(currentUser.id);
            showFragment(Fragments.MAPS);
        }

        @Override
        public void onError(VKError error) {
            String message = "Проблемы с сетью";
            Log.d(MainApplication.log, message);
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            MainActivity.this.onBackPressed();
        }

    };

    @Override
    public void setVisibleProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setInvisibleProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class Fragments  {
        public static final int MAPS = 0;
        public static final int FRIENDS = 1;
        public static final int SETTINGS = 2;
        public static final int CONTACTS = 3;
        public static final int AUTH = 4;
    }

}



