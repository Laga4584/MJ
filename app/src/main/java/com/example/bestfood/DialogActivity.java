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

public class DialogActivity extends Activity {

    Context context;
    ImageButton select, close;
    String[] items_1 = {"test1", "test2", "test3", "test4", "test5", "test6"};
    String[] items_2 = {"수선", "리폼", "염색", "클리닝", "직접 입력"};
    String[] items_3 = {"test1", "test2", "test3", "test4", "test5", "test6", "test7"};
    String[] items_4 = {"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8"};
    DialogListAdapter dialogListAdapter;
    SliderLayoutManager layoutManager;
    RecyclerView dialogView;
    int requestCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        requestCode = getIntent().getIntExtra("requestCode", 0);
        dialogView = findViewById(R.id.viewpager);
        layoutManager = new SliderLayoutManager(this);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //layoutManager
        //        .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        dialogView.setLayoutManager(layoutManager);
        switch (requestCode)
        {
            case 0 :
                dialogListAdapter = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_1)));
                break;
            case 1 :
                dialogListAdapter = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_2)));
                break;
            case 2 :
                dialogListAdapter = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_3)));
                break;
            case 3 :
                dialogListAdapter = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items_4)));
                break;
        }
        dialogView.setAdapter(dialogListAdapter);


        select = findViewById(R.id.button_select);
        close = findViewById(R.id.button_close);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentR = new Intent();
                intentR.putExtra("sendText" , layoutManager.centerChildPosition); //사용자에게 입력받은값 넣기
                setResult(Activity.RESULT_OK, intentR);
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
