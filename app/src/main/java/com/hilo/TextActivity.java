package com.hilo;

import android.os.Bundle;
import android.widget.Toast;

import com.hilo.activity.SwipeBackActivity;
import com.hilo.base.BasePresenterActivity;
import com.hilo.vus.TextVu;

/**
 * Created by hilo on 16/2/24.
 */
public class TextActivity extends BasePresenterActivity<TextVu> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Class getVuClass() {
        return TextVu.class;
    }

    @Override
    protected void onRefreshingListener() {
        Toast.makeText(this, "TextActivity OnRefreshing ...", Toast.LENGTH_SHORT).show();
    }
}
