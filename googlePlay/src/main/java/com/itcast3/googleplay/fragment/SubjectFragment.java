package com.itcast3.googleplay.fragment;

import java.util.List;

import com.itcast3.googleplay.adapter.MyBaseAdapter;
import com.itcast3.googleplay.bean.SubjectInfo;
import com.itcast3.googleplay.holder.BaseHolder;
import com.itcast3.googleplay.holder.SubjectHolder;
import com.itcast3.googleplay.protocol.SubjectProtocol;
import com.itcast3.googleplay.ui.widget.MyListView;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;

public class SubjectFragment extends BaseFragment {
	private List<SubjectInfo> data;
	@Override
	public View onCreateViewSuccessed() {
		MyListView myListView = new MyListView(UIUtils.getContext());
		myListView.setAdapter(new MyAdapter(data));
		return myListView;
	}

	@Override
	public ResultState onLoad() {
		SubjectProtocol subjectProtocol = new SubjectProtocol();
		data = subjectProtocol.getData(0);
		return check(data);
	}
	
	class MyAdapter extends MyBaseAdapter<SubjectInfo>{
		public MyAdapter(List<SubjectInfo> list) {
			super(list);
		}

		@Override
		public BaseHolder getHolder() {
			return new SubjectHolder();
		}

		@Override
		public List<SubjectInfo> onLoadMore() {
			SubjectProtocol subjectProtocol = new SubjectProtocol();
			List<SubjectInfo> moreData = subjectProtocol.getData(getListSize());
			return moreData;
		}
	}
}
