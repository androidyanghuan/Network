package com.example.classroom.fragments;

import androidx.fragment.app.Fragment;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description Fragment基类
 */
public class BaseFragment extends Fragment {

    public <T> ObservableTransformer<T, T> thread() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
