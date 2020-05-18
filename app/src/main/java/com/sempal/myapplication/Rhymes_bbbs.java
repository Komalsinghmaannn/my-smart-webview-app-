package com.sempal.myapplication;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Rhymes_bbbs extends AppCompatActivity {
    MediaPlayer mp;
    ImageView home;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rhymes_bbbs);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mp=new MediaPlayer();
        mp=MediaPlayer.create(this,R.raw.bbbs);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        mp.start();

        home = (ImageView) findViewById(R.id.back1);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Rhymes_bbbs.this,Rhymes.class);
                startActivity(intent);
                stopMusic(v);
            }
        });
    }
    public void stopMusic(View view)
    {
        mp.stop();
        mp.release();
    }
    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}

