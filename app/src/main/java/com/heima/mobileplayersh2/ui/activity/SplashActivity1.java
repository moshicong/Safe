package com.heima.mobileplayersh2.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.heima.mobileplayersh2.R;

/**
 * Created by Administrator on 2016/5/11.
 */
public class SplashActivity1 extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.spash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void processClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity1.this,MainActivity1.class));
                finish();
            }
        },2000);
    }
}
