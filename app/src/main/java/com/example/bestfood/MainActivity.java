package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    EditText search;
    FloatingActionButton fab;
    ImageButton repairerButton;
    ImageButton sampleButton;
    ImageButton noticeButton;
    ImageButton profileButton;

    RepairerListFragment fragment1;
    SampleListFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_SEARCH == i) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String query = search.getText().toString();
                    fragment1 = new RepairerListFragment();
                    fragment1 = RepairerListFragment.newInstance(query);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

                } else {
                    return false;
                }
                return true;
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCase();
            }
        });

        repairerButton = findViewById(R.id.repairer_button);
        sampleButton = findViewById(R.id.sample_button);
        noticeButton = findViewById(R.id.notice_button);
        profileButton = findViewById(R.id.profile_button);

        fragment1 = new RepairerListFragment();
        fragment2 = new SampleListFragment();

        repairerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
            }
        });
        sampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
            }
        });
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
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

        //finish();
    }

}