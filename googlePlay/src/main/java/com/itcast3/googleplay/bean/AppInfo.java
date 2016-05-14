package com.itcast3.googleplay.bean;

import java.util.ArrayList;
import java.util.List;

public class AppInfo {
	private long id;
	private String name;
	private String packageName;
	private String iconUrl;
	private float stars;
	private long size;
	private String downloadUrl;
	private String des;
	
	private String downloadNum;
	private String version;
	private String date;
	private String author;
	
	private List<String> screenList = new ArrayList<String>();
	
	private List<String> safeUrlList  = new ArrayList<String>();
	private List<String> safeDesUrlList  = new ArrayList<String>();
	private List<String> safeDesList = new ArrayList<String>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<String> getScreenList() {
		return screenList;
	}
	public void setScreenList(List<String> screenList) {
		this.screenList = screenList;
	}
	public List<String> getSafeUrlList() {
		return safeUrlList;
	}
	public void setSafeUrlList(List<String> safeUrlList) {
		this.safeUrlList = safeUrlList;
	}
	public List<String> getSafeDesUrlList() {
		return safeDesUrlList;
	}
	public void setSafeDesUrlList(List<String> safeDesUrlList) {
		this.safeDesUrlList = safeDesUrlList;
	}
	public List<String> getSafeDesList() {
		return safeDesList;
	}
	public void setSafeDesList(List<String> safeDesList) {
		this.safeDesList = safeDesList;
	}
}
