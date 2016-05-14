package com.itcast3.googleplay.protocol;

import org.json.JSONArray;
import org.json.JSONObject;
import com.itcast3.googleplay.bean.AppInfo;

public class HomeDetailProtocol extends BaseProtocol<AppInfo>{
	private String packageName;
	@Override
	public AppInfo parsonJson(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			
			AppInfo appInfo = new AppInfo();
			
			appInfo.setDes(jsonObject.getString("des"));
			appInfo.setDownloadUrl(jsonObject.getString("downloadUrl"));
			appInfo.setIconUrl(jsonObject.getString("iconUrl"));
			appInfo.setId(jsonObject.getLong("id"));
			appInfo.setName(jsonObject.getString("name"));
			appInfo.setPackageName(jsonObject.getString("packageName"));
			appInfo.setSize(jsonObject.getLong("size"));
			appInfo.setStars((float)jsonObject.getDouble("stars"));
			
			appInfo.setAuthor(jsonObject.getString("author"));
			appInfo.setDownloadNum(jsonObject.getString("downloadNum"));
			appInfo.setVersion(jsonObject.getString("version"));
			appInfo.setDate(jsonObject.getString("date"));
			
			if(jsonObject.has("screen")){
				JSONArray jsonArray = jsonObject.getJSONArray("screen");
				for(int i=0;i<jsonArray.length();i++){
					appInfo.getScreenList().add(jsonArray.getString(i));
				}
			}
			
			if(jsonObject.has("safe")){
				JSONArray jsonArray = jsonObject.getJSONArray("safe");
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					
					appInfo.getSafeUrlList().add(jsonObject2.getString("safeUrl"));
					appInfo.getSafeDesUrlList().add(jsonObject2.getString("safeDesUrl"));
					appInfo.getSafeDesList().add(jsonObject2.getString("safeDes"));
				}
			}
			
			return appInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//http://127.0.0.1:8090/detail?index=0&packageName=com.ooxx.apk
	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	public String getParams() {
		return "&packageName="+packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
