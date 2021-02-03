package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.adapter.DialogListAdapter;
import com.example.bestfood.adapter.SliderLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterActivity extends Activity {

    Context context;
    ImageButton select, refresh;
    int requestCode;
    TextView tag1_1, tag1_2, tag1_3, tag1_4, tag2_1, tag2_2, tag2_3, tag2_4, tag3_1, tag3_2, tag3_3, tag3_4, tag3_5, tag3_6, tag4_1, tag4_2, tag4_3, tag4_4;
    RadioView radioView1, radioView2, radioView3, radioView4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        requestCode = getIntent().getIntExtra("requestCode", 0);

        ArrayList<TextView> viewList1 = new ArrayList<TextView>();
        ArrayList<TextView> viewList2 = new ArrayList<TextView>();
        ArrayList<TextView> viewList3 = new ArrayList<TextView>();
        ArrayList<TextView> viewList4 = new ArrayList<TextView>();
        tag1_1 = findViewById(R.id.tag1_1);
        tag1_2 = findViewById(R.id.tag1_2);
        tag1_3 = findViewById(R.id.tag1_3);
        tag1_4 = findViewById(R.id.tag1_4);
        tag2_1 = findViewById(R.id.tag2_1);
        tag2_2 = findViewById(R.id.tag2_2);
        tag2_3 = findViewById(R.id.tag2_3);
        tag2_4 = findViewById(R.id.tag2_4);
        tag3_1 = findViewById(R.id.tag3_1);
        tag3_2 = findViewById(R.id.tag3_2);
        tag3_3 = findViewById(R.id.tag3_3);
        tag3_4 = findViewById(R.id.tag3_4);
        tag3_5 = findViewById(R.id.tag3_5);
        tag3_6 = findViewById(R.id.tag3_6);
        tag4_1 = findViewById(R.id.tag4_1);
        tag4_2 = findViewById(R.id.tag4_2);
        tag4_3 = findViewById(R.id.tag4_3);
        tag4_4 = findViewById(R.id.tag4_4);
        viewList1.add(tag1_1);
        viewList1.add(tag1_2);
        viewList1.add(tag1_3);

        if (requestCode == 200) tag1_4.setVisibility(View.GONE);
        else viewList1.add(tag1_4);

        viewList2.add(tag2_1);
        viewList2.add(tag2_2);
        viewList2.add(tag2_3);
        viewList2.add(tag2_4);
        viewList3.add(tag3_1);
        viewList3.add(tag3_2);
        viewList3.add(tag3_3);
        viewList3.add(tag3_4);
        viewList3.add(tag3_5);
        viewList3.add(tag3_6);
        viewList4.add(tag4_1);
        viewList4.add(tag4_2);
        viewList4.add(tag4_3);
        viewList4.add(tag4_4);

        radioView1 = new RadioView(viewList1);
        radioView2 = new RadioView(viewList2);
        radioView3 = new RadioView(viewList3);
        radioView4 = new RadioView(viewList4);

        select = findViewById(R.id.button_select);
        refresh = findViewById(R.id.button_refresh);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentR = new Intent();
                int[] selectedList = {radioView1.getSelected(), radioView2.getSelected(), radioView3.getSelected(), radioView4.getSelected()};
                intentR.putExtra("selectedList" , selectedList); //사용자에게 입력받은값 넣기
                setResult(Activity.RESULT_OK, intentR);
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioView1.setSelected(-1);
                radioView2.setSelected(-1);
                radioView3.setSelected(-1);
                radioView4.setSelected(-1);
            }
        });
    }

    private static class RadioView{
        private ArrayList<TextView> viewList;
        private int selected = -1;
        public RadioView(ArrayList<TextView> viewList){
            this.viewList = viewList;
            for (int i=0; i<viewList.size(); i++){
                final int position = i;
                viewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selected == position) selected = -1;
                        else selected = position;
                        selectView();
                    }
                });
            }
        }

        private void selectView(){
            for (int i=0; i<viewList.size(); i++){
                if (i==selected){
                    viewList.get(i).setSelected(true);
                }else{
                    viewList.get(i).setSelected(false);
                }
            }
        }

        public void setSelected(int selected) {
            this.selected = selected;
            selectView();
        }

        public int getSelected(){
            return selected;
        }

    }
}
