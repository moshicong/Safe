package com.itcast3.googleplay.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.itcast3.googleplay.holder.BaseHolder;
import com.itcast3.googleplay.holder.MoreHolder;
import com.itcast3.googleplay.manager.ThreadManager;
import com.itcast3.googleplay.util.UIUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class MyBaseAdapter1<T> extends BaseAdapter {
    public List<T> list;
    private BaseHolder holder;

    private int LIST_VIEW_ITEM = 0;
    private int LOAD_MORE_ITEM = 1;

    private MoreHolder moreHolder;

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(getCount()-1 == position){
            return LOAD_MORE_ITEM;
        }else {
            return getInnerType(position);
        }
    }

    private int getInnerType(int position) {
        return LIST_VIEW_ITEM;
    }
    public MyBaseAdapter1(List<T> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            if(getItemViewType(i)==LOAD_MORE_ITEM){
                holder = getMoreHolder();
            }else {
                if(view.getTag()!=null){
                    holder = (BaseHolder) view.getTag();
                }
            }
        }
        return null;
    }

    private BaseHolder getMoreHolder() {
        if(moreHolder == null){
            moreHolder = new MoreHolder(hasMore(),this);
        }
        return moreHolder;
    }

    private boolean hasMore() {
        return true;
    }
    public abstract BaseHolder getHolder();
    public void loadMore(){
        ThreadManager.getThreadProxyPool().execute(new RunnableTask());
    }
    class RunnableTask implements Runnable{
        @Override
        public void run() {
            final List<T> onLoadMore = onLoadMore();
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if(onLoadMore == null){
                        moreHolder.setData(MoreHoder.load_more_error);

                    }else {
                        if(onLoadMore.size()==20){
                            moreHolder.setData(MoreHolder.has_more);
                        }else if(onLoadMore.size()<20){
                            moreHolder.setData(MoreHole.no_more);
                        }
                    }
                    if(onLoadMore!=null && list!=null){

                        list.addAll(onLoadMore);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    protected abstract List<T> onLoadMore();
}
