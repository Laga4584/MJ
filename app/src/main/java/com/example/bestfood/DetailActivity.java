package com.example.bestfood;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bestfood.adapter.ImageAdapter;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    public static ArrayList<ImageItem> images;
    ViewPager2 mPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        images = new ArrayList<ImageItem>();
        if (CaseActivity.infoItem.seq > 0){
            selectImageInfo(CaseActivity.infoItem.seq);
        }else{
            MyLog.d(TAG, "currentItem " + CaseFragment1.currentItem.toString());
            selectImageInfo(CaseFragment1.currentItem.seq);
        }

    }

    public void selectImageInfo(int seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ImageItem>> call = remoteService.listImageInfo(seq);
        call.enqueue(new Callback<ArrayList<ImageItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageItem>> call, Response<ArrayList<ImageItem>> response) {
                ArrayList<ImageItem> list = response.body();

                if (response.isSuccessful()) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    images = list;
                    MyLog.d("here images " + images.toString());
                    setAdapter();
                } else {
                    MyLog.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void setAdapter(){
        mPager = (ViewPager2)findViewById(R.id.viewpager);
        ImageAdapter adapter = new ImageAdapter(this, images.size()+1, 1);
        mPager.setAdapter(adapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }
}
