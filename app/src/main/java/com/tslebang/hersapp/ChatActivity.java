package com.tslebang.hersapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tslebang.hersapp.adapters.ChatAdapter;
import com.tslebang.hersapp.models.AllMethods;
import com.tslebang.hersapp.models.Message;
import com.tslebang.hersapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference messagedb;
    FirebaseAuth mAuth;
    ChatAdapter messageAdapter;
    User user;
    List<Message> messageList;

    RecyclerView rvMessage;
    EditText messageEt;
    ImageButton sendBtn;


    //for checking if user saw messAGE
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvMessage = findViewById(R.id.rvMessage);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.imageBtn);
        user = new User();
        messageList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(messageEt.getText().toString())){
                    Message message = new Message(messageEt.getText().toString(),user.getName());
                    messageEt.setText("");
                    messagedb.push().setValue(message);
                }else{
                    Toast.makeText(ChatActivity.this, "you can not send an empty message", Toast.LENGTH_SHORT).show();
                }

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
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(ChatActivity.this, Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            System.out.print("User available=================================================");
        }else{
            System.out.print("no User available=================================================");

        }

        user.setUid(currentUser.getUid());
        user.setEmail(currentUser.getEmail());

        firebaseDatabase.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.setUid(currentUser.getUid());
                AllMethods.name = user.getName();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = firebaseDatabase.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messageList.add(message);
                displayMessages(messageList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for (Message m: messageList){
                    if (m.getKey().equals(message.getKey())){
                        newMessages.add(message);

                    }else{
                        newMessages.add(m);
                    }
                }
                messageList = newMessages;
                displayMessages(messageList);
//                messageList.add(message);
//                displayMessages(messageList);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for (Message m: messageList){
                    if (!m.getKey().equals(message.getKey())){
                        newMessages.add(message);

                    }else{
                        newMessages.add(m);
                    }
                }
                messageList = newMessages;
                displayMessages(messageList);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();
        messageList = new ArrayList<>();
    }

    private void displayMessages(List<Message> messageList) {
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        messageAdapter = new ChatAdapter(ChatActivity.this,messageList,messagedb);
        rvMessage.setAdapter(messageAdapter);
    }
}
