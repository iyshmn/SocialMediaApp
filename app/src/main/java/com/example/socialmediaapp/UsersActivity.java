package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.socialmediaapp.adapter.PhotoAdapater;
import com.example.socialmediaapp.fragment.GalleryFragment;
import com.example.socialmediaapp.model.Post;
import com.example.socialmediaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private ImageView profilepic;
    private TextView username;
    private TextView bio;
    private ImageView gallery;
    private RelativeLayout rLay;
    private FirebaseUser fUser;
    String uname,bioo,profilee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        username = findViewById(R.id.username);
        profilepic = findViewById(R.id.profile_img1);
        bio = findViewById(R.id.bioBox1);
        gallery = findViewById(R.id.gallary1);
        Bundle intent = getIntent().getExtras();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String profileId = intent.getString("publisherId");

        getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();
        getSupportFragmentManager().beginTransaction().replace(R.id.cl1, new GalleryFragment()).commit();
       // getSupportFragmentManager().beginTransaction().replace(R.id , new GalleryFragment()).commit();




    }


}