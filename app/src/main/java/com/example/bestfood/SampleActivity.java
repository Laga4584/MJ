package com.example.bestfood;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bestfood.adapter.ImageAdapter;
import com.example.bestfood.item.SampleItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleActivity extends AppCompatActivity {
    public static final String INFO_SEQ = "INFO_SEQ";
    private final String TAG = this.getClass().getSimpleName();
    public static SampleItem infoItem;
    ViewPager2 mPager;

    int infoSeq;

    ImageView beforeImage;
    ImageView afterImage;
    TextView repairerText;
    TextView brandText;
    TextView serviceText;
    TextView productText;
    TextView descriptionText;
    TextView methodText;
    TextView priceText;
    TextView timeText;
    TextView resultDescriptionText;
    TextView reviewText;
    TextView scoreText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        infoItem = new SampleItem();
        infoSeq = getIntent().getIntExtra(INFO_SEQ, 0);

        selectSampleInfo(infoSeq);
        //setView();
    }

    private void selectSampleInfo(int caseInfoSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<SampleItem> call = remoteService.selectSampleInfo(caseInfoSeq);

        call.enqueue(new Callback<SampleItem>() {
            @Override
            public void onResponse(Call<SampleItem> call, Response<SampleItem> response) {
                SampleItem item = response.body();

                if (response.isSuccessful() && item != null && item.seq > 0) {
                    infoItem = item;
                    setView();

                } else {
                    //infoItem.userSeq = userSeq;
                }
            }
            @Override
            public void onFailure(Call<SampleItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void setView(){
        mPager = findViewById(R.id.viewpager);
        ImageAdapter adapter = new ImageAdapter(this, 2, 0);
        mPager.setAdapter(adapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //beforeImage = (ImageView)findViewById(R.id.before_image);
        //afterImage = (ImageView)findViewById(R.id.after_image);
        repairerText = (TextView)findViewById(R.id.repairer_text);
        brandText = (TextView)findViewById(R.id.brand_text);
        serviceText = (TextView)findViewById(R.id.service_text);
        productText = (TextView)findViewById(R.id.product_text);
        descriptionText = (TextView)findViewById(R.id.description_text);
        methodText = (TextView)findViewById(R.id.method_text);
        priceText = (TextView)findViewById(R.id.price_text);
        timeText = (TextView)findViewById(R.id.time_text);
        resultDescriptionText = (TextView)findViewById(R.id.result_description_text);
        reviewText = (TextView)findViewById(R.id.review_text);
        scoreText = (TextView)findViewById(R.id.score_text);

        //setImage(beforeImage, infoItem.beforeImageFilename);
        //setImage(afterImage, infoItem.afterImageFilename);

        //repairerText.setText(infoItem.repairerSeq);
        brandText.setText(infoItem.brand);
        serviceText.setText(infoItem.service);
        productText.setText(infoItem.product);
        descriptionText.setText(infoItem.description);
        methodText.setText(infoItem.method);
        priceText.setText(infoItem.price);
        timeText.setText(infoItem.resultTime);
        resultDescriptionText.setText(infoItem.resultDescription);
        reviewText.setText(infoItem.review);
        String score = Float.toString(infoItem.score);
        scoreText.setText(score);
    }

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(RemoteService.SAMPLE_URL + fileName).into(imageView);
        }
    }


}
