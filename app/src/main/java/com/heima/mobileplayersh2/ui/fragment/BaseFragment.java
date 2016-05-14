package com.heima.mobileplayersh2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.ui.UIInterface;
import com.heima.mobileplayersh2.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/13.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener , UIInterface {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), getLayoutId(), null);
        initView();
        initListener();
        initData();

        regCommonBtn();
        return view;
    }

    /** 返回viewId引用的view */
    protected View findViewById(int viewId) {
        return view.findViewById(viewId);
    }

    /** 在多个界面间都存在的按钮，点击事件已经由Base处理，那么将点击事件注册也统一处理掉 */
    private void regCommonBtn() {
        View view = findViewById(R.id.back);
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // 把在多个界面间都存在的点击，统一处理掉
        switch (v.getId()){
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            default:
                processClick(v);
                break;
        }
    }

    /** 显示一个内容为msg的吐司 */
    protected void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /** 显示一个内容为msgid引用的string的吐司 */
    protected void toast(int msgId) {
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
    }

    /** 打印一个子类的名称为tag的log */
    protected void logE(String msg) {
        LogUtils.e(getClass(), msg);
    }
}
