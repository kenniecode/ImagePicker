package com.kennie.library.imagepicker.utils;

import android.content.Context;

import com.kennie.library.imagepicker.R;
import com.kennie.library.imagepicker.entity.MediaFile;
import com.kennie.library.imagepicker.entity.MediaFolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @项目名 KennieImagePicker
 * @类名称 MediaUtil
 * @类描述 媒体处理类（对扫描出来的图片、视频做对应聚类处理）
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class MediaUtil {

    public static final int ALL_MEDIA_FOLDER = -1;//全部媒体
    public static final int ALL_VIDEO_FOLDER = -2;//全部视频

    /**
     * 对查询到的图片进行聚类（相册分类）
     *
     * @param context       上下文
     * @param imageFileList 图片文件列表
     * @return List<MediaFolder>
     */
    public static List<MediaFolder> getImageFolder(Context context, ArrayList<MediaFile> imageFileList) {
        return getMediaFolder(context, imageFileList, null);
    }


    /**
     * 对查询到的视频进行聚类（相册分类）
     *
     * @param context       上下文
     * @param videoFileList 视频文件列表
     * @return List<MediaFolder>
     */
    public static List<MediaFolder> getVideoFolder(Context context, ArrayList<MediaFile> videoFileList) {
        return getMediaFolder(context, null, videoFileList);
    }


    /**
     * 对查询到的图片和视频进行聚类（相册分类）
     *
     * @param context       上下文
     * @param imageFileList 图片文件列表
     * @param videoFileList 视频文件列表
     * @return 整理后的聚合列表
     */
    public static List<MediaFolder> getMediaFolder(Context context, ArrayList<MediaFile> imageFileList, ArrayList<MediaFile> videoFileList) {

        //根据媒体所在文件夹Id进行聚类（相册）
        Map<Integer, MediaFolder> mediaFolderMap = new HashMap<>();

        //全部图片、视频文件
        ArrayList<MediaFile> mediaFileList = new ArrayList<>();
        if (imageFileList != null) {
            mediaFileList.addAll(imageFileList);
        }
        if (videoFileList != null) {
            mediaFileList.addAll(videoFileList);
        }

        //对媒体数据进行排序
        Collections.sort(mediaFileList, (o1, o2) -> {
            if (o1.getDateToken() > o2.getDateToken()) {
                return -1;
            } else if (o1.getDateToken() < o2.getDateToken()) {
                return 1;
            } else {
                return 0;
            }
        });

        //全部图片或视频
        if (!mediaFileList.isEmpty()) {
            MediaFolder allMediaFolder = new MediaFolder(ALL_MEDIA_FOLDER, context.getString(R.string.kennie_picker_all), mediaFileList.get(0).getPath(), mediaFileList);
            allMediaFolder.setTag(String.valueOf(ALL_MEDIA_FOLDER));
            if (!(imageFileList == null && videoFileList != null)) { // 非只显示视频
                mediaFolderMap.put(ALL_MEDIA_FOLDER, allMediaFolder);
            }
        }

        //全部视频
        if (videoFileList != null && !videoFileList.isEmpty()) {
            MediaFolder allVideoFolder = new MediaFolder(ALL_VIDEO_FOLDER, context.getString(R.string.all_video), videoFileList.get(0).getPath(), videoFileList);
            allVideoFolder.setTag(String.valueOf(ALL_VIDEO_FOLDER));
            mediaFolderMap.put(ALL_VIDEO_FOLDER, allVideoFolder);
        }

        //对图片进行文件夹分类
        if (imageFileList != null && !imageFileList.isEmpty()) {
            int size = imageFileList.size();
            //添加其他的图片文件夹
            for (int i = 0; i < size; i++) {
                MediaFile mediaFile = imageFileList.get(i);
                int imageFolderId = mediaFile.getFolderId();
                MediaFolder mediaFolder = mediaFolderMap.get(imageFolderId);
                if (mediaFolder == null) {
                    mediaFolder = new MediaFolder(imageFolderId, mediaFile.getFolderName(), mediaFile.getPath(), new ArrayList<>());
                    mediaFolderMap.put(imageFolderId, mediaFolder);
                }
                ArrayList<MediaFile> imageList = mediaFolder.getMediaFileList();
                imageList.add(mediaFile);
            }
        }

        //整理聚类数据
        List<MediaFolder> mediaFolderList = new ArrayList<>();
        for (Integer folderId : mediaFolderMap.keySet()) {
            mediaFolderList.add(mediaFolderMap.get(folderId));
        }

        //按照图片文件夹的数量排序
        Collections.sort(mediaFolderList, (o1, o2) -> {
            if (o1.getTag() == null || o2.getTag() == null) return 0;
            if (String.valueOf(ALL_MEDIA_FOLDER).equals(o1.getTag())) {
                return -1;
            }
            if (String.valueOf(ALL_MEDIA_FOLDER).equals(o2.getTag())) {
                return 1;
            }
            if (String.valueOf(ALL_VIDEO_FOLDER).equals(o1.getTag())) {
                return -1;
            }
            if (String.valueOf(ALL_VIDEO_FOLDER).equals(o2.getTag())) {
                return 1;
            }
            if (o1.getFolderName() != null && o2.getFolderName() != null) {
                return o1.getFolderName().compareTo(o2.getFolderName());
            }
            if (o1.getFolderName() != null) {
                return 1;
            }
            if (o2.getFolderName() != null) {
                return 1;
            }
            return o1.getMediaFileList().size() - o2.getMediaFileList().size();
        });

        return mediaFolderList;
    }
}