package com.itcast3.googleplay.holder;

import java.util.ArrayList;
import java.util.List;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HomeHeaderHolder extends BaseHolder<List<String>>{
	private List<String> data;
	private ViewPager viewPager;
	private RelativeLayout relativeLayout;
	//创建一个管理点的集合
	private List<View> dotList = new ArrayList<View>();

	@Override
	public void refreshView() {
		//viewpager中轮播图片的链接地址集合
		data = getData();
		
		//viewpager设置数据适配器
		viewPager.setAdapter(new MyPagerAdapter());
		
		LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
		android.widget.RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		//设置相对布局内部放置的线性布局在,右下角
		rlparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		relativeLayout.addView(linearLayout, rlparams);
		
		//设置点的大小
		android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				UIUtils.dip2px(6), UIUtils.dip2px(6));
		//设置点的间距
		layoutParams.setMargins(0, 0, UIUtils.dip2px(6), UIUtils.dip2px(6));
		
		linearLayout.removeAllViews();
		dotList.clear();
		
		for(int i=0;i<data.size();i++){
			View view = new View(UIUtils.getContext());
			if(i == 0){
				view.setBackgroundResource(R.drawable.indicator_selected);
			}else{
				view.setBackgroundResource(R.drawable.indicator_normal);
			}
			//在添加点的时候,需要加上间距,以及点的大小
			linearLayout.addView(view, layoutParams);
			dotList.add(view);
		}
		
		//滚动过程中,点要跳跃,图片也需要跳跃,并且做相应绑定,监听viewpager的选中界面发生改变的方法,从而去修改点的选中状态
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				int index = arg0%data.size();
				for(int i=0;i<dotList.size();i++){
					View view = dotList.get(i);
					if(i == index){
						view.setBackgroundResource(R.drawable.indicator_selected);
					}else{
						view.setBackgroundResource(R.drawable.indicator_normal);
					}
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		//设置viewpager滑动的初始位置
		viewPager.setCurrentItem(1000*data.size());
		
		//定时任务,将图片轮播起来
		new RunnableTask().start();
	}
	
	class RunnableTask implements Runnable{
		public void start() {
			//由start方法去触发run方法内部代码的执行,通过handler发送延时的任务
			UIUtils.removeCallBack(this);
			UIUtils.postDelayed(this,2000);
		}
		@Override
		public void run() {
			//跳转到下一个图片
			int prePosition = viewPager.getCurrentItem()+1;
			viewPager.setCurrentItem(prePosition);
			
			//一直执行此任务的过程,执行新的任务之前,将原有任务移除掉
			UIUtils.removeCallBack(this);
			UIUtils.postDelayed(this,2000);
		}
	}
	
	@Override
	public View initView() {
		//构建布局效果
		relativeLayout = new RelativeLayout(UIUtils.getContext());
		LayoutParams layoutParams = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, 
				//将dimens.xml中dp转换成像素,作为参数
				UIUtils.getDimens(R.dimen.list_header_height));
		//将listview指定的宽高规则,作用在相对布局上
		relativeLayout.setLayoutParams(layoutParams);
		
		//创建放置轮播图片的viewpager
		viewPager = new ViewPager(UIUtils.getContext());
		android.widget.RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		relativeLayout.addView(viewPager, rllp);
		
		return relativeLayout;
	}
	
	class MyPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(UIUtils.getContext());
			BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils();
			bitmapUtils.display(imageView,HttpHelper.URL+"image?name="+data.get(position%data.size()));
			
			container.addView(imageView);
			
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
}
