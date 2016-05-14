package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HomeHolder extends BaseHolder<AppInfo>{
	private ImageView app_icon_iv;
	private RatingBar app_rating_bar_star;
	private BitmapUtils bitmapUtils;
	private TextView app_name_tv,app_des_tv,app_size_tv;

	@Override
	public void refreshView() {
		AppInfo data = getData();
		app_des_tv.setText(data.getDes());
		app_rating_bar_star.setRating(data.getStars());
		app_size_tv.setText(data.getSize()+"");
		app_name_tv.setText(data.getName());
		
		//网络异步下载,缓存图片,BitmapUtils,保证只有一个BitmapUtils对象在内存中
		bitmapUtils = BitmapHelp.getBitmapUtils();

		//View ImageView Button TextView
		//ViewGroup 五大布局 ViewPager
		bitmapUtils.display(app_icon_iv,HttpHelper.URL+"image?name="+data.getIconUrl());
	}

	//编写对应Home界面中item的布局结构
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_home_item);
		app_des_tv = (TextView) view.findViewById(R.id.app_des_tv);
		app_icon_iv = (ImageView) view.findViewById(R.id.app_icon_iv);
		app_rating_bar_star = (RatingBar) view.findViewById(R.id.app_rating_bar_star);
		app_size_tv = (TextView) view.findViewById(R.id.app_size_tv);
		app_name_tv = (TextView) view.findViewById(R.id.app_name_tv);
		return view;
	}
}
