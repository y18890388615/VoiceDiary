package com.ysy.voicediary.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseActivity;
import com.ysy.voicediary.ui.fragment.ClassificationFragment;
import com.ysy.voicediary.ui.fragment.SettingFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 首页
 */
public class IndexActivity extends BaseActivity {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    private String[] mTitlesArrays = {"日记", "设置"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public int getLayoutID() {
        return R.layout.activity_index;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //日记
        ClassificationFragment classificationFragment = new ClassificationFragment();
        //设置
        SettingFragment settingFragment = new SettingFragment();
        mFragments.add(classificationFragment);
        mFragments.add(settingFragment);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
        tab.setViewPager(vp, mTitlesArrays);//tab和ViewPager进行关联
    }

    @Override
    public void initData() {

    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitlesArrays[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
