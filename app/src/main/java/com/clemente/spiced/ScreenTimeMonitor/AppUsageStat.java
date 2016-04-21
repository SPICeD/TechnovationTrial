package com.clemente.spiced.ScreenTimeMonitor;

/**
 * Created by 538824 on 4/17/2016.
 */
public class AppUsageStat {
    private String appName;
    private long timeSpent;

    public AppUsageStat(String appName, long timeSpent) {
        this.appName = appName;
        this.timeSpent = timeSpent;
    }
    public AppUsageStat() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getTimeSpent() {
        return timeSpent;
    }


    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }
}

