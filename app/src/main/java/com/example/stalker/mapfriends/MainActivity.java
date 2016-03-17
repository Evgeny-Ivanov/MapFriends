package com.example.stalker.mapfriends;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

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

        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,VKApiUserFull.FIELD_PHOTO_200));
        request.executeWithListener(requestGetUserProfileListener);
    }

    private void createDrawer(VKApiUserFull user){

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {//определяем как библиотека будет скачивать изображения
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                super.set(imageView, uri, placeholder);
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                super.cancel(imageView);
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
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


    private VKRequest.VKRequestListener requestGetUserProfileListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {//успех
            VKList<VKApiUserFull> usersArray = (VKList<VKApiUserFull>)response.parsedModel;
            VKApiUserFull userFull = usersArray.get(0);
            createDrawer(userFull);
        }
    };


}



