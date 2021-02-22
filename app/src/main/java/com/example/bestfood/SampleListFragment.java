package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bestfood.item.SampleItem;
import com.example.bestfood.adapter.SampleListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 명작 정보 리스트를 보여주는 프래그먼트
 */
public class SampleListFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_CODE = 200;

    Context context;

    int userSeq;
    int mode = 0;
    String query = "";

    RecyclerView sampleList;
    LinearLayout noDataTexts;
    EditText searchEdit;
    ImageButton filterButton;

    SampleListAdapter sampleListAdapter;
    GridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 2;


    /**
     * BestFoodListFragment 인스턴스를 생성한다.
     * @return BestFoodListFragment 인스턴스
     */
    public static SampleListFragment newInstance() {
        SampleListFragment f = new SampleListFragment();
        return f;
    }

    /**
     * fragment_repairer_list.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        userSeq = ((App)this.getActivity().getApplication()).getUserSeq();

        View layout = inflater.inflate(R.layout.fragment_sample_list, container, false);

        return layout;
    }
    
    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEdit = view.findViewById(R.id.edit_search);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_SEARCH == i) {
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    query = searchEdit.getText().toString();
                    sampleListAdapter.clearItemList();
                    mode = 0;
                    listInfo(0, query, 0);
                } else {
                    return false;
                }
                return true;
            }
        });

        filterButton = view.findViewById(R.id.button_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        sampleList = view.findViewById(R.id.list_sample);
        noDataTexts = view.findViewById(R.id.texts_no_data);

        setRecyclerView();
        listInfo(mode, query, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int[] selectedList = data.getIntArrayExtra("selectedList");
            String queryString = Integer.toString(selectedList[0]) + ", " + Integer.toString(selectedList[1]) + ", " + Integer.toString(selectedList[2]) + ", " + Integer.toString(selectedList[3]);
            sampleListAdapter.clearItemList();
            mode = 1;
            listInfo(mode, queryString, 0);
        }
    }


    /**
     * 명작 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new GridLayoutManager(context, row);
        sampleList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        sampleListAdapter = new SampleListAdapter(context,
                R.layout.row_sample_list, new ArrayList<SampleItem>());
        sampleList.setAdapter(sampleListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(mode, query, page);
            }
        };
        sampleList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 명작 정보를 조회한다.
     * @param currentPage 현재 페이지
     */
    private void listInfo(int mode, String query, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<SampleItem>> call = remoteService.listSampleInfo(mode, query, currentPage);
        call.enqueue(new Callback<ArrayList<SampleItem>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleItem>> call,
                                   Response<ArrayList<SampleItem>> response) {
                ArrayList<SampleItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    sampleListAdapter.addItemList(list);

                    if (sampleListAdapter.getItemCount() == 0) {
                        noDataTexts.setVisibility(View.VISIBLE);
                        sampleList.setVisibility(View.GONE);
                    } else {
                        noDataTexts.setVisibility(View.GONE);
                        sampleList.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SampleItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}
