package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class FragFirst extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      ViewGroup rootView = (ViewGroup) inflater.inflate(
              R.layout.frame_1p, container, false);
      ViewPager2 viewPager2 = rootView.findViewById(R.id.viewPager2);

      MyAdapter adapter = new MyAdapter(this, 2);
      adapter.setType(MyAdapter.TYPE_VERTICAL_VIEWPAGER);
      viewPager2.setAdapter(adapter);
      viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

      return rootView;
  }

}
