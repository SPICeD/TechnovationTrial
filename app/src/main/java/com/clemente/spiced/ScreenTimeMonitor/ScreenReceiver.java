package com.clemente.spiced.ScreenTimeMonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.Chronometer;

import java.util.List;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication mApplication = ((MyApplication)context.getApplicationContext());
        MySQLiteHelper db = new MySQLiteHelper(context);

        Chronometer chronometer =mApplication.getChronometer();
        SharedPreferences prefs = context.getSharedPreferences("myPrefs",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =prefs.edit();
        System.out.println("AAAAAAAAAAAAAAAAAAA");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
            String chronometer_time = chronometer.getText().toString();
           long time= mApplication.getTime(chronometer_time);
            if(time>0){
                editor.putLong("chronometer_get_long", time);
                editor.commit();
                List<TechTime> times = db.getAllTechTimes(1);
                if(times!= null && times.size()==1){
                    System.out.println("222222222222222222 AAAAAAAAAAAA");
                    TechTime techTime = times.get(1);
                    String timeSpent = techTime.getTimeSpent();
                    long timeSpentLong = mApplication.getTime(timeSpent);
                    long totalTime = timeSpentLong + time;
                    db.updateTechTime(techTime.get_id(), mApplication.convertToTime(totalTime));
                }
                System.out.println("33333333333333 AAAAAAAAAAAAAAAAAAA");

                MyApplication.addRecordToDB(db, time);
            }
            chronometer.stop();

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn = true;
            System.out.println("444444444444444444 AAAAAAAAAAAAAAAAAAA");

            chronometer.setBase(SystemClock.elapsedRealtime() - prefs.getLong("chronometer_get_long", 0));
            chronometer.start();
            System.out.println("!!!!!!!!!!!!!ACTION_SCREEN_ON");
        }
    }
}

