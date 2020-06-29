package com.example.meneses.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;
import com.example.meneses.loginform.ListingRoutes;
import com.example.meneses.loginform.R;
import com.example.meneses.maps.AddRoute;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ModalBottomSheet extends BottomSheetDialogFragment {
    String str;
    String[] strings;
    Rota rota;
    RotaController rotaController;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_content, container, false);
        Button btn_remove = view.findViewById(R.id.btn_remove);
        Button btn_edit = view.findViewById(R.id.btn_edit);
        Bundle bundle = getArguments();
        String str;
        String[] strings;

        //buscando rota da actividade ListingRoutes
        str = bundle.getString("Rota");
        strings = str.split("#");

        databaseHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        rotaController = new RotaController(sqLiteDatabase);

        rota = rotaController.fetchOne(strings[0], strings[1]);

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase = databaseHelper.getWritableDatabase();
                rotaController = new RotaController(sqLiteDatabase);

                rotaController.remove(rota);
                Intent intent = new Intent(getContext(), ListingRoutes.class);
                startActivityForResult(intent, 0);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("ROTA", rota.getOrigem()+"#"+rota.getDestino());

                Intent intent = new Intent(getContext(), AddRoute.class);
                intent.putExtras(bundle1);

                startActivityForResult(intent,0);
            }
        });

        return view;
    }
}
