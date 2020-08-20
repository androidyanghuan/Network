package com.example.classroom.viewModels;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description 定位管理类
 */
public class BoundLocationManager {

    public static void bindLocationListenerIn(Context context,
                                              LocationListener listener, LifecycleOwner lifecycleOwner) {
        new BoundLocationListener(context, listener, lifecycleOwner);
    }

    @SuppressWarnings("MissingPermission")
    static class BoundLocationListener implements LifecycleObserver {
        private final Context mContext;
        private LocationManager mLocationManager;
        private final LocationListener mListener;

        public BoundLocationListener(Context mContext, LocationListener listener, LifecycleOwner lifecycleOwner) {
            this.mContext = mContext;
            this.mListener = listener;
            lifecycleOwner.getLifecycle().addObserver(this);
        }

        // onResume时调用
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void addLocationListener() {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
            Log.e("BoundLocationMgr", "Listener added");
            // 如果可用，强制更新最后一个位置
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                mListener.onLocationChanged(location);
            }
        }

        // onPause时调用
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void removeLocationListener() {
            if (mListener == null) {
                return;
            }
            mLocationManager.removeUpdates(mListener);
            mLocationManager = null;
            Log.e("BoundLocationMgr", "Listener removed");
        }
    }
}
