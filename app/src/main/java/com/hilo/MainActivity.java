package com.hilo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.hilo.base.BasePresenterActivity;
import com.hilo.fragment.RecyclerFragment;
import com.hilo.interfaces.VuCallBack;
import com.hilo.vus.MainVu;

public class MainActivity extends BasePresenterActivity<MainVu>
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerFragment.Callbacks {

    private RecyclerFragment mRecyclerFragment;

    @Override
    protected void onBindVu() {
        View rootView = vu.getView();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showFragment(RecyclerFragment.class);

        rootView.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDelDataCallback != null)
                    mDelDataCallback.execute(-1);
            }
        });

        TextView addView = (TextView) rootView.findViewById(R.id.add);
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddDataCallback != null)
                    mAddDataCallback.execute(-1);
            }
        });
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return super.canSwipeRefreshChildScrollUp();
    }

    @Override
    protected Class getVuClass() {
        return MainVu.class;
    }

    VuCallBack<Integer> mRefreshingCallback;
    public void setOnRefreshingCallback(VuCallBack<Integer> vuCallBack) {
        mRefreshingCallback = vuCallBack;
    }
    @Override
    protected void onRefreshingListener() {
        if(mRefreshingCallback != null)
            mRefreshingCallback.execute(-1);
    }

    VuCallBack<Integer> mDelDataCallback;
    public void setDelDataCallback(VuCallBack<Integer> vuCallBack) {
        mDelDataCallback = vuCallBack;
    }

    VuCallBack<Integer> mAddDataCallback;
    public void setAddDataCallback(VuCallBack<Integer> vuCallBack) {
        mAddDataCallback = vuCallBack;
    }

    private <T extends Fragment> void showFragment(Class<T> clzz) {
        mRecyclerFragment = RecyclerFragment.getInstance(mContext);
        try {
            if(mRecyclerFragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, clzz.newInstance(),clzz.getSimpleName()).commit();
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
        if(!removeFragment()) {
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
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().commit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public void onItemSelected(Activity activity, int position, String text) {
        Toast.makeText(mContext, text + "<"+activity.getLocalClassName()+">" + "\n" +
                "当前是由<"+((MainActivity)activity).getSupportFragmentManager().findFragmentById(R.id.fragmentContainer).getClass().getSimpleName()+">传递过来的", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getSelectedFragment() {
        return 0;
    }

    @Override
    public Activity getActivityCallBacks() {
        return this;
    }
}
