package com.heima.mobileplayersh2.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.bean.AudioItem;
import com.heima.mobileplayersh2.ui.activity.AudioPlayerActivity;
import com.heima.mobileplayersh2.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/11/16.
 */
public class AudioPlayerService extends Service {

    private static final String TAG = "AudioPlayerService";
    private ArrayList<AudioItem> audioItems;
    private int position;
    private AudioServiceBinder mAudioServiceBinder;
    private SharedPreferences mPreferences;

    public static final int PLAYMODE_ALL_REPEAT = 0;
    public static final int PLAYMODE_RANDOM = 1;
    public static final int PLAYMODE_SINGLE_REPEAT = 2;
    private int mPlayMode = PLAYMODE_ALL_REPEAT;

    private static final String NOTIFY_TYPE  = "notify_type";
    private static final int NOTIFY_TYPE_PRE = 0;
    private static final int NOTIFY_TYPE_NEXT = 1;
    private static final int NOTIFY_TYPE_CONTENT = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        LogUtils.e(TAG, "AudioPlayerService.onBind: ");
        return mAudioServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e(TAG, "AudioPlayerService.onCreate: ");

        mAudioServiceBinder = new AudioServiceBinder();
        mPreferences = getSharedPreferences("audio.conf",MODE_PRIVATE);
        mPlayMode = mPreferences.getInt("play_mode", PLAYMODE_ALL_REPEAT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int notifyType = intent.getIntExtra(NOTIFY_TYPE, -1);
        // 从通知栏启动
        switch (notifyType) {
            case NOTIFY_TYPE_PRE:
                mAudioServiceBinder.playPre();
                break;
            case NOTIFY_TYPE_NEXT:
                mAudioServiceBinder.playNext();
                break;
            case NOTIFY_TYPE_CONTENT:
                notifyUpdateUI();
                break;
            default:
                // 不是从通知栏启动
                // 初始化数据
                int position = intent.getIntExtra("position", -1);
                if (position == this.position) {
                    // 重复播放同一首歌
                    notifyUpdateUI();
                    if (!mAudioServiceBinder.isPlaying()){
                        mAudioServiceBinder.start();
                    }

                }else {
                    // 播放新的歌曲
                    audioItems = (ArrayList<AudioItem>) intent.getSerializableExtra("audioItems");
                    this.position = position;

                    mAudioServiceBinder.play();
                }

                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class AudioServiceBinder extends Binder{

        private class OnAudioPreparedListener implements MediaPlayer.OnPreparedListener {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 歌曲准备完成
                mediaPlayer.start();

                // 显示通知
                showNotification();

                // 通知界面更新
                notifyUpdateUI();
            }
        }

        private class OnAudioCompletionListener implements MediaPlayer.OnCompletionListener {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 歌曲播放结束

                // 自动播放下一首歌
                autoPlayNext();
            }
        }

        private MediaPlayer mediaPlayer;

        /** 播放当前选中的歌曲 */
        private void play(){

            AudioItem audioItem = audioItems.get(position);
            LogUtils.e(TAG, "AudioPlayerService.onStartCommand: item=" + audioItem);

            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            // 播放选中的item
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioItem.getPath());
                mediaPlayer.setOnPreparedListener(new OnAudioPreparedListener());
                mediaPlayer.setOnCompletionListener(new OnAudioCompletionListener());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 暂停播放 */
        public void pause(){
            mediaPlayer.pause();

            cancelNotification();
        }

        /** 开始播放 */
        public void start(){
            mediaPlayer.start();

            showNotification();
        }

        /** 如果正在播放则返回true */
        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }

        /** 返回当前音乐的总时长 */
        public int getDuration(){
            return mediaPlayer.getDuration();
        }

        /** 返回当前音乐已播放时间 */
        public int getCurrentPosition(){
            return mediaPlayer.getCurrentPosition();
        }

        /** 跳转播放进度 */
        public void seekTo(int mSec){
            LogUtils.e(TAG, "AudioServiceBinder.seekTo: ");
            mediaPlayer.seekTo(mSec);
        }

        /** 播放上一首歌 */
        public void playPre(){
            if (position!=0){
                position--;
                play();
            }else {
                Toast.makeText(getApplication(),"已经是第一首歌了！",Toast.LENGTH_SHORT).show();
            }
        }

        /** 播放下一首歌 */
        public void playNext() {
            if (position!=audioItems.size()-1){
                position++;
                play();
            }else {
                Toast.makeText(getApplication(),"已经是最后一首歌了！",Toast.LENGTH_SHORT).show();
            }
        }

