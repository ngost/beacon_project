package com.ktds.cocomo.mybeacon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MenuClickListener implements View.OnClickListener {
    Context context;
    ArrayList<String> menu_info;
    Bitmap bm;
    public MenuClickListener(Context context, ArrayList<String> para, Bitmap bm) {
        this.context = context;
        this.bm = bm;
        this.menu_info = para;
    }

    public void onClick(View v) {
        Intent intent = new Intent(context, MenuClickActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent.putExtra("BMP",bytes);

        intent.putExtra("menu_info",menu_info);
        context.startActivity(intent);
    }
}