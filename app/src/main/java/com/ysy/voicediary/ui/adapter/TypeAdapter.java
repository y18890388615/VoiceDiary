package com.ysy.voicediary.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.bean.TypeBean;
import com.ysy.voicediary.ui.activity.DiaryListActivity;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<TypeBean, BaseViewHolder> {
    private Activity activity;
    public TypeAdapter(int layoutResId, @Nullable List<TypeBean> data, Activity activity) {
        super(layoutResId, data);
        this.activity = activity;
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
    }
}
