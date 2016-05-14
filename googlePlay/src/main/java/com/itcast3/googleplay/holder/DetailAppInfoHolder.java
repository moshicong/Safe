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

public class DetailAppInfoHolder extends BaseHolder<AppInfo>{
	private TextView app_name,app_download,app_version,app_pub_time,app_size;
	private RatingBar app_star;
	private ImageView app_info_icon;
	
	@Override
	public void refreshView() {
		AppInfo data = getData();
		
		app_name.setText(data.getName());
		app_star.setRating(data.getStars());
		app_download.setText(data.getDownloadNum());
		app_version.setText(data.getVersion());
		app_pub_time.setText(data.getDate());
		app_size.setText(data.getSize()+"");
		
		BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils();
		bitmapUtils.display(app_info_icon,HttpHelper.URL+"image?name="+data.getIconUrl());
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_appinfo);
		
		app_info_icon = (ImageView) view.findViewById(R.id.app_icon);
		app_name = (TextView) view.findViewById(R.id.app_name);
		app_star = (RatingBar) view.findViewById(R.id.app_star);
		
		app_download = (TextView) view.findViewById(R.id.app_download);
		app_version = (TextView) view.findViewById(R.id.app_version);
		app_pub_time = (TextView) view.findViewById(R.id.app_pub_time);
		app_size = (TextView) view.findViewById(R.id.app_size);
		
		return view;
	}
}
