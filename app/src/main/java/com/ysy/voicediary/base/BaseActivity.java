package com.ysy.voicediary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        BarUtils.setStatusBarVisibility(this, false);//隐藏状态栏
        // ButterKnife
        mUnbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        initData();
    }
    public abstract int getLayoutID();

    /**
     * 初始UI
     */
    public abstract void initView(Bundle savedInstanceState);

    /**
     * 初始数据
     */
    public abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
