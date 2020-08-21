package com.example.meneses.loginform;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.ModalBottomSheet;
import com.example.meneses.controller.RotaController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;
import com.example.meneses.tab.TabActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListingRoutes extends AppCompatActivity {
    RotaController rotaController;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    private FirebaseFirestore mDb;
   List<Rota> rotas ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        rotaController = new RotaController(sqLiteDatabase);
        mDb = FirebaseFirestore.getInstance();
        rotas = new ArrayList<>();
        ListView routesList = (ListView) findViewById(R.id.routesList_id);
        fetchAll();

        Log.d("",rotas.size()+"*****77777777777777");
        final ArrayAdapter<Rota> rotaArrayAdapter = new ArrayAdapter<Rota>(this,
                android.R.layout.simple_list_item_1, rotas);
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

    public void fetchAll(){


        Query query = FirebaseFirestore.getInstance()
                .collection("routes");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<Rota> chats = snapshot.toObjects(Rota.class);
                Log.d("",chats.size()+"++++++++++++++++++++++++++++++");
                update(chats);
                // Update UI
                // ...
            }
        });
//        mDb.collection("routes")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                               Rota rota = document.toObject(Rota.class);
//                                Log.d("",document.get("destino").toString()+"**************************");
//                                Log.d("",rota.getOrigem());
//                               rotas.add(rota);
//                               Log.d("",rotas.size()+"------------------------------");
//                            }
//                        } else {
//                            Log.w("", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }

    public void update(List<Rota> rotas){

        this.rotas = rotas;
        for (Rota rota: rotas)
        Log.d("",rota.getOrigem()+"++++++++++++++++++++++++++++++");
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }
}
