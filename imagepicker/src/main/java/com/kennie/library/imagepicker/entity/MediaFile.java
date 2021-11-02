package com.kennie.library.imagepicker.entity;


/**
 * @项目名 KennieImagePicker
 * @类名称 MediaFile
 * @类描述 媒体实体类
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class MediaFile {

    private String path; // 路径
    private String mime; // 媒体类型
    private Integer folderId; // 媒体分类id
    private String folderName; // 媒体分类名称
    private long duration; // 媒体文件时长
    private long dateToken; // 媒体文件创建日期

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDateToken() {
        return dateToken;
    }

    public void setDateToken(long dateToken) {
        this.dateToken = dateToken;
    }


    @Override
    public String toString() {
        return "MediaFile{" +
                "path='" + path + '\'' +
                ", mime='" + mime + '\'' +
                ", folderId=" + folderId +
                ", folderName='" + folderName + '\'' +
                ", duration=" + duration +
                ", dateToken=" + dateToken +
                '}';
    }
}