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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseItem;
import com.example.bestfood.lib.RemoteLib;

import org.parceler.Parcels;


public class CaseFragment6 extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static final String CASE_ITEM = "CASE_ITEM";
    CaseItem caseItem;
    int status;
    Context context;
    public static final int REQUEST_CODE_1 = 0;
    public static final int REQUEST_CODE_2 = 1;
    public static final int REQUEST_CODE_3 = 2;
    public static final int REQUEST_CODE_4 = 3;

    CardView cardView1, cardView2, cardView3, cardView4, cardView5;
    TextView text1, text2, text3, text4, text5, next, option1, option2, option3, option4;
    ImageView image1, image2, image3, image4, image5;
    EditText edit5;

    String[] items_1 = {"그때 그때 바로 연락주세요!", "불편하지 않을 정도였어요.", "연락이 안되서 답답했어요"};
    String[] items_2 = {"훌륭해요 제 마음에 쏙 들어요!", "전체적으로 만족스러워요", "실망스러워요"};
    String[] items_3 = {"저렴해요", "적당해요", "조금 비싸요"};
    String[] items_4 = {"또 맡기고 싶어요 추천해요!", "만족해요", "다음엔 좀 고민해보려구요"};
    int[] tagList = {0, 0, 0, 0};

    public static CaseFragment6 newInstance(CaseItem caseItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CASE_ITEM, Parcels.wrap(caseItem));

        CaseFragment6 fragment = new CaseFragment6();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_case_6, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView1 = view.findViewById(R.id.card_view_1);
        cardView2 = view.findViewById(R.id.card_view_2);
        cardView3 = view.findViewById(R.id.card_view_3);
        cardView4 = view.findViewById(R.id.card_view_4);
        cardView5 = view.findViewById(R.id.card_view_5);

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
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        edit5 = view.findViewById(R.id.edit5);
        edit5.setVisibility(View.GONE);
        next = view.findViewById(R.id.next);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE_1);
                intent.putExtra("itemList", items_1);
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 0) {
                    Intent intent = new Intent(context, DialogActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_2);
                    intent.putExtra("itemList", items_2);
                    startActivityForResult(intent, REQUEST_CODE_2);
                }
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 1) {
                    Intent intent = new Intent(context, DialogActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_3);
                    intent.putExtra("itemList", items_3);
                    startActivityForResult(intent, REQUEST_CODE_3);
                }
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 2) {
                    Intent intent = new Intent(context, DialogActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_4);
                    intent.putExtra("itemList", items_4);
                    startActivityForResult(intent, REQUEST_CODE_4);
                }
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 3) {
                    image5.setVisibility(View.GONE);
                    edit5.setVisibility(View.VISIBLE);
                    edit5.requestFocus();
                    edit5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            if (EditorInfo.IME_ACTION_DONE == i) {
                                caseItem.review = edit5.getText().toString();
                                edit5.clearFocus();
                                if(status == 4) status += 1;
                                setText(status);
                            }
                            return false;
                        }
                    });
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 4) {
                    caseItem.tag1 = tagList[0];
                    caseItem.tag2 = tagList[1];
                    caseItem.tag3 = tagList[2];
                    caseItem.tag4 = tagList[3];
                    RemoteLib.getInstance().insertCaseInfo(caseItem, caseUploadHandler);
                }
            }
        });
    }

    Handler caseUploadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CaseActivity.caseItem = caseItem;
            RemoteLib.getInstance().updateCaseStatus(caseItem.seq, 6, 5, caseStatusHandler);
        }
    };

    Handler caseStatusHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CaseActivity.caseItem.status = RemoteLib.getInstance().status_list_1[msg.arg1];
            CaseActivity.caseItem.status2 = RemoteLib.getInstance().status_list_2[msg.arg2];
            ((CaseActivity) getActivity()).replaceFragment(msg.arg1);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_1) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            tagList[0] = resultInt;
            option1.setText(items_1[resultInt]);
            image1.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
        }
        else if (requestCode == REQUEST_CODE_2) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            tagList[1] = resultInt;
            option2.setText(items_2[resultInt]);
            image2.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
        }
        else if (requestCode == REQUEST_CODE_3) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            tagList[2] = resultInt;
            option3.setText(items_3[resultInt]);
            image3.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
        }
        else if (requestCode == REQUEST_CODE_4) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            tagList[3] = resultInt;
            option4.setText(items_4[resultInt]);
            image4.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
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

}