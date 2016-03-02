package com.hilo.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hilo.MainActivity;
import com.hilo.R;
import com.hilo.TextActivity;
import com.hilo.interfaces.OnNoDoubleClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hilo on 16/2/19.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<String> data;
    private final static int[] colors = new int[]{R.color.design_bgcolor};

    public RecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public RecyclerAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void updateItems(boolean animated) {
        if (data == null) data = new ArrayList<>();
            data.clear();
        for (int i = 0; i < 20; i++) {
            data.add("点我跳转activity，动画效果 <" + i + ">");
        }
        if(animated) {
            notifyItemRangeChanged(0, data.size());
        } else {
            notifyDataSetChanged();
        }
    }


    public interface RecyclerViewOnItemClickListener {
        void onItemClick(View view, int position);

        void onItemlongClick(View view, int position);
    }

    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        recyclerViewOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        initEvents(holder);
        holder.bindData(data, mContext.getResources().getColor(colors[position % colors.length]), position);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0)
            return data.size();
        return 0;
    }

    private void initEvents(final ViewHolder holder) {
        if (recyclerViewOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    recyclerViewOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    recyclerViewOnItemClickListener.onItemlongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    public void add(String text, int position) {
        data.add(position, text);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder /*AnimateViewHolder*/ {
        private View itemView;
        @Bind(R.id.image)
        ImageView ivItemImage;
        @Bind(R.id.item_1)
        TextView tvContentInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            initEvents();
        }

        public void bindData(List<String> data, int bgColor, int position) {
            tvContentInfo.setText(data.get(position).toString());
            Picasso.with(mContext).load(R.mipmap.chip).into(ivItemImage);
            itemView.setBackgroundColor(bgColor);
        }

       /* @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(-itemView.getHeight() * 0.3f)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }*/

        private void initEvents() {
            tvContentInfo.setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                protected void onNoDoubleClickListener(View v) {
                    if (!mContext.getClass().getName().equals("com.hilo.TextActivity")) {
                        Intent intent = new Intent(mContext, TextActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((MainActivity) mContext).getWindow().setExitTransition(new TransitionSet());
                            mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((MainActivity) mContext)).toBundle());
                        } else {
                            mContext.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(mContext, "不可重复跳转当前Activity", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
