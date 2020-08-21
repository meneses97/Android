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

import android.widget.TextView;
import android.widget.Toast;

import com.example.meneses.loginform.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Traffic extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner origin_spinner;
    Spinner dest_spinner;
    List<Rota> rotaList = new ArrayList<>();
    RotaController rotaController;
    SQLiteDatabase connection;
    DatabaseHelper databaseHelper;
    Button searchBtn;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference rotas = mDb.collection("routes");
    private RouteAdapter adapter;
    TextView origin,destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        origin = findViewById(R.id.origin);
        destination = findViewById(R.id.destination);

//        origin_spinner = findViewById(R.id.originList_spinner);
//        dest_spinner = findViewById(R.id.destList_spinner);
//
//        databaseHelper = new DatabaseHelper(this);
//
//        origin_spinner.setOnItemSelectedListener(this);
//        dest_spinner.setOnItemSelectedListener(this);

        searchBtn = findViewById(R.id.btn_searchBus);

//        setSpinnerValues();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle bundle = new Bundle();

                    Intent intent = new Intent(Traffic.this,Location.class);

//                    bundle.putString("rota", origin_spinner.getSelectedItem().toString()+
//                            "-"+dest_spinner.getSelectedItem().toString());

                    bundle.putString("rota", origin.getText().toString()+
                            "-"+destination.getText().toString());

                    intent.putExtras(bundle);

                    startActivity(intent);
//                }else{
//                    Toast.makeText(getApplicationContext()
//                            , "Routes not registered! Can't move to next step", Toast.LENGTH_LONG).show();
//                }
            }
        });

//        setSpinnerValues();
    }

//    public void setSpinnerValues(){
//
//
//        Query query = rotas.orderBy("origem",Query.Direction.ASCENDING);
//        FirestoreRecyclerOptions<Rota> options = new FirestoreRecyclerOptions.Builder<Rota>()
//                .setQuery(query,Rota.class)
//                .build();
//
//        adapter = new RouteAdapter(options);
//
//        List<String> originList = new ArrayList<>();
//        List<String> destList = new ArrayList<>();
//
//        List<Rota> routes = adapter.getSnapshots();
//
//        connection = databaseHelper.getReadableDatabase();
//        rotaController = new RotaController(connection);
//
////        rotaList = rotaController.fetchAll();
////        for(Rota rota: rotaList){
////            if(!originList.contains(rota.getOrigem())) {
////                originList.add(rota.getOrigem());
////            }
////            if(!destList.contains(rota.getDestino())){
////                destList.add(rota.getDestino());
////            }
////        }
//
//        rotaList = rotaController.fetchAll();
//        for(Rota rota: routes){
//
//                originList.add(rota.getOrigem());
//                destList.add(rota.getDestino());
//        }
//
//        ArrayAdapter<String> originArrayAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, originList);
//        ArrayAdapter<String> destArrayAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, destList);
//
//        originArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        destArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        origin_spinner.setAdapter(originArrayAdapter);
//        dest_spinner.setAdapter(destArrayAdapter);
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
