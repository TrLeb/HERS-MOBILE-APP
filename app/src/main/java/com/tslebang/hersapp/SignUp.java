package com.tslebang.hersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    EditText mEmailEt, mPasswordEt,mNameEt, mPhoneEt ;
    Button mRegisterBtn;
    TextView haveAcc;
    //progress bar
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //initializzzzzzzzzzzzzzzzzzzzzz
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mNameEt = findViewById(R.id.nameEt);
        mPhoneEt = findViewById(R.id.phoneEt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        haveAcc = findViewById(R.id.haveAcc);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User");

        //handle button clicks
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                String name= mNameEt.getText().toString().trim();
                String phone= mPhoneEt.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmailEt.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    mPasswordEt.setError("Password is Required.");
                    return;
                }
                //VALIDATE
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                }
                else if(password.length()<6){
                    mPasswordEt.setError("Password too short: should be 6+");
                    mPasswordEt.setFocusable(true);
                }
                else {
                    registerUser(email, password);
                }
            }


        });
        haveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent haveAcc = new Intent(SignUp.this, LogIn.class);
                startActivity(haveAcc);
            }
        });
        
    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();


                            //get user details
                            String email=user.getEmail();
                            String uid = user.getUid();
                            String name = mNameEt.getText().toString();
                            String phone = mPhoneEt.getText().toString();
                            //when user is registered store their details on firebase realtime database using hashmap
                            HashMap<Object,String> hashMap=new HashMap<>();
                            // put info into hash map
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", phone);

                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //path to store user data
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(SignUp.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, Dashboard.class));
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Authentication Falled", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}