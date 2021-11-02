package com.kennie.library.imagepicker.task;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * @项目名 KennieImagePicker
 * @类名称 DefaultExecutor
 * @类描述 默认线程池
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class DefaultExecutor {

    private static final int THREAD_COUNT = 3;

    private static volatile DefaultExecutor mDefaultExecutor;
    private ExecutorService mExecutorService;

    private DefaultExecutor() {
        mExecutorService = Executors.newFixedThreadPool(THREAD_COUNT,new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("CommonExecutor");
                return thread;
            }
        });
    }

    public static DefaultExecutor getInstance() {
        if (mDefaultExecutor == null) {
            synchronized (DefaultExecutor.class) {
                if (mDefaultExecutor == null) {
                    mDefaultExecutor = new DefaultExecutor();
                }
            }
        }
        return mDefaultExecutor;
    }


    public void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }
}