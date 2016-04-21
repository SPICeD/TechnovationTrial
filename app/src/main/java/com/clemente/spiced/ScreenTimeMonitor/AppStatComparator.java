package com.clemente.spiced.ScreenTimeMonitor;

import java.util.Comparator;


public class AppStatComparator implements Comparator<AppUsageStat> {

    @Override
    public int compare(AppUsageStat e1, AppUsageStat e2) {
        if(e1.getTimeSpent() < e2.getTimeSpent()){
            return 1;
        } else {
            return -1;
        }
    }

}
