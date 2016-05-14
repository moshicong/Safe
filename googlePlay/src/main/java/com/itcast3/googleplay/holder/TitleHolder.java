package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.CategoryInfo;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;
import android.widget.TextView;

public class TitleHolder extends BaseHolder<CategoryInfo>{
	public TitleHolder(CategoryInfo categoryInfo) {
		setData(categoryInfo);
	}
	private TextView category_title_tv;

	@Override
	public void refreshView() {
		CategoryInfo data = getData();
		category_title_tv.setText(data.getTitle());
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_category_title);
		category_title_tv = (TextView) view.findViewById(R.id.category_title_tv);
		return view;
	}
}
