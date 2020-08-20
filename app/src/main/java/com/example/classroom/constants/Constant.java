package com.example.classroom.constants;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description 常量工具类
 */
public class Constant {


    /** 服务器默认地址 */
    public static final String DEFAULT_SERVER_ADDRESS = "https://lanclazz.amdox.com.cn"; // 生产环境
    //   public static final String DEFAULT_SERVER_ADDRESS = "https://test-lanclazz.amdox.com.cn"; // 测试环境
    //   public static final String DEFAULT_SERVER_ADDRESS = "http://192.168.5.23:12950"; // 测试环境 胡博欣

    /** 更新服务器基址 */
    public static final String BASE_UPDATE_SERVER_URL = "https://version.amdox.com.cn";

    /** 更新APK地址 */
    public static final String URL_UPDATE_VERSION = "/version/tablet/getLatestVersion";

    //测试环境：https://ttc.amdox.com.cn/things/加密参数值
    //正式环境：https://tc.amdox.com.cn/things/加密参数值
    /** 设备在线服务 */
    public static final String DEVICE_ON_LINE_SERVER_URL = "https://ttc.amdox.com.cn";

    // 首页轮播图 https://gank.io/api/v2/banners
    public static final String BANNER_URL = "https://gank.io/api/v2/banners";

}
