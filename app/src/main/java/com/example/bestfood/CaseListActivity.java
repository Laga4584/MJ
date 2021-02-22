package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.adapter.CaseListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_CODE = 100;
    Context context;
    int userSeq;

    ImageButton backButton, addButton;
    ToggleButton toggleButton;
    RecyclerView caseList;
    CardView noDataTexts;
    CaseListAdapter caseListAdapter;
    LinearLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 1;
    int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caselist);

        context = this;
        userSeq = ((App)this.getApplication()).getUserSeq();

        if(userSeq==0){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("requestCode", REQUEST_CODE);
            startActivityForResult(intent, REQUEST_CODE);
        }else{
            setView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            userSeq = ((App)getApplication()).getUserSeq();
            setView();
        }
    }

    private void setView(){
        addButton = findViewById(R.id.button_add);
        backButton = findViewById(R.id.button_back);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCase();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        caseList = findViewById(R.id.list_case);

        noDataTexts = findViewById(R.id.texts_no_data);


        toggleButton = findViewById(R.id.button_toggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if(isChecked){
                    mode = 1;
                    setRecyclerView();
                    listInfo(userSeq, 0, 1);
                }else{
                    mode = 0;
                    setRecyclerView();
                    listInfo(userSeq, 0, 0);
                }
            }
        });
        setRecyclerView();
        listInfo(userSeq, 0, mode);
    }

    /**
     * CaseActivity를 실행하고 현재 액티비티를 종료한다.
     */
    public void startCase() {
        Intent intent = new Intent(CaseListActivity.this, CaseActivity.class);
        startActivity(intent);
    }

    /**
     * 케이스 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new LinearLayoutManager(context);
        caseList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        caseListAdapter = new CaseListAdapter(context,
                R.layout.row_case_list, new ArrayList<CaseItem>());
        caseList.setAdapter(caseListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(userSeq, page, mode);
            }
        };
        caseList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 케이스 정보를 조회한다.
     * @param userSeq 사용자 시퀀스
     * @param currentPage 현재 페이지
     */
    private void listInfo(int userSeq, final int currentPage, int mode) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<CaseItem>> call = remoteService.listCaseInfo(userSeq, currentPage, mode);
        call.enqueue(new Callback<ArrayList<CaseItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CaseItem>> call,
                                   Response<ArrayList<CaseItem>> response) {
                ArrayList<CaseItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    caseListAdapter.addItemList(list);

                    if (caseListAdapter.getItemCount() == 0) {
                        noDataTexts.setVisibility(View.VISIBLE);
                        caseList.setVisibility(View.GONE);
                    } else {
                        noDataTexts.setVisibility(View.GONE);
                        caseList.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CaseItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}