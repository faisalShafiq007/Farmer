package com.example.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Profile extends AppCompatActivity  {
FirebaseAuth mAuth;
EditText user,cty;
Button signupbtn;
CircleImageView pimage;
    int GALLERY_PICK=1234;
    StorageReference userprofileimageRef;
    DatabaseReference userRef ;
    String Current_user_id;


ImageButton speakusernme,speakcty;
    private final int REQUEST_CODE_SPEECH_EMAIL=1000;
    private final int REQUEST_CODE_SPEECH_USERNAME=1002;
    private final int REQUEST_CODE_SPEECH_CITY=1003;
    private final int REQUEST_CODE_SPEECH_PASSWORD=1001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        signupbtn = findViewById(R.id.Signupbutton);

        userprofileimageRef = FirebaseStorage.getInstance().getReference().child("profileimage");

        Current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Current_user_id);
        user = findViewById(R.id.username);
        cty = findViewById(R.id.city);

        speakusernme = findViewById(R.id.speakusername);
        speakcty = findViewById(R.id.speakcity);

        pimage = findViewById(R.id.profile_image);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //add this to impelement methods and remove error on this
        //public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener




        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_PICK);

            }
        });

        //retrieving image from storage
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image=dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.profile_image).into(pimage);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


        speakcty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptcity();
            }
        });

        speakusernme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptusername();
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
            Toast.makeText(Profile.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }

    private void promptusername() {
        Intent iusername=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iusername.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iusername.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        iusername.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");

        try {

            startActivityForResult(iusername,REQUEST_CODE_SPEECH_USERNAME);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(Profile.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }

    private void promptcity() {
        Intent icity=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        icity.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        icity.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        icity.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");

        try {

            startActivityForResult(icity,REQUEST_CODE_SPEECH_CITY);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(Profile.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }

    private void promptemail() {
        Intent iemail=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iemail.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iemail.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        iemail.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");

        try {

            startActivityForResult(iemail,REQUEST_CODE_SPEECH_EMAIL);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(Profile.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode ==REQUEST_CODE_SPEECH_EMAIL && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);



        }
        if(requestCode ==REQUEST_CODE_SPEECH_PASSWORD && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);






        }
        if(requestCode ==REQUEST_CODE_SPEECH_USERNAME && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


            user.setText(result.get(0));
            String user1=user.getText().toString();


        }
        if(requestCode ==REQUEST_CODE_SPEECH_CITY && resultCode==RESULT_OK)
        {
            List<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


            cty.setText(result.get(0));
            String city=cty.getText().toString();

        }



        if(requestCode==1234 ){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            Uri ImageUri=data.getData();
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                final ProgressDialog progress = new ProgressDialog(Profile.this);
                final Timer t = new Timer();
                progress.setTitle("Loading");
                progress.setMessage("Wait while uploading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                t.schedule(new TimerTask() {
                    public void run() {
                        progress.dismiss();
                        // when the task active then close the dialog
                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                    }
                }, 6000);
                Uri resulturi=result.getUri();
                final StorageReference filepath=userprofileimageRef.child(Current_user_id +".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            /*final String downloadurl= task.getResult().getStorage().getDownloadUrl().toString();*/
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadurl=uri.toString();
                                    userRef.child("profileimage").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                  /*Intent setupintent=new Intent(setup.this,setup.class);
                                  startActivity(setupintent);*/
                                            Toast.makeText(Profile.this,"image stored",Toast.LENGTH_LONG).show();


                                        }
                                    });
                                }
                            });

                        }
                    }


                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                });

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    
    private void signup() {

        String name=user.getText().toString();
        String City=cty.getText().toString();
        userRef=FirebaseDatabase.getInstance().getReference().child("users").child(Current_user_id);
        HashMap usermap=new HashMap();
        usermap.put("name",name);
        usermap.put("city",City);
        userRef.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
if(task.isSuccessful()){
                Toast.makeText(Profile.this,"data stored succesfully",Toast.LENGTH_LONG).show();
                Intent i=new Intent(Profile.this,MainActivity.class);
                startActivity(i);
            }
            else{
    Toast.makeText(Profile.this,"Please fulfill all fields",Toast.LENGTH_LONG).show();

}}
        });


    }



}
