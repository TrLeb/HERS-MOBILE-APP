package com.tslebang.hersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView dTv;

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    ImageView donateIV, requestIV, callIV, qnaIV, sosIV, locationIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        dTv = findViewById(R.id.dashTv);


        donateIV = findViewById(R.id.donateIV);
        requestIV = findViewById(R.id.requestIV);
        callIV = findViewById(R.id.callIV);
        locationIV = findViewById(R.id.clinicsIV);
        qnaIV = findViewById(R.id.qnaIV);
        sosIV = findViewById(R.id.sosIV);

        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String name = ""+ ds.child("name").getValue();

                    dTv.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "There is no active user", Toast.LENGTH_SHORT).show();
            }
        });

        //ONCLICK LISTENERS
        requestIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Request.class));

            }
        });
        donateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Donate.class));

            }
        });
        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, NearbyClinics.class));

            }
        });

        sosIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Sos.class));
            }
        });

        qnaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(new Intent(Dashboard.this, ChatActivity.class));
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogout){

            mAuth.signOut();
            finish();
            startActivity(new Intent(Dashboard.this, MainActivity.class));
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(Dashboard.this, Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkUserStatus(){
        FirebaseUser user =mAuth.getCurrentUser();
        if (user !=null){
            //stay here
            dTv.setText(user.getDisplayName());

        }else{
            startActivity(new Intent(Dashboard.this, MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}