package com.hilo.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hilo on 15/8/25.
 * <p>
 * Drscription:
 */
public interface Vu {
    void init(LayoutInflater inflater, ViewGroup container, Context context);
    View getView();
}
