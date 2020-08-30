package com.example.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmer.cropassistant.assiatant;
import com.example.farmer.cropdisease.cropdiseaseactivity;
import com.example.farmer.marketvalues.marketvalues;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    String place;
    String language;
    LocationManager locationManager;
    boolean GpsStatus;
    String lana;
    String text;

    private ActionBarDrawerToggle abdt;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    String Current_user_id;


    ImageView imageView, assistantf, cropdiseasef, reportf, marketf;
    TextView tv, locatv, animtv;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        getSupportActionBar().hide();
        animtv = findViewById(R.id.animtext);
        Intent intent = getIntent();
     lana = intent.getStringExtra("language");
        if(lana!=null){
            if(lana.equals("English")){
                setAppolocale("en");
                Log.e("here we go","again");
                Intent h = new Intent(MainActivity.this, MainActivity.class);
                h.putExtra("lan",language);
                startActivity(h);
            }
            else{
                setAppolocale("ur");
                Log.e("here we go","urdu");
                Intent h = new Intent(MainActivity.this, MainActivity.class);
                h.putExtra("lan",language);
                startActivity(h);

            }


        }
        assistantf = findViewById(R.id.assistant);
        cropdiseasef = findViewById(R.id.cropdisease);

        reportf = findViewById(R.id.report);
        marketf = findViewById(R.id.market);
        Current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        NavigationView navigationaView = (NavigationView) findViewById(R.id.nav_view);
        final View headerView = navigationaView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            final ProgressDialog progress = new ProgressDialog(this);
            final Timer t = new Timer();
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            t.schedule(new TimerTask() {
                public void run() {

                    progress.dismiss();
                    String Current_user_id = mAuth.getCurrentUser().getUid();
                    imageView = headerView.findViewById(R.id.circleImageView1);
                    tv = headerView.findViewById(R.id.textview1);
                    userRef.child(Current_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.hasChild("name")) {
                                    String fullname = dataSnapshot.child("name").getValue().toString();
                                    tv.setText(fullname);
                                }
                                if (dataSnapshot.hasChild("profileimage")) {

                                    String image = dataSnapshot.child("profileimage").getValue().toString();
                                    Picasso.get().load(image).placeholder(R.drawable.profile_image).into(imageView);

                                } {


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.

        }

        reportf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ali();
            }
        });
        marketf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent market=new Intent(MainActivity.this, marketvalues.class);
                market.putExtra("lan",language);
                startActivity(market);
            }
        });
        assistantf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assitant = new Intent(MainActivity.this, assiatant.class);
                assitant.putExtra("lan",language);
                startActivity(assitant);
            }
        });
        animtv.startAnimation((Animation) AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate));
        locatv = findViewById(R.id.locationtv);
        cropdiseasef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cropdise=new Intent(MainActivity.this, cropdiseaseactivity.class);
                cropdise.putExtra("lan",lana);
                startActivity(cropdise);
            }
        });

        dl = findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();

        NavigationView navview = findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    Intent h = new Intent(MainActivity.this, MainActivity.class);
                    h.putExtra("lan",language);
                    startActivity(h);
                }
                if (id == R.id.Profile) {
                    Intent p = new Intent(MainActivity.this, profile_show.class);
                    p.putExtra("lan",language);
                    startActivity(p);
                }
                if (id == R.id.Settingsmenu) {
                    Intent s = new Intent(MainActivity.this, Settings.class);
                   s.putExtra("lan",language);
                    startActivity(s);
                }
                if (id == R.id.Logout) {
                    mAuth.signOut();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("name"); // will delete key name
                    editor.remove("email"); // will delete key email
                    editor.commit();
                    Intent m = new Intent(MainActivity.this, Signin.class);
                    m.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(m);
                }
                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == true) {
            /*Toast.makeText(MainActivity.this,"Location Services Is Enabled",Toast.LENGTH_SHORT).show();*/
            startho();

        } else {
            /*  Toast.makeText(MainActivity.this,"Location Services Is Disabled",Toast.LENGTH_SHORT).show();*/
        }
        super.onStart();
    }


    private void startho() {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyLoca", 0);
                    String c = pref.getString("city", null);


                    if (location != null) {

                        //TODO: UI updates.
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(MainActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    // after 2 second (or 2000 miliseconds), the task will be active.
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();
                                    // Logic to handle location object
                                    Log.e("lat:", String.valueOf(location.getLatitude()));
                                    Log.e("lon:", String.valueOf(location.getLongitude()));
                                    locatv.setText(city);
                                    place = city;


                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Myloca", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("city", city);

                                    editor.putString("address", address);
                                    editor.commit();
                                    /*Toast.makeText(MainActivity.this,String.valueOf(location.getLatitude()),LENGTH_LONG).show();*/
                                    Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    } else {

                        Toast.makeText(MainActivity.this, "Cant get your location", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

   }



    private void ali() {

        Intent kintent = new Intent(MainActivity.this, report.class);
        kintent.putExtra("lan",language);
        kintent.addFlags(kintent.FLAG_ACTIVITY_CLEAR_TASK | kintent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(kintent);

    }







    private void setAppolocale(String localcode) {
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf=res.getConfiguration();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            conf.setLocale(new Locale(localcode.toLowerCase()));
        }else{
            conf.locale=new Locale(localcode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);

    }
}

