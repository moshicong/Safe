package com.itcast3.googleplay.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.http.HttpHelper.HttpResult;
import com.itcast3.googleplay.util.IOUtils;
import com.itcast3.googleplay.util.UIUtils;

public abstract class BaseProtocol<T>{
	/*1,先尝试从缓存中获取数据
	获取到有效的缓存数据
	没有获取到有效的缓存数据,请求网络
	2,请求网络过程,
		请求成功
	3,展示(先解析)*/
	public T getData(int index){
		String data = getDataFromLocal(index);
		String result = null;
		if(data!=null){
			result = data;
		}else{
			//请求网络
			result = getDataFromNet(index);
		}
		//解析,因为首页,应用,游戏......对应的json结构都不一致,所以解析方法没法具体实现
		return parsonJson(result);
	}

	//抽象的解析方法
	public abstract T parsonJson(String result);

	//写入数据到缓存中去
	private void writeToLocal(String result,int index) {
		BufferedWriter bufferedWriter = null;
		//1,持久化存储(写到磁盘里面去(文件,sp,数据库))
		//2,非持久化存储(内存(集合(内存溢出,图片处理(三级缓存(LRU,文件,网络)))))BitmapUtils
		File cacheDir = UIUtils.getContext().getCacheDir();
		//此文件,必须是指定链接地址,指定是当前链接地址的那一页数据
		//指定那个界面的那一页的数据缓存文件
		File file = new File(cacheDir,getKey()+index+getParams());
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
			
			//写入数据的操作,(做缓存数据的有效周期,假设缓存数据的有效周期为半个小时)
			//将这半个小时的有效时间,连同json写入到文件中去,将半个小时的有效时间作为文件的第一行写入
			long validTime = System.currentTimeMillis()+30*60*1000;
			bufferedWriter.write(validTime+"\r\n");
			bufferedWriter.write(result.toCharArray());
			bufferedWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(bufferedWriter);
		}
	}
	
	//从缓存中获取数据的方法
	private String getDataFromLocal(int index) {
		BufferedReader bufferedReader = null;
		File cacheDir = UIUtils.getContext().getCacheDir();
		File file = new File(cacheDir, getKey()+index+getParams());
		try {
			bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			//读取第一行数据,获取有效时间戳,判断读取这个文件的时候,文件中的缓存数据是否有效
			long currentTime = System.currentTimeMillis();
			String string = bufferedReader.readLine();
			long validTime = Long.valueOf(string);
			if(currentTime<validTime){
				//数据有效,可以往后读取
				String temp = null;
				StringBuffer stringBuffer = new StringBuffer();
				while((temp = bufferedReader.readLine())!=null){
					stringBuffer.append(temp);
				}
				
				return stringBuffer.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(bufferedReader);
		}
		return null;
	}
	
	//从网络获取数据
	private String getDataFromNet(int index) {
		//维护一个index的字段用于确定返回那一页的数据(项目有分页功能)
		//http://www.ooxx.com/home.jsp?index=40&name=412321&pas=e43213;
//		HttpHelper.URL
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL+getKey()+"?index="+index+getParams());
		//服务端返回的json
		String result = httpResult.getString();
		//将数据添加到缓存中去的操作
		if(result!=null){
			writeToLocal(result,index);
		}
		return result;
	}

	//定义请求指向界面,链接地址的抽象方法
	public abstract String getKey();
	//定义请求对应界面数据,带上参数的抽象方法
	public abstract String getParams();
}
