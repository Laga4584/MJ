package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.SampleListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.item.ReviewItem;
import com.example.bestfood.item.SampleItem;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairerActivity extends AppCompatActivity {
    public static final String INFO_SEQ = "INFO_SEQ";
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    int repairerInfoSeq;
    RepairerItem infoItem;

    RecyclerView sampleList;
    SampleListAdapter sampleListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    int listTypeValue = 1;

    ImageButton backButton, keepButton;
    ImageView profileIcon;
    TextView nameText, infoText;
    Button requestButton;
    TextView serviceText, descriptionText;
    TextView tag1, tag2, tag3, tag4;
    TextView reviewNameText, reviewText;
    ImageView reviewProfileIcon;

    String[] tag1_list = {"그때 그때 바로 연락주세요!", "불편하지 않을 정도였어요.", "연락이 안되서 답답했어요"};
    String[] tag2_list = {"훌륭해요 제 마음에 쏙 들어요!", "전체적으로 만족스러워요", "실망스러워요"};
    String[] tag3_list = {"저렴해요", "적당해요", "조금 비싸요"};
    String[] tag4_list = {"또 맡기고 싶어요 추천해요!", "만족해요", "다음엔 좀 고민해보려구요"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairer);
        context = this;
        infoItem = new RepairerItem();
        repairerInfoSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        
        selectRepairerInfo(repairerInfoSeq);
    }

    private void selectRepairerInfo(int repairerInfoSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<RepairerItem> call = remoteService.selectRepairerInfo(repairerInfoSeq);

        call.enqueue(new Callback<RepairerItem>() {
            @Override
            public void onResponse(Call<RepairerItem> call, Response<RepairerItem> response) {
                RepairerItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    infoItem = item;
                    setView();
                    //loadingText.setVisibility(View.GONE);
                } else {
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<RepairerItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void setView(){
        sampleList = findViewById(R.id.list_sample);
        setRecyclerView();
        listInfo(repairerInfoSeq, 0);

        backButton = findViewById(R.id.button_back);
        keepButton = findViewById(R.id.button_keep);
        profileIcon = findViewById(R.id.icon_profile);
        nameText = findViewById(R.id.text_name);
        infoText = findViewById(R.id.text_info);
        requestButton = findViewById(R.id.button_request);
        serviceText = findViewById(R.id.text_service);
        descriptionText = findViewById(R.id.text_description);
        tag1 = findViewById(R.id.tag1);
        tag2 = findViewById(R.id.tag2);
        tag3 = findViewById(R.id.tag3);
        tag4 = findViewById(R.id.tag4);
        reviewProfileIcon = findViewById(R.id.icon_review_profile);
        reviewNameText = findViewById(R.id.text_review_name);
        reviewText = findViewById(R.id.text_review);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        keepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(keepButton.isSelected()) keepButton.setSelected(false);
                else keepButton.setSelected(true);
            }
        });

        setImage(profileIcon, infoItem.profileImgFilename);
        nameText.setText(infoItem.name);
        String infoString = "완료 " + infoItem.caseCount + " | 평점 " + infoItem.score + " | 견적 응답 " + infoItem.replyPeriod + "일";
        infoText.setText(infoString);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goCaseActivity(context, 0);
            }
        });

        String serviceString = infoItem.product + " / " + infoItem.service;
        serviceText.setText(serviceString);
        descriptionText.setText(infoItem.description);
        /*
        String[] tags = infoItem.tag.split(", ");
        tag1.setText(tag1_list[Integer.parseInt(tags[0])]);
        tag2.setText(tag2_list[Integer.parseInt(tags[1])]);
        tag3.setText(tag3_list[Integer.parseInt(tags[2])]);
        tag4.setText(tag4_list[Integer.parseInt(tags[3])]);
         */
        tag1.setText(tag1_list[Math.round(infoItem.tag1)]);
        tag2.setText(tag2_list[Math.round(infoItem.tag2)]);
        tag3.setText(tag3_list[Math.round(infoItem.tag3)]);
        tag4.setText(tag4_list[Math.round(infoItem.tag4)]);
        //review.setText(infoItem.review);
    }

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.ic_person).into(imageView);
        } else {
            Picasso.get().load(RemoteService.USER_ICON_URL + fileName).into(imageView);
        }
    }

    /**
     * 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.HORIZONTAL);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
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
                listInfo(repairerInfoSeq, page);
            }
        };
        sampleList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 정보를 조회한다.
     * @param currentPage 현재 페이지
     */
    private void listInfo(int repairerInfoSeq, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<SampleItem>> call = remoteService.repairerSampleInfo(repairerInfoSeq, currentPage);
        call.enqueue(new Callback<ArrayList<SampleItem>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleItem>> call,
                                   Response<ArrayList<SampleItem>> response) {
                ArrayList<SampleItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    sampleListAdapter.addItemList(list);

                    if (sampleListAdapter.getItemCount() == 0) {
                        //noDataText.setVisibility(View.VISIBLE);
                    } else {
                        //noDataText.setVisibility(View.GONE);
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

    /**
     * 서버에서 리뷰 정보를 조회한다.
     */
    private void listReview(int repairerSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ReviewItem>> call = remoteService.listRepairerReview(repairerSeq);
        call.enqueue(new Callback<ArrayList<ReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewItem>> call,
                                   Response<ArrayList<ReviewItem>> response) {
                ArrayList<ReviewItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    //sampleListAdapter.addItemList(list);

                    if (sampleListAdapter.getItemCount() == 0) {
                        //noDataText.setVisibility(View.VISIBLE);
                    } else {
                        //noDataText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReviewItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}
