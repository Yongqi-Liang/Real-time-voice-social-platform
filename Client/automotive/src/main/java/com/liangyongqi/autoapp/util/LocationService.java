package com.liangyongqi.autoapp.util;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;
/**
 * @File: LocationService.java
 * @Author: liangyongqi
 * @Email: 
 * @Date: 2024/3/12
 * @Time: 20:07
 */
public class LocationService extends Service {

    private LocationManager locationManager;
    private String locationProvider = null;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getLocation() {
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v("TAG", "定位方式GPS");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v("TAG", "定位方式Network");
        } else {
            Log.e("TAG", "没有可用的位置提供器");
            stopSelf();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                stopSelf();
                return;
            }
        }

        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        try {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                printLocation(location);
            } else {
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void printLocation(Location location) {
        Log.v("TAG", "经纬度：" + location.getLongitude() + "   " + location.getLatitude());
        getAddress(location);

        // 发送位置更新广播
        Intent intent = new Intent("LOCATION_UPDATE");
        intent.putExtra("location", location);
        sendBroadcast(intent);

        stopSelf();
    }


    private void getAddress(Location location) {
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                List<Address> result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Log.v("TAG", "地址信息：" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                printLocation(location);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
