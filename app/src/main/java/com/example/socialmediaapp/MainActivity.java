package com.example.socialmediaapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

        private Button signUp,signIn;
        private FirebaseAuth mAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            FirebaseApp.initializeApp(this);

            signIn = findViewById(R.id.btn_signIn);
            signUp = findViewById(R.id.btn_signUp);
            mAuth = FirebaseAuth.getInstance();
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,registerActivity.class));
                 //   finish();
                    //signUp();
                }
            });
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,loginActivity.class));
                    finish();
                 //  signIn();
                }
            });
        }
        protected void onStart() {
            super.onStart();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser!=null){
              //  transitionToSocialMediaActivity();
            }
        }
//        private void signUp(){
//            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        Toast.makeText(MainActivity.this,"Signing up Successful",Toast.LENGTH_SHORT).show();
//                        transitionToSocialMediaActivity();
//                    }
//                    else{
//                        Log.w("ERROR", "createUserWithEmail:failure", task.getException());
//                        Toast.makeText(MainActivity.this,"Signing up failed",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//        private void signIn(){
//            mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(MainActivity.this,"Signing in Successful",Toast.LENGTH_SHORT).show();
//                    transitionToSocialMediaActivity();
//                }else{
//                    Toast.makeText(MainActivity.this,"Signing in not Successful",Toast.LENGTH_SHORT).show();
//
//                }
//                }
//            });
//        }
    private void transitionToSocialMediaActivity(){
            Intent intent = new Intent(this,registerActivity.class);
            startActivity(intent);
    }
}