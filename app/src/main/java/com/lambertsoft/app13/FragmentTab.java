package com.lambertsoft.app13;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by InnovaTI on 06-03-15.
 */

public class FragmentTab extends Fragment {
    final String TAG = FragmentTab.class.getSimpleName();
    final LatLng HOME = new LatLng(-33.4311092,-70.5950772);

    GoogleMap mMap;
    SupportMapFragment fragment;
    Button buttonViewCar;
    TextView textUserName;

   public FragmentTab() {
       super();
   }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        textUserName = (TextView) view.findViewById(R.id.textUserName);
        //textUserName.setText(Id);
        buttonViewCar = (Button) view.findViewById(R.id.buttonViewCar);

        buttonViewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMap == null) {
                    Log.e(TAG, "mMap is null");
                } else {

                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

                    Marker Home = mMap.addMarker(new MarkerOptions().position(HOME).title("Home"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HOME, 10));

                }
            }

        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = fragment.getMap();
            mMap.addMarker(new MarkerOptions().position(HOME));
        }
    }
}

