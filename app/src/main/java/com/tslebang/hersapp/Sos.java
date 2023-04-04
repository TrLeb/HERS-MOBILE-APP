

package com.tslebang.hersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class Sos extends AppCompatActivity implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationClient;
    private final int REQUEST_CODE = 11;

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Button sosBtn;
    TextView sosNumTv;
    private static final String SOS_MESSAGE = "HELP! I am in danger. Please help me. My current location: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        sosBtn = findViewById(R.id.sosBtn);
        sosNumTv = findViewById(R.id.sosNum);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest().create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location locationr : locationResult.getLocations()) {
                    // Get device location here
                }
            }
        };
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        askLocationPermission();
//                    }else {
//                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                // Send SOS message here
////                sendSMS(SOS_MESSAGE + "latitude: " + locationr.getLatitude() + ", longitude: " + location.getLongitude());
//                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getLastLocation();
        }else {
            askLocationPermission();
        }
    }

    private void sendSMS(String message, String phoneNumber) {
        phoneNumber = sosNumTv.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
    private void getLastLocation(){

    }private void askLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getLastLocation();
        }else {
            askLocationPermission();
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}