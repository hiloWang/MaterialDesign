package com.hilo.events;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.events.interfaces.IVuEvents;
import com.hilo.utils.AnimUtils;
import com.hilo.vus.TextVu;

/**
 * Created by hilo on 16/3/2.
 */
public class TextVuEvents extends IVuEvents {

    private int lastVisibleItem;

    public TextVuEvents(TextVu vu) {
        setRecyclerOnScrollChangeListener(vu);
        setRecyclerOnItemClickListener(vu);
    }

    private void setRecyclerOnScrollChangeListener(final TextVu vu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vu.mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        } else {
            vu.mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == vu.mAdapter.getItemCount()) {
                        // 当滚动到最后一条时的逻辑处理
                        Toast.makeText(vu.mContext, "没有数据可加载", Toast.LENGTH_SHORT).show();
                    } else if (vu.mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {

                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (recyclerView != null && recyclerView.getChildCount() > 0)
                        lastVisibleItem = vu.mLinearLayoutManager.findLastVisibleItemPosition();
                }
            });
        }
    }

    private void setRecyclerOnItemClickListener(final TextVu vu) {
        vu.mAdapter.setRecyclerViewOnItemClickListener(new RecyclerAdapter.RecyclerViewOnItemClickListener() {
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

            }
        });
    }

}
