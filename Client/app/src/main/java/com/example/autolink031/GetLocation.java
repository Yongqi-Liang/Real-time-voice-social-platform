// GetLocation.java
package com.example.autolink031;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class GetLocation {

    private static final int LOCATION_CODE = 301;
    private final AppCompatActivity activity;
    private final LocationManager locationManager;
    private String locationProvider = null;

    public GetLocation(AppCompatActivity activity) {
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public Location getLocation() {
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v("TAG", "定位方式GPS");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v("TAG", "定位方式Network");
        } else {
            Log.e("TAG", "没有可用的位置提供器");
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_CODE);
            } else {
                requestLocationUpdates();
            }
        } else {
            requestLocationUpdates();
        }
        return null;
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

    private String printLocation(Location location) {
        Log.v("TAG", "经纬度：" + location.getLongitude() + "   " + location.getLatitude());
        String ll = String.valueOf(location.getLongitude()+location.getLatitude());
        getAddress(location);
        return ll;
    }

    public LocationListener locationListener = new LocationListener() {
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                } else {
                    Log.e("TAG", "缺少权限");
                }
                break;
        }
    }

    private void getAddress(Location location) {
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(activity, Locale.getDefault());
                List<Address> result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Log.v("TAG", "地址信息：" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        locationManager.removeUpdates(locationListener);
    }
}
