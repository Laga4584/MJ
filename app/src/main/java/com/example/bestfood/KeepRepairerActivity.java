package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.RepairerListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeepRepairerActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    int userSeq;
    RecyclerView repairerList;
    TextView noDataText;
    RepairerListAdapter repairerListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    int listTypeValue = 3;

    /**
     * 액티비티를 생성하고 화면을 구성한다.
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_repairer);

        context = this;

        userSeq = ((App)this.getApplication()).getUserSeq();

        repairerList = findViewById(R.id.list);
        //noDataText = (TextView) view.findViewById(R.id.no_data);

        setRecyclerView();
        listInfo(userSeq, 0);
    }

    /**
     * 명장 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        repairerList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        repairerListAdapter = new RepairerListAdapter(context,
                R.layout.row_repairer_list, new ArrayList<RepairerItem>());
        repairerList.setAdapter(repairerListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(userSeq, page);
            }
        };
        repairerList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 명장 정보를 조회한다.
     * @param currentPage 현재 페이지
     */
    private void listInfo(int userSeq, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<RepairerItem>> call = remoteService.listRepairerKeep(userSeq, currentPage);
        call.enqueue(new Callback<ArrayList<RepairerItem>>() {
            @Override
            public void onResponse(Call<ArrayList<RepairerItem>> call,
                                   Response<ArrayList<RepairerItem>> response) {
                ArrayList<RepairerItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    repairerListAdapter.addItemList(list);

                    if (repairerListAdapter.getItemCount() == 0) {
                        //noDataText.setVisibility(View.VISIBLE);
                    } else {
                        //noDataText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RepairerItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

}
