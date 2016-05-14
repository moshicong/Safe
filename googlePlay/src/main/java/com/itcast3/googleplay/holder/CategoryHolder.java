package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.CategoryInfo;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryHolder extends BaseHolder<CategoryInfo>{
	private ImageView category_item_iv1,category_item_iv2,category_item_iv3;
	private TextView category_item_tv1,category_item_tv2,category_item_tv3;

	@Override
	public void refreshView() {
		CategoryInfo data = getData();
		
		category_item_tv1.setText(data.getName1());
		category_item_tv2.setText(data.getName2());
		category_item_tv3.setText(data.getName3());
		
		BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils();
		
		bitmapUtils.display(category_item_iv1, HttpHelper.URL+"image?name="+data.getUrl1());
		bitmapUtils.display(category_item_iv2, HttpHelper.URL+"image?name="+data.getUrl2());
		bitmapUtils.display(category_item_iv3, HttpHelper.URL+"image?name="+data.getUrl3());
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_category_item);
		category_item_iv1 = (ImageView) view.findViewById(R.id.category_item_iv1);
		category_item_iv2 = (ImageView) view.findViewById(R.id.category_item_iv2);
		category_item_iv3 = (ImageView) view.findViewById(R.id.category_item_iv3);
		
		category_item_tv1 = (TextView) view.findViewById(R.id.category_item_tv1);
		category_item_tv2 = (TextView) view.findViewById(R.id.category_item_tv2);
		category_item_tv3 = (TextView) view.findViewById(R.id.category_item_tv3);
		return view;
	}
}
