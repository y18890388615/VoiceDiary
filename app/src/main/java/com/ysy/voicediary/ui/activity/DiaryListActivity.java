package com.ysy.voicediary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.umeng.socialize.UMShareAPI;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseActivity;
import com.ysy.voicediary.bean.DiaryBean;
import com.ysy.voicediary.db.DiaryBeanDao;
import com.ysy.voicediary.ui.adapter.DiaryAdapter;
import com.ysy.voicediary.utils.DataBaseUtil;
import com.ysy.voicediary.utils.SortListUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DiaryListActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.ll_newType)
    LinearLayout llNewDiary;
    @BindView(R.id.tv_important)
    TextView tvImportant;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll)
    LinearLayout ll;
    private int diary_type;
    private DiaryBeanDao diaryBeanDao;
    private List<DiaryBean> diaryList = new ArrayList<>();
    private DiaryAdapter diaryAdapter;
    private int sortType;
    private final static int TIME_SORT = 0;
    private final static int IMPORTANT_SORT = 1;

    @Override
    public int getLayoutID() {
        return R.layout.activity_diary_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        sortType = IMPORTANT_SORT;
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
        if (diary_type == Constants.AFFAIRS) {
            ll.setVisibility(View.VISIBLE);
        } else {
            ll.setVisibility(View.GONE);
        }
        diaryBeanDao = DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao();
        List<DiaryBean> diaryBeans = diaryBeanDao.loadAll();
        for (int i = 0; i < diaryBeans.size(); i++) {
            if (diary_type == diaryBeans.get(i).getType() && diaryBeans.get(i).getAccount().
                    equals(SPUtils.getInstance().getString(Constants.ACCOUNT))) {
                diaryList.add(diaryBeans.get(i));
            }
        }
        sort();
        initRecycleView();
    }

    /**
     * 排序
     */
    private void sort() {
        List<DiaryBean> diaryListCopy = new ArrayList<>();
        if (sortType == IMPORTANT_SORT) {
            SortListUtil.sort(diaryList, "important", "desc");
        } else if (sortType == TIME_SORT) {
            for (int i = 0; i < diaryList.size(); i++) {
                for (int j = diaryList.size() - 1; j > 0; j--) {
                    String[] split = diaryList.get(j).getTime().split("-");
                    String[] split2 = diaryList.get(j - 1).getTime().split("-");
                    if (Integer.valueOf(split[0]) > Integer.valueOf(split2[0]) ||
                            Integer.valueOf(split[1]) > Integer.valueOf(split2[1]) ||
                            Integer.valueOf(split[2]) > Integer.valueOf(split2[2])) {
                        DiaryBean diaryBean = diaryList.get(j);
                        diaryList.set(j, diaryList.get(j - 1));
                        diaryList.set(j - 1, diaryBean);
                    }
                }
            }
        }
        for (int i = 0; i < diaryList.size(); i++) {
            if (diaryList.get(i).getPriority() == 1) {
                diaryListCopy.add(diaryList.get(i));
            }
        }
        for (int i = 0; i < diaryList.size(); i++) {
            if (diaryList.get(i).getPriority() == 0) {
                diaryListCopy.add(diaryList.get(i));
            }
        }
        diaryList.clear();
        diaryList.addAll(diaryListCopy);
    }


    @OnClick({R.id.ll_newType, R.id.tv_important, R.id.tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_newType://创建新日记
                startActivity(new Intent(this, MainActivity.class).putExtra(Constants.DIARY_TYPE, diary_type));
                break;
            case R.id.tv_important://重要级
                tvImportant.setBackgroundColor(getColor(R.color.color_blue));
                tvTime.setBackgroundColor(getColor(R.color.color_white));
                sortType = IMPORTANT_SORT;
                sort();
                diaryAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_time://日期
                tvImportant.setBackgroundColor(getColor(R.color.color_white));
                tvTime.setBackgroundColor(getColor(R.color.color_blue));
                sortType = TIME_SORT;
                sort();
                diaryAdapter.notifyDataSetChanged();
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
            if (diary_type == diaryBeans.get(i).getType() && diaryBeans.get(i).getAccount().
                    equals(SPUtils.getInstance().getString(Constants.ACCOUNT))) {
                diaryList.add(diaryBeans.get(i));
            }
        }
        sort();
        diaryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