        /** 按照 列表循环、随机播放、单曲循环 的顺序，切换播放模式 */
        public void switchPlayMode(){
            switch (mPlayMode){
                case PLAYMODE_ALL_REPEAT:
                    mPlayMode = PLAYMODE_RANDOM;
                    break;
                case PLAYMODE_RANDOM:
                    mPlayMode = PLAYMODE_SINGLE_REPEAT;
                    break;
                case PLAYMODE_SINGLE_REPEAT:
                    mPlayMode = PLAYMODE_ALL_REPEAT;
                    break;
            }

            // 保存播放模式到配置文件
            mPreferences.edit().putInt("play_mode",mPlayMode).commit();

        }

        /** 返回当前使用的播放模式 {@link #PLAYMODE_ALL_REPEAT}, {@link #PLAYMODE_RANDOM}, {@link #PLAYMODE_SINGLE_REPEAT} */
        public int getPlayMode(){
            return mPlayMode;
        }

        /** 根据当前播放模式自动播放下一首歌 */
        private void autoPlayNext() {
            // 根据播放模式修改poition的位置
            switch (mPlayMode){
                case PLAYMODE_ALL_REPEAT:
                    // 如果是最后一首歌则返回第0首歌，否则选择下一首歌
                    if (position == audioItems.size() - 1) {
                        position = 0;
                    } else {
                        position++;
                    }
                    break;
                case PLAYMODE_RANDOM:
                    // 在列表中随机选择一首
                    position = new Random().nextInt(audioItems.size());
                    break;
                case PLAYMODE_SINGLE_REPEAT:
                    // 保持当前的位置
                    break;
            }

            // 使用选中的position播放歌曲
            play();
        }
    }

    /** 取消通知 */
    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    /** 显示通知 */
    private void showNotification() {
        // 生成Notification对象
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//            notification = getNormalNotificationNewApi();
            notification = getCustomNotificationNewApi();
        } else {
            notification = getNormalNotification();
        }

        // 显示通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    /** 使用新API生成一个自定义布局的通知 */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Notification getCustomNotificationNewApi() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("正在播放： " + audioItems.get(position).getTitle());
        builder.setContent(getRemoteView());
        builder.setOngoing(true);
        return builder.getNotification();
    }

    /** 获取自定义布局要使用的 RemoteViews */
    private RemoteViews getRemoteView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.audio_notfy);

        // 填充文字
        remoteViews.setTextViewText(R.id.audio_notify_tv_title,audioItems.get(position).getTitle());
        remoteViews.setTextViewText(R.id.audio_notify_tv_arties, audioItems.get(position).getArties());

        // 设置点击事件
        remoteViews.setOnClickPendingIntent(R.id.audio_notify_iv_pre, getPreIntent());
        remoteViews.setOnClickPendingIntent(R.id.audio_notify_iv_next, getNextIntent());
        remoteViews.setOnClickPendingIntent(R.id.audio_notify_layout, getContentIntent());

        return remoteViews;
    }

    /** 使用新API生成Notification对象 */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Notification getNormalNotificationNewApi() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("正在播放： "+audioItems.get(position).getTitle());
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(audioItems.get(position).getTitle());
        builder.setContentText(audioItems.get(position).getArties());
        builder.setContentIntent(getContentIntent());
        builder.setOngoing(true);
        return builder.getNotification();
    }

    @NonNull
    /** 生成一个普通的通知 */
    private Notification getNormalNotification() {
        Notification notification = new Notification(R.drawable.icon, "正在播放： "+audioItems.get(position).getTitle(), System.currentTimeMillis());
        notification.setLatestEventInfo(this, audioItems.get(position).getTitle(), audioItems.get(position).getArties(), getContentIntent());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
//        notification.contentView = getRemoteView();
        return notification;
    }

    /** 点击通知空白部分的响应 */
    private PendingIntent getContentIntent() {
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra(NOTIFY_TYPE, NOTIFY_TYPE_CONTENT);

        return PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /** 点击下一曲的响应 */
    private PendingIntent getNextIntent() {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.putExtra(NOTIFY_TYPE, NOTIFY_TYPE_NEXT);

        return PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /** 点击上一曲的响应 */
    private PendingIntent getPreIntent() {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.putExtra(NOTIFY_TYPE, NOTIFY_TYPE_PRE);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /** 通知界面更新 */
    private void notifyUpdateUI() {
        // 获取当前正在播放的歌曲
        AudioItem audioItem = audioItems.get(position);

        // 告知界面更新
        Intent intent = new Intent("com.heima.mobileplayersh2.audio_prepared");
        intent.putExtra("audioItem", audioItem);
        sendBroadcast(intent);
    }
}
