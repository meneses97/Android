package com.example.meneses.loginform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meneses.entities.Rota;
import com.example.meneses.maps.AddRoute;
import com.example.meneses.maps.RouteAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListingRoutes1 extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference rotas = mDb.collection("routes");
    private RouteAdapter adapter;
    private FloatingActionButton addRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_routes1);

        addRoute = findViewById(R.id.btn_add_route);
        addRoute.hide();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            mDb.collection("admin").whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){

                            if(document.getData().get("email").toString().equals(user.getEmail())){
                                addRoute.show();
                            }
                            break;

                        }
                    }
                }
            });

        }
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListingRoutes1.this,AddRoute.class);
                startActivity(intent);
                finish();
            }
        });
        setUpRecycler_view();

    }

    public void setUpRecycler_view(){

        Query query = rotas.orderBy("origem",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Rota> options = new FirestoreRecyclerOptions.Builder<Rota>()
                .setQuery(query,Rota.class)
                .build();

        adapter = new RouteAdapter(options);

       final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            mDb.collection("admin").whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){

                            if(document.getData().get("email").toString().equals(user.getEmail())){
                                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                                    @Override
                                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                        return false;
                                    }

                                    @Override
                                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                                        adapter.deleteItem(viewHolder.getAdapterPosition());
                                    }
                                }).attachToRecyclerView(recyclerView);
                            }
                            break;

                        }
                    }
                }
            });

        }

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//
//                adapter.deleteItem(viewHolder.getAdapterPosition());
//            }
//        }).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
