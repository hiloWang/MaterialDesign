package com.hilo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.hilo.R;
import com.hilo.dialog.actionsheet.NormalDialog;
import com.hilo.interfaces.Vu;
import com.hilo.receiver.ExceptionLoingOutReceiver;
import com.hilo.utils.LogUtils;
import com.hilo.utils.UtilTool;
import com.hilo.view.MultiSwipeRefreshLayout;
import com.hilo.view.SwipeBackLayout;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Created by hilo on 16/2/24.
 */
public abstract class BasePresenterActivity<V extends Vu> extends AppCompatActivity
        implements MultiSwipeRefreshLayout.CanChildScrollUpCallback {

    protected Context mContext;
    protected DelayHandler mHandler;
    protected V vu;
    protected static SwipeRefreshLayout mSwipeRefreshLayout;
    protected Bundle mSaveInstanceBunder;
    protected static LinkedList<Activity> mActivityManager;
    protected SwipeBackLayout swipeBackLayout;
    private ExceptionLoingOutReceiver logOutReceiver;

    private static boolean isExitApp;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    private NormalDialog exitDialog;
    private static final int DELAY_BACK_ACTIVITY = 0x001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        LogUtils.I(BasePresenterActivity.class.getName());
        swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.activity_swipebackbase, null);
        swipeBackLayout.attachToActivity(this);
        mContext = this;
        mHandler = new DelayHandler(this);
        mSaveInstanceBunder = savedInstanceState;
        registerActivityLoginOut();
        if (mActivityManager == null) mActivityManager = new LinkedList<>();
        mActivityManager.add(this);

        try {
            vu = getVuClass().newInstance();
            vu.init(getLayoutInflater(), null);
            setContentView(vu.getView());
            onBindVu();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_swipeback_slide_right_in, R.anim.activity_swipeback_slide_remain);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        trySetupSwipeRefresh();
        updateSwipeRefreshProgressBarTop();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            LogUtils.E("No view with ID main_content to fade in.");
        }
    }

    private void trySetupSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });

            if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout) {
                MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
                mswrl.setCanChildScrollUpCallback(this);
            }
        }
    }

    protected void requestDataRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshingListener();
                onRefreshingStateChanged(false);
            }
        }, 250);
    }

    protected static void onRefreshingStateChanged(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    private void updateSwipeRefreshProgressBarTop() {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        int progressBarStartMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_start_margin);
        int progressBarEndMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_end_margin);
        int top = 0;
        mSwipeRefreshLayout.setProgressViewOffset(false,
                top + progressBarStartMargin, top + progressBarEndMargin);
    }

    // true：can not use swipeRefreshLayout scroll up false:otherwise
    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }


    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.activity_swipeback_slide_right_out);
    }

 /*   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       *//* if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mActivityManager.size() == 1) {
                exitDialog = new NormalDialog(mContext);
                exitDialog.content("亲,真的要走吗?再看会儿吧~(●—●)")
                        .style(NormalDialog.STYLE_TWO)
                        .titleTextSize(23)
                        .btnText("继续逛逛", "残忍退出")
                        .btnTextColor(Color.parseColor("#383838"), Color.parseColor("#D4D4D4"))
                        .btnTextSize(16f, 16f)
                        .showAnim(new BounceTopEnter())
                        .dismissAnim(new SlideBottomExit())
                        .show();
                exitDialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        exitDialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        exitDialog.superDismiss();
                        exitDialog = null;
                        finish();
                    }
                });
            } else {
                finish();
            }
            return true;
        }*//*
        return false;
    }*/

    public static void exit() {
        try {
            UtilTool.setVariablesNull();
            finishAllActivities();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerActivityLoginOut() {
        logOutReceiver = new ExceptionLoingOutReceiver();
        IntentFilter filter = new IntentFilter("android.exception.ExceptionLoingOutReceiver");
        registerReceiver(logOutReceiver, filter);
    }

    private static void finishAllActivities() {
        for (Activity act : mActivityManager) {
            act.finish();
        }
        mActivityManager.clear();
        mActivityManager = null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        afterResume();
    }

    @Override
    protected void onPause() {
        beforePause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        beforeDestroy();
        vu = null;
        mSwipeRefreshLayout = null;
        mActivityManager.remove(this);
        unregisterReceiver(logOutReceiver);
        super.onDestroy();
    }


    protected void onBindVu() {
    }

    protected void afterResume() {
    }

    protected void beforePause() {
    }

    protected void beforeDestroy() {
    }

    protected abstract Class<V> getVuClass();

    protected abstract void onRefreshingListener();


    private static class DelayHandler extends Handler {
        private final WeakReference<BasePresenterActivity> weakReference;

        public DelayHandler(BasePresenterActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BasePresenterActivity mActivity = weakReference.get();
            if (mActivity != null) {
                switch (msg.what) {
                    case DELAY_BACK_ACTIVITY:
                        isExitApp = false;
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }
}
