package com.example.meneses.maps;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.RotaController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;
import com.example.meneses.loginform.ListingRoutes;
import com.example.meneses.loginform.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meneses.loginform.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class AddRoute extends AppCompatActivity {
    Intent intent;
    String originF, destF;
    int pilot, idrota;
    Rota rota;
    Button btn_addroute;
    TextView origin;
    TextView destination;
    RotaController rotaController;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pilot = 0;
        btn_addroute = findViewById(R.id.addNewRoute_btn);
        origin = findViewById(R.id.id_originField);
        destination = findViewById(R.id.id_destField);

        databaseHelper = new DatabaseHelper(this);
        verifyParameters();

        btn_addroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOperation();
            }
        });

    }

    private void verifyParameters(){
        String str;
        String[] strings;
        Bundle bundle = getIntent().getExtras();

        sqLiteDatabase = databaseHelper.getReadableDatabase();
        rotaController = new RotaController(sqLiteDatabase);

        if(bundle!=null && bundle.containsKey("ROTA")){
            str = bundle.getString("ROTA");
            strings = str.split("#");
            rota = rotaController.fetchOne(strings[0], strings[1]);
            idrota = rota.getIdrota();

            origin.setText(rota.getOrigem());
            destination.setText(rota.getDestino());

            pilot = 1;
        }
    }

    private void confirmOperation(){
        originF = origin.getText().toString();
        destF = destination.getText().toString();
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        rotaController = new RotaController(sqLiteDatabase);

        if(pilot == 1){
            rotaController.edit(idrota, originF, destF);

            Toast.makeText(getApplicationContext(), "Editted successfully!", Toast.LENGTH_LONG).show();

            Toast.makeText(getApplicationContext(), "Rota editada com sucesso!", Toast.LENGTH_LONG).show();

            intent = new Intent(this, ListingRoutes.class);
            startActivityForResult(intent, 0);
        }else{
            if(rotaController.fetchOne(originF, destF) != null){

                Toast.makeText(getApplicationContext(), "Route already exists!", Toast.LENGTH_LONG).show();
            }
            if(!isEmptyField(originF) && !isEmptyField(destF)){
                rotaController.insert(originF, destF);
                Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Rota ja existe!", Toast.LENGTH_LONG).show();
            }
            if(!isEmptyField(originF) && !isEmptyField(destF)){
                rotaController.insert(originF, destF);
                Toast.makeText(getApplicationContext(), "Inserido com sucesso!", Toast.LENGTH_LONG).show();

                intent = new Intent(this, ListingRoutes.class);
                startActivityForResult(intent, 0);
            }else {
                Toast.makeText(getApplicationContext(), "Empty fields!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Campo(s) vazio(s)!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isEmptyField(String value){
        boolean result = (TextUtils.isEmpty(value) || value.trim().isEmpty());

        return result;
    }
}
