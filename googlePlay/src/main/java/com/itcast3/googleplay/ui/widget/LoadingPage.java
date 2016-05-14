package com.itcast3.googleplay.ui.widget;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.manager.ThreadManager;
import com.itcast3.googleplay.util.UIUtils;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

//由BaseFragment将四个界面展示逻辑抽取出来管理的LoadingPage
public abstract class LoadingPage extends FrameLayout {
	/* 1,成功展示数据的界面效果
	   2,获取数据为空
	   3,请求网络失败
	   4,正在加载
		*/
	//根据状态去决定,LoadingPage中展示那个界面
	private int STATE_UNLOAD = 0;
	private int STATE_LOADING = 1;
	private int STATE_LOAD_EMPTY = 2;
	private int STATE_LOAD_ERROR = 3;
	private int STATE_LOAD_SUCCESSED = 4;
	
	//通过一个变量维护当前状态
	private int current_state = STATE_UNLOAD;
	
	//创建以上4个展示效果的view对象
	private View successedView;
	
	private View emptyView;
	private View errorView;
	private View loadingView;
	private LayoutParams layoutParams;
	
	public LoadingPage(Context context) {
		super(context);
		initLoadingPage();
	}

	private void initLoadingPage() {
		layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT);
		if(loadingView == null){
			//xml--->view,
			loadingView = onCreateLoadingView();
			//将这个正在加载的界面展示效果,添加到帧布局
			addView(loadingView,layoutParams);
		}
		
		//加载失败的view添加过程
		if(errorView == null){
			errorView = onCreateErrorView();
			addView(errorView,layoutParams);
		}
		
		//加载为空的view的添加过程
		if(emptyView == null){
			emptyView = onCreateEmptyView();
			addView(emptyView,layoutParams);
		}
		//根据当前状态决定,那个界面显示,那个隐藏
		showSafePage();
	}

	//将具体的展示界面的逻辑,一定要放置到主线程中去运行
	private void showSafePage(){
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				showPage();
			}
		});
	}
	
	//根据当前状态决定,那个界面显示,那个隐藏
	private void showPage() {
		if(loadingView!=null){
			loadingView.setVisibility((current_state == STATE_LOADING || current_state == STATE_UNLOAD)?
					View.VISIBLE:View.GONE);
		}
		
		if(errorView!=null){
			errorView.setVisibility(current_state == STATE_LOAD_ERROR?View.VISIBLE:View.GONE);
		}
		
		if(emptyView != null){
			emptyView.setVisibility(current_state == STATE_LOAD_EMPTY?View.VISIBLE:View.GONE);
		}
		//如果成功展示界面的view对象为空
		if(successedView == null && current_state == STATE_LOAD_SUCCESSED){
			//LoadingPage中不需要去管onCreateViewSuccessed具体实现,要做的就是讲这个成功展示的界面添加到LoadingPage中,然后展示
			successedView = onCreateViewSuccessed();
			if(successedView!=null){
				addView(successedView,layoutParams);
			}
		}
		
		if(successedView!=null){
			successedView.setVisibility(current_state == STATE_LOAD_SUCCESSED?View.VISIBLE:View.GONE);
		}
	}
	
	//将请求网络操作的方法在此次做返回值(成功,失败,为空)结果的获取
	public void show(){
		//调用请求网络的方法,并且获取其结果,用于判读那个界面(成功,为空,失败)显示,那个界面隐藏
		if(current_state == STATE_LOAD_EMPTY || 
				current_state == STATE_LOAD_ERROR || current_state == STATE_LOAD_SUCCESSED){
			//状态归位过程
			current_state = STATE_UNLOAD;
		}
		//在状态归位后,再去做全新的一次请求
		if(current_state == STATE_UNLOAD){
/*			new Thread(){
				public void run() {
					//网络请求的状态已经封装在onLoad对象中
					final ResultState onLoad = onLoad();
					//根据网络请求返回的状态,去决定LoadingPage中当前状态,根据当前状态去判断界面的显示情况((成功,失败,为空)隐藏,显示)
					UIUtils.runInMainThread(new Runnable() {
						@Override
						public void run() {
							if(onLoad!=null){
								current_state = onLoad.getState();
								showPage();
							}
						}
					});
				};
			}.start();*/
			
			ThreadManager.getThreadProxyPool().execute(new Runnable() {
				@Override
				public void run() {
					//网络请求的状态已经封装在onLoad对象中
					final ResultState onLoad = onLoad();
					//根据网络请求返回的状态,去决定LoadingPage中当前状态,根据当前状态去判断界面的显示情况((成功,失败,为空)隐藏,显示)
					UIUtils.runInMainThread(new Runnable() {
						@Override
						public void run() {
							if(onLoad!=null){
								current_state = onLoad.getState();
								showPage();
							}
						}
					});
				}
			});
		}
	}
	
	//每一个Fragment在请求网络过程中调用的方法,需要放置到子线程中
	public abstract ResultState onLoad() ;
	//在BaseFragment中实现此抽象方法
	public abstract View onCreateViewSuccessed() ;
	
	private View onCreateEmptyView() {
		return UIUtils.inflate(R.layout.layout_empty);
	}

	private View onCreateErrorView() {
		return UIUtils.inflate(R.layout.layout_error);
	}

	private View onCreateLoadingView() {
		return UIUtils.inflate(R.layout.layout_loading);
	}
	
	public enum ResultState{
		STATE_EMPTY(2),
		STATE_ERROR(3),
		STATE_SUCCESSED(4);
		
		private int state;

		private ResultState(int state){
			this.state = state;
		}
		
		public int getState() {
			return state;
		}
	}
}
