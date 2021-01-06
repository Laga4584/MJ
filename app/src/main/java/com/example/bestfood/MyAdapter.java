package com.example.bestfood;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bestfood.FragFirst1;
import com.example.bestfood.item.CaseInfoItem;


public class MyAdapter extends FragmentStateAdapter {

    public int mCount;
    public int iCount;
    final static int TYPE_VERTICAL_VIEWPAGER = 1001;
    final static int TYPE_HORIZONTAL_VIEWPAGER = 1002;
    private int type;
    CaseInfoItem infoItem;

    public MyAdapter(FragmentActivity fa, int count, int count2) {
        super(fa);
        mCount = count;
        iCount = count2;
    }
    public MyAdapter(@NonNull Fragment fragment, int count, int count2) {
        super(fragment);
        mCount = count;
        iCount = count2;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(type == TYPE_HORIZONTAL_VIEWPAGER){
            if(position==0) {

                FragFirst frag1 = new FragFirst();
                frag1 = FragFirst.newInstance(infoItem);
                return frag1;
            }
            else if(position==1) return new FragSecond();
            else if(position==2) return new FragThird();
            else if(position==3) return new FragFourth();
            else if(position==4) return new FragFifth();
            else if(position==5) return new FragSixth();
            else return new FragSeventh();

        } else {
            if (mCount == 1) {
                if (iCount<2) {
                    if (position == 0) {
                        FragFirst1 frag = new FragFirst1();
                        frag = FragFirst1.newInstance(infoItem);
                        return frag;
                    } else return new FragFirst2();
                }else return new FragFirst2();
            } else if (mCount == 4) {
                if (iCount<2) {
                    if (position == 0) return new FragFourth1();
                    else return new FragFourth2();
                }else return new FragFourth2();
                /*
            } else if (mCount == 6) {
                if (position == 0) return new FragSixth1();
                else return new FragSixth2();

                 */
            } else if (mCount == 10) {return new FragFirst2();
            } else return new FragSixth2();
        }

    }
    public int getType(){
        return this.type;
    }

    public void setType(int type){
        this.type = type;
    }
    public void setCount(int count){
        this.mCount = count;
    }
    public void setInfoItem(CaseInfoItem infoItem){
        this.infoItem = infoItem;
    }

    @Override
    public int getItemCount() {
        if(type == TYPE_HORIZONTAL_VIEWPAGER){
            return iCount+1;
        } else {
            if (iCount<1) return iCount+2;
            else if (iCount==1) return 2;
            else return 1;
        }
    }

}