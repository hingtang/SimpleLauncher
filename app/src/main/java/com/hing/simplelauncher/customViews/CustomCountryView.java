package com.hing.simplelauncher.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hing.simplelauncher.R;
import com.hing.simplelauncher.pojo.Country;

/**
 * Created by HingTang on 5/1/18.
 */
public class CustomCountryView extends LinearLayout {
    private TextView tvName;
    private TextView tvCapital;
    private TextView tvCurrency;

    public CustomCountryView(Context context) {
        super(context);
    }

    public CustomCountryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCountryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomCountryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_country_view, this, true);

        tvName = findViewById(R.id.tvName);
        tvCapital = findViewById(R.id.tvCapital);
        tvCurrency = findViewById(R.id.tvCurrency);
    }

    public void setCountryName(String countryName){
        tvName.setText(countryName);
    }

    public void setCountry(Country country){
        if(tvName.getText()==null || tvName.getText().length()<=0){
            tvName.setText(country.getCountryName());
        }
        tvCapital.setText(country.getCapital());
        tvCurrency.setText(country.getCurrencyList().get(0).getCurrencyName());
    }
}
