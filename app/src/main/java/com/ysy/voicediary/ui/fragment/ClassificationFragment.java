package com.ysy.voicediary.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseFragment;
import com.ysy.voicediary.bean.TypeBean;
import com.ysy.voicediary.ui.adapter.TypeAdapter;
import com.ysy.voicediary.utils.DataBaseUtil;
import com.ysy.voicediary.utils.DialogUtil;
import com.ysy.voicediary.widget.dialog.NewTypeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日记分类
 */
public class ClassificationFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_newType)
    LinearLayout llNewType;
    private List<TypeBean> list = new ArrayList<>();
    private TypeAdapter typeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        initRecycleView();
    }

    private void setData() {
        TypeBean typeBean = new TypeBean();
        typeBean.setTypeId((long) Constants.STUDY);
        typeBean.setTypeName("学习");
        TypeBean typeBean2 = new TypeBean();
        typeBean2.setTypeId((long) Constants.AFFAIRS);
        typeBean2.setTypeName("待办事宜");
        TypeBean typeBean3 = new TypeBean();
        typeBean3.setTypeId((long) Constants.LIFE);
        typeBean3.setTypeName("生活");
        list.add(typeBean);
        list.add(typeBean2);
        list.add(typeBean3);
    }

    private void initRecycleView() {
        List<TypeBean> typeBeans = DataBaseUtil.getInstance().getDaoSession().getTypeBeanDao().loadAll();
        list.clear();
        setData();
        list.addAll(typeBeans);
        typeAdapter = new TypeAdapter(R.layout.item_type, list, getActivity(), new TypeAdapter.OnclickListener() {
            @Override
            public void delete() {
                initRecycleView();
            }
        });
        typeAdapter.openLoadAnimation();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(typeAdapter);

    }

    @OnClick({R.id.ll_newType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_newType://新建类型
                 DialogUtil.showNewTypeDialog(getContext(), new NewTypeDialog.OnClickListener() {
                    @Override
                    public void click() {
                        initRecycleView();
                    }
                });
                break;
        }
    }
}
