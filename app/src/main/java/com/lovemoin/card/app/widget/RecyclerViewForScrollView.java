package com.lovemoin.card.app.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zzt on 15-8-17.
 */
public class RecyclerViewForScrollView extends RecyclerView {
    public RecyclerViewForScrollView(Context context) {
        super(context);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
