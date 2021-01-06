package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bestfood.lib.RemoteLib;

import java.util.Objects;


public class FragSecond extends Fragment {
    TextView methodText;
    TextView priceText;
    TextView timeText;
    Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frame_2p, container, false);

        methodText = rootView.findViewById(R.id.description1);
        priceText = rootView.findViewById(R.id.description2);
        timeText = rootView.findViewById(R.id.description3);
        nextButton = rootView.findViewById(R.id.button);

        methodText.setText(CaseActivity.infoItem.method);
        priceText.setText(CaseActivity.infoItem.price);
        timeText.setText(CaseActivity.infoItem.time);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaseActivity.iCount = 2;
                (Objects.requireNonNull((CaseActivity)getActivity())).adapterSetup();
                RemoteLib.getInstance().updateCaseStatus(2, 0);
            }
        });

        return rootView;
    }
}
