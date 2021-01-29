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

import com.example.bestfood.R;

import java.util.ArrayList;

public class CustomView extends androidx.appcompat.widget.AppCompatImageView {
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    public ArrayList<Integer> position_list_X;
    public ArrayList<Integer> position_list_Y;
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
        this.position_list_X = new ArrayList<Integer>();
        this.position_list_Y = new ArrayList<Integer>();
        this.mPaint = new Paint();
        this.mPaint.setColor(context.getColor(R.color.colorAccent));
        this.mPaint.setAlpha(70);
}


    @Override
    protected void onSizeChanged(int w, int h , int oldw, int oldh){
        //Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //canvas.setBitmap(img);
        //canvas.drawColor(Color.GRAY);

        //mBitmap = mBitmap;
        mCanvas = new Canvas();
    }
    @Override
    protected void onDraw(Canvas canvas){
        for (int i=0; i<position_list_X.size(); i++){
            canvas.drawCircle((int) position_list_X.get(i), (int) position_list_Y.get(i), 30, mPaint);}
    }

    /*
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

     */
}


