package com.ktds.cocomo.mybeacon;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by jinyoung on 2016-11-14.
 */
public class ProgressAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private Context context_2;
    ProgressDialog dialog;

    public ProgressAsyncTask(Context context_1) {
        // TODO Auto-generated constructor stub
        this.context_2=context_1;
        dialog = new ProgressDialog(context_1);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Test");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Bitte warten");
        dialog.setCancelable(false);
        dialog.show();
    }



    @Override
    protected Boolean doInBackground(Void... unused) {
        // TODO Auto-generated method stub

        try{
            Thread.sleep(3000);
        }catch(InterruptedException e)
        {
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        dialog.dismiss();
    }

}