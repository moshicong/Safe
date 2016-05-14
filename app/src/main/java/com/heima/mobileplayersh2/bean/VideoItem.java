package com.heima.mobileplayersh2.bean;

import android.database.Cursor;
import android.provider.MediaStore.Video.Media;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/13.
 */
public class VideoItem implements Serializable {

    private String title;
    private int duration;
    private int size;
    private String path;

    /**
     * 从cursor生成一个对象
     */
    public static VideoItem instanceFromCursor(Cursor cursor) {
        VideoItem videoItem = new VideoItem();
        if (cursor == null || cursor.getCount() == 0) {
            return videoItem;
        }

        // 解析cursor的内容
        videoItem.title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
        videoItem.duration = cursor.getInt(cursor.getColumnIndex(Media.DURATION));
        videoItem.size = cursor.getInt(cursor.getColumnIndex(Media.SIZE));
        videoItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        return videoItem;
    }

    /** 从cursor里解析出完整的播放列表 */
    public static ArrayList<VideoItem> instanceListFromCursor(Cursor cursor) {
        ArrayList<VideoItem> videoItems = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0) {
            return videoItems;
        }

        cursor.moveToPosition(-1);
        while (cursor.moveToNext()){
            VideoItem videoItem = instanceFromCursor(cursor);
            videoItems.add(videoItem);
        }

        return videoItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }
}
