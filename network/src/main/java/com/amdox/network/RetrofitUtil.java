package com.amdox.network;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/11
 * Description 网络请求工具类
 */
public class RetrofitUtil {

    private volatile static RetrofitUtil instance;
    private OkHttpClient ohc;
    private Retrofit retrofit;


    private RetrofitUtil(boolean isLog, String... url) {
        MyLog.isLog = isLog;
        ohc = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new InterceptorUtil.MoreBaseUrlInterceptor(url))
                .addInterceptor(InterceptorUtil.LogInterceptor())
                .proxy(Proxy.NO_PROXY)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url[0])
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(ohc)
                .build();

    }

    public static RetrofitUtil getInstance(boolean isLog, String... url) {
        if (url.length == 0) throw new RuntimeException("argument can't null initialize retrofit failed!");
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil(isLog, url);
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOhc() {
        return ohc;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
