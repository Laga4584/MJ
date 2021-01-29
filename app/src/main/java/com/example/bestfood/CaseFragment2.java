package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.CheckListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.CheckItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaseFragment2 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String CASE_ITEM = "CASE_ITEM";
    Context context;
    CaseItem caseItem;
    TextView description, next;
    LinearLayout noData;
    RecyclerView checkList;
    CheckListAdapter checkListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    int listTypeValue = 1;
    int caseSeq;
    CheckItem selectedItem = new CheckItem();

    public static CaseFragment2 newInstance(CaseItem caseItem) {

        Bundle args = new Bundle();
        args.putParcelable(CASE_ITEM, Parcels.wrap(caseItem));

        CaseFragment2 fragment = new CaseFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            caseItem = Parcels.unwrap(getArguments().getParcelable(CASE_ITEM));
            caseSeq = caseItem.seq;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_case_2, container, false);
        context = this.getActivity();
        
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        description = view.findViewById(R.id.description);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = checkListAdapter.selectedItem;
                if (selectedItem != null){
                    caseItem.repairerSeq = selectedItem.repairerSeq;
                    caseItem.price = selectedItem.price;
                    insertCaseInfo();
                }
            }
        });

        noData = view.findViewById(R.id.no_data);
        checkList = view.findViewById(R.id.check_list);
        
        setRecyclerView();
        listInfo(caseSeq, 0);
    }

    /**
     * 견적 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        checkList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        checkListAdapter = new CheckListAdapter(context,
                R.layout.row_check_list, new ArrayList<CheckItem>(), caseItem);
        checkList.setAdapter(checkListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(caseSeq, page);
            }
        };
        checkList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 견적 정보를 조회한다.
     * @param caseSeq 케이스 시퀀스
     * @param currentPage 현재 페이지
     */
    private void listInfo(int caseSeq, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<CheckItem>> call = remoteService.listCheckInfo(caseSeq, currentPage);
        call.enqueue(new Callback<ArrayList<CheckItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CheckItem>> call,
                                   Response<ArrayList<CheckItem>> response) {
                ArrayList<CheckItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    checkListAdapter.addItemList(list);

                    if (checkListAdapter.getItemCount() == 0) {
                        noData.setVisibility(View.VISIBLE);
                        checkList.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.GONE);
                        checkList.setVisibility(View.VISIBLE);
                        description.setText("완성된 견적서를 선택하시고 다음을 눌러주세요");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CheckItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    /**
     * 사용자가 입력한 정보를 서버에 저장한다.
     */
    private void insertCaseInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.insertCaseInfo(caseItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();
                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }
                    if (seq == 0) {
                        //등록 실패
                    } else {
                        //caseItem.seq = seq;
                        //goNextPage();
                        CaseActivity.caseItem = caseItem;
                        ((CaseActivity) getActivity()).replaceFragment(1);
                        RemoteLib.getInstance().updateCaseStatus(2, 0);
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }
}
