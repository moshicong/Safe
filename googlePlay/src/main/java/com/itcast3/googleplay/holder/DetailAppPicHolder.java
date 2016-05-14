package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;

public class DetailAppPicHolder extends BaseHolder<AppInfo>{
	private ImageView[] imageViews;
	@Override
	public void refreshView() {
		//获取数据,放置到控件上面去
		AppInfo data = getData();
		
		//水平滚动图片的链接地址所在的集合
//		data.getScreenList();
		//放置生成多个bitmapUtils对象,导致每个bitmapUtil使用的内存叠加内存溢出
		BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils();
		
		for(int i=0;i<5;i++){
			if(i<data.getScreenList().size()){
				//如果集合中对应的索引值会小于图片的总张数,才需要将图片控件显示出来,并且给其从网络内存中获取图片
				imageViews[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(imageViews[i],HttpHelper.URL+"image?name="+data.getScreenList().get(i));
			}else{
				//INVISIBLE占位置   GONE不占位置
				imageViews[i].setVisibility(View.GONE);
			}
		}
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_apppic_srceen);
		
		imageViews = new ImageView[5];
		
		imageViews[0] = (ImageView) view.findViewById(R.id.image1);
		imageViews[1] = (ImageView) view.findViewById(R.id.image2);
		imageViews[2] = (ImageView) view.findViewById(R.id.image3);
		imageViews[3] = (ImageView) view.findViewById(R.id.image4);
		imageViews[4] = (ImageView) view.findViewById(R.id.image5);
		
		return view;
	}
}
