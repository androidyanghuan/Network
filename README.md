"# network"

Android网络请求框架

基于retrofit2实现发起网络请求以及json转换实体对象，
rxjava2处理网络响应，
okhttp3处理网络拦截的一套网络请求及返回处理框架。

使用方式：
    在Application中通过RetrofitUtil.getInstance获取框架实例，
入参1，网络请求框架是否打印log日志（必传）
入参2，生产环境和测试环境以及特定环境的域名拦截（必传）
