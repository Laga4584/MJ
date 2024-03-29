package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.adapter.RequestListAdapter;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaseFragment5 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String CASE_ITEM = "CASE_ITEM";
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    Context context;
    CaseItem caseItem;
    RepairerItem repairerItem;
    TextView name, info, title, price, timeText, rateText, reportText, description1, description2, next, sequence;
    TextView option2;
    TextView payButton, cancelButton, finishButton, restartButton;
    EditText edit1, edit3, edit4, edit5;
    CardView content1, result, report;
    LinearLayout content2;
    ImageView image;
    ImageButton prevButton, nextButton;
    RecyclerView requestList;
    RequestListAdapter requestListAdapter;
    LinearLayoutManager layoutManager;
    int listTypeValue = 1;
    ArrayList<ImageItem> requests = new ArrayList<ImageItem>();
    int imageCount = 0;
    int count;
    String[] status_list = {"견적 대기", "견적 완료", "수선 중", "수선 완료", "발송 대기", "배송 완료"};

    public static CaseFragment5 newInstance(CaseItem caseItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CASE_ITEM, Parcels.wrap(caseItem));

        CaseFragment5 fragment = new CaseFragment5();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            caseItem = Parcels.unwrap(getArguments().getParcelable(CASE_ITEM));
        }
        context = this.getActivity();
        count = Arrays.asList(RemoteLib.getInstance().status_list_2).indexOf(caseItem.status2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView;
        if (count < 4) {
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_case_5, container, false);
        }else if (count == 4){
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_case_5_2, container, false);
        }else{
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_case_5_3, container, false);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (count < 4) {
            content1 = view.findViewById(R.id.content1);
            content2 = view.findViewById(R.id.content2);
            content2.setVisibility(View.GONE);
            description1 = view.findViewById(R.id.description1);
            description2 = view.findViewById(R.id.description2);

            name = view.findViewById(R.id.name);
            info = view.findViewById(R.id.info);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);

            payButton = view.findViewById(R.id.button_pay);
            cancelButton = view.findViewById(R.id.button_cancel);

            image = view.findViewById(R.id.image);
            image.setClipToOutline(true);
            sequence = view.findViewById(R.id.sequence);
            prevButton = view.findViewById(R.id.button_prev);
            prevButton.setVisibility(View.GONE);
            nextButton = view.findViewById(R.id.button_next);
            nextButton.setVisibility(View.VISIBLE);
            requestList = view.findViewById(R.id.request_list);
            result = view.findViewById(R.id.result);
            rateText = view.findViewById(R.id.text_rate);
            timeText = view.findViewById(R.id.text_time);
            report = view.findViewById(R.id.report);
            reportText = view.findViewById(R.id.text_report);
            next = view.findViewById(R.id.next);

            selectRepairerInfo(caseItem.repairerSeq);

        }else if (count == 4){
            edit1 = view.findViewById(R.id.edit1);
            option2 = view.findViewById(R.id.option2);
            edit3 = view.findViewById(R.id.edit3);
            edit4 = view.findViewById(R.id.edit4);
            edit5 = view.findViewById(R.id.edit5);
            next = view.findViewById(R.id.next);

            edit1.setText(caseItem.addressee);
            option2.setText(caseItem.address);
            edit3.setText(caseItem.addressDetail);
            edit4.setText(caseItem.addresseePhone);
            edit5.setText(caseItem.memo);

            edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (EditorInfo.IME_ACTION_DONE == i) {
                        caseItem.addressee = edit1.getText().toString();
                        edit1.clearFocus();
                    }
                    return false;
                }
            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AddressActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);

                }
            });
            edit3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (EditorInfo.IME_ACTION_DONE == i) {
                        caseItem.addressee = edit3.getText().toString();
                        edit3.clearFocus();
                    }
                    return false;
                }
            });
            edit4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (EditorInfo.IME_ACTION_DONE == i) {
                        caseItem.addressee = edit4.getText().toString();
                        edit4.clearFocus();
                    }
                    return false;
                }
            });
            edit5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (EditorInfo.IME_ACTION_DONE == i) {
                        caseItem.addressee = edit5.getText().toString();
                        edit5.clearFocus();
                    }
                    return false;
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoteLib.getInstance().insertCaseInfo(caseItem, caseUploadHandler);
                }
            });
        }else{
            finishButton = view.findViewById(R.id.button_finish);
            restartButton = view.findViewById(R.id.button_restart);
            if (count == 5){
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 5, 0, caseStatusHandler);
                    }
                });
                restartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 3, 0, caseStatusHandler);
                    }
                });
            }else{
                restartButton.setVisibility(View.GONE);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 6, 6, caseStatusHandler);
                    }
                });
            }


        }
    }

    private void setView(){

        MyLog.d("here repairerItem " + repairerItem.toString());
        String nameText = repairerItem.name + " 명장";
        name.setText(nameText);
        String infoText = "[" + caseItem.brand + "] " + caseItem.product + " " + caseItem.service + " 외 " + caseItem.dotCount + " 건";
        info.setText(infoText);
        String titleText = "완료 " + repairerItem.caseCount + " | 평점 " + repairerItem.score + " | " + repairerItem.product + " 분야";
        title.setText(titleText);
        String priceText = "예상 견적 " + caseItem.price+ "원 | " + caseItem.time + "일";
        price.setText(priceText);

        if (count == 0) {
            sequence.setVisibility(View.GONE);
            prevButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else if (count == 1){
            priceText = "최종 견적 " + caseItem.price+ "원 | " + caseItem.time + "일";
            price.setText(priceText);
            price.setTextColor(context.getColor(R.color.colorAccent));
            content1.setVisibility(View.GONE);
            content2.setVisibility(View.VISIBLE);
            description1.setText("최종 견적 금액이 처음의 견적보다 낮을 시");
            description2.setText("차액은 3~4 영업일 이내 자동 환불됩니다");
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoteLib.getInstance().updateCaseStatus(caseItem.seq,1, 5, caseStatusHandler);
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoteLib.getInstance().updateCaseStatus(caseItem.seq,4, 4, caseStatusHandler);
                }
            });
            next.setVisibility(View.GONE);
        } else if (count == 2) {
            price.setVisibility(View.GONE);
            description1.setText("명장님이 고객님의 요청 사항에 맞춰 수선을 진행중이에요");
            description2.setVisibility(View.GONE);
        } else if (count == 3) {
            result.setVisibility(View.VISIBLE);
            String suffixString;
            if (caseItem.errorRateTime < 0) suffixString = "단축";
            else suffixString = "초과";
            String rateString = "수선 기간 " + String.valueOf(caseItem.errorRateTime) + "% " + suffixString;
            rateText.setText(rateString);
            report.setVisibility(View.VISIBLE);
            reportText.setText(caseItem.report);
            price.setVisibility(View.GONE);
            description1.setText("명장님이 수선을 종료하고 결과물 사진을 올려주셨어요");
            description2.setVisibility(View.GONE);
            next.setText("확인했어요");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 4, 4, caseUploadHandler);
                }
            });
        }

        sequence.setText(String.valueOf(imageCount+1));

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCount -= 1;
                setImage(image, requests.get(imageCount).filename);
                sequence.setText(String.valueOf(imageCount+1));
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
                setImage(image, requests.get(imageCount).filename);
                sequence.setText(String.valueOf(imageCount+1));
                if (imageCount > 0) {
                    prevButton.setVisibility(View.VISIBLE);
                }
                if (imageCount == requests.size() - 1) {
                    nextButton.setVisibility(View.GONE);
                }
            }
        });

        setRecyclerView();
        listInfo(caseItem.seq);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        option2.setText(data);
                        caseItem.address = data;
                    }
                }
                break;
        }
    }


    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(RemoteService.IMAGE_URL + fileName).into(imageView);
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
     * @param caseSeq 사용자 시퀀스
     */
    private void listInfo(int caseSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ImageItem>> call = remoteService.listImageInfo(caseSeq);
        call.enqueue(new Callback<ArrayList<ImageItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageItem>> call,
                                   Response<ArrayList<ImageItem>> response) {
                ArrayList<ImageItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    if(count != 0){
                        list.remove(0);
                    }
                    requests = list;
                    setImage(image, requests.get(0).filename);
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

    Handler caseUploadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CaseActivity.caseItem = caseItem;
            RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 4, 5, caseStatusHandler);
        }
    };

    Handler caseStatusHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 6) {
                ((Activity)context).finish();
            } else {
                CaseActivity.caseItem.status = RemoteLib.getInstance().status_list_1[msg.arg1];
                CaseActivity.caseItem.status2 = RemoteLib.getInstance().status_list_2[msg.arg2];
                ((CaseActivity) getActivity()).replaceFragment(msg.arg1);
            }
        }
    };

}