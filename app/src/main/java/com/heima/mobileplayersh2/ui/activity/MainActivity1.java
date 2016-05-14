package com.heima.mobileplayersh2.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.widget.TextView;

import com.heima.mobileplayersh2.R;
import com.heima.mobileplayersh2.adapter.MainPagerAdapter;
import com.heima.mobileplayersh2.ui.fragment.AudioListFragment;
import com.heima.mobileplayersh2.ui.fragment.VideoListFragment;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class MainActivity1 extends BaseActivity{
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private MainPagerAdapter mAdapter;
    private TextView tv_video;
    private TextView tv_audio;
    private View indicate_line;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        tv_audio = (TextView) findViewById(R.id.main_tv_audio);
        tv_video = (TextView) findViewById(R.id.main_tv_video);
        indicate_line = findViewById(R.id.main_indicate_line);
    }


    @Override
    public void initListener() {
        tv_audio.setOnClickListener(this);
        tv_video.setOnClickListener(this);
//        fragments = new ArrayList<Fragment>();
        fragments = new ArrayList<Fragment>();
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new OnMainPageChangeListener());


    }



    @Override
    public void initData() {
        fragments.add(new VideoListFragment());
        fragments.add(new AudioListFragment());
        mAdapter.notifyDataSetChanged();
        updateTabs(0);
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        indicate_line.getLayoutParams().width = screenW / fragments.size();
        indicate_line.requestLayout();
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.main_tv_video:
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_tv_audio:
                viewPager.setCurrentItem(1);
                break;


        }

    }
    private class OnMainPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int offsetX = (int) (positionOffset * indicate_line.getWidth());
            int start = position*indicate_line.getWidth();
            int translationX = start + offsetX;
            ViewHelper.setTranslationX(indicate_line,translationX);
        }

        @Override
        public void onPageSelected(int position) {
            updateTabs(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void updateTabs(int position) {
        updateTabs(position,0,tv_video);
        updateTabs(position,1,tv_audio);

    }
    private void updateTabs(int position, int tabPosition, TextView tab){
        int green = getResources().getColor(R.color.green);
        int halfWhite = getResources().getColor(R.color.halfwhite);
        if(position == tabPosition){
            tab.setTextColor(green);
            ViewPropertyAnimator.animate(tab).scaleX(1.2f).scaleY(1.2f);
        }else {
            tab.setTextColor(halfWhite);
            ViewPropertyAnimator.animate(tab).scaleX(1.0f).scaleY(1.0f);

        }
    }

}
