package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaseFragment1_2 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    Context context;

    CaseInfoItem infoItem;
    TextView serviceText;
    TextView productText;
    TextView descriptionText;
    Button cameraButton;
    Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_case_1_2, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = this.getActivity();

        cameraButton = view.findViewById(R.id.camera_button);
        nextButton = view.findViewById(R.id.button);
        serviceText = view.findViewById(R.id.textview1);
        productText = view.findViewById(R.id.textview2);
        descriptionText = view.findViewById(R.id.textview4);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goDetailActivity(context);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaseFragment1.viewPager2.setCurrentItem(0);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        infoItem = new CaseInfoItem();

        if (CaseActivity.infoItem.seq > 0){
            infoItem = CaseActivity.infoItem;
            //selectImageInfo(CaseActivity.infoItem.seq);
        }else{
            MyLog.d(TAG, "currentItem " + CaseFragment1.currentItem.toString());
            selectCaseInfo(CaseFragment1.currentItem.seq);
            //selectImageInfo(FragFirst.currentItem.seq);
        }

        if (!StringLib.getInstance().isBlank(infoItem.service)) {
            serviceText.setText(infoItem.service);
        }
        if (!StringLib.getInstance().isBlank(infoItem.product)) {
            productText.setText(infoItem.product);
        }
        if (!StringLib.getInstance().isBlank(infoItem.product)) {
            descriptionText.setText(infoItem.description);
        }

    }

    public void selectCaseInfo(int seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<CaseInfoItem> call = remoteService.selectCaseInfo(seq);
        call.enqueue(new Callback<CaseInfoItem>() {
            @Override
            public void onResponse(Call<CaseInfoItem> call, Response<CaseInfoItem> response) {
                CaseInfoItem item = response.body();

                if (response.isSuccessful()) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    infoItem = item;
                } else {
                    MyLog.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<CaseInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

}