package com.hilo;

import android.os.Bundle;

import com.hilo.activity.SwipeBackActivity;

/**
 * Created by hilo on 16/2/24.
 */
public class TextActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
    }
}
