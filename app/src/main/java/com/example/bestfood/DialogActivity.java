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
    String[] items;
    DialogListAdapter dialogListAdapter;
    SliderLayoutManager layoutManager;
    RecyclerView dialogView;
    int requestCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        requestCode = getIntent().getIntExtra("requestCode", 0);
        items = getIntent().getStringArrayExtra("itemList");
        dialogView = findViewById(R.id.viewpager);
        layoutManager = new SliderLayoutManager(this);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //layoutManager
        //        .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        dialogView.setLayoutManager(layoutManager);
        dialogListAdapter = new DialogListAdapter(this, R.layout.row_dialog_list,  new ArrayList<String>(Arrays.asList(items)));
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
