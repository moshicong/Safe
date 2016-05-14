package com.itcast3.googleplay.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Intent;
import android.net.Uri;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.bean.DownloadInfo;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.http.HttpHelper.HttpResult;
import com.itcast3.googleplay.util.IOUtils;
import com.itcast3.googleplay.util.UIUtils;

public class DownloadMananger {
	//6中状态
	public static final int STATE_NONE = 0;
	//等待运行
	public static final int STATE_WAITTING = 1;
	//下载
	public static final int STATE_DOWNLOAD = 2;
	//暂停
	public static final int STATE_PAUSE = 3;
	//出错
	public static final int STATE_ERROR = 4;
	//下载完成
	public static final int STATE_DOWNLOADED = 5;
	
	//因为google市场应用,能去同时下载多个应用,要有多个内容观察者对象,去一一对应观察每一个下载的apk
	//observer1------>有缘网的下载过程(下载状态的切换,下载进度条的切换)
	//observer2------>酷狗音乐的下载过程(下载状态的切换,下载进度条切换)
	private List<DownloadObserver> observerList = new ArrayList<DownloadObserver>();
	
	//key作为下载应用的唯一性标示,value下载对应的javabean,使用线程安全的map集合
	private Map<Long,DownloadInfo> downloadInfoMap = new ConcurrentHashMap<Long, DownloadInfo>();
	
	//key作为下载应用的唯一性标示,vlaue下载应用对应的任务对象,使用线程安全的map结合
	private Map<Long,DownloadTask> downloadTaskMap = new ConcurrentHashMap<Long,DownloadTask>();
	
	//单例模式
	//1,私有化构造方法
	private DownloadMananger(){};
	//2,创建一个对象
	private static DownloadMananger downloadMananger = new DownloadMananger();
	//3,提供一个返回当前下载管理对象的方法
	public static DownloadMananger getInstance(){
		return downloadMananger;
	}
	
	//(回调回顾1,定义一个接口 2,定义未实现的业务逻辑方法3,传递一个实现了接口类的对象进来4,调用实现好的业务逻辑方法)
	
	//根据点击的动作,需要去切换进度条的状态,进度条时刻准备去做变化
	//下载状态,根据点击动作,时刻的改变
	
	//观察者模式(回调)
	
	//1,注册一个观察者
	public synchronized void registerObserver(DownloadObserver downloadObserver){
		if(downloadObserver!=null){
			//如果现在的apk对应的observer对象以及在集合中,说明下载过程已经在进行观察,所以没有必要反复的去注册
			if(!observerList.contains(downloadObserver)){
				observerList.add(downloadObserver);
			}
		}
	}
	
	//3,反注册观察者(下载过程不再需要去监听(观察))
	public synchronized void unRegisterObserver(DownloadObserver downloadObserver){
		if(downloadObserver!=null){
			if(observerList.contains(downloadObserver)){
				observerList.remove(downloadObserver);
			}
		}
	}
	
	//2,注册的观察者对象,就管理进度条的变化以及下载状态的变化
	//下载管理者对象而已,如果下载状态发生改变,UI的变化是未知的,所以将修改状态和进度条的操作
	//放置到实现类中去做
	public interface DownloadObserver{
		public void onDownloadStateChange(DownloadInfo downloadInfo);
		public void onDownloadProgressChange(DownloadInfo downloadInfo);
	}

	
	//通过以下方法传递的参数(下载javabean) ,精确定位,通知的是哪一个apk应用进度条以及状态的发生改变
	
	//有缘网---->downloadInfo(下载到那个位置,总大小,下载状态.........)
	//酷狗音乐---->downloadInfo(下载到那个位置,总大小,下载状态.........)
	
	//4,定义一个触发通知进度条改变的方法
	public synchronized void notifyDownloadProgressChange(DownloadInfo downloadInfo){
		for(DownloadObserver downloadObserver:observerList){
			downloadObserver.onDownloadProgressChange(downloadInfo);
		}
	}
	//5,定义一个触发下载状态改变的方法
	public synchronized void notifyDownloadStateChange(DownloadInfo downloadInfo){
		for(DownloadObserver downloadObserver:observerList){
			downloadObserver.onDownloadStateChange(downloadInfo);
		}
	}
	
	//判断应用是否之前下载过的方法
	public DownloadInfo getDownloadInfo(AppInfo appInfo){
		if(appInfo!=null){
			return downloadInfoMap.get(appInfo.getId());
		}
		return null;
	}
	
	//下载方法(1,从头下载2,断点续传下载)
	//appInfo----downloadInfo
	public synchronized void download(AppInfo appInfo){
		//AppInfo用于区分下载的是哪一个应用
		
		//现在下载的应用之前有没有下载过??????每次下载应用的时候,都将此应用维护到集合中去,
		//下一次再去下载应用的时候,就去判断此集合是否包含要去下载的apk,如果有,需要断点续传,没有则从头下载
		DownloadInfo downloadInfo = null;
		if(appInfo!=null){
			downloadInfo = downloadInfoMap.get(appInfo.getId());
			if(downloadInfo == null){
				//之前没有下载过当前的应用
				downloadInfo = DownloadInfo.clone(appInfo);
			}
			//准备去做下载操作
			downloadInfo.setState(STATE_WAITTING);
			//通知UI做转变
			notifyDownloadStateChange(downloadInfo);
			
			//应用只要去下载,就需要将应用对应的javabean放置到downloadInfoMap中去管理
			downloadInfoMap.put(appInfo.getId(), downloadInfo);
			
			//正在的开启线程,做下载操作,告知具体下载的apk应用
			DownloadTask downloadTask = new DownloadTask(downloadInfo);
			ThreadManager.getThreadProxyPool().execute(downloadTask);
			
			//获取一个管理每一个应用下载过程的集合Map<key,>
			downloadTaskMap.put(appInfo.getId(), downloadTask);
		}
	}
	
