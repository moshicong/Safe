package com.itcast3.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class HotProtocol extends BaseProtocol<List<String>>{
	private List<String> hotList = new ArrayList<String>();
	@Override
	public List<String> parsonJson(String result) {
		try {
			hotList.clear();
			JSONArray jsonArray = new JSONArray(result);
			for(int i=0;i<jsonArray.length();i++){
				hotList.add(jsonArray.getString(i));
			}
			return hotList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getKey() {
		return "hot";
	}

	@Override
	public String getParams() {
		return "";
	}
}
