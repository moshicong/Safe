package com.itcast3.googleplay.factory;

import java.util.HashMap;

import com.itcast3.googleplay.fragment.AppFragment;
import com.itcast3.googleplay.fragment.BaseFragment;
import com.itcast3.googleplay.fragment.CategoryFragment;
import com.itcast3.googleplay.fragment.GameFragment;
import com.itcast3.googleplay.fragment.HomeFragment;
import com.itcast3.googleplay.fragment.HotFragment;
import com.itcast3.googleplay.fragment.RecommendFragment;
import com.itcast3.googleplay.fragment.SubjectFragment;

public class FragmentFactory {
    //Map<key,value>
	//集合区存储之前已经创建过的fragment对象
	private static HashMap<Integer, BaseFragment> hashMap = new HashMap<Integer, BaseFragment>();
	public static BaseFragment createFragment(int arg0) {
		//(内存中如果已经有当前根据索引生成的fragment,复用之前的fragment对象,内存中没有索引指向的fragment对象,创建过程)
		BaseFragment fragment = hashMap.get(arg0);
		if(fragment!=null){
			return fragment;
		}else{
			switch (arg0) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new AppFragment();
				break;
			case 2:
				fragment = new GameFragment();
				break;
			case 3:
				fragment = new SubjectFragment();
				break;
			case 4:
				fragment = new RecommendFragment();
				break;
			case 5:
				fragment = new CategoryFragment();
				break;
			case 6:
				fragment = new HotFragment();
				break;
			}
			//集合将创建过的fragment,管理起来
			hashMap.put(arg0, fragment);
			return fragment;
		}
	}
}
