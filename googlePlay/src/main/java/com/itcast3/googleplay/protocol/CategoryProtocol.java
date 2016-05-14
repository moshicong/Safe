package com.itcast3.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.itcast3.googleplay.bean.CategoryInfo;

public class CategoryProtocol extends BaseProtocol<List<CategoryInfo>>{
	private List<CategoryInfo> categoryInfoList = new ArrayList<CategoryInfo>();
	@Override
	public List<CategoryInfo> parsonJson(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			categoryInfoList.clear();
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				//因为要在ListView中,先展示title所在的对象,所以在解析的时候,就先解析title字段
				if(jsonObject.has("title")){
					CategoryInfo categoryInfo = new CategoryInfo();
					categoryInfo.setTitle(jsonObject.getString("title"));
					categoryInfo.setTitle(true);
					
					categoryInfoList.add(categoryInfo);
				}
				
				if(jsonObject.has("infos")){
					JSONArray jsonArray2 = jsonObject.getJSONArray("infos");
					for(int j=0;j<jsonArray2.length();j++){
						JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
						CategoryInfo categoryInfo = new CategoryInfo();
						
						categoryInfo.setName1(jsonObject2.getString("name1"));
						categoryInfo.setName2(jsonObject2.getString("name2"));
						categoryInfo.setName3(jsonObject2.getString("name3"));
						
						categoryInfo.setUrl1(jsonObject2.getString("url1"));
						categoryInfo.setUrl2(jsonObject2.getString("url2"));
						categoryInfo.setUrl3(jsonObject2.getString("url3"));
						
						categoryInfoList.add(categoryInfo);
					}
				}
			}
			return categoryInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public String getParams() {
		return "";
	}
}
