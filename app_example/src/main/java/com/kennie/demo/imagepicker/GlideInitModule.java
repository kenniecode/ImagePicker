package com.kennie.demo.imagepicker;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * @项目名 KennieImagePicker
 * @类名称 GlideInitModule
 * @类描述 Glide参数初始化配置
 * @创建人 Administrator
 * @修改人
 * @创建时间 2021/11/2 21:41
 */
@GlideModule
public class GlideInitModule extends AppGlideModule {

    /**
     * 磁盘缓存大小
     */
    private int diskSize = 1024 * 1024 * 100;
    /**
     * 取1/8最大内存作为最大缓存
     */
    private int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;

    /**
     * 关闭manifest的解析,避免添加相同的module
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * 通过GlideBuilder设置默认的结构(Engine,BitmapPool ,ArrayPool,MemoryCache等等).
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskSize));
        // sd卡中
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "glideCache", diskSize));
        // 自定义内存大小
        builder.setMemoryCache(new LruResourceCache(memorySize));
        // 自定义图片池大小
        builder.setBitmapPool(new LruBitmapPool(memorySize));

        RequestOptions options = new RequestOptions().format(DecodeFormat.PREFER_RGB_565);
        builder.setDefaultRequestOptions(options);
    }
}
