package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaseFragment4 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String CASE_ITEM = "CASE_ITEM";
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    Context context;
    CaseItem caseItem;
    RepairerItem repairerItem;
    TextView text1, text2, text3, text4, text5, next, option2;
    TextView description2, name, info, title, price;
    ImageView image1, image2, image3, image4, image5;
    EditText edit1, edit2, edit3, edit4, edit5;
    int status;
    int count;

    String[] status_list = {"발송 요청", "발송 대기", "발송 완료"};

    public static CaseFragment4 newInstance(CaseItem caseItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CASE_ITEM, Parcels.wrap(caseItem));

        CaseFragment4 fragment = new CaseFragment4();
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
        count = Arrays.asList(status_list).indexOf(caseItem.status2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView;
        if (count == 0) {
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_case_4, container, false);
        }else {
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_case_4_2, container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (count == 0) {
            text1 = view.findViewById(R.id.text1);
            text1.setTextColor(context.getColor(R.color.colorPrimaryDark));
            text2 = view.findViewById(R.id.text2);
            text3 = view.findViewById(R.id.text3);
            text4 = view.findViewById(R.id.text4);
            text5 = view.findViewById(R.id.text5);
            image1 = view.findViewById(R.id.image1);
            image2 = view.findViewById(R.id.image2);
            image3 = view.findViewById(R.id.image3);
            image4 = view.findViewById(R.id.image4);
            image5 = view.findViewById(R.id.image5);
            edit1 = view.findViewById(R.id.edit1);
            edit1.setVisibility(View.GONE);
            option2 = view.findViewById(R.id.option2);
            option2.setVisibility(View.GONE);
            edit3 = view.findViewById(R.id.edit3);
            edit3.setVisibility(View.GONE);
            edit4 = view.findViewById(R.id.edit4);
            edit4.setVisibility(View.GONE);
            edit5 = view.findViewById(R.id.edit5);
            edit5.setVisibility(View.GONE);

            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image1.setVisibility(View.GONE);
                    edit1.setVisibility(View.VISIBLE);
                    edit1.requestFocus();
                    edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            if (EditorInfo.IME_ACTION_DONE == i) {
                                caseItem.addressee = edit1.getText().toString();
                                edit1.clearFocus();
                                if (status == 0) status += 1;
                                setText(status);
                            }
                            return false;
                        }
                    });
                }
            });
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status > 0) {
                        image2.setVisibility(View.GONE);
                        Intent i = new Intent(context, AddressActivity.class);
                        startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                    }
                }
            });
            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status > 1) {
                        image3.setVisibility(View.GONE);
                        edit3.setVisibility(View.VISIBLE);
                        edit3.requestFocus();
                        edit3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                if (EditorInfo.IME_ACTION_DONE == i) {
                                    caseItem.addressDetail = edit3.getText().toString();
                                    edit3.clearFocus();
                                    if (status == 2) status += 1;
                                    setText(status);
                                }
                                return false;
                            }
                        });
                    }
                }
            });
            image4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status > 2) {
                        image4.setVisibility(View.GONE);
                        edit4.setVisibility(View.VISIBLE);
                        edit4.requestFocus();
                        edit4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                if (EditorInfo.IME_ACTION_DONE == i) {
                                    caseItem.addresseePhone = edit4.getText().toString();
                                    edit4.clearFocus();
                                    if (status == 3) status += 1;
                                    setText(status);
                                }
                                return false;
                            }
                        });
                    }
                }
            });
            image5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status > 3) {
                        image5.setVisibility(View.GONE);
                        edit5.setVisibility(View.VISIBLE);
                        edit5.requestFocus();
                        edit5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                if (EditorInfo.IME_ACTION_DONE == i) {
                                    caseItem.memo = edit5.getText().toString();
                                    edit5.clearFocus();
                                    if (status == 4) status += 1;
                                    setText(status);
                                }
                                return false;
                            }
                        });
                    }
                }
            });

            next = view.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status > 4) {
                        insertAddressInfo();
                    }
                }
            });
        }else{
            description2 = view.findViewById(R.id.description2);
            description2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            name = view.findViewById(R.id.name);
            info = view.findViewById(R.id.info);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);

            selectRepairerInfo(caseItem.repairerSeq);
        }
    }

    private void setView(){
        String nameText = repairerItem.name + " 명장";
        name.setText(nameText);
        int dotCount = caseItem.dot.split("/ ").length - 1;
        String infoText = "[" + caseItem.brand + "] " + caseItem.product + " " + caseItem.service + " 외 " + dotCount + " 건";
        info.setText(infoText);
        String titleText = "완료 " + repairerItem.caseCount + " | 평점 " + repairerItem.score + " | " + repairerItem.product + " 분야";
        title.setText(titleText);
        String priceText = "예상 견적 " + caseItem.price + "원 | " + caseItem.time + "일";
        price.setText(priceText);
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
                        option2.setVisibility(View.VISIBLE);
                        if (status == 1) status += 1;
                        setText(status);
                    }
                }
                break;
        }
    }

    private void setText(int status) {
        switch (status) {
            case 1:
                text1.setTextColor(context.getColor(R.color.colorGray));
                text2.setTextColor(context.getColor(R.color.colorPrimaryDark));
                break;
            case 2:
                text2.setTextColor(context.getColor(R.color.colorGray));
                text3.setTextColor(context.getColor(R.color.colorPrimaryDark));
                break;
            case 3:
                text3.setTextColor(context.getColor(R.color.colorGray));
                text4.setTextColor(context.getColor(R.color.colorPrimaryDark));
                break;
            case 4:
                text4.setTextColor(context.getColor(R.color.colorGray));
                text5.setTextColor(context.getColor(R.color.colorPrimaryDark));
                break;
            case 5:
                text5.setTextColor(context.getColor(R.color.colorGray));
                next.setBackgroundColor(context.getColor(R.color.colorAccent));
                break;
        }
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

    private void insertAddressInfo(){
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
                        CaseActivity.caseItem = caseItem;
                        RemoteLib.getInstance().updateCaseStatus(3, 1);
                        //caseItem.status2 = "발송 대기";
                        ((CaseActivity) getActivity()).replaceFragment(2);
                        //goNextPage();
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