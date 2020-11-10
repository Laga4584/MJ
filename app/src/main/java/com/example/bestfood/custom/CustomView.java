package com.example.bestfood.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CustomView extends androidx.appcompat.widget.AppCompatImageView {
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    int X;
    int Y;

    boolean touched = false;

    public CustomView(Context context) {
        super(context);

        init(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
}


    @Override
    protected void onSizeChanged(int w, int h , int oldw, int oldh){
        Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);
        canvas.drawColor(Color.GRAY);

        mBitmap = mBitmap;
        mCanvas = canvas;
    }
    @Override
    protected void onDraw(Canvas canvas){
        if (mBitmap != null){
            //canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        if (touched) {
            canvas.drawCircle(X, Y, 20, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Toast.makeText(super.getContext(), "MotionEvent.ACTION_DOWN", Toast.LENGTH_LONG).show();
            touched = true;
            X = (int) event.getX();
            Y = (int) event.getY();
            invalidate();
        }

        return true;
    }
}


