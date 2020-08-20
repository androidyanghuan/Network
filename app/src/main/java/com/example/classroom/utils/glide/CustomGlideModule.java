package com.example.classroom.utils.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import androidx.annotation.NonNull;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/7/23
 * Description 自定义GlideModule缓存
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
      //  super.applyOptions(context, builder);
        // 内存缓存 50mb
        int memoryCacheSize = 1024 * 1024 * 50;
        // 磁盘缓存 100mb
        int diskCacheSize = 1024 * 1024 * 100;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize))
                .setDiskCache(new InternalCacheDiskCacheFactory(context,diskCacheSize));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
