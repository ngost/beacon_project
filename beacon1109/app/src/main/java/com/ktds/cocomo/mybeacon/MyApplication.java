package com.ktds.cocomo.mybeacon;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

/**
 * Application 설치가 끝나고 최초로 실행될 때 수행되는 코드
 * Created by MinChang Jang on 2016-06-23.
 */
public class MyApplication extends Application {
    private BeaconManager beaconManager;

    /**
     * Application을 설치할 때 실행됨.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());

        // Application 설치가 끝나면 Beacon Monitoring Service를 시작한다.
        // Application을 종료하더라도 Service가 계속 실행된다.
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",

                        // 본인이 연결 할 비콘의 아이디 부분
                        UUID.fromString("aaaaaaaa-bbbb-bbbb-cccc-ccccdddddddd"),

                        // 본인이 연결 할 비콘의 Major ID
                        null,//이건 과연...

                        // 본인이 연결 할 비콘의 Minor ID
                        null));

                /**
                 * 비콘에게 아이디, Major, Minor를 보낼 것이다.
                 */
            }
        });

        // Android 단말이 Beacon 의 송신 범위에 들어가거나, 나왔을 때를 체크한다.
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                // 이미 앱이 실행중이면 Notification만 줍니다.
                if(!isAlreadyRunActivity() ) {
                    showNotification("매장이 있어요", "클릭 해봐요!",list.get(0));
                } else {
                    showNotification("매장이 있어요", "클릭 해봐요!",list.get(0));
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                 //showNotification("매장에서 나감", "다음에 다시 와요!",null);
            }
        });
    }


    /**
     * Notification으로 Beacon 의 신호가 연결되거나 끊겼음을 알림.
     *
     * @param title
     * @param message
     */
    public void showNotification(String title, String message, Beacon beacon) {

        String msg = "비콘접근";
        try {
            msg = URLDecoder.decode(msg, "EUC-KR");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(getApplicationContext(), SplashActivity.class);
//        newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        newIntent.addFlags(newIntent.FLAG_ACTIVITY_NEW_TASK | newIntent.FLAG_ACTIVITY_CLEAR_TOP | newIntent.FLAG_ACTIVITY_SINGLE_TOP);

        //Log.d("major : ",String.valueOf(beacon.getMajor()));
        String beaconMajor="";
        if(beacon !=null){
            beaconMajor = String.valueOf(beacon.getMajor());
            newIntent.putExtra("major", beaconMajor);
        }

        PendingIntent penderIntent = PendingIntent.getActivity(getApplicationContext(), 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        /*try{
            penderIntent.send();
        }catch(Exception e){
            e.printStackTrace();
        }*///이부분을 지운 결과, 자동 실행이 되지 않는다. 다행이다 ㅠ


        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentIntent(penderIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setTicker(message);
        Notification notification = builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1234, notification);

    }

    /**
     *
     * @return
     */
    private boolean isAlreadyRunActivity() {

        ActivityManager activity_manager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task_info =
                activity_manager.getRunningTasks(9999);

        String activityName = "";
        for( int i = 0; i < task_info.size(); i++ ) {
            activityName = task_info.get(i).topActivity.getPackageName();

            if( activityName.startsWith("com.ktds.cocomo.mybeacon") ) {
                return true;
            }
        }
        return false;
    }
}