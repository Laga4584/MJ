package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.InfoListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    FloatingActionButton fab;
    Toolbar toolbar;

    Context context;

    int memberSeq;

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
        memberSeq = ((App)this.getApplication()).getMemberSeq();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCase();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setLogo(R.drawable.ic_keep_off);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_USE_LOGO);

        this.getSupportActionBar().setTitle(R.string.nav_list);

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
                    listInfo(memberSeq, 0);
                }else {
                    orderType = Constant.ORDER_TYPE_RECENT;
                    setRecyclerView();
                    listInfo(memberSeq, 0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setRecyclerView();

        listInfo(memberSeq, 0);
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
            case R.id.menu_chat:
                MyToast.s(this, "채팅 메뉴가 선택되었습니다.");
                Intent intent2 = new Intent(CaseListActivity.this, ChatActivity.class);
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
     * 프래그먼트가 일시 중지 상태가 되었다가 다시 보여질 때 호출된다.
     * BestFoodInfoActivity가 실행된 후,
     * 즐겨찾기 상태가 변경되었을 경우 이를 반영하는 용도로 사용한다.
     */
    @Override
    public void onResume() {
        super.onResume();

        App app = ((App) this.getApplication());
        CaseInfoItem currentInfoItem = app.getCaseInfoItem();

        if (infoListAdapter != null && currentInfoItem != null) {
            infoListAdapter.setItem(currentInfoItem);
            app.setFoodInfoItem(null);
        }
    }

    /**
     * 맛집 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
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
                R.layout.row_bestfood_list, new ArrayList<CaseInfoItem>());
        bestFoodList.setAdapter(infoListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(memberSeq, page);
            }
        };
        bestFoodList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param memberSeq 사용자 시퀀스
     * @param currentPage 현재 페이지
     */
    private void listInfo(int memberSeq, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<CaseInfoItem>> call = remoteService.listCaseInfo(memberSeq, currentPage);
        call.enqueue(new Callback<ArrayList<CaseInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CaseInfoItem>> call,
                                   Response<ArrayList<CaseInfoItem>> response) {
                ArrayList<CaseInfoItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    infoListAdapter.addItemList(list);

                    if (infoListAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                        MyLog.d("noDataText VISIBLE");
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


    /**
     * 맛집 정보 정렬 방식의 텍스트 색상을 설정한다.
     * @param color1 거리순 색상
     * @param color2 인기순 색상
     * @param color3 최근순 색상
     */
    private void setOrderTextColor(int color1, int color2, int color3) {
        //orderMeter.setTextColor(ContextCompat.getColor(context, color1));
        //orderFavorite.setTextColor(ContextCompat.getColor(context, color2));
        //orderRecent.setTextColor(ContextCompat.getColor(context, color3));
    }

    /**
     * 리사이클러뷰의 리스트 형태를 변경한다.
     */
    private void changeListType() {
        if (listTypeValue == 1) {
            listTypeValue = 2;
            listType.setImageResource(R.drawable.ic_list2);
        } else {
            listTypeValue = 1;
            listType.setImageResource(R.drawable.ic_list);

        }
        setLayoutManager(listTypeValue);
    }

}