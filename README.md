# ImagePicker

<p align="center"><img src="./resources/logo.gif" width="50%"/></p>

<p align="center">
    <strong>高仿微信图片选择器</strong>
    <br>
    <br>
    <a href="https://kenniecode.github.io/ImagePicker/">使用文档</a>
    <br>    
</p>


<div align="center" >

[![License](https://img.shields.io/github/license/kenniecode/ImagePicker)](https://github.com/kenniecode/ImagePicker/blob/main/LICENSE)
![MinSdk](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)
[![JitPack](https://jitpack.io/v/kenniecode/ImagePicker.svg)](https://jitpack.io/#kenniecode/ImagePicker)
<img src="https://img.shields.io/badge/language-java-blue.svg"/>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>

</div>



## 简介

> 高仿微信图片选择器 目前支持图片，视频单选，多选，多文件夹切换，大图预览，自定义图片加载器等功能。

如有任何疑问或者Bug，请在 github 上公开讨论[技术问题](https://github.com/kenniecode/KennieFilterMenu/issues)

**开源不易，如果喜欢的话希望给个 `Star` 或 `Fork` ^_^ ，谢谢~~**


## 功能及特点

- 图片、视频单选 
- 图片、视频多选
- 多文件夹切换
- 大图预览
- 自定义图片加载器

## 预览

| ![](resources/screenshots/Screenshot1.png) | ![](resources/screenshots/Screenshot2.png) | ![](resources/screenshots/Screenshot3.png) | ![](resources/screenshots/Screenshot4.png) |
| --- | --- | --- | --- |


## 下载

- [GitHub下载](https://gitee.com/kenniecode/ImagePicker/tree/template%2Flibrary/releases) [![](https://img.shields.io/badge/Download-apk-green.svg)](https://gitee.com/kenniecode/ImagePicker/tree/template%2Flibrary/releases/app-release.apk)
- 扫码

![](./resources/download_qr_code.png)



## 如何使用

### 1.gradle中添加依赖
   
在项目的root build.gradle中添加如下配置：
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
```
添加依赖

```
implementation 'com.lcw.library:imagepicker:2.2.7'

```

### 2、如何自定义图片加载器

（不定死框架，让框架更加灵活，需要去实现ImageLoader接口即可，如果需要显示视频，优先推荐Glide加载框架，可以参考Demo实现）：
```
            public class GlideLoader implements ImageLoader {
                //to do something 可以参考Demo用法
                
            }
```

### 3、一行代码调用：
```
                ImagePicker.getInstance()
                        .setTitle("标题")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .filterGif(false)//设置是否过滤gif图片
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                        .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(MainActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
```

### 4、如何获取选中的图片集合：
```
                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
                        List<String> imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                    }
                }
```


## 更新日志

- [详细日志](./CHANGELOG.md)


## 注意事项

> **使用前请查看注意事项**，当前版本仅支持AndroidX

### 构建环境

> 最低支持api 21

    compileSdk 30
    minSdk 21
    targetSdk 30

> 开发环境

    AndroidStudio ArcticFox 2020.3.1
    JDK 1.8 || JDK 11
    kotlin 1.5.31
    gradle-7.0.2-bin & gradle 7.0.3



## 感谢

- [AliyunGradleConfig](https://github.com/gzu-liyujiang/AliyunGradleConfig)
- [Android 代码规范文档](https://gitee.com/getActivity/AndroidCodeStandard)
- [ImagePicker](https://github.com/mymbrooks/ImagePicker)
- [阿里矢量图](https://www.iconfont.cn/)
- [花瓣](https://huaban.com/)

## LICENSE

```
Copyright (c) 2020-2021 kennie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```