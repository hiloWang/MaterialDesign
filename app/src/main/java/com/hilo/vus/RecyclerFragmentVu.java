package com.hilo.vus;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.base.BasePresenterFragment;
import com.hilo.events.factory.CreateVuSubClass;
import com.hilo.events.factory.VuEventFactory;
import com.hilo.events.interfaces.IVuEvents;
import com.hilo.events.iml.AllVuEventsManagerIml;
import com.hilo.interfaces.Vu;
import com.hilo.others.InvalidVuException;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by hilo on 16/2/25.
 */
public class RecyclerFragmentVu implements Vu {

    public Context mContext;
    public View rootView;
    public RecyclerView mRecyclerView;
    public RecyclerAdapter adapter;
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
        adapter = new RecyclerAdapter(container.getContext());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);
    }

    private void initEvents() {
        VuEventFactory factory = new CreateVuSubClass();
        IVuEvents.createVus vus = factory.createVus(AllVuEventsManagerIml.class);
        try {
            vus.setupVu(this);
        } catch (InvalidVuException e) {
            e.printStackTrace();
        }
    }

    public void setAdapterData(List<String> data) {
        adapter.setData(data);
    }

    public void setDelData(int position) {
        if (adapter.getItemCount() > position)
            adapter.remove(position);
    }

    public void setAddData(String msg, int position) {
        if (adapter.getItemCount() > position)
            adapter.add(msg, position);
    }
}
