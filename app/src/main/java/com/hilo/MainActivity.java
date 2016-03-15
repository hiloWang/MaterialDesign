package com.hilo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hilo.base.BasePresenterActivity;
import com.hilo.base.BasePresenterFragment;
import com.hilo.fragment.RecyclerFragment;
import com.hilo.interfaces.VuCallBack;
import com.hilo.vus.MainVu;

public class MainActivity extends BasePresenterActivity<MainVu> implements BasePresenterFragment.Callbacks {

    private PopupWindow popupWindow;
    private RecyclerFragment mRecyclerFragment;

    @Override
    protected void onBindVu() {
        vu.setAppCompatActivity(this);
//        vu.setPopupWindowCallBack(mPopupWindowCallBack);
        initFragment(RecyclerFragment.class);
    }

    @Override
    protected Class getVuClass() {
        return MainVu.class;
    }

    @Override
    protected void onRefreshingListener() {
        if (mRefreshingCallback != null)
            vu.setRefreshCallBack(mRefreshingCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    protected void startIntroAnimation() {
        if (mStartIntroAnimationCallBack != null)
            mStartIntroAnimationCallBack.execute(-1);
    }

    private VuCallBack mStartIntroAnimationCallBack;

    public void setOnStartIntroAnimationCallBack(VuCallBack vuCallBack) {
        mStartIntroAnimationCallBack = vuCallBack;
    }

    private VuCallBack<Integer> mRefreshingCallback;

    public void setOnRefreshingCallback(VuCallBack<Integer> vuCallBack) {
        mRefreshingCallback = vuCallBack;
    }

    public void setDelDataCallback(VuCallBack<Integer> vuCallBack) {
        vu.setDelDataCallBack(vuCallBack);
    }

    public void setAddDataCallback(VuCallBack<Integer> vuCallBack) {
        vu.setAddDataCallBack(vuCallBack);
    }

    /*VuCallBack<View> mPopupWindowCallBack = new VuCallBack<View>() {
        @Override
        public void execute(View v) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int x = wm.getDefaultDisplay().getWidth() / 4;
            int y = v.getBottom() * 3 / 2;
            View rootView = LayoutInflater.from(mContext).inflate(R.layout.popup_create_listview, null);
            ListView mPopupListView = (ListView) rootView.findViewById(R.id.popup_listview);
            mPopupListView.setAdapter(new PopupAdapter(mContext));
            popupWindow = new PopupWindow(mContext);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());

            popupWindow.setWidth((int) (wm.getDefaultDisplay().getWidth() * 3.5 / 10));
            popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setContentView(rootView);
            popupWindow.setAnimationStyle(R.style.popupCreateAnimation);
            popupWindow.showAtLocation(v, Gravity.LEFT|Gravity.TOP, (int) (wm.getDefaultDisplay().getWidth() * 6.4 / 10), y + UIUtils.dpToPx(4, getResources()));

            mPopupListView.setRecyclerOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Toast.makeText(mContext, "点击了第"+position+"个条目", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
        }
    };*/

    private <T extends Fragment> void initFragment(Class<T> clzz) {
        mRecyclerFragment = RecyclerFragment.getInstance();
        try {
            if (mRecyclerFragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, clzz.newInstance(), clzz.getSimpleName()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, mRecyclerFragment).commit();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!removeFragment()) {
            super.onBackPressed();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean removeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().commit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemSelected(Activity activity, int position, String text) {
        Toast.makeText(mContext, text + "<" + activity.getLocalClassName() + ">" + "\n" +
                "当前是由<" + ((MainActivity) activity).getSupportFragmentManager().findFragmentById(R.id.fragmentContainer).getClass().getSimpleName() + ">传递过来的", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getSelectedFragment() {
        return 0;
    }

    @Override
    public Activity getActivityCallBacks() {
        return this;
    }

    public MenuItem getSettingsMenuItem() {
        return settingsMenuItem;
    }

    protected void updateItems(boolean animated) {
        mRecyclerFragment.updateItems(animated);
    }

    public RecyclerFragment getRecyclerFragment() {
        return mRecyclerFragment;
    }

    @Override
    protected void beforeDestroy() {
        vu.setupAppCompatActivityNull();
    }
}
