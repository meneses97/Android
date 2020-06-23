package com.example.meneses.maps;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.RotaController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;
import com.example.meneses.loginform.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meneses.loginform.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class AddRoute extends AppCompatActivity {
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

        btn_addroute = findViewById(R.id.addNewRoute_btn);
        origin = findViewById(R.id.id_originField);
        destination = findViewById(R.id.id_destField);

        databaseHelper = new DatabaseHelper(this);

        btn_addroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String originF, destF;
                originF = origin.getText().toString();
                destF = destination.getText().toString();

                if(!originF.equals("") && !destF.equals("")) {
                    sqLiteDatabase = databaseHelper.getWritableDatabase();
                    rotaController = new RotaController(sqLiteDatabase);
                    rotaController.insert(originF, destF);

                    Toast.makeText(getApplicationContext(), "Adicionou com sucesso!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Campos vazios!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

}
