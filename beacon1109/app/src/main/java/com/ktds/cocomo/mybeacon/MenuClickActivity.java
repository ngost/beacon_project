package com.ktds.cocomo.mybeacon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MenuClickActivity extends Activity {

    Bitmap bm;
    ArrayList<String> menu_info;
    TextView Menu_textView, Menu_Title, RatingResultText;
    Button EstimateBtn;
    ReferenceConnection referenceConnection;
    RatingBar RatingBar;

    private CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Menu_Title = (TextView)findViewById(R.id.menu_title);
        Menu_textView = (TextView)findViewById(R.id.menu_info_text);
        EstimateBtn = (Button)findViewById(R.id.estimateBtn);



        byte[] bytes = getIntent().getByteArrayExtra("BMP");
        bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        menu_info = (ArrayList<String>) getIntent().getSerializableExtra("menu_info");


        ImageView imageView = (ImageView)findViewById(R.id.menulistenerImg);
        imageView.setImageBitmap(bm);
        Menu_Title.setText(menu_info.get(0));
        Menu_textView.setText(" "+ menu_info.get(2));

    }


    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.estimateBtn:
                mCustomDialog = new CustomDialog(this,
                        menu_info.get(0), // 제목
                        menu_info.get(0)+"에 대해 평가해주세요", // 내용
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener); // 오른쪽 버튼 이벤트
                mCustomDialog.show();
                break;
        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            referenceConnection = new ReferenceConnection(menu_info.get(3), menu_info.get(0), mCustomDialog.getRating()+"","http://211.227.149.160:8080/BeaconServer/beacon/Update_Preference.jsp");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    referenceConnection.requestLogin();

                }
            });thread.start();
            try {
                thread.join();
                finish();
            }catch (Exception e){

            }

            Toast.makeText(getApplicationContext()," 평가점수 : "+mCustomDialog.getRating()+" 반영 되었습니다.",Toast.LENGTH_LONG).show();


        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

}
