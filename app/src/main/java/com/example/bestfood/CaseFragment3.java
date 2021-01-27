package com.example.bestfood;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bestfood.lib.RemoteLib;

import java.util.Objects;


public class CaseFragment3 extends Fragment {
    Context context;

    Button payButton;
    Button nextButton;

    /*public static CaseFragment3_1 newInstance(CaseInfoItem infoItem) {
        Bundle bundle = new Bundle();
    }*/

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
                RemoteLib.getInstance().updateCaseStatus(3, 1);
            }
        });

        return rootView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        payButton = view.findViewById(R.id.payment);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), KakaoPayActivity.class));
            }
        });
    }

}