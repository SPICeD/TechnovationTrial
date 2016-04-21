package com.clemente.spiced.ScreenTimeMonitor;

/**
 * Created by laxmi on 4/1/2016.
 */
public class TechTime {
    private int _id;
    private String CalDate;
    private String DayofWeek;
    private String TimeSpent;

    public TechTime(){}

    public TechTime(String CalDate, String DayofWeek, String TimeSpent) {
        super();
        this.CalDate = CalDate;
        this.TimeSpent = TimeSpent;
        this.DayofWeek = DayofWeek;
    }

    //getters & setters

    @Override
    public String toString() {
        return "Time Spent [_id=" + _id+ "Day=" + DayofWeek + ", date=" + CalDate + ", time=" + TimeSpent
                + "]";
    }

    public String getCalDate() {
        return CalDate;
    }

    public void setCalDate(String calDate) {
        CalDate = calDate;
    }

    public String getDayofWeek() {
        return DayofWeek;
    }

    public void setDayofWeek(String dayofWeek) {
        DayofWeek = dayofWeek;
    }

    public String getTimeSpent() {
        return TimeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        TimeSpent = timeSpent;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
