package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.MessengerActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.UsersActivity;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {
    public SearchUserAdapter(Context context, List<User> users, boolean isFargment) {
        mContext = context;
        mUsers = users;
        this.isFargment = isFargment;
    }

    private Context mContext;
    private List<User> mUsers;
    private List<Post> mPosts;
    private boolean isFargment;
    private FirebaseUser firebaseUser;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new SearchUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(position);


        holder.btnFollow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());
        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
        isFollowed(user.getId(),holder.btnFollow);
        if (user.getId().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(holder.btnFollow.getText().toString().equals(("follow"))){
//                FirebaseDatabase.getInstance().getReference().child("Follow").child((firebaseUser.getUid())).child("Following").child((firebaseUser.getUid())).child("following").child(user.getId()).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow")
                        .child((firebaseUser.getUid())).child("following").child(user.getId()).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow")
                        .child(user.getId()).child("followers").child(firebaseUser.getUid()).setValue(true);
                }else{
                FirebaseDatabase.getInstance().getReference().child("Follow")
                        .child((firebaseUser.getUid())).child("following").child(user.getId()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow")
                        .child(user.getId()).child("followers").child(firebaseUser.getUid()).removeValue();
            }
            }
        });
        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity)v.getContext();
//                Fragment fragment = new GalleryFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.searchRecyclerView,fragment).addToBackStack(null).commit();
                Intent intent = new Intent(mContext, UsersActivity.class);
                intent.putExtra("publisherId", user.getId());
                mContext.startActivity(intent);
                // LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
             //   mContext.startActivity(i);
//              GalleryFragment pf = new GalleryFragment();
//               FragmentTransaction manager = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
//                manager.replace(R.id.constraintLayout,pf);
//                manager.addToBackStack(null).commit();
               // mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileId", user.getId()).apply();
//
//                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.searchRecyclerView, new GalleryFragment()).commit();
//                String name = user.getUsername();
//                String imgUrl = user.getImageurl();
//                String uid = user.getId();
//                String bio = user.getBio();
//                Intent intent = new Intent(mContext, UsersActivity.class);
//                intent.putExtra("uname",name);
//                intent.putExtra("id",uid);
//                intent.putExtra("img",imgUrl);
//                intent.putExtra("bio",bio);
//                mContext.startActivity(intent);
//                Intent intent = new Intent(mContext, UsersActivity.class);
//                intent.putExtra("publisherId", user.getId());
//                mContext.startActivity(intent);
            }
        });
    }

    private void isFollowed(String id, final Button btnFollow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                    btnFollow.setText("following");
                }else{
                    btnFollow.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       public CircleImageView imageProfile;
       public TextView username;
       public TextView fullname;
       public Button btnFollow;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           imageProfile = itemView.findViewById(R.id.image_profile);
           username = itemView.findViewById(R.id.username);
           fullname = itemView.findViewById(R.id.fullname);
           btnFollow = itemView.findViewById(R.id.btn_follow);
       }
   }

}
