package com.example.bestfood.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bestfood.ImageFragment;


public class ImageAdapter extends FragmentStateAdapter {
    int count;
    int type;

    public ImageAdapter(FragmentActivity fa, int count, int type) {
        super(fa);
        this.count = count;
        this.type = type;
    }

    public ImageAdapter(@NonNull Fragment fragment, int count, int type) {
        super(fragment);
        this.count = count;
        this.type = type;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ImageFragment frag1 = new ImageFragment();
        frag1 = ImageFragment.newInstance(position, type);
        return frag1;

    }


    @Override
    public int getItemCount() {
        return this.count;
    }

}