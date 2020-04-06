package com.ysy.voicediary.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseActivity;
import com.ysy.voicediary.bean.AccountBean;
import com.ysy.voicediary.db.AccountBeanDao;
import com.ysy.voicediary.utils.DataBaseUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.ib_rmPwd)
    ImageButton ibRmPwd;
    @BindView(R.id.ll_rmpwd)
    LinearLayout llRmpwd;
    @BindView(R.id.bt_login)
    Button btLogin;
    private AccountBeanDao accountBeanDao;
    private int flag;

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView(Bundle savedInstanceState) {
        //权限
        XXPermissions.with(this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
        //设置密码是否可见
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
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.biyan);
                        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        etPwd.setCompoundDrawables(null, null, drawable1, null);
                    } else {
                        etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//可见
                        Drawable drawable2 = getResources().getDrawable(R.mipmap.eye);
                        drawable2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        etPwd.setCompoundDrawables(null, null, drawable2, null);
                    }


                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        accountBeanDao = DataBaseUtil.getInstance().getDaoSession().getAccountBeanDao();
        flag = Constants.LOGIN;
        etAccount.setText(SPUtils.getInstance().getString(Constants.ACCOUNT));
        //记住密码
        if(SPUtils.getInstance().getBoolean(Constants.REMEMBERPASSWORD)){
            etPwd.setText(SPUtils.getInstance().getString(Constants.PWD));
            //图片改为选中状态
            ibRmPwd.setSelected(true);
        }
    }

    @OnClick({R.id.tv_subtitle, R.id.bt_login, R.id.ib_rmPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_subtitle://副标题
                if (flag == Constants.LOGIN) {
                    switchState(Constants.REGISTERED);
                } else {
                    switchState(Constants.LOGIN);
                }
                break;
            case R.id.bt_login://登录或者注册
                //登录
                if (flag == Constants.LOGIN) {
                    if (etAccount.getText().toString().length() < 6) {
                        ToastUtils.showShort("账号长度小于6，请重新输入");
                        return;
                    }
                    if (etPwd.getText().toString().length() < 6) {
                        ToastUtils.showShort("密码长度小于6，请重新输入");
                        return;
                    }
                    List<AccountBean> accountBeans = accountBeanDao.loadAll();
                    if (accountBeans.size() == 0) {
                        ToastUtils.showShort("账号或密码错误");
                        return;
                    }
                    for (int i = 0; i < accountBeans.size(); i++) {
                        String account = accountBeans.get(i).getAccount();
                        String pwd = accountBeans.get(i).getPwd();
                        if (account.equals(etAccount.getText().toString())
                                && pwd.equals(etPwd.getText().toString())) {
                            startActivity(new Intent(this, IndexActivity.class));
                            //保存账号密码
                            SPUtils.getInstance().put(Constants.ACCOUNT, etAccount.getText().toString());
                            SPUtils.getInstance().put(Constants.PWD, etPwd.getText().toString());
                            finish();
                            break;
                        }
                        if(i == accountBeans.size()-1){
                            ToastUtils.showShort("账号或密码错误");
                        }
                    }

                    //注册
                } else {
                    if (etAccount.getText().toString().length() < 6) {
                        ToastUtils.showShort("账号长度小于6，请重新输入");
                        return;
                    }
                    if (etPwd.getText().toString().length() < 6) {
                        ToastUtils.showShort("密码长度小于6，请重新输入");
                        return;
                    }
                    List<AccountBean> accountBeans = accountBeanDao.loadAll();
                    for (int i = 0; i < accountBeans.size(); i++) {
                        String account = accountBeans.get(i).getAccount();
                        //有重复账号
                        if (account.equals(etAccount.getText().toString())) {
                            ToastUtils.showShort("已有重复账号");
                            return;
                        }
                    }
                    AccountBean accountBean = new AccountBean();
                    accountBean.setAccount(etAccount.getText().toString());
                    accountBean.setPwd(etPwd.getText().toString());
                    accountBeanDao.insert(accountBean);
                    ToastUtils.showShort("注册成功");
                    switchState(Constants.LOGIN);
                }
                break;
            case R.id.ib_rmPwd://记住密码
                if (ibRmPwd.isSelected()) {
                    ibRmPwd.setSelected(false);
                    SPUtils.getInstance().put(Constants.REMEMBERPASSWORD, false);
                } else {
                    ibRmPwd.setSelected(true);
                    SPUtils.getInstance().put(Constants.REMEMBERPASSWORD, true);
                }
                break;
        }
    }

    /**
     * 选择进去时状态（登陆，注册，绑定）
     */
    private void switchState(int flag) {
        this.flag = flag;
        switch (flag) {
            case Constants.LOGIN:
                llRmpwd.setVisibility(View.VISIBLE);
                tvTitle.setText("登录");
                tvSubtitle.setText("注册");
                btLogin.setText("登录");
                break;
            case Constants.REGISTERED:
                llRmpwd.setVisibility(View.INVISIBLE);
                tvTitle.setText("注册");
                tvSubtitle.setText("登录");
                btLogin.setText("注册");
                break;
        }
        etAccount.setText("");
        etPwd.setText("");
    }
}
