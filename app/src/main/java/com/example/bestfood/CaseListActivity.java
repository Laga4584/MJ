package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.InfoListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    int userSeq;

    ImageButton backButton, addButton;
    RecyclerView bestFoodList;
    TextView noDataText;
    Spinner spinner;
    ImageView listType;
    InfoListAdapter infoListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 1;
    String orderType;
    String[] items = {"완료 케이스 보기", "완료 케이스 숨기기"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caselist);

        context = this;
        userSeq = ((App)this.getApplication()).getUserSeq();

        addButton = findViewById(R.id.add_button);
        backButton = findViewById(R.id.back_button);
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

        orderType = Constant.ORDER_TYPE_METER;

        bestFoodList = (RecyclerView)findViewById(R.id.list);
        noDataText = (TextView)findViewById(R.id.no_data);

        spinner = (Spinner)findViewById(R.id.spinner);
        //orderMeter = (TextView) view.findViewById(R.id.order_meter);
        //orderFavorite = (TextView) view.findViewById(R.id.order_favorite);
        //orderRecent = (TextView) view.findViewById(R.id.order_recent);

        //orderMeter.setOnClickListener(this);
        //orderFavorite.setOnClickListener(this);
        //orderRecent.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    orderType = Constant.ORDER_TYPE_METER;
                    setRecyclerView();
                    listInfo(userSeq, 0);
                }else {
                    orderType = Constant.ORDER_TYPE_RECENT;
                    setRecyclerView();
                    listInfo(userSeq, 0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setRecyclerView();

        listInfo(userSeq, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_profile:
                MyToast.s(this, "프로필 메뉴가 선택되었습니다.");
                Intent intent = new Intent(CaseListActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_notice:
                MyToast.s(this, "공지 메뉴가 선택되었습니다.");
                Intent intent2 = new Intent(CaseListActivity.this, NoticeActivity.class);
                startActivity(intent2);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * CaseActivity를 실행하고 현재 액티비티를 종료한다.
     */
    public void startCase() {
        Intent intent = new Intent(CaseListActivity.this, CaseActivity.class);
        startActivity(intent);

        //finish();
    }


    /**
     * 케이스 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        bestFoodList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        infoListAdapter = new InfoListAdapter(context,
                R.layout.row_case_list, new ArrayList<CaseInfoItem>());
        bestFoodList.setAdapter(infoListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(userSeq, page);
            }
        };
        bestFoodList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 케이스 정보를 조회한다.
     * @param userSeq 사용자 시퀀스
     * @param currentPage 현재 페이지
     */
    private void listInfo(int userSeq, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<CaseInfoItem>> call = remoteService.listCaseInfo(userSeq, currentPage);
        call.enqueue(new Callback<ArrayList<CaseInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CaseInfoItem>> call,
                                   Response<ArrayList<CaseInfoItem>> response) {
                ArrayList<CaseInfoItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    infoListAdapter.addItemList(list);

                    if (infoListAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CaseInfoItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}