package com.itcast3.googleplay.util;

import com.itcast3.googleplay.application.BaseApplication;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

public class UIUtils {
	//提供获取上下环境方法
	public static Context getContext(){
		return BaseApplication.getContext();
	}
	//Handler
	public static Handler getHandler(){
		return BaseApplication.getHandler();
	}
	
	//获取主线程对象
	public static Thread getMainThread(){
		return BaseApplication.getMianThread();
	}
	//获取主线程id
	public static int getMainThreadId(){
		return BaseApplication.getMainThreadId();
	}
	
	//xml--->view
	public static View inflate(int layoutId){
		return View.inflate(getContext(), layoutId, null);
	}
	//获取资源文件夹
	public static Resources getResources(){
		return getContext().getResources();
	}
	//获取string操作
	public static String getString(int stringId){
		return getResources().getString(stringId);
	}
	//获取drawable
	public static Drawable getDrawable(int drawableId){
		return getResources().getDrawable(drawableId);
	}
	//获取stringArray数组
	public static String[] getStringArray(int stringArrayId){
		return getResources().getStringArray(stringArrayId);
	}
	//手机的像素密度跟文档中的最接近值
	//1dp = 0.75px
	//1dp = 1px
	//1dp = 1.5px
	//1dp = 2px
	//1dp = 3px
	
	//dip--->px
	public static int dip2px(int dip){
		//获取dip和px的比例关系
		float d = getResources().getDisplayMetrics().density;
		// (int)(80.4+0.5)   (int)(80.6+0.5)
		return (int)(dip*d+0.5);
	}
	
	//px---->dip
	public static int px2dip(int px){
		float d = getResources().getDisplayMetrics().density;
		return (int)(px/d+0.5);
	}
	
	//判读当前类运行的线程是否是主线程
	public static boolean isRunInMainThread(){
		return android.os.Process.myTid() == getMainThreadId();
	}
	
	//通过消息机制将数据返回到主线程中,然后去填充UI
	public static void runInMainThread(Runnable runnable){
		if(isRunInMainThread()){
			//如果当前任务就是在主线中的
			runnable.run();
		}else{
			//如果不在主线程中,Handler发消息,
			getHandler().post(runnable);
		}
	}
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		//根据颜色选择器id,获取颜色选择器对象的方法
		return getResources().getColorStateList(mTabTextColorResId);
	}
	public static int getDimens(int dimenId) {
		return UIUtils.getResources().getDimensionPixelSize(dimenId);
	}
	
	//延时执行任务
	public static void postDelayed(Runnable runnable, long delayedTime) {
		getHandler().postDelayed(runnable, delayedTime);
	}
	//移除handler中维护的任务
	public static void removeCallBack(Runnable r) {
		getHandler().removeCallbacks(r);
	} 
}
