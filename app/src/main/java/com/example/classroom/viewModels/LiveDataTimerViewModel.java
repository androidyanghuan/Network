package com.example.classroom.viewModels;

import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/7
 * Description
 */
public class LiveDataTimerViewModel extends ViewModel {
    private static final String TAG = "LiveDataTimerViewModel";

    private static final int ONE_SECOND = 1000;
    private MutableLiveData<Long> mElapsedTime = new MutableLiveData<>();
    private long mInitialTime;
    private final Timer timer;

    public LiveDataTimerViewModel() {
        mInitialTime = SystemClock.elapsedRealtime();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final long newValue = (SystemClock.elapsedRealtime() - mInitialTime) / 1000;
                Log.e(TAG,"newValue:" + newValue);
                mElapsedTime.postValue(newValue);
            }
        },ONE_SECOND,ONE_SECOND);
    }

    public MutableLiveData<Long> getElapsedTime() {
        return mElapsedTime;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timer.cancel();
        Log.e(TAG,"**********onCleared**********");
    }

}
