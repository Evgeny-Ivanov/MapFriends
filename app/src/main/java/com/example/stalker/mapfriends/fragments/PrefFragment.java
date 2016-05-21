package com.example.stalker.mapfriends.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.example.stalker.mapfriends.R;
import com.example.stalker.mapfriends.SplashActivity;
import com.vk.sdk.VKSdk;


public class PrefFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_pref);

        Preference pref = findPreference("exit_vk_key");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                VKSdk.logout();
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            }
        });
    }

}
