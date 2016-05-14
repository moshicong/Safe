package com.itcast3.googleplay.ui.widget;

import com.itcast3.googleplay.R;

import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		initListView();
	}

	private void initListView() {
		//去掉listView分割线操作
		this.setDivider(null);
		//去掉点中一个条目手机提供的背景色
		this.setSelector(R.drawable.nothing);
		//滚动过程中会出现黑色
		this.setCacheColorHint(Color.TRANSPARENT);
	}
}
