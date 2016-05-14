package com.itcast3.googleplay.fragment;

import java.util.List;

import com.itcast3.googleplay.ui.widget.LoadingPage;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;

import android.os.Bundle;
import android.provider.Contacts.Intents.UI;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {
	private LoadingPage loadingPage;

	//在基类中管理每一个子界面的展示逻辑
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/* 将以下的四套逻辑抽取到BaseFragment中去做管理
		 * 1,成功展示数据的界面效果
		   2,获取数据为空
		   3,请求网络失败
		   4,正在加载
			*/
//		TextView textView = new TextView(UIUtils.getContext());
//		textView.setText(this.getClass().getSimpleName());
		//loadingPage添加了多种界面展示效果,并且内部通过请求网络状态判断好了,view(正在加载,加载为空,加载失败,加载成功)的显示隐藏状态
		loadingPage = new LoadingPage(UIUtils.getContext()){
			//创建成功界面的view对象
			@Override
			public View onCreateViewSuccessed() {
				//BaseFragment是知道每一个子类界面的成功的具体的页面展示效果,具体实现必须再一次抽象
				return BaseFragment.this.onCreateViewSuccessed();
			}
			
			//
			@Override
			public ResultState onLoad() {
				return BaseFragment.this.onLoad();
			}
		};
		return loadingPage;
	}

	//在具体子类的Fragment中实现此抽象方法,即每一个具体子类构建界面的业务路基
	public abstract View onCreateViewSuccessed();
	//在具体的子类的Fragment中实现请求网络的抽象方法
	public abstract ResultState onLoad();
	
	//提供一个子类Fragment对象的联网方法
	public void show(){
		if(loadingPage!=null){
			loadingPage.show();
		}
	}
	
	//检测第一次请求网络过后,对应的状态,从而决定界面(为空,失败,成功)展示情况
	public ResultState check(Object data) {
		if(data == null){
			return ResultState.STATE_ERROR;
		}else{
			if(data instanceof List){
				if(((List)data).size() == 0){
					return ResultState.STATE_EMPTY;
				}else if(((List)data).size() > 0){
					return ResultState.STATE_SUCCESSED;
				}
			}
		}
		return ResultState.STATE_ERROR;
	}
}
