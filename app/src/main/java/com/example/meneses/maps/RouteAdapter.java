package com.example.meneses.maps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meneses.entities.Rota;
import com.example.meneses.loginform.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RouteAdapter extends FirestoreRecyclerAdapter<Rota,RouteAdapter.RouteHolder> {




    public RouteAdapter(@NonNull FirestoreRecyclerOptions<Rota> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RouteHolder holder, int position, @NonNull Rota model) {
        holder.route.setText(model.getOrigem()+" - "+model.getDestino());
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new RouteHolder(view);
    }

    public void deleteItem(int position){

        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class RouteHolder extends RecyclerView.ViewHolder{
        TextView route;

        public RouteHolder(@NonNull View itemView) {
            super(itemView);
            route = itemView.findViewById(R.id.route);
        }
    }
}
