package com.hilo.vus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hilo.MainActivity;
import com.hilo.R;
import com.hilo.interfaces.OnNoDoubleClickListener;
import com.hilo.interfaces.Vu;
import com.hilo.interfaces.VuCallBack;
import com.hilo.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hilo on 16/2/24.
 */
public class MainVu implements Vu {

    @Bind(R.id.icon_create)
    ImageButton iconCreate;
    @Bind(R.id.del)
    TextView del;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private View rootView;
    private Context mContext;
    private AppCompatActivity activity;

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    private VuCallBack mStartIntroAnimationCallBack = new VuCallBack() {
        @Override
        public void execute(Object position) {
            startIntroAnimation();
        }
    };

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, Context context) {
        mContext = context;
        rootView = inflater.inflate(R.layout.activity_main, null);
        ButterKnife.bind(this, rootView);
        ((MainActivity)mContext).setOnStartIntroAnimationCallBack(mStartIntroAnimationCallBack);
    }

    @Override
    public View getView() {
        return rootView;
    }

    public void setAppCompatActivity(AppCompatActivity activity) {
        if (activity instanceof AppCompatActivity) {
            this.activity = activity;
            activity.setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(toggle);
            toggle.syncState();
            if (activity instanceof NavigationView.OnNavigationItemSelectedListener)
                navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
            else
                throw new IllegalStateException("Activity must be implements NavigationView.OnNavigationItemSelectedListener");
        } else throw new IllegalStateException("Activity must be instanceof AppCompatActivity");
    }


    private void startIntroAnimation() {
        fab.setTranslationY(2 * mContext.getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        int actionbarSize = UIUtils.dpToPx(56, mContext.getResources());
        toolbar.setTranslationY(-actionbarSize);
        add.setTranslationY(-actionbarSize);
        del.setTranslationY(-actionbarSize);
        ((MainActivity) mContext).getSettingsMenuItem().getActionView().setTranslationY(-actionbarSize);

        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        add.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        del.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500);
        ((MainActivity) mContext).getSettingsMenuItem().getActionView().animate()
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(600)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {
        fab.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();

        ((MainActivity)mContext).getRecyclerFragment().updateItems(true);
    }

    public void setDelDataCallBack(final VuCallBack<Integer> vuCallBack) {
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vuCallBack != null)
                    vuCallBack.execute(-1);
            }
        });
    }

    public void setAddDataCallBack(final VuCallBack<Integer> vuCallBack) {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vuCallBack != null)
                    vuCallBack.execute(-1);
            }
        });
    }

    public void setPopupWindowCallBack(final VuCallBack<View> vuCallBack) {
        iconCreate.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            protected void onNoDoubleClickListener(View v) {
                if (vuCallBack != null) {
                    vuCallBack.execute(iconCreate);
                }
            }
        });
    }

    public void setRefreshCallBack(VuCallBack<Integer> vuCallBack) {
        if (vuCallBack != null) {
            vuCallBack.execute(-1);
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
