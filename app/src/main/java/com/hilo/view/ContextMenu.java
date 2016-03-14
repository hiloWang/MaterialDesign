package com.hilo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hilo.R;

/**
 * Created by hilo on 16/3/3.
 */
public class ContextMenu extends LinearLayout {

    public ContextMenu(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_context_menu, this, true);
    }
}
