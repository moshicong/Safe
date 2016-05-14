package com.itcast3.googleplay.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Administrator on 2016/5/13.
 */
public class BaseApplication1 extends Application {
    private static Context context;
    private static Handler handler;
    private static Thread mainThread;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();
        super.onCreate();
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Thread getMainThread() {
        return mainThread;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }


    }

