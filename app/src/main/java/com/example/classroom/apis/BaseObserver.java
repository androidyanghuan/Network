package com.example.classroom.apis;

import android.accounts.NetworkErrorException;
import android.content.Context;


import com.example.classroom.entitys.BaseEntity;
import com.example.classroom.views.CustomProgressDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description  Observer基类
 */
public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {

    private Context context;
    private Disposable disposable;
    private boolean isShowDialog;

    protected BaseObserver() {

    }

    protected BaseObserver(Context context, boolean isShowDialog) {
        this.context = context;
        this.isShowDialog = isShowDialog;
    }

    @Override
    public void onSubscribe(Disposable d) {
     //   RxJavaGcManager.getInstance().addDisposable(d);
        this.disposable = d;
        onRequestStart();
        onStartSubscribe(d);
    }


    @Override
    public void onNext(BaseEntity<T> tBaseEntity) {
        onRequestEnd();
        if (tBaseEntity != null) {
            onSuccess(tBaseEntity);
        }

    }

    protected abstract void onSuccess(BaseEntity<T> tBaseEntity);

    protected abstract void onFail(Throwable e, boolean isNetworkException);

    protected abstract void onStartSubscribe(Disposable disposable);

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        if (e instanceof ConnectException
                || e instanceof TimeoutException
                || e instanceof NetworkErrorException
                || e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            onFail(e, true);
        } else {
            onFail(e,false);
        }

    }



    @Override
    public void onComplete() {
        onRequestEnd();
    }

    private void onRequestStart() {
        if (isShowDialog) {
            new android.os.Handler(context.getMainLooper()).post(() -> CustomProgressDialog.createDefaultLoadingDialog(context,isShowDialog));
        }
    }

    private void onRequestEnd() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
     //   RxJavaGcManager.getInstance().disposableActive(disposable);
        if (isShowDialog) {
            CustomProgressDialog.cancel();
        }
    }


}
