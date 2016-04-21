package com.clemente.spiced.ScreenTimeMonitor;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Chronometer;

import com.clemente.spiced.technovationtrial.R;

import java.util.Calendar;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    private Chronometer chronometer;
    MySQLiteHelper db=  new MySQLiteHelper(this);
    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

    private AlarmManager alarmMgr;
    BroadcastReceiver mReceiver;
    boolean screenTurnedOff=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
         mReceiver = new ScreenReceiver();
        Calendar cal = Calendar.getInstance();
        int thisDay = cal.get(Calendar.DAY_OF_YEAR);
        long todayMillis = cal.getTimeInMillis();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        System.out.println("OnCreate chronometer_get_long " + prefs.getLong("chronometer_get_long", 0));

        chronometer  = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime()-prefs.getLong("chronometer_get_long", 0));
        chronometer.setTextColor(getResources().getColor(R.color.red));
        chronometer.setOnChronometerTickListener(
                new Chronometer.OnChronometerTickListener(){

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long myElapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        SharedPreferences settings = getSharedPreferences("Answers", MODE_PRIVATE);
                        String phoneNumberText = settings.getString("phoneNumberText", "");
                        String minutesText = settings.getString("minutesText","");
                        long myElapsedSecs =  myElapsedMillis/1000;
                        //String timeSpent = map
                        if((minutesText!= null) && (minutesText.length() > 0) && myElapsedSecs%(Integer.parseInt(minutesText)*60)==0) {
                            sendSMS(phoneNumberText, "Your kid whose screen time you're monitoring spent " + MyApplication.convertToTime(myElapsedSecs) + " on their phone today!");
                        }
                    }}
        );

        MyApplication app = ((MyApplication)getApplicationContext());
        app.setChronometer(chronometer);
        chronometer.start();
        long last = prefs.getLong("date", 0);
        cal.setTimeInMillis(last);
        int lastDay = cal.get(Calendar.DAY_OF_YEAR);

        if( last==0 || lastDay != thisDay ){
            SharedPreferences.Editor edit = prefs.edit();
            edit.putLong("date", todayMillis);
            if (last != 0){
             //   addRecordToDB(db, );
            }
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            edit.commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void openScreen(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void openInstructions(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }
    public void openGraph(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Graph.class);
        startActivity(intent);
    }
    public void openTable(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Table.class);
        startActivity(intent);
    }
    public void openByApp(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, ByApp.class);
        startActivity(intent);
    }
    public void openBluetooth(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);
    }
    public void openTips(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Tips.class);
        startActivity(intent);
    }
    public void openReason(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, Reason.class);
        startActivity(intent);
    }
    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

        System.out.println("!!!!!!!!!!!111111 chronometer_time " + "onPause");
        SharedPreferences prefs = getSharedPreferences("Answers", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("chronometer_get_text", chronometer.getText().toString());
        System.out.println("chronometer_get_long " + MyApplication.getTime(chronometer.getText().toString()));

        editor.putLong("chronometer_get_long", MyApplication.getTime(chronometer.getText().toString()));
        editor.commit();
        if (!ScreenReceiver.wasScreenOn) {
            System.out.println("!!!!!!!!!!!111111 chronometer_time " + chronometer.getText());
            chronometer.stop();
        } else {
            long timeWhenStopped = 0;
            boolean week = prefs.getBoolean("week", false);
            boolean month = prefs.getBoolean("month", false);
            String chronometer_time = chronometer.getText().toString();
            long time = MyApplication.getTime(chronometer_time);
//            MyApplication.addRecordToDB(db, time);
            System.out.println("111111 time " + time);
            if(time>0){
                editor.putLong("chronometer_get_long", time);
                editor.commit();
                List<TechTime> times = db.getAllTechTimes(1);
                if(times!= null && times.size()==1){
                    TechTime techTime = times.get(0);
                    String timeSpent = techTime.getTimeSpent();
                    System.out.println(" timeSpent " + timeSpent);
                    long timeSpentLong = MyApplication.getTime(timeSpent);
                    System.out.println(" long timeSpent " + timeSpentLong);
                    long totalTime = timeSpentLong* 1000 + time * 1000;
                    System.out.println(" totalTime  " + totalTime);

                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);

                    if(techTime.getDayofWeek().equalsIgnoreCase(MyApplication.getDayOfTheWeek(day))){
                        MyApplication.addRecordToDB(db, time);
                    }else{
                        db.updateTechTime(techTime.get_id(), MyApplication.convertToTime(time));
                    }
                }
                else{
                    MyApplication.addRecordToDB(db, time);
                }
            }
            else{
                MyApplication.addRecordToDB(db, time);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, filter);
        SharedPreferences prefs = getSharedPreferences("Answers", 0);
        boolean week = prefs.getBoolean("week", false);
        boolean month = prefs.getBoolean("month", false);
        SharedPreferences.Editor editor= prefs.edit();
        if(ScreenReceiver.wasScreenOn){
            chronometer.setTextColor(getResources().getColor(R.color.red));
            editor.putString("chronometer_get_text", chronometer.getText().toString());
            System.out.println("chronometer_get_long " + prefs.getLong("chronometer_get_long", 0));
            chronometer.setBase(SystemClock.elapsedRealtime() - (prefs.getLong("chronometer_get_long", 0))*1000);
            chronometer.start();
         }

    }
/*
    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("4444444444444 destroy");
        unregisterReceiver(mReceiver);
    }
    */
}









