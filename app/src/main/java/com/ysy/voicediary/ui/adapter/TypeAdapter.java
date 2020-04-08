package com.ysy.voicediary.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.bean.TypeBean;
import com.ysy.voicediary.ui.activity.DiaryListActivity;
import com.ysy.voicediary.utils.DataBaseUtil;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<TypeBean, BaseViewHolder> {
    private Activity activity;
    private OnclickListener onclickListener;
    public TypeAdapter(int layoutResId, @Nullable List<TypeBean> data, Activity activity, OnclickListener onclickListener) {
        super(layoutResId, data);
        this.activity = activity;
        this.onclickListener = onclickListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TypeBean item) {
        helper.setText(R.id.tv_content, item.getTypeName());
        helper.setOnClickListener(R.id.content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getTypeId() == Constants.STUDY){
                    activity.startActivity(new Intent(activity, DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, Constants.STUDY));
                }else if(item.getTypeId() == Constants.AFFAIRS){
                    activity.startActivity(new Intent(activity, DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, Constants.AFFAIRS));
                }else if(item.getTypeId() == Constants.LIFE){
                    activity.startActivity(new Intent(activity, DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, Constants.LIFE));
                }else{
                    activity.startActivity(new Intent(activity, DiaryListActivity.class).putExtra(Constants.DIARY_TYPE, item.getTypeId()));
                }
            }
        });
        helper.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getTypeId() < 0){
                    ToastUtils.showShort("初始分类不能删除!");
                    return;
                }
                DataBaseUtil.getInstance().getDaoSession().getTypeBeanDao().delete(item);
                ToastUtils.showShort("删除成功");
                onclickListener.delete();
            }
        });
    }
    public interface OnclickListener{
        void delete();
    }
}
