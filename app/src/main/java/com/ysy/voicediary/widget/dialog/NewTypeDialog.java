package com.ysy.voicediary.widget.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.ysy.voicediary.R;
import com.ysy.voicediary.bean.TypeBean;
import com.ysy.voicediary.utils.DataBaseUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTypeDialog extends CenterPopupView {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.bt_complete)
    Button btComplete;
    private OnClickListener onClickListener;

    public NewTypeDialog(@NonNull Context context, OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_new_type;
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

    @OnClick(R.id.bt_complete)
    public void onViewClicked() {
        if(etName.getText().length() > 0){
            TypeBean typeBean = new TypeBean();
            typeBean.setTypeName(etName.getText().toString());
            DataBaseUtil.getInstance().getDaoSession().getTypeBeanDao().insert(typeBean);
            ToastUtils.showShort("新建成功");
            onClickListener.click();
            dismiss();
        }else{
            ToastUtils.showShort("请输入名称");
        }
    }

    public interface OnClickListener{
        void click();
    }
}
