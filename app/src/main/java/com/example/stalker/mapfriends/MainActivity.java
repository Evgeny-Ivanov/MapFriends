package com.example.stalker.mapfriends;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.stalker.mapfriends.fragments.FriendsFragment;
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
import com.vk.sdk.api.model.VKPhotoArray;

import org.json.JSONException;
import org.json.JSONObject;


//http://java-help.ru/material-navigationdrawer/
public class MainActivity extends AppCompatActivity
        implements Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {

    FriendsFragment friendsFragment;
    private Drawer drawer;
    private AccountHeader accountHeader;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        friendsFragment = new FriendsFragment();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//устанавливаем toolbar в качестве ActionBar

        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_200"));
        request.executeWithListener(example);
    }

    private void createDrawer(String name){
        IProfile profile = new ProfileDrawerItem()
                .withName(name)
                .withIcon(R.drawable.img_default);

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
                                .withIdentifier(0),
                        new DrawerItem()
                                .withName(R.string.drawer_item_friends)
                                .withIcon(FontAwesome.Icon.faw_vk)
                                .withIdentifier(1),

                        new SectionDrawerItem()
                                .withName(R.string.drawer_item_extra),

                        new DrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_anchor)
                                .withIdentifier(2),
                        new DrawerItem()
                                .withName(R.string.drawer_item_contact)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withIdentifier(3)
                )
                .build();
    }

    @Override//нажатие на пункт меню
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();//вся работа с фрагментами происходит в транзакции
        switch((int)drawerItem.getIdentifier()){
            case 0:
                break;
            case 1:
                transaction.setCustomAnimations(R.animator.fragment_show,R.animator.fragment_hide);
                transaction.replace(R.id.fragmentContainer, friendsFragment);//добавляем фрагмент
                //transaction.addToBackStack(null);//добавить в стек
                transaction.commit();
                break;
            case 2:
                VKSdk.logout();
                Intent intent = new Intent(this,SplashActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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

    private class DrawerItem extends PrimaryDrawerItem {//база для пунктов меню
        public DrawerItem(){
            super();
            withOnDrawerItemClickListener(MainActivity.this);
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
                //VKPhotoArray photoModel = (VKPhotoArray)responseJSON.get("photo_200");
                createDrawer(last_name + " " + first_name + " 100500");
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onError(VKError error) {
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {//I don't really believe in progress

        }
    };


}



