package com.ysy.voicediary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseActivity;
import com.ysy.voicediary.bean.DiaryBean;
import com.ysy.voicediary.db.DiaryBeanDao;
import com.ysy.voicediary.ui.adapter.DiaryAdapter;
import com.ysy.voicediary.utils.DataBaseUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DiaryListActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.ll_newDiary)
    LinearLayout llNewDiary;
    private int diary_type;
    private DiaryBeanDao diaryBeanDao;
    private List<DiaryBean> diaryList = new ArrayList<>();
    private DiaryAdapter diaryAdapter;

    @Override
    public int getLayoutID() {
        return R.layout.activity_diary_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    private void initRecycleView() {
        diaryAdapter = new DiaryAdapter(R.layout.item_diary, diaryList, this);
        diaryAdapter.openLoadAnimation();
        recycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycleView.setAdapter(diaryAdapter);
    }

    @Override
    public void initData() {
        //获取进来的是什么日记列表
        diary_type = getIntent().getIntExtra(Constants.DIARY_TYPE, diary_type);
        diaryBeanDao = DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao();
        List<DiaryBean> diaryBeans = diaryBeanDao.loadAll();
        for (int i = 0; i < diaryBeans.size(); i++) {
            if (diary_type == diaryBeans.get(i).getType()) {
                diaryList.add(diaryBeans.get(i));
            }
        }
        initRecycleView();
    }


    @OnClick({R.id.ll_newDiary})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_newDiary://创建新日记
                startActivity(new Intent(this, MainActivity.class).putExtra(Constants.DIARY_TYPE, diary_type));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        diaryList.clear();
        diaryBeanDao = DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao();
        List<DiaryBean> diaryBeans = diaryBeanDao.loadAll();
        for (int i = 0; i < diaryBeans.size(); i++) {
            if (diary_type == diaryBeans.get(i).getType()) {
                diaryList.add(diaryBeans.get(i));
            }
        }
        diaryAdapter.notifyDataSetChanged();
    }
}
