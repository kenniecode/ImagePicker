package com.kennie.library.imagepicker.provider;

import android.content.Context;

import androidx.core.content.FileProvider;


/**
 * @项目名 KennieImagePicker
 * @类名称 ImagePickerProvider
 * @类描述 自定义Provider，避免上层发生provider冲突
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class ImagePickerProvider extends FileProvider {

    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }
}