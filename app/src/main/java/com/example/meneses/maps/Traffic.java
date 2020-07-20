package com.example.meneses.maps;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.RotaController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.widget.Toast;

import com.example.meneses.loginform.R;

import java.util.ArrayList;
import java.util.List;

public class Traffic extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner origin_spinner;
    Spinner dest_spinner;
    List<Rota> rotaList = new ArrayList<>();
    Rota rota;
    RotaController rotaController;
    SQLiteDatabase connection;
    DatabaseHelper databaseHelper;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        origin_spinner = findViewById(R.id.originList_spinner);
        dest_spinner = findViewById(R.id.destList_spinner);

        databaseHelper = new DatabaseHelper(this);

        origin_spinner.setOnItemSelectedListener(this);
        dest_spinner.setOnItemSelectedListener(this);

        searchBtn = findViewById(R.id.btn_searchBus);

        setSpinnerValues();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(origin_spinner.getSelectedItem()!=null && dest_spinner.getSelectedItem()!=null){
                    Intent intent = new Intent(Traffic.this,Location.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext()
                            , "Routes not registered! Can't move to next step", Toast.LENGTH_LONG).show();
                }
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Traffic.this,Location.class);
                startActivity(intent);
            }
        });

        setSpinnerValues();
    }

    public void setSpinnerValues(){
        List<String> originList = new ArrayList<>();
        List<String> destList = new ArrayList<>();

        connection = databaseHelper.getReadableDatabase();
        rotaController = new RotaController(connection);

        rotaList = rotaController.fetchAll();

        for(Rota rota: rotaList){
            if(!originList.contains(rota.getOrigem())) {
                originList.add(rota.getOrigem());
            }
            if(!destList.contains(rota.getDestino())){
                destList.add(rota.getDestino());
            }
        }

        ArrayAdapter<String> originArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, originList);
        ArrayAdapter<String> destArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, destList);

        originArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        origin_spinner.setAdapter(originArrayAdapter);
        dest_spinner.setAdapter(destArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