	class DownloadTask implements Runnable{
		private DownloadInfo downloadInfo;
		private HttpResult httpResult = null;
		private InputStream inputStream = null;
		private FileOutputStream fileOutputStream = null;
		
		public DownloadTask(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		@Override
		public void run() {
			//具体的下载方法(downloadInfo对象状态  STATE_WAITTING---->STATE_DOWNLOAD)
			downloadInfo.setState(STATE_DOWNLOAD);
			notifyDownloadStateChange(downloadInfo);
			
			//准备请求网络,将数据通过流的方式写入到本地的文件中去
			String path = downloadInfo.getPath();
			File file = new File(path);
			
			//两部分(1,从头下载2,断点续传)
			if(!file.exists() || file.length()!=downloadInfo.getCurrentPosition() 
					|| downloadInfo.getCurrentPosition()==0){
				//从头开始下载,将原有无效的文件删除,下载位置从0计数
				file.delete();
				downloadInfo.setCurrentPosition(0);
				//请求网络获取数据过程,从头开始下载的过程
				httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadInfo.getDownloadUrl());
			}else{
				//断点续传条件(多线程断点续传下载)
				
				//让服务端返回数据的时候,从downloadInfo.getCurrentPosition()以后做返回
				httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadInfo.getDownloadUrl()
						+"&range="+downloadInfo.getCurrentPosition());
			}
			;
			
			if(httpResult!=null && (inputStream = httpResult.getInputStream())!=null){
				//inputStream写入本地操作
				try {
					//创建一个可以在文件后面追加内容的输出流
					fileOutputStream = new FileOutputStream(file,true);
					
					int temp = -1;
					byte[] buffer = new byte[1024];
					//下载过程要去暂停
					//中止线程的执行(逻辑处理去停止),如果状态一直是下载状态,则继续下载过程
					//如果状态被手动修改为暂停状态,跳出while循环,线程执行结束
					while((temp = inputStream.read(buffer))!=-1 && downloadInfo.getState() == STATE_DOWNLOAD){
						fileOutputStream.write(buffer, 0, temp);
						
						//通知进度条进行更新,维护当前下载到的位置,用作更新
						downloadInfo.setCurrentPosition(downloadInfo.getCurrentPosition()+temp);
						//告知UI去更新进度条
						notifyDownloadProgressChange(downloadInfo);
						
						fileOutputStream.flush();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					
					//下载失败
					downloadInfo.setState(STATE_ERROR);
					//通知用户,修改UI
					notifyDownloadStateChange(downloadInfo);
					//错误文件需要去删除
					file.delete();
					//让用户从头开始下载
					downloadInfo.setCurrentPosition(0);
				}finally{
					//关流
					IOUtils.close(fileOutputStream);
					IOUtils.close(inputStream);
					//关请求链接
					if(httpResult!=null){
						httpResult.close();
					}
				}
				
				//下载完毕
				if(downloadInfo.getCurrentPosition() == downloadInfo.getSize()){
					//下载完毕,就提示用户去做安装
					
					//修改下载状态,然后通知UI做转变(100%---->安装)
					downloadInfo.setState(STATE_DOWNLOADED);
					notifyDownloadStateChange(downloadInfo);
				}else if(downloadInfo.getState() == STATE_PAUSE){
					//下载暂停
					notifyDownloadStateChange(downloadInfo);
				}else{
					//下载失败
					downloadInfo.setState(STATE_ERROR);
					//通知用户,修改UI
					notifyDownloadStateChange(downloadInfo);
					//错误文件需要去删除
					file.delete();
					//让用户从头开始下载
					downloadInfo.setCurrentPosition(0);
				}
			}else{
				//下载失败
				downloadInfo.setState(STATE_ERROR);
				//通知用户,修改UI
				notifyDownloadStateChange(downloadInfo);
				//错误文件需要去删除
				file.delete();
				//让用户从头开始下载
				downloadInfo.setCurrentPosition(0);
			}
			
			//下载任务都是维护在downloadTaskMap,因为下载过程结束,所以需要去移除此任务
			downloadTaskMap.remove(downloadInfo.getId());
		}
	}
	
	//暂停方法(下载或者等待过程中可以去暂停)
	public synchronized void pause(AppInfo appInfo){
		//暂停一个apk下载过程,apk运行状态()
		if(appInfo!=null){
			DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
			if(downloadInfo!=null){
				int state = downloadInfo.getState();
				
				if(state == STATE_WAITTING || state == STATE_DOWNLOAD){
					//如果满足以上条件才可以去暂停(维护任务的集合就没有必要去维护现有apk的下载任务)
					stopDownload(appInfo);
					
					//将状态修改成暂停
					downloadInfo.setState(STATE_PAUSE);
					//下载变成暂停,会伴随UI的改变(30%----->暂停)
					notifyDownloadStateChange(downloadInfo);
				}
			}
		}
	}

	private void stopDownload(AppInfo appInfo) {
		DownloadTask downloadTask = downloadTaskMap.get(appInfo.getId());
		//暂停触发,下载过程就不继续,线程池中也不需要再去维护下载任务,中止线程维护任务的操作
		if(downloadTask!=null){
			ThreadManager.getThreadProxyPool().cancel(downloadTask);
		}
	}
	
	//安装方法(下载完成后(当前下载的currentPosition = size),安装)
	public synchronized void install(AppInfo appInfo){
		//根据apk的路径,去开启安装的activity
		//转换成AppInfo--->downloadInfo,下载apk的存储路径，开启activity做安装操作
		stopDownload(appInfo);
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()),"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(intent);
		}
	}
}
