package com.clemente.spiced.ScreenTimeMonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clemente.spiced.technovationtrial.R;

import java.util.ArrayList;

/**
 * Created by 538824 on 4/17/2016.
 */
public class ByAppStatsAdaptor extends ArrayAdapter<AppUsageStat>{
    public ByAppStatsAdaptor(Context context, ArrayList<AppUsageStat> appUsageStats) {
        super(context, 0, appUsageStats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AppUsageStat appUsageStat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.byappvalue, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvAppName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvTime);
        // Populate the data into the template view using the data object
        tvName.setText(appUsageStat.getAppName());
       // tvHome.setText(appUsageStat.getTimeSpent());
        // Return the completed view to render on screen
        return convertView;
    }
}
