package com.ktds.cocomo.mybeacon;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jinyoung on 2016-11-14.
 */
public class ServiceAsyncTask extends AsyncTask<Void, Void, ServiceListAdapter> {

    private Context context;
    String major;
    private ArrayList<ServiceItem> serviceItems = new ArrayList<ServiceItem>();
    ListView serviceListView;
    ServiceListAdapter mListAdapter;
    ProgressDialog progressDialog;

    public ServiceAsyncTask(Context context, String major, ListView serviceListView) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.major = major;
        this.serviceListView = serviceListView;
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
    protected ServiceListAdapter doInBackground(Void... unused) {
        // TODO Auto-generated method stub
        ServiceConnection serviceConnect = new ServiceConnection(major, "http://211.227.149.160:8080/BeaconServer/beacon/Select_Service.jsp");
        serviceConnect.requestLogin();
        for (int i = 0; i < serviceConnect.getCount(); i++) {
            ServiceItem serviceitem = new ServiceItem(serviceConnect.getName(i), serviceConnect.getInfo(i), serviceConnect.getUrl(i));
            serviceItems.add(serviceitem);
        }
        mListAdapter = new ServiceListAdapter(context, serviceItems);

        return mListAdapter;
    }

        @Override
        protected void onPostExecute (ServiceListAdapter mListAdapter){
            super.onPostExecute(mListAdapter);
            serviceListView.setAdapter(mListAdapter);
            progressDialog.dismiss();
            mListAdapter.notifyDataSetChanged();

        }


}

