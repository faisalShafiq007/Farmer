package com.example.farmer.cropassistant;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.farmer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.TextView;

public class assiatant extends AppCompatActivity {
    private TextView cultivte,hrvst,wter,frtlizer;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_wheat:
             wheat();
                    return true;
                case R.id.navigation_rice:
            rice();
                    return true;
                case R.id.navigation_cotton:
               cotton();
                    return true;

                case R.id.navigation_corn:
               corn();
                    return true;

                case R.id.navigation_sugarcane:
               sugarcane();
                    return true;
            }
            return false;
        }
    };


    private void sugarcane() {
        cultivte.setText(R.string.dd);
        hrvst.setText(R.string.ss);
        wter.setText(R.string.sss);
        frtlizer.setText(getString(R.string.sa)+getString(R.string.ssad));
    }



    private void corn() {
        cultivte.setText(R.string.fff);
        hrvst.setText(R.string.dsd);
        wter.setText(R.string.sfsd);
        frtlizer.setText(getString(R.string.dsfds)+getString(R.string.sdfs));
    }

    private void cotton() {
        cultivte.setText(R.string.sadas);
        hrvst.setText(R.string.q);
        wter.setText(R.string.w);
        frtlizer.setText(getString(R.string.e)+getString(R.string.ee));
    }


    private void rice() {
        cultivte.setText(R.string.r);
        hrvst.setText(R.string.t);
        wter.setText(R.string.y);
        frtlizer.setText(getString(R.string.ty)+getString(R.string.rwe));
    }


    private void wheat() {
        cultivte.setText(R.string.wqeq);
        hrvst.setText(R.string.qweq);
        wter.setText(R.string.qewe);
        frtlizer.setText(getString(R.string.qweqwq)+getString(R.string.wqewq));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assiatant);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().setTitle("Crop Assistant");
        cultivte = findViewById(R.id.cultivation);
        hrvst=findViewById(R.id.harvest);
             wter=findViewById(R.id.water);
             frtlizer=findViewById(R.id.fertilizer);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    protected void onStart() {
        cultivte.setText(R.string.a);
        hrvst.setText(R.string.s);
        wter.setText(R.string.gs);
        frtlizer.setText(getString(R.string.m)+getString(R.string.k));
        super.onStart();
    }

}
