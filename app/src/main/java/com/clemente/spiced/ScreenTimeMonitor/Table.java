package com.clemente.spiced.ScreenTimeMonitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.clemente.spiced.technovationtrial.R;

public class Table extends AppCompatActivity {
    MySQLiteHelper db = new MySQLiteHelper(this);
    GridView gridView;
    ListViewAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView=(ListView)findViewById(R.id.listView1);
        SharedPreferences settings = getSharedPreferences("Answers", 0);
        boolean week = settings.getBoolean("week", false); // The second argument is a default value, if value with name "questionA" will not be found
        boolean month = settings.getBoolean("month", false);
        if(week){
             adapter=new ListViewAdapter(
                    this, db.getAllTechTimes(7));
        }else if(month){
            adapter=new ListViewAdapter(
                    this, db.getAllTechTimes(30));
        }
        else{
            System.out.println("111111111111default");
            adapter=new ListViewAdapter(
                    this, db.getAllTechTimes(7));
        }

        listView.setAdapter(adapter);
    }
}
