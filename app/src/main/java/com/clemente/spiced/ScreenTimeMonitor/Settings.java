package com.clemente.spiced.ScreenTimeMonitor;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.clemente.spiced.technovationtrial.R;

public class Settings extends AppCompatActivity {
    RadioButton radioButtonWeek = null;
    RadioButton radioButtonMonth=null;
    SharedPreferences settings;
    EditText editTextPhone;
    EditText editTextFrequency;

    SharedPreferences.Editor editor;
    Integer daysDisplayed=7;
    Button updateButton;
    String phoneNumberText=null;
    String minutesText=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        System.out.println("@@@@@@@@@@@@@@@@@ On Create");
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


        radioButtonWeek = (RadioButton) findViewById(R.id.radioButton);
        radioButtonMonth = (RadioButton) findViewById(R.id.radioButton2);
        editTextPhone = (EditText) findViewById(R.id.editPhone);
        editTextFrequency = (EditText) findViewById(R.id.editFrequency);
        SharedPreferences settings = getSharedPreferences("Answers", MODE_PRIVATE);

        editor = settings.edit();
        if(settings.getBoolean("week", false)){
            radioButtonWeek.setChecked(true);
        }
        else if (settings.getBoolean("month", false)){
            radioButtonMonth.setChecked(true);
        }
        phoneNumberText =settings.getString("phoneNumberText", "");
        minutesText = settings.getString("minutesText", "");

        editTextPhone.setText(phoneNumberText);
        editTextFrequency.setText(minutesText);

        editor.putBoolean("week", radioButtonWeek.isChecked()); // first argument is a name of a data that you will later use to retrieve it and the second argument is a value that will be stored
        editor.putBoolean("month", radioButtonMonth.isChecked());
        if(phoneNumberText!= null && phoneNumberText.length()>0){
            editor.putString("phoneNumberText", phoneNumberText);
        }
        if(minutesText!= null && minutesText.length()>0){
            editor.putString("minutesText", minutesText);
        }

        editor.commit();
    }
    public void weekClick(View view) {
        editor.putBoolean("week", radioButtonWeek.isChecked());
        editor.putBoolean("month", radioButtonMonth.isChecked());
        editor.commit();
    }
    public void monthClick(View view){
        editor.putBoolean("month", radioButtonMonth.isChecked());
        editor.putBoolean("week", radioButtonWeek.isChecked());
        editor.commit();
        daysDisplayed=30;
    }

    @Override
    protected void onPause() {

        super.onPause();
        SharedPreferences settings = getSharedPreferences("Answers", 0);
        boolean week = settings.getBoolean("week", false);
        boolean month = settings.getBoolean("month", false);
         phoneNumberText = settings.getString("phoneNumberText", "");
         minutesText = settings.getString("minutesText","");
        radioButtonWeek.setChecked(week);
        radioButtonMonth.setChecked(month);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("Answers", 0);
        boolean week = settings.getBoolean("week", false); // The second argument is a default value, if value with name "questionA" will not be found
        boolean month = settings.getBoolean("month", false);
         phoneNumberText = settings.getString("phoneNumberText", "");
         minutesText = settings.getString("minutesText","");
        radioButtonWeek.setChecked(week);
        radioButtonMonth.setChecked(month);
        editTextPhone.setText(phoneNumberText);
        editTextFrequency.setText(minutesText);
    }
    public void updateOnClick(View view){
        editor.putString("phoneNumberText", editTextPhone.getText().toString());
        editor.putString("minutesText", editTextFrequency.getText().toString());
        editor.commit();
    }

}


