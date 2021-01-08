package com.example.bestfood;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bestfood.FragFirst1;
import com.example.bestfood.item.CaseInfoItem;


public class SampleAdapter extends FragmentStateAdapter {
    int count;
    int type;

    public SampleAdapter(FragmentActivity fa, int count, int type) {
        super(fa);
        this.count = count;
        this.type = type;
    }

    public SampleAdapter(@NonNull Fragment fragment, int count, int type) {
        super(fragment);
        this.count = count;
        this.type = type;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        FragSample frag1 = new FragSample();
        frag1 = FragSample.newInstance(position, type);
        return frag1;

    }


    @Override
    public int getItemCount() {
        return this.count;
    }

}