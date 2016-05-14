package com.itcast3.googleplay;

import android.support.v7.app.ActionBarActivity;

public class BaseActivity extends ActionBarActivity {
	//应用中所有的activity界面都需要用到actionbar去管理头(Title),所以让所有activity父类继承至包含了ActionBar的Activity
	//获取FragmentManager管理者对象
	//如果对应的activity继承至ActionBarActivity,就一定要去再activity中配置Theme.AppCompat
}
