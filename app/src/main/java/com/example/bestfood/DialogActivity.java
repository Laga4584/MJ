package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.adapter.DialogListAdapter;
import com.example.bestfood.adapter.SliderLayoutManager;
import com.example.bestfood.lib.MyLog;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogActivity extends Activity {

    Context context;
    LinearLayout birth;
    ImageButton select, close;
    String[] items;
    String[] items_year;
    String[] items_month;
    String[] items_day;
    DialogListAdapter dialogListAdapter1, dialogListAdapter2, dialogListAdapter3;
    SliderLayoutManager layoutManager1, layoutManager2, layoutManager3;
    RecyclerView dialogView1, dialogView2,dialogView3;
    DialogListAdapter dialogListAdapter;
    SliderLayoutManager layoutManager;
    RecyclerView dialogView;
    int requestCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        requestCode = getIntent().getIntExtra("requestCode", 0);
        select = findViewById(R.id.button_select);
        close = findViewById(R.id.button_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(requestCode == 200){
            items_year = getIntent().getStringArrayExtra("itemList1");
            items_month = getIntent().getStringArrayExtra("itemList2");
            items_day = getIntent().getStringArrayExtra("itemList3");

            birth = findViewById(R.id.birth);
            birth.setVisibility(View.VISIBLE);
            dialogView1 = findViewById(R.id.list1);
            layoutManager1 = new SliderLayoutManager(this);
            dialogView1.setLayoutManager(layoutManager1);
            dialogListAdapter1 = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_year)));
            dialogView1.setAdapter(dialogListAdapter1);

            dialogView2 = findViewById(R.id.list2);
            layoutManager2 = new SliderLayoutManager(this);
            dialogView2.setLayoutManager(layoutManager2);
            dialogListAdapter2 = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_month)));
            dialogView2.setAdapter(dialogListAdapter2);

            dialogView3 = findViewById(R.id.list3);
            layoutManager3 = new SliderLayoutManager(this);
            dialogView3.setLayoutManager(layoutManager3);
            dialogListAdapter3 = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_day)));
            dialogView3.setAdapter(dialogListAdapter3);

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentR = new Intent();
                    intentR.putExtra("resultInt1" , layoutManager1.centerChildPosition); //사용자에게 입력받은값 넣기
                    intentR.putExtra("resultInt2" , layoutManager2.centerChildPosition);
                    intentR.putExtra("resultInt3" , layoutManager3.centerChildPosition);
                    setResult(Activity.RESULT_OK, intentR);
                    finish();
                }
            });

        }else{
            items = getIntent().getStringArrayExtra("itemList");
            dialogView = findViewById(R.id.list);
            layoutManager = new SliderLayoutManager(this);
            dialogView.setLayoutManager(layoutManager);
            dialogListAdapter = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items)));
            dialogView.setAdapter(dialogListAdapter);

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentR = new Intent();
                    intentR.putExtra("resultInt" , layoutManager.centerChildPosition); //사용자에게 입력받은값 넣기
                    setResult(Activity.RESULT_OK, intentR);
                    finish();
                }
            });
        }

    }
}
