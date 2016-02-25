package com.hilo.interfaces;

import android.view.View;

import java.util.Calendar;

/**
 * Created by hilo on 16/2/25.
 */
public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if(currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClickListener(v);
        }
    }

    protected abstract void onNoDoubleClickListener(View v);

}
