package com.hing.simplelauncher.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hing.simplelauncher.R;
import com.hing.simplelauncher.customViews.CustomBatteryView;

public class HomeActivity extends AppCompatActivity {
    private CustomBatteryView customBatteryView;

    private BroadcastReceiver batteryInfoReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        registerBatteryinfoReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(batteryInfoReceiver!=null) {
            unregisterReceiver(batteryInfoReceiver);
        }
    }

    private void init() {
        customBatteryView = findViewById(R.id.customBatteryView);
    }

    private void registerBatteryinfoReceiver() {
        batteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                customBatteryView.setBatteryLevel(isCharging, level);
            }
        };

        registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void onNavigateToSimpleLauncherClick(View view) {
        Intent intent = new Intent(this, SimpleLauncherActivity.class);
        startActivity(intent);
    }
}
