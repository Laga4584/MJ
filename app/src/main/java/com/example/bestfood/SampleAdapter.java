package com.example.bestfood;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bestfood.FragFirst1;
import com.example.bestfood.item.CaseInfoItem;


public class SampleAdapter extends FragmentStateAdapter {


    public SampleAdapter(FragmentActivity fa) {
        super(fa);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        FragSample frag1 = new FragSample();
        frag1 = FragSample.newInstance(position);
        return frag1;
    }


    @Override
    public int getItemCount() {
        return 2;
    }

}