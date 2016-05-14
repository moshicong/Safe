package com.itcast3.googleplay.fragment;

import java.util.List;

import com.itcast3.googleplay.adapter.MyBaseAdapter;
import com.itcast3.googleplay.bean.CategoryInfo;
import com.itcast3.googleplay.holder.BaseHolder;
import com.itcast3.googleplay.holder.CategoryHolder;
import com.itcast3.googleplay.holder.TitleHolder;
import com.itcast3.googleplay.protocol.CategoryProtocol;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.ui.widget.MyListView;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CategoryFragment extends BaseFragment {

	private List<CategoryInfo> data;

	//第一次请求网络成功后,要去展示的view对象
	@Override
	public View onCreateViewSuccessed() {
		MyListView myListView = new MyListView(UIUtils.getContext());
		myListView.setAdapter(new MyAdapter(data));
		return myListView;
	}
	
	//第一次请求网络获取数据
	@Override
	public ResultState onLoad() {
		CategoryProtocol categoryProtocol = new CategoryProtocol();
		data = categoryProtocol.getData(0);
		return check(data);
	}

	class MyAdapter extends MyBaseAdapter<CategoryInfo>{
		private int currentPosition;

		public MyAdapter(List<CategoryInfo> list) {
			super(list);
		}

		@Override
		public BaseHolder getHolder() {
			//1,因为分类界面包含(文字头Title,图片+文字),两种类型的条目,所以会有两种类型的holder
			if(list.get(currentPosition).isTitle()){
				return new TitleHolder(list.get(currentPosition));
			}else{
				return new CategoryHolder();
			}
		}

		@Override
		public List<CategoryInfo> onLoadMore() {
			//没有加载更多的逻辑
			return null;
		}
		
		@Override
		public boolean hasMore() {
			return false;
		}
		
		//一共三种条目类型,返回值结果是三
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		//根据索引,判断当前item转换成的view所属条目类型
		@Override
		public int getInnerType(int position) {
			if(list.get(position).isTitle()){
				//title类型的条目索引值
				return super.getInnerType(position)+2;
			}else{
				//图片+文字类型条
				return super.getInnerType(position);
			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			currentPosition= position;
			return super.getView(position, convertView, parent);
		}
	}
}
