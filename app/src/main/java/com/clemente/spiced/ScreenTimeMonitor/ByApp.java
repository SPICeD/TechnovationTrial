package com.clemente.spiced.ScreenTimeMonitor;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clemente.spiced.technovationtrial.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class ByApp extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    UsageStatsManager mUsageStatsManager;
    public static String USAGE_STATS_SERVICE_NAME = "usagestats";
    PackageManager packageManager;
    ArrayList<AppUsageStat> appUsageStatList = null;
    ListView byAppListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        packageManager = getApplicationContext().getPackageManager();
        mUsageStatsManager  = (UsageStatsManager)getApplicationContext().getSystemService(USAGE_STATS_SERVICE_NAME);
        appUsageStatList = new ArrayList<AppUsageStat>();
        byAppListView = (ListView)findViewById(R.id.byAppListView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getUsageStatsList();

    }
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(getApplicationContext().APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getUsageStatsList(){
        if(!hasPermission()){
            startActivityForResult(
                    new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    1);
        }
           Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = calendar.getTimeInMillis()-10000;
        List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        appUsageStatList.clear();
        TreeSet<AppUsageStat> tree = new TreeSet<AppUsageStat>(new AppStatComparator());
        int sortOrder= 1;
        int count = usageStatsList.size();
        int maxCount = 0;
        while (count>0 && tree.size()<10) {
            AppUsageStat stat = new AppUsageStat();
            String packageName = usageStatsList.get(0).getPackageName();
            long totalTimeForPackage = 0;
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                String appName= (String) packageManager.getApplicationLabel(appInfo);
                for (int i=0; i<usageStatsList.size();i++) {
                    if (usageStatsList.get(i).getTotalTimeInForeground() > 0) {
                        if(packageName.equalsIgnoreCase(usageStatsList.get(i).getPackageName())) {
                            totalTimeForPackage = totalTimeForPackage + usageStatsList.get(i).getTotalTimeInForeground();
                            System.out.println("@@@@@@@@@@ Usage " + "appName : " + appName + " " + totalTimeForPackage);
                        }
                    }
                }
                if(totalTimeForPackage > 0) {
                    stat.setAppName( appName + " --> " + getDurationBreakdown(totalTimeForPackage));
                    stat.setTimeSpent(totalTimeForPackage);
                    //appUsageStatList.add(stat);
                    tree.add(stat);
                    }

                ListIterator<UsageStats> itr= usageStatsList.listIterator();
                while(itr.hasNext()){
                    UsageStats stats = itr.next();
                    if(packageName.equals(stats.getPackageName())){
                        itr.remove();
                    }
                }
                count = usageStatsList.size();
            System.out.println("Usage " + "appName : " + stat.getAppName() + " Cumulative: " + stat.getTimeSpent());
            } catch (Exception e) {

            }


            //   Toast.makeText(getApplicationContext(),
            //         "Usage: " + appUsageStatList.toString(), Toast.LENGTH_LONG).show();
            appUsageStatList.clear();
            appUsageStatList.addAll(tree);
            ByAppStatsAdaptor adapter = new ByAppStatsAdaptor(this.getApplicationContext(), appUsageStatList);
            byAppListView.setAdapter(adapter);

        }
    }

    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String result = String.format("%1$02d:%2$02d:%3$02d",
                hours,  minutes, seconds );

        return(result);
    }
    public void getProcessName(View view) {
        System.out.println("getProcessName");
        String foregroundProcess = "";
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        // Process running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*180, time);
            // Sort the stats by the last time used
            if(stats != null) {
                System.out.println("11111");
                for (UsageStats stat:stats ){
                    System.out.println("11111 " + stat.getPackageName());
                }
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                }
                if(mySortedMap != null && !mySortedMap.isEmpty()) {
                    System.out.println("22222");
                    String topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    foregroundProcess = topPackageName;
                }else{
                    System.out.println("333333");
                }
            }
        } else {
            @SuppressWarnings("deprecation") ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
            foregroundProcess = foregroundTaskInfo.topActivity.getPackageName();

        }
        System.out.println(foregroundProcess);
        //return foregroundProcess;
    }
}
