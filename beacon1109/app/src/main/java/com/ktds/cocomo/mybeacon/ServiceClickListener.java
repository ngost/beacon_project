package com.ktds.cocomo.mybeacon;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

public class ServiceClickListener implements View.OnClickListener {
    Context context;
    ArrayList<String> service_info;

    public ServiceClickListener(Context context, ArrayList<String> para) {
        this.context = context;
        this.service_info = para;
    }

    public void onClick(View v) {

        Intent intent = new Intent(context, ServiceClickActivity.class);
        intent.putExtra("service_info",service_info);
        context.startActivity(intent);
    }
}