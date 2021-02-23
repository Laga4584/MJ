package com.example.bestfood;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.bestfood.item.CaseItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaseActivity extends FragmentActivity {
    public static final String INFO_SEQ = "INFO_SEQ";
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    int userSeq;
    int caseSeq;
    public static int iCount;

    ImageButton backButton;
    private TabLayout tabLayout;
    public static CaseItem caseItem;

    CaseFragment1 fragment1;
    CaseFragment2 fragment2;
    CaseFragment3 fragment3;
    CaseFragment4 fragment4;
    CaseFragment5 fragment5;
    CaseFragment6 fragment6;

    String[] status_list = {"요청", "확인", "결제", "발송", "수선", "후기", "완료"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_case);

        context = this;
        caseItem = new CaseItem();
        caseItem.seq = 0;
        userSeq = ((App)getApplication()).getUserSeq();
        caseSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        MyLog.d("here caseSeq "+caseSeq);

        selectCaseInfo(caseSeq);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * 서버에서 케이스 정보를 조회한다.
     * @param caseSeq 케이스 정보 시퀀스
     */
    private void selectCaseInfo(int caseSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<CaseItem> call = remoteService.selectCaseInfo(caseSeq);

        call.enqueue(new Callback<CaseItem>() {
            @Override
            public void onResponse(Call<CaseItem> call, Response<CaseItem> response) {
                CaseItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    caseItem = item;
                    iCount = Arrays.asList(status_list).indexOf(caseItem.status);
                    settingTabLayout();
                    //loadingText.setVisibility(View.GONE);
                } else {
                    caseItem.userSeq = userSeq;
                    iCount = 0;
                    settingTabLayout();
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<CaseItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    //tablayout - viewpager 연결
    public void settingTabLayout()
    {
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                switch (pos)
                {
                    case 0 :
                        break;
                    case 1 :
                        break;
                    case 2 :
                        break;
                    case 3 :
                        break;
                    case 4 :
                        break;
                    case 5 :
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
        tabLayout.selectTab(tabLayout.getTabAt(iCount));
        setFragment();
    }

    public void setFragment(){
        switch(iCount){
            case 0 :
                fragment1 = CaseFragment1.newInstance(caseItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
                break;
            case 1 :
                fragment2 = CaseFragment2.newInstance(caseItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                break;
            case 2 :
                fragment3 = CaseFragment3.newInstance(caseItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                break;
            case 3 :
                fragment4 = CaseFragment4.newInstance(caseItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment4).commit();
                break;
            case 4 :
                fragment5 = CaseFragment5.newInstance(caseItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment5).commit();
                break;
            case 5 :
                fragment6 = CaseFragment6.newInstance(caseItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment6).commit();
                break;
        }
    }


    public void replaceFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        switch(position){
            case 1 :
                fragment2 = CaseFragment2.newInstance(caseItem);
                fragmentTransaction.replace(R.id.container, fragment2).commit();
                tabLayout.selectTab(tabLayout.getTabAt(1));
                break;
            case 2:
                fragment3 = CaseFragment3.newInstance(caseItem);
                fragmentTransaction.replace(R.id.container, fragment3).commit();
                tabLayout.selectTab(tabLayout.getTabAt(2));
                break;
            case 3:
                fragment4 = CaseFragment4.newInstance(caseItem);
                fragmentTransaction.replace(R.id.container, fragment4).commit();
                tabLayout.selectTab(tabLayout.getTabAt(3));
                break;
            case 4:
                fragment5 = CaseFragment5.newInstance(caseItem);
                fragmentTransaction.replace(R.id.container, fragment5).commit();
                tabLayout.selectTab(tabLayout.getTabAt(4));
                break;
            case 5:
                fragment6 = CaseFragment6.newInstance(caseItem);
                fragmentTransaction.replace(R.id.container, fragment6).commit();
                tabLayout.selectTab(tabLayout.getTabAt(5));
                break;
            case 6:
                break;
        }
    }
}

