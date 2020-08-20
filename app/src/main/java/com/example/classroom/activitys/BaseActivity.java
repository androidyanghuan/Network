package com.example.classroom.activitys;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.classroom.app.App;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description  Activity基类
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        App.me.addActivity(this);

    }

    public <T> ObservableTransformer<T, T> thread() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.me.removeActivity(this);
    }
}
