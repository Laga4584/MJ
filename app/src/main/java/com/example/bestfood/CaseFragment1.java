package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseFragment1 extends Fragment {
    public static final String CASE_ITEM = "CASE_ITEM";
    private final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_CODE_1 = 0;
    public static final int REQUEST_CODE_2 = 1;
    public static final int REQUEST_CODE_3 = 2;
    public static final int REQUEST_CODE_4 = 3;

    Context context;
    CaseItem caseItem;
    int status;

    CardView cardView1, cardView2, cardView3, cardView4;
    TextView text1, text2, text3, text4, next, option1, option2, option3, option4;
    ImageView image1, image2, image3, image4;
    EditText edit1, edit2, edit3;

    String[] items_1 = {"test1", "test2", "test3", "test4", "test5", "test6"};
    String[] items_2 = {"수선", "리폼", "염색", "클리닝", "직접 입력"};
    String[] items_3 = {"test1", "test2", "test3", "test4", "test5", "test6", "test7"};
    String[] items_4 = {"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8"};

    public static CaseFragment1 newInstance(CaseItem caseItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CASE_ITEM, Parcels.wrap(caseItem));

        CaseFragment1 fragment = new CaseFragment1();
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
              R.layout.fragment_case_1, container, false);

      return rootView;
  }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView1 = view.findViewById(R.id.card_view_1);
        cardView2 = view.findViewById(R.id.card_view_2);
        cardView3 = view.findViewById(R.id.card_view_3);
        cardView4 = view.findViewById(R.id.card_view_4);

        final DisplayMetrics metrics = ((App) this.getActivity().getApplication()).getMetrics();
        /*
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardView1.getLayoutParams();
        layoutParams.setMargins(metrics.widthPixels/18, metrics.heightPixels/76, metrics.widthPixels/18, metrics.heightPixels/76);
        cardView1.requestLayout();
        cardView1.setContentPadding(metrics.widthPixels/36, metrics.heightPixels/76, metrics.widthPixels/36, metrics.heightPixels/76);
        ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) cardView2.getLayoutParams();
        layoutParams2.setMargins(metrics.widthPixels/18, metrics.heightPixels/76, metrics.widthPixels/18, metrics.heightPixels/76);
        cardView2.requestLayout();
        cardView2.setContentPadding(metrics.widthPixels/36, metrics.heightPixels/76, metrics.widthPixels/36, metrics.heightPixels/76);
        ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) cardView3.getLayoutParams();
        layoutParams3.setMargins(metrics.widthPixels/18, metrics.heightPixels/76, metrics.widthPixels/18, metrics.heightPixels/76);
        cardView3.requestLayout();
        cardView3.setContentPadding(metrics.widthPixels/36, metrics.heightPixels/76, metrics.widthPixels/36, metrics.heightPixels/76);
        ViewGroup.MarginLayoutParams layoutParams4 = (ViewGroup.MarginLayoutParams) cardView4.getLayoutParams();
        layoutParams4.setMargins(metrics.widthPixels/18, metrics.heightPixels/76, metrics.widthPixels/18, metrics.heightPixels/76);
        cardView4.requestLayout();
        cardView4.setContentPadding(metrics.widthPixels/36, metrics.heightPixels/76, metrics.widthPixels/36, metrics.heightPixels/76);
         */

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE_1);
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 0) {
                    Intent intent = new Intent(context, DialogActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_2);
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
                    startActivityForResult(intent, REQUEST_CODE_3);
                }
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 2) {
                    Intent intent = new Intent(context, CameraActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_4);
                    startActivityForResult(intent, REQUEST_CODE_4);
                }
            }
        });
        text1 = view.findViewById(R.id.text1);
        text1.setTextColor(context.getColor(R.color.colorPrimaryDark));
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4 = view.findViewById(R.id.text4);
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        image4 = view.findViewById(R.id.image4);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        edit1 = view.findViewById(R.id.edit1);
        edit1.setVisibility(View.INVISIBLE);
        edit2 = view.findViewById(R.id.edit2);
        edit2.setVisibility(View.INVISIBLE);
        edit3 = view.findViewById(R.id.edit3);
        edit3.setVisibility(View.INVISIBLE);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 3) {
                    insertCaseInfo();
                }
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
                        ((CaseActivity) getActivity()).replaceFragment(0);
                        RemoteLib.getInstance().updateCaseStatus(1, 0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_1) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int sendText = data.getExtras().getInt("sendText");
            if(sendText == items_1.length-1){
                option1.setVisibility(View.INVISIBLE);
                edit1.setVisibility(View.VISIBLE);
                edit1.requestFocus();
                edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (EditorInfo.IME_ACTION_DONE == i) {
                            caseItem.product = edit1.getText().toString();
                            edit1.clearFocus();
                        }
                        return false;
                    }
                });
            }else{
                option1.setText(items_1[sendText]);
            }
            image1.setVisibility(View.INVISIBLE);
            MyLog.d("here status" + status);
            MyLog.d("here requestCode" + requestCode);
            if(status == requestCode) status += 1;
            setText(status);
            MyLog.d("here status2" + status);
        }
        else if (requestCode == REQUEST_CODE_2) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int sendText = data.getExtras().getInt("sendText");
            if(sendText == items_2.length-1){
                option2.setVisibility(View.INVISIBLE);
                edit2.setVisibility(View.VISIBLE);
                edit2.requestFocus();
                edit2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (EditorInfo.IME_ACTION_DONE == i) {
                            caseItem.service = edit2.getText().toString();
                            edit2.clearFocus();
                        }
                        return false;
                    }
                });
            }else{
                option2.setText(items_2[sendText]);
            }
            image2.setVisibility(View.INVISIBLE);
            MyLog.d("here status" + status);
            MyLog.d("here requestCode" + requestCode);
            if(status == requestCode) status += 1;
            setText(status);
            MyLog.d("here status2" + status);
        }
        else if (requestCode == REQUEST_CODE_3) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int sendText = data.getExtras().getInt("sendText");
            if(sendText == items_3.length-1){
                option3.setVisibility(View.INVISIBLE);
                edit3.setVisibility(View.VISIBLE);
                edit3.requestFocus();
                edit3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (EditorInfo.IME_ACTION_DONE == i) {
                            caseItem.brand = edit3.getText().toString();
                            edit3.clearFocus();
                        }
                        return false;
                    }
                });
            }else{
                option3.setText(items_3[sendText]);
            }
            image3.setVisibility(View.INVISIBLE);
            MyLog.d("here status" + status);
            MyLog.d("here requestCode" + requestCode);
            if(status == requestCode) status += 1;
            setText(status);
            MyLog.d("here status2" + status);
        }
        else if (requestCode == REQUEST_CODE_4) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int sendText = data.getExtras().getInt("sendText");
            option4.setText(items_4[sendText]);
            image4.setVisibility(View.INVISIBLE);
            MyLog.d("here status" + status);
            MyLog.d("here requestCode" + requestCode);
            if(status == requestCode) status += 1;
            setText(status);
            MyLog.d("here status2" + status);
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
                next.setBackgroundColor(context.getColor(R.color.colorAccent));
                break;
        }
    }
}
