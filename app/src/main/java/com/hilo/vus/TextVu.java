package com.hilo.vus;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.interfaces.Vu;
import com.hilo.utils.AnimUtils;
import com.hilo.utils.UIUtils;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by hilo on 16/2/25.
 */
public class TextVu implements Vu {

    private View rootView;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, Context context) {
        mContext = context;
        rootView = inflater.inflate(R.layout.activity_text, null);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(context);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
//        mRecyclerView.getItemAnimator().setAddDuration(500);
//        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        } else {
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                        // 当滚动到最后一条时的逻辑处理
                        Toast.makeText(mContext, "没有数据可加载", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mRecyclerView != null && mRecyclerView.getChildCount() > 0)
                        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                }
            });
        }

        mAdapter.setRecyclerViewOnItemClickListener(new RecyclerAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv = (TextView) view.findViewById(R.id.item_1);
                ImageView mImage = (ImageView) view.findViewById(R.id.image);
                Animator animator;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    animator = ViewAnimationUtils.createCircularReveal(
                            mImage,
                            0,
                            0,
                            0,
                            (float) Math.hypot(mImage.getWidth(), mImage.getHeight()));
                    animator.setInterpolator(new AccelerateInterpolator());
                    animator.setDuration(500);
                    animator.start();

                    animator = ViewAnimationUtils.createCircularReveal(
                            tv,
                            0,
                            0,
                            0,
                            (float) Math.hypot(tv.getWidth(), tv.getHeight()));
                    animator.setInterpolator(new AccelerateInterpolator());
                    animator.setDuration(500);
                    animator.start();
                }
                tv.setText("长按Item可触发longClick事件监听");
            }

            @Override
            public void onItemlongClick(View view, int position) {
                Toast.makeText(mContext, "当前点击了第" + position + "个条目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View getView() {
        return rootView;
    }

    public void setAdapterData(List<String> data) {
        mAdapter.setData(data);
    }
}
