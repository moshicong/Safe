package com.itcast3.googleplay.adapter;

import java.util.List;

import com.itcast3.googleplay.holder.BaseHolder;
import com.itcast3.googleplay.holder.MoreHolder;
import com.itcast3.googleplay.manager.ThreadManager;
import com.itcast3.googleplay.manager.ThreadManager.ThreadProxyPool;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public  abstract class MyBaseAdapter<T> extends BaseAdapter {
	public List<T> list;
	private BaseHolder holder;
	
	//定义加载更多和一般条目的标示码
	private int LIST_VIEW_ITEM = 0;
	private int LOAD_MORE_ITEM = 1;
	
	private MoreHolder moreHolder;
	
	//指定listview中总条目的类型数,因为要将加载更多也作为一种条目类型存在,所以类型个数+1
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount()+1;
	}
	
	//判断当前指向的item所属条目类型(0,19,20加载更多)
	@Override
	public int getItemViewType(int position) {
		if(getCount() -1 == position){
			//加载更多的条
			return LOAD_MORE_ITEM;
		}else{
			//一般条目
			return getInnerType(position);
		}
	}
	
	public int getInnerType(int position) {
		return LIST_VIEW_ITEM;
	}

	public MyBaseAdapter(List<T> list) {
		this.list = list;
	}
	
	//手动的多添加一个条目,添加加的这个条目就是加载更多
	@Override
	public int getCount() {
		return list.size()+1;
	}
	
	//返回当前请求返回条目个数的方法
	public int getListSize(){
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	//显示listView中一个条目的方法,如果看见当前条目,则一定调用过getView方法,并且执行结束
	@Override
	public  View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
			//创建一个holder,但是不同界面需要的holder结构都不一样,那创建holder的过程在此类中就没法实现
			//如果对应索引指向的是最后的一个条目,创建加载更多的Holder
			if(getItemViewType(position) == LOAD_MORE_ITEM){
				holder = getMoreHolder();
			}else{
				//如果对应的索引不是最后一个条目,创建一般类型的holder
				holder = getHolder();
			}
		}else{
			//如果convertView中有tag,则直接将其获取出来,获取到的tag就是子类实现getHolder方法中生成的holder对象
			if(convertView.getTag()!=null){
				holder = (BaseHolder) convertView.getTag();
			}
		}
		
		//数据放置到控件上面去的操作
		
		//更多类型条目是不能去使用list集合中的数据,只有一般类型条目可以去使用list集合中的数据,所以在此次要去判断所属条目类型
		if(getItemViewType(position) == LIST_VIEW_ITEM){
			holder.setData(list.get(position));
		}
		
		//返回每一个item中,xml--->view对象
		return holder.getRootView();
	}
	
	//因为所有界面加载更多的UI显示效果都是一致的,所以加载更多的业务逻辑可以做详细处理
	private BaseHolder getMoreHolder() {
		if(moreHolder == null){
			moreHolder = new MoreHolder(hasMore(),this);
		}
		return moreHolder;
	}

	//是为了预留,屏蔽加载更多的方法
	public boolean hasMore() {
		return true;
	}

	public abstract BaseHolder getHolder();

	//现在listView加载更多操作,封装在了listview使用数据适配器的父类中
	public void loadMore() {
		//因为首页,应用,游戏,专题节目加载更多的请求链接地址是不一致,所以不能有其请求网络的具体逻辑实现,将具体实现抽象到子类中
		
	/*	new Thread(){
			public void run() {
				//第二次请求
				final List<T> onLoadMore = onLoadMore();
				//分页请求数据操作,服务端维护好了,每一页最多有20条数据
				
				UIUtils.runInMainThread(new Runnable() {
					@Override
					public void run() {
						//40 20 20 0
						//判断第三请求是否有更多数据
						//如果第二次请求有满的20条数据,认为还有更多的后续数据
						//如果请求获取的数据小于20条,就认为没有更多
						if(onLoadMore == null){
							//让MoreHolder去显示,加载失败,点击重试
							moreHolder.setData(MoreHolder.load_more_error);
						}else{
							if(onLoadMore.size() == 20){
								//认为下一次请求还有更多
								moreHolder.setData(MoreHolder.has_more);
							}else if(onLoadMore.size()<20){
								//认为下一次没有更多数据了
								moreHolder.setData(MoreHolder.no_more);
							}
						}
						
						//将获取更多的数据放置到数据适配器中
						if(onLoadMore!=null && list!=null){
							list.addAll(onLoadMore);
						}
						notifyDataSetChanged();
					}
				});
			};
		}.start();*/
		
		ThreadManager.getThreadProxyPool().execute(new RunnableTask());
	}
	
	class RunnableTask implements Runnable{
		@Override
		public void run() {
			//第二次请求
			final List<T> onLoadMore = onLoadMore();
			//分页请求数据操作,服务端维护好了,每一页最多有20条数据
			
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					//40 20 20 0
					//判断第三请求是否有更多数据
					//如果第二次请求有满的20条数据,认为还有更多的后续数据
					//如果请求获取的数据小于20条,就认为没有更多
					if(onLoadMore == null){
						//让MoreHolder去显示,加载失败,点击重试
						moreHolder.setData(MoreHolder.load_more_error);
					}else{
						if(onLoadMore.size() == 20){
							//认为下一次请求还有更多
							moreHolder.setData(MoreHolder.has_more);
						}else if(onLoadMore.size()<20){
							//认为下一次没有更多数据了
							moreHolder.setData(MoreHolder.no_more);
						}
					}
					
					//将获取更多的数据放置到数据适配器中
					if(onLoadMore!=null && list!=null){
						list.addAll(onLoadMore);
					}
					notifyDataSetChanged();
				}
			});
		}
	}
	
	//具体每一个子类界面的请求更多操作,就在这个方法的实现体中去做
	public abstract List<T> onLoadMore(); 
}
