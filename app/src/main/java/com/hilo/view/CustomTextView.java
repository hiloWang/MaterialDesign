package com.hilo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.hilo.R;

/**
 * Created by hilo on 16/2/22.
 */
public class CustomTextView extends TextView {

    private static final String tag = "hilo";

    private Context context;
    private boolean onceSetup;
    private int customTextViewWidthAttr;
    private int customTextViewWidthMove;
    private float preX;

    // 动态new
    public CustomTextView(Context context) {
        this(context, null);
    }

    // xml
    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // 自定义属性
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);
        int indexCount = ta.getIndexCount();
        for(int i=0; i<indexCount; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                // case的attr，必须在xml预定义才能case到.
                case R.styleable.CustomTextView_ctv_textSize:
                    // 动态改变xml预先定义的字体大小，是不可以的，系统会优先xml定义。所以得到的textSize也是xml文件里定义的
                    int textSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()));
                    this.setTextSize(textSize);
                    break;
                case R.styleable.CustomTextView_ctv_textViewWidth: // format="dimension"(sp,px,dip类型参数)
                    // 参数1:索引
                    // 参数2:字段属性值 这里将style里定义的值转px
                    customTextViewWidthAttr =  ta.getDimensionPixelSize(attr, 0);
                    break;
            }
        }
    }

    /**
     * 设置父View,子View的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 只初始化一次
        if(!onceSetup) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            // 屏幕的宽度 - view的宽度 = 当前控件可以移动位置的宽度
            customTextViewWidthMove = screenWidth - customTextViewWidthAttr;
            onceSetup = true;
            Log.i(tag, "当前屏幕宽度：" + screenWidth + "\n" + "当前控件宽度：" + customTextViewWidthAttr + "\n" + "当前控件在当前屏幕可移动宽度：" + customTextViewWidthMove);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置偏移量
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 如果当前子View,发生了偏移,那么scrollTo移动到该位置上.在MotionEvent里面做偏移控制
        if(changed)
            this.scrollTo(customTextViewWidthMove, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if(scrollX >= customTextViewWidthMove / 2) {
                    scrollTo(customTextViewWidthMove, 0);
                } else scrollTo(0,0);
                // 消费此事件
                return true;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

  /*  private void setTypeface(int style) {
        String file;
        if(style == Typeface.BOLD) {
            file = "fonts/xx.ttf";
        } else {
            file = "fonts/xx.ttf";
        }
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), file));
    }*/
}
