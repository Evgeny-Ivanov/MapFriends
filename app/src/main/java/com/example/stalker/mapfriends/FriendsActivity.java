package com.example.stalker.mapfriends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    private ListView friends;
    private String[] names = {
            "Иван Иванов", "Марья Иванова", "Петр Иванов", "Антон Иванов",
            "Даша Иванова", "Борис Иванов", "Костя Иванов",
            "Игорь Иванов", "Анна Иванова", "Денис Иванов", "Андрей Иванов"
    };
    final String ATTRIBUTE_NAME_TEXT = "name";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friends = (ListView)findViewById(R.id.listFriends);
        friends.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//нельзя выбирать несколько пунктов
        friends.setOnItemClickListener(this);
        friends.setOnScrollListener(this);

        ArrayList<Map<String,Object>> friendsData = new ArrayList<>();
        Map<String,Object> friendData;

        for(int i=0;i<names.length;i++){
            friendData = new HashMap<>();
            friendData.put(ATTRIBUTE_NAME_TEXT, names[i]);//нужно будет доставать из вк
            friendData.put(ATTRIBUTE_NAME_IMAGE, R.drawable.img_default);

            friendsData.add(friendData);
        }

        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE };
        int[] to = { R.id.tvText, R.id.ivImg };

        SimpleAdapter adapter = new SimpleAdapter(this,friendsData,R.layout.item_friend,from,to);
        friends.setAdapter(adapter);
    }

    @Override//действие на нажатие на пункт
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override//обработка состояний прокрутки (закончили,начали прокрутку и просто прокрутили)
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override//обработка прокрутки (сколько пунктов видно на экране...)
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}
