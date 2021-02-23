package com.example.bestfood;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseItem;
import com.example.bestfood.lib.RemoteLib;

import org.parceler.Parcels;


public class CaseFragment3 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String CASE_ITEM = "CASE_ITEM";
    CaseItem caseItem;
    TextView methodText, titleText, amountText, nextButton;

    public static CaseFragment3 newInstance(CaseItem caseItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CASE_ITEM, Parcels.wrap(caseItem));

        CaseFragment3 fragment = new CaseFragment3();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            caseItem = Parcels.unwrap(getArguments().getParcelable(CASE_ITEM));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_case_3, container, false);

        nextButton = rootView.findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 3, 0, caseStatusHandler);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        methodText = view.findViewById(R.id.text_method);
        titleText = view.findViewById(R.id.text_title);
        amountText = view.findViewById(R.id.text_amount);

        String methodString = caseItem.payMethod + " " + caseItem.payCorp;
        methodText.setText(methodString);
        String titleString = caseItem.repairerName + " 명장 [" + caseItem.brand + "] " + caseItem.product + " " + caseItem.service + " 외 " + caseItem.dotCount + " 건";
        titleText.setText(titleString);
        String amountString = caseItem.payAmount + "원 / " + caseItem.payInterest;
        amountText.setText(amountString);
    }

    Handler caseStatusHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CaseActivity.caseItem.status = RemoteLib.getInstance().status_list_1[msg.arg1];
            CaseActivity.caseItem.status2 = RemoteLib.getInstance().status_list_2[msg.arg2];
            ((CaseActivity) getActivity()).replaceFragment(msg.arg1);
        }
    };
}