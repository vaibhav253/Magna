package com.example.vaibhav.magna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref=getSharedPreferences("prefs",0);
                SharedPreferences.Editor editor=pref.edit();
                Boolean register_pref = pref.getBoolean("register_pref", false);
                if (register_pref == false) {

                    editor.putBoolean("register_pref", true);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), Register.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}
