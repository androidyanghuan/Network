package com.example.classroom.utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/13
 * Description 统一管理请求类
 */
public class RxJavaGcManager {

    private static RxJavaGcManager instance;
    private CompositeDisposable compositeDisposable;

    private RxJavaGcManager() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    public static RxJavaGcManager getInstance() {
        if (instance == null) {
            synchronized (RxJavaGcManager.class) {
                if (instance == null) {
                    instance = new RxJavaGcManager();
                }
            }
        }
        return instance;
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    private void deleteDisposable(Disposable disposable) {
        compositeDisposable.delete(disposable);
    }

    public void clearDisposable() {
        compositeDisposable.clear();
    }

    public void disposableActive(Disposable disposable) {
        synchronized (this) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                deleteDisposable(disposable);

            }
        }
    }

}
