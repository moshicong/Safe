package com.itcast3.googleplay.util;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelp {
	private BitmapHelp(){};
	//提供获取唯一的一个BitmapUtil对象的方法
	private static BitmapUtils bitmapUtils = null;
	public static BitmapUtils getBitmapUtils(){
		if(bitmapUtils == null){
			bitmapUtils = new BitmapUtils(UIUtils.getContext());
		}
		//内存(LRU,让图片使用的内存不会内存溢出),文件,网络
		return bitmapUtils;
	}
}
