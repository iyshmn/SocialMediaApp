package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialmediaapp.adapter.UserAdapter;
import com.example.socialmediaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private UserAdapter uAdapter;
    private ArrayList<User> mUserArrayList;
    ImageView imgLogout;
    ImageView imgSetting;
    FirebaseDatabase database;
    String status = "Online";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.mUserArrayList = new ArrayList<>();
        this.database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Users");
        this.uAdapter = new UserAdapter(ChatActivity.this,mUserArrayList);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User users = dataSnapshot.getValue(User.class);
                    if (FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString().equals(users.getId())) {
                        users.setStatus("Online");
                    }
                    mUserArrayList.add(users);
                }
                uAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.auth = FirebaseAuth.getInstance();
        mRecyclerView = (RecyclerView)findViewById(R.id.mainUserRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mRecyclerView.setAdapter(uAdapter);
        imgLogout = findViewById(R.id.img_logOut);
        imgSetting = findViewById(R.id.img_Setting);
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ChatActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setContentView(R.layout.dialog_layout);
                TextView yesBtn, noBtn;

                yesBtn = dialog.findViewById(R.id.yesBtn);
                noBtn = dialog.findViewById(R.id.noBtn);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ChatActivity.this,loginActivity.class));
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }
}