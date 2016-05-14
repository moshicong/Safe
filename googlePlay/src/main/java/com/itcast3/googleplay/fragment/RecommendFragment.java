package com.itcast3.googleplay.fragment;

import java.util.List;
import java.util.Random;

import com.itcast3.googleplay.protocol.RecommendProtocol;
import com.itcast3.googleplay.randomLayout.StellarMap;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class RecommendFragment extends BaseFragment {
	private List<String> data;

	@Override
	public View onCreateViewSuccessed() {
		StellarMap stellarMap = new StellarMap(UIUtils.getContext());
		//设置内部文字和手机边缘边距的方法
		int padding = UIUtils.dip2px(10);
		stellarMap.setInnerPadding(padding, padding, padding, padding);
		//设置上了数据适配器后,就有飞入飞出效果
		stellarMap.setAdapter(new MyAdapter());
		//设置界面返回TextView总数的配套规则
		stellarMap.setRegularity(6,9);
		//飞入,飞出的动画从那组开始
		stellarMap.setGroup(0, true);
		return stellarMap;
	}

	@Override
	public ResultState onLoad() {
		RecommendProtocol recommendProtocol = new RecommendProtocol();
		data = recommendProtocol.getData(0);
		return check(data);
	}
	
	class MyAdapter implements StellarMap.Adapter{
		//返回组的个数
		@Override
		public int getGroupCount() {
			return 2;
		}

		//返回界面中要去显示的文字的个数
		@Override
		public int getCount(int group) {
			return 15;
		}

		//创建要去显示文字的控件
		@Override
		public View getView(int group, int position, View convertView) {
			TextView textView = new TextView(UIUtils.getContext());
			textView.setText(data.get(position));
			
			//随机颜色(r,g,b) 
			//(0-255,0-255,0-255) (0,0,0)黑色  (255,255,255)纯白色
			int red = 30 + new Random().nextInt(210);
			int green = 30 + new Random().nextInt(210);
			int blue = 30 + new Random().nextInt(210);
			
			int rgb = Color.rgb(red, green, blue);
			textView.setTextColor(rgb);
			
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16+new Random().nextInt(10));
			
			return textView;
		}

		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return 0;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			return 1;
		}
	}
}
