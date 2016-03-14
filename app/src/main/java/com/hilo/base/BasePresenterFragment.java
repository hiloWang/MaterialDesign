package com.hilo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilo.interfaces.Vu;

/**
 * Created by hilo on 16/2/25.
 */
public abstract class BasePresenterFragment<V extends Vu> extends Fragment {

    protected Context mContext;
    protected V vu;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        onCreateInitViews();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            vu = getVuClass().newInstance();
            vu.init(inflater, container, container.getContext());
            onBindVu();
            rootView = vu.getView();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        afterResume();
    }

    @Override
    public void onPause() {
        beforePause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 与onCreateView想对应，当该Fragment的视图被移除时调用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        beforeDestroy();
        super.onDestroy();
    }

    /**
     * 与onAttach相对应，当Fragment与Activity关联被取消时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
        vu = null;
        mContext = null;
    }

    protected void onBindVu() {
    }

    protected void afterResume() {
    }

    protected void beforePause() {
    }

    protected void beforeDestroy() {
    }

    protected void onCreateInitViews() {
    }

    protected abstract Class<V> getVuClass();


    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    protected Callbacks mCallbacks = sDummyCallbacks;

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
        public int getSelectedFragment() {
            return 0;
        }

        @Override
        public Activity getActivityCallBacks() {
            return null;
        }

    };

    public static void setVariablesNull() {
        if (sDummyCallbacks != null)
            sDummyCallbacks = null;
    }
}
