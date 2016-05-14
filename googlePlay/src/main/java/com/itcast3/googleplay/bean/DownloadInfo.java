package com.itcast3.googleplay.bean;

import java.io.File;
import android.os.Environment;
import com.itcast3.googleplay.manager.DownloadMananger;

//javabean就是每一个下载apk相应的记录信息
public class DownloadInfo {
	//apk的唯一性标示
	private long id;
	//apk的大小
	private long size;
	//当前下载到的位置
	private long currentPosition;
	//下载的百分比(进度条)
	private float progress;
	//下载过后apk的名称
	private String name;
	//下载apk的地址
	private String downloadUrl;
	//apk的包名
	private String packageName;
	//当前的下载状态
	private int state;
	//存放apk的路径
	private String path;
	
	private static String GOOGLEMARKET = "googlemarket";
	private static String DOWNLOAD = "download";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(long currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	//获取下载百分比的方法
	public float getProgress() {
		//当前下载到的位置/总大小 = 进度条百分比
		if(getSize() == 0){
			return 0;
		}
		return (currentPosition+0.0f)/getSize();
	}
	public void setProgress(float progress) {
		this.progress = progress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	//定apk所在的路径,AppInfo--->DownLoadInfo
	public static DownloadInfo clone(AppInfo appInfo){
		DownloadInfo downloadInfo = new DownloadInfo();
		
		downloadInfo.setId(appInfo.getId());
		downloadInfo.setPackageName(appInfo.getPackageName());
		downloadInfo.setDownloadUrl(appInfo.getDownloadUrl());
		downloadInfo.setName(appInfo.getName());
		downloadInfo.setSize(appInfo.getSize());
		
		downloadInfo.setCurrentPosition(0);
		downloadInfo.setState(DownloadMananger.STATE_NONE);
		downloadInfo.setProgress(0);
		
		//下载路径维护   有缘网     sdcard/googlemarket/download/有缘网.apk
		//有缘网     sd/googlemarket/download/酷狗音乐.apk
//		downloadInfo.setPath(path);
		
		if(getFilePath()!=null){
			downloadInfo.setPath(getFilePath()+appInfo.getName()+".apk");
		}
		
		return downloadInfo;
	}
	private static String getFilePath() {
		StringBuffer buffer = new StringBuffer();
		String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		buffer.append(sdcardPath);
		buffer.append(File.separator);
		buffer.append(GOOGLEMARKET);
		buffer.append(File.separator);
		buffer.append(DOWNLOAD);
		buffer.append(File.separator);
		
		//创建指定路径文件夹的操作
		if(createFile(buffer.toString())){
			return buffer.toString();
		}else{
			return null;
		}
	}
	private static boolean createFile(String path) {
		File file = new File(path);
		if(!file.exists() || !file.isDirectory()){
			//文件夹不存在,或者不是个文件夹
			return file.mkdirs();
		}
		return true;
	}
}
