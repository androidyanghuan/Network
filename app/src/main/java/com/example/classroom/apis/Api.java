package com.example.classroom.apis;


import com.example.classroom.entitys.BannerInfo;
import com.example.classroom.entitys.BaseEntity;
import com.example.classroom.entitys.ClassifyInfo;
import com.example.classroom.entitys.GirlInfo;
import com.example.classroom.entitys.HotInfo;
import com.example.classroom.entitys.UpdateInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/11
 * Description api接口
 */
public interface Api {

    /**
     * 检查更新
     * param checkVersionUrl
     * http://version.amdox.com.cn/version/tablet/getLatestVersion
     */
    @GET
    Observable<BaseEntity<UpdateInfo>> checkVersion(@Url String checkVersionUrl);


    /**
     * 获取首页轮播图 https://gank.io/api/v2/banners
     */
    @GET
    Observable<BaseEntity<List<BannerInfo>>> getBanner(@Url String url);

    /**
     * 获取首页分类数据 https://gank.io/api/v2/data/category/GanHuo/type/Android/page/1/count/20
     */
    @GET("https://gank.io/api/v2/data/category/{category}/type/{type}/page/{page}/count/{count}")
    Observable<BaseEntity<List<ClassifyInfo>>> getType(@Path("category") String category, @Path("type") String type, @Path("page") int page, @Path("count") int count);

    /**
     * 获取妹纸列表 https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10
     */
    @GET("https://gank.io/api/v2/data/category/Girl/type/Girl/page/{page}/count/50")
    Observable<BaseEntity<List<GirlInfo>>> getGirl(@Path("page") int page);

    /**
     * 获取本周热门 https://gank.io/api/v2/hot/likes/category/Article/count/20
     */
    @GET("https://gank.io/api/v2/hot/likes/category/Article/count/20")
    Observable<BaseEntity<List<HotInfo>>> getHot();


}
