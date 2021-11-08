package com.kennie.library.mediapicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @项目名
 * @类名称 MediaMimeType
 * @类描述 媒体类型
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:51
 */
public enum MediaMimeType implements Parcelable {

    // 图片
    IMAGE(0),
    // 语音
    AUDIO(1),
    // 音频
    MUSIC(2),
    // 视频
    VIDEO(3),
    // 文件
    FILES(4);

    private int value;

    MediaMimeType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MediaMimeType status(int status) {
        MediaMimeType mediaMimeType = null;
        switch (status) {
            case 0:
                mediaMimeType = IMAGE;
                break;
            case 1:
                mediaMimeType = AUDIO;
                break;
            case 2:
                mediaMimeType = MUSIC;
                break;
            case 3:
                mediaMimeType = VIDEO;
                break;
            case 4:
                mediaMimeType = FILES;
                break;
            default:
                throw new IllegalArgumentException("media type " + status + "is not valid");
        }
        return mediaMimeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }


    public static final Creator<MediaMimeType> CREATOR = new Creator<MediaMimeType>() {
        @Override
        public MediaMimeType createFromParcel(Parcel in) {
            return MediaMimeType.values()[in.readInt()];
        }

        @Override
        public MediaMimeType[] newArray(int size) {
            return new MediaMimeType[size];
        }
    };
}
