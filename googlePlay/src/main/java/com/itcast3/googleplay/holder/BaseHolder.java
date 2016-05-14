package com.itcast3.googleplay.holder;

import android.view.View;

public abstract class BaseHolder<T>{
	private View view;
	private T mData;

	public BaseHolder() {
		//xml--->view对象的操作,但是具体实现是未知的,每一个listview中item的布局都不一致
		view = initView();
		//给在ListView中展示的item对应view对象设置tag的操作,tag的值为holder对象
		view.setTag(this);
	}
	
	//提供一个listview中item转换成view对象返回的方法
	public View getRootView(){
		return view;
	}
	
	//数据传递进来给view对象使用的方法
	public void setData(T mData){
		this.mData = mData;
		//将传递进来的数据,放置到控件上面去
		refreshView();
	}
	
	public T getData(){
		return mData;
	}

	//数据未知(因为是泛型),控件也未知(因为都封装在view中),所以此方法无法实现,抽象
	public abstract void refreshView() ;
	public abstract View initView() ;
}
