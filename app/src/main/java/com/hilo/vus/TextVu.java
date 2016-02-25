package com.hilo.vus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilo.R;
import com.hilo.interfaces.Vu;

/**
 * Created by hilo on 16/2/25.
 */
public class TextVu implements Vu {

    View rootView;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_text, null);
    }

    @Override
    public View getView() {
        return rootView;
    }
}
