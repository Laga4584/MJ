package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bestfood.lib.RemoteLib;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;


public class CaseFragment3 extends Fragment {
    Context context;
    Button payButton;
    Button nextButton;

    KakaoPay kakaoPay;
    KakaoPayReadyVO kakaoPayReadyVO;

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = this.getActivity();

        payButton = view.findViewById(R.id.payment);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoPayReadyVO = new KakaoPayReadyVO();
                kakaoPay = new KakaoPay();
                try {
                    kakaoPayReadyVO = kakaoPay.kakaoPayReady();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    URL redirect = new URL(kakaoPayReadyVO.getNext_redirect_app_url());
                    URLConnection redirectConnection = redirect.openConnection();
                    kakaoPay.openConnectionCheckRedirects(redirectConnection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*try {
                    kakaoPay.kakaoPayApprove();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_case_3, container, false);

        nextButton = rootView.findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaseActivity.iCount = 3;
                (Objects.requireNonNull((CaseActivity)getActivity())).adapterSetup();
                RemoteLib.getInstance().updateCaseStatus(3, 0);
            }
        });

        return rootView;
    }

}