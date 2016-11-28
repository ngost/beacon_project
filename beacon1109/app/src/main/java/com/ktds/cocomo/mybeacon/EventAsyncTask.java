package com.ktds.cocomo.mybeacon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by jinyoung on 2016-11-14.
 */
public class EventAsyncTask extends AsyncTask<Void, Void, EventListAdapter> {

    Bitmap bmp;
    private Context context;
    String major;
    LayoutInflater inflater;
    View v;
    private ArrayList<EventItem> eventItems = new ArrayList<EventItem>();
    ListView eventListView;
    EventListAdapter mListAdapter;
    ProgressDialog progressDialog;

    public EventAsyncTask(Context context, String major, ListView eventListView) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.major = major;
        this.eventListView = eventListView;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected EventListAdapter doInBackground(Void... unused) {
        // TODO Auto-generated method stub
        EventConnection eventConnect = new EventConnection(major, "http://211.227.149.160:8080/BeaconServer/beacon/Select_Event.jsp");
        eventConnect.requestLogin();
        for (int i = 0; i < eventConnect.getCount(); i++) {
            EventItem eventitem = new EventItem(eventConnect.getSubject(i), eventConnect.getContent(i), eventConnect.getUrl(i)
                    , eventConnect.getIng(i));
            eventItems.add(eventitem);
            Log.d("item: ",eventitem.e_ing);
        }
        mListAdapter = new EventListAdapter(context, eventItems);

        return mListAdapter;
    }

        @Override
        protected void onPostExecute (EventListAdapter mListAdapter){
            super.onPostExecute(mListAdapter);
//            for(int i=0;i<mListAdapter.getCount();i++)
//            {
//                setBmpFromUrl(mListAdapter.e_url.get(i));
//                mListAdapter.bm.add();
//            }

            eventListView.setAdapter(mListAdapter);
            progressDialog.dismiss();
            mListAdapter.notifyDataSetChanged();

        }


}

