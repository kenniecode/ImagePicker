package com.kennie.library.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * @项目名 KennieImagePicker
 * @类名称 SquareImageView
 * @类描述 正方形ImageView
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}