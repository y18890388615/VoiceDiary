package com.ysy.voicediary.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseFragment;
import com.ysy.voicediary.ui.activity.LoginActivity;
import com.ysy.voicediary.utils.DialogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingFragment extends BaseFragment {
    @BindView(R.id.nikeName)
    RelativeLayout nikeName;
    @BindView(R.id.settingOne)
    RelativeLayout settingOne;
    @BindView(R.id.ll_pwd)
    RelativeLayout settingTwo;
    @BindView(R.id.ll_logout)
    RelativeLayout settingThree;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.nikeName, R.id.settingOne, R.id.ll_pwd, R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nikeName:
                break;
            case R.id.settingOne:
                break;
            case R.id.ll_pwd://修改密码
                DialogUtil.showUpdate(getContext());
                break;
            case R.id.ll_logout://退出登录
                ActivityUtils.finishAllActivities();
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }
}
