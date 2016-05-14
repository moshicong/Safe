package com.itcast3.googleplay.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class BaseApplication extends Application {
	//上下文环境
	private static Context context;
	//handler对象
	private static Handler handler;
	//主线程对象
	private static Thread mianThread;
	//主线程id
	private static int mainThreadId;
	@Override
	public void onCreate() {
		super.onCreate();
		//在此次去做工程中必须要用到变量的初始化操作
		context = getApplicationContext();
		handler = new Handler();
		//获取主线程对象
		mianThread = Thread.currentThread();
		//获取主线程id,获取当前类的线程id(Application运行在主线程中)
		mainThreadId = android.os.Process.myTid();
	}
	//alt+shift+s+R
	public static Context getContext() {
		return context;
	}
	public static Handler getHandler() {
		return handler;
	}
	public static Thread getMianThread() {
		return mianThread;
	}
	public static int getMainThreadId() {
		return mainThreadId;
	}
}
