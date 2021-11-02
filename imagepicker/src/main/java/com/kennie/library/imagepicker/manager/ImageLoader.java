package com.kennie.library.imagepicker.manager;

import android.widget.ImageView;

import java.io.Serializable;


/**
 * @项目名 KennieImagePicker
 * @类名称 ImageLoader
 * @类描述 开放图片加载接口
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public interface ImageLoader extends Serializable {

    /**
     * 缩略图加载方案
     *
     * @param imageView
     * @param imagePath
     */
    void loadImage(ImageView imageView, String imagePath);

    /**
     * 大图加载方案
     *
     * @param imageView
     * @param imagePath
     */
    void loadPreImage(ImageView imageView, String imagePath);


    /**
     * 视频播放方案
     *
     * @param imageView
     * @param path
     */
//    void loadVideoPlay(ImageView imageView, String path);

    /**
     * 缓存管理
     */
    void clearMemoryCache();
}