package com.example.farmer.cropdisease;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class cropdiseaseactivity extends AppCompatActivity {
    FirebaseVisionImageLabeler labeler;
    Button gallery,procss,cmera;
    ImageView imgview,spek;
    private static final int GALLERY_PICK = 1;
    private Uri imageuri;
    TextView msgtxt;
    int intlangu;
    FirebaseVisionImage image;
    int camerapick=10;
    FirebaseAuth mAuht;
    String currentuserid;
    DatabaseReference userRef;
String message;
String lang;
TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropdiseaseactivity);
        FirebaseApp.initializeApp(this);
        gallery=findViewById(R.id.getfromgallery);
        spek=findViewById(R.id.speakk);
        msgtxt=findViewById(R.id.messagetext);
        getSupportActionBar().setTitle("Plant disease");
        procss=findViewById(R.id.process);
        imgview=findViewById(R.id.imageView);
        cmera=findViewById(R.id.camera);
        mAuht=FirebaseAuth.getInstance();
        currentuserid=mAuht.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("language")){
                    lang = dataSnapshot.child("language").getValue().toString();
                    Log.e("language",lang);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
    @Override
    public void onInit(int i) {
if(i==TextToSpeech.SUCCESS){
        intlangu=textToSpeech.setLanguage(Locale.ENGLISH);

}
    }
});




        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });
        cmera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camerafunction();
            }
        });

    }

    private void camerafunction() {
        camerapick=11;
        Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent,camerapick);

    }

    private void tester(){

        final FirebaseAutoMLRemoteModel remoteModel = new FirebaseAutoMLRemoteModel.Builder("Plant").build();




        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            try {

                                FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                                        new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(remoteModel)
                                                .setConfidenceThreshold(0.5f)  // Evaluate your model in the Firebase console
                                                // to determine an appropriate value.
                                                .build();
                                labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);

                            } catch (FirebaseMLException e) {

                                e.printStackTrace();
                                // ...
                            }

                        }
                        // Success.
                    }
                });



        try {

            image = FirebaseVisionImage.fromFilePath(cropdiseaseactivity.this, imageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void opengallery() {

        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GALLERY_PICK);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(camerapick==11){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imgview.setImageBitmap(bitmap);
            camerapick=10;
        }


        if(requestCode==GALLERY_PICK&&resultCode==RESULT_OK&&data!=null){
            imageuri=data.getData();
            Picasso.get().load(imageuri).
                    resize(1500, 700).into(imgview);


            tester();
            procss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    labeler.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                @Override
                                public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                    for (FirebaseVisionImageLabel label: labels) {

                                        String text = label.getText();

                                        int confidence = (int) label.getConfidence();
                                        if(text.equals(null)){
                                            Toast.makeText(cropdiseaseactivity.this,"Not resembling",Toast.LENGTH_LONG).show();
                                        }
                                        Log.e("label",text);
                                        Log.e("confidence", String.valueOf(confidence));
if(text.equals("blight")){
    message=  getString(R.string.blightcure);
}
else if(text.equals("asteryellow")){
    message= getString(R.string.asteryellowcure);
}
else if(text.equals("canker")){
    message=getString(R.string.cankercure);
}
else if(text.equals("crowngall")){
    message=getString(R.string.crowgallcure);
}
else if(text.equals("bacterialwilt")){
    message=getString(R.string.bacterialwiltcure);
}
else if(text.equals("rust")){
    message=getString(R.string.rustcure);
}
else if(text.equals("anthracnose")){
    message=getString(R.string.Anthracnosecure);
}
else if(text.equals("potatoscab")){
    message= getString(R.string.potatoscabcure);
}
else if(text.equals("downymildew")){
    message=getString(R.string.downeymildewcure);
}
else if(text.equals("brownrot")){
    message=getString(R.string.brownrotcure);
}
else if(text.equals("leafspot")){
    message=getString(R.string.leafspotcure);
}
else if(text.equals("blackknot")){
    message=getString(R.string.blackknotcure);
}
else if(text.equals("clubroot")){
    message=getString(R.string.clubrootcure);
}
else if(text.equals("graymold")){
    message=getString(R.string.graymoldcure);
}
else if(text.equals("appleScab")){
    message=getString(R.string.applescabcure);
}
else if(text.equals("leafcurl")){
    message=getString(R.string.leafcurlcure);
}
else if(text.equals("blossomendrot")){
    message=getString(R.string.blossomendcur);
}
else if(text.equals("cedarapplerust")){
    message=getString(R.string.cedarapplerustcure);
}
else if(text.equals("powderymildew")){
    message=getString(R.string.powderymildewcure);
}
else if(text.equals("cornsmut")){
    message=getString(R.string.cornsmutcure);
}
else if(text.equals("mosaicvirus")){
    message=getString(R.string.mosaicviruscure);
}
else if(text.equals("dampingoff")){
    message=getString(R.string.dampingoffcure);
}
else if(text.equals("yellowmottle")){
    message=getString(R.string.yellowmotlecure);
}


Toast.makeText(cropdiseaseactivity.this,text,Toast.LENGTH_LONG).show();
                                   /*     new AlertDialog.Builder(cropdiseaseactivity.this)
                                                .setTitle(text)
                                                .setMessage(message)

                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("Speak", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        int speech=textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH
                                                                ,null);
                                                        Log.e("one", message);
                                                    }
                                                })

                                                // A null listener allows the button to dismiss the dialog and take no further action.

                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();*/
                                   if(!message.isEmpty()) {
                                    spek.setVisibility(View.VISIBLE);
                                       msgtxt.setVisibility(View.VISIBLE);
                                       spek.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               int speech = textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH
                                                       , null);

                                           }
                                       });
                                       msgtxt.setText(message);


                                   }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception

                                    e.printStackTrace();
                                }
                            });

                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        camerapick=10;
    }
}
