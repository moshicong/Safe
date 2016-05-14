package com.heima.notificationdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String msg = getIntent().getStringExtra("msg");
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view){
        cancelNotification();
    }

    public void show(View view){
        showNotification();
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
        builder.setTicker("正在播放： beijingbeijing");
        builder.setContent(getRemoteView());
        builder.setOngoing(true);
        return builder.getNotification();
    }

    /** 获取自定义布局要使用的 RemoteViews */
    private RemoteViews getRemoteView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.audio_notfy);

        // 填充文字
        remoteViews.setTextViewText(R.id.audio_notify_tv_title,"北北北京");
        remoteViews.setTextViewText(R.id.audio_notify_tv_arties, "王峰峰");

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
        builder.setTicker("正在播放： beijingbeijing");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("北京北京");
        builder.setContentText("汪峰");
        builder.setContentIntent(getContentIntent());
        builder.setOngoing(true);
        return builder.getNotification();
    }

    @NonNull
    /** 生成一个普通的通知 */
    private Notification getNormalNotification() {
        Notification notification = new Notification(R.drawable.icon, "正在播放： dida", System.currentTimeMillis());
        notification.setLatestEventInfo(this, "滴答", "侃侃", getContentIntent());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
//        notification.contentView = getRemoteView();
        return notification;
    }

    /** 点击通知空白部分的响应 */
    private PendingIntent getContentIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "我是从通知栏启动的啊!");

        return PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /** 点击下一曲的响应 */
    private PendingIntent getNextIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "我是下一曲!");

        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /** 点击上一曲的响应 */
    private PendingIntent getPreIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "我是上一曲!");

        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
