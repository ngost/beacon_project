package com.ktds.cocomo.mybeacon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;

public class MainActivity extends TabActivity {

    private Boolean eventisConnected;
    private Boolean serviceIsConnected;
    private String major;
    BeaconManager beaconManager;
    MenuConnection serverConnect;
    EventConnection eventConnect;
    ServiceConnection serviceConnect;
    private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    private ArrayList<EventItem> eventItems = new ArrayList<EventItem>();
    private ArrayList<ServiceItem> serviceItems = new ArrayList<ServiceItem>();
    MenuListAdapter mListAdapter;
    ProgressDialog progDialog;
    Context context = this;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = new BeaconManager(this);
        serviceIsConnected = false;
        eventisConnected = false;
        major = getIntent().getStringExtra("major");
        Log.d("major=",major);

        //final ProgressDialog dialog = ProgressDialog.show(context, "Please wait..", "Doing stuff..", true);
        //dialog.setCancelable(true);
       // final Runnable myRun = new Runnable(){
         //   public void run(){
          //      Looper.myLooper().prepare();
                //DO EVERYTHING YOU WANT!

                //Finally
            //    runOnUiThread(new Runnable() {
             //       @Override
            //        public void run() {
            //            dialog.dismiss();
           //         }
          ///      });
         //   }
      //  };


        //new ProgressAsyncTask(context).execute();






        Thread jinThread = new Thread(new Runnable() {
            @Override
            public void run() {
                serverConnect = new MenuConnection(major,"http://211.227.149.160:8080/BeaconServer/beacon/Select_Menu.jsp");
                serverConnect.requestLogin();
                    for(int i = 0; i<serverConnect.getCount(); i++){
                        MenuItem menuitem = new MenuItem(serverConnect.getMajor(i),serverConnect.getName(i),serverConnect.getPrice(i)
                        ,serverConnect.getUrl(i),serverConnect.getContent(i),serverConnect.getPrefer(i));
                        menuItems.add(menuitem);
                }
            }
        });
        jinThread.start();
        try{
            jinThread.join();
        }catch (Exception e){
            Log.d("error", "error");
        }

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imgBtn = (ImageButton) v.getTag(R.id.goodButton);
                imgBtn.setImageResource(R.drawable.love);
                mListAdapter.notifyDataSetChanged();
            }
        };


        final TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpectab1 = tabHost.newTabSpec("첫번째탭").setIndicator("메뉴");
        tabSpectab1.setContent(R.id.tab1);
        tabHost.addTab(tabSpectab1);

        ListView menuListView = (ListView)findViewById(R.id.MenuList);

        mListAdapter = new MenuListAdapter(this,menuItems,mOnClickListener);
        menuListView.setAdapter(mListAdapter);

        TabHost.TabSpec tabSpectab2 = tabHost.newTabSpec("두번째탭").setIndicator("이벤트");
        tabSpectab2.setContent(R.id.tab2);
        tabHost.addTab(tabSpectab2);


        TabHost.TabSpec tabSpectab3 = tabHost.newTabSpec("세번째탭").setIndicator("제휴서비스");
        tabSpectab3.setContent(R.id.tab3);
        tabHost.addTab(tabSpectab3);

        tabHost.setCurrentTab(0);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("두번째탭")) {
                    //if (!eventisConnected) {
                        ListView eventListView = (ListView) findViewById(R.id.EventList);
                        EventAsyncTask eventAsyncTask = new EventAsyncTask(context, major, eventListView);
                        eventAsyncTask.execute();
                      //  eventisConnected = true;
                    //}
                }
                if (tabId.equals("세번째탭")) {
                    ListView serviceListView = (ListView) findViewById(R.id.ServiceList);
                    ServiceAsyncTask serviceAsyncTask = new ServiceAsyncTask(context, major, serviceListView);
                    serviceAsyncTask.execute();
                    //serviceIsConnected = true;

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }
}
