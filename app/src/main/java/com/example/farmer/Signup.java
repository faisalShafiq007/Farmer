package com.example.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Signup extends AppCompatActivity {
    EditText Email,pass;
    TextView signin,sg;
    FirebaseAuth mauth;
    Button signupbtn;
    ImageButton semail,spassword;
    private final int REQUEST_CODE_SPEECH_INPUT=1000;
    private final int REQUEST_CODE_SPEECH_PASSWORD=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        checkconnetion();
        mauth=FirebaseAuth.getInstance();
        Email=findViewById(R.id.email);
        signupbtn=findViewById(R.id.Signupbutton);
        pass=findViewById(R.id.password);
        signin=findViewById(R.id.signinintent);
        semail=findViewById(R.id.speakemail);
        spassword=findViewById(R.id.speakpassword);
        sg=findViewById(R.id.signin);
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

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupint=new Intent(Signup.this,Signin.class);
                signupint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signupint);

            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ssignup();
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
            Toast.makeText(Signup.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
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
            Toast.makeText(Signup.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode ==REQUEST_CODE_SPEECH_INPUT && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            Email.setText(result.get(0));
            String a=Email.getText().toString();

            if(a.isEmpty()){
                Email.setError("please type email");
            }
            else{
                Email(a);
            }



        }
        if(requestCode ==REQUEST_CODE_SPEECH_PASSWORD && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


            pass.setText(result.get(0));
            String pas=pass.getText().toString();
            if(pas.isEmpty()){
                pass.setError("please type password");
            }else{
                password(pas);
            }

        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    private void password(String pas) {

    }

    private void Email(String a) {

    }



    private void Ssignup() {
        String e=Email.getText().toString();
        String p=pass.getText().toString();
        if(e.isEmpty()){
            Email.setError("please type email");
        }
        if(p.isEmpty()){
            pass.setError("please enter password");
        }
        if(!e.contains("@")){
            Email.setError("please type full email e.g xxx@x.com ");
        }
        if(p.length()<8){
            pass.setError("please enter password with length 8");

        }



        else{
            final ProgressDialog progress = new ProgressDialog(Signup.this);
            final Timer t = new Timer();
            progress.setTitle("Creating Acount");
            progress.setMessage("Wait while we register you...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            t.schedule(new TimerTask() {
                public void run() {
                    progress.dismiss();

                    // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
         mauth.createUserWithEmailAndPassword(Email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
               SharedPreferences.Editor editor = pref.edit();
               editor.putString("email", Email.getText().toString());
               editor.putString("password", pass.getText().toString());
               Intent mintent=new Intent(Signup.this,Profile.class);
               mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(mintent);

               Toast.makeText(Signup.this,"Signup complete",Toast.LENGTH_SHORT).show();

           }
           else{
               Toast.makeText(Signup.this,"Signup Failed",Toast.LENGTH_SHORT).show();
            checkconnetion();

           }
             }
         });
        }
    }
    public void checkconnetion(){
        ConnectivityManager manager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(null!=activenetwork){
            if(activenetwork.getType()==ConnectivityManager.TYPE_WIFI){


            }
            if(activenetwork.getType()==ConnectivityManager.TYPE_MOBILE){


            }

        }
        else{
            Toast.makeText(Signup.this,"No Internet connection",Toast.LENGTH_SHORT).show();

        }}




}
