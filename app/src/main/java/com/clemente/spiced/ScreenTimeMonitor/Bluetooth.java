package com.clemente.spiced.ScreenTimeMonitor;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.clemente.spiced.technovationtrial.R;

public class Bluetooth extends AppCompatActivity {
    private Chronometer chronometerBlueTooth;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PAIRED_DEVICE = 2;
    Button btnDevice1;
    TextView deviceName1;
    SharedPreferences.Editor editor;
    IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
    IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
    IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        deviceName1 = (TextView) findViewById(R.id.DeviceName1);
        SharedPreferences settings = getSharedPreferences("Answers", MODE_PRIVATE);
        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
        chronometerBlueTooth = (Chronometer) findViewById(R.id.chronometerDevice1);
        editor = settings.edit();
        deviceName1.setText(settings.getString("device1", ""));
        if (!settings.getString("device1", "").equals("")){
            editor.putString("device1", deviceName1.getText().toString());
            deviceName1.setText(settings.getString("device1", ""));
            editor.commit();
        }
    }
    public void openListPairedDevices(View view) {
        System.out.println("open screen method");
        Intent intent = new Intent(this, ListPairedDevicesActivity.class);
        startActivityForResult(intent, 1 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences settings = getSharedPreferences("Answers", MODE_PRIVATE);
        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){

            deviceName1.setText(data.getStringExtra("listItem"));
            editor.putString("device1", deviceName1.getText().toString());
            editor.commit();
            deviceName1.setText(settings.getString("device1", ""));
            editor = settings.edit();
            if (!settings.getString("device1", "").equals("")){
                editor.putString("device1", deviceName1.getText().toString());
                deviceName1.setText(settings.getString("device1", ""));
                editor.commit();
            }
            try {
                Intent intent = new Intent(BluetoothDevice.ACTION_ACL_CONNECTED);
                BluetoothDevice device = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);//
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //       }if (requestCode == REQUEST_PAIRED_DEVICE) {
//                deviceName1.setText(data.getExtras().getString("listItem"));
            }
        }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
/*
            this.registerReceiver(mReceiver, filter1);
            this.registerReceiver(mReceiver, filter2);
            this.registerReceiver(mReceiver, filter3);
*/
           if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
               chronometerBlueTooth.setBase(SystemClock.elapsedRealtime() - chronometerBlueTooth.getBase());
               chronometerBlueTooth.setTextColor(getResources().getColor(R.color.red));
               chronometerBlueTooth.start();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
               chronometerBlueTooth.stop();
            }
        }
    };

}


