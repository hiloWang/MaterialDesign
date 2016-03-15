package com.hilo.vus;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.events.TextVuEvents;
import com.hilo.events.factory.CreateVuSubClass;
import com.hilo.events.factory.VuEventFactory;
import com.hilo.events.iml.AllVuEventsManagerIml;
import com.hilo.events.interfaces.IVuEvents;
import com.hilo.interfaces.Vu;
import com.hilo.others.InvalidVuException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by hilo on 16/2/25.
 */
public class TextVu implements Vu {

    public View rootView;
    public Context mContext;
    public RecyclerAdapter mAdapter;
    public LinearLayoutManager mLinearLayoutManager;
    @Bind(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private IVuEvents vuEvents;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, Context context) {
        initViews(inflater, container, context);
        initEvents();
    }

    @Override
    public View getView() {
        return rootView;
    }

    public void setAdapterData(List<String> data) {
        mAdapter.setData(data);
    }

    private void initViews(LayoutInflater inflater, ViewGroup container, Context context) {
        mContext = context;
        rootView = inflater.inflate(R.layout.activity_text, null);
        ButterKnife.bind(this, rootView);
        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(context);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);
    }

    private void initEvents() {
        try {
            VuEventFactory vuEventFactory = new CreateVuSubClass();
            IVuEvents.createVus v = vuEventFactory.createVus(AllVuEventsManagerIml.class);
            vuEvents = v.setupVu(this);
        } catch (InvalidVuException e) {
            e.printStackTrace();
        }
    }

    public void removeVuEvents() {
        if (vuEvents instanceof TextVuEvents) {
            ((TextVuEvents)vuEvents).removeVuEvents();
        }
    }
}
