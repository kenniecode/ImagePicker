package com.kennie.library.imagepicker.listener;

import com.kennie.library.imagepicker.data.MediaFolder;

import java.util.List;

/**
 * 图片扫描数据回调接口
 */
public interface MediaLoadCallback {
    void loadMediaSuccess(List<MediaFolder> mediaFolderList);
}