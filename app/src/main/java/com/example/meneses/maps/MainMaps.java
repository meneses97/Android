package com.example.meneses.maps;

import android.content.Intent;
import android.os.Bundle;

import com.example.meneses.loginform.ListingRoutes;
import com.example.meneses.thread.UpdateLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meneses.loginform.R;

public class MainMaps extends Fragment {

    Button traffic_button;
    Button timetable_button;
    Button addNewRoute_button;
    Button viewRoutes_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_maps2,container,false);
        timetable_button = view.findViewById(R.id.timetable_btn);
        traffic_button = view.findViewById(R.id.traffic_btn);
        addNewRoute_button = view.findViewById(R.id.btn_addRotas);
        viewRoutes_button = view.findViewById(R.id.btn_viewRotas);

        UpdateLocation updateLocation = new UpdateLocation(143,getActivity());
        new Thread(updateLocation).start();

        traffic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Traffic.class);

                startActivity(intent);
            }
        });

        timetable_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Em construcao!", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getActivity(), Timetable.class);
//
//                startActivity(intent);
            }
        });

        addNewRoute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddRoute.class);

                startActivity(intent);
            }
        });

        viewRoutes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListingRoutes.class);

                startActivity(intent);
            }
        });

        return view;

    }


}
