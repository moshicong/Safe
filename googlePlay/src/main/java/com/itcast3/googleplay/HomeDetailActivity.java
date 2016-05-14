package com.itcast3.googleplay;

import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.holder.DetailAppDesHolder;
import com.itcast3.googleplay.holder.DetailAppInfoHolder;
import com.itcast3.googleplay.holder.DetailAppPicHolder;
import com.itcast3.googleplay.holder.DetailAppSafeHolder;
import com.itcast3.googleplay.holder.DetailDownloadHolder;
import com.itcast3.googleplay.protocol.HomeDetailProtocol;
import com.itcast3.googleplay.ui.widget.LoadingPage;
import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class HomeDetailActivity extends BaseActivity {
	private LoadingPage loadingPage;
	private String packageName;
	private AppInfo data;
	private ActionBar supportActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		packageName = getIntent().getStringExtra("packageName");
		
		initActionBar();
		//包名要作为传递给服务端返回,那个apk详情数据的字段
		loadingPage = new LoadingPage(UIUtils.getContext()) {
			@Override
			public ResultState onLoad() {
				return HomeDetailActivity.this.onLoad();
			}
			
			@Override
			public View onCreateViewSuccessed() {
				return HomeDetailActivity.this.onCreateViewSuccessed();
			}
		};
		setContentView(loadingPage);
		
		//因为之前的fragment中,选中界面后,有触发请求网络的方法,详情界面需要去手动触发请求网络的操作
		if(loadingPage!=null){
			loadingPage.show();
		}
		//回调(1,定义一个接口2,定义一个为实现的业务逻辑方法3,传递一个实现了接口的类的对象进来,4,调用已经实现(接口实现类中)好的业务逻辑方法)
	}
	
	private void initActionBar() {
		supportActionBar = getSupportActionBar();
		supportActionBar.setTitle(UIUtils.getString(R.string.app_name));
		
		supportActionBar.setDisplayHomeAsUpEnabled(true);//左上角可以设置图片
		supportActionBar.setHomeButtonEnabled(true);//可用，可以被点击
	}
	//详情界面请求网络操作
	protected ResultState onLoad() {
		HomeDetailProtocol homeDetailProtocol = new HomeDetailProtocol();
		homeDetailProtocol.setPackageName(packageName);
		data = homeDetailProtocol.getData(0);
		if(data!=null){
			return ResultState.STATE_SUCCESSED;
		}
		return ResultState.STATE_ERROR;
	}

	//详情界面返回成功获取数据的UI(View)效果的方法
	protected View onCreateViewSuccessed() {
		//详情界面一共分成竖直方向上的四个模块,每个模块最终通过添加view的方式加到详情界面上来
		View view = UIUtils.inflate(R.layout.layout_detail);
		
		//最顶端应用信息的帧布局
		FrameLayout detail_appinfo_fl = (FrameLayout) view.findViewById(R.id.detail_appinfo_fl);
		DetailAppInfoHolder detailAppInfoHolder = new DetailAppInfoHolder();
		detailAppInfoHolder.setData(data);
		detail_appinfo_fl.addView(detailAppInfoHolder.getRootView());
		
		FrameLayout detail_appsafe_fl = (FrameLayout) view.findViewById(R.id.detail_appsafe_fl);
		DetailAppSafeHolder detailAppSafeHolder = new DetailAppSafeHolder();
		detailAppSafeHolder.setData(data);
		detail_appsafe_fl.addView(detailAppSafeHolder.getRootView());
		
		HorizontalScrollView detail_apppic_hsv = (HorizontalScrollView) view.findViewById(R.id.detail_apppic_hsv);
		DetailAppPicHolder detailAppPicHolder = new DetailAppPicHolder();
		detailAppPicHolder.setData(data);
		detail_apppic_hsv.addView(detailAppPicHolder.getRootView());
		
		FrameLayout detail_appdes_fl = (FrameLayout) view.findViewById(R.id.detail_appdes_fl);
		DetailAppDesHolder detailAppDesHolder = new DetailAppDesHolder();
		detailAppDesHolder.setData(data);
		detail_appdes_fl.addView(detailAppDesHolder.getRootView());
		
		//获取详情界面底部模块对应的帧布局对象
		FrameLayout download_fl = (FrameLayout) view.findViewById(R.id.download_fl);
		DetailDownloadHolder detailDownloadHolder = new DetailDownloadHolder();
		detailDownloadHolder.setData(data);
		download_fl.addView(detailDownloadHolder.getRootView());
		//注册一个观察者(用于观察进度条,以及状态的转变)
		detailDownloadHolder.registerObserver();
		
		return view;
	}
	
	//顶部actionbar上按钮的点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//系统提供回退按钮的id,顶部actionbar对应的布局中点中的按钮,等于回退的按钮,结束activity
		if(item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
