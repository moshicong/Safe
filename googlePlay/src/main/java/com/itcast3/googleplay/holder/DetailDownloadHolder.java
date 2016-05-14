package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.bean.DownloadInfo;
import com.itcast3.googleplay.manager.DownloadMananger;
import com.itcast3.googleplay.manager.DownloadMananger.DownloadObserver;
import com.itcast3.googleplay.ui.widget.ProgressHorizontal;
import com.itcast3.googleplay.util.UIUtils;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class DetailDownloadHolder extends BaseHolder<AppInfo> implements DownloadObserver,OnClickListener{
	private Button download_btn;
	private FrameLayout download_fl;
	private AppInfo appInfo;
	private DownloadMananger downloadMananger;
	//页面中显示的下载中的状态
	private int state;
	//下载过重进度条的值
	private float progress;
	private ProgressHorizontal progressHorizontal;
	
	@Override
	public void setData(AppInfo mData) {
		if(downloadMananger == null){
			downloadMananger = DownloadMananger.getInstance();
		}
		//打开界面的时候,可能就有一个初始的下载状态,以及一个初始的下载进度条
		//将其之前的下载状态以及下载进度条获取出来,用于展示
		DownloadInfo downloadInfo = downloadMananger.getDownloadInfo(mData);
		if(downloadInfo == null){
			//无状态state_none,也无进度条具体值0
			state = DownloadMananger.STATE_NONE;
			progress = 0;
		}else{
			//将上次下载中维护的状态,以及上次下载中下载的的进度条位置获取出来,用于展示
			state = downloadInfo.getState();
			progress = downloadInfo.getProgress();
		}
		
		super.setData(mData);
	}

	@Override
	public void refreshView() {
		appInfo = getData();
		//将状态更新到UI上去
		refreshUIMainThread(state,progress);
	}

	//将进度条,以及状态更新到界面上去
	private void refreshUIMainThread(int state, float progress) {
		//将状态再一次定义成全局的状态
		this.state = state;
		//将进度条再一次定义成全局所处的进度条位置
		this.progress = progress;
		
		switch (state) {
		case DownloadMananger.STATE_NONE:
			//让按钮显示文字下载
			download_btn.setText(UIUtils.getString(R.string.app_state_download));
			download_btn.setVisibility(View.VISIBLE);
			progressHorizontal.setVisibility(View.GONE);
			break;
		case DownloadMananger.STATE_WAITTING:
			download_btn.setText(UIUtils.getString(R.string.app_state_waiting));
			download_btn.setVisibility(View.VISIBLE);
			progressHorizontal.setVisibility(View.GONE);
			break;
		case DownloadMananger.STATE_DOWNLOAD:
			//正在下载,进度条需要去改变
			//由进度条中progress的值就管理了中间显示百分比的文字,以及进度条的递增过程
			progressHorizontal.setProgress(progress);
			progressHorizontal.setVisibility(View.VISIBLE);
			download_btn.setVisibility(View.GONE);
			progressHorizontal.setCenterText("");
			//进度条中间不需要去显示任何的文字
			break;
		case DownloadMananger.STATE_ERROR:
			download_btn.setText(UIUtils.getString(R.string.app_state_error));
			download_btn.setVisibility(View.VISIBLE);
			progressHorizontal.setVisibility(View.GONE);
			break;
		case DownloadMananger.STATE_PAUSE:
			progressHorizontal.setProgress(progress);
			progressHorizontal.setVisibility(View.VISIBLE);
			download_btn.setVisibility(View.GONE);
			progressHorizontal.setCenterText(UIUtils.getString(R.string.app_state_paused));
			break;
		case DownloadMananger.STATE_DOWNLOADED:
			download_btn.setText(UIUtils.getString(R.string.app_state_downloaded));
			download_btn.setVisibility(View.VISIBLE);
			progressHorizontal.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_download);
		
		download_btn = (Button) view.findViewById(R.id.download_btn);
		download_fl = (FrameLayout) view.findViewById(R.id.download_fl);
		
		//要将帧布局中,进度条通过progress进行填充,download_btn需要去修改状态的改变
		progressHorizontal = new ProgressHorizontal(UIUtils.getContext());
		//设置进度条显示文字
		progressHorizontal.setProgressTextVisible(true);
		//指定显示的文字颜色
		progressHorizontal.setProgressTextColor(Color.WHITE);
		//文字的大小
		progressHorizontal.setProgressTextSize(UIUtils.dip2px(18));
		//进度条的前景(下载到的位置)以及背景(映衬前景的控件)

		//设置前景
		progressHorizontal.setProgressResource(R.drawable.progress_normal);
		//设置背景
		progressHorizontal.setProgressBackgroundResource(R.drawable.progress_bg);
		
		//水平的进度条,需要放置到帧布局中填充满整个帧布局显示
		LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT);
		
		download_fl.addView(progressHorizontal, layoutParams);
		
		download_btn.setOnClickListener(this);
		download_fl.setOnClickListener(this);
		
		return view;
	}

	public void registerObserver() {
		downloadMananger.registerObserver(this);
	}

	//状态方式改变的具体的业务逻辑实现的方法,UI处理
	@Override
	public void onDownloadStateChange(DownloadInfo downloadInfo) {
		//通过传递进来downInfo对象去指定修改那个APK中UI的操作
		refreshUI(downloadInfo);
	}
	
	//进度条的具体的业务逻辑的实现方法,UI处理
	@Override
	public void onDownloadProgressChange(DownloadInfo downloadInfo) {
		//通过传递进来downInfo对象去指定修改那个APK中UI的操作
		refreshUI(downloadInfo);
	}

	private void refreshUI(final DownloadInfo downloadInfo) {
		//在线程中处理的下载对象的id和界面显示对象id一致,则说明线程处理的就是当前的界面apk的下载过程
		if(downloadInfo.getId() == appInfo.getId()){
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					refreshUIMainThread(downloadInfo.getState(), downloadInfo.getProgress());
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download_btn:
		case R.id.download_fl:
			//根据当前状态去做,下载状态的切换,UI的刷新
			//点击按钮前       state_none state_pause state_error ,点击按钮后就去调用 DownloadManager中下载方法
			if(state == DownloadMananger.STATE_NONE || 
				state== DownloadMananger.STATE_PAUSE || state== DownloadMananger.STATE_ERROR){
				downloadMananger.download(appInfo);
			}else if(state== DownloadMananger.STATE_WAITTING || state== DownloadMananger.STATE_DOWNLOAD){
				//点击按钮前       state_waitting state_download,暂停,调用暂停方法
				downloadMananger.pause(appInfo);
			}else if(state == DownloadMananger.STATE_DOWNLOADED){
				//点击按钮前       state_downloaded     安装
				downloadMananger.install(appInfo);
			}
			break;
		}
	}
}
