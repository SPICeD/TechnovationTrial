package com.clemente.spiced.ScreenTimeMonitor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laxmi on 4/1/2016.
 */

    public class MySQLiteHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 5;
        private static final String DATABASE_NAME = "TechTimeDB";
    public static final String TABLE_TABLE1 = "TechTime";

    public static final String KEY_ID = "_id";
    public static final String KEY_CALDATE = "CalDate";
    public static final String KEY_DAYOFWEEK = "DayofWeek";
    public static final String KEY_TIMESPENT = "TimeSpent";

    public static final String[] COLUMNS = {KEY_ID,KEY_CALDATE,KEY_DAYOFWEEK,KEY_TIMESPENT};

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE1_TABLE = "CREATE TABLE TechTime ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "DayofWeek TEXT, " +
                    "CalDate TEXT, "+
                    "TimeSpent spent TEXT )";

            db.execSQL(CREATE_TABLE1_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS TechTime");
          this.onCreate(db);
        }
    /*
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        //String delete = "TRUNCATE from TechTime";
        db.delete("TechTime",null,null);
    }
    */
    public void addTechTime(TechTime techTime) {
        Log.d("addTechTime", techTime.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CALDATE, techTime.getCalDate()); // get title
       values.put(KEY_DAYOFWEEK, techTime.getDayofWeek()); // get author
       values.put(KEY_TIMESPENT, techTime.getTimeSpent());

        db.insert(TABLE_TABLE1,
                null,
                values);

        db.close();
    }
    public void updateTechTime(int id, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        String strFilter = "_id=" + id;
        ContentValues args = new ContentValues();
        args.put(KEY_TIMESPENT, value);
        db.update(TABLE_TABLE1, args, strFilter, null);
    }
    public List<TechTime> getAllTechTimes(int daysDisplayed) {
        List<TechTime> techTimes = new ArrayList<TechTime>();
        String query = "SELECT  * FROM " + TABLE_TABLE1 + " ORDER BY " + KEY_ID + " DESC " + " LIMIT " + daysDisplayed;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        TechTime techTime = null;

        try {
            if (cursor.moveToFirst()) {
                do {
                    techTime = new TechTime();
                    techTime.set_id(Integer.parseInt(cursor.getString(0)));
                    techTime.setCalDate(cursor.getString(1));
                    techTime.setDayofWeek(cursor.getString(2));
                    techTime.setTimeSpent(cursor.getString(3));
                    techTimes.add(techTime);

                } while (cursor.moveToNext());
            }
        }
        catch(Exception e){
            e.printStackTrace();
    } finally {
        cursor.close();
    }
        return techTimes;
    }
/*
    public Cursor getDetails() {
        String query = "SELECT  * FROM " + TABLE_TABLE1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
  */
}

