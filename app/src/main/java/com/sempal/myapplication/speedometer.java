package com.sempal.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.text.DecimalFormat;

public class speedometer extends Activity implements LocationListener {

    public NotificationCompat.Builder builder;
    public NotificationManager notificationManager;
    public static WindowManager windowManager;
    public static View viewOverlay;
    public boolean paused = false, runThrough = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.iconn);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#222222"));
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("Speedometer", bm, Color.parseColor("#222222"));
            setTaskDescription(td);
        }else{
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);


        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setColor(Color.rgb(230, 184, 0));
        builder.setSmallIcon(R.drawable.ic_home);
        builder.setLargeIcon(bm);
        builder.setContentTitle("Speedometer");
        builder.setContentText("000.0 km/h");
        builder.setDefaults(Notification.DEFAULT_LIGHTS).setVibrate(new long[]{0l});
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification note = builder.build();
        note.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(99, builder.build());
        notificationManager.notify(99, note);


        try{
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            this.onLocationChanged(null);
        }catch(SecurityException se){ }
    }








    @Override
    public void onPause(){
        super.onPause();
        //paused = true;

        boolean playAnywhere = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(Settings.canDrawOverlays(this)){
                playAnywhere = true;
            }
        }else{
            playAnywhere = true;
        }

        if(playAnywhere){
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.x = 0;
            params.y = 0;

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            viewOverlay = inflater.inflate(R.layout.activity_overlay, null);

            windowManager.addView(viewOverlay, params);

            RelativeLayout rootOverlay = (RelativeLayout) viewOverlay.findViewById(R.id.rootOverlay);

            rootOverlay.setOnTouchListener(new View.OnTouchListener(){ int downX, downY;


                @Override
                public boolean onTouch(View v, MotionEvent event){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        downX = params.x-(int)event.getRawX();
                        downY = params.y-(int)event.getRawY();
                    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        params.x = (int)event.getRawX()+downX;
                        params.y = (int)event.getRawY()+downY;

                        windowManager.updateViewLayout(viewOverlay, params);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        paused = false;
        runThrough = false;

        if(windowManager != null){
            windowManager.removeView(viewOverlay);
            windowManager = null;
        }
    }

    @Override
    public void onLocationChanged(Location location){
        TextView cs = (TextView) findViewById(R.id.cspeed);
        if(location == null || location.getSpeed() == 0){
            cs.setText("000.0 km/h");
            if(windowManager != null){
                //builder.setContentText("000.0 mph");
                TextView ocs = (TextView) viewOverlay.findViewById(R.id.cspeed);
                ocs.setText("000.0");

                //notificationManager.notify(99, builder.build());
                //runThrough = true;
            }
        }else{
            cs.setText(new DecimalFormat("###.#").format(location.getSpeed()*2.23694)+" mph");
            if(windowManager != null){
                TextView ocs = (TextView) viewOverlay.findViewById(R.id.cspeed);
                ocs.setText(new DecimalFormat("###.#").format(location.getSpeed()*2.23694));
                notificationManager.notify(99, builder.build());
            }
            runThrough = false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }

    @Override
    public void onProviderEnabled(String provider){
    }

    @Override
    public void onProviderDisabled(String provider){
    }



}
