package com.itcast3.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.itcast3.googleplay.bean.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfo>>{
	private List<SubjectInfo> subjectInfoList = new ArrayList<SubjectInfo>();
	@Override
	public List<SubjectInfo> parsonJson(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.setDes(jsonObject.getString("des"));
				subjectInfo.setUrl(jsonObject.getString("url"));

				subjectInfoList.add(subjectInfo);
			}
			return subjectInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParams() {
		return "";
	}

}
