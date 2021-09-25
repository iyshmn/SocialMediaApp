package com.example.socialmediaapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.PhotoAdapater;
import com.example.socialmediaapp.model.Post;
import com.example.socialmediaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PhotoAdapater photoAdapter;
    private ViewPager2 mViewPager2;
    private List<Post> gallery;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser fUser;
    String profileId;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);
        this.mRecyclerView =(RecyclerView) view.findViewById(R.id.recycler_constraint);
        this.mViewPager2 = view.findViewById(R.id.vertical);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        gallery = new ArrayList<>();
        photoAdapter =  new PhotoAdapater(getContext(),gallery);
       mRecyclerView.setAdapter(photoAdapter);
      //  this.mViewPager2.setAdapter(photoAdapter);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");
        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }
        myphotos();

        return view;
    }

    private void myphotos() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gallery.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Post post = snapshot1.getValue(Post.class);
                    if(post.getPublisher().equals(profileId)){
                        gallery.add(post);
                    }
                }
                Collections.reverse(gallery);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}