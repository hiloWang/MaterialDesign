package com.hilo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.hilo.R;
import com.hilo.interfaces.Vu;
import com.hilo.others.pulltorefresh.PullRefreshLayout;
import com.hilo.receiver.ExceptionLoingOutReceiver;
import com.hilo.utils.LogUtils;
import com.hilo.utils.UtilTool;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hilo on 16/2/24.
 */
public abstract class BasePresenterActivity<V extends Vu> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static PullRefreshLayout mSwipeRefreshLayout;
    public static LinkedList<Activity> mActivityManager;
    public static Map<String, SoftReference<PullRefreshLayout>> mSwipeRefreshManager;

    protected Context mContext;
    protected DelayHandler mHandler;
    protected V vu;
    protected Bundle mSaveInstanceBunder;
    private ExceptionLoingOutReceiver logOutReceiver;
    protected MenuItem settingsMenuItem;

    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    private static final int DELAY_BACK_ACTIVITY = 0x001;
    private long LAST_CLICK_TIME = 0;
    private long MIN_CLICK_DELAY_TIME = 2000;

    protected boolean pendingIntroAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        LogUtils.I(BasePresenterActivity.class.getName());

        initViews(savedInstanceState);

        try {
            vu = getVuClass().newInstance();
            vu.init(getLayoutInflater(), null, mContext);
            setContentView(vu.getView());
            onBindVu();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        } else {
            updateItems(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSwipeRefreshManager.containsKey(getClass().getName())) {
            mSwipeRefreshLayout = mSwipeRefreshManager.get(getClass().getName()).get();
        }
        afterResume();
    }

    @Override
    protected void onPause() {
        beforePause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        beforeDestroy();
        repleaseResources();
        super.onDestroy();
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {

        if (mActivityManager != null && mActivityManager.size() != 1) {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.activity_swipeback_slide_right_out);
        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackCount == 0) {
                if (System.currentTimeMillis() - LAST_CLICK_TIME > MIN_CLICK_DELAY_TIME) {
                    LAST_CLICK_TIME = System.currentTimeMillis();
                    Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
                } else {
                    repleaseResources();
                    UtilTool.setVariablesNull();
                    finishThis();
                }
            } else {
                super.onBackPressed();
                overridePendingTransition(0, R.anim.activity_swipeback_slide_right_out);
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_swipeback_slide_right_in, R.anim.activity_swipeback_slide_remain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        settingsMenuItem = menu.findItem(R.id.action_settings);
        settingsMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        trySetupSwipeRefresh();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            LogUtils.E("No view with ID main_content to fade in.");
        }
    }


    private void initViews(Bundle savedInstanceState) {
        mContext = this;
        mSaveInstanceBunder = savedInstanceState;
        mHandler = new DelayHandler(this);
//        registerActivityLoginOut();
        if (mActivityManager == null) mActivityManager = new LinkedList<>();
        if (mSwipeRefreshManager == null) mSwipeRefreshManager = new LinkedHashMap<>();
        mActivityManager.add(this);
    }

    private void trySetupSwipeRefresh() {
        mSwipeRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshManager.put(getClass().getName(), new SoftReference<>(mSwipeRefreshLayout));
            mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefreshing() {
                    requestDataRefresh();
                }
            });
            mSwipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
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

    // true：can not use swipeRefreshLayout scroll up false:otherwise
    protected static void onRefreshingStateChanged(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
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

    private void registerActivityLoginOut() {
        logOutReceiver = new ExceptionLoingOutReceiver();
        IntentFilter filter = new IntentFilter("android.exception.ExceptionLoingOutReceiver");
        registerReceiver(logOutReceiver, filter);
    }

    private static void finishAllActivities() {
        if (mActivityManager == null) return;
        for (Activity act : mActivityManager) {
            act.finish();
        }
        mActivityManager.clear();
        mActivityManager = null;
    }

    public static void logoutReceiverRepleaseResources() {
        try {
            UtilTool.setVariablesNull();
            finishAllActivities();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void repleaseResources() {

        if (mSwipeRefreshManager != null && mSwipeRefreshManager.containsKey(getClass().getName())) {
            mSwipeRefreshManager.remove(getClass().getName());
            if (mSwipeRefreshManager != null && mSwipeRefreshManager.size() == 0) {
                mSwipeRefreshManager = null;
                mSwipeRefreshLayout.removeAllViews();
                mSwipeRefreshLayout = null;
            }
        }
        if (mActivityManager != null && mActivityManager.contains(this)) {
            mActivityManager.remove(this);
            if (mActivityManager != null && mActivityManager.size() == 0) {
                mActivityManager = null;
            }
        }
    }

    private void finishThis() {
        if (mContext != null) mContext = null;
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        finish();
        System.gc();
    }

    protected void onBindVu() {
    }

    protected void afterResume() {
    }

    protected void beforePause() {
    }

    protected void beforeDestroy() {
    }

    protected void updateItems(boolean animated) {
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
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }
}
