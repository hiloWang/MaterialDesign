package com.hilo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilo.R;

import java.util.List;

/**
 * Created by hilo on 16/2/29.
 */
public class PopupAdapter extends BaseAdapter {

    private Context mContext;
    private static final int[] popup_icon_image = new int[]{R.mipmap.popup_icon_1, R.mipmap.popup_icon_2, R.mipmap.popup_icon_3, R.mipmap.popup_icon_4};
    private static final String[] popup_content_text = new String[]{"测试1","测试2","测试3","测试4"};

    public PopupAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return popup_content_text.length;
    }

    @Override
    public Object getItem(int position) {
        return popup_content_text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_actioncreate, null);
        ImageView popupImageView = (ImageView) convertView.findViewById(R.id.popup_item_imageview);
        TextView popupTextView = (TextView) convertView.findViewById(R.id.popup_item_textview);
        popupImageView.setImageResource(popup_icon_image[position]);
        popupTextView.setText(popup_content_text[position]);
        return convertView;
    }



}
