package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bestfood.adapter.RepairerListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
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
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    public static final int REQUEST_CODE = 100;
    String query = "";
    int userSeq;
    int mode = 0;

    RecyclerView repairerList;
    TextView noDataText;
    EditText search;
    ImageButton filter;
    RepairerListAdapter repairerListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 3;
    String orderType;

    /**
     * BestFoodListFragment 인스턴스를 생성한다.
     * @return BestFoodListFragment 인스턴스
     */
    public static RepairerListFragment newInstance() {
        RepairerListFragment f = new RepairerListFragment();
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

        search = view.findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_SEARCH == i) {
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    query = search.getText().toString();
                    repairerListAdapter.clearItemList();
                    mode = 0;
                    listInfo(mode, query, 0);

                } else {
                    return false;
                }
                return true;
            }
        });

        filter = view.findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        repairerList = (RecyclerView) view.findViewById(R.id.list);
        //noDataText = (TextView) view.findViewById(R.id.no_data);

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        ViewGroup.LayoutParams params = repairerList.getLayoutParams();
        repairerList.setPadding(metrics.widthPixels/18, metrics.heightPixels*25/760, metrics.widthPixels/18, metrics.heightPixels*25/760);
        repairerList.setLayoutParams(params);

         */
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
            repairerListAdapter.clearItemList();
            mode = 1;
            listInfo(mode, queryString, 0);
        }
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
                listInfo(mode, query, page);
            }
        };
        repairerList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 명장 정보를 조회한다.
     * @param currentPage 현재 페이지
     */
    private void listInfo(int mode, String query, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<RepairerItem>> call = remoteService.listRepairerInfo(mode, query, currentPage);
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
