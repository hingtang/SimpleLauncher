package com.hing.simplelauncher.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hing.simplelauncher.R;

/**
 * TODO: document your custom view class.
 */
public class CustomBatteryView extends LinearLayout {
    private ImageView imgvBattery;
    private TextView tvBattery;

    public CustomBatteryView(Context context) {
        super(context);
        init(context);
    }

    public CustomBatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBatteryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_battery_view, this, true);

        imgvBattery = findViewById(R.id.imgvBattery);
        tvBattery = findViewById(R.id.tvBattery);
    }

    public void setBatteryLevel(boolean isCharging, int level){
        tvBattery.setText(level+"%");
        if(isCharging){
            imgvBattery.setImageResource(R.drawable.batter_in_charging);
        }else if(level>75){
            imgvBattery.setImageResource(R.drawable.battery_full);
        }else if(level>50){
            imgvBattery.setImageResource(R.drawable.battery_third);
        }else if(level>25){
            imgvBattery.setImageResource(R.drawable.battery_half);
        }else if (level>0){
            imgvBattery.setImageResource(R.drawable.battery_quarter);
        }else{
            imgvBattery.setImageResource(R.drawable.battery_empty);
        }
    }
}
