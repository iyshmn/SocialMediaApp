package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.ChatActivity;
import com.example.socialmediaapp.MessengerActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> users) {
        mContext = context;
        mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View v = LayoutInflater.from(mContext).inflate(R.layout.profile_screen, parent , false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_row, parent, false);
    return new ViewHolder(view);
       // return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User u = mUsers.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(u.getId()))
        {
            holder.itemView.setVisibility(View.GONE);
        }
        holder.username.setText(u.getUsername());
        holder.status.setText(u.getStatus());
        Picasso.get().load(u.getImageurl()).placeholder(R.drawable.profile).into(holder.imageProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MessengerActivity.class);
//                intent.putExtra("name", u.getUsername());
//                intent.putExtra("RecieverImage", u.getImageurl());
//                intent.putExtra("uid", u.getId());
                String name = u.getUsername();
                String imgUrl = u.getImageurl();
                String uid = u.getId();
                Intent i = new Intent(mContext,MessengerActivity.class);
                i.putExtra("uname",name);
                i.putExtra("id",uid);
                i.putExtra("img",imgUrl);
               // LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
                mContext.startActivity(i);
            }
        });
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        final User user = mUsers.get(position);
//        holder.username.setText(user.getUsername());
//        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageProfile=itemView.findViewById(R.id.user_image);
            this.username=itemView.findViewById(R.id.user_name);
            this.status=itemView.findViewById(R.id.user_status);
        }
    }

}
