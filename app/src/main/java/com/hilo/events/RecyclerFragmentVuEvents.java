package com.hilo.events;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.base.BasePresenterFragment;
import com.hilo.utils.AnimUtils;

/**
 * Created by hilo on 16/3/2.
 */
public class RecyclerFragmentVuEvents {

    private int lastVisibleItem;

    private RecyclerFragmentVuEvents() {}
    private static RecyclerFragmentVuEvents recyclerFragmentVuEvents = new RecyclerFragmentVuEvents();

    public static RecyclerFragmentVuEvents getDefaultRecyclerViewManager() {
        return recyclerFragmentVuEvents;
    }

    public void setOnScrollChangeListener(final Context context, RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager, final RecyclerAdapter adapter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        } else {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                        // 当滚动到最后一条时的逻辑处理
                        Toast.makeText(context, "没有数据可加载", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (recyclerView != null && recyclerView.getChildCount() > 0)
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                }
            });
        }
    }

    public void setOnItemClickListener(Context context, RecyclerAdapter adapter, final BasePresenterFragment.Callbacks callbacks) {
        adapter.setRecyclerViewOnItemClickListener(new RecyclerAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv = (TextView) view.findViewById(R.id.item_1);
                ImageView mImage = (ImageView) view.findViewById(R.id.image);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    AnimUtils.attrCreateCircularReveal(mImage, 500);
                    AnimUtils.attrCreateCircularReveal(tv, 2000);
                }
                tv.setText("长按Item可触发longClick事件监听");
            }

            @Override
            public void onItemlongClick(View view, int position) {
                if (callbacks != null)
                    callbacks.onItemSelected(callbacks.getActivityCallBacks(), position, "当前点击了第" + position + "个条目");
            }
        });
    }
}
