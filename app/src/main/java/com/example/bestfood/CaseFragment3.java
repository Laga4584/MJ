package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import org.w3c.dom.Text;


public class CaseFragment3 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String CASE_ITEM = "CASE_ITEM";
    CaseItem caseItem;
    TextView next;

    TextView method;
    TextView name;
    TextView amount;

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

        next = rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoteLib.getInstance().updateCaseStatus(3, 0);
                ((CaseActivity) getActivity()).replaceFragment(2);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        method = (TextView) view.findViewById(R.id.kakaopay_method);
        name = (TextView) view.findViewById(R.id.kakaopay_itemname);
        amount = (TextView) view.findViewById(R.id.kakaopay_amount);

        /*String strmethod = ((KakaoPayActivity)KakaoPayActivity.context_kakaopay).pay_method;
        String strpurchase = ((KakaoPayActivity)KakaoPayActivity.context_kakaopay).pay_purchasecorp;
        String stramount = ((KakaoPayActivity)KakaoPayActivity.context_kakaopay).pay_amount;
        String strinterest = ((KakaoPayActivity)KakaoPayActivity.context_kakaopay).pay_interest;
        String strmonth = ((KakaoPayActivity)KakaoPayActivity.context_kakaopay).pay_month;
        String strname = ((KakaoPayActivity)KakaoPayActivity.context_kakaopay).pay_name;

        if (strmethod == "CARD") {
            method.setText("신용카드 " + strpurchase);
            if (strinterest == "Y") {
                amount.setText(stramount + "/ " + strmonth + " 무이자");
            }
            else if (strinterest == "N") {
                amount.setText(stramount + "/ " + strmonth + " 할부");
            }
        } else if (strmethod == "MONEY") {
            method.setText("계좌이체");
        }

        name.setText(strname);*/
    }
}