package com.example.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Signin extends AppCompatActivity {
    EditText Email,pass;
    TextView signup,sg;
    Button signinbutton;
    FirebaseAuth mAuth;
    ImageButton semail,spassword;
    private final int REQUEST_CODE_SPEECH_INPUT=1000;
    private final int REQUEST_CODE_SPEECH_PASSWORD=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
getSupportActionBar().hide();
       Email=findViewById(R.id.email);
       signinbutton=findViewById(R.id.signinbtn);
       pass=findViewById(R.id.password);
       mAuth=FirebaseAuth.getInstance();
       signup=findViewById(R.id.signupintent);
       semail=findViewById(R.id.speakemail);
       spassword=findViewById(R.id.speakpassword);
  sg=findViewById(R.id.signin);
  signinbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          signinmehtod();
      }
  });
       semail.setOnClickListener(new View.OnClickListener() {


           @Override
           public void onClick(View v) {
               promptspeech();

           }
       });
       spassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                 promptpassword();
           }
       });

       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent signupint=new Intent(Signin.this,Signup.class);
               signupint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(signupint);
           }
       });


    }



    private void promptpassword() {
        Intent ipas=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ipas.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        ipas.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        ipas.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");

        try {

            startActivityForResult(ipas,REQUEST_CODE_SPEECH_PASSWORD);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(Signin.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }
    public void promptspeech()
    {
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");

        try {
            startActivityForResult(i,REQUEST_CODE_SPEECH_INPUT );
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(Signin.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode ==REQUEST_CODE_SPEECH_INPUT && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            Email.setText(result.get(0));




        }
        if(requestCode ==REQUEST_CODE_SPEECH_PASSWORD && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


            pass.setText(result.get(0));


        }


        super.onActivityResult(requestCode, resultCode, data);

    }



    private void signinmehtod() {
        String e=Email.getText().toString();
        String p=pass.getText().toString();

        if(e.isEmpty()){
            Email.setError("please type email");
        }
        else if(p.isEmpty()){
            pass.setError("please enter password");
        }
        else if(!e.contains("@")){
            Email.setError("please type full email e.g xxx@x.com ");
        }
        else if(p.length()<8){
            pass.setError("please enter password with length 8");
        }

        else{

            final ProgressDialog progress = new ProgressDialog(this);
            final Timer t = new Timer();


            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            t.schedule(new TimerTask() {
                public void run() {
                    progress.dismiss();

                    mAuth.signInWithEmailAndPassword(Email.getText().toString(),pass.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()) {
                              Toast.makeText(Signin.this, "Succesfully sign in", Toast.LENGTH_LONG).show();
                              SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                              SharedPreferences.Editor editor = pref.edit();
                              editor.putString("email", Email.getText().toString());
                              editor.putString("password", pass.getText().toString());
                              editor.commit();
                              Intent main = new Intent(Signin.this, MainActivity.class);
                              main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(main);
                          }else{
                              Toast.makeText(Signin.this,"Failed to Login",Toast.LENGTH_LONG).show();
                          }
                        }
                    });
                    t.cancel();
                    // when the task active then close the dialog
                    // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.



        }
    }
}
