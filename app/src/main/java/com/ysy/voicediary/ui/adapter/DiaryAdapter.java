package com.ysy.voicediary.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.bean.DiaryBean;
import com.ysy.voicediary.ui.activity.MainActivity;

import java.util.List;

public class DiaryAdapter extends BaseQuickAdapter<DiaryBean, BaseViewHolder> {
    private Context context;
    public DiaryAdapter(int layoutResId, @Nullable List<DiaryBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DiaryBean item) {
        helper.setText(R.id.tv_content,item.getTitle())
                .setText(R.id.update_time, TimeUtils.millis2String(item.getUpdate_time()));
        helper.setOnClickListener(R.id.content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class).
                        putExtra(Constants.DIARY_TYPE, item.getType()).putExtra(Constants.UPDATE_ID, item.getDiaryId()));
            }
        });
    }
}
