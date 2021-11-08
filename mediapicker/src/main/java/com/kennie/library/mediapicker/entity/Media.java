package com.kennie.library.mediapicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @项目名
 * @类名称 MediaType
 * @类描述 媒体信息
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:51
 */
public class Media implements Parcelable {

    /**
     * ID
     */
    private long ID;

    /**
     * 媒体名称
     */
    private String mediaName;

    /**
     * 媒体路径
     */
    private String mediaPath;

    /**
     * 媒体后缀名
     */
    private String mediaExt;

    /**
     * 媒体资源类型(MediaType组合)
     */
    private String mimeType;
    private MediaMimeType mediaMimeType;


    /**
     * 媒体大小
     */
    private long mediaSize;

    /**
     * 媒体格式化大小(GB、MB)
     */
    private String mediaFormatSize;

    /**
     * 媒体MD5
     */
    private String mediaMD5;


    /**
     * 扩展信息，可以放置任意的数据内容，也可以去掉此属性
     */
    private String extra;


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaExt() {
        return mediaExt;
    }

    public void setMediaExt(String mediaExt) {
        this.mediaExt = mediaExt;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public MediaMimeType getMediaMimeType() {
        return mediaMimeType;
    }

    public void setMediaMimeType(MediaMimeType mediaMimeType) {
        this.mediaMimeType = mediaMimeType;
    }

    public long getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaFormatSize() {
        return mediaFormatSize;
    }

    public void setMediaFormatSize(String mediaFormatSize) {
        this.mediaFormatSize = mediaFormatSize;
    }

    public String getMediaMD5() {
        return mediaMD5;
    }

    public void setMediaMD5(String mediaMD5) {
        this.mediaMD5 = mediaMD5;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ID);
        dest.writeString(this.mediaName);
        dest.writeString(this.mediaPath);
        dest.writeString(this.mediaExt);
        dest.writeString(this.mimeType);
        dest.writeInt(this.mediaMimeType == null ? -1 : this.mediaMimeType.ordinal());
        dest.writeLong(this.mediaSize);
        dest.writeString(this.mediaFormatSize);
        dest.writeString(this.mediaMD5);
        dest.writeString(this.extra);
    }

    public void readFromParcel(Parcel source) {
        this.ID = source.readLong();
        this.mediaName = source.readString();
        this.mediaPath = source.readString();
        this.mediaExt = source.readString();
        this.mimeType = source.readString();
        int tmpMediaMimeType = source.readInt();
        this.mediaMimeType = tmpMediaMimeType == -1 ? null : MediaMimeType.values()[tmpMediaMimeType];
        this.mediaSize = source.readLong();
        this.mediaFormatSize = source.readString();
        this.mediaMD5 = source.readString();
        this.extra = source.readString();
    }

    public Media() {
    }

    protected Media(Parcel in) {
        this.ID = in.readLong();
        this.mediaName = in.readString();
        this.mediaPath = in.readString();
        this.mediaExt = in.readString();
        this.mimeType = in.readString();
        int tmpMediaMimeType = in.readInt();
        this.mediaMimeType = tmpMediaMimeType == -1 ? null : MediaMimeType.values()[tmpMediaMimeType];
        this.mediaSize = in.readLong();
        this.mediaFormatSize = in.readString();
        this.mediaMD5 = in.readString();
        this.extra = in.readString();
    }

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
