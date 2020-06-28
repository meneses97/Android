package com.example.meneses.loginform;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.ModalBottomSheet;
import com.example.meneses.controller.RotaController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;
import com.example.meneses.tab.TabActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;

public class ListingRoutes extends AppCompatActivity {
    RotaController rotaController;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        rotaController = new RotaController(sqLiteDatabase);

        ListView routesList = (ListView) findViewById(R.id.routesList_id);

        final ArrayAdapter<Rota> rotaArrayAdapter = new ArrayAdapter<Rota>(this,
                android.R.layout.simple_list_item_1, rotaController.fetchAll());
        routesList.setAdapter(rotaArrayAdapter);

        routesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rota rota = (Rota) parent.getItemAtPosition(position);
                ModalBottomSheet modalBottomSheet = new ModalBottomSheet();
                Bundle bundle = new Bundle();

                bundle.putString("Rota", rota.getOrigem()+"#"+rota.getDestino());
                modalBottomSheet.setArguments(bundle);

                modalBottomSheet.show(getSupportFragmentManager(), "modalMenu");
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }
}
