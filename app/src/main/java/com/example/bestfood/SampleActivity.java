package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bestfood.adapter.RequestListAdapter;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.item.SampleItem;
import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleActivity extends AppCompatActivity {
    public static final String INFO_SEQ = "INFO_SEQ";
    private final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_CODE = 100;
    Context context;
    int sampleSeq, userSeq;
    SampleItem sampleItem;
    RepairerItem repairerItem;
    UserItem userItem;

    ImageButton backButton, keepButton;
    TextView nameText, infoText, titleText, estimateText, resultText, rateText, sequenceText,
            reviewNameText, reviewText, tag1Text, tag2Text, tag3Text, tag4Text;
    ImageView repairerProfileIcon, itemImage, reviewProfileIcon;
    SimpleRatingBar ratingBar;

    ImageButton prevButton, nextButton;
    RecyclerView requestList;
    RequestListAdapter requestListAdapter;
    LinearLayoutManager layoutManager;
    int listTypeValue = 1;
    boolean keep = false;
    ArrayList<ImageItem> requests = new ArrayList<ImageItem>();
    int imageCount = 0;
    String[] tag1_list = {"그때 그때 바로 연락주세요!", "불편하지 않을 정도였어요.", "연락이 안되서 답답했어요"};
    String[] tag2_list = {"훌륭해요 제 마음에 쏙 들어요!", "전체적으로 만족스러워요", "실망스러워요"};
    String[] tag3_list = {"저렴해요", "적당해요", "조금 비싸요"};
    String[] tag4_list = {"또 맡기고 싶어요 추천해요!", "만족해요", "다음엔 좀 고민해보려구요"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        context = this;
        sampleItem = new SampleItem();
        sampleSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        
        selectCaseInfo(sampleSeq);
    }

    private void selectCaseInfo(int sampleSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<SampleItem> call = remoteService.selectSampleInfo(sampleSeq);

        call.enqueue(new Callback<SampleItem>() {
            @Override
            public void onResponse(Call<SampleItem> call, Response<SampleItem> response) {
                SampleItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    sampleItem = item;
                    selectUserInfo(sampleItem.userSeq);

                } else {
                    //sampleItem.userSeq = userSeq;
                }
            }
            @Override
            public void onFailure(Call<SampleItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void selectUserInfo(int userSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<UserItem> call = remoteService.selectUserSeq(userSeq);

        call.enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                UserItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    userItem = item;
                    selectRepairerInfo(sampleItem.repairerSeq);
                    //loadingText.setVisibility(View.GONE);
                } else {
                    //loadingText.setVisibility(View.VISIBLE);
                    //((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void selectRepairerInfo(int repairerInfoSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<RepairerItem> call = remoteService.selectRepairerInfo(repairerInfoSeq);

        call.enqueue(new Callback<RepairerItem>() {
            @Override
            public void onResponse(Call<RepairerItem> call, Response<RepairerItem> response) {
                RepairerItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    repairerItem = item;
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

        backButton = findViewById(R.id.button_back);
        keepButton = findViewById(R.id.button_keep);

        repairerProfileIcon = findViewById(R.id.icon_repairer_profile);
        nameText = findViewById(R.id.text_repairer_name);
        infoText = findViewById(R.id.text_repairer_info);
        titleText = findViewById(R.id.text_title);
        estimateText = findViewById(R.id.text_estimate);
        resultText = findViewById(R.id.text_result);
        rateText = findViewById(R.id.text_rate);

        itemImage = findViewById(R.id.image_item);
        itemImage.setClipToOutline(true);
        sequenceText = findViewById(R.id.text_sequence);
        prevButton = findViewById(R.id.button_prev);
        prevButton.setVisibility(View.GONE);
        nextButton = findViewById(R.id.button_next);
        nextButton.setVisibility(View.VISIBLE);
        requestList = findViewById(R.id.list_request);

        tag1Text = findViewById(R.id.text_tag1);
        tag2Text = findViewById(R.id.text_tag2);
        tag3Text = findViewById(R.id.text_tag3);
        tag4Text = findViewById(R.id.text_tag4);

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
                if (userSeq == 0) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    if (keepButton.isSelected()) {
                        RemoteLib.getInstance().deleteKeep(userSeq, sampleSeq, 1);
                        keepButton.setSelected(false);
                    } else {
                        RemoteLib.getInstance().insertKeep(userSeq, sampleSeq, 1);
                        keepButton.setSelected(true);
                    }
                }
            }
        });

        setImage(1, repairerProfileIcon, repairerItem.profileImgFilename);
        String nameString = repairerItem.name + " 명장";
        nameText.setText(nameString);
        String infoString = "완료 " + repairerItem.caseCount + " | 평점 " + repairerItem.score + " | " + repairerItem.product + " 분야";
        infoText.setText(infoString);
        String titleString = "[" + sampleItem.brand + "] " + sampleItem.product + " " + sampleItem.service + " 외 " + sampleItem.dotCount + " 건";
        titleText.setText(titleString);
        String estimateString = "예상 견적 " + sampleItem.price + "원 | " + sampleItem.time + "일";
        estimateText.setText(estimateString);
        String resultString  = "최종 결과 " + sampleItem.priceFinal + "원 | " + sampleItem.timeResult + "일";
        resultText.setText(resultString);
        String errorRateString, errorRatePriceString, errorRateTimeString;

        if (sampleItem.errorRate > 0) errorRateString = "+" + Float.toString(sampleItem.errorRate);
        else errorRateString = "-" + Float.toString(sampleItem.errorRate);

        if (sampleItem.errorRatePrice > 0) errorRatePriceString = "+" + Float.toString(sampleItem.errorRatePrice);
        else errorRatePriceString = "-" + Float.toString(sampleItem.errorRatePrice);

        if (sampleItem.errorRateTime > 0) errorRateTimeString = "+" + Float.toString(sampleItem.errorRateTime);
        else errorRateTimeString = "-" + Float.toString(sampleItem.errorRateTime);
        String rateString = "견적 오차율 " + errorRateString + "% ( 가격 " + errorRatePriceString + "% | 기간 " + errorRateTimeString + "% )";
        rateText.setText(rateString);

        tag1Text.setText(tag1_list[Math.round(sampleItem.tag1)]);
        tag2Text.setText(tag2_list[Math.round(sampleItem.tag2)]);
        tag3Text.setText(tag3_list[Math.round(sampleItem.tag3)]);
        tag4Text.setText(tag4_list[Math.round(sampleItem.tag4)]);

        setImage(1, reviewProfileIcon, userItem.userIconFilename);
        reviewNameText.setText(userItem.name);
        reviewText.setText(sampleItem.review);
        
        sequenceText.setText(String.valueOf(imageCount+1));

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCount -= 1;
                setImage(0, itemImage, requests.get(imageCount).filename);
                sequenceText.setText(String.valueOf(imageCount+1));
                if (imageCount < 1) {
                    prevButton.setVisibility(View.GONE);
                }
                if (imageCount < requests.size() - 1) {
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCount += 1;
                setImage(0, itemImage, requests.get(imageCount).filename);
                sequenceText.setText(String.valueOf(imageCount+1));
                if (imageCount > 0) {
                    prevButton.setVisibility(View.VISIBLE);
                }
                if (imageCount == requests.size() - 1) {
                    nextButton.setVisibility(View.GONE);
                }
            }
        });

        setRecyclerView();
        listInfo(sampleItem.seq);

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setRating(sampleItem.score);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            userSeq = ((App)getApplication()).getUserSeq();
            if(userSeq!=0) {
                RemoteLib.getInstance().selectKeepInfo(userSeq, sampleSeq, 0, keepButton);
            }
        }
    }

    private void setImage(int mode, ImageView imageView, String fileName) {

        if (mode == 0) {
            if (StringLib.getInstance().isBlank(fileName)) {
                Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
            } else {
                Picasso.get().load(RemoteService.IMAGE_URL + fileName).into(imageView);
            }
        }else {
            if (StringLib.getInstance().isBlank(fileName)) {
                Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
            } else {
                Picasso.get().load(RemoteService.USER_ICON_URL + fileName).into(imageView);
            }
        }
    }

    /**
     * 이미지 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new LinearLayoutManager(context);
        requestList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        requestListAdapter = new RequestListAdapter(context,
                R.layout.row_request_list, new ArrayList<ImageItem>());
        requestList.setAdapter(requestListAdapter);
    }

    /**
     * 서버에서 이미지 정보를 조회한다.
     * @param sampleSeq 사용자 시퀀스
     */
    private void listInfo(int sampleSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ImageItem>> call = remoteService.listImageInfo(sampleSeq);
        call.enqueue(new Callback<ArrayList<ImageItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageItem>> call,
                                   Response<ArrayList<ImageItem>> response) {
                ArrayList<ImageItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    requests = list;
                    setImage(0, itemImage, requests.get(0).filename);
                    list.remove(0);
                    requestListAdapter.addItemList(list);

                    if (requestListAdapter.getItemCount() == 0) {
                        //noDataText.setVisibility(View.VISIBLE);
                    } else {
                        //noDataText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}
