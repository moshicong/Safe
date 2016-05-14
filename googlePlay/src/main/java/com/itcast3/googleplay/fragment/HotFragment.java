package com.itcast3.googleplay.fragment;

import java.util.List;
import java.util.Random;

import com.itcast3.googleplay.protocol.HotProtocol;
import com.itcast3.googleplay.ui.widget.FlowLayout;
import com.itcast3.googleplay.ui.widget.FlowerLayout;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.DrawableUtil;
import com.itcast3.googleplay.util.UIUtils;

import android.R.color;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HotFragment extends BaseFragment {
	private List<String> data;

	@Override
	public View onCreateViewSuccessed() {
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		//在scrollView内部去添加自定义控件
		int padding = UIUtils.dip2px(10);
		scrollView.setPadding(padding, padding, padding, padding);
		
		
//		//测量onMeasure 布局onlayout  绘画onDraw
//		FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
//		
//		//设置每一个行间textView水平方向间距
//		int horizontalSpacing = UIUtils.dip2px(6);
//		flowLayout.setHorizontalSpacing(horizontalSpacing);
//		
//		//设置行于行竖直方向间距
//		int verticalSpacing = UIUtils.dip2px(10);
//		flowLayout.setVerticalSpacing(verticalSpacing);
		
		FlowerLayout flowLayout = new FlowerLayout(UIUtils.getContext());
		
		//在FlowLayout添加TextView过程中,texView的个数,就是data.size()
		for(int i=0; i<data.size();i++){
			final TextView textView = new TextView(UIUtils.getContext());
			textView.setTextColor(Color.WHITE);
			//文字居中
			textView.setGravity(Gravity.CENTER);
			//给控件设置文字
			textView.setText(data.get(i));
			
			int red = 30+new Random().nextInt(210);
			int green = 30+new Random().nextInt(210);
			int blue = 30+new Random().nextInt(210);
			
			//此颜色用于绘画textView的背景
			int rgb = Color.rgb(red, green, blue);
			Drawable drawableNormal = DrawableUtil.getGradientDrawable(rgb, UIUtils.dip2px(6));

			//创建选中的背景图片
			//偏白色
			int pressRgb = 0xffcecece;
			Drawable drawablePress = DrawableUtil.getGradientDrawable(pressRgb, UIUtils.dip2px(6));
			
			StateListDrawable stateListDrawable = DrawableUtil.getStateListDrawable(drawablePress, drawableNormal);
			
			//设置包含了背景选择器的背景图
			textView.setBackgroundDrawable(stateListDrawable);
			
			textView.setPadding(padding, padding, padding, padding);

			//给textView设置点击事件,让其可以被点击
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), textView.getText().toString(), 0).show();
				}
			});
			
			flowLayout.addView(textView);
		}
		scrollView.addView(flowLayout);
		return scrollView;
	}
	
	
	@Override
	public ResultState onLoad() {
		HotProtocol hotProtocol = new HotProtocol();
		data = hotProtocol.getData(0);
		return check(data);
	}
}
