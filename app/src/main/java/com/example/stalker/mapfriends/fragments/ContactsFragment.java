package com.example.stalker.mapfriends.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.stalker.mapfriends.R;


public class ContactsFragment extends Fragment {

    @Override//что отрисовать во фрагменте
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_contacts, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Button btnIlya = (Button)getView().findViewById(R.id.btnIlya);
        Button btnZhenya = (Button)getView().findViewById(R.id.btnZhenya);

        // создаем обработчик нажатия
        View.OnClickListener oclBtnIlya = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/iproha94"));
                startActivity(browserIntent);
            }
        };

        // создаем обработчик нажатия
        View.OnClickListener oclBtnZhenya = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id72663816"));
                startActivity(browserIntent);
            }
        };

        // присвоим обработчик кнопке OK (btnOk)
        btnIlya.setOnClickListener(oclBtnIlya);
        btnZhenya.setOnClickListener(oclBtnZhenya);
    }


}

