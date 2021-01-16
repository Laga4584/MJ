package com.example.bestfood.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bestfood.CaseFragment5;
import com.example.bestfood.CaseFragment1;
import com.example.bestfood.CaseFragment1_1;
import com.example.bestfood.CaseFragment1_2;
import com.example.bestfood.CaseFragment4;
import com.example.bestfood.CaseFragment4_1;
import com.example.bestfood.CaseFragment4_2;
import com.example.bestfood.CaseFragment2;
import com.example.bestfood.CaseFragment7;
import com.example.bestfood.CaseFragment6;
import com.example.bestfood.CaseFragment6_2;
import com.example.bestfood.CaseFragment3;
import com.example.bestfood.item.CaseInfoItem;


public class ViewpagerAdapter extends FragmentStateAdapter {

    public int mCount;
    public int iCount;
    public static int TYPE_VERTICAL_VIEWPAGER = 1001;
    public static int TYPE_HORIZONTAL_VIEWPAGER = 1002;
    private int type;
    CaseInfoItem infoItem;

    public ViewpagerAdapter(FragmentActivity fa, int count, int count2) {
        super(fa);
        mCount = count;
        iCount = count2;
    }
    public ViewpagerAdapter(@NonNull Fragment fragment, int count, int count2) {
        super(fragment);
        mCount = count;
        iCount = count2;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(type == TYPE_HORIZONTAL_VIEWPAGER){
            if(position==0) {

                CaseFragment1 frag1 = new CaseFragment1();
                frag1 = CaseFragment1.newInstance(infoItem);
                return frag1;
            }
            else if(position==1) return new CaseFragment2();
            else if(position==2) return new CaseFragment3();
            else if(position==3) return new CaseFragment4();
            else if(position==4) return new CaseFragment5();
            else if(position==5) return new CaseFragment6();
            else return new CaseFragment7();

        } else {
            if (mCount == 1) {
                if (iCount<2) {
                    if (position == 0) {
                        CaseFragment1_1 frag = new CaseFragment1_1();
                        frag = CaseFragment1_1.newInstance(infoItem);
                        return frag;
                    } else return new CaseFragment1_2();
                }else return new CaseFragment1_2();
            } else if (mCount == 4) {
                if (iCount<2) {
                    if (position == 0) return new CaseFragment4_1();
                    else return new CaseFragment4_2();
                }else return new CaseFragment4_2();
                /*
            } else if (mCount == 6) {
                if (position == 0) return new FragSixth1();
                else return new FragSixth2();

                 */
            } else if (mCount == 10) {return new CaseFragment1_2();
            } else return new CaseFragment6_2();
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