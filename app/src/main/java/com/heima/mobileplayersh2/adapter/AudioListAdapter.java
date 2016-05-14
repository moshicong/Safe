package com.heima.mobileplayersh2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.bean.AudioItem;
import com.heima.mobileplayersh2.bean.VideoItem;
import com.heima.mobileplayersh2.utils.StringUtils;

/**
 * Created by Administrator on 2015/11/13.
 */
public class AudioListAdapter extends CursorAdapter {

    public AudioListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public AudioListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public AudioListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 生成新的 view
        View view = View.inflate(context, R.layout.main_audio_list_item, null);
        ViewHoder hoder = new ViewHoder(view);
        view.setTag(hoder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 填充 view
        ViewHoder hoder = (ViewHoder) view.getTag();

        AudioItem audioItem = AudioItem.instanceFromCursor(cursor);

        hoder.tv_title.setText(audioItem.getTitle());
        hoder.tv_arties.setText(audioItem.getArties());
    }

    private class ViewHoder {
        TextView tv_title, tv_arties;

        public ViewHoder(View root) {
            tv_title = (TextView) root.findViewById(R.id.main_audio_item_tv_title);
            tv_arties = (TextView) root.findViewById(R.id.main_audio_item_tv_arties);
        }
    }
}
