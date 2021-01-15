package com.example.bestfood.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context) { super(context); }
    public SquareFrameLayout(Context context, AttributeSet attrs) { super(context, attrs); }
    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); } }
