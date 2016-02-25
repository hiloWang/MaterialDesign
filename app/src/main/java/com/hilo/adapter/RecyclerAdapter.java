package com.hilo.adapter;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilo.MainActivity;
import com.hilo.R;
import com.hilo.TextActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hilo on 16/2/19.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mData;
    private final static int[] colors = new int[]{R.color.design_bgcolor};

    public RecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public RecyclerAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        mData = data;
    }

    public void setData(List<String> data) {
        mData = data;
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
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, int position) {
        if(recyclerViewOnItemClickListener != null) {
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
        holder.bindData(mData, mContext.getResources().getColor(colors[position % colors.length]), position);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        if (mData != null && mData.size() > 0)
            return mData.size();
        return 0;
    }

    public void add(String text, int position) {
        mData.add(position, text);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder /*AnimateViewHolder*/ {
        private List<String> data;
        private int bgColor, position;
        private View itemView;
        private TextView mTv1;
        private ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mTv1 = (TextView) itemView.findViewById(R.id.item_1);
            mImage = (ImageView) itemView.findViewById(R.id.image);

            mTv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TextActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((MainActivity) mContext).getWindow().setExitTransition(new TransitionSet());
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((MainActivity) mContext)).toBundle());
                    } else {
                        mContext.startActivity(intent);
                    }
                }
            });

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animator animator;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        animator = ViewAnimationUtils.createCircularReveal(
                                mImage,
                                0,
                                0,
                                0,
                                (float) Math.hypot(mImage.getWidth(), mImage.getHeight()));
                        animator.setInterpolator(new AccelerateInterpolator());
                        animator.setDuration(500);
                        animator.start();
                    }
                }
            });

        }

        public void bindData(List<String> data, int bgColor, int position) {
            this.data = data;
            this.bgColor = bgColor;
            this.position = position;
            mTv1.setText(data.get(position).toString());
            Picasso.with(mContext).load(R.mipmap.chip).into(mImage);
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
    }
}
