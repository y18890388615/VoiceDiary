package com.ysy.voicediary.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseFragment;
import com.ysy.voicediary.ui.activity.DiaryListActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日记分类
 */
public class ClassificationFragment extends BaseFragment {
    @BindView(R.id.iv_study)
    ImageView ivStudy;
    @BindView(R.id.iv_affairs)
    ImageView ivAffairs;
    @BindView(R.id.iv_life)
    ImageView ivLife;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_study, R.id.iv_affairs, R.id.iv_life})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_study://学习
                startActivity(new Intent(getContext(), DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, Constants.STUDY));
                break;
            case R.id.iv_affairs://待办事宜
                startActivity(new Intent(getContext(), DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, Constants.AFFAIRS));
                break;
            case R.id.iv_life://生活
                startActivity(new Intent(getContext(), DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, Constants.LIFE));
                break;
        }
    }
}
