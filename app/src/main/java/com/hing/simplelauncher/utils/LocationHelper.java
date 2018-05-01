package com.hing.simplelauncher.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by HingTang on 5/1/18.
 */
public class LocationHelper {

    public static Location getLastLocation(LocationManager locationManager) {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            return locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCountryName(Context context, double latitude, double longtitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList = null;
        try{
            addressList = geocoder.getFromLocation(latitude, longtitude,1);
            if(addressList!=null && !addressList.isEmpty()){
                return addressList.get(0).getCountryName();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getEnglishCountryName(Context context, double latitude, double longtitude){
        Geocoder geocoder = new Geocoder(context, Locale.US);
        List<Address> addressList = null;
        try{
            addressList = geocoder.getFromLocation(latitude, longtitude,1);
            if(addressList!=null && !addressList.isEmpty()){
                return addressList.get(0).getCountryName();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
