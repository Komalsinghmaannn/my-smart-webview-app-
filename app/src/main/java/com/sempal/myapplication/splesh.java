package com.sempal.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.navigation.NavigationView;

public class splesh extends AppCompatActivity {

   // String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        getSupportActionBar();

 //@SuppressLint("WrongConstant") SharedPreferences settings=getPreferences("PREFS", 0);
    //    assert settings != null;
     //   password=settings.getString("password","");




        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }


        },2000);
    }

  //  private SharedPreferences getPreferences(String prefs, int i) {
    //    String preft;
    //    return null;
    }

