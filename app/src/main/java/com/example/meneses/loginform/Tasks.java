package com.example.meneses.loginform;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.UserController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.User;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tasks extends AppCompatActivity {
    EditText userName,userPassword,userEmail;
    TextView userLogin;
    Button regUser;

    UserController userController;
    SQLiteDatabase connection;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        databaseHelper = new DatabaseHelper(this);
        connection = databaseHelper.getWritableDatabase();

        userController = new UserController(connection);

        setUpViews();

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tasks.this,MainActivity.class));
            }
        });

        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){

                    String name = userName.getText().toString();
                    String password = userPassword.getText().toString();
                    String email = userEmail.getText().toString();

                    User user = new User(email, name, password);

                    Boolean checkemail = userController.checkemail(email);

                    if (checkemail){

                        Boolean insert = userController.insertUser(user);
                        if (insert){

                            Toast.makeText(getApplicationContext(),"Registado com Sucesso",Toast.LENGTH_SHORT).show();

                        }
                            else{
                            Toast.makeText(getApplicationContext(),"Nao foi possivel!",Toast.LENGTH_SHORT).show();
                        }


                    } else{
                        Toast.makeText(getApplicationContext(),"O email ja existe!",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private Boolean validate(){

        Boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty()){

            Toast.makeText(this,"Por favor! Introduza todos detalhes,",Toast.LENGTH_SHORT).show();

        }else {

            result = true;
        }
        return result;
    }


    public void setUpViews() {

        userName = findViewById(R.id.name);
        userPassword = findViewById(R.id.password);
        userEmail = findViewById(R.id.email);
        userLogin = findViewById(R.id.tvUserLogin);
        regUser = findViewById(R.id.btRegister);

    }
}
