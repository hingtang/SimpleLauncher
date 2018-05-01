package com.hing.simplelauncher.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hing.simplelauncher.R;
import com.hing.simplelauncher.customViews.CustomBatteryView;
import com.hing.simplelauncher.customViews.CustomCountryView;
import com.hing.simplelauncher.pojo.Country;
import com.hing.simplelauncher.servers.CountryAPI;
import com.hing.simplelauncher.utils.AlertHelper;
import com.hing.simplelauncher.utils.Constants;
import com.hing.simplelauncher.utils.LocationHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements LocationListener, retrofit2.Callback<List<Country>> {
    private final static String TAG = "HomeActivity";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private CustomBatteryView customBatteryView;
    private CustomCountryView customCountryView;

    private BroadcastReceiver batteryInfoReceiver;
    private LocationManager locationManager;
    private Location location;
    private ArrayList<String> permissionList = new ArrayList<>();
    private ArrayList<String> permissionListToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private boolean isGPS = false;
    private boolean isNetwork = false;
    private boolean canGetLocation = true;

    private Retrofit retrofit;
    private Gson gson;
    private CountryAPI api;

    private Retrofit getRetrofit() {
        return (retrofit == null) ? retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build() : retrofit;
    }

    private Gson getGson() {
        return (gson == null) ? gson = new GsonBuilder().setLenient().create() : gson;
    }

    private CountryAPI getCountryApi() {
        return (api == null) ? api = getRetrofit().create(CountryAPI.class) : api;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        registerBatteryinfoReceiver();
        initLocationWidget();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (batteryInfoReceiver != null) {
            unregisterReceiver(batteryInfoReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void init() {
        customBatteryView = findViewById(R.id.customBatteryView);
        customCountryView = findViewById(R.id.customCountryView);
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

    private void initLocationWidget() {
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionListToRequest = findUnAskedPermissions(permissionList);

        if (!isGPS && !isNetwork) {
            AlertDialog alert = AlertHelper.createAlert(this, getString(R.string.title_gps_not_enable), getString(R.string.message_turn_on_gps),
                    getString(R.string.message_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }, getString(R.string.message_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            alert.show();
            location = LocationHelper.getLastLocation(locationManager);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionListToRequest.size() > 0) {
                    requestPermissions(permissionListToRequest.toArray(new String[permissionListToRequest.size()]), ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
            getLocation();
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                if (isGPS) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            updateUI(location);
                        }
                    }
                } else if (isNetwork) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            updateUI(location);
                        }
                    }
                } else {
                    location.setLatitude(0);
                    location.setLongitude(0);
                    updateUI(location);
                }
            } else {
                Log.e(TAG, " Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> permissionList) {
        ArrayList result = new ArrayList();
        for (String permission : permissionList) {
            if (!hasPermission(permission)) {
                result.add(permission);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private void updateUI(Location location) {
        String countryName = LocationHelper.getCountryName(this, location.getLatitude(), location.getLongitude());
        customCountryView.setCountryName(countryName);
        String englishCountryName = LocationHelper.getEnglishCountryName(this, location.getLatitude(), location.getLongitude());
        Log.e(TAG, "country: " + englishCountryName);
        Call<List<Country>> call = getCountryApi().getCountryInfo(englishCountryName);
        call.enqueue(this);
    }

    public void onNavigateToSimpleLauncherClick(View view) {
        Intent intent = new Intent(this, SimpleLauncherActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String permission : permissionListToRequest) {
                    if (!hasPermission(permission)) {
                        permissionsRejected.add(permission);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            AlertHelper.showMessage(this, getString(R.string.message_access_permission),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]),
                                                        ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    } else {
                        Log.d(TAG, "No rejected permissions.");
                        canGetLocation = true;
                        getLocation();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
        if (response.isSuccessful()) {
            for (int i = 0; i < response.body().size(); i++) {
                Country country = response.body().get(i);
                if (!country.getCountryName().isEmpty() && !country.getCapital().isEmpty() && country.getCurrencyList().size() > 0) {
                    customCountryView.setCountry(country);
                    break;
                }
            }
        } else {
            Log.e(TAG, "error: " + response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Country>> call, Throwable t) {
        Log.e(TAG, "failure: " + t.getMessage());
    }
}
