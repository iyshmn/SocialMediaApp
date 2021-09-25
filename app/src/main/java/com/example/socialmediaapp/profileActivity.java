package com.example.socialmediaapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.adapter.UserAdapter;
import com.example.socialmediaapp.fragment.BioFragment;
import com.example.socialmediaapp.fragment.GalleryFragment;
import com.example.socialmediaapp.fragment.SearchFragment;
import com.example.socialmediaapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class profileActivity extends AppCompatActivity {
    float x1,x2,y1,y2;
    private ConstraintLayout cl;
    String status = "Online";
    private SwipeListener mSwipeListener;
    private StorageTask uploadTask;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference userReference;
    private FirebaseDatabase db;
    private FirebaseUser fuser;
    private Fragment selectorFragment;
    private View background;
    private ImageView vector;
    private View ellipse_1;
    private View ellipse_2;
    private TextView profile_page;
    private View ellipse_3;
    private ImageView update_profile_picture;
    private View rectangle_1;
    private TextView about__usersername;
    private ImageView vector_ek2;
    private ImageView vector_ek3;
    private ImageView friends;
    private ImageView gallery;
    private ImageView profilePic;
    private Uri imageUri;
    private String profileId;
    private TextView bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        background= (View) findViewById(R.id._bg__profile_screen_ek2);
        vector = (ImageView) findViewById(R.id.chat);
        update_profile_picture = (ImageView) findViewById(R.id.update_profile_picture);
        this.bio=findViewById(R.id.bioBox);
        bio.setClickable(true);
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BioFragment bio = new BioFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cl,bio);
                ft.addToBackStack(null).commit();
            }
        });
        about__usersername = (TextView) findViewById(R.id.about__usersername);
        this.cl = findViewById(R.id.cl);
        this.fuser = FirebaseAuth.getInstance().getCurrentUser();
        this.mSwipeListener = new SwipeListener(cl);
        this.gallery = findViewById(R.id.gallary);
        vector_ek2 = (ImageView) findViewById(R.id.friends);
        vector_ek3 = (ImageView) findViewById(R.id.uploadImage);
        vector_ek2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment search = new SearchFragment() ;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cl,search);
                ft.addToBackStack(null).commit();
            }
        });
        this.vector_ek3.setClickable(true);
        this.gallery.setClickable(true);
        this.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFragment gallery = new GalleryFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cl,gallery);
                ft.addToBackStack(null).commit();
            }
        });
        this.vector_ek3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(profileActivity.this,"Working", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(profileActivity.this , postActivity.class));
            }
        });
        this.profilePic = findViewById((R.id.profile_img));
       this.storage = FirebaseStorage.getInstance();
       this.storageReference=storage.getReference();
       this.db =    FirebaseDatabase.getInstance();
       this.userReference = db.getReference();

        FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                about__usersername.setText(user.getUsername());
               // user.setStatus(status);
                bio.setText((user.getBio()));
                Picasso.get().load(user.getImageurl()).into(profilePic);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        update_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();

            }
        });
    }
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri= data.getData();
            this.profilePic.setImageURI(imageUri);

            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading your Profile Picture..");
        pd.show();

       // final String randomKey = UUID.randomUUID().toString();
       // StorageReference mountainImagesRef = storageReference.child("Images/"+randomKey);
        if (imageUri!= null) {
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Uploads").child(System.currentTimeMillis() + ".jpeg");
            uploadTask = fileRef.putFile(imageUri);
            new User().setImageurl(imageUri.toString());
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("imageurl").setValue(url);
                        pd.dismiss();
                    } else {

                    }
                }
            });
        }else{
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }




//        mountainImagesRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                pd.dismiss();
//                Snackbar.make(findViewById(android.R.id.content),"Profile Picture Updated",Snackbar.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                Toast.makeText(getApplicationContext(),"Failed to Upload Image",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                double progressPerecent = (100.00*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
//                pd.setMessage("Progress "+(int)progressPerecent+"%");
//            }
//        });

    }

    class SwipeListener implements View.OnTouchListener {
        private GestureDetector gd;
        SwipeListener(View v){
            int threshold = 100;
            int velocity_threshold = 100;
            GestureDetector.SimpleOnGestureListener l = new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float xDiff = e2.getX()-e1.getX();
                    float yDiff = e2.getY()-e1.getY();
                    if(Math.abs(xDiff)>Math.abs(yDiff)){
                        if(Math.abs(xDiff)>threshold&&Math.abs(velocityX )>velocity_threshold){
                            if(xDiff>0){
                                Toast.makeText(profileActivity.this,"Swipped Left",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent i = new Intent(profileActivity.this, ChatActivity.class);
                                startActivity(i);
                            }
                            return  true;
                        }
                    }
                    return false;
                }
            };
            this.gd = new GestureDetector(l);
            v.setOnTouchListener(this);
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return gd.onTouchEvent(event);
        }
    }
}
