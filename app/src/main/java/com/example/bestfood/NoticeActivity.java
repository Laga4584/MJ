package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.NoticeListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.NoticeItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    Context context;

    int userSeq;

    RecyclerView bestFoodList;
    TextView noDataText;
    NoticeListAdapter infoListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 1;
    String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        context = this;
        userSeq = ((App)this.getApplication()).getUserSeq();

        orderType = Constant.ORDER_TYPE_METER;

        bestFoodList = (RecyclerView)findViewById(R.id.list);
        noDataText = (TextView)findViewById(R.id.no_data);
        setRecyclerView();

        listInfo(userSeq, 0);
    }

    
    /**
     * CaseActivity를 실행하고 현재 액티비티를 종료한다.
     */
    public void startCase() {
        Intent intent = new Intent(NoticeActivity.this, CaseActivity.class);
        startActivity(intent);

        //finish();
    }

    /**
     * 프래그먼트가 일시 중지 상태가 되었다가 다시 보여질 때 호출된다.
     * BestFoodInfoActivity가 실행된 후,
     * 즐겨찾기 상태가 변경되었을 경우 이를 반영하는 용도로 사용한다.
     */
    /*
    @Override
    public void onResume() {
        super.onResume();

        App app = ((App) this.getApplication());
        NoticeItem currentInfoItem = app.getNoticeItem();

        if (infoListAdapter != null && currentInfoItem != null) {
            infoListAdapter.setItem(currentInfoItem);
            app.setFoodInfoItem(null);
        }
    }

     */

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

        infoListAdapter = new NoticeListAdapter(context,
                R.layout.row_notice_list, new ArrayList<NoticeItem>());
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
     * 서버에서 맛집 정보를 조회한다.
     * @param userSeq 사용자 시퀀스
     * @param currentPage 현재 페이지
     */
    private void listInfo(int userSeq, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<NoticeItem>> call = remoteService.listNoticeInfo(userSeq, currentPage);
        call.enqueue(new Callback<ArrayList<NoticeItem>>() {
            @Override
            public void onResponse(Call<ArrayList<NoticeItem>> call,
                                   Response<ArrayList<NoticeItem>> response) {
                ArrayList<NoticeItem> list = response.body();

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
            public void onFailure(Call<ArrayList<NoticeItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

}