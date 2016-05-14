package com.heima.mobileplayersh2.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.lyrics.LyricsView;
import com.heima.mobileplayersh2.service.AudioPlayerService;
import com.heima.mobileplayersh2.utils.StringUtils;

/**
 * Created by Administrator on 2016/5/12.
 */
public class AudioPlayerActivity1 extends BaseActivity1 {
    private static final int MSG_UPDATE_POSITION = 0;
    private static final int MSG_ROLL_LYRICS = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE_POSITION:
                    startUpdatePosition();
                    break;
                case MSG_ROLL_LYRICS:
                    startRollLyric();
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
        new IntentFilter("com.heima.mobileplayersh2.audio_prepared");
        mOnAudiotReceiver = new OnAudiotReceiver();
        registerReceiver(mOnAudiotReceiver,filter);

    }

    @Override
    public void initData() {

    }

    @Override
    public void processClick(View v) {

    }

    private void startRollLyric() {

    }

    private void startUpdatePosition() {
    }
    private class OnAudioSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(!fromUser){
                return;
            }
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

    private void updatePosition(int position) {
        int duration = mAudioServiceBinder.getDuration();
        String durationStr = StringUtils.formatDuration(duration);
        String positionStr = StringUtils.formatDuration(position);
        tv_position.setText(position + "/"+duration);
        sk_position.setProgress(position);
    }

    private class OnAudiotReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updatePauseBtn();
        }
    }

    private void updatePauseBtn() {
        if(mAudioServiceBinder.isPlaying()){
            iv_pause.setImageResource(R.drawable.audio_pause_selector);
        }else{
            iv_pause.setImageResource(R.drawable.audio_play_selector);
        }
    }
}
