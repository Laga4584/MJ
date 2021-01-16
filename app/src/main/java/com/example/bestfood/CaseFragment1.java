package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bestfood.adapter.ViewpagerAdapter;
import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseFragment1 extends Fragment implements View.OnClickListener {
    public static final String INFO_ITEM = "INFO_ITEM";
    private final String TAG = this.getClass().getSimpleName();

    int iCount;
    public static ViewPager2 viewPager2;
    ViewpagerAdapter adapter;
    Boolean clicked;
    Button nextButton;
    CaseInfoItem infoItem;
    public static CaseInfoItem currentItem = null;

    public static CaseFragment1 newInstance(CaseInfoItem infoItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_ITEM, Parcels.wrap(infoItem));

        CaseFragment1 fragment = new CaseFragment1();
        fragment.setArguments(bundle);

        return fragment;
    }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      if (getArguments() != null) {
          infoItem = Parcels.unwrap(getArguments().getParcelable(INFO_ITEM));
          if (infoItem.seq != 0) {
              CaseActivity.infoItem = infoItem;
          }
          MyLog.d(TAG, "infoItem " + infoItem);
      }

      // 신규 케이스인지 기존 케이스인지
      if (infoItem.seq > 0) iCount = 2;
      else iCount = 1;

      ViewGroup rootView = (ViewGroup) inflater.inflate(
              R.layout.fragment_case_1, container, false);
      viewPager2 = rootView.findViewById(R.id.viewPager2);
      ViewpagerAdapter adapter = new ViewpagerAdapter(this, 1, iCount);
      /*
      clicked = ((CaseActivity)getActivity()).getClicked();
      MyLog.d("clicked check", clicked.toString());
      if (clicked) {
          adapter.setCount(10);
      }
    */

      adapter.setType(ViewpagerAdapter.TYPE_VERTICAL_VIEWPAGER);
      adapter.setInfoItem(infoItem);
      viewPager2.setAdapter(adapter);
      viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

      if (infoItem.seq > 0)viewPager2.setCurrentItem(1);
      else viewPager2.setCurrentItem(0);

      /*
      nextButton = rootView.findViewById(R.id.button);
      if (viewPager2.getCurrentItem()==0) {
          nextButton.setText("견적 요청하기");
      }else
          nextButton.setText("수정하기");

      nextButton.setOnClickListener(this);

       */

      return rootView;
  }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button) {
            /*
            if (viewPager2.getCurrentItem()==0) {
                viewPager2.setCurrentItem(1);
                nextButton.setText("수정하기");
                insertCaseInfo();
                CaseActivity.infoItem = infoItem;
                MyLog.d(TAG, "button clicked");
            }else{
                viewPager2.setCurrentItem(0);
                nextButton.setText("견적 요청하기");
            }

             */
        }
    }

    /**
     * 사용자가 입력한 정보를 서버에 저장한다.
     */
    private void insertCaseInfo() {

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertCaseInfo(currentItem);
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
                        currentItem.seq = seq;
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
