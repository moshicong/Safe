package com.itcast3.googleplay.fragment;

import java.util.List;

import com.itcast3.googleplay.HomeDetailActivity;
import com.itcast3.googleplay.adapter.MyBaseAdapter;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.holder.BaseHolder;
import com.itcast3.googleplay.holder.HomeHeaderHolder;
import com.itcast3.googleplay.holder.HomeHolder;
import com.itcast3.googleplay.protocol.HomeProtocol;
import com.itcast3.googleplay.ui.widget.MyListView;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeFragment extends BaseFragment {
	private List<AppInfo> data;
	private HomeProtocol homeProtocol;

	//返回首页成功展示数据的view对象
	@Override
	public View onCreateViewSuccessed() {
		MyListView listView = new MyListView(UIUtils.getContext());
		
		HomeHeaderHolder homeHeaderHolder = new HomeHeaderHolder();
		homeHeaderHolder.setData(homeProtocol.getPicUrlList());
		
		if(listView.getHeaderViewsCount()<1){
			listView.addHeaderView(homeHeaderHolder.getRootView());
		}
		listView.setAdapter(new MyAdapter(data,listView));
		return listView;
	}
	/* 将以下的四套逻辑抽取到BaseFragment中去做管理
	 * 1,成功展示数据的界面效果()
	   2,获取数据为空
	   3,请求网络失败
	   4,正在加载
	*/
	//每一个子类界面的联网操作
	@Override
	public ResultState onLoad() {
		//发送第一次请求
		homeProtocol = new HomeProtocol();
		data = homeProtocol.getData(0);
		//判断data中数据,从而决定显示界面(成功,失败,为空)的逻辑
		return check(data);
	}

	class  MyAdapter extends MyBaseAdapter<AppInfo> implements OnItemClickListener{
		private ListView listView;
		public MyAdapter(List<AppInfo> list,ListView listView) {
			super(list);
			this.listView = listView;
			listView.setOnItemClickListener(this);
		}

		//创建HomeFragment中ListView使用数据适配器的holder
		@Override
		public BaseHolder getHolder() {
			return new HomeHolder();
		}

		//首页加载更多的具体实现
		@Override
		public List<AppInfo> onLoadMore() {
			HomeProtocol homeProtocol = new HomeProtocol();
			//服务端返回的数据条目个数,就是解析过后封装到list集合的大小
			List<AppInfo> moreData = homeProtocol.getData(getListSize());
			return moreData;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			Intent intent = new Intent(UIUtils.getContext(),HomeDetailActivity.class);
			intent.putExtra("packageName",list.get(arg2-1).getPackageName());
			startActivity(intent);
		}
	}
}
