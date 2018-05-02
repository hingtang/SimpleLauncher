package com.hing.simplelauncher.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.hing.simplelauncher.interfaces.ILocationUpdateListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by HingTang on 5/1/18.
 */
public class LocationHelper {
    private static final String TAG = "LocationHelper";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    public static void getLocation(boolean canGetLocation, LocationManager locationManager,
                                   ILocationUpdateListener locationUpdateListener, LocationListener locationListener) {
        Location location = getLastLocation(locationManager);
        try {
            if (canGetLocation) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            locationUpdateListener.getLoation(location);
                        }
                    }
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            locationUpdateListener.getLoation(location);
                        }
                    }
                } else {
                    location.setLatitude(0);
                    location.setLongitude(0);
                    locationUpdateListener.getLoation(location);
                }
            } else {
                Log.e(TAG, " Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

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
