package com.ktds.cocomo.mybeacon;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import java.util.ArrayList;

public class EventClickListener implements View.OnClickListener {
    Context context;
    ArrayList<String> event_info;

    public EventClickListener(Context context, ArrayList<String> para) {
        this.context = context;
        this.event_info = para;
    }

    public void onClick(View v) {

        Intent intent = new Intent(context, EventClickActivity.class);
        intent.putExtra("event_info",event_info);
        context.startActivity(intent);
    }
}