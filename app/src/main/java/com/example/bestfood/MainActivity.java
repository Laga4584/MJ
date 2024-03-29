package com.example.bestfood;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageButton repairerButton;
    ImageButton sampleButton;
    ImageButton noticeButton;
    ImageButton mypageButton;

    RepairerListFragment fragment1;
    SampleListFragment fragment2;
    NoticeFragment fragment3;
    MyPageFragment fragment4;

    int colorAccent, colorPrimary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCase();
            }
        });

        repairerButton = findViewById(R.id.button_repairer);
        sampleButton = findViewById(R.id.button_sample);
        noticeButton = findViewById(R.id.button_notice);
        mypageButton = findViewById(R.id.button_mypage);

        fragment1 = new RepairerListFragment();
        fragment2 = new SampleListFragment();
        fragment3 = new NoticeFragment();
        fragment4 = new MyPageFragment();

        colorAccent = getResources().getColor(R.color.colorAccent, null);
        colorPrimary = getResources().getColor(R.color.colorPrimaryDark, null);
        repairerButton.getDrawable().setColorFilter(colorAccent, PorterDuff.Mode.SRC_ATOP);
        repairerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
                repairerButton.getDrawable().setColorFilter(colorAccent, PorterDuff.Mode.SRC_ATOP);
                sampleButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                noticeButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                mypageButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
            }
        });
        sampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                repairerButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                sampleButton.getDrawable().setColorFilter(colorAccent, PorterDuff.Mode.SRC_ATOP);
                noticeButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                mypageButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
            }
        });
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                repairerButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                sampleButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                noticeButton.getDrawable().setColorFilter(colorAccent, PorterDuff.Mode.SRC_ATOP);
                mypageButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
            }
        });
        mypageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment4).commit();
                repairerButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                sampleButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                noticeButton.getDrawable().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
                mypageButton.getDrawable().setColorFilter(colorAccent, PorterDuff.Mode.SRC_ATOP);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
    }

    /**
     * CaseActivity를 실행하고 현재 액티비티를 종료한다.
     */
    public void startCase() {
        Intent intent = new Intent(MainActivity.this, CaseListActivity.class);
        startActivity(intent);
    }

}