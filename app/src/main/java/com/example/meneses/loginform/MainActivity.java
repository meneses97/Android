package com.example.meneses.loginform;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meneses.controller.UserController;
import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.User;
import com.example.meneses.tab.TabActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements Serializable {


    Button login;
    GoogleSignInClient mGoogleSignInClient;
    TextView userEmail,password;
    Boolean mLocationPermissionGranted;
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    UserController userController;
    SQLiteDatabase connection;
    DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private long backPressedTime;

    private ProgressDialog mProgress;
    private FirebaseFirestore mDb;
    boolean pilot = false;
    User eUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        setUpViews();
        getLocationPermission();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mDb = FirebaseFirestore.getInstance();
        eUser = new User();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                String passwd = password.getText().toString();

                if (TextUtils.isEmpty(email)){

                    userEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(passwd)){
                    password.setError("Password is Required");
                    return;
                }
                mProgress.show();
                mAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(MainActivity.this,"Logged in successfuly",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),TabActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this,"Error! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
        });

        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Tasks.class));
            }
        });
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });



    }

    public void saveUser(){

        if(eUser != null){
            DocumentReference locationRef = mDb.collection("users")
                    .document(FirebaseAuth.getInstance().getUid());
            locationRef.set(eUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d("","User saved!"+eUser.getName());
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (backPressedTime +2000>System.currentTimeMillis()){
//            backPressedTime=0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }else {

            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });

    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void signIn() {
//        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();


        startActivityForResult(signInIntent, 1);
        mProgress.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Success", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            CollectionReference citiesRef = mDb.collection("users");

// Create a query against the collection.

                            mDb.collection("users").whereEqualTo("email",user.getEmail())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if(user.getEmail().equals(document.getData().get("email").toString())){
                                                        pilot = true;
                                                    }
                                                    Log.d("", pilot + " => " + document.getData().get("email"));
                                                }
                                                if (pilot == false) {
                                                    Log.d("", pilot+"");
                                                    String name = user.getDisplayName();
                                                    String email = user.getEmail();


                                                    Map<String, Object> mUser = new HashMap<>();
                                                    mUser.put("email", email);
                                                    mUser.put("name", name);

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
                                                }
                                            } else {
                                                Log.w("", "Error getting documents.", task.getException());
                                            }
                                        }
                                    });
//                            Query emails = mDb.collection("users").whereEqualTo("email", true);


//                            if (pilot == false) {
//                                String name = user.getDisplayName();
//                                String email = user.getEmail();
//
//
//                                Map<String, Object> mUser = new HashMap<>();
//                                mUser.put("email", email);
//                                mUser.put("name", name);
//
//// Add a new document with a generated ID
//                                mDb.collection("users")
//                                        .add(mUser)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w("", "Error adding document", e);
//                                            }
//                                        });
//                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Error", "Google sign in failed", e);
                // ...
            }
        }
    }



    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();

//            Intent intent = new Intent(MainActivity.this,TabActivity.class);
            Intent intent = new Intent(MainActivity.this,TabActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }

    public void setUpViews() {

        userEmail = findViewById(R.id.username);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.login);

    }


}
