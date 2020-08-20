package com.amdox.network;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorUtil {
    private static final String TAG = "InterceptorUtil";

    /** 日志拦截器 */
    public static HttpLoggingInterceptor LogInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message ->
                MyLog.log(TAG, "http log interceptor:" + message));
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY); //设置打印数据的级别
        return httpLoggingInterceptor;

    }

    /** 头拦截器 */
    public static Interceptor HeaderInterceptor(){
        return chain -> {
            //在这里你可以做一些想做的事,比如token失效时,重新获取token
            //或者添加header等等
            // https://blog.csdn.net/yrmao9893/article/details/69791519
            return chain.proceed(chain.request());
        };
    }

    /**
     * https://blog.csdn.net/Jason_996/article/details/78659019
     */
    public static class MoreBaseUrlInterceptor implements Interceptor {

        private String[] urls;

        public MoreBaseUrlInterceptor(String[] urls) {
            this.urls = urls;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取原始的originalRequest
            Request originalRequest = chain.request();
            //获取原始url
            HttpUrl oldUrl = originalRequest.url();
            //获取originalRequest的创建者builder
            Request.Builder builder = originalRequest.newBuilder();
            //获取头信息的集合如：manage,mdffx
            List<String> urlNameList = originalRequest.headers("urlName");
            if (urlNameList.size() > 0) {
                //删除原有配置中的值,就是namesAndValues集合里的值
                builder.removeHeader("urlName");
                //获取头信息中配置的value,如：manage或者mdffx
                String urlName = urlNameList.get(0);
                HttpUrl baseURL = null;
                //根据头信息中配置的value,来匹配新的base_url地址
                if ("formal".equals(urlName)) {
                    baseURL = HttpUrl.parse(urls[0]);
                }else if ("test".equals(urlName)) {
                    baseURL = HttpUrl.parse(urls[0]);
                } else if ("local".equals(urlName)) {
                    baseURL = HttpUrl.parse(urls[0]);
                }else if ("onLine".equals(urlName)) {
                    baseURL = HttpUrl.parse(urls[1]);
                }
                if (null == baseURL) return null;
                //重建新的HttpUrl，需要重新设置的url部分
                HttpUrl newHttpUrl = oldUrl.newBuilder()
                        .scheme(baseURL.scheme())//http协议如：http或者https
                        .host(baseURL.host())//主机地址
                        .port(baseURL.port())//端口
                        .build();
                //获取处理后的新newRequest
                Request newRequest = builder.url(newHttpUrl).build();
                return chain.proceed(newRequest);
            } else {
                return chain.proceed(originalRequest);
            }

        }
    }

}
