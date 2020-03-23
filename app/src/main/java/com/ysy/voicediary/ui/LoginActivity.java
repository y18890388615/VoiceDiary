package com.ysy.voicediary.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_authCode)
    TextView tvAuthCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.re_autoCode)
    RelativeLayout reAutoCode;
    @BindView(R.id.ib_rmPwd)
    ImageButton ibRmPwd;
    @BindView(R.id.ll_rmpwd)
    LinearLayout llRmpwd;
    @BindView(R.id.bt_login)
    Button btLogin;

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView(Bundle savedInstanceState) {
        etPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左上右下四张图片
                Drawable drawable = etPwd.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > etPwd.getWidth()
                        - etPwd.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    if (etPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码
                    } else {
                        etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//可见
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {

    }

}
