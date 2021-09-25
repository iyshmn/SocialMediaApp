package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.adapter.MessagesAdater;
import com.example.socialmediaapp.model.Messages;
import com.example.socialmediaapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerActivity extends AppCompatActivity {
   String ReciverImage, ReciverUID, ReciverName, SenderUID;

    CircleImageView profileImage;
    TextView reciverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;
    MessagesAdater adater;


    CardView sendBtn;
    EditText edtMessage;

    String senderRoom, reciverRoom;

    RecyclerView messageAdater;
    ArrayList<Messages> messagesArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        ReciverName=getIntent().getStringExtra("uname");
        ReciverImage=getIntent().getStringExtra("img");
        ReciverUID=getIntent().getStringExtra("id");
        profileImage = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.reciverName);
        messageAdater = findViewById(R.id.messageAdater);
//        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                Picasso.get().load(user.getImageurl()).into(profileImage);
//                reciverName.setText(user.getUsername());
//                ReciverUID = user.getId();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        Picasso.get().load(ReciverImage).into(profileImage);
        sendBtn = findViewById(R.id.sendBtn);
        edtMessage = findViewById(R.id.edtMessage);
        reciverName.setText(" " + ReciverName);
        //messageAdater=findViewById(R.id.messageAdater);
        messagesArrayList = new ArrayList<>();
        adater = new MessagesAdater(MessengerActivity.this, messagesArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdater.setLayoutManager(linearLayoutManager);
        messageAdater.setAdapter(adater);
        SenderUID = firebaseAuth.getUid();
        DatabaseReference reference = database.getReference().child("Users").child(firebaseAuth.getUid());


        senderRoom = SenderUID + ReciverUID;
        reciverRoom = ReciverUID + SenderUID;


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sImage = snapshot.child("imageurl").getValue().toString();
                rImage=ReciverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference chatRefrece = database.getReference().child("chats").child(senderRoom).child("messages");
        chatRefrece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(MessengerActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMessage.setText("");
                Date date = new Date();

                Messages messages = new Messages(message, SenderUID, date.getTime());
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}