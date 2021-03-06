package com.example.stalker.mapfriends.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.R;
import com.example.stalker.mapfriends.interfaces.ProgressbarVisibility;
import com.google.android.gms.maps.MapFragment;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;


/**
 * Created by stalker on 09.03.16.
 */
public class FriendsFragment extends ListFragment
    implements AdapterView.OnItemClickListener{

    VKList<VKApiUserFull> friendsVK;

    @Override//что отрисовать во фрагменте
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_friends,null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("friends",friendsVK);
    }

    @Override//нет доступа к ui
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override//доступ к ui появился
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getView - ссылка на фрагмент
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);//нельзя выбирать несколько пунктов
        getListView().setOnItemClickListener(this);


        if(savedInstanceState != null) {
            Log.d(MainApplication.log,"savedInstanceState != null");
            friendsVK = savedInstanceState.getParcelable("friends");
            if(friendsVK != null)
                getListView().setAdapter(new ItemFriendsAdapter(getActivity(), friendsVK));
        }

        if(friendsVK == null) {
            if(getActivity() instanceof ProgressbarVisibility)
                ((ProgressbarVisibility)getActivity()).setVisibleProgressBar();
            VKRequest requestGetFriends = VKApi.friends()
                    .get(VKParameters.from(VKApiConst.FIELDS,
                            VKApiUserFull.FIELD_PHOTO_100,
                            VKApiConst.NAME_CASE, "gen"));
            requestGetFriends.executeWithListener(requestGetFriendsListener);
        }

    }

    @Override//действие на нажатие на пункт
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MapFriend mapFriend = (MapFriend)getActivity();
        mapFriend.showMapFriends(friendsVK.get(position));
    }

    static class ViewHolder {
        TextView txtItem;
        ImageView imageItem;

    }

    private class ItemFriendsAdapter extends BaseAdapter{//адаптер - получает данные и выдает View для отображения пункта списка

        private VKList<VKApiUserFull> users;
        private Context context;
        private LayoutInflater inflater;

        ItemFriendsAdapter(Context context,VKList<VKApiUserFull> users){
            super();
            this.users = users;
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return users.getCount();
        }

        @Override// элемент по позиции
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override// id по позиции
        public long getItemId(int position) {
            return position;
        }

        @Override// пункт списка
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {//android может ипользовать элементы списка
                                     //созданные ранее но невидные сейчас на экране
                                     //(что бы не собирать view из xml лишний раз)
                convertView = inflater.inflate(R.layout.item_friend, parent,false);
                viewHolder = new ViewHolder();
                viewHolder.txtItem = (TextView)convertView.findViewById(R.id.nameFriend);
                viewHolder.imageItem = (ImageView) convertView.findViewById(R.id.imageFriend);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            VKApiUserFull user = (VKApiUserFull)getItem(position);
            String fullName = "Карта " + user.last_name + " " + user.first_name;
            viewHolder.txtItem.setText(fullName);

            Picasso.with(context)
                    .load(user.photo_100)
                    .error(R.drawable.img_default)
                    .into(viewHolder.imageItem);

            return convertView;
        }
    }


    private VKRequest.VKRequestListener requestGetFriendsListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            friendsVK = (VKList<VKApiUserFull>)response.parsedModel;
            if(isAdded()) {
                getListView().setAdapter(new ItemFriendsAdapter(getActivity(), friendsVK));
            }
            if(getActivity() instanceof ProgressbarVisibility)
                ((ProgressbarVisibility)getActivity()).setInvisibleProgressBar();
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            String message = "Что то не так с сетью";
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

            if(getActivity() instanceof ProgressbarVisibility)
                ((ProgressbarVisibility)getActivity()).setInvisibleProgressBar();
        }
    };

    public interface MapFriend{
        void showMapFriends(VKApiUserFull friend);
    }

}
