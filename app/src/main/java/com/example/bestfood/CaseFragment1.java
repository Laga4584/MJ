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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.adapter.RequestListAdapter;
import com.example.bestfood.item.CaptureItem;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.lib.BitmapLib;
import com.example.bestfood.lib.FileLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    CardView cardView1, cardView2, cardView3, cardView4, cardView4_2;
    TextView text1, text2, text3, text4, nextButton, option1, option2, option3, option4;
    ImageView image1, image2, image3, image4, requestImage;
    EditText edit1, edit2, edit3;
    ArrayList<ImageItem> requests = new ArrayList<ImageItem>();
    ArrayList<File> imageFiles = new ArrayList<File>();
    RecyclerView requestList;
    RequestListAdapter requestListAdapter;
    LinearLayoutManager layoutManager;

    String[] items_1 = {"test1", "test2", "test3", "test4", "test5", "test6"};
    String[] items_2 = {"수선", "리폼", "염색", "클리닝", "직접 입력"};
    String[] items_3 = {"test1", "test2", "test3", "test4", "test5", "test6", "test7"};
    boolean isSavingImage;
    boolean isConvertingImage;

    public static ArrayList<CaptureItem> captureItemList = new ArrayList<CaptureItem>();

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
        cardView4_2 = view.findViewById(R.id.card_view_4_2);
        cardView4_2.setVisibility(View.GONE);
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
        edit1.setVisibility(View.GONE);
        edit2 = view.findViewById(R.id.edit2);
        edit2.setVisibility(View.GONE);
        edit3 = view.findViewById(R.id.edit3);
        edit3.setVisibility(View.GONE);
        requestImage = view.findViewById(R.id.image_request);
        requestList = view.findViewById(R.id.list_request);
        nextButton = view.findViewById(R.id.button_next);

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
                    Intent intent = new Intent(context, CameraActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_4);
                    startActivityForResult(intent, REQUEST_CODE_4);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status > 3) {
                    setText(5);
                    RemoteLib.getInstance().insertCaseInfo(caseItem, caseUploadHandler);
                }
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
            int resultInt = data.getExtras().getInt("resultInt");
            if(resultInt == items_1.length-1){
                option1.setVisibility(View.GONE);
                edit1.setVisibility(View.VISIBLE);
                edit1.requestFocus();
                edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (EditorInfo.IME_ACTION_DONE == i) {
                            caseItem.product = edit1.getText().toString();
                            edit1.clearFocus();
                            edit1.setVisibility(View.GONE);
                            option1.setVisibility(View.VISIBLE);
                            option1.setText(edit1.getText().toString());
                        }
                        return false;
                    }
                });
            }else{
                caseItem.product = items_1[resultInt];
                option1.setText(items_1[resultInt]);
            }
            image1.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
        }
        else if (requestCode == REQUEST_CODE_2) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            if(resultInt == items_2.length-1){
                option2.setVisibility(View.GONE);
                edit2.setVisibility(View.VISIBLE);
                edit2.requestFocus();
                edit2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (EditorInfo.IME_ACTION_DONE == i) {
                            caseItem.service = edit2.getText().toString();
                            edit2.clearFocus();
                            edit2.setVisibility(View.GONE);
                            option2.setVisibility(View.VISIBLE);
                            option2.setText(edit2.getText().toString());
                        }
                        return false;
                    }
                });
            }else{
                caseItem.service = items_2[resultInt];
                option2.setText(items_2[resultInt]);

            }
            image2.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
        }
        else if (requestCode == REQUEST_CODE_3) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            if(resultInt == items_3.length-1){
                option3.setVisibility(View.GONE);
                edit3.setVisibility(View.VISIBLE);
                edit3.requestFocus();
                edit3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (EditorInfo.IME_ACTION_DONE == i) {
                            caseItem.brand = edit3.getText().toString();
                            edit3.clearFocus();
                            edit3.setVisibility(View.GONE);
                            option3.setVisibility(View.VISIBLE);
                            option3.setText(edit3.getText().toString());
                        }
                        return false;
                    }
                });
            }else{
                caseItem.brand = items_3[resultInt];
                option3.setText(items_3[resultInt]);
            }
            image3.setVisibility(View.GONE);
            if(status == requestCode) status += 1;
            setText(status);
        }
        else if (requestCode == REQUEST_CODE_4) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            cardView4.setVisibility(View.GONE);
            cardView4_2.setVisibility(View.VISIBLE);
            cardView4_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CameraActivity.class);
                    intent.putExtra("requestCode", REQUEST_CODE_4);
                    startActivityForResult(intent, REQUEST_CODE_4);
                }
            });
            requestImage.setImageBitmap(captureItemList.get(0).bitmap);
            for (int i=1; i<captureItemList.size(); i++) {
                ImageItem newItem = new ImageItem();
                newItem.check = 0;
                newItem.request = captureItemList.get(i).request;
                requests.add(newItem);
            }
            setRecyclerView();

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
                nextButton.setBackgroundColor(context.getColor(R.color.colorAccent));
                break;
            case 5:
                nextButton.setBackgroundColor(context.getColor(R.color.colorGray));
                nextButton.setText("이미지를 업로드하는 중...");
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
        setLayoutManager(1);

        requestListAdapter = new RequestListAdapter(context,
                R.layout.row_request_list, requests);
        requestList.setAdapter(requestListAdapter);
    }

    Handler caseUploadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int seq = msg.what;
            MyLog.d("here seq" + seq);
            caseItem.seq = seq;
            CaseActivity.caseItem = caseItem;
            insertCaptureInfo(captureItemList.size(), seq);
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

    private void insertCaptureInfo(final int count, final int caseSeq){
        int currentPosition = captureItemList.size()-count;
        String imageFilename = caseSeq + "_" + currentPosition;
        final File imageFile = FileLib.getInstance().getImageFile(context, imageFilename);
        isConvertingImage = true;

        final Handler finishHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isSavingImage = false;
                if(count>1) {
                    insertCaptureInfo(count-1, caseSeq);
                }
                else {
                    RemoteLib.getInstance().updateCaseStatus(caseSeq, 1, 0, caseStatusHandler);
                }
            }
        };

        Handler imageUploadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isConvertingImage = false;
                int currentPosition = captureItemList.size()-count;
                String labelString;
                String dotString="";
                if(count == captureItemList.size()){
                    labelString = "main";
                    dotString = captureItemList.get(currentPosition).positionX.toString().replace("[", "").replace("]", "")
                    + "/" + captureItemList.get(currentPosition).positionY.toString().replace("[", "").replace("]", "");
                }
                else {
                    labelString = "detail";
                }
                uploadCaseImage(caseSeq, labelString, dotString, captureItemList.get(currentPosition).request, imageFile, finishHandler);
            }
        };
            BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler, imageFile, captureItemList.get(currentPosition).bitmap);

    }

    /**
     * 케이스 이미지를 서버에 업로드한다.
     * @param infoSeq 케이스 정보 일련번호
     * @param label 이미지 설명
     * @param file 파일 객체
     * @param handler 처리 결과를 응답할 핸들러
     */
    public void uploadCaseImage(int infoSeq, String label, String dot, String request, File file, final Handler handler) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("multipart/form-data"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody infoSeqBody =
                RequestBody.create(
                        "" + infoSeq, MediaType.parse("multipart/form-data"));
        RequestBody labelBody =
                RequestBody.create(
                        label, MediaType.parse("multipart/form-data"));
        RequestBody dotBody =
                RequestBody.create(
                        dot, MediaType.parse("multipart/form-data"));

        RequestBody requestBody =
                RequestBody.create(
                        request, MediaType.parse("multipart/form-data"));

        Call<ResponseBody> call =
                remoteService.uploadCaseImage(infoSeqBody, labelBody, dotBody, requestBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadCaseImage success");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadCaseImage fail");
            }
        });
    }

}
