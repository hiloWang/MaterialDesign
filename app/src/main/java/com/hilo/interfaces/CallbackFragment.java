package com.hilo.interfaces;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * 用于Fragment与ctivity之间通信(不可直接用，要将该类的方法复制到fragment里，例如：RecycleFragment)
 *
 * 1、当fragment添加到activity中时，会调用fragment的方法onAttach()，这个方法中适合检查activity是否实现了CallbackFragment.Callbacks接口
 * 2、在一个fragment从activity中剥离的时候，就会调用onDetach方法，这个时候正好可以把它的接口恢复为默认接口。防止不必要的错误。注意看onAttach方法中的代码，在赋值之前要做一个判断，看看Activity中有没有实现了这个接口，用到了instanceof。如果没有实现接口，我们就抛出异常。
 这个时候我们就实现了Fragment与Activity之间的通讯，把主要的事情还是都交给管理员Activity做比较好。
 */
public class CallbackFragment extends Fragment {

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
        void onItemSelected(long l);

        /**
         *
         */
        int getSelectedFragment();

        Activity getActivity();
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }

        @Override
        public int getSelectedFragment(){
            return 0;
        }

        @Override
        public Activity getActivity() {
            return null;
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CallbackFragment() {
    }

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

}