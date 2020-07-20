package com.example.meneses.loginform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.meneses.controller.UserController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.User;
import com.example.meneses.tab.TabActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Tasks extends AppCompatActivity {
    EditText userName,userPassword,verPassword,userEmail,carId,matId,licId;
    TextView userLogin;
    Button regUser;
    private ProgressDialog mProgress;
    User user;
    private FirebaseFirestore mDb;

    UserController userController;
    SQLiteDatabase connection;
    DatabaseHelper databaseHelper;
    FirebaseAuth firebaseAuth;
    boolean pilot = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        databaseHelper = new DatabaseHelper(this);
        connection = databaseHelper.getWritableDatabase();
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        userController = new UserController(connection);

        setUpViews();

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        user = new User();
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tasks.this,MainActivity.class));
            }
        });

        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = userName.getText().toString();
                String password = userPassword.getText().toString();
                final String email = userEmail.getText().toString();
                String pass2 = verPassword.getText().toString();

                final String car =  carId.getText().toString();
                final String matricula = matId.getText().toString();
                final String lic = licId.getText().toString();



                if (TextUtils.isEmpty(name)) {

                    userName.setError("Full name is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {

                    userEmail.setError("Email is Required");
                    return;
                }


                if (TextUtils.isEmpty(password)) {
                    userPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6) {

                    userPassword.setError("Password must be more than 5 characters");
                    return;
                }

                if (!password.equals(pass2)) {
                    verPassword.setError("Password must be the same");
                    return;
                }

                if(TextUtils.isEmpty(car)){
                    carId.setError("Required!");
                }
                if(TextUtils.isEmpty(matricula)){
                    carId.setError("Required!");
                }
                if(TextUtils.isEmpty(lic)){
                    carId.setError("Required!");
                }
                mProgress.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Tasks.this, "User Created.", Toast.LENGTH_SHORT).show();

                            Map<String, Object> mUser = new HashMap<>();
                            mUser.put("email", email);
                            mUser.put("name", name);
                            mUser.put("matricula",matricula);
                            mUser.put("marca",car);
                            mUser.put("licenca",lic);

// Add a new document with a generated ID
                            mDb.collection("users")
                                    .add(mUser)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("", "Error adding document", e);
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));


                        } else {
                            Toast.makeText(Tasks.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgress.setCanceledOnTouchOutside(true);
                            mProgress.setCancelable(true);

                        }
                    }
                });




            }
        });

    }


    public void saveUser(){

        if(user != null){
            DocumentReference locationRef = mDb.collection("users")
                    .document(FirebaseAuth.getInstance().getUid());
            locationRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d("","User saved!");
                    }
                }
            });
        }

    }



    public void setUpViews() {

        userName = findViewById(R.id.name);
        userPassword = findViewById(R.id.pass);
        userEmail = findViewById(R.id.email);
        userLogin = findViewById(R.id.tvUserLogin);
        regUser = findViewById(R.id.btRegister);
        verPassword = findViewById(R.id.password2);
        carId = findViewById(R.id.carid);
        matId = findViewById(R.id.matId);
        licId = findViewById(R.id.licId);

    }
}
