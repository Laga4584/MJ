package com.example.bestfood;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;


public class CaseActivity extends FragmentActivity {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 7;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);



        //ViewPager2
        mPager = findViewById(R.id.viewpager);
        //Adapter
        pagerAdapter = new MyAdapter(this, num_page);
        ((MyAdapter) pagerAdapter).setType(MyAdapter.TYPE_HORIZONTAL_VIEWPAGER);
        mPager.setAdapter(pagerAdapter);
        //Indicator

        //ViewPager Setting
        /*
        if (((MyAdapter) pagerAdapter).getType() == MyAdapter.TYPE_HORIZONTAL_VIEWPAGER){
            mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        }
        else{
            mPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        }

         */
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                tabLayout.selectTab(tabLayout.getTabAt(position%num_page));
            }

        });


        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });

        settingTabLayout();

    }

    //tablayout - viewpager 연결
    public void settingTabLayout()
    {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        tabLayout.getTabAt(0).setIcon(android.R.drawable.ic_menu_add);
        tabLayout.getTabAt(1).setIcon(android.R.drawable.ic_menu_agenda);
        tabLayout.getTabAt(2).setIcon(android.R.drawable.ic_menu_crop);
        tabLayout.getTabAt(3).setIcon(android.R.drawable.ic_menu_send);
        tabLayout.getTabAt(4).setIcon(android.R.drawable.ic_menu_manage);
        tabLayout.getTabAt(5).setIcon(android.R.drawable.ic_menu_save);
        tabLayout.getTabAt(6).setIcon(android.R.drawable.ic_menu_edit);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                switch (pos)
                {
                    case 0 :
                        mPager.setCurrentItem(0);
                        break;
                    case 1 :
                        mPager.setCurrentItem(1);
                        break;
                    case 2 :
                        mPager.setCurrentItem(2);
                        break;
                    case 3 :
                        mPager.setCurrentItem(3);
                        break;
                    case 4 :
                        mPager.setCurrentItem(4);
                        break;
                    case 5 :
                        mPager.setCurrentItem(5);
                        break;
                    case 6 :
                        mPager.setCurrentItem(6);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}

