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
import android.util.Log;
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
        SharedPreferences prefs = getSharedPreferences("Answers", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
         mReceiver = new ScreenReceiver();
        Calendar cal = Calendar.getInstance();
        int thisDay = cal.get(Calendar.DAY_OF_YEAR);
        long todayMillis = cal.getTimeInMillis();
        long chronometerTimeSec = getDBTime();
     //   long chronometerTimeSec = prefs.getLong("chronometerTimeSec", 0);
        Log.d(MyApplication.LOG,"-------> OnCreate chronometerTimeSec " + chronometerTimeSec);

        chronometer  = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - prefs.getLong("chronometerTimeSec", 0));
        chronometer.setTextColor(getResources().getColor(R.color.red));
        chronometer.setOnChronometerTickListener(
                new Chronometer.OnChronometerTickListener() {

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long myElapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        SharedPreferences settings = getSharedPreferences("Answers", MODE_PRIVATE);
                        String phoneNumberText = settings.getString("phoneNumberText", "");
                        String minutesText = settings.getString("minutesText", "");
                        long myElapsedSecs = myElapsedMillis / 1000;
                        //String timeSpent = map
                        if ((minutesText != null) && (minutesText.length() > 0) && myElapsedSecs % (Integer.parseInt(minutesText) * 60) == 0) {
                            sendSMS(phoneNumberText, "Your kid whose screen time you're monitoring spent " + MyApplication.convertToTime(myElapsedSecs) + " on their phone today!");
                        }
                    }
                }
        );

        MyApplication app = ((MyApplication)getApplicationContext());
        app.setChronometer(chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - (chronometerTimeSec * 1000));
        Log.d(MyApplication.LOG,"-------> OnCreate chronometer Start");
        chronometer.start();
        long last = prefs.getLong("date", 0);
        cal.setTimeInMillis(last);
/*        int lastDay = cal.get(Calendar.DAY_OF_YEAR);

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
*/
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
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void openInstructions(View view) {
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }
    public void openGraph(View view) {
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, Graph.class);
        startActivity(intent);
    }
    public void openTable(View view) {
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, Table.class);
        startActivity(intent);
    }
    public void openByApp(View view) {
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, ByApp.class);
        startActivity(intent);
    }
    public void openBluetooth(View view) {
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);
    }
    public void openTips(View view) {
        Log.d(MyApplication.LOG,"open screen method");
        Intent intent = new Intent(this, Tips.class);
        startActivity(intent);
    }
    public void openReason(View view) {
        Log.d(MyApplication.LOG,"open screen method");
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

        String chronometer_time = chronometer.getText().toString();
        long chronometerTimeSec = MyApplication.getTime(chronometer_time);
        SharedPreferences prefs = getSharedPreferences("Answers", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("chronometer_get_text", chronometer_time);
        editor.putLong("chronometerTimeSec", chronometerTimeSec);
        Log.d(MyApplication.LOG,"===>> onPause chronometerTimeSec " + chronometerTimeSec);
        editor.commit();
        if (!ScreenReceiver.wasScreenOn) {
            screenTurnedOff = true;
            Log.d(MyApplication.LOG,"===>> onPause ===>>OFF chronometer_time " + chronometer_time + " Screen OFF");
            chronometer.stop();

        } else {
            screenTurnedOff=false;
            Log.d(MyApplication.LOG,"===>> onPause ===> chronometer_time " + chronometerTimeSec + " Screen ON");
            if(chronometerTimeSec>0){
                editor.putLong("chronometerTimeSec", chronometerTimeSec);
                editor.commit();
                List<TechTime> times = db.getAllTechTimes(1);
                if(times!= null && times.size()==1){
                    TechTime techTime = times.get(0);
                    Log.d(MyApplication.LOG," techTime  " + techTime.toString());
                    String dbTime = techTime.getTimeSpent();
                    long dbTimeSec = MyApplication.getTime(dbTime);
                    Log.d(MyApplication.LOG," timeSpent from DB " + dbTime + " long timeSpent:" + dbTimeSec);
                    long totalTimeSec = dbTimeSec + chronometerTimeSec;
                    Log.d(MyApplication.LOG," totalTimeSec  " + totalTimeSec);

                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    Log.d(MyApplication.LOG," Today is " + MyApplication.getDayOfTheWeek(day));
                    Log.d(MyApplication.LOG," techTime.getCalDate()  " + techTime.getCalDate());
                    if(techTime.getCalDate().trim().equalsIgnoreCase(MyApplication.getDayOfTheWeek(day).trim())) {
                        Log.d(MyApplication.LOG,"Dates Match.  Update Record");
                        db.updateTechTime(techTime.get_id(), MyApplication.convertToTime(chronometerTimeSec));
                    }else{
                        Log.d(MyApplication.LOG,"Today is differet. Add Record");
                        MyApplication.addRecordToDB(db, chronometerTimeSec);
                    }
                }
                else{//No record in DB. So add a new one
                    Log.d(MyApplication.LOG,"-------> No Records in DB ");
                    MyApplication.addRecordToDB(db, chronometerTimeSec);
                }
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, filter);
        SharedPreferences prefs = getSharedPreferences("Answers", 0);
        SharedPreferences.Editor editor= prefs.edit();
        String chronometer_time = chronometer.getText().toString();
        long chronometerTimeSec = MyApplication.getTime(chronometer_time);
        if(ScreenReceiver.wasScreenOn){
            chronometer.setTextColor(getResources().getColor(R.color.red));
            editor.putString("chronometer_get_text", chronometer.getText().toString());
            editor.putLong("chronometerTimeSec", chronometerTimeSec);
            editor.commit();
            Log.d(MyApplication.LOG,"chronometerTimeSec " + prefs.getLong("chronometerTimeSec", 0));
            if(screenTurnedOff) {
               // chronometer.setBase(SystemClock.elapsedRealtime() - (prefs.getLong("chronometerTimeSec", 0)) * 1000);
                chronometer.start();
            }
            else{

            }
         }

    }

    private long getDBTime() {
        long dbTimeSec =0;

        List<TechTime> times = db.getAllTechTimes(1);
        if (times != null && times.size() == 1) {
            TechTime techTime = times.get(0);
            Log.d(MyApplication.LOG, " techTime  " + techTime.toString());
            String dbTime = techTime.getTimeSpent();
            dbTimeSec = MyApplication.getTime(dbTime);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            Log.d(MyApplication.LOG, " Today is " + MyApplication.getDayOfTheWeek(day));
            Log.d(MyApplication.LOG, " techTime.getCalDate()  " + techTime.getCalDate());
            if (!techTime.getCalDate().trim().equalsIgnoreCase(MyApplication.getDayOfTheWeek(day).trim())) {
                dbTimeSec = 0;
            }
        }
        return dbTimeSec;
    }
/*
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(MyApplication.LOG,("4444444444444 destroy");
        unregisterReceiver(mReceiver);
    }
    */
}









