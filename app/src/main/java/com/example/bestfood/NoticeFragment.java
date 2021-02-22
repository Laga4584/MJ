package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.adapter.NoticeListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.NoticeItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    int userSeq;

    RecyclerView noticeList;
    LinearLayout noNoticeTexts;
    NoticeListAdapter noticeListAdapter;
    LinearLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    int listTypeValue = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_notice, container, false);

        context = this.getActivity();
        userSeq = ((App)this.getActivity().getApplication()).getUserSeq();

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noticeList = view.findViewById(R.id.list_notice);

        noNoticeTexts = view.findViewById(R.id.texts_no_notice);
        setRecyclerView();
        listInfo(userSeq, 0);
    }

    /**
     * 공지 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new LinearLayoutManager(context);
        noticeList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        noticeListAdapter = new NoticeListAdapter(context,
                R.layout.row_notice_list, new ArrayList<NoticeItem>());
        noticeList.setAdapter(noticeListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(userSeq, page);
            }
        };
        noticeList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 공지 정보를 조회한다.
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
                    noticeListAdapter.addItemList(list);

                    if (noticeListAdapter.getItemCount() == 0) {
                        noNoticeTexts.setVisibility(View.VISIBLE);
                        noticeList.setVisibility(View.GONE);
                    } else {
                        noNoticeTexts.setVisibility(View.GONE);
                        noticeList.setVisibility(View.VISIBLE);
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