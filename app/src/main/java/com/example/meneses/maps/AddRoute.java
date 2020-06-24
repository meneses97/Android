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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meneses.loginform.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class AddRoute extends AppCompatActivity {
    Button btn_addroute;
    EditText origin;
    EditText destination;
    RotaController rotaController;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get bundled parameters
        getParameters();

        btn_addroute = findViewById(R.id.addNewRoute_btn);
        origin = (EditText) findViewById(R.id.id_originField);
        destination = (EditText) findViewById(R.id.id_destField);

        databaseHelper = new DatabaseHelper(this);

        btn_addroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String originF, destF;
                originF = origin.getText().toString();
                destF = destination.getText().toString();

                sqLiteDatabase = databaseHelper.getReadableDatabase();
                rotaController = new RotaController(sqLiteDatabase);

                if(rotaController.fetchOne(originF, destF)!=null){
                    Toast.makeText(getApplicationContext(), "Rota ja existe!", Toast.LENGTH_LONG).show();
                }

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

    private void getParameters(){
        Bundle bundle = getIntent().getExtras();
        String str;
        String[] strings;

        if(bundle!=null && bundle.containsKey("ROTA")){
            str = bundle.getString("ROTA");
            strings = str.split("#");

            origin.setText(strings[0]);
            destination.setText(strings[1]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

}
