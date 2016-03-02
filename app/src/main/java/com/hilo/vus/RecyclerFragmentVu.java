package com.hilo.vus;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.base.BasePresenterFragment;
import com.hilo.events.RecyclerFragmentVuEvents;
import com.hilo.interfaces.Vu;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by hilo on 16/2/25.
 */
public class RecyclerFragmentVu implements Vu {

    public Context mContext;
    public View rootView;
    public RecyclerView mRecyclerView;
    public RecyclerAdapter mAdapter;
    public LinearLayoutManager mLinearLayoutManager;

    public BasePresenterFragment.Callbacks mCallbacks;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, final Context context) {
        initViews(inflater, container, context);
        initEvents();
    }

    @Override
    public View getView() {
        return rootView;
    }

    public void setLongClickCallBack(BasePresenterFragment.Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    private void initViews(LayoutInflater inflater, ViewGroup container, Context context) {
        mContext = context;
        rootView = inflater.inflate(R.layout.textactivity_recycleview, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        mLinearLayoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        mAdapter = new RecyclerAdapter(container.getContext());
//        mRecyclerView.getItemAnimator().setAddDuration(500);
//        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEvents() {
        recyclerViewEvent();
        adapterEvent();
    }

    private void recyclerViewEvent() {
        RecyclerFragmentVuEvents.getDefaultRecyclerViewManager().setOnScrollChangeListener(this);
    }

    private void adapterEvent() {
        RecyclerFragmentVuEvents.getDefaultRecyclerViewManager().setOnItemClickListener(this);
    }

    public void setAdapterData(List<String> data) {
        mAdapter.setData(data);
    }

    public void setDelData(int position) {
        if (mAdapter.getItemCount() > position)
            mAdapter.remove(position);
    }

    public void setAddData(String msg, int position) {
        if (mAdapter.getItemCount() > position)
            mAdapter.add(msg, position);
    }
}
