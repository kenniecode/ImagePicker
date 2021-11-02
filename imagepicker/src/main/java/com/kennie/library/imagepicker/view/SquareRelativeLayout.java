package com.kennie.library.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;


/**
 * @项目名 KennieImagePicker
 * @类名称 SquareRelativeLayout
 * @类描述 正方形RelativeLayout
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class SquareRelativeLayout extends RelativeLayout {
    public SquareRelativeLayout(Context context) {
        this(context, null);
    }

    public SquareRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}