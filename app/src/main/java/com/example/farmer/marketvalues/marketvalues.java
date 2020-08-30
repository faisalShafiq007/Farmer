package com.example.farmer.marketvalues;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farmer.R;

public class marketvalues extends AppCompatActivity {

ImageView rice,wheat,cotton,oilseed,maize,sugarcane;
TextView ricetv,wheattv,cottontv,corntv,sugarcanetv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketvalues);
        rice=findViewById(R.id.Riceimageview);
        wheat=findViewById(R.id.Wheatimageview);
        cotton=findViewById(R.id.Cottonimageview);
        oilseed=findViewById(R.id.Oilseedimageview);
        sugarcane=findViewById(R.id.Sugarcaneimageview);
        ricetv=findViewById(R.id.ricetextview);
        wheattv=findViewById(R.id.wheattextview);
        cottontv=findViewById(R.id.cottontextview);
        corntv=findViewById(R.id.corntextview);
        sugarcanetv=findViewById(R.id.sugarcanetextview);

        rice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
               ricetv.setText(getString(R.string.oo)+"1500/1800 "+getString(R.string.eew)+"/40kg");
               wheattv.setText(R.string.qwewq);
               cottontv.setText(R.string.tyy);
corntv.setText(R.string.uiui);
sugarcanetv.setText(R.string.ewrewr);
            }
        });
        wheat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
           wheattv.setText(getString(R.string.qwewqq)+"1450 "+getString(R.string.eee)+"/40kg");
                ricetv.setText(R.string.tttt);
                cottontv.setText(R.string.yyy);
                corntv.setText(R.string.uuu);
                sugarcanetv.setText(R.string.iii);

            }
        });
    cotton.setOnClickListener(new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            cottontv.setText(getString(R.string.iio)+"8000/9000 "+getString(R.string.rwwe)+"/40kg");
            ricetv.setText(R.string.qwewqe);
            wheattv.setText(R.string.asda);
            corntv.setText(R.string.asdas);
            sugarcanetv.setText(R.string.ghng);
        }
    });
        oilseed.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                corntv.setText(getString(R.string.werewr)+"800/1200 "+getString(R.string.rtyrt)+"/40kg");
                cottontv.setText(R.string.dsfdsf);
                ricetv.setText(R.string.dfgdf);
                wheattv.setText(R.string.rtytr);
                sugarcanetv.setText(R.string.wqewqe);

            }
        });

        sugarcane.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                sugarcanetv.setText(getString(R.string.dfds)+"135/190 "+getString(R.string.hnhn)+"/40kg");
                cottontv.setText(R.string.yuy);
                ricetv.setText(R.string.iki);
                wheattv.setText(R.string.oko);
                corntv.setText(R.string.plp);


            }
        });
    }

}
