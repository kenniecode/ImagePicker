package com.kennie.library.imagepicker.task;

import com.kennie.library.imagepicker.entity.MediaFolder;

import java.util.List;


/**
 * @项目名 KennieImagePicker
 * @类名称 MediaScanCallback
 * @类描述 图片扫描数据回调接口
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public interface MediaScanCallback {
    void onLoadMedia(List<MediaFolder> mediaFolderList);
}