package com.kennie.library.imagepicker.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kennie.library.imagepicker.R;


/**
 * @项目名 KennieImagePicker
 * @类名称 BaseActivity
 * @类描述 BaseActivity基类
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public abstract class BaseActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在5.0系统以上设置状态栏颜色
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.tool_bar_color));

        setContentView(bindLayout());

        initConfig();
        initView();
        initListener();
        getData();
    }

    protected abstract int bindLayout();

    protected void initConfig() {
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void getData();
}