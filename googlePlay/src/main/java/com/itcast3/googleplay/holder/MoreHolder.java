package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.adapter.MyBaseAdapter;
import com.itcast3.googleplay.adapter.MyBaseAdapter1;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreHolder extends BaseHolder<Integer>{
	//通过三个状态(有更多数据,没有更多数据,加载更多出错)维护加载更多现实样式
	public static int has_more = 0;
	public static int no_more = 1;
	public static int load_more_error = 2;
	
	private LinearLayout load_more_ll;
	private TextView load_more_error_tv;
	private MyBaseAdapter adapter;
	
	//一进入应用的时候,就默认有更多数据,尝试着去请求网络,展示加载更多的进度条,
	//1,在第二次请求网络的过程中确实拿到更多数据了,有更多
	//2,在第二次请求过程中,没有拿到数据,没有更多,进度条以后都不要再显示出来
	
	//hasMore的作用就是,去判断创建MoreHolder对象的时候,是否默认有更多数据
	public MoreHolder(boolean hasMore,MyBaseAdapter adapter) {
		setData(hasMore?has_more:no_more);
		this.adapter = adapter;
	}



	@Override
	public void refreshView() {
		//对应MoreHolder,其数据就是以上状态中的其中一种
		Integer data = getData();
		
		if(data == has_more){
			//有更多数据
			load_more_ll.setVisibility(View.VISIBLE);
			load_more_error_tv.setVisibility(View.GONE);
		}
		
		if(data == no_more){
			load_more_ll.setVisibility(View.GONE);
			load_more_error_tv.setVisibility(View.GONE);
		}
		
		if(data == load_more_error){
			//加载更多数据失败
			load_more_ll.setVisibility(View.GONE);
			load_more_error_tv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public View initView() {
		//生成加载更多的布局结构,并且将其转换成view对象
		View view = UIUtils.inflate(R.layout.layout_load_more);
		//正在加载的线性布局
		load_more_ll = (LinearLayout) view.findViewById(R.id.load_more_ll);
		//加载失败的textView
		load_more_error_tv = (TextView) view.findViewById(R.id.load_more_error_tv);
		return view;
	}

	//进度条(显示)拖拽出来以后,就真的要尝试着请求网络,重写BaseHolder中的getRootView方法,添加上请求网络的逻辑
	@Override
	public View getRootView() {
		//有数据,请求网络
		//将加载更多的逻辑,放到基本数据适配器中处理MyBaseAdapter
		if(getData() == has_more && adapter!=null){
			adapter.loadMore();
		}
		return super.getRootView();
	}
}
