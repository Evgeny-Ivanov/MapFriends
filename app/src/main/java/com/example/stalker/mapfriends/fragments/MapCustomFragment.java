package com.example.stalker.mapfriends.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.example.stalker.mapfriends.MainApplication;
import com.example.stalker.mapfriends.interfaces.ProgressbarVisibility;
import com.example.stalker.mapfriends.message.DataAndStatusMsg;
import com.example.stalker.mapfriends.model.Coor;
import com.example.stalker.mapfriends.network.CoordinatesServerLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


/**
 * Created by stalker on 02.04.16.
 */

//два основных класса для работы с картой - MapFragment и GoogleMap
public class MapCustomFragment extends MapFragment
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<DataAndStatusMsg> {
    private GoogleMap map;
    public static final String BUNDLE_ID_USER = "idUser";
    private int idUser;
    private int loaderId = 1;//по id через getLoader потом сможем получать Loader


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null){
            //при создании фрагмента данные которые нужно в него передать,
            //перердаем через setArguments()
            //нельзя передавать через конструктор, т.к. android любит удалять фрагменты а потом
            //востанавливать их через дефолтные конструкторы
            idUser = getArguments().getInt(BUNDLE_ID_USER);
        }
        if(getActivity() instanceof ProgressbarVisibility)
            ((ProgressbarVisibility)getActivity()).setVisibleProgressBar();
        getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {//выполниться когда карта будет готова к использованию
        this.map = map;
        LatLng latLngMoscow = new LatLng(55.7522200, 37.6155600);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLngMoscow));
        //restartLoader – создание нового лоадера в любом случае
        //getLoader – просто получение лоадера с указанным ID
        Loader<DataAndStatusMsg> loader = getActivity().getLoaderManager().restartLoader(loaderId, getArguments(), this);//создание лоадера если он не существовал
        loader.forceLoad();
    }



    @Override//вызываетя при создание Loader (при вызове initLoader)
    public Loader<DataAndStatusMsg> onCreateLoader(int id, Bundle args) {
        Log.d(MainApplication.log, "onCreateLoader");
        return new CoordinatesServerLoader(getActivity(),args);
    }

    @Override//срабатывает когда Loader завершил свою работу и вернул результат
    public void onLoadFinished(Loader<DataAndStatusMsg> loader, DataAndStatusMsg data) {
        Log.d(MainApplication.log,"onLoadFinished");
        if(data !=null) {
            List<Coor> coors = data.getCoors();
            for (Coor coor : coors) {
                MarkerOptions markerOptions = new MarkerOptions().title(coor.getDate().toString());
                LatLng latLng = new LatLng(coor.getLatitude(), coor.getLongitude());
                markerOptions.position(latLng);
                map.addMarker(markerOptions);
            }

            if (!coors.isEmpty()) {
                Coor zoomCoor = coors.get(coors.size() - 1);
                float zoom = 10;
                LatLng latLng = new LatLng(zoomCoor.getLatitude(), zoomCoor.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        }

            if(getActivity() instanceof ProgressbarVisibility)
            ((ProgressbarVisibility)getActivity()).setInvisibleProgressBar();
    }

    @Override//вызывается при уничтожении (только в случае, когда хоть раз были получены данные.)
    public void onLoaderReset(Loader<DataAndStatusMsg> loader) {
        Log.d(MainApplication.log,"onLoaderReset");
    }


}
