package com.ktds.cocomo.mybeacon;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    String major=null;
    String aaa;
/*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //setIntent(intent);
        //Log.d("major",intent.getStringExtra("beacon").toString());
       // Toast.makeText(getApplicationContext(),"되냐",Toast.LENGTH_LONG).show();
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        String executeType = "start";
        if(getIntent().getStringExtra("major")!=null) {
            major = getIntent().getStringExtra("major");
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                intent = new Intent(SplashActivity.this, MainActivity.class);
                try {
                    if (major!=null) {
                        intent.putExtra("major", major);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if(major == null){
                        Intent justIntent = new Intent(SplashActivity.this, JustActivity.class);
                        startActivity(justIntent);
                        finish();
                    }
                }  catch (NullPointerException e) {
                }


            }
        }, 1000);
    }
}
