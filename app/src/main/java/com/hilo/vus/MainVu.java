package com.hilo.vus;

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
import android.widget.TextView;

import com.hilo.R;
import com.hilo.interfaces.Vu;
import com.hilo.interfaces.VuCallBack;

/**
 * Created by hilo on 16/2/24.
 */
public class MainVu implements Vu {

    private View rootView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    private TextView addView, delView;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_main, null);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        delView = (TextView) rootView.findViewById(R.id.del);
        addView = (TextView) rootView.findViewById(R.id.add);
    }

    @Override
    public View getView() {
        return rootView;
    }

    public void setAppCompatActivity(AppCompatActivity activity) {
        if (activity instanceof AppCompatActivity) {
            activity.setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            if (activity instanceof NavigationView.OnNavigationItemSelectedListener)
                navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
            else
                throw new IllegalStateException("Activity must be implements NavigationView.OnNavigationItemSelectedListener");
        } else throw new IllegalStateException("Activity must be instanceof AppCompatActivity");
    }

    public void setDelDataCallBack(final VuCallBack<Integer> vuCallBack) {
        delView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vuCallBack != null)
                    vuCallBack.execute(-1);
            }
        });
    }

    public void setAddDataCallBack(final VuCallBack<Integer> vuCallBack) {
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vuCallBack != null)
                    vuCallBack.execute(-1);
            }
        });
    }

    public void setRefreshCallBack(VuCallBack<Integer> vuCallBack) {
        if (vuCallBack != null) {
            vuCallBack.execute(-1);
        }
    }

}
