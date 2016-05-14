package com.heima.mobileplayersh2.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.ui.UIInterface;
import com.heima.mobileplayersh2.utils.LogUtils;

/**
 * Created by Administrator on 2016/5/11.
 */
public abstract class BaseActivity1 extends FragmentActivity implements View.OnClickListener,UIInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initListener();
        initData();
        regCommonBtn();
    }

    private void regCommonBtn() {
        View view = findViewById(R.id.back);
        if(view!=null){
            view.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            default:
                processClick(v);
        }

    }
   protected void toast(String msg){
       Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
   }
    protected void toast(int msgId){
        Toast.makeText(this,msgId,Toast.LENGTH_LONG).show();
    }
    protected void logE(String msg){
        LogUtils.e(getClass(),msg);
    }
}
