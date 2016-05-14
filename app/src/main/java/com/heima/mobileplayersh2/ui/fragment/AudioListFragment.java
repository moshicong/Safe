package com.heima.mobileplayersh2.ui.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.adapter.AudioListAdapter;
import com.heima.mobileplayersh2.bean.AudioItem;
import com.heima.mobileplayersh2.db.MobileAsyncQueryHandler;
import com.heima.mobileplayersh2.ui.activity.AudioPlayerActivity;
import com.heima.mobileplayersh2.utils.CursorUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/13.
 */
public class AudioListFragment extends BaseFragment {

    private ListView listView;
    private AudioListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.main_audio_list;
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.simple_listview);
    }

    @Override
    public void initListener() {
        mAdapter = new AudioListAdapter(getActivity(),null);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnAudioItemClickListener());
    }

    @Override
    public void initData() {
        // 从MediaProvider查询数据
        ContentResolver resolver = getActivity().getContentResolver();
//        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI,new String[]{Media._ID,Media.DATA,Media.DISPLAY_NAME,Media.ARTIST},null,null,null);
////        CursorUtils.printCursor(cursor);
//
//        mAdapter.swapCursor(cursor);

        AsyncQueryHandler asyncQueryHandler = new MobileAsyncQueryHandler(resolver);
        asyncQueryHandler.startQuery(1,mAdapter,Media.EXTERNAL_CONTENT_URI,new String[]{Media._ID,Media.DATA,Media.DISPLAY_NAME,Media.ARTIST},null,null,null);
    }

    @Override
    public void processClick(View v) {

    }

    private class OnAudioItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 获取被点击的数据
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            ArrayList<AudioItem> audioItems = AudioItem.instanceListFromCursor(cursor);

            // 跳转到播放界面
            Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
            intent.putExtra("audioItems",audioItems);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }
}
