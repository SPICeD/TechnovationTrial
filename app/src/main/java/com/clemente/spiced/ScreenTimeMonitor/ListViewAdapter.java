package com.clemente.spiced.ScreenTimeMonitor;


import java.util.List;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clemente.spiced.technovationtrial.R;

/**
 * Created by laxmi on 4/3/2016.
 */
public class ListViewAdapter extends BaseAdapter {

        public List<TechTime> list;
        Activity activity;
        TextView txtId;
        TextView txtDayOfWeek;
        TextView txtCalDate;
        TextView txtTimeSpent;
        public ListViewAdapter(AppCompatActivity activity, List<TechTime> list){
            super();
            this.activity=activity;
            this.list=list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater=activity.getLayoutInflater();
            if(convertView == null){
                convertView=inflater.inflate(R.layout.addxml, null);
                txtId=(TextView) convertView.findViewById(R.id.txtId);
                txtDayOfWeek=(TextView) convertView.findViewById(R.id.dayOfWeek);
                txtCalDate=(TextView) convertView.findViewById(R.id.calDate);
                txtTimeSpent=(TextView) convertView.findViewById(R.id.timeSpent);

            }
           // position=0;
            TechTime  techTime=list.get(position);
            if(techTime!=null) {
     //           System.out.println("3333333333333333333333333333333333333 " + techTime.toString());
                txtId.setText(Integer.toString(techTime.get_id()));
                txtDayOfWeek.setText(techTime.getDayofWeek());
                txtCalDate.setText(techTime.getCalDate());
                txtTimeSpent.setText(techTime.getTimeSpent());
            }
            return convertView;
        }

    }

