package com.hilo.fragment;

import android.content.Context;
import android.widget.Toast;

import com.hilo.MainActivity;
import com.hilo.base.BasePresenterFragment;
import com.hilo.interfaces.VuCallBack;
import com.hilo.utils.UIUtils;
import com.hilo.vus.RecyclerFragmentVu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hilo on 16/2/19.
 */
public class RecyclerFragment extends BasePresenterFragment<RecyclerFragmentVu> {

    private List<String> mData;

    // 下拉刷新
    VuCallBack<Integer> mOnRefreshCallBack = new VuCallBack<Integer>() {
        @Override
        public void execute(Integer position) {
            if(position == -1) {
            }
        }
    };

    // 删除item数据
    VuCallBack<Integer> mDelDataCallback = new VuCallBack<Integer>() {
        @Override
        public void execute(Integer position) {
            vu.setDelData(1);
        }
    };

    // 添加item数据
    VuCallBack<Integer> mAddDataCallback = new VuCallBack<Integer>() {
        @Override
        public void execute(Integer position) {
            vu.setAddData("new Data", 1);
        }
    };

    public static RecyclerFragment getInstance() {
        RecyclerFragment fragment = new RecyclerFragment();
        return fragment;
    }

    @Override
    protected void onCreateInitViews() {
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("点我跳转activity，动画效果 <" + i + ">");
        }
        ((MainActivity)mContext).setOnRefreshingCallback(mOnRefreshCallBack);
    }

    @Override
    protected void onBindVu() {
        vu.setLongClickCallBack(mCallbacks);
        ((MainActivity) mContext).setDelDataCallback(mDelDataCallback);
        ((MainActivity) mContext).setAddDataCallback(mAddDataCallback);

        vu.setAdapterData(mData);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected Class<RecyclerFragmentVu> getVuClass() {
        return RecyclerFragmentVu.class;
    }
}
