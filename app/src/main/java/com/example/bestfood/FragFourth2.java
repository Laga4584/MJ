package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class FragFourth2 extends Fragment {
    TextView stateText;
    TextView addressText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frame_4p_2, container, false);

        stateText = rootView.findViewById(R.id.text1);
        if (FragFourth.iCount < 2) stateText.setText("발송 대기 중입니다.");
        else stateText.setText("발송 되었습니다.");

        String address = CaseActivity.infoItem.address + " " + CaseActivity.infoItem.address_detail;
        addressText = rootView.findViewById(R.id.text2);
        addressText.setText(address);

        return rootView;
    }

}