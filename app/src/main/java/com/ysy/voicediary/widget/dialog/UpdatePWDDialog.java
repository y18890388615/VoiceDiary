package com.ysy.voicediary.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.bean.AccountBean;
import com.ysy.voicediary.db.AccountBeanDao;
import com.ysy.voicediary.ui.activity.LoginActivity;
import com.ysy.voicediary.utils.DataBaseUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更改密码dialog
 */
public class UpdatePWDDialog extends CenterPopupView {

    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_new_pwd2)
    EditText etNewPwd2;
    @BindView(R.id.bt_complete)
    Button btComplete;
    private Context context;

    public UpdatePWDDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_update_pwd;
    }

    @Override
    protected int getMaxWidth() {
        return getWidth();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.bind(this).unbind();
    }

    @OnClick({R.id.bt_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_complete://修改密码完成
                if(etOldPwd.getText().length() ==0){
                    ToastUtils.showShort("请输入旧密码");
                    return;
                }else if(etNewPwd.getText().length() ==0){
                    ToastUtils.showShort("请输入新密码");
                    return;
                }else if(etNewPwd2.getText().length() ==0){
                    ToastUtils.showShort("请再次输入新密码");
                    return;
                }else if(!etNewPwd2.getText().toString().equals(etNewPwd.getText().toString())){
                    ToastUtils.showShort("再次输入密码不一致，请重新输入!");
                    return;
                }else if(!etOldPwd.getText().toString().equals(SPUtils.getInstance().getString(Constants.PWD))){
                    ToastUtils.showShort("密码输入错误!");
                    return;
                }else if(etNewPwd2.getText().length() < 6){
                    ToastUtils.showShort("密码请输入六位数以上!");
                    return;
                }
                SPUtils.getInstance().put(Constants.PWD, "");
                SPUtils.getInstance().put(Constants.REMEMBERPASSWORD, false);
                List<AccountBean> list = DataBaseUtil.getInstance().getDaoSession().getAccountBeanDao().
                        queryBuilder().where(AccountBeanDao.Properties.Account.
                        eq(SPUtils.getInstance().getString(Constants.ACCOUNT))).list();
                AccountBean accountBean = list.get(0);
                accountBean.setPwd(etNewPwd.getText().toString());
                DataBaseUtil.getInstance().getDaoSession().getAccountBeanDao().update(accountBean);
                ActivityUtils.finishAllActivities();
                context.startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }
}
