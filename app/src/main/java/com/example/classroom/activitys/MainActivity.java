package com.example.classroom.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classroom.R;
import com.example.classroom.listeners.MyLocationListener;
import com.example.classroom.viewModels.BoundLocationManager;
import com.example.classroom.viewModels.LiveDataTimerViewModel;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private LiveDataTimerViewModel mLiveDataTimerViewModel;
    private TextView mTextView;

    private static final int REQUEST_LOCATION_PERMISSION_CODE = 10;
    private LocationListener mGpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.hello_tv);
        // 1
    /*    ChronometerViewModel cvm = new ViewModelProvider(this).get(ChronometerViewModel.class);
        Chronometer ch = findViewById(R.id.chronometer);
        if (cvm.getStartTime() == null) {
            long realtime = SystemClock.elapsedRealtime();
            cvm.setStartTime(realtime);
            ch.setBase(realtime);
        } else {
            ch.setBase(cvm.getStartTime());
        }
        ch.start();*/

        // 2
    /*    mLiveDataTimerViewModel = new ViewModelProvider(this).get(LiveDataTimerViewModel.class);
        subscribe();*/

        // 3

        mGpsListener = new MyLocationListener(mTextView);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        } else {
            bindLocationListener();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            bindLocationListener();
        } else {
            Toast.makeText(this, "本应用程序需要访问您的位置权限", Toast.LENGTH_LONG).show();
        }
    }

    // 绑定定位监听
    private void bindLocationListener() {
        BoundLocationManager.bindLocationListenerIn(getApplicationContext(),mGpsListener,this);
    }

    private void subscribe() {
        mLiveDataTimerViewModel.getElapsedTime().observe(this, aLong -> {
            mTextView.setText(String.format("seconds elapsed%d", aLong));
            Log.e(TAG, "Updating timer:" + aLong);
        });

    }


}
