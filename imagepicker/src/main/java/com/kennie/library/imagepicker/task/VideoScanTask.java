package com.kennie.library.imagepicker.task;

import android.content.Context;

import com.kennie.library.imagepicker.entity.MediaFile;
import com.kennie.library.imagepicker.utils.MediaUtil;
import com.kennie.library.imagepicker.loader.VideoScanner;

import java.util.ArrayList;

/**
 * 媒体库扫描任务（视频）
 */

/**
 * @项目名 KennieImagePicker
 * @类名称 VideoScanTask
 * @类描述 媒体库扫描任务（视频）
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class VideoScanTask implements Runnable {
    private Context mContext;
    private VideoScanner mVideoScanner;
    private MediaScanCallback mMediaLoadCallback;

    public VideoScanTask(Context context, MediaScanCallback mediaLoadCallback) {
        this.mContext = context;
        this.mMediaLoadCallback = mediaLoadCallback;
        mVideoScanner = new VideoScanner(context);
    }

    @Override
    public void run() {
        //存放所有视频
        ArrayList<MediaFile> videoFileList = new ArrayList<>();

        if (mVideoScanner != null) {
            videoFileList = mVideoScanner.queryMedia();
        }

        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.onLoadMedia(MediaUtil.getVideoFolder(mContext, videoFileList));
        }
    }
}