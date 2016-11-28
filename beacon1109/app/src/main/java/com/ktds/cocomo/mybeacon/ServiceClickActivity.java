package com.ktds.cocomo.mybeacon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ServiceClickActivity extends Activity {

    Bitmap bm;
    ArrayList<String> service_info;
    ImageView imgview;
    TextView subject;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        service_info = (ArrayList<String>) getIntent().getSerializableExtra("service_info");

        Thread tThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL aURL;
                    aURL = new URL(service_info.get(2));
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    bm = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    Log.e("DEBUGTAG", "이 이미지는 못가져왔어", e);
                }
            }
        });
        tThread.start();
        try {
            tThread.join();
        } catch (Exception e) {
            Log.d("error", "error");
        }

        imgview = (ImageView) findViewById(R.id.servicelistenerImg);
        subject = (TextView) findViewById(R.id.service_name);
        content = (TextView) findViewById(R.id.service_info_text);

        imgview.setImageBitmap(bm);
        subject.setText(service_info.get(0));
        content.setText(service_info.get(1));
    }
}
