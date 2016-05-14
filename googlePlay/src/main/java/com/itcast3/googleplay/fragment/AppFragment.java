package com.itcast3.googleplay.fragment;

import java.util.List;

import com.itcast3.googleplay.adapter.MyBaseAdapter;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.holder.BaseHolder;
import com.itcast3.googleplay.holder.HomeHolder;
import com.itcast3.googleplay.protocol.AppProtocol;
import com.itcast3.googleplay.ui.widget.MyListView;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;

public class AppFragment extends BaseFragment {
	private List<AppInfo> data;

	//构建成功界面的view对象
	@Override
	public View onCreateViewSuccessed() {
		MyListView myListView = new MyListView(UIUtils.getContext());
		myListView.setAdapter(new MyAdapter(data));
		return myListView;
	}

	//请求应用界面数据,并且监测其状态方法
	@Override
	public ResultState onLoad() {
		AppProtocol appProtocol = new AppProtocol();
		data = appProtocol.getData(0);
		return check(data);
	}
	
	class MyAdapter extends MyBaseAdapter<AppInfo>{

		public MyAdapter(List<AppInfo> list) {
			super(list);
		}

		@Override
		public BaseHolder getHolder() {
			return new HomeHolder();
		}

		@Override
		public List<AppInfo> onLoadMore() {
			AppProtocol appProtocol = new AppProtocol();
			List<AppInfo> moreData = appProtocol.getData(getListSize());
			return moreData;
		}
	}
}
