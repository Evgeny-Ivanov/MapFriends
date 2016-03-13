package com.example.stalker.mapfriends;


import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.stalker.mapfriends.fragments.AuthFragment;
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


//http://java-help.ru/material-navigationdrawer/
public class MainActivity extends AppCompatActivity
        implements Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {

    FriendsFragment friendsFragment;
    AuthFragment authFragment;
    private Drawer drawer;
    private AccountHeader accountHeader;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        friendsFragment = new FriendsFragment();
        authFragment = new AuthFragment();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//устанавливаем toolbar в качестве ActionBar

        String name = "Иванов Евгений";//надо доставать из vk
        IProfile profile = new ProfileDrawerItem()
                .withName(name)
                .withIcon(R.drawable.img_default);

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(profile)
                .withSelectionListEnabled(false)//выключить список профилей
                .withOnAccountHeaderListener(this)
                .withTextColorRes(R.color.md_white_1000)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)//включаем стрелку
                .withActionBarDrawerToggleAnimated(true)//анимация стрелки
                .withAccountHeader(accountHeader)
                .withSliderBackgroundColorRes(R.color.primary)//цвет
                .addDrawerItems(
                        new DrawerItem()
                                .withName(R.string.drawer_item_my_map)
                                .withIcon(FontAwesome.Icon.faw_map),
                        new DrawerItem()
                                .withName(R.string.drawer_item_friends)
                                .withIcon(FontAwesome.Icon.faw_vk)
                                .withIdentifier(1),

                        new SectionDrawerItem()
                                .withName(R.string.drawer_item_extra),

                        new DrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_anchor),
                        new DrawerItem()
                                .withName(R.string.drawer_item_contact)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withIdentifier(2)
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
                transaction.setCustomAnimations(R.animator.fragment_show,R.animator.fragment_hide);
                transaction.replace(R.id.fragmentContainer, authFragment);
                transaction.commit();
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
            withSelectedColorRes(R.color.primary_dark);
            withOnDrawerItemClickListener(MainActivity.this);
            withSelectedIconColorRes(R.color.md_white_1000);
            withSelectedTextColorRes(R.color.md_white_1000);
        }
    }
}



