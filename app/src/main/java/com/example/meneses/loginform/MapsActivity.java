package com.example.meneses.loginform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener
       {

    private GoogleMap mMap;;
    MapView mMapView;
    View mView;

    public MapsActivity(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

           @Nullable
           @Override
           public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
               mView = inflater.inflate(R.layout.activity_maps,container,false);

               return mView;
           }

           @Override
           public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
               super.onViewCreated(view, savedInstanceState);
               mMapView = (MapView) mView.findViewById(R.id.map);
               if (mMapView!=null){
                   mMapView.onCreate(null);
                   mMapView.onResume();
                   mMapView.getMapAsync(this);
               }
           }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);

        }

    }

           @Override
           public boolean onMyLocationButtonClick() {
               return false;
           }

           @Override
           public void onMyLocationClick(@NonNull Location location) {
               Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
           }
       }
