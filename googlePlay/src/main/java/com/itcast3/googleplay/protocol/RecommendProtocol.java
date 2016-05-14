package com.itcast3.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class RecommendProtocol extends BaseProtocol<List<String>>{
	public List<String> recommendList = new ArrayList<String>();
	@Override
	public List<String> parsonJson(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			recommendList.clear();
			for(int i=0;i<jsonArray.length();i++){
				recommendList.add(jsonArray.getString(i));
			}
			return recommendList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getKey() {
		return "recommend";
	}

	@Override
	public String getParams() {
		return "";
	}
}
