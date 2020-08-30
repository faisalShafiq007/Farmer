package com.example.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    ImageView imageView;
    FirebaseAuth mAuth;

    private final int SPLASH_DISPLAY_LENGTH = 3500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();


        TextView textView=findViewById(R.id.textviw);// Declare an imageView to show the animation.
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        Animation bouncee = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        textView.startAnimation(bouncee);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",  0);
                String e=pref.getString("email",null);

                String p=pref.getString("password",null);

                if(e==null){
                Intent intent=new Intent(Splash.this, Signin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Splash.this.finish();}
                else{
                    mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Intent intent_1=new Intent(Splash.this, MainActivity.class);
                           intent_1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent_1);
                        }
                    });

                }
            }
        }, SPLASH_DISPLAY_LENGTH);







    }

}
