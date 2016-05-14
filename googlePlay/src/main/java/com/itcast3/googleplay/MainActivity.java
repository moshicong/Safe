package com.itcast3.googleplay;

import com.itcast3.googleplay.factory.FragmentFactory;
import com.itcast3.googleplay.fragment.BaseFragment;
import com.itcast3.googleplay.ui.widget.PagerTab;
import com.itcast3.googleplay.util.UIUtils;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class MainActivity extends BaseActivity {

	private PagerTab pagerTab;
	private ViewPager pager;
	
	private ActionBar supportActionBar;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout drawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//顶部顶部指针自定义控件,底部viewpager,viewpager去放置view对象
		//view对象来源??PagerAdapter中由instantiateItem提供
		//由Fragment的oncreateView方法提供
		
		//获取顶部指针
		pagerTab = (PagerTab) findViewById(R.id.pagerTab);
		pager = (ViewPager) findViewById(R.id.pager);
		
		//创建一个viewpager的数据适配器,继承至FragmentPagerAdapter
		pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		//指针和viewpager一一对应的关系,绑定在一起
		pagerTab.setViewPager(pager);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		
		initActionBar();
		
		pagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				//根据索引获取fragment对象
				BaseFragment createFragment = FragmentFactory.createFragment(arg0);
				//请求网络方法
				createFragment.show();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	private void initActionBar() {
		supportActionBar = getSupportActionBar();
		//左上角的应用名称的设置
		supportActionBar.setTitle(UIUtils.getString(R.string.app_name));
		
		supportActionBar.setDisplayHomeAsUpEnabled(true);//左上角可以设置图片
		supportActionBar.setHomeButtonEnabled(true);//可用，可以被点击

		//弹出缩放的方法
//		toggle
		
		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this, 
				drawerLayout, 
				R.drawable.ic_drawer_am, 
				
				R.string.drawer_open, 
				R.string.drawer_close);
		
		//调用当前同步方法，才可以将侧拉菜单和按钮的点击操作绑定起来
		actionBarDrawerToggle.syncState();
	}
	
	class MyAdapter extends FragmentPagerAdapter{
		private String[] stringArrays;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			//初始操作,指针标题获取出来
			stringArrays = UIUtils.getStringArray(R.array.tab_names);
		}
		//返回指针绑定的Fragment对象,oncreateView方法的返回view
		@Override
		public Fragment getItem(int arg0) {
			//根据传递进来的索引,生成fragment,然后将其oncreateView返回值结果view展示在界面上
			//代码的优化
			//FragmentFactory
			return FragmentFactory.createFragment(arg0);
		}

		@Override
		public int getCount() {
			//模块的总个数
			return stringArrays.length;
		}
		
		//给应用去设置标题
		@Override
		public CharSequence getPageTitle(int position) {
			return stringArrays[position];
		}
	}
	
	//点击了菜单上按钮的时候会去触发的方法,actionBarDrawerToggle左上角按钮掉onOptionsItemSelected要去拖拽出侧拉菜单
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item) || actionBarDrawerToggle.onOptionsItemSelected(item);
	}
}
