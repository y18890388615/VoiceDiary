package com.ysy.voicediary.ui.adapter;

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
import com.ysy.voicediary.ui.activity.DiaryListActivity;
import com.ysy.voicediary.ui.activity.MainActivity;
import com.ysy.voicediary.utils.DataBaseUtil;

import java.util.List;

public class DiaryAdapter extends BaseQuickAdapter<DiaryBean, BaseViewHolder> {
    private DiaryListActivity activity;

    public DiaryAdapter(int layoutResId, @Nullable List<DiaryBean> data, DiaryListActivity activity) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DiaryBean item) {
        helper.setText(R.id.tv_content, item.getTitle())
                .setText(R.id.update_time, TimeUtils.millis2String(item.getUpdate_time()))
                .setText(R.id.tv_address, item.getAddress());
        helper.setOnClickListener(R.id.content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, MainActivity.class).
                        putExtra(Constants.DIARY_TYPE, item.getType()).putExtra(Constants.UPDATE_ID, item.getDiaryId()));
            }
        });
        if (item.getPriority() == 1) {
            helper.setText(R.id.tv_stick, "取消置顶");
        }else{
            helper.setText(R.id.tv_stick, "置顶");
        }
        helper.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().delete(item);
                activity.onResume();
            }
        });
        helper.setOnClickListener(R.id.tv_stick, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setPriority(item.getPriority() == 0 ? 1 : 0);
                DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().update(item);
                activity.onResume();
            }
        });
        helper.setOnClickListener(R.id.left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, item.getContent());
                //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
                shareIntent = Intent.createChooser(shareIntent, item.getTitle());
                activity.startActivity(shareIntent);
            }
        });
    }
}
