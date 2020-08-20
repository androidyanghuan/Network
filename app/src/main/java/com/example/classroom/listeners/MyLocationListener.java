package com.example.classroom.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description
 */
public class MyLocationListener implements LocationListener {

    private TextView textView;

    public MyLocationListener(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void onLocationChanged(Location location) {
        textView.setText(String.format("%s, %s, %s", location.getLatitude(), location.getLongitude(), location.getProvider()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(textView.getContext(), "Provider enabled: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
