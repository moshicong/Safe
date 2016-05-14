package com.itcast3.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.itcast3.googleplay.bean.AppInfo;

public class AppProtocol extends BaseProtocol<List<AppInfo>>{
	private List<AppInfo> appInfoList = new ArrayList<AppInfo>();

	@Override
	public List<AppInfo> parsonJson(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			appInfoList.clear();
			for(int i=0;i<jsonArray.length();i++){
				AppInfo appInfo = new AppInfo();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				appInfo.setDes(jsonObject.getString("des"));
				appInfo.setDownloadUrl(jsonObject.getString("downloadUrl"));
				appInfo.setIconUrl(jsonObject.getString("iconUrl"));
				appInfo.setId(jsonObject.getLong("id"));
				appInfo.setName(jsonObject.getString("name"));
				appInfo.setPackageName(jsonObject.getString("packageName"));
				appInfo.setSize(jsonObject.getLong("size"));
				appInfo.setStars((float)jsonObject.getDouble("stars"));
				
				appInfoList.add(appInfo);
			}
			return appInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//app.jsp app.do
	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public String getParams() {
		return "";
	}
}
