package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;


public class FragSixth extends Fragment {

    TextView stateText;
    TextView addressText;
    Button prevButton;
    Button nextButton;

    String[] status_list = {"배송 중", "배송 완료"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frame_6p, container, false);
        ViewPager2 viewPager2 = rootView.findViewById(R.id.viewPager2);

        /*
        MyAdapter adapter = new MyAdapter(this, 6, 2);
        adapter.setType(MyAdapter.TYPE_VERTICAL_VIEWPAGER);
        viewPager2.setAdapter(adapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        */

        stateText = rootView.findViewById(R.id.text1);
        addressText = rootView.findViewById(R.id.text2);
        prevButton = rootView.findViewById(R.id.button1);
        nextButton = rootView.findViewById(R.id.button2);

        int state = Arrays.asList(status_list).indexOf(CaseActivity.infoItem.status2);
        String address = CaseActivity.infoItem.address + " " + CaseActivity.infoItem.address_detail;

        if (state == 0){
            stateText.setText("배송 중입니다.");
            addressText.setText(address);
            prevButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }else{
            stateText.setText("배송이 완료되었습니다.\n상품이 마음에 드셨다면 종료 버튼을 눌러주세요.");
            addressText.setVisibility(View.GONE);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CaseActivity.mPager.setCurrentItem(3);
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CaseActivity.iCount = 6;
                    ((CaseActivity)getActivity()).adapterSetup();
                    CaseActivity.mPager.setCurrentItem(6);
                }
            });
        }


        return rootView;
    }

}