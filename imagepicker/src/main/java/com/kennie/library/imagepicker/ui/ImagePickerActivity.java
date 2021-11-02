package com.kennie.library.imagepicker.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanc.activityresult.launcher.AppDetailsSettingsLauncher;
import com.dylanc.activityresult.launcher.RequestMultiplePermissionsLauncher;
import com.dylanc.callbacks.Callback0;
import com.dylanc.callbacks.Callback2;
import com.kennie.library.imagepicker.ImagePicker;
import com.kennie.library.imagepicker.R;
import com.kennie.library.imagepicker.ui.adapter.ImageFoldersAdapter;
import com.kennie.library.imagepicker.ui.adapter.ImagePickerAdapter;
import com.kennie.library.imagepicker.entity.MediaFile;
import com.kennie.library.imagepicker.entity.MediaFolder;
import com.kennie.library.imagepicker.task.DefaultExecutor;
import com.kennie.library.imagepicker.task.MediaScanCallback;
import com.kennie.library.imagepicker.manager.ConfigManager;
import com.kennie.library.imagepicker.manager.SelectionManager;
import com.kennie.library.imagepicker.provider.ImagePickerProvider;
import com.kennie.library.imagepicker.task.ImageScanTask;
import com.kennie.library.imagepicker.task.MediaScanTask;
import com.kennie.library.imagepicker.task.VideoScanTask;
import com.kennie.library.imagepicker.utils.DataUtil;
import com.kennie.library.imagepicker.utils.MediaFileUtil;
import com.kennie.library.imagepicker.utils.Utils;
import com.kennie.library.imagepicker.view.ImageFolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @项目名 KennieImagePicker
 * @类名称 ImagePickerActivity
 * @类描述 多图选择页面
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class ImagePickerActivity extends BaseActivity implements ImagePickerAdapter.OnItemClickListener, ImageFoldersAdapter.OnImageFolderChangeListener {

    private static String TAG = ImagePickerActivity.class.getSimpleName();
    /**
     * 启动参数
     */
    private String mTitle;
    private boolean isShowCamera;
    private boolean isShowImage;
    private boolean isShowVideo;
    private boolean isSingleType;
    private int mMaxCount;
    private List<String> mImagePaths;

    /**
     * 界面UI
     */
    private TextView mTvTitle;
    private TextView mTvCommit;
    private AppCompatTextView mTvImageTime; // 悬浮日期时间
    private RecyclerView mRecyclerView;
    private AppCompatTextView mTvImageFolders;
    private ImageFolderPopupWindow mImageFolderPopupWindow;
    private ProgressDialog mProgressDialog;
    private RelativeLayout mRlBottom;

    private GridLayoutManager mGridLayoutManager;
    private ImagePickerAdapter mImagePickerAdapter;

    //图片数据源
    private List<MediaFile> mMediaFileList;
    //文件夹数据源
    private List<MediaFolder> mMediaFolderList;

    private File mPicDir;


    //是否显示时间
    private boolean isShowTime;

    //表示屏幕亮暗
    private static final int LIGHT_OFF = 0;
    private static final int LIGHT_ON = 1;

    private Handler mMyHandler = new Handler();
    private Runnable mHideRunnable = () -> hideImageTime();


    private final RequestMultiplePermissionsLauncher requestMultiplePermissionsLauncher = new RequestMultiplePermissionsLauncher(this);


    /**
     * 大图预览页相关
     */
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;//用于在大图预览页中点击提交按钮标识


    /**
     * 拍照相关
     */
    private String mFilePath;
    private static final int REQUEST_CODE_CAPTURE = 0x02;//点击拍照标识

    /**
     * 权限相关
     */
    private static final int REQUEST_PERMISSION_CAMERA_CODE = 0x03;


    @Override
    protected int bindLayout() {
        return R.layout.activity_imagepicker;
    }


    @Override
    protected void initView() {
        initConfig();
        initUi();
        initEvent();
        startWhenPermissionGrant();
    }


    /**
     * 初始化配置
     */
    private void initConfig() {
        mTitle = ConfigManager.getInstance().getTitle();
        isShowCamera = ConfigManager.getInstance().isShowCamera();
        isShowImage = ConfigManager.getInstance().isShowImage();
        isShowVideo = ConfigManager.getInstance().isShowVideo();
        isSingleType = ConfigManager.getInstance().isSingleType();
        mPicDir = ConfigManager.getInstance().getCachePicDir();
        mMaxCount = ConfigManager.getInstance().getMaxCount();
        SelectionManager.getInstance().setMaxCount(mMaxCount);
        //载入历史选择记录
        mImagePaths = ConfigManager.getInstance().getImagePaths();
        if (mImagePaths != null && !mImagePaths.isEmpty()) {
            SelectionManager.getInstance().addImagePathsToSelectList(mImagePaths);
        }
    }

    private void initUi() {
        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.scanner_image));

        //顶部栏相关
        mTvTitle = findViewById(R.id.tv_actionBar_title);
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        mTvCommit = findViewById(R.id.tv_actionBar_commit);

        //滑动悬浮标题相关
        mTvImageTime = findViewById(R.id.tv_image_time);

        //底部栏相关
        mRlBottom = findViewById(R.id.rl_main_bottom);
        mTvImageFolders = findViewById(R.id.tv_main_imageFolders);

        //列表相关
        mRecyclerView = findViewById(R.id.rv_main_images);
        mGridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //注释说当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小。
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(60);

        mMediaFileList = new ArrayList<>();
        mImagePickerAdapter = new ImagePickerAdapter(this, mMediaFileList);
        mImagePickerAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mImagePickerAdapter);
    }

    private void initEvent() {
        findViewById(R.id.iv_actionBar_back).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        mTvCommit.setOnClickListener(v -> commitSelection());

        mTvImageFolders.setOnClickListener(view -> {
            if (mImageFolderPopupWindow != null) {
                setLightMode(LIGHT_OFF);
                mImageFolderPopupWindow.showAsDropDown(mRlBottom, 0, 0);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                updateImageTime();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateImageTime();
            }
        });
    }


    protected void startWhenPermissionGrant() {
        //进行权限的判断
        boolean hasPermission = checkPermission(this);
        if (!hasPermission) {
            //具有拍照权限，sd卡权限，开始扫描任务
            requestMultiplePermissionsLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, this::startScannerTask, new Callback2<List<String>, AppDetailsSettingsLauncher>() {
                @Override
                public void invoke(List<String> strings, AppDetailsSettingsLauncher appDetailsSettingsLauncher) {
                    //没有权限
                    Toast.makeText(ImagePickerActivity.this, getString(R.string.permission_tip), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            startScannerTask();
        }
    }

    /**
     * 开启扫描任务
     */
    private void startScannerTask() {
        Runnable mediaLoadTask = null;

        //照片、视频全部加载
        if (isShowImage && isShowVideo) {
            mediaLoadTask = new MediaScanTask(this, new MediaLoader());
        }

        //只加载视频
        if (!isShowImage && isShowVideo) {
            mediaLoadTask = new VideoScanTask(this, new MediaLoader());
        }

        //只加载图片
        if (isShowImage && !isShowVideo) {
            mediaLoadTask = new ImageScanTask(this, new MediaLoader());
        }

        //不符合以上场景，采用照片、视频全部加载
        if (mediaLoadTask == null) {
            mediaLoadTask = new MediaScanTask(this, new MediaLoader());
        }

        DefaultExecutor.getInstance().execute(mediaLoadTask);
    }


    /**
     * 处理媒体数据加载成功后的UI渲染
     */
    class MediaLoader implements MediaScanCallback {

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onLoadMedia(final List<MediaFolder> mediaFolderList) {
            boolean notDestroyed = assertNotDestroyed(ImagePickerActivity.this);
            if (!notDestroyed) return;
            runOnUiThread(() -> {
                if (!mediaFolderList.isEmpty()) {
                    //默认加载全部照片
                    mMediaFileList.addAll(mediaFolderList.get(0).getMediaFileList());
                    mImagePickerAdapter.notifyDataSetChanged();

                    //图片文件夹数据
                    mMediaFolderList = new ArrayList<>(mediaFolderList);
                    mImageFolderPopupWindow = new ImageFolderPopupWindow(ImagePickerActivity.this, mMediaFolderList);
                    mImageFolderPopupWindow.setAnimationStyle(R.style.imageFolderAnimator);
                    mImageFolderPopupWindow.getAdapter().setOnImageFolderChangeListener(ImagePickerActivity.this);
                    mImageFolderPopupWindow.setOnDismissListener(() -> setLightMode(LIGHT_ON));
                    updateCommitButton();
                }
                if (null != mProgressDialog) mProgressDialog.cancel();
            });
        }
    }

    private static boolean assertNotDestroyed(@NonNull Activity activity) {
        if (activity.isFinishing()) {
            return false;
        }
        if (activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    /**
     * 隐藏时间
     */
    private void hideImageTime() {
        if (isShowTime) {
            isShowTime = false;
            ObjectAnimator.ofFloat(mTvImageTime, "alpha", 1, 0).setDuration(300).start();
        }
    }

    /**
     * 显示时间
     */
    private void showImageTime() {
        if (!isShowTime) {
            isShowTime = true;
            ObjectAnimator.ofFloat(mTvImageTime, "alpha", 0, 1).setDuration(300).start();
        }
    }

    /**
     * 更新时间
     */
    private void updateImageTime() {
        int position = mGridLayoutManager.findFirstVisibleItemPosition();
        if (position != RecyclerView.NO_POSITION) {
            MediaFile mediaFile = mImagePickerAdapter.getMediaFile(position);
            if (mediaFile != null) {
                if (mTvImageTime.getVisibility() != View.VISIBLE) {
                    mTvImageTime.setVisibility(View.VISIBLE);
                }
                String time = Utils.getImageTime(mediaFile.getDateToken());
                mTvImageTime.setText(time);
                showImageTime();
                mMyHandler.removeCallbacks(mHideRunnable);
                mMyHandler.postDelayed(mHideRunnable, 1500);
            }
        }
    }

    /**
     * 设置屏幕的亮度模式
     *
     * @param lightMode
     */
    private void setLightMode(int lightMode) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        switch (lightMode) {
            case LIGHT_OFF:
                layoutParams.alpha = 0.7f;
                break;
            case LIGHT_ON:
                layoutParams.alpha = 1.0f;
                break;
        }
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 点击图片
     *
     * @param view
     * @param position
     */
    @Override
    public void onMediaClick(View view, int position) {
        if (isShowCamera) {
            if (position == 0) {
                if (!SelectionManager.getInstance().isCanChoose()) {
                    Toast.makeText(this, String.format(getString(R.string.select_image_max), mMaxCount), Toast.LENGTH_SHORT).show();
                    return;
                }
                showCamera();
                return;
            }
        }

        if (mMediaFileList != null) {
            DataUtil.getInstance().setMediaData(mMediaFileList);
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            if (isShowCamera) {
                intent.putExtra(ImagePreviewActivity.IMAGE_POSITION, position - 1);
            } else {
                intent.putExtra(ImagePreviewActivity.IMAGE_POSITION, position);
            }
            startActivityForResult(intent, REQUEST_SELECT_IMAGES_CODE);
        }
    }

    /**
     * 选中/取消选中图片
     *
     * @param view
     * @param position
     */
    @Override
    public void onMediaCheck(View view, int position) {
        if (isShowCamera) {
            if (position == 0) {
                if (!SelectionManager.getInstance().isCanChoose()) {
                    Toast.makeText(this, String.format(getString(R.string.select_image_max), mMaxCount), Toast.LENGTH_SHORT).show();
                    return;
                }
                showCamera();
                return;
            }
        }

        //执行选中/取消操作
        MediaFile mediaFile = mImagePickerAdapter.getMediaFile(position);
        if (mediaFile != null) {
            String imagePath = mediaFile.getPath();
            if (isSingleType) {
                //如果是单类型选取，判断添加类型是否满足（照片视频不能共存）
                ArrayList<String> selectPathList = SelectionManager.getInstance().getSelectPaths();
                if (!selectPathList.isEmpty()) {
                    //判断选中集合中第一项是否为视频
                    if (!SelectionManager.isCanAddSelectionPaths(imagePath, selectPathList.get(0))) {
                        //类型不同
                        Toast.makeText(this, getString(R.string.single_type_choose), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            boolean addSuccess = SelectionManager.getInstance().addImageToSelectList(imagePath);
            if (addSuccess) {
                mImagePickerAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(this, String.format(getString(R.string.select_image_max), mMaxCount), Toast.LENGTH_SHORT).show();
            }
        }
        updateCommitButton();
    }

    /**
     * 更新确认按钮状态
     */
    private void updateCommitButton() {
        //改变确定按钮UI
        int selectCount = SelectionManager.getInstance().getSelectPaths().size();
        if (selectCount == 0) {
            mTvCommit.setEnabled(false);
            mTvCommit.setText(getString(R.string.kennie_picker_confirm));
            return;
        }
        if (selectCount < mMaxCount) {
            mTvCommit.setEnabled(true);
            mTvCommit.setText(String.format(getString(R.string.kennie_picker_confirm_msg), selectCount, mMaxCount));
            return;
        }
        if (selectCount == mMaxCount) {
            mTvCommit.setEnabled(true);
            mTvCommit.setText(String.format(getString(R.string.kennie_picker_confirm_msg), selectCount, mMaxCount));
            return;
        }
    }

    /**
     * 跳转相机拍照
     */
    private void showCamera() {

        if (isSingleType) {
            //如果是单类型选取，判断添加类型是否满足（照片视频不能共存）
            ArrayList<String> selectPathList = SelectionManager.getInstance().getSelectPaths();
            if (!selectPathList.isEmpty()) {
                if (MediaFileUtil.isVideoFileType(selectPathList.get(0))) {
                    //如果存在视频，就不能拍照了
                    Toast.makeText(this, getString(R.string.single_type_choose), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        //拍照存放路径
        mFilePath = mPicDir.getAbsolutePath() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, ImagePickerProvider.getFileProviderName(this), new File(mFilePath));
        } else {
            uri = Uri.fromFile(new File(mFilePath));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE);
    }

    /**
     * 当图片文件夹切换时，刷新图片列表数据源
     *
     * @param view
     * @param position
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onImageFolderChange(View view, int position) {
        MediaFolder mediaFolder = mMediaFolderList.get(position);
        //更新当前文件夹名
        String folderName = mediaFolder.getFolderName();
        if (!TextUtils.isEmpty(folderName)) {
            mTvImageFolders.setText(folderName);
        }
        //更新图片列表数据源
        mMediaFileList.clear();
        mMediaFileList.addAll(mediaFolder.getMediaFileList());
        mImagePickerAdapter.notifyDataSetChanged();

        mImageFolderPopupWindow.dismiss();
    }

    /**
     * 拍照回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAPTURE) {
                //通知媒体库刷新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath)));
                //添加到选中集合
                SelectionManager.getInstance().addImageToSelectList(mFilePath);

                ArrayList<String> list = new ArrayList<>(SelectionManager.getInstance().getSelectPaths());
                Intent intent = new Intent();
                intent.putStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES, list);
                setResult(RESULT_OK, intent);
                SelectionManager.getInstance().removeAll();//清空选中记录
                finish();
            }

            if (requestCode == REQUEST_SELECT_IMAGES_CODE) {
                commitSelection();
            }
        }
    }

    /**
     * 选择图片完毕，返回
     */
    private void commitSelection() {
        ArrayList<String> list = new ArrayList<>(SelectionManager.getInstance().getSelectPaths());
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES, list);
        setResult(RESULT_OK, intent);
        SelectionManager.getInstance().removeAll(); // 清空选中记录
        finish();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        mImagePickerAdapter.notifyDataSetChanged();
        updateCommitButton();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (null != mProgressDialog) mProgressDialog.dismiss();
            ConfigManager.getInstance().setImagePaths(new ArrayList<>());//清空选中数据
            ConfigManager.getInstance().getImageLoader().clearMemoryCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean checkPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
}
