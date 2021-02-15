package com.example.bestfood.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.App;
import com.example.bestfood.R;
import com.example.bestfood.lib.MyLog;

public class SliderLayoutManager extends LinearLayoutManager {

    public RecyclerView recyclerView;
    LinearSnapHelper snapHelper;
    OnItemSelectedListener callback = null;
    private Context context;
    public int centerChildPosition = 2;

    public SliderLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        /*
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.getChildAt(1).get
            }
        });

         */
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //scaleDownView();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private void scaleDownView(){
        DisplayMetrics metrics = ((App) context.getApplicationContext()).getMetrics();
        float recyclerViewCenter = recyclerView.getHeight()/2.0f;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            final View child = recyclerView.getChildAt(i);
            TextView textView = recyclerView.getChildAt(i).findViewById(R.id.textItem);
            float childCenterX = (getDecoratedBottom(child) + getDecoratedTop(child))/2.0f;
            if(childCenterX>recyclerViewCenter - metrics.density*20 && childCenterX<recyclerViewCenter + metrics.density*20){
                textView.setTextColor(context.getColor(R.color.colorPrimaryDark));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.height = metrics.densityDpi*20/160;
                textView.setLayoutParams(params);
                centerChildPosition = recyclerView.getChildAdapterPosition(child);
                /*
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentR = new Intent();
                        intentR.putExtra("resultInt" , recyclerView.getChildAdapterPosition(child)); //사용자에게 입력받은값 넣기
                        ((Activity)context).setResult(Activity.RESULT_OK, intentR);
                        ((Activity)context).finish();
                    }
                });

                 */

            }else{
                textView.setTextColor(context.getColor(R.color.colorDarkGray));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.height = metrics.densityDpi*15/160;
                textView.setLayoutParams(params);
            }
        }

    }

    private void scaleDownView_2(){
        float recyclerViewCenter = recyclerView.getHeight()/2.0f;
        float minDistance = recyclerView.getHeight();
        int position = -1;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            //MyLog.d("here getTop " + i + " / " + getDecoratedTop(recyclerView.getChildAt(i)));
            //MyLog.d("here getBottom " +i + " / " + getDecoratedBottom(recyclerView.getChildAt(i)));
            View child = recyclerView.getChildAt(i);
            float childCenterX = (getDecoratedBottom(child) + getDecoratedTop(child))/2.0f;
            float  newDistance = Math.abs(childCenterX - recyclerViewCenter);
            if (newDistance < minDistance) {
                minDistance = newDistance;
                position = recyclerView.getChildLayoutPosition(child);
            }
        }

        //callback.onItemSelected(position);
        DisplayMetrics metrics = ((App) context.getApplicationContext()).getMetrics();

        /*
        if(position >= getChildCount()-1) {
            position = getChildCount() -1;
        }


        MyLog.d("here childviewCount" + getChildCount());
        MyLog.d("here center" + position);
        MyLog.d("here getTop" + getDecoratedTop(recyclerView.getChildAt(position)));
        MyLog.d("here getBottom" + getDecoratedBottom(recyclerView.getChildAt(position)));

        */
        MyLog.d("here getPosition" + position);
        MyLog.d("here getCenter" + recyclerViewCenter);
        MyLog.d("here getTop" + getDecoratedTop(recyclerView.getChildAt(position)));
        MyLog.d("here getBottom" + getDecoratedBottom(recyclerView.getChildAt(position)));



        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            TextView textView = recyclerView.getChildAt(i).findViewById(R.id.textItem);
            if(i == position){
                textView.setTextColor(context.getColor(R.color.colorPrimaryDark));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.height = metrics.densityDpi*20/160;
                textView.setLayoutParams(params);
            }else{
                textView.setTextColor(context.getColor(R.color.colorDarkGray));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.height = metrics.densityDpi*15/160;
                textView.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.callback = listener ;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(int position);
    }

}
