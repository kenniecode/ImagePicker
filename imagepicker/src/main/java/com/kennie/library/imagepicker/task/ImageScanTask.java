package com.kennie.library.imagepicker.task;

import android.content.Context;

import com.kennie.library.imagepicker.entity.MediaFile;
import com.kennie.library.imagepicker.loader.ImageScanner;
import com.kennie.library.imagepicker.utils.MediaUtil;

import java.util.ArrayList;


/**
 * @项目名 KennieImagePicker
 * @类名称 ImageScanTask
 * @类描述 媒体库扫描任务（图片）
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class ImageScanTask implements Runnable {

    private Context mContext;
    private ImageScanner mImageScanner;
    private MediaScanCallback mMediaLoadCallback;

    public ImageScanTask(Context context, MediaScanCallback mediaLoadCallback) {
        this.mContext = context;
        this.mMediaLoadCallback = mediaLoadCallback;
        mImageScanner = new ImageScanner(context);
    }

    @Override
    public void run() {
        //存放所有照片
        ArrayList<MediaFile> imageFileList = new ArrayList<>();

        if (mImageScanner != null) {
            imageFileList = mImageScanner.queryMedia();
        }

        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.onLoadMedia(MediaUtil.getImageFolder(mContext, imageFileList));
        }
    }
}