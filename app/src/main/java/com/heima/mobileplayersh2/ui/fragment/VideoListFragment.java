package com.heima.mobileplayersh2.ui.fragment;

import android.app.IntentService;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.adapter.VideoListAdapter;
import com.heima.mobileplayersh2.bean.VideoItem;
import com.heima.mobileplayersh2.db.MobileAsyncQueryHandler;
import com.heima.mobileplayersh2.ui.activity.VideoPlayerActivity;
//import com.heima.mobileplayersh2.ui.activity.VitamioPlayerActivity;
import com.heima.mobileplayersh2.utils.CursorUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/13.
 */
public class VideoListFragment extends BaseFragment {

    private ListView listView;
    private VideoListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.main_video_list;
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.simple_listview);
    }

    @Override
    public void initListener() {
        mAdapter = new VideoListAdapter(getActivity(),null);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnVideoItemClickListener());
    }

    @Override
    public void initData() {
        // 从MediaProvider里查询视频信息
        ContentResolver resolver = getActivity().getContentResolver();
//        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[]{Media._ID, Media.DATA, Media.TITLE, Media.SIZE, Media.DURATION}, null, null, null);

//        CursorUtils.printCursor(cursor);
//        mAdapter.swapCursor(cursor);

        // 使用子线程执行查询操作
        AsyncQueryHandler asyncQueryHandler = new MobileAsyncQueryHandler(resolver);
        asyncQueryHandler.startQuery(0, mAdapter, Media.EXTERNAL_CONTENT_URI, new String[]{Media._ID, Media.DATA, Media.TITLE, Media.SIZE, Media.DURATION}, null, null, null);
    }

    @Override
    public void processClick(View v) {

    }

    private class OnVideoItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 获取被点击数据
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//            VideoItem videoItem = VideoItem.instanceFromCursor(cursor);
            ArrayList<VideoItem> videoItems = VideoItem.instanceListFromCursor(cursor);

            // 跳转到播放界面
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
//            Intent intent = new Intent(getActivity(), VitamioPlayerActivity.class);
//            intent.putExtra("videoItem",videoItem);
            intent.putExtra("videoItems",videoItems);
            intent.putExtra("position",position);
            startActivity(intent);

        }
    }
}
