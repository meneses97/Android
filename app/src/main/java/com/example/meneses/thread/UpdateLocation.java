package com.example.meneses.thread;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.meneses.entities.Car;
import com.example.meneses.entities.Rota;
import com.example.meneses.entities.User;
import com.example.meneses.entities.UserLocation;
import com.example.meneses.weather.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static java.lang.Thread.sleep;

public class UpdateLocation extends Fragment implements Runnable {

    long miniTime;
    private UserLocation userLocation;
    private FirebaseFirestore mDb;
    private Activity activity;
    LatLng lat;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    public UpdateLocation() {
    }


    @SuppressLint("ValidFragment")
    public UpdateLocation(long time, Activity activity){
        this.activity = activity;
        miniTime = time;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mDb = FirebaseFirestore.getInstance();

    }






    public void saveUserLocation(){

        if(userLocation != null){

            try {
                DocumentReference locationRef = mDb.collection("User Locations")
                        .document(FirebaseAuth.getInstance().getUid());
                locationRef.update("geoPoint",lat,"timestamp",new GregorianCalendar().getTime()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("","Updated!");
                        }else{
                            DocumentReference locationRef = mDb.collection("User Locations")
                                    .document(FirebaseAuth.getInstance().getUid());
                            locationRef.set(userLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d("","saved!");
                                    }
                                }
                            });
                        }
                    }
                });
            }catch (Exception e){

            }





        }

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                    lat = new LatLng(location.getLatitude(),location.getLongitude());
                                    if (userLocation==null){
                                        userLocation = new UserLocation();}
                                    userLocation.setGeoPoint(latLng);
                                    userLocation.setTimestamp(new GregorianCalendar().getTime());

                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        String name = user.getDisplayName();
                                        String email = user.getEmail();
//                                        userLocation.setUser(new User(email,name));
                                        mDb.collection("users").whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                String marca="";
                                                String matricula="";
                                                String licenca="";
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document : task.getResult()){

                                                        Map<String,Object> rotaD = (HashMap) document.get("rota");
                                                        Rota rota = new Rota();

                                                        if (rotaD != null){
                                                            rota.setDestino(rotaD.get("destino").toString());
                                                            rota.setOrigem(rotaD.get("origem").toString());

                                                            userLocation.setRota(rota);

                                                        }

                                                        if (document.contains("marca")
                                                                && document.contains("matricula")
                                                                && document.contains("licenca")) {
                                                            marca = document.getData().get("marca").toString();
                                                            matricula = document.getData().get("matricula").toString();
                                                            licenca = document.getData().get("licenca").toString();

                                                            Car car = new Car();
                                                            car.setMarca(marca);
                                                            car.setMatricula(matricula);
                                                            car.setLicenca(licenca);
                                                            userLocation.setCar(car);
                                                            userLocation.setUser(new User(user.getEmail(),document.getData().get("name").toString()));

                                                        }else{
                                                            userLocation.setUser(new User(user.getEmail(),user.getDisplayName()));

                                                        }

                                                        break;

                                                    }

                                                }
                                            }
                                        });

                                    }



                                    saveUserLocation();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(getContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.getApplicationContext());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };

    @Override
    public void run() {
        getLastLocation();
        try {
            sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        run();
    }
}
