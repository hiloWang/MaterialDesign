package com.hilo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hilo.MainActivity;
import com.hilo.R;
import com.hilo.adapter.RecyclerAdapter;
import com.hilo.interfaces.VuCallBack;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by hilo on 16/2/19.
 */
public class RecyclerFragment extends BaseRecyclerFragment{

    private View rootView;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;
    private RecyclerAdapter mAdapter;
    private Context mContext;
    private List<String> mData;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(Activity activity, int position, String text);

        int getSelectedFragment();

        Activity getActivityCallBacks();
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Activity activity, int position, String text) {
        }

        @Override
        public int getSelectedFragment(){
            return 0;
        }

        @Override
        public Activity getActivityCallBacks() {
            return null;
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    public static RecyclerFragment getInstance(Context context) {
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = new ArrayList<>();
        for(int i=0; i<20; i++) {
            mData.add("测试数据 "+i);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.textactivity_recycleview, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecycleView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        } else {
            mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                        // 当滚动到最后一条时的逻辑处理
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(mRecycleView != null && mRecycleView.getChildCount() > 0)
                        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                }
            });
        }*/
        mRecycleView.setItemAnimator(new FadeInAnimator());
//        mRecycleView.getItemAnimator().setAddDuration(500);
//        mRecycleView.getItemAnimator().setRemoveDuration(500);
        mAdapter = new RecyclerAdapter(mContext, mData);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecycleView.setAdapter(scaleAdapter);
        ((MainActivity)mContext).setDelDataCallback(mDelDataCallback);
        ((MainActivity)mContext).setAddDataCallback(mAddDataCallback);
    }

    VuCallBack<Integer> mDelDataCallback = new VuCallBack<Integer>() {
        @Override
        public void execute(Integer position) {
            if(mAdapter.getItemCount() > 1)
                mAdapter.remove(1);
        }
    };

    VuCallBack<Integer> mAddDataCallback = new VuCallBack<Integer>() {
        @Override
        public void execute(Integer position) {
            mAdapter.add("new Data", 1);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setRecyclerViewOnItemClickListener(new RecyclerAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv = (TextView) view.findViewById(R.id.item_1);
                tv.setText("dont click me!! plz");
            }

            @Override
            public void onItemlongClick(View view, int position) {
                mCallbacks.onItemSelected(mCallbacks.getActivityCallBacks(), position, "当前点击了第"+position+"个条目");
            }
        });
    }
}
