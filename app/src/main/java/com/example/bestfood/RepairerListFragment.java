package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.adapter.RepairerListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 명장 정보 리스트를 보여주는 프래그먼트
 */
public class RepairerListFragment extends Fragment implements View.OnClickListener {
    public static final String QUERY = "QUERY";
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    String query;
    int userSeq;

    RecyclerView bestFoodList;
    TextView noDataText;
    Spinner spinner;
    ImageView listType;
    RepairerListAdapter infoListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 3;
    String orderType;
    String[] items = {"완료 케이스 보기", "완료 케이스 숨기기"};

    /**
     * BestFoodListFragment 인스턴스를 생성한다.
     * @return BestFoodListFragment 인스턴스
     */
    public static RepairerListFragment newInstance(String query) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(QUERY, Parcels.wrap(query));
        RepairerListFragment f = new RepairerListFragment();
        f.setArguments(bundle);
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
        if (getArguments() != null) {
            query = Parcels.unwrap(getArguments().getParcelable(QUERY));
        }else query = "";
        context = this.getActivity();

        userSeq = ((App)this.getActivity().getApplication()).getUserSeq();

        View layout = inflater.inflate(R.layout.fragment_repairer_list, container, false);

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
        //((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_list);

        orderType = Constant.ORDER_TYPE_METER;

        bestFoodList = (RecyclerView) view.findViewById(R.id.list);
        //noDataText = (TextView) view.findViewById(R.id.no_data);

        //spinner = (Spinner) view.findViewById(R.id.spinner);

        setRecyclerView();

        listInfo(query, 0);
    }


    /**
     * 명장 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
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

        infoListAdapter = new RepairerListAdapter(context,
                R.layout.row_repairer_list, new ArrayList<RepairerItem>());
        bestFoodList.setAdapter(infoListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(query, page);
            }
        };
        bestFoodList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 명장 정보를 조회한다.
     * @param currentPage 현재 페이지
     */
    private void listInfo(String query, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<RepairerItem>> call = remoteService.listRepairerInfo(query, currentPage);
        call.enqueue(new Callback<ArrayList<RepairerItem>>() {
            @Override
            public void onResponse(Call<ArrayList<RepairerItem>> call,
                                   Response<ArrayList<RepairerItem>> response) {
                ArrayList<RepairerItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    infoListAdapter.addItemList(list);

                    if (infoListAdapter.getItemCount() == 0) {
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

    /**
     * 각종 버튼에 대한 클릭 처리를 정의한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        /*
            if (v.getId() == R.id.order_meter) {
                orderType = Constant.ORDER_TYPE_METER;

                setOrderTextColor(R.color.text_color_green,
                        R.color.text_color_black, R.color.text_color_black);

            } else if (v.getId() == R.id.order_favorite) {
                orderType = Constant.ORDER_TYPE_FAVORITE;

                setOrderTextColor(R.color.text_color_black,
                        R.color.text_color_green, R.color.text_color_black);

            } else if (v.getId() == R.id.order_recent) {
                orderType = Constant.ORDER_TYPE_RECENT;

                setOrderTextColor(R.color.text_color_black,
                        R.color.text_color_black, R.color.text_color_green);
            }

            setRecyclerView();
            listInfo(userSeq, GeoItem.getKnownLocation(), orderType, 0);
*/
    }
}
