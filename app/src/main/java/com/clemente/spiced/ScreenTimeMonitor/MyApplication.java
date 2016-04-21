package com.clemente.spiced.ScreenTimeMonitor;

import android.app.Application;
import android.widget.Chronometer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 538824 on 4/18/2016.
 */
public class MyApplication extends Application {
    Chronometer chronometer;
    MySQLiteHelper db;
    static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public void onCreate(){
        super.onCreate();
    }
    public Chronometer getChronometer() {
        return chronometer;
    }

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }

    public static long getTime(String time){
        int seconds = 0, minutes = 0, hours = 0;
        if(time!=null && time.length()>0) {
            String[] parts = time.split(":");
            int intTime = 0;
            if (parts.length < 2 || parts.length > 3) {
                intTime = Integer.parseInt(time);
            }

            if (parts.length == 2) {
                seconds = Integer.parseInt(parts[1]);
                minutes = Integer.parseInt(parts[0]);
            } else if (parts.length == 3) {
                seconds = Integer.parseInt(parts[2]);
                minutes = Integer.parseInt(parts[1]);
                hours = Integer.parseInt(parts[1]);
            }
        }
     long value = hours * 60 * 60 + minutes * 60 + seconds;
        return value;
    }
    public static void addRecordToDB( MySQLiteHelper db, long chronometer_time){
        Date date = new Date();
        String dayOfWeek="";
        String currentDate = dateFormat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        int intTime = 0;
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;

        dayOfWeek= getDayOfTheWeek(day);

        int seconds = (int) (chronometer_time) % 60 ;
        int minutes = (int) ((chronometer_time / (60)) % 60);
        int hours   = (int) ((chronometer_time / (60*60)) % 24);
        String timeSpent = String.format("%2d:%02d:%02d",
                hours,minutes, seconds);

        intTime = minutes + (hours*60);
        TechTime techTime = new TechTime();
        techTime.setCalDate(currentDate);
        techTime.setDayofWeek(dayOfWeek);
        techTime.setTimeSpent(timeSpent);
        db.addTechTime(techTime);

  }

    public static String convertToTime(long chronometer_time){
        int seconds = (int) (chronometer_time) % 60 ;
        int minutes = (int) ((chronometer_time / (60)) % 60);
        int hours   = (int) ((chronometer_time / (60*60)) % 24);
        String timeSpent = String.format("%2d:%02d:%02d",
                hours,minutes, seconds);

       return timeSpent;
    }

    public static String getDayOfTheWeek(int day){
        String dayOfWeek=null;
        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeek="Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek="Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek="Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek="Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek="Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek="Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek="Saturday";
        }
        return dayOfWeek;

    }
}