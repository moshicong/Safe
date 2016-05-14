package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.SubjectInfo;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectHolder extends BaseHolder<SubjectInfo>{
	private ImageView subject_pic_iv;
	private TextView subject_des_tv;

	//数据放置到控件上去的方法
	@Override
	public void refreshView() {
		SubjectInfo data = getData();
		subject_des_tv.setText(data.getDes());
		BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils();
		bitmapUtils.display(subject_pic_iv,HttpHelper.URL+"image?name="+data.getUrl());
	}

	//将SubjectFragment中listview执行item的布局(xml),转换成view对象
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_subject_item);
		subject_pic_iv = (ImageView) view.findViewById(R.id.subject_pic_iv);
		subject_des_tv = (TextView) view.findViewById(R.id.subject_des_tv);
		return view;
	}
}
