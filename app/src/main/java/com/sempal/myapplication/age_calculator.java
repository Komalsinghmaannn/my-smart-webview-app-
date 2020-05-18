package com.sempal.myapplication;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.TimeZone;



public class age_calculator extends AppCompatActivity {

    private AdView mAdView;

    private TextView description;
    private EditText byear;
    private EditText bmonth;
    private EditText bday;
    private EditText ageyear;
    private EditText agemonth;
    private EditText ageday;
    private EditText interest;
    private EditText duration;
    private EditText year;
    private EditText month;
    private TextView result;
    private TextView ciresult;
    private Button calc;
    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculator);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        description = (TextView) findViewById(R.id.tvResult);


        result = (TextView) findViewById(R.id.tvResult);
        byear = (EditText) findViewById(R.id.etBirhdayYear);
        bmonth = (EditText) findViewById(R.id.etBirhdayMonth);
        bday = (EditText) findViewById(R.id.etBirhdayDay);
        ageyear = (EditText) findViewById(R.id.etAgeYear);
        agemonth = (EditText) findViewById(R.id.etAgeMonth);
        ageday = (EditText) findViewById(R.id.etAgeDay);
        year = (EditText) findViewById(R.id.etYear);
        month = (EditText) findViewById(R.id.etMonth);
        calc = (Button) findViewById(R.id.buttonCalculate);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String yr = String.valueOf(calendar.get(Calendar.YEAR));
        int thismn = (calendar.get(Calendar.MONTH)) + 1;
        String mn = String.valueOf(thismn);
        String da = String.valueOf(calendar.get(Calendar.DATE));

        ageyear.setText(yr);
        agemonth.setText(mn);
        ageday.setText(da);


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                try {
                    long birthYear = Long.parseLong(byear.getText().toString());
                    long birthMonth = Long.parseLong(bmonth.getText().toString());
                    long birthDay = Long.parseLong(bday.getText().toString());

                    long ageYear = Long.parseLong(ageyear.getText().toString());
                    long ageMonth = Long.parseLong(agemonth.getText().toString());
                    long ageDay = Long.parseLong(ageday.getText().toString());

                    if (ageMonth > birthMonth && ageDay > birthDay){
                        long ageYearDiff = ageYear - birthYear;
                        long ageMonthDiff = ageMonth - birthMonth;
                        long ageDayDiff = ageDay - birthDay;

                        if (birthDay < 32 && ageDay < 32 && ageMonth < 13 && birthMonth < 13)
                        {
                            year.setText(ageYearDiff+" Year");
                            month.setText(ageMonthDiff+" Month");
                            result.setText(ageDayDiff+" Days");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if ( ageMonth > birthMonth && ageDay <= birthDay ){
                        long ageYearDiff = (ageYear - birthYear);
                        long ageMonthDiff = (ageMonth - birthMonth) -1;
                        long ageDayDiff = 0;
                        if (ageMonth == 1 || ageMonth == 3 || ageMonth == 5 || ageMonth == 7 || ageMonth == 8 || ageMonth == 10 || ageMonth == 12 ) {
                            ageDayDiff = (ageDay - birthDay) + 31;
                        }
                        else if (ageMonth == 2 ) {
                            ageDayDiff = (ageDay - birthDay) + 28;
                        }
                        else {
                            ageDayDiff = (ageDay - birthDay) + 30;
                        }

                        if (birthDay < 32 && ageDay < 32 && ageMonth < 13 && birthMonth < 13)
                        {
                            year.setText(ageYearDiff+" Year");
                            month.setText(ageMonthDiff+" Month");
                            result.setText(ageDayDiff+" Days");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (ageMonth <= birthMonth && ageDay > birthDay){
                        long ageYearDiff = (ageYear - birthYear) - 1;
                        long ageMonthDiff = (ageMonth - birthMonth) + 12;
                        long ageDayDiff = ageDay - birthDay;

                        if (birthDay < 32 && ageDay < 32 && ageMonth < 13 && birthMonth < 13)
                        {
                            year.setText(ageYearDiff+" Year");
                            month.setText(ageMonthDiff+" Month");
                            result.setText(ageDayDiff+" Days");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (ageMonth <= birthMonth && ageDay <= birthDay){
                        long ageYearDiff = (ageYear - birthYear) - 1;
                        long ageMonthDiff = (ageMonth - birthMonth) + 11;
                        long ageDayDiff = 0;
                        if (ageMonth == 1 || ageMonth == 3 || ageMonth == 5 || ageMonth == 7 || ageMonth == 8 || ageMonth == 10 || ageMonth == 12 ) {
                            ageDayDiff = (ageDay - birthDay) + 31;
                        }
                        else if (ageMonth == 2 ) {
                            ageDayDiff = (ageDay - birthDay) + 28;
                        }
                        else {
                            ageDayDiff = (ageDay - birthDay) + 30;
                        }

                        if (birthDay < 32 && ageDay < 32 && ageMonth < 13 && birthMonth < 13)
                        {
                            year.setText(ageYearDiff+" Year");
                            month.setText(ageMonthDiff+" Month");
                            result.setText(ageDayDiff+" Days");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Month Problm",Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Please enter all data!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
