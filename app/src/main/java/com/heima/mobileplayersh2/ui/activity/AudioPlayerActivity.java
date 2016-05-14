package com.heima.mobileplayersh2.ui.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.bean.AudioItem;
import com.heima.mobileplayersh2.lyrics.LyricLoader;
import com.heima.mobileplayersh2.lyrics.LyricsView;
import com.heima.mobileplayersh2.service.AudioPlayerService;
import com.heima.mobileplayersh2.utils.StringUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/11/16.
 */
public class AudioPlayerActivity extends BaseActivity {

    private static final int MSG_UPDATE_POSITION = 0;
    private static final int MSG_ROLL_LYRICS = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_POSITION:
                    startUpdatePosition();
                    break;
                case MSG_ROLL_LYRICS:
                    startRollLyric();
                    break;
            }
        }
    };

    private AudioServiceConnection mServiceConnection;
    private AudioPlayerService.AudioServiceBinder mAudioServiceBinder;
    private ImageView iv_pause;
    private OnAudiotReceiver mOnAudiotReceiver;
    private TextView tv_title;
    private TextView tv_arties;
    private ImageView iv_wave;
    private TextView tv_position;
    private SeekBar sk_position;
    private ImageView iv_pre;
    private ImageView iv_next;
    private ImageView iv_playmode;
    private LyricsView lyricsView;

    @Override
    public int getLayoutId() {
        return R.layout.audioplayer;
    }

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.audioplayer_tv_title);
        tv_arties = (TextView) findViewById(R.id.audioplayer_tv_arties);
        iv_wave = (ImageView) findViewById(R.id.audioplayer_iv_wave);
        lyricsView = (LyricsView) findViewById(R.id.audioplayer_lyricsview);

        tv_position = (TextView) findViewById(R.id.audioplayer_tv_position);
        sk_position = (SeekBar) findViewById(R.id.audioplayer_sk_position);
        iv_pause = (ImageView) findViewById(R.id.audioplayer_iv_pause);
        iv_pre = (ImageView) findViewById(R.id.audioplayer_iv_pre);
        iv_next = (ImageView) findViewById(R.id.audioplayer_iv_next);
        iv_playmode = (ImageView) findViewById(R.id.audioplayer_iv_playmode);
    }

    @Override
    public void initListener() {
        iv_pause.setOnClickListener(this);
        iv_pre.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_playmode.setOnClickListener(this);

        sk_position.setOnSeekBarChangeListener(new OnAudioSeekBarChangeListener());

        // 注册广播
        IntentFilter filter = new IntentFilter("com.heima.mobileplayersh2.audio_prepared");
        mOnAudiotReceiver = new OnAudiotReceiver();
        registerReceiver(mOnAudiotReceiver, filter);
    }

    @Override
    public void initData() {
        // 获取初始化数据

//        ArrayList<AudioItem> audioItems = (ArrayList<AudioItem>) getIntent().getSerializableExtra("audioItems");
//        int position = getIntent().getIntExtra("position", -1);

//        Intent service = new Intent(this, AudioPlayerService.class);
//        service.putExtra("audioItems",audioItems);
//        service.putExtra("position",position);

        Intent service = new Intent(getIntent());
        service.setClass(this, AudioPlayerService.class);

        mServiceConnection = new AudioServiceConnection();
        bindService(service, mServiceConnection, Service.BIND_AUTO_CREATE);
        startService(service);

        // 开启示波器动画
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_wave.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.audioplayer_iv_pause:
                switchPauseStatus();
                break;
            case R.id.audioplayer_iv_pre:
                playPre();
                break;
            case R.id.audioplayer_iv_next:
                playNext();
                break;
            case R.id.audioplayer_iv_playmode:
                switchPlayMode();
                break;
        }
    }

    /** 切换播放模式 */
    private void switchPlayMode() {
        mAudioServiceBinder.switchPlayMode();

        updatePlayModeBtn();
    }

    /** 根据当前使用的播放模式，更新使用的图片 */
    private void updatePlayModeBtn() {
        switch (mAudioServiceBinder.getPlayMode()){
            case AudioPlayerService.PLAYMODE_ALL_REPEAT:
                iv_playmode.setImageResource(R.drawable.audio_playmode_allrepeat_selector);
                break;
            case AudioPlayerService.PLAYMODE_RANDOM:
                iv_playmode.setImageResource(R.drawable.audio_playmode_random_selector);
                break;
            case AudioPlayerService.PLAYMODE_SINGLE_REPEAT:
                iv_playmode.setImageResource(R.drawable.audio_playmode_singlerepeat_selector);
                break;
        }
    }

    /** 播放上一首歌 */
    private void playPre() {
        mAudioServiceBinder.playPre();
    }

    /** 播放下一首歌 */
    private void playNext() {
        mAudioServiceBinder.playNext();
    }

    /**
     * 切换播放状态
     */
    private void switchPauseStatus() {
        if (mAudioServiceBinder.isPlaying()) {
            // 正在播放
            mAudioServiceBinder.pause();
        } else {
            // 暂停状态
            mAudioServiceBinder.start();
        }

        updatePauseBtn();
    }

    /**
     * 根据当前的播放状态切换暂停按钮使用的图片
     */
    private void updatePauseBtn() {
        if (mAudioServiceBinder.isPlaying()) {
            // 正在播放
            iv_pause.setImageResource(R.drawable.audio_pause_selector);
        } else {
            // 暂停状态
            iv_pause.setImageResource(R.drawable.audio_play_selector);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        logE("AudioPlayerActivity.onDestroy: ");
        unbindService(mServiceConnection);
        unregisterReceiver(mOnAudiotReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }

    private class AudioServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAudioServiceBinder = (AudioPlayerService.AudioServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private class OnAudiotReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 音乐准备完成，开始播放
            // 更新暂停按钮的图片
            updatePauseBtn();

            // 获取正在播放的歌曲
            AudioItem audioItem = (AudioItem) intent.getSerializableExtra("audioItem");

            // 初始化标题和歌手名
            tv_title.setText(audioItem.getTitle());
            tv_arties.setText(audioItem.getArties());

            // 开启时间更新
            int duration = mAudioServiceBinder.getDuration();
            sk_position.setMax(duration);
            startUpdatePosition();

            // 更新播放模式使用的图片
            updatePlayModeBtn();

            // 开启歌词更新
//            File lyricFile = new File(Environment.getExternalStorageDirectory(),"Download/test/audio/"+audioItem.getTitle()+".lrc");
            File lyricFile = LyricLoader.loadLyricFile(audioItem.getTitle());
            lyricsView.setLyricFile(lyricFile);
            startRollLyric();
        }
    }

    /** 更新歌词高亮行，并延迟再更新 */
    private void startRollLyric() {
        lyricsView.roll(mAudioServiceBinder.getCurrentPosition(),mAudioServiceBinder.getDuration());

        mHandler.sendEmptyMessage(MSG_ROLL_LYRICS);
    }

    /** 获取当前已播放时间，更新播放进度,并延迟一段时间后再次更新 */
    private void startUpdatePosition() {
        
        logE("AudioPlayerActivity.startUpdatePosition: ");
        // 获取时间参数
        int position = mAudioServiceBinder.getCurrentPosition();
        updatePosition(position);

        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 500);

    }

    /** 根据当前播放时间，修改播放进度 */
    private void updatePosition(int position) {
        int duration = mAudioServiceBinder.getDuration();

        // 生成时间字符串
        String durationStr = StringUtils.formatDuration(duration);
        String positionStr = StringUtils.formatDuration(position);

        tv_position.setText(positionStr +" / "+ durationStr);
        sk_position.setProgress(position);
    }

    private class OnAudioSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            // 不是用户发起的变更，则不处理
            if (!fromUser){
                return;
            }

            // 跳转播放进度
            mAudioServiceBinder.seekTo(progress);
            updatePosition(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
