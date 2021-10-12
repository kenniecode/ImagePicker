package com.kennie.library.imagepicker.task;

import android.content.Context;

import com.kennie.library.imagepicker.data.MediaFile;
import com.kennie.library.imagepicker.listener.MediaLoadCallback;
import com.kennie.library.imagepicker.loader.ImageScanner;
import com.kennie.library.imagepicker.loader.MediaHandler;
import com.kennie.library.imagepicker.loader.VideoScanner;

import java.util.ArrayList;

/**
 * 媒体库扫描任务（图片、视频）
 */
public class MediaLoadTask implements Runnable {

    private Context mContext;
    private ImageScanner mImageScanner;
    private VideoScanner mVideoScanner;
    private MediaLoadCallback mMediaLoadCallback;

    public MediaLoadTask(Context context, MediaLoadCallback mediaLoadCallback) {
        this.mContext = context;
        this.mMediaLoadCallback = mediaLoadCallback;
        mImageScanner = new ImageScanner(context);
        mVideoScanner = new VideoScanner(context);
    }

    @Override
    public void run() {
        //存放所有照片
        ArrayList<MediaFile> imageFileList = new ArrayList<>();
        //存放所有视频
        ArrayList<MediaFile> videoFileList = new ArrayList<>();

        if (mImageScanner != null) {
            imageFileList = mImageScanner.queryMedia();
        }
        if (mVideoScanner != null) {
            videoFileList = mVideoScanner.queryMedia();
        }

        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.loadMediaSuccess(MediaHandler.getMediaFolder(mContext, imageFileList, videoFileList));
        }
    }
}