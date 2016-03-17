package com.example.stalker.mapfriends.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stalker.mapfriends.R;

/**
 * Created by stalker on 13.03.16.
 */
public class SplashFragment extends Fragment {
    @Override//что отрисовать во фрагменте
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_splash,null);
    }
}
