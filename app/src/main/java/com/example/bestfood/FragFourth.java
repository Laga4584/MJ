package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragFourth extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    Button nextButton;
    CaseInfoItem infoItem;
    ViewPager2 viewPager2;
    public static int iCount;

    String[] status_list = {"발송 요청", "발송 대기", "발송 완료"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frame_4p, container, false);

        infoItem = new CaseInfoItem();
        infoItem = CaseActivity.infoItem;

        iCount = Arrays.asList(status_list).indexOf(infoItem.status2);
        MyLog.d(TAG, "here iCount " + iCount);

        viewPager2 = rootView.findViewById(R.id.viewPager2);

        MyAdapter adapter = new MyAdapter(this, 4, iCount);
        adapter.setType(MyAdapter.TYPE_VERTICAL_VIEWPAGER);
        viewPager2.setAdapter(adapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        MyLog.d(TAG, "here Case iCount " + CaseActivity.iCount);
        if (CaseActivity.iCount < 4) {
            if (iCount < 1) viewPager2.setCurrentItem(0);
            else viewPager2.setCurrentItem(1);
        }else viewPager2.setCurrentItem(0);

        // if (iCount ==2) 버튼 없애기

        nextButton = rootView.findViewById(R.id.button);
        if (viewPager2.getCurrentItem()==0) {
            nextButton.setText("발송 신청하기");
        }else
            nextButton.setText("수정하기");
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.button) {
                    if (viewPager2.getCurrentItem()==0) {
                        viewPager2.setCurrentItem(1);
                        nextButton.setText("수정하기");
                        insertAddressInfo();
                    }else{
                        viewPager2.setCurrentItem(0);
                        nextButton.setText("발송 신청하기");

                    }
                }
            }
        });

        return rootView;
    }

    private void insertAddressInfo(){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertCaseInfo(infoItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();

                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }

                    if (seq == 0) {
                        //등록 실패
                    } else {
                        infoItem.seq = seq;
                        CaseActivity.infoItem.address = infoItem.address;
                        CaseActivity.infoItem.address_detail = infoItem.address_detail;
                        //goNextPage();
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

}