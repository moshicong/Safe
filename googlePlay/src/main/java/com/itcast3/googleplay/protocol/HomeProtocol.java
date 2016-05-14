package com.itcast3.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.itcast3.googleplay.bean.AppInfo;

public class HomeProtocol extends BaseProtocol<List<AppInfo>> {
	private List<String> picUrlList = new ArrayList<String>();
	private List<AppInfo> appInfoList = new ArrayList<AppInfo>();
	//获取轮播图片集合的方法
	public List<String> getPicUrlList(){
		return picUrlList;
	}
	
	//HomeFragment解析数据的方法
	@Override
	public List<AppInfo> parsonJson(String result) {
		//手动解析过程
		try {
			JSONObject jsonObject = new JSONObject(result);
			if(jsonObject.has("picture")){
				JSONArray jsonArray = jsonObject.getJSONArray("picture");
				picUrlList.clear();
				for(int i=0;i<jsonArray.length();i++){
					String string = jsonArray.getString(i);
					picUrlList.add(string);
				}
			}
			
			if(jsonObject.has("list")){
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				appInfoList.clear();
				for(int i=0;i<jsonArray.length();i++){
					AppInfo appInfo = new AppInfo();
					
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					appInfo.setDes(jsonObject2.getString("des"));
					appInfo.setDownloadUrl(jsonObject2.getString("downloadUrl"));
					appInfo.setIconUrl(jsonObject2.getString("iconUrl"));
					appInfo.setId(jsonObject2.getLong("id"));
					appInfo.setName(jsonObject2.getString("name"));
					appInfo.setPackageName(jsonObject2.getString("packageName"));
					appInfo.setSize(jsonObject2.getLong("size"));
					appInfo.setStars((float)jsonObject2.getDouble("stars"));
					
					appInfoList.add(appInfo);
				}
			}
			
			return appInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//生成homeFragment中请求网络的部分链接地址home.jsp
	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParams() {
		return "";
	}
}
