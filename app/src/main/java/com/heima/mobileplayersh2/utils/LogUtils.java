package com.heima.mobileplayersh2.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/11/13.
 */
public class LogUtils {
    private static final boolean ENABLE = true;

    /**
     * 打印一个debug等级的log
     */
    public static void d(String tag, String msg) {
        if (ENABLE) {
            Log.d("itcast_" + tag, msg);
        }
    }

    /**
     * 打印一个 error 等级的log
     */
    public static void e(String tag, String msg) {
        if (ENABLE) {
            Log.e("itcast_" + tag, msg);
        }
    }

    /**
     * 打印一个 error 等级的log
     */
    public static void e(Class cls, String msg) {
        if (ENABLE) {
            Log.e("itcast_" + cls.getSimpleName(), msg);
        }
    }
}
