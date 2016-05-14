package com.itcast3.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
	//创建一个线程池的管理代理对象(反射,代理(Proxy),注解)
	private static ThreadProxyPool threadProxyPool;
	private static Object object = new Object();
	//提供一个获取代理对象的方法
	public static ThreadProxyPool getThreadProxyPool(){
		//同步创建对象过程
		synchronized(object){
			if(threadProxyPool == null){
				// cpu 2*cpu核数+1
	
				//时间片轮转(cpu随机的给某一个线程去使用,如果线程过多,资源都消耗在线程切换的过程中)
				//50
				//5
				threadProxyPool = new ThreadProxyPool(5,5,5L);
			}
			return threadProxyPool;
		}
	}
	
	
	public static class ThreadProxyPool{
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;
		private ThreadPoolExecutor threadPoolExecutor;
		public ThreadProxyPool(int corePoolSize,int maximumPoolSize,long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}
		//开启,执行任务
		public void execute(Runnable runnable){
			if(runnable == null){
				return;
			}
			
			if(threadPoolExecutor == null || threadPoolExecutor.isShutdown()){
				threadPoolExecutor = new ThreadPoolExecutor(
						//3  核心线程数
						corePoolSize, 
						//5 最大线程数
						maximumPoolSize, 
						//没有执行任务的线程存活时间
						keepAliveTime, 
						//存活时间的单位
						TimeUnit.MILLISECONDS, 
						//线程中要准备去执行任务排队的队列
						new LinkedBlockingQueue<Runnable>(), 
						//线程的工厂,创建线程
						Executors.defaultThreadFactory(),
						//队列中如果放置不下任务时候的异常处理
						new AbortPolicy());
			}
			
			threadPoolExecutor.execute(runnable);
		}
		
		//需要中止的是线程池中任务队列里面的任务
		public void cancel(Runnable runnable){
			if(runnable!=null){
				//获取线程池对象,然后从其内部去移除相应的任务
				threadPoolExecutor.getQueue().remove(runnable);
			}
		}
	}
}
