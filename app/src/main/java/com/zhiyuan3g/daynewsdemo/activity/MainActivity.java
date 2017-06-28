package com.zhiyuan3g.daynewsdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.zhiyuan3g.daynewsdemo.R;
import com.zhiyuan3g.daynewsdemo.adapter.MyViewPagerAdapter;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ImageView image_mysc;
    private ViewPager viewPager;
    private MyViewPagerAdapter viewPagerAdapter;
    private PagerSlidingTabStrip pagerSlidingTabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        initView();
        initDefaultView();
    }

    private void initDefaultView() {
        viewPager.setCurrentItem(0);
    }

    private void initView() {

        image_mysc = (ImageView) findViewById(R.id.image_mysc);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setTabBackground(Color.RED);
        pagerSlidingTabStrip.setOnPageChangeListener(this);
        image_mysc.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,MySCActivity.class);
        startActivity(intent);
    }
}
