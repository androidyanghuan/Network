package com.example.classroom.app;

import android.app.Activity;
import android.app.Application;

import com.amdox.network.RetrofitUtil;
import com.bumptech.glide.Glide;
import com.example.classroom.R;
import com.example.classroom.apis.Api;
import com.example.classroom.constants.Constant;
import com.example.classroom.utils.MyLog;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description 自定义Application
 */
public class App extends Application {

    public static volatile App me;
    private Api api;
    //用于存放我们所有activity的数组
    private List<Activity> activities = new ArrayList<>();

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(android.R.color.holo_red_light,android.R.color.holo_blue_light,android.R.color.holo_green_light);//全局设置主题颜色
          //  return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            return new MaterialHeader(context);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(context.getResources().getDimension(R.dimen.dp_8));
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        api = RetrofitUtil.getInstance(MyLog.isLog, Constant.DEFAULT_SERVER_ADDRESS,Constant.DEVICE_ON_LINE_SERVER_URL).getRetrofit().create(Api.class);
        Glide.get(this);
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
    }

    public Api getApi() {
        return api;
    }

    //向集合中添加一个activity
    public void addActivity(Activity activity){
        //将activity加入到集合中
        activities.add(activity);
    }

    public void removeActivity(Activity activity){
        //移除已经销毁的Activity
        activities.remove(activity);
    }

    //结束掉所有的Activity
    public void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
