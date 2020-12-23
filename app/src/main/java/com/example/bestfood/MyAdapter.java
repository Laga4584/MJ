package com.example.bestfood;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class MyAdapter extends FragmentStateAdapter {

    public int mCount;
    final static int TYPE_VERTICAL_VIEWPAGER = 1001;
    final static int TYPE_HORIZONTAL_VIEWPAGER = 1002;
    private int type;

    public MyAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    public MyAdapter(@NonNull Fragment fragment, int count) {
        super(fragment);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(type == TYPE_HORIZONTAL_VIEWPAGER){
            if(position==0) return new FragFirst();
            else if(position==1) return new FragSecond();
            else if(position==2) return new FragThird();
            else if(position==3) return new FragFourth();
            else if(position==4) return new FragFifth();
            else if(position==5) return new FragSixth();
            else return new FragSeventh();

        } else{
            return new FragmentEighth();
        }


    }
    public int getType(){
        return this.type;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public int getItemCount() {
        if(type == TYPE_HORIZONTAL_VIEWPAGER){
            return 7;
        } else {
            return 2;
        }
    }

    public int getRealPosition(int position) { return position % mCount; }


}