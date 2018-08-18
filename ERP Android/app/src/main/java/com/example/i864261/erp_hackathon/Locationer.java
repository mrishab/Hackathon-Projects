package com.example.i864261.erp_hackathon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Locationer {

    private MainActivity ctx;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locationer(final MainActivity ctx){
        this.ctx = ctx;
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                ctx.locationLongTextView.setText(Double.toString(location.getLongitude()));
                ctx.locationLatTextView.setText(Double.toString(location.getLatitude()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.i("Location", "Status change");

            }

            @Override
            public void onProviderEnabled(String s) {
                Log.i("Location", "Enabled");

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.i("Location", "Disabled");

            }
        };
        requestUpdates();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation(){
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null){
            requestUpdates();
        }
        return loc;
    }

    private void requestUpdates(){
        if (ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Log.e("Location", "Permission Denied");
        }
    }
}
