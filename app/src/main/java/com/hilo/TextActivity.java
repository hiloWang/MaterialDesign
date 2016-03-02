package com.hilo;

import android.widget.Toast;

import com.hilo.base.BasePresenterActivity;
import com.hilo.vus.TextVu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hilo on 16/2/24.
 */
public class TextActivity extends BasePresenterActivity<TextVu> {
    List<String> mData;

    @Override
    protected void onBindVu() {
        initData();
    }

    @Override
    protected Class getVuClass() {
        return TextVu.class;
    }

    @Override
    protected void onRefreshingListener() {
        Toast.makeText(mContext, mContext.getClass().getName(), Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("点我跳转activity，动画效果 <" + i + ">");
        }
        vu.setAdapterData(mData);
    }
}
