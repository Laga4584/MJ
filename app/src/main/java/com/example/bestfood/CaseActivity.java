package com.example.bestfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaseActivity extends FragmentActivity {
    public static final String INFO_SEQ = "INFO_SEQ";
    private final String TAG = this.getClass().getSimpleName();
    public static CaseInfoItem currentItem = null;

    Context context;
    int userSeq;
    int caseInfoSeq;
    public static int iCount;

    public Boolean clicked = false;
    public static ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 7;

    private TabLayout tabLayout;
    public static CaseInfoItem infoItem;

    String[] status_list = {"견적 요청", "견적 확인", "결제", "발송", "수선", "배송", "후기"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        infoItem = new CaseInfoItem();
        userSeq = ((App)getApplication()).getMemberSeq();

        caseInfoSeq = getIntent().getIntExtra(INFO_SEQ, 0);




        //Fragment로 넘길 기본적인 정보를 저장한다.
        //infoItem = new CaseInfoItem();
        //infoItem.userSeq = userSeq;
        //MyLog.d(TAG, "infoItem " + infoItem.toString());

        //ViewPager2
        mPager = findViewById(R.id.viewpager);
        selectCaseInfo(caseInfoSeq);
        //adapterSetup();
        //settingTabLayout();
        //mPager.setCurrentItem(iCount);


    }



    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param caseInfoSeq 맛집 정보 시퀀스
     */
    private void selectCaseInfo(int caseInfoSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<CaseInfoItem> call = remoteService.selectCaseInfo(caseInfoSeq);

        call.enqueue(new Callback<CaseInfoItem>() {
            @Override
            public void onResponse(Call<CaseInfoItem> call, Response<CaseInfoItem> response) {
                CaseInfoItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    infoItem = item;
                    //infoItem.status = item.status;

                    MyLog.d(TAG, "here item " + item.toString());
                    iCount = Arrays.asList(status_list).indexOf(infoItem.status);

                    adapterSetup();
                    //setView();
                    //loadingText.setVisibility(View.GONE);
                } else {
                    //MyLog.d(TAG, "here item " + item.toString());
                    infoItem.userSeq = userSeq;
                    iCount = 0;
                    adapterSetup();
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<CaseInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }


    public void adapterSetup(){
        //Adapter
        pagerAdapter = new MyAdapter(this, 8, iCount);
        ((MyAdapter) pagerAdapter).setType(MyAdapter.TYPE_HORIZONTAL_VIEWPAGER);
        ((MyAdapter) pagerAdapter).setInfoItem(infoItem);
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
        MyLog.d(TAG, "here iCount  " + iCount);
        //mPager.setCurrentItem(0);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPager.setCurrentItem(iCount);
            }
        });
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
    /*
    public void notifyToSlideToRespectivePage(int pageNumber) {

        clicked = true;
        MyLog.d("clicked check case", clicked.toString());
        adapterSetup();
    }
    public boolean getClicked(){
        return clicked;
    }

     */
}

