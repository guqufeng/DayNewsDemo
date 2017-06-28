package com.zhiyuan3g.daynewsdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhiyuan3g.daynewsdemo.fragment.NewsGuoji;
import com.zhiyuan3g.daynewsdemo.fragment.NewsGuonei;
import com.zhiyuan3g.daynewsdemo.fragment.NewsJunshi;
import com.zhiyuan3g.daynewsdemo.fragment.NewsShehui;
import com.zhiyuan3g.daynewsdemo.fragment.NewsTiyu;
import com.zhiyuan3g.daynewsdemo.fragment.NewsTop;
import com.zhiyuan3g.daynewsdemo.fragment.NewsYule;

import java.util.List;

/**
 * Created by kkkkk on 2016/10/13.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private String[] title = {"头条","社会","国内","国际","军事","娱乐","体育"};

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NewsTop();
            case 1:
                return new NewsShehui();
            case 2:
                return new NewsGuonei();
            case 3:
                return new NewsGuoji();
            case 4:
                return new NewsJunshi();
            case 5:
                return new NewsYule();
            case 6:
                return new NewsTiyu();

        }
        return null;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
