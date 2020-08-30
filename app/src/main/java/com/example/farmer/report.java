package com.example.farmer;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class report extends AppCompatActivity {

    ImageView tdayimg,tmrwimg,thirdayimg,fdimg,sdimg;
    TextView tday, tomorw, dat, sdat, tfat, fourthdat;
    TextView a, b, c, d;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    String jagah="";
    TextView today;
    TextView cityin;
    class Weatherb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... addressa) {
            try {

                URL url = new URL(addressa[0]);
                HttpURLConnection connectionc = (HttpURLConnection) url.openConnection();
                //Establish connection with adress
                connectionc.connect();
                //retrieve data from url
                InputStream is = connectionc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //retrieve data and return as a string
                int data = isr.read();
String contenta="";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    contenta = contenta + ch;
                    data = isr.read();


                }
                return contenta;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search() {

        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Myloca",  0);
            String loc=pref.getString("city",null);
            Log.e("place",loc);
            cityin.setText(loc);
            String contenta="";
            Weatherb weather = new Weatherb();
            contenta = weather.execute("https://api.openweathermap.org/data/2.5/forecast?q="+loc+"&appid=b89b3ca8d366343a3ac1ce2776c1ea03").get();
         /*   Log.e("Content", contenta);*/
            JSONObject jsonObject = new JSONObject(contenta);

            String list = jsonObject.getString("list");

           /* Log.i("listnew", list);*/

            JSONArray array = new JSONArray(list);


            String main = "";

            /*JSONObject data = json.getJSONObject("data");*/

            for (int i = 0; i <= 0; i++) {

                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
               String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                    JSONObject w=warray.getJSONObject(0);
                    String mainw=w.getString("main");


                /*  Log.e("main",main);*/
                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                Log.e("temp", temp);
                double c = ((Double.parseDouble(temp)) - (273.15));
                Log.e("value", String.valueOf(c));
                String d = String.format("%.0f", c);
                int  tempor=Integer.valueOf(d.trim());


                          tday.setText(d + " °C");

                Log.e("value", String.valueOf(tempor));
                if(mainw.equals("Clear")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.sunnyback);
                            today.setText("Clear");
                        }
                    });
                   }
                else if(mainw.equals("Haze")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            today.setText("Haze");
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.hazeback);
                        }
                    });
                }
                else if(mainw.equals("Snow")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Snow");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.snowback);
                        }
                    });
                    }
                else if(mainw.equals("Clouds")){

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Cloud");
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.cloudyback);
                        }
                    });
                }
                else if(mainw.equals("Smoke")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Smoke");
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.smokeback);
                            // Stuff that updates the UI

                        }
                    });
                     }
                else if(mainw.equals("Rain")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Rain");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.rainingback);

                        }
                    });
                                  }
                else if(mainw.equals("Mist")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Mist");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.mistback);

                        }
                    });
                }   else if(mainw.equals("Drizzle")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Drizzle");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.drizzleback);

                        }
                    });
                }

            }
            for (int i = 1; i <= 1; i++) {

                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                    JSONObject w=warray.getJSONObject(0);
                    String mainw=w.getString("main");
                    Log.e("main",mainw);



                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");

                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);

                tomorw.setText(d + " °C");
                int   tempor=Integer.parseInt(d.trim());

                if(mainw.equals("Clear")){
                    tmrwimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    tmrwimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    tmrwimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    tmrwimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    tmrwimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    tmrwimg.setImageResource(R.drawable.raining);
                   }
                else if(mainw.equals("Mist")){
                    tmrwimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    tmrwimg.setImageResource(R.drawable.drizzle);
                }

            }
            for (int i = 2; i <= 2; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                    JSONObject w=warray.getJSONObject(0);
                    String mainw=w.getString("main");
                    Log.e("main",mainw);



                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                dat.setText(d + " °C");
                int   tempor=Integer.parseInt(d);

                if(mainw.equals("Clear")){
                    thirdayimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    thirdayimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    thirdayimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    thirdayimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    thirdayimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    thirdayimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    thirdayimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    thirdayimg.setImageResource(R.drawable.drizzle);
                }

            }
            for (int i = 3; i <= 3; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");

                JSONObject jsonObjecttemp = new JSONObject(main);
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                    JSONObject w=warray.getJSONObject(0);
                    String mainw=w.getString("main");
                    Log.e("main",mainw);


                String temp = jsonObjecttemp.getString("temp");

                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                sdat.setText(d + " °C");
                int   tempor=Integer.parseInt(d);

                if(mainw.equals("Clear")){
                    fdimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    fdimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    fdimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    fdimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    fdimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    fdimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    fdimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    fdimg.setImageResource(R.drawable.drizzle);
                }

            }

            for (int i = 4; i <= 4; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                    JSONObject w=warray.getJSONObject(0);
                    String mainw=w.getString("main");
                    Log.e("main",mainw);


                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                tfat.setText(d + " °C");
                int   tempor=Integer.parseInt(d);


                if(mainw.equals("Clear")){
                    sdimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    sdimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    sdimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    sdimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    sdimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    sdimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    sdimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    sdimg.setImageResource(R.drawable.drizzle);
                }

            }
            for (int i = 5; i <= 5; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                    JSONObject w=warray.getJSONObject(0);
                    String mainw=w.getString("main");
                    Log.e("main",mainw);


                /*  Log.e("main",main);*/
                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                fourthdat.setText(d + " °C");

            }






        } catch (Exception e) {
            e.printStackTrace();
        }
        // also just top the timer thread, otherwise, you may receive a crash report
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        tday=findViewById(R.id.todaytemp);
        tomorw=findViewById(R.id.tomorrowtemp);
        dat=findViewById(R.id.daytomorrowtemp);
        sdat=findViewById(R.id.sectomorrowtemp);
        today=findViewById(R.id.tday);
        tfat=findViewById(R.id.thirdtomorrowtemp);
        fourthdat=findViewById(R.id.fourtomorrowtemp);
        a=findViewById(R.id.c);
        b=findViewById(R.id.d);
        c=findViewById(R.id.e);
        d=findViewById(R.id.f);

cityin=findViewById(R.id.cityintent);
        tmrwimg=findViewById(R.id.tomorrowimage);
        fdimg=findViewById(R.id.firstdayimage);
        sdimg=findViewById(R.id.seconddayimage);
        thirdayimg=findViewById(R.id.thirddayimage);
getSupportActionBar().hide();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        String concat=date.substring(0,3);
        Integer conert=Integer.parseInt(date.substring(3,5));






        Integer fa=(conert)+2;
        Integer fb=(fa)+1;
        Integer fc=(fb)+1;
        Integer fd=(fc)+1;
        a.setText("D.A.T");
        b.setText("4th day");
        c.setText("5th day");
        d.setText("Fourth Day");
        final ProgressDialog progress = new ProgressDialog(this);
        final Timer t = new Timer();

        progress.setTitle("Loading");
        progress.setMessage("Wait we are getting current weather...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        t.schedule(new TimerTask() {
            public void run() {
                search();
                progress.dismiss();
                // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 5000);


    }

    @Override
    public void onBackPressed() {
        Intent main=new Intent(report.this,MainActivity.class);
        startActivity(main);
        super.onBackPressed();
    }
}
